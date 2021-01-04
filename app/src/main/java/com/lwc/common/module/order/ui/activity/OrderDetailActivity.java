package com.lwc.common.module.order.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.BaseFragmentActivity;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Order;
import com.lwc.common.module.common_adapter.FragmentsPagerAdapter;
import com.lwc.common.module.order.ui.fragment.DeviceDetailFragment;
import com.lwc.common.module.order.ui.fragment.OrderDetailFragment;
import com.lwc.common.module.order.ui.fragment.OrderStateFragment;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.MyTextView;
import com.lwc.common.widget.CustomDialog;
import com.lwc.common.widget.CustomViewPager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 订单详情
 */
public class OrderDetailActivity extends BaseFragmentActivity {

    @BindView(R.id.txtActionbarTitle)
    MyTextView txtActionbarTitle;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.rBtnState)
    RadioButton rBtnState;
    @BindView(R.id.rBtnOrderDetail)
    RadioButton rBtnOrderDetail;
    @BindView(R.id.rBtnDeviceDetail)
    RadioButton rBtnDeviceDetail;
    @BindView(R.id.viewLine1)
    View viewLine1;
    @BindView(R.id.viewLine2)
    View viewLine2;
    @BindView(R.id.viewLine3)
    View viewLine3;
    @BindView(R.id.cViewPager)
    CustomViewPager cViewPager;
    @BindView(R.id.txtEmptyView)
    TextView txtEmptyView;
    @BindView(R.id.imgRight)
    ImageView imgRight;
    private HashMap fragmentHashMap = null;
    private HashMap rButtonHashMap = null;
    private Order myOrder = null;
    public static OrderDetailActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        activity = this;
        ButterKnife.bind(this);
        Intent intent = getIntent();
        myOrder = (Order) intent.getSerializableExtra("data");
        String orderId =  intent.getStringExtra("orderId");

        getIntentData();
        initStatusBar();

        if (myOrder != null) {
            addFragmenInList();
            addRadioButtonIdInList();
            bindViewPage(fragmentHashMap);
            initEngines();
            initView();
        } else if (!TextUtils.isEmpty(orderId)) {
            getOrderInfo(orderId);
        }
    }


    protected void initStatusBar(){
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .navigationBarColor(R.color.white).init();
    }
    /**
     * 获取订单详情
     */
    private void getOrderInfo(String orderId) {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", orderId);
        HttpRequestUtils.httpRequest(this, "查询订单详情", RequestValue.POST_ORDER_INFO, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        myOrder = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), Order.class);

                        addFragmenInList();
                        addRadioButtonIdInList();
                        bindViewPage(fragmentHashMap);
                        initEngines();
                        initView();

                        break;
                    default:
                        ToastUtil.showLongToast(OrderDetailActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                ToastUtil.showLongToast(OrderDetailActivity.this, msg);
            }
        });
    }

    @Override
    public void initView() {
        txtActionbarTitle.setText("订单详情");
        cViewPager.setCurrentItem(0);
        rBtnState.setChecked(true);
        showLineView(1);
        if (myOrder.getStatusId().equals("" + Order.STATUS_YIWANCHENG) || myOrder.getStatusId().equals("" + Order.STATUS_YIPINGJIA) || myOrder.getStatusId().equals("" + Order.STATUS_SHOUHOU)) {
            rBtnDeviceDetail.setVisibility(View.VISIBLE);
            viewLine3.setVisibility(View.INVISIBLE);
        } else {
            rBtnDeviceDetail.setVisibility(View.GONE);
            viewLine3.setVisibility(View.GONE);
        }
    }

    @Override
    public void initEngines() {
    }

    @Override
    public void getIntentData() {
        //&& !myOrder.getStatusId().equals(""+Order.STATUS_YIWANCHENG) && !myOrder.getStatusId().equals(""+Order.STATUS_YIPINGJIA)
        if (myOrder != null && !myOrder.getStatusId().equals(""+Order.STATUS_YIQUXIAO) && !myOrder.getStatusId().equals(""+Order.STATUS_DAIJIEDAN)) {
            setBohao(true);
        } else {
            imgRight.setVisibility(View.GONE);
        }
    }

    public void setBohao(boolean isShow) {
        if (imgRight == null){
            return;
        }
        if (isShow) {
            imgRight.setVisibility(View.VISIBLE);
          //  imgRight.setImageResource(R.drawable.bohao);
            ImageLoaderUtil.getInstance().displayFromLocal(OrderDetailActivity.this,imgRight,R.drawable.bohao, Utils.dip2px(OrderDetailActivity.this,18),Utils.dip2px(OrderDetailActivity.this,18));
            imgRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = myOrder.getMaintenancePhone();
                    DialogUtil.showMessageDg(OrderDetailActivity.this, "温馨提示", "确定拨打工程师电话：" + phone + "？", new CustomDialog.OnClickListener() {

                        @Override
                        public void onClick(CustomDialog dialog, int id, Object object) {
                            dialog.dismiss();
                            Intent intent = new Intent(
                                    Intent.ACTION_CALL);
                            String phone = myOrder.getMaintenancePhone();
                            Uri data = Uri.parse("tel:" + phone);
                            intent.setData(data);
                            startActivity(intent);
                        }
                    });
                }
            });
        } else {
            imgRight.setVisibility(View.GONE);
        }
    }
    /**
     * 往fragmentHashMap中添加 Fragment
     */
    private void addFragmenInList() {
        fragmentHashMap = new HashMap<>();

        OrderStateFragment orderStateFragment = new OrderStateFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("data", myOrder);
        orderStateFragment.setArguments(bundle1);
        OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("data", myOrder);
        orderDetailFragment.setArguments(bundle2);

        fragmentHashMap.put(0, orderStateFragment);
        fragmentHashMap.put(1, orderDetailFragment);
        if (myOrder.getStatusId().equals(""+Order.STATUS_YIWANCHENG) || myOrder.getStatusId().equals(""+Order.STATUS_YIPINGJIA) || myOrder.getStatusId().equals(""+Order.STATUS_SHOUHOU)) {
            DeviceDetailFragment deviceDetailFragment = new DeviceDetailFragment();
            Bundle bundle3 = new Bundle();
            bundle3.putSerializable("data", myOrder);
            deviceDetailFragment.setArguments(bundle3);
            fragmentHashMap.put(2, deviceDetailFragment);
        }
    }

    /**
     * 往rButtonHashMap中添加 RadioButton Id
     */
    private void addRadioButtonIdInList() {
        rButtonHashMap = new HashMap<>();
        rButtonHashMap.put(0, rBtnState);
        rButtonHashMap.put(1, rBtnOrderDetail);
        if (myOrder.getStatusId().equals(""+Order.STATUS_YIWANCHENG) || myOrder.getStatusId().equals(""+Order.STATUS_YIPINGJIA) || myOrder.getStatusId().equals(""+Order.STATUS_SHOUHOU)) {
            rButtonHashMap.put(2, rBtnDeviceDetail);
        }
    }

    @OnClick({R.id.rBtnState, R.id.rBtnOrderDetail, R.id.rBtnDeviceDetail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rBtnState:
                cViewPager.setCurrentItem(0);
                showLineView(1);
                break;
            case R.id.rBtnOrderDetail:
                showLineView(2);
                cViewPager.setCurrentItem(1);
                break;
            case R.id.rBtnDeviceDetail:
                showLineView(3);
                cViewPager.setCurrentItem(2);
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
                viewLine2.setVisibility(View.INVISIBLE);
                if (myOrder.getStatusId().equals(""+Order.STATUS_YIWANCHENG) || myOrder.getStatusId().equals(""+Order.STATUS_YIPINGJIA) || myOrder.getStatusId().equals(""+Order.STATUS_SHOUHOU)) {
                    viewLine3.setVisibility(View.INVISIBLE);
                }
                break;
            case 2:
                viewLine2.setVisibility(View.VISIBLE);
                viewLine1.setVisibility(View.INVISIBLE);
                if (myOrder.getStatusId().equals(""+Order.STATUS_YIWANCHENG) || myOrder.getStatusId().equals(""+Order.STATUS_YIPINGJIA) || myOrder.getStatusId().equals(""+Order.STATUS_SHOUHOU)) {
                    viewLine3.setVisibility(View.INVISIBLE);
                }
                break;
            case 3:
                viewLine3.setVisibility(View.VISIBLE);
                viewLine2.setVisibility(View.INVISIBLE);
                viewLine1.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void bindViewPage(HashMap<Integer, Fragment> fragmentList) {
        //是否滑动
        cViewPager.setPagingEnabled(true);
        cViewPager.setAdapter(new FragmentsPagerAdapter(getSupportFragmentManager(), fragmentList));
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

    /**
     * 获取设备类型
     */
    public String getDeviceModel(){
        if(fragmentHashMap != null){
            //return ((OrderDetailFragment)fragmentHashMap.get(1));
        }
        return "";
    }

    @Override
    public void finish() {
        if (MainActivity.activity == null) {
            IntentUtil.gotoActivity(this, MainActivity.class);
        }
        super.finish();
    }
}
