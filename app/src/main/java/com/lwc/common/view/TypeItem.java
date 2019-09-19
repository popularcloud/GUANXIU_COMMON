package com.lwc.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.bean.Malfunction;


public class TypeItem extends LinearLayout {

	private View view;
	private Context mContext;
	private TextView txt_name, txt_money;
	private ImageView iv_delete;
	private View view_line;

	public TypeItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TypeItem(Context context) {
		this(context,null);
		this.mContext = context;
		view = View.inflate(context, R.layout.item_type, this);
		txt_name = (TextView) view.findViewById(R.id.txt_name);
		txt_money = (TextView) view.findViewById(R.id.txt_money);
		iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
		view_line = (View) view.findViewById(R.id.view_line);
	}

	public void initData(Malfunction repair){
		//初始化数据
		txt_name.setText(repair.getDeviceTypeName() + " -> "
				+ repair.getReqairName());
		txt_money.setText("");
	}

	public void setListener(Malfunction repair, OnClickListener listener) {
		iv_delete.setTag(repair);
		iv_delete.setOnClickListener(listener);
	}

	public void setLineGone() {
		view_line.setVisibility(GONE);
	}
}
