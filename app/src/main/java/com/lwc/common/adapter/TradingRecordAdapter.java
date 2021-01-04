package com.lwc.common.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.lwc.common.R;
import com.lwc.common.bean.TradingRecordBean;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 订单状态
 */
public class TradingRecordAdapter extends SuperAdapter<TradingRecordBean> {

    private ImageLoaderUtil imageLoaderUtil;
    private Context context;
    public TradingRecordAdapter(Context context, List<TradingRecordBean> items, int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
        imageLoaderUtil = ImageLoaderUtil.getInstance();
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, TradingRecordBean item) {
        ImageView img = holder.getView(R.id.iv_type);
        String jj="+";
        if (item.getTransactionScene() == 1) {
            img.setImageResource(R.drawable.ic_paytype_red);
            if (item.getPaymentType().equals("0")){
                holder.setText(R.id.txtTitle, "红包收入");
                holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
            } else {
                jj="-";
                holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.all_black));
                holder.setText(R.id.txtTitle, "红包支出");
            }
        } else if (item.getTransactionScene() == 2) {
            if (item.getTransactionMeans().equals("1")) {
                img.setImageResource(R.drawable.ic_paytype_balance);
                if (item.getPaymentType().equals("0")){
                    holder.setText(R.id.txtTitle, "订单收入");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                } else {
                    jj="-";
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.all_black));
                    holder.setText(R.id.txtTitle, "余额支付");
                }
            } else if (item.getTransactionMeans().equals("2")) {
                img.setImageResource(R.drawable.ic_paytype_ali);
                if (item.getPaymentType().equals("0")){
                    holder.setText(R.id.txtTitle, "订单收入");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                } else {
                    jj="-";
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.all_black));
                    holder.setText(R.id.txtTitle, "支付宝支付");
                }
            } else if (item.getTransactionMeans().equals("3")) {
                img.setImageResource(R.drawable.ic_paytype_wechat);
                if (item.getPaymentType().equals("0")){
                    holder.setText(R.id.txtTitle, "订单收入");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                } else {
                    jj="-";
                    holder.setText(R.id.txtTitle, "微信支付");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.all_black));
                }
            }
        }else if (item.getTransactionScene() == 4) {
            holder.setText(R.id.txtTitle, "邀请奖励");
            holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
            img.setImageResource(R.drawable.ic_invitation_reward);
        }else if (item.getTransactionScene() == 9) {
            if(item.getTransactionType() == 14 || item.getTransactionType() == 15){
                holder.setText(R.id.txtTitle, "租赁退款");
                holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                img.setImageResource(R.drawable.ic_order_return);
            }else{
                if (item.getTransactionMeans().equals("1")) {
                    img.setImageResource(R.drawable.ic_paytype_balance);
                    if (item.getPaymentType().equals("0")){
                        holder.setText(R.id.txtTitle, "钱包收入");
                        holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    } else {
                        jj="-";
                        holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.all_black));
                        holder.setText(R.id.txtTitle, "钱包支出");
                    }
                } else if (item.getTransactionMeans().equals("2")) {
                    img.setImageResource(R.drawable.ic_paytype_ali);
                    if (item.getPaymentType().equals("0")){
                        holder.setText(R.id.txtTitle, "支付宝充值");
                        holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    } else {
                        jj="-";
                        holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.all_black));
                        holder.setText(R.id.txtTitle, "支付宝提现");
                    }
                } else if (item.getTransactionMeans().equals("3")) {
                    img.setImageResource(R.drawable.ic_paytype_wechat);
                    if (item.getPaymentType().equals("0")){
                        holder.setText(R.id.txtTitle, "微信充值");
                        holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                    } else {
                        jj="-";
                        holder.setText(R.id.txtTitle, "微信提现");
                        holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.all_black));
                    }
                }
            }

        } else {
            if (item.getTransactionMeans().equals("1")) {
                img.setImageResource(R.drawable.ic_paytype_balance);
                if (item.getPaymentType().equals("0")){
                    holder.setText(R.id.txtTitle, "钱包收入");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                } else {
                    jj="-";
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.all_black));
                    holder.setText(R.id.txtTitle, "钱包支出");
                }
            } else if (item.getTransactionMeans().equals("2")) {
                img.setImageResource(R.drawable.ic_paytype_ali);
                if (item.getPaymentType().equals("0")){
                    holder.setText(R.id.txtTitle, "支付宝充值");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                } else {
                    jj="-";
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.all_black));
                    holder.setText(R.id.txtTitle, "支付宝提现");
                }
            } else if (item.getTransactionMeans().equals("3")) {
                img.setImageResource(R.drawable.ic_paytype_wechat);
                if (item.getPaymentType().equals("0")){
                    holder.setText(R.id.txtTitle, "微信充值");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.red_money));
                } else {
                    jj="-";
                    holder.setText(R.id.txtTitle, "微信提现");
                    holder.setTextColor(R.id.tv_money, context.getResources().getColor(R.color.all_black));
                }
            }
        }
        holder.setText(R.id.tv_money, jj+ Utils.getMoney(item.getTransactionAmount()));         //金额
        holder.setText(R.id.txtTime, item.getCreateTime()); //时间

    }
}
