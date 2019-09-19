package com.lwc.common.module.mine;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.WebViewLoadHtmlActivity;
import com.lwc.common.bean.CodeUrlBean;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Sheng;
import com.lwc.common.module.bean.Shi;
import com.lwc.common.utils.FileUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.ZXingUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class MyInvitationCodeActivity extends BaseActivity implements AMapLocationListener{

    @BindView(R.id.iv_bg)
    ImageView imageView;
    @BindView(R.id.iv_code)
    ImageView iv_code;
    @BindView(R.id.tv_save)
    TextView tv_save;
    @BindView(R.id.tv_share)
    TextView tv_share;
    @BindView(R.id.tv_desc)
    TextView tv_desc;
    @BindView(R.id.rl_screenshot)
    RelativeLayout rl_screenshot;
    private ArrayList<Sheng> shengs;
    private ArrayList<Shi> shis;
    private Shi selectedShi;
    private Sheng selectedSheng;

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private String address_province;
    private String address_city;
    private CodeUrlBean codeUrlBean;
    //  private String filePath;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_my_invitation_code;
    }

    @Override
    protected void findViews() {
        setTitle("我的邀请码");

        // 获取内置SD卡路径
        String sdCardPath = Environment.getExternalStorageDirectory().getPath();
        // 图片文件路径
      /*  try {
           // filePath = Environment.getExternalStorageDirectory().getCanonicalPath() +File.separator+ "mixiu"+File.separator+"screenshot.jpg";
        } catch (IOException e) {
            e.printStackTrace();
        }*/

      /*  setRight(R.drawable.ic_code_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    savePic(3);
            }
        });*/
        setRight("活动规则","#fd4154" ,new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(codeUrlBean.getDescription())){
                    Bundle bundle = new Bundle();
                    bundle.putString("title","活动规则");
                    bundle.putString("content",codeUrlBean.getDescription());
                    IntentUtil.gotoActivity(MyInvitationCodeActivity.this, WebViewLoadHtmlActivity.class,bundle);
                }
            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePic(1);
            }
        });

        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePic(3);
            }
        });

    }

    @Override
    protected void init() {

    }

    private Handler mHandler = new Handler();

    protected void savePic(final int type) {

        tv_save.setVisibility(View.GONE);
        tv_share.setVisibility(View.GONE);

        if (rl_screenshot == null) {
            return;
        }
        rl_screenshot.setDrawingCacheEnabled(true);
        rl_screenshot.buildDrawingCache();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                final Bitmap bmp = rl_screenshot.getDrawingCache(); // 获取图片
                if (bmp != null) {
                    saveImageToGallery(MyInvitationCodeActivity.this, bmp, type);
                } else {
                    ToastUtil.showToast(MyInvitationCodeActivity.this, "图片保存失败！");
                }
                rl_screenshot.destroyDrawingCache(); // 保存过后释放资源
            }
        },1000);
    }

    public void saveImageToGallery(Context context, Bitmap bmp, int type) {
        try {
            String dirName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+ File.separator + "mixiu";
            // 首先保存图片
            File appDir = new File(Environment.getExternalStorageDirectory().getCanonicalPath());
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            long mImageTime = System.currentTimeMillis();
            long dateSeconds = mImageTime / 1000;

            String fileName = mImageTime+"_screenshot.jpg";
            File file = new File(appDir, fileName);

            if (file.exists()) {
                file.delete();
            }



            int mImageWidth = bmp.getWidth();
            int mImageHeight = bmp.getHeight();

            // 保存截屏到系统MediaStore
            ContentValues values = new ContentValues();
            ContentResolver resolver = getContentResolver();
            values.put(MediaStore.Images.ImageColumns.DATA, file.getPath());
            values.put(MediaStore.Images.ImageColumns.TITLE, fileName);
            values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, mImageTime);
            values.put(MediaStore.Images.ImageColumns.DATE_ADDED, dateSeconds);
            values.put(MediaStore.Images.ImageColumns.DATE_MODIFIED, dateSeconds);
            values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.ImageColumns.WIDTH, mImageWidth);
            values.put(MediaStore.Images.ImageColumns.HEIGHT, mImageHeight);
            Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);

            OutputStream out = resolver.openOutputStream(uri);
            bmp.compress(Bitmap.CompressFormat.JPEG, 60, out);// bitmap转换成输出流，写入文件
            out.flush();
            out.close();

            // update file size in the database
            values.clear();
            values.put(MediaStore.Images.ImageColumns.SIZE,
                    new File(file.getPath()).length());
            resolver.update(uri, values, null, null);


       /*     resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);//保存图片信息到系统MediaStore*/

            tv_save.setVisibility(View.VISIBLE);
            tv_share.setVisibility(View.VISIBLE);
            if(type == 3){
                Bundle bundle = new Bundle();
                bundle.putString("sharePic", file.getPath());
                IntentUtil.gotoActivity(MyInvitationCodeActivity.this, SimpleShareActivity.class, bundle);
            }else{
                ToastUtil.showToast(MyInvitationCodeActivity.this, "图片保存成功！"+file.getPath());
            }

    }catch (Exception e) {
            ToastUtil.showToast(MyInvitationCodeActivity.this, e.getMessage());
     }
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
        String sheng = FileUtil.readAssetsFile(this, "sheng.json");
        String shi = FileUtil.readAssetsFile(this, "shi.json");
        shengs = JsonUtil.parserGsonToArray(sheng, new TypeToken<ArrayList<Sheng>>() {});
        shis = JsonUtil.parserGsonToArray(shi, new TypeToken<ArrayList<Shi>>() {});

        address_province = (String) SharedPreferencesUtils.getParam(this,"address_province","");
        address_city = (String) SharedPreferencesUtils.getParam(this,"address_city","");

                Log.d("联网成功","获取了位置信息省"
                + address_province + ", 市:"
                + address_city);

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
   /*     Log.d("联网成功","获取了位置信息id address_province_id）"
                + selectedShi.getDmId() + ", address_city_id:"
                + selectedSheng.getDmId());*/
        getDataFromNet();
    }

    @Override
    protected void initGetData() {
        analysisPresentLocation();
        //startLocation();
    }


    private void getDataFromNet(){
        Map<String,String> params = new HashMap<>();
        if(selectedSheng != null){
            params.put("province_id",selectedSheng.getDmId());
            params.put("province_name",selectedSheng.getName());
        }
        if(selectedShi != null){
            params.put("city_id",selectedShi.getDmId());
            params.put("city_name",selectedShi.getName());
        }
        HttpRequestUtils.httpRequest(this, "生成邀请二维码链接", RequestValue.GET_USER_ADDASSOCIATIONINFO, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        try {
                            codeUrlBean = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), CodeUrlBean.class);
                            if(codeUrlBean == null){
                                ToastUtil.showToast(MyInvitationCodeActivity.this,"生成二维码失败！");
                                return;
                            }
                            ImageLoaderUtil.getInstance().displayFromNetD(MyInvitationCodeActivity.this,codeUrlBean.getImg(),imageView);
                            ImageLoaderUtil.getInstance().displayFromNetD(MyInvitationCodeActivity.this,codeUrlBean.getQrcode(),iv_code);
                       /*     Bitmap bitmaps =  ZXingUtils.createQRImage(codeUrlBean.getUrl(),138,138);
                            iv_code.setImageBitmap(bitmaps);*/

                        /*    if(!TextUtils.isEmpty(codeUrlBean.getDescription())){
                                tv_desc.setText(codeUrlBean.getDescription());
                            }*/

                       /*     if(!TextUtils.isEmpty(codeUrlBean.getColor())){
                                int roundRadius = 10; // 8dp 圆角半径
                                GradientDrawable gd = new GradientDrawable();//创建drawable
                                gd.setColor(Color.parseColor(codeUrlBean.getColor()));
                                gd.setCornerRadius(roundRadius);
                                tv_save.setBackground(gd);
                            }*/


                        }catch (Exception ex){
                            ToastUtil.showToast(MyInvitationCodeActivity.this,"数据解析失败！");
                        }
                        break;
                    default:
                        ToastUtil.showToast(MyInvitationCodeActivity.this,"生成二维码失败！");
                        break;
                }
            }
            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showToast(MyInvitationCodeActivity.this,"生成二维码失败！");
            }
        });
    }

    @Override
    protected void widgetListener() {

    }
}
