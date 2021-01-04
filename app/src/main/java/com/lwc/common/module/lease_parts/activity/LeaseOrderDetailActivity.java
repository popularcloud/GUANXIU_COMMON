package com.lwc.common.module.lease_parts.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.bean.WxBean;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.lease_parts.bean.OrderDetailBean;
import com.lwc.common.module.lease_parts.fragment.LeaseShoppingCartFragment;
import com.lwc.common.module.wallet.ui.PayPwdActivity;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.DisplayUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
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
import com.lwc.common.widget.CancelOrderDialog;
import com.lwc.common.widget.CustomDialog;
import com.lwc.common.widget.GetPhoneDialog;
import com.lwc.common.widget.PayTypeDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LeaseOrderDetailActivity extends BaseActivity {


    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    @BindView(R.id.tv_status_desc)
    TextView tv_status_desc;

    @BindView(R.id.tv_btn01)
    TextView tv_btn01;
    @BindView(R.id.tv_btn02)
    TextView tv_btn02;
    @BindView(R.id.tv_btn03)
    TextView tv_btn03;
    @BindView(R.id.tv_btn04)
    TextView tv_btn04;
    @BindView(R.id.tv_btn05)
    TextView tv_btn05;

    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtPhone)
    TextView txtPhone;
    @BindView(R.id.txtAddress)
    TextView txtAddress;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_spece)
    TextView tv_spece;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_sum)
    TextView tv_sum;
    @BindView(R.id.iv_header)
    ImageView iv_header;
    @BindView(R.id.imgRight)
    ImageView imgRight;

    @BindView(R.id.tv_total_money)
    TextView tv_total_money;
    @BindView(R.id.tv_transportation_money)
    TextView tv_transportation_money;
    @BindView(R.id.tv_discount)
    TextView tv_discount;
    @BindView(R.id.tv_real_pay)
    TextView tv_real_pay;
    @BindView(R.id.tv_pay_method)
    TextView tv_pay_method;

    @BindView(R.id.tv_remark)
    TextView tv_remark;
    @BindView(R.id.tv_order_num)
    TextView tv_order_num;
    @BindView(R.id.tv_create_time)
    TextView tv_create_time;
    @BindView(R.id.tv_pay_type)
    TextView tv_pay_type;
    @BindView(R.id.tv_pay_time)
    TextView tv_pay_time;
    @BindView(R.id.tv_send_type)
    TextView tv_send_type;
    @BindView(R.id.tv_send_time)
    TextView tv_send_time;
    @BindView(R.id.tv_send_num)
    TextView tv_send_num;
    @BindView(R.id.tv_receiving_time)
    TextView tv_receiving_time;

    @BindView(R.id.ll_pay_type)
    LinearLayout ll_pay_type;
    @BindView(R.id.ll_pay_time)
    LinearLayout ll_pay_time;
    @BindView(R.id.ll_send_type)
    LinearLayout ll_send_type;
    @BindView(R.id.ll_send_num)
    LinearLayout ll_send_num;
    @BindView(R.id.ll_send_time)
    LinearLayout ll_send_time;
    @BindView(R.id.ll_receiving_time)
    LinearLayout ll_receiving_time;
    @BindView(R.id.ll_remark)
    LinearLayout ll_remark;

    @BindView(R.id.ll_btn)
    LinearLayout ll_btn;
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;

    @BindView(R.id.tv_copy_order_num)
    TextView tv_copy_order_num;

    @BindView(R.id.fragment_container)
    FrameLayout fragment_container;
    private LeaseShoppingCartFragment leaseShoppingCartFragment;
    private FragmentManager fragmentManager;

    private List<String> reasons = new ArrayList<>();
    private OrderDetailBean orderDetailBean;

    private CancelOrderDialog cancelOrderDialog;
    private String orderId;

    private Handler mHandle;
    private boolean IsCancel = true;
    private long stamp01 = 0;
    private PayTypeDialog payTypeDialog;
    private int openType;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_lease_order_detail;
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
        MsgReadUtil.hasMessage(LeaseOrderDetailActivity.this,tv_msg);
    }

    @Override
    protected void init() {

        setTitle("订单详情");
        txtActionbarTitle.setTextColor(getResources().getColor(R.color.white));

        int height = DisplayUtil.getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rl_title.getLayoutParams();
        layoutParams.setMargins(0,height,0,0);
        rl_title.setLayoutParams(layoutParams);

        ImmersionBar.with(LeaseOrderDetailActivity.this)
                .transparentStatusBar()
                .statusBarDarkFont(false).init();

        reasons.add("我不想租了");
        reasons.add("信息填写错误，我不想拍了");
        reasons.add("平台缺货");
        reasons.add("其他原因");

        orderId = getIntent().getStringExtra("order_id");
        openType = getIntent().getIntExtra("openType",0);

        if(openType == 0){
            ll_btn.setVisibility(View.GONE);
        }

        getOrderDetail();

        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindowUtil.showHeaderPopupWindow(LeaseOrderDetailActivity.this,imgRight,leaseShoppingCartFragment,fragment_container,fragmentManager);
            }
        });

        leaseShoppingCartFragment = new LeaseShoppingCartFragment();
        fragmentManager = getSupportFragmentManager();
    }


    @Override
    protected void initGetData() {

    }

    @Override
    protected void widgetListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IsCancel = true;
    }

    @OnClick({R.id.tv_btn01,R.id.tv_btn02,R.id.tv_btn03,R.id.tv_btn04,R.id.tv_btn05,R.id.tv_copy_order_num,R.id.tv_copy_send_num})
    public void onBtnClick(View view){
        TextView myText = (TextView) view;
        switch (view.getId()){
            case R.id.tv_btn01:
            case R.id.tv_btn02:
            case R.id.tv_btn03:
            case R.id.tv_btn04:
            case R.id.tv_btn05:
                switch (myText.getText().toString()){
                    case "退租":
                        Bundle bundle01 = new Bundle();
                        bundle01.putSerializable("orderDetailBean", orderDetailBean);
                        bundle01.putInt("returnType", 3);
                        IntentUtil.gotoActivity(LeaseOrderDetailActivity.this, LeaseApplyForRefundActivity.class, bundle01);
                        break;
                    case "联系客服":
                        GetPhoneDialog picturePopupWindow = new GetPhoneDialog(LeaseOrderDetailActivity.this, new GetPhoneDialog.CallBack() {
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
                    case "取消订单":
                        cancelOrderDialog = new CancelOrderDialog(this, new CancelOrderDialog.CallBack() {
                            @Override
                            public void onSubmit(int position) {
                                cancelOrderDialog.dismiss();
                                cancelLeaseOrder(reasons.get(position),orderDetailBean.getOrderId());
                            }
                        },reasons,"取消订单","请选择取消订单原因",true);
                        cancelOrderDialog.show();
                        break;
                    case "退租订单":
                        Bundle bundle03 = new Bundle();
                        bundle03.putInt("pageType",7);
                        bundle03.putString("orderId",orderDetailBean.getOrderId());
                        IntentUtil.gotoActivity(LeaseOrderDetailActivity.this, MyLeaseOrderListActivity.class,bundle03);
                        break;
                    case "退款订单":
                        Bundle bundle04 = new Bundle();
                        bundle04.putInt("pageType",6);
                        bundle04.putString("orderId",orderDetailBean.getOrderId());
                        IntentUtil.gotoActivity(LeaseOrderDetailActivity.this, MyLeaseOrderListActivity.class,bundle04);
                        break;
                    case "退款":
                        if("2".equals(orderDetailBean.getStatusId())) { //待发货
                            Bundle bundle02 = new Bundle();
                            bundle02.putSerializable("orderDetailBean", orderDetailBean);
                            bundle02.putInt("returnType", 0);
                            IntentUtil.gotoActivity(this, LeaseApplyForRefundActivity.class, bundle02);
                        }else if("3".equals(orderDetailBean.getStatusId())){ //待收货
                            Bundle bundle02 = new Bundle();
                            bundle02.putSerializable("orderDetailBean",orderDetailBean);
                            IntentUtil.gotoActivity(this, LeaseSelectReturnTypeActivity.class,bundle02);
                        }
                        break;
                    case "确认收货":
                        inLease(orderDetailBean.getOrderId());
                        break;
                    case "撤销申请":
                        retrunApply(orderDetailBean.getOrderId());
                        break;
                    case "缴费":
                    case "续租":
                        Bundle bundle = new Bundle();
                        bundle.putString("order_id",orderDetailBean.getOrderId());
                        IntentUtil.gotoActivity(this, LeaseOrderNeedPayDetailActivity.class,bundle);
                        break;
                    case "付款":
                        showPayInfo(orderDetailBean.getOrderId(),Utils.chu(orderDetailBean.getPayPrice(),"100"));
                        break;
                    case "删除订单":
                        delOrder(orderDetailBean.getOrderId());
                        break;
                }
                break;
            case R.id.tv_copy_order_num:
                String orderNum = tv_order_num.getText().toString().trim();
                if(!TextUtils.isEmpty(orderNum)){
                    if(Utils.copy(orderNum,LeaseOrderDetailActivity.this)){
                        ToastUtil.showToast(LeaseOrderDetailActivity.this,"复制成功！");
                    }else{
                        ToastUtil.showToast(LeaseOrderDetailActivity.this,"复制失败！");
                    }
                }
                break;
             case R.id.tv_copy_send_num:
                String sendNum = tv_send_num.getText().toString().trim();
                if(!TextUtils.isEmpty(sendNum)){
                    if(Utils.copy(sendNum,LeaseOrderDetailActivity.this)){
                        ToastUtil.showToast(LeaseOrderDetailActivity.this,"复制成功！");
                    }else{
                        ToastUtil.showToast(LeaseOrderDetailActivity.this,"复制失败！");
                    }
                }
                break;
        }
    }

    /**
     * 获取订单详情
     */
    private void getOrderDetail(){
        HttpRequestUtils.httpRequest(this, "获取订单详情", RequestValue.LEASEMANAGE_GETLEASEORDER + "?order_id="+orderId, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    orderDetailBean = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response,"data"), OrderDetailBean.class);

                    tv_status.setText(orderDetailBean.getStatusName());
                    txtName.setText(orderDetailBean.getOrderContactName());
                    txtPhone.setText(orderDetailBean.getOrderContactPhone());
                    txtAddress.setText(orderDetailBean.getOrderProvinceName()+orderDetailBean.getOrderCityName()+orderDetailBean.getOrderTownName()+orderDetailBean.getOrderContactAddress());

                    ImageLoaderUtil.getInstance().displayFromNetDCircular(LeaseOrderDetailActivity.this,orderDetailBean.getGoodsImg(),iv_header,R.drawable.image_default_picture);

                    String goodsName = orderDetailBean.getGoodsName();
                    String goodsNameStr = "租赁  " + goodsName;
                    SpannableStringBuilder showGoodsName = Utils.getSpannableStringBuilder(0, 2, getResources().getColor(R.color.transparent), goodsNameStr, 10, true);
                    tv_title.setText(showGoodsName);

                    tv_spece.setText(orderDetailBean.getLeaseSpecs()+","+orderDetailBean.getLeaseMonTime()+"个月,"+("1".equals(orderDetailBean.getPayType())?"月付":"季付"));

                    if("1".equals(orderDetailBean.getPayType())){
                        tv_pay_method.setText("按月支付");
                    }else{
                        tv_pay_method.setText("按季支付");
                    }

                    String needShowMoney = "￥"+Utils.getMoney(Utils.chu(orderDetailBean.getGoodsPrice(),"100"));
                    SpannableStringBuilder showGoodsPrice = Utils.getSpannableStringBuilder(1, needShowMoney.length() - 2, getResources().getColor(R.color.all_black), needShowMoney, 15, true);
                    tv_price.setText(showGoodsPrice);
                    //tv_price.setText("￥"+Utils.getMoney(Utils.chu(orderDetailBean.getGoodsPrice(),"100")));

                    tv_sum.setText("x"+orderDetailBean.getGoodsNum());

                    String totalMoney = Utils.cheng(orderDetailBean.getGoodsPrice(),String.valueOf(orderDetailBean.getGoodsNum()));
                    if("2".equals(orderDetailBean.getPayType())){
                        totalMoney = Utils.cheng(totalMoney,"3");
                    }
                    tv_total_money.setText("￥"+Utils.getMoney(Utils.chu(totalMoney,"100")));

                    String realShowMoney = "￥"+Utils.getMoney(Utils.chu(orderDetailBean.getPayPrice(),"100"));
                    SpannableStringBuilder realwGoodsPrice = Utils.getSpannableStringBuilder(1, realShowMoney.length() - 2, getResources().getColor(R.color.red_money), realShowMoney, 15, true);
                    tv_real_pay.setText(realwGoodsPrice);

                    tv_discount.setText("-￥"+Utils.getMoney(Utils.chu(orderDetailBean.getDiscountAmount(),"100")));
                    tv_transportation_money.setText("+￥0.00");

                    if(TextUtils.isEmpty(orderDetailBean.getUserMessage())){
                        ll_remark.setVisibility(View.GONE);
                    }else{
                        ll_remark.setVisibility(View.VISIBLE);
                        tv_remark.setText(orderDetailBean.getUserMessage());
                    }

                    tv_order_num.setText(orderDetailBean.getOrderId());
                    tv_create_time.setText(orderDetailBean.getCreateTime());

                    String createTime = orderDetailBean.getCreateTime();

                    try{
                        stamp01 = dateToStamp(createTime);
                    }catch (Exception e){}

                    setItemData();

                    if(orderDetailBean.getTuikuanNum() > 0){
                        tv_btn02.setText("退款订单");
                        tv_btn02.setVisibility(View.VISIBLE);
                    }else{
                        tv_btn02.setVisibility(View.GONE);
                    }

                    if(orderDetailBean.getTuizuNum() > 0){
                        tv_btn03.setText("退租订单");
                        tv_btn03.setVisibility(View.VISIBLE);
                    }else{
                        tv_btn03.setVisibility(View.GONE);
                    }

                    switch (orderDetailBean.getStatusId()){
                        case "1": //待付款
                            tv_btn01.setVisibility(View.VISIBLE);
                            tv_btn01.setText("付款");
                            tv_btn04.setVisibility(View.VISIBLE);
                            tv_btn04.setText("取消订单");

                            //开启付款倒计时
                            startTimer();
                            break;
                        case "2": //代发货
                            if(orderDetailBean.getGoodsRealNum() > 0){
                                tv_btn01.setText("退款");
                                tv_btn01.setVisibility(View.VISIBLE);
                            }else{
                                tv_btn01.setVisibility(View.GONE);
                            }

                            tv_btn04.setVisibility(View.GONE);
                            break;
                        case "3": //已发货-待收货

                            tv_btn01.setVisibility(View.VISIBLE);
                            tv_btn01.setText("确认收货");

                            if(orderDetailBean.getGoodsRealNum() > 0){
                                tv_btn04.setText("退款");
                                tv_btn04.setVisibility(View.VISIBLE);
                            }else{
                                tv_btn04.setVisibility(View.GONE);
                            }
                            break;
                        case "4": //已收货，租用中
                            ll_receiving_time.setVisibility(View.VISIBLE);

                            tv_btn01.setVisibility(View.VISIBLE);
                            tv_btn01.setText("缴费");

                            if(orderDetailBean.getGoodsRealNum() > 0){
                                tv_btn04.setText("退租");
                                tv_btn04.setVisibility(View.VISIBLE);
                            }else{
                                tv_btn04.setVisibility(View.GONE);
                            }
                            break;
                        case "5":  //已逾期
                            tv_btn01.setVisibility(View.VISIBLE);
                            tv_btn01.setText("缴费");

                            if(orderDetailBean.getGoodsRealNum() > 0){
                                tv_btn04.setText("退租");
                                tv_btn04.setVisibility(View.VISIBLE);
                            }else{
                                tv_btn04.setVisibility(View.GONE);
                            }
                            break;
                        case "6": // 退款
                        case "7": // 退租
                            tv_btn05.setVisibility(View.VISIBLE);
                            tv_btn01.setVisibility(View.GONE);
                            tv_btn04.setVisibility(View.GONE);
                            break;
                        case "8": //取消
                            tv_btn01.setVisibility(View.VISIBLE);
                            tv_btn01.setText("删除订单");
                            tv_btn04.setVisibility(View.GONE);
                            tv_btn05.setVisibility(View.VISIBLE);
                            break;
                        case "9": //已到期
                            tv_btn01.setVisibility(View.VISIBLE);
                            tv_btn01.setText("续租");

                            if(orderDetailBean.getGoodsRealNum() > 0){
                                tv_btn04.setVisibility(View.VISIBLE);
                                tv_btn04.setText("退租");
                            }else{
                                tv_btn04.setVisibility(View.GONE);
                            }
                            break;
                    }
                }else{
                    ToastUtil.showToast(LeaseOrderDetailActivity.this,common.getInfo());
                    finish();
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }


    private void startTimer(){
        IsCancel = false;
        mHandle = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 999:
                        updatePayTime();
                        removeMessages(999);
                        if(!IsCancel){
                            sendEmptyMessageDelayed(999, 1000);
                        }
                        break;
                }
            }
        };

        mHandle.sendEmptyMessage(999);
    }

    private void updatePayTime(){

        long curStamp = System.currentTimeMillis();

        final long halfHour = 30 * 60 * 1000; //半个小时的时间戳
        final long stamp02 = curStamp - stamp01;
        if(stamp02 < halfHour){ //小于半个钟 说明还在支付范围
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String showTime = Utils.getTimeMill(halfHour - stamp02);
                    tv_status_desc.setText("订单超过30分钟将自动取消，请在"+showTime+"内完成支付");
                }
            });
        }else{
            IsCancel = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getOrderDetail();
                }
            });

        }

    }

    /*
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }

    private void setItemData(){

        if(TextUtils.isEmpty(orderDetailBean.getTransactionMeans())){
            ll_pay_type.setVisibility(View.GONE);
        }else{
            ll_pay_type.setVisibility(View.VISIBLE);
            //1钱包 2.支付宝 3.微信
            switch (orderDetailBean.getTransactionMeans()){
                case "1":
                    tv_pay_type.setText("账户余额");
                    break;
                case "2":
                    tv_pay_type.setText("支付宝支付");
                    break;
                case "3":
                    tv_pay_type.setText("微信支付");
                    break;
            }
        }

        if(TextUtils.isEmpty(orderDetailBean.getPayLeaseTime())){
            ll_pay_time.setVisibility(View.GONE);
        }else{
            ll_pay_time.setVisibility(View.VISIBLE);
            tv_pay_time.setText(orderDetailBean.getPayLeaseTime());
        }

        if(TextUtils.isEmpty(orderDetailBean.getCourierNumber())){
            ll_send_num.setVisibility(View.GONE);
        }else{
            ll_send_num.setVisibility(View.VISIBLE);
            tv_send_num.setText(orderDetailBean.getCourierNumber());
        }

        if(!TextUtils.isEmpty(orderDetailBean.getSendName())){
            ll_send_type.setVisibility(View.VISIBLE);
            tv_send_type.setText(orderDetailBean.getSendName());
        }else{
            ll_send_type.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(orderDetailBean.getToLeaseTime())){
            ll_send_time.setVisibility(View.GONE);
        }else{
            ll_send_time.setVisibility(View.VISIBLE);
            tv_send_time.setText(orderDetailBean.getToLeaseTime());
        }

        if(TextUtils.isEmpty(orderDetailBean.getLeaseInTime())){
            ll_receiving_time.setVisibility(View.GONE);
        }else{
            ll_receiving_time.setVisibility(View.VISIBLE);
            tv_receiving_time.setText(orderDetailBean.getLeaseInTime());
        }
    }


    /**
     * 取消订单
     */
    private void cancelLeaseOrder(String reason,String orderId){
        Map<String,String> params = new HashMap<>();
        params.put("order_id",orderId);
        params.put("user_reason",reason);
        HttpRequestUtils.httpRequest(this, "取消订单",RequestValue.LEASEMANAGE_CANCELLEASEORDER, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    ToastUtil.showToast(LeaseOrderDetailActivity.this,common.getInfo());
                    getOrderDetail();
                }else{
                    ToastUtil.showToast(LeaseOrderDetailActivity.this,common.getInfo());
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
        HttpRequestUtils.httpRequest(this, "确认收货",RequestValue.LEASEMANAGE_INLEASE, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    ToastUtil.showToast(LeaseOrderDetailActivity.this,"确认收货成功!");
                    getOrderDetail();
                }else{
                    ToastUtil.showToast(LeaseOrderDetailActivity.this,common.getInfo());
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
        HttpRequestUtils.httpRequest(this, "撤销申请",RequestValue.LEASEMANAGE_UODOLEASEBRANCHORDER, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    ToastUtil.showToast(LeaseOrderDetailActivity.this,common.getInfo());
                    getOrderDetail();
                }else{
                    ToastUtil.showToast(LeaseOrderDetailActivity.this,common.getInfo());
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

        User user = SharedPreferencesUtils.getInstance(LeaseOrderDetailActivity.this).loadObjectData(User.class);
        if (TextUtils.isEmpty(user.getPayPassword())) {
            DialogUtil.showMessageDg(LeaseOrderDetailActivity.this, "温馨提示", "您还未设置支付密码\n请先去设置支付密码！", new CustomDialog.OnClickListener() {

                @Override
                public void onClick(CustomDialog dialog, int id, Object object) {
                    dialog.dismiss();
                    IntentUtil.gotoActivity(LeaseOrderDetailActivity.this, PayPwdActivity.class);
                }
            });
            return;
        }

        payTypeDialog = new PayTypeDialog(LeaseOrderDetailActivity.this, payPrice,new PayTypeDialog.CallBack() {
            @Override
            public void onSubmit(final int payType, String passWord) {

                Map<String,String> params = new HashMap<>();
                params.put("transaction_means",String.valueOf(payType));
                params.put("orderId",orderId);
                if(passWord != null){
                    params.put("payPassword", Utils.md5Encode(passWord));
                }
                params.put("appType","user");

                HttpRequestUtils.httpRequest(LeaseOrderDetailActivity.this, "拉取支付信息", RequestValue.LEASEMANAGE_PAY_LEASEINFO, params, "POST", new HttpRequestUtils.ResponseListener() {
                    @Override
                    public void getResponseData(final String response) {
                        Common common = JsonUtil.parserGsonToObject(response, Common.class);
                        if("1".equals(common.getStatus())){
                            // ToastUtil.showToast(ConfirmLeaseOrderActivity.this,"拉取支付信息成功!");

                            switch (payType){//1钱包 2.支付宝 3.微信
                                case 1:
                                    payTypeDialog.hideError();
                                    payTypeDialog.dismiss();
                                    ToastUtil.showToast(LeaseOrderDetailActivity.this,"支付成功！");
                                    String data = JsonUtil.getGsonValueByKey(response,"data");
                                    String integral = JsonUtil.getGsonValueByKey(data,"integral");
                                    final Bundle bundle = new Bundle();
                                    bundle.putString("integral",integral);
                                    bundle.putString("price",Utils.cheng(payPrice,"100"));
                                    IntentUtil.gotoActivity(LeaseOrderDetailActivity.this, PaySuccessActivity.class,bundle);
                                    finish();
                                    break;
                                case 2:
                                    try {
                                        final JSONObject obj = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
                                        PayServant.getInstance().aliPay(obj,LeaseOrderDetailActivity.this, new AliPayCallBack() {
                                            @Override
                                            public void OnAliPayResult(boolean success, boolean isWaiting, String msg) {
                                                ToastUtil.showLongToast(LeaseOrderDetailActivity.this, msg);
                                                if (success) {
                                                    String data = JsonUtil.getGsonValueByKey(response,"data");
                                                    String integral = JsonUtil.getGsonValueByKey(data,"integral");
                                                    final Bundle bundle = new Bundle();
                                                    bundle.putString("integral",integral);
                                                    bundle.putString("price",payPrice);
                                                    IntentUtil.gotoActivity(LeaseOrderDetailActivity.this, PaySuccessActivity.class,bundle);
                                                    finish();
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
                                            LeaseOrderDetailActivity.this, wx.getAppid(),
                                            wx.getPartnerId(), wx.getPrepayId(), wx.getNonceStr(),
                                            wx.getTimeStamp(), wx.getPackageStr(), wx.getSign());
                                    break;
                            }


                        }else{
                            ToastUtil.showToast(LeaseOrderDetailActivity.this,common.getInfo());
                            payTypeDialog.showError(common.getInfo());
                        }
                    }

                    @Override
                    public void returnException(Exception e, String msg) {
                        LLog.eNetError(e.toString());
                    }
                });

               /* ToastUtil.showToast(ConfirmLeaseOrderActivity.this,"支付成功！");
                IntentUtil.gotoActivity(ConfirmLeaseOrderActivity.this,PaySuccessActivity.class);*/
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
        HttpRequestUtils.httpRequest(LeaseOrderDetailActivity.this, "删除订单",RequestValue.LEASEMANAGE_DELETELEASEORDER, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    ToastUtil.showToast(LeaseOrderDetailActivity.this,common.getInfo());
                    finish();
                }else{
                    ToastUtil.showToast(LeaseOrderDetailActivity.this,common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }
}
