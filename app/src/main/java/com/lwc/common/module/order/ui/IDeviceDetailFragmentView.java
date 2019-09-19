package com.lwc.common.module.order.ui;

import com.lwc.common.module.bean.AfterService;

import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 设备详情
 */
public interface IDeviceDetailFragmentView {

    /**
     * 设置设备详情信息
     *
     * @param list
     */
    void setDeviceDetailInfor(List<AfterService> list);
}
