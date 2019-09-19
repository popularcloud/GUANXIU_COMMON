package com.lwc.common.module.integral.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.module.integral.adapter.IntegralOrderAdapter;
import com.lwc.common.module.integral.bean.UserIntegralBean;
import com.lwc.common.module.integral.presenter.IntegralOrderPresenter;
import com.lwc.common.module.integral.view.IntegralOrderView;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.yanzhenjie.sofia.Sofia;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author younge
 * @date 2019/3/27 0027
 * @email 2276559259@qq.com
 * 积分订单
 */
public class IntegralOrderActivity extends BaseActivity implements IntegralOrderView {

    @BindView(R.id.tv_msg)
    TextView tv_msg;
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.FMoneyTV)
    TextView FMoneyTV;
    private IntegralOrderAdapter adapter;
    ArrayList<UserIntegralBean.DataBean> list = new ArrayList<>();
    private IntegralOrderPresenter presenter;
    private int currentPage = 1;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_integral_order;
    }

    @Override
    protected void findViews() {
        setTitle("我的积分");
        setRight("兑换记录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.gotoActivity(IntegralOrderActivity.this,IntegralExchangeRecordActivity.class);
            }
        });
        presenter = new IntegralOrderPresenter(this,this);
        initRecycleView();

    }

    @Override
    protected void init() {

    }

    @Override
    protected void initGetData() {
        BGARefreshLayoutUtils.beginRefreshing(mBGARefreshLayout);
    }

    @Override
    protected void widgetListener() {

    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        Sofia.with(this)
                .statusBarBackground(Color.parseColor("#fb5251"))
                .statusBarLightFont();
    }

    private void initRecycleView() {
        BGARefreshLayoutUtils.initRefreshLayout(this, mBGARefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IntegralOrderAdapter(this, list, R.layout.item_trading_record);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                UserIntegralBean.DataBean dataBean = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("IntegralOrderDetail",dataBean);
                IntentUtil.gotoActivity(IntegralOrderActivity.this,IntegralOrderDetailActivity.class,bundle);
            }
        });
        recyclerView.setAdapter(adapter);

        mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                currentPage = 1;
                presenter.getUserIntegralMsg(String.valueOf(currentPage));
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                currentPage ++;
                presenter.getUserIntegralMsg(String.valueOf(currentPage));
                return true;
            }
        });
    }


    @Override
    public void onGetUserIntegral(UserIntegralBean userIntegralBean) {
       if(currentPage == 1){
           if(userIntegralBean != null){
               if(!TextUtils.isEmpty(userIntegralBean.getIntegral())){
                   FMoneyTV.setText(Utils.chu(userIntegralBean.getIntegral(),"100"));
                   SharedPreferencesUtils.setParam(IntegralOrderActivity.this,"MyIntegral",userIntegralBean.getIntegral());
               }else{
                   FMoneyTV.setText("0");
                   SharedPreferencesUtils.setParam(IntegralOrderActivity.this,"MyIntegral","0");
               }

               if(userIntegralBean.getData() != null && userIntegralBean.getData().size() > 0){
                   list.clear();
                   list.addAll(userIntegralBean.getData());
                   adapter.replaceAll(list);
                   tv_msg.setVisibility(View.GONE);
               }else{
                   tv_msg.setVisibility(View.VISIBLE);
               }

           }else{
               tv_msg.setVisibility(View.VISIBLE);
           }
       }else{
           if(userIntegralBean != null){
               if(!TextUtils.isEmpty(userIntegralBean.getIntegral())){
                   FMoneyTV.setText(Utils.chu(userIntegralBean.getIntegral(),"100"));
                   SharedPreferencesUtils.setParam(IntegralOrderActivity.this,"MyIntegral",userIntegralBean.getIntegral());
               }else{
                   FMoneyTV.setText("0");
                   SharedPreferencesUtils.setParam(IntegralOrderActivity.this,"MyIntegral","0");
               }

               if(userIntegralBean.getData() != null && userIntegralBean.getData().size() > 0){
                   list.addAll(userIntegralBean.getData());
                   adapter.replaceAll(list);
                   tv_msg.setVisibility(View.GONE);
               }else{
                   ToastUtil.showToast(IntegralOrderActivity.this,"没有更多数据了");
               }

           }else{
               ToastUtil.showToast(IntegralOrderActivity.this,"没有更多数据了");
           }
       }

        BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
        BGARefreshLayoutUtils.endLoadingMore(mBGARefreshLayout);
    }

    @Override
    public void onLoadError(String msg) {
        ToastUtil.showToast(this,msg);
        BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
        BGARefreshLayoutUtils.endLoadingMore(mBGARefreshLayout);
    }
}
