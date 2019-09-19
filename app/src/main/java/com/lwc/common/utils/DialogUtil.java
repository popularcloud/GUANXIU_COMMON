package com.lwc.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.configs.TApplication;
import com.lwc.common.module.bean.Coupon;
import com.lwc.common.widget.CouponDialog;
import com.lwc.common.widget.CustomDialog;

import java.util.List;

/**
 * 对话框封装工具类
 *
 * @date 2013-03-20
 */
public class DialogUtil {

    private static CustomDialog dialog;

    /**
     * 显示提示信息对话框
     *
     * @param context
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public static void showMessageDg(Context context, String title, String msg, CustomDialog.OnClickListener listener) {
        dialog = new CustomDialog(context);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        } else {
            dialog.setTitle(context.getString(R.string.prompt));
        }
        dialog.setMessage(msg);

        dialog.setEnterBtn(listener);
        dialog.setCancelBtn(new CustomDialog.OnClickListener() {

            @Override
            public void onClick(CustomDialog dialog, int id, Object object) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static Dialog dialog(Context context, String title, String butText, String cancel, String msg, View.OnClickListener listener, View.OnClickListener listener2, boolean isCenter) {
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialog.setContentView(R.layout.dialog_delete_remind);
        Window win = dialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setGravity(Gravity.CENTER);
        win.setAttributes(lp);
        TextView tv_titel = (TextView) dialog.findViewById(R.id.tv_titel);
        TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
        TextView tv_qd = (TextView) dialog.findViewById(R.id.tv_qd);
        TextView tv_qx = (TextView) dialog.findViewById(R.id.tv_qx);
        if (isCenter) {
            tv_msg.setGravity(Gravity.CENTER);
        }
        tv_msg.setText(msg);
        if (!TextUtils.isEmpty(cancel))
            tv_qx.setText(cancel);
        if (!TextUtils.isEmpty(butText))
            tv_qd.setText(butText);
        if (!TextUtils.isEmpty(title)) {
            tv_titel.setVisibility(View.VISIBLE);
            tv_titel.setText(title);
        }
        tv_qd.setOnClickListener(listener);
        if (listener2 != null) {
            tv_qx.setOnClickListener(listener2);
        } else {
            tv_qx.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
        return dialog;
    }

    public static CouponDialog dialogCoupon(Context context, String title, List<Coupon> coupons, View.OnClickListener listener, View.OnClickListener listener2, View.OnClickListener listener3) {
        CouponDialog dialog = new CouponDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(coupons);
        dialog.setEnterBtn(listener);
        dialog.setCancelBtn(listener2);
        dialog.setGbBtn(listener3);
        dialog.show();
        return dialog;
    }
    public static Dialog dialogBind(Context context, String companyName, String msgStr, String btn, View.OnClickListener listener) {
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialog.setContentView(R.layout.dialog_remind);
        Window win = dialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setGravity(Gravity.CENTER);
        win.setAttributes(lp);
        TextView tv_titel = (TextView) dialog.findViewById(R.id.tv_titel);
		TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
        Button tv_qd = (Button) dialog.findViewById(R.id.btn);
        if (!TextUtils.isEmpty(companyName)) {
            String msg = "恭喜您已成功成为" + companyName + "专属客户！";
            tv_titel.setText(Utils.getSpannableStringBuilder(8, 8 + companyName.length(), context.getResources().getColor(R.color.status), msg, 19));
        } else {
            tv_titel.setText(msgStr);
            tv_msg.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(btn)) {
            tv_qd.setText(btn);
        }
        if (listener != null) {
            tv_qd.setOnClickListener(listener);
        } else {
            tv_qd.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
        return dialog;
    }

    public static CustomDialog showUpdateAppDg(Context context, String title, String butText, String msg, CustomDialog.OnClickListener listener) {
        dialog = new CustomDialog(context);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        } else {
            dialog.setTitle(context.getString(R.string.prompt));
        }
        dialog.setCancelable(false);
        dialog.setGoneBut2();
        dialog.setMsgGra();
        dialog.setMessage(msg);
        dialog.setButton1Text(butText);
        dialog.setEnterBtn(listener);
//		dialog.setCancelBtn(new CustomDialog.OnClickListener() {
//
//			@Override
//			public void onClick(CustomDialog dialog, int id, Object object) {
//				dialog.dismiss();
//			}
//		});
        dialog.show();
        return dialog;
    }

    public static void showMessageDg(Context context, String title, String butText, String msg, CustomDialog.OnClickListener listener) {
        dialog = new CustomDialog(context);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        } else {
            dialog.setTitle(context.getString(R.string.prompt));
        }
        dialog.setButton1Text(butText);
        dialog.setMessage(msg);

        dialog.setEnterBtn(listener);
        dialog.setCancelBtn(new CustomDialog.OnClickListener() {

            @Override
            public void onClick(CustomDialog dialog, int id, Object object) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showMessageDg(Context context, String title, String butText, String cancel, String msg, CustomDialog.OnClickListener listener, CustomDialog.OnClickListener listener2) {
        dialog = new CustomDialog(context);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        } else {
            dialog.setTitle(context.getString(R.string.prompt));
        }
        dialog.setButton1Text(butText);
//		dialog.setMsgGra();
        dialog.setMessage(msg);

        dialog.setEnterBtn(listener);
        dialog.setButton2Text(cancel);
        if (listener2 != null) {
            dialog.setCancelBtn(listener2);
        } else {
            dialog.setCancelBtn(new CustomDialog.OnClickListener() {

                @Override
                public void onClick(CustomDialog dialog, int id, Object object) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    public static void showMessageUp(Context context, String title, String butText, String cancel, String msg, CustomDialog.OnClickListener listener, CustomDialog.OnClickListener listener2) {
        dialog = new CustomDialog(context);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        } else {
            dialog.setTitle(context.getString(R.string.prompt));
        }
        dialog.setButton1Text(butText);
        dialog.setMsgGra();
        dialog.setMessage(msg);

        dialog.setEnterBtn(listener);
        dialog.setButton2Text(cancel);
        if (listener2 != null) {
            dialog.setCancelBtn(listener2);
        } else {
            dialog.setCancelBtn(new CustomDialog.OnClickListener() {

                @Override
                public void onClick(CustomDialog dialog, int id, Object object) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    /**
     * 描述：输入法的控制
     * <p>
     * createTime 2014-3-17 下午4:57:33 createAuthor kpxingxing
     * <p>
     * updateTime 2014-3-17 下午4:57:33 updateAuthor kpxingxing updateInfo
     *
     * @param view
     * @param show
     */
    public static void showInput(View view, boolean show) {
        try {
            Context context = TApplication.context;
            if (show) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, 0);
            } else {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
