package com.lwc.common.adapter;

import android.content.Context;

import com.lwc.common.R;
import com.lwc.common.module.bean.Malfunction;

import java.util.List;

public class MalfunctionAdapter extends ComAdapter<Malfunction> {

	public MalfunctionAdapter(Context context, List<Malfunction> datas, int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ComViewHolder holder, Malfunction t) {
		holder.setText(R.id.txt_skill, t.getReqairName());
	}

}
