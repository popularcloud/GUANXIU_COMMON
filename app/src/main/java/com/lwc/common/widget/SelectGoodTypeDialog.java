package com.lwc.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.lwc.common.R;

/**
 * 自定义对话框
 * 
 */
public class SelectGoodTypeDialog extends Dialog implements View.OnClickListener{

	/** 上下文 */
	private Context context;
	CallBack callBack;
	public SelectGoodTypeDialog(Context context, CallBack callBack, String one, String two) {
		super(context, R.style.BottomDialogStyle);
		// 拿到Dialog的Window, 修改Window的属性
		Window window = getWindow();
		window.getDecorView().setPadding(0, 0, 0, 0);
		// 获取Window的LayoutParams
		WindowManager.LayoutParams attributes = window.getAttributes();
		attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
		attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		// 一定要重新设置, 才能生效
		window.setAttributes(attributes);
		this.callBack = callBack;
		init(context, one, two);
	}

	/**
	 *
	 * @param context
	 * @param theme
     */
	public SelectGoodTypeDialog(Context context, int theme) {
		super(context, theme);
		init(context, null, null);
	}

	public SelectGoodTypeDialog(Context context) {
		super(context, R.style.dialog_style);
		init(context, null, null);
	}

	/**
	 * 初始话对话框
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param context
	 * 
	 */
	protected void init(Context context, String one, String two) {
		this.context = context;
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
		this.setContentView(R.layout.dialog_select_goods_type_layout);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		}
	}

	public interface CallBack {

		/**
		 * 调用图库
		 */
		void oneClick();

		/**
		 * 拍照
		 */
		void twoClick();

		/**
		 * 取消
		 */
		void cancelCallback();
	}
}
