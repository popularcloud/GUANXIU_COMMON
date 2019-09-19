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
import com.lwc.common.module.common_adapter.CouponListDialogAdapter;

import java.util.List;

/**
 * 自定义对话框
 * 
 */
public class CouponDialog extends Dialog {

	/** 上下文 */
	private Context context;

	private TextView tv_titel, tv_tip, tv_lq, tv_yqrx;
	private RecyclerView recyclerView;
	private ImageView iv_cancel;


	public CouponDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	/**
	 *
	 * @param context
	 * @param theme
     */
	public CouponDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public CouponDialog(Context context) {
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
		this.setContentView(R.layout.dialog_coupon);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		Window win = getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		win.setGravity(Gravity.CENTER);
		win.setAttributes(lp);
		tv_titel = (TextView) findViewById(R.id.tv_titel);
		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		tv_lq = (TextView) findViewById(R.id.tv_lq);
		tv_yqrx = (TextView) findViewById(R.id.tv_yqrx);
		iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
	}

	/**
	 * 设置标题栏
	 * 
	 */
	@Override
	public void setTitle(CharSequence title) {
		tv_titel.setText(title);
	}

	public void setTipVisibility(int visibility) {
		tv_lq.setVisibility(View.GONE);
		tv_yqrx.setVisibility(View.GONE);
		iv_cancel.setVisibility(View.VISIBLE);
		tv_tip.setVisibility(visibility);
	}

	/**
	 * 设置优惠券
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	public void setMessage(List<Coupon> coupons) {
		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		CouponListDialogAdapter adapter = new CouponListDialogAdapter(context, coupons, R.layout.item_coupon_dialog);
		recyclerView.setAdapter(adapter);
	}


	/**
	 * 设置按钮1
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param listener
	 *        按钮监听事件
	 */
	public void setEnterBtn(View.OnClickListener listener) {
		tv_lq.setOnClickListener(listener);
	}

	/**
	 * 设置取消按钮
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param listener
	 */
	public void setCancelBtn(View.OnClickListener listener) {
		tv_yqrx.setOnClickListener(listener);
	}

	public void setGbBtn(View.OnClickListener listener) {
		iv_cancel.setOnClickListener(listener);
	}

	@Override
	public void setOnDismissListener(OnDismissListener listener) {
		super.setOnDismissListener(listener);
	}
}
