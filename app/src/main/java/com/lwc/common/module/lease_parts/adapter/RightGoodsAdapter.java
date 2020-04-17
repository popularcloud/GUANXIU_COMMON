package com.lwc.common.module.lease_parts.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.lease_parts.bean.LeaseRightBean;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

/**
 * User: Liumj(liumengjie@365tang.cn)
 * Date: 2016-10-11
 * Time: 15:19
 * describe:  左侧适配器
 */
public class RightGoodsAdapter extends SuperAdapter<LeaseRightBean> {

	private Context mContext;

	public RightGoodsAdapter(Context context, List<LeaseRightBean> items, int layoutResId) {
		super(context, items, layoutResId);
		mContext = context;
	}

	@Override
	public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, LeaseRightBean item) {
		TextView tv_name = holder.itemView.findViewById(R.id.tv_name);
		tv_name.setText(item.getName());
	}
}
