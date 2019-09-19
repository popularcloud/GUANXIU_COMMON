package com.lwc.common.module.integral.view;

import com.lwc.common.module.integral.bean.IntegralGoodsDetailBean;

/**
 * @author younge
 * @date 2019/4/1 0001
 * @email 2276559259@qq.com
 */
public interface IntegralGoodsDetailView {

    /**
     * 获取积分商品详情
     * @param bean
     */
    void onLoadGoodsDetailSuccess(IntegralGoodsDetailBean bean);


    /**
     * 获取加载错误信息
     * @param msg
     */
    void onLoadError(String msg);
}
