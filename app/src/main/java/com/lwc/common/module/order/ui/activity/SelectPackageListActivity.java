package com.lwc.common.module.order.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Coupon;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.module.bean.SortByValid;
import com.lwc.common.module.common_adapter.CouponListAdapter;
import com.lwc.common.module.common_adapter.MyPackageListAdapter;
import com.lwc.common.module.common_adapter.SelectCouponListAdapter;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.ToastUtil;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 选择套餐或优惠券列表
 * 
 * @author 何栋
 * @date 2018年8月21日
 */
public class SelectPackageListActivity extends BaseActivity {

	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;
	@BindView(R.id.mBGARefreshLayout)
	BGARefreshLayout mBGARefreshLayout;
	@BindView(R.id.tv_msg)
	TextView tv_msg;
	@BindView(R.id.but_no)
	Button but_no;
	//套餐列表
	private ArrayList<PackageBean> myMeals = new ArrayList<>();
	//优惠券列表
	private ArrayList<Coupon> couponList = new ArrayList<>();
	private MyPackageListAdapter adapter;//套餐Adapter
	private CouponListAdapter adapter2;//优惠券Adapter
	private int type = 0;//0 选择优惠券列表，1选择套餐列表
	private String orderId, couponId, packageId;
	private int refused;

	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_package_list;
	}

	@Override
	protected void findViews() {
		ButterKnife.bind(this);
		BGARefreshLayoutUtils.initRefreshLayout(this, mBGARefreshLayout);
		but_no.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent bundle2 = new Intent();
				bundle2.putExtra("package",new PackageBean(true));
				bundle2.putExtra("coupon",new Coupon(true));
				setResult(RESULT_OK, bundle2);
				onBackPressed();
			}
		});
	}

	public void onBack(View v){
		onBackPressed();
	}

	@Override
	protected void init() {
	}

	@Override
	protected void initGetData() {
		type = getIntent().getIntExtra("type", 0);
		orderId = getIntent().getStringExtra("orderId");
		refused = getIntent().getIntExtra("refused", 0);
		couponId = getIntent().getStringExtra("couponId");
		packageId = getIntent().getStringExtra("packageId");
	}

	@Override
	protected void widgetListener() {
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		if (type == 1) {
			setTitle("选择套餐");
			but_no.setText("不使用套餐");
			getselectMealList();
			tv_msg.setBackgroundResource(R.drawable.no_pachage);
			adapter = new MyPackageListAdapter(this, myMeals, R.layout.item_my_package,4);
			adapter.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(View itemView, int viewType, int position) {
					PackageBean pack = adapter.getItem(position);
					if (!TextUtils.isEmpty(pack.getErrorMsg())) {
						ToastUtil.showToast(SelectPackageListActivity.this, pack.getErrorMsg());
						return;
					}
					Intent bundle2 = new Intent();
					bundle2.putExtra("package", pack);
					setResult(RESULT_OK, bundle2);
					onBackPressed();
				}
			});
			recyclerView.setAdapter(adapter);
		} else {
			setTitle("选择优惠券");
			but_no.setText("不使用优惠券");
			getCoupoon();
			tv_msg.setBackgroundResource(R.drawable.zwyhj);
			adapter2 = new CouponListAdapter(this, couponList, R.layout.item_select_coupon);
			recyclerView.setAdapter(adapter2);
			adapter2.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(View itemView, int viewType, int position) {
					Coupon cou = couponList.get(position);
					Intent bundle2 = new Intent();
					bundle2.putExtra("coupon", cou);
					setResult(RESULT_OK, bundle2);
					onBackPressed();
				}
			});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 用户选择套餐列表
	 */
	private void getselectMealList() {
		HashMap<String, String> map = new HashMap<>();
		map.put("orderId", orderId);
		if (refused == 1) {
			map.put("operate", "0");
		} else {
			map.put("operate", "1");
		}
		if (!TextUtils.isEmpty(couponId)) {
			map.put("couponId", couponId);
		}
		HttpRequestUtils.httpRequest(this, "getselectMealList", RequestValue.GET_USER_SELECT_PACKAGE_LIST, map, "GET", new HttpRequestUtils.ResponseListener() {
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

					Collections.sort(myMeals,new SortByValid());
					adapter.replaceAll(myMeals);
					if (myMeals.size() > 0) {
						but_no.setVisibility(View.VISIBLE);
						tv_msg.setVisibility(View.GONE);
					} else {
						tv_msg.setVisibility(View.VISIBLE);
					}
				} else {
					ToastUtil.showToast(SelectPackageListActivity.this, common.getInfo());
				}
			}

			@Override
			public void returnException(Exception e, String msg) {
				ToastUtil.showToast(SelectPackageListActivity.this, msg);
			}
		});
	}

	/**
	 * 查询优惠券列表
	 */
	private void getCoupoon() {
		HashMap<String, String> map = new HashMap<>();
		map.put("orderId", orderId);
		if (refused == 1) {
			map.put("operate", "0");
		} else {
			map.put("operate", "1");
		}
		if (!TextUtils.isEmpty(packageId)) {
			map.put("packageId", packageId);
		}
		HttpRequestUtils.httpRequest(this, "getCoupoon", RequestValue.GET_USER_COUPON_LIST, map, "GET", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String result) {
				Common common = JsonUtil.parserGsonToObject(result, Common.class);
				if (common.getStatus().equals("1")) {
					ArrayList<Coupon> current = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(result, "data"), new TypeToken<ArrayList<Coupon>>() {
					});
					if (current != null && current.size() > 0) {
						couponList = current;
					} else {
						couponList = new ArrayList<>();
					}
					adapter2.replaceAll(couponList);
					adapter2.notifyDataSetChanged();
					if (couponList.size() > 0) {
						tv_msg.setVisibility(View.GONE);
						but_no.setVisibility(View.VISIBLE);
					} else {
						tv_msg.setVisibility(View.VISIBLE);
					}
				} else {
					ToastUtil.showToast(SelectPackageListActivity.this, common.getInfo());
				}
			}

			@Override
			public void returnException(Exception e, String msg) {
				ToastUtil.showToast(SelectPackageListActivity.this, msg);
			}
		});
	}
}
