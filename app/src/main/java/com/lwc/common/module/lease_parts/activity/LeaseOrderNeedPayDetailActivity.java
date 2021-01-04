package com.lwc.common.module.lease_parts.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.bean.WxBean;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.interf.OnItemCheckedCallBack;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.lease_parts.adapter.NeedPayAdapter;
import com.lwc.common.module.lease_parts.bean.NeedPayBean;
import com.lwc.common.module.lease_parts.bean.UserPayDetailBean;
import com.lwc.common.module.lease_parts.fragment.LeaseShoppingCartFragment;
import com.lwc.common.module.wallet.ui.PayPwdActivity;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.DisplayUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.MsgReadUtil;
import com.lwc.common.utils.PayServant;
import com.lwc.common.utils.PopupWindowUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.AliPayCallBack;
import com.lwc.common.widget.CustomDialog;
import com.lwc.common.widget.PayTypeDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LeaseOrderNeedPayDetailActivity extends BaseActivity {

    @BindView(R.id.rv_datas)
    RecyclerView rv_datas;

    @BindView(R.id.tv_price_month)
    TextView tv_price_month;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_submit)
    TextView tv_submit;
    @BindView(R.id.tv_SumMoney)
    TextView tv_SumMoney;
    @BindView(R.id.cb_all_select)
    CheckBox cb_all_select;
    @BindView(R.id.rl_goods_detail)
    RelativeLayout rl_goods_detail;
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;

    @BindView(R.id.tv_msg)
    TextView tv_msg;

    @BindView(R.id.fragment_container)
    FrameLayout fragment_container;
    private LeaseShoppingCartFragment leaseShoppingCartFragment;
    private FragmentManager fragmentManager;

    private List<NeedPayBean> needPayBeans = new ArrayList<>();
    private UserPayDetailBean orderDetailBean;
    private NeedPayAdapter adapter;
    private PayTypeDialog payTypeDialog;
    private String totalMoney;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_lease_order_need_pay_detail;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        tv_msg.setBackgroundResource(R.drawable.btn_fill_white);
        tv_msg.setTextColor(getResources().getColor(R.color.red_money));

        //获取未读租赁消息
        MsgReadUtil.hasMessage(LeaseOrderNeedPayDetailActivity.this,tv_msg);
    }

    @Override
    protected void init() {

        setTitle("缴费详情");
        txtActionbarTitle.setTextColor(getResources().getColor(R.color.white));

        int height = DisplayUtil.getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rl_title.getLayoutParams();
        layoutParams.setMargins(0,height,0,0);
        rl_title.setLayoutParams(layoutParams);

        ImmersionBar.with(LeaseOrderNeedPayDetailActivity.this)
                .statusBarColor(R.color.transparent)
                .statusBarDarkFont(false).init();

        setRight(R.drawable.ic_more_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindowUtil.showHeaderPopupWindow(LeaseOrderNeedPayDetailActivity.this,imgRight,leaseShoppingCartFragment,fragment_container,fragmentManager);
            }
        });
        initRecycleView();

        leaseShoppingCartFragment = new LeaseShoppingCartFragment();
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void initGetData() {

    }

    @Override
    protected void widgetListener() {
        cb_all_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for(NeedPayBean needPayBean : adapter.getData()){
                    if(!needPayBean.isPaidIn()){
                        needPayBean.setChecked(isChecked);
                    }
                }
                adapter.notifyDataSetChanged();
                calculationMoney();
            }

        });
    }


    @OnClick({R.id.tv_submit,R.id.rl_goods_detail})
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.tv_submit:

                if("0".equals(orderDetailBean.getOrder().getHavePay())){
                    ToastUtil.showToast(LeaseOrderNeedPayDetailActivity.this,"您有正在审核中的退租订单，不能进行缴费！");
                    return;
                }

                if(needPayBeans != null){
                    int renewalNum = 0;
                    for(int i = 0;i<needPayBeans.size();i++){
                        if(needPayBeans.get(i).isChecked()){
                            renewalNum = renewalNum +1;
                        }
                    }
                    if(renewalNum == 0){
                        ToastUtil.showToast(LeaseOrderNeedPayDetailActivity.this,"请选择要缴费的月份");
                        return;
                    }
                    showPayInfo(orderDetailBean.getOrder().getOrderId(),renewalNum);
                }
                break;
            case R.id.rl_goods_detail:
                Bundle bundle = new Bundle();
                bundle.putString("order_id",orderDetailBean.getOrder().getOrderId());
                IntentUtil.gotoActivity(this, LeaseOrderDetailActivity.class,bundle);
                break;
        }
    }

    private void initRecycleView() {

        rv_datas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NeedPayAdapter(this, needPayBeans, R.layout.item_need_pay, new OnItemCheckedCallBack() {
            @Override
            public void onItemChecked(int position, boolean isChecked) {
                    adapter.getData().get(position).setChecked(isChecked);
                    calculationMoney();
            }
        });
        rv_datas.setAdapter(adapter);

        getOrderData();
    }

    private void getOrderData() {

        tv_SumMoney.setText("￥ 0.00");
        cb_all_select.setChecked(false);

        final String order_id = getIntent().getStringExtra("order_id");

        Map<String,String> param = new HashMap<>();
        param.put("order_id",order_id);
        HttpRequestUtils.httpRequest(this, "获取缴费订单详情", RequestValue.LEASEMANAGE_USERPAYDETAIL, param, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    orderDetailBean = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response,"data"), UserPayDetailBean.class);
                    String payPrice = Utils.getMoney(Utils.chu(orderDetailBean.getPayPrice(),"100"));
                    tv_price_month.setText(payPrice);
                    tv_name.setText(orderDetailBean.getOrder().getGoodsName());
                    tv_time.setText(orderDetailBean.getOrder().getLeaseInTime());

                    String lastPayTime = orderDetailBean.getOrder().getRenewalTime();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        Date date =  simpleDateFormat.parse(lastPayTime);
                        Calendar rightNow = Calendar.getInstance();
                        rightNow.setTime(date);
                        needPayBeans.clear();

                       for(int i = 0;i < orderDetailBean.getPay().size(); i++){
                           UserPayDetailBean.PayBean payBean = orderDetailBean.getPay().get(i);
                           String price = Utils.getMoney(Utils.chu(payBean.getPayPrice(),"100"));
                           String time = payBean.getPayTime();
                           needPayBeans.add(new NeedPayBean(price,simpleDateFormat.parse(time),true));
                       }

                       int needPayMonth = 0;
                        if("9".equals(orderDetailBean.getOrder().getStatusId())){
                            needPayMonth = 12;
                        }else{
                            if("0".equals(orderDetailBean.getOrder().getRenewalType())){//是否到期后续费（0否 1是）
                                needPayMonth = orderDetailBean.getOrder().getLeaseMonTime() - orderDetailBean.getPay().size();
                            }
                        }

                      for(int i = 0;i < needPayMonth; i++){
                          rightNow.add(Calendar.MONTH,1);
                          needPayBeans.add(new NeedPayBean(payPrice,rightNow.getTime(),false));
                      }
                        adapter.replaceAll(needPayBeans);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }else{
                    ToastUtil.showToast(LeaseOrderNeedPayDetailActivity.this,common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }

    private void calculationMoney(){
        totalMoney = "0";
        for(NeedPayBean needPayBean :  adapter.getData()){
            if(needPayBean.isChecked()){
                totalMoney = Utils.jia(totalMoney,needPayBean.getMoney());
            }
        }
        tv_SumMoney.setText("￥"+Utils.getMoney(totalMoney));

    }

    private void showPayInfo(final String orderId, final int renewalNum){

        User user = SharedPreferencesUtils.getInstance(LeaseOrderNeedPayDetailActivity.this).loadObjectData(User.class);
        if (TextUtils.isEmpty(user.getPayPassword())) {
            DialogUtil.showMessageDg(LeaseOrderNeedPayDetailActivity.this, "温馨提示", "您还未设置支付密码\n请先去设置支付密码！", new CustomDialog.OnClickListener() {

                @Override
                public void onClick(CustomDialog dialog, int id, Object object) {
                    dialog.dismiss();
                    IntentUtil.gotoActivity(LeaseOrderNeedPayDetailActivity.this, PayPwdActivity.class);
                }
            });
            return;
        }

        payTypeDialog = new PayTypeDialog(this,totalMoney, new PayTypeDialog.CallBack() {
            @Override
            public void onSubmit(final int payType, String passWord) {
                Map<String,String> params = new HashMap<>();
                params.put("transaction_means",String.valueOf(payType));
                params.put("orderId",orderId);
                if(passWord != null){
                    params.put("payPassword", Utils.md5Encode(passWord));
                }
                params.put("appType","user");
                params.put("renewalNum",String.valueOf(renewalNum));

                HttpRequestUtils.httpRequest(LeaseOrderNeedPayDetailActivity.this, "拉取支付信息", RequestValue.LEASEMANAGE_PAY_LEASERENEWALINFO, params, "POST", new HttpRequestUtils.ResponseListener() {
                    @Override
                    public void getResponseData(String response) {
                        Common common = JsonUtil.parserGsonToObject(response, Common.class);
                        if("1".equals(common.getStatus())){
                            // ToastUtil.showToast(ConfirmLeaseOrderActivity.this,"拉取支付信息成功!");
                            String data = JsonUtil.getGsonValueByKey(response,"data");
                            String integral = JsonUtil.getGsonValueByKey(data,"integral");
                            final Bundle bundle = new Bundle();
                            bundle.putString("integral",integral);
                            bundle.putString("price",Utils.cheng(totalMoney,"100"));
                            switch (payType){//1钱包 2.支付宝 3.微信
                                case 1:
                                    payTypeDialog.hideError();
                                    payTypeDialog.dismiss();
                                    ToastUtil.showToast(LeaseOrderNeedPayDetailActivity.this,"支付成功！");
                                   // IntentUtil.gotoActivity(LeaseOrderNeedPayDetailActivity.this, PaySuccessActivity.class,bundle);

                                    getOrderData();
                                    break;
                                case 2:
                                    try {
                                        final JSONObject obj = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
                                        PayServant.getInstance().aliPay(obj, LeaseOrderNeedPayDetailActivity.this, new AliPayCallBack() {
                                            @Override
                                            public void OnAliPayResult(boolean success, boolean isWaiting, String msg) {
                                                ToastUtil.showLongToast(LeaseOrderNeedPayDetailActivity.this, msg);
                                                if (success) {
                                                    ToastUtil.showToast(LeaseOrderNeedPayDetailActivity.this,"支付成功！");
                                                  /*  IntentUtil.gotoActivity(LeaseOrderNeedPayDetailActivity.this, PaySuccessActivity.class,bundle);*/
                                                    getOrderData();
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
                                            LeaseOrderNeedPayDetailActivity.this, wx.getAppid(),
                                            wx.getPartnerId(), wx.getPrepayId(), wx.getNonceStr(),
                                            wx.getTimeStamp(), wx.getPackageStr(), wx.getSign());

                                    getOrderData();
                                    break;
                            }


                        }else{
                            ToastUtil.showToast(LeaseOrderNeedPayDetailActivity.this,common.getInfo());
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
}
