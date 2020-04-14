package com.lwc.common.module.repairs.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gcssloop.widget.PagerGridLayoutManager;
import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.adapter.MalfunctionAdapter;
import com.lwc.common.adapter.RepairTypeAdapter;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Malfunction;
import com.lwc.common.module.bean.Repairs;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 选择维修范围
 */
public class RepairsTypeActivity extends BaseActivity {

	@BindView(R.id.rv_repair_type_01)
	RecyclerView rv_repair_type_01;
	@BindView(R.id.rv_repair_type_02)
	RecyclerView rv_repair_type_02;

	List<Repairs> repairsList01 = new ArrayList<>();
	List<Repairs> repairsList02 = new ArrayList<>();
	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_repairs_type;
	}

	@Override
	protected void findViews() {
	}


	public void getARepairType(){
		HttpRequestUtils.httpRequest(this, "获取所有维修类型devicemold==1", RequestValue.MALFUNCTION+"?deviceMold="+ 1 + "&clientType=person", null, "GET", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String response) {
				Common common = JsonUtil.parserGsonToObject(response, Common.class);
				switch (common.getStatus()) {
					case "1":
						repairsList01 = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<Repairs>>(){});

						if(repairsList01.size() > 0){

							int rows = (repairsList01.size()-1)/5 + 1;

							// 布局管理器
							PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(rows, 5, PagerGridLayoutManager.HORIZONTAL);
							rv_repair_type_01.setLayoutManager(layoutManager);

							//设置适配器
							RepairTypeAdapter repairTypeAdapter = new RepairTypeAdapter(RepairsTypeActivity.this,repairsList01);
							rv_repair_type_01.setAdapter(repairTypeAdapter);
						}
						break;
					default:
						LLog.i("获取所有维修类型：" + common.getInfo());
						break;
				}
			}

			@Override
			public void returnException(Exception e, String msg) {
				LLog.eNetError(e.toString());
			}
		});
	}

	public void getARepairType2(){
		HttpRequestUtils.httpRequest(this, "获取所有维修类型devicemold==3", RequestValue.MALFUNCTION+"?deviceMold="+ 3 + "&clientType=person", null, "GET", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String response) {
				Common common = JsonUtil.parserGsonToObject(response, Common.class);
				switch (common.getStatus()) {
					case "1":
						repairsList02 = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<Repairs>>(){});
						if(repairsList02.size() > 0){

							int rows = (repairsList02.size()-1)/5 + 1;

							// 布局管理器
							PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(rows, 5, PagerGridLayoutManager.HORIZONTAL);
							rv_repair_type_02.setLayoutManager(layoutManager);

							//设置适配器
							RepairTypeAdapter repairTypeAdapter = new RepairTypeAdapter(RepairsTypeActivity.this,repairsList02);
							rv_repair_type_02.setAdapter(repairTypeAdapter);
						}
						break;
					default:
						LLog.i("获取所有维修类型：" + common.getInfo());
						break;
				}
			}

			@Override
			public void returnException(Exception e, String msg) {
				LLog.eNetError(e.toString());
			}
		});
	}

	@Override
	protected void init() {
		setTitle("维修范围");
		getARepairType();
		getARepairType2();
	}

	@Override
	protected void initGetData() {
	}

	@Override
	protected void widgetListener() {
	}
}
