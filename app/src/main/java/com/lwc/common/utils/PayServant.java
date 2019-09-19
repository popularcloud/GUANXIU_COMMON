package com.lwc.common.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.lwc.common.view.AliPayCallBack;
import com.lwc.common.view.PayResult;
import com.lwc.common.wxapi.WXContants;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

/**
 * Created by yuqianli on 15/11/26.
 */
public class PayServant {
	private static PayServant ourInstance = new PayServant();

	public static PayServant getInstance() {
		return ourInstance;
	}

	private PayServant() {
	}

//	/**
//	 * check whether the device has authentication alipay account.
//	 * 查询终端设备是否存在支付宝认证账户
//	 */
//	public void aliPayCheckAccount(final Activity activity,
//			final AliPayCheckCallBack callBack) {
//		ThreadUtils.runOnNonUIthread(new Runnable() {
//			@Override
//			public void run() {
//				// 构造PayTask 对象
//				PayTask payTask = new PayTask(activity);
//				// 调用查询接口，获取查询结果
//				boolean isExist = payTask.checkAccountIfExist();
//				callBack.OnCheckAccountExist(isExist);
//			}
//		});
//	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo(String order_sn, String zhifufangshi) {
		Random r = new Random();
		return order_sn + "_" + r.nextInt(99999) + "_" + zhifufangshi + "_ise";
	}

	/**
	* call alipay sdk pay. 调用SDK支付
	*
	 * @param activity
	* @param callback
	*/
	public void aliPay(JSONObject data, final Activity activity,
					   final AliPayCallBack callback) {
		try {
			final String orderStr = data.optString("orderStr");
			// ＊＊＊发起支付请求
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					// 构造PayTask 对象
					PayTask alipay = new PayTask(activity);
					Map<String, String> result = alipay.payV2(orderStr, true);
					// 调用支付接口，获取支付结果
					PayResult payResult = new PayResult(result);
					/**
					 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
					 */
//					String resultInfo = payResult.getResult();// 同步返回需要验证的信息
					final String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为9000则代表支付成功
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (TextUtils.equals(resultStatus, "9000")) {
								// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
								ToastUtil.showLongToast(activity, "支付成功");
								callback.OnAliPayResult(true, false, "支付成功");
							} else {
								callback.OnAliPayResult(false, false, "支付失败");
								// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
								ToastUtil.showLongToast(activity, "支付失败");
							}
						}
					});
				}
			};
			ExecutorServiceUtil.getInstance().execute(runnable);
		} catch (Exception e) {
			LLog.e("payservant", e.getMessage());
		}
	}

