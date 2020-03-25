package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.lwc.common.R;
import com.lwc.common.module.bean.HasMsg;
import com.lwc.common.module.bean.MyMsg;
import com.lwc.common.utils.ImageLoaderUtil;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 我的订单
 */
public class MyMsgListAdapter extends SuperAdapter<MyMsg> {

    private final ImageLoaderUtil imageLoaderUtil;
    private Context context;
    private List<HasMsg> hasMsgs;

    public MyMsgListAdapter(Context context, List<MyMsg> items, List<HasMsg> hasMsgs , int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
        imageLoaderUtil = ImageLoaderUtil.getInstance();
        this.hasMsgs = hasMsgs;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, MyMsg item) {
        holder.setText(R.id.tv_title, item.getMessageTitle());
        holder.setText(R.id.tv_time, item.getCreateTime());
        if (!TextUtils.isEmpty(item.getMessageType()) && item.getMessageType().equals("1")) {
            holder.setImageResource(R.id.iv_icon, R.drawable.newstyle_mixiu);
        } else if (!TextUtils.isEmpty(item.getMessageType()) && item.getMessageType().equals("2")) {
            holder.setImageResource(R.id.iv_icon, R.drawable.newstyle_information);
        } else if (!TextUtils.isEmpty(item.getMessageType()) && item.getMessageType().equals("3")) {
            holder.setImageResource(R.id.iv_icon, R.drawable.newstyle_gift);
        } else {
            holder.setImageResource(R.id.iv_icon, R.drawable.newstyle_oder);
        }

        holder.setText(R.id.tv_desc, item.getMessageContent());

        //设置个数
        //HasMsg hasMsg = hasMsgs.get(layoutPosition);
        for(int i = 0;i<hasMsgs.size();i++){
            HasMsg hasMsg = hasMsgs.get(i);
            if(hasMsg.getType().equals(item.getMessageType())){
                if(hasMsg.getCount() > 0){
                    holder.setVisibility(R.id.iv_new_msg,View.VISIBLE);
                    if(hasMsg.getCount() > 99){
                        holder.setText(R.id.iv_new_msg,"...");
                    }else{
                        holder.setText(R.id.iv_new_msg,String.valueOf(hasMsg.getCount()));
                    }
                }else{
                    holder.setVisibility(R.id.iv_new_msg,View.INVISIBLE);
                }

            }
        }


    }
}
