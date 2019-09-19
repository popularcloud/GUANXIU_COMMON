package com.lwc.common.module.login.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.configs.ServerConfig;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.ActivityBean;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.User;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.ProgressUtils;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.LimitInputTextWatcher;
import com.lwc.common.widget.CustomDialog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 新用户完善资料
 */
public class NewUserActivity extends BaseActivity {

    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.edtNickname)
    EditText edtNickname;
    @BindView(R.id.show_pwd)
    ImageView show_pwd;
    @BindView(R.id.tv_government)
    TextView tv_government;
    @BindView(R.id.tv_user)
    TextView tv_user;
    @BindView(R.id.tv_select)
    TextView tv_select;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.edtInvite)
    EditText edtInvite;
    @BindView(R.id.ll_invite)
    LinearLayout ll_invite;
    private ProgressUtils progressUtils;
    private boolean isShowPwd = false;
    private String userType = "5";
    private boolean isUserAgreement = false;
    private String nickname;
    private String token;
    private User user;
    private SharedPreferencesUtils preferencesUtils;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_new_user;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        setTitle("完善资料");
        img_back.setVisibility(View.GONE);
        token = getIntent().getStringExtra("token");
    }

    @Override
    public void init() {
    }

    @Override
    protected void initGetData() {
        progressUtils = new ProgressUtils();
        preferencesUtils = SharedPreferencesUtils.getInstance(this);
        edtNickname.addTextChangedListener(new LimitInputTextWatcher(edtNickname));
    }

    @Override
    protected void widgetListener() {
    }

    @OnClick({R.id.tv_select, R.id.btnRegister, R.id.show_pwd, R.id.tv_user, R.id.tv_government, R.id.tv_user_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_select:
                if (isUserAgreement) {
                    tv_select.setCompoundDrawables(Utils.getDrawable(this, R.drawable.invoice_order_noselected), null, null, null);
                } else {
                    tv_select.setCompoundDrawables(Utils.getDrawable(this, R.drawable.invoice_order_selected), null, null, null);
                }
                isUserAgreement = !isUserAgreement;
                break;
            case R.id.tv_user_agreement:
                Bundle bundle = new Bundle();
                bundle.putString("url", ServerConfig.DOMAIN.replace("https", "http")+"/main/h5/agreement.html");
                bundle.putString("title", "用户注册协议");
                IntentUtil.gotoActivity(this, InformationDetailsActivity.class, bundle);
            break;
            case R.id.tv_user:
                userType = "5";
                tv_user.setCompoundDrawables(Utils.getDrawable(this, R.drawable.registered_selected), null, null, null);
                tv_government.setCompoundDrawables(Utils.getDrawable(this, R.drawable.registered_unselected), null, null, null);
                ll_invite.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_government:
                userType = "3";
                tv_user.setCompoundDrawables(Utils.getDrawable(this, R.drawable.registered_unselected), null, null, null);
                tv_government.setCompoundDrawables(Utils.getDrawable(this, R.drawable.registered_selected), null, null, null);
                ll_invite.setVisibility(View.GONE);
                break;
            case R.id.show_pwd:
                if (isShowPwd) {
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edtPassword.setSelection(edtPassword.getText().toString().length());
                    isShowPwd = false;
                    show_pwd.setImageResource(R.drawable.registered_closeeye);
                } else {
                    isShowPwd = true;
                    show_pwd.setImageResource(R.drawable.registered_eye);
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edtPassword.setSelection(edtPassword.getText().toString().length());
                }
                break;
            case R.id.btnRegister:
                String pwd = edtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtil.showLongToast(this, "请输入密码");
                    return;
                }
                if (pwd.length() < 6) {
                    ToastUtil.showLongToast(this, "请输入6-16位长度新密码!");
                    return;
                }
                nickname = edtNickname.getText().toString().trim();
                if (TextUtils.isEmpty(nickname)) {
                    ToastUtil.showLongToast(this, "请输入姓名");
                    return;
                }
                if (!isUserAgreement) {
                    ToastUtil.showLongToast(this, "请仔细阅读《密修注册协议》并点击同意");
                    return;
                }
                if (Utils.isFastClick(3000, RequestValue.PERFECT_USER_INFO)) {
                    return;
                }
                register(pwd);
                break;
        }
    }

    /**
     * 完善资料
     * @param pwd 密码
     */
    public void register(final String pwd) {
        progressUtils.showCustomProgressDialog(this);
        Map<String, String> map = new HashMap<>();
        map.put("password", Utils.md5Encode(pwd));
        map.put("roleId", userType);
        map.put("nickName", nickname);
        String inviteCode = edtInvite.getText().toString().trim();
        if (userType.equals("5") && !TextUtils.isEmpty(inviteCode)) {
            map.put("inviteCode", inviteCode);
        }
        HttpRequestUtils.httpRequest(this, "完善资料", RequestValue.PERFECT_USER_INFO, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        user = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), User.class);
                        preferencesUtils.saveString("user_role", user.getRoleId());
                        preferencesUtils.saveString("token", token);
                        preferencesUtils.saveObjectData(user);
                        SharedPreferencesUtils.getInstance(NewUserActivity.this).saveString("former_pwd", pwd);
                        if (!TextUtils.isEmpty(user.getMsg())){
                            DialogUtil.showUpdateAppDg(NewUserActivity.this, "温馨提示", "知道了", user.getMsg(), new CustomDialog.OnClickListener() {

                                @Override
                                public void onClick(CustomDialog dialog, int id, Object object) {
                                    getActivity(user.getUserPhone());
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            getActivity(user.getUserPhone());
                        }
                        break;
                    default:
                        ToastUtil.showLongToast(NewUserActivity.this, common.getInfo());
                }
                progressUtils.dismissCustomProgressDialog();
            }

            @Override
            public void returnException(Exception e, String msg) {
                progressUtils.dismissCustomProgressDialog();
                if (msg != null && !msg.equals("")){
                    ToastUtil.showLongToast(NewUserActivity.this, msg);
                }
            }
        });
    }

    /**
     * 查询活动数据
     * @param phone 登录用户手机号
     */
    private void getActivity(final String phone) {
        HttpRequestUtils.httpRequest(this, "getActivity", RequestValue.GET_CHECK_ACTIVITY, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        DataSupport.deleteAll(ActivityBean.class);
                        List<ActivityBean> activityBeans = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<ActivityBean>>() {
                        });
                        if (activityBeans != null && activityBeans.size() > 0)
                            DataSupport.saveAll(activityBeans);
                        if (activityBeans != null && activityBeans.size() > 0) {
                            for (int i=0; i<activityBeans.size(); i++) {
                                ActivityBean ab = activityBeans.get(i);
                                if (!ab.getRewardFashion().equals("2") && ab.getConditionIndex().replace("/","").equals(RequestValue.REGISTER2.replace("/",""))) {
                                    SharedPreferencesUtils.getInstance(NewUserActivity.this).saveString("registerActivityId"+phone, ab.getActivityId());
                                }
                                if (ab.getCoupons() != null && ab.getCoupons().size() > 0) {
                                    DataSupport.saveAll(ab.getCoupons());
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
                IntentUtil.gotoActivityAndFinish(NewUserActivity.this, MainActivity.class);
            }
            @Override
            public void returnException(Exception e, String msg) {
                finish();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DialogUtil.showMessageDg(NewUserActivity.this, "温馨提示", "退出", "取消", "未完善资料，是无法报修的哦！是否确定退出app？", new CustomDialog.OnClickListener() {
                @Override
                public void onClick(CustomDialog dialog, int id, Object object) {
                    preferencesUtils.deleteAppointObjectData(User.class);
                    finish();
                    dialog.dismiss();
                }
            }, new CustomDialog.OnClickListener() {

                @Override
                public void onClick(CustomDialog dialog, int id, Object object) {
                    dialog.dismiss();
                }
            });
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
