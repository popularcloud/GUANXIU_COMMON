package com.lwc.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.lwc.common.R;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.view.MatrixImageView;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 图片放大框
 */

public class PhotoBigFrameDialog extends Dialog {

    private MatrixImageView imgUrl;
    private ImageLoaderUtil imageLoader;
    private LinearLayout lLayout;

    public PhotoBigFrameDialog(Context context, String path) {
        super(context);
//        //去掉标题栏
        Window win = getWindow();
        win.requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_photo_big_frame);
        initViews();
        imageLoader.displayFromNetD(context,path, imgUrl);
        imgUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissNoticeDialog();
            }
        });
        lLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissNoticeDialog();
            }
        });
    }

    /**
     * 初始化页面组件
     */
    private void initViews() {
        imgUrl = (MatrixImageView) findViewById(R.id.imgUrl);
        lLayout = (LinearLayout) findViewById(R.id.lLayout);
        imageLoader = ImageLoaderUtil.getInstance();
    }

    /**
     * 显示
     */
    public void showNoticeDialog() {
        if (!isShowing())
            this.show();
    }

    /**
     * 隐藏
     */
    public void dismissNoticeDialog() {
        if (isShowing())
            this.dismiss();
    }
}
