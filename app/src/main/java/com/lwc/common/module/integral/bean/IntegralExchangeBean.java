package com.lwc.common.module.integral.bean;

/**
 * @author younge
 * @date 2019/4/3 0003
 * @email 2276559259@qq.com
 */
public class IntegralExchangeBean {
    /**
     * exchangeId : 兑换商品id
     * integralName : 商品名称
     * integralNum : 积分数量
     * createTime : 2019-03-29 16:15:10
     * integralCount : 购买数量
     */

    private String exchangeId;
    private String integralName;
    private String integralNum;
    private String createTime;
    private String integralCount;
    private String integralCover;
    private String exchangeStatus; //兑换状态（1：兑换中；2：已完成）
    private String exchangeType; //兑换类型（1：积分兑换；2：抽奖奖励）

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
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

    public String getIntegralCount() {
        return integralCount;
    }

    public void setIntegralCount(String integralCount) {
        this.integralCount = integralCount;
    }

    public String getIntegralCover() {
        return integralCover;
    }

    public void setIntegralCover(String integralCover) {
        this.integralCover = integralCover;
    }

    public String getExchangeStatus() {
        return exchangeStatus;
    }

    public void setExchangeStatus(String exchangeStatus) {
        this.exchangeStatus = exchangeStatus;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }
}
