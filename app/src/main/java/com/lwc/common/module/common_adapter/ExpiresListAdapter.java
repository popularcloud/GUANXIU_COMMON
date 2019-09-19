package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.widget.ImageView;
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
public class ExpiresListAdapter extends SuperAdapter<Coupon> {

    private Context context;
    private int mType=0;
    public ExpiresListAdapter(Context context, List<Coupon> items, int layoutResId, int type) {
        super(context, items, layoutResId);
        this.context = context;
        this.mType = type;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, Coupon item) {
        TextView txtDiscountAmount = holder.findViewById(R.id.txtDiscountAmount);
        ImageView iv_status = holder.findViewById(R.id.iv_status);

        String str = "¥ "+item.getDiscountAmount();
        SpannableStringBuilder stringBuilder=new SpannableStringBuilder(str);
        AbsoluteSizeSpan ab=new AbsoluteSizeSpan(30,true);
        //文本字体绝对的大小
        stringBuilder.setSpan(ab,1,str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtDiscountAmount.setText(stringBuilder);

        //txtDiscountAmount.setText(Utils.getSpannableStringBuilder(1, str.length(), context.getResources().getColor(R.color.white), str, 30));
        if (mType == 1) {
            iv_status.setImageResource(R.drawable.ysy_bg);
        } else if (mType == 2) {
            iv_status.setImageResource(R.drawable.ygq_bg);
        }
        holder.setText(R.id.txtFullReductionAmount, "满" + item.getFullReductionAmount()+"元可用");
//        holder.setText(R.id.txtTime, item.getCreateTime()+" - "+item.getExpirationTime());
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        SimpleDateFormat sdf2 = new SimpleDateFormat( "yyyy.MM.dd" );
        try {
            Date date = sdf.parse(item.getCreateTime());
            holder.setText(R.id.txtTime, sdf2.format(date)+" - "+ sdf2.format(sdf.parse(item.getExpirationTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.setText(R.id.txtName, item.getCouponName());

    }
}
