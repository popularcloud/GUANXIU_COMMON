/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lwc.common.module.zxing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.lwc.common.R;
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.controler.global.GlobalValue;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Malfunction;
import com.lwc.common.module.bean.Repairs;
import com.lwc.common.module.bean.RepairsCompany;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.mine.UserInfoActivity;
import com.lwc.common.module.repairs.ui.activity.ApplyForMaintainActivity;
import com.lwc.common.module.zxing.camera.CameraManager;
import com.lwc.common.module.zxing.decode.DecodeThread;
import com.lwc.common.module.zxing.utils.BeepManager;
import com.lwc.common.module.zxing.utils.CaptureActivityHandler;
import com.lwc.common.module.zxing.utils.InactivityTimer;
import com.lwc.common.module.zxing.utils.RGBLuminanceSource;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.PictureUtils;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.SystemInvokeUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a viewfinder to help the user place the barcode
 * correctly, shows feedback as the image processing is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 * 二维码
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback, OnClickListener {

	private static final String TAG = CaptureActivity.class.getSimpleName();

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;

	private SurfaceView scanPreview = null;
	private RelativeLayout scanContainer;
	private RelativeLayout scanCropView;
	private ImageView scanLine;
	/** 点击选取二维码相片 */
	private TextView openTv,tvQd;

	private Rect mCropRect = null;
	private Camera camera;
	private Camera.Parameters parameter;
	private Dialog dialog;
	private SharedPreferencesUtils preferencesUtils;
	private User user;

	public Handler getHandler() {
		return handler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	private boolean isHasSurface = false;

	/** 照片路径 */
	private String photo_path;

	private Bitmap scanBitmap;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		//隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//隐藏状态栏
		//定义全屏参数
		int flag=WindowManager.LayoutParams.FLAG_FULLSCREEN;
		//设置当前窗体为全屏显示
		window.setFlags(flag, flag);

		setContentView(R.layout.activity_zxing_capture);

		scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
		scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
		scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
		scanLine = (ImageView) findViewById(R.id.capture_scan_line);
		openTv = (TextView) findViewById(R.id.btn_to_photo);
		tvQd = (TextView) findViewById(R.id.tvQd);
		openTv.setOnClickListener(this);
		tvQd.setOnClickListener(this);

		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);

		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -0.5f, Animation.RELATIVE_TO_PARENT, 0.9f);
		animation.setDuration(4500);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.RESTART);
		scanLine.startAnimation(animation);
		preferencesUtils = SharedPreferencesUtils.getInstance(CaptureActivity.this);
		user = preferencesUtils.loadObjectData(User.class);
	}

	public void onBack(View view) {
		onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		cameraManager = new CameraManager(getApplication());
		handler = null;
		if (isHasSurface) {
			initCamera(scanPreview.getHolder());
		} else {
			scanPreview.getHolder().addCallback(this);
		}
		inactivityTimer.onResume();
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		beepManager.close();
		cameraManager.closeDriver();
		if (!isHasSurface) {
			scanPreview.getHolder().removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!isHasSurface) {
			isHasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isHasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	/**
	 * A valid barcode has been found, so give an indication of success and show the results.
	 *
	 * @param rawResult
	 *        The contents of the barcode.
	 *
	 * @param bundle
	 *        The extras
	 */
	public void handleDecode(Result rawResult, Bundle bundle) {
		inactivityTimer.onActivity();
		beepManager.playBeepSoundAndVibrate();
//		String matchUrl = "\\b(([\\w-]+://?|www[.])[^\\s()<>]+(?:\\([\\w\\d]+\\)|([^[:punct:]\\s]|/)))";
//		String data = rawResult.getText();
//		if (data.contains("&k=wx_first")) {
//			bundle.putString("meche_ID", data + "&app=true");
//		} else if (data.matches(matchUrl)) {
//			bundle.putInt("width", mCropRect.width());
//			bundle.putInt("height", mCropRect.height());
//			bundle.putString("result", data);
//			IntentUtil.gotoActivity(CaptureActivity.this, ResultActivity.class, bundle);
//		} else {
//			bundle.putInt("width", mCropRect.width());
//			bundle.putInt("height", mCropRect.height());
//			bundle.putString("result", data);
//			IntentUtil.gotoActivity(CaptureActivity.this, ResultActivity.class, bundle);
//		}
		bindCompany(rawResult.getText());
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			if (handler == null) {
				handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
			}
			initCrop();
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		// camera error
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage("相机打开出错，请稍后重试");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}

		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
	}

	public Rect getCropRect() {
		return mCropRect;
	}

	/**
	 * 初始化截取的矩形区域
	 */
	private void initCrop() {
		int cameraWidth = cameraManager.getCameraResolution().y;
		int cameraHeight = cameraManager.getCameraResolution().x;

		/** 获取布局中扫描框的位置信息 */
		int[] location = new int[2];
		scanCropView.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeight();

		int cropWidth = scanCropView.getWidth();
		int cropHeight = scanCropView.getHeight();

		/** 获取布局容器的宽高 */
		int containerWidth = scanContainer.getWidth();
		int containerHeight = scanContainer.getHeight();

		/** 计算最终截取的矩形的左上角顶点x坐标 */
		int x = cropLeft * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的左上角顶点y坐标 */
		int y = cropTop * cameraHeight / containerHeight;

		/** 计算最终截取的矩形的宽度 */
		int width = cropWidth * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的高度 */
		int height = cropHeight * cameraHeight / containerHeight;

		/** 生成最终的截取的矩形 */
		mCropRect = new Rect(x, y, width + x, height + y);
	}

	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tvQd) {
			SystemInvokeUtils.invokeMapDepot(this, GlobalValue.REPAIRS_PHOTO_REQUESTCODE0);
		} else if (v.getId() == R.id.btn_to_photo){
			if (isFlashlightOn()) {
				Closeshoudian();
				if (!isFlashlightOn()) {
					openTv.setText("开灯");
					openTv.setCompoundDrawables(null, Utils.getDrawable(this, R.drawable.no_open), null, null);
				}
			} else {
				Openshoudian();
				if (isFlashlightOn()) {
					openTv.setCompoundDrawables(null, Utils.getDrawable(this, R.drawable.open), null, null);
					openTv.setText("关灯");
				}
			}
		}
	}

	/**
	 * 调用相册之后返回图片
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case GlobalValue.REPAIRS_PHOTO_REQUESTCODE0:
				photo_path = PictureUtils.getPath(this, data); // android 4.4及以上获取uri问题
				if (photo_path == null) {
//					photo_path = Utils.getPath(getApplicationContext(),data.getData());
				}
				Result result = scanningImage(photo_path);
//				ToastUtil.showLongToast(CaptureActivity.this, "识别内容为："+result.getText());
				if (result != null) {
					bindCompany(result.getText());
				} else {
					ToastUtil.showLongToast(this, "无法识别的二维码");
				}
				break;
			}
		}
	}

	private void bindCompany(String uid) {
		String uidStr = "";
		if (TextUtils.isEmpty(uid) || (!uid.contains("?str=") && !uid.contains("bindOrderIndex=")) && !uid.contains("?codeId=")) {
			if (!TextUtils.isEmpty(uid) && uid.startsWith("http")) {
				Bundle bundle = new Bundle();
				bundle.putString("url", uid);
				bundle.putString("title", "扫描结果");
				IntentUtil.gotoActivityAndFinish(this, InformationDetailsActivity.class, bundle);
			} else if(!TextUtils.isEmpty(uid)) {
				dialog = DialogUtil.dialogBind(this, "", "二维码识别的内容："+uid,"", new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						finish();
					}
				});
			} else {
				ToastUtil.showLongToast(this, "无法识别的二维码");
				finish();
			}
			return;
		} else {
			if (uid.contains("?str=")) {
				uidStr = uid.substring(uid.indexOf("?str=") + 5, uid.length());
			}else if (uid.contains("bindOrderIndex=")) {
				uidStr = uid.substring(uid.indexOf("bindOrderIndex=") + 15, uid.length());
			}else if(uid.contains("?codeId=")){
				Log.d("联网成功","进入了else获取到的二维码数据:"+uid);
				uidStr = uid.substring(uid.indexOf("codeId=") + 7, uid.length());
			}
		}
		if (TextUtils.isEmpty(uidStr)) {
			ToastUtil.showLongToast(this, "无法识别的二维码");
			finish();
			return;
		}
		if (uid.contains("?str=")) {
			if (user != null && user.getIsSecrecy() != null && user.getIsSecrecy().equals("2")) {
				dialog = DialogUtil.dialogBind(this, "", "此功能仅限个人用户使用！","", new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						finish();
					}
				});
			} else if (user != null && !TextUtils.isEmpty(user.getParentCompanyName())){
				String msg = "您的账号已绑定过专属服务公司，如需更改绑定，请先进入密修APP 我的->个人资料->特约维修公司 进行解绑！";
				dialog = DialogUtil.dialog(CaptureActivity.this, "温馨提示", "去解绑", "取消", msg, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						IntentUtil.gotoActivityAndFinish(CaptureActivity.this, UserInfoActivity.class);
					}
				}, new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						finish();
					}
				}, false);
			} else {
				bindCompanyHttp(uidStr);
			}
		} else if (uid.contains("bindOrderIndex=")) {
			deviceScanCode(uidStr);
		} else if (uid.contains("codeId=")) {
			bindCode(uidStr);
		}
	}

	private void deviceScanCode(String uidStr) {
		Map<String, String> map = new HashMap<>();
		map.put("qrcodeIndex", uidStr);
		HttpRequestUtils.httpRequest(this, "deviceScanCode", RequestValue.SCAN_CODE, map, "GET", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String response) {
				Common common = JsonUtil.parserGsonToObject(response, Common.class);
				switch (common.getStatus()) {
					case "1":
						try {
							if (!response.contains("data")) {
								dialog = DialogUtil.dialogBind(CaptureActivity.this, "", "该二维码还未绑定设备！","", new View.OnClickListener(){
									@Override
									public void onClick(View v) {
										dialog.dismiss();
										finish();
									}
								});
							} else {
								JSONObject object = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
								String deviceTypeId = object.optString("deviceTypeId");
								String deviceTypeName = object.optString("deviceTypeName");
								String reqairId = object.optString("reqairId");
								String reqairnName = object.optString("reqairName");
								int isSecrecy = object.optInt("isSecrecy");
								String orderCompanyId = object.optString("orderCompanyId");
								String orderCompanyName = object.optString("orderCompanyName");
								String qrcodeIndex = object.optString("qrcodeIndex");
								Repairs repairs = new Repairs(deviceTypeId, deviceTypeName, "");
								Malfunction malfunction = new Malfunction(reqairId, reqairnName);
								RepairsCompany rc = new RepairsCompany();
								rc.setCompanyId(orderCompanyId);
								rc.setCompanyName(orderCompanyName);
								Bundle bundle = new Bundle();
//								bundle.putSerializable("malfunction", malfunction);
								bundle.putSerializable("repairs", repairs);
								bundle.putSerializable("repairsCompany", rc);
								bundle.putInt("isSecrecy", isSecrecy);
								bundle.putString("qrcodeIndex", qrcodeIndex);
								IntentUtil.gotoActivityAndFinish(CaptureActivity.this, ApplyForMaintainActivity.class, bundle);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						break;
					default:
						finish();
						ToastUtil.showLongToast(CaptureActivity.this, common.getInfo());
						break;
				}
			}
			@Override
			public void returnException(Exception e, String msg) {
				LLog.eNetError(e.toString());
				ToastUtil.showLongToast(CaptureActivity.this, msg);
			}
		});
	}


	private void bindCode(String uidStr) {
		Map<String, String> map = new HashMap<>();
		map.put("codeId", uidStr);
		HttpRequestUtils.httpRequest(this, "绑定邀请二维码", RequestValue.GET_USER_BINDASSOCIATIONINFO, map, "POST", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String response) {
				Common common = JsonUtil.parserGsonToObject(response, Common.class);
				switch (common.getStatus()) {
					case "1":
							ToastUtil.showLongToast(CaptureActivity.this, common.getInfo());
							finish();
						break;
					default:
						ToastUtil.showLongToast(CaptureActivity.this, common.getInfo());
						finish();
						break;
				}
			}

			@Override
			public void returnException(Exception e, String msg) {
				LLog.eNetError(e.toString());
				ToastUtil.showLongToast(CaptureActivity.this, msg);
				finish();
			}
		});
	}

	private void bindCompanyHttp(String uidStr) {
		Map<String, String> map = new HashMap<>();
		map.put("str", uidStr);
		HttpRequestUtils.httpRequest(this, "bindCompany", RequestValue.BIND_COMPANY, map, "POST", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String response) {
				Common common = JsonUtil.parserGsonToObject(response, Common.class);
				switch (common.getStatus()) {
					case "1":
						try {
							JSONObject object = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
							Intent data = new Intent();
							data.putExtra("companyName", object.optString("companyName"));
							setResult(RESULT_OK, data);
							finish();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						break;
					default:
						finish();
						ToastUtil.showLongToast(CaptureActivity.this, common.getInfo());
						break;
				}
			}

			@Override
			public void returnException(Exception e, String msg) {
				LLog.eNetError(e.toString());
				ToastUtil.showLongToast(CaptureActivity.this, msg);
			}
		});
	}

	/**
	 * 扫描获取的图片
	 *
	 * @version 1.0
	 * @createTime 2018年1月8日,下午2:13:03
	 * @updateTime 2015年1月8日,下午2:13:03
	 * @createAuthor 何栋
	 * @updateAuthor 何栋
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 *
	 * @param path
	 * @return
	 */
	protected Result scanningImage(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		// DecodeHintType 和EncodeHintType
		Hashtable<DecodeHintType, String> hints = new Hashtable<>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); // 设置二维码内容的编码
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		int sampleSize = (int) (options.outHeight / (float) 300);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);

		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		} catch (com.google.zxing.NotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对result数据进行中文乱码处理
	 *
	 * @version 1.0
	 * @createTime 2015年1月8日,下午2:13:20
	 * @updateTime 2015年1月8日,下午2:13:20
	 * @createAuthor 何栋
	 * @updateAuthor 何栋
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 *
	 * @param str
	 * @return
	 */
	private String recode(String str) {
		String formart = "";
		try {
			boolean ISO = Charset.forName("ISO-8859-1").newEncoder().canEncode(str);
			if (ISO) {
				formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
			} else {
				formart = str;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return formart;
	}

	public void Openshoudian() {
		//异常处理一定要加，否则Camera打开失败的话程序会崩溃
		try {
			Log.d("smile","camera打开");
			camera = cameraManager.getCamera();
		} catch (Exception e) {
			Log.d("smile","Camera打开有问题");
			ToastUtil.showLongToast(this, "Camera被占用，请先关闭");
		}
		if(camera != null)
		{
			//打开闪光灯
			camera.startPreview();
			parameter = camera.getParameters();
			parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			camera.setParameters(parameter);
			Log.d("smile","闪光灯打开");
		}
	}
	public void Closeshoudian()
	{
		if (camera != null)
		{
			//关闭闪光灯
			Log.d("smile", "closeCamera()");
			parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			camera.setParameters(parameter);
		}
	}

	/*    *
     * 是否开启了闪光灯
     * @return
     */
	public boolean isFlashlightOn() {
		try {
			Camera.Parameters parameters = camera.getParameters();
			String flashMode = parameters.getFlashMode();
			if (flashMode.equals(android.hardware.Camera.Parameters.FLASH_MODE_TORCH)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
}