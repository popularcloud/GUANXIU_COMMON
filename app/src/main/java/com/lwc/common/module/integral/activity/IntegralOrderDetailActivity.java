package com.lwc.common.module.integral.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.module.integral.bean.UserIntegralBean;
import com.lwc.common.utils.Utils;
import com.yanzhenjie.sofia.Sofia;

import butterknife.BindView;

/**
 * @author younge
 * @date 2019/4/3 0003
 * @email 2276559259@qq.com
 */
public class IntegralOrderDetailActivity extends BaseActivity{

    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_order_no)
    TextView tv_order_no;
    @BindView(R.id.tv_remarks)
    TextView tv_remarks;
    @BindView(R.id.tv_integral)
    TextView tv_integral;
    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_integral_order_detail;
    }

    @Override
    protected void findViews() {
        setTitle("积分详情");
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initGetData() {
        getIntegralOrderDetailData();
    }

    @Override
    protected void widgetListener() {

    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        Sofia.with(this)
                .statusBarBackground(Color.parseColor("#fb5251"))
                .statusBarLightFont();
    }


    private void getIntegralOrderDetailData(){
        UserIntegralBean.DataBean dataBean = (UserIntegralBean.DataBean) getIntent().getSerializableExtra("IntegralOrderDetail");

        if(dataBean == null){
            return;
        }
        switch (dataBean.getTransactionStatus()){
            case "1":
                tv_status.setText("已完成");
                break;
            case "2":
                tv_status.setText("处理中");
                break;
            case "3":
                tv_status.setText("审核失败");
                break;
        }

        switch (dataBean.getTransactionScene()){
            case 1:
                tv_type.setText("订单结算");
                break;
            case 2:
                tv_type.setText("购买套餐");
                break;
            case 3:
                tv_type.setText("积分兑换");
                break;
            case 4:
                tv_type.setText("签到奖励");
                break;
            case 5:
                if(dataBean.getPaymentType().equals("0")){
                    tv_type.setText("积分抽奖");
                }else{  //转出
                    tv_type.setText("抽奖奖励");
                }
                break;
        }

        tv_time.setText(dataBean.getCreateTime());
        tv_order_no.setText(dataBean.getTransactionNo());
        tv_remarks.setText(dataBean.getTransactionRemark());

        if("0".equals(dataBean.getPaymentType())){
            tv_integral.setText("-"+ Utils.chu(dataBean.getUserIntegral(),"100"));
        }else{
            tv_integral.setText("+"+Utils.chu(dataBean.getUserIntegral(),"100"));
        }

    }
}
