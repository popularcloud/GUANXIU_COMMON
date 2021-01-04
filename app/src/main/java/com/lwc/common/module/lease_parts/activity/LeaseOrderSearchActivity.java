package com.lwc.common.module.lease_parts.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.bean.WxBean;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.MyMsg;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.lease_parts.adapter.LeaseOrderListAdapter;
import com.lwc.common.module.lease_parts.bean.MyOrderBean;
import com.lwc.common.module.lease_parts.bean.OrderDetailBean;
import com.lwc.common.module.lease_parts.inteface_callback.OnOrderBtnClick;
import com.lwc.common.module.message.ui.MsgListActivity;
import com.lwc.common.module.wallet.ui.PayPwdActivity;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.MsgReadUtil;
import com.lwc.common.utils.PayServant;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.AliPayCallBack;
import com.lwc.common.widget.CancelOrderDialog;
import com.lwc.common.widget.CustomDialog;
import com.lwc.common.widget.GetPhoneDialog;
import com.lwc.common.widget.PayTypeDialog;

import org.byteam.superadapter.OnItemClickListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author younge
 * @date 2018/12/19 0019
 * @email 2276559259@qq.com
 */
public class LeaseOrderSearchActivity extends BaseActivity{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    @BindView(R.id.et_search)
    TextView et_search;
    @BindView(R.id.mBGARefreshLayout)
    BGARefreshLayout mBGARefreshLayout;
    private LeaseOrderListAdapter adapter;
    private List<MyOrderBean> myOrders = new ArrayList<>();

    private int page = 1;

    private String wd;

    private List<String> reasons = new ArrayList<>();
    private CancelOrderDialog cancelOrderDialog;

