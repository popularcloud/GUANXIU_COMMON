package com.lwc.common.module.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 维修类型
 */
public class Repairs extends DataSupport implements Serializable{

    private String deviceTypeId;
    private String deviceTypeName;
    private String deviceTypeIcon;
    private Long deviceMode;
    public Repairs(String repairsId, String devicename, String deviceTypeIcon) {
        this.deviceTypeId = repairsId;
        this.deviceTypeName = devicename;
        this.deviceTypeIcon = deviceTypeIcon;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getDeviceTypeIcon() {
        return deviceTypeIcon;
    }

    public void setDeviceTypeIcon(String deviceTypeIcon) {
        this.deviceTypeIcon = deviceTypeIcon;
    }

    public Long getDeviceMode() {
        return deviceMode;
    }

    public void setDeviceMode(Long deviceMode) {
        this.deviceMode = deviceMode;
    }

    @Override
    public String toString() {
        return "Repairs{" +
                "deviceTypeId=" + deviceTypeId +
                ", deviceTypeName='" + deviceTypeName + '\'' +
                ", deviceTypeIcon='" + deviceTypeIcon + '\'' +
                '}';
    }
}
