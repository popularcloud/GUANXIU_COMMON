package com.lwc.common.module.integral.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.lwc.common.R;
import com.lwc.common.module.integral.bean.UserIntegralBean;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 积分订单记录
 */
public class IntegralOrderAdapter extends SuperAdapter<UserIntegralBean.DataBean> {

    private Context context;
    public IntegralOrderAdapter(Context context, List<UserIntegralBean.DataBean> items, int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, UserIntegralBean.DataBean item) {
        ImageView img = holder.getView(R.id.iv_type);
        String jj="+";

        switch (item.getTransactionScene()){
            case 1: //订单
                img.setImageResource(R.drawable.ic_integral_order);
                if(item.getPaymentType().equals("0")){ //转入
                    holder.setText(R.id.txtTitle, "订单结算");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                    jj="-";
                }else{  //转出
                    holder.setText(R.id.txtTitle, "订单结算");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    jj="+";
                }
                break;
            case 2: //购买套餐
                img.setImageResource(R.drawable.ic_pakage_buy);
                if(item.getPaymentType().equals("0")){ //转入
                    holder.setText(R.id.txtTitle, "购买套餐");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                    jj="-";
                }else{  //转出
                    holder.setText(R.id.txtTitle, "购买套餐");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    jj="+";
                }
                break;
            case 3: //积分兑换
                img.setImageResource(R.drawable.ic_integral_exchange);
                if(item.getPaymentType().equals("0")){ //转入
                    holder.setText(R.id.txtTitle, "积分兑换");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                    jj="-";
                }else{  //转出
                    holder.setText(R.id.txtTitle, "积分兑换");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    jj="+";
                }
                break;
            case 4: //签到
                img.setImageResource(R.drawable.ic_sign);
                if(item.getPaymentType().equals("0")){ //转入
                    holder.setText(R.id.txtTitle, "签到奖励");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                    jj="-";
                }else{  //转出
                    holder.setText(R.id.txtTitle, "签到奖励");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    jj="+";
                }
                break;
            case 5: //抽奖
                if(item.getPaymentType().equals("0")){ //转入
                    img.setImageResource(R.drawable.ic_luck_reduce);
                    holder.setText(R.id.txtTitle, "积分抽奖");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                    jj="-";
                }else{  //转出
                    holder.setText(R.id.txtTitle, "抽奖奖励");
                    img.setImageResource(R.drawable.ic_luck_add);
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    jj="+";
                }
                break;
            case 6: //抽奖
                if(item.getPaymentType().equals("0")){ //转入
                    img.setImageResource(R.drawable.ic_lease_order_integral);
                    holder.setText(R.id.txtTitle, "租赁订单");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                    jj="-";
                }else{  //转出
                    holder.setText(R.id.txtTitle, "租赁订单");
                    img.setImageResource(R.drawable.ic_lease_order_integral);
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    jj="+";
                }
                break;
            case 7: //抽奖
                if(item.getPaymentType().equals("0")){ //转入
                    img.setImageResource(R.drawable.ic_xufei_integral);
                    holder.setText(R.id.txtTitle, "订单缴费");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                    jj="-";
                }else{  //转出
                    holder.setText(R.id.txtTitle, "订单缴费");
                    img.setImageResource(R.drawable.ic_xufei_integral);
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    jj="+";
                }
                break;
            default:
                break;
        }
        holder.setText(R.id.tv_money, jj+ Utils.chu(item.getUserIntegral(),"100"));         //金额
        holder.setText(R.id.txtTime, item.getCreateTime()); //时间


       /* if ("1".equals(item.getTransactionMeans())) {
            img.setImageResource(R.drawable.account_red_envelopes);
            if (item.getPaymentType().equals("0")){
                holder.setText(R.id.txtTitle, "红包收入");
                holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
            } else {
                jj="-";
                holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                holder.setText(R.id.txtTitle, "红包支出");
            }
        } else if (item.getTransactionScene() == 2) {
            if (item.getTransactionMeans().equals("1")) {
                img.setImageResource(R.drawable.account_balance2);
                if (item.getPaymentType().equals("0")){
                    holder.setText(R.id.txtTitle, "订单收入");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                } else {
                    jj="-";
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    holder.setText(R.id.txtTitle, "余额支付");
                }
            } else if (item.getTransactionMeans().equals("2")) {
                img.setImageResource(R.drawable.account_alipay);
                if (item.getPaymentType().equals("0")){
                    holder.setText(R.id.txtTitle, "订单收入");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                } else {
                    jj="-";
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    holder.setText(R.id.txtTitle, "支付宝支付");
                }
            } else if (item.getTransactionMeans().equals("3")) {
                img.setImageResource(R.drawable.account_wechat);
                if (item.getPaymentType().equals("0")){
                    holder.setText(R.id.txtTitle, "订单收入");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                } else {
                    jj="-";
                    holder.setText(R.id.txtTitle, "微信支付");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                }
            }
        } else if(item.getTransactionScene() == 4){
            if (item.getTransactionMeans().equals("1")) {
                img.setImageResource(R.drawable.account_balance2);
                if (item.getPaymentType().equals("0")){
                    holder.setText(R.id.txtTitle, "订单收入");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                } else {
                    jj="-";
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    holder.setText(R.id.txtTitle, "余额支付");
                }
            } else if (item.getTransactionMeans().equals("2")) {
                img.setImageResource(R.drawable.account_alipay);
                if (item.getPaymentType().equals("0")){
                    holder.setText(R.id.txtTitle, "订单收入");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                } else {
                    jj="-";
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    holder.setText(R.id.txtTitle, "支付宝支付");
                }
            } else if (item.getTransactionMeans().equals("3")) {
                img.setImageResource(R.drawable.account_wechat);
                if (item.getPaymentType().equals("0")){
                    holder.setText(R.id.txtTitle, "订单收入");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                } else {
                    jj="-";
                    holder.setText(R.id.txtTitle, "微信支付");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                }
            }
        }else {
            if (item.getTransactionMeans().equals("1")) {
                img.setImageResource(R.drawable.account_balance2);
                if (item.getPaymentType().equals("0")){
                    holder.setText(R.id.txtTitle, "钱包收入");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                } else {
                    jj="-";
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    holder.setText(R.id.txtTitle, "钱包支出");
                }
            } else if (item.getTransactionMeans().equals("2")) {
                img.setImageResource(R.drawable.account_alipay);
                if (item.getPaymentType().equals("0")){
                    holder.setText(R.id.txtTitle, "支付宝充值");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                } else {
                    jj="-";
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    holder.setText(R.id.txtTitle, "支付宝提现");
                }
            } else if (item.getTransactionMeans().equals("3")) {
                img.setImageResource(R.drawable.account_wechat);
                if (item.getPaymentType().equals("0")){
                    holder.setText(R.id.txtTitle, "微信充值");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.color_33));
                } else {
                    jj="-";
                    holder.setText(R.id.txtTitle, "微信提现");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                }
            }
        }*/

    }
}
