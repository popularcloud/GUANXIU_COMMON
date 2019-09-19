package com.lwc.common.module.bean;

import java.util.List;

/**
 * @author younge
 * @date 2019/8/6 0006
 * @email 2276559259@qq.com
 */
public class MyProfitBean {
    /**
     * countPopelAll : 2
     * sumMoneyAll : 10000
     * countPopelDay : 0
     * sumMoneyDay : 0
     * page : [{"createTime":"2017-11-30 11:44:53","transactionAmount":888,"transactionMeans":1,"paymentType":0,"transactionNo":"151201349368690300","transactionStatus":1,"transactionRemark":"通过活动：双十二红包雨，获取奖金8.88元"}]
     */

    private int countPopelAll;
    private int sumMoneyAll;
    private int countPopelDay;
    private int sumMoneyDay;
    private List<PageBean> data;

    public int getCountPopelAll() {
        return countPopelAll;
    }

    public void setCountPopelAll(int countPopelAll) {
        this.countPopelAll = countPopelAll;
    }

    public int getSumMoneyAll() {
        return sumMoneyAll/100;
    }

    public void setSumMoneyAll(int sumMoneyAll) {
        this.sumMoneyAll = sumMoneyAll;
    }

    public int getCountPopelDay() {
        return countPopelDay;
    }

    public void setCountPopelDay(int countPopelDay) {
        this.countPopelDay = countPopelDay;
    }

    public int getSumMoneyDay() {
        return sumMoneyDay/100;
    }

    public void setSumMoneyDay(int sumMoneyDay) {
        this.sumMoneyDay = sumMoneyDay;
    }

    public List<PageBean> getData() {
        return data;
    }

    public void setData(List<PageBean> data) {
        this.data = data;
    }

    public static class PageBean {
        /**
         * createTime : 2017-11-30 11:44:53
         * transactionAmount : 888
         * transactionMeans : 1
         * paymentType : 0
         * transactionNo : 151201349368690300
         * transactionStatus : 1
         * transactionRemark : 通过活动：双十二红包雨，获取奖金8.88元
         */

        private String createTime;
        private int transactionAmount;
        private int transactionMeans;
        private int paymentType;
        private String transactionNo;
        private int transactionStatus;
        private String transactionRemark;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getTransactionAmount() {
            return transactionAmount;
        }

        public void setTransactionAmount(int transactionAmount) {
            this.transactionAmount = transactionAmount;
        }

        public int getTransactionMeans() {
            return transactionMeans;
        }

        public void setTransactionMeans(int transactionMeans) {
            this.transactionMeans = transactionMeans;
        }

        public int getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(int paymentType) {
            this.paymentType = paymentType;
        }

        public String getTransactionNo() {
            return transactionNo;
        }

        public void setTransactionNo(String transactionNo) {
            this.transactionNo = transactionNo;
        }

        public int getTransactionStatus() {
            return transactionStatus;
        }

        public void setTransactionStatus(int transactionStatus) {
            this.transactionStatus = transactionStatus;
        }

        public String getTransactionRemark() {
            return transactionRemark;
        }

        public void setTransactionRemark(String transactionRemark) {
            this.transactionRemark = transactionRemark;
        }
    }
}
