package com.lwc.common.module.lease_parts.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.module.lease_parts.bean.OrderDetailBean;
import com.lwc.common.module.lease_parts.fragment.LeaseShoppingCartFragment;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.MsgReadUtil;
import com.lwc.common.utils.PopupWindowUtil;
import com.lwc.common.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择服务类型
 */
public class LeaseSelectReturnTypeActivity extends BaseActivity {


    @BindView(R.id.iv_header)
    ImageView iv_header;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_spece)
    TextView tv_spece;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_sum)
    TextView tv_sum;
    @BindView(R.id.imgRight)
    ImageView imgRight;
    @BindView(R.id.tv_msg)
    TextView tv_msg;

    @BindView(R.id.fragment_container)
    FrameLayout fragment_container;
    private LeaseShoppingCartFragment leaseShoppingCartFragment;
    private FragmentManager fragmentManager;

    private OrderDetailBean orderDetailBean;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_lease_select_type;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取未读租赁消息
        MsgReadUtil.hasMessage(this,tv_msg);
    }

    @Override
    protected void init() {

        setTitle("选择服务类型");
        setRight(R.drawable.ic_more_black, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindowUtil.showHeaderPopupWindow(LeaseSelectReturnTypeActivity.this,imgRight,leaseShoppingCartFragment,fragment_container,fragmentManager);
            }
        });

        ImmersionBar.with(LeaseSelectReturnTypeActivity.this)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true).init();

        orderDetailBean = (OrderDetailBean) getIntent().getSerializableExtra("orderDetailBean");

        loadToUI();

        leaseShoppingCartFragment = new LeaseShoppingCartFragment();
        fragmentManager = getSupportFragmentManager();
    }

    private void loadToUI(){
        if(orderDetailBean != null){
            ImageLoaderUtil.getInstance().displayFromNetDCircular(this,orderDetailBean.getGoodsImg(),iv_header,R.drawable.image_default_picture);

            String goodsName = orderDetailBean.getGoodsName();
            String goodsNameStr = "租赁  " + goodsName;
            SpannableStringBuilder showGoodsName = Utils.getSpannableStringBuilder(0, 2, LeaseSelectReturnTypeActivity.this.getResources().getColor(R.color.transparent), goodsNameStr, 10, true);

            tv_title.setText(showGoodsName);
           // tv_price.setText("￥"+ Utils.getMoney(Utils.chu(orderDetailBean.getGoodsPrice(),"100")));

            String needShowMoney = "￥"+Utils.getMoney(Utils.chu(orderDetailBean.getGoodsPrice(),"100"));
            SpannableStringBuilder showGoodsPrice = Utils.getSpannableStringBuilder(1, needShowMoney.length() - 2, getResources().getColor(R.color.all_black), needShowMoney, 15, true);
            tv_price.setText(showGoodsPrice);

            tv_spece.setText(orderDetailBean.getLeaseSpecs()+","+orderDetailBean.getLeaseMonTime()+"个月,"+("1".equals(orderDetailBean.getPayType())?"月付":"季付"));
            tv_sum.setText("x "+orderDetailBean.getGoodsRealNum());
        }
    }

    @Override
    protected void initGetData() {

    }

    @Override
    protected void widgetListener() {

    }


    @OnClick({R.id.rl_01,R.id.rl_02})
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.rl_01:
                Bundle bundle01 = new Bundle();
                bundle01.putSerializable("orderDetailBean",orderDetailBean);
                bundle01.putInt("returnType",1);
                IntentUtil.gotoActivity(this,LeaseApplyForRefundActivity.class,bundle01);
                break;
            case R.id.rl_02:
                Bundle bundle02 = new Bundle();
                bundle02.putSerializable("orderDetailBean",orderDetailBean);
                bundle02.putInt("returnType",2);
                IntentUtil.gotoActivity(this,LeaseApplyForRefundActivity.class,bundle02);
                break;
        }
    }
}
