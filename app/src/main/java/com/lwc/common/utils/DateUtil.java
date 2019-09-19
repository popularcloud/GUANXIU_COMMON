package com.lwc.common.utils;

import com.lwc.common.R;
import com.lwc.common.configs.TApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 秒
     */
    private static String seconds = TApplication.context.getString(R.string.second);
    /**
     * 分
     */
    private static String minutes = TApplication.context.getString(R.string.minute);
    /**
     * 时
     */
    private static String hours = TApplication.context.getString(R.string.hour);
    /**
     * 刚刚
     */
    private static String before = TApplication.context.getString(R.string.befor);

    /**
     * 获取系统当前时间
     *
     * @return 时间字符串
     * @version 1.0
     * @createTime 2013-10-24,上午9:58:21
     * @updateTime 2013-10-24,上午9:58:21
     * @createAuthor liujingguo
     * @updateAuthor liujingguo
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public static String getDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取发送时间
     *
     * @return
     * @version 1.0
     * @createTime 2017年8月6日, 上午10:45:56
     * @updateTime 2017年8月6日, 上午10:45:56
     * @createAuthor 何栋
     * @updateAuthor 何栋
     * @updateInfo
     */
    public static String getSendDate(Long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取当前时间点.
     *
     * @return 当前时间.
     * @version 1.0
     * @createTime 2013年11月25日, 下午5:44:39
     * @updateTime 2013年11月25日, 下午5:44:39
     * @createAuthor 何栋
     * @updateAuthor 何栋
     * @updateInfo
     */
    public static String getTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String time = formatter.format(currentTime);
        return time;
    }

    /**
     * 获取当前时间戳.
     *
     * @return 当前时间的时间戳.
     * @version 1.0
     * @createTime 2017年6月14日, 下午3:50:15
     * @updateTime 2017年6月14日, 下午3:50:15
     * @createAuthor 何栋
     * @updateAuthor 何栋
     * @updateInfo
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间(按指定格式).
     *
     * @param pattern 时间格式.
     * @return 当前时间(按指定格式).
     * @version 1.0
     * @createTime 2017年6月14日, 下午3:54:59
     * @updateTime 2017年6月14日, 下午3:54:59
     * @createAuthor 何栋
     * @updateAuthor 何栋
     * @updateInfo
     */
    public static String getCurrentTime(String pattern) {
        return timestampToPatternTime(getCurrentTime(), pattern);
    }

    /**
     * 将时间戳转换为指定时间(按指定格式).
     *
     * @param timestamp 时间戳.
     * @param pattern   时间格式.
     * @return 指定时间(按指定格式).
     * @version 1.0
     * @createTime 2017年6月14日, 下午3:58:22
     * @updateTime 2017年6月14日, 下午3:58:22
     * @createAuthor 何栋
     * @updateAuthor 何栋
     * @updateInfo
     */
    public static String timestampToPatternTime(long timestamp, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(timestamp));
    }

    /**
     * 将指定格式的时间转换为时间戳.
     *
     * @param time    时间(按指定格式).
     * @param pattern 时间格式.
     * @return 指定时间的时间戳.
     * @throws ParseException 当传入的时间与时间格式不匹配或传入不能识别的时间格式时抛出异常.
     * @version 1.0
     * @createTime 2017年6月14日, 下午4:04:18
     * @updateTime 2017年6月14日, 下午4:04:18
     * @createAuthor 何栋
     * @updateAuthor 何栋
     * @updateInfo
     */
    public static long patternTimeToTimestamp(String time, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = sdf.parse(time);
        return date.getTime();
    }

    /**
     * 计算距离当前时间.
     *
     * @param timestamp 当时时间戳.
     * @return 若距离当前时间小于1分钟则返回xx秒之前，若距离当前时间小于1小时则返回xx分钟之前，若距离当前时间小于1天则返回xx小时前， 若时间为今年则返回MM-dd，否则返回yyyy-MM-dd.
     * @version 1.0
     * @createTime 2014年6月20日, 上午10:36:55
     * @updateTime 2014年6月20日, 上午10:36:55
     * @createAuthor 何栋
     * @updateAuthor 何栋
     * @updateInfo
     */
    public static String fromTheCurrentTime(long timestamp) {
        if (timestamp < 1000000000000l) {
            timestamp = timestamp * 1000;
        }
        long timeDifference = (getCurrentTime() - timestamp) / 1000;
        if (timeDifference < 0) {
            return before;
        } else if (timeDifference < 60) {
            return String.valueOf(timeDifference) + seconds;
        } else if (timeDifference / 60 < 60) {
            return String.valueOf(timeDifference / 60) + minutes;
        } else if (timeDifference / 3600 < 24) {
            return String.valueOf(timeDifference / 3600) + hours;
        } else if (timestampToPatternTime(timestamp, "yyyy").equals(getCurrentTime("yyyy"))) {
            return timestampToPatternTime(timestamp, "MM-dd");
        } else {
            return timestampToPatternTime(timestamp, "yyyy-MM-dd");
        }
    }

    /**
     * 计算距离当前时间.
     *
     * @param time    当时时间
     * @param pattern 时间格式.
     * @return 若距离当前时间小于1分钟则返回xx秒之前，若距离当前时间小于1小时则返回xx分钟之前，若距离当前时间小于1天则返回xx小时前， 若时间为今年则返回MM-dd，否则返回yyyy-MM-dd.若传入的时间与时间格式不匹配则返回null.
     * @version 1.0
     * @createTime 2014年6月20日, 上午10:39:37
     * @updateTime 2014年6月20日, 上午10:39:37
     * @createAuthor 何栋
     * @updateAuthor 何栋
     * @updateInfo
     */
    public static String fromTheCurrentTime(String time, String pattern) {
        try {
            return fromTheCurrentTime(patternTimeToTimestamp(time, pattern));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return 该秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long mss) {
        long days = mss / (60 * 60 * 24);
        long hours = (mss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % (60 * 60)) / 60;
        long seconds = mss % 60;
        return days + " 天 " + hours + " 小时 " + minutes + " 分 ";
    }


    /**
     * 以空格号拆分， 返回前面的
     *
     * @param str
     * @return
     */
    public static String splitStr(String str) {

        String[] strings = str.split(" ");
        return strings[0];
    }


    /**
     * 字符串拆分， 返回前面的
     *
     * @param str               要拆分的字符串
     * @param regularExpression 拆分的方式
     * @return
     */
    public static String splitStr(String str, String regularExpression) {

        String[] strings = str.split(regularExpression);
        return strings[0];
    }

    /**
     * 时间加法计算
     *
     * @param date 天数
     * @return 按格式日期
     */
    public static String addTime(int date) {

        Calendar c = Calendar.getInstance();

        int date1 = c.get(Calendar.DATE); // 获取当前的天数

        c.set(Calendar.DATE, date1 + date);
        String timeStr = timeToStr(c.getTime());
        return timeStr;
    }

    /**
     * 通用工具类
     * —————————————————————————————————————————————————
     */


    /**
     * 时间相减
     * 注意 ： 这是算出准确的时分秒， 单单取天有点不准，因为漏掉后面的时间
     * int[]  0：天； 1： 时； 2 ：分；  3： 秒。
     *
     * @param begin
     * @param end
     * @return
     */
    public static int[] subtract(Date begin, Date end) {
        int[] date = new int[4];

        long between = (end.getTime() - begin.getTime()) / 1000;//除以1000是为了转换成秒
        int day = (int) (between / (24 * 3600));
        int hour = (int) (between % (24 * 3600) / 3600);
        int minute = (int) (between % 3600 / 60);
        int second = (int) (between - (day * 24 * 3600) - (hour * 3600) - (minute * 60));
        date[0] = day;
        date[1] = hour;
        date[2] = minute;
        date[3] = second;

        return date;
    }

    /**
     * 将时间字符串转换成 Date
     * DATE_FROMAT
     * 注意 date 和 dateFromat 样式要一致才能转换成功
     *
     * @param date
     * @param dateFromat FORMAT1("yyyy-MM-dd HH:mm:ss"), FORMAT2("yyyy-MM-dd HH:mm"), FORMAT3("yyyy-MM-dd");
     * @return
     */
    public static Date format(String date, String dateFromat) {
        SimpleDateFormat dfs = new SimpleDateFormat(dateFromat);
        Date date1 = null;
        try {
            date1 = dfs.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    /**
     * 获取当前时间  枚举类型 DATE_FROMAT
     * FORMAT1("yyyy-MM-dd HH:mm:ss"), FORMAT2("yyyy-MM-dd HH:mm"), FORMAT3("yyyy-MM-dd");
     *
     * @param dateFromat 指定的格式返回， 例如 "yyyy-MM-dd HH:mm"
     * @return
     */
    public static String getCurrentTimeToFromat(String dateFromat) {
        Date date = new Date();
        return format(date, dateFromat);
    }

    /**
     * 时间转换为字符串:yyyy-MM-dd HH:mm
     */
    @Deprecated
    public static String timeToStr(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (date != null)
            return timeFormat.format(date);
        return null;
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param dateFromat
     * @return
     */
    public static String format(Date date, String dateFromat) {

        SimpleDateFormat timeFormat = new SimpleDateFormat(dateFromat.toString());
        if (date != null)
            return timeFormat.format(date);
        return null;
    }

    public static enum DATE_FROMAT {

        FORMAT1("yyyy-MM-dd HH:mm:ss"), FORMAT2("yyyy-MM-dd HH:mm"), FORMAT3("yyyy-MM-dd");

        private String format;

        DATE_FROMAT(String format) {
            this.format = format;
        }

        //复写toString  输出String值
        @Override
        public String toString() {
            return this.format;
        }
    }

    /**
     * 获取当前的时间，以秒返回
     * @return
     */
    public static String getCurrentTimeToSecond() {
        Date date = new Date();
        return date.getTime() + "";
    }

}
