package com.lwc.common.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lwc.common.R;
import com.lwc.common.module.order.ui.activity.PayActivity;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.module.wallet.ui.WithdrawPayActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "WXPayEntryActivity";
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);
		api = WXAPIFactory.createWXAPI(this, WXContants.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == 0) {
				ToastUtil.showLongToast(WXPayEntryActivity.this, "支付成功");
				try {
					if (WithdrawPayActivity.activity != null) {
						WithdrawPayActivity.activity.finish();
					}
					if (PayActivity.activity != null) {
						PayActivity.activity.setResult(RESULT_OK);
						PayActivity.activity.finish();
					}
				} catch (Exception e){}
				finish();
				return;
			} else if (resp.errCode == -2){
				ToastUtil.showLongToast(WXPayEntryActivity.this, "您已取消支付");
			} else if (resp.errCode == -1){
				ToastUtil.showLongToast(WXPayEntryActivity.this, "支付失败");
			}
			if (WithdrawPayActivity.activity != null) {
				WithdrawPayActivity.activity.payFailure();
			}
			if (PayActivity.activity != null){
				PayActivity.activity.finish();
			}
			finish();
		}
	}
}