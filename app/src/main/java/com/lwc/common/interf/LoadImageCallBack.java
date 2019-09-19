package com.lwc.common.interf;

import android.graphics.Bitmap;

/**
 * 异步加载图片回调接口
 *
 * @version 1.0
 * @date 2013-4-20
 *
 */
public interface LoadImageCallBack {

	/**
	 * 加载图片完成，返回
	 *
	 * @version 1.0
	 * @date 2013-4-20
	 *
	 * @param bitmap
	 */
	public void onCallBack(Bitmap bitmap);
	
}
