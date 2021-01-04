package com.lwc.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.configs.ServerConfig;
import com.lwc.common.module.invoice.OpenInvoiceActivity;
import com.lwc.common.utils.CommonUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义对话框
 * 
 */
public class InvoiceInformationDialog extends Dialog implements View.OnClickListener{

	/** 上下文 */
	private Context context;
	CallBack callBack;
	private LinearLayout ll_Invoice_personal_name;
	private LinearLayout ll_Invoice_title;
	private LinearLayout ll_invoice_tax_No;
	private TextView tv_open_invoice_desc;

	private int invoiceType = 2; //1.企业抬头  2.个人

	public InvoiceInformationDialog(Context context, CallBack callBack, String prices, String two) {
		super(context, R.style.BottomDialogStyle);
		// 拿到Dialog的Window, 修改Window的属性
		Window window = getWindow();
		window.getDecorView().setPadding(0, 0, 0, 0);
		// 获取Window的LayoutParams
		WindowManager.LayoutParams attributes = window.getAttributes();
		attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
		attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		// 一定要重新设置, 才能生效
		window.setAttributes(attributes);
		this.callBack = callBack;
		init(context, prices, two);
	}

	/**
	 *
	 * @param context
	 * @param theme
     */
	public InvoiceInformationDialog(Context context, int theme) {
		super(context, theme);
		init(context, null, null);
	}

	public InvoiceInformationDialog(Context context) {
		super(context, R.style.dialog_style);
		init(context, null, null);
	}

	/**
	 * 初始话对话框
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param context
	 * 
	 */
	protected void init(final Context context, final String prices, String two) {
		this.context = context;
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
		this.setContentView(R.layout.dialog_invoice_information_layout);

		ImageView ic_close = findViewById(R.id.ic_close);
		TextView tv_submit = findViewById(R.id.tv_submit);

		ll_Invoice_personal_name = findViewById(R.id.ll_Invoice_personal_name);
		ll_Invoice_title = findViewById(R.id.ll_Invoice_title);
		ll_invoice_tax_No = findViewById(R.id.ll_invoice_tax_No);

		TextView tv_normal_invoice = findViewById(R.id.tv_normal_invoice);
		TextView tv_no_invoice = findViewById(R.id.tv_no_invoice);

		final TextView tv_personal = findViewById(R.id.tv_personal);
		final TextView tv_unit = findViewById(R.id.tv_unit);
		 tv_open_invoice_desc = findViewById(R.id.tv_open_invoice_desc);


		final EditText et_personal_name = findViewById(R.id.et_personal_name);
		final EditText et_unit_invoice = findViewById(R.id.et_unit_invoice);
		final EditText et_invoice_num = findViewById(R.id.et_invoice_num);
		final TextView tv_invoice_price = findViewById(R.id.tv_invoice_price);
		final EditText et_email = findViewById(R.id.et_email);
		final EditText et_more_message = findViewById(R.id.et_more_message);


		tv_invoice_price.setText(Utils.getMoney(Utils.chu(prices,"100"))+"元");

		/**
		 * 不开发票
		 */
		tv_no_invoice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(callBack != null){
					callBack.onSubmit(null);
				}
			}
		});

		tv_open_invoice_desc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("url", ServerConfig.DOMAIN.replace("https", "http")+"/main/h5/invoice.html");
				bundle.putString("title", "开票说明");
				IntentUtil.gotoActivity(context, InformationDetailsActivity.class, bundle);
			}
		});

		tv_personal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				invoiceType = 2;
				ll_Invoice_personal_name.setVisibility(View.VISIBLE);
				ll_Invoice_title.setVisibility(View.GONE);
				ll_invoice_tax_No.setVisibility(View.GONE);

				tv_personal.setBackgroundResource(R.drawable.button_red_round_shape);
				tv_personal.setTextColor(Color.parseColor("#ff3a3a"));

				tv_unit.setBackgroundResource(R.drawable.button_gray_round_shape_f0);
				tv_unit.setTextColor(Color.parseColor("#ff000000"));

			}
		});

		tv_unit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				invoiceType = 1;
				ll_Invoice_personal_name.setVisibility(View.GONE);
				ll_Invoice_title.setVisibility(View.VISIBLE);
				ll_invoice_tax_No.setVisibility(View.VISIBLE);

				tv_unit.setBackgroundResource(R.drawable.button_red_round_shape);
				tv_unit.setTextColor(Color.parseColor("#ff3a3a"));

				tv_personal.setBackgroundResource(R.drawable.button_gray_round_shape_f0);
				tv_personal.setTextColor(Color.parseColor("#ff000000"));
			}
		});


		ic_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		tv_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Map<String,String> params = new HashMap<>();
				params.put("invoice_type","1");
				params.put("invoice_amount",prices);

				if(invoiceType == 1){//企业
					params.put("invoice_title_type","1");

					String unitInvoice = et_unit_invoice.getText().toString().trim();
					if(!TextUtils.isEmpty(unitInvoice)){
						params.put("invoice_title",unitInvoice);
					}else{
						ToastUtil.showToast(context,"请填写发票抬头");
						return;
					}
					String invoiceNum = et_invoice_num.getText().toString().trim();
					if(!TextUtils.isEmpty(invoiceNum)){
						params.put("duty_paragraph",invoiceNum);
					}else{
						ToastUtil.showToast(context,"请填写发票税号");
						return;
					}

					String email = et_email.getText().toString().trim();

					if(CommonUtils.isEmail(email)){
						params.put("user_email",email);
					}else{
						ToastUtil.showToast(context,"请填写正确的电子邮箱");
						return;
					}

					String moreMessage = et_more_message.getText().toString().trim();
					if(!TextUtils.isEmpty(moreMessage)){
						params.put("remark",moreMessage);
					}
				}else{ //个人
					params.put("invoice_title_type","2");
					String personalName = et_personal_name.getText().toString().trim();
					if(!TextUtils.isEmpty(personalName)){
						params.put("invoice_title",personalName);
					}else{
						ToastUtil.showToast(context,"请填写个人名称");
						return;
					}
					String email = et_email.getText().toString().trim();
					if(CommonUtils.isEmail(email)){
						params.put("user_email",email);
					}else{
						ToastUtil.showToast(context,"请填写电子邮箱");
						return;
					}

					String moreMessage = et_more_message.getText().toString().trim();
					if(!TextUtils.isEmpty(moreMessage)){
						params.put("remark",moreMessage);
					}
				}
				params.put("invoice_content","设备租赁费用");

				if(callBack != null){
					callBack.onSubmit(params);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		}
	}

	public interface CallBack {
		void onSubmit(Map<String,String> params);
	}
}
