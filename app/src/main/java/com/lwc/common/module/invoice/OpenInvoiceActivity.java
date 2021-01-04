package com.lwc.common.module.invoice;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.configs.ServerConfig;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.InvoiceHistory;
import com.lwc.common.utils.CommonUtils;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 开发票
 * 
 * @author 何栋
 * @date 2018年1月05日
 */
public class OpenInvoiceActivity extends BaseActivity {

	private InvoiceHistory invoiceHistory;

	@BindView(R.id.rl_name)
	LinearLayout rl_name;
	@BindView(R.id.et_name)
	EditText et_name;
	@BindView(R.id.rl_phone)
	LinearLayout rl_phone;
	@BindView(R.id.et_phone)
	EditText et_phone;
	@BindView(R.id.rl_address)
	LinearLayout rl_address;
	@BindView(R.id.et_address)
	EditText et_address;
	@BindView(R.id.rl_email)
	LinearLayout rl_email;
	@BindView(R.id.et_email)
	EditText et_email;
	@BindView(R.id.line_name)
	View line_name;
	@BindView(R.id.line_phone)
	View line_phone;
	@BindView(R.id.line_address)
	View line_address;

	@BindView(R.id.et_head)
	EditText et_head;
	@BindView(R.id.rl_duty)
	LinearLayout rl_duty;
	@BindView(R.id.et_duty)
	EditText et_duty;
	@BindView(R.id.line_duty)
	View line_duty;
	@BindView(R.id.tv_content)
	TextView tv_content;
	@BindView(R.id.tv_money)
	TextView tv_money;
	@BindView(R.id.et_more)
	EditText et_more;
	@BindView(R.id.tv_dianzi)
	TextView tv_dianzi;
	@BindView(R.id.tv_zhizhi)
	TextView tv_zhizhi;
	@BindView(R.id.tv_tip)
	TextView tv_tip;
	@BindView(R.id.tv_qiye_head)
	TextView tv_qiye_head;
	@BindView(R.id.tv_geren_head)
	TextView tv_geren_head;
	private String orderIds;
	private String totalManey;
	private String invoiceOperationType = "order";  // order   package

