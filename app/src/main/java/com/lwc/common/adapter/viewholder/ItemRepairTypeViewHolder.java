package com.lwc.common.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemRepairTypeViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.item_name)
     public TextView item_name;
    @BindView(R.id.item_image)
    public ImageView item_image;

    public ItemRepairTypeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
};