package com.lwc.common.module;


import android.support.v4.app.Fragment;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.components.ImmersionOwner;
import com.lwc.common.R;
import com.lwc.common.view.MyTextView;


/**
 * activity的base类,用于基本数据的初始化
 *
 * @author yang
 */
public abstract class BaseFragment extends Fragment implements ImmersionOwner {

    public MyTextView txtActionbarTitle;

    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    public void onVisible() {
        lazyLoad();
    }

    /**
     * 此方法在Base中已调用，在覆写的faragment中会执行，无需再调用一次
     */
    protected abstract void lazyLoad();

    /**
     * faragment隐藏时调用此方法
     */
    public void onInvisible() {
    }

    /**
     * 初始化view 和各种相关的数据
     */
    public abstract void init();

    /**
     * 初始化引擎
     */
    public abstract void initEngines(View view);

    /**
     * 获取Intent传过来的数据
     */
    public abstract void getIntentData();

    /**
     * 设置各种监听器
     */
    public abstract void setListener();


    @Override
    public boolean immersionBarEnabled() {

        return true;
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this)
                .statusBarAlpha(0.0f)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .init();
    }

    /**
     * 懒加载，在view初始化完成之前执行
     * On lazy after view.
     */
    @Override
    public void onLazyBeforeView() {
    }

    /**
     * 懒加载，在view初始化完成之后执行
     * On lazy before view.
     */
    @Override
    public void onLazyAfterView() {
    }
}
