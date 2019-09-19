package com.lwc.common.module.integral.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.integral.bean.IntegralExchangeBean;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.widget.ZQImageViewRoundOval;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;


/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 积分兑换记录
 */
public class IntegralExchangeAdapter extends SuperAdapter<IntegralExchangeBean> {

    private Context context;
    public IntegralExchangeAdapter(Context context, List<IntegralExchangeBean> items, int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, IntegralExchangeBean item) {
        ZQImageViewRoundOval img = holder.findViewById(R.id.iv_header);
        TextView tv_title = holder.findViewById(R.id.tv_title);
        TextView tv_time = holder.findViewById(R.id.tv_time);
        TextView tv_integral = holder.findViewById(R.id.tv_integral);
        TextView tv_status = holder.findViewById(R.id.tv_status);

        if("2".equals(item.getExchangeType())){
            String priceString ="抽奖礼品";
            SpannableStringBuilder stringBuilder=new SpannableStringBuilder(priceString);
            AbsoluteSizeSpan ab=new AbsoluteSizeSpan(12,true);
            //文本字体绝对的大小
            stringBuilder.setSpan(ab,0,priceString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_integral.setText(stringBuilder);
        }else{
            String priceString =Utils.chu(item.getIntegralNum(),"100")+" 积分";
            SpannableStringBuilder stringBuilder=new SpannableStringBuilder(priceString);
            AbsoluteSizeSpan ab=new AbsoluteSizeSpan(12,true);
            //文本字体绝对的大小
            stringBuilder.setSpan(ab,priceString.length() - 2,priceString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_integral.setText(stringBuilder);
        }


        tv_title.setText(item.getIntegralName());
        tv_time.setText(""+item.getCreateTime());
        tv_status.setText("1".equals(item.getExchangeStatus())?"兑换中":"兑换成功");
        img.setType(ZQImageViewRoundOval.TYPE_ROUND);
        ImageLoaderUtil.getInstance().displayFromNet(context,item.getIntegralCover(),img);
    }
}
