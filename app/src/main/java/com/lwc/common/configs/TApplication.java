package com.lwc.common.configs;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.lwc.common.utils.LLog;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.wxapi.WXContants;
import com.taobao.sophix.SophixManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zhihu.matisse.internal.utils.Platform;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.io.IOException;
import java.io.InputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * 全局公用Application类
 *
 * @author CodeApe
 * @version 1.0
 * @date 2014年12月24日
 * @Copyright: Copyright (c) 2013 Shenzhen Tentinet Technology Co., Ltd. Inc. All rights reserved.
 */
public class TApplication extends LitePalApplication {

    /**
     * 全局上下文，可用于文本、图片、sp数据的资源加载，不可用于视图级别的创建和展示
     */
    public static Context context;

//    public static Location location;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 整个应用程序的初始入口函数
     * <p/>
     * 本方法内一般用来初始化程序的全局数据，或者做应用的长数据保存取回操作
     *
     * @version 1.0
     * @createTime 2014年12月24日, 下午2:12:17
     * @updateTime 2014年12月24日, 下午2:12:17
     * @createAuthor CodeApe
     * @updateAuthor CodeApe
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    @Override
    public void onCreate() {
        CrashReport.initCrashReport(getApplicationContext(), "78fb35bded", false);
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch();
//        if (isMainProcess()) {
//            OpenInstall.init(this);
//        }
        // 实例化全局调用的上下文
        context = this;
        super.onCreate();

        LitePal.initialize(this);

        initJPushEngin();

        initHttp();

        initUmeng();
    }

    private void initUmeng() {
        UMConfigure.init(this,"58c881f565b6d6452500238a","umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
        UMConfigure.setLogEnabled(true);


        //友盟分享
        // 微信 appid appsecret
        PlatformConfig.setWeixin(WXContants.APP_ID, "423021696389ddc36adcaba1c8b85b92");
        // 新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("4183168490","13c9bbe4adad913dca82210a582626d2","http://www.lsh-sd.com/download.jsp");
        //QQ空间
//       PlatformConfig.setQQZone("1106219367", "BfkvsTXzmKZxBzVo");
         PlatformConfig.setQQZone("1108183259", "saHmqXjWTRPSN8jl");
       //  UMShareAPI.get(this);
    }


    public void initHttp() {

        //        双向认证
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null,null, "qg2k9p79deag8");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        if (!ServerConfig.SERVER_API_URL.startsWith("https://" + hostname)) {
                            return false;
                        }
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 初始化极光推送引擎
     */
    private void initJPushEngin() {
        LLog.i("启动极光推送引擎");
        //极光推送
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
    }

//    public boolean isMainProcess() {
//        int pid = android.os.Process.myPid();
//        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
//            if (appProcess.pid == pid) {
//                return getApplicationInfo().packageName.equals(appProcess.processName);
//            }
//        }
//        return false;
//    }
}
