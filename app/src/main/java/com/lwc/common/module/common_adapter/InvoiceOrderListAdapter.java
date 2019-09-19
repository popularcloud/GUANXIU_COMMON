package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.view.View;

import com.lwc.common.R;
import com.lwc.common.module.bean.InvoiceOrder;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 我的订单  发票
 */
public class InvoiceOrderListAdapter extends SuperAdapter<InvoiceOrder> {

    public InvoiceOrderListAdapter(Context context, List<InvoiceOrder> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, InvoiceOrder item) {

        holder.setText(R.id.tv_order_id, "订单号： " + item.getOrderId());  //订单号
        holder.setText(R.id.tv_order_time, item.getCreateTime());
        if (isSelect != null && isSelect.size() > 0) {
            if (isSelect.get(layoutPosition).equals("1")) {
                holder.setImageResource(R.id.iv_checked, R.drawable.invoice_order_selected1);
            } else {
                holder.setImageResource(R.id.iv_checked, R.drawable.invoice_order_noselected1);
            }
        } else {
            holder.setVisibility(R.id.iv_checked, View.GONE);
        }
        String type = item.getDeviceTypeName();
        if (item.getReqairName() != null && !item.getReqairName().equals("")){
            type = type +"-"+item.getReqairName();
        }
        holder.setText(R.id.tv_type, type); //设备类型
        String str = Utils.getMoney(item.getOrderAmount())+" 元";
        holder.setText(R.id.tv_money, Utils.getSpannableStringBuilder(0, str.length()-1, mContext.getResources().getColor(R.color.red_money), str, 18));
    }

    private List<String> isSelect = new ArrayList<>();
    public void setDateSelect(List<String> isSelect) {
        this.isSelect = isSelect;
    }
}
