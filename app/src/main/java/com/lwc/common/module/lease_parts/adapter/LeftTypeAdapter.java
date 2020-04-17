package com.lwc.common.module.lease_parts.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.TypedValue;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.lease_parts.bean.DrugBean;
import com.lwc.common.module.lease_parts.bean.LeaseLeftBean;
import com.lwc.common.utils.DisplayUtil;
import com.lwc.common.utils.Utils;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

import kotlin.Unit;

/**
 * User: Liumj(liumengjie@365tang.cn)
 * Date: 2016-10-11
 * Time: 15:19
 * describe:  左侧适配器
 */
public class LeftTypeAdapter extends SuperAdapter<LeaseLeftBean> {

	private int selectPos = 0;
	private Context mContext;

	public LeftTypeAdapter(Context context, List<LeaseLeftBean> items, int layoutResId) {
		super(context, items, layoutResId);
		mContext = context;
	}

	public int getSelectPos() {
		return selectPos;
	}

	public void setSelectPos(int selectPos) {
		this.selectPos = selectPos;
	}

	@Override
	public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, LeaseLeftBean item) {

		TextView tv_name = holder.itemView.findViewById(R.id.tv_name);

		tv_name.setText(item.getName());

		if(layoutPosition == selectPos){
			//tv_name.setTextColor(Color.parseColor("#000000"));
			TextPaint tp = tv_name.getPaint();
			tp.setFakeBoldText(true);
			tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
			tv_name.setBackgroundColor(Color.parseColor("#ffffff"));

			Drawable drawable= mContext.getResources().getDrawable(R.drawable.ic_blue_line);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			tv_name.setCompoundDrawables(drawable,null,null,null);

		}else{
		//	tv_name.setTextColor(Color.parseColor("#000000"));
			TextPaint tp = tv_name.getPaint();
			tp.setFakeBoldText(false);
			tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
			tv_name.setBackgroundColor(Color.parseColor("#f0f1f6"));

			Drawable drawable= mContext.getResources().getDrawable(R.drawable.ic_blue_line);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			tv_name.setCompoundDrawables(null,null,null,null);
		}
	}
}
