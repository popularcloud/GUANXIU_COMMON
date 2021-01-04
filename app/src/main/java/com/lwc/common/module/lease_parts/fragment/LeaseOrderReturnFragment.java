package com.lwc.common.module.lease_parts.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.lease_parts.activity.LeaseOrderRefundDetailActivity;
import com.lwc.common.module.lease_parts.adapter.LeaseOrderReturnListAdapter;
import com.lwc.common.module.lease_parts.bean.LeaseReturnDetailBean;
import com.lwc.common.module.lease_parts.inteface_callback.OnOrderBtnClick;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.widget.CancelOrderDialog;
import com.lwc.common.widget.GetPhoneDialog;
import com.lwc.common.widget.PayTypeDialog;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

public class LeaseOrderReturnFragment extends BaseFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    @BindView(R.id.ll_null)
    LinearLayout ll_null;
    private LeaseOrderReturnListAdapter adapter;
    private List<LeaseReturnDetailBean> myOrders = new ArrayList<>();;
    //加载的page页
    private int page = 1;

    private int pageType = 0;

    private List<String> reasons = new ArrayList<>();
    private CancelOrderDialog cancelOrderDialog;

    private PayTypeDialog payTypeDialog;
    private String orderId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lease_order, null);
        ButterKnife.bind(this, view);
        BGARefreshLayoutUtils.initRefreshLayout(getContext(), mBGARefreshLayout);

        pageType = getArguments().getInt("pageType");
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEngines(view);
        init();
        setListener();
        bindRecycleView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBGARefreshLayout.beginRefreshing();
    }

    private void bindRecycleView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LeaseOrderReturnListAdapter(getContext(), myOrders, R.layout.item_lease_order, pageType, new OnOrderBtnClick() {
            @Override
            public void onItemClick(String btnText, int position,final Object o) {
                switch (btnText){
                    case "联系客服":
                        GetPhoneDialog picturePopupWindow = new GetPhoneDialog(getContext(), new GetPhoneDialog.CallBack() {
                            @Override
                            public void twoClick() {
                                Utils.lxkf(MainActivity.activity, "400-881-0769");
                            }
                            @Override
                            public void cancelCallback() {
                            }
                        }, "", "呼叫 400-881-0769");
                        picturePopupWindow.show();
                        break;
                     case "撤销申请":
                         retrunApply(String.valueOf(o));
                        break;
                     case "删除订单":
                         delOrder(String.valueOf(o));
                        break;
                }
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putString("branch_id",adapter.getItem(position).getBranchId());
                    bundle.putString("pageType",String.valueOf(pageType));
                    IntentUtil.gotoActivity(getContext(), LeaseOrderRefundDetailActivity.class,bundle);
            }
        });
        recyclerView.setAdapter(adapter);

       // mBGARefreshLayout.beginRefreshing();
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
            //刷新控件监听器
            mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
                @Override
                public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                    page = 1;
                    getDateList();
                }

                @Override
                public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                    page++;
                    getDateList();
                    return true;
                }
            });
    }

    public void getReturnTypeByOrderId(String orderId){
        this.orderId = orderId;
        page = 1;
       // getDateList();
    }

    /**
     * 获取我的订单
     */
    private void getDateList(){

        Map<String,String> params = new HashMap<>();
        params.put("curPage",String.valueOf(page));
        params.put("status_id",String.valueOf(pageType));
        if(!TextUtils.isEmpty(orderId)){
            params.put("order_id",orderId);
        }

        HttpRequestUtils.httpRequest(getActivity(), "获取我的订单+"+pageType, RequestValue.LEASEMANAGE_QUERYLEASEBRANCHORDERS, params, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                   // ToastUtil.showToast(getActivity(),"获取我的订单成功!");
                    //if(!TextUtils.isEmpty(orderId)){
                    orderId = "";
                  //  }
                    myOrders = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response,"data"),new TypeToken<ArrayList<LeaseReturnDetailBean>>(){});
                    if(page == 1){
                        adapter.replaceAll(myOrders);

                        if(myOrders != null && myOrders.size() > 0){
                            ll_null.setVisibility(View.GONE);
                            mBGARefreshLayout.setVisibility(View.VISIBLE);
                        }else{
                            ll_null.setVisibility(View.VISIBLE);
                            mBGARefreshLayout.setVisibility(View.GONE);
                        }

                    }else{
                        adapter.addAll(myOrders);
                    }
                }else{
                    ToastUtil.showToast(getActivity(),common.getInfo());
                }

                if(page == 1){
                    mBGARefreshLayout.endRefreshing();
                }else{
                    mBGARefreshLayout.endLoadingMore();
                }

            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                if(page == 1){
                    mBGARefreshLayout.endRefreshing();
                }else{
                    mBGARefreshLayout.endLoadingMore();
                }
            }
        });
    }

    /**
     * 撤销申请
     * @param orderId
     */
    private void retrunApply(String orderId){
        Map<String,String> params = new HashMap<>();
        params.put("branch_id",orderId);
        HttpRequestUtils.httpRequest(getActivity(), "撤销申请",RequestValue.LEASEMANAGE_UODOLEASEBRANCHORDER, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    ToastUtil.showToast(getContext(),common.getInfo());
                    mBGARefreshLayout.beginRefreshing();
                }else{
                    ToastUtil.showToast(getContext(),common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }

    private void delOrder(String orderId){
        Map<String,String> params = new HashMap<>();
        params.put("branch_id",orderId);
        HttpRequestUtils.httpRequest(getActivity(), "删除订单",RequestValue.LEASEMANAGE_DELETELEASEBRANCHORDER, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    ToastUtil.showToast(getContext(),common.getInfo());
                    mBGARefreshLayout.beginRefreshing();
                }else{
                    ToastUtil.showToast(getContext(),common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }


}
