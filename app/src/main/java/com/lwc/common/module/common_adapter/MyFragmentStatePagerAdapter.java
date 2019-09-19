package com.lwc.common.module.common_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;


public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private HashMap<Integer, Fragment> fragmentList;

    public MyFragmentStatePagerAdapter(FragmentManager fm, HashMap<Integer, Fragment> fragmentList) {
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
       //super.destroyItem(container, position, object);
       // fragmentList.remove(object);
    }
}
