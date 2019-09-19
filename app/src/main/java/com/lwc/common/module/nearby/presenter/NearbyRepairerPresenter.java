package com.lwc.common.module.nearby.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Repairman;
import com.lwc.common.module.nearby.ui.INearbyRepairerView;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 附近的维修员
 */
public class NearbyRepairerPresenter {

    private final String TAG = "NearbyRepairerPresenter";
    private INearbyRepairerView iNearbyRepairerView;
    private Activity activity;
    private Context context;
    private BGARefreshLayout mBGARefreshLayout;

    public NearbyRepairerPresenter(Context context, Activity activity, INearbyRepairerView iNearbyRepairerView, BGARefreshLayout mBGARefreshLayout) {
        this.context = context;
        this.activity = activity;
        this.iNearbyRepairerView = iNearbyRepairerView;
        this.mBGARefreshLayout = mBGARefreshLayout;
    }

    /**
     * 获取附近维修员
     */
    public void getNearbyRepairers(int curPage) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "1");
        map.put("curPage", curPage+"");
        HttpRequestUtils.httpRequest(activity, "getNearbyRepairers", RequestValue.NEAR_ENGINEER, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {

                    case "1":
                        try {
                            JSONObject object = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
                            int count = object.optInt("count");
                            List<Repairman> repairmans = JsonUtil.parserGsonToArray(object.optString("data"), new TypeToken<ArrayList<Repairman>>() {
                            });
                            iNearbyRepairerView.notifyData(repairmans, count);
                            iNearbyRepairerView.closeDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    default:
                        ToastUtil.showLongToast(context, common.getInfo());
                        iNearbyRepairerView.closeDialog();
                        break;
                }
                mBGARefreshLayout.endRefreshing();
                mBGARefreshLayout.endLoadingMore();
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.e(TAG, e.toString());
                ToastUtil.showNetErr(context);
                mBGARefreshLayout.endRefreshing();
                iNearbyRepairerView.closeDialog();
                mBGARefreshLayout.endLoadingMore();
            }
        });
    }
}
