package com.lwc.common.module.lease_parts.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.MyMsg;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.invoice.InvoiceActivity;
import com.lwc.common.module.lease_parts.activity.LeaseMsgAuthenticationActivity;
import com.lwc.common.module.lease_parts.activity.LeaseMsgAuthenticationAllowedActivity;
import com.lwc.common.module.lease_parts.activity.LeaseNeedPayActivity;
import com.lwc.common.module.lease_parts.activity.MyCollectActivity;
import com.lwc.common.module.lease_parts.activity.MyLeaseOrderListActivity;
import com.lwc.common.module.lease_parts.activity.PaySuccessActivity;
import com.lwc.common.module.message.ui.MsgListActivity;
import com.lwc.common.module.repairs.ui.activity.AddressManagerActivity;
import com.lwc.common.module.wallet.ui.WalletActivity;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.MsgReadUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.Utils;
import com.lwc.common.widget.GetPhoneDialog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LeaseMinetFragment extends BaseFragment{

    @BindView(R.id.ll_my_order)
    LinearLayout ll_my_order;
    @BindView(R.id.ll_authentication)
    LinearLayout ll_authentication;
    @BindView(R.id.iv_header)
    ImageView iv_header;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_authentication)
    TextView tv_authentication;
    @BindView(R.id.tv_collect_value)
    TextView tv_collect_value;
    @BindView(R.id.tv_money_value)
    TextView tv_money_value;
    @BindView(R.id.tv_msg)
    TextView tv_msg;

    @BindView(R.id.tv_type01_msg)
    TextView tv_type01_msg;
    @BindView(R.id.tv_type02_msg)
    TextView tv_type02_msg;
    @BindView(R.id.tv_type03_msg)
    TextView tv_type03_msg;
    @BindView(R.id.tv_type04_msg)
    TextView tv_type04_msg;
    @BindView(R.id.tv_type05_msg)
    TextView tv_type05_msg;
    @BindView(R.id.tv_type06_msg)
    TextView tv_type06_msg;
    @BindView(R.id.tv_type07_msg)
    TextView tv_type07_msg;
    @BindView(R.id.tv_type08_msg)
    TextView tv_type08_msg;

    @BindView(R.id.tv_heaer_authentication)
    TextView tv_heaer_authentication;

    private SharedPreferencesUtils preferencesUtils = null;
    private User user = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lease_mine, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void init() {

    }

    @Override
    public void onResume() {
        super.onResume();

        getUserInfor();

        getOrderMessage();

        //获取未读租赁消息
        MsgReadUtil.hasMessage(getActivity(),tv_msg);
    }



    @Override
    public void initEngines(View view) {

    }

    @Override
    public void getIntentData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && getActivity() != null){
            ImmersionBar.with(getActivity())
                    .statusBarColor(R.color.red_money)
                    .statusBarDarkFont(true).init();

           getUserInfor();

            //获取未读租赁消息
            MsgReadUtil.hasMessage(getActivity(),tv_msg);
        }
    }

    @OnClick({R.id.ll_my_order,R.id.tv_need_pay_money,R.id.ll_authentication,R.id.tv_type01,R.id.tv_type02,R.id.tv_type03,R.id.tv_type04,
            R.id.tv_type05,R.id.tv_type06,R.id.tv_type07,R.id.ll_address,R.id.ll_txtInvoice,R.id.ll_kefu,R.id.tv_collect_value,R.id.tv_collect,
            R.id.tv_money,R.id.tv_money_value,R.id.iv_right,R.id.tv_msg,R.id.tv_heaer_authentication})
    public void onBtnClick(View view){

        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.ll_my_order:
                IntentUtil.gotoActivity(getActivity(), MyLeaseOrderListActivity.class);
                break;
            case R.id.tv_need_pay_money:
                IntentUtil.gotoActivity(getActivity(), LeaseNeedPayActivity.class);
                break;
            case R.id.ll_authentication:
            case R.id.tv_heaer_authentication:
                switch (user.getIsCertification()){
                    case "1":
                    case "2":
                        IntentUtil.gotoActivity(getActivity(), LeaseMsgAuthenticationAllowedActivity.class);
                        break;
                    case "3":
                    case "0":
                        IntentUtil.gotoActivity(getActivity(), LeaseMsgAuthenticationActivity.class);
                        break;
                }
                break;
            case R.id.tv_type01:
                bundle.putInt("pageType",1);
                IntentUtil.gotoActivity(getActivity(), MyLeaseOrderListActivity.class,bundle);
                break;
            case R.id.tv_type02:
                bundle.putInt("pageType",2);
                IntentUtil.gotoActivity(getActivity(), MyLeaseOrderListActivity.class,bundle);
                break;
            case R.id.tv_type03:
                bundle.putInt("pageType",3);
                IntentUtil.gotoActivity(getActivity(), MyLeaseOrderListActivity.class,bundle);
                break;
            case R.id.tv_type04:
                bundle.putInt("pageType",4);
                IntentUtil.gotoActivity(getActivity(), MyLeaseOrderListActivity.class,bundle);
                break;
            case R.id.tv_type05:
                bundle.putInt("pageType",5);
                IntentUtil.gotoActivity(getActivity(), MyLeaseOrderListActivity.class,bundle);
                break;
            case R.id.tv_type06:
                bundle.putInt("pageType",6);
                IntentUtil.gotoActivity(getActivity(), MyLeaseOrderListActivity.class,bundle);
                break;
            case R.id.tv_type07:
                bundle.putInt("pageType",7);
                IntentUtil.gotoActivity(getActivity(), MyLeaseOrderListActivity.class,bundle);
                break;
            case R.id.ll_address:
                IntentUtil.gotoActivity(getActivity(), AddressManagerActivity.class);
                break;
            case R.id.ll_txtInvoice: //发票与报销
                IntentUtil.gotoActivity(getActivity(), InvoiceActivity.class);
                break;
            case R.id.tv_collect_value:
            case R.id.tv_collect:
                IntentUtil.gotoActivity(getActivity(), MyCollectActivity.class);
                break;
            case R.id.tv_money_value:
            case R.id.tv_money:
                IntentUtil.gotoActivity(getActivity(), WalletActivity.class);
                break;
            case R.id.ll_kefu: //发票与报销
                GetPhoneDialog picturePopupWindow = new GetPhoneDialog(getContext(), new GetPhoneDialog.CallBack() {
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
            case R.id.iv_right:
            case R.id.tv_msg:
                MyMsg msg = new MyMsg();
                msg.setMessageType("5");
                msg.setMessageTitle("租赁消息");
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("myMsg", msg);
                IntentUtil.gotoActivity(getContext(), MsgListActivity.class,bundle1);
                break;
        }
    }


    /**
     * 更新用户信息
     */
    private void getUserInfor() {
        HttpRequestUtils.httpRequest(getActivity(), "getUserInfor", RequestValue.USER_INFO, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if (common != null) {
                    switch (common.getStatus()) {
                        case "1":
                            user = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), User.class);
                            if(user != null){
                                SharedPreferencesUtils.getInstance(getActivity()).saveObjectData(user);
                            }
                            loadUI();
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
            }
        });
    }

    private void loadUI(){
        if(user == null){
            return;
        }
        ImageLoaderUtil.getInstance().displayFromNetDCircularT(getContext(),user.getUserHeadImage(),iv_header,R.drawable.ic_lease_default_head);
        if(!TextUtils.isEmpty(user.getUserRealname())){
            tv_name.setText(user.getUserRealname());
        }else{
            tv_name.setText(user.getUserPhone());
        }

        tv_money_value.setText(user.getBanlance());
        tv_collect_value.setText(user.getCollectionNum());
      switch (user.getIsCertification()){
          case "0":
              tv_authentication.setText("未认证");
              tv_heaer_authentication.setText("未实名 >");
              break;
          case "1":
              tv_authentication.setText("审核中");
              tv_heaer_authentication.setText("未实名 >");
              break;
          case "2":
              tv_authentication.setText("已认证");
              tv_heaer_authentication.setText("已实名 >");
              break;
          case "3":
              tv_authentication.setText("认证失败");
              tv_heaer_authentication.setText("未实名 >");
              break;
      }
    }

    private void getOrderMessage(){
        HttpRequestUtils.httpRequest(getActivity(), "查询用户订单数量", RequestValue.LEASEMANAGE_ORDERNUMDATA, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String result) {
                Common common = JsonUtil.parserGsonToObject(result, Common.class);
                if (common.getStatus().equals("1")) {
                    HashMap<String,String> current = JsonUtil.parserGsonToMap(JsonUtil.getGsonValueByKey(result, "data"), new TypeToken<HashMap<String, String>>(){});

                    if("0".equals(current.get("1"))){  //待付款
                        tv_type01_msg.setVisibility(View.GONE);
                    }else{
                        tv_type01_msg.setVisibility(View.VISIBLE);
                        tv_type01_msg.setText(current.get("1"));
                    }

                    if("0".equals(current.get("2"))){ //代发货
                        tv_type02_msg.setVisibility(View.GONE);
                    }else{
                        tv_type02_msg.setVisibility(View.VISIBLE);
                        tv_type02_msg.setText(current.get("2"));
                    }

                    if("0".equals(current.get("3"))){  //待收货
                        tv_type03_msg.setVisibility(View.GONE);
                    }else{
                        tv_type03_msg.setVisibility(View.VISIBLE);
                        tv_type03_msg.setText(current.get("3"));
                    }

                    if("0".equals(current.get("4"))){  //租用中
                        tv_type04_msg.setVisibility(View.GONE);
                    }else{
                        tv_type04_msg.setVisibility(View.VISIBLE);
                        tv_type04_msg.setText(current.get("4"));
                    }

                    if("0".equals(current.get("5"))){ //已逾期
                        tv_type05_msg.setVisibility(View.GONE);
                    }else{
                        tv_type05_msg.setVisibility(View.VISIBLE);
                        tv_type05_msg.setText(current.get("5"));
                    }

                    if("0".equals(current.get("8"))){ //退租
                        tv_type07_msg.setVisibility(View.GONE);
                    }else{
                        tv_type07_msg.setVisibility(View.VISIBLE);
                        tv_type07_msg.setText(current.get("8"));
                    }

                    if("0".equals(current.get("7"))){  //退款
                        tv_type06_msg.setVisibility(View.GONE);
                    }else{
                        tv_type06_msg.setVisibility(View.VISIBLE);
                        tv_type06_msg.setText(current.get("7"));
                    }

                    if("0".equals(current.get("6"))){ //缴费
                        tv_type08_msg.setVisibility(View.GONE);
                    }else{
                        tv_type08_msg.setVisibility(View.VISIBLE);
                        tv_type08_msg.setText(current.get("6"));
                    }
                }

            }

            @Override
            public void returnException(Exception e, String msg) {
                //ToastUtil.showToast(getContext(), msg);
            }
        });
    }
}
