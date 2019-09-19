package com.lwc.common.module.integral.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author younge
 * @date 2019/7/12 0012
 * @email 2276559259@qq.com
 */
public class IntegralLuckBean implements Serializable{

    /**
     * remark : 积分抽奖规则
     * remarkShort : 积分抽奖规则（短）
     * goods : [{"luckyId":"2","luckyName":"奖品名称","luckyImage":"奖品图片"}]
     */

    private String remark;
    private String remarkShort;
    private List<GoodsBean> goods;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemarkShort() {
        return remarkShort;
    }

    public void setRemarkShort(String remarkShort) {
        this.remarkShort = remarkShort;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * luckyId : 2
         * luckyName : 奖品名称
         * luckyImage : 奖品图片
         */

        private String luckyId;
        private String luckyName;
        private String luckyImage;

        public String getLuckyId() {
            return luckyId;
        }

        public void setLuckyId(String luckyId) {
            this.luckyId = luckyId;
        }

        public String getLuckyName() {
            return luckyName;
        }

        public void setLuckyName(String luckyName) {
            this.luckyName = luckyName;
        }

        public String getLuckyImage() {
            return luckyImage;
        }

        public void setLuckyImage(String luckyImage) {
            this.luckyImage = luckyImage;
        }
    }
}
