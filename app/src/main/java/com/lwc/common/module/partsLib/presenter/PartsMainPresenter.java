package com.lwc.common.module.partsLib.presenter;

import android.app.Activity;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.PartsTypeBean;
import com.lwc.common.module.partsLib.ui.view.PartsMainView;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author younge
 * @date 2018/12/26 0026
 * @email 2276559259@qq.com
 */
public class PartsMainPresenter {

    private Activity activity;
    private PartsMainView partsMainView;
    public PartsMainPresenter(Activity activity,PartsMainView partsMainView){
        this.activity = activity;
        this.partsMainView =  partsMainView;
    }

    /**
     * 获取配件类型
     */
    public void getPartsData(){
        HttpRequestUtils.httpRequest(activity, "getPartsData", RequestValue.GET_ACCESSORIES_TYPES,null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        try {
                            List<PartsTypeBean> partsBeanList = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<PartsTypeBean>>() {});
                            if(partsMainView != null){
                                partsMainView.onLoadPartsType(partsBeanList);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            partsMainView.onLoadError(e.getMessage());
                        }
                        break;
                    default:
                        partsMainView.onLoadError(common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                partsMainView.onLoadError(msg);
            }
        });
    }

    /**
     * 获取配件库首页轮播
     */
    public void getBannerData(){
        HttpRequestUtils.httpRequest(activity, "getBannerData", RequestValue.GET_ADVERTISING+"?local=5",null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        ArrayList<ADInfo>  infos = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<ADInfo>>() {});
                        partsMainView.onBannerLoadSuccess(infos);
                        break;
                    default:
                        partsMainView.onLoadError(common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                partsMainView.onLoadError(msg);
            }
        });
    }
}
