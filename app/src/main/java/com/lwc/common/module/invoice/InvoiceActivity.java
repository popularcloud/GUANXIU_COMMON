package com.lwc.common.module.invoice;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.InvoiceOrder;
import com.lwc.common.module.common_adapter.FragmentsPagerAdapter;
import com.lwc.common.module.common_adapter.InvoiceOrderListAdapter;
import com.lwc.common.module.order.ui.fragment.FinishFragment;
import com.lwc.common.module.order.ui.fragment.ProceedFragment;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.MyTextView;
import com.lwc.common.widget.CustomViewPager;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 发票与报销
 * 
 * @author 何栋
 * @date 2018年1月05日
 */
public class InvoiceActivity extends BaseActivity {
	@BindView(R.id.txtActionbarTitle)
	MyTextView txtActionbarTitle;
	@BindView(R.id.img_back)
	ImageView imgBack;
	@BindView(R.id.rBtnUnderway)
	RadioButton rBtnUnderway;
	@BindView(R.id.rBtnFinish)
	RadioButton rBtnFinish;
	@BindView(R.id.viewLine1)
	View viewLine1;
	@BindView(R.id.viewLine3)
	View viewLine3;
	@BindView(R.id.cViewPager)
	CustomViewPager cViewPager;

	private HashMap rButtonHashMap;
	private HashMap<Integer, Fragment> fragmentHashMap;
	private OrderInvoiceFragment orderInvoiceFragment;
	private PackageInvoiceFragment packageInvoiceFragment;

	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_invoice_order_list;
	}

	@Override
	protected void findViews() {
		setTitle("订单开票");
		setRight("发票记录", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				IntentUtil.gotoActivity(InvoiceActivity.this, InvoiceHistoryActivity.class);
			}
		});
		addFragmenInList();
		addRadioButtonIdInList();
		init();
		bindViewPage(fragmentHashMap);
	}

	/**
	 * 往fragmentHashMap中添加 Fragment
	 */
	private void addFragmenInList() {

		fragmentHashMap = new HashMap<>();
		orderInvoiceFragment = new OrderInvoiceFragment();
		packageInvoiceFragment = new PackageInvoiceFragment();
		fragmentHashMap.put(0, orderInvoiceFragment);
		fragmentHashMap.put(1, packageInvoiceFragment);
	}
	@Override
	protected void init() {
	}

	@Override
	protected void initGetData() {

	}

	@Override
	protected void widgetListener() {

	}

	/**
	 * 往rButtonHashMap中添加 RadioButton Id
	 */
	private void addRadioButtonIdInList() {

		rButtonHashMap = new HashMap<>();
		rButtonHashMap.put(0, rBtnUnderway);
		rButtonHashMap.put(1, rBtnFinish);
	}

	@OnClick({R.id.rBtnUnderway, R.id.rBtnFinish})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.rBtnUnderway:
				showLineView(1);
				cViewPager.setCurrentItem(0);
				break;
			case R.id.rBtnFinish:
				showLineView(2);
				cViewPager.setCurrentItem(1);
				break;
		}
	}


	/**
	 * 显示RadioButton线条
	 *
	 * @param num 1 ： 显示已发布下的线条  2 ： 显示未完成下的线条  3： 显示已完成下的线条  。未选中的线条将会被隐藏
	 */
	private void showLineView(int num) {
		switch (num) {
			case 1:
				viewLine1.setVisibility(View.VISIBLE);
				viewLine3.setVisibility(View.INVISIBLE);
				break;
			case 2:
				viewLine3.setVisibility(View.VISIBLE);
				viewLine1.setVisibility(View.INVISIBLE);
				break;
		}
	}

	private void bindViewPage(HashMap<Integer, Fragment> fragmentList) {
		//是否滑动
		cViewPager.setPagingEnabled(true);
		cViewPager.setAdapter(new FragmentsPagerAdapter(getSupportFragmentManager(), fragmentList));
		cViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				cViewPager.setChecked(rButtonHashMap, position);
				showLineView(position + 1);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}
}
