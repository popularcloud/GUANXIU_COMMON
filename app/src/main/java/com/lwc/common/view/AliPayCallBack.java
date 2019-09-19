package com.lwc.common.view;

/**
 * Created by yuqianli on 15/11/26.
 */
public interface AliPayCallBack {
    void OnAliPayResult(boolean success, boolean isWaiting, String msg) ;
}
