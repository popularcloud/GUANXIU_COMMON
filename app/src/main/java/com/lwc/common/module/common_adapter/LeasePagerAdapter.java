package com.lwc.common.module.common_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.lwc.common.module.lease_parts.bean.RecommendItemBean;

import java.util.ArrayList;
import java.util.HashMap;

public class LeasePagerAdapter extends FragmentPagerAdapter {

    private ArrayList<RecommendItemBean> titles;
    HashMap<Integer, Fragment> fragmentHashMap;

    public LeasePagerAdapter(FragmentManager fm, ArrayList<RecommendItemBean> list,HashMap<Integer,Fragment> fragmentHashMap) {
        super(fm);
        this.titles = list;
        this.fragmentHashMap = fragmentHashMap;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position).getRecommendName();
    }

    @Override
    public int getCount() {
        return fragmentHashMap == null?0:fragmentHashMap.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentHashMap.get(position);
    }
}
