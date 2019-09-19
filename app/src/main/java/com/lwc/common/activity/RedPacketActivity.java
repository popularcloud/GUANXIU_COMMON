package com.lwc.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.ActivityBean;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.integral.activity.IntegralMainActivity;
import com.lwc.common.utils.Constants;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.TileButton;
import com.lwc.common.module.wallet.ui.WalletActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RedPacketActivity extends BaseActivity {

	@BindView(R.id.rl_bg)
	RelativeLayout rl_bg;
	@BindView(R.id.iv_close)
	ImageView iv_close;
	@BindView(R.id.tv_money)
	TextView tv_money;
	@BindView(R.id.tv_money_unit)
	TextView tv_money_unit;
	@BindView(R.id.layout_share)
	RelativeLayout layout_share;
	private ActivityBean activityBean;
	private String activityId;
	private boolean isOpen = false;
	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_red_packet;
	}

	@Override
	protected void findViews() {
		ButterKnife.bind(this);
		activityBean = (ActivityBean) getIntent().getSerializableExtra("activityBean");
		activityId = getIntent().getStringExtra("activityId");
		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		long [] pattern = {100,400,100,400}; // 停止 开启 停止 开启
		vibrator.vibrate(pattern,-1); //重复两次上面的pattern 如果只想震动一次，index设为-1

//		BaseEffects view = new NewsPaper();
//		view.setDuration(1000);
//		view.start(layout_share);
	}

	@Override
	protected void init() {
	}

	@Override
	protected void initGetData() {
	}

	@Override
	protected void widgetListener() {
	}

	@OnClick({R.id.iv_close, R.id.rl_bg})
	public void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.iv_close:
			setResult(Constants.RED_ID_RESULT);
			finish();

			break;
		case R.id.rl_bg:
			if(!isOpen){
				if (Utils.isFastClick(3000,"打开红包")) {
					return;
				}
				openRedPacket();
			}else{
				if (activityBean == null) {
					IntentUtil.gotoActivityAndFinish(this, WalletActivity.class);
				} else {
					setResult(Constants.RED_ID_TO_RESULT);
					finish();
				}
			}
			break;
		default:
			break;
		}
	}

	public void openRedPacket() {
		if (isOpen){
			return;
		}
		Map<String, String> map = new HashMap<>();
		if (activityBean != null) {
			activityId = activityBean.getActivityId();
		}
		map.put("activityId", activityId);
		HttpRequestUtils.httpRequest(this, "openRedPacket", RequestValue.GET_RED_PACKET_MONEY, map, "GET", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String response) {
				Common common = JsonUtil.parserGsonToObject(response, Common.class);
				switch (common.getStatus()) {
					case "1":
						try {
							if (isOpen) {
								return;
							}
							Intent intent = new Intent(MainActivity.KEY_UPDATE_USER);
							LocalBroadcastManager.getInstance(RedPacketActivity.this).sendBroadcast(intent);
							ToastUtil.showToast(RedPacketActivity.this, common.getInfo());
							JSONObject obj = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
							String str = "";
							double money = obj.optDouble("money");
							isOpen = true;
							if (money > 0) {
								tv_money.setVisibility(View.VISIBLE);
								tv_money_unit.setVisibility(View.VISIBLE);
								str = Utils.getMoney(money+"");
								tv_money.setText(str);
								rl_bg.setBackgroundResource(R.drawable.open_red_envelope_bg);
							} else {
								rl_bg.setBackgroundResource(R.drawable.red_envelope_ubg);
							}
						} catch (Exception e) {
						}
						break;
					default:
						ToastUtil.showLongToast(RedPacketActivity.this, common.getInfo());
						break;
				}
			}

			@Override
			public void returnException(Exception e, String msg) {
				ToastUtil.showLongToast(RedPacketActivity.this, msg);
			}
		});
	}
	
	public void finish() {
	    super.finish();  
//	    //关闭窗体动画显示  
	    overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
	}
}
