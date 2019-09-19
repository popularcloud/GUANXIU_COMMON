package com.lwc.common.module.order.ui;

import com.lwc.common.module.bean.Order;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 订单详情
 */
public interface IOrderDetailFragmentView {

    /**
     * 设置设备详情信息
     *
     * @param myOrder
     */
    void setDeviceDetailInfor(Order myOrder);
}
