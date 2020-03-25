package com.lwc.common.activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.configs.BroadcastFilters;
import com.lwc.common.module.wallet.ui.WalletActivity;
import com.lwc.common.utils.Constants;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.view.MyTextView;
import com.yanzhenjie.sofia.Sofia;

import butterknife.ButterKnife;

/**
 * 基类Activity
 * 
 * @Description 所有的Activity都继承自此Activity，并实现基类模板方法，本类的目的是为了规范团队开发项目时候的开发流程的命名， 基类也用来处理需要集中分发处理的事件、广播、动画等，如开发过程中有发现任何改进方案都可以一起沟通改进。
 * @author 何栋
 * @version 1.0
 * @date 2014年3月29日
 * @Copyright: Copyright (c) 2014 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 * 
 */
public abstract class BaseActivity extends FragmentActivity {

	/** 广播接收器 */
	public BroadcastReceiver receiver;
	/** 广播过滤器 */
	public IntentFilter filter;
	public Bundle savedInstanceState;
	private MyTextView txtActionbarTitle;
	private TextView tvqd;
	private ImageView imgRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		int layoutId = getContentViewId(savedInstanceState);

		setContentView(layoutId);
		ButterKnife.bind(this);
		//初始化状态栏
		initStatusBar();
		try {
			txtActionbarTitle = (MyTextView) findViewById(R.id.txtActionbarTitle);
			tvqd = (TextView) findViewById(R.id.tvQd);
			imgRight = (ImageView) findViewById(R.id.imgRight);
		} catch (Exception e){}
		findViews();
		initGetData();
		widgetListener();
		init();
		registerReceiver();
	}

	/**
	 * 子类可重写改方法定制状态栏样式
	 */
	protected void initStatusBar(){
/*		Sofia.with(this)
				.statusBarBackground(Color.parseColor("#ffffff"))
				.statusBarDarkFont()
				.navigationBarBackground(R.color.white)
				.navigationBarBackgroundAlpha(0);*/
		ImmersionBar.with(this)
				.statusBarColor(R.color.white)
				.statusBarDarkFont(true)
				.navigationBarColor(R.color.white).init();
	}

	protected void setTitle(String title) {
		if (txtActionbarTitle != null && title != null) {
			txtActionbarTitle.setText(title);
		}
	}

	protected void setTitle(String title,int textColor) {
		if (txtActionbarTitle != null && title != null) {
			txtActionbarTitle.setText(title);
			txtActionbarTitle.setTextColor(textColor);
		}
	}

	protected void setRight(String right, View.OnClickListener listener) {
		if (tvqd != null && right != null) {
			tvqd.setVisibility(View.VISIBLE);
			tvqd.setText(right);
			tvqd.setOnClickListener(listener);
		}
	}
	protected void setRight(String right,String color ,View.OnClickListener listener) {
		if (tvqd != null && right != null) {
			tvqd.setVisibility(View.VISIBLE);
			tvqd.setText(right);
			tvqd.setTextColor(Color.parseColor(color));
			tvqd.setOnClickListener(listener);
		}
	}
	protected void setRight(String right) {
		if (tvqd != null && right != null) {
			tvqd.setText(right);
		}
	}
	protected void setRight(int right, View.OnClickListener listener) {
		if (imgRight != null && right != 0) {
			imgRight.setVisibility(View.VISIBLE);
			imgRight.setImageResource(right);
			imgRight.setOnClickListener(listener);
		}
	}

	protected void goneRight() {
		imgRight.setVisibility(View.GONE);
	}
	protected void visibleRight() {
		imgRight.setVisibility(View.VISIBLE);
	}

	public void onBack(View view) {
		onBackPressed();
	}

	/**
	 * 获取显示view的xml文件ID
	 * 
	 * 在Activity的 {@link #onCreate(Bundle)}里边被调用
	 * 
	 * @version 1.0
	 * @param savedInstanceState
	 * @createTime 2014年4月21日,下午2:31:19
	 * @updateTime 2014年4月21日,下午2:31:19
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @return xml文件ID
	 */
	protected abstract int getContentViewId(Bundle savedInstanceState);

	/**
	 * 控件查找
	 * 
	 * @version 1.0
	 * @createTime 2014年4月21日,下午1:58:20
	 * @updateTime 2014年4月21日,下午1:58:20
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	protected abstract void findViews();

	/**
	 * 初始化应用程序，设置一些初始化数据都获取数据等操作
	 * 
	 * 在{@link #widgetListener()}之后被调用
	 * 
	 * @version 1.0
	 * @createTime 2014年4月21日,下午1:55:15
	 * @updateTime 2014年4月21日,下午1:55:15
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	protected abstract void init();

	/**
	 * 获取上一个界面传送过来的数据
	 * 
	 * 在{@link BaseActivity#init()}之前被调用
	 * 
	 * @version 1.0
	 * @createTime 2014年4月21日,下午2:42:56
	 * @updateTime 2014年4月21日,下午2:42:56
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	protected abstract void initGetData();

	/**
	 * 组件监听模块
	 * 
	 * 在{@link #findViews()}后被调用
	 * 
	 * @version 1.0
	 * @createTime 2014年4月21日,下午1:56:06
	 * @updateTime 2014年4月21日,下午1:56:06
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	protected abstract void widgetListener();

	/**
	 * 设置广播过滤器
	 * 
	 * @version 1.0
	 * @createTime 2014年5月22日,下午1:43:15
	 * @updateTime 2014年5月22日,下午1:43:15
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	protected void setFilterActions() {
		filter = new IntentFilter();
		filter.addAction(BroadcastFilters.ADD_MACHINE_SUCCESS);
		filter.addAction(BroadcastFilters.UPDATE_USER_INFO_ICON);
		filter.addAction(BroadcastFilters.UPDATE_PASSWORD);
		filter.addAction(BroadcastFilters.NOTIFI_DATA_MACHINE_LIST);
		filter.addAction(BroadcastFilters.SHOW_MACHINE_INFO);
		filter.addAction(BroadcastFilters.NOTIFI_MAIN_ORDER_MENTION);
		filter.addAction(BroadcastFilters.NOTIFI_NEARBY_PEOPLE);
		filter.addAction(BroadcastFilters.NOTIFI_MESSAGE_COUNT);
		filter.addAction(BroadcastFilters.NOTIFI_WAITTING_ORDERS);
		filter.addAction(BroadcastFilters.NOTIFI_ORDER_INFO_MENTION);
		filter.addAction(BroadcastFilters.NOTIFI_ORDER_INFO_GUAQI);
		filter.addAction(BroadcastFilters.NOTIFI_ORDER_INFO_TANCHUAN);
		filter.addAction(BroadcastFilters.NOTIFI_ORDER_GETTED_MENTION);
		filter.addAction(BroadcastFilters.NOTIFI_ORDER_PRIASE_MENTION);
		filter.addAction(BroadcastFilters.UPDATE_USER_LOGIN_SUCCESSED);
		filter.addAction(BroadcastFilters.NOTIFI_BUTTON_STATUS);
		filter.addAction(BroadcastFilters.LOGIN_OUT_ACTION);
		filter.addAction(BroadcastFilters.NOTIFI_NEAR_ORDER);
		filter.addAction(BroadcastFilters.NOTIFI_ORDER_INFO_CHANGE);
		filter.addAction(BroadcastFilters.NOTIFI_CLOSE_SLIDING_MENU);

	}

	/**
	 * 注册广播
	 * 
	 * @version 1.0
	 * @createTime 2014年5月22日,下午1:41:25
	 * @updateTime 2014年5月22日,下午1:41:25
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	protected void registerReceiver() {
		setFilterActions();
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				BaseActivity.this.onReceive(context, intent);
			}
		};
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Constants.RED_ID_TO_RESULT) {
			IntentUtil.gotoActivityAndFinish(this, WalletActivity.class);
			finish();
		} else if (resultCode == Constants.RED_ID_RESULT){
			finish();
		}
	}

	/**
	 * 广播监听回调
	 * 
	 * @version 1.0
	 * @createTime 2014年5月22日,下午1:40:30
	 * @updateTime 2014年5月22日,下午1:40:30
	 * @createAuthor CodeApe
	 * @updateAuthor CodeApe
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param context
	 *        上下文
	 * @param intent
	 *        广播附带内容
	 */
	protected void onReceive(Context context, Intent intent) {
		// TODO 父类集中处理共同的请求
	}

	@Override
	protected void onDestroy() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		super.onDestroy();
	}

}
