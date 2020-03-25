package com.lwc.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lwc.common.R;
import com.lwc.common.bean.NearBean;
import com.lwc.common.module.bean.Repairman;
import com.lwc.common.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class MyMapUtil {
    private static boolean isLoading = false;
    private static boolean isAdding = false;
    private static ArrayList<NearBean> nearList2;
    private static List<Marker> mapMarkers2;
    private static ArrayList<String> imagepaths;
    private static List<Marker> mapMarkers = new ArrayList<>();

    protected static boolean isAddingMarker = true;

    public static void addMarkersToMap(final Context mainActivity, final AMap aMap,
                                       final ArrayList<Repairman> nearList) {
        if (nearList.size() == 0) {
            isLoading = false;
            //全部在地图上隐藏
            for (int i = 0; i < mapMarkers.size(); i++) {
                mapMarkers.get(i).remove();
                mapMarkers.get(i).destroy();
            }
            //清空集合
            mapMarkers = new ArrayList<>();
            aMap.reloadMap(); //刷新地图
        } else {
            if (!isAdding) {

                isAdding = true;
                for (int i = 0; i < mapMarkers.size(); i++) {
                    mapMarkers.get(i).remove();
                    mapMarkers.get(i).destroy();
                }
                mapMarkers = new ArrayList<>();
                if (imagepaths == null) {
                    imagepaths = new ArrayList<>();
                } else {
                    imagepaths.clear();
                }
                aMap.reloadMap();//刷新地图
                for (int i = 0; i < nearList.size(); i++) {
                    imagepaths.add(nearList.get(i).getMaintenanceHeadImage());
                }

                if (isAddingMarker) {
                    for (int i = 0; i < nearList.size(); i++) {
                        //这里设置地图定位的图标
                        final Repairman nearBean = nearList.get(i);
                        String picture = nearBean.getMaintenanceHeadImage();
                        if (!TextUtils.isEmpty(picture)) {
                            final View view = View.inflate(mainActivity,R.layout.view_head, null);
                            LinearLayout ll_head_bg = (LinearLayout) view.findViewById(R.id.ll_head_bg);
                            final CircleImageView imgHead = (CircleImageView) view.findViewById(R.id.img_map_head);
                            if (nearBean.getIsBusy().equals("1")) {
                                ll_head_bg.setBackgroundResource(R.drawable.icon_map_new_busy);
                            } else {
                                ll_head_bg.setBackgroundResource(R.drawable.icon_map_new_commen);
                            }
                            Glide.with(mainActivity).load(picture).
                                    placeholder(R.drawable.icon_default_portrait)
                                    .error(R.drawable.icon_default_portrait)
                                    .into(new SimpleTarget<GlideDrawable>() {
                                        @Override
                                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                            //展示图片
                                            imgHead.setImageDrawable(resource);
//                                                Bitmap bitmap = convertViewToBitmap(view);
//                                                markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                            LatLng lat = new LatLng(Double.valueOf(nearBean.getMaintenanceLatitude()), Double.valueOf(nearBean.getMaintenanceLongitude()));
                                            MarkerOptions draggable = new MarkerOptions().anchor(0.5f, 0.5f).position(lat).title(nearBean.getMaintenanceName())
                                                    .snippet(nearBean.getMaintenanceId()).icon(BitmapDescriptorFactory.fromView(view)).draggable(false);//
                                            Marker marker = aMap.addMarker(draggable);
                                            mapMarkers.add(marker);
                                        }
                                    });
                        } else {
                            View view = View.inflate(mainActivity,R.layout.view_head, null);
                            LinearLayout ll_head_bg = (LinearLayout) view.findViewById(R.id.ll_head_bg);
                            CircleImageView imgHead = (CircleImageView) view.findViewById(R.id.img_map_head);
                            if (nearList.get(i).getIsBusy().equals("1")) {
                                ll_head_bg.setBackgroundResource(R.drawable.icon_map_new_busy);
                            } else {
                                ll_head_bg.setBackgroundResource(R.drawable.icon_map_new_commen);
                            }
//                            if (!TextUtils.isEmpty(picture)) {
//                                ImageLoaderUtil.getInstance().displayFromNetD(mainActivity, picture+"?x-oss-process=image/resize,m_fixed,w_60,h_60", imgHead);
//                            } else {
                            imgHead.setImageResource(R.drawable.icon_default_portrait);
//                            }
                            LatLng lat = new LatLng(Double.valueOf(nearList.get(i).getMaintenanceLatitude()), Double.valueOf(nearList.get(i).getMaintenanceLongitude()));
                            MarkerOptions draggable = new MarkerOptions().anchor(0.5f, 0.5f).position(lat).title(nearList.get(i).getMaintenanceName())
                                    .snippet(nearBean.getMaintenanceId()).icon(BitmapDescriptorFactory.fromView(view)).draggable(false);
                            Marker marker = aMap.addMarker(draggable);
                            mapMarkers.add(marker);
                        }
                    }
                    isAdding = false;
                    isLoading = false;
                    isAddingMarker = true;
                }
            }
        }
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * 过滤
     *
     * @param aMap
     * @param mapMarkers
     * @param nearList
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    private static void fliterData(final AMap aMap, final List<Marker> mapMarkers, final ArrayList<NearBean> nearList) {
        if (mapMarkers2 == null) {
            mapMarkers2 = new ArrayList<>();
        } else {
            mapMarkers2.clear();
        }
        if (nearList2 == null) {
            nearList2 = new ArrayList<>();
        } else {
            nearList2.clear();
        }
        for (int i = 0; i < mapMarkers.size(); i++) {
            mapMarkers.get(i).hideInfoWindow();
            for (int j = 0; j < nearList.size(); j++) {
                if (nearList.get(j).equals(mapMarkers.get(i).getTitle())) {
                    mapMarkers.get(i).setPosition(new LatLng(Double.valueOf(nearList.get(j).getLat()), Double.valueOf(nearList.get(j).getLon())));
//                    aMap.invalidate();// 刷新地图
                    mapMarkers2.add(mapMarkers.get(i));
                } else {
                    nearList2.add(nearList.get(j));
                    mapMarkers.get(i).destroy();
                }
            }
        }
        mapMarkers.clear();
        mapMarkers.addAll(mapMarkers2);
        nearList.clear();
        nearList.addAll(nearList2);
    }
}
