package com.lwc.common.module.setting.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.configs.ServerConfig;
import com.lwc.common.module.bean.User;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.LogUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.SpUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.utils.VersionUpdataUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity implements OnClickListener {

	@BindView(R.id.img_notifi)
	ImageView img_notifi;
	@BindView(R.id.img_voice)
	ImageView img_voice;
	@BindView(R.id.txt_vision)
	TextView txt_vision;
	@BindView(R.id.txtFeedback)
	TextView txtFeedback;
	@BindView(R.id.btnOutLogin)
	TextView btnOutLogin;
	@BindView(R.id.txtUserAgreement)
	TextView txtUserAgreement;
	@BindView(R.id.iv_red)
	ImageView iv_red;

	private User user;
	private SpUtil sp;
	private SharedPreferencesUtils preferencesUtils;
	private Dialog dialog;

	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_setting;
	}

	@Override
	protected void findViews() {
		ButterKnife.bind(this);
		preferencesUtils = SharedPreferencesUtils.getInstance(SettingActivity.this);
		sp = SpUtil.getSpUtil(this.getString(R.string.spkey_file_userinfo), Context.MODE_PRIVATE);
		setTitle("设置");
		img_notifi.setOnClickListener(this);
		img_voice.setOnClickListener(this);
		txt_vision.setOnClickListener(this);
		txtFeedback.setOnClickListener(this);
		btnOutLogin.setOnClickListener(this);
		txtUserAgreement.setOnClickListener(this);
		user = SharedPreferencesUtils.getInstance(this).loadObjectData(User.class);
		img_voice.setBackgroundResource(
				sp.getSPValue(SettingActivity.this.getString(R.string.spkey_file_is_ring) + user.getUserId(), true)
						? R.drawable.shezhi_anniu2 : R.drawable.shezhi_anniu1);

	}

	private void setNotifiImgBack(){
		boolean isOpened = false;
		try {
			isOpened = NotificationManagerCompat.from(this).areNotificationsEnabled();

			//Log.d("联网成功","进入setNotifiImgBack,状态为"+isOpened);
		} catch (Exception e) {
			e.printStackTrace();
			isOpened = false;
		}
		img_notifi.setBackgroundResource(isOpened ? R.drawable.shezhi_anniu2 : R.drawable.shezhi_anniu1);
	}

	@Override
	protected void onResume() {
		super.onResume();
		String versionCode = preferencesUtils.loadString("versionCode");
		if (versionCode != null && VersionUpdataUtil.isNeedUpdate(SettingActivity.this, Integer.parseInt(versionCode))){
			iv_red.setVisibility(View.VISIBLE);
		} else {
			iv_red.setVisibility(View.GONE);
		}

		//Log.d("联网成功","进入Resume");
		setNotifiImgBack();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_notifi:
			Intent intent = new Intent();
			if (Build.VERSION.SDK_INT >= 26) {
				// android 8.0引导
				intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
				intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
			} else if (Build.VERSION.SDK_INT >= 21) {
				// android 5.0-7.0
				intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
				intent.putExtra("app_package", getPackageName());
				intent.putExtra("app_uid", getApplicationInfo().uid);
			} else {
				// 其他
				intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
				intent.setData(Uri.fromParts("package", getPackageName(), null));
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		case R.id.img_voice:
			boolean spValue1 = sp.getSPValue(SettingActivity.this.getString(R.string.spkey_file_is_ring) + user.getUserId(), true);
			SpUtil.putSPValue(SettingActivity.this, SettingActivity.this.getString(R.string.spkey_file_userinfo), Context.MODE_PRIVATE,
					SettingActivity.this.getString(R.string.spkey_file_is_ring) + user.getUserId(),
					!spValue1);
			ToastUtil
					.showToast(SettingActivity.this,
							"声音震动提示" + (spValue1 ? "已关闭"
											: "已开启"));
			img_voice.setBackgroundResource(
					spValue1
							? R.drawable.shezhi_anniu1 : R.drawable.shezhi_anniu2);
			if (spValue1) {
				Utils.setNotification4(SettingActivity.this);
			} else if (!spValue1) {
				Utils.setNotification1(SettingActivity.this);
			}
			break;
		case R.id.txt_vision:
			IntentUtil.gotoActivity(SettingActivity.this, VesionActivity.class);
			break;
		case R.id.txtUserAgreement:
			Bundle bundle = new Bundle();
			bundle.putString("url", ServerConfig.DOMAIN.replace("https", "http")+"/main/h5/agreement.html");
			bundle.putString("title", "用户注册协议");
			IntentUtil.gotoActivity(this, InformationDetailsActivity.class, bundle);
			break;
		case R.id.txtFeedback:
			IntentUtil.gotoActivity(SettingActivity.this, SuggestActivity.class);
			break;
		case R.id.btnOutLogin:
			dialog = DialogUtil.dialog(SettingActivity.this, "温馨提示", null, null, "您确定要退出登录吗？", new OnClickListener() {
				@Override
				public void onClick(View v) {
					//UMShareAPI.get(SettingActivity.this).deleteOauth(SettingActivity.this, SHARE_MEDIA.QQ, authListener);
					//UMShareAPI.get(SettingActivity.this).deleteOauth(SettingActivity.this, SHARE_MEDIA.WEIXIN, authListener);
					//UMShareAPI.get(SettingActivity.this).deleteOauth(SettingActivity.this, SHARE_MEDIA.SINA, authListener);
					dialog.dismiss();
					setResult(RESULT_OK);
					onBackPressed();
				}
			}, null, true);
			break;

		default:
			break;
		}

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


	UMAuthListener authListener = new UMAuthListener() {
		/**
		 * @desc 授权开始的回调
		 * @param platform 平台名称
		 */
		@Override
		public void onStart(SHARE_MEDIA platform) {
			LogUtil.out("third_login","开始获取登录信息---->"+platform.name());
		}

		/**
		 * @desc 授权成功的回调
		 * @param platform 平台名称
		 * @param action 行为序号，开发者用不上
		 * @param data 用户资料返回
		 */
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			LogUtil.out("third_login","获取登录信息成功----》"+platform.name() + "action"+action);
		}

		/**
		 * @desc 授权失败的回调
		 * @param platform 平台名称
		 * @param action 行为序号，开发者用不上
		 * @param t 错误原因
		 */
		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			LogUtil.out("third_login","获取登录信息失败----》"+platform.name() + t.getMessage());
		}

		/**
		 * @desc 授权取消的回调
		 * @param platform 平台名称
		 * @param action 行为序号，开发者用不上
		 */
		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			LogUtil.out("third_login","取消了"+platform.name() + action);
		}
	};
}
