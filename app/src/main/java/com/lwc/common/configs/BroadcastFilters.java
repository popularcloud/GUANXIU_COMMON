package com.lwc.common.configs;

/**
 * 设置广播过滤器（filters）常量
 */
public class BroadcastFilters {
    // ***************************注册广播接收动作 ***************************//
    /**
     * 退出登陆动作
     */
    public static final String LOGIN_OUT_ACTION = "LOGIN_OUT_ACTION";

    /**
     * app 包名
     */
    static String BASE = TApplication.context.getPackageName();
    /**
     * 添加设备成功之后的广播
     */
    public static final String ADD_MACHINE_SUCCESS = "com.lwc.common.activity.AddMachineActivity.add.machine" + BASE;

    /**
     * 登录完成的广播
     */
    public static final String UPDATE_USER_LOGIN_SUCCESSED = "UPDATE_USER_LOGIN_SUCCESSED" + BASE;
    /**
     * 用户头像改变之后的广播
     */
    public static final String UPDATE_USER_INFO_ICON = "com.lwc.common.activity.UserInfoActivity.userinfo_icon" + BASE;
    /**
     * 用户头像改变之后的广播
     */
    public static final String SHOW_MACHINE_INFO = "SHOW_MACHINE_INFO" + BASE;
    /**
     * 密码修改成功之后的广播
     */
    public static final String UPDATE_PASSWORD = "UPDATE_PASSWORD" + BASE;
    /**
     * 设备报修成功之后的广播
     */
    public static final String NOTIFI_DATA_MACHINE_LIST = "NOTIFI_DATA_MACHINE_LIST" + BASE;
    /**
     * 设备报修成功之后的广播
     */
    public static final String NOTIFI_MAIN_ORDER_MENTION = "NOTIFI_MAIN_ORDER_MENTION" + BASE;

    /**
     * 获取附近的人成功之后的广播
     */
    public static final String NOTIFI_NEARBY_PEOPLE = "NOTIFI_NEARBY_PEOPLE" + BASE;
    /**
     * 获取消息数成功之后的广播
     */
    public static final String NOTIFI_MESSAGE_COUNT = "NOTIFI_MESSAGE_COUNT" + BASE;
    /**
     * 获取未接订单成功之后的广播
     */
    public static final String NOTIFI_WAITTING_ORDERS = "NOTIFI_WAITTING_ORDERS" + BASE;
    /**
     * 订单状态刷新广播
     */
    public static final String NOTIFI_ORDER_INFO_MENTION = "NOTIFI_ORDER_INFO_MENTION" + BASE;
    /**
     * 自动接单按钮状态刷新广播
     */
    public static final String NOTIFI_BUTTON_STATUS = "NOTIFI_BUTTON_STATUS" + BASE;
    /**
     * 订单评价之后的刷新广播
     */
    public static final String NOTIFI_ORDER_PRIASE_MENTION = "NOTIFI_ORDER_PRIASE_MENTION" + BASE;
    /**
     * 订单被接单提示广播
     */
    public static final String NOTIFI_ORDER_GETTED_MENTION = "NOTIFI_ORDER_GETTED_MENTION" + BASE;
    /**
     * 订单挂起操作
     */
    public static final String NOTIFI_ORDER_INFO_GUAQI = "NOTIFI_ORDER_INFO_GUAQI" + BASE;
    /**
     * 订单弹窗操作
     */
    public static final String NOTIFI_ORDER_INFO_TANCHUAN = "NOTIFI_ORDER_INFO_TANCHUAN" + BASE;
    /**
     * 订单状态改变之后的广播
     */
    public static final String NOTIFI_ORDER_INFO_CHANGE = "NOTIFI_ORDER_INFO_CHANGE" + BASE;
    public static final String NOTIFI_CLOSE_SLIDING_MENU = "NOTIFI_CLOSE_SLIDING_MENU" + BASE;

    /** 附近订单 **/
    public static final String NOTIFI_NEAR_ORDER = "NOTIFI_NEAR_ORDER" + BASE;
}
