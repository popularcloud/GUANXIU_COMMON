package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.lwc.common.R;
import com.lwc.common.module.bean.OrderState;
import com.lwc.common.utils.ImageLoaderUtil;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 订单状态
 */
public class OrderStateAdapter extends SuperAdapter<OrderState> {

    private ImageLoaderUtil imageLoaderUtil;
    public OrderStateAdapter(Context context, List<OrderState> items, int layoutResId) {
        super(context, items, layoutResId);
        imageLoaderUtil = ImageLoaderUtil.getInstance();
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, OrderState item) {

        //第一项隐藏上面的线
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

        //除了最后一项外全部设置以下view
        if (item.getComment() != null) {

            //显示 用户已评价 界面的相关操作
            holder.setVisibility(R.id.lLinearStars, View.VISIBLE);
            holder.setVisibility(R.id.lLayoutState, View.GONE);
            holder.setVisibility(R.id.viewLine2, View.INVISIBLE);
            holder.setVisibility(R.id.lLayoutLines, View.GONE);

            holder.setText(R.id.txtStatus1, item.getProcessTitle());    //订单状态
            holder.setText(R.id.txtTime1, item.getCreateTime()); //时间
//            holder.setRating(R.id.rBarLevel, item.getComment().getExpertise_level());  //专业水平

           // holder.setRating(R.id.rBarSevice, item.getComment().getSynthesize_grade());  //服务态度
            com.hedgehog.ratingbar.RatingBar ratingBar = holder.findViewById(R.id.rBarSevice);

            if (item.getComment() != null) {
                ratingBar.setStarCount(5);
                ratingBar.setStar(item.getComment().getSynthesize_grade());
            } else {
                ratingBar.setStar(0);
            }

//            holder.setRating(R.id.rBarSpeed, item.getComment().getSpecialty());  //上门速度
            if (!TextUtils.isEmpty(item.getComment().getComment_content())) {
                holder.setVisibility(R.id.txtMsgPrise, View.VISIBLE);
                holder.setText(R.id.txtMsgPrise, "评价内容：" + item.getComment().getComment_content());
            } else {
                holder.setVisibility(R.id.txtMsgPrise, View.GONE);
            }
            String labels = item.getComment().getComment_labels();
            if (!TextUtils.isEmpty(labels)) {
                String [] arr= null;
                if (labels.contains("_")) {
                    arr = labels.split("_");
                } else if (labels.contains(",")) {
                    arr = labels.split(",");
                } else {
                    arr = labels.split(" ");
                }
                if (arr.length == 1) {
                    holder.setVisibility(R.id.tagview, View.VISIBLE);
                    holder.setText(R.id.tagview, arr[0]);
                    holder.setVisibility(R.id.ll_tagContent, View.VISIBLE);
                } else if (arr.length == 2) {
                    holder.setVisibility(R.id.tagview, View.VISIBLE);
                    holder.setText(R.id.tagview, arr[0]);
                    holder.setVisibility(R.id.tagview2, View.VISIBLE);
                    holder.setText(R.id.tagview2, arr[1]);
                    holder.setVisibility(R.id.ll_tagContent, View.VISIBLE);
                } else if (arr.length == 3) {
                    holder.setVisibility(R.id.tagview, View.VISIBLE);
                    holder.setText(R.id.tagview, arr[0]);
                    holder.setVisibility(R.id.tagview2, View.VISIBLE);
                    holder.setText(R.id.tagview2, arr[1]);
                    holder.setVisibility(R.id.tagview3, View.VISIBLE);
                    holder.setText(R.id.tagview3, arr[2]);
                    holder.setVisibility(R.id.ll_tagContent, View.VISIBLE);
                }
            }
        } else {
            holder.setVisibility(R.id.lLinearStars, View.GONE);
            holder.setVisibility(R.id.lLayoutLines, View.VISIBLE);
            holder.setVisibility(R.id.lLayoutState, View.VISIBLE);
//            holder.setVisibility(R.id.viewLine2, View.VISIBLE);
            holder.setText(R.id.txtStatus, item.getProcessTitle());    //订单状态
            holder.setText(R.id.txtMsg, item.getProcessContent());         //信息
            holder.setText(R.id.txtTime, item.getCreateTime()); //时间
        }
    }
}
