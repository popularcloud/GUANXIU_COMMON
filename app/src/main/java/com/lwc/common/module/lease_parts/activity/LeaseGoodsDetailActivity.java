package com.lwc.common.module.lease_parts.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.utils.DisplayUtil;
import com.lwc.common.view.ImageCycleView;
import com.lwc.common.widget.SelectGoodTypeDialog;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author younge
 * @date 2018/12/19 0019
 * @email 2276559259@qq.com
 */
public class LeaseGoodsDetailActivity extends BaseActivity{

    @BindView(R.id.ad_view)
    ImageCycleView ad_view;
    @BindView(R.id.tv_add_cart)
    TextView tv_add_cart;


    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_lease_good_detail;
    }

    @Override
    protected void findViews() {
    }

    @Override
    protected void init() {

        int screenWidth = DisplayUtil.getScreenWidth(this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ad_view.getLayoutParams();
        layoutParams.height = screenWidth;
        ad_view.setLayoutParams(layoutParams);

        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(false)
                .init();
    }

    @OnClick(R.id.tv_add_cart)
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.tv_add_cart:
                SelectGoodTypeDialog selectGoodTypeDialog = new SelectGoodTypeDialog(LeaseGoodsDetailActivity.this,null,null,null);
                selectGoodTypeDialog.show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initGetData() {
    }


    @Override
    protected void widgetListener() {

    }
}
