package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.lwc.common.R;
import com.lwc.common.module.bean.MyMsg;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 我的订单
 */
public class MsgListAdapter extends SuperAdapter<MyMsg> {

    private final ImageLoaderUtil imageLoaderUtil;
    private Context context;
    public MsgListAdapter(Context context, List<MyMsg> items, int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
        imageLoaderUtil = ImageLoaderUtil.getInstance();
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, MyMsg item) {
        String [] arr = item.getCreateTime().trim().split(" ");
        String date = "";
        if (layoutPosition > 0) {
            date = getItem(layoutPosition-1).getCreateTime().trim().split(" ")[0];
        }
        if (layoutPosition == 0 || (date != null && !date.equals(arr[0]))) {
            holder.setVisibility(R.id.tv_date, View.VISIBLE);
            holder.setText(R.id.tv_date, arr[0]);
        } else {
            holder.setVisibility(R.id.tv_date, View.GONE);
        }
        holder.setText(R.id.tv_title, item.getMessageTitle());
        holder.setText(R.id.tv_time, arr[1]);
        if (!TextUtils.isEmpty(item.getMessageType()) && item.getMessageType().equals("1")) {
            holder.setVisibility(R.id.tv_title, View.VISIBLE);
            holder.setVisibility(R.id.tv_time, View.VISIBLE);
            holder.setVisibility(R.id.rl_more, View.GONE);
        } else if (!TextUtils.isEmpty(item.getMessageType()) && item.getMessageType().equals("2")) {
            holder.setVisibility(R.id.tv_title, View.VISIBLE);
            holder.setVisibility(R.id.tv_time, View.VISIBLE);
        } else if (!TextUtils.isEmpty(item.getMessageType()) && item.getMessageType().equals("3")) {
            holder.setVisibility(R.id.tv_title, View.GONE);
            holder.setVisibility(R.id.tv_time, View.GONE);
        } else {
            holder.setVisibility(R.id.tv_title, View.VISIBLE);
            holder.setVisibility(R.id.tv_time, View.VISIBLE);
        }
        holder.setText(R.id.tv_desc, item.getMessageContent());
        LinearLayout llContent = holder.findViewById(R.id.ll_content);
        if(layoutPosition == getCount()-1) {
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setMargins(Utils.dip2px(context, 10f), Utils.dip2px(context, 10f), Utils.dip2px(context, 10f),Utils.dip2px(context, 30f));
            llContent.setLayoutParams(layout);
        } else {
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setMargins(Utils.dip2px(context, 10f), Utils.dip2px(context, 10f), Utils.dip2px(context, 10f),0);
            llContent.setLayoutParams(layout);
        }

        if("0".equals(item.getIsRead())){
            holder.setVisibility(R.id.tv_isMsgRed,View.VISIBLE);
        }else{
            holder.setVisibility(R.id.tv_isMsgRed,View.GONE);
        }
    }
}
