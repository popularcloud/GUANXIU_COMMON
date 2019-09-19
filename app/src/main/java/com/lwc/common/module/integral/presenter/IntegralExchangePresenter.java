package com.lwc.common.module.integral.presenter;

import android.app.Activity;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.integral.activity.IntegralExchangeRecordActivity;
import com.lwc.common.module.integral.bean.IntegralExchangeBean;
import com.lwc.common.module.integral.view.IntegralExchangeView;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author younge
 * @date 2019/3/28 0028
 * @email 2276559259@qq.com
 */
public class IntegralExchangePresenter {

    private Activity activity;
    private IntegralExchangeView integralExchangeView;

    public IntegralExchangePresenter(IntegralExchangeRecordActivity integralExchangeRecordActivity){
        this.activity = integralExchangeRecordActivity;
        integralExchangeView = integralExchangeRecordActivity;
    }

    public void getUserIntegralMsg(String currentPage){
        Map<String,String> param = new HashMap<>();
        param.put("curPage",currentPage);
        HttpRequestUtils.httpRequest(activity, "/integral/exchange/goods", RequestValue.GET_INTEGRAL_EXCHANGE_GOODS,param, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        try {
                            List<IntegralExchangeBean> integralExchangeBeanList = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<IntegralExchangeBean>>() {});
                            if(integralExchangeView != null){
                                integralExchangeView.onGetUserIntegral(integralExchangeBeanList);
                            }else{
                                integralExchangeView.onLoadError("暂无数据");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            integralExchangeView.onLoadError(e.getMessage());
                        }
                        break;
                    default:
                        integralExchangeView.onLoadError(common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                integralExchangeView.onLoadError(msg);
            }
        });
    }
}
