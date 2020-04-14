package com.lwc.common.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.common_adapter.FragmentsPagerAdapter;
import com.lwc.common.module.order.ui.fragment.FinishFragment;
import com.lwc.common.module.order.ui.fragment.ProceedFragment;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.MyTextView;
import com.lwc.common.widget.CustomViewPager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 */
public class MyOrderFragment extends BaseFragment {


    @BindView(R.id.txtActionbarTitle)
    MyTextView txtActionbarTitle;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.rBtnUnderway)
    RadioButton rBtnUnderway;
    @BindView(R.id.rBtnFinish)
    RadioButton rBtnFinish;
    @BindView(R.id.viewLine1)
    View viewLine1;
    @BindView(R.id.viewLine3)
    View viewLine3;
    @BindView(R.id.cViewPager)
    CustomViewPager cViewPager;
    @BindView(R.id.rl_title)
    ViewGroup titleContainer;
    private HashMap rButtonHashMap;
    private HashMap<Integer, Fragment> fragmentHashMap;
    private ProceedFragment proceedFragment;
    private FinishFragment finishFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_order, null);
        ButterKnife.bind(this, view);
        initEngines(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addFragmenInList();
        addRadioButtonIdInList();
        init();
        bindViewPage(fragmentHashMap);
        CollapsingToolbarLayout.LayoutParams lp1 = (CollapsingToolbarLayout.LayoutParams) titleContainer.getLayoutParams();
        lp1.topMargin = Utils.getStatusBarHeight(getActivity());
        titleContainer.setLayoutParams(lp1);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && getActivity()!=null){
            ImmersionBar.with(getActivity())
                    .statusBarColor(R.color.white)
                    .statusBarDarkFont(true)
                    .navigationBarColor(R.color.white).init();
        }
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void init() {
        txtActionbarTitle.setText("我的订单");
        imgBack.setVisibility(View.GONE);
        cViewPager.setCurrentItem(0);
        rBtnUnderway.setChecked(true);
        showLineView(1);
    }

    @Override
    public void initEngines(View view) {
    }


    @Override
    public void setListener() {
    }


    @Override
    public void getIntentData() {
    }

    /**
     * 往fragmentHashMap中添加 Fragment
     */
    private void addFragmenInList() {

        fragmentHashMap = new HashMap<>();
        proceedFragment = new ProceedFragment();
        finishFragment = new FinishFragment();
        fragmentHashMap.put(0, proceedFragment);
        fragmentHashMap.put(1, finishFragment);
    }

    /**
     * 往rButtonHashMap中添加 RadioButton Id
     */
    private void addRadioButtonIdInList() {

        rButtonHashMap = new HashMap<>();
        rButtonHashMap.put(0, rBtnUnderway);
        rButtonHashMap.put(1, rBtnFinish);
    }

    @OnClick({R.id.rBtnUnderway, R.id.rBtnFinish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rBtnUnderway:
                showLineView(1);
                cViewPager.setCurrentItem(0);
                break;
            case R.id.rBtnFinish:
                showLineView(2);
                cViewPager.setCurrentItem(1);
                break;
        }
    }


    /**
     * 显示RadioButton线条
     *
     * @param num 1 ： 显示已发布下的线条  2 ： 显示未完成下的线条  3： 显示已完成下的线条  。未选中的线条将会被隐藏
     */
    private void showLineView(int num) {
        switch (num) {
            case 1:
                viewLine1.setVisibility(View.VISIBLE);
                viewLine3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                viewLine3.setVisibility(View.VISIBLE);
                viewLine1.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void bindViewPage(HashMap<Integer, Fragment> fragmentList) {
        //是否滑动
        cViewPager.setPagingEnabled(true);
        cViewPager.setAdapter(new FragmentsPagerAdapter(getChildFragmentManager(), fragmentList));
        cViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                cViewPager.setChecked(rButtonHashMap, position);
                showLineView(position + 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
