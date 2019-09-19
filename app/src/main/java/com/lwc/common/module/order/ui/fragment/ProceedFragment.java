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
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.bean.Order;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.common_adapter.OrderListAdapter;
import com.lwc.common.module.login.ui.LoginActivity;
import com.lwc.common.module.order.presenter.OrderListPresenter;
import com.lwc.common.module.order.ui.IOrderListFragmentView;
import com.lwc.common.module.order.ui.activity.OrderDetailActivity;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 进行中
 */
public class ProceedFragment extends BaseFragment implements IOrderListFragmentView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    private OrderListAdapter adapter;
    private List<Order> myOrders;
    @BindView(R.id.tctTip)
    TextView tctTip;
    private OrderListPresenter presenter;
    //加载的page页
    private int page = 1;
    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finish, null);
        ButterKnife.bind(this, view);
        BGARefreshLayoutUtils.initRefreshLayout(getContext(), mBGARefreshLayout);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEngines(view);
        init();
        setListener();
        bindRecycleView();
        tctTip.setText("暂无订单信息");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBGARefreshLayout != null && user != null)
            mBGARefreshLayout.beginRefreshing();  //请求网络数据
    }

    private void bindRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setLayoutManager(new VegaLayoutManager(getContext()));
        adapter = new OrderListAdapter(getContext(), myOrders, R.layout.item_order);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("data", myOrders.get(position));
                IntentUtil.gotoActivity(getContext(), OrderDetailActivity.class, bundle);
            }
        });
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void lazyLoad() {
    }

    @Override
    public void init() {
        user = SharedPreferencesUtils.getInstance(getActivity()).loadObjectData(User.class);
    }

    @Override
    public void initEngines(View view) {
        presenter = new OrderListPresenter(this, getActivity());
    }

    @Override
    public void getIntentData() {
    }

    @Override
    public void setListener() {
        if (user == null) {
            mBGARefreshLayout.setVisibility(View.GONE);
            String str = "你还未登录哦！";
            tctTip.setText(Utils.getSpannableStringBuilder(3, 5, getResources().getColor(R.color.btn_blue_nomal), str, 15));
            tctTip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferencesUtils.getInstance(getActivity()).deleteAppointObjectData(User.class);
                    SharedPreferencesUtils.getInstance(getActivity()).saveString("token","");
                    IntentUtil.gotoActivity(getActivity(), LoginActivity.class);
                }
            });
        }
        if (user != null) {
            //刷新控件监听器
            mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
                @Override
                public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                    page = 1;
                    presenter.getOrders(1, 10, 1);
                }

                @Override
                public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                    page++;
                    presenter.loadOrders(page, 10, "1");
                    return true;
                }
            });
        }
    }

    @Override
    public void notifyData(List<Order> myOrders) {
        this.myOrders = myOrders;
        adapter.replaceAll(myOrders);
        if (this.myOrders != null && this.myOrders.size() > 0) {
            tctTip.setVisibility(View.GONE);
        } else {
            tctTip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void addData(List<Order> myOrders) {
        if (myOrders == null || myOrders.size() == 0){
            page--;
            ToastUtil.showLongToast(getActivity(), "我是有底线的，暂无更多订单");
            return;
        }
        this.myOrders.addAll(myOrders);
        adapter.addAll(myOrders);
    }

    @Override
    public BGARefreshLayout getBGARefreshLayout() {
        return mBGARefreshLayout;
    }
}
