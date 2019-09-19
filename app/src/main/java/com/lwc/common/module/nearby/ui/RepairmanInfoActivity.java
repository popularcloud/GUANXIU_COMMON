package com.lwc.common.module.nearby.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Evaluate;
import com.lwc.common.module.bean.Repairman;
import com.lwc.common.module.common_adapter.EvaluateListAdapter;
import com.lwc.common.module.common_adapter.TagsAdapter;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.SpaceItemDecoration;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.Tag;
import com.lwc.common.view.TagListView;
import com.lwc.common.widget.CircleImageView;
import com.lwc.common.widget.GridSpacingItemDecoration;
import com.lwc.common.widget.PhotoBigFrameDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 维修员信息
 */
public class RepairmanInfoActivity extends BaseActivity {

    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.ratingBar)
    com.hedgehog.ratingbar.RatingBar ratingBar;
    @BindView(R.id.txtOrderCount)
    TextView txtOrderCount;
    @BindView(R.id.imgHead)
    CircleImageView imgHead;
    @BindView(R.id.rv_tags)
    RecyclerView rv_tags;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    @BindView(R.id.tv_msg)
    TextView tv_msg;

    private final ArrayList<Tag> mTags = new ArrayList<>();
    private Repairman repairman;
    private ImageLoaderUtil imageLoaderUtil;
    private String repair_id;
    private int page=1;
    private EvaluateListAdapter adapter;
    private ArrayList<Evaluate> evaluates = new ArrayList<>();

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_repairman_info;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        BGARefreshLayoutUtils.initRefreshLayout(this, mBGARefreshLayout);
    }

    @Override
    public void init() {
        setTitle("工程师信息");
    }

    @Override
    protected void initGetData() {
        repairman = (Repairman) getIntent().getExtras().getSerializable("repair_bean");
        repair_id = getIntent().getStringExtra("repair_id");
        setViewData();
        if (repairman == null && !TextUtils.isEmpty(repair_id)) {
            getMaintenanceInfo();
        } else if (repairman != null) {
            setViewData();
        }
        getEvaluates();
    }

    @Override
    protected void widgetListener() {
    }

    /**
     * 获取维修员信息
     */
    private void getMaintenanceInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("maintenanceId", repair_id);
        HttpRequestUtils.httpRequest(this, "查询工程师信息", RequestValue.POST_MAINTENANCE_INFO, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        repairman = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), Repairman.class);
                        if (repairman == null) {
                            ToastUtil.showLongToast(RepairmanInfoActivity.this, "工程师数据异常");
                            onBackPressed();
                            return;
                        }
                        setViewData();
                        break;
                    default:
                        ToastUtil.showLongToast(RepairmanInfoActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                ToastUtil.showLongToast(RepairmanInfoActivity.this, msg);
            }
        });
    }

    /**
     * 设置view数据
     */
    private void setViewData() {
        imageLoaderUtil = ImageLoaderUtil.getInstance();
        if (repairman != null) {

            String picture = repairman.getMaintenanceHeadImage();
            if (picture != null && !TextUtils.isEmpty(picture)) {
                imageLoaderUtil.displayFromNet(this, picture, imgHead);
            } else {
                imageLoaderUtil.displayFromLocal(this, imgHead, R.drawable.default_portrait_100);
            }

            txtOrderCount.setText(String.valueOf(repairman.getOrderCount()));   //完成订单总数

            String name = repairman.getMaintenanceName();
            if (name != null && !TextUtils.isEmpty(name)) {
                txtName.setText(name);
            } else {
                txtName.setText("暂无");
            }
//            txtSkills.setText(repairman.getSkills());
            if (!TextUtils.isEmpty(repairman.getMaintenanceStar())) {
                Float avgservice = Float.parseFloat(repairman.getMaintenanceStar());
                ratingBar.setStarCount(5);
                ratingBar.setStar(avgservice);
            } else {
                ratingBar.setStar(0);
            }
            if (!TextUtils.isEmpty(repairman.getMaintenanceLabelNames())) {
                rv_tags.setVisibility(View.VISIBLE);
                TagsAdapter tagsAdapter = new TagsAdapter(this,mTags,null);

                GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);

                rv_tags.setLayoutManager(gridLayoutManager);
               // rv_tags.addItemDecoration(new GridSpacingItemDecoration(3,24,true));
                rv_tags.addItemDecoration(new SpaceItemDecoration(Utils.dip2px(RepairmanInfoActivity.this,12),0,SpaceItemDecoration.GRIDLAYOUT));
                rv_tags.setAdapter(tagsAdapter);

                String[] labels = repairman.getMaintenanceLabelNames().split(",");
                if (labels != null && labels.length > 0) {
                    for (int i=0; i<labels.length; i++) {
                        String [] names = labels[i].split("_");
                        Tag tag = new Tag();
                        tag.setLabelName(names[0]+"  "+names[1]);
                        mTags.add(tag);
                    }
                    tagsAdapter.notifyDataSetChanged();
                }
            } else {
                rv_tags.setVisibility(View.GONE);
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EvaluateListAdapter(this, evaluates, R.layout.item_evaluate);
        recyclerView.setAdapter(adapter);
        //刷新控件监听器
        mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                page = 1;
                getEvaluates();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                page++;
                getEvaluates();
                return true;
            }
        });
    }

    /**
     * 获取评价信息列表
     */
    private void getEvaluates() {
        HashMap<String, String> map = new HashMap<>();
        map.put("curPage",page+"");
        map.put("maintenanceId", repair_id);
        HttpRequestUtils.httpRequest(this, "getEvaluates", RequestValue.GET_COMMENT_LIST, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String result) {
                Common common = JsonUtil.parserGsonToObject(result, Common.class);
                if (common.getStatus().equals("1")) {
                    ArrayList<Evaluate> current = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(result, "data"), new TypeToken<ArrayList<Evaluate>>() {
                    });
                    if (page == 1) {
                        if (current != null && current.size() > 0) {
                            evaluates = current;
                        } else {
                            evaluates = new ArrayList<>();
                        }
                    } else if (page > 1) {
                        if (current != null && current.size() > 0) {
                            evaluates.addAll(current);
                        } else {
                            ToastUtil.showToast(RepairmanInfoActivity.this, "暂无更多评价信息");
                            page--;
                        }
                    }
                    adapter.replaceAll(evaluates);
                    if (evaluates.size() > 0) {
                        tv_msg.setVisibility(View.GONE);
                    } else {
                        tv_msg.setVisibility(View.VISIBLE);
                    }
                } else {
                    ToastUtil.showToast(RepairmanInfoActivity.this, common.getInfo());
                }
                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
                BGARefreshLayoutUtils.endLoadingMore(mBGARefreshLayout);
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showToast(RepairmanInfoActivity.this, msg);
                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
                BGARefreshLayoutUtils.endLoadingMore(mBGARefreshLayout);
            }
        });
    }

    /**
     * 点击头像查看大图
     * @param view
     */
    @OnClick({R.id.imgHead})
    public void onViewClicked(View view) {
        if (!TextUtils.isEmpty(repairman.getMaintenanceHeadImage())) {
            PhotoBigFrameDialog frameDialog = new PhotoBigFrameDialog(RepairmanInfoActivity.this, repairman.getMaintenanceHeadImage());
            frameDialog.showNoticeDialog();
        }
    }
}