//	/**
//	 * call alipay sdk pay. 调用SDK支付
//	 *
//	 * @param activity
//	 * @param outTradeNo
//	 *            订单号后台生成即可
//	 * @param callback
//	 * @param subject
//	 *            商品名
//	 * @param body
//	 *            商品描述
//	 * @param priceTV
//	 *            商品价格
//	 */
//	public void aliPay(JSONObject data, final Activity activity,
//			final AliPayCallBack callback) {
//
//		try {
//			// ＊＊＊生成订单
//			String orderInfo = "partner=" + "\"" + data.getString("partner")
//					+ "\"";// 签约合作者身份ID
//			orderInfo += "&seller_id=" + "\"" + data.getString("seller_id")
//					+ "\"";// 签约卖家支付宝账号
//			orderInfo += "&out_trade_no=" + "\""
//					+ data.getString("out_trade_no") + "\"";// 商户网站唯一订单号
//			orderInfo += "&subject=" + "\"" + data.getString("subject") + "\"";// 商品名称
//			orderInfo += "&body=" + "\"" + data.getString("body") + "\"";// 商品详情
//			orderInfo += "&total_fee=" + "\"" + data.getString("total_fee")
//					+ "\"";// 商品金额
//			orderInfo += "&notify_url=" + "\"" + data.getString("notify_url")
//					+ "\"";// 服务器异步通知页面路径
//			orderInfo += "&service=\"" + data.getString("service") + "\"";// 服务接口名称，
//																			// 固定值
//			orderInfo += "&payment_type=\"" + data.getString("payment_type")
//					+ "\"";// 支付类型， 固定值
//			orderInfo += "&_input_charset=\""
//					+ data.getString("_input_charset") + "\"";// 参数编码， 固定值
//			// 设置未付款交易的超时时间
//			// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
//			// 取值范围：1m～15d。
//			// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
//			// 该参数数值不接受小数点，如1.5h，可转换为90m。
//			orderInfo += "&it_b_pay=\"" + data.getString("it_b_pay") + "\"";
//			// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
//			// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
//			// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//			// orderInfo += "&return_url=\""+return_url+"\"";
//			// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
//			// orderInfo += "&paymethod=\"expressGateway\"";
//
//			// ＊＊＊对订单做RSA 签名
//			String sign = data.getString("sign");
//			Log.d("test", "no encode sign:"+sign);
//			// 仅需对sign 做URL编码
////			sign = URLEncoder.encode(sign, "UTF-8");
//			Log.d("test", "encode sign:"+sign);
//			final String payInfo = orderInfo + "&sign=\"" + sign
//					+ "\"&sign_type=\"" + data.getString("sign_type") + "\"";
//			// ＊＊＊完整的符合支付宝参数规范的订单信息
//
//			// ＊＊＊发起支付请求
//			ThreadUtils.runOnNonUIthread(new Runnable() {
//				@Override
//				public void run() {
//					// 构造PayTask 对象
//					PayTask alipay = new PayTask(activity);
//					// 调用支付接口，获取支付结果
//					AliPayResult payResult = new AliPayResult(alipay
//							.pay(payInfo));
//					// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
////					String resultInfo = payResult.getResult();
//					String resultStatus = payResult.getResultStatus();
//					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
//					if (TextUtils.equals(resultStatus, "9000")) {
//						// 成功支付
//						callback.OnAliPayResult(true, false, "支付成功");
//					} else {
//						// 判断resultStatus 为非“9000”则代表可能支付失败
//						// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
//						if (TextUtils.equals(resultStatus, "8000")) {
//							// 支付结果确认中
//							callback.OnAliPayResult(false, true, "支付结果确认中");
//						} else {
//							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//							callback.OnAliPayResult(false, false, "支付失败");
//						}
//					}
//				}
//			});
//		} catch (Exception e) {
//			MyLog.e("payservant", e.getMessage());
//		}
//
//	}

