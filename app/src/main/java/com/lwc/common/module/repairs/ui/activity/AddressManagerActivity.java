package com.lwc.common.module.repairs.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.module.bean.Address;
import com.lwc.common.module.common_adapter.AddressManagerAdapter;
import com.lwc.common.module.repairs.presenter.AddressManagerPresenter;
import com.lwc.common.module.repairs.ui.IAddressManagerView;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.view.MyTextView;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 地址管理
 */
public class AddressManagerActivity extends BaseActivity implements IAddressManagerView {

    @BindView(R.id.txtActionbarTitle)
    MyTextView txtActionbarTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    @BindView(R.id.btnAddAddress)
    TextView btnAddAddress;
    @BindView(R.id.iv_no_data)
    ImageView iv_no_data;
    private AddressManagerAdapter adapter;
    private List<Address> addresses = new ArrayList<>();
    private AddressManagerPresenter presenter;
    private SharedPreferencesUtils preferencesUtils;


    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_address_manager;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        BGARefreshLayoutUtils.initRefreshLayout(AddressManagerActivity.this, mBGARefreshLayout, false);
        bindRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBGARefreshLayout.beginRefreshing();
    }

    /**
     * 初始化列表加载对象
     */
    private void bindRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressManagerAdapter(AddressManagerActivity.this, addresses, R.layout.item_address_manager);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("address", addresses.get(position));
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void init() {
        setTitle("地址管理");
    }

    @Override
    protected void initGetData() {
        presenter = new AddressManagerPresenter(this, this, this, mBGARefreshLayout);
        preferencesUtils = SharedPreferencesUtils.getInstance(this);
    }

    @Override
    protected void widgetListener() {
        mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                presenter.getUserAddressList();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                return false;
            }
        });
    }

    @OnClick(R.id.btnAddAddress)
    public void onViewClicked() {
        IntentUtil.gotoActivity(AddressManagerActivity.this, AddAddressActivity.class);
    }

    @Override
    public void notifyData(List<Address> addresses) {

        if(addresses != null && addresses.size() > 0){
            iv_no_data.setVisibility(View.GONE);
            mBGARefreshLayout.setVisibility(View.VISIBLE);
            this.addresses = addresses;
            adapter.replaceAll(addresses);
        }else{
            iv_no_data.setVisibility(View.VISIBLE);
            mBGARefreshLayout.setVisibility(View.GONE);
        }

    }
}
