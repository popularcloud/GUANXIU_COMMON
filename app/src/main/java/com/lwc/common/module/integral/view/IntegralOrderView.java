package com.lwc.common.module.integral.view;

import com.lwc.common.module.integral.bean.UserIntegralBean;

/**
 * @author younge
 * @date 2019/3/27 0027
 * @email 2276559259@qq.com
 */
public interface IntegralOrderView {

    /**
     * 获取用户积分
     * @param userIntegralBean
     */
    void onGetUserIntegral(UserIntegralBean userIntegralBean);

    /**
     * 获取加载错误信息
     * @param msg
     */
    void onLoadError(String msg);
}
