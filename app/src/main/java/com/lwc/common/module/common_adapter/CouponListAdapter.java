package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.bean.Coupon;
import com.lwc.common.module.repairs.ui.activity.ApplyForMaintainActivity;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 优惠券
 */
public class CouponListAdapter extends SuperAdapter<Coupon> {

    private Context context;
    public CouponListAdapter(Context context, List<Coupon> items, int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, Coupon item) {
        TextView txtDiscountAmount = holder.findViewById(R.id.txtDiscountAmount);

        String str = "¥ "+item.getDiscountAmount();
        SpannableStringBuilder stringBuilder=new SpannableStringBuilder(str);
        AbsoluteSizeSpan ab=new AbsoluteSizeSpan(30,true);
        //文本字体绝对的大小
        stringBuilder.setSpan(ab,1,str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtDiscountAmount.setText(stringBuilder);
        //txtDiscountAmount.setText(Utils.getSpannableStringBuilder(1, str.length(), context.getResources().getColor(R.color.white), str, 34));
        TextView tv_ljsy = holder.findViewById(R.id.tv_ljsy);
    /*    if (item.getCouponType() == 1) {
            holder.setBackgroundResource(R.id.rl_bg, R.drawable.yhj_bg);
            holder.setBackgroundResource(R.id.tv_ljsy, R.drawable.ty_but);
            holder.setTextColor(R.id.tv_ljsy, context.getResources().getColor(R.color.white));
        } else if (item.getCouponType() == 2) {
            holder.setBackgroundResource(R.id.rl_bg, R.drawable.smf_bg);
            holder.setBackgroundResource(R.id.tv_ljsy, R.drawable.smf_but);
            holder.setTextColor(R.id.tv_ljsy, context.getResources().getColor(R.color.white));
        } else if (item.getCouponType() == 3) {
            holder.setBackgroundResource(R.id.rl_bg, R.drawable.yhj_bg);
            holder.setBackgroundResource(R.id.tv_ljsy, R.drawable.rj_but);
            holder.setTextColor(R.id.tv_ljsy, context.getResources().getColor(R.color.white));
        } else {
            holder.setBackgroundResource(R.id.rl_bg, R.drawable.yhj_bg);
            holder.setBackgroundResource(R.id.tv_ljsy, R.drawable.yj_but);
            holder.setTextColor(R.id.tv_ljsy, context.getResources().getColor(R.color.white));
        }*/
        holder.setText(R.id.txtFullReductionAmount, "满" + item.getFullReductionAmount()+"元可用");

        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        SimpleDateFormat sdf2 = new SimpleDateFormat( "yyyy.MM.dd" );
        try {
            Date date = sdf.parse(item.getCreateTime());
            holder.setText(R.id.txtTime, sdf2.format(date)+" - "+ sdf2.format(sdf.parse(item.getExpirationTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.setText(R.id.txtName, item.getCouponName());
        if (item.getIsSoonOverdue() == 1) {
            holder.setVisibility(R.id.iv_icon, View.VISIBLE);
            holder.setImageResource(R.id.iv_icon, R.drawable.kgq_icon);
        } else if (item.getIsNew() == 1) {
            holder.setVisibility(R.id.iv_icon, View.VISIBLE);
            holder.setImageResource(R.id.iv_icon, R.drawable.xd_icon);
        } else {
            holder.setVisibility(R.id.iv_icon, View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(item.getCouponDescribe())) {
            holder.setVisibility(R.id.txtInfo, View.VISIBLE);
            holder.setText(R.id.tv_desc, item.getCouponDescribe());
        } else {
            holder.setVisibility(R.id.txtInfo, View.INVISIBLE);
        }
        final TextView tv_desc = holder.getView(R.id.tv_desc);
        holder.setOnClickListener(R.id.txtInfo, new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (tv_desc.getVisibility() == View.GONE) {
                    tv_desc.setVisibility(View.VISIBLE);
                } else {
                    tv_desc.setVisibility(View.GONE);
                }
            }
        });
        tv_ljsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.gotoActivity(context, ApplyForMaintainActivity.class);
            }
        });
    }
}
