package com.lwc.common.fragment;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Repairman;
import com.lwc.common.module.bean.User;
import com.lwc.common.utils.Constants;
import com.lwc.common.utils.DisplayUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.MyMapUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.SystemUtil;
import com.lwc.common.view.MyTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 地图
 * @author 何栋
 * @date 2017年11月20日
 * @Copyright: lwc
 */
public class MapFragment extends BaseActivity implements AMap.OnMyLocationChangeListener, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter {

    @BindView(R.id.txtActionbarTitle)
    MyTextView txtActionbarTitle;

    /**
     * 地图对象
     */
    private AMap aMap;
    /**
     * 地图控件
     */
    private MapView mapView;
    private SharedPreferencesUtils preferencesUtils;
    private User user;
    private ArrayList<Repairman> repairmanArrayList;
    private Marker marker;
    private Location mLocation;


    @OnClick(R.id.img_location)
    public void onClickView(View v) {
        if (mLocation != null) {
            aMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()),
                            16, 30, 0)),
                    500, null);
        }
    }

    /**
     * 初适化定位
     */
    private void initLocation() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(120000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeColor(Color.parseColor("#1B62AF"));
        myLocationStyle.radiusFillColor(Color.parseColor("#331B62AF"));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(1);
        myLocationStyle.anchor(0.5f, 0.5f);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_marker)));
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMyLocationChangeListener(this);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.fragment_map;
    }

    @Override
    protected void findViews() {
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState); // 此方法必须重写
        aMap = mapView.getMap();
        ButterKnife.bind(this);
    }

    @Override
    public void init() {
        txtActionbarTitle.setText("地图");
        initLocation();
    }

    @Override
    protected void initGetData() {
        preferencesUtils = SharedPreferencesUtils.getInstance(this);
        user = preferencesUtils.loadObjectData(User.class);
    }

    @Override
    protected void widgetListener() {

    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        mapView.invalidate();
    }

    /**
     * 获取附近工程师
     */
    private void getNearEngineer() {
        if (SystemUtil.isBackground(this) || mLocation == null) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("type", "3");
        HttpRequestUtils.httpRequest(this, "getNearEngineer", RequestValue.NEAR_ENGINEER, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        repairmanArrayList = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<Repairman>>() {
                        });
                        if (SystemUtil.isBackground(MapFragment.this)) {
                            return;
                        }
                        if (aMap != null && repairmanArrayList != null) {
                            MyMapUtil.addMarkersToMap(MapFragment.this, aMap, repairmanArrayList);// 往地图上添加marker
                        }
                        break;
                    default:
                        LLog.i("getNearEngineer" + common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        if (this.marker != null) {
            this.marker.hideInfoWindow();
        }
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        LLog.i(" 进入地图3   onDestroy");
        if (mapView != null)
            mapView.onDestroy();
    }

    /**
     * 自定义infowinfow窗口
     *
     * @param marker
     * @param view
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public void render(Marker marker, View view) {
        Repairman n = null;
        if (marker.getSnippet() != null && repairmanArrayList != null) {
            for (int i = 0; i < repairmanArrayList.size(); i++) {
                if (marker.getSnippet().equals(repairmanArrayList.get(i).getMaintenanceId())) {
                    n = repairmanArrayList.get(i);
                    break;
                }
            }
        }
        if (n != null) {// TODO 以下需要自定义
            TextView name = ((TextView) view.findViewById(R.id.name));
            TextView status = ((TextView) view.findViewById(R.id.status));
            TextView company = ((TextView) view.findViewById(R.id.company));
            TextView phone = ((TextView) view.findViewById(R.id.phone));
            TextView skills = ((TextView) view.findViewById(R.id.skills));
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) skills.getLayoutParams();
            layoutParams.width = DisplayUtil.getScreenWidth(this) - DisplayUtil.dip2px(this, 150);
            name.setText("姓名: " + n.getMaintenanceName());
            if (!Constants.isRepairer) {

                if (n.getIsBusy().equals("1")) {
                    status.setTextColor(this.getResources().getColor(R.color.red_money));
                    status.setText("繁忙");
                } else {
                    status.setTextColor(this.getResources().getColor(R.color.text_blue_click));
                    status.setText("空闲");
                }
                if (n.getDeviceTypeName() != null && !TextUtils.isEmpty(n.getDeviceTypeName())) {
                    skills.setText("擅长: " + n.getDeviceTypeName());
                } else {
                    skills.setText("擅长: " + "暂无");
                }
                skills.setVisibility(View.VISIBLE);
            } else {
                skills.setVisibility(View.GONE);
                status.setText("");
            }
            if (!TextUtils.isEmpty(n.getCompanyName())) {
                company.setText("公司: " + n.getCompanyName());
            } else if (!TextUtils.isEmpty(n.getMaintenanceName()))
                phone.setVisibility(View.GONE);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
        render(marker, infoWindow);
        return infoWindow;
    }

    /**
     * 监听自定义infowindow窗口的infocontents事件回调
     */
    @Override
    public View getInfoContents(Marker marker) {
        View infoContent = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        render(marker, infoContent);
        return infoContent;
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (location != null && location.getLatitude() > 0 && location.getLongitude() > 0) {
            user.setLon(location.getLongitude() + "");
            user.setLat(location.getLatitude() + "");
            updateUserData(location);
            com.lwc.common.bean.Location lo = new com.lwc.common.bean.Location();
            lo.setLatitude(location.getLatitude());
            lo.setLongitude(location.getLongitude());
            lo.setStrValue(location.toString());
            preferencesUtils.saveObjectData(lo);
            mLocation = location;
        }
    }

    private void updateUserData(final Location aLocation) {
        if (SystemUtil.isBackground(this)) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        if (aLocation != null) {
            String lat = aLocation.getLatitude() + "";
            String lon = aLocation.getLongitude() + "";
            params.put("lat", lat);
            params.put("lon", lon);
        }
        HttpRequestUtils.httpRequest(this, "updateUserData", RequestValue.UP_USER_INFOR, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                getNearEngineer();
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError("updateUserInfo1  " + e.toString());
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        this.marker = marker;
        return false;
    }
}
