package com.lwc.common.module.nearby.ui;

import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.adapter.MySkillsAdapter;
import com.lwc.common.bean.MySkillBean;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 维修员擅长技能列表
 * 
 * @Description TODO
 * @author 何栋
 * @date 2016年4月24日
 * @Copyright: lwc
 */
public class MySkillsActivity extends BaseActivity {

	private ListView lv_skills;
	private MySkillsAdapter adapter;
	private final List<MySkillBean> skills = new ArrayList<>();

	@Override
	protected int getContentViewId(Bundle savedInstanceState) {
		return R.layout.activity_my_skills;
	}

	@Override
	protected void findViews() {
		setTitle("工程师技能");
		lv_skills = (ListView) findViewById(R.id.lv_skills);
		adapter = new MySkillsAdapter(MySkillsActivity.this, skills, R.layout.item_my_skill);
		lv_skills.setAdapter(adapter);
	}

	@Override
	protected void init() {
		String userId = getIntent().getStringExtra("userId");
		getMySkills(userId);
	}

	/**
	 * 获取我的技能
	 * 
	 * 
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 */
	private void getMySkills(final String userId) {
		HashMap<String, String> params = new HashMap<>();
		params.put("userId", userId);
		HttpRequestUtils.httpRequest(this, "getMySkills", RequestValue.METHOD_GET_MY_SKILLS, params, "GET", new HttpRequestUtils.ResponseListener() {
			@Override
			public void getResponseData(String result) {
				Common common = JsonUtil.parserGsonToObject(result, Common.class);
				if (common.getStatus().equals("1")) {
					ArrayList<MySkillBean> current = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(result, "data"), new TypeToken<ArrayList<MySkillBean>>() {
					});
					skills.addAll(current);
					adapter.notifyDataSetChanged();
				} else {
					ToastUtil.showToast(MySkillsActivity.this, common.getInfo());
				}
			}

			@Override
			public void returnException(Exception e, String msg) {
				ToastUtil.showToast(MySkillsActivity.this, msg);
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
