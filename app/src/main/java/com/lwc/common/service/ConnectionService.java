package com.lwc.common.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lwc.common.configs.ServerConfig;
import com.lwc.common.module.bean.User;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.NetUtil;
import com.lwc.common.utils.SharedPreferencesUtils;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;


public class ConnectionService extends Service {
	private static final String TAG = "BackService";
	/** 心跳检测时间  */
	private static final long HEART_BEAT_RATE = 6 * 1000;
	/** 重连间隔时间  */
	private static final long DELAY_MILLIS = 10000;
	//WebSocketClient 和 address
	private WebSocketClient mWebSocketClient;
	private String address = "ws:"+ ServerConfig.SERVER_API_URL.substring(5, ServerConfig.SERVER_API_URL.length())+"/ckLogin";
	/** 消息广播  */
	public static final String MESSAGE_ACTION = "com.lwc.common.message_ACTION";
	/** 心跳广播  */
	public static final String HEART_BEAT_ACTION = "com.lwc.common.heart_beat_ACTION";

	private long sendTime = 0L;
	private int count = 0;
	private boolean isRemove=true;
	private User user;
	private boolean isConn=false;
	private boolean isDestroy = false;

	@Override
	public void onCreate() {
		super.onCreate();
		user = SharedPreferencesUtils.getInstance(getApplicationContext()).loadObjectData(User.class);
		if (user == null){
			return;
		}
		new InitSocketThread().start();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// 发送心跳包
	private Handler mHandler = new Handler();
	private Runnable heartBeatRunnable = new Runnable() {
		@Override
		public void run() {
			if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
				boolean isSuccess = sendMsg("PING");// 就发送一个\r\n过去, 如果发送失败，就重新初始化一个socket
				if (!isSuccess) {
					removeCallbacks();
					releaseLastSocket();
					new InitSocketThread().start();
				}
			}
			if (!isRemove){
				removeCallbacks();
			}
			mHandler.postDelayed(this, HEART_BEAT_RATE);
			isRemove = false;
		}
	};
	private Handler mHandler2 = new Handler();
	private Runnable heartBeatRunnable2 = new Runnable() {
		@Override
		public void run() {
			LLog.i("WebSocket  启动重连");
			isConn = true;
			if (NetUtil.isNetworkAvailable(getApplicationContext())){
				LLog.i("WebSocket  重连中...");
				new InitSocketThread().start();
			} else {
				if (count <= 10) {
					count++;
					mHandler2.postDelayed(this, DELAY_MILLIS);
				} else if (count <= 20) {
					count++;
					mHandler2.postDelayed(this, DELAY_MILLIS+5000);
				} else {
					if (isConn) {
						isConn = false;
						mHandler2.removeCallbacks(this);
					}
					onDestroy();
				}
			}
		}
	};
	public boolean sendMsg(String msg) {
		if (null == msg || null == mWebSocketClient || null == mWebSocketClient.getConnection()) {
			return false;
		}
		try {
//			LLog.i("WebSocket  mWebSocketClient.isConnecting()：" + mWebSocketClient.isConnecting());
//			if (mWebSocketClient.isConnecting()) {
				mWebSocketClient.send(msg);
				LLog.i("WebSocket  发送消息：" + msg);
				sendTime = System.currentTimeMillis();// 每次发送成功数据，就改一下最后成功发送的时间，节省心跳间隔时间
//			} else {
//				return false;
//			}
		}catch (Exception e){}
		return true;
	}

	//初始化WebSocketClient
	private void initSocket() throws URISyntaxException {
		user = SharedPreferencesUtils.getInstance(getApplicationContext()).loadObjectData(User.class);
		if (user == null){
			return;
		}
		if (mWebSocketClient != null){
			releaseLastSocket();
		}
		if(mWebSocketClient == null) {
			mWebSocketClient = new WebSocketClient(new URI(address)) {
				@Override
				public void onOpen(ServerHandshake serverHandshake) {
					//连接成功
					LLog.i("WebSocket  连接成功");
					JSONObject obj = new JSONObject();
					try {
						obj.put("auth_user", user.getUserName());
						obj.put("auth_pwd", user.getPassword());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					sendMsg(obj.toString());
				}

				@Override
				public void onMessage(String s) {
					//服务端消息
					LLog.i("WebSocket  服务端消息："+s);
					if (s.equals("PONG")) {// 处理心跳回复
						Intent intent = new Intent(HEART_BEAT_ACTION);
						sendBroadcast(intent);
					} else if (s.contains(":\"10000\",")){
						if (isConn) {
							isConn = false;
							mHandler2.removeCallbacks(heartBeatRunnable2);
						}
						if (!isRemove){
							removeCallbacks();
						}
						mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);// 初始化成功后，就准备发送心跳包
						isRemove = false;
					} else {
						// 其他消息回复
						Intent intent = new Intent(MESSAGE_ACTION);
						intent.putExtra("message", s);
						sendBroadcast(intent);
					}
				}

				@Override
				public void onClose(int i, String s, boolean remote) {
					//连接断开，remote判定是客户端断开还是服务端断开
//					if (remote) {
//						LLog.i("WebSocket  服务器断开："+s);
//						closeConnect();
//					} else {
						LLog.i("WebSocket  连接断开："+s);
						closeConnect();
						if (!isDestroy) {
							if (isConn) {
								isConn = false;
								mHandler2.removeCallbacks(heartBeatRunnable2);
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							user = SharedPreferencesUtils.getInstance(getApplicationContext()).loadObjectData(User.class);
							if (user != null && user.getUserId() != null && !user.getUserId().equals("") && !user.getUserId().equals("0")) {
								count = 0;
								mHandler2.postDelayed(heartBeatRunnable2, DELAY_MILLIS);
							}
						}
//					}
				}

				@Override
				public void onError(Exception e) {
					count++;
					releaseLastSocket();
					if (count <= 5) {
						new InitSocketThread().start();
					}
				}
			};
			connectWebSocket();
		}
	}

	//连接
	private void connectWebSocket() {
		new Thread(){
			@Override
			public void run() {
				LLog.i("WebSocket  开始连接...");
				try {
					if (mWebSocketClient != null) {
						mWebSocketClient.connect();
					}
				} catch (Exception e){}
			}
		}.start();
	}

	//断开连接
	private void closeConnect() {
		try {
			if (mWebSocketClient != null) {
				mWebSocketClient.close();
				LLog.i("WebSocket  断开连接...");
				removeCallbacks();
				releaseLastSocket();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void removeCallbacks() {
		if (isRemove){
			return;
		}
		isRemove = true;
		mHandler.removeCallbacks(heartBeatRunnable);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isDestroy = true;
		closeConnect();
	}

	// 释放socket
	private void releaseLastSocket() {
		if (null != mWebSocketClient) {
			WebSocket sk = mWebSocketClient.getConnection();
			if (!sk.isClosed()) {
				sk.close();
			}
			mWebSocketClient = null;
		}
	}

	class InitSocketThread extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				initSocket();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
}