package com.lwc.common.module.bean;

import java.io.Serializable;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 维修员
 */
public class Repairman implements Serializable {


    private String maintenanceName;// ": "维修师名称",          //维修师名称
    private String isSecrecy;// ": "是否认证",                   （0：否 1:是）
    private String isBusy;// ": "是否繁忙",                      //是否繁忙（0：否 1:是）
    private String orderCount;// ": "订单数量",                  //订单数量
    private String maintenanceStar;//": "维修员评分",            //维修员评分
    private String maintenanceHeadImage;// ": "维修时头像"     //维修时头像
    private String serviceAttitude;//": "0",                       //服务态度
    private String averageSpeed;//": "0",                       //上门速度
    private String expertiseLevel;//": "0",                        //专业水平
    private String maintenanceLatitude;// ": "维修员经度",         //维修员经度
    private String maintenanceLongitude;// ": "维修员纬度"        //维修员纬度
    private String companyName;// ": "维修师公司名称"          //维修师公司名称
    private String deviceTypeName;// ": "维修师维修的设备"      //维修师维修的设备
    private String maintenanceId;
    private int isInvite;//是否邀请 0：否 1:是
    private String maintenanceLabelNames;//标签名_评论数量

    public String getMaintenanceLabelNames() {
        return maintenanceLabelNames;
    }

    public void setMaintenanceLabelNames(String maintenanceLabelNames) {
        this.maintenanceLabelNames = maintenanceLabelNames;
    }

    public int getIsInvite() {
        return isInvite;
    }

    public void setIsInvite(int isInvite) {
        this.isInvite = isInvite;
    }

    public int getIsLive() {
        return isLive;
    }

    public void setIsLive(int isLive) {
        this.isLive = isLive;
    }

    private int isLive;//维修员是否在线 0：否 1：是

    public String getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(String maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public String getMaintenanceName() {
        return maintenanceName;
    }

    public void setMaintenanceName(String maintenanceName) {
        this.maintenanceName = maintenanceName;
    }

    public String getIsSecrecy() {
        return isSecrecy;
    }

    public void setIsSecrecy(String isSecrecy) {
        this.isSecrecy = isSecrecy;
    }

    public String getIsBusy() {
        return isBusy;
    }

    public void setIsBusy(String isBusy) {
        this.isBusy = isBusy;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getMaintenanceHeadImage() {
        return maintenanceHeadImage;
    }

    public void setMaintenanceHeadImage(String maintenanceHeadImage) {
        this.maintenanceHeadImage = maintenanceHeadImage;
    }

    public String getMaintenanceStar() {
        return maintenanceStar;
    }

    public void setMaintenanceStar(String maintenanceStar) {
        this.maintenanceStar = maintenanceStar;
    }

    public String getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(String averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public String getServiceAttitude() {
        return serviceAttitude;
    }

    public void setServiceAttitude(String serviceAttitude) {
        this.serviceAttitude = serviceAttitude;
    }

    public String getExpertiseLevel() {
        return expertiseLevel;
    }

    public void setExpertiseLevel(String expertiseLevel) {
        this.expertiseLevel = expertiseLevel;
    }

    public String getMaintenanceLatitude() {
        return maintenanceLatitude;
    }

    public void setMaintenanceLatitude(String maintenanceLatitude) {
        this.maintenanceLatitude = maintenanceLatitude;
    }

    public String getMaintenanceLongitude() {
        return maintenanceLongitude;
    }

    public void setMaintenanceLongitude(String maintenanceLongitude) {
        this.maintenanceLongitude = maintenanceLongitude;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }
}
