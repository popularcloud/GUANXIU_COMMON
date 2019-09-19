package com.lwc.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.view.BaseEffects;
import com.lwc.common.view.NewsPaper;

/**
 * 自定义对话框
 * 
 */
public class CustomDialog extends Dialog {

	/** 对话框按钮1的ID */
	public static final int ID_BUTTON_1 = R.id.custom_dialog_btn_1;
	/** 对话框按钮2的ID */
	public static final int ID_BUTTON_2 = R.id.custom_dialog_btn_2;

	/** 上下文 */
	private Context context;

	/** 内容视图 */
	private View view_Content;
	/** 等待框视图 */
	private View view_Progress;
	/** 按钮组视图 */
	private View view_Buttons;

	/** 标题 */
	private TextView txt_Title;
	/** 提示信息 */
	private TextView txt_Message;
	/** 等待信息 */
	private TextView txt_Progress;

	/** 按钮1 红色 */
	private TextView btn_Button1;
	/** 按钮2 灰色 */
	private TextView btn_Button2;
	View view_line_btns;
	private RelativeLayout main;

	public CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	/**
	 *
	 * @param context
	 * @param theme
     */
	public CustomDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public CustomDialog(Context context) {
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
		this.setCancelable(false);
		this.setCanceledOnTouchOutside(false);
		this.setContentView(R.layout.view_custom_dialog);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		view_Buttons = findViewById(R.id.custom_dialog_view_buttons);
		view_Progress = findViewById(R.id.custom_dialog_view_progress);
		view_Content = findViewById(R.id.custom_dialog_view_content);
		main = (RelativeLayout)findViewById(R.id.main);
		txt_Title = (TextView) findViewById(R.id.custom_dialog_txt_title);
		txt_Progress = (TextView) findViewById(R.id.custom_dialog_txt_progress);
		txt_Message = (TextView) findViewById(R.id.custom_dialog_txt_message);


		btn_Button1 = (TextView) findViewById(R.id.custom_dialog_btn_1);
		btn_Button2 = (TextView) findViewById(R.id.custom_dialog_btn_2);
		view_line_btns = findViewById(R.id.view_line_btns);

		widgetListener();
	}

	private void start() {
		BaseEffects animator = new NewsPaper();
		animator.setDuration(500);
		animator.start(main);
	}


	public void setGoneBut2() {
		view_line_btns.setVisibility(View.GONE);
		btn_Button2.setVisibility(View.GONE);
	}
	public void setButton1Text(String text) {
		btn_Button1.setText(text);
	}

	public void setButton2Text(String text) {
		btn_Button2.setText(text);
	}
	/**
	 * 组件
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 */
	private void widgetListener() {
		this.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				start();
			}
		});
	}

	/**
	 * 设置标题栏
	 * 
	 */
	@Override
	public void setTitle(CharSequence title) {
		txt_Title.setVisibility(View.VISIBLE);
		txt_Title.setText(title);
	}

	public void setTitle(CharSequence title, int resource, int color) {
		txt_Title.setVisibility(View.VISIBLE);
		txt_Title.setText(title);
		txt_Title.setTextColor(color);

	}

	/**
	 * 设置提示信息
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param msg
	 */
	public void setMessage(String msg) {
		txt_Message.setVisibility(View.VISIBLE);
		txt_Message.setText(msg);
	}

	public void setMsgGra() {
		txt_Message.setGravity(Gravity.LEFT);
	}

	/**
	 * 设置等待对话框
	 *
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 *
	 * @param progress
	 */
	public void setProgress(String progress) {
		view_Progress.setVisibility(View.VISIBLE);
		view_Content.setVisibility(View.GONE);
		txt_Progress.setText(progress);
	}

	/**
	 * 设置按钮1
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param listener
	 *        按钮监听事件
	 */
	public void setEnterBtn(final OnClickListener listener) {
		view_Buttons.setVisibility(View.VISIBLE);
		btn_Button1.setVisibility(View.VISIBLE);
		btn_Button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onClick(CustomDialog.this, ID_BUTTON_1, null);
			}
		});
	}

	/**
	 * 设置取消按钮
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param listener
	 */
	public void setCancelBtn(final OnClickListener listener) {
		view_Buttons.setVisibility(View.VISIBLE);
		btn_Button2.setVisibility(View.VISIBLE);
		btn_Button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onClick(CustomDialog.this, ID_BUTTON_2, null);
			}
		});
	}

	/**
	 * 对话框按钮点击回调事件
	 * 
	 * @version 1.0
	 * @date 2013-7-30
	 * 
	 */
	public interface OnClickListener {
		/**
		 * 对话框按钮被点击时候调用
		 * 
		 * @version 1.0
		 * @createTime 2013-7-30,上午10:13:13
		 * @updateTime 2013-7-30,上午10:13:13
		 * @updateInfo (此处输入修改内容,若无修改可不写.)
		 * 
		 * @param dialog
		 *        当前对话框对象
		 * @param id
		 *        被点击按钮的id(例：CustomDialog.ID_BUTTON_1 , CustomDialog.ID_BUTTON_2 ...)
		 * 
		 * @param object
		 *        附带信息
		 */
		public void onClick(CustomDialog dialog, int id, Object object);

	}
}
