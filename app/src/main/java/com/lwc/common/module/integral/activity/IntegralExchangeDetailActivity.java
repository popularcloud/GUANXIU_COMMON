package com.lwc.common.module.integral.activity;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.integral.bean.IntegralExchangeDetailBean;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.widget.ZQImageViewRoundOval;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * @author younge
 * @date 2019/4/11 0011
 * @email 2276559259@qq.com
 * 兑换详情
 */
public class IntegralExchangeDetailActivity extends BaseActivity{

    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtPhone)
    TextView txtPhone;

    @BindView(R.id.iv_header)
    ZQImageViewRoundOval iv_header;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_order_number)
    TextView tv_order_number;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_integral)
    TextView tv_integral;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.tv_my_desc)
    TextView tv_my_desc;
    @BindView(R.id.tv_platform_desc)
    TextView tv_platform_desc;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_integral_exchange_detail;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void init() {
        setTitle("兑换详情");
    }

    @Override
    protected void initGetData() {
        String exchange_id = getIntent().getStringExtra("exchange_id");
        Map<String,String> param = new HashMap<>();
        param.put("exchange_id",exchange_id);
        HttpRequestUtils.httpRequest(this, "integral/exchange/goodsDetail 兑换详情", RequestValue.GET_INTEGRAL_EXCHANGE_GOODSDETAIL,param, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        try {
                           IntegralExchangeDetailBean integralExchangeDetailBean = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"),IntegralExchangeDetailBean.class);

                            txtAddress.setText(integralExchangeDetailBean.getOrderContactAddress());
                            txtName.setText(integralExchangeDetailBean.getOrderContactName());
                            txtPhone.setText(integralExchangeDetailBean.getOrderContactPhone());

                            iv_header.setType(ZQImageViewRoundOval.TYPE_ROUND);
                            ImageLoaderUtil.getInstance().displayFromNetD(IntegralExchangeDetailActivity.this,integralExchangeDetailBean.getIntegralCover(),iv_header);
                            tv_title.setText(integralExchangeDetailBean.getIntegralName());
                            tv_time.setText(integralExchangeDetailBean.getCreateTime());

                            if("2".equals(integralExchangeDetailBean.getExchangeType())){
                                String priceString ="抽奖礼品";
                                SpannableStringBuilder stringBuilder=new SpannableStringBuilder(priceString);
                                AbsoluteSizeSpan ab=new AbsoluteSizeSpan(12,true);
                                //文本字体绝对的大小
                                stringBuilder.setSpan(ab,0,priceString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                tv_integral.setText(stringBuilder);
                            }else{
                                String priceString =Utils.chu(String.valueOf(integralExchangeDetailBean.getIntegralNum()),"100")+" 积分";
                                SpannableStringBuilder stringBuilder=new SpannableStringBuilder(priceString);
                                AbsoluteSizeSpan ab=new AbsoluteSizeSpan(12,true);
                                //文本字体绝对的大小
                                stringBuilder.setSpan(ab,priceString.length() - 2,priceString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                tv_integral.setText(stringBuilder);
                            }

                          /*  String priceString =Utils.chu(String.valueOf(integralExchangeDetailBean.getIntegralNum()),"100")+" 积分";
                            SpannableStringBuilder stringBuilder=new SpannableStringBuilder(priceString);
                            AbsoluteSizeSpan ab=new AbsoluteSizeSpan(12,true);
                            //文本字体绝对的大小
                            stringBuilder.setSpan(ab,priceString.length() - 2,priceString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            tv_integral.setText(stringBuilder);*/

                            tv_status.setText(integralExchangeDetailBean.getExchangeStatus() == 1?"兑换中":"兑换成功");
                            if(!TextUtils.isEmpty(integralExchangeDetailBean.getBuyerNote())){
                                tv_my_desc.setText(integralExchangeDetailBean.getBuyerNote());
                            }

                            if(!TextUtils.isEmpty(integralExchangeDetailBean.getSellerNote())){
                                tv_platform_desc.setText(integralExchangeDetailBean.getSellerNote());
                            }

                            tv_order_number.setText(integralExchangeDetailBean.getExchangeId());

                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.showToast(IntegralExchangeDetailActivity.this,e.getMessage());
                        }
                        break;
                    default:
                        ToastUtil.showToast(IntegralExchangeDetailActivity.this,common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showToast(IntegralExchangeDetailActivity.this,msg);
            }
        });
    }

    @Override
    protected void widgetListener() {

    }
}
