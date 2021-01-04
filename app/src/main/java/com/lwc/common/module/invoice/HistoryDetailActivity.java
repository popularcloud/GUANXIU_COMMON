package com.lwc.common.module.invoice;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.module.bean.InvoiceHistory;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 开票记录详情
 * 
 * @author 何栋
 * @date 2018年1月05日
 */
public class HistoryDetailActivity extends BaseActivity {
	private InvoiceHistory invoiceHistory;

	@BindView(R.id.rl_name)
	LinearLayout rl_name;
	@BindView(R.id.tv_name)
	TextView tv_name;
	@BindView(R.id.rl_phone)
	LinearLayout rl_phone;
	@BindView(R.id.tv_phone)
	TextView tv_phone;
	@BindView(R.id.rl_address)
	LinearLayout rl_address;
	@BindView(R.id.tv_address)
	TextView tv_address;
	@BindView(R.id.rl_email)
	LinearLayout rl_email;
	@BindView(R.id.tv_email)
	TextView tv_email;
	@BindView(R.id.tv_head)
	TextView tv_head;
	@BindView(R.id.tv_duty)
	TextView tv_duty;
	@BindView(R.id.tv_content)
	TextView tv_content;
	@BindView(R.id.tv_money)
	TextView tv_money;
	@BindView(R.id.tv_apply_time)
	TextView tv_apply_time;
	@BindView(R.id.rl_find_invoice)
	RelativeLayout rl_find_invoice;
	@BindView(R.id.rl_duty)
	LinearLayout rl_duty;
	@BindView(R.id.tv_count)
	TextView tv_count;
	@BindView(R.id.tv_id)
	TextView tv_id;
	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_invoice_detail;
	}

	@Override
	protected void findViews() {
		ButterKnife.bind(this);
		setTitle("发票详情");
	}

	@Override
	protected void init() {
	}

	@Override
	public void onResume() {
		super.onResume();
	}


	@OnClick({R.id.rl_find_invoice, R.id.rl_order_list})
	public void clickView(View view) {
		switch (view.getId()) {
			case R.id.rl_find_invoice:
				Bundle bundle1 = new Bundle();
				bundle1.putString("url", invoiceHistory.getInvoiceImages());
				IntentUtil.gotoActivity(HistoryDetailActivity.this, FindInvoiceActivity.class, bundle1);
				break;
			case R.id.rl_order_list:
				Bundle bundle = new Bundle();
				bundle.putString("invoiceHistoryIds", invoiceHistory.getInvoiceOrders());
				if("1".equals( invoiceHistory.getBuyType())){
					IntentUtil.gotoActivity(HistoryDetailActivity.this, InvoiceOrderActivity.class, bundle);
				}else if("2".equals( invoiceHistory.getBuyType())){
					IntentUtil.gotoActivity(HistoryDetailActivity.this, InvoicePackageActivity.class, bundle);
				}else{
					IntentUtil.gotoActivity(HistoryDetailActivity.this, InvoiceLeaseOrderActivity.class, bundle);
			}
				break;
		}
	}

	@Override
	protected void initGetData() {
		invoiceHistory = (InvoiceHistory)getIntent().getSerializableExtra("invoiceHistory");
		if (invoiceHistory == null) {
			ToastUtil.showToast(this, "详情信息异常，请稍候重试");
			onBackPressed();
		} else {
			try {
				if (TextUtils.isEmpty(invoiceHistory.getInvoiceImages())) {
					rl_find_invoice.setVisibility(View.GONE);
				} else {
					rl_find_invoice.setVisibility(View.VISIBLE);
				}
				if (!TextUtils.isEmpty(invoiceHistory.getInvoiceType()) && invoiceHistory.getInvoiceType().equals("1")) {
					rl_name.setVisibility(View.GONE);
					rl_phone.setVisibility(View.GONE);
					rl_address.setVisibility(View.GONE);
					rl_email.setVisibility(View.VISIBLE);
				} else {
					rl_email.setVisibility(View.GONE);
					tv_name.setText(invoiceHistory.getAcceptName());
					tv_phone.setText(invoiceHistory.getAcceptPhone());
					tv_address.setText(invoiceHistory.getAcceptAddress());
				}
				tv_email.setText(invoiceHistory.getUserEmail());
				tv_head.setText(invoiceHistory.getInvoiceTitle());
				if (!TextUtils.isEmpty(invoiceHistory.getInvoiceType()) && invoiceHistory.getInvoiceTitleType().equals("2")) {
					rl_duty.setVisibility(View.GONE);
				} else {
					tv_duty.setText(invoiceHistory.getDutyParagraph());
				}
				tv_content.setText(invoiceHistory.getInvoiceContent());
				tv_money.setText(Utils.getMoney(Utils.chu(invoiceHistory.getInvoiceAmount(), "100"))+" 元");
				tv_apply_time.setText(invoiceHistory.getCreateTime());
				String[] arr = invoiceHistory.getInvoiceOrders().split(",");
				tv_count.setText("此发票含" + arr.length + "个订单");
				tv_id.setText(invoiceHistory.getInvoiceId());
			} catch (Exception e){}
		}
	}

	@Override
	protected void widgetListener() {
	}
}
