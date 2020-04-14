package com.lwc.common.module.bean;


import java.io.Serializable;

public class IndexAdBean implements Serializable {

    /**
     * urlType : 0
     * leaseId : 1585189354342SB
     * createTime : 2020-03-26 10:22:34
     * leaseDetail : 详情
     * isValid : 1
     * leaseTitle : 标题
     * leaseUrl : 跳转地址
     * imageLocalhost : 1
     * sn : 0
     * leaseImageUrl : 地址
     */

    private int urlType;
    private String leaseId;
    private String createTime;
    private String leaseDetail;
    private int isValid;
    private String leaseTitle;
    private String leaseUrl;
    private int imageLocalhost;  //显示位置 1 上 2 下  3 左  4 右
    private int sn;
    private String leaseImageUrl;

    public int getUrlType() {
        return urlType;
    }

    public void setUrlType(int urlType) {
        this.urlType = urlType;
    }

    public String getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLeaseDetail() {
        return leaseDetail;
    }

    public void setLeaseDetail(String leaseDetail) {
        this.leaseDetail = leaseDetail;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public String getLeaseTitle() {
        return leaseTitle;
    }

    public void setLeaseTitle(String leaseTitle) {
        this.leaseTitle = leaseTitle;
    }

    public String getLeaseUrl() {
        return leaseUrl;
    }

    public void setLeaseUrl(String leaseUrl) {
        this.leaseUrl = leaseUrl;
    }

    public int getImageLocalhost() {
        return imageLocalhost;
    }

    public void setImageLocalhost(int imageLocalhost) {
        this.imageLocalhost = imageLocalhost;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public String getLeaseImageUrl() {
        return leaseImageUrl;
    }

    public void setLeaseImageUrl(String leaseImageUrl) {
        this.leaseImageUrl = leaseImageUrl;
    }
}
