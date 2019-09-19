package com.lwc.common.module.integral.view;

import com.lwc.common.module.integral.bean.IntegralExchangeBean;
import com.lwc.common.module.integral.bean.UserIntegralBean;

import java.util.List;

/**
 * @author younge
 * @date 2019/3/27 0027
 * @email 2276559259@qq.com
 */
public interface IntegralExchangeView {

    /**
     * 获取兑换记录
     * @param integralExchangeBeans
     */
    void onGetUserIntegral(List<IntegralExchangeBean> integralExchangeBeans);

    /**
     * 获取加载错误信息
     * @param msg
     */
    void onLoadError(String msg);
}
