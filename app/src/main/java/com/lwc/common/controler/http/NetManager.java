package com.lwc.common.controler.http;

import android.util.Log;

import com.lwc.common.configs.ServerConfig;

/**
 * 网络请求管理类
 *
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 */
public class NetManager {

  /**
   * 主机名称
   */
//    public final static String HOST = "http://www.lsh-sd.com:9999";
  //测试
//    public static String HOST = "http://liyifu.ngrok.cc";
//  public static final String HOST = ServerConfig.SERVER_API_URL;
  //    public static String HOST = "http://192.168.0.166:8080";
//  public static String HOST = "http://119.23.215.51";


  /**
   * 获取请求的url;
   *
   * @param key
   * @return
   */
  public static String getUrl(String key) {
    return ServerConfig.SERVER_API_URL + key;
  }
}
