package com.lwc.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.module.bean.Coupon;
import com.lwc.common.module.common_adapter.SelectCouponListAdapter;

import org.byteam.superadapter.OnItemClickListener;

import java.util.List;

/**
 * 自定义对话框
 * 
 */
public class SelectCouponDialog extends Dialog {

	/** 上下文 */
	private Context context;

	private TextView tv_titel;
	private RecyclerView recyclerView;
	private ImageView iv_cancel;
	private SelectCouponListAdapter adapter;
	public Coupon coupon;
	private int oldPosition;
	private List<Coupon> coupons;
	public SelectCouponDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	/**
	 *
	 * @param context
	 * @param theme
     */
	public SelectCouponDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public SelectCouponDialog(Context context) {
		super(context, R.style.DialogTheme);
		init(context);
	}

	/**
	 * 初始话对话框
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param context
	 * 
	 */
	protected void init(Context context) {
		this.context = context;
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(false);
		this.setContentView(R.layout.dialog_select_coupon);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		Window win = getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		win.setGravity(Gravity.BOTTOM);
		win.setAttributes(lp);
		tv_titel = (TextView) findViewById(R.id.tv_titel);
		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
		iv_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	/**
	 * 设置标题栏
	 * 
	 */
	@Override
	public void setTitle(CharSequence title) {
		tv_titel.setText(title);
	}

	/**
	 * 设置优惠券
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	public void setMessage(final List<Coupon> list) {
		coupons = list;
		for(int i=0; i<coupons.size(); i++) {
			coupons.get(i).setSelect(0);
		}
		oldPosition = coupons.indexOf(coupon);
		this.coupon.setSelect(1);
		coupons.set(oldPosition, this.coupon);

		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		adapter = new SelectCouponListAdapter(context, coupons, R.layout.item_select_coupon);
		recyclerView.setAdapter(adapter);
		adapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
            public void onItemClick(View itemView, int viewType, int position) {
				Coupon cou = coupons.get(oldPosition);
				cou.setSelect(0);
				coupons.set(oldPosition, cou);

				coupon = adapter.getItem(position);
				coupon.setSelect(1);
				coupons.set(position, coupon);

				adapter.replaceAll(coupons);
				oldPosition = position;
			}
        });
	}

	public Coupon getCoupon(){
		return coupon;
	}
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	@Override
	public void setOnDismissListener(OnDismissListener listener) {
		super.setOnDismissListener(listener);
	}
}
