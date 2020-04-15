package com.lwc.common.adapter.viewholder;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.module.common_adapter.MyPackageListAdapter;
import com.lwc.common.module.order.ui.activity.NewPackageListActivity;
import com.lwc.common.module.order.ui.activity.PackageDetailActivity;
import com.lwc.common.utils.DisplayUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.OnItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MealTypeViewHolder extends BaseViewHolder{

    @BindView(R.id.rv_meal)
    RecyclerView rv_meal;
    @BindView(R.id.tv_electric)
    TextView tv_electric;
    private MyPackageListAdapter adapter;

    public MealTypeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void bindViewHolder(Object data, final Context context) {
        ArrayList<PackageBean> packageBeanList = (ArrayList<PackageBean>) data;

        rv_meal.setLayoutManager(new LinearLayoutManager(context));
        //rv_meal.setItemViewCacheSize(5);
        adapter = new MyPackageListAdapter(context, packageBeanList, R.layout.item_my_package,0);
        rv_meal.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                if (Utils.gotoLogin(MainActivity.user,MainActivity.activity)) {
                    PackageBean pack = adapter.getItem(position);
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("package", pack);
                    bundle2.putInt("position", position);
                    IntentUtil.gotoActivity(context, PackageDetailActivity.class, bundle2);
                }
            }
        });

        tv_electric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.gotoLogin(MainActivity.user,MainActivity.activity))
                    IntentUtil.gotoActivity(MainActivity.activity, NewPackageListActivity.class);
            }
        });

    /*    if(LeaseHomeActivity.mainFragment.newestOrders != null && LeaseHomeActivity.mainFragment.newestOrders.size() > 0){
            rv_meal.setPadding(0,0,0, DisplayUtil.dip2px(context,100));
        }*/
    }
}
