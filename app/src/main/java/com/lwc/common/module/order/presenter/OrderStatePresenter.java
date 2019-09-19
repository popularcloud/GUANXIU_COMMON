package com.lwc.common.module.order.presenter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Order;
import com.lwc.common.module.bean.OrderState;
import com.lwc.common.module.order.ui.IOrderStateFragmentView;
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
 * 订单状态
 */
public class OrderStatePresenter {
    private final String TAG = "OrderStatePresenter";
    private IOrderStateFragmentView iOrderStateFragmentView;
    private BGARefreshLayout mBGARefreshLayout;
    private Activity context;

    public OrderStatePresenter(Activity context, IOrderStateFragmentView iOrderStateFragmentView) {
        this.iOrderStateFragmentView = iOrderStateFragmentView;
        this.context = context;
        mBGARefreshLayout = iOrderStateFragmentView.getBGARefreshLayout();
    }

    /**
     * 获取订单状态
     *
     * @param oid
     */
    public void getOrderState(String oid) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "1");
        HttpRequestUtils.httpRequest(context, "getOrderState", RequestValue.ORDER_STATE+oid, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {

                    case "1":
                        List<OrderState> orderStates = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<OrderState>>() {
                        });
                        iOrderStateFragmentView.notifyData(orderStates);
                        if (orderStates != null && orderStates.size() > 0) {
                            int lastSize = orderStates.size() - 1;
                            String lastTitle = orderStates.get(lastSize).getProcessTitle();
                            int state = orderStates.get(lastSize).getStatusId();  //订单状态，也就是标题
                            switch (state) {
                                //订单在这三种状态，设置状态按钮文字为取消订单，背景颜色为红色
                                case 1://待接单
                                    iOrderStateFragmentView.cutBottomButton(true);
                                    iOrderStateFragmentView.setFinishLayoutBtnName("取消", "催单");
                                    break;
                                case 2://已接单
                                    iOrderStateFragmentView.cutBottomButton(true);
                                    iOrderStateFragmentView.setFinishLayoutBtnName("取消", "催单");
//                                    iOrderStateFragmentView.setBottomButtonColour(Color.parseColor("#ff0000"));
                                    break;
                                case 8://已完成待确认
//                                    iOrderStateFragmentView.cutBottomButton(true);
//                                    iOrderStateFragmentView.setFinishLayoutBtnName("拒绝", "接受");
                                    iOrderStateFragmentView.cutBottomButton(false);
                                    iOrderStateFragmentView.setStateBtnText("确认完成");
                                    iOrderStateFragmentView.setBottomButtonColour(Color.parseColor("#2e344e"));
                                    break;
                                case 11://已完成 待评价
                                    //显示单按钮布局
//                                    iOrderStateFragmentView.cutBottomButton(false);
//                                    iOrderStateFragmentView.setStateBtnText("评价");
//                                    iOrderStateFragmentView.setBottomButtonColour(Color.parseColor("#2e344e"));
                                    iOrderStateFragmentView.cutBottomButton(true);
                                    iOrderStateFragmentView.setFinishLayoutBtnName("申诉", "评价");
                                    break;
                                case 6://已报价待确认
                                    iOrderStateFragmentView.cutBottomButton(true);
                                    iOrderStateFragmentView.setFinishLayoutBtnName("拒绝", "确认");
                                    break;
                                case Order.STATUS_JIANCEBAOJIA:
                                case Order.STATUS_FANCHANGBAOJIA:
                                    if (TextUtils.isEmpty(orderStates.get(lastSize).getOrderType()) || !orderStates.get(lastSize).getOrderType().equals("3")) {
                                        iOrderStateFragmentView.cutBottomButton(false);
                                        iOrderStateFragmentView.setStateBtnText("查看报价单");
                                        iOrderStateFragmentView.setBottomButtonColour(Color.parseColor("#2e344e"));
                                    }
                                    break;
                                case Order.STATUS_SONGHUIANZHUANG:
                                    iOrderStateFragmentView.cutBottomButton(false);
                                    iOrderStateFragmentView.setStateBtnText("确认送回");
                                    iOrderStateFragmentView.setBottomButtonColour(Color.parseColor("#2e344e"));
                                    break;
                                case Order.STATUS_SHENQINGFANCHANG:
//                                    iOrderStateFragmentView.cutBottomButton(true);
//                                    iOrderStateFragmentView.setFinishLayoutBtnName("拒绝返厂", "");
                                    if (!TextUtils.isEmpty(orderStates.get(lastSize).getOrderType()) && orderStates.get(lastSize).getOrderType().equals("1")) {
                                        iOrderStateFragmentView.cutBottomButton(true);
                                        iOrderStateFragmentView.setFinishLayoutBtnName("拒绝返厂", "同意返厂");
                                    } else {
                                        iOrderStateFragmentView.cutBottomButton(false);
                                        iOrderStateFragmentView.setStateBtnText("同意返厂");
                                        iOrderStateFragmentView.setBottomButtonColour(Color.parseColor("#2e344e"));
                                    }
                                    break;
                                default:
                                    //默认按钮只显示状态
                                    iOrderStateFragmentView.cutBottomButton(false);
                                    iOrderStateFragmentView.setStateBtnText(lastTitle);
                                    iOrderStateFragmentView.setBottomButtonColour(Color.parseColor("#9f9f9f"));
                                    break;
                            }
                        }
                        break;
                    default:
                        ToastUtil.showLongToast(context, common.getInfo());
                        break;
                }
                mBGARefreshLayout.endRefreshing();
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(TAG, e.toString());
                ToastUtil.showNetErr(context);
                mBGARefreshLayout.endRefreshing();
            }
        });
    }


    /**
     * 取消订单信息
     *
     * @param oId
     */
    public void cancelOrder(final String oId, String remark) {

        Map<String, String> map = new HashMap<>();
        String url = RequestValue.ORDER_CANCEL;
        if (remark.equals(""+Order.STATUS_SHENQINGFANCHANG)){
            url = RequestValue.POST_DETECTION_REFUSE+oId;
        } else {
            map.put("orderId", oId);
            map.put("remark", remark);
        }
        HttpRequestUtils.httpRequest(context, "cancelOrder", url, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        getOrderState(oId);
                        break;
                    default:
                        ToastUtil.showLongToast(context, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(TAG, e.toString());
                ToastUtil.showNetErr(context);
            }
        });
    }

    /**
     * 订单完成信息
     *
     * @param oId
     */
    public void finishOrder(final String oId) {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", oId);
        HttpRequestUtils.httpRequest(context, "finishOrder", RequestValue.ORDER_FINISH, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        getOrderState(oId);
                        break;
                    default:
                        ToastUtil.showLongToast(context, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(TAG, e.toString());
                ToastUtil.showNetErr(context);
            }
        });
    }

    /**
     * 用户拒绝订单
     *
     * @param oId
     */
    public void refuseOrder(final String oId, String msg) {
        Map<String, String> map = new HashMap<>();
        String url = RequestValue.ORDER_REFUSE;
        if (msg.equals(""+Order.STATUS_SHENQINGFANCHANG)){
            url = RequestValue.POST_DETECTION_TONGYI+oId;
        } else {
            map.put("orderId", oId);
            map.put("remark", msg);
        }

        HttpRequestUtils.httpRequest(context, "refuseOrder", url, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        getOrderState(oId);
                        break;
                    default:
                        ToastUtil.showLongToast(context, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(TAG, e.toString());
                ToastUtil.showNetErr(context);
            }
        });
    }

    /**
     * 订单完成信息
     *
     * @param oId
     */
    public void replacementOrder(final String oId) {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", oId);
        HttpRequestUtils.httpRequest(context, "replacementOrder", RequestValue.ORDER_REPLACEMENT, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        getOrderState(oId);
                        break;
                    default:
                        ToastUtil.showLongToast(context, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(TAG, e.toString());
                ToastUtil.showNetErr(context);
            }
        });
    }

    /**
     * 投诉
     *
     * @param uId
     * @param msg
     */
    public void complaint(String uId, String msg) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uId);
        map.put("msg", msg);
        HttpRequestUtils.httpRequest(context, "complaint", RequestValue.COMPLAINT, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "10000":
                        ToastUtil.showToast(context, "投诉成功");
                        break;
                    default:
                        ToastUtil.showLongToast(context, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(TAG, e.toString());
                ToastUtil.showNetErr(context);
            }
        });
    }
}
