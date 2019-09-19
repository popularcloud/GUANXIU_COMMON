package com.lwc.common.module.order.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.bean.AfterService;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Order;
import com.lwc.common.module.bean.OrderState;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.common_adapter.OrderStateAdapter;
import com.lwc.common.module.integral.activity.IntegralMainActivity;
import com.lwc.common.module.mine.ShareActivity;
import com.lwc.common.module.order.presenter.OrderStatePresenter;
import com.lwc.common.module.order.ui.IOrderStateFragmentView;
import com.lwc.common.module.order.ui.activity.AppealOrderActivity;
import com.lwc.common.module.order.ui.activity.CannotMaintainActivity;
import com.lwc.common.module.order.ui.activity.EvaluateActivity;
import com.lwc.common.module.order.ui.activity.OrderDetailActivity;
import com.lwc.common.module.order.ui.activity.QuoteAffirmActivity;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.widget.CustomDialog;
import com.lwc.common.widget.DialogStyle1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 订单状态
 */
public class OrderStateFragment extends BaseFragment implements IOrderStateFragmentView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    //订单状态
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    @BindView(R.id.btn)
    TextView btn;
    @BindView(R.id.btnRepulse)
    TextView btnRepulse;
    @BindView(R.id.btnAccept)
    TextView btnAccept;
    @BindView(R.id.lLayoutDoubleButton)
    LinearLayout lLayoutDoubleButton;
    @BindView(R.id.ib_share)
    ImageButton ib_share;
    private OrderStateAdapter adapter;
    private List<OrderState> orderStates;
    private Order myOrder = null;
    private OrderStatePresenter presenter;
    private SharedPreferencesUtils preferencesUtils = null;
    private User user = null;
    private int state;
    private String orderIsShare;
    private String isShare;
    private String deviceTypeMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_state, null);
        ButterKnife.bind(this, view);
        BGARefreshLayoutUtils.initRefreshLayout(getContext(), mBGARefreshLayout, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEngines(view);
        getIntentData();
        init();
        bindRecycleView();
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBGARefreshLayout.beginRefreshing();//执行加载数据
    }

    private void bindRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderStateAdapter(getContext(), orderStates, R.layout.item_order_status);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void init() {
        user = preferencesUtils.loadObjectData(User.class);
    }

    @Override
    public void initEngines(View view) {
        presenter = new OrderStatePresenter(getActivity(), this);
        preferencesUtils = SharedPreferencesUtils.getInstance(getContext());
    }

    @Override
    public void getIntentData() {
        myOrder = (Order) getArguments().getSerializable("data");
    }

    @Override
    public void setListener() {
        //刷新控件监听器
        mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                getData();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                return false;
            }
        });
    }

    /**
     * 请求网络获取数据
     */
    private void getData() {
        presenter.getOrderState(myOrder.getOrderId());
    }

    @Override
    public void notifyData(List<OrderState> orderStates) {
        this.orderStates = orderStates;
        adapter.replaceAll(orderStates);
        if (orderStates != null && orderStates.size() > 0) {
            int lastSize = orderStates.size() - 1;
            state = orderStates.get(lastSize).getStatusId();
            if (state != Order.STATUS_DAIJIEDAN && state != Order.STATUS_YIQUXIAO && OrderDetailActivity.activity != null) {
                OrderDetailActivity.activity.setBohao(true);
            } else if (OrderDetailActivity.activity != null){
                OrderDetailActivity.activity.setBohao(false);
            }

            //获取订单详情数据
            getOrderInfo();

            //获取售后状态
            getDeviceDetailInfor(myOrder.getOrderId());
            recyclerView.scrollToPosition(orderStates.size()-1);

                //显示分享红包有奖  用户支付完成后的状态  18  11  12 13 3 8
                if(state == Order.STATUS_QUERENBAOJIA || state == Order.STATUS_YIWANCHENG
                        || state == Order.STATUS_YIPINGJIA   || state == Order.STATUS_SHOUHOU
                        || state == Order.STATUS_CHULI || state == Order.STATUS_YIWANCHENGDAIQUEREN){
                    ib_share.setVisibility(View.VISIBLE);
                    ib_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showShareIsPrize();
                        }
                    });
            }
          /*  int IntegralIsShow = (int) SharedPreferencesUtils.getParam(getActivity(),"IntegralIsShow",0);
            if(state == Order.STATUS_QUERENBAOJIA && IntegralIsShow == 0){
                showIntergralInfo();
                SharedPreferencesUtils.setParam(getActivity(),"IntegralIsShow",1);
            }*/
        }
    }


    private void showIntergralInfo(){
        String getIntegral = (String) SharedPreferencesUtils.getParam(getActivity(),"get_integral","0");
        DialogUtil.dialog(getActivity(), "温馨提示", "前往兑换", "下次再说", "支付成功,本次获得" + Utils.chu(getIntegral,"100")  + "积分,可前往积分商城兑换礼品",
                new View.OnClickListener() { // sure
                    @Override
                    public void onClick(View v) {
                        IntentUtil.gotoActivity(getActivity(), IntegralMainActivity.class);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().onBackPressed();
                    }
                }, true);
    }

    private void getDeviceDetailInfor(String oid){
        Map<String, String> map = new HashMap<>();
        map.put("type", "2");
        HttpRequestUtils.httpRequest(getActivity(), "getOrderStateInDetail", RequestValue.ORDER_STATE+oid, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        List<AfterService> myOrders = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<AfterService>>() {
                        });
                        setDeviceDetailInfor(myOrders);
                        break;
                    default:
                        ToastUtil.showLongToast(getActivity(), common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(OrderDetailActivity.class.getSimpleName(), e.toString());
                ToastUtil.showToast(getActivity(), msg);
            }
        });
    }

    public void setDeviceDetailInfor(List<AfterService> asList) {
        if (asList == null || asList.size()==0){
            //OrderDetailActivity.activity.setBohao(false);
            return;
        }
        AfterService afterService = asList.get(asList.size() - 1);
        //1:正常 2：申请返修 3：接受返修 4：开始处理 5：完成待确认 ',
        int type = afterService.getWarrantyState();
        if (type == AfterService.STATUS_CHULI || type == AfterService.STATUS_GUOQI) {
            if (type == AfterService.STATUS_GUOQI && OrderDetailActivity.activity != null){
                OrderDetailActivity.activity.setBohao(false);
            } else if (OrderDetailActivity.activity != null){
                OrderDetailActivity.activity.setBohao(true);
            }
        } else if (type == AfterService.STATUS_ZHENGCHANG || type == 6) {
            if (asList.get(0).getWarrantyState() == AfterService.STATUS_GUOQI){
                if (OrderDetailActivity.activity != null) {
                    OrderDetailActivity.activity.setBohao(false);
                }
            } else {
                if (OrderDetailActivity.activity != null) {
                    OrderDetailActivity.activity.setBohao(true);
                }
            }
        } else if (type == AfterService.STATUS_WANGCHENGDAIQUEREN ) {
            if (OrderDetailActivity.activity != null) {
                OrderDetailActivity.activity.setBohao(true);
            }
        } else if (type == AfterService.STATUS_YIJIESHOU || type == AfterService.STATUS_YISHENQING) {
            if (OrderDetailActivity.activity != null) {
                OrderDetailActivity.activity.setBohao(true);
            }
        }
    }

    private void getOrderInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", myOrder.getOrderId());
        HttpRequestUtils.httpRequest(getActivity(), "查询订单详情===>用于分享红包获取信息", RequestValue.POST_ORDER_INFO, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        Order thisOrder = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), Order.class);
                        deviceTypeMode = thisOrder.getDeviceTypeMold();
                        if (thisOrder == null) {
                            return;
                        }
                        // 保存订单分享状态  分享的时候用与判断
                        SharedPreferencesUtils.setParam(getContext(),"isShare",thisOrder.getIsShare());
                        SharedPreferencesUtils.setParam(getContext(),"settlementStatus",thisOrder.getSettlementStatus());
                        break;
                    default:
                        ToastUtil.showLongToast(getActivity(), common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }


    @Override
    public void setStateBtnText(String state) {
        btn.setText(state);
    }

    @OnClick({R.id.btn, R.id.btnRepulse, R.id.btnAccept})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn:
                switch (btn.getText().toString()) {
                    case "取消 ":
                        executeCancel("取消订单");
                        break;
                    case "评价":
                        executeEvaluate();
                        break;
                    case "查看报价单":
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", myOrder);
                        bundle.putString("deviceTypeMode", deviceTypeMode);
                        IntentUtil.gotoActivity(getContext(), QuoteAffirmActivity.class, bundle);
                        break;
                    case "投诉":
                        executeComplaint();
                        break;
                    case "确认完成":
                        presenter.finishOrder(myOrder.getOrderId());
                        break;
                    case "确认送回":
                    case "同意返厂":
                        presenter.refuseOrder(myOrder.getOrderId(), Order.STATUS_SHENQINGFANCHANG+"");
                        break;
                }
                break;
            //拒绝系列按钮
            case R.id.btnRepulse:
                switch (btnRepulse.getText().toString()) {
                    case "拒绝并返修":
//                        upDataOrder(user.getUserId() + "", myOrder.getOrderId() + "", "9");
                        break;
                    case "拒绝分级":
//                        upDataOrder(user.getUserId() + "", myOrder.getOrderId() + "", "3");
                        break;
                    case "拒绝":
//                        upDataOrder(user.getUserId() + "", myOrder.getOrderId() + "", "3");
                        executeCancel("拒绝订单");
                        break;
                    case "取消":
                        if (state == Order.STATUS_JIEDAN) {
                            executeCancel("取消订单");
                        } else {
                            presenter.cancelOrder(myOrder.getOrderId(), "");
                        }
                        break;
                    case "拒绝返厂":
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("order", myOrder);
                        IntentUtil.gotoActivity(getActivity(), CannotMaintainActivity.class, bundle2);
//                        presenter.cancelOrder(myOrder.getOrderId(), Order.STATUS_SHENQINGFANCHANG+"");
                        break;
                    case "申诉":
                        if (myOrder.getIsAppeal() == 1) {
                            DialogUtil.showMessageDg(getActivity(), "温馨提示", "联系客服", "该订单已提交过申诉信息，客服人员正在处理中，请耐心等待！如需加急可联系客服！", new CustomDialog.OnClickListener() {
                                @Override
                                public void onClick(CustomDialog dialog, int id, Object object) {
                                    dialog.dismiss();
                                    Utils.lxkf(getActivity(), null);
                                }
                            });
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", myOrder);
                            IntentUtil.gotoActivityForResult(getContext(), AppealOrderActivity.class, bundle, 2018);
                        }
                        break;
                }
                break;
            //同意系列按钮
            case R.id.btnAccept:
                switch (btnAccept.getText().toString()) {
                    case "评价":
//                        DialogUtil.showMessageDg(getActivity(), "温馨提示", "去评价", "评价完成后，订单将无法申诉，在保修期内的订单可以申请售后，是否去评价？", new CustomDialog.OnClickListener() {
//
//                            @Override
//                            public void onClick(CustomDialog dialog, int id, Object object) {
                                executeEvaluate();
//                                dialog.dismiss();
//                            }
//                        });
                        break;
                    case "同意返厂":
                        presenter.refuseOrder(myOrder.getOrderId(), Order.STATUS_SHENQINGFANCHANG+"");
                        break;
                    case "接受":
                        presenter.finishOrder(myOrder.getOrderId());
                        break;
                    case "催单":
                        pressOrder();
                        break;
                    case "同意分级":
                        presenter.replacementOrder(myOrder.getOrderId());
                        break;
                    case "确认":
                        DialogUtil.showMessageDg(getActivity(), "风险提示", "同意", "如果您同意了工程师的报价，那\n么将不允许退货，且责任自行承担！", new CustomDialog.OnClickListener() {

                            @Override
                            public void onClick(CustomDialog dialog, int id, Object object) {
                                presenter.replacementOrder(myOrder.getOrderId());
                                dialog.dismiss();
                            }
                        });
                        break;
                }
                break;
        }
    }

    /**
     * 执行投诉
     */
    private void executeComplaint() {
        //弹框输入理由
        DialogStyle1 dialogStyle1 = new DialogStyle1(getContext());
        dialogStyle1.initDialogContent("投诉", "取消", "提交投诉");
        dialogStyle1.setDialogClickListener(new DialogStyle1.DialogClickListener() {
            @Override
            public void leftClick(Context context, DialogStyle1 dialog) {
                dialog.dismissDialog1();
            }

            @Override
            public void rightClick(Context context, DialogStyle1 dialog) {
                presenter.complaint(user.getUserId() + "", dialog.getEdtText());
                dialog.dismissDialog1();
            }
        });
        dialogStyle1.showDialog1();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2018 && resultCode == getActivity().RESULT_OK) {
            myOrder.setIsAppeal(1);
        }
    }

    /**
     * 执行评价
     */
    private void executeEvaluate() {
        //跳转评星界面
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", myOrder);
        IntentUtil.gotoActivity(getContext(), EvaluateActivity.class, bundle);
    }

    /**
     * 执行取消_ 按钮操作
     */
    private void executeCancel(final String msg) {
        //弹框输入理由
        DialogStyle1 dialogStyle1 = new DialogStyle1(getContext());
        dialogStyle1.initDialogContent(msg, null, null);
        dialogStyle1.setDialogClickListener(new DialogStyle1.DialogClickListener() {
            @Override
            public void leftClick(Context context, DialogStyle1 dialog) {
                dialog.dismissDialog1();
            }

            @Override
            public void rightClick(Context context, DialogStyle1 dialog) {
                //取消订单的理由
                String reason = dialog.getEdtText();
                if (reason == null || TextUtils.isEmpty(reason)) {
                    ToastUtil.showLongToast(getContext(), "请输入取消理由!");
                } else {
                    if (state == Order.STATUS_JIEDAN) {
                        presenter.cancelOrder(myOrder.getOrderId(), reason);
                    } else {
                        presenter.refuseOrder(myOrder.getOrderId(), reason);
                    }
                    dialog.dismissDialog1();
                }
            }
        });
        dialogStyle1.showDialog1();
    }

    @Override
    public BGARefreshLayout getBGARefreshLayout() {
        return mBGARefreshLayout;
    }

    @Override
    public void cutBottomButton(boolean isShowFinishLayout) {

        if (isShowFinishLayout) {
            lLayoutDoubleButton.setVisibility(View.VISIBLE);
            btn.setVisibility(View.GONE);
        } else {
            lLayoutDoubleButton.setVisibility(View.GONE);
            btn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setFinishLayoutBtnName(String leftName, String rightName) {
        btnRepulse.setText(leftName);
        btnAccept.setText(rightName);
    }

    private void showShareIsPrize(){
        HttpRequestUtils.httpRequest(getActivity(), "getShareData", RequestValue.GET_ADVERTISING+"?local=6&role=1",null, "GET", new HttpRequestUtils.ResponseListener() {
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
                            IntentUtil.gotoActivity(getActivity(), ShareActivity.class,bundle);
                            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                        break;
                    default:
                        ToastUtil.showToast(getActivity(),"获取分享信息失败");
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showToast(getActivity(),"获取分享信息失败"+msg);
            }
        });


    }


    @Override
    public void setBottomButtonColour(int colour) {
        if (colour == Color.parseColor("#9f9f9f")) {
            btn.setVisibility(View.GONE);
        }
    }

    /**
     * 用户拒绝订单
     *
     */
    public void pressOrder() {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", myOrder.getOrderId());
        HttpRequestUtils.httpRequest(getActivity(), "pressOrder", RequestValue.ORDER_PRESS, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        ToastUtil.showLongToast(getActivity(), "系统已通知工程师尽快处理该订单！");
                        break;
                    default:
                        ToastUtil.showLongToast(getActivity(), common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showLongToast(getActivity(), msg);
            }
        });
    }
}
