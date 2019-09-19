package com.lwc.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.interf.OnLookRewardCallBack;
import com.lwc.common.utils.ImageLoaderUtil;

/**
 * 签到成功对话框
 * 
 */
public class LuckSuccessDialog extends Dialog {

	/** 上下文 */
	private Context context;

	private TextView tv_notice, tv_luck_again, tv_look_reward;
	private ImageView iv_cancel,iv_header;


	public LuckSuccessDialog(Context context, String notice, String reward, int rewardType, OnLookRewardCallBack onLookRewardCallBack) {
		super(context, true, null);
		init(context,notice,reward,rewardType,onLookRewardCallBack);
	}


	/**
	 * 初始话对话框
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param context notice reward onLookRewardCallBack
	 * 
	 */
	protected void init(Context context, String notice, String headerImg, final int rewardType, final OnLookRewardCallBack onLookRewardCallBack) {
		this.context = context;
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(false);
		this.setContentView(R.layout.dialog_luck_sucess);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		Window win = getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		win.setGravity(Gravity.CENTER);
		win.setAttributes(lp);
		tv_notice = (TextView) findViewById(R.id.tv_notice);
		iv_header = (ImageView) findViewById(R.id.iv_header);
		tv_look_reward = (TextView) findViewById(R.id.tv_look_reward);
		tv_luck_again = (TextView) findViewById(R.id.tv_luck_again);
		iv_cancel = (ImageView) findViewById(R.id.iv_cancel);

		tv_notice.setText(notice);
		ImageLoaderUtil.getInstance().displayFromNet(context,headerImg,iv_header);

		tv_look_reward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onLookRewardCallBack != null){
					onLookRewardCallBack.onItemClick(rewardType);
					dismiss();
				}
			}
		});

		tv_luck_again.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onLookRewardCallBack != null){
					onLookRewardCallBack.onItemClick(-1);
					dismiss();
				}
			}
		});

		iv_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	public void setGbBtn(View.OnClickListener listener) {
		iv_cancel.setOnClickListener(listener);
	}

	@Override
	public void setOnDismissListener(OnDismissListener listener) {
		super.setOnDismissListener(listener);
	}
}
