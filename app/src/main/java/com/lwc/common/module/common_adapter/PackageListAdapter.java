package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.module.order.ui.activity.PackageDetailActivity;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 购买套餐Adapter
 */
public class PackageListAdapter extends SuperAdapter<PackageBean> {

    private Context context;
    public PackageListAdapter(Context context, List<PackageBean> items, int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, PackageBean item) {
        TextView tv_desc = holder.findViewById(R.id.tv_desc);
        Button btn_gm = holder.findViewById(R.id.btn_gm);
        String count;
        if (item.getRemissionCount() == 0) {
            count = "无限";
        } else {
            count = item.getRemissionCount()+"";
        }
        if (item.getPackageType() == 1) {
            holder.setBackgroundResource(R.id.rl_content, R.drawable.cheng_xiao);
            btn_gm.setTextColor(context.getResources().getColor(R.color.chengColor));
            holder.setText(R.id.tv_count, "减免上门费："+count+"次");
        } else if (item.getPackageType() == 2) {
            holder.setBackgroundResource(R.id.rl_content, R.drawable.zi_xiao);
            btn_gm.setTextColor(context.getResources().getColor(R.color.red_money));
            holder.setText(R.id.tv_count, "减免维修费："+count+"次");
        } else if (item.getPackageType() == 3) {
            holder.setBackgroundResource(R.id.rl_content, R.drawable.lan_xiao);
            btn_gm.setTextColor(context.getResources().getColor(R.color.blue_00aaf5));
            holder.setText(R.id.tv_count, "减免上门维修费："+count+"次");
        }
        holder.setText(R.id.tv_title, item.getPackageName());

        holder.setText(R.id.tv_time, "有效期："+item.getValidDay()+"天");


        String priceString = "¥"+Utils.getMoney(item.getPackagePrice());
        SpannableStringBuilder stringBuilder=new SpannableStringBuilder(priceString);
        AbsoluteSizeSpan ab=new AbsoluteSizeSpan(12,true);
        //文本字体绝对的大小
        stringBuilder.setSpan(ab,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.setText(R.id.tv_money, stringBuilder);

        holder.setText(R.id.tv_yingjian, "注：不包括硬件更换费用");
        tv_desc.setTag(item);
        tv_desc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PackageBean pack = (PackageBean) v.getTag();
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("package", pack);
                IntentUtil.gotoActivity(context, PackageDetailActivity.class, bundle2);
            }
        });
        btn_gm.setTag(item);
        btn_gm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageBean pack = (PackageBean) v.getTag();
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("package", pack);
                IntentUtil.gotoActivity(context, PackageDetailActivity.class, bundle2);
            }
        });
    }
}
