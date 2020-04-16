package com.lwc.common.module.lease_parts.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.module.BaseFragmentActivity;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.common_adapter.FragmentsPagerAdapter;
import com.lwc.common.module.lease_parts.fragment.LeaseCategoryFragment;
import com.lwc.common.module.lease_parts.fragment.LeaseHomeFragment;
import com.lwc.common.module.lease_parts.fragment.LeaseMinetFragment;
import com.lwc.common.module.lease_parts.fragment.LeaseShoppingCartFragment;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.SpUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.widget.CustomViewPager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeaseHomeActivity extends BaseFragmentActivity {

    @BindView(R.id.cViewPager)
    CustomViewPager cViewPager;
    @BindView(R.id.radio_home)
    RadioButton radioHome;
    @BindView(R.id.radio_order)
    RadioButton radioOrder;
    @BindView(R.id.radio_news)
    RadioButton radioNews;
    @BindView(R.id.radio_mine)
    RadioButton radioMine;
    @BindView(R.id.group_tab)
    RadioGroup groupTab;
    @BindView(R.id.my_content_view)
    RelativeLayout myContentView;

    /**
     * fragment相关
     */
    private HashMap<Integer, Fragment> fragmentHashMap;
    private HashMap rButtonHashMap;

    /**
     * 用户信息相关
     */
    private SharedPreferencesUtils preferencesUtils;
    public static User user;

    /**
     * 获取当前实例
     */
    public static LeaseHomeActivity activity;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lease_home);
        ButterKnife.bind(this);

        activity = this;

        initView();

        addFragmenInList();
        addRadioButtonIdInList();
        bindViewPage(fragmentHashMap);
        cViewPager.setCurrentItem(0, false);
        radioHome.setChecked(true);

        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .navigationBarColor(R.color.white).init();
    }

    @Override
    public void initView() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 往rButtonHashMap中添加 RadioButton Id
     */
    private void addRadioButtonIdInList() {
        rButtonHashMap = new HashMap<>();
        rButtonHashMap.put(0, radioHome);
        rButtonHashMap.put(1, radioNews);
        rButtonHashMap.put(2, radioOrder);
        rButtonHashMap.put(3, radioMine);
    }

    /**
     * 往fragmentHashMap中添加 Fragment
     */
    private void addFragmenInList() {

        fragmentHashMap = new HashMap<>();
        fragmentHashMap.put(0, new LeaseHomeFragment());
        fragmentHashMap.put(1, new LeaseCategoryFragment());
        fragmentHashMap.put(2, new LeaseShoppingCartFragment());
        fragmentHashMap.put(3, new LeaseMinetFragment());
    }

    private void bindViewPage(HashMap<Integer, Fragment> fragmentList) {
        //是否滑动
        cViewPager.setPagingEnabled(false);
        cViewPager.setOffscreenPageLimit(4);//最多缓存4个页面
        cViewPager.setAdapter(new FragmentsPagerAdapter(getSupportFragmentManager(), fragmentList));
        cViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                cViewPager.setChecked(rButtonHashMap, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    int type = 0;

    @OnClick({R.id.radio_home, R.id.radio_order, R.id.radio_news, R.id.radio_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.radio_home:
                cViewPager.setCurrentItem(0, false);
                break;
            case R.id.radio_news:
                cViewPager.setCurrentItem(1, false);
                break;
            case R.id.radio_order:
                cViewPager.setCurrentItem(2, false);
                break;
            case R.id.radio_mine:
                cViewPager.setCurrentItem(3, false);
                break;
        }
    }

    @Override
    public void initEngines() {
    }

    @Override
    public void getIntentData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
