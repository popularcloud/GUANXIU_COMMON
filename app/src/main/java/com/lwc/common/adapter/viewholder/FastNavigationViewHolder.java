package com.lwc.common.adapter.viewholder;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.lwc.common.R;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.module.bean.BroadcastBean;
import com.lwc.common.module.integral.activity.IntegralMainActivity;
import com.lwc.common.module.lease_parts.activity.LeaseHomeActivity;
import com.lwc.common.module.order.ui.activity.NewPackageListActivity;
import com.lwc.common.module.order.ui.activity.PackageListActivity;
import com.lwc.common.module.repairs.ui.activity.ApplyForMaintainActivity;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.AutoHorizontalScrollTextView;
import com.lwc.common.view.AutoVerticalScrollTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FastNavigationViewHolder extends BaseViewHolder{

    //对话框
    private Dialog dialog;

    //轮播通知
    @BindView(R.id.ll_gb)
    LinearLayout ll_gb;
    @BindView(R.id.tv_gb)
    AutoVerticalScrollTextView tv_gb;
    @BindView(R.id.tv_gd)
    AutoHorizontalScrollTextView tv_gd;

    //滚动广告类别
    private ArrayList<BroadcastBean> noticesList;

    //滚动通知是否滚动
    private boolean IS_NOTICE_START = false;
    private int number = 0;

    public FastNavigationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void bindViewHolder(Object data, Context context) {
        noticesList = (ArrayList<BroadcastBean>) data;

        if (noticesList != null && noticesList.size() > 0) {
            ll_gb.setVisibility(View.VISIBLE);
            for (int i = 0; i < noticesList.size(); i++) {
                BroadcastBean bb = noticesList.get(i);
                if (bb.getIsExtend() == 1 && !TextUtils.isEmpty(bb.getAnnunciateContentExtend()) && bb.getIsValid() == 1) {
                    SharedPreferencesUtils.getInstance(context).saveString("guangboStr", bb.getAnnunciateContentExtend());
                    break;
                }
            }
            for (int i = 0; i < noticesList.size(); i++) {
                BroadcastBean bb = noticesList.get(i);
                if (TextUtils.isEmpty(bb.getAnnunciateContent())) {
                    noticesList.remove(i);
                }
            }
            if (noticesList.size() > 1) {
                tv_gd.setVisibility(View.GONE);
                tv_gb.setVisibility(View.VISIBLE);
                tv_gb.setText(noticesList.get(0).getAnnunciateContent());
                startGd();
            } else if (noticesList.size() > 0) {
                tv_gd.setVisibility(View.VISIBLE);
                tv_gd.setSelected(true);
                tv_gb.setVisibility(View.GONE);
                String content = noticesList.get(0).getAnnunciateContent();
                if (content.length() < 30 && content.length() >= 20) {
                    content = content+ "                                  ";
                } else if (content.length() < 20 && content.length() >= 15) {
                    content = content + "                                            ";
                } else if (content.length() < 15 && content.length() >= 10) {
                    content = content + "                                                      ";
                }

                tv_gd.setText(content);
            }
        } else {
            ll_gb.setVisibility(View.GONE);
        }
    }

    /**
     * 滚动通知栏开始滚动
     */
    private void startGd() {
        IS_NOTICE_START = true;
        new Thread() {
            @Override
            public void run() {
                while (IS_NOTICE_START) {
                    SystemClock.sleep(3000);
                    handler.sendEmptyMessage(199);
                }
            }
        }.start();
    }

    /**
     * 处理通知滚动的事件
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 199) {
                tv_gb.next();
                number++;
                tv_gb.setText(noticesList.get(number % noticesList.size()).getAnnunciateContent());
            }
        }
    };

    @OnClick({R.id.ll_repairs,R.id.ll_meal,R.id.ll_integral,R.id.ll_lease,R.id.ll_customer_service})
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.ll_repairs:
                if (Utils.gotoLogin(MainActivity.user,MainActivity.activity)) {
                 //   if ((LeaseHomeActivity.user.getRoleId() != null && LeaseHomeActivity.user.getRoleId().equals("5"))) {
                        IntentUtil.gotoActivity(MainActivity.activity, ApplyForMaintainActivity.class);
                 /*   } else {
                        ToastUtil.showLongToast(LeaseHomeActivity.activity,"您没有权限报修！");
                    }*/
                }
                break;
            case R.id.ll_meal:
                if (Utils.gotoLogin(MainActivity.user,MainActivity.activity))
                    IntentUtil.gotoActivity(MainActivity.activity, NewPackageListActivity.class);
                break;
            case R.id.ll_integral:
                if (Utils.gotoLogin(MainActivity.user,MainActivity.activity))
                    IntentUtil.gotoActivity(MainActivity.activity, IntegralMainActivity.class);
                break;
            case R.id.ll_lease:
                //ToastUtil.showToast(MainActivity.activity,"功能开发中...");
                if (Utils.gotoLogin(MainActivity.user,MainActivity.activity))
                    IntentUtil.gotoActivity(MainActivity.activity, LeaseHomeActivity.class);
                break;
            case R.id.ll_customer_service:
                dialog = DialogUtil.dialog(MainActivity.activity, "拨打客服电话", "拨号", "取消", "400-881-0769", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Utils.lxkf(MainActivity.activity, null);
                    }
                }, null, true);
                break;
        }
    }

}
