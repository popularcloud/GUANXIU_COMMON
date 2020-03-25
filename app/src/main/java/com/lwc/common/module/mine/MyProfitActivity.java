package com.lwc.common.module.mine;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.adapter.MyProfitAdapter;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.MyProfitBean;
import com.lwc.common.module.integral.activity.IntegralExchangeRecordActivity;
import com.lwc.common.module.wallet.ui.WalletActivity;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.yanzhenjie.sofia.Sofia;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author younge
 * @date 2019/3/27 0027
 * @email 2276559259@qq.com
 * 积分订单
 */
public class MyProfitActivity extends BaseActivity {

    @BindView(R.id.tv_msg)
    TextView tv_msg;
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.FMoneyTV)
    TextView FMoneyTV;

    @BindView(R.id.tv_person_num)
    TextView tv_person_num;
    @BindView(R.id.tv_profit_count)
    TextView tv_profit_count;
    @BindView(R.id.tv_person_sum)
    TextView tv_person_sum;
    private MyProfitAdapter adapter;
    ArrayList<MyProfitBean.PageBean> list = new ArrayList<>();
    private int currentPage = 1;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_my_profit;
    }

    @Override
    protected void findViews() {
        setTitle("我的收益");
        setRight("我的钱包", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.gotoActivity(MyProfitActivity.this,WalletActivity.class);
            }
        });
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

    @SuppressLint("ResourceType")
    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        ImmersionBar.with(this)
                .statusBarColor(R.color.red_fb)
                .statusBarDarkFont(true)
                .navigationBarColor(R.color.white).init();
    }

    private void initRecycleView() {
        BGARefreshLayoutUtils.initRefreshLayout(this, mBGARefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyProfitAdapter(this, list, R.layout.item_profit_record);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
/*                MyProfitBean.PageBean dataBean = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("IntegralOrderDetail",dataBean);
                IntentUtil.gotoActivity(MyProfitActivity.this,IntegralOrderDetailActivity.class,bundle);*/
            }
        });
        recyclerView.setAdapter(adapter);

        mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                currentPage = 1;
                getMyProfitData();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                currentPage ++;
                getMyProfitData();
                return true;
            }
        });
    }

    private void getMyProfitData(){
        Map<String,String> param = new HashMap<>();
        param.put("curPage",String.valueOf(currentPage));
        HttpRequestUtils.httpRequest(MyProfitActivity.this, "用户收益记录", RequestValue.GET_PAYMENT_INVITERECORD,param, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);

                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
                BGARefreshLayoutUtils.endLoadingMore(mBGARefreshLayout);
                switch (common.getStatus()) {
                    case "1":
                        try {
                            MyProfitBean myProfitBean = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"),MyProfitBean.class);

                            if(myProfitBean != null){
                                 tv_person_num.setText(String.valueOf(myProfitBean.getCountPopelDay()));
                                 tv_profit_count.setText(Utils.getMoney(String.valueOf(myProfitBean.getSumMoneyDay())));
                                 tv_person_sum.setText(String.valueOf(myProfitBean.getCountPopelAll()));
                                FMoneyTV.setText(Utils.getMoney(String.valueOf(myProfitBean.getSumMoneyAll())));
                                if(myProfitBean.getData() != null){
                                    tv_msg.setVisibility(View.GONE);
                                    mBGARefreshLayout.setVisibility(View.VISIBLE);
                                    if(currentPage == 1){
                                        list.clear();
                                    }
                                    list.addAll(myProfitBean.getData());
                                    adapter.notifyDataSetChanged();
                                }else{
                                    if(currentPage == 1){
                                       tv_msg.setVisibility(View.VISIBLE);
                                        mBGARefreshLayout.setVisibility(View.GONE);
                                    }
                                }
                            }else{
                                ToastUtil.showToast(MyProfitActivity.this,"暂无数据！");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.showToast(MyProfitActivity.this,"获取收益信息失败！");
                        }
                        break;
                    default:
                        ToastUtil.showToast(MyProfitActivity.this,"获取收益信息失败！");
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showToast(MyProfitActivity.this,"获取收益信息失败！");
                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
                BGARefreshLayoutUtils.endLoadingMore(mBGARefreshLayout);
            }
        });
    }
}
