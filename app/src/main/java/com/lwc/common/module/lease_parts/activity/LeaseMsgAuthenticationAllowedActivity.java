package com.lwc.common.module.lease_parts.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.bean.PickerView;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.PhotoToken;
import com.lwc.common.module.bean.Sheng;
import com.lwc.common.module.bean.Shi;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.bean.Xian;
import com.lwc.common.module.lease_parts.bean.AuthenticatBean;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.widget.RoundAngleImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 租赁认证通过
 *
 */
public class LeaseMsgAuthenticationAllowedActivity extends BaseActivity {

    @BindView(R.id.iv_id_back)
    RoundAngleImageView iv_id_back;
    @BindView(R.id.iv_id_positive)
    RoundAngleImageView iv_id_positive;
    @BindView(R.id.iv_business_license)
    RoundAngleImageView iv_business_license;
    @BindView(R.id.iv_leaseContract)
    RoundAngleImageView iv_leaseContract;

    @BindView(R.id.tv_authentication_type)
    TextView tv_authentication_type;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_company_name)
    TextView tv_company_name;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_id_card)
    TextView tv_id_card;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_addressDetail)
    TextView tv_addressDetail;
    @BindView(R.id.tv_ic_creditCode)
    TextView tv_ic_creditCode;


    @BindView(R.id.ll_address)
    LinearLayout ll_address;
    @BindView(R.id.ll_detail_address)
    LinearLayout ll_detail_address;
    @BindView(R.id.ll_businessCode)
    LinearLayout ll_businessCode;
    @BindView(R.id.ll_businessCode_img)
    LinearLayout ll_businessCode_img;
    @BindView(R.id.ll_field_img)
    LinearLayout ll_field_img;
    @BindView(R.id.ll_company)
    LinearLayout ll_company;


    private String idPositivePath;
    private String idBackPath;
    private String businessLicensePath;
    private String leaseContractPath;

    private ImageView presentImgView;

    private String phone;
    private File cutfile;

    private int AUTHENTICATIONTYPE = 0; //0企业  1.个人
    private boolean isUserAgreement = false;

    private PhotoToken token;
    private ProgressDialog pd;
    private String imgPath1;
    private List<String> urlStrs = new ArrayList();


    private ArrayList<PickerView> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private List<Sheng> shengs = new ArrayList<>();
    private List<Shi> shis = new ArrayList<>();
    private List<Xian> xians = new ArrayList<>();
    //排序后的城市
    private List<List<Shi>> sortShi = new ArrayList<>();
    //排序后的县
    private List<List<List<Xian>>> sortXian = new ArrayList<>();
    /**
     * 选中的省
     */
    private Sheng selectedSheng;
    /**
     * 选中的市
     */
    private Shi selectedShi;
    /**
     * 选中的县
     */
    private Xian selectedXian;

    private User user = null;

    private AuthenticatBean authenticatBean;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_lease_msg_authentication_allowed;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void init() {
        setTitle("实名认证");
        getAuthenticationData();
    }

    @Override
    protected void initGetData() {

    }

    @Override
    protected void widgetListener() {

    }

    private void getAuthenticationData() {
        user = SharedPreferencesUtils.getInstance(this).loadObjectData(User.class);
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id",user.getUserId());
        HttpRequestUtils.httpRequest(this, "查看我的认证信息",RequestValue.LEASEMANAGE_GETAUTHORGUSER , params, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if("1".equals(common.getStatus())) {
                    // ToastUtil.showToast(getActivity(),"获取我的订单成功!");
                    authenticatBean = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), AuthenticatBean.class);
                    if(authenticatBean != null){
                        tv_name.setText(authenticatBean.getUserRealname());
                        tv_phone.setText(authenticatBean.getUserPhone());
                        tv_id_card.setText(authenticatBean.getUserIdcard());
                        ImageLoaderUtil.getInstance().displayFromNetDCircular(LeaseMsgAuthenticationAllowedActivity.this,authenticatBean.getIdcardFace(),iv_id_positive,R.drawable.img_default_load);
                        ImageLoaderUtil.getInstance().displayFromNetDCircular(LeaseMsgAuthenticationAllowedActivity.this,authenticatBean.getIdcardBack(),iv_id_back,R.drawable.img_default_load);
                        if(TextUtils.isEmpty(authenticatBean.getCompanyNo())){
                            tv_authentication_type.setText("个人认证");
                            ll_company.setVisibility(View.GONE);
                            ll_address.setVisibility(View.GONE);
                            ll_detail_address.setVisibility(View.GONE);
                            ll_businessCode.setVisibility(View.GONE);
                            ll_businessCode_img.setVisibility(View.GONE);
                            ll_field_img.setVisibility(View.GONE);

                        }else{
                            tv_authentication_type.setText("企业认证");
                            tv_company_name.setText(authenticatBean.getCompanyName());
                            tv_address.setText(authenticatBean.getCompanyProvinceName()+authenticatBean.getCompanyCityName()+authenticatBean.getCompanyTownName());
                            tv_addressDetail.setText(authenticatBean.getCompanyAddress());
                            tv_ic_creditCode.setText(authenticatBean.getCompanyNo());
                            ImageLoaderUtil.getInstance().displayFromNetD(LeaseMsgAuthenticationAllowedActivity.this,authenticatBean.getCompanyImg(),iv_business_license);
                            ImageLoaderUtil.getInstance().displayFromNetD(LeaseMsgAuthenticationAllowedActivity.this,authenticatBean.getCompanyPlaceImg(),iv_leaseContract);
                        }
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
