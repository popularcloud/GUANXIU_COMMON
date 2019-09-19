package com.lwc.common.module.bean;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 图片
 */
public class Picture {

    /**
     * status : 10000
     * data : http://120.77.242.131/upload/user/picture/5a5c61b70bb84a3fa41693bf7c6242ff.jpeg
     * info : 操作成功
     */

    private String status;
    private String data;
    private String info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
