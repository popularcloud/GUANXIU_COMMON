package com.lwc.common.module.bean;

import android.text.TextUtils;

import com.lwc.common.utils.Utils;

import java.io.Serializable;

/**
 * 套餐
 * @author 何栋
 * @date 2018/8/18 0018
 * @email 294663966@qq.com
 */
public class PackageBean implements Serializable {


    /**
     * packageId:套餐ID
     * packageName:套餐名称
     * packageType:套餐类型（1.上门 2.维修 3.上门维修）
     * remissionCount:减免次数(0:无限）
     * townName:可用的镇名称,多个用",“分隔
     * remark:备注
     * expirationTime:过期时间
     * createTime:创建时间
     * residueRemissionCount:剩余减免次数
     */
    private String packageId;
    private String packageName;
    private int packageType;
    private int remissionCount;
    private String townNames;
    private String remark;
    private String expirationTime;
    private String createTime;
    private int residueRemissionCount;
    private String packagePrice;//套餐价格
    private int validDay;//套餐有效期/天
    /**
     * isValid : 0 是否可以使用  0否 1是
     * errorMsg : 不可用原因
     */
    private Integer isValid;
    private String errorMsg;

    //是否不可选择
    private boolean isOnSelect = false;

    public PackageBean(){

    }

    public PackageBean(boolean isOnSelect){
        this.isOnSelect = isOnSelect;
    }

    public String getPackagePrice() {
        if (!TextUtils.isEmpty(packagePrice)){
            return Utils.chu(packagePrice, "100");
        } else {
            packagePrice = "0";
        }
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public int getValidDay() {
        return validDay;
    }

    public void setValidDay(int validDay) {
        this.validDay = validDay;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getPackageType() {
        return packageType;
    }

    public void setPackageType(int packageType) {
        this.packageType = packageType;
    }

    public int getRemissionCount() {
        return remissionCount;
    }

    public void setRemissionCount(int remissionCount) {
        this.remissionCount = remissionCount;
    }

    public String getTownNames() {
        return townNames;
    }

    public void setTownNames(String townNames) {
        this.townNames = townNames;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getResidueRemissionCount() {
        return residueRemissionCount;
    }

    public void setResidueRemissionCount(int residueRemissionCount) {
        this.residueRemissionCount = residueRemissionCount;
    }

    public Integer getIsValid() {
        if(isValid == null){
            return 1;
        }
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isOnSelect() {
        return isOnSelect;
    }

    public void setOnSelect(boolean onSelect) {
        isOnSelect = onSelect;
    }
}
