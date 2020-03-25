package com.lwc.common.module.bean;

import android.text.TextUtils;

import com.lwc.common.bean.PartsDetailBean;
import com.lwc.common.utils.PatternUtil;
import com.lwc.common.utils.Utils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 订单
 */
public class Order implements Serializable{

    public static final int STATUS_DAIJIEDAN = 1;//待接单
    public static final int STATUS_JIEDAN = 2;//已接单
    public static final int STATUS_CHULI = 3;//处理中
    public static final int STATUS_ZHUANDAN = 4;//转单待确认
    public static final int STATUS_TONGYIFENJI = 5;//同意分级待对方确认
    public static final int STATUS_BAOJIA = 6;//已报价待确认
    public static final int STATUS_GUAQI = 7;//已挂起
    public static final int STATUS_YIWANCHENGDAIQUEREN = 8;//已完成待确认
    public static final int STATUS_DAIFANXIU = 9;//待返修
    public static final int STATUS_JUJUEFANXIU = 10;//拒绝返修
    public static final int STATUS_YIWANCHENG = 11;//已完成
    public static final int STATUS_YIPINGJIA = 12;//已评价
    public static final int STATUS_SHOUHOU = 13;//售后
    public static final int STATUS_YIQUXIAO = 14;//已取消
    public static final int STATUS_JUJUEWANGCHENG = 15;//用户拒绝完成
    public static final int STATUS_DAODAXIANCHANG = 16;//到达现场
    public static final int STATUS_JIANCEBAOJIA = 17;//检测并报价
    public static final int STATUS_QUERENBAOJIA = 18;//用户确认报价并支付
    public static final int STATUS_SHENQINGFANCHANG = 19;//申请返厂待确认
    public static final int STATUS_FANCHANGZHONG = 20;//返厂中
    public static final int STATUS_SONGHUIANZHUANG = 21;//送回安装待确认
    public static final int STATUS_FANCHANGBAOJIA = 23;//返厂报价



    // 用户支付完成后的状态  18  11  12 13 3 8

    private String orderId;//": "170831102758556102HB",         //订单ID
    private String statusId;//": "状态ID"                       //状态ID
    private String statusName;//": "状态名称",                 //状态名称
    private String deviceTypeName;//": "设备类型名称"          //设备类型名称
    private String reqairName;//": "故障名称",                   //故障名称
    private String orderDescription;//": "订单备注"               //订单备注
    private String createTime;//": "订单创建时间",               //订单创建时间
    private String maintenanceHeadImage;// ": "维修时头像"     //维修时头像
    private String maintenanceName;// ": "维修师名称",          //维修师名称
    private String companyName;// ": "维修师公司名称"          //维修师公司名称
    private String maintenancePhone;// ": "维修师电话",          //维修时电话
    private String orderLatitude;// ": "订单经度",                 //订单经度
    private String orderLongitude;// ": "订单纬度"                //订单纬度
    private String maintenanceLatitude;// ": "维修员经度",         //维修员经度
    private String maintenanceLongitude ;//": "维修员纬度"        //维修员纬度
    private String maintenanceStar;// ”:“维修员评分”             //维修员评分
    private String orderImage;
    private String orderCompanyName;//
    private String orderAmount;
    private String orderContactName;// ": "订单联系人",        //订单联系人
    private String orderContactAddress;// ": "订单联系地址",    //订单联系地址
    private String orderContactPhone;//": "订单联系电话",      //订单联系电话
    private String userCompanyName;// ": "用户公司名称",       //用户公司名称
    private String companyAddress;// ": "用户公司地址"          //用户公司地址
    private boolean hasRecord;//": "是否有检修记录 Bool类型",
    private String visitCost;//": "上门费",
    private String maintainCost;//": "维修费",
    private String otherCost;//": "其他费用",
    private boolean hasSettlement;//": "是否有结算记录  Bool类型",
    private String sumCost;//": "总金额",
    private int settlementStatus;//": "结算状态（1.未支付 2.已支付）",
    private int settlementPlatform;//": "支付平台(1.余额 2.支付宝 3.微信)
    private String faultType;// "故障类型(1.软件 2.硬件 3.其他）",
    private String remark;//费用详情
    private String orderType;
    private String maintenanceId;
    private String isSecrecy; //是否是私密定单（0：否 1：是）
    private int isAppeal; //是否申诉（0：未申诉 1.申诉中 2.申诉完成）
    private String discountAmount;//优惠金额
    private List<Malfunction> orderRepairs;
    private String hardwareCost;
    private String packageType;
    private String isShare;
    private String deviceTypeMold;
    private List<PartsDetailBean> accessories;//配件信息
    private String packageId;
    private String packagePrice;
    private String packageName;



    public String getHardwareCost() {
        return hardwareCost;
    }

    public void setHardwareCost(String hardwareCost) {
        this.hardwareCost = hardwareCost;
    }

    public List<Malfunction> getOrderRepairs() {
        return orderRepairs;
    }

