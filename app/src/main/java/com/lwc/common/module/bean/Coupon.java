package com.lwc.common.module.bean;

import android.text.TextUtils;

import com.lwc.common.utils.Utils;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 优惠券
 * @author 何栋
 * @date 2018/7/18 0018
 * @email 294663966@qq.com
 */
public class Coupon extends DataSupport implements Serializable {

    /**
     * couponName : 测试推广活动优惠卷
     * isOverdue : 0
     * createTime : 2018-06-07 15:31:22
     * expirationTime : 2018-07-07 15:31:22
     * couponType : 1
     * isSoonOverdue : 0
     * discountAmount : 800
     * isNew : 0
     * couponId : 1528356682914UC
     * fullReductionAmount : 0
     * couponDescribe : 没有
     */

    private String couponName;//优惠券名称
    private int isOverdue;//是否过期（0:否 1:是）
    private String createTime;
    private String expirationTime;
    private int couponType;//优惠卷类型(1:通用 2:上门优惠 3:软件优惠 4:硬件优惠)
    private int isSoonOverdue;//是否快过期（0:否 1:是）
    private String discountAmount;//优惠金额
    private int isNew;//是否是新优惠卷(0:否 1:是）

    private String couponId;//优惠券ID
    private String fullReductionAmount;//满减限制金额(默认:0 //表示无限制）
    private String couponDescribe;//优惠卷描述
    private String activityId; //活动ID
    private int select;
    private int validTime;

    //是否不可选择
    private boolean isOnSelect = false;

    public Coupon(){

    }

    public Coupon(boolean isOnSelect){
        this.isOnSelect = isOnSelect;
    }

    public int getValidTime() {
        return validTime;
    }

    public void setValidTime(int validTime) {
        this.validTime = validTime;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public int getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(int isOverdue) {
        this.isOverdue = isOverdue;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    public int getIsSoonOverdue() {
        return isSoonOverdue;
    }

    public void setIsSoonOverdue(int isSoonOverdue) {
        this.isSoonOverdue = isSoonOverdue;
    }

    public String getDiscountAmount() {
        if (!TextUtils.isEmpty(discountAmount)){
            return Utils.chu(discountAmount, "100");
        } else {
            discountAmount = "0";
        }
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getFullReductionAmount() {
        if (!TextUtils.isEmpty(fullReductionAmount)){
            return Utils.chu(fullReductionAmount, "100");
        } else {
            fullReductionAmount = "0";
        }
        return fullReductionAmount;
    }

    public void setFullReductionAmount(String fullReductionAmount) {
        this.fullReductionAmount = fullReductionAmount;
    }

    public String getCouponDescribe() {
        return couponDescribe;
    }

    public void setCouponDescribe(String couponDescribe) {
        this.couponDescribe = couponDescribe;
    }

    public boolean isOnSelect() {
        return isOnSelect;
    }

    public void setOnSelect(boolean onSelect) {
        isOnSelect = onSelect;
    }
}
