package com.lwc.common.module.invoice;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.InvoiceOrder;
import com.lwc.common.module.common_adapter.InvoiceOrderListAdapter;
import com.lwc.common.module.order.ui.activity.OrderDetailActivity;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.ToastUtil;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 发票所含订单列表
 * 
 * @author 何栋
 * @date 2018年1月05日
 */
public class InvoiceOrderActivity extends BaseActivity {

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;
	@BindView(R.id.mBGARefreshLayout)
	BGARefreshLayout mBGARefreshLayout;
	private List<InvoiceOrder> myOrders = new ArrayList<>();
	private InvoiceOrderListAdapter adapter;
	@BindView(R.id.rl_bottom)
	RelativeLayout rl_bottom;
	@BindView(R.id.tv_msg)
	TextView tv_msg;
	private String invoiceHistoryIds;
	private int page=1;

	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_invoice_history;
	}

	@Override
	protected void findViews() {
		ButterKnife.bind(this);
		BGARefreshLayoutUtils.initRefreshLayout(this, mBGARefreshLayout);
		setTitle("所含订单");
		rl_bottom.setVisibility(View.GONE);
		tv_msg.setVisibility(View.GONE);
	}

	@Override
	protected void init() {
	}

	@Override
	public void onResume() {
		super.onResume();
		getOrderList();  //请求网络数据
	}

	private void getOrderList() {
		HashMap<String, String> params = new HashMap<>();
		params.put("curPage", page+"");
		params.put("ids", invoiceHistoryIds);

		HttpRequestUtils.httpRequest(this, "InvoiceOrderList", RequestValue.GET_INVOICE_ORDER_LIST, params, "GET", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String result) {
				Common common = JsonUtil.parserGsonToObject(result, Common.class);
				if (common.getStatus().equals("1")) {
					ArrayList<InvoiceOrder> current = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(result, "data"), new TypeToken<ArrayList<InvoiceOrder>>() {
					});
					if (page == 1) {
						if (current != null && current.size() > 0){
							myOrders = current;
						} else {
							myOrders = new ArrayList<>();
						}
					} else if (page > 1) {
						if (current!= null && current.size() > 0) {
							myOrders.addAll(current);
						} else {
							page--;
						}
					}
					adapter.replaceAll(myOrders);
				} else {
					ToastUtil.showToast(InvoiceOrderActivity.this, common.getInfo());
				}
				BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
				BGARefreshLayoutUtils.endLoadingMore(mBGARefreshLayout);
			}

			@Override
			public void returnException(Exception e, String msg) {
				ToastUtil.showToast(InvoiceOrderActivity.this, msg);
				BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
				BGARefreshLayoutUtils.endLoadingMore(mBGARefreshLayout);
			}
		});
	}

	@Override
	protected void initGetData() {
		invoiceHistoryIds = getIntent().getStringExtra("invoiceHistoryIds");
	}

	@Override
	protected void widgetListener() {
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
//		recyclerView.setLayoutManager(new VegaLayoutManager(this));
		adapter = new InvoiceOrderListAdapter(this, myOrders, R.layout.item_invoice_order);
		adapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int viewType, int position) {
					Bundle bundle = new Bundle();
					bundle.putString("orderId", myOrders.get(position).getOrderId());
					IntentUtil.gotoActivity(InvoiceOrderActivity.this, OrderDetailActivity.class,bundle);

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
				page++;
				getOrderList();
				return true;
			}
		});
	}
}