    public void setOrderRepairs(List<Malfunction> orderRepairs) {
        this.orderRepairs = orderRepairs;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getIsAppeal() {
        return isAppeal;
    }

    public void setIsAppeal(int isAppeal) {
        this.isAppeal = isAppeal;
    }

    public String getIsSecrecy() {
        return isSecrecy;
    }

    public void setIsSecrecy(String isSecrecy) {
        this.isSecrecy = isSecrecy;
    }

    public String getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(String maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    public int getSettlementPlatform() {
        return settlementPlatform;
    }

    public void setSettlementPlatform(int settlementPlatform) {
        this.settlementPlatform = settlementPlatform;
    }

    public int getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(int settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public String getSumCost() {
        return sumCost;
    }

    public void setSumCost(String sumCost) {
        this.sumCost = sumCost;
    }

    public boolean isHasSettlement() {
        return hasSettlement;
    }

    public void setHasSettlement(boolean hasSettlement) {
        this.hasSettlement = hasSettlement;
    }

    public String getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(String otherCost) {
        this.otherCost = otherCost;
    }

    public String getMaintainCost() {
        return maintainCost;
    }

    public void setMaintainCost(String maintainCost) {
        this.maintainCost = maintainCost;
    }

    public String getVisitCost() {
        return visitCost;
    }

    public void setVisitCost(String visitCost) {
        this.visitCost = visitCost;
    }

    public boolean isHasRecord() {
        return hasRecord;
    }

    public void setHasRecord(boolean hasRecord) {
        this.hasRecord = hasRecord;
    }

    public String getOrderContactName() {
        return orderContactName;
    }

    public void setOrderContactName(String orderContactName) {
        this.orderContactName = orderContactName;
    }

    public String getOrderContactAddress() {
        return orderContactAddress;
    }

    public void setOrderContactAddress(String orderContactAddress) {
        this.orderContactAddress = orderContactAddress;
    }

    public String getOrderContactPhone() {
        return orderContactPhone;
    }

    public void setOrderContactPhone(String orderContactPhone) {
        this.orderContactPhone = orderContactPhone;
    }

    public String getUserCompanyName() {
        return userCompanyName;
    }

    public void setUserCompanyName(String userCompanyName) {
        this.userCompanyName = userCompanyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getOrderAmount() {
        if (!TextUtils.isEmpty(orderAmount) && PatternUtil.isNumer(orderAmount)){
            return Utils.chu(orderAmount, "100");
        } else {
            orderAmount = "0";
        }
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }


    public String getRepairCompanyName() {
        return repairCompanyName;
    }

    public void setRepairCompanyName(String repairCompanyName) {
        this.repairCompanyName = repairCompanyName;
    }

    private String repairCompanyName;//用户报修指定的维修公司

    public String getOrderImage() {
        return orderImage;
    }

    public void setOrderImage(String orderImage) {
        this.orderImage = orderImage;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getReqairName() {
        return reqairName;
    }

    public void setReqairName(String reqairName) {
        this.reqairName = reqairName;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMaintenanceHeadImage() {
        return maintenanceHeadImage;
    }

    public void setMaintenanceHeadImage(String maintenanceHeadImage) {
        this.maintenanceHeadImage = maintenanceHeadImage;
    }

    public String getMaintenanceName() {
        return maintenanceName;
    }

    public void setMaintenanceName(String maintenanceName) {
        this.maintenanceName = maintenanceName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOrderLatitude() {
        return orderLatitude;
    }

    public void setOrderLatitude(String orderLatitude) {
        this.orderLatitude = orderLatitude;
    }

    public String getMaintenancePhone() {
        return maintenancePhone;
    }

    public void setMaintenancePhone(String maintenancePhone) {
        this.maintenancePhone = maintenancePhone;
    }

    public String getOrderLongitude() {
        return orderLongitude;
    }

    public void setOrderLongitude(String orderLongitude) {
        this.orderLongitude = orderLongitude;
    }

    public String getMaintenanceLatitude() {
        return maintenanceLatitude;
    }

    public void setMaintenanceLatitude(String maintenanceLatitude) {
        this.maintenanceLatitude = maintenanceLatitude;
    }

    public String getMaintenanceLongitude() {
        return maintenanceLongitude;
    }

    public void setMaintenanceLongitude(String maintenanceLongitude) {
        this.maintenanceLongitude = maintenanceLongitude;
    }

    public String getMaintenanceStar() {
        return maintenanceStar;
    }

    public void setMaintenanceStar(String maintenanceStar) {
        this.maintenanceStar = maintenanceStar;
    }

    public String getOrderCompanyName() {
        return orderCompanyName;
    }

    public void setOrderCompanyName(String orderCompanyName) {
        this.orderCompanyName = orderCompanyName;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public String getDeviceTypeMold() {
        return deviceTypeMold;
    }

    public void setDeviceTypeMold(String deviceTypeMold) {
        this.deviceTypeMold = deviceTypeMold;
    }

    public List<PartsDetailBean> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<PartsDetailBean> accessories) {
        this.accessories = accessories;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
