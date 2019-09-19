package com.lwc.common.module.repairs.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.adapter.MalfunctionAdapter;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Malfunction;
import com.lwc.common.module.bean.Repairs;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 选择故障类型
 * 
 * @author 何栋
 * @date 2016年4月24日
 * @Copyright: lwc
 */
public class MalfunctionSelectActivity extends BaseActivity {

	private ListView lv_skills;
	private MalfunctionAdapter adapter;
	private final List<Malfunction> skills = new ArrayList<>();
	private Repairs repairs;

	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_my_skills;
	}

	@Override
	protected void findViews() {
		lv_skills = (ListView) findViewById(R.id.lv_skills);
		adapter = new MalfunctionAdapter(MalfunctionSelectActivity.this, skills, R.layout.item_my_skill);
		lv_skills.setAdapter(adapter);
		lv_skills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("malfunction", adapter.getItem(position));
				bundle.putSerializable("repairs", repairs);
				IntentUtil.gotoActivityAndFinish(MalfunctionSelectActivity.this, ApplyForMaintainActivity.class, bundle);
			}
		});
	}

	@Override
	protected void init() {
		repairs = (Repairs)getIntent().getSerializableExtra("repairs");
		getSkillsType(repairs.getDeviceTypeId());
		setTitle("选择"+repairs.getDeviceTypeName()+"设备故障类型");
	}

	/**
	 * 获取设备故障类型
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 */
	private void getSkillsType(final String deviceTypeId) {
		HashMap<String, String> params = new HashMap<>();
		params.put("deviceTypeId", deviceTypeId);
		HttpRequestUtils.httpRequest(this, "getSkillsType", RequestValue.GET_SKILLS_TYPE, params, "GET", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String result) {
				Common common = JsonUtil.parserGsonToObject(result, Common.class);
				if (common.getStatus().equals("1")) {
					ArrayList<Malfunction> list = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(result, "data"), new TypeToken<ArrayList<Malfunction>>() {
					});
					skills.addAll(list);
					adapter.notifyDataSetChanged();
				} else {
					ToastUtil.showToast(MalfunctionSelectActivity.this, common.getInfo());
				}
			}

			@Override
			public void returnException(Exception e, String msg) {
				ToastUtil.showToast(MalfunctionSelectActivity.this, msg);
			}
		});
	}

	@Override
	protected void initGetData() {
	}

	@Override
	protected void widgetListener() {
	}
}
