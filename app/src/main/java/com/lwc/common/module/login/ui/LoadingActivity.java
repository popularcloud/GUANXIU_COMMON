package com.lwc.common.module.login.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.activity.UserGuideActivity;
import com.lwc.common.configs.ServerConfig;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.setting.ui.SettingActivity;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.OSUtils;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.view.CountDownProgressView;
import com.lwc.common.view.ImageCycleView;
import com.wkp.runtimepermissions.callback.PermissionCallBack;
import com.wkp.runtimepermissions.util.RuntimePermissionUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 欢迎界面
 *
 * @author 何栋
 * @date 2017年11月20日
 * @Copyright: lwc
 */
public class LoadingActivity extends Activity {

  private SharedPreferencesUtils preferencesUtils = null;
  private User user = null;
  private ImageView imageView,iv_gz;
  private CountDownProgressView countdownProgressView;

  //记录是否跳过倒计时
  private boolean skip = false;

  private String token;
  private ImageCycleView ad_view;
  int hasPermissionCount = 0;
  int noPermissionCount = 0;
  private Dialog dialog;

  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_loading);

    imageView = (ImageView)findViewById(R.id.imageView);
    iv_gz = (ImageView)findViewById(R.id.iv_gz);
    ad_view = (ImageCycleView) findViewById(R.id.ad_view);
    countdownProgressView = (CountDownProgressView) findViewById(R.id.countdownProgressView);

    countdownProgressView.setProgressListener( new CountDownProgressView.OnProgressListener() {
      @Override
      public void onProgress(int progress) {
        if (skip){
          countdownProgressView.stop();
          return;
        }

          if (progress == 1) {
            gotoActivity();
          }
      }
    });

    countdownProgressView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        gotoActivity();
      }
    });

    applyPermission();

  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  public void applyPermission() {
    //权限检查，回调是权限申请结果
    RuntimePermissionUtil.checkPermissions(this, RuntimePermissionUtil.CAMERA, new PermissionCallBack() {
      @Override
      public void onCheckPermissionResult(boolean hasPermission) {
        if (hasPermission) {
          hasPermissionCount = hasPermissionCount + 1;
          Log.d("联网成功","通过权限"+hasPermissionCount);
        }else {
          //显示权限不具备的界面
          toSettingPage();
          noPermissionCount = noPermissionCount + 1;
          Log.d("联网成功","拒绝权限"+noPermissionCount);
        }

        onPermissionFinish();
      }
    });
    RuntimePermissionUtil.checkPermissions(this, RuntimePermissionUtil.LOCATION, new PermissionCallBack() {
      @Override
      public void onCheckPermissionResult(boolean hasPermission) {
        if (hasPermission) {
          hasPermissionCount = hasPermissionCount + 1;
          Log.d("联网成功","通过权限"+hasPermissionCount);
        }else {
          //显示权限不具备的界面
          toSettingPage();
          noPermissionCount = noPermissionCount + 1;
          Log.d("联网成功","拒绝权限"+noPermissionCount);
        }
        onPermissionFinish();
      }
    });
    RuntimePermissionUtil.checkPermissions(this, RuntimePermissionUtil.CONTACTS, new PermissionCallBack() {
      @Override
      public void onCheckPermissionResult(boolean hasPermission) {
        if (hasPermission) {
          hasPermissionCount = hasPermissionCount + 1;
          Log.d("联网成功","通过权限"+hasPermissionCount);
        }else {
          //显示权限不具备的界面
          toSettingPage();
          noPermissionCount = noPermissionCount + 1;
          Log.d("联网成功","拒绝权限"+noPermissionCount);
        }
        onPermissionFinish();
      }
    });
    RuntimePermissionUtil.checkPermissions(this, RuntimePermissionUtil.PHONE, new PermissionCallBack() {
      @Override
      public void onCheckPermissionResult(boolean hasPermission) {
        if (hasPermission) {
          hasPermissionCount = hasPermissionCount + 1;
          Log.d("联网成功","通过权限"+hasPermissionCount);
        }else {
          //显示权限不具备的界面
          toSettingPage();
          noPermissionCount = noPermissionCount + 1;
          Log.d("联网成功","拒绝权限"+noPermissionCount);
        }
        onPermissionFinish();
      }
    });
    RuntimePermissionUtil.checkPermissions(this, RuntimePermissionUtil.STORAGE, new PermissionCallBack() {
      @Override
      public void onCheckPermissionResult(boolean hasPermission) {
        if (hasPermission) {
          hasPermissionCount = hasPermissionCount + 1;
          Log.d("联网成功","通过权限"+hasPermissionCount);
        }else {
          //显示权限不具备的界面
          toSettingPage();
          noPermissionCount = noPermissionCount + 1;
          Log.d("联网成功","拒绝权限"+noPermissionCount);
        }
        onPermissionFinish();
      }
    });
  }

  private void toSettingPage(){
    ToastUtil.showLongToast(this,"检测到该应用部分权限未授予，将影响app的正常使用，请前往应用管理中授权");
  }
  /**
   * 跳转页面
   */
  private void gotoActivity() {
    skip = true;
    IntentUtil.gotoActivityToTopAndFinish(LoadingActivity.this, MainActivity.class);
  }

  @Override
  protected void onResume() {
    super.onResume();
   // init();

  }

  public void onPermissionFinish(){

    if(hasPermissionCount + noPermissionCount < 5){
      return;
    }

    preferencesUtils = SharedPreferencesUtils.getInstance(LoadingActivity.this);
    user = preferencesUtils.loadObjectData(User.class);
    token = preferencesUtils.loadString("token");

    //判断是否已启动过app
    boolean isFirstTime = preferencesUtils.loadBooleanData("isfirsttime1");
    if (!isFirstTime) {
      dialog = DialogUtil.dialog(LoadingActivity.this, "用户协议和隐私政策", "同意", "查看协议详情", "请你务必谨慎阅读，充分理解'用户协议'和'隐私政策'各条款;您可以点击查看协议详情了解更多信息，或者点击同意开始接受我们的服务？", new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          preferencesUtils.saveBooleanData("isfirsttime1", true);//表示已首次启动过
          Bundle bundle = new Bundle();
          bundle.putString("type","9");
          IntentUtil.gotoActivity(LoadingActivity.this, UserGuideActivity.class,bundle);
          dialog.dismiss();
        }
      }, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Bundle bundle = new Bundle();
          bundle.putString("url", ServerConfig.DOMAIN.replace("https", "http")+"/main/h5/agreement.html");
          bundle.putString("title", "用户注册协议");
          IntentUtil.gotoActivity(LoadingActivity.this, InformationDetailsActivity.class, bundle);
        }
      }, true);


      return;
    }else{
      getBoot();
    }

  }

  /**
   * 初始化数据
   */
  public void init() {

  }

  /**
   * 查询启动广告图
   */
  private void getBoot() {
    HttpRequestUtils.httpRequest(this, "advertising", RequestValue.GET_ADVERTISING+"?local=2&role=4", null, "GET", new HttpRequestUtils.ResponseListener() {
      @Override
      public void getResponseData(String response) {
        Common common = JsonUtil.parserGsonToObject(response, Common.class);
        switch (common.getStatus()) {
          case "1":
            try {
              ArrayList<ADInfo> infos = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<ADInfo>>() {});
              if(infos == null || infos.size() < 1){
                gotoActivity();
                return;
              }
              ADInfo info = infos.get(0);
              String imgUrls = info.getAdvertisingImageUrl();
              ArrayList<ADInfo> adInfos = new ArrayList<ADInfo>();
              if(!TextUtils.isEmpty(imgUrls)){
                String[] urls = imgUrls.split(",");
                for (String url: urls) {
                  ADInfo ads = new ADInfo(url);
                  adInfos.add(ads);
                }
                ad_view.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                ad_view.setImageResources(adInfos, mAdCycleViewListener);
                ad_view.startImageCycle();

                iv_gz.setVisibility(View.GONE);
                countdownProgressView.setVisibility(View.VISIBLE);
                countdownProgressView.start();
              }else{
                gotoActivity();
              }
            }catch (Exception ex){
              gotoActivity();
            }
            break;
          default:
            gotoActivity();
            break;
        }
      }
      @Override
      public void returnException(Exception e, String msg) {
        gotoActivity();
      }
    });
  }

  /**
   * 定义轮播图监听
   */
  private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
    @Override
    public void onImageClick(ADInfo info, int position, View imageView) {
    }

    @Override
    public void displayImage(String imageURL, final ImageView imageView) {
      ImageLoaderUtil.getInstance().displayFromNetD(LoadingActivity.this, imageURL, imageView);// 使用ImageLoader对图片进行加装！
    }
  };

  public void finish() {
    super.finish();
    //关闭窗体动画显示
    overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
