package com.lwc.common.module.partsLib.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.fragment.FilterFragment;
import com.lwc.common.module.bean.PartsBean;
import com.lwc.common.module.common_adapter.PartsListAdapter;
import com.lwc.common.module.partsLib.presenter.PartsListPresenter;
import com.lwc.common.module.partsLib.ui.view.PartsListView;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.ToastUtil;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author younge
 * @date 2018/12/19 0019
 * @email 2276559259@qq.com
 */
public class PartsListActivity extends BaseActivity implements PartsListView{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
/*    @BindView(R.id.tctTip)
    TextView tctTip;*/
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_filter)
    TextView tv_filter;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.iv_no_data)
    ImageView iv_no_data;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mDrawerContent;

    private PartsListAdapter partsListAdapter;
    private List<PartsBean> partsBeenList = new ArrayList<>();
    private PartsListPresenter partsListPresenter;
    private String searchText = "";
    private String typeId = "";
    private int currentPage = 1;
    private String attributeSeach = "";
    private int priceOrderStatus = 0; //0正常 1顺序  2倒叙
    private FragmentManager fragmentManager;
    private FilterFragment filterfragment;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_parts_list;
    }

    @Override
    protected void findViews() {
        partsListPresenter = new PartsListPresenter(this,this);
        BGARefreshLayoutUtils.initRefreshLayout(this, mBGARefreshLayout, false);
        //初始化侧边筛选布局
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerContent = (FrameLayout) findViewById(R.id.drawer_content);

        partsListAdapter = new PartsListAdapter(this,partsBeenList,R.layout.item_parts_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(PartsListActivity.this));
        recyclerView.setAdapter(partsListAdapter);
        partsListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("accessories_id",partsListAdapter.getItem(position).getAccessoriesId());
                IntentUtil.gotoActivity(PartsListActivity.this,PartsDetailActivity.class,bundle1);
            }
        });

        mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                currentPage = 1;
                searchData(false);
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                currentPage ++;
                searchData(false);
                return true;
            }
        });
    }

    @Override
    protected void init() {

    }

    @OnClick({R.id.tv_filter,R.id.tv_search,R.id.tv_price,R.id.iv_no_data})
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.tv_filter:
                mDrawerLayout.openDrawer(mDrawerContent);
                break;
            case R.id.tv_search:
                searchText = et_search.getText().toString().trim();
                searchData(true);
                break;
            case R.id.tv_price:
                Drawable drawable = null;
                switch (priceOrderStatus){
                    case 0:
                        drawable = getResources().getDrawable(R.drawable.ic_price_up);
                        priceOrderStatus = 1;
                        break;
                    case 1:
                        drawable = getResources().getDrawable(R.drawable.ic_price_down);
                        priceOrderStatus = 2;
                        break;
                    case 2:
                        drawable = getResources().getDrawable(R.drawable.ic_price_normal);
                        priceOrderStatus = 0;
                        break;
                }
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                tv_price.setCompoundDrawables(null,null,drawable,null);
                mBGARefreshLayout.beginRefreshing();
                break;
             case R.id.iv_no_data:
                 iv_no_data.setVisibility(View.GONE);
                 recyclerView.setVisibility(View.VISIBLE);
                 searchText = "";
                 et_search.setText("");
                 searchData(false);
                 break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBGARefreshLayout.beginRefreshing();
    }

    @Override
    protected void initGetData() {
         Intent myIntent = getIntent();
         searchText = myIntent.getStringExtra("searchText");
         typeId = myIntent.getStringExtra("typeId");
         et_search.setText(searchText);

        filterfragment = new FilterFragment();
        fragmentManager = getSupportFragmentManager();
        final Bundle bundle = new Bundle();
        bundle.putString("departmentName","");
        bundle.putString("typeId",typeId);
        filterfragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.drawer_content, filterfragment).commit();
    }
    
    private void searchData(boolean isClickSearch){
        if(isClickSearch){
            if(TextUtils.isEmpty(searchText)){
                ToastUtil.showToast(PartsListActivity.this,"请输入要搜索的内容");
                return;
            }
        }
        //获取列表数据
        partsListPresenter.searchPartsData(typeId,searchText,attributeSeach,String.valueOf(priceOrderStatus),currentPage);
    }

    @Override
    protected void widgetListener() {

    }


    public void onDefiniteScreen(String typeIds,String otherSeachs){
        typeId = typeIds;
        attributeSeach = otherSeachs;
        mBGARefreshLayout.beginRefreshing();
    }

    @Override
    public void onLoadDataList(List<PartsBean> returnPartsBeenList) {


        if(returnPartsBeenList != null && returnPartsBeenList.size() > 0){
            partsBeenList.clear();
            partsBeenList.addAll(returnPartsBeenList);
            iv_no_data.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            partsBeenList.clear();
            iv_no_data.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        partsListAdapter.notifyDataSetChanged();
        mBGARefreshLayout.endRefreshing();
    }

    @Override
    public void onLoadError(String msg) {
        ToastUtil.showToast(this,msg);
        mBGARefreshLayout.endRefreshing();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(fragmentManager != null){
            fragmentManager = null;
        }
    }
}
