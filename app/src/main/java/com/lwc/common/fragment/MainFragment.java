package com.lwc.common.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.adapter.BannerViewHolder;
import com.lwc.common.adapter.MyGridViewAdpter;
import com.lwc.common.adapter.MyViewPagerAdapter;
import com.lwc.common.adapter.OrderViewHolder;
import com.lwc.common.configs.ServerConfig;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.interf.OnCommClickCallBack;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.bean.BroadcastBean;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Order;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.module.bean.Repairman;
import com.lwc.common.module.bean.Repairs;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.common_adapter.NearbyRepairAdapter;
import com.lwc.common.module.integral.activity.IntegralLuckDrawActivity;
import com.lwc.common.module.integral.activity.IntegralMainActivity;
import com.lwc.common.module.login.ui.LoginActivity;
import com.lwc.common.module.mine.MyInvitationCodeActivity;
import com.lwc.common.module.nearby.ui.RepairmanInfoActivity;
import com.lwc.common.module.order.ui.activity.OrderDetailActivity;
import com.lwc.common.module.order.ui.activity.PackageDetailActivity;
import com.lwc.common.module.order.ui.activity.PackageListActivity;
import com.lwc.common.module.partsLib.ui.activity.PartsMainActivity;
import com.lwc.common.module.repairs.ui.activity.ApplyForMaintainActivity;
import com.lwc.common.module.repairs.ui.activity.MalfunctionSelectActivity;
import com.lwc.common.module.zxing.activity.CaptureActivity;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.SystemUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.AutoHorizontalScrollTextView;
import com.lwc.common.view.AutoVerticalScrollTextView;
import com.lwc.common.view.ImageCycleView;
import com.lwc.common.view.MyGridView;
import com.lwc.common.view.MyTextView;
import com.lwc.common.widget.ShowImgWithBtnDialog;
import com.yanzhenjie.sofia.Sofia;
import com.yanzhenjie.sofia.StatusView;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import org.byteam.superadapter.OnItemClickListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 *
 * @author 何栋
 * @date 2018年5月13日
 * @Copyright: lwc
 */
public class MainFragment extends BaseFragment implements AMapLocationListener {


    @BindView(R.id.txtActionbarTitle)
    MyTextView txtActionbarTitle;//标题栏
    @BindView(R.id.imgRight)
    ImageView imgRight;
    @BindView(R.id.iv_refresh)
    ImageView iv_refresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;//附近工程师列表
    @BindView(R.id.ad_view)
    ImageCycleView mAdView;//轮播图
    @BindView(R.id.banner_normal)
    MZBannerView mMZBanner;
    @BindView(R.id.order_normal)
    MZBannerView orderMZBanner;
    @BindView(R.id.ll_gb)
    LinearLayout ll_gb;//广播
    @BindView(R.id.tv_gb)
    AutoVerticalScrollTextView tv_gb;
    @BindView(R.id.tv_gd)
    AutoHorizontalScrollTextView tv_gd;

    @BindView(R.id.title_layout)
    ViewGroup titleContainer;

    @BindView(R.id.ll_order)//最新订单
    LinearLayout llGetOrderMention;

    //滚动套餐布局数据
    @BindView(R.id.rl_pak_content)
    RelativeLayout rl_pak_content;
    @BindView(R.id.rl_pak_title)
     RelativeLayout rl_pak_title;
    @BindView(R.id.v_pak_title)
     View v_pak_title;

    @BindView(R.id.tv_office_equipment)
    TextView tv_office_equipment;
    @BindView(R.id.tv_electric)
    TextView tv_electric;

    @BindView(R.id.viewpager)
    ViewPager viewpager;//快速维修菜单
    @BindView(R.id.points)
    LinearLayout group;
    @BindView(R.id.status_view)
    StatusView status_view;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nested_scroll_view;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;

    @BindView(R.id.btn_factory_after)
    TextView btn_factory_after;

    /**
     * 当前实例
     */
    public static MainFragment mainFragment;

