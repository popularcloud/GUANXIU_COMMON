package com.lwc.common.module.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by 何栋 on 2018/03/25.
 * 294663966@qq.com
 * 检查是否有新消息
 */
public class HasMsg extends DataSupport implements Serializable{

   // type         1.系统消息 2.资讯消息 3.活动消息 4.个人消息 0.所有
    /**
     * type : 4
     * hasMessage : true
     * count : 60
     */

    private String type;
    private boolean hasMessage;
    private int count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isHasMessage() {
        return hasMessage;
    }

    public void setHasMessage(boolean hasMessage) {
        this.hasMessage = hasMessage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
