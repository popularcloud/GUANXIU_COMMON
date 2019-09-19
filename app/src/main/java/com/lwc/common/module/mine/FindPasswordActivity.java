package com.lwc.common.module.mine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.utils.CommonUtils;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.SystemUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 找回密码
 * @author 何栋
 * @date 2018-08-08
 */
public class FindPasswordActivity extends BaseActivity {


	@BindView(R.id.edtPhone)
	EditText edtPhone;
	@BindView(R.id.del_phone)
	ImageView del_phone;
	@BindView(R.id.show_pwd)
	ImageView show_pwd;
	@BindView(R.id.edtCode)
	EditText edtCode;
	@BindView(R.id.btnCode)
	Button btnCode;
	@BindView(R.id.edtPassword)
	EditText edtPassword;
	@BindView(R.id.btnRegister)
	Button btnRegister;
	@BindView(R.id.tv_bb)
	TextView tv_bb;
	private int count = 60;
	private boolean isShow = false;

	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_find_pwd;
	}

	@Override
	protected void findViews() {
		ButterKnife.bind(this);
	}

	@Override
	public void init() {
		setTitle("找回密码");
		tv_bb.setText("版本号:" + SystemUtil.getCurrentVersionName(this));
		edtPhone.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (edtPhone.getText().toString().trim().length() > 0) {
					del_phone.setVisibility(View.VISIBLE);
				} else {
					edtPhone.setSelection(0);
					del_phone.setVisibility(View.GONE);
				}
			}
		});

		edtPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (edtPassword.getText().toString().trim().length() > 0) {
					show_pwd.setVisibility(View.VISIBLE);
				} else {
					edtPassword.setSelection(0);
					show_pwd.setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	protected void initGetData() {
	}

	@Override
	protected void widgetListener() {
	}

	/**
	 * 获取验证码倒计时
	 */
	Handler handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (count == 0) {
				count = 60;
				btnCode.setEnabled(true);
				btnCode.setText("获取验证码");
				return;
			}
			btnCode.setText(count-- + "s");
			btnCode.setEnabled(false);
			handle.sendEmptyMessageDelayed(0, 1000);
		}
	};

	@OnClick({R.id.btnCode, R.id.btnRegister, R.id.tvLogin,R.id.del_phone,R.id.show_pwd})
	public void onViewClicked(View view) {
		String phone = null;
		switch (view.getId()) {
			case R.id.show_pwd:
				if(isShow){
					//edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					show_pwd.setImageResource(R.drawable.ic_pwd_look);
					isShow = false;
				}else{
					edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
					show_pwd.setImageResource(R.drawable.ic_pwd_close);
					isShow = true;
				}
				break;
			case R.id.del_phone:
				edtPhone.setText("");
				del_phone.setVisibility(View.GONE);
				break;
			case R.id.tvLogin:
				finish();
				break;
			case R.id.btnCode:
				phone = edtPhone.getText().toString().trim();
				if (TextUtils.isEmpty(phone)) {
					ToastUtil.showToast(this, "请输入手机号码");
					return;
				}
				if (!CommonUtils.isPhoneNum(phone)) {
					ToastUtil.showToast(this, "请输入正确的手机号码");
					return;
				}
				getCode(phone);
				break;
			case R.id.btnRegister:

				phone = edtPhone.getText().toString();
				String pwd = edtPassword.getText().toString();
				String code = edtCode.getText().toString();
				if (TextUtils.isEmpty(phone)) {
					ToastUtil.showLongToast(this, "请输入手机号码");
					return;
				}
				if (!CommonUtils.isPhoneNum(phone)) {
					ToastUtil.showLongToast(this, "请输入正确的手机号码");
					return;
				}
				if (TextUtils.isEmpty(code)) {
					ToastUtil.showLongToast(this, "请输入验证码");
					return;
				}
				if (code == null || code.length() < 4) {
					ToastUtil.showLongToast(this, "请输入正确的验证码");
					return;
				}
				if (TextUtils.isEmpty(pwd)) {
					ToastUtil.showLongToast(this, "请输入新密码");
					return;
				}
				if (pwd.length() < 6) {
					ToastUtil.showLongToast(this, "请输入6-16位长度的新密码!");
					return;
				}
				if (Utils.isFastClick(3000, RequestValue.BACK_PWD)) {
					return;
				}
				findPwd();
				break;
		}
	}

	/**
	 * 获取验证码
	 * @param phone
     */
	public void getCode(String phone) {
		HttpRequestUtils.httpRequest(this, "getCode", RequestValue.GET_CODE+phone, null, "GET", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String response) {
				Common common = JsonUtil.parserGsonToObject(response, Common.class);
				switch (common.getStatus()) {
					case "1":
						handle.sendEmptyMessageDelayed(0, 1000);
						ToastUtil.showToast(FindPasswordActivity.this, "获取成功，请继续填写信息");
						break;
					default:
						ToastUtil.showLongToast(FindPasswordActivity.this, common.getInfo());
						break;
				}
			}
			@Override
			public void returnException(Exception e, String msg) {
				ToastUtil.showLongToast(FindPasswordActivity.this, msg);
			}
		});
	}

	/**
	 * 找回密码
	 */
	private void findPwd() {
		Map<String, String> map = new HashMap<>();
		map.put("phone", edtPhone.getText().toString().trim());
		map.put("password", Utils.md5Encode(edtPassword.getText().toString().trim()));
		map.put("code", edtCode.getText().toString().trim());
		map.put("clientType", "person");
		HttpRequestUtils.httpRequest(this, "findPwd", RequestValue.BACK_PWD, map, "POST", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String response) {
				Common common = JsonUtil.parserGsonToObject(response, Common.class);
				switch (common.getStatus()) {

					case "1":
						SharedPreferencesUtils.getInstance(FindPasswordActivity.this).saveString("former_name",edtPhone.getText().toString().trim());
						SharedPreferencesUtils.getInstance(FindPasswordActivity.this).saveString("former_pwd","");
						ToastUtil.showLongToast(FindPasswordActivity.this, "找回成功，请使用新密码登录");
						overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
						finish();
						break;
					default:
						ToastUtil.showLongToast(FindPasswordActivity.this, common.getInfo());
						break;
				}
			}

			@Override
			public void returnException(Exception e, String msg) {
				if (msg != null && !msg.equals("")) {
					ToastUtil.showLongToast(FindPasswordActivity.this, msg);
				}
			}
		});
	}
}
