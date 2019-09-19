package com.lwc.common.module.integral.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.interf.OnItemClickCallBack;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.integral.adapter.GoodsAdapter;
import com.lwc.common.module.integral.bean.IntegralgoodsBean;
import com.lwc.common.module.integral.bean.UserIntegralBean;
import com.lwc.common.module.integral.presenter.IntegralMainPresenter;
import com.lwc.common.module.integral.presenter.IntegralOrderPresenter;
import com.lwc.common.module.integral.view.IntegralMainView;
import com.lwc.common.module.integral.view.IntegralOrderView;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.SpaceItemDecoration;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.ImageCycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author younge
 * @date 2019/3/25 0025
 * @email 2276559259@qq.com
 * desc 积分兑换
 */
public class IntegralMainActivity extends BaseActivity implements IntegralMainView ,IntegralOrderView{

    @BindView(R.id.ad_view)
    ImageCycleView mAdView;//轮播图
    @BindView(R.id.rv_goods)
    RecyclerView rv_goods; //商品
    @BindView(R.id.rl_title)
    RelativeLayout rl_title; //商品

    private ArrayList<ADInfo> infos = new ArrayList<>();//广告轮播图
    private ArrayList<IntegralgoodsBean> goodsBeens = new ArrayList<>();//商品信息
    private GoodsAdapter goodsAdapter;
    IntegralMainPresenter presenter;
    IntegralOrderPresenter integralOrderPresenter;
    private int currentPage;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_integral_main;
    }

    @Override
    protected void findViews() {
        setTitle("积分兑换");
        goodsAdapter = new GoodsAdapter(this, goodsBeens, new OnItemClickCallBack() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("goodsId",goodsBeens.get(position).getIntegralId());
                IntentUtil.gotoActivity(IntegralMainActivity.this, IntegralGoodsDetailActivity.class,bundle);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        rv_goods.setLayoutManager(gridLayoutManager);
        rv_goods.addItemDecoration(new SpaceItemDecoration(25,0,SpaceItemDecoration.GRIDLAYOUT));
        rv_goods.setAdapter(goodsAdapter);
        presenter = new IntegralMainPresenter(this);
        integralOrderPresenter = new IntegralOrderPresenter(this,this);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initGetData() {
        presenter.getBannerData();
        presenter.getPartsData();
        currentPage = 1;
        integralOrderPresenter.getUserIntegralMsg(String.valueOf(currentPage));
    }

    @Override
    protected void widgetListener() {

    }

    /**
     * 定义轮播图监听
     */
    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {

            if("4".equals(info.getUrlType())){
                IntentUtil.gotoActivity(IntegralMainActivity.this, IntegralLuckDrawActivity.class);
            }else{
                // 点击图片后,有内链和外链的区别
                Bundle bundle = new Bundle();
                if (!TextUtils.isEmpty(info.getAdvertisingUrl()))
                    bundle.putString("url", info.getAdvertisingUrl());
                if (!TextUtils.isEmpty(info.getAdvertisingTitle()))
                    bundle.putString("title", info.getAdvertisingTitle());
                IntentUtil.gotoActivity(IntegralMainActivity.this, InformationDetailsActivity.class, bundle);
            }

        }

        @Override
        public void displayImage(final String imageURL, final ImageView imageView) {
            ImageLoaderUtil.getInstance().displayFromNetD(IntegralMainActivity.this, imageURL, imageView);// 使用ImageLoader对图片进行加装！
        }
    };

    @Override
    public void onBannerLoadSuccess(List<ADInfo> adInfoList) {
        infos.clear();
        infos.addAll(adInfoList);
        if ( infos != null && infos.size() > 0) {
            mAdView.setImageResources(infos, mAdCycleViewListener);
            mAdView.startImageCycle();
        }
    }

    @Override
    public void onLoadGoods(List<IntegralgoodsBean> partsBeanList) {
        goodsBeens.clear();
        goodsBeens.addAll(partsBeanList);
        goodsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetUserIntegral(UserIntegralBean userIntegralBean) {
            if(userIntegralBean != null) {
                if (!TextUtils.isEmpty(userIntegralBean.getIntegral())) {
                    SharedPreferencesUtils.setParam(IntegralMainActivity.this, "MyIntegral", userIntegralBean.getIntegral());
                } else {
                    SharedPreferencesUtils.setParam(IntegralMainActivity.this, "MyIntegral", "0");
                }
            }
    }

    @Override
    public void onLoadError(String msg) {
        ToastUtil.showToast(this,msg);
    }
}
