package com.lwc.common.module.integral.presenter;

import android.app.Activity;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.integral.activity.IntegralGoodsDetailActivity;
import com.lwc.common.module.integral.bean.IntegralGoodsDetailBean;
import com.lwc.common.module.integral.bean.IntegralgoodsBean;
import com.lwc.common.module.integral.view.IntegralGoodsDetailView;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author younge
 * @date 2019/4/1 0001
 * @email 2276559259@qq.com
 */
public class IntegralGoodsPresenter {

    private Activity activity;
    private IntegralGoodsDetailView integralGoodsDetailView;

    public IntegralGoodsPresenter(IntegralGoodsDetailActivity integralGoodsDetailView){
        this.activity = integralGoodsDetailView;
        this.integralGoodsDetailView = integralGoodsDetailView;
    }

    public void getIntegralGoodsDetail(String goodsId){
        Map<String,String> map = new HashMap<>();
        map.put("integral_id",goodsId);
        HttpRequestUtils.httpRequest(activity, "getgoodsDetail", RequestValue.GET_INTEGRAL_GOODSDETAIL,map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        try {
                            IntegralGoodsDetailBean partsBeanList = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"),IntegralGoodsDetailBean.class);
                            if(integralGoodsDetailView != null){
                                integralGoodsDetailView.onLoadGoodsDetailSuccess(partsBeanList);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            integralGoodsDetailView.onLoadError(e.getMessage());
                        }
                        break;
                    default:
                        integralGoodsDetailView.onLoadError(common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                integralGoodsDetailView.onLoadError(msg);
            }
        });
    }
}
