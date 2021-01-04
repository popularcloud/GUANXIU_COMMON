package com.lwc.common.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.fragment.InformationFragment;
import com.lwc.common.fragment.MainFragment;
import com.lwc.common.fragment.MineFragment;
import com.lwc.common.fragment.MyOrderFragment;
import com.lwc.common.fragment.NewMainFragment;
import com.lwc.common.module.BaseFragmentActivity;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.bean.ActivityBean;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Coupon;
import com.lwc.common.module.bean.HasMsg;
import com.lwc.common.module.bean.Update;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.common_adapter.FragmentsPagerAdapter;
import com.lwc.common.module.integral.activity.IntegralMainActivity;
import com.lwc.common.module.login.ui.LoginActivity;
import com.lwc.common.module.mine.SignInActivity;
import com.lwc.common.utils.ApkUtil;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.SpUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.utils.VersionUpdataUtil;
import com.lwc.common.widget.CouponDialog;
import com.lwc.common.widget.CustomDialog;
import com.lwc.common.widget.CustomViewPager;
import com.lwc.common.widget.FloatDragView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 程序入口,主页
 *
 * @author 何栋
 * @date 2017年11月20日
 * @Copyright: lwc
 */
public class MainActivity extends BaseFragmentActivity {

    @BindView(R.id.cViewPager)
    CustomViewPager cViewPager;
    @BindView(R.id.radio_home)
    RadioButton radioHome;
    @BindView(R.id.radio_order)
    RadioButton radioOrder;
    @BindView(R.id.radio_news)
    RadioButton radioNews;
    @BindView(R.id.radio_mine)
    RadioButton radioMine;
    @BindView(R.id.group_tab)
    RadioGroup groupTab;
    @BindView(R.id.my_content_view)
    RelativeLayout myContentView;
    @BindView(R.id.iv_red_dian)
    TextView iv_red_dian;
    @BindView(R.id.rl_show_new_user)
    RelativeLayout rl_show_new_user;
    @BindView(R.id.ib_show_new_user)
    ImageView ib_show_new_user;
    @BindView(R.id.iv_dialog_close)
    ImageView iv_dialog_close;

    /**
     * fragment相关
     */
    private HashMap<Integer, Fragment> fragmentHashMap;
    public static NewMainFragment mainFragment;
    private MineFragment mineFragment;
    private HashMap rButtonHashMap;

    /**
     * 用户信息相关
     */
    private SharedPreferencesUtils preferencesUtils;
    public static User user;

    //极光推送参数
    public static boolean isForeground = false;
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static final String KEY_UPDATE_USER = "update_user_info";
    public static final String MESSAGE_RECEIVED_ACTION = "com.lwc.common.MESSAGE_RECEIVED_ACTION";
    private MessageReceiver mMessageReceiver;

    /**
     * 获取当前实例
     */
    public static MainActivity activity;

    /**
     * 都是为了防止多次加载用户信息的 逻辑有点奇怪  没时间改了
     */
    private boolean isGetUserInfo = true;//过滤获取多次用户信息
    private boolean isRedShow = true; //是否显示了红包

    /**
     * 活动对象
     */
    private ActivityBean ab;

    /**
     * 优化券弹窗
     */
    private CouponDialog dialog;
    /**
     * 优惠券集合
     */
    private List<Coupon> listC = new ArrayList<>();

    /**
     * 签到图片按钮
     */
    private ImageView signView;


