package com.lwc.common.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.module.order.ui.activity.PackageDetailActivity;
import com.lwc.common.module.order.ui.activity.PayActivity;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.Utils;
import com.zhouwei.mzbanner.holder.MZViewHolder;

public class BannerViewHolder implements MZViewHolder<PackageBean> {
        private Button btn_gm;
        private RelativeLayout rl_content;
        private TextView tv_count, tv_money;
        private TextView tv_title, tv_time, tv_yingjian;
        private Context context;
        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.item_home_meal,null);
            btn_gm = (Button) view.findViewById(R.id.btn_gm);
            rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
            tv_count = (TextView) view.findViewById(R.id.tv_count);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_money = (TextView) view.findViewById(R.id.tv_money);
            tv_yingjian  = (TextView) view.findViewById(R.id.tv_yingjian);
            this.context = context;
            return view;
        }

        @Override
        public void onBind(final Context context, int position, final PackageBean item) {
            // 数据绑定
            String count;
            if (item.getRemissionCount() == 0) {
                count = "无限";
            } else {
                count = item.getRemissionCount()+"";
            }
            if (item.getPackageType() == 1) {
                rl_content.setBackgroundResource(R.drawable.cheng_xiao);
                btn_gm.setTextColor(context.getResources().getColor(R.color.chengColor));
                tv_count.setText("减免上门费："+count+"次");
            } else if (item.getPackageType() == 2) {
                rl_content.setBackgroundResource(R.drawable.zi_xiao);
                btn_gm.setTextColor(context.getResources().getColor(R.color.red_money));
                tv_count.setText("减免维修费："+count+"次");
            } else if (item.getPackageType() == 3) {
                rl_content.setBackgroundResource(R.drawable.lan_xiao);
                btn_gm.setTextColor(context.getResources().getColor(R.color.blue_00aaf5));
                tv_count.setText("减免上门维修费："+count+"次");
            }
            tv_title.setText(item.getPackageName());

            tv_time.setText("有效期："+item.getValidDay()+"天");

            tv_money.setText("¥"+ Utils.getMoney(item.getPackagePrice()));

            tv_yingjian.setText("注：不包括硬件更换费用");
            rl_content.setTag(item);
            rl_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PackageBean pack = (PackageBean) v.getTag();
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("package", pack);
                    IntentUtil.gotoActivity(context, PackageDetailActivity.class, bundle2);
                }
            });

            btn_gm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle2 = new Bundle();
                    bundle2.putString("packageId", item.getPackageId());
                    bundle2.putString("money", Utils.cheng(item.getPackagePrice(), "100"));
                    IntentUtil.gotoActivityForResult(context, PayActivity.class, bundle2, 1520);
                }
            });
        }
    }