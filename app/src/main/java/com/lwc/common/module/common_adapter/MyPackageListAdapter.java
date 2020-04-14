package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.bean.PackageBean;
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
 * 我的套餐Adapter
 */
public class MyPackageListAdapter extends SuperAdapter<PackageBean> {

    private Context context;
    private int type = 0;
    public MyPackageListAdapter(Context context, List<PackageBean> items, int layoutResId,int showType) {
        super(context, items, layoutResId);
        this.context = context;
        this.type = showType;
    }

    public void setType(int type) {
       //this.type = type;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, PackageBean item) {
        TextView tv_used = holder.findViewById(R.id.tv_used);
        TextView tv_count = holder.findViewById(R.id.tv_count);
        TextView tv_time = holder.findViewById(R.id.tv_time);
        TextView tv_title = holder.findViewById(R.id.tv_title);
        ImageView iv_recommend = holder.findViewById(R.id.iv_recommend);


        tv_title.setText(item.getPackageName());
    /*    if(TextUtils.isEmpty(item.getRemark())){
            holder.setVisibility(R.id.tv_desc,View.GONE);
        }else{*/
            holder.setVisibility(R.id.tv_desc,View.VISIBLE);
            holder.setText(R.id.tv_desc,item.getRemarkT());
       // }

        String priceString = item.getPackagePrice() + "元";
        holder.setText(R.id.tv_price, Utils.getSpannableStringBuilder(priceString.length()-1, priceString.length(), mContext.getResources().getColor(R.color.black), priceString, 12));

        if(type == 0){ //首页
            tv_count.setVisibility(View.GONE);
            tv_time.setVisibility(View.GONE);
        }else if (type == 1) {  //列表
            tv_count.setVisibility(View.GONE);
            tv_time.setVisibility(View.GONE);

        } else if(type == 2){ //待使用
            tv_count.setVisibility(View.VISIBLE);
            tv_time.setVisibility(View.VISIBLE);
            tv_used.setText("立即使用");
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            SimpleDateFormat sdf2 = new SimpleDateFormat( "yyyy.MM.dd" );
            try {
                Date date = sdf.parse(item.getCreateTime());
                holder.setText(R.id.tv_time, "有效期:"+sdf2.format(date)+" - "+ sdf2.format(sdf.parse(item.getExpirationTime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String syCount = "剩余:"+item.getResidueRemissionCount()+"次";
            tv_count.setText(syCount);
        }else if(type == 4){
            holder.setVisibility(R.id.tv_price,View.GONE);
            tv_count.setVisibility(View.VISIBLE);
            tv_time.setVisibility(View.VISIBLE);
            tv_used.setText("立即使用");
            holder.setVisibility(R.id.ll_price01,View.GONE);
            holder.setVisibility(R.id.ll_price02,View.VISIBLE);
        }

        if(type == 3 ){// 灰色背景
            tv_used.setText("立即使用");
            holder.itemView.setBackgroundResource(R.drawable.circle_bg_8px_00);
            tv_used.setBackgroundResource(R.drawable.button_package00_cirle_fill);
            tv_used.setTextColor(Color.parseColor("#cccccc"));
            tv_count.setVisibility(View.VISIBLE);
            tv_time.setVisibility(View.VISIBLE);
            tv_count.setTextColor(Color.WHITE);
            tv_time.setTextColor(Color.WHITE);
            tv_title.setTextColor(Color.WHITE);
            holder.setTextColor(R.id.tv_price,Color.WHITE);
            holder.setTextColor(R.id.tv_desc,Color.WHITE);
            holder.setText(R.id.tv_desc,item.getRemarkT());
            holder.setText(R.id.tv_price, Utils.getSpannableStringBuilder(priceString.length()-1, priceString.length(), mContext.getResources().getColor(R.color.white), priceString, 12));

            String syCount = "剩余:"+item.getResidueRemissionCount()+"次";
            tv_count.setText(syCount);
            if("1".equals(item.getPackageLabel())){
                iv_recommend.setVisibility(View.VISIBLE);
                iv_recommend.setImageResource(R.drawable.ic_white_recommend);
            }

            holder.itemView.setClickable(false);
        }else if(type == 4){
            if(item.getIsValid() == 0){
                tv_used.setText("立即使用");
                holder.itemView.setBackgroundResource(R.drawable.circle_bg_8px_00);
                tv_used.setBackgroundResource(R.drawable.button_package00_cirle_fill);
                tv_used.setTextColor(Color.parseColor("#cccccc"));
                tv_count.setTextColor(Color.WHITE);
                tv_time.setTextColor(Color.WHITE);
                tv_title.setTextColor(Color.WHITE);
                holder.setText(R.id.tv_desc,item.getErrorMsg());
                holder.setTextColor(R.id.tv_desc,Color.WHITE);
                holder.setTextColor(R.id.tv_count02,Color.WHITE);
                holder.setTextColor(R.id.tv_count_txt,Color.WHITE);
                String remissionCount = item.getResidueRemissionCount() +"次";
                holder.setText(R.id.tv_count02, Utils.getSpannableStringBuilder(remissionCount.length()-1, remissionCount.length(), mContext.getResources().getColor(R.color.white),remissionCount, 12));
            }else{
                if((layoutPosition+1)%5 == 0){
                    holder.itemView.setBackgroundResource(R.drawable.circle_bg_8px_05);
                    tv_used.setBackgroundResource(R.drawable.button_package05_cirle_fill);
                }else if((layoutPosition+1)%5 == 4){
                    holder.itemView.setBackgroundResource(R.drawable.circle_bg_8px_04);
                    tv_used.setBackgroundResource(R.drawable.button_package04_cirle_fill);
                }else if((layoutPosition+1)%5 == 3){
                    holder.itemView.setBackgroundResource(R.drawable.circle_bg_8px_03);
                    tv_used.setBackgroundResource(R.drawable.button_package03_cirle_fill);
                }else if((layoutPosition+1)%5 == 2){
                    holder.itemView.setBackgroundResource(R.drawable.circle_bg_8px_02);
                    tv_used.setBackgroundResource(R.drawable.button_package02_cirle_fill);
                }else if((layoutPosition+1)%5 == 1){
                    holder.itemView.setBackgroundResource(R.drawable.circle_bg_8px_01);
                    tv_used.setBackgroundResource(R.drawable.button_package01_cirle_fill);
                }
                holder.setText(R.id.tv_desc,item.getRemarkT());
                String remissionCount = item.getResidueRemissionCount() +"次";
                holder.setText(R.id.tv_count02, Utils.getSpannableStringBuilder(remissionCount.length()-1, remissionCount.length(), mContext.getResources().getColor(R.color.black),remissionCount, 12));
            }
        }else{ //自定义颜色
            if("1".equals(item.getPackageLabel())){
                iv_recommend.setVisibility(View.VISIBLE);
                iv_recommend.setImageResource(R.drawable.ic_red_recommend);
            }
            if((layoutPosition+1)%5 == 0){
                holder.itemView.setBackgroundResource(R.drawable.circle_bg_8px_05);
                tv_used.setBackgroundResource(R.drawable.button_package05_cirle_fill);
            }else if((layoutPosition+1)%5 == 4){
                holder.itemView.setBackgroundResource(R.drawable.circle_bg_8px_04);
                tv_used.setBackgroundResource(R.drawable.button_package04_cirle_fill);
            }else if((layoutPosition+1)%5 == 3){
                holder.itemView.setBackgroundResource(R.drawable.circle_bg_8px_03);
                tv_used.setBackgroundResource(R.drawable.button_package03_cirle_fill);
            }else if((layoutPosition+1)%5 == 2){
                holder.itemView.setBackgroundResource(R.drawable.circle_bg_8px_02);
                tv_used.setBackgroundResource(R.drawable.button_package02_cirle_fill);
            }else if((layoutPosition+1)%5 == 1){
                holder.itemView.setBackgroundResource(R.drawable.circle_bg_8px_01);
                tv_used.setBackgroundResource(R.drawable.button_package01_cirle_fill);
            }
        }

    }

    @Override
    public int getItemCount() {
        int myItemCounts = super.getItemCount();
        if(type == 0 && myItemCounts > 5){
            myItemCounts = 5;
        }
        return myItemCounts;
    }
}
