package com.lwc.common.module.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 维修公司
 */
public class RepairsCompany extends DataSupport implements Serializable {


    private String companyId;//": "170831102758556102HB",     //公司ID
    private String companyName;//": "广东立升科技有限公司"   //公司名称
    private String isSecrecy;//0：否 1：一级私密公司(可接政府订单，但不可接涉密订单） 2：申请中 3:二级私密公司(可接涉密订单)

    public String getIsSecrecy() {
        return isSecrecy;
    }

    public void setIsSecrecy(String isSecrecy) {
        this.isSecrecy = isSecrecy;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
