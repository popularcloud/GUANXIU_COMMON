package com.lwc.common.module.integral.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.interf.OnItemClickCallBack;
import com.lwc.common.interf.OnLookRewardCallBack;
import com.lwc.common.module.bean.Address;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.integral.adapter.LuckAdapter;
import com.lwc.common.module.integral.bean.IntegralLuckBean;
import com.lwc.common.module.integral.bean.IntegralLuckResultBean;
import com.lwc.common.module.repairs.ui.activity.AddressManagerActivity;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.SpaceItemDecoration;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.widget.LuckSuccessDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * @author younge
 * @date 2019/7/8 0025
 * @email 2276559259@qq.com
 * desc 积分抽奖
 */
public class IntegralLuckDrawActivity extends BaseActivity {

    @BindView(R.id.rv_lucky)
    RecyclerView rv_lucky;
    @BindView(R.id.ll_light_switch)
    LinearLayout ll_light_switch;
    @BindView(R.id.tv_rule)
    TextView tv_rule;
    @BindView(R.id.tv_short_rule)
    TextView tv_short_rule;
    private MyTask swithBgTask;

    private int [] selItems = new int[]{0,1,2,5,8,7,6,3,0};
    private List<IntegralLuckBean.GoodsBean> goodsBeens = new ArrayList<>();
    private LuckAdapter luckAdapter;
    private int presentId;
    private int previousId;
    private boolean isLightSwitch = false;
    private boolean ishasAddress;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
              //  Log.d("联网成功","进入handle");
                switch (msg.what) {
                    case 1:
                        if(isLightSwitch){
                            ll_light_switch.setBackgroundResource(R.drawable.ic_luck_lamp_bg1);
                            isLightSwitch = false;
                         //   Log.d("联网成功","换背景1");
                        }else{
                            ll_light_switch.setBackgroundResource(R.drawable.ic_luck_lamp_bg2);
                            isLightSwitch = true;
                           // Log.d("联网成功","换背景2");
                        }
                        break;
                }
        }
    };

    private class MyTask extends TimerTask {
        @Override
        public void run() {
           // Log.d("联网成功","执行task");
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }
    }

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_integral_luck_draw;
    }

    @Override
    protected void findViews() {
        setTitle("积分抽奖");
    }

    @Override
    protected void init() {
      //  Log.d("联网成功","进入init");
        Timer timer = new Timer();
        swithBgTask = new MyTask();
        timer.schedule(swithBgTask, 1, 500);
    }

    @Override
    protected void initGetData() {
        HttpRequestUtils.httpRequest(IntegralLuckDrawActivity.this, "luckyRoom", RequestValue.GET_LUCKY_LUCKYROOM,null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        try {
                            IntegralLuckBean bean = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), IntegralLuckBean.class);
                            onLoadData(bean);
                        } catch (Exception e) {
                            ///e.printStackTrace();
                            ToastUtil.showToast(IntegralLuckDrawActivity.this,e.getMessage());
                        }
                        break;
                    default:
                        ToastUtil.showToast(IntegralLuckDrawActivity.this,common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showToast(IntegralLuckDrawActivity.this,msg);
            }
        });
    }

    private void onLoadData(IntegralLuckBean bean){
        if(bean == null || bean.getGoods() == null || bean.getGoods().size() < 8){
            ToastUtil.showToast(IntegralLuckDrawActivity.this,"初始化抽奖信息失败");
            finish();
        }else{
            goodsBeens.clear();
            goodsBeens.addAll(bean.getGoods());
            goodsBeens.add(4,new IntegralLuckBean.GoodsBean());
            luckAdapter = new LuckAdapter(this, goodsBeens, new OnItemClickCallBack() {
                @Override
                public void onItemClick(int position) {
                    if(position == 4){
                        rv_lucky.getChildAt(4).setEnabled(false);
                       isHasAddress();
                    }
                }
            });
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3){
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }
            };
            rv_lucky.setLayoutManager(gridLayoutManager);
            rv_lucky.addItemDecoration(new SpaceItemDecoration(4,0,SpaceItemDecoration.GRIDLAYOUT));
            rv_lucky.setAdapter(luckAdapter);

            tv_rule.setText(bean.getRemark());
            if(!TextUtils.isEmpty(bean.getRemarkShort())){
                tv_short_rule.setText(bean.getRemarkShort().trim());
            }

        }
    }

    /**
     * 判断是否有地址
     * @return
     */
    private void isHasAddress() {
        ishasAddress = false;
        HttpRequestUtils.httpRequest(this, "getUserAddress", RequestValue.GET_USER_ADDRESS, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        List<Address> addresses = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<Address>>() {
                        });
                        if(addresses != null && addresses.size() > 0){
                           getLuck();
                        }else{
                            rv_lucky.getChildAt(4).setEnabled(true);
                            ToastUtil.showToast(IntegralLuckDrawActivity.this,"请填写收货地址");
                            IntentUtil.gotoActivity(IntegralLuckDrawActivity.this, AddressManagerActivity.class);
                        }
                        break;
                    default:
                        rv_lucky.getChildAt(4).setEnabled(true);
                        ToastUtil.showToast(IntegralLuckDrawActivity.this,"请填写收货地址");
                        IntentUtil.gotoActivity(IntegralLuckDrawActivity.this, AddressManagerActivity.class);
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                rv_lucky.getChildAt(4).setEnabled(true);
                ToastUtil.showToast(IntegralLuckDrawActivity.this,"请填写收货地址");
                IntentUtil.gotoActivity(IntegralLuckDrawActivity.this, AddressManagerActivity.class);
            }
        });
    }

    /**
     * 获取抽奖
     */
    private void getLuck(){
        HttpRequestUtils.httpRequest(IntegralLuckDrawActivity.this, "luckyDraw", RequestValue.GET_LUCKY_LUCKYDRAW,null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        try {
                            IntegralLuckResultBean bean = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), IntegralLuckResultBean.class);
                            if(bean != null){
                                startLuck(bean);
                            }else{
                                ToastUtil.showToast(IntegralLuckDrawActivity.this,"抱歉，您未中奖");
                                rv_lucky.getChildAt(4).setEnabled(true);
                            }

                        } catch (Exception e) {
                            ///e.printStackTrace();
                            ToastUtil.showToast(IntegralLuckDrawActivity.this,e.getMessage());
                            rv_lucky.getChildAt(4).setEnabled(true);
                        }
                        break;
                    default:
                        ToastUtil.showToast(IntegralLuckDrawActivity.this,common.getInfo());
                        rv_lucky.getChildAt(4).setEnabled(true);
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showToast(IntegralLuckDrawActivity.this,msg);
            }
        });
    }
    /**
     * 开始抽奖
     */
    private void startLuck(final IntegralLuckResultBean bean){
        previousId = 0;
        presentId = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int j = 0; j < 3; j ++){
                    for(int i : selItems){
                        presentId = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // rv_lucky.getChildAt(presentId).setBackgroundResource(R.drawable.img_yellow_bg);
                                rv_lucky.getChildAt(presentId).setAlpha(1f);
                                if(previousId != presentId){
                                   // rv_lucky.getChildAt(previousId).setBackgroundResource(R.drawable.img_red_bg);
                                    rv_lucky.getChildAt(previousId).setAlpha(0.5f);
                                }
                                previousId = presentId;
                            }
                        });
                        try {
                            if(j == 2){
                                Thread.sleep(500);
                                if(bean.getLuckyId().equals(goodsBeens.get(presentId).getLuckyId())){
                                  //  ToastUtil.showToast(IntegralLuckDrawActivity.this,bean.getRemark());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            rv_lucky.getChildAt(4).setEnabled(true);
                                            LuckSuccessDialog luckSuceessDialog = new LuckSuccessDialog(IntegralLuckDrawActivity.this, bean.getRemark(), bean.getLuckyImage(), bean.getLuckyType(), new OnLookRewardCallBack() {
                                                @Override
                                                public void onItemClick(int type) {
                                                    if(type == -1){//再抽一次

                                                    }else if(type == 1){  //积分奖励
                                                        IntentUtil.gotoActivity(IntegralLuckDrawActivity.this, IntegralOrderActivity.class);
                                                    }else{ //实物和虚拟物品
                                                        IntentUtil.gotoActivity(IntegralLuckDrawActivity.this,IntegralExchangeRecordActivity.class);
                                                    }
                                                }
                                            });
                                            luckSuceessDialog.show();
                                        }
                                    });
                                    break;
                                }
                            }else{
                                Thread.sleep(100);
                            }

                        } catch (InterruptedException e) {
                            rv_lucky.getChildAt(4).setEnabled(true);
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).start();
    }

    @Override
    protected void widgetListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(swithBgTask != null){
            swithBgTask.cancel();
        }
    }
}
