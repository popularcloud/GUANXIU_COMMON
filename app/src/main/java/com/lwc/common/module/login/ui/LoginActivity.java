package com.lwc.common.module.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.controler.global.GlobalValue;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.ActivityBean;
import com.lwc.common.module.bean.Address;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Coupon;
import com.lwc.common.module.bean.Malfunction;
import com.lwc.common.module.bean.Repairs;
import com.lwc.common.module.bean.RepairsCompany;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.mine.FindPasswordActivity;
import com.lwc.common.utils.CommonUtils;
import com.lwc.common.utils.Constants;
import com.lwc.common.utils.DataBaseManagerUtil;
import com.lwc.common.utils.FileUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.KeyboardUtil;
import com.lwc.common.utils.LogUtil;
import com.lwc.common.utils.ProgressUtils;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.SystemUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * @author 何栋
 * @version 1.0
 * @date 2017/7/21 11:50
 * @email 294663966@qq.com
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements AMapLocationListener {


    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.edtCode)
    EditText edtCode;
    @BindView(R.id.btnCode)
    TextView btnCode;
    @BindView(R.id.txtFindPassWord)
    TextView txtFindPassWord;
/*    @BindView(R.id.tv_zhdl)
    TextView tv_zhdl;
    @BindView(R.id.tv_dxdl)
    TextView tv_dxdl;*/
    @BindView(R.id.tvRegist)
    TextView btnRegist;
    @BindView(R.id.ll_regist)
    LinearLayout ll_regist;
    @BindView(R.id.ll_code)
    LinearLayout ll_code;
    @BindView(R.id.ll_pwd)
    LinearLayout ll_pwd;
    @BindView(R.id.txtChangeLogin)
    TextView txtChangeLogin;
    @BindView(R.id.del_phone)
    ImageView del_phone;
    @BindView(R.id.show_pwd)
    ImageView show_pwd;
    private ProgressUtils progressUtils;
    private SharedPreferencesUtils preferencesUtils;
    private User user;
    private int type = 2;
    private String phone;
    private String phoneStr;
    private UMShareAPI umShareAPI;
    private boolean isShow = false;

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private String address_province;
    private String address_city;
    private Double lat;
    private Double lon;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        preferencesUtils = SharedPreferencesUtils.getInstance(this);
        progressUtils = new ProgressUtils();
        preferencesUtils.saveString("token","");
        preferencesUtils.deleteAppointObjectData(User.class);
        startLocation();
        init();
    }


    private void startLocation(){

        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置返回地址信息，默认为true
        mLocationOption.setNeedAddress(true);
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //单次定位
        mLocationOption.setOnceLocation(true);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);

// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
      /*          aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息*/
                address_province = aMapLocation.getProvince().replace("省","");//省信息
                address_city = aMapLocation.getCity().replace("市","");//城市信息

                lat = aMapLocation.getLatitude();
                lon = aMapLocation.getLongitude();

                aMapLocation.getLatitude();

                SharedPreferencesUtils.setParam(LoginActivity.this,"address_province",address_province);
                SharedPreferencesUtils.setParam(LoginActivity.this,"address_city",address_city);

                Log.d("联网成功","获取了位置信息 address_province"
                        + address_province + ", address_city:"
                        + address_city);
                //  analysisPresentLocation();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }

        }
    }

    @Override
    public void init() {

        //设置短信登录优先
       // ll_regist.setVisibility(View.INVISIBLE);
        ll_pwd.setVisibility(View.GONE);
        ll_code.setVisibility(View.VISIBLE);
        txtFindPassWord.setVisibility(View.INVISIBLE);
        setTitle("短信登录");
        //tv_bb.setText("MX" + SystemUtil.getCurrentVersionName(this));
        String name = preferencesUtils.loadString("former_name");
        if (name != null) {
            edtPhone.setText(name);
        }

        String pwd = preferencesUtils.loadString("former_pwd");
        if (pwd != null) {
            edtPassword.setText(pwd);
        } else {
            edtPassword.setText("");
        }
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

        umShareAPI = UMShareAPI.get(this);
    }

    @Override
    protected void initGetData() {
    }

    @Override
    protected void widgetListener() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 检查输入数据是否正确
     * @return true 表示通过检查  false 表示有数据输入不正确
     */
    private boolean check() {
        if (TextUtils.isEmpty(edtPhone.getText().toString().trim())) {
            ToastUtil.showToast(this, "账号不能为空！");
            return false;
        }
        if (type == 1) {
            if (TextUtils.isEmpty(edtPassword.getText().toString().trim())) {
                ToastUtil.showToast(this, "密码不能为空！");
                return false;
            }
        } else {
            if (TextUtils.isEmpty(edtCode.getText().toString().trim())) {
                ToastUtil.showToast(this, "验证码不能为空！");
                return false;
            }
        }
        return true;
    }

    private int count = 60;
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

    /**
     * 设置极光推送的别名
     */
    private void setJPushAlias(String uid) {
        //以用户id作为别名
        JPushInterface.setAlias(this, Constants.JPUSH_ALIAS, uid);
    }

    @OnClick({R.id.btnCode, R.id.txtFindPassWord, R.id.tvRegist,R.id.btnSureLogin, R.id.del_phone,
            R.id.txtChangeLogin,R.id.iv_third_qq,R.id.iv_third_wechat,R.id.iv_third_weibo,R.id.show_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.del_phone:
                edtPhone.setText("");
                del_phone.setVisibility(View.GONE);
                break;
            case R.id.txtChangeLogin:

                if("密码登录".equals(txtChangeLogin.getText().toString())){
                    txtChangeLogin.setText("短信登录");
                    type = 1;
                   /* tv_zhdl.setBackgroundResource(R.drawable.button_login_select);
                    tv_dxdl.setBackgroundResource(R.drawable.button_login_select2);
                    tv_zhdl.setTextColor(getResources().getColor(R.color.blue_00aaf5));
                    tv_dxdl.setTextColor(getResources().getColor(R.color.white));*/
                   // ll_regist.setVisibility(View.VISIBLE);
                    ll_code.setVisibility(View.GONE);
                    ll_pwd.setVisibility(View.VISIBLE);
                    txtFindPassWord.setVisibility(View.VISIBLE);
                    setTitle("账号登录");
                }else{
                    txtChangeLogin.setText("密码登录");
                    type = 2;
              /*      tv_zhdl.setBackgroundResource(R.drawable.button_login_select2);
                    tv_dxdl.setBackgroundResource(R.drawable.button_login_select);
                    tv_dxdl.setTextColor(getResources().getColor(R.color.blue_00aaf5));
                    tv_zhdl.setTextColor(getResources().getColor(R.color.white));*/
                   // ll_regist.setVisibility(View.INVISIBLE);
                    ll_pwd.setVisibility(View.GONE);
                    ll_code.setVisibility(View.VISIBLE);
                    txtFindPassWord.setVisibility(View.INVISIBLE);
                    setTitle("短信登录");
                }

                break;
            case R.id.txtFindPassWord:
                IntentUtil.gotoActivity(this, FindPasswordActivity.class);
                break;
            case R.id.btnSureLogin:
                if (check()) {
                    if (Utils.isFastClick(3000, "x")) {
                        return;
                    }
                    login();
                }
                break;
            case R.id.tvRegist:
                IntentUtil.gotoActivityForResult(this, RegisterActivity.class, RESULT_FIRST_USER);
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
            case R.id.iv_third_qq:
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.QQ,authListener);
                break;
            case R.id.iv_third_wechat:
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN,authListener);
                break;
            case R.id.iv_third_weibo:
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.SINA,authListener);
                break;
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK == resultCode){
            if (RESULT_FIRST_USER == requestCode) {
                String name = preferencesUtils.loadString("former_name");
                edtPhone.setText(name);
                String pwd = preferencesUtils.loadString("former_pwd");
                edtPassword.setText(pwd);
                login();
            }else{
                UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /**
     * 获取验证码
     * @param phone 手机号
     */
    public void getCode(String phone) {
        HttpRequestUtils.httpRequest(this, "getCode", RequestValue.GET_CODE + phone, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        handle.sendEmptyMessageDelayed(0, 1000);
                        ToastUtil.showToast(LoginActivity.this, "验证码发送成功");
                        break;
                    default:
                        ToastUtil.showLongToast(LoginActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showLongToast(LoginActivity.this, msg);
            }
        });
    }
    /**
     * 登录
     */
    private void login() {
        progressUtils.showCustomProgressDialog(this);
        //清除本地数据
        DataSupport.deleteAll(Address.class);
        DataSupport.deleteAll(RepairsCompany.class);
        DataSupport.deleteAll(Malfunction.class);
        DataSupport.deleteAll(Repairs.class);

        Map<String, String> map = new HashMap<>();
        phoneStr = edtPhone.getText().toString().trim();
        map.put("phone", phoneStr);
        if (type == 1) {
            map.put("password", Utils.md5Encode(edtPassword.getText().toString().trim()));
        } else {
            map.put("code", edtCode.getText().toString().trim());
        }
        map.put("clientType", GlobalValue.CLIENT_TYPE);
        map.put("systemCode", SystemUtil.getSystemVersion());
        map.put("phoneType", SystemUtil.getDeviceBrand() + "_" + SystemUtil.getSystemModel());

        if(lon != null){
            map.put("lon",String.valueOf(lon));
        }

        if(lat != null){
            map.put("lat",String.valueOf(lat));
        }
        //手机系统版
        //手机型号
        String url = "";
        if (type == 1) {
            url = RequestValue.METHOD_NEW_USER_LOGIN;
        } else {
            url = RequestValue.METHOD_NEW_USER_CODE_LOGIN;
        }
        HttpRequestUtils.httpRequest(this, "login", url, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        analysisLogin(response);
                        break;
                    default:
                        ToastUtil.showLongToast(LoginActivity.this, common.getInfo());
                        progressUtils.dismissCustomProgressDialog();
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                progressUtils.dismissCustomProgressDialog();
                if (msg != null && !msg.equals("")) {
                    ToastUtil.showLongToast(LoginActivity.this, msg);
                }
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

        user = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), User.class);
        preferencesUtils.saveString("user_role", user.getRoleId());

        SharedPreferencesUtils.setParam(LoginActivity.this,"isNew", user.getIsNew());
        // 构建该用户的专用数据库
        DataBaseManagerUtil.createDataBase(user.getUserId());
        // 构建该用户的专用文件目录
        FileUtil.createAllFile(user.getUserName());
        preferencesUtils.saveString("former_name", user.getUserPhone());

        setJPushAlias(user.getUserId());

        KeyboardUtil.showInput(false, LoginActivity.this);
        progressUtils.dismissCustomProgressDialog();
        preferencesUtils.saveString("token", user.getToken());
      /*  if (type == 1) {
            getActivity();
            getUserAddress();
        } else {*/
                         /*   if (TextUtils.isEmpty(user.getUserPassword())) {
                                Bundle bundle = new Bundle();
                                bundle.putString("token", user.getToken());
                                IntentUtil.gotoActivityAndFinish(LoginActivity.this, NewUserActivity.class, bundle);
                            } else {*/
            preferencesUtils.saveObjectData(user);
            getUserAddress();
            getActivity();
            preferencesUtils.saveString("token", user.getToken());
            IntentUtil.gotoActivityToTopAndFinish(LoginActivity.this, MainActivity.class);
            //}
      //  }
        overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
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
                        boolean newRegister = preferencesUtils.loadBooleanData("new_register" + phoneStr);
                        if (newRegister && activityBeans != null && activityBeans.size() > 0) {
                            preferencesUtils.saveBooleanData("new_register" + phoneStr, false);
                            for (int i = 0; i < activityBeans.size(); i++) {
                                ActivityBean ab = activityBeans.get(i);
                                if (!ab.getRewardFashion().equals("2") && ab.getConditionIndex().replace("/", "").equals(RequestValue.REGISTER2.replace("/", ""))) {
                                    SharedPreferencesUtils.getInstance(LoginActivity.this).saveString("registerActivityId" + phoneStr, ab.getActivityId());
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
                user.setPassword(edtPassword.getText().toString().trim() + "");
                preferencesUtils.saveString("former_pwd", edtPassword.getText().toString().trim());
                preferencesUtils.saveObjectData(user);
                IntentUtil.gotoActivityToTopAndFinish(LoginActivity.this, MainActivity.class);
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
                        String data = JsonUtil.getGsonValueByKey(response, "data");
                        if(!TextUtils.isEmpty(data)){
                            List<Address> addresses = JsonUtil.parserGsonToArray(data, new TypeToken<ArrayList<Address>>() {
                            });
                            DataSupport.saveAll(addresses);
                        }else{
                            List<Address> addresses = new ArrayList<>();
                            DataSupport.deleteAll(Address.class);
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (MainActivity.activity != null) {
                finish();
            } else {
                IntentUtil.gotoActivityAndFinish(this, MainActivity.class);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 第三方登录
     * @param data
     * @param name
     */
    private void thirdLogin(final Map<String,String> data, String name){
        final Map<String,String> params = new HashMap<>();
        String uid = data.get("uid");

        switch (name){
            case "QQ":
                params.put("qqid",uid);
                params.put("loginTypeName","QQ");
                break;
            case "WEIXIN":
                params.put("openid",uid);
                params.put("loginTypeName","微信");
                break;
            case "SINA":
                params.put("weiboid",uid);
                params.put("loginTypeName","新浪");
                break;
        }

        params.put("clientType", GlobalValue.CLIENT_TYPE);
        params.put("systemCode", SystemUtil.getSystemVersion());
        params.put("phoneType", SystemUtil.getDeviceBrand() + "_" + SystemUtil.getSystemModel());

        HttpRequestUtils.httpRequest(this, "loginByThird", RequestValue.GET_USER_LOGINBYTHIRD, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        try {
                            JSONObject object = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
                            if(object.getBoolean("isBind") == false){
                                Bundle bundle = new Bundle();
                                params.put("name",data.get("name"));
                                params.put("iconurl",data.get("iconurl"));
                                switch (data.get("gender")){
                                    case "女":
                                        params.put("gender","0");
                                        break;
                                    case "男":
                                        params.put("gender","1");
                                        break;
                                    case "0":
                                        params.put("gender","1");
                                        break;
                                    case "1":
                                        params.put("gender","0");
                                        break;
                                }

                                bundle.putSerializable("params", (Serializable) params);
                                IntentUtil.gotoActivity(LoginActivity.this,BindPhoneActivity.class,bundle);
                            }else{
                                analysisLogin(response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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


    /**####################第三方登录的回调########################**/

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            LogUtil.out("third_login","开始获取登录信息---->"+platform.name());
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
           // ToastUtil.showToast(LoginActivity.this,platform.name() + "action"+action);
            LogUtil.out("third_login","获取登录信息成功----》"+platform.name() + "action"+action);
            for(String keys : data.keySet()){
                LogUtil.out("third_login","获取的信息："+keys + "===" + data.get(keys));
            }

            thirdLogin(data,platform.name());
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            ToastUtil.showToast(LoginActivity.this,"失败：" + t.getMessage());
            LogUtil.out("third_login","获取登录信息失败----》"+platform.name() + t.getMessage());
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            ToastUtil.showToast(LoginActivity.this,"取消了：");
            LogUtil.out("third_login","取消了"+platform.name() + action);
        }
    };

}
