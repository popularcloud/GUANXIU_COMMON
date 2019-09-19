package com.lwc.common.module.integral.view;

import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.integral.bean.IntegralgoodsBean;

import java.util.List;

/**
 * @author younge
 * @date 2019/3/27 0027
 * @email 2276559259@qq.com
 */
public interface IntegralMainView {

    /**
     * 获取积分商品轮播数据
     * @param adInfoList
     */
    void onBannerLoadSuccess(List<ADInfo> adInfoList);

    /**
     * 获取积分商品
     * @param partsBeanList
     */
    void onLoadGoods(List<IntegralgoodsBean> partsBeanList);

    /**
     * 获取加载错误信息
     * @param msg
     */
    void onLoadError(String msg);
}
