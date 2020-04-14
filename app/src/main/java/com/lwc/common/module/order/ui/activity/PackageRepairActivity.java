package com.lwc.common.module.order.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Address;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.PackageBean;
import com.lwc.common.module.repairs.ui.activity.AddressManagerActivity;
import com.lwc.common.module.repairs.ui.activity.ApplyForMaintainActivity;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 套餐报修
 */
public class PackageRepairActivity extends BaseActivity {


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
    @BindView(R.id.rLayoutAddress)
    RelativeLayout rLayoutAddress;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_desc)
    TextView tv_desc;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_used)
    TextView tv_used;
    @BindView(R.id.et_my_desc)
    EditText et_my_desc;
    @BindView(R.id.btnConfirm)
    TextView btnConfirm;
    @BindView(R.id.tv_total_money)
    TextView tv_total_money;
    @BindView(R.id.lLayout0)
    LinearLayout lLayout0;
    @BindView(R.id.iv_recommend)
    ImageView iv_recommend;

    private List<Address> addressesList = new ArrayList<>();
    private Address checkedAdd;

    private PackageBean myPackage;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_package_repair_confirm;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void init() {

        setTitle("套餐报修");

        myPackage = (PackageBean) getIntent().getSerializableExtra("packageBean");

        //获取默认地址
        addressesList = DataSupport.findAll(Address.class);
        setDefaultAddress();

        if(myPackage != null){
            tv_title.setText(myPackage.getPackageName());
            String priceString = myPackage.getPackagePrice()+"元";
            tv_price.setText(Utils.getSpannableStringBuilder(priceString.length()-1, priceString.length(),getResources().getColor(R.color.black), priceString, 12));
            tv_desc.setText(myPackage.getRemarkT());


            String totalMoney ="￥" + myPackage.getPackagePrice();
            tv_total_money.setText(Utils.getSpannableStringBuilder(0, 1,getResources().getColor(R.color.red_money), totalMoney, 12));
            if("1".equals(myPackage.getPackageLabel())){
                iv_recommend.setVisibility(View.VISIBLE);
                iv_recommend.setImageResource(R.drawable.ic_red_recommend);
            }
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedAdd == null){
                    ToastUtil.showToast(PackageRepairActivity.this,"请添加地址");
                    return;
                }
                checkedAddress();
            }
        });
        rLayoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.gotoActivityForResult(PackageRepairActivity.this, AddressManagerActivity.class, INTENT_ADDRESS_MANAGER);
            }
        });
    }

    @Override
    protected void initGetData() {

    }

    @Override
    protected void widgetListener() {

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
           }else if(requestCode == 1602){
            /**
             * 支付成功就去报修
             */
            submitOrder();

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

    public void submitOrder() {

        JSONObject map = new JSONObject();
        try {
            map.put("addressId", checkedAdd.getUserAddressId());

           if(!TextUtils.isEmpty(myPackage.getDeviceId())){
               JSONArray jSONArray = new JSONArray();
               JSONObject o = new JSONObject();
               o.put("repairId", myPackage.getRepairId());
               o.put("deviceId", myPackage.getDeviceId());
               jSONArray.put(o);
               map.put("orderRepairs",jSONArray);
           }

            map.put("isSecrecy", "0");

           if(!TextUtils.isEmpty(et_my_desc.getText())){
               map.put("description", et_my_desc.getText());
           }

           map.put("deviceTypeMold", myPackage.getDeviceTypeMold());
           map.put("packageId",myPackage.getPackageId());
        } catch (Exception e) {
        }
        HttpRequestUtils.httpRequestNew(PackageRepairActivity.this, "submitOrder", RequestValue.SUBMIT_ORDER, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        ToastUtil.showLongToast(PackageRepairActivity.this, "报修成功");
                        finish();
                        IntentUtil.gotoActivity(PackageRepairActivity.this, MainActivity.class);
                        break;
                    default:
                        ToastUtil.showLongToast(PackageRepairActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showLongToast(PackageRepairActivity.this,msg);
            }
        });
    }

    /**
     * 检查城市
     */
    private void checkedAddress(){
        HashMap<String, String> map = new HashMap<>();
        map.put("packageId",myPackage.getPackageId());
        map.put("addressId",checkedAdd.getUserAddressId());
        HttpRequestUtils.httpRequest(PackageRepairActivity.this, "检查城市", RequestValue.JUDGEREGION, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                   String data = JsonUtil.getGsonValueByKey(response,"data");
                   String isValid = JsonUtil.getGsonValueByKey(data,"isValid");
                   String errorMsg = JsonUtil.getGsonValueByKey(data,"errorMsg");

                   if("1".equals(isValid)){

                       if(MainActivity.mainFragment.newestOrders != null && MainActivity.mainFragment.newestOrders.size() > 0){
                           ToastUtil.showToast(PackageRepairActivity.this,"你已有订单在进行中了");
                       }

                       Bundle bundle2 = new Bundle();
                       bundle2.putString("packageId", myPackage.getPackageId());
                       bundle2.putString("money", Utils.cheng(myPackage.getPackagePrice(), "100"));
                       IntentUtil.gotoActivityForResult(PackageRepairActivity.this, PayActivity.class, bundle2, 1602);
                   }else{
                       ToastUtil.showLongToast(PackageRepairActivity.this, errorMsg);
                   }

                }else{
                    ToastUtil.showLongToast(PackageRepairActivity.this, common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showLongToast(PackageRepairActivity.this,msg);
            }
        });
    }
}
