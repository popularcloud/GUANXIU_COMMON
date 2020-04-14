package com.lwc.common.module;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.lwc.common.R;


/**
 * activity的base类,用于基本数据的初始化
 *
 * @author 何栋
 */
public abstract class BaseFragmentActivity extends AppCompatActivity {

    private boolean isCloseBackKey = false;
    private boolean isOpenDoubleClickToExit = false;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    /**
     * 关闭返回键
     */
    public void closeBackKey() {
        this.isCloseBackKey = true;
    }

    /**
     * 打开返回键
     */
    public void openBackkey() {
        this.isCloseBackKey = false;
    }

    /**
     * 关闭二次点击退出功能
     */
    public void closeDoubleClickToExit() {
        this.isOpenDoubleClickToExit = false;
    }

    /**
     * 打开二次点击退出功能
     */
    public void openDoubleClickToExit() {
        this.isOpenDoubleClickToExit = true;
    }

    /**
     * 设置回退按钮事件
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isCloseBackKey) {
            if (keyCode == KeyEvent.KEYCODE_BACK)
                return true;
        }

        if (isOpenDoubleClickToExit) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {

                    Toast.makeText(BaseFragmentActivity.this,
                            R.string.double_click_to_exit_app,
                            Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                    return true;
                } else {
                    //清除用户信息  退出时不清除用户信息，保持登录状态
                    // delectUserInfo();
                    //退出应用
                    System.exit(0);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void onBack(View v) {
        finish();
    }


    /**
     * 初始化view
     */
    public abstract void initView();

    /**
     * 初始化引擎
     */
    public abstract void initEngines();

    /**
     * 获取Intent传过来的数据
     */
    public abstract void getIntentData();

}
