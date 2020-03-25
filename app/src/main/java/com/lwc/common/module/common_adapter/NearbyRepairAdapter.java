package com.lwc.common.module.common_adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.lwc.common.R;
import com.lwc.common.adapter.ComViewHolder;
import com.lwc.common.bean.Location;
import com.lwc.common.fragment.MainFragment;
import com.lwc.common.module.bean.Repairman;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.Utils;
import com.lwc.common.widget.CircleImageView;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 附近工程师
 */
public class NearbyRepairAdapter extends SuperAdapter<Repairman> {

    private ImageLoaderUtil imageLoaderUtil;
    private Context context;

    public NearbyRepairAdapter(Context context, List<Repairman> items, int layoutResId) {
        super(context, items, layoutResId);
        this.context = context;
        imageLoaderUtil = ImageLoaderUtil.getInstance();
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, Repairman item) {

        holder.setVisibility(R.id.txtDistance, ComViewHolder.VISIBLE);
        LatLng latLng2 = new LatLng(Double.parseDouble(item.getMaintenanceLatitude()), Double.parseDouble(item.getMaintenanceLongitude()));
        Location location = SharedPreferencesUtils.getInstance(context).loadObjectData(Location.class);
        if (location != null) {
            float calculateLineDistance = AMapUtils.calculateLineDistance(
                    new LatLng(location.getLatitude(), location.getLongitude()), latLng2);

            int distance = (int) calculateLineDistance;
            DecimalFormat df = new DecimalFormat("#.00");
            holder.setText(R.id.txtDistance, (distance > 1000 ? df.format((float) distance / 1000) + "km" : distance + "m"));
        }
        CircleImageView img = holder.getView(R.id.imgIcon);
        if (item.getMaintenanceHeadImage() != null && !item.getMaintenanceHeadImage().equals("")) {
            imageLoaderUtil.displayFromNet(context, item.getMaintenanceHeadImage(), img);
        } else {
            img.setImageResource(R.drawable.default_portrait_100);
        }

        String userName = item.getMaintenanceName();
        if (userName != null && !TextUtils.isEmpty(userName)) {
            holder.setText(R.id.txtName, item.getMaintenanceName());    //用户名字
        } else {
            holder.setText(R.id.txtName, "暂无");
        }

        com.hedgehog.ratingbar.RatingBar ratingBar = holder.findViewById(R.id.ratingBar_1);
        if (!TextUtils.isEmpty(item.getMaintenanceStar())) {
            ratingBar.setStarCount(5);
            ratingBar.setStar(Float.parseFloat(item.getMaintenanceStar()));
        } else {
            ratingBar.setStar(0);
        }

        String skilled = item.getDeviceTypeName();
        if (!TextUtils.isEmpty(skilled)) {
            holder.setText(R.id.txtSkilled_content,skilled);
        } else {
            holder.setText(R.id.txtSkilled_content, "暂无");
        }
        if (!TextUtils.isEmpty(item.getOrderCount())) {
            holder.setText(R.id.txtOrderCount, "已完成" + item.getOrderCount() + "单");
        } else {
            holder.setText(R.id.txtOrderCount, "已完成0单");
        }
        TextView tvYQ = holder.getView(R.id.tv_yq);
        if (item.getIsInvite() == 0) {
            tvYQ.setText("邀请维修");
            tvYQ.setTextColor(context.getResources().getColor(R.color.white));
            tvYQ.setBackgroundResource(R.drawable.button_blue_round_shape);
        } else {
            tvYQ.setText("已邀请");
            tvYQ.setTextColor(context.getResources().getColor(R.color.white));
            tvYQ.setBackgroundResource(R.drawable.button_gray_round_shape);
        }
        tvYQ.setTag(item);
        tvYQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Repairman items = (Repairman)v.getTag();
                if (items.getIsInvite() == 0) {
                    MainFragment.mainFragment.informInvite(items);
                }
            }
        });

    }
}
