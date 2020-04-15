package com.lwc.common.module.lease_parts.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.module.lease_parts.adapter.LeftAdapter;
import com.lwc.common.module.lease_parts.adapter.RightAdapter;
import com.lwc.common.module.lease_parts.bean.DrugBean;
import com.lwc.common.module.lease_parts.bean.DrugItemBean;
import com.lwc.common.module.lease_parts.bean.DrugListBean;
import com.lwc.common.module.partsLib.ui.activity.PartsListActivity;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.ToastUtil;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author younge
 * @date 2018/12/19 0019
 * @email 2276559259@qq.com
 */
public class LeaseTypeSearchActivity extends BaseActivity{


    @BindView(R.id.tv_search)
    TextView tv_search;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.main_left_rv)
    RecyclerView mLeftRvRecyclerView;
    @BindView(R.id.main_right_rv)
    RecyclerView mRightRvRecyclerView;

    private String searchText = "";
    private String typeId = "";

    private LeftAdapter leftAdapter;
    private RightAdapter rightAdapter;

    private List<DrugBean> drugBeanList;
    private List<DrugListBean> listBeanList;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_lease_type_search;
    }

    @Override
    protected void findViews() {
        tv_search.setTextColor(Color.parseColor("#1481ff"));
    }

    @Override
    protected void init() {
        leftAdapter=new LeftAdapter(this,drugBeanList,R.layout.item_main_left);
        rightAdapter=new RightAdapter(this,listBeanList,R.layout.item_main_right);

        mLeftRvRecyclerView.setAdapter(leftAdapter);
        mRightRvRecyclerView.setAdapter(rightAdapter);

        mLeftRvRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRightRvRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        leftAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                DrugBean drugBean = drugBeanList.get(position);
                listBeanList.clear();
                listBeanList.addAll(drugBean.getmList());
                leftAdapter.setSelectPos(position);
                leftAdapter.notifyDataSetChanged();
                rightAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void initGetData() {
        drugBeanList=new ArrayList<>();
        listBeanList=new ArrayList<>();

        DrugBean d1=new DrugBean();
        d1.setTitle("糖尿病");
        DrugBean d2=new DrugBean();
        d2.setTitle("高血压");
        DrugBean d3=new DrugBean();
        d3.setTitle("高血脂");

        DrugListBean l1=new DrugListBean();

        l1.setType("口服药");


        DrugListBean l2=new DrugListBean();

        l2.setType("胰岛素");

        DrugItemBean b1=new DrugItemBean();
        b1.setName("二甲双胍");
        DrugItemBean b2=new DrugItemBean();
        b2.setName("维生素C");
        DrugItemBean b3=new DrugItemBean();
        b3.setName("格列本服");
        DrugItemBean b4=new DrugItemBean();
        b4.setName("得每通胶囊");
        DrugItemBean b5=new DrugItemBean();
        b5.setName("阿卡波糖");
        DrugItemBean b6=new DrugItemBean();
        b6.setName("美卡素");

        List<DrugItemBean> list1=new ArrayList<>();
        List<DrugItemBean> list2=new ArrayList<>();
        list1.add(b1);
        list1.add(b2);
        list1.add(b3);
        list1.add(b4);
        list2.add(b5);
        list2.add(b6);

        l1.setmList(list1);
        l2.setmList(list2);

        List<DrugListBean> li1=new ArrayList<>();
        List<DrugListBean> li2=new ArrayList<>();
        List<DrugListBean> li3=new ArrayList<>();

        li1.add(l1);
        li2.add(l2);
        li3.add(l1);
        li3.add(l2);

        d1.setmList(li1);
        d2.setmList(li2);
        d3.setmList(li3);


        drugBeanList.add(d1);
        drugBeanList.add(d2);
        drugBeanList.add(d3);

        listBeanList.addAll(drugBeanList.get(0).getmList());
    }

    @Override
    protected void widgetListener() {}

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        ImmersionBar.with(LeaseTypeSearchActivity.this)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .navigationBarColor(R.color.white).init();
    }

    @OnClick({R.id.tv_search})
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.tv_search:
                searchText = et_search.getText().toString().trim();
                if(TextUtils.isEmpty(searchText)){
                    ToastUtil.showToast(LeaseTypeSearchActivity.this,"请输入需要搜索的内容");
                    return;
                }
                typeId = "";
                searchProduct();
                break;
        }
    }

    /**
     * 搜索
     */
    private void searchProduct(){
        Bundle bundle = new Bundle();
        bundle.putString("searchText",searchText);
        bundle.putString("typeId",typeId);
        IntentUtil.gotoActivity(LeaseTypeSearchActivity.this, PartsListActivity.class,bundle);

        et_search.setText("");
        searchText = "";
    }
}
