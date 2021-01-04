package com.lwc.common.module.lease_parts.activity;

import android.os.Bundle;
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
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.lease_parts.bean.LeaseReturnDetailBean;
import com.lwc.common.module.lease_parts.fragment.LeaseShoppingCartFragment;
import com.lwc.common.module.wallet.ui.WalletActivity;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.PopupWindowUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.widget.GetPhoneDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 退租退款详情
 */
public class LeaseOrderRefundDetailActivity extends BaseActivity {

    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.tv_status_desc)
    TextView tv_status_desc;
    @BindView(R.id.imgRight)
    ImageView imgRight;

    @BindView(R.id.tv_btn01)
    TextView tv_btn01;
    @BindView(R.id.tv_btn02)
    TextView tv_btn02;

    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.rl_status_01)
    RelativeLayout rl_status_01;
    @BindView(R.id.rl_status_02)
    RelativeLayout rl_status_02;
    @BindView(R.id.tv_return_all_money)
    TextView tv_return_all_money;

    @BindView(R.id.ll_return_reason_desc)
    LinearLayout ll_return_reason_desc;
    @BindView(R.id.tv_return_reason_desc)
    TextView tv_return_reason_desc;

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


    @BindView(R.id.tv_return_reason)
    TextView tv_return_reason;
    @BindView(R.id.tv_return_money)
    TextView tv_return_money;
    @BindView(R.id.tv_create_time)

    TextView tv_create_time;
    @BindView(R.id.tv_detail_num)
    TextView tv_detail_num;
    @BindView(R.id.tv_return_order)
    TextView tv_return_order;

    @BindView(R.id.tv_detail_title)
    TextView tv_detail_title;
    @BindView(R.id.tv_detail_reason)
    TextView tv_detail_reason;
    @BindView(R.id.tv_detail_explain)
    TextView tv_detail_explain;
    @BindView(R.id.tv_detail_money)
    TextView tv_detail_money;
    @BindView(R.id.ll_return_num)
    LinearLayout ll_return_num;

    @BindView(R.id.fragment_container)
    FrameLayout fragment_container;
    private LeaseShoppingCartFragment leaseShoppingCartFragment;
    private FragmentManager fragmentManager;


    private List<String> reasons = new ArrayList<>();
    private String branchId;
    private LeaseReturnDetailBean orderDetailBean;
    private String pageType;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_lease_order_return_detail;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void init() {


        txtActionbarTitle.setTextColor(getResources().getColor(R.color.white));

        ImmersionBar.with(LeaseOrderRefundDetailActivity.this)
                .statusBarColor(R.color.red_money)
                .statusBarDarkFont(false).init();

        reasons.add("我不想租了");
        reasons.add("信息填写错误，我不想拍了");
        reasons.add("平台缺货");
        reasons.add("其他原因");

        setRight(R.drawable.photo_img_priview_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               PopupWindowUtil.showHeaderPopupWindow(LeaseOrderRefundDetailActivity.this,imgRight,leaseShoppingCartFragment,fragment_container,fragmentManager);
            }
        });


        leaseShoppingCartFragment = new LeaseShoppingCartFragment();
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void initGetData() {
        branchId = getIntent().getStringExtra("branch_id");
        pageType = getIntent().getStringExtra("pageType");
        if("6".equals(pageType)){
            setTitle("退款详情");
            tv_detail_title.setText("退款信息");
            tv_detail_reason.setText("退款原因：");
            tv_detail_explain.setText("退款说明：");
            tv_detail_money.setText("退款金额：");
            tv_detail_num.setText("退款单号：");
        }else if("7".equals(pageType)){
            setTitle("退租详情");
            tv_detail_title.setText("退租信息");
            tv_detail_reason.setText("退租原因：");
            tv_detail_explain.setText("退租说明：");
            tv_detail_money.setText("退租金额：");
            tv_detail_num.setText("退租单号：");
        }
        getOrderReturnDetail();
    }

    @Override
    protected void widgetListener() {

    }


    @OnClick({R.id.tv_btn01,R.id.tv_btn02})
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.tv_btn01:
                GetPhoneDialog picturePopupWindow = new GetPhoneDialog(this, new GetPhoneDialog.CallBack() {
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
            case R.id.tv_btn02:
                TextView myText = (TextView) view;
                switch (myText.getText().toString()){
                    case "撤销申请":
                        retrunApply(branchId);
                        break;
                    case "删除订单":
                        delOrder(branchId);
                        break;
                }
                break;
        }
    }

    private void delOrder(String orderId){
        Map<String,String> params = new HashMap<>();
        params.put("branch_id",orderId);
        HttpRequestUtils.httpRequest(this, "删除订单",RequestValue.LEASEMANAGE_DELETELEASEBRANCHORDER, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    ToastUtil.showToast(LeaseOrderRefundDetailActivity.this,common.getInfo());
                    finish();
                }else{
                    ToastUtil.showToast(LeaseOrderRefundDetailActivity.this,common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }

    private void getOrderReturnDetail(){
        Map<String,String> params = new HashMap<>();
        params.put("branch_id",branchId);
        HttpRequestUtils.httpRequest(this, "获取订单详情", RequestValue.LEASEMANAGE_GETLEASEBRANCHORDER, params, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    orderDetailBean = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response,"data"), LeaseReturnDetailBean.class);

                    tv_status.setText(orderDetailBean.getStatusDeatilName());
      /*              txtName.setText(orderDetailBean.getGoodsName());
                    txtPhone.setText(orderDetailBean.getOrderContactPhone());
                    txtAddress.setText(orderDetailBean.getOrderContactAddress());*/

                    ImageLoaderUtil.getInstance().displayFromNetDCircular(LeaseOrderRefundDetailActivity.this,orderDetailBean.getGoodsImg(),iv_header,R.drawable.image_default_picture);

                    String goodsName = orderDetailBean.getGoodsName();
                    String goodsNameStr = "租赁  " + goodsName;
                    SpannableStringBuilder showGoodsName = Utils.getSpannableStringBuilder(0, 2, getResources().getColor(R.color.transparent), goodsNameStr, 10, true);
                    tv_title.setText(showGoodsName);

                    tv_spece.setText(orderDetailBean.getLeaseSpecs()+","+orderDetailBean.getLeaseMonTime()+"个月,"+("1".equals(orderDetailBean.getPayType())?"月付":"季付"));

                    String needShowMoney = "￥"+Utils.getMoney(Utils.chu(orderDetailBean.getGoodsPrice(),"100"));
                    SpannableStringBuilder showGoodsPrice = Utils.getSpannableStringBuilder(1, needShowMoney.length() - 2, getResources().getColor(R.color.all_black), needShowMoney, 15, true);
                    tv_price.setText(showGoodsPrice);
                    tv_sum.setText("x"+orderDetailBean.getGoodsNum());


                    //tv_remark.setText(orderDetailBean.);
                    tv_return_reason.setText(orderDetailBean.getApplyReason());
                    if(!TextUtils.isEmpty(orderDetailBean.getMoney())){
                        tv_return_money.setText("￥"+Utils.getMoney(Utils.chu(orderDetailBean.getMoney(),"100")));
                    }else{
                        tv_return_money.setText("￥"+Utils.getMoney("0"));
                    }
                    tv_create_time.setText(orderDetailBean.getCreateTime());
                    tv_return_order.setText(orderDetailBean.getBranchId());

                    if(!TextUtils.isEmpty(orderDetailBean.getApplyTime())){
                        tv_status_desc.setText(orderDetailBean.getApplyTime());
                    }

                    if(!TextUtils.isEmpty(orderDetailBean.getInstructions())){
                        ll_return_reason_desc.setVisibility(View.VISIBLE);
                        tv_return_reason_desc.setText(orderDetailBean.getInstructions());
                    }else{
                        ll_return_reason_desc.setVisibility(View.GONE);
                    }

                    switch (orderDetailBean.getStatusDeatilId()){
                        case "101": //退款中
                            tv_btn01.setVisibility(View.VISIBLE);
                            tv_btn02.setVisibility(View.VISIBLE);
                            tv_btn02.setText("撤销申请");
                            rl_status_01.setVisibility(View.VISIBLE);
                            rl_status_02.setVisibility(View.GONE);
                            txtName.setText("您已成功发起退款申请，请耐心等待平台处理");
                            txtAddress.setText("如果平台拒绝，您可以联系客服咨询原因");
                            break;
                        case "102": //退款成功
                            tv_btn01.setVisibility(View.VISIBLE);
                            tv_btn02.setVisibility(View.VISIBLE);
                            tv_btn02.setText("删除订单");
                            rl_status_02.setVisibility(View.VISIBLE);
                            rl_status_01.setVisibility(View.GONE);
                            if(!TextUtils.isEmpty(orderDetailBean.getMoney())){
                                tv_return_all_money.setText("￥"+Utils.getMoney(Utils.chu(orderDetailBean.getMoney(),"100")));
                            }else{
                                tv_return_all_money.setText("￥"+Utils.getMoney("0"));
                            }
                            rl_status_02.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    IntentUtil.gotoActivity(LeaseOrderRefundDetailActivity.this, WalletActivity.class);
                                }
                            });
                            break;
                        case "103": //拒绝退款
                            tv_btn01.setVisibility(View.VISIBLE);
                            tv_btn02.setVisibility(View.VISIBLE);
                            tv_btn02.setText("删除订单");
                            rl_status_01.setVisibility(View.VISIBLE);
                            rl_status_02.setVisibility(View.GONE);
                            txtName.setText("拒绝原因:");
                            txtAddress.setText(orderDetailBean.getRefusedReason());
                            break;
                        case "104": //退租中
                            tv_btn01.setVisibility(View.VISIBLE);
                            tv_btn02.setVisibility(View.VISIBLE);
                            tv_btn02.setText("撤销申请");
                            rl_status_01.setVisibility(View.VISIBLE);
                            rl_status_02.setVisibility(View.GONE);
                            txtName.setText("您已成功发起退租申请，请耐心等待平台处理");
                            txtAddress.setText("如果平台拒绝，您可以联系客服咨询原因");
                            break;
                        case "105":  //退租成功
                            tv_btn01.setVisibility(View.VISIBLE);
                            tv_btn02.setVisibility(View.VISIBLE);
                            tv_btn02.setText("删除订单");
                            rl_status_02.setVisibility(View.VISIBLE);
                            rl_status_01.setVisibility(View.GONE);
                            if(!TextUtils.isEmpty(orderDetailBean.getMoney())){
                                tv_return_all_money.setText("￥"+Utils.getMoney(Utils.chu(orderDetailBean.getMoney(),"100")));
                            }else{
                                tv_return_all_money.setText("￥"+Utils.getMoney("0"));
                            }
                            rl_status_02.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    IntentUtil.gotoActivity(LeaseOrderRefundDetailActivity.this, WalletActivity.class);
                                }
                            });
                            break;
                        case "106": // 拒绝退租
                            tv_btn01.setVisibility(View.VISIBLE);
                            tv_btn02.setVisibility(View.VISIBLE);
                            tv_btn02.setText("删除订单");
                            rl_status_01.setVisibility(View.VISIBLE);
                            rl_status_02.setVisibility(View.GONE);
                            txtName.setText("拒绝原因:");
                            txtAddress.setText(orderDetailBean.getRefusedReason());
                            break;
                    }
                }else{
                    ToastUtil.showToast(LeaseOrderRefundDetailActivity.this,common.getInfo());
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
        HttpRequestUtils.httpRequest(LeaseOrderRefundDetailActivity.this, "撤销申请",RequestValue.LEASEMANAGE_UODOLEASEBRANCHORDER, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())){
                    ToastUtil.showToast(LeaseOrderRefundDetailActivity.this,"撤销成功!");

                   // getOrderReturnDetail();
                    finish();
                }else{
                    ToastUtil.showToast(LeaseOrderRefundDetailActivity.this,common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Bundle bundle = new Bundle();
        bundle.putInt("pageType",Integer.parseInt(pageType));
        IntentUtil.gotoActivity(this, MyLeaseOrderListActivity.class,bundle);
    }
}
