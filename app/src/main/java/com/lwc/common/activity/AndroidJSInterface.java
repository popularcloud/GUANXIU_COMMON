package com.lwc.common.activity;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.lwc.common.module.login.ui.LoginActivity;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

/**
 * webview 与 js 交互
 * @author 何栋
 * @date 2018-01-10
 */
public class AndroidJSInterface {
	private Context mContext;
	private String repairsId;
	public AndroidJSInterface(Context context, String repairs){
		mContext = context;
		this.repairsId = repairs;
	}

	/**
	 * js调用该方法弹出传入提示信息用原生弹出窗提示
	 * @param msg
     */
	@JavascriptInterface
    public void showToast(String msg) {
		ToastUtil.showToast(mContext, msg);
    }

	/**
	 * js调用该方法获取手机app的token等登录信息
	 * @return
     */
	@JavascriptInterface
	public String getToken() {
		return SharedPreferencesUtils.getInstance(mContext).loadString("token")+"_"+ Utils.getDeviceId(mContext)+"_"+"ANDROID";
	}

	/**
	 * 回到登录页面
	 */
	@JavascriptInterface
	public void gotoLogin() {
		IntentUtil.gotoActivity(mContext, LoginActivity.class);
	}

	/**
	 * js调用该方法获取手机app的token,维修类型id等信息
	 * @return
	 */
	@JavascriptInterface
	public String getTokenAndRepairsId() {
		return SharedPreferencesUtils.getInstance(mContext).loadString("token")+"_"+ Utils.getDeviceId(mContext)+"_"+"ANDROID"+"_"+repairsId;
	}

	/**
	 * js调用该方法，传入分享所需要的参数
	 * @param shareTitle
	 * @param sharePhoto
	 * @param flink
	 * @param shareDes
     * @param activityId
     */
	@JavascriptInterface
	public void setShare(String shareTitle, String sharePhoto, String flink, String shareDes, String activityId) {
		SharedPreferencesUtils.getInstance(mContext).saveString("shareTitle", shareTitle);
		SharedPreferencesUtils.getInstance(mContext).saveString("sharePhoto", sharePhoto);
		SharedPreferencesUtils.getInstance(mContext).saveString("flink", flink);
		SharedPreferencesUtils.getInstance(mContext).saveString("shareDes", shareDes);
		SharedPreferencesUtils.getInstance(mContext).saveString("activityId", activityId);
//		SharedPreferencesUtils.getInstance(mContext).saveString("checkShare", checkShare);
	}

	/**
	 * js调用该方法，传入分享所需要的参数
	 * @param shareTitle
	 * @param sharePhoto
	 * @param flink
	 * @param shareDes
	 * @param activityId
	 */
	@JavascriptInterface
	public void showShare(String shareTitle, String sharePhoto, String flink, String shareDes, String activityId) {
		SharedPreferencesUtils.getInstance(mContext).saveString("shareTitle", shareTitle);
		SharedPreferencesUtils.getInstance(mContext).saveString("sharePhoto", sharePhoto);
		SharedPreferencesUtils.getInstance(mContext).saveString("flink", flink);
		SharedPreferencesUtils.getInstance(mContext).saveString("shareDes", shareDes);
		SharedPreferencesUtils.getInstance(mContext).saveString("activityId", activityId);
		if (InformationDetailsActivity.activity != null) {
			InformationDetailsActivity.activity.doOauthVerify();
		}
	}
	
	@JavascriptInterface
	public String getInterface(){
		return "android_js_interface";
	}

}
