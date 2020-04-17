package com.lwc.common.module.lease_parts.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.lwc.common.R;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.lease_parts.activity.LeaseGoodsListActivity;
import com.lwc.common.module.lease_parts.adapter.LeftTypeAdapter;
import com.lwc.common.module.lease_parts.adapter.RightGoodsAdapter;
import com.lwc.common.module.lease_parts.bean.LeaseLeftBean;
import com.lwc.common.module.lease_parts.bean.LeaseRightBean;
import com.lwc.common.utils.IntentUtil;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maxcion_home on 2017/9/8.
 */

public class LeaseCategoryFragment extends BaseFragment {

    @BindView(R.id.rv_left_menu)
    RecyclerView rv_left_menu;
    @BindView(R.id.rv_right_goods)
    RecyclerView rv_right_goods;

    private List<LeaseLeftBean> leaseLeftBeans;
    private List<LeaseRightBean> leaseRightBeans;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lease_category, container, false);
       ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void init() {

        initLeftRecyclerView();
        initRightRecyclerView();
    }


    @Override
    public void initEngines(View view) {

    }

    @Override
    public void getIntentData() {

    }

    @Override
    public void setListener() {

    }

    private void initLeftRecyclerView() {
        leaseLeftBeans = new ArrayList<>();
        leaseLeftBeans.add(new LeaseLeftBean(1,"推荐分类",true));
        leaseLeftBeans.add(new LeaseLeftBean(2,"电脑",false));
        leaseLeftBeans.add(new LeaseLeftBean(3,"显示器",false));
        leaseLeftBeans.add(new LeaseLeftBean(4,"打印机",false));

        final LeftTypeAdapter leftTypeAdapter = new LeftTypeAdapter(getContext(),leaseLeftBeans,R.layout.item_lease_left_menu);
        rv_left_menu.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_left_menu.setAdapter(leftTypeAdapter);

        leftTypeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                leftTypeAdapter.setSelectPos(position);
                leftTypeAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initRightRecyclerView() {

        leaseRightBeans = new ArrayList<>();
        leaseRightBeans.add(new LeaseRightBean(1,"笔记本","1"));
        leaseRightBeans.add(new LeaseRightBean(2,"台式","1"));
        leaseRightBeans.add(new LeaseRightBean(3,"主机","1"));
        leaseRightBeans.add(new LeaseRightBean(4,"平板电脑","1"));

        final RightGoodsAdapter rightGoodsAdapter = new RightGoodsAdapter(getContext(),leaseRightBeans,R.layout.item_lease_right_goods);
        rv_right_goods.setLayoutManager(new GridLayoutManager(getContext(),3));
        rv_right_goods.setAdapter(rightGoodsAdapter);

        rightGoodsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                IntentUtil.gotoActivity(getActivity(), LeaseGoodsListActivity.class);
            }
        });
    }
}
