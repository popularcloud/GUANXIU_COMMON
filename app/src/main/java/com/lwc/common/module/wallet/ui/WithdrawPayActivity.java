package com.lwc.common.module.wallet.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.bean.TradingRecordBean;
import com.lwc.common.bean.WxBean;
import com.lwc.common.configs.ServerConfig;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.User;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.KeyboardUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.PayServant;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.AliPayCallBack;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 充值
 * 
 * @author 何栋
 * @date 2017年11月29日
 */
public class WithdrawPayActivity extends BaseActivity {

	@BindView(R.id.et_money)
	EditText et_money;
	@BindView(R.id.tBtnSecretWechat)
	ToggleButton tBtnSecretWechat;
	@BindView(R.id.tBtnSecretAlipay)
	ToggleButton tBtnSecretAlipay;
	private User user;
	public static WithdrawPayActivity activity;
	private String tradeid;
	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_withdraw_pay;
	}

	@Override
	protected void findViews() {
		ButterKnife.bind(this);
		setTitle("充值");
		user = SharedPreferencesUtils.getInstance(this).loadObjectData(User.class);
		activity = this;
	}

	@OnClick({R.id.tBtnSecretWechat, R.id.tBtnSecretAlipay,R.id.rl_ali_pay, R.id.rl_wechat_pay, R.id.txtWithdraw})
	public void onClickView(View view) {
		switch (view.getId()) {
			case R.id.rl_wechat_pay:
			case R.id.tBtnSecretWechat:
				tBtnSecretWechat.setChecked(true);
				tBtnSecretAlipay.setChecked(false);
				break;
			case R.id.rl_ali_pay:
			case R.id.tBtnSecretAlipay:
				tBtnSecretWechat.setChecked(false);
				tBtnSecretAlipay.setChecked(true);
				break;
			case R.id.txtWithdraw:
				String money = et_money.getText().toString().trim();
				if (TextUtils.isEmpty(money)) {
					ToastUtil.showLongToast(WithdrawPayActivity.this, "请输入充值金额");
					return;
				}
				if (!TextUtils.isEmpty(money) && (money.equals("0.") || money.equals("0.0") || money.equals("0.00"))) {
					ToastUtil.showLongToast(WithdrawPayActivity.this, "请输入正确的充值金额");
					return;
				}
				if (money.substring(money.length()-1, money.length()).equals(".")) {
					money = money.substring(0, money.length()-1);
				}

				if (Double.parseDouble(money) == 0) {
					ToastUtil.showLongToast(WithdrawPayActivity.this, "充值金额要大于0");
					return;
				}
				if (Double.parseDouble(money) > 10000) {
					ToastUtil.showLongToast(WithdrawPayActivity.this, "充值金额单次最多1万元");
					return;
				}
				String payType = "3";
				if (tBtnSecretAlipay.isChecked()) {
					payType = "2";
				} else if (tBtnSecretWechat.isChecked()){
					payType = "3";
				}
				HashMap<String, String> params = new HashMap<>();
				params.put("user_role","5");
				params.put("user_id",user.getUserId());
				params.put("user_real_name",user.getUserRealname());
				params.put("transaction_amount", Utils.cheng(money,"100"));
				params.put("transaction_means", payType);
				params.put("appType", "person");

				HttpRequestUtils.httpRequest(WithdrawPayActivity.this, "recharge充值", RequestValue.GET_PAY_RECHARGE, params, "POST", new HttpRequestUtils.ResponseListener() {
					@Override
					public void getResponseData(String response) {
						Common common = JsonUtil.parserGsonToObject(response, Common.class);
						if (common != null && common.getStatus().equals("1")) {
							if (tBtnSecretAlipay.isChecked()) {
								try {
									final JSONObject obj = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
									PayServant.getInstance().aliPay(obj, WithdrawPayActivity.this, new AliPayCallBack() {
										@Override
										public void OnAliPayResult(boolean success, boolean isWaiting, String msg) {
											ToastUtil.showLongToast(WithdrawPayActivity.this, msg);
											if (success) {
												onBackPressed();
											} else {
												tradeid = obj.optString("tradeid");
												payFailure();
											}
										}
									});
								} catch (JSONException e) {
									e.printStackTrace();
								}
							} else if (tBtnSecretWechat.isChecked()){
								WxBean wx = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), WxBean.class);
								tradeid = wx.getTradeid();
								PayServant.getInstance().weChatPay2(
										WithdrawPayActivity.this, wx.getAppid(),
										wx.getPartnerId(), wx.getPrepayId(), wx.getNonceStr(),
										wx.getTimeStamp(), wx.getPackageStr(), wx.getSign());
							}
						} else if (common != null){
							ToastUtil.showLongToast(WithdrawPayActivity.this, common.getInfo());
						}else {
							ToastUtil.showLongToast(WithdrawPayActivity.this, "服务器返回数据结构有误");
						}

					}
					@Override
					public void returnException(Exception e, String msg) {
						LLog.eNetError(e.toString());
					}
				});
			default:
				break;
		}
	}

	public void payFailure() {
//		HashMap<String, String> params = new HashMap<>();
//		params.put("transaction_no",tradeid);
//		params.put("transaction_status","4");
//		OkHttpUtils.post().url(ServerConfig.DOMAIN+"/main/webAppPay/updateUserPaymentRecord").params(params).build().execute(new StringCallback() {
//			@Override
//			public void onError(Call call, Exception e, int id) {
//				LLog.eNetError(e.toString());
//			}
//			@Override
//			public void onResponse(String response, int id) {
//				Common common = JsonUtil.parserGsonToObject(response, Common.class);
//				if (common != null && common.getStatus().equals("1")) {
//
//				} else if (common != null){
//					ToastUtil.showLongToast(WithdrawPayActivity.this, common.getInfo());
//				}else {
//					ToastUtil.showLongToast(WithdrawPayActivity.this, "服务器返回数据结构有误");
//				}
//			}
//		});
	}

	@Override
	protected void init() {
	}

	@Override
	protected void initGetData() {
	}

	@Override
	protected void widgetListener() {
		et_money.addTextChangedListener(new TextWatcher()
		{
			public void afterTextChanged(Editable edt)
			{

				String temp = edt.toString();
				if(temp.lastIndexOf(".") != temp.indexOf(".") || temp.indexOf(".") == 0) {
					et_money.setText(temp.substring(0, temp.length()-1));
					et_money.setSelection(temp.length()-1);
					ToastUtil.showToast(WithdrawPayActivity.this, "请输入正确的金额");
					return;
				}
				int posDot = temp.indexOf(".");
				if (posDot <= 0) return;
				if (temp.length() - posDot - 1 > 2)
				{
					edt.delete(posDot + 3, posDot + 4);
				}
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
		});
	}

	@Override
	public void finish() {
		super.finish();
		KeyboardUtil.showInput(false, this);
	}
}
