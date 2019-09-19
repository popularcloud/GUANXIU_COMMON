package com.lwc.common.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.interf.OnCommClickCallBack;
import java.io.IOException;
import java.net.URL;

/**
 * @author younge
 * @date 2019/7/23 0023
 * @email 2276559259@qq.com
 */
public class ShowImgWithBtnDialog extends Dialog{


    private Activity context;
    private RelativeLayout rl_share_bg;
    private String bgUrl;
    private ImageView iv_close;
    private TextView tv_left;
    private TextView tv_right;
    private OnCommClickCallBack onCommClick;
    Drawable drawable = null;

    public ShowImgWithBtnDialog(Activity context,String bgUrl) {
        super(context);
        this.context = context;
        this.bgUrl = bgUrl;

    }

    public ShowImgWithBtnDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ShowImgWithBtnDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ShowImgWithBtnDialog init(OnCommClickCallBack onCommClick){
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        this.setContentView(R.layout.dialog_show_img_with_btn);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rl_share_bg = (RelativeLayout) findViewById(R.id.rl_share_bg);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_right = (TextView) findViewById(R.id.tv_right);
        this.onCommClick = onCommClick;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    drawable = Drawable.createFromStream(
                            new URL(bgUrl).openStream(), "image.jpg");
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rl_share_bg.setBackground(drawable);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        widgetListener();
        return this;
    }


    private void widgetListener() {
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCommClick != null){
                    dismiss();
                    //onCommClick.onCommClick("close");
                }
            }
        });

        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCommClick != null){
                    dismiss();
                    onCommClick.onCommClick("left");
                }
            }
        });

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCommClick != null){
                    dismiss();
                    onCommClick.onCommClick("right");
                }
            }
        });
    }


}
