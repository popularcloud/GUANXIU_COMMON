package com.lwc.common.module.repairs.ui;

import com.lwc.common.module.bean.Address;

import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 地址管理
 */
public interface IAddressManagerView {

    void notifyData(List<Address> addresses);
}
