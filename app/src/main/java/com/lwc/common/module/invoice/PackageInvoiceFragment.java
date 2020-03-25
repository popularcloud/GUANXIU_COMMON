package com.lwc.common.module.invoice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.InvoiceOrder;
import com.lwc.common.module.bean.InvoicePackageOrder;
import com.lwc.common.module.common_adapter.InvoiceOrderListAdapter;
import com.lwc.common.module.common_adapter.InvoicePackageListAdapter;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 订单开票
 */
public class PackageInvoiceFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    @BindView(R.id.iv_checked)
    ImageView iv_checked;
    @BindView(R.id.tv_maney)
    TextView tv_maney;
    @BindView(R.id.tv_all_select)
    TextView tv_all_select;
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    private boolean isChecked = false;
    private ArrayList<InvoicePackageOrder> myOrders = new ArrayList<>();
    private List<String> isSelect = new ArrayList<>();
    private InvoicePackageListAdapter adapter;
    private int page=1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_invoice, null);
        ButterKnife.bind(this, view);

        BGARefreshLayoutUtils.initRefreshLayout(getContext(), mBGARefreshLayout);
        String str = "合计: ¥ 0";
        tv_maney.setText(Utils.getSpannableStringBuilder(1, str.length()-1, getResources().getColor(R.color.red_money), str, 16));
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mBGARefreshLayout != null){
            mBGARefreshLayout.beginRefreshing();
        }
    }

    @OnClick({R.id.iv_checked, R.id.tv_all_select, R.id.tv_next})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.iv_checked:
            case R.id.tv_all_select:
                if (myOrders == null || myOrders.size() == 0) {
                    isChecked = false;
                    iv_checked.setImageResource(R.drawable.invoice_order_noselected1);
                    return;
                }
                if (isChecked) {
                    isChecked = false;
                    //tv_all_select.setTextColor(getResources().getColor(R.color.black_66));
                    iv_checked.setImageResource(R.drawable.invoice_order_noselected1);
                } else {
                    isChecked = true;
                    //tv_all_select.setTextColor(getResources().getColor(R.color.blue_00aaf5));
                    iv_checked.setImageResource(R.drawable.invoice_order_selected1);
                }
                updateList();
                break;
            case R.id.tv_next:
                //TODO 要传开票订单号列表和总金额过去
                String allManey = "0";
                String ids = "";
                for (int i=0; i<isSelect.size(); i++) {
                    if (isSelect.get(i).equals("1")) {
                        allManey = Utils.jia(allManey, myOrders.get(i).getOrderAmount());
                        ids = ids+myOrders.get(i).getOrderId()+",";
                    }
                }
                if (allManey.equals("0") || ids.equals("")) {
                    ToastUtil.showToast(getActivity(), "请选择你要开发票的订单");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("invoiceOrderIds", ids.substring(0, ids.length()-1));
                bundle.putString("totalManey", allManey);
                bundle.putString("invoiceOperationType","package");
                IntentUtil.gotoActivity(getActivity(), OpenInvoiceActivity.class, bundle);
                break;
        }
    }


    /**
     *
     * 查询可开发票订单列表
     *
     */
    private void getOrderList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("curPage", page+"");
        HttpRequestUtils.httpRequest(getActivity(), "InvoicePackageList", RequestValue.GET_INVOICE_PACKAGE_LIST, params, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String result) {
                Common common = JsonUtil.parserGsonToObject(result, Common.class);
                if (common.getStatus().equals("1")) {
                    ArrayList<InvoicePackageOrder> current = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(result, "data"), new TypeToken<ArrayList<InvoicePackageOrder>>() {
                    });
                    if (page == 1) {
                        if (current != null && current.size() > 0){
                            myOrders = current;
                            isSelect = new ArrayList<>();
                            for (int i=0; i<myOrders.size(); i++){
                                isSelect.add("0");
                            }
                        } else {
                            myOrders = new ArrayList<>();
                            isSelect = new ArrayList<>();
                        }
                    } else if (page > 1) {
                        if (current!= null && current.size() > 0) {
                            myOrders.addAll(current);
                            for (int i=0; i<current.size(); i++){
                                isSelect.add("0");
                            }
                        } else {
                            page--;
                        }
                    }
                    updateMoney();
//					tv_all_select.setText("全选（"+myOrders.size()+"）");
                    adapter.setDateSelect(isSelect);
                    adapter.replaceAll(myOrders);
                    if (myOrders.size() > 0) {
                        tv_msg.setVisibility(View.GONE);
                    } else {
                        tv_msg.setVisibility(View.VISIBLE);
                    }
                } else {
                    ToastUtil.showToast(getContext(), common.getInfo());
                }
                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
                BGARefreshLayoutUtils.endLoadingMore(mBGARefreshLayout);
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showToast(getContext(), msg);
                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
                BGARefreshLayoutUtils.endLoadingMore(mBGARefreshLayout);
            }
        });
    }

    private void updateList() {
        if (isChecked) {
            for (int i=0; i<myOrders.size(); i++){
                isSelect.set(i, "1");
            }
            updateMoney();
        } else {
            for (int i=0; i<myOrders.size(); i++){
                isSelect.set(i, "0");
            }
            updateMoney();
        }
        adapter.setDateSelect(isSelect);
        adapter.notifyDataSetChanged();
    }

    private void updateMoney() {
        isChecked = true;
        String allManey = "0";
        int count = 0;
        for (int i=0; i<myOrders.size(); i++){
            if (isSelect.get(i).equals("1")) {
                count ++;
                allManey = Utils.jia(allManey, myOrders.get(i).getOrderAmount());
            } else {
                isChecked = false;
            }
        }
        String str = "合计: ¥ "+Utils.getMoney(allManey);
        tv_all_select.setText("全选("+count+")");
        tv_maney.setText(str);
        if (!isChecked) {
            tv_all_select.setTextColor(getResources().getColor(R.color.black_66));
            iv_checked.setImageResource(R.drawable.invoice_order_noselected1);
        } else {
            tv_all_select.setTextColor(getResources().getColor(R.color.btn_blue_nomal));
            iv_checked.setImageResource(R.drawable.invoice_order_selected1);
        }
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void init() {

    }

    @Override
    public void initEngines(View view) {

    }


    @Override
    public void getIntentData() {
    }

    @Override
    public void setListener() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//		recyclerView.setLayoutManager(new VegaLayoutManager(this));
        adapter = new InvoicePackageListAdapter(getContext(), myOrders, R.layout.item_invoice_order);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                if (isSelect.get(position).equals("0")) {
                    isSelect.set(position, "1");
                } else {
                    isSelect.set(position, "0");
                }

                adapter.setDateSelect(isSelect);
                adapter.notifyDataSetChanged();
                updateMoney();
            }
        });
        recyclerView.setAdapter(adapter);

        //刷新控件监听器
        mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                page = 1;
                getOrderList();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
//				page++;
//				getOrderList();
                return false;
            }
        });
        mBGARefreshLayout.beginRefreshing();
    }
}
