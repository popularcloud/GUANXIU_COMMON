package com.lwc.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lwc.common.R;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.wallet.ui.PayPwdActivity;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

/**
 * 自定义对话框
 * 
 */
public class PayTypeDialog extends Dialog implements View.OnClickListener{

	/** 上下文 */
	private Context context;
	CallBack callBack;

	RelativeLayout rl_qb;
	RelativeLayout rl_wechat;
	RelativeLayout rl_alipay;
	LinearLayout ll_pay_type;
	LinearLayout ll_pay_pwd;
	ImageView ic_close;
	ImageView iv_close_two;
	TextView tv_error_msg;

	PasswordEditText et_psd;

	private int payType = 1; //1钱包 2.支付宝 3.微信

	private User user = null;
	private TextView tv_forget_pwd;

	private boolean isEnableMoney = true;

	public PayTypeDialog(Context context,String payPrice,CallBack callBack) {
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
		init(context,payPrice);
	}

	/**
	 *
	 * @param context
	 * @param theme
     */
	public PayTypeDialog(Context context, int theme) {
		super(context, theme);
		init(context,null);
	}

	public PayTypeDialog(Context context) {
		super(context, R.style.dialog_style);
		init(context,null);
	}

	/**
	 * 初始话对话框
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param context
	 * 
	 */
	protected void init(final Context context, String payPrice) {
		this.context = context;
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
		this.setContentView(R.layout.dialog_pay_type);

		rl_qb = findViewById(R.id.rl_qb);
		tv_error_msg = findViewById(R.id.tv_error_msg);
		rl_wechat = findViewById(R.id.rl_wechat);
		rl_alipay = findViewById(R.id.rl_alipay);
		ll_pay_type = findViewById(R.id.ll_pay_type);
		ll_pay_pwd = findViewById(R.id.ll_pay_pwd);
		et_psd = findViewById(R.id.et_psd);
		tv_forget_pwd = findViewById(R.id.tv_forget_pwd);

		iv_close_two = findViewById(R.id.iv_close_two);
		ic_close = findViewById(R.id.ic_close);


		TextView tv_qb_pay = findViewById(R.id.tv_qb_pay);
		user = SharedPreferencesUtils.getInstance(context).loadObjectData(User.class);

		String money;
		String resultStr = Utils.jian(user.getBanlance(),payPrice);
		if(Double.parseDouble(resultStr) > 0){
		//if(false){
			money = "剩余："+ user.getBanlance();
			tv_qb_pay.setTextColor(context.getResources().getColor(R.color.black));
			isEnableMoney = true;
		}else{
			money = "剩余："+ user.getBanlance();
			tv_qb_pay.setTextColor(context.getResources().getColor(R.color.gray_99));
			isEnableMoney = false;
		}
		tv_qb_pay.setText("余额支付("+money+")");


		ic_close.setOnClickListener(this);
		iv_close_two.setOnClickListener(this);
		tv_forget_pwd.setOnClickListener(this);
		widgetListener();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ic_close:
				dismiss();
				if(callBack != null){
					callBack.onColse();
				}
				break;
			case R.id.rl_qb:
				if (isEnableMoney){
					payType = 1;
					changeLayout(0);
				}else{
					/*ToastUtil.showLongToast();
					ToastUtil.showToast(context,"余额不足");*/
					Toast.makeText(context,"余额不足",Toast.LENGTH_LONG).show();
				}


				break;
			case R.id.rl_wechat:
				//changeLayout(0);
				payType = 3;
				callBack.onSubmit(payType,null);

				break;
			case R.id.rl_alipay:
				//changeLayout(0);
				payType = 2;
				callBack.onSubmit(payType,null);

				break;
			case R.id.iv_close_two:
				changeLayout(1);
				break;
			case R.id.tv_forget_pwd:
				dismiss();
				IntentUtil.gotoActivity(context, PayPwdActivity.class);
				break;
		}
	}

	public interface CallBack {
		void onSubmit(int payType,String password);
		void onColse();
	}

	private void changeLayout(int type){
		if(type == 0){
			ll_pay_type.setVisibility(View.GONE);
			ll_pay_pwd.setVisibility(View.VISIBLE);
			showInput(et_psd);
		}else{
			ll_pay_type.setVisibility(View.VISIBLE);
			ll_pay_pwd.setVisibility(View.GONE);
			hideInput();
		}


	};

	public void showInput(final EditText et) {
		et.requestFocus();
		InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
	}

	protected void hideInput() {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
		View v = getWindow().peekDecorView();
		if (null != v) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}


	public void showError(String errorText){
		tv_error_msg.setVisibility(View.VISIBLE);
		tv_error_msg.setText(errorText);
		et_psd.setText("");
	}

	public void hideError(){
		tv_error_msg.setVisibility(View.GONE);
	}


	protected void widgetListener() {

		rl_qb.setOnClickListener(this);
		rl_wechat.setOnClickListener(this);
		rl_alipay.setOnClickListener(this);


		et_psd.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				 String psd = s.toString().trim();
				if(!TextUtils.isEmpty(psd) && psd.length() == 6){
					if(callBack != null){
						callBack.onSubmit(payType,psd.toString());
					}
				}

			}
		});
	}
}
