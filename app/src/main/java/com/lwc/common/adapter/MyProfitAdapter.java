package com.lwc.common.adapter;

import android.content.Context;

import com.lwc.common.R;
import com.lwc.common.module.bean.MyProfitBean;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 积分订单记录
 */
public class MyProfitAdapter extends SuperAdapter<MyProfitBean.PageBean> {

    private Context context;
    public MyProfitAdapter(Context context, List<MyProfitBean.PageBean> items, int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, MyProfitBean.PageBean item) {
        holder.setText(R.id.tv_money,"+"+Utils.getMoney(Utils.chu(String.valueOf(item.getTransactionAmount()),"100")));         //金额
        holder.setText(R.id.txtTime, item.getCreateTime()); //时间
    }
}