    /**
     * 广播接收器
     */
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                if (user != null) {
                    hasMessage();
                }
            } else if (intent.getAction().equals(KEY_UPDATE_USER)) {
                if (user != null) {
                    getUserInfor();
                    if (mainFragment != null) {
                       // mainFragment.getGb();
                    }
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_new);
        ButterKnife.bind(this);

        activity = this;

        initView();

        openDoubleClickToExit();

        addFragmenInList();
        addRadioButtonIdInList();
        bindViewPage(fragmentHashMap);
        cViewPager.setCurrentItem(0, false);
        radioHome.setChecked(true);
        registerMessageReceiver();
        //通知
        if (user != null) {
            boolean spValue1 = SpUtil.getSpUtil(this.getString(R.string.spkey_file_userinfo), Context.MODE_PRIVATE).getSPValue(this.getString(R.string.spkey_file_is_ring) + user.getUserId(), true);
            if (spValue1) {
                Utils.setNotification1(this);
            } else if (!spValue1) {
                Utils.setNotification4(this);
            }
        }

        signView = FloatDragView.addFloatDragView(this, myContentView, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.gotoLogin(user, MainActivity.this))
                    IntentUtil.gotoActivity(MainActivity.this, SignInActivity.class);
            }
        });

        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true).init();
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        filter.addAction(KEY_UPDATE_USER);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();

        startUptateAPP();

        if (isGetUserInfo && user != null) {
            isGetUserInfo = false;
            getUserInfor();
        }
        if (user != null)
            hasMessage();
    }


    /**
     * 查询活动列表
     */
    private void getMyActivity() {
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
                        if (activityBeans != null && activityBeans.size() > 0) {
                            DataSupport.saveAll(activityBeans);
                            for (int i = 0; i < activityBeans.size(); i++) {
                                if (activityBeans.get(i).getCoupons() != null && activityBeans.get(i).getCoupons().size() > 0) {
                                    DataSupport.saveAll(activityBeans.get(i).getCoupons());
                                }
                            }
                        }
                        findCoupen();
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

    public void findCoupen() {
        isRedShow = false;
        List<ActivityBean> list = DataSupport.findAll(ActivityBean.class);
        List<Coupon> listCoupon = DataSupport.findAll(Coupon.class);
        if (list != null && list.size() > 0 && listCoupon != null && listCoupon.size() > 0) {
            listC.clear();
            for (int i = 0; i < list.size(); i++) {
                ab = list.get(i);
                if (ab.getRewardFashion().equals("2") && (TextUtils.isEmpty(ab.getConditionIndex()) || ab.getConditionIndex().replace("/", "").equals(RequestValue.REGISTER2.replace("/", "")) || ab.getConditionIndex().equals("null"))) {
                    if (ab.getConditionIndex().replace("/", "").equals(RequestValue.REGISTER2.replace("/", ""))) {
                        if ("1".equals(SharedPreferencesUtils.getParam(MainActivity.this, "isNew", "0"))) {
                            SharedPreferencesUtils.setParam(MainActivity.this, "isNew", "0");
                            for (int j = 0; j < listCoupon.size(); j++) {
                                Coupon coupon = listCoupon.get(j);
                                if (coupon.getActivityId().trim().equals(ab.getActivityId().trim())) {
                                    listC.add(coupon);
                                }
                            }
                        }
                    } else {
                        for (int j = 0; j < listCoupon.size(); j++) {
                            Coupon coupon = listCoupon.get(j);
                            if (coupon.getActivityId().trim().equals(ab.getActivityId().trim())) {
                                listC.add(coupon);
                            }
                        }
                    }

                    break;
                }
            }
            if (listC != null && listC.size() > 0) {
                dialog = DialogUtil.dialogCoupon(MainActivity.this, ab.getActivityName(), listC, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        HttpRequestUtils.getCoupon(MainActivity.this, ab.getActivityId(), "1", dialog);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openRed();
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openRed();
                        dialog.dismiss();
                    }
                });
            } else {
                openRed();
            }
        } else {
            openRed();
        }
    }

    private void openRed() {
        String phone = SharedPreferencesUtils.getInstance(MainActivity.this).loadString("former_name");
        String activityId = SharedPreferencesUtils.getInstance(MainActivity.this).loadString("registerActivityId" + phone);
        if (!TextUtils.isEmpty(activityId)) {
            SharedPreferencesUtils.getInstance(MainActivity.this).saveString("registerActivityId" + phone, "");
            Bundle bundle = new Bundle();
            bundle.putSerializable("activityId", activityId);
            IntentUtil.gotoActivityForResult(MainActivity.this, RedPacketActivity.class, bundle, 10003);
            MainActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        }
    }

    /**
     * 获取个人信息
     */
    private void getUserInfor() {
        HttpRequestUtils.httpRequest(this, "getUserInfor", RequestValue.USER_INFO, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if (common != null) {
                    switch (common.getStatus()) {
                        case "1":
                            String userRole = preferencesUtils.loadString("user_role");
                            String id = "3";
                            if (!TextUtils.isEmpty(user.getRoleId())) {
                                id = user.getRoleId();
                            } else if (!TextUtils.isEmpty(userRole)) {
                                id = userRole;
                            }
                            user = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), User.class);
                            if (user != null && !TextUtils.isEmpty(id)) {
                                user.setRoleId(id);
                            }
                            if (user != null) {
                                preferencesUtils.saveObjectData(user);
                            }
                            //判断是否为新用户
                            //user.setIs_new("1");
                            if ("1".equals(SharedPreferencesUtils.getParam(MainActivity.this, "isNewOpen", "0"))) {
                                showNewUserDialog();
                            }
                            break;
                        default:
                            break;
                    }
                }
                if (isRedShow)
                    getMyActivity();
                //findCoupen();
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }


    /**
     * 往rButtonHashMap中添加 RadioButton Id
     */
    private void addRadioButtonIdInList() {
        rButtonHashMap = new HashMap<>();
        rButtonHashMap.put(0, radioHome);
        rButtonHashMap.put(1, radioNews);
        rButtonHashMap.put(2, radioOrder);
        rButtonHashMap.put(3, radioMine);
    }

    /**
     * 往fragmentHashMap中添加 Fragment
     */
    private void addFragmenInList() {
        mainFragment = new NewMainFragment();
        mineFragment = new MineFragment();
        fragmentHashMap = new HashMap<>();
        fragmentHashMap.put(0, mainFragment);
        fragmentHashMap.put(1, new InformationFragment());
        fragmentHashMap.put(2, new MyOrderFragment());
        fragmentHashMap.put(3, mineFragment);
    }

    private void bindViewPage(HashMap<Integer, Fragment> fragmentList) {
        //是否滑动
        cViewPager.setPagingEnabled(false);
        cViewPager.setOffscreenPageLimit(4);//最多缓存4个页面
        cViewPager.setAdapter(new FragmentsPagerAdapter(getSupportFragmentManager(), fragmentList));
        cViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                cViewPager.setChecked(rButtonHashMap, position);
                if (signView != null) {
                    if (position == 0) {
                        signView.setVisibility(View.VISIBLE);
                    } else {
                        signView.setVisibility(View.GONE);
                    }
                }
                startUptateAPP();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    int type = 0;

    @OnClick({R.id.radio_home, R.id.radio_order, R.id.radio_news, R.id.radio_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.radio_home:
                cViewPager.setCurrentItem(0, false);
                if (type != 0) {
                    ///mainFragment.getNewestOrder();
                }
                type = 0;
                break;
            case R.id.radio_news:
                cViewPager.setCurrentItem(1, false);
                if (type == 0)
                    // mainFragment.updateBar(true);
                    type = 2;
                break;
            case R.id.radio_order:
//                if (user == null) {
//                    mainFragment.setGone(true);
//                    cViewPager.setCurrentItem(1, false);
//                    radioHome.performClick();
//                    type = 0;
//                    IntentUtil.gotoActivity(this, LoginActivity.class);
//                    break;
//                }
                cViewPager.setCurrentItem(2, false);
                if (type == 0)
                    //mainFragment.updateBar(true);
                    type = 1;
                break;
            case R.id.radio_mine:
                cViewPager.setCurrentItem(3, false);
                if (type == 0)
                    // mainFragment.updateBar(true);
                    type = 3;
                break;
        }
    }

    /**
     * 启动更新APP功能
     */
    private void startUptateAPP() {
        getVersionInfor();
    }

    public static long getAppVersionCode(Context context) {
        long appVersionCode = 0;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                appVersionCode = packageInfo.versionCode;
            } else {
                appVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionCode;
    }

    /**
     * 获取升级接口版本信息
     */
    private void getVersionInfor() {
        Map<String, String> params = new HashMap<>();
        params.put("clientType", "person");
        HttpRequestUtils.httpRequest(this, "getVersionInfo", RequestValue.UPDATE_APP, params, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        Update update = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), Update.class);
                        if (update != null && !TextUtils.isEmpty(update.getVersionCode())) {
                            //  String versionCode = preferencesUtils.loadString("versionCode");
                            String versionCode = String.valueOf(getAppVersionCode(MainActivity.this));
                            /**
                             * 线上的版本号
                             */
                            int version = Integer.valueOf(update.getVersionCode());

                            if (versionCode != null && update.getVersionCode().equals(versionCode)) {
                                return;
                            }
                            final String akpPath = update.getUrl();
                            //需要更新
                            if (VersionUpdataUtil.isNeedUpdate(MainActivity.this, version) && update.getIsValid().equals("1")) {
                                if (mineFragment != null) {
                                    mineFragment.updateVersionCode();
                                }
                                startUpdateDialog(update, akpPath);
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


    private void startUpdateDialog(Update update, final String akpPath) {
        String isForce = update.getIsForce(); //是否强制更新
        if (isForce.equals("1")) {
            DialogUtil.showUpdateAppDg(MainActivity.this, "版本更新", "立即更新", update.getMessage(), new CustomDialog.OnClickListener() {

                @Override
                public void onClick(CustomDialog dialog, int id, Object object) {
                    checkpInstallPrmission(akpPath, dialog);
                }
            });
        } else {
            DialogUtil.showMessageUp(MainActivity.this, "版本更新", "立即更新", "稍后再说", update.getMessage(), new CustomDialog.OnClickListener() {
                @Override
                public void onClick(CustomDialog dialog, int id, Object object) {
                    checkpInstallPrmission(akpPath, dialog);
                }
            }, null);
        }
    }

    /**
     * 检查是否有安装权限
     */
    private void checkpInstallPrmission(final String apkPath, CustomDialog updateDialog) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (getPackageManager().canRequestPackageInstalls()) {
                ApkUtil.downloadAPK(MainActivity.this, apkPath);
                updateDialog.dismiss();
            } else {
                updateDialog.dismiss();
                DialogUtil.showMessageUp(MainActivity.this, "授予安装权限", "立即设置", "取消", "检测到您没有授予安装应用的权限，请在设置页面授予", new CustomDialog.OnClickListener() {
                    @Override
                    public void onClick(CustomDialog dialog, int id, Object object) {
                        Uri uri = Uri.parse("package:" + getPackageName());
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, uri);
                        startActivityForResult(intent, 19900);
                        dialog.dismiss();
                    }
                }, null);
            }
        } else {
            ApkUtil.downloadAPK(MainActivity.this, apkPath);
            updateDialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1991 && resultCode == RESULT_OK) {
            outLogin();
            preferencesUtils.saveString("token", "");
//                        setJPushAlias(user.getUid());
            preferencesUtils.deleteAppointObjectData(User.class);
            preferencesUtils.saveString("former_pwd", "");
            IntentUtil.gotoActivity(this, LoginActivity.class);
            onBackPressed();
        } else if (requestCode == 8869 && resultCode == RESULT_OK) {
            String companyName = data.getStringExtra("companyName");
            DialogUtil.dialogBind(this, companyName, "", "", null);
        } else if (requestCode == 1520 && resultCode == RESULT_OK) {
            // SharedPreferencesUtils.setParam(PayActivity.this,"get_integral", jsonObj.getJSONObject("integral"));
            String getIntegral = (String) SharedPreferencesUtils.getParam(MainActivity.this, "get_integral", "0");
            DialogUtil.dialog(MainActivity.this, "温馨提示", "前往兑换", "下次再说", "支付成功,本次获得" + Utils.chu(getIntegral, "100") + "积分,可前往积分商城兑换礼品",
                    new View.OnClickListener() { // sure
                        @Override
                        public void onClick(View v) {
                            IntentUtil.gotoActivity(MainActivity.this, IntegralMainActivity.class);
                        }
                    }, null, true);
        } else if (requestCode == 19900 && resultCode == RESULT_OK) { //设置成功返回

        }
    }

    @Override
    public void initView() {
        preferencesUtils = SharedPreferencesUtils.getInstance(MainActivity.this);
        user = preferencesUtils.loadObjectData(User.class);
    }

    /**
     * 新用户首次登录
     */
    private void showNewUserDialog() {
        HttpRequestUtils.httpRequest(this, "advertising", RequestValue.GET_ADVERTISING + "?local=4", null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        ArrayList<ADInfo> infos = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<ADInfo>>() {
                        });
                        if (infos != null && infos.size() > 0) {
                            String myUrl = infos.get(0).getAdvertisingImageUrl();
                            ImageLoaderUtil.getInstance().displayFromNetD(MainActivity.this, myUrl, ib_show_new_user);
                            rl_show_new_user.setVisibility(View.VISIBLE);
                            iv_dialog_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rl_show_new_user.setVisibility(View.GONE);
                                }
                            });
                            rl_show_new_user.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rl_show_new_user.setVisibility(View.GONE);
                                }
                            });
                            user.setIsNew("0");
                            SharedPreferencesUtils.setParam(MainActivity.this, "isNewOpen", "0");
                        }
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
            }
        });


    }

    @Override
    public void initEngines() {
    }

    @Override
    public void getIntentData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMessageReceiver != null){
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        }
        // 注销服务
