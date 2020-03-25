package com.lwc.common.module.integral.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Address;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.integral.bean.IntegralGoodsDetailBean;
import com.lwc.common.module.repairs.ui.activity.AddressManagerActivity;
import com.lwc.common.module.wallet.ui.InputPayPwdAndAddressActivity;
import com.lwc.common.module.wallet.ui.PayPwdActivity;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.widget.CustomDialog;
import com.lwc.common.widget.ZQImageViewRoundOval;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @author younge
 * @date 2019/4/11 0011
 * @email 2276559259@qq.com
 * 积分兑换订单确认
 */
public class IntegralOrderConfirmActivity extends BaseActivity{

    /**
     * 跳转地址管理
     */
    private final int INTENT_ADDRESS_MANAGER = 1000;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtPhone)
    TextView txtPhone;
    @BindView(R.id.rLayoutAddress)
    RelativeLayout rLayoutAddress;

    @BindView(R.id.iv_header)
    ZQImageViewRoundOval iv_header;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_integral)
    TextView tv_integral;
    @BindView(R.id.et_my_desc)
    EditText et_my_desc;
    @BindView(R.id.btnConfirm)
    TextView btnConfirm;
    @BindView(R.id.lLayout0)
    LinearLayout lLayout0;

    private List<Address> addressesList = new ArrayList<>();
    private Address checkedAdd;
    private IntegralGoodsDetailBean integralGoodsDetailBean;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_integral_order_confirm;
    }

    @Override
    protected void findViews() {
        setTitle("兑换信息");
    }

    @Override
    protected void init() {
        //获取默认地址
        addressesList = DataSupport.findAll(Address.class);
        setDefaultAddress();
        integralGoodsDetailBean = (IntegralGoodsDetailBean) getIntent().getSerializableExtra("integralGoodsDetailBean");

        if(integralGoodsDetailBean != null){
            iv_header.setType(ZQImageViewRoundOval.TYPE_ROUND);
            ImageLoaderUtil.getInstance().displayFromNetD(IntegralOrderConfirmActivity.this,integralGoodsDetailBean.getIntegralCover(),iv_header);
            tv_title.setText(integralGoodsDetailBean.getIntegralName());

            String priceString =Utils.chu(String.valueOf(integralGoodsDetailBean.getIntegralNum()),"100")+" 积分";
            SpannableStringBuilder stringBuilder=new SpannableStringBuilder(priceString);
            AbsoluteSizeSpan ab=new AbsoluteSizeSpan(12,true);
            //文本字体绝对的大小
            stringBuilder.setSpan(ab,priceString.length() - 2,priceString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_integral.setText(stringBuilder);
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedAdd == null){
                    ToastUtil.showToast(IntegralOrderConfirmActivity.this,"请选择地址");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("need_integral", integralGoodsDetailBean.getIntegralNum());
                IntentUtil.gotoActivityForResult(IntegralOrderConfirmActivity.this, InputPayPwdAndAddressActivity.class, bundle, 12302);
            }
        });
        rLayoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.gotoActivityForResult(IntegralOrderConfirmActivity.this, AddressManagerActivity.class, INTENT_ADDRESS_MANAGER);
            }
        });
    }

    /**
     * 设置默认地址
     */
    private void setDefaultAddress() {
        for (int i = 0; i < addressesList.size(); i++) {
            Address address = addressesList.get(i);
            if (address.getIsDefault() == 1) {
                lLayout0.setVisibility(View.VISIBLE);
                checkedAdd = address;
                txtName.setText("" + checkedAdd.getContactName());
                txtPhone.setText(checkedAdd.getContactPhone());
                txtAddress.setText(checkedAdd.getProvinceName() + checkedAdd.getCityName()+ checkedAdd.getContactAddress().replace("_", ""));
                break;
            }
        }
        if(addressesList == null || addressesList.size() == 0){
            checkedAdd = null;
            lLayout0.setVisibility(View.GONE);
            txtName.setText("");
            txtPhone.setText("");
            txtAddress.setText("点击选择地址");
        }
    }

    @Override
    protected void initGetData() {

    }

    @Override
    protected void widgetListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode == 12302) {
                String payPassword = data.getStringExtra("PWD");

                String integral_id = integralGoodsDetailBean.getIntegralId();

                Map<String, String> params = new HashMap<>();
                params.put("payPassword", payPassword);
                params.put("addressId", checkedAdd.getUserAddressId());
                params.put("integral_id", integral_id);
                params.put("integral_count", "1");
                params.put("buyer_note",et_my_desc.getText().toString());
                payForIntegralGoods(params);
            }else if(requestCode == INTENT_ADDRESS_MANAGER){
                checkedAdd = (Address) data.getSerializableExtra("address");
                txtName.setVisibility(View.VISIBLE);
                txtPhone.setVisibility(View.VISIBLE);
                txtName.setText("" + checkedAdd.getContactName());
                txtPhone.setText(checkedAdd.getContactPhone());
                txtAddress.setText("地址：" + checkedAdd.getContactAddress().replace("_", ""));
            }
        }else{
            if(requestCode == INTENT_ADDRESS_MANAGER){
                //获取默认地址
                addressesList = DataSupport.findAll(Address.class);
                setDefaultAddress();
            }
        }
    }

    private void payForIntegralGoods(Map<String, String> params) {
        HttpRequestUtils.httpRequest(this, "payForIntegralGoods", RequestValue.INTEGRAL_EXCHANGE, params, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        ToastUtil.showToast(IntegralOrderConfirmActivity.this, "操作成功");
                        if (integralGoodsDetailBean != null) {
                            double needReduceIntegral = Double.parseDouble(integralGoodsDetailBean.getIntegralNum());
                            String integral = (String) SharedPreferencesUtils.getParam(IntegralOrderConfirmActivity.this, "MyIntegral", "0");
                            double myIntegral = Double.parseDouble(integral);
                            double resultIntegral = myIntegral - needReduceIntegral;
                            SharedPreferencesUtils.setParam(IntegralOrderConfirmActivity.this, "MyIntegral", String.valueOf(resultIntegral));
                        }
                        IntentUtil.gotoActivity(IntegralOrderConfirmActivity.this,IntegralMainActivity.class);
                        break;
                    default:
                        if (common.getInfo().equals("支付密码错误")) {
                        DialogUtil.showMessageDg(IntegralOrderConfirmActivity.this, "温馨提示", "忘记密码", "重新输入", "您输入的支付密码错误!", new CustomDialog.OnClickListener() {
                            @Override
                            public void onClick(CustomDialog dialog, int id, Object object) {
                                dialog.dismiss();
                                IntentUtil.gotoActivity(IntegralOrderConfirmActivity.this, PayPwdActivity.class);
                            }
                        }, new CustomDialog.OnClickListener() {
                            @Override
                            public void onClick(CustomDialog dialog, int id, Object object) {
                                dialog.dismiss();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("need_integral", integralGoodsDetailBean.getIntegralNum());
                                IntentUtil.gotoActivityForResult(IntegralOrderConfirmActivity.this, InputPayPwdAndAddressActivity.class, bundle, 12302);
                            }
                        });
                    }else{
                        ToastUtil.showToast(IntegralOrderConfirmActivity.this, "兑换失败！" + common.getInfo());
                    }

                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showToast(IntegralOrderConfirmActivity.this, "兑换失败！");
            }
        });
    }
}
