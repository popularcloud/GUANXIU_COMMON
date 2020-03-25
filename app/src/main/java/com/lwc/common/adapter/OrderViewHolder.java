package com.lwc.common.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.lwc.common.R;
import com.lwc.common.module.bean.Order;
import com.lwc.common.module.order.ui.activity.OrderDetailActivity;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.Utils;
import com.lwc.common.widget.CircleImageView;
import com.lwc.common.widget.CustomDialog;
import com.zhouwei.mzbanner.holder.MZViewHolder;

public class OrderViewHolder implements MZViewHolder<Order> {
    private com.hedgehog.ratingbar.RatingBar rBarLevel;
    private CircleImageView img_icon;
    private ImageView btn_engineer_phone;
    private TextView txtMaintainName, txtOrderStatus, txtDistance;
    private LinearLayout ll_order;
    private Dialog dialog;

    @Override
    public View createView(Context context) {
        // 返回页面布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_order, null);
        ll_order = (LinearLayout) view.findViewById(R.id.ll_order);
        rBarLevel = (com.hedgehog.ratingbar.RatingBar) view.findViewById(R.id.rBarLevel);
        img_icon = (CircleImageView) view.findViewById(R.id.img_icon);
        txtMaintainName = (TextView) view.findViewById(R.id.txtMaintainName);
        btn_engineer_phone = (ImageView) view.findViewById(R.id.btn_engineer_phone);
        txtOrderStatus = (TextView) view.findViewById(R.id.txtOrderStatus);
        txtDistance = (TextView) view.findViewById(R.id.txtDistance);

        try {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.start1);
            int starsImgHeight = bmp.getHeight();
            //将获取的图片高度设置给RatingBar
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)rBarLevel.getLayoutParams();
            lp.width = bmp.getWidth() * 5 + 80;
            lp.height =starsImgHeight;
            rBarLevel.setLayoutParams(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onBind(final Context context, int position, Order newestOrder) {
        // 数据绑定
        String picture = newestOrder.getMaintenanceHeadImage();
        if (!TextUtils.isEmpty(picture)) {
            ImageLoaderUtil.getInstance().displayFromNet(context, newestOrder.getMaintenanceHeadImage(), img_icon);
        } else {
            ImageLoaderUtil.getInstance().displayFromLocal(context, img_icon, R.drawable.default_portrait_100);
        }

        if (!TextUtils.isEmpty(newestOrder.getMaintenanceStar())) {
            rBarLevel.setStarCount(5);
            rBarLevel.setStar(Float.parseFloat(newestOrder.getMaintenanceStar()));
        } else {
            rBarLevel.setStar(0);
        }
        setJuli(context, newestOrder);
        txtOrderStatus.setText(newestOrder.getStatusName());
        txtMaintainName.setText(newestOrder.getMaintenanceName());
        btn_engineer_phone.setTag(newestOrder.getMaintenancePhone());
        btn_engineer_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = (String)v.getTag();
                dialog = DialogUtil.dialog(context, "拨打工程师电话", "拨号", "取消", phone, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Utils.lxkf(context, phone);
                    }
                }, null, true);
            }
        });
        ll_order.setTag(newestOrder);
        ll_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Order order = (Order) v.getTag();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", order);
                    IntentUtil.gotoActivity(context, OrderDetailActivity.class, bundle);
                }
        });
    }

    private void setJuli(Context context, Order newestOrder) {
        com.lwc.common.bean.Location location = SharedPreferencesUtils.getInstance(context).loadObjectData(com.lwc.common.bean.Location.class);
        if (newestOrder == null || TextUtils.isEmpty(newestOrder.getMaintenanceLatitude()) || location == null) {
            return;
        }
        LatLng latLng2 = new LatLng(Double.parseDouble(newestOrder.getMaintenanceLatitude()), Double.parseDouble(newestOrder.getMaintenanceLongitude()));
        float calculateLineDistance = AMapUtils.calculateLineDistance(
                new LatLng(location.getLatitude(), location.getLongitude()), latLng2);
        if (calculateLineDistance > 10000000) {
            txtDistance.setText("工程师离线");
        } else {
            txtDistance.setText("距离您 " + (calculateLineDistance > 1000 ? calculateLineDistance / 1000 + "km" : (int) calculateLineDistance + "m"));
        }
    }
}