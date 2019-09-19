package com.lwc.common.module.login.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.configs.ServerConfig;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.ActivityBean;
import com.lwc.common.module.bean.Address;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Coupon;
import com.lwc.common.module.bean.Sheng;
import com.lwc.common.module.bean.Shi;
import com.lwc.common.module.bean.User;
import com.lwc.common.utils.CommonUtils;
import com.lwc.common.utils.Constants;
import com.lwc.common.utils.DataBaseManagerUtil;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.FileUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.KeyboardUtil;
import com.lwc.common.utils.ProgressUtils;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.SystemUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.LimitInputTextWatcher;
import com.lwc.common.widget.CustomDialog;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 注册界面
 */
public class RegisterActivity extends BaseActivity implements AMapLocationListener {

    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.edtCode)
    EditText edtCode;
    @BindView(R.id.btnCode)
    TextView btnCode;
    @BindView(R.id.del_phone)
    ImageView del_phone;
    @BindView(R.id.tv_select)
    TextView tv_select;
    @BindView(R.id.edtInvite)
    EditText edtInvite;
    @BindView(R.id.ll_invite)
    LinearLayout ll_invite;
    @BindView(R.id.tv_bb)
    TextView tv_bb;
    private int count = 60;
    private SharedPreferencesUtils preferencesUtils;
    private boolean isShowPwd = false, isUserAgreement = false;
    private String userType = "5", nickname;
    private User user;
    private ProgressUtils progressUtils;

    private ArrayList<Shi> shis;
    private Shi selectedShi;
    private Sheng selectedSheng;
    private Double lat;
    private Double lon;

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private String address_province;
    private String address_city;

    public void onBack(View view) {
        onBackPressed();
    }

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_regist;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        progressUtils = new ProgressUtils();
        preferencesUtils = SharedPreferencesUtils.getInstance(this);
        tv_bb.setText("版本号:" + SystemUtil.getCurrentVersionName(this));
        //edtNickname.addTextChangedListener(new LimitInputTextWatcher(edtNickname));
        startLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("注册");
    }

    @Override
    public void init() {
    }

    @Override
    protected void initGetData() {
    }

    /**
     * 组件事件监听
     */
    @Override
    protected void widgetListener() {
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
    }

    /**
     * 开始定位
     */
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

                SharedPreferencesUtils.setParam(RegisterActivity.this,"address_province",address_province);
                SharedPreferencesUtils.setParam(RegisterActivity.this,"address_city",address_city);

                analysisPresentLocation();
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

    private void analysisPresentLocation(){
        String shi = FileUtil.readAssetsFile(this, "shi.json");
        shis = JsonUtil.parserGsonToArray(shi, new TypeToken<ArrayList<Shi>>() {});

   /*     address_province = (String) SharedPreferencesUtils.getParam(this,"address_province","");
        address_city = (String) SharedPreferencesUtils.getParam(this,"address_city","");*/

        if (!TextUtils.isEmpty(address_city) && shis != null) {
            address_province = address_province.replace("省","");
            address_city = address_city.replace("市","");
            for (int i = 0; i < shis.size(); i++) {
                if (shis.get(i).getName().equals(address_city)) {
                    selectedShi = shis.get(i);
                    selectedSheng = new Sheng();
                    selectedSheng.setDmId(selectedShi.getParentid());
                    selectedSheng.setName(address_province);
                    break;
                }
            }
        }
        Log.d("联网成功","获取了位置信息id address_province_id）"
                + selectedShi.getDmId() + ", address_city_id:"
                + selectedSheng.getDmId());
    }

    /**
     * 验证码倒计时
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
                        ToastUtil.showToast(RegisterActivity.this, "验证码发送成功，请继续填写信息");
                        break;
                    default:
                        ToastUtil.showLongToast(RegisterActivity.this, common.getInfo());
                        break;
                }
            }
            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showLongToast(RegisterActivity.this, msg);
            }
        });
    }

    /**
     * 页面点击事件监听方法
     * @param view 点击的view对象
     */
    @OnClick({R.id.btnCode, R.id.tv_select, R.id.btnRegister, R.id.tvLogin, R.id.del_phone, R.id.tv_user_agreement})
    public void onViewClicked(View view) {
        String phone = null;
        switch (view.getId()) {
            case R.id.tv_select:
                if (isUserAgreement) {
                    tv_select.setCompoundDrawables(Utils.getDrawable(this, R.drawable.weixuanzhong), null, null, null);
                } else {
                    tv_select.setCompoundDrawables(Utils.getDrawable(this, R.drawable.xuanzhong), null, null, null);
                }
                isUserAgreement = !isUserAgreement;
                break;
            case R.id.tv_user_agreement:
                Bundle bundle = new Bundle();
                bundle.putString("url", ServerConfig.DOMAIN.replace("https", "http")+"/main/h5/agreement.html");
                bundle.putString("title", "用户注册协议");
                IntentUtil.gotoActivity(this, InformationDetailsActivity.class, bundle);
            break;
            case R.id.del_phone:
                edtPhone.setText("");
                del_phone.setVisibility(View.GONE);
                break;
 /*           case R.id.show_pwd:
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
                break;*/
            case R.id.tvLogin:
                onBackPressed();
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
                phone = edtPhone.getText().toString().trim();
               // String pwd = edtPassword.getText().toString().trim();
                String code = edtCode.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showLongToast(this, "请输入手机号码");
                    return;
                }
                if (!CommonUtils.isPhoneNum(phone)) {
                    ToastUtil.showToast(this, "请输入正确的手机号码");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showLongToast(this, "请输入验证码");
                    return;
                }
               /* if (TextUtils.isEmpty(pwd)) {
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
                }*/
                if (!isUserAgreement) {
                    ToastUtil.showLongToast(this, "请仔细阅读《密修注册协议》并点击同意");
                    return;
                }
                if (Utils.isFastClick(2000, RequestValue.REGISTER2)) {
                    return;
                }
                register(null, phone, code);
                break;
        }
    }

    /**
     * 注册
     * @param pwd 密码
     * @param phone 手机号
     * @param code 验证码
     */
    public void register(final String pwd, final String phone, String code) {
        progressUtils.showCustomProgressDialog(this);
        Map<String, String> map = new HashMap<>();
        map.put("user_phone", phone);
       // map.put("user_password", Utils.md5Encode(pwd));
        map.put("code", code);
        map.put("userType", userType);
        if(selectedSheng != null){
            map.put("provinceId", selectedSheng.getDmId());
        }
       if(selectedShi != null){
           map.put("cityId",selectedShi.getDmId());
       }

       if(lon != null){
           map.put("lon",String.valueOf(lon));
       }

       if(lat != null){
           map.put("lat",String.valueOf(lat));
       }
        //map.put("user_realname", nickname);
        String inviteCode = edtInvite.getText().toString().trim();
        if (userType.equals("5") && !TextUtils.isEmpty(inviteCode)) {
            map.put("inviteCode", inviteCode);
        }
        HttpRequestUtils.httpRequest(this, "register", RequestValue.REGISTER2, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        SharedPreferencesUtils.getInstance(RegisterActivity.this).saveBooleanData("new_register" + phone, true);
                        SharedPreferencesUtils.getInstance(RegisterActivity.this).saveString("former_name", phone);
                        if(!TextUtils.isEmpty(pwd)){
                            SharedPreferencesUtils.getInstance(RegisterActivity.this).saveString("former_pwd", pwd);
                        }
                        if (response.contains("data")){
                            try{
                                JSONObject object = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
                                String msg =object.optString("msg");
                                final String users =object.optString("users");
                                if (!TextUtils.isEmpty(msg)) {
                                    DialogUtil.showUpdateAppDg(RegisterActivity.this, "温馨提示", "知道了", msg, new CustomDialog.OnClickListener() {

                                        @Override
                                        public void onClick(CustomDialog dialog, int id, Object object) {
                                            toMainActivity(users,phone);
                                        }
                                    });
                                }else{
                                    toMainActivity(users,phone);
                                }
                            } catch (Exception e){}
                        } else {
                            ToastUtil.showLongToast(RegisterActivity.this, "注册成功，快去登录吧！");
                            setResult(RESULT_OK);
                            finish();
                        }
                        break;
                    default:
                        ToastUtil.showLongToast(RegisterActivity.this, common.getInfo());
                }
                progressUtils.dismissCustomProgressDialog();
            }

            @Override
            public void returnException(Exception e, String msg) {
                progressUtils.dismissCustomProgressDialog();
                if (msg != null && !msg.equals("")){
                    ToastUtil.showLongToast(RegisterActivity.this, msg);
                }
            }
        });
    }


    private void toMainActivity(String users,String phone){
        if (MainActivity.activity != null) {
            MainActivity.activity.finish();
        }

        user = JsonUtil.parserGsonToObject(users, User.class);
        preferencesUtils.saveString("user_role", user.getRoleId());

        SharedPreferencesUtils.setParam(RegisterActivity.this, "isNew", user.getIsNew());
        // 构建该用户的专用数据库
        DataBaseManagerUtil.createDataBase(user.getUserId());
        // 构建该用户的专用文件目录
        FileUtil.createAllFile(user.getUserName());

        setJPushAlias(user.getUserId());

        KeyboardUtil.showInput(false, RegisterActivity.this);
        progressUtils.dismissCustomProgressDialog();
        preferencesUtils.saveString("token", user.getToken());
        preferencesUtils.saveObjectData(user);
        getUserAddress();
        getActivity(phone);

        IntentUtil.gotoActivityToTopAndFinish(RegisterActivity.this, MainActivity.class);

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
    private void getActivity(final String phone) {
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
                                    SharedPreferencesUtils.getInstance(RegisterActivity.this).saveString("registerActivityId" + phone, ab.getActivityId());
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
                //user.setPassword(edtPassword.getText().toString().trim() + "");
               // preferencesUtils.saveString("former_pwd", edtPassword.getText().toString().trim());
                preferencesUtils.saveObjectData(user);
                IntentUtil.gotoActivityToTopAndFinish(RegisterActivity.this, MainActivity.class);
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
