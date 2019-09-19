package com.lwc.common.module.bean;

import android.text.TextUtils;

import com.lwc.common.utils.PatternUtil;
import com.lwc.common.utils.Utils;

import java.io.Serializable;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 *
 * 用户信息
 */
public class User implements Serializable{

    private String bindCompanyId;//": "用户保修绑定公司ID",       //用户绑定的公司 保修选公司的
    private String bindCompanyName;//": "用户保修绑定公司名称", //用户绑定的公司 保修选公司的
    private String createTime;//": "2017-08-17 16:41:48",          //创建时间
    private String modifyTime;//": "2017-08-31 10:52:44",           //最后修改时间
    private String parentCompanyId;//": "用户所属的父公司ID",     //用户所属的父公司ID
    private String parentCompanyName;//": "用户所属的父公司名称",//用户所属的父公司名称
    private String userCompanyId;//": "170831102758556102YU",    //所属公司ID
    private String userCompanyName;//": "测试",                 ////所属公司名称
    private String userHeadImage;//": "https://IP/logo_white_fe6da1ec.png",   //头像
    private String userId;//": "170817164144587101AH",             //用户ID
    private String userLatitude;//": 123.236986,                    //纬度
    private String userLongitude;//": 36.456789,                    //经度
    private String userPhone;//": "13219950722",                   //用户电话
    private String userRealname;//": "啦啦啦",                     //用户真事姓名
    private String userSex;//": 1,                                 //用户性别 0：女 1：男
    private String userSignature;//": "用户的签名"
    private String userName;//": "13219950722", //用户名
    private String roleId;//": "3",              //用户权限ID 3.机关  5个人
    private String token;//": "thisistoken",       //token
    private String isValid;//": 1,                 //是否有效 0否 1:是
    private String password;
    private String lon;
    private String lat;
    private String payPassword;//": "支付密码",                    //支付密码（为null是没有设置）
    private String banlance;//": "0",                              //余额
    private String openid;//": "微信标示",                             //微信标示
    private String aliToken;
    private String isSecrecy;//审核状态 0未申请 1审核中 2已通过 3未通过  //仅适用于机关单位
    private String userPassword;
    private String qrCode;//二维码标识
    private String msg;
    private String inviteCode;//邀请码
    private String isNew;  //"isNew": "是否是新用户 0：否 1：是"
    private String userIntegral;
    private String coupon;

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getIsSecrecy() {
        return isSecrecy;
    }

    public void setIsSecrecy(String isSecrecy) {
        this.isSecrecy = isSecrecy;
    }

    public String getAliToken() {
        return aliToken;
    }

    public void setAliToken(String aliToken) {
        this.aliToken = aliToken;
    }
    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getBanlance() {
        if (!TextUtils.isEmpty(banlance) && PatternUtil.isNumer(banlance)){
            return Utils.getMoney(Utils.chu(banlance, "100"));
        } else {
            banlance = "0.00";
        }
        return banlance;
    }

    public void setBanlance(String banlance) {
        this.banlance = banlance;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBindCompanyName() {
        return bindCompanyName;
    }

    public void setBindCompanyName(String bindCompanyName) {
        this.bindCompanyName = bindCompanyName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getBindCompanyId() {
        return bindCompanyId;
    }

    public void setBindCompanyId(String bindCompanyId) {
        this.bindCompanyId = bindCompanyId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getParentCompanyId() {
        return parentCompanyId;
    }

    public void setParentCompanyId(String parentCompanyId) {
        this.parentCompanyId = parentCompanyId;
    }

    public String getParentCompanyName() {
        return parentCompanyName;
    }

    public void setParentCompanyName(String parentCompanyName) {
        this.parentCompanyName = parentCompanyName;
    }

    public String getUserCompanyId() {
        return userCompanyId;
    }

    public void setUserCompanyId(String userCompanyId) {
        this.userCompanyId = userCompanyId;
    }

    public String getUserCompanyName() {
        return userCompanyName;
    }

    public void setUserCompanyName(String userCompanyName) {
        this.userCompanyName = userCompanyName;
    }

    public String getUserHeadImage() {
        return userHeadImage;
    }

    public void setUserHeadImage(String userHeadImage) {
        this.userHeadImage = userHeadImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(String userLatitude) {
        this.userLatitude = userLatitude;
    }

    public String getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(String userLongitude) {
        this.userLongitude = userLongitude;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getUserIntegral() {
        return userIntegral;
    }

    public void setUserIntegral(String userIntegral) {
        this.userIntegral = userIntegral;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }
}
