package com.lwc.common.module.lease_parts.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcssloop.widget.PagerGridLayoutManager;
import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.adapter.RepairTypeAdapter;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.fragment.InformationFragment;
import com.lwc.common.fragment.MineFragment;
import com.lwc.common.fragment.MyOrderFragment;
import com.lwc.common.fragment.NewMainFragment;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Repairs;
import com.lwc.common.module.common_adapter.FragmentsPagerAdapter;
import com.lwc.common.module.integral.activity.IntegralLuckDrawActivity;
import com.lwc.common.utils.DisplayUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.ImageCycleView;
import com.lwc.common.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LeaseHomeFragment extends BaseFragment{


    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_search)
    TextView tv_search;

    @BindView(R.id.ad_view)
    ImageCycleView mAdView;
    @BindView(R.id.rv_repair_type)
    RecyclerView rv_repair_type;
    @BindView(R.id.vp_goodList)
    CustomViewPager vp_goodList;

    @BindView(R.id.tv_sel01)
    TextView tv_sel01;
    @BindView(R.id.tv_sel02)
    TextView tv_sel02;
    @BindView(R.id.tv_sel03)
    TextView tv_sel03;
    @BindView(R.id.tv_sel04)
    TextView tv_sel04;
    @BindView(R.id.tv_line01)
    TextView tv_line01;
    @BindView(R.id.tv_line02)
    TextView tv_line02;
    @BindView(R.id.tv_line03)
    TextView tv_line03;
    @BindView(R.id.tv_line04)
    TextView tv_line04;

    private HashMap<Integer, Fragment> fragmentHashMap = new HashMap<>();

    /**
     * 轮播图数据
     */
    private ArrayList<ADInfo> infos;
    private ArrayList<Repairs> myLeaseTypeLists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lease_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void init() {

        img_back.setImageResource(R.drawable.arrow_left);
        tv_search.setTextColor(Color.parseColor("#1481ff"));
        tv_search.setText("搜索");

        getWheelPic();

        getLeaseType();

        initViewpager();
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

    @OnClick({R.id.tv_sel01,R.id.tv_sel02,R.id.tv_sel03,R.id.tv_sel04})
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.tv_sel01:
                changeText(1);
                break;
            case R.id.tv_sel02:
                changeText(2);
                break;
            case R.id.tv_sel03:
                changeText(3);
                break;
            case R.id.tv_sel04:
                changeText(4);
                break;
        }
    }

    private void changeText(int position){
        switch (position){
            case 1:
                tv_line01.setVisibility(View.VISIBLE);
                tv_sel01.setTextColor(Color.parseColor("#000000"));
                tv_line02.setVisibility(View.GONE);
                tv_sel02.setTextColor(Color.parseColor("#333333"));
                tv_line03.setVisibility(View.GONE);
                tv_sel03.setTextColor(Color.parseColor("#333333"));
                tv_line04.setVisibility(View.GONE);
                tv_sel04.setTextColor(Color.parseColor("#333333"));
                break;
            case 2:
                tv_line01.setVisibility(View.GONE);
                tv_sel01.setTextColor(Color.parseColor("#333333"));
                tv_line02.setVisibility(View.VISIBLE);
                tv_sel02.setTextColor(Color.parseColor("#000000"));
                tv_line03.setVisibility(View.GONE);
                tv_sel03.setTextColor(Color.parseColor("#333333"));
                tv_line04.setVisibility(View.GONE);
                tv_sel04.setTextColor(Color.parseColor("#333333"));
                break;
            case 3:
                tv_line01.setVisibility(View.GONE);
                tv_sel02.setTextColor(Color.parseColor("#333333"));
                tv_line02.setVisibility(View.GONE);
                tv_sel02.setTextColor(Color.parseColor("#333333"));
                tv_line03.setVisibility(View.VISIBLE);
                tv_sel02.setTextColor(Color.parseColor("#000000"));
                tv_line04.setVisibility(View.GONE);
                tv_sel02.setTextColor(Color.parseColor("#333333"));
                break;
            case 4:
                tv_line01.setVisibility(View.GONE);
                tv_sel02.setTextColor(Color.parseColor("#333333"));
                tv_line02.setVisibility(View.GONE);
                tv_sel02.setTextColor(Color.parseColor("#333333"));
                tv_line03.setVisibility(View.GONE);
                tv_sel02.setTextColor(Color.parseColor("#333333"));
                tv_line04.setVisibility(View.VISIBLE);
                tv_sel02.setTextColor(Color.parseColor("#000000"));
                break;
        }
    }

     /*
      *  获取轮播图
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
                            setWheelPic();
                        }
                        break;
                    default:
                        LLog.i("获取首页轮播图" + common.getInfo());
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
     * 设置轮播图
     */
    public void setWheelPic(){
        mAdView.setImageResources(infos, mAdCycleViewListener);
        mAdView.startImageCycle();
    }

    /**
     * 轮播图点击事件
     */
    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if("4".equals(info.getUrlType())){ //链接类型为4 时为内部链接  app内部跳转
                if (Utils.gotoLogin(MainActivity.user,MainActivity.activity)) {
                    IntentUtil.gotoActivity(MainActivity.activity, IntegralLuckDrawActivity.class);
                }
            }else{  //其他为外部链接 直接打开网页
                Bundle bundle = new Bundle();
                if (TextUtils.isEmpty(info.getAdvertisingUrl())){
                    return;
                }
                bundle.putString("url", info.getAdvertisingUrl());
                if (!TextUtils.isEmpty(info.getAdvertisingTitle()))
                    bundle.putString("title", info.getAdvertisingTitle());
                IntentUtil.gotoActivity(MainActivity.activity, InformationDetailsActivity.class, bundle);
            }

        }

        @Override
        public void displayImage(final String imageURL, final ImageView imageView) {
            ImageLoaderUtil.getInstance().displayFromNetD(getActivity(), imageURL, imageView);// 使用ImageLoader对图片进行加装！
        }
    };

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
                            setLeaseType();
                        }
                        break;
                    default:
                        LLog.i("获取租赁设备类型" + common.getInfo());
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
     * 设置租赁设备类型
     */
    public void setLeaseType(){
        // 布局管理器
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(1, 5, PagerGridLayoutManager.HORIZONTAL);
        rv_repair_type.setLayoutManager(layoutManager);

        // 滚动辅助器
   /*     PagerGridSnapHelper snapHelper = new PagerGridSnapHelper();
        snapHelper.attachToRecyclerView(rv_repair_type);*/

        //设置适配器
        RepairTypeAdapter repairTypeAdapter = new RepairTypeAdapter(getContext(),myLeaseTypeLists);
        rv_repair_type.setAdapter(repairTypeAdapter);
    }

    /**
     * 初始化列表
     */
    private void initViewpager() {

        addFragmenInList();

        //是否滑动
        vp_goodList.setPagingEnabled(false);
        vp_goodList.setOffscreenPageLimit(4);//最多缓存4个页面
        vp_goodList.setAdapter(new FragmentsPagerAdapter(getChildFragmentManager(), fragmentHashMap));
        vp_goodList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeText(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        vp_goodList.setCurrentItem(0, false);
    }

    /**
     * 往fragmentHashMap中添加 Fragment
     */
    private void addFragmenInList() {
        fragmentHashMap.put(0, new LeaseGoodListFragment());
        fragmentHashMap.put(1, new LeaseGoodListFragment());
        fragmentHashMap.put(2, new LeaseGoodListFragment());
        fragmentHashMap.put(3, new LeaseGoodListFragment());
    }

}
