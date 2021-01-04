package com.lwc.common.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.lwc.common.R;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.login.ui.LoginActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;


public class Utils {

    private static long lastClickTime;
    private static String urlStr = "";

    /**
     * 防止连续点击触发重复提交事件
     *
     * @return
     */
    public synchronized static boolean isFastClick(int number, String url) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < number && urlStr.equals(url)) {
            return true;
        }
        urlStr = url;
        lastClickTime = time;
        return false;
    }

    public static boolean gotoLogin(User user, Context context) {

        Log.d("联网成功", "没有登录跳到登录" + context.getPackageName());

        if (user == null) {
            SharedPreferencesUtils.getInstance(context).saveString("token", "");
            IntentUtil.gotoActivity(context, LoginActivity.class);
            return false;
        }
        return true;
    }


    public static boolean isLogin() {

        return false;
    }

    //自定义报警通知（震动铃声都不要）
    public static void setNotification4(Context context) {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
        builder.statusBarDrawable = R.drawable.logo;
//		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为自动消失
        builder.notificationDefaults = Notification.DEFAULT_LIGHTS;// 设置为铃声与震动都不要
        JPushInterface.setDefaultPushNotificationBuilder(builder);
    }

    //自定义报警通知（震动铃声都要）
    public static void setNotification1(Context context) {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
        builder.statusBarDrawable = R.drawable.logo;//消息栏显示的图标
//		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;// 设置为铃声与震动都要
        JPushInterface.setDefaultPushNotificationBuilder(builder);
    }

    //自定义报警通知（铃声）
    public static void setNotification2(Context context) {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
        builder.statusBarDrawable = R.drawable.logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;// 设置为铃声与震动都要
        JPushInterface.setDefaultPushNotificationBuilder(builder);
    }

    //自定义报警通知（震动）
    public static void setNotification3(Context context) {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
        builder.statusBarDrawable = R.drawable.logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为自动消失
        builder.notificationDefaults = Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;// 震动
        JPushInterface.setDefaultPushNotificationBuilder(builder);
    }

    public static String addComma(String str) {

        // 将传进数字反转
        String reverseStr = new StringBuilder(str).reverse().toString();

        String strTemp = "";
        for (int i = 0; i < reverseStr.length(); i++) {
            if (i * 3 + 3 > reverseStr.length()) {
                strTemp += reverseStr.substring(i * 3, reverseStr.length());
                break;
            }
            strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
        }
        // 将 【789,456,】 中最后一个【,】去除
        if (strTemp.endsWith(",")) {
            strTemp = strTemp.substring(0, strTemp.length() - 1);
        }

        // 将数字重新反转
        String resultStr = new StringBuilder(strTemp).reverse().toString();
        return resultStr;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static int getCurrentVersion(Context c) {
        int versionCode = 0;
        try {
            PackageInfo pkg = c.getPackageManager().getPackageInfo(
                    c.getPackageName(), 0);
            String remoteVersion = c.getSharedPreferences("account", 0).getString("remoteVersion", pkg.versionName).replace(".", "");
            versionCode = Integer.parseInt(remoteVersion);
            if (versionCode < Integer.parseInt(pkg.versionName.replace(".", ""))) {
                return Integer.parseInt(pkg.versionName.replace(".", ""));
            }
        } catch (NameNotFoundException e) {
            Log.e("test", e.getMessage());
        }
        return versionCode;
    }

    public static String getCurrentVersionName(Context c) {
        String versionCode = "";
        try {
            PackageInfo pkg = c.getPackageManager().getPackageInfo(
                    c.getPackageName(), 0);
            versionCode = c.getSharedPreferences("account", 0).getString("remoteVersion", pkg.versionName);
            if (Integer.parseInt(versionCode.replace(".", "")) < Integer.parseInt(pkg.versionName.replace(".", ""))) {
                return pkg.versionName;
            }
        } catch (NameNotFoundException e) {
            Log.e("test", e.getMessage());
        }
        return versionCode;
    }

    /**
     * 获取缩略图地址
     *
     * @param url
     * @return
     */
    public static String getUrl(String url) {
        int index = url.indexOf("?");
        if (index > 0) {
            return url.substring(0, index) + "?imageView2/2/w/120/h/120/interlace/1/q/100";
        } else {
            return url + "?imageView2/2/w/120/h/120/interlace/1/q/100";
        }
    }

    // 数字转时分秒
    public static String getTime(long time) {
        String timeStr = null;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
//				if (hour < 24) {
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "小时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
//				} else {
//					int day = hour/24;
//					hour = hour % 24;
//					timeStr = day + "天" + hour + "小时";
//				}

            }
        }
        return timeStr;
    }

    public static String getTimeMill(long time) {
        String timeStr = null;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / (1000 * 60);
            if (minute < 60) {
                second = (time % (1000 * 60)) / 1000;

                timeStr = unitFormat(minute) + "分钟" + unitFormat(second) + "秒";
            } else {
                hour = minute / 60;
//				if (hour < 24) {
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "小时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
//				} else {
//					int day = hour/24;
//					hour = hour % 24;
//					timeStr = day + "天" + hour + "小时";
//				}

            }
        }
        return timeStr;
    }

    // 将 BASE64 编码的字符串 s 进行解码
    public static String getFromBASE64(String s) {
        if (s == null)
            return null;
        try {
            String enToStr = Base64
                    .encodeToString(s.getBytes(), Base64.DEFAULT);
            return enToStr;
        } catch (Exception e) {
            return null;
        }
    }

    public static String md5Encode(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            Log.e("Utils", e.getMessage());
            return "";
        }
        try {
            byte[] byteArray = inStr.getBytes("UTF-8");
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString().toLowerCase();
        } catch (Exception e) {
            return inStr;
        }
    }

    /**
     * @param time 时间戳
     * @param str  时间显示的格式 比如yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDateTimeByMillisecond(long time, String str) {
        try {
            Date date = new Date(time);
            SimpleDateFormat format = new SimpleDateFormat(str);
            String timeStr = format.format(date);
            return timeStr;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param time 时间戳
     * @return 跨年显示年  当前年份显示 月日
     */
    public static String getDateByMillisecond(long time) {
        try {
            SimpleDateFormat format2 = new SimpleDateFormat(
                    "yyyy年MM月dd日");
            Date date2 = new Date();
            Date date = new Date(time);
            String oldTime = format2.format(date);
            String newTime = format2.format(date2);
            if (newTime.substring(0, 4).equals(oldTime.substring(0, 4))) {
                SimpleDateFormat format = new SimpleDateFormat(
                        "MM月dd日");
                return format.format(date);
            } else {
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy年MM月dd日");
                return format.format(date);
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param time 时间戳
     * @return
     */
    public static String getDateTimeByMillisecond(long time) {
        try {
            SimpleDateFormat format2 = new SimpleDateFormat(
                    "yyyy-MM-dd");
            String timeStr = "";
            Date date2 = new Date();
            Date date = new Date(time);
            String oldTime = format2.format(date);
            String newTime = format2.format(date2);
            if (date2.getTime() > time) {
                if (newTime.substring(0, 4).equals(oldTime.substring(0, 4))) {
                    if (newTime.substring(5, 7).equals(oldTime.substring(5, 7))) {
                        int day = Integer.parseInt(newTime.substring(8, newTime.length()));
                        int day2 = Integer.parseInt(oldTime.substring(8, oldTime.length()));
                        if (day == day2) {
                            SimpleDateFormat format = new SimpleDateFormat(
                                    "HH:mm");
                            return timeStr = format.format(date);
                        } else {
                            if ((day - day2) == 1) {
//								SimpleDateFormat format = new SimpleDateFormat(
//										"HH:mm");
                                return timeStr = "昨天";// + format.format(date);
                            } else {
                                SimpleDateFormat format = new SimpleDateFormat(
                                        "MM-dd");
                                return timeStr = format.format(date);
                            }
                        }
                    } else {
                        SimpleDateFormat format = new SimpleDateFormat(
                                "MM-dd");
                        return timeStr = format.format(date);
                    }
                } else {
                    SimpleDateFormat format = new SimpleDateFormat(
                            "yyyy-MM-dd");
                    return timeStr = format.format(date);
                }
            } else {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                return timeStr = format.format(date);
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param time 时间戳
     * @return true 表示已刷新   false 表示未刷新
     */
    public static boolean isRefresh(long time) {
        try {
            SimpleDateFormat format2 = new SimpleDateFormat(
                    "yyyy-MM-dd");
            Date date2 = new Date();
            Date date = new Date(time);
            String oldTime = format2.format(date);
            String newTime = format2.format(date2);
            if (date2.getTime() > time) {
                if (newTime.substring(0, 4).equals(oldTime.substring(0, 4))) {
                    if (newTime.substring(5, 7).equals(oldTime.substring(5, 7))) {
                        int day = Integer.parseInt(newTime.substring(8, newTime.length()));
                        int day2 = Integer.parseInt(oldTime.substring(8, oldTime.length()));
                        if (day == day2) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取今天23点59分时间戳
     *
     * @return
     */
    public static long dayTimeTwentythree() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(date) + " 23:59:59";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeStemp = (long) date.getTime() / 1000;
        return timeStemp;
    }

    private static String unitFormat(long i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    // 获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" + Build.BOARD.length() % 10
                + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; // 13 位

        try {
            serial = Build.class.getField("SERIAL").get(null)
                    .toString();
            // API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode())
                    .toString();
        } catch (Exception exception) {
            // serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        // 使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode())
                .toString();
    }

    public static String ToDBC(String input) {
        input = stringFilter(input);
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }

        return new String(c);
    }

    public static String stringFilter(String str) {
        str = str.replaceAll("!", "! ").replaceAll(":", ": ").replaceAll(",", ", ").replaceAll("'", "' ").replaceAll("\"", "\" ")
                .replaceAll("！", "! ").replaceAll("：", ": ").replaceAll("，", ", ").replaceAll("‘", "' ").replaceAll("“", "\" ");// 替换中文标号  
        String regEx = "[『』]"; // 清除掉特殊字符  
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 判断缓存的String数据是否到期
     *
     * @param str
     * @return true：到期了 false：还没有到期
     */
    public static boolean isDue(String str) {
        return isDue(str.getBytes());
    }

    /**
     * 判断缓存的byte数据是否到期
     *
     * @param data
     * @return true：到期了 false：还没有到期
     */
    public static boolean isDue(byte[] data) {
        String[] strs = getDateInfoFromDate(data);
        if (strs != null && strs.length == 2) {
            String saveTimeStr = strs[0];
            while (saveTimeStr.startsWith("0")) {
                saveTimeStr = saveTimeStr
                        .substring(1, saveTimeStr.length());
            }
            long saveTime = Long.valueOf(saveTimeStr);
            long deleteAfter = Long.valueOf(strs[1]);
            if (System.currentTimeMillis() > saveTime + deleteAfter * 1000) {
                return true;
            }
        }
        return false;
    }

    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public static String newStringWithDateInfo(int second, String strInfo) {
        return createDateInfo(second) + strInfo;
    }

    public static byte[] newByteArrayWithDateInfo(int second, byte[] data2) {
        byte[] data1 = createDateInfo(second).getBytes();
        byte[] retdata = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, retdata, 0, data1.length);
        System.arraycopy(data2, 0, retdata, data1.length, data2.length);
        return retdata;
    }

    public static String clearDateInfo(String strInfo) {
        if (strInfo != null && hasDateInfo(strInfo.getBytes())) {
            strInfo = strInfo.substring(strInfo.indexOf(mSeparator) + 1,
                    strInfo.length());
        }
        return strInfo;
    }

    public static byte[] clearDateInfo(byte[] data) {
        if (hasDateInfo(data)) {
            return copyOfRange(data, indexOf(data, mSeparator) + 1,
                    data.length);
        }
        return data;
    }

    private static boolean hasDateInfo(byte[] data) {
        return data != null && data.length > 15 && data[13] == '-'
                && indexOf(data, mSeparator) > 14;
    }

    private static String[] getDateInfoFromDate(byte[] data) {
        if (hasDateInfo(data)) {
            String saveDate = new String(copyOfRange(data, 0, 13));
            String deleteAfter = new String(copyOfRange(data, 14,
                    indexOf(data, mSeparator)));
            return new String[]{saveDate, deleteAfter};
        }
        return null;
    }

    private static int indexOf(byte[] data, char c) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == c) {
                return i;
            }
        }
        return -1;
    }

    private static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }

    private static final char mSeparator = ' ';

    private static String createDateInfo(int second) {
        String currentTime = System.currentTimeMillis() + "";
        while (currentTime.length() < 13) {
            currentTime = "0" + currentTime;
        }
        return currentTime + "-" + second + mSeparator;
    }

    /*
     * Bitmap → byte[]
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /*
     * byte[] → Bitmap
     */
    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /*
     * Drawable → Bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /*
     * Bitmap → Drawable
     */
    @SuppressWarnings("deprecation")
    public static Drawable bitmap2Drawable(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        return new BitmapDrawable(bm);
    }

    public static boolean isString(String str) {
        if (str == null || str.equals("") || str.equals("null") || str.equals("0") || str.equals("0.0") || str.equals("0.00")) {
            return false;
        } else {
            return true;
        }
    }

    public static Drawable getDrawable(Context c, int res) {
        Drawable drawable = c.getResources().getDrawable(res);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    /**
     * 对textview 中指定文本进行字体颜色 大小 特殊处理
     *
     * @param start
     * @param end
     * @param color
     * @param str
     * @return
     */
    public static SpannableStringBuilder getSpannableStringBuilder(int start, int end, int color, String str) {
        return getSpannableStringBuilder(start, end, color, str, 15);
    }

    /**
     * 对textview 中指定文本进行字体颜色 大小 特殊处理
     *
     * @param start
     * @param end
     * @param color
     * @param str
     * @return
     */
    public static SpannableStringBuilder getSpannableStringBuilder(int start, int end, int color, String str, int textSize) {
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
        builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new AbsoluteSizeSpan(textSize, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static SpannableStringBuilder getSpannableStringBuilder(int start, int end, int color, String str, int textSize, boolean isBold) {
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
        builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new AbsoluteSizeSpan(textSize, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static SpannableStringBuilder getSpannableStringBuilder(int start, int end, String str, int textSize, boolean isBold) {
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        builder.setSpan(new AbsoluteSizeSpan(textSize, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static SpannableStringBuilder getSpannableStringBuilder(int start, int end, int color, int bgColor, String str, int textSize, boolean isBold) {
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
        BackgroundColorSpan bgSpan = new BackgroundColorSpan(bgColor);
        builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(bgSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new AbsoluteSizeSpan(textSize, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static float convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private static final int BUF_SIZE = 2048;

    public static boolean prepareDex(Context context, File dexInternalStoragePath, String dex_file) {
        BufferedInputStream bis = null;
        OutputStream dexWriter = null;

        try {
            bis = new BufferedInputStream(context.getAssets().open(dex_file));
            dexWriter = new BufferedOutputStream(new FileOutputStream(dexInternalStoragePath));
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while ((len = bis.read(buf, 0, BUF_SIZE)) > 0) {
                dexWriter.write(buf, 0, len);
            }
            dexWriter.close();
            bis.close();
            return true;
        } catch (IOException e) {
            if (dexWriter != null) {
                try {
                    dexWriter.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            return false;
        }
    }

    /**
     * 判断应用是否开启某个权限
     *
     * @param c
     * @param per 权限 "android.permission.RECORD_AUDIO"
     * @return
     */
    public static boolean isOpenPermission(Context c, String per) {
        PackageManager pm = c.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(per, "com.lwc.common"));
        return permission;
    }

    /**
     * 字符串中识别手机号码
     *
     * @param str
     * @return
     */
    public static String[] findNumber(String str) {
        Pattern pattern = Pattern
                .compile("(?<!\\d)(?:(?:1[34578]\\d{9})|(?:861[34578]\\d{9}))(?!\\d)");
        Matcher matcher = pattern.matcher(str);
        StringBuffer bf = new StringBuffer();

        while (matcher.find()) {
            bf.append(matcher.group()).append(",");
        }
        String strText = bf.toString();
        Log.e("strText", strText);
        String str2[] = strText.split(",");
        return str2;
    }

    /**
     * @param packageContext
     * @param cls
     * @param keyvalues      需要传进去的String参数{{key1,values},{key2,value2}...}
     * @Description: 跳转, 带参数的方法;需要其它的数据类型,再继续重载吧
     */
    public static void toNextActivity(Context packageContext, Class<?> cls,
                                      String[][] keyvalues) {
        Intent i = new Intent(packageContext, cls);
        if (keyvalues != null && keyvalues.length > 0) {
            for (String[] strings : keyvalues) {
                i.putExtra(strings[0], strings[1]);
            }
        }
        i.setPackage(packageContext.getPackageName());
        packageContext.startActivity(i);
    }

    /**
     * 返回时间戳和随机数组成的图片名字
     *
     * @return
     */
    public static String getImgName() {
        Random random = new Random();
        int x = random.nextInt(999999);
        String key = new Date().getTime() + "_" + x + ".jpg";
        return key;
    }

    public static void deleteFile(String path, Context context) {
        if (!TextUtils.isEmpty(path)) {
            try {
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = context.getContentResolver();
                String where = MediaStore.Images.Media.DATA + "='" + path + "'";
                //删除图片
                mContentResolver.delete(uri, where, null);

                //发送广播
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File file = new File(path);
                Uri uri1 = Uri.fromFile(file);
                intent.setData(uri1);
                context.sendBroadcast(intent);
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断手机中有没有某个应用程序
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();//获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名
        //从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        if ((c >= 0x4e00) && (c <= 0x9fbb)) {
            return true;
        } else {
            return false;
        }
//		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
//		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
//				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
//				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
//				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
//				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
//				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
//			return true;
//		}
//		return false;
    }

    public static String jia(String v1, String v2) {
        BigDecimal bd1 = new BigDecimal(v1);
        BigDecimal bd2 = new BigDecimal(v2);
        return getNumber(bd1.add(bd2).toString() + ""); // 加
    }

    public static String jian(String v1, String v2) {
        BigDecimal bd1 = new BigDecimal(v1);
        BigDecimal bd2 = new BigDecimal(v2);
        return getNumber(bd1.subtract(bd2).toString() + ""); // 减
    }

    public static String cheng(String v1, String v2) {
        BigDecimal bd1 = new BigDecimal(v1);
        BigDecimal bd2 = new BigDecimal(v2);
        return getNumber(bd1.multiply(bd2).toString()); // 乘
    }

    public static String chu(String v1, String v2) {
        BigDecimal bd1 = new BigDecimal(v1);
        BigDecimal bd2 = new BigDecimal(v2);
        return getNumber(bd1.divide(bd2, 2, BigDecimal.ROUND_HALF_UP).toString()); // 除
    }

    public static String getNumber(String str) {
        if (str.endsWith(".00") || str.endsWith(".0") || str.endsWith(".000") || str.endsWith(".0000")) {
            str = str.substring(0, str.indexOf("."));
        }
        return str;
    }

    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        if (DEVICE_ID == null || DEVICE_ID.equals("") || DEVICE_ID.length() < 8) {
            String device_id = SharedPreferencesUtils.getInstance(context).loadString("device_id");
            if (TextUtils.isEmpty(device_id)) {
                Random random = new Random();
                int x = random.nextInt(99);
                DEVICE_ID = new Date().getTime() + "" + x;
                SharedPreferencesUtils.getInstance(context).saveString("device_id", DEVICE_ID);
            } else {
                DEVICE_ID = device_id;
            }
        }
        return DEVICE_ID;
    }

    public static String getMoney(String money) {
        if (money.contains(".")) {
            int index = money.indexOf(".");
            if (index == money.length() - 1) {
                money = money + "00";
            } else if (index == money.length() - 2) {
                money = money + "0";
            }
        } else {
            money = money + ".00";
        }
        return money;
    }

    public static String formatTwoPoint(Double str) {
        if (str == 0d) {
            return "0.00";
        }
        DecimalFormat df = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            df = new DecimalFormat("######0.00");
            return df.format(str);
        } else {
            BigDecimal bg = new BigDecimal(str);
            double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return String.valueOf(f1);
        }
    }

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    public static void lxkf(Context context, String phone) {
// 检查是否获得了权限（Android6.0运行时权限）
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
// 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CALL_PHONE)) {
// 返回值：
//如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
// 弹窗需要解释为何需要该权限，再次请求授权
                Toast.makeText(context, "请授权！", Toast.LENGTH_LONG).show();
// 帮跳转到该应用的设置界面，让用户手动授权
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", "com.lwc.common", null);
                intent.setData(uri);
                context.startActivity(intent);
            } else {
// 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        } else {
// 已经获得授权，可以打电话
            CallPhone(context, phone);
        }
    }

    private static void CallPhone(Context context, String phone) {
        if (!TextUtils.isEmpty(phone)) {
            phone = "tel:"+phone;
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse(phone);
        intent.setData(data);
        context.startActivity(intent);
    }


    // 状态栏高度
    private static  int statusBarHeight = 0;
    // 屏幕像素点
    private static final Point screenSize = new Point();

    // 获取屏幕像素点
    public static Point getScreenSize(Activity context) {

        if (context == null) {
            return screenSize;
        }
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            Display diplay = wm.getDefaultDisplay();
            if (diplay != null) {
                diplay.getMetrics(mDisplayMetrics);
                int W = mDisplayMetrics.widthPixels;
                int H = mDisplayMetrics.heightPixels;
                if (W * H > 0 && (W > screenSize.x || H > screenSize.y)) {
                    screenSize.set(W, H);
                }
            }
        }
        return screenSize;
    }

    // 获取状态栏高度
    public static int getStatusBarHeightSign(Context context) {
        if (statusBarHeight <= 0) {
            Rect frame = new Rect();
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        if (statusBarHeight <= 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(x);

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    /**
     * 复制内容到剪切板
     *
     * @param copyStr
     * @return
     */
    public static boolean copy(String copyStr,Context context) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            Log.d("复制失败",e.getMessage());
            return false;
        }
    }
}
