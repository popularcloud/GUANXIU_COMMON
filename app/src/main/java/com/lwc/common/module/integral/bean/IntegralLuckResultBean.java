package com.lwc.common.module.integral.bean;

/**
 * @author younge
 * @date 2019/7/15 0015
 * @email 2276559259@qq.com
 */
public class IntegralLuckResultBean {

    /**
     * luckyId : 2
     * luckyImage : 奖品图片
     * luckyType : 奖品类型（1：积分 2：实物 3虚拟物品）
     */

    private String luckyId;
    private String luckyImage;
    private int luckyType;
    private String remark;

    public String getLuckyId() {
        return luckyId;
    }

    public void setLuckyId(String luckyId) {
        this.luckyId = luckyId;
    }

    public String getLuckyImage() {
        return luckyImage;
    }

    public void setLuckyImage(String luckyImage) {
        this.luckyImage = luckyImage;
    }

    public int getLuckyType() {
        return luckyType;
    }

    public void setLuckyType(int luckyType) {
        this.luckyType = luckyType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
