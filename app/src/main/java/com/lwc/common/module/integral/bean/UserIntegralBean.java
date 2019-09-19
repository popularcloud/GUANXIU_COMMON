package com.lwc.common.module.integral.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author younge
 * @date 2019/4/2 0002
 * @email 2276559259@qq.com
 */
public class UserIntegralBean {

    /**
     * integral : 123
     * data : [{"createTime":"2019-03-05","transactionAmount":"888","userIntegral":"555","transactionMeans":"1","paymentType":"1","transactionNo":"123","transactionStatus":"1111","transactionRemark":"23123123"},{"createTime":"2019-03-05","transactionAmount":"888","userIntegral":"555","transactionMeans":"1","paymentType":"1","transactionNo":"123","transactionStatus":"1111","transactionRemark":"23123123"}]
     */

    private String integral;
    private List<DataBean> data;

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * createTime : 2019-03-05
         * transactionAmount : 888
         * userIntegral : 555
         * transactionMeans : 1
         * paymentType : 1
         * transactionNo : 123
         * transactionStatus : 1111
         * transactionRemark : 23123123
         */

        private String createTime;
        private String transactionAmount;
        private String userIntegral;
        private String transactionMeans; //交易方式（1.红包 2.支付宝 3.微信）
        private String paymentType;
        private String transactionNo;
        private String transactionStatus;
        private String transactionRemark;
        private int transactionScene;//交易场景(1.红包 2.订单，3 积分兑换)

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTransactionAmount() {
            return transactionAmount;
        }

        public void setTransactionAmount(String transactionAmount) {
            this.transactionAmount = transactionAmount;
        }

        public String getUserIntegral() {
            return userIntegral;
        }

        public void setUserIntegral(String userIntegral) {
            this.userIntegral = userIntegral;
        }

        public String getTransactionMeans() {
            return transactionMeans;
        }

        public void setTransactionMeans(String transactionMeans) {
            this.transactionMeans = transactionMeans;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getTransactionNo() {
            return transactionNo;
        }

        public void setTransactionNo(String transactionNo) {
            this.transactionNo = transactionNo;
        }

        public String getTransactionStatus() {
            return transactionStatus;
        }

        public void setTransactionStatus(String transactionStatus) {
            this.transactionStatus = transactionStatus;
        }

        public String getTransactionRemark() {
            return transactionRemark;
        }

        public void setTransactionRemark(String transactionRemark) {
            this.transactionRemark = transactionRemark;
        }

        public int getTransactionScene() {
            return transactionScene;
        }

        public void setTransactionScene(int transactionScene) {
            this.transactionScene = transactionScene;
        }
    }
}