//	/**
//	 * call alipay sdk pay. 调用SDK支付
//	 *
//	 * @param activity
//	 * @param outTradeNo
//	 *            订单号后台生成即可
//	 * @param callback
//	 * @param subject
//	 *            商品名
//	 * @param body
//	 *            商品描述
//	 * @param price
//	 *            商品价格
//	 */
//	public void aliPay(String subject, String body, String price,
//			final Activity activity, String outTradeNo,
//			final AliPayCallBack callback) {
//		if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
//				|| TextUtils.isEmpty(SELLER)) {
//			throw new RuntimeException("支付宝相关参数未设置");
//		}
//		// ＊＊＊生成订单
//		String orderInfo = "partner=" + "\"" + PARTNER + "\"";// 签约合作者身份ID
//		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";// 签约卖家支付宝账号
//		orderInfo += "&out_trade_no=" + "\"" + outTradeNo + "\"";// 商户网站唯一订单号
//		orderInfo += "&subject=" + "\"" + subject + "\"";// 商品名称
//		orderInfo += "&body=" + "\"" + body + "\"";// 商品详情
//		orderInfo += "&total_fee=" + "\"" + price + "\"";// 商品金额
//		orderInfo += "&notify_url=" + "\""
//				+ "http://www.1topnet.com/v1/test/alipay" + "\"";// 服务器异步通知页面路径http://notify.msp.hk/notify.htm
//		orderInfo += "&service=\"mobile.securitypay.pay\"";// 服务接口名称， 固定值
//		orderInfo += "&payment_type=\"1\"";// 支付类型， 固定值
//		orderInfo += "&_input_charset=\"utf-8\"";// 参数编码， 固定值
//		// 设置未付款交易的超时时间
//		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
//		// 取值范围：1m～15d。
//		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
//		// 该参数数值不接受小数点，如1.5h，可转换为90m。
//		orderInfo += "&it_b_pay=\"30m\"";
//		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
//		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
//		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//		orderInfo += "&return_url=\"m.alipay.com\"";
//		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
//		// orderInfo += "&paymethod=\"expressGateway\"";
//		Log.d("test", "orderInfo:" + orderInfo);
//
//		// ＊＊＊对订单做RSA 签名
//		String sign = AliSignUtils.sign(orderInfo, RSA_PRIVATE);
//		try {
//			// 仅需对sign 做URL编码
//			sign = URLEncoder.encode(sign, "UTF-8");
//			Log.d("test", "sign:" + sign);
//		} catch (UnsupportedEncodingException e) {
//			MyLog.e("payservant", e.getMessage());
//		}
//
//		// ＊＊＊完整的符合支付宝参数规范的订单信息
//		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
//				+ SIGN_TYPE;
//
//		// ＊＊＊发起支付请求
//		ThreadUtils.runOnNonUIthread(new Runnable() {
//			@Override
//			public void run() {
//				// 构造PayTask 对象
//				PayTask alipay = new PayTask(activity);
//				// 调用支付接口，获取支付结果
//				AliPayResult payResult = new AliPayResult(alipay.pay(payInfo));
//				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
////				String resultInfo = payResult.getResult();
//				String resultStatus = payResult.getResultStatus();
//				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
//				if (TextUtils.equals(resultStatus, "9000")) {
//					// 成功支付
//					callback.OnAliPayResult(true, false, "支付成功");
//				} else {
//					// 判断resultStatus 为非“9000”则代表可能支付失败
//					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
//					if (TextUtils.equals(resultStatus, "8000")) {
//						// 支付结果确认中
//						callback.OnAliPayResult(false, true, "支付结果确认中");
//					} else {
//						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//						callback.OnAliPayResult(false, false, "支付失败");
//					}
//				}
//			}
//		});
//	}

	// test
//	public void weChatPay(Activity activity) {
//		IWXAPI api = WXAPIFactory.createWXAPI(activity, "wxb4ba3c02aa476ea1");
//		String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
//		Toast.makeText(activity, "获取订单中...", Toast.LENGTH_SHORT).show();
//		try {
//			byte[] buf = WechatUtil.httpGet(url);
//			if (buf != null && buf.length > 0) {
//				String content = new String(buf);
//				Log.e("get server pay params:", content);
//				JSONObject json = new JSONObject(content);
//				if (!json.has("retcode")) {
//					PayReq req = new PayReq();
//					// req.appId = "wxf8b4f85f3a794e77"; // 测试用appId
//					req.appId = json.getString("appid");
//					req.partnerId = json.getString("partnerid");
//					req.prepayId = json.getString("prepayid");
//					req.nonceStr = json.getString("noncestr");
//					req.timeStamp = json.getString("timestamp");
//					req.packageValue = json.getString("package");
//					req.sign = json.getString("sign");
//					req.extData = "app data"; // optional
//					Toast.makeText(activity, "正常调起支付", Toast.LENGTH_SHORT)
//							.show();
//					// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//					api.sendReq(req);
//				} else {
//					Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
//					Toast.makeText(activity, "返回错误" + json.getString("retmsg"),
//							Toast.LENGTH_SHORT).show();
//				}
//			} else {
//				Log.d("PAY_GET", "服务器请求错误");
//				Toast.makeText(activity, "服务器请求错误", Toast.LENGTH_SHORT).show();
//			}
//		} catch (Exception e) {
//			Log.e("PAY_GET", "异常：" + e.getMessage());
//			Toast.makeText(activity, "异常：" + e.getMessage(), Toast.LENGTH_SHORT)
//					.show();
//		}
//	}

	public void weChatPay2(Activity activity, String appid, String partnerid,
			String prepayid, String noncestr, String timestamp,
			String packageValue, String sign) {
		IWXAPI api = WXAPIFactory.createWXAPI(activity, null);
		// String url =
		// "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
//		Toast.makeText(activity, "获取订单中...", Toast.LENGTH_SHORT).show();
		try {
			// byte[] buf = WechatUtil.httpGet(url);
			// if (buf != null && buf.length > 0) {
			// String content = new String(buf);
			// Log.e("get server pay params:", content);
			// JSONObject json = new JSONObject(content);
			// if (!json.has("retcode")) {
			PayReq req = new PayReq();
			// req.appId = "wxf8b4f85f3a794e77"; // 测试用appId
			req.appId = appid;
			req.partnerId = partnerid;
			req.prepayId = prepayid;
			req.nonceStr = noncestr;
			req.timeStamp = timestamp;
			req.packageValue = packageValue;
			req.sign = sign;
			// req.extData = "app data"; // optional
//			Toast.makeText(activity, "正常调起支付", Toast.LENGTH_SHORT).show();
			// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
			api.registerApp(WXContants.APP_ID);
			api.sendReq(req);
			// } else {
			// Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
			// Toast.makeText(activity, "返回错误" + json.getString("retmsg"),
			// Toast.LENGTH_SHORT).show();
			// }
			// } else {
			// Log.d("PAY_GET", "服务器请求错误");
			// Toast.makeText(activity, "服务器请求错误", Toast.LENGTH_SHORT).show();
			// }
		} catch (Exception e) {
			Log.e("PAY_GET", "异常：" + e.getMessage());
			ToastUtil.showLongToast(activity, "异常：" + e.getMessage());
		}
	}

