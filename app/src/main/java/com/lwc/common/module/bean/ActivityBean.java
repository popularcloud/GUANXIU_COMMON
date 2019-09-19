package com.lwc.common.module.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * 何栋
 * 2017-12-06
 */
public class ActivityBean extends DataSupport implements Serializable {

    private String activityId;//": "123",                    //活动ID
    private String conditionId;//": "123",                  //触发ID
    private String conditionName;//": "报修",              //触发名称
    private String conditionIndex;//": "order/saveOrder",     //触发标示  null  ""
    private String assignCrowd;//": 2,                     //活动指定人群 （1.所有 2.用户 3.维修员）
    private String activityName;//": "双十二红包雨"         //活动名称
    /**
     * ruleUrl : 活动规则连接
     * rewardFashion : 1
     * coupons : [{"coupon_id":"xxxxxxxxAB","coupon_name":"xxxxxxx","activity_id":"xxxxxxxxAB","coupon_type":"1","discount_amount":"100","coupon_describe":"xxxx","full_reduction_amount":"10000","valid_time":"30","create_time":"2018-01-19 16:44:09"}]
     */
    private String ruleUrl;
    private String rewardFashion;//活动奖励方式 （1. 红包 2.优惠卷）

    private List<Coupon> coupons;


    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public String getConditionIndex() {
        return conditionIndex;
    }

    public void setConditionIndex(String conditionIndex) {
        this.conditionIndex = conditionIndex;
    }

    public String getAssignCrowd() {
        return assignCrowd;
    }

    public void setAssignCrowd(String assignCrowd) {
        this.assignCrowd = assignCrowd;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getRuleUrl() {
        return ruleUrl;
    }

    public void setRuleUrl(String ruleUrl) {
        this.ruleUrl = ruleUrl;
    }

    public String getRewardFashion() {
        return rewardFashion;
    }

    public void setRewardFashion(String rewardFashion) {
        this.rewardFashion = rewardFashion;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }
}