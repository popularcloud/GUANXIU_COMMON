package com.lwc.common.module.lease_parts.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.bean.WxBean;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Address;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.lease_parts.adapter.LeaseConfirmAdapter;
import com.lwc.common.module.lease_parts.bean.ShopCarBean;
import com.lwc.common.module.repairs.ui.activity.AddressManagerActivity;
import com.lwc.common.module.wallet.ui.PayPwdActivity;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.PayServant;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.AliPayCallBack;
import com.lwc.common.widget.CustomDialog;
import com.lwc.common.widget.InvoiceInformationDialog;
import com.lwc.common.widget.PayTypeDialog;

import org.byteam.superadapter.OnItemClickListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 确认租赁订单
 */
public class ConfirmLeaseOrderActivity extends BaseActivity {


    /**
     * 跳转地址管理
     */
    private final int INTENT_ADDRESS_MANAGER = 1000;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtPhone)
    TextView txtPhone;
    @BindView(R.id.btnConfirm)
    TextView btnConfirm;
    @BindView(R.id.rLayoutAddress)
    RelativeLayout rLayoutAddress;

    @BindView(R.id.lLayout0)
    LinearLayout lLayout0;

    @BindView(R.id.rv_goods)
    RecyclerView rv_goods;

    @BindView(R.id.tv_total_money)
    TextView tv_total_money;
    @BindView(R.id.tv_discount_money)
    TextView tv_discount_money;

    @BindView(R.id.tv_invoice)
    TextView tv_invoice;

    @BindView(R.id.tv_total_money_two)
    TextView tv_total_money_two;

    private LeaseConfirmAdapter adapter;
    List<ShopCarBean> submitBeans;

    private Map<String,String> invoiceParams;

    private List<Address> addressesList = new ArrayList<>();
    private Address checkedAdd;

    private InvoiceInformationDialog invoiceInformationDialog;

    private PayTypeDialog payTypeDialog;
    //实付金额
    private String payPrice;

    private SharedPreferencesUtils preferencesUtils = null;
    private User user = null;
    private Dialog dialog;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_confirm_lease_order;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        preferencesUtils = SharedPreferencesUtils.getInstance(this);
        user = preferencesUtils.loadObjectData(User.class);
    }

    @Override
    protected void init() {

        setTitle("确认订单");

        //获取默认地址
        addressesList = DataSupport.findAll(Address.class);
        setDefaultAddress();

        rLayoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.gotoActivityForResult(ConfirmLeaseOrderActivity.this, AddressManagerActivity.class, INTENT_ADDRESS_MANAGER);
            }
        });
        bindRecycleView();
    }

    private void getGoodActivity() {

        if(submitBeans == null || submitBeans.size() < 1){
            return;
        }

        Map<String,String> params = new HashMap<>();
        StringBuilder ids = new StringBuilder();
        StringBuilder counts = new StringBuilder();
        for(int i = 0;i < submitBeans.size(); i++){
            ShopCarBean shopCarBean = submitBeans.get(i);
            ids.append(shopCarBean.getGoodsId()+",");

            String oneMonthprices = Utils.cheng(shopCarBean.getGoodsPrice(),String.valueOf(shopCarBean.getGoodsNum()));

            if("1".equals(shopCarBean.getPayType())){
                counts.append(oneMonthprices+",");
            }else{
                counts.append(Utils.cheng(oneMonthprices,"3")+",");
            }
        }
        params.put("ids",ids.toString());
        params.put("counts",counts.toString());

        HttpRequestUtils.httpRequest(this, "查询商品优惠价格", RequestValue.LEASEMANAGE_GOODSACTIVITY, params, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    String data = JsonUtil.getGsonValueByKey(response,"data");
                    payPrice = JsonUtil.getGsonValueByKey(data,"payPrice");
                    String discountAmount = JsonUtil.getGsonValueByKey(data,"discountAmount");

                    if(!TextUtils.isEmpty(payPrice)){
                        String money = Utils.getMoney(Utils.chu(payPrice,"100"));
                        SpannableStringBuilder spannableStringBuilder = Utils.getSpannableStringBuilder(1,money.length() - 2,"￥"+ money,24,true);
                        tv_total_money_two.setText(spannableStringBuilder);
                    }

                    if(!TextUtils.isEmpty(discountAmount)){
                        tv_discount_money.setText("-￥"+Utils.getMoney(Utils.chu(discountAmount,"100")));
                    }

                }else{
                    ToastUtil.showToast(ConfirmLeaseOrderActivity.this,common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }

    @Override
    protected void initGetData() {

    }

    @Override
    protected void widgetListener() {

    }

    private void bindRecycleView() {

        String shopCarBeans = getIntent().getStringExtra("shopCarBeans");
        submitBeans = JsonUtil.parserGsonToArray(shopCarBeans,new TypeToken<ArrayList<ShopCarBean>>(){});

        getGoodActivity();
        rv_goods.setLayoutManager(new LinearLayoutManager(ConfirmLeaseOrderActivity.this));
        adapter = new LeaseConfirmAdapter(ConfirmLeaseOrderActivity.this, submitBeans, R.layout.item_confirm_order);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
            }
        });
        rv_goods.setAdapter(adapter);

        if(submitBeans != null){
            String total = "0";
            for(ShopCarBean shopCarBean : submitBeans){
                String singleMoney =Utils.cheng(shopCarBean.getGoodsPrice(),String.valueOf(shopCarBean.getGoodsNum()));
                if("2".equals(shopCarBean.getPayType())){
                    singleMoney = Utils.cheng(singleMoney,"3");
                }
                total = Utils.jia(total,singleMoney);
            }

            tv_total_money.setText("￥"+Utils.getMoney(Utils.chu(total,"100")));
        }
    }

    /**
     * 设置默认地址
     */
    private void setDefaultAddress() {
        for (int i = 0; i < addressesList.size(); i++) {
            Address address = addressesList.get(i);
            if (address.getIsDefault() == 1) {
                lLayout0.setVisibility(View.VISIBLE);
                checkedAdd = address;
                txtName.setText("" + checkedAdd.getContactName());
                txtPhone.setText(checkedAdd.getContactPhone());
                txtAddress.setText(checkedAdd.getProvinceName() + checkedAdd.getCityName()+ checkedAdd.getContactAddress().replace("_", ""));
                break;
            }
        }
        if(addressesList == null || addressesList.size() == 0){
            checkedAdd = null;
            lLayout0.setVisibility(View.GONE);
            txtName.setText("");
            txtPhone.setText("");
            txtAddress.setText("点击添加地址");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
             if(requestCode == INTENT_ADDRESS_MANAGER){
                checkedAdd = (Address) data.getSerializableExtra("address");
                txtName.setVisibility(View.VISIBLE);
                txtPhone.setVisibility(View.VISIBLE);
                txtName.setText("" + checkedAdd.getContactName());
                txtPhone.setText(checkedAdd.getContactPhone());
                txtAddress.setText("" + checkedAdd.getContactAddress().replace("_", ""));
           }else{
            if(requestCode == INTENT_ADDRESS_MANAGER){
                //获取默认地址
                addressesList = DataSupport.findAll(Address.class);
                setDefaultAddress();
            }
        }
        }else{
            //获取默认地址
            addressesList = DataSupport.findAll(Address.class);
            setDefaultAddress();
        }


    }

    @OnClick({R.id.ll_invoice,R.id.btnConfirm})
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.ll_invoice:
                if(TextUtils.isEmpty(payPrice)){
                    ToastUtil.showToast(ConfirmLeaseOrderActivity.this,"开票金额获取失败，请稍后重试");
                    return;
                }
                if(invoiceInformationDialog == null){
                    invoiceInformationDialog = new InvoiceInformationDialog(ConfirmLeaseOrderActivity.this, new InvoiceInformationDialog.CallBack() {
                        @Override
                        public void onSubmit(Map<String,String> params) {
                            invoiceInformationDialog.dismiss();
                            invoiceParams = params;
                            if(params != null){
                                tv_invoice.setTextColor(Color.parseColor("#000000"));
                                tv_invoice.setText("普通发票");
                            }else{
                                tv_invoice.setTextColor(Color.parseColor("#999999"));
                                tv_invoice.setText("不开发票");
                            }
                        }
                    },payPrice,"");
                    invoiceInformationDialog.show();
                }else{
                    invoiceInformationDialog.show();
                }

                break;
            case R.id.btnConfirm:

                switch (user.getIsCertification()){
                    case "0":
                    case "1":
                    case "3":
                        ToastUtil.showToast(ConfirmLeaseOrderActivity.this,"认证后才能下单！");
                        dialog = DialogUtil.dialog(ConfirmLeaseOrderActivity.this, "温馨提示", "前往认证", "取消", "认证后才能下单", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                IntentUtil.gotoActivity(ConfirmLeaseOrderActivity.this,LeaseMsgAuthenticationActivity.class);
                            }
                        }, null, true);
                        break;
                    case "2":
                        if (Utils.isFastClick(3000,"确认商品")) {
                            return;
                        }
                        submitOrder();
                        break;
                }
                break;
        }
    }


    private void submitOrder(){


/*        LinearLayout linearLayout = (LinearLayout) adapter.getView(0,null,null);

        EditText editText = linearLayout.findViewById(R.id.et_message);
        String message = editText.getText().toString();
        Log.d("联网成功!",message);*/


        String ids = submitBeans.get(0).getCarId();
        Map<String,String> params = new HashMap<>();

        if(TextUtils.isEmpty(ids)){ //
            ids = submitBeans.get(0).getGoodsId();
            params.put("goods_id",ids);
            params.put("goods_num",String.valueOf(submitBeans.get(0).getGoodsNum()));
            params.put("pay_type",submitBeans.get(0).getPayType());
            params.put("lease_mon_time",submitBeans.get(0).getLeaseMonTime());
            if(!TextUtils.isEmpty(submitBeans.get(0).getRemark())){
                params.put("user_message",submitBeans.get(0).getRemark());
            }

        }else{
            updateCar(submitBeans.get(0).getCarId(),submitBeans.get(0).getRemark());
            for(int i = 1;i < submitBeans.size();i++){
                ShopCarBean shopCarBean = submitBeans.get(i);
                ids = ids + ","+submitBeans.get(i).getCarId();

                if(!TextUtils.isEmpty(shopCarBean.getRemark())){
                    updateCar(shopCarBean.getCarId(),shopCarBean.getRemark());
                }

            }
            params.put("in_car_id",ids);
        }

        if(checkedAdd == null){
            ToastUtil.showToast(this,"请选择地址");
            return;
        }


        params.put("address_id",checkedAdd.getUserAddressId());

        if(invoiceParams != null){
            params.putAll(invoiceParams);
        }

        if (TextUtils.isEmpty(user.getPayPassword())) {
            DialogUtil.showMessageDg(this, "温馨提示", "您还未设置支付密码\n请先去设置支付密码！", new CustomDialog.OnClickListener() {

                @Override
                public void onClick(CustomDialog dialog, int id, Object object) {
                    dialog.dismiss();
                    IntentUtil.gotoActivity(ConfirmLeaseOrderActivity.this, PayPwdActivity.class);
                }
            });
            return;
        }

        HttpRequestUtils.httpRequest(this, "确认商品", RequestValue.LEASEMANAGE_ORDER_SAVE, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                   // ToastUtil.showToast(ConfirmLeaseOrderActivity.this,"提交成功!");
                    String orderId = JsonUtil.getGsonValueByKey(response,"data");
                    showPayInfo(orderId);
                }else{
                    ToastUtil.showToast(ConfirmLeaseOrderActivity.this,common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }

    /**
     * 更新购物车提交过来的备注
     */
    private void updateCar(String carId,String message){
        Map<String,String> params = new HashMap<>();
        params.put("car_id",carId);
        params.put("user_message",message);
        HttpRequestUtils.httpRequest(ConfirmLeaseOrderActivity.this, "修改购物车", RequestValue.LEASEMANAGE_MODLEASEGOODSCAR, params, "POST", new HttpRequestUtils.ResponseListener() {

            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        //ToastUtil.showToast(getActivity(),common.getInfo());
                        // mBGARefreshLayout.beginRefreshing();
                        break;
                    default:
                        ToastUtil.showToast(ConfirmLeaseOrderActivity.this,common.getInfo());
                        break;
                }

            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }

    private void showPayInfo(final String orderId){

        payTypeDialog = new PayTypeDialog(ConfirmLeaseOrderActivity.this,Utils.chu(payPrice,"100"), new PayTypeDialog.CallBack() {
            @Override
            public void onSubmit(final int payType, String passWord) {

                Map<String,String> params = new HashMap<>();
                params.put("transaction_means",String.valueOf(payType));
                params.put("orderId",orderId);
                if(passWord != null){
                    params.put("payPassword", Utils.md5Encode(passWord));
                }
                params.put("appType","user");

                HttpRequestUtils.httpRequest(ConfirmLeaseOrderActivity.this, "拉取支付信息", RequestValue.LEASEMANAGE_PAY_LEASEINFO, params, "POST", new HttpRequestUtils.ResponseListener() {
                    @Override
                    public void getResponseData(String response) {
                        Common common = JsonUtil.parserGsonToObject(response, Common.class);
                        if("1".equals(common.getStatus())){
                           // ToastUtil.showToast(ConfirmLeaseOrderActivity.this,"拉取支付信息成功!");

                            String data = JsonUtil.getGsonValueByKey(response,"data");
                            String integral = JsonUtil.getGsonValueByKey(data,"integral");
                            final Bundle bundle = new Bundle();
                            bundle.putString("integral",integral);
                            bundle.putString("price",payPrice);

                            switch (payType){//1钱包 2.支付宝 3.微信
                                case 1:
                                    payTypeDialog.hideError();
                                    payTypeDialog.dismiss();
                                    ToastUtil.showToast(ConfirmLeaseOrderActivity.this,"支付成功！");
                                    IntentUtil.gotoActivity(ConfirmLeaseOrderActivity.this,PaySuccessActivity.class,bundle);
                                    finish();
                                    break;
                                case 2:
                                    try {
                                        final JSONObject obj = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
                                        PayServant.getInstance().aliPay(obj, ConfirmLeaseOrderActivity.this, new AliPayCallBack() {
                                            @Override
                                            public void OnAliPayResult(boolean success, boolean isWaiting, String msg) {
                                                ToastUtil.showLongToast(ConfirmLeaseOrderActivity.this, msg);
                                                if (success) {
                                                    setResult(RESULT_OK);

                                                    ToastUtil.showToast(ConfirmLeaseOrderActivity.this,"支付成功！");
                                                    IntentUtil.gotoActivity(ConfirmLeaseOrderActivity.this,PaySuccessActivity.class,bundle);
                                                }
                                                finish();
                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 3:
                                    WxBean wx = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), WxBean.class);
                                    PayServant.getInstance().weChatPay2(
                                            ConfirmLeaseOrderActivity.this, wx.getAppid(),
                                            wx.getPartnerId(), wx.getPrepayId(), wx.getNonceStr(),
                                            wx.getTimeStamp(), wx.getPackageStr(), wx.getSign());
                                    break;
                            }
                        }else{
                            ToastUtil.showToast(ConfirmLeaseOrderActivity.this,common.getInfo());
                            payTypeDialog.showError(common.getInfo());
                        }
                    }

                    @Override
                    public void returnException(Exception e, String msg) {
                        LLog.eNetError(e.toString());
                    }
                });
               /* ToastUtil.showToast(ConfirmLeaseOrderActivity.this,"支付成功！");
                IntentUtil.gotoActivity(ConfirmLeaseOrderActivity.this,PaySuccessActivity.class);*/
            }

            @Override
            public void onColse() {
                finish();
                IntentUtil.gotoActivity(ConfirmLeaseOrderActivity.this, MyLeaseOrderListActivity.class);
            }
        });
        payTypeDialog.show();

        payTypeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
                IntentUtil.gotoActivity(ConfirmLeaseOrderActivity.this, MyLeaseOrderListActivity.class);
            }
        });
    }
}
