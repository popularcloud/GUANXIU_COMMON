package com.lwc.common.module.order.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.module.common_adapter.MyPackageListAdapter;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.ToastUtil;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


/**
 * 套餐类型
 */
public class NewPackageListActivity extends BaseActivity {

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;
	@BindView(R.id.mBGARefreshLayout)
	BGARefreshLayout mBGARefreshLayout;
	@BindView(R.id.tv_msg)
	TextView tv_msg;
	private ArrayList<PackageBean> myMeals = new ArrayList<>();
	private MyPackageListAdapter adapter;

	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_new_package_list;
	}

	@Override
	protected void findViews() {
		setTitle("套餐类型");
		ButterKnife.bind(this);
		BGARefreshLayoutUtils.initRefreshLayout(this, mBGARefreshLayout);
	}

	public void onBack(View v){
		onBackPressed();
	}

	@Override
	protected void init() {
	}

	@Override
	protected void initGetData() {
	}

	@Override
	protected void widgetListener() {
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new MyPackageListAdapter(this, myMeals, R.layout.item_my_package,1);
		adapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(View itemView, int viewType, int position) {
				PackageBean pack = adapter.getItem(position);
				Bundle bundle2 = new Bundle();
				bundle2.putSerializable("package", pack);
				bundle2.putInt("position",position);
				IntentUtil.gotoActivity(NewPackageListActivity.this, PackageDetailActivity.class, bundle2);
			}
		});
		recyclerView.setAdapter(adapter);
		//刷新控件监听器
		mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
			@Override
			public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
				getMealList();
			}

			@Override
			public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
				return false;
			}
		});

		mBGARefreshLayout.beginRefreshing();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//getMealList();
	}

	private void getMealList() {
		HashMap<String, String> map = new HashMap<>();
		map.put("location", "2");
		HttpRequestUtils.httpRequest(this, "getMealList", RequestValue.GET_PACKAGE_LIST, map, "GET", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String result) {
				Common common = JsonUtil.parserGsonToObject(result, Common.class);
				if (common.getStatus().equals("1")) {
					ArrayList<PackageBean> current = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(result, "data"), new TypeToken<ArrayList<PackageBean>>() {
					});
					if (current != null && current.size() > 0) {
						myMeals = current;
					} else {
						myMeals = new ArrayList<>();
					}
					adapter.replaceAll(myMeals);
					if (myMeals.size() > 0) {
						tv_msg.setVisibility(View.GONE);
					} else {
						tv_msg.setVisibility(View.VISIBLE);
					}
				} else {
					ToastUtil.showToast(NewPackageListActivity.this, common.getInfo());
				}
				BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
			}

			@Override
			public void returnException(Exception e, String msg) {
				ToastUtil.showToast(NewPackageListActivity.this, msg);
				BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
			}
		});
	}
}
