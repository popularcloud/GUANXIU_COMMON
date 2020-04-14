package com.lwc.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.Toast;

import com.lwc.common.R;
import com.lwc.common.controler.global.GlobalValue;
import com.lwc.common.widget.SelectPhotoDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

/**
 * Toast操作工具类
 * 
 * @author 何栋
 * @date 2013-03-19
 * @version 1.0.0
 * 
 */
public class ToastUtil {

	private static Toast toast;

	/**
	 * 显示选择图片
	 * @param context
	 */
	public static void showPhotoSelect(final Activity context, final int count) {
		SelectPhotoDialog picturePopupWindow = new SelectPhotoDialog(context, new SelectPhotoDialog.CallBack() {
			@Override
			public void oneClick() {
				Intent openCameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
//				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				context.startActivityForResult(openCameraIntent, GlobalValue.REPAIRS_CAMERA_REQUESTCODE0);
			}

			@Override
			public void twoClick() {
					Matisse.from(context)
							.choose(MimeType.ofImage())//照片视频全部显示
							.countable(true)//有序选择图片
							.maxSelectable(count)//最大选择数量为9
							.gridExpectedSize(180)//图片显示表格的大小getResources()
//						.getDimensionPixelSize(R.dimen.grid_expected_size)
							.restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)//图像选择和预览活动所需的方向。
							.thumbnailScale(0.95f)//缩放比例
							.theme(R.style.Matisse_Zhihu)//主题  暗色主题 R.style.Matisse_Dracula
							.imageEngine(new GlideEngine())//加载方式
							.forResult(GlobalValue.REPAIRS_PHOTO_REQUESTCODE0);//请求码
			}

			@Override
			public void cancelCallback() {
			}
		}, "拍照", "手机相册");// 拍照弹出框
		picturePopupWindow.show();
	}

	/**
	 * 显示选择图片
	 * @param context
	 */
	public static void showPhotoSelect(final Activity context) {
		SelectPhotoDialog picturePopupWindow = new SelectPhotoDialog(context, new SelectPhotoDialog.CallBack() {
			@Override
			public void oneClick() {
				Intent openCameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
//				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				context.startActivityForResult(openCameraIntent, GlobalValue.REPAIRS_CAMERA_REQUESTCODE0);
			}

			@Override
			public void twoClick() {
				SystemInvokeUtils.invokeMapDepot(context, GlobalValue.REPAIRS_PHOTO_REQUESTCODE0);
			}

			@Override
			public void cancelCallback() {
			}
		}, "拍照", "手机相册");// 拍照弹出框
		picturePopupWindow.show();
	}

	/**
	 * 显示提示信息
	 * 
	 * @version 1.0
	 * @date 2013-03-19
	 * @param text
	 *        提示内容
	 */
	public static void showToast(Context context, String text) {
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER,0,0);
		} else {
			toast.setText(text);
		}
		toast.show();

	}

	public static void showLongToast(Context context, String text) {
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER,0,0);
		} else {
			toast.setText(text);
		}
		toast.show();

	}

	public static void showNetErr(Context context) {
		if (toast == null) {
			toast = Toast.makeText(context, "网络请求错误,请检查网络是否正常!", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER,0,0);
		} else {
			toast.setText("网络请求错误,请检查网络是否正常！");
		}
		toast.show();

	}

	public static void showNetMsg(Context context, String msg) {
		if (toast == null) {
			toast = Toast.makeText(context, "网络请求错误!", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER,0,0);
		} else {
			toast.setText("网络请求错误");
		}
		if (msg != null && !msg.equals("")) {
			toast.setText(msg);
		}
		toast.show();
	}
}
