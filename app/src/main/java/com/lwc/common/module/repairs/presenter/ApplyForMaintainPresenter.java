package com.lwc.common.module.repairs.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Malfunction;
import com.lwc.common.module.bean.User;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.ProgressUtils;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 申请报修
 */
public class ApplyForMaintainPresenter {

    private final String TAG = "ApplyForMaintainPresenter";
    private Context context;
    private Activity activity;
    private ProgressUtils progressUtils;
    private SharedPreferencesUtils preferencesUtils;
    private User user;

    public ApplyForMaintainPresenter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        progressUtils = new ProgressUtils();

        preferencesUtils = SharedPreferencesUtils.getInstance(context);
        user = preferencesUtils.loadObjectData(User.class);
    }

    /**
     * 提交订单
     *
     * @param ua_id      用户报修地址
     * @param is_secrecy 是否认证
     * @param images     图片
     * @param remark     备注
     * @param courier_number     快递单号
     * @param device_type_mold   设备类型模式
     */
    public void submitOrder(ArrayList<Malfunction> malList, String parent_cid, String ua_id,
                            String is_secrecy, String images, final String remark, String qrcodeIndex,String courier_number,String device_type_mold) {

        progressUtils.showCustomProgressDialog(context);
        JSONObject map = new JSONObject();
        try {
            if (!TextUtils.isEmpty(parent_cid)) {
                map.put("companyId", parent_cid);
            }
            map.put("addressId", ua_id);

            JSONArray jSONArray = new JSONArray();
            for (int i = 0; i < malList.size(); i++) {
                Malfunction m = malList.get(i);
                JSONObject o = new JSONObject();
                o.put("repairId", m.getReqairId());
                o.put("deviceId", m.getDid());
                jSONArray.put(o);
            }
            map.put("orderRepairs", jSONArray);
            if (!TextUtils.isEmpty(is_secrecy)) {
                map.put("isSecrecy", is_secrecy);
            }
            if (!TextUtils.isEmpty(images)) {
                map.put("image", images);
            }
            if (!TextUtils.isEmpty(remark)) {
                map.put("description", remark);
            }
            if (!TextUtils.isEmpty(qrcodeIndex)) {
                map.put("qrcodeIndex", qrcodeIndex);
            }
            if (!TextUtils.isEmpty(courier_number)) {
                map.put("courier_number", courier_number);
            }
            if (!TextUtils.isEmpty(device_type_mold)) {
                map.put("deviceTypeMold", device_type_mold);
            }
        } catch (Exception e) {
        }
        HttpRequestUtils.httpRequestNew(activity, "submitOrder", RequestValue.SUBMIT_ORDER, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        ToastUtil.showLongToast(context, "报修成功");
                        activity.setResult(activity.RESULT_OK);
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
                progressUtils.dismissCustomProgressDialog();
                LLog.iNetSucceed(TAG, e.toString());
                ToastUtil.showNetErr(context);
            }
        });
    }
}
