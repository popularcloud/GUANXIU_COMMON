package com.lwc.common.module.common_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Created by 何栋 on 2016/12/15.
 * 294663966@qq.com
 * 通用viewpager 嵌套 fragment适配器
 */
public class FragmentsPagerAdapter extends FragmentPagerAdapter {

    private HashMap<Integer, Fragment> fragmentList;
    public FragmentsPagerAdapter(FragmentManager fm, HashMap<Integer, Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList==null?0:fragmentList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
