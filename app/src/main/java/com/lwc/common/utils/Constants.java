package com.lwc.common.utils;


import android.os.Environment;

import com.amap.api.maps.model.LatLng;

public class Constants {

	public static final int ERROR = 1001;// 网络异常
	public static final int ROUTE_START_SEARCH = 2000;
	public static final int ROUTE_END_SEARCH = 2001;
	public static final int ROUTE_BUS_RESULT = 2002;// 路径规划中公交模式
	public static final int ROUTE_DRIVING_RESULT = 2003;// 路径规划中驾车模式
	public static final int ROUTE_WALK_RESULT = 2004;// 路径规划中步行模式
	public static final int ROUTE_NO_RESULT = 2005;// 路径规划没有搜索到结果

	public static final int GEOCODER_RESULT = 3000;// 地理编码或者逆地理编码成功
	public static final int GEOCODER_NO_RESULT = 3001;// 地理编码或者逆地理编码没有数据

	public static final int POISEARCH = 4000;// poi搜索到结果
	public static final int POISEARCH_NO_RESULT = 4001;// poi没有搜索到结果
	public static final int POISEARCH_NEXT = 5000;// poi搜索下一页

	public static final int BUSLINE_LINE_RESULT = 6001;// 公交线路查询
	public static final int BUSLINE_id_RESULT = 6002;// 公交id查询
	public static final int BUSLINE_NO_RESULT = 6003;// 异常情况

	public static final LatLng BEIJING = new LatLng(39.90403, 116.407525);// 北京市经纬度
	public static final LatLng DONGWAN = new LatLng(113.747409, 23.052918);// 东莞市经纬度
	public static final LatLng ZHONGGUANCUN = new LatLng(39.983456, 116.3154950);// 北京市中关村经纬度
	public static final LatLng SHANGHAI = new LatLng(31.238068, 121.501654);// 上海市经纬度
	public static final LatLng FANGHENG = new LatLng(39.989614, 116.481763);// 方恒国际中心经纬度
	public static final LatLng CHENGDU = new LatLng(30.679879, 104.064855);// 成都市经纬度
	public static final LatLng XIAN = new LatLng(34.341568, 108.940174);// 西安市经纬度
	public static final LatLng ZHENGZHOU = new LatLng(34.7466, 113.625367);// 郑州市经纬度

	public static  final String RSA_PATCH = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDB7XkG0hycy+0fdScp5swe9SsHcTo8FgSz6j3rL6H4UqemDD5gYBWTduEtp1JqJFGJeiyoDh/PmciMi79h/5GC4lVjhO4Lexg8BxRhUKoqBiusd7Z+k5gJm7CVbpyaLHekGnb845taPUUg+5JAa74N4D+kEZpkxzKnwEd/r2ptptQVTxA+Ti4lf3baTjSuQIK9KIjR8Ly7pj+PXq0C7B0H24jjXIhHB0lozwACOR0GSgHDhrbv0GXFhqDhPGxEKi7nHl0shSNj0X5xDlZDVGVynVwl9xynJNDtfGe+0p0hP8s9ZMZYhrAy/2z6E2meYvtm6pafpmQWS1wZVHBaAAaxAgMBAAECggEATXfT1QmyLGC6RMrD7lUoTWWa3so4fTA+iLCWM5E/nrdGCWrysAUyGUpwAZDJt2TyvXx0wl2CD6hbGOnqdfdl5WcMvUio3yU5NSGO3qEemNMGN0tLN502KwBqtgcNv/jrgj6gMb2qnY3EQ7y7W8iuj1R5l80AZCm+7ow9A6DLXymHfpJf49f2dY8X7UFX34JiMGwVgUsHNA7J2Y/yQJr7QPaA1LJjM13a0fXam6QhtknhdEZ1MUeYR0fqhIVhE7lDRH3lHq2DjedSuuV28zhpjVj+f6onEQAu8OolLaHe/KDbm1foTcYnr+wW0r4dJ3EDX/T0GiA/UDeySea5y4cW0QKBgQDn65Jd918SJdzw/i5/6UPBFOTjdBy/qhZBY959tqiOEhtD39uZECOWNflZBvcLZiJAJo6EHphpf7FwfQ2tTGSKoglepkY6CKGyJ/P+j66Sjfu0Ek05N7eBT0VN4WU/KB+mizsfThn87zIKxL4B/D0GLIHEp2tMnNjiFEa8TNQrgwKBgQDWEA9l57t+5w5iYVVH8kxWJSHpNy2r9JlIhZn2a8cPfRFVwyFDVMMYx2viB1tItpxRaeNCHQN6vSDHScgigHO1RGfcVVPk0x7lBd1EkYHy6PzBEGkq3Yotdf7fvnVXGpHER0T1GHFDJPGTgNBrN3azT9cy5xh2Ph8Ft/1HSwBquwKBgB8Wi+M+sqbLHofLes44pZVvSJNY0DW9Bgiz63IslwGcWb1Kd7RX3n1XNNqDPYBwq2yv/nClbUbPECkFCl1NnTicKedfIJDM8WfhRSq9deg9Uh08Ss+9sUKLLr4W1ayWo09+4Gj83izyn2GkJ9g6SK/YLXj//Dkj2UrM7wJ0xggjAoGAWmQpvGRgPjQfVNe5BUqBhZbkfPB57rS4zXDtFKtqQVYUrdxZ/W6be6kqGyX0G/5Qf+hwuIg74eZfIwg65KbNNWwPV3EIngWwdBFH4B55ezN7F6NwZVNrd8rtt1lbf06O4w0dR0ns+edtw1WPrNac3xcFzk7be4K74tRQtXwsTuUCgYA77BOSG5GJBvwyyIPIzFlxPZgqjdYj1g2G/BDKIMYBL45DyZVtwDb/dhjPMjZrFFLLAR3pQiSnpHGxK2DwaNMZJRE4u5qUWKzLN3VYWNePBOoWrilNTDE0n2l/btEu0IOssiPxrnxYjzB+E2ovNpUonpg1uuI8RmF78zdcxWt9/w==";

	public static final int RED_ID_RESULT = 10005;// 红包关闭
	public static final int RED_ID_TO_RESULT = 10006;// 查看详情

	public static final int JPUSH_ALIAS = 3120;//极光推送别名设置

	// *****************************请求码 ******************************//
	/**
	 * 从本地获取图片请求码
	 */
	public static final int REQUEST_CODE_SELECT_LOCAL = 11;

	/**
	 * 应用根目录
	 */
	public static final String PATH_BASE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/COMMON/";
	/**
	 * 拍照文件夹
	 */
	public static final String PATH_CAMERA = PATH_BASE + "Camera/";
	/**
	 * 拍照缓存文件
	 */
	public static final String PATH_IMAGE_TEMP = PATH_CAMERA + "temp.jpg";

	/**
	 * 数据库版本
	 */
	public static final int DATA_BASE_VERSION = 4;

	public static final boolean isRepairer = false;// 是否维修员
}
