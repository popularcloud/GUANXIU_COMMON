package com.lwc.common.module.wallet.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.adapter.TradingRecordAdapter;
import com.lwc.common.bean.TradingRecordBean;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.User;
import com.lwc.common.utils.BGARefreshLayoutUtils;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.yanzhenjie.sofia.Sofia;

import org.byteam.superadapter.OnItemClickListener;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author 何栋
 * @Dete 2017-11-28
 * 我的钱包
 */
public class WalletActivity extends BaseActivity {

	@BindView(R.id.FMoneyTV)
	TextView FMoneyTV;
	@BindView(R.id.tv_msg)
	TextView tv_msg;
	@BindView(R.id.recyclerView)
	RecyclerView recyclerView;
	@BindView(R.id.mBGARefreshLayout)
	BGARefreshLayout mBGARefreshLayout;
	@BindView(R.id.ll_title)
	RelativeLayout ll_title;
	ArrayList<TradingRecordBean> list = new ArrayList<>();
	private User user;
	private TradingRecordAdapter adapter;
	private SharedPreferencesUtils preferencesUtils;
	private int curPage=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BGARefreshLayoutUtils.initRefreshLayout(this, mBGARefreshLayout);
		init();
	}

	public void onBack(View view) {
		onBackPressed();
	}

	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_wallet;
	}

	@Override
	protected void findViews() {
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_title.getLayoutParams();
		layoutParams.setMargins(0, Utils.getStatusBarHeight(WalletActivity.this),0,0);
		ll_title.setLayoutParams(layoutParams);
	}

	@OnClick({R.id.txtPay, R.id.txtWithdraw, R.id.txtManage})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtWithdraw:
			Bundle bundle = new Bundle();
			bundle.putString("money", user.getBanlance());
			IntentUtil.gotoActivity(WalletActivity.this, WithdrawDepositActivity.class, bundle);
			break;
		case R.id.txtPay:
			IntentUtil.gotoActivity(WalletActivity.this, WithdrawPayActivity.class);
			break;
		case R.id.txtManage:
			IntentUtil.gotoActivity(WalletActivity.this, PaySettingActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getUserInfor();
		curPage = 1;
	}

	@Override
	protected void initStatusBar() {
		//super.initStatusBar();
		Sofia.with(this)
				.statusBarBackground(Color.parseColor("#00000000"))
				.invasionStatusBar();
	}

	private void getHistory() {
		Map<String, String> map = new HashMap<>();
		map.put("curPage", curPage+"");
		HttpRequestUtils.httpRequest(WalletActivity.this, "getHistory", RequestValue.GET_WALLET_HISTORY, map, "GET", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String response) {
				mBGARefreshLayout.endRefreshing();
				mBGARefreshLayout.endLoadingMore();
				Common common = JsonUtil.parserGsonToObject(response, Common.class);
				switch (common.getStatus()) {
					case "1":
						try {
							JSONObject object = new JSONObject(JsonUtil.getGsonValueByKey(response, "data"));
							if (object != null) {
								ArrayList<TradingRecordBean> listBean = JsonUtil.parserGsonToArray(object.optString("data"), new TypeToken<ArrayList<TradingRecordBean>>() {
								});
								if (listBean != null && listBean.size() > 0) {
									if (curPage == 1) {
										list = listBean;
									} else if (curPage > 1) {
										list.addAll(listBean);
									}
									tv_msg.setVisibility(View.GONE);
									adapter.replaceAll(list);
								} else {
									if (curPage > 1) {
										curPage--;
										ToastUtil.showToast(WalletActivity.this, "我是有底线的，已无更多记录！");
									} else if (curPage == 1) {
										tv_msg.setVisibility(View.VISIBLE);
									}
								}
							}
							break;
						} catch (Exception e) {
						}
					default:
						ToastUtil.showLongToast(WalletActivity.this, common.getInfo());
						LLog.i("getHistory" + common.getInfo());
						break;
				}

			}
			@Override
			public void returnException(Exception e, String msg) {
				LLog.eNetError(e.toString());
				mBGARefreshLayout.endRefreshing();
				mBGARefreshLayout.endLoadingMore();
				ToastUtil.showLongToast(WalletActivity.this, msg);
			}
		});
	}

	/**
	 * 获取个人信息
	 */
	private void getUserInfor() {
		HttpRequestUtils.httpRequest(WalletActivity.this, "getUserInfor", RequestValue.USER_INFO, null, "GET", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String response) {
				getHistory();
				Common common = JsonUtil.parserGsonToObject(response, Common.class);
				switch (common.getStatus()) {
					case "1":
						user = preferencesUtils.loadObjectData(User.class);
						String userRole = preferencesUtils.loadString("user_role");
						String id = "";
						if (user != null && user.getRoleId() != null) {
							id=user.getRoleId();
						} else  if (!TextUtils.isEmpty(userRole)){
							id = userRole;
						}
						user = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), User.class);
						if(user != null) {
							user.setRoleId(id);
							preferencesUtils.saveObjectData(user);
							FMoneyTV.setText(Utils.getMoney(user.getBanlance()));
						}
						break;
					default:
						break;
				}
			}

			@Override
			public void returnException(Exception e, String msg) {
				LLog.eNetError(e.toString());
			}
		});
	}

	protected void init() {
		preferencesUtils = SharedPreferencesUtils.getInstance(this);
		user = preferencesUtils.loadObjectData(User.class);
		if (adapter == null) {
			recyclerView.setLayoutManager(new LinearLayoutManager(this));
			adapter = new TradingRecordAdapter(this, list, R.layout.item_trading_record);
			adapter.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(View itemView, int viewType, int position) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("tradingRecordBean", adapter.getItem(position));
					IntentUtil.gotoActivity(WalletActivity.this, WalletDetailsActivity.class, bundle);
				}
			});
			recyclerView.setAdapter(adapter);
		}

		mBGARefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
			@Override
			public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
				curPage = 1;
				getHistory();
			}

			@Override
			public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
				curPage++;
				getHistory();
				return true;
			}
		});
		FMoneyTV.setText(user.getBanlance());
	}

	@Override
	protected void initGetData() {

	}

	@Override
	protected void widgetListener() {

	}


}