//        stopService(mServiceIntent);
    }

    private void outLogin() {
        HttpRequestUtils.httpRequest(this, "outLogin", RequestValue.EXIT, null, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
            }

            @Override
            public void returnException(Exception e, String msg) {
            }
        });
    }

    public void hasMessage() {
        HttpRequestUtils.httpRequest(this, "hasMessage", RequestValue.HAS_MESSAGE, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if (common.getStatus().equals("1")) {
                    DataSupport.deleteAll(HasMsg.class);
                    ArrayList<HasMsg> current = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<HasMsg>>() {
                    });
                    if (current != null && current.size() > 0) {
                        DataSupport.saveAll(current);
                        for (int i = 0; i < current.size(); i++) {
                            HasMsg hasMsg = current.get(i);
                            if ("0".equals(hasMsg.getType())) {
                                if (hasMsg.isHasMessage()) {
                                    iv_red_dian.setVisibility(View.VISIBLE);
                                    if (hasMsg.getCount() > 99) {
                                        iv_red_dian.setText("...");
                                    } else {
                                        iv_red_dian.setText(String.valueOf(hasMsg.getCount()));
                                    }
                                    if (mineFragment != null) {
                                        mineFragment.updateNewMsg(hasMsg);
                                    }

                                } else {
                                    iv_red_dian.setVisibility(View.GONE);
                                    if (mineFragment != null) {
                                        mineFragment.updateNewMsg(null);
                                    }
                                }
                                break;
                            }
                        }
                    }
                } else {
                    if (mineFragment != null) {
                        mineFragment.updateNewMsg(null);
                    }
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                if (mineFragment != null) {
                    mineFragment.updateNewMsg(null);
                }
            }
        });
    }
}
