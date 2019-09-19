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
    private View statusBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(this, true);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintDrawable(getResources().getDrawable(R.drawable.title_bg_new));
//        }
    /*    try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                    @Override
                    public boolean queueIdle() {
                        if (isStatusBar()) {
                            initStatusBar();
                            getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                                @Override
                                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                    initStatusBar();
                                }
                            });
                        }
                        //只走一次
                        return false;
                    }
                });
            }
        } catch (Exception e) {
        }*/

     /*   process(savedInstanceState);
        setStatusBar();*/
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }


    /*protected void process(Bundle savedInstanceState) {
        // 华为,OPPO机型在StatusBarUtil.setLightStatusBar后布局被顶到状态栏上去了
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View content = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            if (content != null && !isUseFullScreenMode()) {
                content.setFitsSystemWindows(true);
            }
        }
    }

    // 在setContentView之前执行
    public void setStatusBar() {
    *//*
     为统一标题栏与状态栏的颜色，我们需要更改状态栏的颜色，而状态栏文字颜色是在android 6.0之后才可以进行更改
     所以统一在6.0之后进行文字状态栏的更改
    *//*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isUseFullScreenMode()) {
                ActionBarUtil.transparencyBar(this);
            } else {
                ActionBarUtil.setStatusBarColor(this, setStatusBarColor());
            }

            if (isUserLightMode()) {
                ActionBarUtil.setLightStatusBar(this, true);
            }
        }

    }

    // 是否设置成透明状态栏，即就是全屏模式
    protected boolean isUseFullScreenMode() {
        return false;
    }

    protected int setStatusBarColor() {
        return R.color.white;
    }*/

    // 是否改变状态栏文字颜色为黑色，默认为黑色
    protected boolean isUserLightMode() {
        return true;
    }

   /* private void initStatusBar() {
        if (statusBarView == null) {
            int identifier = getResources().getIdentifier("statusBarBackground", "id", "android");
            statusBarView = getWindow().findViewById(identifier);
        }
        if (statusBarView != null) {
            statusBarView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    protected boolean isStatusBar() {
        return true;
    }

    @TargetApi(19)
    protected static void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }*/

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
                    //清除用户信息
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
