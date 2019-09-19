package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.module.order.ui.activity.SelectPackageListActivity;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 我的套餐Adapter
 */
public class MyPackageListAdapter extends SuperAdapter<PackageBean> {

    private Context context;
    private int type = 0;
    public MyPackageListAdapter(Context context, List<PackageBean> items, int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
    }

    public void setType(int type) {
       this.type = type;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, PackageBean item) {
        TextView tv_find = holder.findViewById(R.id.tv_find);
        String count;
        String syCount;
        if (item.getRemissionCount() == 0) {
            count = "无限";
        } else {
            count = item.getRemissionCount()+"";
        }
        if (item.getResidueRemissionCount() == 0) {
            syCount = "无限";
        } else {
            syCount = item.getResidueRemissionCount()+"";
        }
        if (item.getPackageType() == 1) {
            holder.setBackgroundResource(R.id.rl_content, R.drawable.cheng_xiao);
            holder.setText(R.id.tv_count, "减免上门费："+count+"次");
        } else if (item.getPackageType() == 2) {
            holder.setBackgroundResource(R.id.rl_content, R.drawable.zi_xiao);
            holder.setText(R.id.tv_count, "减免维修费："+count+"次");
        } else if (item.getPackageType() == 3) {
            holder.setBackgroundResource(R.id.rl_content, R.drawable.lan_xiao);
            holder.setText(R.id.tv_count, "减免上门维修费："+count+"次");
        }
        holder.setText(R.id.tv_title, item.getPackageName());
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        SimpleDateFormat sdf2 = new SimpleDateFormat( "yyyy.MM.dd" );
        try {
            Date date = sdf.parse(item.getCreateTime());
            holder.setText(R.id.tv_time, "有效时间："+sdf2.format(date)+" - "+ sdf2.format(sdf.parse(item.getExpirationTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.setText(R.id.tv_yingjian, "注：不包括硬件更换费用");
        if (type == 2) {
            // tv_find.setVisibility(View.GONE);
           // holder.setVisibility(R.id.tv_desc, View.GONE);
            holder.setBackgroundResource(R.id.rl_content, R.drawable.exp_package);
        } else {
            if (!TextUtils.isEmpty(item.getErrorMsg())) {
                holder.setBackgroundResource(R.id.rl_content, R.drawable.exp_package);
                holder.setVisibility(R.id.tv_msg, View.GONE);
                holder.setText(R.id.tv_msg, item.getErrorMsg());
            } else {
                holder.setVisibility(R.id.tv_msg, View.GONE);
            }
            holder.setVisibility(R.id.tv_desc, View.VISIBLE);
            holder.setText(R.id.tv_desc, "剩余："+syCount+"次");

            if(context instanceof SelectPackageListActivity){
                tv_find.setVisibility(View.GONE);
            }
            //需求需要隐藏查看使用区域
           // tv_find.setVisibility(View.GONE);
          /*  tv_find.setTag(item);
            tv_find.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PackageBean pack = (PackageBean) v.getTag();
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("package", pack);
                    bundle2.putInt("type", 1);
                    IntentUtil.gotoActivity(context, PackageDetailActivity.class, bundle2);
                }
            });*/
        }
    }
}
