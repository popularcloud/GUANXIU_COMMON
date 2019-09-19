package com.lwc.common.module.integral.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.module.integral.adapter.IntegralExchangeAdapter;
import com.lwc.common.module.integral.bean.IntegralExchangeBean;
import com.lwc.common.module.integral.presenter.IntegralExchangePresenter;
import com.lwc.common.module.integral.view.IntegralExchangeView;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.ToastUtil;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author younge
 * @date 2019/4/3 0003
 * @email 2276559259@qq.com
 * 积分兑换记录
 */
public class IntegralExchangeRecordActivity extends BaseActivity implements IntegralExchangeView {

    @BindView(R.id.tv_msg)
    TextView tv_msg;
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private IntegralExchangeAdapter adapter;
    List<IntegralExchangeBean> list = new ArrayList<>();
    private int currentPage;
    private IntegralExchangePresenter presenter;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_integral_exchange_record;
    }

    @Override
    protected void findViews() {
        setTitle("兑换记录");
        presenter = new IntegralExchangePresenter(this);
        initRecycleView();
    }

    private void initRecycleView() {
        BGARefreshLayoutUtils.initRefreshLayout(this, mBGARefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IntegralExchangeAdapter(this, list, R.layout.item_exchange_integral_record);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("exchange_id",list.get(position).getExchangeId());
                IntentUtil.gotoActivity(IntegralExchangeRecordActivity.this,IntegralExchangeDetailActivity.class,bundle);
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
    protected void init() {
        mBGARefreshLayout.beginRefreshing();
    }

    @Override
    protected void initGetData() {

    }

    @Override
    protected void widgetListener() {

    }

    @Override
    public void onGetUserIntegral(List<IntegralExchangeBean> integralExchangeBeans) {
        if(integralExchangeBeans != null && integralExchangeBeans.size() > 0){
            tv_msg.setVisibility(View.GONE);
            if(currentPage == 1){
                list.clear();
            }
            list.addAll(integralExchangeBeans);
            adapter.notifyDataSetChanged();
        }else{
            if(currentPage == 1){
                tv_msg.setVisibility(View.VISIBLE);
            }else{
                ToastUtil.showToast(IntegralExchangeRecordActivity.this,"暂无更多数据");
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
