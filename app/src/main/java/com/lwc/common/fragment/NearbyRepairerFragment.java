package com.lwc.common.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.bean.Repairman;
import com.lwc.common.module.common_adapter.NearbyRepairAdapter;
import com.lwc.common.module.nearby.presenter.NearbyRepairerPresenter;
import com.lwc.common.module.nearby.ui.INearbyRepairerView;
import com.lwc.common.module.nearby.ui.RepairmanInfoActivity;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.view.MyTextView;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 附近工程师
 * @author 何栋
 * @date 2017年11月20日
 * @Copyright: lwc
 */
public class NearbyRepairerFragment extends BaseFragment implements INearbyRepairerView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    @BindView(R.id.txtActionbarTitle)
    MyTextView txtActionbarTitle;
    @BindView(R.id.tctTip)
    TextView tctTip;
    @BindView(R.id.repairerCount)
    TextView repairerCount;
    @BindView(R.id.img_back)
    ImageView imgBack;
    private NearbyRepairAdapter adapter;
    private List<Repairman> repairmans = new ArrayList<>();
    private NearbyRepairerPresenter presenter;
    //    private CustomProgressDialog dialogPro;
    private int curPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near_repairer, null);
        ButterKnife.bind(this, view);
        BGARefreshLayoutUtils.initRefreshLayout(getContext(), mBGARefreshLayout);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEngines(view);
        init();
        bindRecycleView();
        setListener();
    }

    private void bindRecycleView() {
        if (getContext() != null) {
            if (adapter == null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                recyclerView.setLayoutManager(new VegaLayoutManager(getContext()));
                adapter = new NearbyRepairAdapter(getContext(), repairmans, R.layout.item_nearby_repairman);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int viewType, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("repair_bean", adapter.getItem(position));
                        IntentUtil.gotoActivity(getActivity(), RepairmanInfoActivity.class, bundle);
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void init() {
        imgBack.setVisibility(View.GONE);
        txtActionbarTitle.setText("附近工程师");
    }

    @Override
    public void initEngines(View view) {
        loadNearbyRepairerPresenter();
    }

    /**
     * 加载NearbyRepairerPresenter
     */
    private void loadNearbyRepairerPresenter() {
        if (getContext() != null && getActivity() != null) {
            if (presenter == null) {
                presenter = new NearbyRepairerPresenter(getContext(), getActivity(), this, mBGARefreshLayout);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateView(){
        if (this.repairmans == null || this.repairmans.size() == 0) {
            getNearbyRepairers();
        }
    }

    /**
     * 获取附近的维修员
     */
    public void getNearbyRepairers() {
        if (presenter != null) {
            presenter.getNearbyRepairers(curPage);
        }
    }

    public void closeDialog() {
    }

    @Override
    public void getIntentData() {
    }

    @Override
    public void setListener() {
        mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                bindRecycleView();
                loadNearbyRepairerPresenter();
                curPage = 1;
                getNearbyRepairers();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                curPage++;
                getNearbyRepairers();
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void notifyData(List<Repairman> list, int count) {
        if (list != null && list.size() > 0 && tctTip != null) {
            if (curPage == 1) {
                this.repairmans = list;
            } else if (curPage > 1) {
                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < this.repairmans.size(); j++) {
                        if (list.get(i).getMaintenanceId().equals(this.repairmans.get(j).getMaintenanceId())) {
                            this.repairmans.remove(j);
                            break;
                        }
                    }
                }
                this.repairmans.addAll(list);
            }
            adapter.replaceAll(repairmans);
            tctTip.setVisibility(View.GONE);
            repairerCount.setText("附近有" + count + "个工程师");
        } else if (tctTip != null) {
            if (curPage == 1) {
                repairerCount.setText("附近有0个工程师");
                tctTip.setVisibility(View.VISIBLE);
                repairmans = new ArrayList<>();
                adapter.replaceAll(repairmans);
            } else {
                ToastUtil.showLongToast(getActivity(), "附近暂无更多工程师");
                tctTip.setVisibility(View.GONE);
                curPage--;
            }
        }
    }
}