package com.lwc.common.module.order.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.bean.WxBean;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Coupon;
import com.lwc.common.module.bean.Detection;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.login.ui.LoginActivity;
import com.lwc.common.module.wallet.ui.InputPayPwdActivity;
import com.lwc.common.module.wallet.ui.PayPwdActivity;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.PayServant;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.AliPayCallBack;
import com.lwc.common.widget.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PayActivity extends BaseActivity {

	@BindView(R.id.tv_money)
	TextView tv_money;
	@BindView(R.id.tv_dens)
	TextView tv_dens;
	@BindView(R.id.tv_qb_money)
	TextView tv_qb_money;
	@BindView(R.id.tv_qb_pay)
	TextView tv_qb_pay;
	@BindView(R.id.tv_weixin)
	TextView tv_weixin;
	@BindView(R.id.tv_alipay)
	TextView tv_alipay;
	@BindView(R.id.rl_wechat)
	RelativeLayout rl_wechat;
	@BindView(R.id.rl_alipay)
	RelativeLayout rl_alipay;
	@BindView(R.id.iv_weixin)
	ImageView iv_weixin;
	@BindView(R.id.iv_alipay)
	ImageView iv_alipay;
	@BindView(R.id.iv_qb)
	ImageView iv_qb;
	private User user;
	private Detection detection;
	private boolean isQb = false;
	private String total;
	public static PayActivity activity;
	private String pwd, packageId, money;
	private String images, cause;
	private Coupon coupon;
	private PackageBean packageB;
	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_pay;
	}

	@Override
	protected void findViews() {
		ButterKnife.bind(this);
		activity = this;
		user = SharedPreferencesUtils.getInstance(this).loadObjectData(User.class);
	}

	@Override
	protected void init() {
	}

	@Override
	protected void initGetData() {
		operate = getIntent().getStringExtra("operate");
		images = getIntent().getStringExtra("images");
		cause = getIntent().getStringExtra("cause");
		detection = (Detection)getIntent().getSerializableExtra("data");
		coupon = (Coupon)getIntent().getSerializableExtra("coupon");
		packageB = (PackageBean)getIntent().getSerializableExtra("package");
		packageId = getIntent().getStringExtra("packageId");
		total = getIntent().getStringExtra("money");
		if (TextUtils.isEmpty(packageId)) {
			total = "0";
			int maintainCostInt = Integer.parseInt(TextUtils.isEmpty(detection.getMaintainCost())?"0":detection.getMaintainCost());
			int otherCostInt = Integer.parseInt(TextUtils.isEmpty(detection.getOtherCost())?"0":detection.getOtherCost());
			int visitCostInt = Integer.parseInt(TextUtils.isEmpty(detection.getVisitCost())?"0":detection.getVisitCost());
			int hardwareCostInt = Integer.parseInt(TextUtils.isEmpty(detection.getHardwareCost())?"0":detection.getHardwareCost());
			if (detection != null) {
				if (operate.equals("2")) {  //提交故障确认单 支付
				/*	if (!TextUtils.isEmpty(detection.getVisitCost())&& packageB != null && packageB.getPackageType() != 1 && packageB.getPackageType() != 3) {
						visitCostInt = 0;
					}
					if (!TextUtils.isEmpty(detection.getMaintainCost()) && Integer.parseInt(detection.getMaintainCost()) > 0 && packageB != null && packageB.getPackageType() != 2 && packageB.getPackageType() != 3) {
						total = Utils.jia(total, detection.getMaintainCost());
					}
					if (!TextUtils.isEmpty(detection.getOtherCost()) && Integer.parseInt(detection.getOtherCost()) > 0 && packageB != null) {
						total = Utils.jia(total, detection.getOtherCost());
					}
					if (!TextUtils.isEmpty(detection.getHardwareCost()) && Integer.parseInt(detection.getHardwareCost()) > 0) {
						total = Utils.jia(total, detection.getHardwareCost());
					}*/

					if (packageB != null && !TextUtils.isEmpty(packageB.getPackageId())) {
						if (packageB.getPackageType() == 1) {
							visitCostInt = 0;
						} else if (packageB.getPackageType() == 2) {
							maintainCostInt = 0;
						} else if (packageB.getPackageType() == 3) {
							visitCostInt = 0;
							maintainCostInt = 0;
						}
					}

					//int lastTotalInt = 0;
					if (coupon != null && !TextUtils.isEmpty(coupon.getDiscountAmount()) && Integer.parseInt(coupon.getDiscountAmount()) > 0) {

						//优惠额度
						int discountAmountInt = Integer.parseInt(coupon.getDiscountAmount());
						int discountAmountIntP = discountAmountInt * 100;
						int lastDiscount = discountAmountIntP;
						//优惠卷类型(1:通用 2:上门优惠 3:软件优惠 4:硬件优惠)
						if (coupon.getCouponType() == 1) {
							//先减上门费
							if (lastDiscount > visitCostInt) {
								lastDiscount = lastDiscount - visitCostInt;
								visitCostInt = 0;
							} else {
								visitCostInt = visitCostInt - lastDiscount;
								lastDiscount = 0;
							}

							//减维修费
							if (lastDiscount > maintainCostInt) {
								lastDiscount = lastDiscount - maintainCostInt;
								maintainCostInt = 0;

							} else {
								maintainCostInt = maintainCostInt - lastDiscount;
								lastDiscount = 0;
							}

							//减服务附加费
							if (lastDiscount > otherCostInt) {
								lastDiscount = lastDiscount - otherCostInt;
								otherCostInt = 0;

							} else {
								otherCostInt = otherCostInt - lastDiscount;
								lastDiscount = 0;
							}

							//减硬件费用
							if (lastDiscount > hardwareCostInt) {
								lastDiscount = lastDiscount - hardwareCostInt;
								hardwareCostInt = 0;
							} else {
								hardwareCostInt = hardwareCostInt - lastDiscount;
								lastDiscount = 0;
							}
						} else if (coupon.getCouponType() == 2) {
							if (lastDiscount > visitCostInt) {
								lastDiscount = lastDiscount - visitCostInt;
								visitCostInt = 0;
							} else {
								visitCostInt = visitCostInt - lastDiscount;
								lastDiscount = 0;
							}
						} else if (coupon.getCouponType() == 3) {
							//减维修费
							if (lastDiscount > maintainCostInt) {
								lastDiscount = lastDiscount - maintainCostInt;
								maintainCostInt = 0;
							} else {
								maintainCostInt = maintainCostInt - lastDiscount;
								lastDiscount = 0;
							}
							//减服务附加费
							if (lastDiscount > otherCostInt) {
								lastDiscount = lastDiscount - otherCostInt;
								otherCostInt = 0;
							} else {
								otherCostInt = otherCostInt - lastDiscount;
								lastDiscount = 0;
							}
						} else if (coupon.getCouponType() == 4) {
							//减硬件费用
							if (lastDiscount > hardwareCostInt) {
								lastDiscount = lastDiscount - hardwareCostInt;
								hardwareCostInt = 0;
							} else {
								hardwareCostInt = hardwareCostInt - lastDiscount;
								lastDiscount = 0;
							}
						}
					}
					int totalInt = visitCostInt + maintainCostInt + hardwareCostInt + otherCostInt;
					total = String.valueOf(totalInt);
					} else { //拒绝返厂和维修
					if (packageB != null && packageB.getPackageType() == 1) {
						total = "0";
					} else {
						total = Utils.jia(total, detection.getReVisitCost()==null?detection.getVisitCost():detection.getReVisitCost());
					}
				}
				tv_money.setText("¥ " + Utils.getMoney(Utils.chu(total, "100")));

				if("0".equals(total)){
					tv_weixin.setTextColor(Color.parseColor("#C9C9C9"));
					tv_alipay.setTextColor(Color.parseColor("#C9C9C9"));
					rl_wechat.setClickable(false);
					rl_alipay.setClickable(false);
					iv_weixin.setImageResource(R.drawable.ic_paytype_wechat_no);
					iv_alipay.setImageResource(R.drawable.ic_paytype_ali_no);
				}else{
					tv_weixin.setTextColor(Color.parseColor("#666666"));
					tv_alipay.setTextColor(Color.parseColor("#666666"));
					rl_wechat.setClickable(true);
					rl_alipay.setClickable(true);
					iv_weixin.setImageResource(R.drawable.ic_paytype_wechat);
					iv_alipay.setImageResource(R.drawable.ic_paytype_ali);
				}
			} else {
				ToastUtil.showLongToast(this, "数据异常");
				finish();
			}
		} else {// 购买套餐
			tv_money.setText("¥ " + Utils.getMoney(Utils.chu(total, "100")));
		}
		if (user != null && !TextUtils.isEmpty(user.getBanlance()) && Float.parseFloat(user.getBanlance().trim()) >= Float.parseFloat(Utils.chu(total, "100"))) {
			tv_qb_money.setText("剩余："+Utils.getMoney(user.getBanlance())+" 元");
			tv_qb_pay.setTextColor(getResources().getColor(R.color.black_66));
			iv_qb.setImageResource(R.drawable.ic_paytype_balance);
			isQb = true;
		} else {
			if (user != null && TextUtils.isEmpty(user.getBanlance())) {
				user.setBanlance("0");
			} else if (user == null){
				//ToastUtil.showLongToast(this, "登录信息过期，请重新登录！");
				SharedPreferencesUtils.getInstance(PayActivity.this).saveString("token","");
				IntentUtil.gotoActivityAndFinish(this, LoginActivity.class);
				return;
			}
			isQb = false;
			iv_qb.setImageResource(R.drawable.ic_paytype_balance_no);
			tv_qb_money.setText("余额不足，剩余："+Utils.getMoney(user.getBanlance())+" 元");
			tv_qb_pay.setTextColor(getResources().getColor(R.color.gray_99));
		}
	}

	@Override
	protected void widgetListener() {
	}

	@OnClick({R.id.iv_close, R.id.rl_qb, R.id.rl_wechat, R.id.rl_alipay})
	public void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.iv_close:
			finish();
			break;
		case R.id.rl_qb:
			//TODO 钱包余额支付
			user = SharedPreferencesUtils.getInstance(this).loadObjectData(User.class);
			payType = "1";
			if (isQb) {
				if (TextUtils.isEmpty(user.getPayPassword())) {
					DialogUtil.showMessageDg(this, "温馨提示", "您还未设置支付密码\n请先去设置支付密码！", new CustomDialog.OnClickListener() {

						@Override
						public void onClick(CustomDialog dialog, int id, Object object) {
							dialog.dismiss();
							IntentUtil.gotoActivity(PayActivity.this, PayPwdActivity.class);
						}
					});
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putString("money", Utils.chu(total, "100"));
				if (detection != null && detection.getOrderId() != null) {
					bundle.putString("title", "支付订单：" + detection.getOrderId());
				} else if (!TextUtils.isEmpty(packageId)) {
					bundle.putString("title", "购买套餐");
				}
				IntentUtil.gotoActivityForResult(this, InputPayPwdActivity.class, bundle, 12302);
			}
			break;
		case R.id.rl_wechat:
			//TODO 微信支付
			payType = "3";
			pay();
			break;
		case R.id.rl_alipay:
			//TODO 支付宝支付
			payType = "2";
			pay();
			break;
		default:
			break;
		}
	}
	String payType = "0";
	String operate = "0";
	public void pay() {
		String url = RequestValue.POST_PAY_INFO;
		HashMap<String, String> params = new HashMap<>();
		if (TextUtils.isEmpty(packageId)) {
			String role = "5";
			if (!TextUtils.isEmpty(user.getRoleId())) {
				role = user.getRoleId();
			}
			params.put("user_role", role);
			params.put("operate", operate);
			params.put("orderId", detection.getOrderId());
			params.put("appType", "person");
			if (operate.equals("1")) {
				if (images != null)
					params.put("images",images);
				params.put("cause", cause);
			}
			if (coupon != null && !TextUtils.isEmpty(coupon.getCouponId())) {
				params.put("couponId", coupon.getCouponId());
			}
			if (packageB != null && !TextUtils.isEmpty(packageB.getPackageId())) {
				params.put("packageId", packageB.getPackageId());
			}
			params.put("transaction_amount",total);
		} else {
			payPack();
			return;
		}
		params.put("transaction_means", payType);

		if (payType.equals("1")) {
			params.put("payPassword", pwd);
		}
		HttpRequestUtils.httpRequest(this, "查询支付详情", url, params, "POST", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String response) {
				Common common = JsonUtil.parserGsonToObject(response, Common.class);
				if (common.getStatus().equals("1")){
					try {
						final JSONObject jsonObj = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
						//保存支付获得的积分
						SharedPreferencesUtils.setParam(PayActivity.this,"get_integral", jsonObj.getString("integral"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
						if (payType.equals("2")) {
							try {
								final JSONObject obj = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
								PayServant.getInstance().aliPay(obj, PayActivity.this, new AliPayCallBack() {
									@Override
									public void OnAliPayResult(boolean success, boolean isWaiting, String msg) {
										ToastUtil.showLongToast(PayActivity.this, msg);
										if (success) {
											setResult(RESULT_OK);
										}
										finish();
									}
								});
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else if (payType.equals("3")){
							WxBean wx = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), WxBean.class);
							PayServant.getInstance().weChatPay2(
									PayActivity.this, wx.getAppid(),
									wx.getPartnerId(), wx.getPrepayId(), wx.getNonceStr(),
									wx.getTimeStamp(), wx.getPackageStr(), wx.getSign());
						} else if(payType.equals("1")) {
							if (TextUtils.isEmpty(packageId) && operate != null && operate.equals("1")) {
								user.setBanlance(Utils.jian(Utils.cheng(user.getBanlance(), "100"), detection.getVisitCost()));
							} else {
								user.setBanlance(Utils.jian(Utils.cheng(user.getBanlance(), "100"), total));
							}
							SharedPreferencesUtils.getInstance(PayActivity.this).saveObjectData(user);
							ToastUtil.showToast(PayActivity.this, "支付成功！");
							setResult(RESULT_OK);
							finish();
						}

				} else if (common.getInfo().contains("支付密码错误")) {
					DialogUtil.showMessageDg(PayActivity.this, "温馨提示", "忘记密码", "重新输入", "您输入的支付密码错误!", new CustomDialog.OnClickListener() {
						@Override
						public void onClick(CustomDialog dialog, int id, Object object) {
							dialog.dismiss();
							IntentUtil.gotoActivity(PayActivity.this, PayPwdActivity.class);
						}
					}, new CustomDialog.OnClickListener() {
						@Override
						public void onClick(CustomDialog dialog, int id, Object object) {
							dialog.dismiss();
							Bundle bundle = new Bundle();
							if (TextUtils.isEmpty(packageId)) {
								if (operate.equals("2")) {
									bundle.putString("money", Utils.chu(total, "100"));
								} else {
									bundle.putString("money", Utils.chu(detection.getVisitCost(), "100"));
								}
							} else {
								bundle.putString("money", Utils.chu(total, "100"));
							}
							bundle.putString("title", "支付订单："+detection.getOrderId());
							IntentUtil.gotoActivityForResult(PayActivity.this, InputPayPwdActivity.class, bundle, 12302);
						}
					});
				} else {
					ToastUtil.showLongToast(PayActivity.this, common.getInfo());
				}
			}

			@Override
			public void returnException(Exception e, String msg) {
				LLog.eNetError(e.toString());
				ToastUtil.showLongToast(PayActivity.this, msg);
			}
		});
	}

	public void payPack() {
		JSONObject params = new JSONObject();
		try {
			params.put("packageId", packageId);
			params.put("transaction_means", payType);
			params.put("appType", "person");
			if (payType.equals("1")) {
				params.put("payPassword", pwd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpRequestUtils.httpRequestNew(this, "查询支付详情", RequestValue.POST_PAY_BUY_PACKAGE, params, "POST", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String response) {
				Common common = JsonUtil.parserGsonToObject(response, Common.class);
				if (common.getStatus().equals("1")){

					try {
						final JSONObject jsonObj = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
						//保存支付获得的积分
						SharedPreferencesUtils.setParam(PayActivity.this,"get_integral", jsonObj.getString("integral"));
					} catch (JSONException e) {
						e.printStackTrace();
					}

					if (payType.equals("2")) {
						try {
							final JSONObject obj = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));

							PayServant.getInstance().aliPay(obj, PayActivity.this, new AliPayCallBack() {
								@Override
								public void OnAliPayResult(boolean success, boolean isWaiting, String msg) {
									ToastUtil.showLongToast(PayActivity.this, msg);
									if (success) {
										setResult(RESULT_OK);
									}
									finish();
								}
							});
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else if (payType.equals("3")){

						WxBean wx = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), WxBean.class);
						PayServant.getInstance().weChatPay2(
								PayActivity.this, wx.getAppid(),
								wx.getPartnerId(), wx.getPrepayId(), wx.getNonceStr(),
								wx.getTimeStamp(), wx.getPackageStr(), wx.getSign());
					} else if(payType.equals("1")) {
						if (TextUtils.isEmpty(packageId) && operate != null && operate.equals("1")) {
							user.setBanlance(Utils.jian(Utils.cheng(user.getBanlance(), "100"), detection.getVisitCost()));
						} else {
							user.setBanlance(Utils.jian(Utils.cheng(user.getBanlance(), "100"), total));
						}
						SharedPreferencesUtils.getInstance(PayActivity.this).saveObjectData(user);

						setResult(RESULT_OK);
						ToastUtil.showToast(PayActivity.this, "支付成功！");
						finish();
					}

				} else if (common.getInfo().contains("支付密码错误")) {
					DialogUtil.showMessageDg(PayActivity.this, "温馨提示", "忘记密码", "重新输入", "您输入的支付密码错误!", new CustomDialog.OnClickListener() {
						@Override
						public void onClick(CustomDialog dialog, int id, Object object) {
							dialog.dismiss();
							IntentUtil.gotoActivity(PayActivity.this, PayPwdActivity.class);
						}
					}, new CustomDialog.OnClickListener() {
						@Override
						public void onClick(CustomDialog dialog, int id, Object object) {
							dialog.dismiss();
							Bundle bundle = new Bundle();
							if (TextUtils.isEmpty(packageId)) {
								if (operate.equals("2")) {
									bundle.putString("money", Utils.chu(total, "100"));
								} else {
									if(detection != null){
										bundle.putString("money", Utils.chu(detection != null?detection.getVisitCost():"0", "100"));
									}

								}
							} else {
								bundle.putString("money", Utils.chu(total, "100"));
							}
							bundle.putString("title", "支付订单："+(detection != null?detection.getOrderId():"购买套餐"));
							IntentUtil.gotoActivityForResult(PayActivity.this, InputPayPwdActivity.class, bundle, 12302);
						}
					});
				} else {
					ToastUtil.showLongToast(PayActivity.this, common.getInfo());
				}
			}

			@Override
			public void returnException(Exception e, String msg) {
				LLog.eNetError(e.toString());
				ToastUtil.showLongToast(PayActivity.this, msg);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 12302 && resultCode == RESULT_OK) {
			pwd = data.getStringExtra("PWD");
			payType = "1";
			pay();
		}
	}

	public void paySuccess() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setResult(RESULT_OK);
				onBackPressed();
			}
		});
	}
	
	public void finish() {
	    super.finish();  
//	    //关闭窗体动画显示  
	    overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
	}
}
