package com.lwc.common.module.lease_parts.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.bean.PickerView;
import com.lwc.common.configs.ServerConfig;
import com.lwc.common.controler.global.GlobalValue;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.PhotoToken;
import com.lwc.common.module.bean.Sheng;
import com.lwc.common.module.bean.Shi;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.bean.Xian;
import com.lwc.common.utils.CommonUtils;
import com.lwc.common.utils.FileUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.KeyboardUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.PictureUtils;
import com.lwc.common.utils.QiniuUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.widget.RoundAngleImageView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.zhihu.matisse.Matisse;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 租赁认证
 *
 */
public class LeaseMsgAuthenticationActivity extends BaseActivity {

    @BindView(R.id.iv_id_back)
    RoundAngleImageView iv_id_back;
    @BindView(R.id.iv_id_positive)
    RoundAngleImageView iv_id_positive;
    @BindView(R.id.iv_business_license)
    RoundAngleImageView iv_business_license;
    @BindView(R.id.iv_leaseContract)
    RoundAngleImageView iv_leaseContract;
    @BindView(R.id.cb_enterprise)
    CheckBox cb_enterprise;
    @BindView(R.id.cb_personal)
    CheckBox cb_personal;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_getCode_01)
    TextView tv_getCode_01;
    @BindView(R.id.tv_getCode_02)
    TextView tv_getCode_02;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.btnNext)
    TextView btnNext;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_idCard)
    EditText et_idCard;
    @BindView(R.id.et_addressDetail)
    EditText et_addressDetail;
    @BindView(R.id.et_creditCode)
    EditText et_creditCode;
    @BindView(R.id.et_company_name)
    EditText et_company_name;
    @BindView(R.id.et_code_01)
    EditText et_code_01;
    @BindView(R.id.et_code_02)
    EditText et_code_02;
    @BindView(R.id.tv_name_txt)
    TextView tv_name_txt;

    @BindView(R.id.ll_code_02)
    LinearLayout ll_code_02;
    @BindView(R.id.ll_code_01)
    LinearLayout ll_code_01;

    @BindView(R.id.ll_address)
    LinearLayout ll_address;
    @BindView(R.id.ll_detail_address)
    LinearLayout ll_detail_address;
    @BindView(R.id.ll_businessCode)
    LinearLayout ll_businessCode;
    @BindView(R.id.ll_businessCode_img)
    LinearLayout ll_businessCode_img;
    @BindView(R.id.ll_field_img)
    LinearLayout ll_field_img;
    @BindView(R.id.ll_company)
    LinearLayout ll_company;
    @BindView(R.id.tv_select)
    TextView tv_select;


    private String idPositivePath;
    private String idBackPath;
    private String businessLicensePath;
    private String leaseContractPath;

    private String maintenance_sex;

    private ImageView presentImgView;

    private String phone;
    private File cutfile;

    private int AUTHENTICATIONTYPE = 0; //0企业  1.个人
    private boolean isUserAgreement = false;

    private PhotoToken token;
    private ProgressDialog pd;
    private String imgPath1;
    private List<String> urlStrs = new ArrayList();
    private HashMap<String, String> params;

    private ArrayList<PickerView> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private List<Sheng> shengs = new ArrayList<>();
    private List<Shi> shis = new ArrayList<>();
    private List<Xian> xians = new ArrayList<>();
    //排序后的城市
    private List<List<Shi>> sortShi = new ArrayList<>();
    //排序后的县
    private List<List<List<Xian>>> sortXian = new ArrayList<>();
    /**
     * 选中的省
     */
    private Sheng selectedSheng;
    /**
     * 选中的市
     */
    private Shi selectedShi;
    /**
     * 选中的县
     */
    private Xian selectedXian;
    private User user;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_lease_msg_authentication;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void init() {
        setTitle("实名认证");
        pd = new ProgressDialog(this);
        pd.setMessage("正在上传图片...");
        pd.setCancelable(false);

        getAreaData();
        myThread.start();
    }

    @OnClick({R.id.iv_id_positive,R.id.iv_id_back,R.id.tv_address,R.id.iv_business_license,
            R.id.tv_getCode_01,R.id.tv_getCode_02,R.id.btnNext,R.id.iv_leaseContract,R.id.tv_select,R.id.tv_user_agreement})
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.iv_id_positive:
                ToastUtil.showPhotoSelect(this, 1);
                presentImgView = iv_id_positive;
                break;
            case R.id.iv_id_back:
                ToastUtil.showPhotoSelect(this, 1);
                presentImgView = iv_id_back;
                break;
            case R.id.iv_business_license:
                ToastUtil.showPhotoSelect(this, 1);
                presentImgView = iv_business_license;
                break;
            case R.id.iv_leaseContract:
                ToastUtil.showPhotoSelect(this, 1);
                presentImgView = iv_leaseContract;
                break;
            case R.id.tv_address:
                showPickerView();
                break;
            case R.id.btnNext: //下一步
                checkedParams();
                break;
            case R.id.tv_getCode_01:
                phone = et_phone.getText().toString().trim();
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
            case R.id.tv_getCode_02:
                phone = et_phone.getText().toString().trim();
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
                bundle.putString("url", ServerConfig.DOMAIN.replace("https", "http")+"/main/h5/agreement.html?type=1");
                bundle.putString("title", "密修租赁协议");
                IntentUtil.gotoActivity(this, InformationDetailsActivity.class, bundle);
                break;
        }
    }

    private Thread myThread = new Thread() {
        @Override
        public void run() {
            super.run();
            loadOptionsPickerViewData();
        }
    };

    private void showPickerView() {
        KeyboardUtil.showInput(false, this);
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                selectedSheng = shengs.get(options1);
                selectedShi = sortShi.get(options1).get(options2);
                selectedXian = sortXian.get(options1).get(options2).get(options3);
                String tx = selectedSheng.getName() + "-" +
                        selectedShi.getName() + "-" +
                        selectedXian.getName();
                tv_address.setText(tx);
            }
        })

                .setTitleText("选择公司/店铺地址")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    /**
     * 获取地区数据
     */
    private void getAreaData() {
        String sheng = FileUtil.readAssetsFile(this, "sheng.json");
        String shi = FileUtil.readAssetsFile(this, "shi.json");
        String xian = FileUtil.readAssetsFile(this, "xian.json");
        shengs = JsonUtil.parserGsonToArray(sheng, new TypeToken<ArrayList<Sheng>>() {
        });
        shis = JsonUtil.parserGsonToArray(shi, new TypeToken<ArrayList<Shi>>() {
        });

        String address_province = (String) SharedPreferencesUtils.getParam(this,"address_province","广东");
        String address_city = (String) SharedPreferencesUtils.getParam(this,"address_city","东莞");

        if (!TextUtils.isEmpty(address_city) && shis != null) {
            for (int i = 0; i < shis.size(); i++) {
                if (shis.get(i).getName().equals(address_city)) {
                    selectedShi = shis.get(i);
                    shis.remove(selectedShi);
                    break;
                }
            }
            if (selectedShi != null) {
                for (int i = 0; i < shengs.size(); i++) {
                    if (shengs.get(i).getDmId().equals(selectedShi.getParentid())) {
                        selectedSheng = shengs.get(i);
                        shengs.remove(selectedSheng);
                        break;
                    }
                }
                if (selectedSheng != null) {
                    //shengs.clear();
                    shengs.add(0,selectedSheng);
                    // shis.clear();
                    shis.add(0,selectedShi);
                }
            }
        }
        xians = JsonUtil.parserGsonToArray(xian, new TypeToken<ArrayList<Xian>>() {
        });
    }

    /**
     * 加载地区弹框数据
     */
    private void loadOptionsPickerViewData() {
        //添加省
        for (int i = 0; i < shengs.size(); i++) {
            Sheng sheng = shengs.get(i);
            options1Items.add(new PickerView(i, sheng.getName()));
        }
//        //添加市
        for (int j = 0; j < shengs.size(); j++) {
            Sheng sheng = shengs.get(j);
            ArrayList<String> options2Items_01 = new ArrayList<>();
            List<Shi> tempSortShi = new ArrayList<>();
            for (int z = 0; z < shis.size(); z++) {
                Shi shi = shis.get(z);
                if (sheng.getDmId().equals(shi.getParentid())) {
                    options2Items_01.add(shi.getName());
                    tempSortShi.add(shi);
                }
            }
            options2Items.add(options2Items_01);
            sortShi.add(tempSortShi);

        }
        //添加县
        for (int p = 0; p < shengs.size(); p++) { //遍历省级
            ArrayList<ArrayList<String>> provinceAreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            List<List<Xian>> tempProvince = new ArrayList<>();
            Sheng sheng = shengs.get(p);
            for (int s = 0; s < shis.size(); s++) {  //省级下的市
                ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
                List<Xian> tempXian = new ArrayList<>();
                Shi shi = shis.get(s);
                if (sheng.getDmId().equals(shi.getParentid())) {
                    for (int x = 0; x < xians.size(); x++) {
                        Xian xian = xians.get(x);
                        if (shi.getDmId().equals(xian.getParentid())) {
                            cityList.add(xian.getName());
                            tempXian.add(xian);
                        }
                    }
                    provinceAreaList.add(cityList);
                    tempProvince.add(tempXian);
                }
            }
            options3Items.add(provinceAreaList);
            sortXian.add(tempProvince);
        }
        LLog.i("options3Items    " + options3Items.toString());
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
                        if(AUTHENTICATIONTYPE == 0){
                            handle02.sendEmptyMessageDelayed(0, 1000);
                        }else{
                            handle01.sendEmptyMessageDelayed(0, 1000);
                        }

                        ToastUtil.showToast(LeaseMsgAuthenticationActivity.this, "验证码发送成功");
                        break;
                    default:
                        ToastUtil.showLongToast(LeaseMsgAuthenticationActivity.this, common.getInfo());
                        break;
                }
            }
            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showLongToast(LeaseMsgAuthenticationActivity.this, msg);
            }
        });
    }

    private int count01 = 60;
    /**
     * 验证码倒计时
     */
    Handler handle01 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (count01 == 0) {
                count01 = 60;
                tv_getCode_01.setEnabled(true);
                tv_getCode_01.setText("获取验证码");
                return;
            }
            tv_getCode_01.setText(count01-- + "s");
            tv_getCode_01.setEnabled(false);
            handle01.sendEmptyMessageDelayed(0, 1000);
        }
    };

    private int count02 = 60;
    /**
     * 验证码倒计时
     */
    Handler handle02 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (count02 == 0) {
                count02 = 60;
                tv_getCode_02.setEnabled(true);
                tv_getCode_02.setText("获取验证码");
                return;
            }
            tv_getCode_02.setText(count02-- + "s");
            tv_getCode_02.setEnabled(false);
            handle02.sendEmptyMessageDelayed(0, 1000);
        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalValue.REPAIRS_PHOTO_REQUESTCODE0:
                    List<Uri> mSelected = Matisse.obtainResult(data);
                    if(mSelected != null && mSelected.size() > 0){
                        String picPath = PictureUtils.getPath(LeaseMsgAuthenticationActivity.this,mSelected.get(0));
                        if(presentImgView != null){
                            ImageLoaderUtil.getInstance().displayFromLocalCircular(LeaseMsgAuthenticationActivity.this,presentImgView,picPath,900,600,R.drawable.img_default_load);
                        }
                        switch (presentImgView.getId()){
                            case R.id.iv_id_positive:
                                idPositivePath = picPath;
                                break;
                            case R.id.iv_id_back:
                                idBackPath = picPath;
                                break;
                            case R.id.iv_business_license:
                                businessLicensePath = picPath;
                                break;
                             case R.id.iv_leaseContract:
                                 leaseContractPath = picPath;
                                break;
                        }
                    }
                    break;
                case GlobalValue.REPAIRS_CAMERA_REQUESTCODE0:
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    if (bm == null) {
                        return;
                    }
                    File f = null;
                    try {
                        f = FileUtil.saveMyBitmap(bm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
/*                    File file = new Compressor.Builder(RegistActivity.this).setMaxHeight(1920).setMaxWidth(1080)
                            .setQuality(85).setCompressFormat(Bitmap.CompressFormat.PNG).setDestinationDirectoryPath(FileConfig.PATH_IMAGES)
                            .build().compressToFile(f);*/
                    String filePath = f.getAbsolutePath();
                    if(presentImgView != null){
                        ImageLoaderUtil.getInstance().displayFromLocalCircular(LeaseMsgAuthenticationActivity.this,presentImgView,filePath,900,600,R.drawable.img_default_load);
                    }
                    switch (presentImgView.getId()){
                        case R.id.iv_id_positive:
                            idPositivePath = filePath;
                            break;
                        case R.id.iv_id_back:
                            idBackPath = filePath;
                            break;
                        case R.id.iv_business_license:
                            businessLicensePath = filePath;
                            break;
                        case R.id.iv_leaseContract:
                            leaseContractPath = filePath;
                            break;
                    }
                    break;
                case GlobalValue.TAILORING_REQUESTCODE:
                    if(cutfile == null){
                        return;
                    }
                    break;

            }
        }
    }


    @NonNull
    private Intent CutForPhoto(Uri uri) {
        try {
            //直接裁剪
            Intent intent = new Intent("com.android.camera.action.CROP");
            //设置裁剪之后的图片路径文件
            cutfile = new File(Environment.getExternalStorageDirectory().getPath(),
                    "cutcamera"+ System.currentTimeMillis()+".jpg"); //随便命名一个
            if (cutfile.exists()){ //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
                cutfile.delete();
            }
            cutfile.createNewFile();
            //初始化 uri
            Uri imageUri = uri; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            outputUri = Uri.fromFile(cutfile);

            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop",true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            //String sss= android.os.Build.MODEL;
            if(Build.MODEL.contains("EDI-AL10")|| Build.MODEL.contains("HUAWEI"))
            {//华为特殊处理 不然会显示圆
                intent.putExtra("aspectX", 9998);
                intent.putExtra("aspectY", 9999);
            }
            else
            {
                intent.putExtra("aspectX", 900);
                intent.putExtra("aspectY", 600);
            }
            //设置要裁剪的宽高
            intent.putExtra("outputX", 900); //200dp
            intent.putExtra("outputY",600);
            intent.putExtra("scale",true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data",false);
            if (imageUri != null) {
                intent.setDataAndType(imageUri, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            return intent;
        } catch (IOException e) {
            e.toString();
            e.printStackTrace();
        }
        return null;
    }

    private Uri stringToUri(String path){
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //如果是7.0android系统
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA,path);
            uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        }else{
            uri = Uri.fromFile(new File(path));
        }
        return uri;
    }

    @Override
    protected void initGetData() {
    }

    @Override
    protected void widgetListener() {
        cb_enterprise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    AUTHENTICATIONTYPE = 0;
                    cb_enterprise.setChecked(true);
                    cb_personal.setChecked(false);
                    tv_name_txt.setText("法人姓名");
                    ll_code_01.setVisibility(View.GONE);
                    ll_code_02.setVisibility(View.VISIBLE);
                    ll_address.setVisibility(View.VISIBLE);
                    ll_detail_address.setVisibility(View.VISIBLE);
                    ll_businessCode.setVisibility(View.VISIBLE);
                    ll_businessCode_img.setVisibility(View.VISIBLE);
                    ll_field_img.setVisibility(View.VISIBLE);
                    ll_company.setVisibility(View.VISIBLE);

                }
            }
        });
        cb_personal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    AUTHENTICATIONTYPE = 1;
                    cb_personal.setChecked(true);
                    cb_enterprise.setChecked(false);
                    tv_name_txt.setText("姓名");
                    ll_code_01.setVisibility(View.VISIBLE);
                    ll_code_02.setVisibility(View.GONE);
                    ll_address.setVisibility(View.GONE);
                    ll_detail_address.setVisibility(View.GONE);
                    ll_businessCode.setVisibility(View.GONE);
                    ll_businessCode_img.setVisibility(View.GONE);
                    ll_field_img.setVisibility(View.GONE);
                    ll_company.setVisibility(View.GONE);
                }
            }
        });
    }


    private void checkedParams(){

        String name = et_name.getText().toString();
        String companyName = et_company_name.getText().toString();
        String phone = et_phone.getText().toString();
        String idCard = et_idCard.getText().toString();
        String address = tv_address.getText().toString();
        String addressDetail = et_addressDetail.getText().toString();
        String creditCode = et_creditCode.getText().toString();
        String code02 = et_code_02.getText().toString();
        String code01 = et_code_01.getText().toString();

        params = new HashMap<>();

        if(TextUtils.isEmpty(name)){
            if(AUTHENTICATIONTYPE == 0) { //企业认证
                ToastUtil.showToast(LeaseMsgAuthenticationActivity.this, "请填写法人名称");
            }else{
                ToastUtil.showToast(LeaseMsgAuthenticationActivity.this,"请填写真实姓名");
            }
            return;
        }else{
            params.put("user_realname",name);
        }


        if(AUTHENTICATIONTYPE == 0) { //企业认证
            if(TextUtils.isEmpty(companyName)){
                ToastUtil.showToast(LeaseMsgAuthenticationActivity.this, "请填写公司名称");
                return;
            }else{
                params.put("company_name",companyName);
            }

        }

        if(!CommonUtils.isPhoneNum(phone)){
            ToastUtil.showToast(LeaseMsgAuthenticationActivity.this, "请填写手机号");
            return;
        }else{
            params.put("user_phone",phone);
        }

        if(!CommonUtils.isID(idCard)){
            ToastUtil.showToast(LeaseMsgAuthenticationActivity.this, "请填写正确的身份证号");
            return;
        }else{
            params.put("user_idcard",idCard);
        }

        if(TextUtils.isEmpty(idPositivePath)){
            ToastUtil.showToast(LeaseMsgAuthenticationActivity.this, "请上传身份证正面");
            return;
        }else{
            urlStrs.add(idPositivePath);
        }

        if(TextUtils.isEmpty(idBackPath)){
            ToastUtil.showToast(LeaseMsgAuthenticationActivity.this, "请上传身份证反面");
            return;
        }else{
            urlStrs.add(idBackPath);
        }


        if(AUTHENTICATIONTYPE == 0){
            if(selectedXian == null){
                ToastUtil.showToast(LeaseMsgAuthenticationActivity.this, "请选择公司/店铺地址");
                return;
            }else{
                params.put("company_province_id",selectedSheng.getDmId());
                params.put("company_province_name",selectedSheng.getName());
                params.put("company_city_id",selectedShi.getDmId());
                params.put("company_city_name",selectedShi.getName());
                params.put("company_town_id",selectedXian.getDmId());
                params.put("company_town_name",selectedXian.getName());
            }

            if(TextUtils.isEmpty(addressDetail)){
                ToastUtil.showToast(LeaseMsgAuthenticationActivity.this, "请填写公司/店铺详细地址");
                return;
            }else{
                params.put("company_address",addressDetail);
            }
            if(!CommonUtils.isLicense18(creditCode)){
                ToastUtil.showToast(LeaseMsgAuthenticationActivity.this, "请输入正确的营业执照信用代码");
                return;
            }else{
                params.put("company_no",creditCode);
            }

            if(TextUtils.isEmpty(businessLicensePath)){
                ToastUtil.showToast(LeaseMsgAuthenticationActivity.this, "请上传营业执照");
                return;
            }else{
                urlStrs.add(businessLicensePath);
            }

            if(TextUtils.isEmpty(businessLicensePath)){
                ToastUtil.showToast(LeaseMsgAuthenticationActivity.this, "请上传营业执照");
                return;
            }else{
                urlStrs.add(businessLicensePath);
            }

            if(TextUtils.isEmpty(leaseContractPath)){
                ToastUtil.showToast(LeaseMsgAuthenticationActivity.this, "请上传场地租赁合作/场地购买合同");
                return;
            }else{
                urlStrs.add(leaseContractPath);
            }

            if(TextUtils.isEmpty(code02)){
                ToastUtil.showToast(LeaseMsgAuthenticationActivity.this, "请输入验证码");
                return;
            }else{
                params.put("code",code02);
            }
        }else{
            if(TextUtils.isEmpty(code01)){
                ToastUtil.showToast(LeaseMsgAuthenticationActivity.this, "请输入验证码");
                return;
            }else{
                params.put("code",code01);
            }
        }

        if(!isUserAgreement){
            ToastUtil.showToast(LeaseMsgAuthenticationActivity.this,"请勾选租赁协议");
            return;
        }

        uploadPhoto(new File(urlStrs.get(0)));
    }

    /**
     * 认证
     */
    private void authentication(){

        HttpRequestUtils.httpRequest(this, "用户申请认证", RequestValue.LEASEMANAGE_AUTHORGUSER, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    ToastUtil.showToast(LeaseMsgAuthenticationActivity.this,common.getInfo());
                    getUserInfor();
                }else{
                    ToastUtil.showToast(LeaseMsgAuthenticationActivity.this,common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
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
                            user = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), User.class);
                            if (user != null) {
                                SharedPreferencesUtils.getInstance(LeaseMsgAuthenticationActivity.this).saveObjectData(user);
                            }
                            break;
                        default:
                            break;
                    }
                }
                finish();
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                finish();
            }
        });
    }

    private void getToken(final File photoPath) {
        if (token != null) {
            return;
        }
        HttpRequestUtils.httpRequest(this, "获取上传图片token", RequestValue.GET_PICTURE, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                LLog.iNetSucceed(" getPhotoToken " + response);
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        token = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), PhotoToken.class);
                        if (photoPath != null) {
                            uploadPhoto(photoPath);
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }

    private void uploadPhoto(final File path) {
        if (token == null) {
            getToken(path);
            return;
        }
        pd.show();
        final String key = Utils.getImgName();
        UploadManager um = QiniuUtil.getUploadManager();
        um.put(path, key, token.getSecurityToken(),
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info,
                                         JSONObject response) {
                        if (info.isOK()) {
                            String url = "";
                            if(!TextUtils.isEmpty(token.getDomain())){
                                url = token.getDomain()+ key;
                            }else{
                                url = ServerConfig.RUL_IMG + key;
                            }
                            if (TextUtils.isEmpty(imgPath1)) {
                                imgPath1 = url;
                            } else {
                                imgPath1 = imgPath1+","+url;
                            }
                            urlStrs.remove(path.getPath());
//                            Utils.deleteFile(path.getPath(), CannotMaintainActivity.this);

                            if(AUTHENTICATIONTYPE == 0){//0企业
                                switch (urlStrs.size()){
                                    case 3:
                                        params.put("idcard_face",url);
                                        break;
                                    case 2:
                                        params.put("idcard_back",url);
                                        break;
                                    case 1:
                                        params.put("company_img",url);
                                        break;
                                     case 0:
                                        params.put("company_place_img",url);
                                        break;
                                }
                            }else{
                                switch (urlStrs.size()){
                                    case 1:
                                        params.put("idcard_face",url);
                                        break;
                                    case 0:
                                        params.put("idcard_back",url);
                                        break;
                                }
                            }


                            if (urlStrs.size() > 0 && !TextUtils.isEmpty(urlStrs.get(0))){
                                uploadPhoto(new File(urlStrs.get(0)));
                            } else {
                                if(urlStrs.size() == 0){
                                    authentication();
                                }
                                pd.dismiss();
                            }
                            LLog.i("联网  图片地址" + url);
                        } else {
                            pd.dismiss();
                            ToastUtil.showLongToast(LeaseMsgAuthenticationActivity.this, "图片上传失败");
                        }
                    }
                }, null);
    }

}
