package com.lwc.common.module.order.presenter;

import android.app.Activity;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Order;
import com.lwc.common.module.order.ui.IOrderListFragmentView;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 我的订单列表 3个fragment的控制层
 */
public class OrderListPresenter {

    private final String TAG = "OrderListPresenter";

    private IOrderListFragmentView iOrderListView;
    private BGARefreshLayout mBGARefreshLayout;
    private Activity activity;
    public OrderListPresenter(IOrderListFragmentView iOrderListView, Activity activity) {
        this.iOrderListView = iOrderListView;
        mBGARefreshLayout = iOrderListView.getBGARefreshLayout();
        this.activity = activity;
    }

    /**
     * 获取订单列表
     *
     * @param pageNow
     * @param pageSize
     * @param type
     */
    public void getOrders(int pageNow, int pageSize, int type) {
        BGARefreshLayoutUtils.beginRefreshing(mBGARefreshLayout);
        Map<String, String> map = new HashMap<>();
        map.put("curPage", pageNow+"");
        map.put("pageSize", pageSize+"");
        map.put("type", type+"");
        HttpRequestUtils.httpRequest(activity, "getOrders", RequestValue.MY_ORDERS, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                LLog.iNetSucceed(TAG, response);
                String json = JsonUtil.getGsonValueByKey(response, "data");
                List<Order> myOrders = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(json, "data"), new TypeToken<ArrayList<Order>>() {
                });
                iOrderListView.notifyData(myOrders);
                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(TAG, e.toString());
                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
            }
        });
    }

    /**
     * 加载更多订单
     *
     * @param pageNow
     * @param pageSize
     * @param type
     */
    public void loadOrders(int pageNow, int pageSize, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("curPage", pageNow+"");
        map.put("pageSize", pageSize+"");
        map.put("type", type+"");
        HttpRequestUtils.httpRequest(activity, "getOrders", RequestValue.MY_ORDERS, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                String json = JsonUtil.getGsonValueByKey(response, "data");
                List<Order> myOrders = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(json, "data"), new TypeToken<ArrayList<Order>>() {
                });
                LLog.i(TAG, myOrders.toString());
                iOrderListView.addData(myOrders);
                BGARefreshLayoutUtils.endLoadingMore(mBGARefreshLayout);
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(TAG, e.toString());
                BGARefreshLayoutUtils.endRefreshing(mBGARefreshLayout);
            }
        });
    }
}
