package com.lwc.common.module.bean;

import com.lwc.common.bean.PartsDetailBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 故障检测
 */
public class Detection implements Serializable {


   private String createTime;//": "2018-1-11 15:20:14",                   //创建时间
    private String exampleId;//": 1,                                    //故障实例ID
    private String exampleName;//": "软件",                            //故障实例名称
    private String faultDescribe;//": "洒洒水阿里",                       //故障描述
    private String faultType;//": 1,                                 //故障类型(1.软件 2.硬件 3.其他）
    private String isValid;//": 1,                                    //是否有效（0.无效 1.有效）
    private String maintainCost;//": 50,                              //维修费用
    private String modifyTime;//": "2018-01-11 15:20:16",
    private String orderId;//": "170906095543685101RR",               //所属订单ID
    private String otherCost;//": 0,                                  //其他费用
    private String recordId;//": "170906095543685101RR",              //记录ID
    private String visitCost;//": 20                                   //上门费用
    private String remark;//": “这是备注”                            //备注
    private String describeImages;// ": “故障检测图片”               //故障检测图片
    private String discountAmount;//优惠金额
    private String couponId;//优惠券id
    private int couponType;//优惠券类型 (1:通用 2:上门优惠 3:软件优惠 4:硬件优惠)
    private String packageId;//套餐id
    private int packageType;//套餐类型（1.上门 2.维修 3.上门维修）
    private String hardwareCost;//硬件维修费
    private String reVisitCost;//原上门费用
    private List<PartsDetailBean> accessories;//配件信息

    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    public String getHardwareCost() {
        return hardwareCost;
    }

    public void setHardwareCost(String hardwareCost) {
        this.hardwareCost = hardwareCost;
    }

    private ArrayList<Solution> orderFaults;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public int getPackageType() {
        return packageType;
    }

    public void setPackageType(int packageType) {
        this.packageType = packageType;
    }

    public ArrayList<Solution> getOrderFaults() {
        return orderFaults;
    }

    public void setOrderFaults(ArrayList<Solution> orderFaults) {
        this.orderFaults = orderFaults;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExampleId() {
        return exampleId;
    }

    public void setExampleId(String exampleId) {
        this.exampleId = exampleId;
    }

    public String getExampleName() {
        return exampleName;
    }

    public void setExampleName(String exampleName) {
        this.exampleName = exampleName;
    }

    public String getFaultDescribe() {
        return faultDescribe;
    }

    public void setFaultDescribe(String faultDescribe) {
        this.faultDescribe = faultDescribe;
    }

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getMaintainCost() {
        return maintainCost;
    }

    public void setMaintainCost(String maintainCost) {
        this.maintainCost = maintainCost;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(String otherCost) {
        this.otherCost = otherCost;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getVisitCost() {
        return visitCost;
    }

    public void setVisitCost(String visitCost) {
        this.visitCost = visitCost;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDescribeImages() {
        return describeImages;
    }

    public void setDescribeImages(String describeImages) {
        this.describeImages = describeImages;
    }

    public String getReVisitCost() {
        return reVisitCost;
    }

    public void setReVisitCost(String reVisitCost) {
        this.reVisitCost = reVisitCost;
    }

    public List<PartsDetailBean> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<PartsDetailBean> accessories) {
        this.accessories = accessories;
    }
}
