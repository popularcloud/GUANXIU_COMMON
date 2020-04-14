package com.lwc.common.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gcssloop.widget.PagerGridLayoutManager;
import com.gcssloop.widget.PagerGridSnapHelper;
import com.lwc.common.R;
import com.lwc.common.adapter.RepairTypeAdapter;
import com.lwc.common.module.bean.Repairs;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeaseTypeViewHolder extends BaseViewHolder{

    @BindView(R.id.rv_repair_type)
    RecyclerView rv_repair_type;
    public LeaseTypeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void bindViewHolder(Object data, Context context) {

        List<Repairs> leaseTypeData = (List<Repairs>) data;
        // 布局管理器
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(1, 5, PagerGridLayoutManager.HORIZONTAL);
        rv_repair_type.setLayoutManager(layoutManager);

        // 滚动辅助器
   /*     PagerGridSnapHelper snapHelper = new PagerGridSnapHelper();
        snapHelper.attachToRecyclerView(rv_repair_type);*/

        //设置适配器
        RepairTypeAdapter repairTypeAdapter = new RepairTypeAdapter(context,leaseTypeData);
        rv_repair_type.setAdapter(repairTypeAdapter);

    }

}
