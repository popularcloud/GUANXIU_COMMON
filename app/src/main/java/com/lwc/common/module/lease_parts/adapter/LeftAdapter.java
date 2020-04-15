package com.lwc.common.module.lease_parts.adapter;

import android.content.Context;
import android.graphics.Color;

import com.lwc.common.R;
import com.lwc.common.adapter.viewholder.BaseViewHolder;
import com.lwc.common.module.lease_parts.bean.DrugBean;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * User: Liumj(liumengjie@365tang.cn)
 * Date: 2016-10-11
 * Time: 15:19
 * describe:  左侧适配器
 */
public class LeftAdapter extends SuperAdapter<DrugBean> {
	private int selectPos=0;

	public LeftAdapter(Context context, List<DrugBean> items, int layoutResId) {
		super(context, items, layoutResId);
		//R.layout.item_main_left
	}

	public int getSelectPos() {
		return selectPos;
	}

	public void setSelectPos(int selectPos) {
		this.selectPos = selectPos;
	}

	@Override
	public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, DrugBean item) {
		if(selectPos==holder.getAdapterPosition()){
			//holder.setVisible(R.id.item_main_left_bg,true);
			//holder.convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			holder.setTextColor(R.id.item_main_left_type, Color.parseColor("#40a5f3"));
		}else{
			//holder.convertView.setBackgroundColor(Color.parseColor("#f7f7f7"));
			holder.setTextColor(R.id.item_main_left_type, Color.parseColor("#333333"));
			//holder.setVisible(R.id.item_main_left_bg,false);
		}

		holder.setText(R.id.item_main_left_type,item.getTitle());
	}
}
