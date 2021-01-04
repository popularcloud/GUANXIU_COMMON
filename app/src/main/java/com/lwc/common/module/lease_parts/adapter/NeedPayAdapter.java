package com.lwc.common.module.lease_parts.adapter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.interf.OnItemCheckedCallBack;
import com.lwc.common.module.lease_parts.bean.NeedPayBean;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author younge
 * @date 2018/12/20 0020
 * @email 2276559259@qq.com
 */
public class NeedPayAdapter extends SuperAdapter<NeedPayBean>{

    private OnItemCheckedCallBack onItemCheckedCallBack;
    public NeedPayAdapter(Context context, List<NeedPayBean> items, int layoutResId, OnItemCheckedCallBack onItemCheckedCallBack) {
        super(context, items, layoutResId);
        this.onItemCheckedCallBack = onItemCheckedCallBack;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, final int layoutPosition, NeedPayBean item) {

        CheckBox cb_isAdd = holder.findViewById(R.id.cb_isAdd);
        TextView tv_price = holder.findViewById(R.id.tv_price);
        TextView tv_time = holder.findViewById(R.id.tv_time);

        tv_price.setText(item.getMoney());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

     /*   Date nowDate = new Date();
        Date listDate = item.getTime();
        int compareDate = listDate.compareTo(nowDate);*/
        if(item.isPaidIn()){
            cb_isAdd.setEnabled(false);
            tv_price.setTextColor(mContext.getResources().getColor(R.color.line_cc));
            tv_time.setTextColor(mContext.getResources().getColor(R.color.line_cc));
        }else{
            cb_isAdd.setEnabled(true);
            tv_price.setTextColor(mContext.getResources().getColor(R.color.black));
            tv_time.setTextColor(mContext.getResources().getColor(R.color.black));
        }

        cb_isAdd.setOnCheckedChangeListener(null);
        if(!item.isPaidIn()){
            if(item.isChecked()){
                cb_isAdd.setChecked(true);
            }else{
                cb_isAdd.setChecked(false);
            }
        }else{
            cb_isAdd.setChecked(false);
            item.setChecked(false);
        }


        String datas = format.format(item.getTime());
        tv_time.setText(datas);

        cb_isAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onItemCheckedCallBack.onItemChecked(layoutPosition,isChecked);

            }
        });
    }
}
