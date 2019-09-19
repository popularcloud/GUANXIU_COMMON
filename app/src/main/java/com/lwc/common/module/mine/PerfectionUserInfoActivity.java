package com.lwc.common.module.mine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.ImageBrowseActivity;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.adapter.MyGridViewPhotoAdpter;
import com.lwc.common.bean.PickerView;
import com.lwc.common.configs.FileConfig;
import com.lwc.common.configs.ServerConfig;
import com.lwc.common.controler.global.GlobalValue;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.AuthenticationInfo;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.PhotoToken;
import com.lwc.common.module.bean.Sheng;
import com.lwc.common.module.bean.Shi;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.bean.Xian;
import com.lwc.common.utils.FileUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.KeyboardUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.PictureUtils;
import com.lwc.common.utils.QiniuUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.LimitInputTextWatcher;
import com.lwc.common.view.MyGridView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.zhihu.matisse.Matisse;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

/**
 * 申请单位认证
 */
public class PerfectionUserInfoActivity extends BaseActivity {
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.edtAddressDetail)
    EditText edtAddressDetail;
    @BindView(R.id.edtCompanyName)
    EditText edtCompanyName;
    @BindView(R.id.gridview_my)
    MyGridView myGridview;
    private int countPhoto=4;//可选图片数量
    private List<String> urlStrs = new ArrayList();
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
    private Sheng selectedSheng;
    private Shi selectedShi;
    private Xian selectedXian;
    private String imgPath1="";
    private PhotoToken token;
    private ProgressDialog pd;
    private File dataFile;
    private SharedPreferencesUtils preferencesUtils;
    private String recordId;
    private AuthenticationInfo ai;
    private User user;
    private MyGridViewPhotoAdpter adpter;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_perfection_user_info;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        getAreaData();
        myThread.start();
        edtName.addTextChangedListener(new LimitInputTextWatcher(edtName));
        pd = new ProgressDialog(this);
        pd.setMessage("正在上传图片...");
        pd.setCancelable(false);
        setTitle("机关单位认证");
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initGetData() {
        preferencesUtils = SharedPreferencesUtils.getInstance(PerfectionUserInfoActivity.this);
        user = preferencesUtils.loadObjectData(User.class);
        updateView();
        getUserInfor();
    }

    public void updateView() {
        ai = preferencesUtils.loadObjectData(AuthenticationInfo.class);
        if (ai != null && ai.getUserId() != null && ai.getUserId().equals(user.getUserId())) {
            if (!TextUtils.isEmpty(ai.getApplicantName())) {
                edtName.setText(ai.getApplicantName());
            }
            recordId = ai.getRecordId();
            if (!TextUtils.isEmpty(ai.getCityId())&&!TextUtils.isEmpty(ai.getCityName())
                    &&!TextUtils.isEmpty(ai.getProvinceId())&&!TextUtils.isEmpty(ai.getProvinceName())
                    &&!TextUtils.isEmpty(ai.getTownId())&&!TextUtils.isEmpty(ai.getTownName())) {
                selectedShi = new Shi();
                selectedSheng = new Sheng();
                selectedXian = new Xian();
                selectedShi.setDmId(ai.getCityId());
                selectedShi.setName(ai.getCityName());
                selectedSheng.setDmId(ai.getProvinceId());
                selectedSheng.setName(ai.getProvinceName());
                selectedXian.setDmId(ai.getTownId());
                selectedXian.setName(ai.getTownName());
                String tx = selectedSheng.getName() + "-" +
                        selectedShi.getName() + "-" +
                        selectedXian.getName();
                txtAddress.setText(tx);
            }

            if (!TextUtils.isEmpty(ai.getOrganizationName())) {
                edtCompanyName.setText(ai.getOrganizationName());
            }
            if (!TextUtils.isEmpty(ai.getDetailedAddress())) {
                edtAddressDetail.setText(ai.getDetailedAddress());
            }
        }

        urlStrs.add("");
        adpter = new MyGridViewPhotoAdpter(this, urlStrs);
        myGridview.setAdapter(adpter);
        myGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adpter.getItem(position).equals("")) {
                    int count = countPhoto;
                    if (adpter.getCount() > 1) {
                        count = count - adpter.getCount()+1;
                    }
                    showTakePopupWindow(count);
                } else {
                    Intent intent = new Intent(PerfectionUserInfoActivity.this, ImageBrowseActivity.class);
                    intent.putExtra("index", position);
                    intent.putStringArrayListExtra("list", (ArrayList)adpter.getLists());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void widgetListener() {
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

    private Thread myThread = new Thread() {
        @Override
        public void run() {
            super.run();
            loadOptionsPickerViewData();
        }
    };

    @OnClick({R.id.btnSubmit, R.id.txtAddress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                String name = edtName.getText().toString().trim();
                String unitAddress = edtAddressDetail.getText().toString().trim();
                String companyName = edtCompanyName.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    ToastUtil.showLongToast(this, "请输入真实姓名");
                    return;
                }

                if (selectedSheng == null || selectedShi == null || selectedXian == null) {
                    ToastUtil.showLongToast(this, "请输入单位所在地区");
                    return;
                }

                if (TextUtils.isEmpty(companyName)) {
                    ToastUtil.showLongToast(this, "请输入单位名称");
                    return;
                }
                if (TextUtils.isEmpty(unitAddress)) {
                    ToastUtil.showLongToast(this, "请输入详细地址");
                    return;
                }
                urlStrs = adpter.getLists();
                if (urlStrs != null && urlStrs.size() > 0 && !TextUtils.isEmpty(urlStrs.get(0))) {
                    uploadPhoto(new File(urlStrs.get(0)));
                } else {
                    if (isFastClick()) {
                        return;
                    }
                    upUserInfor(name, companyName, unitAddress);
                }
                break;
            case R.id.txtAddress:
                showPickerView();
                break;
        }
    }

    private void submit() {
        String name = edtName.getText().toString().trim();
        String unitAddress = edtAddressDetail.getText().toString().trim();
        String companyName = edtCompanyName.getText().toString().trim();
        upUserInfor(name, companyName, unitAddress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case GlobalValue.REPAIRS_PHOTO_REQUESTCODE0:
                    List<Uri> mSelected = Matisse.obtainResult(data);
                    if (mSelected != null && mSelected.size() > 0) {
                        urlStrs = adpter.getLists();
                        urlStrs.remove(urlStrs.size()-1);
                        for(int i=0; i<mSelected.size(); i++) {
                            Uri uri = mSelected.get(i);
                            urlStrs.add(PictureUtils.getPath(PerfectionUserInfoActivity.this, uri));
                        }
                    }
                    if (urlStrs.size() < countPhoto) {
                        urlStrs.add("");
                    }
                    adpter.setLists(urlStrs);
                    adpter.notifyDataSetChanged();
                    break;
                case GlobalValue.REPAIRS_CAMERA_REQUESTCODE0:
                    try {
                        Bitmap bm = (Bitmap) data.getExtras().get("data");
                        if (bm == null) {
                            return;
                        }
                        File f = FileUtil.saveMyBitmap(bm);
                        File file = new Compressor.Builder(PerfectionUserInfoActivity.this).setMaxHeight(1080).setMaxWidth(1920)
                                .setQuality(85).setCompressFormat(Bitmap.CompressFormat.PNG).setDestinationDirectoryPath(FileConfig.PATH_IMAGES)
                                .build().compressToFile(f);
                        if (file != null) {
                            dataFile = file;
                        } else {
                            dataFile = f;
                        }
                    } catch (Exception e) {
                    }
                    if (dataFile != null) {
                        if (urlStrs != null && urlStrs.get(urlStrs.size()-1).equals("")) {
                            urlStrs.remove(urlStrs.size()-1);
                        }
                        urlStrs.add(dataFile.getAbsolutePath());
                        if (urlStrs.size() < countPhoto) {
                            urlStrs.add("");
                        }
                        adpter.setLists(urlStrs);
                        adpter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showToast(PerfectionUserInfoActivity.this, "选择图片失败");
                    }
            }
        }
    }

    private void showTakePopupWindow(int count) {
        ToastUtil.showPhotoSelect(this, count);
        getToken(null);
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
        if (!pd.isShowing())
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
                                url = token.getDomain() + key;
                            }else{
                                url = ServerConfig.RUL_IMG + key;
                            }

                            if (TextUtils.isEmpty(imgPath1)) {
                                imgPath1 = url;
                            } else {
                                imgPath1 = imgPath1+","+url;
                            }
                            urlStrs.remove(path.getPath());
