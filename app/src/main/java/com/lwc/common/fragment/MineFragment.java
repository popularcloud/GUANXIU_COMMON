package com.lwc.common.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.activity.UserGuideActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.HasMsg;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.integral.activity.IntegralOrderActivity;
import com.lwc.common.module.invoice.InvoiceActivity;
import com.lwc.common.module.message.ui.MyMsgActivity;
import com.lwc.common.module.mine.MyInvitationCodeActivity;
import com.lwc.common.module.mine.MyProfitActivity;
import com.lwc.common.module.mine.ShareActivity;
import com.lwc.common.module.mine.UserInfoActivity;
import com.lwc.common.module.order.ui.activity.MyCheckActivity;
import com.lwc.common.module.order.ui.activity.MyPackageActivity;
import com.lwc.common.module.repairs.ui.activity.AddressManagerActivity;
import com.lwc.common.module.setting.ui.SettingActivity;
import com.lwc.common.module.setting.ui.SuggestActivity;
import com.lwc.common.module.wallet.ui.WalletActivity;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.MyTextView;
import com.lwc.common.widget.CustomDialog;
import com.lwc.common.widget.PhotoBigFrameDialog;
import com.lwc.common.widget.ZQImageViewRoundOval;
import com.yanzhenjie.sofia.Sofia;
import com.yanzhenjie.sofia.StatusView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的
 * @author 何栋
 * @date 2017年11月20日
 * @Copyright: lwc
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.img_head)
    ZQImageViewRoundOval img_head;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_sign)
    TextView txt_sign;
    @BindView(R.id.txtActionbarTitle)
    MyTextView txtActionbarTitle;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.iv_myNewMsg)
    TextView iv_myNewMsg;
    @BindView(R.id.imgRight)
    ImageView imgRight;
    @BindView(R.id.iv_setting)
    ImageView iv_setting;
    @BindView(R.id.txtIdentity)
    TextView txtIdentity;
    @BindView(R.id.txtIntegral)
    LinearLayout txtIntegral;
    @BindView(R.id.rl_title)
    ViewGroup titleContainer;
    @BindView(R.id.tv_coupon)
    LinearLayout tv_coupon;
    @BindView(R.id.tv_Package)
    LinearLayout tv_Package;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_integral)
    TextView tv_integral;
    @BindView(R.id.tv_couponValue)
    TextView tv_couponValue;
    @BindView(R.id.status_view)
    StatusView status_view;

    private SharedPreferencesUtils preferencesUtils = null;
    private User user = null;
    private ImageLoaderUtil imageLoaderUtil = null;
    private String picture = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ImmersionBar.with(getActivity()).statusBarColor(Color.parseColor("#3599ff")).statusBarDarkFont(true).init();
    }



    @OnClick({R.id.txtFeedback, R.id.txtUserGuide, R.id.tv_coupon, R.id.tv_Package, R.id.img_head,
            R.id.txt_acount ,R.id.txt_machine, R.id.iv_msg, R.id.txtUserInfor, R.id.txt_name, R.id.txt_sign,
            R.id.txt_kefu, R.id.txtInvoice,R.id.txtIntegral,R.id.txt_addressManager,R.id.ll_money,
            R.id.ll_couponValue,R.id.ll_integral,R.id.iv_setting,R.id.txtInvitationCode,R.id.txtProfit})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.txtUserGuide:
                Bundle bundle = new Bundle();
                bundle.putString("type","8");
                IntentUtil.gotoActivity(getActivity(), UserGuideActivity.class,bundle);
                break;
            case R.id.tv_Package:
                if (Utils.gotoLogin(user, getActivity())) {
                    IntentUtil.gotoActivity(getActivity(), MyPackageActivity.class);
                }
                break;
            case R.id.tv_coupon:
            case R.id.ll_couponValue:
                if (Utils.gotoLogin(user, getActivity())) {
                    IntentUtil.gotoActivity(getActivity(), MyCheckActivity.class);
                }
                break;
            case R.id.img_head:
                if (Utils.gotoLogin(user, getActivity()) && !TextUtils.isEmpty(user.getUserHeadImage())) {
                    PhotoBigFrameDialog frameDialog = new PhotoBigFrameDialog(getActivity(), user.getUserHeadImage());
                    frameDialog.showNoticeDialog();
                }
                break;
            case R.id.txt_machine: //分享
                IntentUtil.gotoActivity(getActivity(), ShareActivity.class);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.txt_addressManager:
                if (Utils.gotoLogin(user, getActivity()))
                    IntentUtil.gotoActivity(getActivity(), AddressManagerActivity.class);
                break;
            case R.id.iv_msg:
                if (Utils.gotoLogin(user, getActivity())){
                    IntentUtil.gotoActivity(getActivity(), MyMsgActivity.class);
                }

                break;
            case R.id.txtUserInfor:
            case R.id.txt_name:
            case R.id.txt_sign:
                if (Utils.gotoLogin(user, getActivity()))
                    IntentUtil.gotoActivity(getActivity(), UserInfoActivity.class);
                break;
            case R.id.txt_kefu:
                DialogUtil.showMessageDg(getActivity(), "拨打电话", "400-881-0769", new CustomDialog.OnClickListener() {

                    @Override
                    public void onClick(CustomDialog dialog, int id, Object object) {
                        dialog.dismiss();
                        Utils.lxkf(getActivity(), null);
                    }
                });
                break;
            case R.id.txtInvoice: //发票与报销
                if (Utils.gotoLogin(user, getActivity()))
                    IntentUtil.gotoActivity(getActivity(), InvoiceActivity.class);
                break;
            case R.id.txt_acount:
            case R.id.ll_money:
                if (Utils.gotoLogin(user, getActivity()))
                    IntentUtil.gotoActivity(getActivity(), WalletActivity.class);
            break;
            case R.id.txtFeedback:
                if (Utils.gotoLogin(user, getActivity()))
                    IntentUtil.gotoActivity(getActivity(), SuggestActivity.class);
                break;
            case R.id.txtIntegral: //积分订单
            case R.id.ll_integral:
                if (Utils.gotoLogin(user, getActivity()))
                    IntentUtil.gotoActivity(getActivity(), IntegralOrderActivity.class);
                break;
            case R.id.txtInvitationCode: //我的邀请码
                if (Utils.gotoLogin(user, getActivity()))
                    IntentUtil.gotoActivity(getActivity(), MyInvitationCodeActivity.class);
                break;
            case R.id.txtProfit: //我的收益
                if (Utils.gotoLogin(user, getActivity()))
                    IntentUtil.gotoActivity(getActivity(), MyProfitActivity.class);
               // ToastUtil.showToast(getActivity(),"敬请期待");
                break;
            case R.id.iv_setting: //设置
                if (Utils.gotoLogin(user, getActivity())) {
                    IntentUtil.gotoActivityForResult(getActivity(), SettingActivity.class, 1991);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfor();
    }

    @SuppressLint("ResourceType")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && getActivity() != null){
            getUserInfor();
            ((MainActivity)getActivity()).hasMessage();
            ImmersionBar.with(getActivity())
                    .statusBarColor(R.color.blue_9ff)
                    .statusBarDarkFont(true)
                    .navigationBarColor(R.color.white).init();
        }
    }


    private void showDataInDisplay(){
            user = preferencesUtils.loadObjectData(User.class);
            if (user == null) {
                txt_name.setText("登录/注册");
                return;
            }
            LLog.i("用户信息" + user.toString());
            if (!TextUtils.isEmpty(user.getUserRealname())) {
                txt_name.setText(user.getUserRealname());
            } else if (!TextUtils.isEmpty(user.getUserName())) {
                txt_name.setText(user.getUserName());
            } else {
                txt_name.setText(user.getUserPhone());
            }

            if(!TextUtils.isEmpty(user.getBanlance())){
                tv_money.setText(user.getBanlance());
            }else{
                tv_money.setText("- -");
            }

            if(!TextUtils.isEmpty(user.getUserIntegral())){
                tv_integral.setText(Utils.chu(user.getUserIntegral(),"100"));
            }else{
                tv_integral.setText("- -");
            }

            if(!TextUtils.isEmpty(user.getCoupon())){
                tv_couponValue.setText(user.getCoupon());
            }else{
                tv_couponValue.setText("- -");
            }
            //取消性别显示
       /* if ("1".equals(user.getUserSex())) {
            Drawable drawable = getResources().getDrawable(R.drawable.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            txt_name.setCompoundDrawables(null, null, drawable, null);
        } else if ("0".equals(user.getUserSex())) {
            Drawable drawable = getResources().getDrawable(R.drawable.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            txt_name.setCompoundDrawables(null, null, drawable, null);
        }*/
            //替换下面的代码
            if (getActivity() != null) {
                txt_sign.setBackgroundColor(getActivity().getResources().getColor(R.color.transparent));
            }
            if (!TextUtils.isEmpty(user.getUserSignature())) {
                txt_sign.setText(user.getUserSignature());
            } else {
                txt_sign.setText("您还没有设置签名哦");
            }
            if (!TextUtils.isEmpty(user.getUserHeadImage()) && !user.getUserHeadImage().equals(picture)) {
                picture = user.getUserHeadImage();
                img_head.setType(ZQImageViewRoundOval.TYPE_CIRCLE);
                imageLoaderUtil.displayFromNetD(getActivity(), user.getUserHeadImage(), img_head);
            }
            updateVersionCode();
           /* if (user.getRoleId() != null && user.getRoleId().equals("5")) {
                tv_coupon.setVisibility(View.VISIBLE);
                tv_Package.setVisibility(View.VISIBLE);
            } else {
                tv_coupon.setVisibility(View.GONE);
                tv_Package.setVisibility(View.GONE);
                if (user.getIsSecrecy() != null && user.getIsSecrecy().equals("0")) {
                    txtIdentity.setText("未认证");
                } else if (user.getIsSecrecy() != null && user.getIsSecrecy().equals("1")) {
                    txtIdentity.setText("正在审核");
                } else if (user.getIsSecrecy() != null && user.getIsSecrecy().equals("2")) {
                    txtIdentity.setText("审核通过");
                } else if (user.getIsSecrecy() != null && user.getIsSecrecy().equals("3")) {
                    txtIdentity.setText("审核失败");
                }
            }*/
    }

    public void updateNewMsg(HasMsg hasMsg) {
        try {
            if(hasMsg!=null && hasMsg.getCount() > 0){
                iv_myNewMsg.setVisibility(View.VISIBLE);
                if(hasMsg.getCount() > 99){
                    iv_myNewMsg.setText("...");
                }else{
                    iv_myNewMsg.setText(String.valueOf(hasMsg.getCount()));
                }
            }else{
                iv_myNewMsg.setVisibility(View.GONE);
            }
        }catch (Exception e){}
    }

    public void updateVersionCode() {
        try {
            if (preferencesUtils == null) {
                preferencesUtils = SharedPreferencesUtils.getInstance(getContext());
            }
            String versionCode = preferencesUtils.loadString("versionCode");
          /*  if (iv_red != null && !TextUtils.isEmpty(versionCode) && VersionUpdataUtil.isNeedUpdate(getContext(), Integer.parseInt(versionCode))) {
                iv_red.setVisibility(View.VISIBLE);
            } else if (iv_red != null) {
                iv_red.setVisibility(View.GONE);
            }*/
        }catch (Exception e){}
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void init() {
        preferencesUtils = SharedPreferencesUtils.getInstance(getContext());
        user = preferencesUtils.loadObjectData(User.class);
        imageLoaderUtil = ImageLoaderUtil.getInstance();
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
                            String userRole = preferencesUtils.loadString("user_role");
                            String id = "3";
                            if (user!=null) {
                                id = user.getRoleId();
                            } else if (!TextUtils.isEmpty(userRole)) {
                                id = userRole;
                            }
                            user = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), User.class);
                            if (user != null && !TextUtils.isEmpty(id)) {
                                user.setRoleId(id);
                            }
                            if(user != null){
                                preferencesUtils.saveObjectData(user);
                            }
                            showDataInDisplay();
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

}
