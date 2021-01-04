package com.lwc.common.bean;

import android.text.TextUtils;

import com.lwc.common.utils.PatternUtil;
import com.lwc.common.utils.Utils;

import java.io.Serializable;

/**
 * 交易记录
 * 
 * @Description TODO
 * @author cc
 * @date 2016年4月24日
 * @Copyright: lwc
 */
public class TradingRecordBean implements Serializable {

	/** 变量描述 */
	private static final long serialVersionUID = 1L;

	private String createTime;//": "2017-11-30 11:44:53",         //创建时间
	private String transactionAmount;//": 888,                  //金额（前端要除100）
	private String transactionMeans;//": 1,                     //交易方式（1.余额 2.支付宝 3.微信）
	private String paymentType;//": 0,                        //收支类型 （0.转入 1.转出）
	private String transactionNo;//": "151201349368690300",     //交易单号
	private String transactionStatus;//": 1,                 //交易状态(1.已完成 2.处理中 3.审核失败)
	private String transactionRemark;//": "通过活动：双十二红包雨，获取奖金8.88元"      //备注
	private int transactionScene;//交易场景(1.红包 2.订单，3余额 )
	private int spendType;//类型（1路费 2红包 3积分兑换 4新用户补贴 5接单奖励 6邀请奖励 7邀请维修 8渠道分成 9租赁安装 10维修退费 11租赁退款 ）
	private int transactionType;// 交易类型（1.充值 2.充值结算 3.购买 4.支付 5.订单结算 6.红包 7.提现 8.缴纳保证金 9.保证金结算 10.扣除保证金 11.财务支出 12.退回保证金 13租赁支付 14租赁退款 15租赁退租

	public int getTransactionScene() {
		return transactionScene;
	}

	public void setTransactionScene(int transactionScene) {
		this.transactionScene = transactionScene;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTransactionAmount() {
		if (!TextUtils.isEmpty(transactionAmount) && PatternUtil.isNumer(transactionAmount)){
			return Utils.chu(transactionAmount, "100");
		}
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getTransactionMeans() {
		return transactionMeans;
	}

	public void setTransactionMeans(String transactionMeans) {
		this.transactionMeans = transactionMeans;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getTransactionRemark() {
		return transactionRemark;
	}

	public void setTransactionRemark(String transactionRemark) {
		this.transactionRemark = transactionRemark;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}
}
