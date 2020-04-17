package com.lwc.common.module.lease_parts.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Dimension;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.lease_parts.bean.DrugItemBean;
import com.lwc.common.module.lease_parts.bean.DrugListBean;
import com.lwc.common.module.lease_parts.viewholder.LeaseGoodsViewHolder;
import com.lwc.common.widget.TagsLayout;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;
import java.util.zip.Inflater;

/**
 * User: Liumj(liumengjie@365tang.cn)
 * Date: 2016-10-11
 * Time: 15:51
 * describe:  右边适配器
 */
public class LeaseGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private Context context;

	public LeaseGoodsAdapter(Context context){
		this.context = context;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_lease_good,parent,false);
		return new LeaseGoodsViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

		TagsLayout tl_tags = holder.itemView.findViewById(R.id.tl_tags);

		String [] tags = new String[]{"免押金","3月起租","满500减20"};
		if(tags.length > 0){
			tl_tags.setVisibility(View.VISIBLE);
			//String[] tags =item.getLabelName().split(",");
			tl_tags.removeAllViews();
			for (int i = 0; i < tags.length; i++) {
				if(i > 3){
					break;
				}
				TextView textView = new TextView(context);
				textView.setText(tags[i]);
				textView.setTextColor(Color.parseColor("#ff3a3a"));
				textView.setTextSize(Dimension.SP,12);
				textView.setGravity(Gravity.CENTER);
				textView.setPadding(5,0,5,0);
				textView.setBackgroundResource(R.drawable.round_square_red_line);
				textView.setText(tags[i]);
				tl_tags.addView(textView);
			}
		}else{
			tl_tags.setVisibility(View.GONE);
		}

	}

	@Override
	public int getItemCount() {
		return 8;
	}
}
