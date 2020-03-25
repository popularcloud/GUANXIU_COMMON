package com.lwc.common.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.executors.OnPageInLastTask;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.bean.Common;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.widget.PageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 资讯详情
 *
 * @author 何栋
 * @date 2017年11月29日
 */
public class UserGuideActivity extends BaseActivity {

	@BindView(R.id.webView1)
	WebView webview;
/*	@BindView(R.id.iv_long)
	ImageView iv_long;*/
	@BindView(R.id.rl_title)
	RelativeLayout rl_title;
	@BindView(R.id.fl_content)
	FrameLayout fl_content;
	@BindView(R.id.tv_rightNow)
	TextView tv_rightNow;
	@BindView(R.id.vp_loop_advertisement)
	ViewPager vp_loop_advertisement;
	@BindView(R.id.dot_horizontal)
	LinearLayout dot_horizontal;
	private String loadType;
	private List<ImageView> viewList;
	private PagerAdapter pagerAdapter;


	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_user_guide;
	}

	@Override
	protected void findViews() {
		ButterKnife.bind(this);
		loadType = getIntent().getStringExtra("type");
		//loadType = "9";
		if("8".equals(loadType)){
			setTitle("用户指南");
			webViewSetting();
			webview.setVisibility(View.VISIBLE);
			rl_title.setVisibility(View.VISIBLE);
			fl_content.setVisibility(View.GONE);
		}else{
			tv_rightNow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			webview.setVisibility(View.GONE);
			rl_title.setVisibility(View.GONE);
			fl_content.setVisibility(View.VISIBLE);
			viewList = new ArrayList<>();
			getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
					WindowManager.LayoutParams. FLAG_FULLSCREEN);
			viewPagerInit();
		}
	}

	private void viewPagerInit() {

		 pagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return viewList.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
									Object object) {
				// TODO Auto-generated method stub
				container.removeView(viewList.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				// TODO Auto-generated method stub
				container.addView(viewList.get(position));
				return viewList.get(position);
			}
		};

	}

	@Override
	protected void init() {
		HttpRequestUtils.httpRequest(this, "advertising", RequestValue.GET_ADVERTISING+"?local="+loadType+"&role=4", null, "GET", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String response) {
				Common common = JsonUtil.parserGsonToObject(response, Common.class);
				switch (common.getStatus()) {
					case "1":
						ArrayList<ADInfo> infos = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<ADInfo>>() {});

						if(infos == null || infos.size() < 1){
							finish();
							return;
						}
						ADInfo info = infos.get(0);
						String imgUrls = info.getAdvertisingImageUrl();
						Log.d("联网成功imgUrls=",imgUrls);
						if(!TextUtils.isEmpty(imgUrls)){
							String[] urls = imgUrls.split(",");
							if("8".equals(loadType)){
								String myUrl = urls[0];
								Log.d("联网成功myUrl=",myUrl);
								if(!TextUtils.isEmpty(myUrl)){
									String newUrl = myUrl.replace("https","http");
									Log.d("联网成功newUrl=",newUrl);
									loadWeb(newUrl);
								}else{

								}

							}else{ //首次加载的引导图
								if(urls != null && urls.length > 0){
									viewList.clear();
									for(int i = 0;i<urls.length;i++){
										ImageView imageView = new ImageView(UserGuideActivity.this);
										imageView.setScaleType(ImageView.ScaleType.FIT_XY);
										ImageLoaderUtil.getInstance().displayFromNetD(UserGuideActivity.this,urls[i],imageView);
										viewList.add(imageView);
									}
									vp_loop_advertisement.setAdapter(pagerAdapter);
									/**
									 * 第二个参数：存放小圆点的容器
									 * 第三个参数：ViewPager的页面数量
									 */
									vp_loop_advertisement.addOnPageChangeListener(new PageIndicator(UserGuideActivity.this, dot_horizontal, viewList.size(), new OnPageInLastTask() {
										@Override
										public void onPageInLast() {
											if("9".equals(loadType)){
												tv_rightNow.setVisibility(View.VISIBLE);
											}
										}
									}));
									//pagerAdapter.notifyDataSetChanged();
								}
							}
						}else{
							finish();
						}
						break;
					default:
						finish();
						break;
				}
			}
			@Override
			public void returnException(Exception e, String msg) {
				finish();
			}
		});
	}

	public void loadWeb(String url) {

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				if (MainActivity.activity == null) {
					IntentUtil.gotoActivityAndFinish(UserGuideActivity.this, MainActivity.class);
				}
//				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
		});
		webview.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onReceivedTitle(WebView view, String title) {
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				System.out.println("---------------"+newProgress);
				super.onProgressChanged(view, newProgress);
			}

			@Override
			public View getVideoLoadingProgressView() {
				FrameLayout frameLayout = new FrameLayout(UserGuideActivity.this);
				frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
				return frameLayout;
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
									 JsResult result) {
				// TODO Auto-generated method stub
				return super.onJsAlert(view, url, message, result);
			}
		});
		webview.loadUrl(url);

	}

	public void refresh() {
		webview.reload();
	}

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface", "NewApi" })
	@JavascriptInterface
	private void webViewSetting() {
		WebSettings webSettings = webview.getSettings();
		webSettings.setDefaultTextEncodingName("utf-8");
		webSettings.setJavaScriptEnabled(true);
		webSettings.setPluginState(WebSettings.PluginState.ON);
		webSettings.setUseWideViewPort(true); // 关键点
		webSettings.setAllowFileAccess(true); // 允许访问文件
		webSettings.setSupportZoom(true); // 支持缩放
		webSettings.setLoadWithOverviewMode(true);
		//webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

		webSettings.setMediaPlaybackRequiresUserGesture(false);
		webSettings.setPluginState(WebSettings.PluginState.ON);

//		webview.setWebChromeClient(new WebChromeClient());

		webview.requestFocus();
		webview.setHorizontalScrollBarEnabled(false);//水平不显示
		webview.setVerticalScrollBarEnabled(false); //垂直不显示
//        webview.setScrollBarStyle(0);
	}

	@Override
	protected void initGetData() {
	}

	@Override
	protected void widgetListener() {
	}

	@Override
	public void onDestroy() {
		/*if (webview != null) {
			ViewGroup parent = (ViewGroup) webview.getParent();
			if (parent != null) {
				parent.removeView(webview);
			}
			webview.removeAllViews();
			webview.destroy();
		}*/
		super.onDestroy();
	}

	/** 全屏容器界面 */
	static class FullscreenHolder extends FrameLayout {

		public FullscreenHolder(Context ctx) {
			super(ctx);
			setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
		}

		@Override
		public boolean onTouchEvent(MotionEvent evt) {
			return true;
		}
	}

	private void setStatusBarVisibility(boolean visible) {
		int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
		getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (MainActivity.activity == null) {
					return false;
				}
			default:
				return super.onKeyUp(keyCode, event);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 101 && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			onBackPressed();
		}
	}

/*	@Override
	public void finish() {
		if (MainActivity.activity == null) {
			IntentUtil.gotoActivity(this, MainActivity.class);
		}
		super.finish();
	}*/
}
