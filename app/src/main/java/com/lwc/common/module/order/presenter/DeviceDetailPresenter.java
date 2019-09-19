package com.lwc.common.module.order.presenter;

import android.app.Activity;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.AfterService;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.order.ui.IDeviceDetailFragmentView;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 设备详情
 */
public class DeviceDetailPresenter {

    private final String TAG = "DeviceDetailPresenter";
    private IDeviceDetailFragmentView iDeviceDetailFragmentView;
    private Activity activity;
    public DeviceDetailPresenter(IDeviceDetailFragmentView iDeviceDetailFragmentView, Activity activity) {
        this.iDeviceDetailFragmentView = iDeviceDetailFragmentView;
        this.activity = activity;
    }

    /**
     * 更新售后订单信息
     * @param oid
     */
    public void updateOrderInfor(final String oid, int type, final BGARefreshLayout mBGARefreshLayout) {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", oid);
        String url = "";
        if (type == AfterService.STATUS_YISHENQING) {
            url = RequestValue.ORDER_WARRANTY_SAVE;
        } else if (type == AfterService.STATUS_YIJIESHOU) {
            url = RequestValue.ORDER_AFTER_CANCEL;
        } else if (type == AfterService.STATUS_WANGCHENGDAIQUEREN) {
            url = RequestValue.ORDER_AFTER_FINISH;
        }
        HttpRequestUtils.httpRequest(activity, "updateOrderInfo", url, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        getOrderInfor(oid, mBGARefreshLayout);
                        break;
                    default:
                        ToastUtil.showLongToast(activity, common.getInfo());
                        break;
                }
            }
            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(TAG, e.toString());
                ToastUtil.showLongToast(activity, msg);
                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
            }
        });
    }

    /**
     * 获取售后订单信息
     * @param oid
     */
    public void getOrderInfor(String oid, final BGARefreshLayout mBGARefreshLayout) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "2");
        HttpRequestUtils.httpRequest(activity, "getOrderState", RequestValue.ORDER_STATE+oid, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        List<AfterService> myOrders = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<AfterService>>() {
                        });
                        iDeviceDetailFragmentView.setDeviceDetailInfor(myOrders);
                        break;
                    default:
                        ToastUtil.showLongToast(activity, common.getInfo());
                        break;
                }
                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(TAG, e.toString());
                ToastUtil.showToast(activity, msg);
                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
            }
        });
    }
}
