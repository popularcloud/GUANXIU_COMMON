package com.lwc.common.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.lwc.common.R;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.adapter.viewholder.ItemRepairTypeViewHolder;
import com.lwc.common.module.bean.Repairs;
import com.lwc.common.module.repairs.ui.activity.MalfunctionSelectActivity;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.Utils;

import java.util.List;

public class RepairTypeAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Repairs> repairsList; //所有维修类型集合

    public RepairTypeAdapter(Context context,List<Repairs> repairsList) {
        mContext = context;
        this.repairsList = repairsList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View convertView = View.inflate(mContext, R.layout.index_home_grid_item, null);
            ItemRepairTypeViewHolder holder = new ItemRepairTypeViewHolder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemRepairTypeViewHolder holder1 = (ItemRepairTypeViewHolder) holder;
        holder1.item_name.setText(repairsList.get(position).getDeviceTypeName() + "");
        ImageLoaderUtil.getInstance().displayFromNet(mContext, repairsList.get(position).getDeviceTypeIcon().replace("https","http"),  holder1.item_image);

        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.gotoLogin(MainActivity.user, MainActivity.activity)) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("repairs", repairsList.get(position));
                    IntentUtil.gotoActivity(mContext, MalfunctionSelectActivity.class, bundle);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return repairsList==null?0:repairsList.size();
    }

}