//	/**
//	 *
//	 * @param activity
//	 * @param appID
//	 *            appid
//	 * @param partnerID
//	 *            商户号
//	 * @param prepayID
//	 *            订单id
//	 *            商户平台key
//	 */
//	public void weChatPay(Activity activity, String appID, String partnerID,
//			String prepayID, String sign) {
//		IWXAPI api = WXAPIFactory.createWXAPI(activity, appID);
//		api.registerApp(appID);
//
//		PayReq request = new PayReq();
//		request.appId = appID;
//		request.partnerId = partnerID;
//		request.prepayId = prepayID;
//		request.packageValue = "Sign=WXPay";
//		request.nonceStr = RandomUtils.generateString(32);
//		request.timeStamp = String.valueOf(System.nanoTime());
//		//
//		// SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
//		// parameters.put("appid", request.appId);
//		// parameters.put("partnerid", request.partnerId);
//		// parameters.put("prepayid", request.prepayId);
//		// parameters.put("package", request.packageValue);
//		// parameters.put("noncestr", request.nonceStr);
//		// parameters.put("timestamp", request.timeStamp);
//
//		request.sign = sign;// WXUtil.createSign("UTF-8",
//							// "b18d26198723914a5a0121c0cfcab9ed", parameters);
//		api.sendReq(request);
//	}

//	/**
//	 *
//	 * @param activity
//	 * @param orderStr
//	 *            生成的订单字符串
//	 * @param callBack
//	 */
//	public void aliPay(final Activity activity, final String orderStr,
//			final AliPayCallBack callBack) {
//		ThreadUtils.runOnNonUIthread(new Runnable() {
//			@Override
//			public void run() {
//				PayTask payTask = new PayTask(activity);
//				// 调用支付接口，获取支付结果
//				AliPayResult payResult = new AliPayResult(payTask.pay(orderStr));
//				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
//				String resultInfo = payResult.getResult();
//				String resultStatus = payResult.getResultStatus();
//				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
//				if (TextUtils.equals(resultStatus, "9000")) {
//					// 成功支付
//					callBack.OnAliPayResult(true, false, resultInfo);
//				} else {
//					// 判断resultStatus 为非“9000”则代表可能支付失败
//					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
//					if (TextUtils.equals(resultStatus, "8000")) {
//						// 支付结果确认中
//						callBack.OnAliPayResult(false, true, resultInfo);
//					} else {
//						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//						callBack.OnAliPayResult(false, false, resultInfo);
//					}
//				}
//			}
//		});
//	}
}
