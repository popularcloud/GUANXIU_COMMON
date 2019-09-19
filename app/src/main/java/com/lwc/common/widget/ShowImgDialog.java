package com.lwc.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lwc.common.R;
import com.lwc.common.interf.OnCommClickCallBack;

/**
 * @author younge
 * @date 2019/7/23 0023
 * @email 2276559259@qq.com
 */
public class ShowImgDialog  extends Dialog{


    private Context context;
    private RelativeLayout rl_share_bg;
    private ImageView iv_close;
    private OnCommClickCallBack onCommClick;

    public ShowImgDialog(Context context) {
        super(context);
        this.context = context;
    }

    public ShowImgDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ShowImgDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ShowImgDialog init(OnCommClickCallBack onCommClick){
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        this.setContentView(R.layout.dialog_show_img);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rl_share_bg = (RelativeLayout) findViewById(R.id.rl_share_bg);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        this.onCommClick = onCommClick;
        widgetListener();
        return this;
    }


    private void widgetListener() {
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCommClick != null){
                    dismiss();
                    onCommClick.onCommClick("close");
                }
            }
        });

        rl_share_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCommClick != null){
                    dismiss();
                    onCommClick.onCommClick(null);
                }
            }
        });
    }


}
