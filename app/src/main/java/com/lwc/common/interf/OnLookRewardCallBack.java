package com.lwc.common.interf;

/**
 * @author younge
 * @date 2019/4/2 0002
 * @email 2276559259@qq.com
 */
public interface OnLookRewardCallBack {

    int RewardIntergray = 1; //积分奖励
    int RewardCoupon = 2;//优惠券奖励

    void onItemClick(int type);
}