	private String invoiceType="1";
	private String invoiceTitelType="1";
	private String titleStr;
	private String duty;
	private String email;
	private String name;
	private String phone;
	private String address;

	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_invoice_open;
	}

	@Override
	protected void findViews() {
		ButterKnife.bind(this);
		setTitle("开票信息");
		setRight("开票说明", new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("url", ServerConfig.DOMAIN.replace("https", "http")+"/main/h5/invoice.html");
				bundle.putString("title", "开票说明");
				IntentUtil.gotoActivity(OpenInvoiceActivity.this, InformationDetailsActivity.class, bundle);
			}
		});
	}

	@Override
	protected void init() {
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 *
	 * 查询开票信息
	 *
	 */
	private void getInvoiceInfoList() {
		HashMap<String, String> params = new HashMap<>();
		HttpRequestUtils.httpRequest(this, "查询开票信息", RequestValue.GET_INVOICE_INFO, params, "GET", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String result) {
				Common common = JsonUtil.parserGsonToObject(result, Common.class);
				if (common.getStatus().equals("1")) {
					invoiceHistory = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(result, "data"), InvoiceHistory.class);
					if (invoiceHistory != null) {
						et_head.setText(invoiceHistory.getInvoiceTitle());
				/*		if (!TextUtils.isEmpty(invoiceHistory.getInvoiceType()) && invoiceHistory.getInvoiceType().equals("2")) {
							tv_zhizhi.performClick();
							if (!TextUtils.isEmpty(invoiceHistory.getAcceptName()))
								et_name.setText(invoiceHistory.getAcceptName());
							if (!TextUtils.isEmpty(invoiceHistory.getAcceptPhone()))
								et_phone.setText(invoiceHistory.getAcceptPhone());
							if (!TextUtils.isEmpty(invoiceHistory.getAcceptAddress()))
								et_address.setText(invoiceHistory.getAcceptAddress());
						} else {*/
							if (!TextUtils.isEmpty(invoiceHistory.getUserEmail()))
								et_email.setText(invoiceHistory.getUserEmail());
					/*	}*/
						if (!TextUtils.isEmpty(invoiceHistory.getInvoiceTitleType()) && invoiceHistory.getInvoiceTitleType().equals("2")) {
							if (!TextUtils.isEmpty(invoiceHistory.getDutyParagraph()))
								et_duty.setText(invoiceHistory.getDutyParagraph());
							tv_geren_head.performClick();
						} else {
							if (!TextUtils.isEmpty(invoiceHistory.getDutyParagraph()))
								et_duty.setText(invoiceHistory.getDutyParagraph());
						}
					}
				} else {
					ToastUtil.showToast(OpenInvoiceActivity.this, common.getInfo());
				}
			}

			@Override
			public void returnException(Exception e, String msg) {
				ToastUtil.showToast(OpenInvoiceActivity.this, msg);
			}
		});
	}

	@OnClick({R.id.tv_dianzi, R.id.tv_zhizhi, R.id.tv_qiye_head, R.id.tv_geren_head, R.id.submit})
	public void clickView(View view) {
		String str = "电子发票\n最快5分钟开具";
		switch (view.getId()) {
			case R.id.tv_dianzi:
				rl_name.setVisibility(View.GONE);
				rl_address.setVisibility(View.GONE);
				rl_phone.setVisibility(View.GONE);
				line_address.setVisibility(View.GONE);
				line_name.setVisibility(View.GONE);
				line_phone.setVisibility(View.GONE);
				rl_email.setVisibility(View.VISIBLE);
				tv_dianzi.setBackgroundResource(R.drawable.button_blue_shape);
				tv_zhizhi.setBackgroundResource(R.drawable.button_invoice_shape);
				tv_dianzi.setTextColor(getResources().getColor(R.color.white));
				tv_dianzi.setText(Utils.getSpannableStringBuilder(0, str.length()-1, getResources().getColor(R.color.white), str, 14));
				tv_zhizhi.setTextColor(getResources().getColor(R.color.black_66));
				tv_tip.setText("电子发票和纸质发票具备等同法律效力，可支持报销入账");
				invoiceType = "1";
				break;
			case R.id.tv_zhizhi:
				rl_name.setVisibility(View.VISIBLE);
				rl_address.setVisibility(View.VISIBLE);
				rl_phone.setVisibility(View.VISIBLE);
//				line_address.setVisibility(View.VISIBLE);
				line_name.setVisibility(View.VISIBLE);
				line_phone.setVisibility(View.VISIBLE);
				rl_email.setVisibility(View.GONE);

				tv_dianzi.setBackgroundResource(R.drawable.button_invoice_shape);
				tv_zhizhi.setBackgroundResource(R.drawable.button_blue_shape);
				tv_dianzi.setTextColor(getResources().getColor(R.color.black_66));
				tv_dianzi.setText(Utils.getSpannableStringBuilder(0, str.length()-1, getResources().getColor(R.color.black_66), str, 14));
				tv_zhizhi.setTextColor(getResources().getColor(R.color.white));
				tv_tip.setText("邮费到付，电子发票和纸质发票具备等同法律效力，可支持报销入账");
				invoiceType = "2";
				break;
			case R.id.tv_qiye_head:
				tv_qiye_head.setCompoundDrawables(Utils.getDrawable(this, R.drawable.xuanzhong),null,null,null);
				tv_geren_head.setCompoundDrawables(Utils.getDrawable(this, R.drawable.weixuanzhong),null,null,null);
				rl_duty.setVisibility(View.VISIBLE);
				line_duty.setVisibility(View.VISIBLE);
				invoiceTitelType = "1";
				break;
			case R.id.tv_geren_head:
				tv_geren_head.setCompoundDrawables(Utils.getDrawable(this, R.drawable.xuanzhong),null,null,null);
				tv_qiye_head.setCompoundDrawables(Utils.getDrawable(this, R.drawable.weixuanzhong),null,null,null);
				rl_duty.setVisibility(View.GONE);
				line_duty.setVisibility(View.GONE);
				invoiceTitelType = "2";
				break;
			case R.id.submit:
				//TODO 提交
				titleStr = et_head.getText().toString().trim();
				if (TextUtils.isEmpty(titleStr)) {
					ToastUtil.showToast(this, "请填写发票抬头");
					return;
				}
				duty = et_duty.getText().toString().trim();
				if (invoiceTitelType.equals("1") && TextUtils.isEmpty(duty)) {
					ToastUtil.showToast(this, "请填写公司税号");
					return;
				}
				email = et_email.getText().toString().trim();
				if (invoiceType.equals("1")) {
					if (TextUtils.isEmpty(email)) {
						ToastUtil.showToast(this, "请填写电子邮箱");
						return;
					}

					if (!CommonUtils.isEmail(email)) {
						ToastUtil.showToast(this, "电子邮箱格式不正确");
						return;
					}
				} else if (invoiceType.equals("2")) {
					name = et_name.getText().toString().trim();
					if (TextUtils.isEmpty(name)) {
						ToastUtil.showToast(this, "请填写收件人");
						return;
					}
					phone = et_phone.getText().toString().trim();
					if (TextUtils.isEmpty(phone)) {
						ToastUtil.showToast(this, "请填写联系电话");
						return;
					}
					address = et_address.getText().toString().trim();
					if (TextUtils.isEmpty(address)) {
						ToastUtil.showToast(this, "请填写收件地址");
						return;
					}
				}
				submit();
				break;
		}
	}

	private void submit() {
		if (Utils.isFastClick(1500, RequestValue.POST_INVOICE)) {
			return;
		}
		HashMap<String, String> params = new HashMap<>();
		params.put("invoice_type", invoiceType);
		params.put("invoice_title_type", invoiceTitelType);
		params.put("invoice_title", titleStr);
		params.put("invoice_content", tv_content.getText().toString().trim());
		params.put("invoice_amount", Utils.cheng(totalManey, "100"));
		params.put("invoice_orders", orderIds);

		if("order".equals(invoiceOperationType)){
			params.put("buy_type", "1");
		}else if("package".equals(invoiceOperationType)){
			params.put("buy_type", "2");
		}else{
			params.put("buy_type", "3");
		}

		if (invoiceTitelType.equals("1")) {
			params.put("duty_paragraph", duty);
		}
		if (invoiceType.equals("2")){
			params.put("accept_name", name);
			params.put("accept_phone", phone);
			params.put("accept_address", address);
		} else {
			params.put("user_email", email);
		}
		params.put("remark", et_more.getText().toString().trim());

		HttpRequestUtils.httpRequest(this, "submitInvoice", RequestValue.POST_INVOICE, params, "POST", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String result) {
				Common common = JsonUtil.parserGsonToObject(result, Common.class);
				if (common.getStatus().equals("1")) {
					ToastUtil.showLongToast(OpenInvoiceActivity.this, "提交成功");
					onBackPressed();
				} else {
					ToastUtil.showToast(OpenInvoiceActivity.this, common.getInfo());
				}
			}

			@Override
			public void returnException(Exception e, String msg) {
				ToastUtil.showToast(OpenInvoiceActivity.this, msg);
			}
		});
	}

	@Override
	protected void initGetData() {
		orderIds = getIntent().getStringExtra("invoiceOrderIds");
		totalManey = getIntent().getStringExtra("totalManey");
		invoiceOperationType = getIntent().getStringExtra("invoiceOperationType");
		if("package".equals(invoiceOperationType)){
			tv_content.setText("维修套餐费");
		}else if("leaseOrder".equals(invoiceOperationType)){
			tv_content.setText("设备租赁费用");
		}
		tv_money.setText(Utils.getMoney(totalManey)+" 元");
		getInvoiceInfoList();
	}

	@Override
	protected void widgetListener() {
	}
}
