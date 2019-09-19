package com.lwc.common.module.repairs.presenter;

import android.app.Activity;
import android.content.Context;

import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Address;
import com.lwc.common.module.bean.Common;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.ProgressUtils;
import com.lwc.common.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 */
public class AddAddressPresenter {

    private final String TAG = "AddressManagerPresenter";
    private Context context;
    private Activity activity;
    private ProgressUtils progressUtils;


    public AddAddressPresenter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        progressUtils = new ProgressUtils();

    }

    public void addUserAddress(String contact_name, String contact_phone, String contact_address,
                               String province_id, String city_id, String town_id, double latitude, double longitude) {
        progressUtils.showCustomProgressDialog(context);
        Map<String, String> map  = new HashMap<>();
        map.put("contact_name", contact_name);
        map.put("contact_phone", contact_phone);
        map.put("contact_address", contact_address);
        if (province_id != null){
            map.put("province_id", province_id);
        }
        if (city_id != null){
            map.put("city_id", city_id);
        }
        if (town_id != null){
            map.put("town_id", town_id);
        }
        if (latitude != 0) {
            map.put("latitude", latitude+"");
            map.put("longitude", longitude+"");
        }
//        is_default       是否默认 0：否 1：是
        HttpRequestUtils.httpRequest(activity, "addUserAddress", RequestValue.ADD_OR_AMEND_ADDRESS, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        Address address = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), Address.class);
                        address.save();
                        ToastUtil.showToast(context, common.getInfo());
                        activity.finish();
                        break;
                    default:
                        ToastUtil.showLongToast(context, common.getInfo());
                        break;
                }
                progressUtils.dismissCustomProgressDialog();
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(TAG, e.toString());
                if (msg != null && !msg.equals("")) {
                    ToastUtil.showLongToast(context, msg);
                } else {
                    ToastUtil.showLongToast(context, e.toString());
                }
                progressUtils.dismissCustomProgressDialog();
            }
        });
    }
    private List<Address> addresses;
    public void upUserAddress(String ua_id, String contact_name, String contact_phone, String contact_address,
                              String province_id, String city_id, String town_id, double latitude, double longitude) {
        progressUtils.showCustomProgressDialog(context);

        Map<String, String> map = new HashMap<>();
        map.put("user_address_id", ua_id);
        map.put("contact_name", contact_name);
        map.put("contact_phone", contact_phone);
        map.put("contact_address", contact_address);
        if (province_id != null){
            map.put("province_id", province_id);
        }
        if (city_id != null){
            map.put("city_id", city_id);
        }
        if (town_id != null){
            map.put("town_id", town_id);
        }
        if (latitude != 0) {
            map.put("latitude", latitude+"");
            map.put("longitude", longitude+"");
        }
        HttpRequestUtils.httpRequest(activity, "upUserAddress", RequestValue.ADD_OR_AMEND_ADDRESS, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {

                    case "1":
                        ToastUtil.showToast(context, common.getInfo());
                        addresses = DataSupport.findAll(Address.class);
                        Address address = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), Address.class);
                        for (int i = 0; i < addresses.size(); i++){
                            Address tempAddress = addresses.get(i);
                            if (tempAddress.getUserAddressId() == address.getUserAddressId()){
                                address.update(i);
                            }
                        }
                        activity.finish();
                        break;
                    default:
                        ToastUtil.showLongToast(context, common.getInfo());
                        break;
                }
                progressUtils.dismissCustomProgressDialog();
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(TAG, e.toString());
                if (msg != null && !msg.equals("")) {
                    ToastUtil.showLongToast(context, msg);
                } else {
                    ToastUtil.showNetErr(context);
                }
                progressUtils.dismissCustomProgressDialog();
            }
        });
    }
}
