package com.lwc.common.module.lease_parts.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lwc.common.R;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.lease_parts.adapter.LeaseGoodsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LeaseGoodListFragment extends BaseFragment{

    @BindView(R.id.rv_goods)
    RecyclerView rv_goods;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lease_good_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        rv_goods.setLayoutManager(new GridLayoutManager(getContext(),2));
        LeaseGoodsAdapter leaseGoodsAdapter = new LeaseGoodsAdapter(getContext());
        rv_goods.setAdapter(leaseGoodsAdapter);
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
}