//                            Utils.deleteFile(path.getPath(), PerfectionUserInfoActivity.this);
                            if (urlStrs.size() > 0 && !TextUtils.isEmpty(urlStrs.get(0))){
                                uploadPhoto(new File(urlStrs.get(0)));
                            } else {
                                submit();
                                pd.dismiss();
                            }
                            LLog.i("联网  图片地址"+url);
                        } else {
                            pd.dismiss();
                            ToastUtil.showLongToast(PerfectionUserInfoActivity.this, "图片上传失败");
                        }
                    }
                }, null);
    }

    private static long lastClickTime;

    private synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private void showPickerView() {// 弹出选择器
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
                txtAddress.setText(tx);
            }
        })

                .setTitleText("城市选择")
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
     * 获取个人信息
     */
    private void getUserInfor() {
        HttpRequestUtils.httpRequest(this, "查询用户认证信息", RequestValue.GET_USER_AUTH_INFO, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        AuthenticationInfo ai = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), AuthenticationInfo.class);
                        if (ai != null) {
                            ai.setUserId(user.getUserId());
                            preferencesUtils.saveObjectData(ai);
                            updateView();
                        }
                        break;
                    default:
                        ToastUtil.showLongToast(PerfectionUserInfoActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
            }
        });
    }

    /**
     * 申请机关单位认证
     *
     * @param realname
     * @param companyName
     * @param unitAddress
     */
    private void upUserInfor(final String realname, final String companyName, final String unitAddress) {
        Map<String, String> map = new HashMap<>();
        map.put("organization_name", companyName);
        map.put("applicant_name", realname);
        map.put("detailed_address", unitAddress);
        map.put("province_id", selectedSheng.getDmId());
        map.put("province_name", selectedSheng.getName());
        map.put("city_id", selectedShi.getDmId());
        map.put("city_name", selectedShi.getName());
        map.put("town_id", selectedXian.getDmId());
        map.put("town_name", selectedXian.getName());
        if (!TextUtils.isEmpty(recordId)){
            map.put("record_id", recordId);
        }

        map.put("applicant_images", imgPath1);
        HttpRequestUtils.httpRequest(this, "申请机关单位认证", RequestValue.IMPROVE_USER_INFOR, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        Intent intent = new Intent(MainActivity.KEY_UPDATE_USER);
                        LocalBroadcastManager.getInstance(PerfectionUserInfoActivity.this).sendBroadcast(intent);
                        IntentUtil.gotoActivity(PerfectionUserInfoActivity.this, AttestationInfoActivity.class);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        ToastUtil.showToast(PerfectionUserInfoActivity.this, common.getInfo());
                        finish();
                        break;
                    default:
                        ToastUtil.showLongToast(PerfectionUserInfoActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                if (msg != null && !msg.equals("")) {
                    ToastUtil.showLongToast(PerfectionUserInfoActivity.this, msg);
                } else {
                    ToastUtil.showLongToast(PerfectionUserInfoActivity.this, "请求失败，请稍候重试!");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ai == null) {
            ai = new AuthenticationInfo();
            ai.setUserId(user.getUserId());
        }
        String name = edtName.getText().toString().trim();
        String unitAddress = edtAddressDetail.getText().toString().trim();
        String companyName = edtCompanyName.getText().toString().trim();

        if (!TextUtils.isEmpty(name)) {
            ai.setApplicantName(name);
        }

        if (selectedSheng != null && selectedShi != null && selectedXian != null) {
            ai.setCityId(selectedShi.getDmId());
            ai.setCityName(selectedShi.getName());
            ai.setProvinceId(selectedSheng.getDmId());
            ai.setProvinceName(selectedSheng.getName());
            ai.setTownId(selectedXian.getDmId());
            ai.setTownName(selectedXian.getName());
        }

        if (!TextUtils.isEmpty(companyName)) {
            ai.setOrganizationName(companyName);
        }
        if (!TextUtils.isEmpty(unitAddress)) {
            ai.setDetailedAddress(unitAddress);
        }
        if (!TextUtils.isEmpty(imgPath1))
            ai.setApplicantImages(imgPath1);
        preferencesUtils.saveObjectData(ai);
    }

}
