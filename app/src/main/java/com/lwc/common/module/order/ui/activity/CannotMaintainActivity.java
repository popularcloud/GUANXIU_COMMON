package com.lwc.common.module.order.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.ImageBrowseActivity;
import com.lwc.common.adapter.MyGridViewPhotoAdpter;
import com.lwc.common.configs.FileConfig;
import com.lwc.common.configs.ServerConfig;
import com.lwc.common.controler.global.GlobalValue;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Coupon;
import com.lwc.common.module.bean.Detection;
import com.lwc.common.module.bean.Order;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.module.bean.PhotoToken;
import com.lwc.common.utils.FileUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.KeyboardUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.PictureUtils;
import com.lwc.common.utils.QiniuUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
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
 * 拒绝维修
 */
public class CannotMaintainActivity extends BaseActivity {


    @BindView(R.id.et_desc)
    EditText et_desc;
    @BindView(R.id.tv_title_reason)
    TextView tv_title_reason;
    @BindView(R.id.gridview_my)
    MyGridView myGridview;
    private int countPhoto=8;//可选图片数量
    private List<String> urlStrs = new ArrayList();
    private String imgPath1;
    private PhotoToken token;
    private ProgressDialog pd;
    private File dataFile;
    private String desc;
    private Detection detection;
    private final int request = 1030;
    private MyGridViewPhotoAdpter adpter;
    private Order order;
    private PackageBean pak;
    private Coupon coupon;
    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.tv_pak)
    TextView tv_pak;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_cannot_maintain;
    }

    @Override
    protected void findViews() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        pd = new ProgressDialog(this);
        pd.setMessage("正在上传图片...");
        pd.setCancelable(false);
        urlStrs.add("");
        adpter = new MyGridViewPhotoAdpter(this, urlStrs);
        myGridview.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去掉默认点击背景
        myGridview.setAdapter(adpter);
        myGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adpter.getItem(position).equals("")) {
                    int count = countPhoto;
                    if (adpter.getCount() > 1) {
                        count = count - adpter.getCount()+1;
                    }
                    showTakePopupWindow1(count);
                } else {
                    Intent intent = new Intent(CannotMaintainActivity.this, ImageBrowseActivity.class);
                    intent.putExtra("index", position);
                    intent.putStringArrayListExtra("list", (ArrayList)adpter.getLists());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initGetData() {
        detection = (Detection)getIntent().getSerializableExtra("detection");
        order = (Order)getIntent().getSerializableExtra("order");
        if (detection != null) {
            setTitle("拒绝维修");
            tv_title_reason.setText("拒绝维修原因");
            getRefusedInfo();
           /* if (detection.getPackageId() != null) {
                pak = new PackageBean();
                pak.setPackageId(detection.getPackageId());
                pak.setPackageType(detection.getPackageType());
            }
            if (detection.getCouponId() != null) {
                coupon = new Coupon();
                coupon.setCouponId(detection.getCouponId());
                coupon.setDiscountAmount(detection.getDiscountAmount());
                coupon.setCouponType(detection.getCouponType());
            }*/
           // setView();
        } else if (order != null) {
            setTitle("拒绝返厂");
            tv_title_reason.setText("拒绝返厂原因");
            getRefusedInfo();
        }
    }

    private void setView() {
        //显示最优套餐
        if(pak != null && !TextUtils.isEmpty(pak.getPackageId())) {
            if (pak.getPackageType() == 1) {
                tv_pak.setText("-" + Utils.chu(detection.getReVisitCost() == null?detection.getVisitCost():detection.getReVisitCost(), "100") + "元");
            } else if (pak.getPackageType() == 2) {
                pak.setPackageId("");
                tv_pak.setText("暂无可用套餐");
                //ToastUtil.showToast(this, "所选套餐不能减免上门费");
            } else if (pak.getPackageType() == 3) {
                //tv_pak.setText("-" + Utils.chu(detection.getReVisitCost(), "100") + "元");
                pak.setPackageId("");
                tv_pak.setText("暂无可用套餐");
                //ToastUtil.showToast(this, "所选套餐不能减免上门费");
            }
        } else if (pak != null && TextUtils.isEmpty(pak.getPackageId())) {
            tv_pak.setText("不使用套餐减免");
        } else {
            tv_pak.setText("暂无可用套餐");
        }

        /*if (coupon != null && !TextUtils.isEmpty(coupon.getDiscountAmount()) && Integer.parseInt(coupon.getDiscountAmount()) > 0) {
            if (coupon.getCouponType() == 1) {
                tv_amount.setText("-"+coupon.getDiscountAmount()+"元");
            } else if (coupon.getCouponType() == 2) {
                tv_amount.setText("-"+coupon.getDiscountAmount()+"元");
            } else if (coupon.getCouponType() == 3) {
                tv_amount.setText("不使用套餐减免");
                ToastUtil.showToast(this, "所选优惠券不能减免上门费");
            } else if (coupon.getCouponType() == 4) {
                tv_amount.setText("不使用套餐减免");
                ToastUtil.showToast(this, "所选优惠券不能减免上门费");
            }
        } else if (coupon != null && TextUtils.isEmpty(coupon.getDiscountAmount()) || Integer.parseInt(coupon.getDiscountAmount()) == 0) {
            tv_amount.setText("不使用优惠券");
        } else {
            tv_amount.setText("暂无可用优惠券");
        }*/

        tv_amount.setText("优惠券不可使用");
        tv_amount.setTextColor(Color.parseColor("#ff868686"));
    }

    @Override
    protected void widgetListener() {
    }

    @OnClick({R.id.rl_coupon, R.id.rl_pak, R.id.btnAffirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnAffirm:
                desc = et_desc.getText().toString().trim();
                if (TextUtils.isEmpty(desc)) {
                    if (order != null) {
                        ToastUtil.showLongToast(this, "请填写拒绝返厂原因");
                    }else{
                        ToastUtil.showLongToast(this, "请填写拒绝维修原因");
                    }
                    return;
                }
                urlStrs = adpter.getLists();
                if (urlStrs != null && urlStrs.size() > 0 && !TextUtils.isEmpty(urlStrs.get(0))) {
                    uploadPhoto(new File(urlStrs.get(0)));
                } else {
                    submit();
                }
                break;
            case R.id.rl_coupon:
                Bundle bundle3 = new Bundle();
                if (pak != null && !TextUtils.isEmpty(pak.getPackageId())) {
                    bundle3.putString("packageId", pak.getPackageId());
                }
                bundle3.putInt("refused", 1);
                if (order != null)
                    bundle3.putString("orderId", order.getOrderId());
                else
                    bundle3.putString("orderId", detection.getOrderId());
                IntentUtil.gotoActivityForResult(this, SelectPackageListActivity.class, bundle3, 1040);
                break;
            case R.id.rl_pak:
                Bundle bundle4 = new Bundle();
                if (order != null)
                    bundle4.putString("orderId", order.getOrderId());
                else
                    bundle4.putString("orderId", detection.getOrderId());
                bundle4.putInt("type", 1);
                if (coupon != null && !TextUtils.isEmpty(coupon.getCouponId())) {
                    bundle4.putString("couponId", coupon.getCouponId());
                }
                bundle4.putInt("refused", 1);
                IntentUtil.gotoActivityForResult(this, SelectPackageListActivity.class, bundle4, 1050);
                break;
        }
    }

    private void submit(){
        Bundle bundle2 = new Bundle();
        if (order != null)
            detection.setOrderId(order.getOrderId());
        bundle2.putSerializable("data", detection);
        bundle2.putString("operate", "1");
        coupon.setCouponId("");
        bundle2.putSerializable("coupon", coupon);
        bundle2.putSerializable("package", pak);
        bundle2.putString("images", imgPath1);
        bundle2.putString("cause", desc);
        IntentUtil.gotoActivityForResult(this, PayActivity.class, bundle2, request);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void showTakePopupWindow1(int count) {
        KeyboardUtil.showInput(false, this);
        ToastUtil.showPhotoSelect(this, count);
        getToken(null);
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
//                            Utils.deleteFile(path.getPath(), CannotMaintainActivity.this);
                            if (urlStrs.size() > 0 && !TextUtils.isEmpty(urlStrs.get(0))){
                                uploadPhoto(new File(urlStrs.get(0)));
                            } else {
                                submit();
                                pd.dismiss();
                            }
                            LLog.i("联网  图片地址" + url);
                            pd.dismiss();
                        } else {
                            pd.dismiss();
                            ToastUtil.showLongToast(CannotMaintainActivity.this, "图片上传失败");
                        }
                    }
                }, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1040 && resultCode == RESULT_OK) {
            coupon = (Coupon)data.getSerializableExtra("coupon");
            if (coupon == null) {
                coupon = new Coupon();
            }
            setView();
        } else if (requestCode == 1050 && resultCode == RESULT_OK) {
            pak = (PackageBean)data.getSerializableExtra("package");
            if (pak == null) {
                pak = new PackageBean();
            }
            setView();
        } else if (requestCode == request && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        } else if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalValue.REPAIRS_PHOTO_REQUESTCODE0:
                    List<Uri> mSelected = Matisse.obtainResult(data);
                    if (mSelected != null && mSelected.size() > 0) {
                        urlStrs = adpter.getLists();
                        urlStrs.remove(urlStrs.size()-1);
                        for(int i=0; i<mSelected.size(); i++) {
                            Uri uri = mSelected.get(i);
                            urlStrs.add(PictureUtils.getPath(CannotMaintainActivity.this, uri));
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
                        File file = new Compressor.Builder(CannotMaintainActivity.this).setMaxHeight(1080).setMaxWidth(1920)
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
                        ToastUtil.showToast(CannotMaintainActivity.this, "选择图片失败");
                    }
                    break;
            }
        }
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

    /**
     * 查询拒绝返厂信息
     */
    private void getRefusedInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", order != null?order.getOrderId():detection.getOrderId());
        HttpRequestUtils.httpRequest(this, "查询拒绝返厂详情", RequestValue.GET_REFUSED, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        String myOrderId = order != null?order.getOrderId():detection.getOrderId();
                        detection = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), Detection.class);
                        if (detection == null) {
                            ToastUtil.showLongToast(CannotMaintainActivity.this, "数据异常");
                            onBackPressed();
                            return;
                        }
                        detection.setOrderId(myOrderId);
                        pak = new PackageBean();
                        pak.setPackageId(detection.getPackageId());
                        pak.setPackageType(detection.getPackageType());
                        coupon = new Coupon();
                        coupon.setCouponId(detection.getCouponId());
                        coupon.setDiscountAmount(detection.getDiscountAmount());
                        coupon.setCouponType(detection.getCouponType());
                        setView();
                        break;
                    default:
                        ToastUtil.showLongToast(CannotMaintainActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                ToastUtil.showLongToast(CannotMaintainActivity.this, msg);
            }
        });
    }
}
