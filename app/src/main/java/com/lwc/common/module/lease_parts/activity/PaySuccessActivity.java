package com.lwc.common.module.lease_parts.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.lease_parts.fragment.LeaseShoppingCartFragment;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.MsgReadUtil;
import com.lwc.common.utils.PopupWindowUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 支付成功
 */
public class PaySuccessActivity extends BaseActivity {

    @BindView(R.id.tv_to_home)
    TextView tv_to_home;
    @BindView(R.id.tv_look_order)
    TextView tv_look_order;

    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_integral)
    TextView tv_integral;
    @BindView(R.id.tv_msg)
    TextView tv_msg;

    @BindView(R.id.fragment_container)
    FrameLayout fragment_container;


    private LeaseShoppingCartFragment leaseShoppingCartFragment;
    private FragmentManager fragmentManager;
    private User user;


    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_pay_success;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        //获取未读租赁消息
        MsgReadUtil.hasMessage(PaySuccessActivity.this,tv_msg);

    }

    @Override
    protected void init() {
        setTitle("");

        leaseShoppingCartFragment = new LeaseShoppingCartFragment();
        fragmentManager = getSupportFragmentManager();

        setRight(R.drawable.ic_more_black, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindowUtil.showHeaderPopupWindow(PaySuccessActivity.this,imgRight,leaseShoppingCartFragment,fragment_container,fragmentManager);
            }
        });

        String integral = getIntent().getStringExtra("integral");
        String price = getIntent().getStringExtra("price");

        tv_price.setText("实付金额："+ Utils.getMoney(Utils.chu(price,"100")) + "元");
        tv_integral.setText("获得"+ Utils.chu(integral,"100")  +"积分，可到积分商城兑换物品");

        getUserInfor();
    }

    @Override
    protected void initGetData() {

    }

    @Override
    protected void widgetListener() {

    }

    @OnClick({R.id.tv_to_home,R.id.tv_look_order})
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.tv_to_home:
                IntentUtil.gotoActivity(PaySuccessActivity.this, LeaseHomeActivity.class);
                break;
            case R.id.tv_look_order:
                IntentUtil.gotoActivity(PaySuccessActivity.this, MyLeaseOrderListActivity.class);
                break;
        }
    }


    /**
     * 更新用户信息
     */
    private void getUserInfor() {
        HttpRequestUtils.httpRequest(PaySuccessActivity.this, "getUserInfor", RequestValue.USER_INFO, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if (common != null) {
                    switch (common.getStatus()) {
                        case "1":
                            user = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), User.class);
                            if(user != null){
                                SharedPreferencesUtils.getInstance(PaySuccessActivity.this).saveObjectData(user);
                            }

                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }
}
