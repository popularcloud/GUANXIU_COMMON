package com.lwc.common.module.integral.presenter;

import android.app.Activity;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.integral.bean.IntegralgoodsBean;
import com.lwc.common.module.integral.activity.IntegralMainActivity;
import com.lwc.common.module.integral.view.IntegralMainView;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author younge
 * @date 2019/3/26 0026
 * @email 2276559259@qq.com
 */
public class IntegralMainPresenter{

    private Activity activity;
    private IntegralMainView integralMainView;

    public IntegralMainPresenter(IntegralMainActivity integralMainActivity){
        this.activity = integralMainActivity;
        integralMainView = integralMainActivity;
    }

    /**
     * 获取配件类型
     */
    public void getPartsData(){
        HttpRequestUtils.httpRequest(activity, "getIntegralgoods", RequestValue.GET_INTEGRAL_GOODS,null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        try {
                            List<IntegralgoodsBean> partsBeanList = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<IntegralgoodsBean>>() {});
                            if(integralMainView != null){
                                integralMainView.onLoadGoods(partsBeanList);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            integralMainView.onLoadError(e.getMessage());
                        }
                        break;
                    default:
                        integralMainView.onLoadError(common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                integralMainView.onLoadError(msg);
            }
        });
    }

    /**
     * 获取积分兑换首页轮播
     */
    public void getBannerData(){
        HttpRequestUtils.httpRequest(activity, "getBannerData", RequestValue.GET_ADVERTISING+"?local=7",null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        ArrayList<ADInfo>  infos = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<ADInfo>>() {});
                        integralMainView.onBannerLoadSuccess(infos);
                        break;
                    default:
                        integralMainView.onLoadError(common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                integralMainView.onLoadError(msg);
            }
        });
    }
}
