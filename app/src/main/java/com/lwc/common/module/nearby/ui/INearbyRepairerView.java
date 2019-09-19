package com.lwc.common.module.nearby.ui;

import com.lwc.common.module.bean.Repairman;

import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 */
public interface INearbyRepairerView {

    /**
     * 刷新数据
     * @param repairmans
     */
    void notifyData(List<Repairman> repairmans, int count);

    void closeDialog();
}
