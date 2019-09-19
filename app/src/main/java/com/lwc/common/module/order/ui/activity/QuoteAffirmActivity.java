package com.lwc.common.module.order.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.ImageBrowseActivity;
import com.lwc.common.adapter.HardwareDetailAdapter;
import com.lwc.common.adapter.MyGridViewPhotoAdpter;
import com.lwc.common.bean.PartsDetailBean;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.interf.OnCommClickCallBack;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Coupon;
import com.lwc.common.module.bean.Detection;
import com.lwc.common.module.bean.Order;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.module.integral.activity.IntegralMainActivity;
import com.lwc.common.module.mine.ShareActivity;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.MyGridView;
import com.lwc.common.view.SolutionItem;
import com.lwc.common.widget.CustomDialog;
import com.lwc.common.widget.ShowImgDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 故障确认单
 */
public class QuoteAffirmActivity extends BaseActivity {

    @BindView(R.id.tv_desc)
    TextView tv_desc;
    @BindView(R.id.ll_jjfa)
    LinearLayout ll_jjfa;
    @BindView(R.id.tv_smf)
    TextView tv_smf;
    @BindView(R.id.rl_smf)
    RelativeLayout rl_smf;
    @BindView(R.id.rl_fwf)
    RelativeLayout rl_fwf;
    @BindView(R.id.rl_qtf)
    RelativeLayout rl_qtf;
    @BindView(R.id.tv_qtf)
    TextView tv_qtf;
    @BindView(R.id.tv_titel)
    TextView tv_titel;
    @BindView(R.id.tv_fwf)
    TextView tv_fwf;
    @BindView(R.id.rl_remark)
    RelativeLayout rl_remark;
    @BindView(R.id.tv_remark)
    TextView tv_remark;
    @BindView(R.id.tv_total)
    TextView tv_total;
    @BindView(R.id.ll_photo)
    LinearLayout ll_photo;
    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.tv_pak)
    TextView tv_pak;
    @BindView(R.id.rl_coupon)
    RelativeLayout rl_coupon;
    @BindView(R.id.rl_pak)
    RelativeLayout rl_pak;
    @BindView(R.id.rl_yjf)
    RelativeLayout rl_yjf;
    @BindView(R.id.tv_yjf)
    TextView tv_yjf;
    @BindView(R.id.gridview_my)
    MyGridView myGridview;
    @BindView(R.id.rv_hardware)
    RecyclerView rv_hardware;
    @BindView(R.id.rl_hardware)
    RelativeLayout rl_hardware;
    private Coupon coupon;
    private Order myOrder;
    private Detection detection;
    private final int request = 1030;
    private PackageBean pak;
    //总计
    private String total = "0";
    private MyGridViewPhotoAdpter adpter;
    private List<String> urlStrs = new ArrayList();
    private HardwareDetailAdapter hardwareDetailAdapter;
    private String deviceTypeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_quote_affirm;
    }

    @Override
    protected void findViews() {
        setTitle("故障确认单");
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initGetData() {
        myOrder = (Order) getIntent().getSerializableExtra("data");
        deviceTypeMode =  getIntent().getStringExtra("deviceTypeMode");
        setRight("收费标准", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(deviceTypeMode)){
                    ToastUtil.showToast(QuoteAffirmActivity.this,"数据获取失败！请稍后使用");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("deviceTypeMold",deviceTypeMode);
                bundle.putString("title", "收费标准");
                IntentUtil.gotoActivity(QuoteAffirmActivity.this, FeeStandardActivity.class, bundle);
            }
        });
        getDetectionInfo();
    }

    @Override
    protected void widgetListener() {
    }

    /**
     * 获取报价单
     */
    private void getDetectionInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("order_id", myOrder.getOrderId());
        HttpRequestUtils.httpRequest(this, "查询检测详情", RequestValue.GET_DETECTION_INFO, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        detection = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), Detection.class);
                        if (detection == null) {
                            ToastUtil.showLongToast(QuoteAffirmActivity.this, "数据异常");
                            onBackPressed();
                            return;
                        }
                        pak = new PackageBean();
                        pak.setPackageId(detection.getPackageId());
                        pak.setPackageType(detection.getPackageType());
                        coupon = new Coupon();
                        coupon.setCouponId(detection.getCouponId());
                        coupon.setDiscountAmount(detection.getDiscountAmount());
                        coupon.setCouponType(detection.getCouponType());
                        setDeviceDetailInfor();
                        break;
                    default:
                        ToastUtil.showLongToast(QuoteAffirmActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                ToastUtil.showLongToast(QuoteAffirmActivity.this, msg);
            }
        });
    }

    /**
     * 设置页面数据展示
     */
    private void setDeviceDetailInfor() {
        int count = 0;

        total = "0";

        //显示账单费用
        String visitCost = "0";
        visitCost = detection.getVisitCost();
         //上门费
        if (!TextUtils.isEmpty(detection.getVisitCost()) && Integer.parseInt(detection.getVisitCost()) > 0) {
            rl_smf.setVisibility(View.VISIBLE);
            tv_smf.setText(Utils.getMoney(Utils.chu(detection.getVisitCost(), "100")) + " 元");
            tv_smf.setTextColor(Color.BLACK);
            count++;
        } else {
            rl_smf.setVisibility(View.VISIBLE);
            tv_smf.setText("已免除");
            tv_smf.setTextColor(Color.parseColor("#ff3a3a"));
        }
         //维修费
        String maintainCost = "0";
        if (!TextUtils.isEmpty(detection.getMaintainCost()) && Integer.parseInt(detection.getMaintainCost()) > 0) {
            rl_fwf.setVisibility(View.VISIBLE);
            tv_fwf.setText(Utils.getMoney(Utils.chu(detection.getMaintainCost(), "100")) + " 元");
            maintainCost = detection.getMaintainCost();
            count++;
            tv_titel.setText("维修价格");
        } else {
            rl_fwf.setVisibility(View.GONE);
        }
         //附加服务费
        String otherCost = "0";
        if (!TextUtils.isEmpty(detection.getOtherCost()) && Integer.parseInt(detection.getOtherCost()) > 0) {
            rl_qtf.setVisibility(View.VISIBLE);
            otherCost = detection.getOtherCost();
            count++;
            tv_qtf.setText(Utils.getMoney(Utils.chu(detection.getOtherCost(), "100")) + " 元");
        } else {
            rl_qtf.setVisibility(View.GONE);
        }
        //硬件费用
        Double hardwareCostD = 0d;
        List<PartsDetailBean> accessories = detection.getAccessories();
        String hardwareCostStr = detection.getHardwareCost();
        if(accessories != null && accessories.size() > 0 && !TextUtils.isEmpty(hardwareCostStr)){
            rl_yjf.setVisibility(View.VISIBLE);
            count++;

            hardwareCostD = Double.parseDouble(hardwareCostStr.trim());

            tv_yjf.setText(Utils.getMoney(String.valueOf(hardwareCostD/100)) + " 元");

            rl_hardware.setVisibility(View.VISIBLE);

            hardwareDetailAdapter = new HardwareDetailAdapter(this,accessories,R.layout.item_hardware_detail,true);
            rv_hardware.setLayoutManager(new LinearLayoutManager(this));
            rv_hardware.setAdapter(hardwareDetailAdapter);
        } else {
            rl_yjf.setVisibility(View.GONE);
            rl_hardware.setVisibility(View.GONE);
        }
        //备注
        if (!TextUtils.isEmpty(detection.getRemark())) {
            rl_remark.setVisibility(View.VISIBLE);
            tv_remark.setText(detection.getRemark());
        } else {
            rl_remark.setVisibility(View.GONE);
        }

        //判断套餐显示
        if (pak != null && !TextUtils.isEmpty(pak.getPackageId()) && pak.getPackageType() > 0) {
            String showMoney = "";
            if (pak.getPackageType() == 1) {
                showMoney = "-" + Utils.chu(visitCost, "100") ;
                visitCost = "0";
            } else if (pak.getPackageType() == 2) {
                showMoney = "-" + Utils.chu(maintainCost, "100");
                maintainCost = "0";
            } else if (pak.getPackageType() == 3) {
                showMoney = "-" + Utils.chu(Utils.jia(maintainCost,visitCost), "100");
                visitCost = "0";
                maintainCost = "0";
            }
            tv_pak.setText(Utils.getMoney(showMoney)+ "元");
        } else if (pak != null && pak.isOnSelect()) {
             tv_pak.setText("不使用套餐减免");
        } else {
            tv_pak.setText("暂无可用套餐");
           // tv_pak.setTextColor(Color.parseColor("#ff868686"));
           // rl_pak.setClickable(false);
        }

        Double maintainCostInt = Double.parseDouble(maintainCost);
        Double otherCostInt = Double.parseDouble(otherCost);
        Double visitCostInt = Double.parseDouble(visitCost);
       // Double hardwareCostInt = Double.parseDouble(hardwareCost);
        Double totalInt = visitCostInt + maintainCostInt + hardwareCostD + otherCostInt;
        Double lastTotalInt = 0d;
        if (coupon != null && !TextUtils.isEmpty(coupon.getDiscountAmount()) && Integer.parseInt(coupon.getDiscountAmount()) > 0) {

            //优惠额度
            Double discountAmountInt = Double.parseDouble(coupon.getDiscountAmount());
            Double discountAmountIntP = discountAmountInt * 100;

            Double lastDiscount = discountAmountIntP;
            //优惠卷类型(1:通用 2:上门优惠 3:软件优惠 4:硬件优惠)
            if(coupon.getCouponType() == 1){
                //先减上门费
                if(lastDiscount > visitCostInt){
                    lastDiscount = lastDiscount - visitCostInt;
                    visitCostInt = 0d;
                }else{
                    visitCostInt = visitCostInt - lastDiscount;
                    lastDiscount = 0d;
                }

                //减维修费
                if(lastDiscount > maintainCostInt){
                    lastDiscount = lastDiscount - maintainCostInt;
                    maintainCostInt = 0d;

                }else{
                    maintainCostInt = maintainCostInt - lastDiscount;
                    lastDiscount = 0d;
                }

                //减服务附加费
                if(lastDiscount > otherCostInt){
                    lastDiscount = lastDiscount - otherCostInt;
                    otherCostInt = 0d;

                }else{
                    otherCostInt = otherCostInt - lastDiscount;
                    lastDiscount = 0d;
                }

                //减硬件费用
                if(lastDiscount > hardwareCostD){
                    lastDiscount = lastDiscount - hardwareCostD;
                    hardwareCostD = 0d;
                }else{
                    hardwareCostD = hardwareCostD - lastDiscount;
                    lastDiscount = 0d;
                }
            }else if(coupon.getCouponType() == 2){
                if(lastDiscount > visitCostInt) {
                    lastDiscount = lastDiscount - visitCostInt;
                    visitCostInt = 0d;
                }else{
                    visitCostInt = visitCostInt - lastDiscount;
                    lastDiscount = 0d;
                }
            }else if(coupon.getCouponType() == 3){
                //减维修费
                if(lastDiscount > maintainCostInt){
                    lastDiscount = lastDiscount - maintainCostInt;
                    maintainCostInt = 0d;
                }else{
                    maintainCostInt = maintainCostInt - lastDiscount;
                    lastDiscount = 0d;
                }
                //减服务附加费
                if(lastDiscount > otherCostInt){
                    lastDiscount = lastDiscount - otherCostInt;
                    otherCostInt = 0d;
                }else{
                    otherCostInt = otherCostInt - lastDiscount;
                    lastDiscount = 0d;
                }
            }else if(coupon.getCouponType() == 4){
                //减硬件费用
                if(lastDiscount > hardwareCostD){
                    lastDiscount = lastDiscount - hardwareCostD;
                    hardwareCostD = 0d;
                }else{
                    hardwareCostD = hardwareCostD - lastDiscount;
                    lastDiscount = 0d;
                }
            }

            totalInt = visitCostInt + maintainCostInt + hardwareCostD + otherCostInt;
            if(discountAmountIntP == lastDiscount){
                tv_amount.setText("不使用优惠券！！");
            }else{
                tv_amount.setText("-" + Utils.getMoney(Utils.chu(String.valueOf(discountAmountIntP-lastDiscount),"100")) + "元");
            }

            String str = "总计 ( " + count + " ): " + Utils.getMoney(String.valueOf(totalInt/100)) + " 元";
            tv_total.setText(Utils.getSpannableStringBuilder(str.indexOf(":") + 1, str.length() - 1, getResources().getColor(R.color.red_money), str, 15));


           /* if(lastTotalInt >= 0){ //优惠券小于等于总价
                tv_amount.setText("-" + String.valueOf(discountAmountInt) + "元");
                String str = "总计 ( " + count + " ): " + Utils.chu(String.valueOf(lastTotalInt), "100") + " 元";
                tv_total.setText(Utils.getSpannableStringBuilder(str.indexOf(":") + 1, str.length() - 1, getResources().getColor(R.color.red_money), str, 15));
            }else{ //
                tv_amount.setText("-" + String.valueOf(discountAmountInt) + "元");
                String str = "总计 ( " + count + " ):0元";
                tv_total.setText(Utils.getSpannableStringBuilder(str.indexOf(":") + 1, str.length() - 1, getResources().getColor(R.color.red_money), str, 15));
            }*/

           /* if (coupon.getCouponType() == 1) {
                if (hardwareCostInt >= discountAmountInt) {
                    hardwareCost = Utils.jian(hardwareCost, discountAmount);
                } else if (maintainCostInt >= discountAmountInt) {
                    maintainCost = Utils.jian(maintainCost, discountAmount);
                } else if (otherCostInt >= discountAmountInt) {
                    otherCost = Utils.jian(otherCost, discountAmount);
                } else if (visitCostInt >= discountAmountInt) {
                    visitCost = Utils.jian(visitCost, discountAmount);
                } else if ((hardwareCostInt + maintainCostInt + otherCostInt + visitCostInt) >= discountAmountInt) {
                    int yu = discountAmountInt;
                    if (hardwareCostInt > 0) {
                        yu = Integer.parseInt(Utils.jian(discountAmount, hardwareCost));
                        hardwareCost = "0";
                    }
                    if (maintainCostInt > 0 && maintainCostInt >= yu) {
                        maintainCost = Utils.jian(maintainCost, yu + "");
                        yu = 0;
                    } else {
                        yu = Integer.parseInt(Utils.jian("" + yu, maintainCost));
                        maintainCost = "0";
                    }
                    if (otherCostInt > 0 && otherCostInt >= yu) {
                        otherCost = Utils.jian(otherCost, yu + "");
                        yu = 0;
                    } else {
                        yu = Integer.parseInt(Utils.jian("" + yu, otherCost));
                        otherCost = "0";
                    }
                    if (visitCostInt > 0 && visitCostInt >= yu) {
                        visitCost = Utils.jian(visitCost, yu + "");
                        yu = 0;
                    } else {
                        yu = Integer.parseInt(Utils.jian("" + yu, visitCost));
                        visitCost = "0";
                    }
                } else {
                    coupon = new Coupon();
                    tv_amount.setText("不使用优惠券");
                }
            } else if (coupon.getCouponType() == 2) {
                if (visitCostInt >= discountAmountInt) {
                    visitCost = Utils.jian(visitCost, discountAmount);
                } else {
                    if (pak == null || TextUtils.isEmpty(pak.getPackageId())) {
                        ToastUtil.showToast(QuoteAffirmActivity.this, "优惠券金额大于订单金额！");
                    } else {
                        ToastUtil.showToast(QuoteAffirmActivity.this, "套餐减免已包含优惠券减免金额！");
                    }
                    coupon = new Coupon();
                    tv_amount.setText("不使用优惠券");
                }
            } else if (coupon.getCouponType() == 3) {
                if (maintainCostInt > otherCostInt && maintainCostInt >= discountAmountInt) {
                    maintainCost = Utils.jian(maintainCost, discountAmount);
                } else if (otherCostInt >= discountAmountInt) {
                    otherCost = Utils.jian(otherCost, discountAmount);
                } else if (maintainCostInt < discountAmountInt && otherCostInt < discountAmountInt && Integer.parseInt(Utils.jia(maintainCost, otherCost)) >= discountAmountInt) {
                    String yu = Utils.jian(discountAmount, maintainCost);
                    otherCost = Utils.jian(otherCost, yu);
                    maintainCost = "0";
                } else {
                    if (pak == null || TextUtils.isEmpty(pak.getPackageId())) {
                        ToastUtil.showToast(QuoteAffirmActivity.this, "优惠券金额大于订单软件维修金额！");
                    } else {
                        ToastUtil.showToast(QuoteAffirmActivity.this, "套餐减免已包含优惠券减免金额！");
                    }
                    coupon = new Coupon();
                    tv_amount.setText("不使用优惠券");
                }
            } else if (coupon.getCouponType() == 4) {
                if (hardwareCostInt >= discountAmountInt) {
                    hardwareCost = Utils.jian(hardwareCost, discountAmount);
                } else {
                    ToastUtil.showToast(QuoteAffirmActivity.this, "所选硬件优惠券金额大于订单硬件更换金额！");
                    coupon = new Coupon();
                    tv_amount.setText("不使用优惠券");
                }
            }*/

        } else if (coupon != null &&  coupon.isOnSelect()) {
            tv_amount.setText("不使用优惠券");
            String str = "总计 ( " + count + " ): " + Utils.getMoney(Utils.chu(String.valueOf(totalInt), "100"))+ " 元";
            tv_total.setText(Utils.getSpannableStringBuilder(str.indexOf(":") + 1, str.length() - 1, getResources().getColor(R.color.red_money), str, 15));
        } else {
            tv_amount.setText("暂无可用优惠券");
            tv_amount.setTextColor(Color.parseColor("#ff868686"));
            rl_coupon.setClickable(false);
            String str = "总计 ( " + count + " ): " + Utils.getMoney(Utils.chu(String.valueOf(totalInt), "100")) + " 元";
            tv_total.setText(Utils.getSpannableStringBuilder(str.indexOf(":") + 1, str.length() - 1, getResources().getColor(R.color.red_money), str, 15));
        }


       /* if (!TextUtils.isEmpty(visitCost) && visitCostInt > 0) {
            total = Utils.jia(total, visitCost);
        }
        if (!TextUtils.isEmpty(maintainCost) && maintainCostInt > 0) {
            total = Utils.jia(total, maintainCost);
        }
        if (otherCostInt > 0) {
            total = Utils.jia(total, otherCost);
        }
        if (!TextUtils.isEmpty(hardwareCost) && hardwareCostInt > 0) {
            total = Utils.jia(total, hardwareCost);
        }*/

        if (!TextUtils.isEmpty(detection.getDescribeImages())) {
            String[] arr = detection.getDescribeImages().split(",");
            urlStrs.clear();
            if (arr.length > 0) {
                for (int i = 0; i < arr.length; i++) {
                    urlStrs.add(arr[i]);
                }
                adpter = new MyGridViewPhotoAdpter(this, urlStrs);
                adpter.setIsShowDel(false);
                myGridview.setAdapter(adpter);
                myGridview.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去掉默认点击背景
                myGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(QuoteAffirmActivity.this, ImageBrowseActivity.class);
                        intent.putExtra("index", position);
                        intent.putStringArrayListExtra("list", (ArrayList) adpter.getLists());
                        startActivity(intent);
                    }
                });
            } else {
                ll_photo.setVisibility(View.GONE);
            }
        } else {
            ll_photo.setVisibility(View.GONE);
        }
        tv_desc.setText(detection.getFaultDescribe());
        if (detection.getOrderFaults() != null && detection.getOrderFaults().size() > 0) {
            ll_jjfa.removeAllViews();
            for (int i = 0; i < detection.getOrderFaults().size(); i++) {
                SolutionItem reItem = new SolutionItem(this);
                reItem.initData(detection.getOrderFaults().get(i));
                ll_jjfa.addView(reItem);
            }
        }
    }

    @OnClick({R.id.rl_coupon, R.id.rl_pak, R.id.btnRefuse, R.id.btnAffirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnRefuse:
                if (!TextUtils.isEmpty(detection.getVisitCost()) && Integer.parseInt(detection.getVisitCost()) > 0) {
                    DialogUtil.showMessageDg(QuoteAffirmActivity.this, "温馨提示", "确定要拒绝维修吗？拒绝维修\n您将要支付工程师上门费" + Utils.chu(detection.getReVisitCost(), "100") + "元", new CustomDialog.OnClickListener() {

                        @Override
                        public void onClick(CustomDialog dialog, int id, Object object) {
                            dialog.dismiss();
                            Bundle bundle = new Bundle();
                            detection.setOrderId(myOrder.getOrderId());
                            bundle.putSerializable("detection", detection);
                            IntentUtil.gotoActivityForResult(QuoteAffirmActivity.this, CannotMaintainActivity.class, bundle, request);
                        }
                    });
                } else {
                    detection.setOrderId(myOrder.getOrderId());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("detection", detection);
                    IntentUtil.gotoActivityForResult(this, CannotMaintainActivity.class, bundle, request);
                }
                break;
            case R.id.btnAffirm:
                Bundle bundle2 = new Bundle();
                bundle2.putString("operate", "2");
                detection.setOrderId(myOrder.getOrderId());
                bundle2.putSerializable("data", detection);
                bundle2.putSerializable("coupon", coupon);
                bundle2.putSerializable("package", pak);
                IntentUtil.gotoActivityForResult(this, PayActivity.class, bundle2, 1060);
                break;
            case R.id.rl_coupon:
                Bundle bundle3 = new Bundle();
                bundle3.putString("orderId", myOrder.getOrderId());
                if (pak != null && !TextUtils.isEmpty(pak.getPackageId())) {
                    bundle3.putString("packageId", pak.getPackageId());
                }
                IntentUtil.gotoActivityForResult(this, SelectPackageListActivity.class, bundle3, 1040);
                break;
            case R.id.rl_pak:
                Bundle bundle4 = new Bundle();
                bundle4.putString("orderId", myOrder.getOrderId());
                if (coupon != null && !TextUtils.isEmpty(coupon.getCouponId())) {
                    bundle4.putString("couponId", coupon.getCouponId());
                }
                bundle4.putInt("type", 1);
                IntentUtil.gotoActivityForResult(this, SelectPackageListActivity.class, bundle4, 1050);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request && resultCode == RESULT_OK) {
            onBackPressed();
        } else if (requestCode == 1040 && resultCode == RESULT_OK) {
            coupon = (Coupon) data.getSerializableExtra("coupon");
            if (coupon == null) {
                coupon = new Coupon();
            }
            setDeviceDetailInfor();
        } else if (requestCode == 1050 && resultCode == RESULT_OK) {
            pak = (PackageBean) data.getSerializableExtra("package");
            if (pak == null) {
                pak = new PackageBean();
            }
            setDeviceDetailInfor();
        }else if(requestCode == 1060 && resultCode == RESULT_OK){ //获取支付信息成功
            SharedPreferencesUtils.setParam(QuoteAffirmActivity.this,"isShare","0");
            SharedPreferencesUtils.setParam(QuoteAffirmActivity.this,"settlementStatus",2);
            new ShowImgDialog(this).init(new OnCommClickCallBack() {
                @Override
                public void onCommClick(Object object) {
                    if(object == null){
                        showShareIsPrize();
                    }else{
                       // SharedPreferencesUtils.setParam(QuoteAffirmActivity.this,"IntegralIsShow",0);
                        //showIntergralInfo();
                        finish();
                    }

                }
            }).show();
        }
    }



    private void showShareIsPrize(){
        HttpRequestUtils.httpRequest(QuoteAffirmActivity.this, "getShareData", RequestValue.GET_ADVERTISING+"?local=6&role=1",null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        ArrayList<ADInfo> infos = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<ADInfo>>() {});
                        ADInfo adInfo = infos.get(0);
                        if(myOrder != null){
                            ShareActivity.isOrderShare = true;
                            Bundle bundle = new Bundle();
                            bundle.putString("orderId",myOrder.getOrderId());
                            bundle.putString("shareContent","分享有奖");
                            bundle.putString("shareTitle","密修一下，维修到家");
                            bundle.putString("FLink",adInfo.getAdvertisingUrl());
                            bundle.putString("urlPhoto",adInfo.getAdvertisingImageUrl());
                            bundle.putString("goneQQ","true");
                            IntentUtil.gotoActivity(QuoteAffirmActivity.this, ShareActivity.class,bundle);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }
                        break;
                    default:
                        ToastUtil.showToast(QuoteAffirmActivity.this,"获取分享信息失败");
                        finish();
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showToast(QuoteAffirmActivity.this,"获取分享信息失败"+msg);
                finish();
            }
        });
    }
}
