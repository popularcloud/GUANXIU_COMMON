package com.lwc.common.module.integral.presenter;

import android.app.Activity;

import com.lwc.common.activity.BaseActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.integral.activity.IntegralOrderActivity;
import com.lwc.common.module.integral.bean.UserIntegralBean;
import com.lwc.common.module.integral.view.IntegralOrderView;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author younge
 * @date 2019/3/28 0028
 * @email 2276559259@qq.com
 */
public class IntegralOrderPresenter{

    private Activity activity;
    private IntegralOrderView integralOrderView;

    public IntegralOrderPresenter(BaseActivity activity, IntegralOrderView integralOrderView){
        this.activity = activity;
        this.integralOrderView = integralOrderView;
    }

    public void getUserIntegralMsg(String currentPage){
        Map<String,String> param = new HashMap<>();
        param.put("curPage",currentPage);
        HttpRequestUtils.httpRequest(activity, "getIntegralMsg", RequestValue.GET_INTEGRAL_RECORD,param, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        try {
                            UserIntegralBean userIntegralBean = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"),UserIntegralBean.class);
                            if(integralOrderView != null){
                                integralOrderView.onGetUserIntegral(userIntegralBean);
                            }else{
                                integralOrderView.onLoadError("暂无数据");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            integralOrderView.onLoadError(e.getMessage());
                        }
                        break;
                    default:
                        integralOrderView.onLoadError(common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                integralOrderView.onLoadError(msg);
            }
        });
    }
}
