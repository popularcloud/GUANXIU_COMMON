package com.lwc.common.module.order.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.adapter.AfterServiceAdapter;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.bean.AfterService;
import com.lwc.common.module.bean.Order;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.order.presenter.DeviceDetailPresenter;
import com.lwc.common.module.order.ui.IDeviceDetailFragmentView;
import com.lwc.common.module.order.ui.activity.OrderDetailActivity;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author 何栋
 * @version 1.0
 * @date 2017/6/13 17:39
 * @email 294663966@qq.com
 * 设备详情
 */
public class DeviceDetailFragment extends BaseFragment implements IDeviceDetailFragmentView {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    //订单状态
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    @BindView(R.id.btn)
    TextView btn;
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    private Order myOrder = null;
    private DeviceDetailPresenter presenter;
    private User user = null;
    private List<AfterService> asList = new ArrayList<AfterService>();
    private AfterServiceAdapter adapter;
    private AfterService afterService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_detail, null);
        ButterKnife.bind(this, view);
        BGARefreshLayoutUtils.initRefreshLayout(getContext(), mBGARefreshLayout, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEngines(view);
        getIntentData();
        bindRecycleView();
        setListener();
        getData();
    }

    private void bindRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AfterServiceAdapter(getContext(), asList, R.layout.item_order_status);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 请求网络获取数据
     */
    private void getData() {
        /**
         *  Typeid 非0  是在地图页面进入
         */
        presenter.getOrderInfor(myOrder.getOrderId() + "", mBGARefreshLayout);
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void init() {
    }

    @Override
    public void initEngines(View view) {
        presenter = new DeviceDetailPresenter(this, getActivity());
        SharedPreferencesUtils preferencesUtils = SharedPreferencesUtils.getInstance(getContext());
        user = preferencesUtils.loadObjectData(User.class);
    }

    @Override
    public void getIntentData() {
        myOrder = (Order) getArguments().getSerializable("data");
    }

    @Override
    public void setListener() {
        //刷新控件监听器
        mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                getData();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                return false;
            }
        });
    }

    @Override
    public void setDeviceDetailInfor(List<AfterService> asList) {
        this.asList = asList;
        if (this.asList == null || this.asList.size()==0){
            tv_msg.setVisibility(View.VISIBLE);
            OrderDetailActivity.activity.setBohao(false);
            return;
        }
        tv_msg.setVisibility(View.GONE);
        afterService = this.asList.get(this.asList.size()-1);
        adapter.replaceAll(asList);
        //1:正常 2：申请返修 3：接受返修 4：开始处理 5：完成待确认 ',
        int type = afterService.getWarrantyState();
        if (type == AfterService.STATUS_CHULI || type == AfterService.STATUS_GUOQI) {
            btn.setVisibility(View.GONE);
            if (type == AfterService.STATUS_GUOQI && OrderDetailActivity.activity != null){
                OrderDetailActivity.activity.setBohao(false);
            } else if (OrderDetailActivity.activity != null){
                OrderDetailActivity.activity.setBohao(true);
            }
        } else if (type == AfterService.STATUS_ZHENGCHANG || type == 6) {
            if (this.asList.get(0).getWarrantyState() == AfterService.STATUS_GUOQI){
                btn.setVisibility(View.GONE);
                if (OrderDetailActivity.activity != null) {
                    OrderDetailActivity.activity.setBohao(false);
                }
            } else {
                if (OrderDetailActivity.activity != null) {
                    OrderDetailActivity.activity.setBohao(true);
                }
                btn.setVisibility(View.VISIBLE);
                btn.setText("申请售后");
            }
        } else if (type == AfterService.STATUS_WANGCHENGDAIQUEREN ) {
            btn.setVisibility(View.VISIBLE);
            btn.setText("确认完成");
            if (OrderDetailActivity.activity != null) {
                OrderDetailActivity.activity.setBohao(true);
            }
        } else if (type == AfterService.STATUS_YIJIESHOU || type == AfterService.STATUS_YISHENQING) {
            btn.setVisibility(View.VISIBLE);
            btn.setText("取消");
            if (OrderDetailActivity.activity != null) {
                OrderDetailActivity.activity.setBohao(true);
            }
        }
    }

    @OnClick(R.id.btn)
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn:
                String text = btn.getText().toString().trim();
                if (text.equals("申请售后")) {
                    presenter.updateOrderInfor(myOrder.getOrderId() , AfterService.STATUS_YISHENQING, mBGARefreshLayout);
                } else if (text.equals("确认完成")) {
                    presenter.updateOrderInfor(myOrder.getOrderId(), AfterService.STATUS_WANGCHENGDAIQUEREN, mBGARefreshLayout);
                } else if (text.equals("取消")) {
                    presenter.updateOrderInfor(myOrder.getOrderId(), AfterService.STATUS_YIJIESHOU, mBGARefreshLayout);
                }
                break;
        }
    }
}
