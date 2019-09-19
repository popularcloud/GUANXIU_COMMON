package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.bean.Coupon;
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
public class SelectCouponListAdapter extends SuperAdapter<Coupon> {

    private Context context;
    public SelectCouponListAdapter(Context context, List<Coupon> items, int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, Coupon item) {
        TextView txtDiscountAmount = holder.findViewById(R.id.txtDiscountAmount);
        String str = "¥ "+item.getDiscountAmount();
        txtDiscountAmount.setText(Utils.getSpannableStringBuilder(1, str.length(), context.getResources().getColor(R.color.white), str, 35));
       /* if (item.getCouponType() == 1) {
            holder.setBackgroundResource(R.id.rl_bg, R.drawable.ty_bg);
        } else if (item.getCouponType() == 2) {
            holder.setBackgroundResource(R.id.rl_bg, R.drawable.smf_bg);
        } else if (item.getCouponType() == 3) {
            holder.setBackgroundResource(R.id.rl_bg, R.drawable.rj_bg);
        } else {
            holder.setBackgroundResource(R.id.rl_bg, R.drawable.yj_bg);
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
//        if (item.getSelect() == 1) {
//            holder.setImageResource(R.id.iv_icon, R.drawable.select_coupon);
//        } else {
//            holder.setImageResource(R.id.iv_icon, R.drawable.no_coupon);
//        }
    }
}
