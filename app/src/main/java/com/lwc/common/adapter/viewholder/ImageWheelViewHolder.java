package com.lwc.common.adapter.viewholder;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lwc.common.R;
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.module.bean.ADInfo;
import com.lwc.common.module.integral.activity.IntegralLuckDrawActivity;
import com.lwc.common.module.zxing.activity.CaptureActivity;
import com.lwc.common.utils.DisplayUtil;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.ImageCycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageWheelViewHolder extends BaseViewHolder{

    @BindView(R.id.ad_view)
    ImageCycleView mAdView;
    @BindView(R.id.iv_scan)
    ImageView iv_scan;
    public ImageWheelViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void bindViewHolder(Object data, Context context) {
        ArrayList<ADInfo> adInfos = (ArrayList<ADInfo>) data;
        mAdView.setImageResources(adInfos, mAdCycleViewListener);
        mAdView.startImageCycle();
       RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mAdView.mGroup.getLayoutParams();
       layoutParams.bottomMargin = DisplayUtil.dip2px(context,23);
       mAdView.mGroup.setLayoutParams(layoutParams);

       iv_scan.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                       if (Utils.gotoLogin(MainActivity.user, MainActivity.activity))
                           IntentUtil.gotoActivityForResult(MainActivity.activity, CaptureActivity.class, 8869);
                   }
       });
    }


    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if("4".equals(info.getUrlType())){ //链接类型为4 时为内部链接  app内部跳转
                if (Utils.gotoLogin(MainActivity.user,MainActivity.activity)) {
                    IntentUtil.gotoActivity(MainActivity.activity, IntegralLuckDrawActivity.class);
                }
            }else{  //其他为外部链接 直接打开网页
                Bundle bundle = new Bundle();
                if (TextUtils.isEmpty(info.getAdvertisingUrl())){
                    return;
                }
                bundle.putString("url", info.getAdvertisingUrl());
                if (!TextUtils.isEmpty(info.getAdvertisingTitle()))
                    bundle.putString("title", info.getAdvertisingTitle());
                IntentUtil.gotoActivity(MainActivity.activity, InformationDetailsActivity.class, bundle);
            }

        }

        @Override
        public void displayImage(final String imageURL, final ImageView imageView) {
            ImageLoaderUtil.getInstance().displayFromNetD(MainActivity.activity, imageURL, imageView);// 使用ImageLoader对图片进行加装！
        }
    };
}
