package com.lwc.common.module.lease_parts.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxz.PagerSlidingTabStrip;
import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.module.bean.MyMsg;
import com.lwc.common.module.common_adapter.LeasePagerAdapter;
import com.lwc.common.module.lease_parts.bean.RecommendItemBean;
import com.lwc.common.module.lease_parts.fragment.LeaseOrderFragment;
import com.lwc.common.module.lease_parts.fragment.LeaseOrderReturnFragment;
import com.lwc.common.module.message.ui.MsgListActivity;
import com.lwc.common.module.order.ui.fragment.FinishFragment;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.MsgReadUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的租赁订单
 */
public class MyLeaseOrderListActivity extends com.lwc.common.module.BaseFragmentActivity {

	@BindView(R.id.img_back)
	ImageView imgBack;
	@BindView(R.id.iv_right)
	ImageView iv_right;
	@BindView(R.id.tv_msg)
	TextView tv_msg;
	@BindView(R.id.et_search)
	TextView et_search;
	@BindView(R.id.cViewPager)
	CustomViewPager cViewPager;

	@BindView(R.id.tabs)
	PagerSlidingTabStrip tabs;

	private HashMap<Integer, Fragment> fragmentHashMap;
	private FinishFragment finishFragment;
	private ArrayList<RecommendItemBean> arrayList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_lease_order_list);
		ButterKnife.bind(this);
		imgBack.setVisibility(View.VISIBLE);
		iv_right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyMsg msg = new MyMsg();
				msg.setMessageType("5");
				msg.setMessageTitle("租赁消息");
				Bundle bundle = new Bundle();
				bundle.putSerializable("myMsg", msg);
				IntentUtil.gotoActivity(MyLeaseOrderListActivity.this, MsgListActivity.class,bundle);
			}
		});

		ImmersionBar.with(this)
				.statusBarColor(R.color.white)
				.statusBarDarkFont(true).init();

		int leaseOrderCount = (int) SharedPreferencesUtils.getParam(this,"leaseOrderCount",0);
		if(leaseOrderCount > 0){
			tv_msg.setVisibility(View.VISIBLE);
			tv_msg.setText(String.valueOf(leaseOrderCount));
		}else{
			tv_msg.setVisibility(View.GONE);
		}

		addFragmenInList();
		bindViewPage();

        int pageType = getIntent().getIntExtra("pageType",0);
		//退租订单
		String orderId = getIntent().getStringExtra("orderId");
		if(!TextUtils.isEmpty(orderId)){
			showReturnOrder(pageType,orderId);
		}
        cViewPager.setCurrentItem(pageType, false);

		et_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				IntentUtil.gotoActivity(MyLeaseOrderListActivity.this, LeaseOrderSearchActivity.class);
			}
		});

		et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					IntentUtil.gotoActivity(MyLeaseOrderListActivity.this, LeaseOrderSearchActivity.class);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		//获取未读租赁消息
		MsgReadUtil.hasMessage(MyLeaseOrderListActivity.this,tv_msg);
	}

	/**
	 * 往fragmentHashMap中添加 Fragment
	 */
	private void addFragmenInList() {


		fragmentHashMap = new HashMap<>();

		 LeaseOrderFragment leaseOrderFragment0 = new LeaseOrderFragment();
		Bundle bundle0 =new Bundle();
		 bundle0.putInt("pageType",0);
		 leaseOrderFragment0.setArguments(bundle0);
		fragmentHashMap.put(0,leaseOrderFragment0);

		LeaseOrderFragment leaseOrderFragment1 = new LeaseOrderFragment();

		Bundle bundle1 =new Bundle();
		bundle1.putInt("pageType",1);
		leaseOrderFragment1.setArguments(bundle1);
		fragmentHashMap.put(1,leaseOrderFragment1);

		LeaseOrderFragment leaseOrderFragment2 = new LeaseOrderFragment();
		Bundle bundle2 =new Bundle();
		bundle2.putInt("pageType",2);
		leaseOrderFragment2.setArguments(bundle2);
		fragmentHashMap.put(2,leaseOrderFragment2);

		LeaseOrderFragment leaseOrderFragment3 = new LeaseOrderFragment();
		Bundle bundle3 =new Bundle();
		bundle3.putInt("pageType",3);
		leaseOrderFragment3.setArguments(bundle3);
		fragmentHashMap.put(3,leaseOrderFragment3);

		LeaseOrderFragment leaseOrderFragment4 = new LeaseOrderFragment();
		Bundle bundle4 =new Bundle();
		bundle4.putInt("pageType",4);
		leaseOrderFragment4.setArguments(bundle4);
		fragmentHashMap.put(4,leaseOrderFragment4);

		LeaseOrderFragment leaseOrderFragment5 = new LeaseOrderFragment();
		Bundle bundle5 =new Bundle();
		bundle5.putInt("pageType",5);
		leaseOrderFragment5.setArguments(bundle5);
		fragmentHashMap.put(5,leaseOrderFragment5);

		LeaseOrderReturnFragment leaseOrderFragment6 = new LeaseOrderReturnFragment();
		Bundle bundle6 =new Bundle();
		bundle6.putInt("pageType",6);
		leaseOrderFragment6.setArguments(bundle6);
		fragmentHashMap.put(6,leaseOrderFragment6);

		LeaseOrderReturnFragment leaseOrderFragment7 = new LeaseOrderReturnFragment();
		Bundle bundle7 =new Bundle();
		bundle7.putInt("pageType",7);
		leaseOrderFragment7.setArguments(bundle7);
		fragmentHashMap.put(7,leaseOrderFragment7);
	}

	@OnClick({R.id.img_back})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.img_back:
				onBackPressed();
				break;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		String orderId = intent.getStringExtra("orderId");
		int pageType = intent.getIntExtra("pageType",0);

		if(fragmentHashMap != null && !TextUtils.isEmpty(orderId)){
			showReturnOrder(pageType,orderId);
		}


	}

	public void showReturnOrder(int pageType,String orderId){
		if(!TextUtils.isEmpty(orderId)){
			((LeaseOrderReturnFragment)fragmentHashMap.get(pageType)).getReturnTypeByOrderId(orderId);
		}

        cViewPager.setCurrentItem(pageType,false);
	}

	private void bindViewPage() {
		arrayList.add(new RecommendItemBean("全部"));
		arrayList.add(new RecommendItemBean("待付款"));
		arrayList.add(new RecommendItemBean("待发货"));
		arrayList.add(new RecommendItemBean("待收货"));
		arrayList.add(new RecommendItemBean("租用中"));
		arrayList.add(new RecommendItemBean("已逾期"));
		arrayList.add(new RecommendItemBean("退款"));
		arrayList.add(new RecommendItemBean("退租"));

		//是否滑动
		cViewPager.setPagingEnabled(true);
		cViewPager.setAdapter(new LeasePagerAdapter(getSupportFragmentManager(),arrayList,fragmentHashMap));
		tabs.setViewPager(cViewPager);
		cViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		// 设置Tab底部线的高度,传入的是dp
		tabs.setUnderlineHeight(0);
		// 设置Tab 指示器Indicator的高度,传入的是dp
		tabs.setIndicatorHeight(1);
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(getResources().getColor(R.color.red_money));
		// 设置Tab标题文字的大小,传入的是sp
		tabs.setTextSize(14);
		// 设置选中Tab文字的颜色
		tabs.setSelectedTextColor(getResources().getColor(R.color.black));
		//设置正常Tab文字的颜色
		tabs.setTextColor(getResources().getColor(R.color.color_33));
	}


	@Override
	public void initView() {

	}

	@Override
	public void initEngines() {

	}

	@Override
	public void getIntentData() {

	}
}
