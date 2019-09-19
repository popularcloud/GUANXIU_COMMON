package com.lwc.common.utils;

import android.util.Base64;

/**
 * 加密解密的工具
 *
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 
 */
public class DecodeUtils {
	/**
	 * 
	 * 加密
	 * 
	 * 
	 * 
	 * @param targetStr
	 * 
	 * @return
	 */
	public static String encode(String targetStr) {
		return Base64.encodeToString(targetStr.getBytes(), Base64.DEFAULT);
	}

	/**
	 * 
	 * 解密
	 * 
	 * 
	 * 
	 * @param targetStr
	 * 
	 * @return
	 */
	public static String decode(String targetStr) {
		return new String(Base64.decode(targetStr.getBytes(), Base64.DEFAULT));
	}
}
