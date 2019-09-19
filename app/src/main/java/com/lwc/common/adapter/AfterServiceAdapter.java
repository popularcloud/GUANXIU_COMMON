package com.lwc.common.adapter;

import android.content.Context;
import android.view.View;

import com.lwc.common.R;
import com.lwc.common.module.bean.AfterService;
import com.lwc.common.utils.ImageLoaderUtil;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 订单状态
 */
public class AfterServiceAdapter extends SuperAdapter<AfterService> {

    private ImageLoaderUtil imageLoaderUtil;

    public AfterServiceAdapter(Context context, List<AfterService> items, int layoutResId) {
        super(context, items, layoutResId);

        imageLoaderUtil = ImageLoaderUtil.getInstance();
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, AfterService item) {
        //ordertype 1待接单，2已接单，3处理中，4.已分级待确认，5.待对方确认，6.已报价待确认，7.已挂起， 8.已完成待确认，9.待返修，10.拒绝返修，11.已完成，12.已评价，20.已取消

        //第一项隐藏上面的线,订单提交等待接单饿了
        if (layoutPosition == 0) {
            holder.setVisibility(R.id.viewLine1, View.INVISIBLE);
            if (layoutPosition == (getCount()-1)) {
                holder.setVisibility(R.id.viewLine2, View.INVISIBLE);
            } else {
                holder.setVisibility(R.id.viewLine2, View.VISIBLE);
            }
        } else if (layoutPosition == (getCount()-1)) {
            holder.setVisibility(R.id.viewLine1, View.VISIBLE);
            holder.setVisibility(R.id.viewLine2, View.INVISIBLE);
        } else {
            holder.setVisibility(R.id.viewLine1, View.VISIBLE);
            holder.setVisibility(R.id.viewLine2, View.VISIBLE);
        }
        holder.setVisibility(R.id.lLinearStars, View.GONE);
        holder.setVisibility(R.id.lLayoutLines, View.VISIBLE);
        holder.setVisibility(R.id.lLayoutState, View.VISIBLE);
        holder.setText(R.id.txtStatus, item.getProcessTitle());    //订单状态
        holder.setText(R.id.txtMsg, item.getProcessContent());         //信息
        holder.setText(R.id.txtTime, item.getCreateTime()); //时间

    }
}
