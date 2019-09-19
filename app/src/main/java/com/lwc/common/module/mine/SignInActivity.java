package com.lwc.common.module.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.bean.SignBean;
import com.lwc.common.bean.SignedListBean;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.interf.OnLookRewardCallBack;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.integral.activity.IntegralMainActivity;
import com.lwc.common.module.order.ui.activity.MyCheckActivity;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.widget.SignInSuccessDialog;
import com.yanzhenjie.sofia.Sofia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author younge
 * @date 2019/5/11 0011
 * @email 2276559259@qq.com
 * 兑换详情
 */
public class SignInActivity extends BaseActivity implements CalendarView.OnYearViewChangeListener, CalendarView.OnWeekChangeListener, CalendarView.OnCalendarLongClickListener, CalendarView.OnMonthChangeListener, CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener, CalendarView.OnCalendarInterceptListener, CalendarView.OnViewChangeListener {

    @BindView(R.id.calendarLayout)
    CalendarLayout calendarLayout;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.tv_present_date)
    TextView tv_present_date;
    @BindView(R.id.btn_sign)
    Button btn_sign;
    @BindView(R.id.tv_continuity_sign_in)
    TextView tv_continuity_sign_in;
    @BindView(R.id.tv_total_sign_in)
    TextView tv_total_sign_in;
    @BindView(R.id.tv_integral_sign_in)
    TextView tv_integral_sign_in;
    @BindView(R.id.tv_sign_rule_content)
    TextView tv_sign_rule_content;
    @BindView(R.id.iv_previous)
    ImageView iv_previous;
    @BindView(R.id.iv_next)
    ImageView iv_next;

    private int searchYear = 2019;
    private int searchMonth = 3;
    private SignedListBean signedListBean;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void findViews() {
        setTitle("签到");

        searchYear = mCalendarView.getCurYear();
        searchMonth = mCalendarView.getCurMonth();
        tv_present_date.setText(searchYear+"/"+searchMonth);
    }

    @Override
    protected void init() {

        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnCalendarLongClickListener(this, true);
        mCalendarView.setOnWeekChangeListener(this);
        mCalendarView.setOnYearViewChangeListener(this);

        //设置日期拦截事件，仅适用单选模式，当前无效
        mCalendarView.setOnCalendarInterceptListener(this);
        mCalendarView.setOnViewChangeListener(this);
        mCalendarView.setClickable(false);
    }

    @Override
    protected void initGetData() {

        Map<String,String> params = new HashMap<>();
        params.put("year",String.valueOf(searchYear));
        params.put("month",String.valueOf(searchMonth));

        HttpRequestUtils.httpRequest(this, "获取签到信息", RequestValue.GET_SIGN_SIGNTIME, params, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        signedListBean = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), SignedListBean.class);
                        loadDataToUI();
                        break;
                    default:
                        ToastUtil.showLongToast(SignInActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                if (msg != null && !msg.equals("")) {
                    ToastUtil.showLongToast(SignInActivity.this, msg);
                } else {
                    ToastUtil.showLongToast(SignInActivity.this, "请求失败，请稍候重试!");
                }
            }
        });
    }

    private void loadDataToUI() {
        if(signedListBean != null){
             if(signedListBean.getInfo() == null){
                 tv_continuity_sign_in.setText("0");
                 tv_total_sign_in.setText("0");
                 tv_integral_sign_in.setText("0");
                 tv_sign_rule_content.setText(signedListBean.getRemark());
             }else{
                 tv_continuity_sign_in.setText(String.valueOf(signedListBean.getInfo().getSign_count()));
                 tv_total_sign_in.setText(String.valueOf(signedListBean.getInfo().getSign_sum()));
                 tv_integral_sign_in.setText(Utils.chu(String.valueOf(signedListBean.getInfo().getSign_integral()),"100"));
                 tv_sign_rule_content.setText(signedListBean.getRemark());
             }


            List<SignedListBean.DayBean> dayBeans = signedListBean.getDay();
            if(dayBeans != null){
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                int currentDay = calendar.get(java.util.Calendar.DAY_OF_MONTH);
                int currentMonth = calendar.get(java.util.Calendar.MONTH);
                Map<String, Calendar> map = new HashMap<>();
                for(int i = 0;i < dayBeans.size();i++){
                    SignedListBean.DayBean dayBean = dayBeans.get(i);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date date = sdf.parse(dayBean.getCreate_time());
                        System.out.println(date);
                        calendar.setTime(date);
                        map.put(getSchemeCalendar(calendar.get(java.util.Calendar.YEAR), calendar.get(java.util.Calendar.MONTH)+1,calendar.get(java.util.Calendar.DAY_OF_MONTH), 0xFF40db25, "签到").toString(),
                                getSchemeCalendar(calendar.get(java.util.Calendar.YEAR), calendar.get(java.util.Calendar.MONTH)+1,calendar.get(java.util.Calendar.DAY_OF_MONTH), 0xFF40db25, "签到"));

                        //判断今日是否签到过
                        if(i == 0 && (currentMonth+1) == searchMonth){
                            int lastSignDay = calendar.get(java.util.Calendar.DAY_OF_MONTH);
                            if(currentDay == lastSignDay){
                                btn_sign.setEnabled(false);
                                btn_sign.setText("今日已签到");
                            }else{
                                btn_sign.setEnabled(true);
                                btn_sign.setText("签到");
                            }
                        }

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mCalendarView.setSchemeDate(map);
                    calendarLayout.expand();
                }

            }
        }
    }

    /**
     * 签到
     */
    protected void signIn() {
        HttpRequestUtils.httpRequest(this, "签到", RequestValue.GET_SIGN_SIGNIN, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        SignBean signBean = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), SignBean.class);
                        if(signBean.isStatus()){
                            signSuccess(signBean);
                        }else{
                            ToastUtil.showToast(SignInActivity.this,"您今天已经签到过了");
                            btn_sign.setEnabled(false);
                            btn_sign.setText("今日已签到");
                        }
                        break;
                    default:
                        ToastUtil.showLongToast(SignInActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                if (msg != null && !msg.equals("")) {
                    ToastUtil.showLongToast(SignInActivity.this, msg);
                } else {
                    ToastUtil.showLongToast(SignInActivity.this, "请求失败，请稍候重试!");
                }
            }
        });
    }

    /**
     * 签到成功
     */
    private void signSuccess(SignBean signBean) {

        if(signBean != null) {
            if ("0".equals(signBean.getType())) {
                SignInSuccessDialog dialog = new SignInSuccessDialog(SignInActivity.this, signBean.getMsg(), signBean.getScoreMsg(), OnLookRewardCallBack.RewardIntergray, new OnLookRewardCallBack() {
                    @Override
                    public void onItemClick(int type) {
                        IntentUtil.gotoActivity(SignInActivity.this, IntegralMainActivity.class);
                    }
                });
                dialog.show();
            } else if ("1".equals(signBean.getType())) {
                SignInSuccessDialog dialog = new SignInSuccessDialog(SignInActivity.this, signBean.getMsg(), signBean.getCouponMsg(), OnLookRewardCallBack.RewardCoupon, new OnLookRewardCallBack() {
                    @Override
                    public void onItemClick(int type) {
                        IntentUtil.gotoActivity(SignInActivity.this, MyCheckActivity.class);
                    }
                });
                dialog.show();
            }
        }
        initGetData();
    }

    @Override
    protected void widgetListener() {

    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        Sofia.with(this)
                .statusBarBackground(Color.parseColor("#fb5050"));
    }

    @OnClick({R.id.btn_sign,R.id.iv_next,R.id.iv_previous})
    public void onBtnOnclick(View view){
        switch (view.getId()){
            case R.id.btn_sign:
                if(btn_sign.isEnabled()){
                    btn_sign.setEnabled(false);
                    btn_sign.setText("今日已签到");
                    signIn();
                }
                break;
            case R.id.iv_next:
                mCalendarView.scrollToNext();
                break;
            case R.id.iv_previous:
                mCalendarView.scrollToPre();
                break;
        }
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }

    @Override
    public void onYearViewChange(boolean isClose) {

    }

    @Override
    public void onWeekChange(List<Calendar> weekCalendars) {

    }

    @Override
    public void onCalendarLongClickOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarLongClick(Calendar calendar) {

    }

    @Override
    public void onMonthChange(int year, int month) {
        searchMonth = month;
        searchYear = year;
        initGetData();
        tv_present_date.setText(year+"/"+month);
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {

    }

    @Override
    public void onYearChange(int year) {

    }

    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        return false;
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {

    }

    @Override
    public void onViewChange(boolean isMonthView) {

    }
}
