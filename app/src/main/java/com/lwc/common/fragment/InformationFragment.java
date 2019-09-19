package com.lwc.common.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.gyf.immersionbar.ImmersionBar;
import com.lwc.common.R;
import com.lwc.common.activity.AndroidJSInterface;
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.configs.ServerConfig;
import com.lwc.common.module.BaseFragment;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.MyTextView;
import com.yanzhenjie.sofia.Sofia;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 资讯
 *
 * @author 何栋
 * @date 2017年11月20日
 * @Copyright: lwc
 */
public class InformationFragment extends BaseFragment {


    @BindView(R.id.txtActionbarTitle)
    MyTextView txtActionbarTitle;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.progressBar1)
    ProgressBar progressbar;
    @BindView(R.id.webView1)
    WebView webview;
    @BindView(R.id.rl_title)
    ViewGroup titleContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, null);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        CollapsingToolbarLayout.LayoutParams lp1 = (CollapsingToolbarLayout.LayoutParams) titleContainer.getLayoutParams();
        lp1.topMargin = Utils.getStatusBarHeight(getActivity());
        titleContainer.setLayoutParams(lp1);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            ImmersionBar.with(getActivity()).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        }
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void init() {
        txtActionbarTitle.setText("资讯");
        imgBack.setVisibility(View.GONE);
        webViewSetting();
        loadWeb();
    }

    public void loadWeb() {
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                IntentUtil.gotoActivity(getActivity(), InformationDetailsActivity.class, bundle);
                return true;
            }
        });
        webview.loadUrl(ServerConfig.DOMAIN.replace("https", "http") + "/main/h5/userNews.html?clientType=person");
    }

    public void refresh() {
        webview.reload();
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "NewApi"})
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
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        webview.setWebChromeClient(new WebChromeClient());

        webview.requestFocus();
        webview.setHorizontalScrollBarEnabled(false);//水平不显示
        webview.setVerticalScrollBarEnabled(false); //垂直不显示
        //        webview.setScrollBarStyle(0);
        addJSInterface();
    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    private void addJSInterface() {
        AndroidJSInterface ajsi = new AndroidJSInterface(getActivity(), "");
        webview.addJavascriptInterface(ajsi, ajsi.getInterface());
    }

    @Override
    public void onDestroy() {
        if (webview != null) {
            ViewGroup parent = (ViewGroup) webview.getParent();
            if (parent != null) {
                parent.removeView(webview);
            }
            webview.removeAllViews();
            webview.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void initEngines(View view) {
    }


    @Override
    public void setListener() {
    }


    @Override
    public void getIntentData() {
    }
}