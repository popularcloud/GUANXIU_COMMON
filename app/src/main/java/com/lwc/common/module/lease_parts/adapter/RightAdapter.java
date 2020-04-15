package com.lwc.common.module.lease_parts.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

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
public class RightAdapter extends SuperAdapter<DrugListBean> {


	public RightAdapter(Context context, List<DrugListBean> items, int layoutResId) {
		super(context, items, layoutResId);
	}
	/*public RightAdapter(List<DrugListBean> data) {
		super(R.layout.item_main_right, data);
	}*/


	@Override
	public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, DrugListBean item) {
		holder.setText(R.id.item_main_right_title,item.getType());
		//TagFlowLayout  flowlayout = helper.getView(R.id.item_main_right_taglayout);
		final List<DrugItemBean> drugItemBeen = item.getmList();
		//final DrugTagAdapter drugAdapter=new DrugTagAdapter(mContext,drugItemBeen);

		//flowlayout.setAdapter(drugAdapter);
		/*flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
			@Override
			public boolean onTagClick(View view, int position, FlowLayout parent) {
				DrugItemBean drugItemBean = drugItemBeen.get(position);
				for (DrugItemBean b:
						drugItemBeen) {
					b.setCheck(false);
				}
				drugItemBean.setCheck(true);
				drugAdapter.notifyDataChanged();
				Snackbar.make(helper.convertView, "点击了"+drugItemBean.getName(), Snackbar.LENGTH_SHORT).show();
				return false;
			}
		});*/
	}
}
