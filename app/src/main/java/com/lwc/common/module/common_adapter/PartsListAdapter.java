package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Dimension;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.bean.PartsBean;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.widget.TagsLayout;
import com.lwc.common.widget.ZQImageViewRoundOval;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * @author younge
 * @date 2018/12/20 0020
 * @email 2276559259@qq.com
 */
public class PartsListAdapter extends SuperAdapter<PartsBean>{


    public PartsListAdapter(Context context, List<PartsBean> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, PartsBean item) {
        TextView tv_title = holder.findViewById(R.id.tv_title);
        TextView tv_prices = holder.findViewById(R.id.tv_prices);
        TagsLayout tl_tags = holder.findViewById(R.id.tl_tags);
        ZQImageViewRoundOval iv_display = holder.findViewById(R.id.iv_display);

        tv_title.setText(item.getAccessoriesName());

        String priceString = "¥ "+Utils.getMoney(Utils.chu(item.getAccessoriesPrice(),"100"));
        SpannableStringBuilder stringBuilder=new SpannableStringBuilder(priceString);
        AbsoluteSizeSpan ab=new AbsoluteSizeSpan(12,true);
        //文本字体绝对的大小
        stringBuilder.setSpan(ab,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_prices.setText(stringBuilder);
        iv_display.setType(ZQImageViewRoundOval.TYPE_ROUND);
        ImageLoaderUtil.getInstance().displayFromNet(mContext,item.getAccessoriesImg(),iv_display);
        if(!TextUtils.isEmpty(item.getAttributeName())){
            tl_tags.setVisibility(View.VISIBLE);
            String[] tags =item.getAttributeName().split(",");
            tl_tags.removeAllViews();
            for (int i = 0; i < tags.length; i++) {
                if(i > 2){
                    break;
                }
                TextView textView = new TextView(mContext);
                textView.setText(tags[i]);
                textView.setTextColor(Color.parseColor("#666666"));
                textView.setTextSize(Dimension.SP,12);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(10,0,10,0);
                textView.setBackgroundResource(R.drawable.round_square_gray);
                int index = tags[i].indexOf("：");
                String tagStr = tags[i].substring(index+1);
                textView.setText(tagStr);
                tl_tags.addView(textView);
            }
        }else{
            tl_tags.setVisibility(View.INVISIBLE);
        }

    }
}
