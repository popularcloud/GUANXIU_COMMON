package com.lwc.common.module.repairs.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.bean.Location;
import com.lwc.common.module.bean.PoiBean;
import com.lwc.common.module.common_adapter.PoiAdapter;
import com.lwc.common.module.repairs.view.AutoListView;
import com.lwc.common.utils.KeyboardUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 修改地址
 */
public class SelectAddressActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener, AutoListView.OnRefreshListener, AutoListView.OnLoadListener{

    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.iv_delete)
    ImageView iv_delete;
    @BindView(R.id.lv_list)
    AutoListView lv_list;

    private LatLonPoint lp = new LatLonPoint(23.04, 113.75);

    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;

    private AMapLocationClient locationClient = null;//定位类
    private double mLatitude,mLongitude;//定位的经纬度

    private PoiAdapter mAdapter;
    private int currentPage=0;//页数从第0页开始
    private List<PoiBean> poiData=new ArrayList<>();
    private AMapLocation mLoc;//首次进入定位成功信息
    private String mCity;
    private String cityStr;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_amend;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        Location location = SharedPreferencesUtils.getInstance(SelectAddressActivity.this).loadObjectData(Location.class);
        cityStr = getIntent().getStringExtra("city_str");
        if (location != null)
            lp = new LatLonPoint(location.getLatitude(), location.getLongitude());
        initLocation();
    }

    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        locationClient.startLocation();
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String city,double latitude,double longitude) {
        String keyword = et_search.getText().toString().trim();
        String mType="餐饮服务|商务住宅|政府机构及社会团体|地名地址信息|科教文化服务|生活服务|公司企业|道路附属设施|公共设施";
        query = new PoiSearch.Query("", mType, cityStr);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        if (!TextUtils.isEmpty(keyword)) {
            query = new PoiSearch.Query(keyword,mType, cityStr);
        }
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        if (lp != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            //以当前定位的经纬度为准搜索周围5000米范围
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
       /*     if (TextUtils.isEmpty(keyword)) {
                poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 10000));//
            }*/
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                mCity = loc.getCity();
                mLoc=loc;
                lp.setLongitude(loc.getLongitude());
                lp.setLatitude(loc.getLatitude());
                //得到定位信息
                Log.e("yufs","定位详细信息："+loc.toString());
                mLatitude=loc.getLatitude();
                mLongitude=loc.getLongitude();
                //初始化地图对象
                //查询周边
                doSearchQuery(loc.getCity(),loc.getLatitude(),loc.getLongitude());
//                latSearchList(mLatitude, mLongitude);
            } else {
                ToastUtil.showLongToast(SelectAddressActivity.this, "定位失败，请打开位置权限");
            }
        }
    };

    /**
     * 设置地图相关属性信息
     * @return
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(false); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    private String keyword="";
    @Override
    public void init() {
    }

    @Override
    protected void initGetData() {

    }

    @Override
    protected void widgetListener() {
        mAdapter=new PoiAdapter(this,poiData);
        lv_list.setAdapter(mAdapter);
        lv_list.setOnRefreshListener(this);
        lv_list.setOnLoadListener(this);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiBean poiBean = poiData.get((int) id);
                KeyboardUtil.showInput(false, SelectAddressActivity.this);
                Intent i = new Intent();
                i.putExtra("address", poiBean.getSnippet()+poiBean.getTitleName());
                i.putExtra("latitude",poiBean.getPoint().getLatitude());
                i.putExtra("longitude", poiBean.getPoint().getLongitude());
                setResult(RESULT_OK, i);
                onBackPressed();
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = et_search.getText().toString().trim();
                if (!value.equals(keyword)) {
                    iv_delete.setVisibility(View.VISIBLE);
                    keyword = value;
                    currentPage=0;
                    poiData.clear();
                    doSearchQuery(mCity,mLatitude,mLongitude);
                } else if (value.equals("")){
                    iv_delete.setVisibility(View.GONE);
                    currentPage=0;
                    poiData.clear();
                    doSearchQuery(mCity,mLatitude,mLongitude);
                }
            }
        });

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
                iv_delete.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    lv_list.setVisibility(View.VISIBLE);
                    lv_list.onLoadComplete();
                    try {
                        List<PoiItem> poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                        List<PoiBean> tem=new ArrayList<>();
                        if (poiItems != null && poiItems.size() > 0) {
                            for (int i = 0; i < poiItems.size(); i++) {
                                PoiItem poiItem = poiItems.get(i);
                                PoiBean bean=new PoiBean();
                                bean.setTitleName(poiItem.getTitle());
                                bean.setCityName(poiItem.getCityName());
                                bean.setAd(poiItem.getAdName());
                                bean.setSnippet(poiItem.getSnippet());
                                bean.setPoint(poiItem.getLatLonPoint());
                                Log.e("yufs",""+poiItem.getTitle()+","+poiItem.getProvinceName()+","
                                        +poiItem.getCityName()+","
                                        +poiItem.getAdName()+","//区
                                        +poiItem.getSnippet()+","
                                        +poiItem.getLatLonPoint()+"\n");
                                tem.add(bean);
                            }
                            poiData.addAll(tem);
                            mAdapter.notifyDataSetChanged();
                            /* if (isSearch){
                                    moveMapCamera(poiItems.get(0).getLatLonPoint().getLatitude(),poiItems.get(0).getLatLonPoint().getLongitude());
                            }*/
                        }
                    } catch (Exception e){}
                }
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }

    @Override
    public void onRefresh() {
        lv_list.onRefreshComplete();
    }

    @Override
    public void onLoad() {
        currentPage++;
        doSearchQuery(mCity,mLatitude,mLongitude);
    }
}
