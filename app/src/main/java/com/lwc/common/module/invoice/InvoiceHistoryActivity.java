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
import com.lwc.common.module.bean.InvoiceHistory;
import com.lwc.common.module.common_adapter.InvoiceHistoryListAdapter;
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
 * 开票记录
 * 
 * @author 何栋
 * @date 2018年1月05日
 */
public class InvoiceHistoryActivity extends BaseActivity {

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;
	@BindView(R.id.mBGARefreshLayout)
	BGARefreshLayout mBGARefreshLayout;
	@BindView(R.id.rl_bottom)
	RelativeLayout rl_bottom;
	@BindView(R.id.tv_msg)
	TextView tv_msg;
	private List<InvoiceHistory> myOrders = new ArrayList<>();
	private InvoiceHistoryListAdapter adapter;
	private int page=1;

	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_invoice_history;
	}

	@Override
	protected void findViews() {
		ButterKnife.bind(this);
		BGARefreshLayoutUtils.initRefreshLayout(this, mBGARefreshLayout);
		setTitle("发票记录");
		tv_msg.setText("暂无发票记录");
		rl_bottom.setVisibility(View.GONE);
	}

	@Override
	protected void init() {
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mBGARefreshLayout != null)
			mBGARefreshLayout.beginRefreshing();  //请求网络数据
	}

	/**
	 *
	 * 查询可开发票订单列表
	 *
	 */
	private void getOrderList() {

		HashMap<String, String> params = new HashMap<>();
		params.put("curPage", page+"");
		HttpRequestUtils.httpRequest(this, "InvoiceHistoryList", RequestValue.GET_INVOICE_HISTORY_LIST, params, "GET", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String result) {
				Common common = JsonUtil.parserGsonToObject(result, Common.class);
				if (common.getStatus().equals("1")) {
					ArrayList<InvoiceHistory> current = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(result, "data"), new TypeToken<ArrayList<InvoiceHistory>>() {
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
					if (myOrders.size() > 0) {
						tv_msg.setVisibility(View.GONE);
					} else {
						tv_msg.setVisibility(View.VISIBLE);
					}
				} else {
					ToastUtil.showToast(InvoiceHistoryActivity.this, common.getInfo());
				}
				BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
				BGARefreshLayoutUtils.endLoadingMore(mBGARefreshLayout);
			}

			@Override
			public void returnException(Exception e, String msg) {
				ToastUtil.showToast(InvoiceHistoryActivity.this, msg);
				BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
				BGARefreshLayoutUtils.endLoadingMore(mBGARefreshLayout);
			}
		});
	}

	@Override
	protected void initGetData() {
	}

	@Override
	protected void widgetListener() {
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
//		recyclerView.setLayoutManager(new VegaLayoutManager(this));
		adapter = new InvoiceHistoryListAdapter(this, myOrders, R.layout.item_invoice_history);
		adapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int viewType, int position) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("invoiceHistory", adapter.getItem(position));
				IntentUtil.gotoActivity(InvoiceHistoryActivity.this, HistoryDetailActivity.class, bundle);
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
