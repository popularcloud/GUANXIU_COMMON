package com.lwc.common.module.order.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.module.common_adapter.MyPackageListAdapter;
import com.lwc.common.module.order.ui.activity.PackageDetailActivity;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 待使用套餐
 */
public class UsePackageFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    @BindView(R.id.tctTip)
    TextView tctTip;
    private MyPackageListAdapter adapter;
    private List<PackageBean> packageBeanList = new ArrayList<>();

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
        tctTip.setCompoundDrawables(null, Utils.getDrawable(getActivity(), R.drawable.no_pachage), null, null);
        init();
        setListener();
        bindRecycleView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBGARefreshLayout != null)
            mBGARefreshLayout.beginRefreshing();  //请求网络数据
    }

    private void bindRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyPackageListAdapter(getContext(), packageBeanList, R.layout.item_my_package,2);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                PackageBean pack = adapter.getItem(position);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("package", pack);
                bundle2.putInt("type", 1);
                IntentUtil.gotoActivity(getActivity(), PackageDetailActivity.class, bundle2);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void getPackageList() {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "1");
        HttpRequestUtils.httpRequest(getActivity(), "getPackageList", RequestValue.GET_USER_PACKAGE_LIST, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String result) {
                Common common = JsonUtil.parserGsonToObject(result, Common.class);
                if (common.getStatus().equals("1")) {
                    ArrayList<PackageBean> current = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(result, "data"), new TypeToken<ArrayList<PackageBean>>() {
                    });
                    if (current != null && current.size() > 0) {
                        packageBeanList = current;
                    } else {
                        packageBeanList = new ArrayList<>();
                    }
                    adapter.replaceAll(packageBeanList);
                    if (packageBeanList.size() > 0) {
                        tctTip.setVisibility(View.GONE);
                    } else {
                        tctTip.setVisibility(View.VISIBLE);
                    }
                } else {
                    ToastUtil.showToast(getActivity(), common.getInfo());
                }
                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showToast(getActivity(), msg);
                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
            }
        });
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void init() {
    }

    @Override
    public void initEngines(View view) {
    }

    @Override
    public void getIntentData() {
    }

    @Override
    public void setListener() {
        //刷新控件监听器
        mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                getPackageList();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                return false;
            }
        });
    }

}
