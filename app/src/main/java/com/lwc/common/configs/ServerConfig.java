package com.lwc.common.configs;

import com.lwc.common.utils.SharedPreferencesUtils;

/**
 * 服务端配置类
 * 
 * @Description 此处定义服务器的链接地址配置和接口请求的方法，
 * @author CodeApe
 * @version 1.0
 * @date 2014年4月4日
 * @Copyright: Copyright (c) 2013 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 * 
 */
public class ServerConfig {

	/** 请求接口数据成功状态码 */
	public static final String RESPONSE_STATUS_SUCCESS = "1";

	/*************************** 服务器地址 ****************************/
	public static String DOMAIN = "https://47.97.217.243";
	//public final static String DOMAIN = "http://192.168.0.116";
	//public static String DOMAIN = "https://www.ls-mx.com";
	//正式服务器
	//public static String DOMAIN = "https://www.ls-mx.com";
	public static String SERVER_API_URL = DOMAIN +"/api";

	//测试服务器
	//	public static String SERVER_API_URL = "http://119.23.215.51/api";
//	 public final static String SERVER_API_URL = "http://192.168.2.166/api";
//	public final static String SERVER_API_URL = "http://192.168.0.53/api";
//	 public final static String SERVER_API_URL = "http://nanshen.com/api";
//	public final static String SERVER_API_URL = "https://www.lsh-sd.com/api";
//	public final static String SERVER_API_URL = "https://www.ls-mx.co54
// 	1111111111111111111111111111111111111111111111111111111111111111111m/api";//https://www.lucklyking.com
	//public static String RUL_IMG="http://oydarwpnz.bkt.clouddn.com/";//七牛图片
	public static String RUL_IMG="http://cdn.mixiu365.com/";//七牛图片

	/** 标识true */
	public static final String TRUE = "1";
	/** 标识false */
	public static final String FALSE = "0";

	/** 选择本地图片请求码 */
	public static final int REQUEST_CODE_SELECT_IMAGE = 29;

	/** 拍照获取图片请求码 */
	public static final int REQUEST_CODE_SELECT_PHOTOGRAPH = 10;
	/** 异步下载图片失败 */
	public static final int WHAT_ASYN_LOADIMAGE_ERROR = 1;
	/** 异步下载图片成功 */
	public static final int WHAT_ASYN_LOADIMAGE_SUCCESS = 2;
	/** 生成缩略图返回应答处理 */
	public static final int WHAT_COMPRESS_IMAGE_RESPONSE = 9;
	/** 图片预览请求码 */
	public static final int REQUEST_CODE_IMAGE_PREVIEW = 15;
	/** 压缩图片完毕完毕 */
	public static final int WHAT_COMPRESS_IMAGE_FINISH = 20;

}
