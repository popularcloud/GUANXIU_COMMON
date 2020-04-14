package com.lwc.common.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gcssloop.widget.PagerGridLayoutManager;
import com.gcssloop.widget.PagerGridSnapHelper;
import com.lwc.common.R;
import com.lwc.common.adapter.RepairTypeAdapter;
import com.lwc.common.module.bean.Repairs;
import com.lwc.common.module.repairs.ui.activity.RepairsTypeActivity;
import com.lwc.common.utils.IntentUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepairTypeViewHolder extends BaseViewHolder{

    @BindView(R.id.rv_repair_type)
    RecyclerView rv_repair_type;
    @BindView(R.id.tv_electric)
    TextView tv_electric;

    public RepairTypeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void bindViewHolder(Object data, final Context context) {

        List<Repairs> repairsList = (List<Repairs>) data;
        // 布局管理器
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(2, 5, PagerGridLayoutManager.HORIZONTAL);
        rv_repair_type.setLayoutManager(layoutManager);

        // 滚动辅助器
      /*  PagerGridSnapHelper snapHelper = new PagerGridSnapHelper();
        snapHelper.attachToRecyclerView(rv_repair_type);*/



        //设置适配器
        RepairTypeAdapter repairTypeAdapter = new RepairTypeAdapter(context,repairsList);
        rv_repair_type.setAdapter(repairTypeAdapter);


        tv_electric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.gotoActivity(context, RepairsTypeActivity.class);
            }
        });

    }

}
