package com.lwc.common.controler.global;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 全局参数
 */

public class GlobalValue {

    /**
     * 头像名称
     */
    public static final String ICON_HEAD_NAME = "head.jpg";
    /**
     * 用户端的标识  （个人版）
     */
    public static final String CLIENT_TYPE = "person";

    /**
     * 登录的标记
     */
    public static final String IS_LOGIN = "is_login";

    /**
     * 用户id
     */
    public static final String USERID = "userId";
    /**
     * 用户头像
     */
    public static final String AVATAR = "avatar";


    /**
     * 定义定位权限
     */
    public static final int MY_ACCESS_COARSE_LOCATION = 4;

    public static final int READ_EXTERNAL_STORAGE = 5;
    /**
     * 定义访问存储设备的权限值
     */
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3;

    /**
     * 定义使用摄像头的权限值
     */
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;

    /**
     * 定义打电话的权限值
     */
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    /**
     * 获取图库
     */
    public static final int PHOTO_REQUESTCODE = 2000;

    /**
     * 维修0获取图库吗
     */
    public static final int REPAIRS_PHOTO_REQUESTCODE0 = 2010;
    /**
     * 维修1获取图库吗
     */
    public static final int REPAIRS_PHOTO_REQUESTCODE1 = 2011;

    /**
     * 维修0获取拍照功能
     */
    public static final int REPAIRS_CAMERA_REQUESTCODE0 = 20012;
    /**
     * 维修1获取拍照功能
     */
    public static final int REPAIRS_CAMERA_REQUESTCODE1 = 2013;
    /**
     * 获取拍照功能
     */
    public static final int CAMERA_REQUESTCODE = 2001;

    /**
     * 修改图片
     */
    public static final int MODIFY_PHOTO_REQUESTCODE = 2002;

}
