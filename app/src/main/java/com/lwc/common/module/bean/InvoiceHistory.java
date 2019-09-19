package com.lwc.common.module.bean;

import java.io.Serializable;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 我的订单
 */
public class InvoiceHistory implements Serializable {

    private String invoiceImages;
    private String acceptAddress;//": "广东东莞南城区澳南路1号东日大厦403",    //纸质发票联系地址
    private String acceptName;//": "何栋",                                     //纸质发票联系姓名
    private String acceptPhone;//": "18818997564",                             //纸质发票联系电话
    private String createTime;//": "2018-1-8 09:13:23",                          //创建时间
    private String dutyParagraph;//": "963256322222222C",                      //税号
    private String invoiceAmount;//": 200,                                     //订单金额
    private String invoiceContent;//": "维修费用",                              //发票正文
    private String invoiceId;//": "1",                                          //发票ID
    private String invoiceOrders;//": "1,2,3",                                   //发票包含的订单
    private String invoiceStatus;//": 1,                                //发票状态（1.未开票 2.已开票）
    private String invoiceTitle;//": "广东立升科技电脑公司",             //发票抬头
    private String invoiceTitleType;//": 1,                      //发票抬头类型（1.企业抬头，2.个人抬头）
    private String invoiceType;//": 1,                          //发票类型（1.电子发票 2.纸质发票）
    private String isValid;//": 1,                              //是否有效（0：无效 1：有效 2：软删除）
    private String modifyTime;//": "2018-01-08 09:14:17",        //修改时间
    private String remark;//": "",                             //备注信息
    private String userEmail;//": "2998868900@qq.com",         //电子发票收件信息
    private String userId;//": "170901094644576101RK"          //用户ID

    public String getInvoiceImages() {
        return invoiceImages;
    }

    public void setInvoiceImages(String invoiceImages) {
        this.invoiceImages = invoiceImages;
    }

    public String getAcceptAddress() {
        return acceptAddress;
    }

    public void setAcceptAddress(String acceptAddress) {
        this.acceptAddress = acceptAddress;
    }

    public String getAcceptName() {
        return acceptName;
    }

    public void setAcceptName(String acceptName) {
        this.acceptName = acceptName;
    }

    public String getAcceptPhone() {
        return acceptPhone;
    }

    public void setAcceptPhone(String acceptPhone) {
        this.acceptPhone = acceptPhone;
    }

    public String getDutyParagraph() {
        return dutyParagraph;
    }

    public void setDutyParagraph(String dutyParagraph) {
        this.dutyParagraph = dutyParagraph;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getInvoiceContent() {
        return invoiceContent;
    }

    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceOrders() {
        return invoiceOrders;
    }

    public void setInvoiceOrders(String invoiceOrders) {
        this.invoiceOrders = invoiceOrders;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceTitleType() {
        return invoiceTitleType;
    }

    public void setInvoiceTitleType(String invoiceTitleType) {
        this.invoiceTitleType = invoiceTitleType;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
