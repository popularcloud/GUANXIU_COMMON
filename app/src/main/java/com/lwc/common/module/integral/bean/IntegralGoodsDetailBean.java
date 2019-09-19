package com.lwc.common.module.integral.bean;

import java.io.Serializable;

/**
 * @author younge
 * @date 2019/4/1 0001
 * @email 2276559259@qq.com
 */
public class IntegralGoodsDetailBean implements Serializable{

    /**
     * integralDetails : 商品详情
     * integralName : 积分商品名称
     * integralNum : 需要积分数量
     * createTime : 2019-03-29 15:50:57
     * integralId : 商品id
     * integralInventory : 商品库存
     * integralImg : 商品封面
     */

    private String integralDetails;
    private String integralName;
    private String integralNum;
    private String createTime;
    private String integralId;
    private String integralInventory;
    private String integralCover;
    private String integralBanner;

    public String getIntegralDetails() {
        return integralDetails;
    }

    public void setIntegralDetails(String integralDetails) {
        this.integralDetails = integralDetails;
    }

    public String getIntegralName() {
        return integralName;
    }

    public void setIntegralName(String integralName) {
        this.integralName = integralName;
    }

    public String getIntegralNum() {
        return integralNum;
    }

    public void setIntegralNum(String integralNum) {
        this.integralNum = integralNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIntegralId() {
        return integralId;
    }

    public void setIntegralId(String integralId) {
        this.integralId = integralId;
    }

    public String getIntegralInventory() {
        return integralInventory;
    }

    public void setIntegralInventory(String integralInventory) {
        this.integralInventory = integralInventory;
    }

    public String getIntegralCover() {
        return integralCover;
    }

    public void setIntegralCover(String integralCover) {
        this.integralCover = integralCover;
    }

    public String getIntegralBanner() {
        return integralBanner;
    }

    public void setIntegralBanner(String integralBanner) {
        this.integralBanner = integralBanner;
    }
}
