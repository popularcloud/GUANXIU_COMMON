package com.lwc.common.module.invoice;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.view.MatrixImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 开票记录详情
 * 
 * @author 何栋
 * @date 2018年1月05日
 */
public class FindInvoiceActivity extends BaseActivity {


	@BindView(R.id.miv)
	MatrixImageView miv;
	private String url;


	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_invoice_find;
	}

	@Override
	protected void findViews() {
		ButterKnife.bind(this);
		setTitle("电子发票");
	}

	@Override
	protected void init() {
	}

	@Override
	protected void initGetData() {
		url = getIntent().getStringExtra("url");
		Glide.with(this).load(url).error(R.drawable.loading_bg).fitCenter().priority( Priority.HIGH)
				.into(miv);
	}

	@Override
	protected void widgetListener() {
	}
}
