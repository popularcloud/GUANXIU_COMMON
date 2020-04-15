package com.lwc.common.module.lease_parts.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Repairs;
import com.lwc.common.module.integral.activity.IntegralLuckDrawActivity;
import com.lwc.common.utils.DisplayUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.ImageCycleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void lazyLoad() {
        img_back.setImageResource(R.drawable.arrow_left);
        tv_search.setTextColor(Color.parseColor("#1481ff"));

        //getWheelPic();

        //getLeaseType();
    }

    @Override
    public void init() {

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
            ImageLoaderUtil.getInstance().displayFromNetD(MainActivity.activity, imageURL, imageView);// 使用ImageLoader对图片进行加装！
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
}
