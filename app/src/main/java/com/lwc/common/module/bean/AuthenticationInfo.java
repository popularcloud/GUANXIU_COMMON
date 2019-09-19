package com.lwc.common.module.bean;

import java.io.Serializable;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 用户地址
 */
public class AuthenticationInfo implements Serializable {

    private String createTime;//": "2018-1-11 15:20:14",                   //创建时间
    private int second;//": 3600,                                    //已经耗时多少秒
    private String applicantName;//": "申请人姓名",                      //申请人姓名
    private String applicantImages;// ": "申请图片",                      //申请图片
    private String organizationName;// ":“单位名”,                       //单位名
    private String provinceName;//": “省名”,                            //省名
    private String cityName;//":“市名”,                                 //市名
    private String townName;// ": "区名",                              //区名
    private String detailedAddress;// ": "详细地址",                      //详细地址
    private String auditStatus;//": 0,
    private String provinceId;//": "省ID",                               //省ID
    private String cityId;//": "市ID",                                   //市ID
    private String townId;//": "区ID",                                  //区ID
    private String remark;// ": "备注",                                 //备注
    private String passTime;// ": "通过时间",                           //通过时间

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPassTime() {
        return passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }

    private String recordId;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantImages() {
        return applicantImages;
    }

    public void setApplicantImages(String applicantImages) {
        this.applicantImages = applicantImages;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getTownId() {
        return townId;
    }

    public void setTownId(String townId) {
        this.townId = townId;
    }
}
