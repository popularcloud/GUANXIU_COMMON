package com.lwc.common.bean;

import java.io.Serializable;

/**
 * @author 何栋
 * @date 2018/5/14 0014
 * @email 294663966@qq.com
 */
public class ProdctBean implements Serializable{

    private String deviceTypeName;//": "电脑",                    //维修的设备类型名称
    private String deviceTypeIcon;//": "http://www.baidu.com.jpg"    //维修的设备类型图标
    private String deviceTypeId;//":

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

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }
}
