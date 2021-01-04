package com.lwc.common.module.bean;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 通用接口实体
 */
public class CommonData {

    private String status;
    private String info;
    private Object data;

    public Object getData() {
       return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
