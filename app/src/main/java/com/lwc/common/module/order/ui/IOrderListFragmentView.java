package com.lwc.common.module.order.ui;

import com.lwc.common.module.bean.Order;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 */

public interface IOrderListFragmentView {


    void notifyData(List<Order> myOrders);

    void addData(List<Order> myOrders);

    /**
     * 获取刷新控件实例
     */
    BGARefreshLayout getBGARefreshLayout();
}