    private List<Repairs> listDatas01;//总的数据源  设备类型
    private List<Repairs> listDatas02;//总的数据源  设备类型

    //定位相关
    public AMapLocationClient mLocationClient = null;
    private Location mLocation;

    private SharedPreferencesUtils preferencesUtils;
    private User user;

    //套餐列表
    private ArrayList<PackageBean> listI = new ArrayList<>();

    //广告轮播图列表
    private ArrayList<ADInfo> infos = new ArrayList<>();

    //附近工程师列表
    private ArrayList<Repairman> repairmanArrayList = new ArrayList<>();
    private NearbyRepairAdapter adapter;
    private int curPage=1, pages, number = 0;

    //广播通知列表
    private ArrayList<BroadcastBean> list;

    //最新订单状态
    private ArrayList<Order> newestOrders;

    //滚动广播是否滚动
    private boolean isTrue = false;

    //对话框
    private Dialog dialog;
    private ShowImgWithBtnDialog showImgWithBtnDialog;


    //小圆点图片的集合
    private ImageView[] ivPoints;
    //GridView作为一个View对象添加到ViewPager集合中
    private List<View> viewPagerList;
    //每页显示的最大的数量
    private int totalPage, mPageSize = 8;
    private Long deviceMold = 1L;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, view);
        mainFragment = this;
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

        //获取滚动公告信息
        getGb();

        getAdvertising();
        getTypeAll();
        setAnyBarAlpha(0);
        initStatusBar();
        tv_gd.setSelected(true);
        getAdForInvitation();

    }

    //初始化状态栏
    private void initStatusBar() {
        nested_scroll_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int headerHeight = titleContainer.getHeight();
                int scrollDistance = Math.min(scrollY, headerHeight);
                int statusAlpha = (int) ((float) scrollDistance / (float) headerHeight * 255F);
                if(scrollDistance >= headerHeight){
                    titleContainer.setBackgroundColor(Color.parseColor("#ffffff"));
                    txtActionbarTitle.setVisibility(View.VISIBLE);
                    txtActionbarTitle.setTextColor(Color.parseColor("#000000"));
                    ImmersionBar.with(getActivity())
                            .statusBarColor(R.color.white)
                            .statusBarDarkFont(true).init();
                }else{
                    titleContainer.setBackgroundColor(Color.parseColor("#00000000"));
                    txtActionbarTitle.setVisibility(View.GONE);
                    ImmersionBar.with(getActivity())
                            .transparentStatusBar()
                            .statusBarDarkFont(true)
                            .fitsSystemWindows(false).init();
                }
                setAnyBarAlpha(statusAlpha);
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && getActivity() != null){
            ImmersionBar.with(getActivity())
                    .statusBarColor(R.color.white)
                    .statusBarDarkFont(true).init();
        }
    }

    private void setAnyBarAlpha(int alpha) {
        status_view.getBackground().mutate().setAlpha(alpha);
    }

    /**
     * 初适化定位
     */
    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。

        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        //获取一次定位结果：
        option.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        option.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        option.setNeedAddress(true);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(option);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void init() {
        txtActionbarTitle.setText("首页");
        preferencesUtils = SharedPreferencesUtils.getInstance(getContext());
        user = preferencesUtils.loadObjectData(User.class);
        //初始化扫一扫
        imgRight.setImageResource(R.drawable.sweep_code);
        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.gotoLogin(user, getActivity()))
                    IntentUtil.gotoActivityForResult(getActivity(), CaptureActivity.class, 8869);
            }
        });
        //初始化附近工程师适配器
        if (adapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new NearbyRepairAdapter(getContext(), repairmanArrayList, R.layout.item_nearby_repairman);
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int viewType, int position) {
                    if (Utils.gotoLogin(user, getActivity())) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("repair_id", adapter.getItem(position).getMaintenanceId());
                        IntentUtil.gotoActivity(getActivity(), RepairmanInfoActivity.class, bundle);
                    }
                }
            });
            recyclerView.setAdapter(adapter);
        }
        //开始定位
        initLocation();
    }

    private void initMZBanner() {
        // 设置数据
        mMZBanner.setPages(listI, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        mMZBanner.setIndicatorVisible(false);
        mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                PackageBean pack = listI.get(position);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("package", pack);
                IntentUtil.gotoActivity(getActivity(), PackageDetailActivity.class, bundle2);
            }
        });
        if(listI != null && listI.size() > 1){
            mMZBanner.start();
        }else{
            mMZBanner.setCanLoop(false);
        }

    }

    private void intOrderMZBanner(){
        orderMZBanner.setPages(newestOrders, new MZHolderCreator<OrderViewHolder>() {
            @Override
            public OrderViewHolder createViewHolder() {
                return new OrderViewHolder();
            }
        });
        orderMZBanner.setIndicatorVisible(false);
        orderMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", newestOrders.get(position));
                IntentUtil.gotoActivity(getActivity(), OrderDetailActivity.class, bundle);
            }
        });
        orderMZBanner.start();
    }

    @Override
    public void initEngines(View view) { }

    @Override
    public void getIntentData() {
    }

    @Override
    public void setListener() {
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        if (user != null)
            getNewestOrder();
        if (list != null && list.size() > 1 && !isTrue) {
            startGd();
        }
        if ( infos != null && infos.size() > 0) {
            mAdView.setImageResources(infos, mAdCycleViewListener);
            mAdView.startImageCycle();
        }
        if (mMZBanner != null && listI.size() > 0)
            mMZBanner.start();//开始轮播
    }

    @Override
    public void onPause() {
        super.onPause();
        isTrue = false;
        mAdView.pushImageCycle();
        if (mMZBanner != null && listI.size() > 0)
            mMZBanner.pause();//暂停轮播
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isTrue = false;
        mAdView.pushImageCycle();
    }

    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if("4".equals(info.getUrlType())){
                if (Utils.gotoLogin(user, getActivity())) {
                    IntentUtil.gotoActivity(getActivity(), IntegralLuckDrawActivity.class);
                }
            }else{
                // 点击图片后,有内链和外链的区别
                Bundle bundle = new Bundle();
                if (TextUtils.isEmpty(info.getAdvertisingUrl())){
                    return;
                }
                bundle.putString("url", info.getAdvertisingUrl());
                if (!TextUtils.isEmpty(info.getAdvertisingTitle()))
                    bundle.putString("title", info.getAdvertisingTitle());
                IntentUtil.gotoActivity(getActivity(), InformationDetailsActivity.class, bundle);
                //ToastUtil.showToast(getActivity(),"点击了图片");
            }

        }

        @Override
        public void displayImage(final String imageURL, final ImageView imageView) {
            ImageLoaderUtil.getInstance().displayFromNetD(getActivity(), imageURL, imageView);// 使用ImageLoader对图片进行加装！
        }
    };

    /**
     * 获取最新订单
     */
    public void getNewestOrder() {
        if (Utils.isFastClick(5000, RequestValue.ORDER_VIEW) || user == null) {
            return;
        }
        HttpRequestUtils.httpRequest(getActivity(), "getNewestOrder", RequestValue.ORDER_VIEW, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        newestOrders = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<Order>>() {
                        });
                        if (newestOrders != null && newestOrders.size() > 0) {
                            llGetOrderMention.setVisibility(View.VISIBLE);

                            intOrderMZBanner();

                            if (repairmanArrayList == null || repairmanArrayList.size() == 0) {
                                getNearEngineer();
                            }
                        } else {
                            llGetOrderMention.setVisibility(View.GONE);
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

    @OnClick({R.id.btn_meal, R.id.tvMoreMeal, R.id.btn_repairs, R.id.btn_factory_after, R.id.btn_map, R.id.btn_kefu,
            R.id.ll_order, R.id.tv_refresh,R.id.tv_office_equipment,R.id.tv_electric})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_meal:
            case R.id.tvMoreMeal:
                if (Utils.gotoLogin(user, getActivity())) {
                    IntentUtil.gotoActivity(getActivity(), PackageListActivity.class);
                }
                break;
            case R.id.btn_repairs:
                if (Utils.gotoLogin(user, getActivity())) {
                    startRepairActivity();
                }
                break;
            case R.id.btn_factory_after:

                if("厂家售后".equals(((TextView)view).getText().toString())){
                    if (Utils.gotoLogin(user, getActivity()))
                        DialogUtil.dialogBind(getActivity(), "", "此功能正在建设中，多谢亲对我们的关注！", "确定", null);
                }else{
                    if (Utils.gotoLogin(user, getActivity()))
                    IntentUtil.gotoActivity(getActivity(), PartsMainActivity.class);
                }
                break;
            case R.id.btn_map: //改成积分兑换
             //   IntentUtil.gotoActivity(getActivity(), IntegralLuckDrawActivity.class);
               if (Utils.gotoLogin(user, getActivity()))
                    IntentUtil.gotoActivity(getActivity(), IntegralMainActivity.class);
                break;
            case R.id.ll_order:
                break;
            case R.id.btn_kefu:
                dialog = DialogUtil.dialog(getActivity(), "拨打客服电话", "拨号", "取消", "400-881-0769", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Utils.lxkf(getActivity(), null);
                    }
                }, null, true);
                break;
            case R.id.tv_refresh:
                Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.version_image_rotate);
                LinearInterpolator lin = new LinearInterpolator();
                operatingAnim.setInterpolator(lin);
                if (operatingAnim != null) {
                    iv_refresh.startAnimation(operatingAnim);
                }
                if (curPage >= pages) {
                    curPage = 1;
                } else {
                    curPage++;
                }
                getNearEngineer();
                break;
            case R.id.tv_office_equipment: //显示办公设备快捷方式
                tv_electric.setTextColor(Color.parseColor("#000000"));
                tv_office_equipment.setTextColor(Color.parseColor("#1481ff"));
                deviceMold = 1L;
                getTypeAll();
                break;
            case R.id.tv_electric: //显示家用电器快捷方式
                tv_electric.setTextColor(Color.parseColor("#1481ff"));
                tv_office_equipment.setTextColor(Color.parseColor("#000000"));
                deviceMold = 3L;
                getTypeAll();
                break;
        }
    }

    /**
     * 获取设备类型
     */
    private void getTypeAll() {
        if(deviceMold == 1){
            if(listDatas01 != null && listDatas01.size() > 0){
                initDeviceType(listDatas01);
            }
        }else{
            if(listDatas02 != null && listDatas02.size() > 0){
                initDeviceType(listDatas02);
            }
        }
        HttpRequestUtils.httpRequest(getActivity(), "getAll", RequestValue.GET_TYPE_ALL_BY_UPER+"?deviceMold="+deviceMold+"&clientType=user", null, "GET", new HttpRequestUtils.ResponseListener() {

            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        if("1".equals(deviceMold)){
                            listDatas01 = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<Repairs>>() {
                            });
                            initDeviceType(listDatas01);
                        }else{
                            listDatas02 = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<Repairs>>() {
                            });
                            initDeviceType(listDatas02);
                        }

                        break;
                    default:
                        LLog.i("getTypeAll" + common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                getAdvertising();
            }
        });
    }

    private void initDeviceType(List<Repairs> listDatas) {
        totalPage = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<>();
        for (int i = 0; i < totalPage; i++) {
            //每个页面都是inflate出一个新实例
            final MyGridView gridView = (MyGridView) View.inflate(getActivity(), R.layout.home_gridview, null);
            gridView.setAdapter(new MyGridViewAdpter(getContext(), listDatas, i, mPageSize));
            //添加item点击监听
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    final Repairs obj = (Repairs)gridView.getItemAtPosition(position);
                    obj.setDeviceTypeMold(deviceMold);
                    user = preferencesUtils.loadObjectData(User.class);
                    //测试 先注释掉 发布版本的时候记得判断权限
                    if (user == null) {
                        SharedPreferencesUtils.getInstance(getActivity()).saveString("token","");
                        IntentUtil.gotoActivity(getActivity(), LoginActivity.class);
                        return;
                    }
                    if ((user.getRoleId() != null && user.getRoleId().equals("5"))) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("repairs", obj);
                        IntentUtil.gotoActivity(getActivity(), MalfunctionSelectActivity.class, bundle);
                    } else {
                        ToastUtil.showLongToast(getContext(),"权限不足，请重新登录");
                    }
                }
            });
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }
        //设置ViewPager适配器
        viewpager.setAdapter(new MyViewPagerAdapter(viewPagerList));
        if (totalPage > 1) {
            //添加小圆点
            ivPoints = new ImageView[totalPage];
            group.removeAllViews();
            for (int i = 0; i < totalPage; i++) {
                //循坏加入点点图片组
                ivPoints[i] = new ImageView(getContext());
                if (i == 0) {
                    ivPoints[i].setImageResource(R.drawable.home_quickrepair_turnpage);
                } else {
                    ivPoints[i].setImageResource(R.drawable.home_quickrepair_turnpage2);
                }
                ivPoints[i].setPadding(8, 8, 8, 8);
                group.addView(ivPoints[i]);
            }
            //设置ViewPager的滑动监听，主要是设置点点的背景颜色的改变
            viewpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < totalPage; i++) {
                        if (i == position) {
                            ivPoints[i].setImageResource(R.drawable.home_quickrepair_turnpage);
                        } else {
                            ivPoints[i].setImageResource(R.drawable.home_quickrepair_turnpage2);
                        }
                    }
                }
            });
        }
    }

    /**
     * 启动报修界面
     */
    private void startRepairActivity() {
        user = preferencesUtils.loadObjectData(User.class);
        if (!Utils.gotoLogin(user, getActivity())) {
            return;
        }
        if ((user.getRoleId() != null && user.getRoleId().equals("5"))) {
            IntentUtil.gotoActivity(getActivity(), ApplyForMaintainActivity.class);
        } else {
            ToastUtil.showLongToast(getContext(),"您没有权限报修！");
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getLatitude() > 0 && aMapLocation.getLongitude() > 0) {
            com.lwc.common.bean.Location lo = new com.lwc.common.bean.Location();
            lo.setLatitude(aMapLocation.getLatitude());
            lo.setLongitude(aMapLocation.getLongitude());
            lo.setStrValue(aMapLocation.toString());
            preferencesUtils.saveObjectData(lo);
            mLocation = aMapLocation;
            if (user != null) {
                user.setLat(aMapLocation.getLatitude() + "");
                user.setLon(aMapLocation.getLongitude() + "");
                updateUserData(aMapLocation);
            }
            getNearEngineer();
            LLog.i("定位结果：" + mLocation.toString());

            //保存定位信息
            String province = aMapLocation.getProvince();
            String city = aMapLocation.getCity();

            SharedPreferencesUtils.setParam(getActivity(),"address_province",province);
            SharedPreferencesUtils.setParam(getActivity(),"address_city",city);
        /*    SharedPreferencesUtils.setParam(getActivity(),"address_lat",aMapLocation.getLatitude());
            SharedPreferencesUtils.setParam(getActivity(),"address_long",aMapLocation.getLongitude());*/
        }
    }

    private void updateUserData(final Location aLocation) {
        if (SystemUtil.isBackground(getContext()) || user == null) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        if (aLocation != null) {
            String lat = aLocation.getLatitude() + "";
            String lon = aLocation.getLongitude() + "";
            params.put("lat", lat);
            params.put("lon", lon);
        }
        HttpRequestUtils.httpRequest(getActivity(), "updateUserData", RequestValue.UP_USER_INFOR, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if (common.getStatus().trim().equals(ServerConfig.RESPONSE_STATUS_SUCCESS)) {
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError("updateUserInfo1  " + e.toString());
            }
        });
    }

    /**
     * 获取附近工程师
     */
    private void getNearEngineer() {
        if (mLocation == null) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("type", "4");
        map.put("curPage", curPage+"");
        map.put("pageSize", "5");
        map.put("latitude", mLocation.getLatitude()+"");
        map.put("longitude", mLocation.getLongitude()+"");
        HttpRequestUtils.httpRequest(getActivity(), "getNearEngineer", RequestValue.NEAR_ENGINEER, map, "GET", new HttpRequestUtils.ResponseListener() {

            @Override
            public void getResponseData(String response) {
                iv_refresh.clearAnimation();
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        try {
                            JSONObject object = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
                            pages = object.optInt("pages");
                            repairmanArrayList = JsonUtil.parserGsonToArray(object.optString("data"), new TypeToken<ArrayList<Repairman>>() {
                            });
                            adapter.replaceAll(repairmanArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                iv_refresh.clearAnimation();
            }
        });
    }

    /**
     * 获取广告图
     */
    private void getAdvertising() {
        Map<String,String> params = new HashMap<>();
        params.put("local","1");
        params.put("role","4");//角色为个人
        HttpRequestUtils.httpRequest(getActivity(), "获取广告轮播图", RequestValue.GET_ADVERTISING, params, "GET", new HttpRequestUtils.ResponseListener() {

            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        infos = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<ADInfo>>() {
                        });
                        mAdView.setImageResources(infos, mAdCycleViewListener);
                        mAdView.startImageCycle();
                        break;
                    default:
                        LLog.i("getAdvertising" + common.getInfo());
                        break;
                }

                getMealList();
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                getMealList();
            }
        });
    }

    public void informInvite(final Repairman rep) {
        if (!Utils.gotoLogin(user, getActivity())) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("maintenanceId", rep.getMaintenanceId());
        HttpRequestUtils.httpRequest(getActivity(), "informInvite", RequestValue.INFORM_INVITE, params, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if (common.getStatus().equals(ServerConfig.RESPONSE_STATUS_SUCCESS)) {
                    int index = repairmanArrayList.indexOf(rep);
                    rep.setIsInvite(1);
                    repairmanArrayList.set(index, rep);
                    adapter.replaceAll(repairmanArrayList);
                    DialogUtil.dialogBind(getActivity(), "", "您的维修邀请已发出，请亲耐心等待一下哦！", "确定", null);
                } else if (common.getStatus().equals("5")){
                    dialog = DialogUtil.dialog(getActivity(), "温馨提示", "马上报修", "稍候再说", "您当前没有待接单的订单，如需邀请工程师维修，请先去报修！", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            startRepairActivity();
                        }
                    }, null, false);
                } else {
                    ToastUtil.showLongToast(getActivity(), common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError("updateUserInfo1  " + e.toString());
                ToastUtil.showLongToast(getActivity(), msg);
            }
        });
    }

    /**
     * 获取滚动公告信息
     */
    public void getGb() {
        isTrue = false;
        HttpRequestUtils.httpRequest(getActivity(), "getGb", RequestValue.GET_ANNUNCIATE, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        list = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<BroadcastBean>>() {
                        });
                        preferencesUtils.saveString("guangboStr", "");
                        if (list != null && list.size() > 0) {
                            ll_gb.setVisibility(View.VISIBLE);
                            for (int i = 0; i < list.size(); i++) {
                                BroadcastBean bb = list.get(i);
                                if (bb.getIsExtend() == 1 && !TextUtils.isEmpty(bb.getAnnunciateContentExtend()) && bb.getIsValid() == 1) {
                                    preferencesUtils.saveString("guangboStr", bb.getAnnunciateContentExtend());
                                    break;
                                }
                            }
                            for (int i = 0; i < list.size(); i++) {
                                BroadcastBean bb = list.get(i);
                                if (TextUtils.isEmpty(bb.getAnnunciateContent())) {
                                    list.remove(i);
                                }
                            }
                            if (list.size() > 1) {
                                tv_gd.setVisibility(View.GONE);
                                tv_gb.setVisibility(View.VISIBLE);
                                tv_gb.setText(list.get(0).getAnnunciateContent());
                                startGd();
                            } else if (list.size() > 0) {
                                tv_gd.setVisibility(View.VISIBLE);
                                tv_gd.setSelected(true);
                                tv_gb.setVisibility(View.GONE);
                                String content = list.get(0).getAnnunciateContent();
                                if (content.length() < 30 && content.length() >= 20) {
                                    content = content+ "                                  ";
                                } else if (content.length() < 20 && content.length() >= 15) {
                                    content = content + "                                            ";
                                } else if (content.length() < 15 && content.length() >= 10) {
                                    content = content + "                                                      ";
                                }

                                tv_gd.setText(content);
                            }
                        } else {
                            ll_gb.setVisibility(View.GONE);
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

    private void startGd() {
        isTrue = true;
        new Thread() {
            @Override
            public void run() {
                while (isTrue) {
                    SystemClock.sleep(3000);
                    handler.sendEmptyMessage(199);
                }
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 199) {
                tv_gb.next();
                number++;
                tv_gb.setText(list.get(number % list.size()).getAnnunciateContent());
            }
        }
    };

    /**
     * 获取套餐信息
     */
    private void getMealList() {
        String user_role = (String) SharedPreferencesUtils.getParam(getContext(),"user_role","0");
        if(!"3".equals(user_role)){
            rl_pak_content.setVisibility(View.VISIBLE);
            rl_pak_title.setVisibility(View.VISIBLE);
            v_pak_title.setVisibility(View.VISIBLE);
        }else{
            rl_pak_content.setVisibility(View.GONE);
            rl_pak_title.setVisibility(View.GONE);
            v_pak_title.setVisibility(View.GONE);
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("location", "1");
        HttpRequestUtils.httpRequest(getActivity(), "getMealList", RequestValue.GET_PACKAGE_LIST, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String result) {
                Common common = JsonUtil.parserGsonToObject(result, Common.class);
                if (common.getStatus().equals("1")) {
                    ArrayList<PackageBean> current = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(result, "data"), new TypeToken<ArrayList<PackageBean>>() {
                    });
                    if (current != null && current.size() > 0) {
                        rl_pak_content.setVisibility(View.VISIBLE);
                        rl_pak_title.setVisibility(View.VISIBLE);
                        v_pak_title.setVisibility(View.VISIBLE);
                        listI = current;
                        initMZBanner();
                    } else {
                        listI = new ArrayList<>();
                        rl_pak_content.setVisibility(View.GONE);
                        rl_pak_title.setVisibility(View.GONE);
                        v_pak_title.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
            }
        });
    }

    /**
     * 弹出邀请注册提示框
     */
    private void getAdForInvitation() {
        Map<String,String> params = new HashMap<>();
        params.put("local","10");
        params.put("role","4");
        HttpRequestUtils.httpRequest(getActivity(), "弹出邀请注册提示框", RequestValue.GET_ADVERTISING, params, "GET", new HttpRequestUtils.ResponseListener() {

            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                       List<ADInfo> invatationInfos = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<ADInfo>>() {
                        });

                        if(invatationInfos != null && invatationInfos.size() > 0){
                            showImgWithBtnDialog = new ShowImgWithBtnDialog(getActivity(), invatationInfos.get(0).getAdvertisingImageUrl()).init(new OnCommClickCallBack() {
                                @Override
                                public void onCommClick(Object object) {
                                    if("left".equals(object)){
                                        showImgWithBtnDialog.dismiss();
                                    }else{
                                        if (Utils.gotoLogin(user, getActivity()))
                                            IntentUtil.gotoActivity(getActivity(), MyInvitationCodeActivity.class);
                                    }
                                }
                            });
                            showImgWithBtnDialog.show();
                        }
                        break;
                    default:
                        LLog.i("获取邀请注册信息失败！" + common.getInfo());
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
