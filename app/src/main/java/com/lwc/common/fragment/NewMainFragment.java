package com.lwc.common.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.adapter.MainContentAdapter;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.bean.BroadcastBean;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.IndexAdBean;
import com.lwc.common.module.bean.Order;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.module.bean.Repairs;
import com.lwc.common.module.order.ui.activity.OrderDetailActivity;
import com.lwc.common.module.zxing.activity.CaptureActivity;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.DisplayUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.MyTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

public class NewMainFragment extends BaseFragment {


    @BindView(R.id.rv_main_content)
    RecyclerView rv_main_content;
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    @BindView(R.id.cv_presentOrder)
    FrameLayout cv_presentOrder;
    @BindView(R.id.iv_header)
    ImageView iv_header;
    @BindView(R.id.iv_call_phone)
    ImageView iv_call_phone;
    @BindView(R.id.tv_engineer_name)
    TextView tv_engineer_name;
    @BindView(R.id.tv_engineer_count)
    TextView tv_engineer_count;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.tv_distance)
    TextView tv_distance;
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.txtActionbarTitle)
    MyTextView txtActionbarTitle;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.imgRight)
    ImageView imgRight;


    int scrollDistance = 0;

    private MainContentAdapter mainContentAdapter;
    /**
     * 轮播图片集合
     */
    private ArrayList<ADInfo> infos;

    /***
     * 维修类型集合
     */
    private List<Repairs> repairsList = new ArrayList<>(10);

    /**
     * 租赁类型集合
     */
    private ArrayList<Repairs> myLeaseTypeLists;

    /**
     * 最新订单
     */
    public ArrayList<Order> newestOrders;

    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_main, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        if(getActivity() != null){
                initStatusBar();
        }
    }

    @Override
    protected void lazyLoad() {
        if(getActivity() != null){
                initStatusBar();
                getNewestOrder();
        }
    }



    @Override
    public void init() {

        initRecycleView();

        //初始化扫一扫
        imgRight.setVisibility(View.VISIBLE);
        imgRight.setImageResource(R.drawable.ic_blue_scan);
        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.gotoLogin(MainActivity.user, getActivity()))
                    IntentUtil.gotoActivityForResult(getActivity(), CaptureActivity.class, 8869);
            }
        });

        cv_presentOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newestOrders != null && newestOrders.size() > 0){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", newestOrders.get(0));
                    IntentUtil.gotoActivity(getActivity(), OrderDetailActivity.class, bundle);
                }
            }
        });

    }

    /**
     * 初始化列表
     */
    private void initRecycleView() {
        BGARefreshLayoutUtils.initRefreshLayout(getContext(), mBGARefreshLayout, false);
        rv_main_content.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_main_content.setItemViewCacheSize(6);
        mainContentAdapter = new MainContentAdapter(getContext());
        rv_main_content.setAdapter(mainContentAdapter);

        mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                scrollDistance = 0;

                getWheelPic();

                getNewestOrder();

                getNotices();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                return true;
            }
        });

        mBGARefreshLayout.beginRefreshing();

    }

    @Override
    public void initEngines(View view) {

    }

    @Override
    public void getIntentData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() != null){
            getNewestOrder();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 获取轮播图
     */
    public void getWheelPic() {
        Map<String,String> params = new HashMap<>();
        params.put("local","1");//首页轮播图
        params.put("role","4");//角色为个人
        HttpRequestUtils.httpRequest(getActivity(), "获取首页轮播图", RequestValue.GET_ADVERTISING, params, "GET", new HttpRequestUtils.ResponseListener() {

            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        infos = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<ADInfo>>() {
                        });
                        if(infos != null && infos.size() > 0){
                            mainContentAdapter.setImageWheelData(infos);
                        }
                        getARepairType();
                        break;
                    default:
                        LLog.i("获取首页轮播图" + common.getInfo());
                        mBGARefreshLayout.endRefreshing();
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                mBGARefreshLayout.endRefreshing();
            }
        });
    }

    /**
     * 获取所有维修类型
     */
    public void getARepairType(){
        HttpRequestUtils.httpRequest(getActivity(), "获取所有维修类型", RequestValue.GET_TYPE_ALL__2, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        List<Repairs> presentRepairsList = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<Repairs>>(){});
                        repairsList.clear();
                        int first = 0;
                        int tow = 0;
                       /* if(presentRepairsList != null && presentRepairsList.size() > 0){
                            for(int i = 0;i < presentRepairsList.size();i++){
                                Repairs repairs = presentRepairsList.get(i);
                                if(repairs.getDeviceTypeMold() == 1){//办公
                                    if(first == 5){
                                        continue;
                                    }
                                    repairsList.add(first,repairs);
                                    first++;
                                }else{ //家电
                                    if(tow == 5){
                                        continue;
                                    }
                                    repairsList.add(tow,repairs);
                                    tow++;
                                }

                                if(first == 5 && tow == 5){
                                    break;
                                }
                            }
                            Collections.sort(repairsList);
                            mainContentAdapter.setRepairTypeData(repairsList);
                        }*/
                        getLeaseType();
                        break;
                    default:
                        LLog.i("获取所有维修类型：" + common.getInfo());
                        mBGARefreshLayout.endRefreshing();
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                mBGARefreshLayout.endRefreshing();
            }
        });
    }


    /**
     * 获取租赁设备类型
     */
    public void getLeaseType() {
        HttpRequestUtils.httpRequest(getActivity(), "获取租赁设备类型", RequestValue.SCAN_GETLEASEDEVICETYPES, null, "GET", new HttpRequestUtils.ResponseListener() {

            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        myLeaseTypeLists = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<Repairs>>() {
                        });
                        if(myLeaseTypeLists != null && myLeaseTypeLists.size() > 0){
                            mainContentAdapter.setLeaseTypeData(myLeaseTypeLists);
                        }
                        getADList();
                        break;
                    default:
                        LLog.i("获取租赁设备类型" + common.getInfo());
                        mBGARefreshLayout.endRefreshing();
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                mBGARefreshLayout.endRefreshing();
            }
        });
    }


    /**
     * 获取广告位
     */
    public void getADList() {
        HttpRequestUtils.httpRequest(getActivity(), "获取广告", RequestValue.LEASEMANAGE_GETLEASEIMAGES, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String result) {
                Common common = JsonUtil.parserGsonToObject(result, Common.class);
                if (common.getStatus().equals("1")) {
                    List<IndexAdBean> indexAdBeans = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(result, "data"), new TypeToken<ArrayList<IndexAdBean>>() {});
                    if(indexAdBeans != null && indexAdBeans.size() > 0){
                        mainContentAdapter.setIndexAdData(indexAdBeans);
                    }
                    getMealList();
                }else{
                    LLog.i("获取广告" + common.getInfo());
                    mBGARefreshLayout.endRefreshing();
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                mBGARefreshLayout.endRefreshing();
            }
        });
    }

    /**
     * 获取可购买的套餐信息
     */
    public void getMealList() {
        HashMap<String, String> map = new HashMap<>();
        map.put("location", "1");
        HttpRequestUtils.httpRequest(getActivity(), "获取可用套餐", RequestValue.GET_PACKAGE_LIST, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String result) {
                Common common = JsonUtil.parserGsonToObject(result, Common.class);
                if (common.getStatus().equals("1")) {
                    ArrayList<PackageBean> packageBeanList = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(result, "data"), new TypeToken<ArrayList<PackageBean>>() {});
                    if(packageBeanList != null && packageBeanList.size() > 0){
                        mainContentAdapter.setMealTypeData(packageBeanList);
                    }
                    mBGARefreshLayout.endRefreshing();
                }else{
                    LLog.i("获取可用套餐" + common.getInfo());
                    mBGARefreshLayout.endRefreshing();
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                mBGARefreshLayout.endRefreshing();
            }
        });
    }

    /**
     * 获取最新订单
     */
    public void getNewestOrder() {
        if (MainActivity.user == null) {
            cv_presentOrder.setVisibility(View.GONE);
            return;
        }
        Log.d("联网成功","获取订单信息");
        HttpRequestUtils.httpRequest(getActivity(), "获取最新订单", RequestValue.ORDER_VIEW, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        newestOrders = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<Order>>() {
                        });
                        if (newestOrders != null && newestOrders.size() > 0) {

                            rv_main_content.setPadding(0,0,0, DisplayUtil.dip2px(getContext(),100));

                            cv_presentOrder.setVisibility(View.VISIBLE);
                            final Order order = newestOrders.get(0);

                            ImageLoaderUtil.getInstance().displayFromNetDCircularT(getContext(),order.getMaintenanceHeadImage(),iv_header,R.drawable.default_portrait_100);
                            tv_engineer_name.setText(TextUtils.isEmpty(order.getMaintenanceName())?"暂无":order.getMaintenanceName());
                            if(TextUtils.isEmpty(order.getOrderCount()) || "0".equals(order.getOrderCount())){
                                tv_engineer_count.setVisibility(View.GONE);
                            }else{
                                tv_engineer_count.setVisibility(View.VISIBLE);
                                tv_engineer_count.setText("接单量"+order.getOrderCount()+"单");
                            }

                            tv_status.setText(order.getStatusName());

                            iv_call_phone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final String phone = order.getMaintenancePhone();
                                    dialog = DialogUtil.dialog(getContext(), "拨打工程师电话", "拨号", "取消", phone, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            Utils.lxkf(getContext(), phone);
                                        }
                                    }, null, true);
                                }
                            });

                            setJuli(getContext(),order);
                        } else {
                            rv_main_content.setPadding(0,0,0,0);
                            cv_presentOrder.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        ToastUtil.showToast(getContext(), common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
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
            tv_distance.setText("工程师离线");
        } else {
            tv_distance.setText("距离您 " + (calculateLineDistance > 1000 ? calculateLineDistance / 1000 + "km" : (int) calculateLineDistance + "m"));
        }
    }

    /**
     * 初始化状态栏
     */
    private void initStatusBar() {
        rv_main_content.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollDistance = scrollDistance+dy;

               // Log.d("联网成功","scrollDistance===="+scrollDistance);
                    if(scrollDistance >= 88){
                        if(rl_title.getVisibility() == View.GONE){
                            rl_title.setVisibility(View.VISIBLE);
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rl_title.getLayoutParams();
                            layoutParams.topMargin = DisplayUtil.getStatusBarHeight(getContext());
                            rl_title.setLayoutParams(layoutParams);

                            txtActionbarTitle.setText("首页");
                            img_back.setVisibility(View.GONE);
                            ImmersionBar.with(getActivity())
                                    .statusBarColor(R.color.white)
                                    .statusBarDarkFont(true)
                                    .navigationBarColor(R.color.white).init();
                        }

                    }else{
                        if(rl_title.getVisibility() == View.VISIBLE) {
                            rl_title.setVisibility(View.GONE);
                            ImmersionBar.with(getActivity())
                                    .transparentStatusBar()
                                    .statusBarDarkFont(false)
                                    .init();
                        }
                    }
            }
        });


        /**
         * 切换时状态栏
         */
        if(scrollDistance >= 88){
            rl_title.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rl_title.getLayoutParams();
            layoutParams.topMargin = DisplayUtil.getStatusBarHeight(getContext());
            rl_title.setLayoutParams(layoutParams);

            txtActionbarTitle.setText("首页");
            img_back.setVisibility(View.GONE);
            ImmersionBar.with(getActivity())
                    .statusBarColor(R.color.white)
                    .statusBarDarkFont(true)
                    .navigationBarColor(R.color.white).init();
        }else{
            rl_title.setVisibility(View.GONE);
            ImmersionBar.with(getActivity())
                    .transparentStatusBar()
                    .statusBarDarkFont(false)
                    .init();
        }
    }


    /**
     * 获取滚动公告信息
     */
    public void getNotices() {
        HttpRequestUtils.httpRequest(getActivity(), "获取滚动公告", RequestValue.GET_ANNUNCIATE, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        ArrayList<BroadcastBean> noticesList = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<BroadcastBean>>() {
                        });
                        SharedPreferencesUtils.getInstance(getContext()).saveString("guangboStr", "");
                        mainContentAdapter.setNotices(noticesList);

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


}
