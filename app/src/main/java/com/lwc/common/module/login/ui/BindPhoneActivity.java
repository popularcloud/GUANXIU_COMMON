package com.lwc.common.module.login.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.ActivityBean;
import com.lwc.common.module.bean.Address;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Coupon;
import com.lwc.common.module.bean.User;
import com.lwc.common.utils.Constants;
import com.lwc.common.utils.DataBaseManagerUtil;
import com.lwc.common.utils.FileUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.KeyboardUtil;
import com.lwc.common.utils.ProgressUtils;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.widget.CircleImageView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

public class BindPhoneActivity extends BaseActivity {

    @BindView(R.id.btn_sure)
    TextView btn_sure;
    @BindView(R.id.tv_get_code)
    TextView tv_get_code;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_desc)
    TextView tv_desc;
    @BindView(R.id.iv_header)
    CircleImageView iv_header;
    private HashMap<String, String> params;
    private SharedPreferencesUtils preferencesUtils;
    private String phone;
    private ProgressUtils progressUtils;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void init() {

        setTitle("绑定手机号");
        preferencesUtils = SharedPreferencesUtils.getInstance(this);
        progressUtils = new ProgressUtils();
    }

    @Override
    protected void initGetData() {
        params = (HashMap<String, String>) getIntent().getSerializableExtra("params");
        if(params != null){
            tv_name.setText(params.get("name"));
            tv_desc.setText("已关联您的"+params.get("loginTypeName")+"账号，请绑定您的手机号");
            ImageLoaderUtil.getInstance().displayFromNet(this,params.get("iconurl"),iv_header);
        }
    }

    @Override
    protected void widgetListener() {
        //绑定手机号
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = et_phone.getText().toString().trim();
                String code = et_code.getText().toString().trim();

                if(TextUtils.isEmpty(phone)){
                    ToastUtil.showToast(BindPhoneActivity.this,"请输入手机号！");
                    return;
                }
                if(TextUtils.isEmpty(code)){
                    ToastUtil.showToast(BindPhoneActivity.this,"请输入验证码！");
                    return;
                }

                if(params == null){
                    ToastUtil.showToast(BindPhoneActivity.this,"获取信息失败!请重新登录");
                    return;
                }
                params.put("phone",phone);
                params.put("code",code);

                bindPhone();
            }
        });

        tv_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = et_phone.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    ToastUtil.showToast(BindPhoneActivity.this,"请输入手机号！");
                    return;
                }
                getPhoneCode(phone);
            }
        });
    }

    private int count = 60;
    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (count == 0) {
                count = 60;
                tv_get_code.setEnabled(true);
                tv_get_code.setText("获取验证码");
                return;
            }
            tv_get_code.setText(count-- + "s");
            tv_get_code.setEnabled(false);
            handle.sendEmptyMessageDelayed(0, 1000);
        }
    };

    /**
     * 获取手机验证码
     * @param phone
     */
    private void getPhoneCode(String phone){
        HttpRequestUtils.httpRequest(this, "getCode", RequestValue.GET_CODE + phone, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        handle.sendEmptyMessageDelayed(0, 1000);
                        ToastUtil.showToast(BindPhoneActivity.this, "验证码发送成功");
                        break;
                    default:
                        ToastUtil.showLongToast(BindPhoneActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showLongToast(BindPhoneActivity.this, msg);
            }
        });
    }

    /**
     * 绑定手机号
     *
     */
    private void bindPhone(){
        HttpRequestUtils.httpRequest(this, "bindByThird", RequestValue.GET_USER_BINDBYTHIRD, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        analysisLogin(response);
                        break;
                    default:
                        ToastUtil.showLongToast(BindPhoneActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showLongToast(BindPhoneActivity.this, msg);
            }
        });
    }

    /**
     * 解析登录数据
     * @param response
     */
    private void analysisLogin(String response) {
        if (MainActivity.activity != null) {
            MainActivity.activity.finish();
        }

        User user = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), User.class);
        preferencesUtils.saveString("user_role", user.getRoleId());

        SharedPreferencesUtils.setParam(this,"isNew", user.getIsNew());
        // 构建该用户的专用数据库
        DataBaseManagerUtil.createDataBase(user.getUserId());
        // 构建该用户的专用文件目录
        FileUtil.createAllFile(user.getUserName());
        preferencesUtils.saveString("former_name", phone);

        setJPushAlias(user.getUserId());

        KeyboardUtil.showInput(false, this);
        progressUtils.dismissCustomProgressDialog();
        preferencesUtils.saveString("token", user.getToken());

        preferencesUtils.saveObjectData(user);
        getUserAddress();
        getActivity();
        preferencesUtils.saveString("token", user.getToken());
        IntentUtil.gotoActivityToTopAndFinish(BindPhoneActivity.this, MainActivity.class);

        overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    /**
     * 设置极光推送的别名
     */
    private void setJPushAlias(String uid) {
        //以用户id作为别名
        JPushInterface.setAlias(this, Constants.JPUSH_ALIAS, uid);
    }

    /**
     * 查询活动
     */
    private void getActivity() {
        HttpRequestUtils.httpRequest(this, "getActivity", RequestValue.GET_CHECK_ACTIVITY, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        DataSupport.deleteAll(ActivityBean.class);
                        DataSupport.deleteAll(Coupon.class);
                        List<ActivityBean> activityBeans = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<ActivityBean>>() {
                        });
                        if (activityBeans != null && activityBeans.size() > 0)
                            DataSupport.saveAll(activityBeans);
                        boolean newRegister = preferencesUtils.loadBooleanData("new_register" + phone);
                        if (newRegister && activityBeans != null && activityBeans.size() > 0) {
                            preferencesUtils.saveBooleanData("new_register" + phone, false);
                            for (int i = 0; i < activityBeans.size(); i++) {
                                ActivityBean ab = activityBeans.get(i);
                                if (!ab.getRewardFashion().equals("2") && ab.getConditionIndex().replace("/", "").equals(RequestValue.REGISTER2.replace("/", ""))) {
                                    SharedPreferencesUtils.getInstance(BindPhoneActivity.this).saveString("registerActivityId" + phone, ab.getActivityId());
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
            }

            @Override
            public void returnException(Exception e, String msg) {
            }
        });
    }

    /**
     * 获取用户地址
     */
    private void getUserAddress() {
        HttpRequestUtils.httpRequest(this, "getUserAddress", RequestValue.GET_USER_ADDRESS, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        List<Address> addresses = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<Address>>() {
                        });
                        DataSupport.saveAll(addresses);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
            }
        });
    }
}
