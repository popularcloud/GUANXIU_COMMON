package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.widget.TextView;

import com.lwc.common.R;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * @author younge
 * @date 2018/12/25 0025
 * @email 2276559259@qq.com
 */
public class SpecAdapter extends SuperAdapter<String>{


    public SpecAdapter(Context context, List<String> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, String item) {
        TextView tv_title = holder.findViewById(R.id.tv_title);
        if(layoutPosition > 3){
            return;
        }else{
            tv_title.setText(item);
        }

    }
}
