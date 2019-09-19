package com.lwc.common.bean;

import java.util.List;

/**
 * @author younge
 * @date 2019/5/22 0022
 * @email 2276559259@qq.com
 */
public class SignedListBean {

    /**
     * day : [{"create_time":"2019-04-29 10:41:13"},{"create_time":"2019-04-28 10:40:49"}]
     * info : [{"sign_count":7,"sign_integral":2800,"sign_sum":4}]
     * remark : 积分获得说明
     */

    private String remark;
    private List<DayBean> day;
    private InfoBean info;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<DayBean> getDay() {
        return day;
    }

    public void setDay(List<DayBean> day) {
        this.day = day;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class DayBean {
        /**
         * create_time : 2019-04-29 10:41:13
         */

        private String create_time;

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }

    public static class InfoBean {
        /**
         * sign_count : 7
         * sign_integral : 2800
         * sign_sum : 4
         */

        private int sign_count;
        private int sign_integral;
        private int sign_sum;

        public int getSign_count() {
            return sign_count;
        }

        public void setSign_count(int sign_count) {
            this.sign_count = sign_count;
        }

        public int getSign_integral() {
            return sign_integral;
        }

        public void setSign_integral(int sign_integral) {
            this.sign_integral = sign_integral;
        }

        public int getSign_sum() {
            return sign_sum;
        }

        public void setSign_sum(int sign_sum) {
            this.sign_sum = sign_sum;
        }
    }
}
