package com.lwc.common.module.integral.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.ImageBrowseActivity;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.integral.bean.IntegralGoodsDetailBean;
import com.lwc.common.module.integral.presenter.IntegralGoodsPresenter;
import com.lwc.common.module.integral.view.IntegralGoodsDetailView;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.ImageCycleView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;

/**
 * @author younge
 * @date 2019/4/1 0001
 * @email 2276559259@qq.com
 * 积分商品详情
 */
public class IntegralGoodsDetailActivity extends BaseActivity implements IntegralGoodsDetailView {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_integral)
    TextView tv_integral;
    @BindView(R.id.wv_detail)
    WebView wv_detail;
    @BindView(R.id.btn_submit)
    TextView btn_submit;
    @BindView(R.id.ad_view)
    ImageCycleView mAdView;//轮播图
    private IntegralGoodsPresenter integralGoodsPresenter;
    private IntegralGoodsDetailBean integralGoodsDetailBean;
    private ArrayList<ADInfo> infos = new ArrayList<>();
    private String[] imgs;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_integral_good_detial;
    }

    @Override
    protected void findViews() {
        integralGoodsPresenter = new IntegralGoodsPresenter(this);
        setTitle("详情");
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initGetData() {
        String goodsId = getIntent().getStringExtra("goodsId");
        integralGoodsPresenter.getIntegralGoodsDetail(goodsId);
    }

    @Override
    protected void widgetListener() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("integralGoodsDetailBean", integralGoodsDetailBean);
                //IntentUtil.gotoActivityForResult(IntegralGoodsDetailActivity.this, InputPayPwdAndAddressActivity.class, bundle, 12302);
                IntentUtil.gotoActivity(IntegralGoodsDetailActivity.this, IntegralOrderConfirmActivity.class, bundle);
            }
        });
    }

    @Override
    public void onLoadGoodsDetailSuccess(IntegralGoodsDetailBean bean) {

        if (bean != null) {
            tv_title.setText(bean.getIntegralName());

            String priceString =Utils.chu(bean.getIntegralNum(), "100");
            tv_integral.setText(priceString);
            integralGoodsDetailBean = bean;

            WebSettings webSettings = wv_detail.getSettings();//获取webview设置属性
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
            webSettings.setJavaScriptEnabled(true);//支持js
          //  webSettings.setBuiltInZoomControls(true); // 显示放大缩小
            webSettings.setSupportZoom(true); // 可以缩放

            String context = getNewContent(bean.getIntegralDetails());
            wv_detail.loadDataWithBaseURL(null, context, "text/html", "UTF-8", null);
            //显示轮播
            if (!TextUtils.isEmpty(bean.getIntegralBanner())) {
                imgs = bean.getIntegralBanner().split(",");
                infos.clear();
                for (int i = 0; i < imgs.length; i++) {
                    ADInfo adInfo = new ADInfo();
                    adInfo.setAdvertisingImageUrl(imgs[i]);
                    infos.add(adInfo);
                }

                if (infos != null && infos.size() > 0) {
                    mAdView.setImageResources(infos, mAdCycleViewListener);
                    mAdView.startImageCycle();
                }
            }
            setIntegralText(0);
        } else {
            ToastUtil.showToast(IntegralGoodsDetailActivity.this, "获取数据失败！");
        }
    }

    /**
     * 将html文本内容中包含img标签的图片，宽度变为屏幕宽度，高度根据宽度比例自适应
     **/
    public String getNewContent(String htmltext){
        try {
            Document doc= Jsoup.parse(htmltext);
            Elements elements=doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width","100%").attr("height","auto");
            }

            return doc.toString();
        } catch (Exception e) {
            return htmltext;
        }
    }

    private void setIntegralText(double needReduceIntegral) {
        String integral = (String) SharedPreferencesUtils.getParam(IntegralGoodsDetailActivity.this, "MyIntegral", "0");

        double myIntegral = Double.parseDouble(integral);
        double resultIntegral = myIntegral - needReduceIntegral;
        SharedPreferencesUtils.setParam(IntegralGoodsDetailActivity.this, "MyIntegral", String.valueOf(resultIntegral));

        double goodsIntegral = Double.parseDouble(integralGoodsDetailBean.getIntegralNum());
    /*    StringBuilder content = new StringBuilder();
        SpannableStringBuilder stringBuilder;*/
        if(resultIntegral >= goodsIntegral){
           // content.append("立即兑换(可用积分为 " + Utils.chu(String.valueOf(resultIntegral), "100") + ")");
            btn_submit.setClickable(true);
            btn_submit.setBackgroundColor(Color.parseColor("#ff3a3a"));
            btn_submit.setEnabled(true);
            btn_submit.setText("立即兑换");
        }else{
          //  content.append("积分不足(可用积分为 " + Utils.chu(String.valueOf(resultIntegral), "100") + ")");
            btn_submit.setClickable(false);
            btn_submit.setBackgroundColor(Color.parseColor("#afafaf"));
            btn_submit.setEnabled(false);
            btn_submit.setText("积分不足");
        }

       /* stringBuilder = new SpannableStringBuilder(content);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);
        //字体颜色
        stringBuilder.setSpan(foregroundColorSpan, 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        AbsoluteSizeSpan ab = new AbsoluteSizeSpan(10, true);
        //文本字体绝对的大小
        stringBuilder.setSpan(ab, 4, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        btn_submit.setText(stringBuilder);*/
    }

    @Override
    public void onLoadError(String msg) {
        ToastUtil.showToast(this, msg);
    }

    /**
     * 定义轮播图监听
     */
    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            // 点击图片后,有内链和外链的区别
         /*   Bundle bundle = new Bundle();
            if (!TextUtils.isEmpty(info.getAdvertisingUrl()))
                bundle.putString("url", info.getAdvertisingUrl());
            if (!TextUtils.isEmpty(info.getAdvertisingTitle()))
                bundle.putString("title", info.getAdvertisingTitle());
            IntentUtil.gotoActivity(IntegralGoodsDetailActivity.this, InformationDetailsActivity.class, bundle);*/
            if(infos != null){
                ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(imgs));
                Intent intent = new Intent(IntegralGoodsDetailActivity.this, ImageBrowseActivity.class);
                intent.putExtra("index", position);
                intent.putStringArrayListExtra("list", arrayList);
                startActivity(intent);
            }
        }

        @Override
        public void displayImage(final String imageURL, final ImageView imageView) {
            ImageLoaderUtil.getInstance().displayFromNetD(IntegralGoodsDetailActivity.this, imageURL, imageView);// 使用ImageLoader对图片进行加装！
        }
    };
}