    private PayTypeDialog payTypeDialog;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_lease_order_search;
    }

    @Override
    protected void findViews() {
        bindRecycleView();
    }

    @Override
    protected void init() {
        setTitle("订单缴费");
        setRight(R.drawable.ic_message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMsg msg = new MyMsg();
                msg.setMessageType("5");
                msg.setMessageTitle("租赁消息");
                Bundle bundle = new Bundle();
                bundle.putSerializable("myMsg", msg);
                IntentUtil.gotoActivity(LeaseOrderSearchActivity.this, MsgListActivity.class,bundle);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        //获取未读租赁消息
        MsgReadUtil.hasMessage(LeaseOrderSearchActivity.this,tv_msg);
    }

    @Override
    protected void initGetData() {
    }


    @Override
    protected void widgetListener() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                wd = s.toString();
                mBGARefreshLayout.beginRefreshing();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void bindRecycleView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(LeaseOrderSearchActivity.this));
        adapter = new LeaseOrderListAdapter(LeaseOrderSearchActivity.this, myOrders, R.layout.item_lease_order, 5, new OnOrderBtnClick() {
            @Override
            public void onItemClick(String btnText, int position,Object o) {
                final MyOrderBean myOrder = (MyOrderBean) o;
                switch (btnText){
                    case "取消订单":
                        reasons.clear();
                        reasons.add("我不想租了");
                        reasons.add("信息填写错误，我不想拍了");
                        reasons.add("平台缺货");
                        reasons.add("其他原因");
                        cancelOrderDialog = new CancelOrderDialog(LeaseOrderSearchActivity.this, new CancelOrderDialog.CallBack() {
                            @Override
                            public void onSubmit(int position) {
                                cancelOrderDialog.dismiss();
                                cancelLeaseOrder(reasons.get(position),myOrder.getOrderId());
                            }
                        },reasons,"取消订单","请选择取消订单原因",true);

                        cancelOrderDialog.show();
                        break;
                    case "确认收货":
                        inLease(myOrder.getOrderId());
                        break;
                    case "续租":
                        Bundle bundle = new Bundle();
                        bundle.putString("order_id",myOrder.getOrderId());
                        IntentUtil.gotoActivity(LeaseOrderSearchActivity.this, LeaseOrderNeedPayDetailActivity.class,bundle);
                        break;
                    case "缴费":
                        Bundle bundle_pay = new Bundle();
                        bundle_pay.putString("order_id",myOrder.getOrderId());
                        IntentUtil.gotoActivity(LeaseOrderSearchActivity.this, LeaseOrderNeedPayDetailActivity.class,bundle_pay);
                        break;
                    case "退租":
                        OrderDetailBean orderDetailBean = new OrderDetailBean();
                        orderDetailBean.setGoodsImg(myOrder.getGoodsImg());
                        orderDetailBean.setGoodsName(myOrder.getGoodsName());
                        orderDetailBean.setGoodsRealNum(myOrder.getGoodsRealNum());
                        orderDetailBean.setLeaseMonTime(myOrder.getLeaseMonTime());
                        orderDetailBean.setPayType(myOrder.getPayType());
                        orderDetailBean.setGoodsNum(myOrder.getGoodsNum());
                        orderDetailBean.setGoodsPrice(myOrder.getGoodsPrice());
                        orderDetailBean.setPayPrice(myOrder.getPayPrice());
                        orderDetailBean.setGoodsId(myOrder.getGoodsId());
                        orderDetailBean.setOrderId(myOrder.getOrderId());
                        orderDetailBean.setLeaseSpecs(myOrder.getLeaseSpecs());

                        Bundle bundle01 = new Bundle();
                        bundle01.putSerializable("orderDetailBean", orderDetailBean);
                        bundle01.putInt("returnType", 3);
                        IntentUtil.gotoActivity(LeaseOrderSearchActivity.this, LeaseApplyForRefundActivity.class, bundle01);
                        break;
                    case "联系客服":
                        GetPhoneDialog picturePopupWindow = new GetPhoneDialog(LeaseOrderSearchActivity.this, new GetPhoneDialog.CallBack() {
                            @Override
                            public void twoClick() {
                                Utils.lxkf(MainActivity.activity, "400-881-0769");
                            }
                            @Override
                            public void cancelCallback() {
                            }
                        }, "", "呼叫 400-881-0769");
                        picturePopupWindow.show();
                        break;
                    case "付款":
                        showPayInfo(myOrder.getOrderId(),Utils.chu(myOrder.getPayPrice(),"100"));
                        break;
                    case "撤销申请":
                        retrunApply(myOrder.getOrderId());
                        break;
                    case "退租订单":
                       // MyLeaseOrderListActivity.showReturnOrder(7,myOrder.getOrderId());
                        Bundle bundle03 = new Bundle();
                        bundle03.putInt("pageType",7);
                        bundle03.putString("orderId",myOrder.getOrderId());
                        IntentUtil.gotoActivity(LeaseOrderSearchActivity.this, MyLeaseOrderListActivity.class,bundle03);
                        break;
                    case "退款订单":
                       // ((MyLeaseOrderListActivity)getActivity()).showReturnOrder(6,myOrder.getOrderId());
                        Bundle bundle04 = new Bundle();
                        bundle04.putInt("pageType",6);
                        bundle04.putString("orderId",myOrder.getOrderId());
                        IntentUtil.gotoActivity(LeaseOrderSearchActivity.this, MyLeaseOrderListActivity.class,bundle04);
                        break;
                    case "删除订单":
                        delOrder(myOrder.getOrderId());
                        break;
                }
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("order_id",adapter.getItem(position).getOrderId());
                bundle.putInt("openType",1);
                IntentUtil.gotoActivity(LeaseOrderSearchActivity.this, LeaseOrderDetailActivity.class,bundle);
            }
        });
        recyclerView.setAdapter(adapter);
        BGARefreshLayoutUtils.initRefreshLayout(this, mBGARefreshLayout);
        //刷新控件监听器
        mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                page = 1;
                getDateList();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                page ++;
                getDateList();
                return true;
            }
        });

        mBGARefreshLayout.beginRefreshing();
    }

    /**
     * 获取我的订单
     */
    private void getDateList(){
        String requestUrl = RequestValue.LEASEMANAGE_QUERYLEASEORDERS+"?&curPage="+page;
        if(!TextUtils.isEmpty(wd)){
            requestUrl = requestUrl + "&wd="+wd;
        }
        HttpRequestUtils.httpRequest(LeaseOrderSearchActivity.this, "搜索订单",requestUrl, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    // ToastUtil.showToast(getActivity(),"获取我的订单成功!");
                    myOrders = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response,"data"),new TypeToken<ArrayList<MyOrderBean>>(){});
                    if(page == 1){
                        adapter.replaceAll(myOrders);
                    }else{
                        adapter.addAll(myOrders);
                    }
                }else{
                    ToastUtil.showToast(LeaseOrderSearchActivity.this,common.getInfo());
                }

                if(page == 1){
                    mBGARefreshLayout.endRefreshing();
                }else{
                    mBGARefreshLayout.endLoadingMore();
                }

            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                if(page == 1){
                    mBGARefreshLayout.endRefreshing();
                }else{
                    mBGARefreshLayout.endLoadingMore();
                }
            }
        });
    }

    /**
     * 取消订单
     */
    private void cancelLeaseOrder(String reason,String orderId){
        Map<String,String> params = new HashMap<>();
        params.put("order_id",orderId);
        params.put("user_reason",reason);

        HttpRequestUtils.httpRequest(LeaseOrderSearchActivity.this, "取消订单",RequestValue.LEASEMANAGE_CANCELLEASEORDER, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    ToastUtil.showToast(LeaseOrderSearchActivity.this,common.getInfo());
                    mBGARefreshLayout.beginRefreshing();
                }else{
                    ToastUtil.showToast(LeaseOrderSearchActivity.this,common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }

    /**
     * 确认收货
     * @param orderId
     */
    private void inLease(String orderId){
        Map<String,String> params = new HashMap<>();
        params.put("order_id",orderId);
        HttpRequestUtils.httpRequest(LeaseOrderSearchActivity.this, "确认收货",RequestValue.LEASEMANAGE_INLEASE, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    ToastUtil.showToast(LeaseOrderSearchActivity.this,common.getInfo());
                    mBGARefreshLayout.beginRefreshing();
                }else{
                    ToastUtil.showToast(LeaseOrderSearchActivity.this,common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }

    /**
     * 撤销申请
     * @param orderId
     */
    private void retrunApply(String orderId){
        Map<String,String> params = new HashMap<>();
        params.put("branch_id",orderId);
        HttpRequestUtils.httpRequest(LeaseOrderSearchActivity.this, "撤销申请",RequestValue.LEASEMANAGE_UODOLEASEBRANCHORDER, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    ToastUtil.showToast(LeaseOrderSearchActivity.this,common.getInfo());
                    mBGARefreshLayout.beginRefreshing();
                }else{
                    ToastUtil.showToast(LeaseOrderSearchActivity.this,common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }

    /**
     * 付款
     * @param orderId
     */
    private void showPayInfo(final String orderId,final String payPrice){

        User user = SharedPreferencesUtils.getInstance(LeaseOrderSearchActivity.this).loadObjectData(User.class);
        if (TextUtils.isEmpty(user.getPayPassword())) {
            DialogUtil.showMessageDg(LeaseOrderSearchActivity.this, "温馨提示", "您还未设置支付密码\n请先去设置支付密码！", new CustomDialog.OnClickListener() {

                @Override
                public void onClick(CustomDialog dialog, int id, Object object) {
                    dialog.dismiss();
                    IntentUtil.gotoActivity(LeaseOrderSearchActivity.this, PayPwdActivity.class);
                }
            });
            return;
        }

        payTypeDialog = new PayTypeDialog(LeaseOrderSearchActivity.this, payPrice,new PayTypeDialog.CallBack() {
            @Override
            public void onSubmit(final int payType, String passWord) {

                Map<String,String> params = new HashMap<>();
                params.put("transaction_means",String.valueOf(payType));
                params.put("orderId",orderId);
                if(passWord != null){
                    params.put("payPassword", Utils.md5Encode(passWord));
                }
                params.put("appType","user");

                HttpRequestUtils.httpRequest(LeaseOrderSearchActivity.this, "拉取支付信息", RequestValue.LEASEMANAGE_PAY_LEASEINFO, params, "POST", new HttpRequestUtils.ResponseListener() {
                    @Override
                    public void getResponseData(String response) {
                        Common common = JsonUtil.parserGsonToObject(response, Common.class);
                        if("1".equals(common.getStatus())){
                            // ToastUtil.showToast(ConfirmLeaseOrderActivity.this,"拉取支付信息成功!");

                            switch (payType){//1钱包 2.支付宝 3.微信
                                case 1:
                                    payTypeDialog.hideError();
                                    payTypeDialog.dismiss();
                                    ToastUtil.showToast(LeaseOrderSearchActivity.this,"支付成功！");
                                    String data = JsonUtil.getGsonValueByKey(response,"data");
                                    String integral = JsonUtil.getGsonValueByKey(data,"integral");
                                    final Bundle bundle = new Bundle();
                                    bundle.putString("integral",integral);
                                    bundle.putString("price",Utils.cheng(payPrice,"100"));
                                    IntentUtil.gotoActivity(LeaseOrderSearchActivity.this, PaySuccessActivity.class,bundle);
                                    break;
                                case 2:
                                    try {
                                        final JSONObject obj = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
                                        PayServant.getInstance().aliPay(obj, LeaseOrderSearchActivity.this, new AliPayCallBack() {
                                            @Override
                                            public void OnAliPayResult(boolean success, boolean isWaiting, String msg) {
                                                ToastUtil.showLongToast(LeaseOrderSearchActivity.this, msg);
                                                if (success) {
                                                    mBGARefreshLayout.beginRefreshing();
                                                }
                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 3:
                                    WxBean wx = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), WxBean.class);
                                    PayServant.getInstance().weChatPay2(
                                            LeaseOrderSearchActivity.this, wx.getAppid(),
                                            wx.getPartnerId(), wx.getPrepayId(), wx.getNonceStr(),
                                            wx.getTimeStamp(), wx.getPackageStr(), wx.getSign());
                                    break;
                            }


                        }else{
                            ToastUtil.showToast(LeaseOrderSearchActivity.this,common.getInfo());
                            payTypeDialog.showError(common.getInfo());
                        }
                    }

                    @Override
                    public void returnException(Exception e, String msg) {
                        LLog.eNetError(e.toString());
                    }
                });
            }

            @Override
            public void onColse() {

            }
        });
        payTypeDialog.show();
    }

    private void delOrder(String orderId){
        Map<String,String> params = new HashMap<>();
        params.put("order_id",orderId);
        HttpRequestUtils.httpRequest(LeaseOrderSearchActivity.this, "删除订单",RequestValue.LEASEMANAGE_DELETELEASEORDER, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    ToastUtil.showToast(LeaseOrderSearchActivity.this,common.getInfo());
                    mBGARefreshLayout.beginRefreshing();
                }else{
                    ToastUtil.showToast(LeaseOrderSearchActivity.this,common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }
}
