package com.lwc.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.bean.Solution;
import com.lwc.common.utils.Utils;


public class SolutionItem extends LinearLayout {

	private View view;
	private Context mContext;
	private TextView txt_skill;
	private TextView txt_skill_money;

	public SolutionItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SolutionItem(Context context) {
		this(context,null);
		this.mContext = context;
		view = View.inflate(context, R.layout.item_solution, this);
		txt_skill_money = (TextView) view.findViewById(R.id.txt_skill_money);
		txt_skill = (TextView) view.findViewById(R.id.txt_skill);
	}

	public void initData(Solution solution){
		String name = solution.getExampleName();
		String money = Utils.getMoney(Utils.chu(""+solution.getMaintainCost(), "100"))+"元";
		//初始化数据
		txt_skill.setText(name);
		txt_skill_money.setText(money);
	}
	
}
