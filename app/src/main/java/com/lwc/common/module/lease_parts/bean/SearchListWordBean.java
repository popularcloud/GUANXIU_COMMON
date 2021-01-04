package com.lwc.common.module.lease_parts.bean;

import java.util.List;

public class SearchListWordBean {

    private List<LeaseGoodsKeywordBean> leaseGoodsKeyword;
    private List<LeaseGoodsKeywordBean> leaseGoodsKeywordNew;


    public List<LeaseGoodsKeywordBean> getLeaseGoodsKeyword() {
        return leaseGoodsKeyword;
    }

    public void setLeaseGoodsKeyword(List<LeaseGoodsKeywordBean> leaseGoodsKeyword) {
        this.leaseGoodsKeyword = leaseGoodsKeyword;
    }

    public List<LeaseGoodsKeywordBean> getLeaseGoodsKeywordNew() {
        return leaseGoodsKeywordNew;
    }

    public void setLeaseGoodsKeywordNew(List<LeaseGoodsKeywordBean> leaseGoodsKeywordNew) {
        this.leaseGoodsKeywordNew = leaseGoodsKeywordNew;
    }

    public static class LeaseGoodsKeywordBean {
        /**
         * count : 1
         * keyword_name : 搜索
         */

        private int count;
        private String keyword_name;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getKeyword_name() {
            return keyword_name;
        }

        public void setKeyword_name(String keyword_name) {
            this.keyword_name = keyword_name;
        }
    }
}
