package com.lwc.common.module.mine;

import android.os.Bundle;
import android.view.View;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;

/**
 * 账户界面
 * 
 * @Description TODO
 * @author 何栋
 * @date 2016年4月24日
 * @Copyright: lwc
 */
public class AccountActivity extends BaseActivity {

	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_account;
	}

	@Override
	protected void findViews() {

		setTitle("账户");

		setRight("明细", new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	public void clickPay(View v) {

	}

	public void clickWithdraw(View v) {

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

}
