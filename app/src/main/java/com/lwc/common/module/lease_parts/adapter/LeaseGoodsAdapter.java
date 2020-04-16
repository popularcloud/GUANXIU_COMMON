package com.lwc.common.module.lease_parts.adapter;

import android.content.Context;

import com.lwc.common.R;
import com.lwc.common.module.lease_parts.bean.DrugItemBean;
import com.lwc.common.module.lease_parts.bean.DrugListBean;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * User: Liumj(liumengjie@365tang.cn)
 * Date: 2016-10-11
 * Time: 15:51
 * describe:  右边适配器
 */
public class LeaseGoodsAdapter extends SuperAdapter<DrugListBean> {


	public LeaseGoodsAdapter(Context context, List<DrugListBean> items, int layoutResId) {
		super(context, items, layoutResId);
	}

	@Override
	public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, DrugListBean item) {
		holder.setText(R.id.item_main_right_title,item.getType());

		final List<DrugItemBean> drugItemBeen = item.getmList();

	}
}
