package com.lwc.common.module.lease_parts.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.bean.PartsBean;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.view.MyTextView;
import com.wyh.slideAdapter.ItemBind;
import com.wyh.slideAdapter.ItemView;
import com.wyh.slideAdapter.SlideAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


public class LeaseShoppingCartFragment extends BaseFragment{


    @BindView(R.id.txtActionbarTitle)
    MyTextView txtActionbarTitle;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.tvQd)
    TextView tvQd;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    @BindView(R.id.tv_SumMoney)
    TextView tv_SumMoney;
    @BindView(R.id.tv_submit)
    TextView tv_submit;
    @BindView(R.id.cb_box)
    CheckBox cb_box;

    /**
     * 购物清单数据
     */
    private List<PartsBean> addListBeans = new ArrayList<>();
    private SlideAdapter slideAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lease_shopping_cart, container, false);
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
        txtActionbarTitle.setText("购物车");
        tvQd.setText("管理");
        tvQd.setVisibility(View.VISIBLE);
        img_back.setVisibility(View.GONE);

        addListBeans.add(new PartsBean());
        addListBeans.add(new PartsBean());
        addListBeans.add(new PartsBean());
        addListBeans.add(new PartsBean());

        initRecyclerView();
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

    private void initRecyclerView() {
        BGARefreshLayoutUtils.initRefreshLayout(getContext(), mBGARefreshLayout, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                refreshLayout.endRefreshing();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                return true;
            }
        });

        ItemBind itemBind = new ItemBind<PartsBean>() {
            @Override
            public void onBind(ItemView itemView, final PartsBean data, final int position) {

                itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setOnClickListener(R.id.ll_reduce, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setOnClickListener(R.id.ll_add, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setOnClickListener(R.id.hide_delete, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.showToast(getContext(),"删除");
                            }
                        })
                        .setOnClickListener(R.id.add_favorites, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.showToast(getContext(),"加入收藏夹");
                            }
                        });
            }
        };

        slideAdapter = SlideAdapter.load(addListBeans)           //加载数据
                .item(R.layout.item_shopping_lease_good,0,0,R.layout.hide_drag_item,0.40f)//指定布局
                .bind(itemBind)
                .padding(0)
                .into(recyclerView);  //填充到recyclerView中


    }
}
