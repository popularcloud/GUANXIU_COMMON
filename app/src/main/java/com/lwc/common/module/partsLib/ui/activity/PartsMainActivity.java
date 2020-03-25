package com.lwc.common.module.partsLib.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.adapter.MyViewPagerAdapter;
import com.lwc.common.adapter.PartsGridViewAdpter;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.bean.PartsTypeBean;
import com.lwc.common.module.partsLib.presenter.PartsMainPresenter;
import com.lwc.common.module.partsLib.ui.view.PartsMainView;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.ImageCycleView;
import com.lwc.common.view.MyGridView;
import com.yanzhenjie.sofia.Sofia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author younge
 * @date 2018/12/19 0019
 * @email 2276559259@qq.com
 */
public class PartsMainActivity extends BaseActivity implements PartsMainView{

    @BindView(R.id.ad_view)
    ImageCycleView mAdView;//轮播图
    @BindView(R.id.viewpager)
    ViewPager viewpager;//快速维修菜单
    @BindView(R.id.points)
    LinearLayout group;
    @BindView(R.id.tv_search)
    TextView tv_search;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.rl_title)
    LinearLayout rl_title;

    private ArrayList<ADInfo> infos = new ArrayList<>();//广告轮播图
    private List<PartsTypeBean> listDatas;//总的数据源  设备类型
    private ImageView[] ivPoints;//小圆点图片的集合
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
    private int totalPage, mPageSize = 8;//每页显示的最大的数量
    private String searchText = "";
    private String typeId = "";

    private PartsMainPresenter partsMainPresenter;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_parts_main;
    }

    @Override
    protected void findViews() {
        partsMainPresenter = new PartsMainPresenter(this,this);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rl_title.getLayoutParams();
        layoutParams.setMargins(0, Utils.getStatusBarHeight(PartsMainActivity.this),0,0);
        rl_title.setLayoutParams(layoutParams);
    }

    @Override
    protected void init() {}


    @Override
    protected void initGetData() {
        //获取配件库轮播图
        partsMainPresenter.getBannerData();
        //获取配件类别
        partsMainPresenter.getPartsData();
    }

    @Override
    protected void widgetListener() {}

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        Sofia.with(this)
                .statusBarBackgroundAlpha(0)
                .invasionStatusBar();
    }

    /**
     * 定义轮播图监听
     */
    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            // 点击图片后,有内链和外链的区别
            Bundle bundle = new Bundle();
            if (TextUtils.isEmpty(info.getAdvertisingUrl())){
                return;
            }
                bundle.putString("url", info.getAdvertisingUrl());
            if (!TextUtils.isEmpty(info.getAdvertisingTitle()))
                bundle.putString("title", info.getAdvertisingTitle());
            IntentUtil.gotoActivity(PartsMainActivity.this, InformationDetailsActivity.class, bundle);
        }

        @Override
        public void displayImage(final String imageURL, final ImageView imageView) {
            ImageLoaderUtil.getInstance().displayFromNetD(PartsMainActivity.this, imageURL, imageView);// 使用ImageLoader对图片进行加装！
        }
    };

    /**
     * 加载配件库类型
     */
    private void initDeviceType() {
        totalPage = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<>();
        for (int i = 0; i < totalPage; i++) {
            //每个页面都是inflate出一个新实例
            final MyGridView gridView = (MyGridView) View.inflate(PartsMainActivity.this, R.layout.home_gridview, null);
            gridView.setAdapter(new PartsGridViewAdpter(PartsMainActivity.this, listDatas, i, mPageSize));
            //添加item点击监听
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    typeId = listDatas.get(position).getTypeId();
                    searchProduct();
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
                ivPoints[i] = new ImageView(PartsMainActivity.this);
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


    @OnClick({R.id.tv_search})
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.tv_search:
                searchText = et_search.getText().toString().trim();
                if(TextUtils.isEmpty(searchText)){
                    ToastUtil.showToast(PartsMainActivity.this,"请输入需要搜索的内容");
                    return;
                }
                typeId = "";
                searchProduct();
                break;
        }
    }

    /**
     * 搜索
     */
    private void searchProduct(){
        Bundle bundle = new Bundle();
        bundle.putString("searchText",searchText);
        bundle.putString("typeId",typeId);
        IntentUtil.gotoActivity(PartsMainActivity.this, PartsListActivity.class,bundle);

        et_search.setText("");
        searchText = "";
    }

    @Override
    public void onLoadPartsType(List<PartsTypeBean> partsBeanList) {
        listDatas = partsBeanList;
        initDeviceType();
    }

    @Override
    public void onLoadError(String msg) {
        ToastUtil.showToast(this,msg);
    }

    @Override
    public void onBannerLoadSuccess(List<ADInfo> adInfoList) {
        infos.clear();
        infos.addAll(adInfoList);
        if ( infos != null && infos.size() > 0) {
            mAdView.setImageResources(infos, mAdCycleViewListener);
            mAdView.startImageCycle();
        }
    }
}
