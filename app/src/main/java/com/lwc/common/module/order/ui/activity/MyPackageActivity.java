package com.lwc.common.module.order.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.module.common_adapter.FragmentsPagerAdapter;
import com.lwc.common.module.order.ui.fragment.ExpPackageFragment;
import com.lwc.common.module.order.ui.fragment.UsePackageFragment;
import com.lwc.common.module.order.ui.fragment.UseRecordPackageFragment;
import com.lwc.common.view.MyTextView;
import com.lwc.common.widget.CustomViewPager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的套餐
 * 
 * @author 何栋
 * @date 2017年12月01日
 */
public class MyPackageActivity extends com.lwc.common.module.BaseFragmentActivity {

	@BindView(R.id.txtActionbarTitle)
	MyTextView txtActionbarTitle;
	@BindView(R.id.img_back)
	ImageView imgBack;
	@BindView(R.id.rBtnUnderway)
	RadioButton rBtnUnderway;
	@BindView(R.id.rBtnExpires)
	RadioButton rBtnExpires;
	@BindView(R.id.rBtnUseRecord)
	RadioButton rBtnUseRecord;
	@BindView(R.id.viewLine1)
	View viewLine1;
	@BindView(R.id.viewLine2)
	View viewLine2;
	@BindView(R.id.viewLine3)
	View viewLine3;
	@BindView(R.id.cViewPager)
	CustomViewPager cViewPager;
	private HashMap rButtonHashMap;
	private HashMap<Integer, Fragment> fragmentHashMap;
	private UsePackageFragment unusedFragment;
	private ExpPackageFragment expiresFragment;
	private UseRecordPackageFragment useRecordPackageFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_package_list);
		ButterKnife.bind(this);
		txtActionbarTitle.setText("我的套餐");
		imgBack.setVisibility(View.VISIBLE);
		addFragmenInList();
		addRadioButtonIdInList();
		bindViewPage(fragmentHashMap);
		cViewPager.setCurrentItem(0, false);
		initStatusBar();
	}

	protected void initStatusBar(){
		ImmersionBar.with(this)
				.statusBarColor(R.color.white)
				.statusBarDarkFont(true)
				.navigationBarColor(R.color.white).init();
	}

	/**
	 * 往fragmentHashMap中添加 Fragment
	 */
	private void addFragmenInList() {
		fragmentHashMap = new HashMap<>();
		unusedFragment = new UsePackageFragment();
		useRecordPackageFragment = new UseRecordPackageFragment();
		expiresFragment = new ExpPackageFragment();

		fragmentHashMap.put(0, unusedFragment);
		fragmentHashMap.put(1, useRecordPackageFragment);
		fragmentHashMap.put(2, expiresFragment);
	}

	/**
	 * 往rButtonHashMap中添加 RadioButton Id
	 */
	private void addRadioButtonIdInList() {
		rButtonHashMap = new HashMap<>();
		rButtonHashMap.put(0, rBtnUnderway);
		rButtonHashMap.put(1, rBtnExpires);
		rButtonHashMap.put(2, rBtnUnderway);
	}

	@OnClick({R.id.rBtnUnderway, R.id.rBtnExpires,R.id.rBtnUseRecord, R.id.img_back})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.rBtnUnderway:
				showLineView(1);
				cViewPager.setCurrentItem(0);
				rBtnUnderway.setChecked(true);
				break;
			case R.id.rBtnUseRecord:
				showLineView(2);
				cViewPager.setCurrentItem(1);
				rBtnUseRecord.setChecked(true);
				break;
			case R.id.rBtnExpires:
				showLineView(3);
				cViewPager.setCurrentItem(2);
				rBtnExpires.setChecked(true);
				break;
			case R.id.img_back:
				onBackPressed();
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
				viewLine2.setVisibility(View.INVISIBLE);
				viewLine3.setVisibility(View.INVISIBLE);
				break;
			case 2:
				viewLine2.setVisibility(View.VISIBLE);
				viewLine1.setVisibility(View.INVISIBLE);
				viewLine3.setVisibility(View.INVISIBLE);
				break;
			case 3:
				viewLine3.setVisibility(View.VISIBLE);
				viewLine1.setVisibility(View.INVISIBLE);
				viewLine2.setVisibility(View.INVISIBLE);
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
