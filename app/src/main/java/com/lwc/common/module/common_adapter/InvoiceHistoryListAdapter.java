package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.text.TextUtils;

import com.lwc.common.R;
import com.lwc.common.module.bean.InvoiceHistory;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 我的订单
 */
public class InvoiceHistoryListAdapter extends SuperAdapter<InvoiceHistory> {

    public InvoiceHistoryListAdapter(Context context, List<InvoiceHistory> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, InvoiceHistory item) {

        if (!TextUtils.isEmpty(item.getInvoiceStatus()) && item.getInvoiceStatus().equals("1")) {
            holder.setText(R.id.tv_status, "开票中");
        } else if (!TextUtils.isEmpty(item.getInvoiceStatus()) && item.getInvoiceStatus().equals("2")) {
            holder.setText(R.id.tv_status, "已开票");
        } else {
            holder.setText(R.id.tv_status, "系统处理中");
        }
        holder.setText(R.id.tv_order_time, item.getCreateTime());
        if (!TextUtils.isEmpty(item.getInvoiceType()) && item.getInvoiceType().equals("1")) {
            holder.setText(R.id.tv_type, "电子发票");
        } else {
            holder.setText(R.id.tv_type, "纸质发票");
        }
        String str = Utils.getMoney(Utils.chu(item.getInvoiceAmount(), "100"))+" 元";
        holder.setText(R.id.tv_money, Utils.getSpannableStringBuilder(0, str.length()-1, mContext.getResources().getColor(R.color.red_money), str, 18));
    }

}
