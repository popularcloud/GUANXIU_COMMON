package com.lwc.common.module.bean;

import java.io.Serializable;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 活动
 */
public class BroadcastBean implements Serializable {

    private String annunciateContent;//通知内容
    private String annunciateId; //通知ID
    private String annunciateUrl;//点击通知跳转URL
    private String createTime;// 创建时间
    private int isValid; //是否有效(0否 1是)
    private String modifyTime;//修改时间
    private int isExtend;// //是否显示扩招内容(0否 1是)
    private String annunciateContentExtend; //扩展内容
    private int annunciateObject;//": "1",               //接受通知的对象（1.全部 2.维修员 3.政府用户 4.个人用户）

    public int getAnnunciateObject() {
        return annunciateObject;
    }

    public void setAnnunciateObject(int annunciateObject) {
        this.annunciateObject = annunciateObject;
    }

    public int getIsExtend() {
        return isExtend;
    }

    public void setIsExtend(int isExtend) {
        this.isExtend = isExtend;
    }

    public String getAnnunciateContentExtend() {
        return annunciateContentExtend;
    }

    public void setAnnunciateContentExtend(String annunciateContentExtend) {
        this.annunciateContentExtend = annunciateContentExtend;
    }

    public String getAnnunciateContent() {
        return annunciateContent;
    }

    public void setAnnunciateContent(String annunciateContent) {
        this.annunciateContent = annunciateContent;
    }

    public String getAnnunciateId() {
        return annunciateId;
    }

    public void setAnnunciateId(String annunciateId) {
        this.annunciateId = annunciateId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAnnunciateUrl() {
        return annunciateUrl;
    }

    public void setAnnunciateUrl(String annunciateUrl) {
        this.annunciateUrl = annunciateUrl;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
}
