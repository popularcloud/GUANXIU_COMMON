package com.lwc.common.module.order.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.configs.BroadcastFilters;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.interf.OnTagClickCallBack;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Order;
import com.lwc.common.module.common_adapter.TagsAdapter;
import com.lwc.common.utils.EmojiUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.SpaceItemDecoration;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.Tag;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 评价页面
 *
 * @author 何栋
 * @date 2017年11月9日
 * @Copyright: lwc
 */
public class EvaluateActivity extends BaseActivity {

    protected String oid = "";

    @BindView(R.id.et_comment_content)
    EditText et_comment_content;
    @BindView(R.id.rv_tags)
    RecyclerView rv_tags;
    @BindView(R.id.tv_cha)
    TextView tv_cha;
    @BindView(R.id.tv_yiban)
    TextView tv_yiban;
    @BindView(R.id.tv_haibucuo)
    TextView tv_haibucuo;
    @BindView(R.id.tv_manyi)
    TextView tv_manyi;
    @BindView(R.id.tv_tuijin)
    TextView tv_tuijin;
    @BindView(R.id.tv_tip)
    TextView tv_tip;
    private final ArrayList<Tag> mTags = new ArrayList<>();
    private int star = 0;
    private String commentLabels;
    private ArrayList<Tag> tagHp = new ArrayList<>();
    private ArrayList<Tag> tagCp = new ArrayList<>();
    private TagsAdapter tagsAdapter;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_prise;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        setTitle("订单评价");

        rv_tags.setVisibility(View.VISIBLE);
        tagsAdapter = new TagsAdapter(this, mTags, new OnTagClickCallBack() {
            @Override
            public void onTagClickListener(Tag tag) {
                int count = 0;
                for (int i=0;i<mTags.size();i++) {
                    Tag t = mTags.get(i);
                    if(t.isChecked()) {
                        count++;
                    }
                }
                if (tag.isChecked()) {
                    tag.setChecked(false);
                } else {
                    if (count<3) {
                        tag.setChecked(true);
                    } else {
                        ToastUtil.showLongToast(EvaluateActivity.this, "最多可选择3个标签");
                        return;
                    }
                }
                for (int i=0;i<mTags.size();i++) {
                    Tag t = mTags.get(i);
                    if(t.getLabelName().equals(tag.getLabelName())) {
                        mTags.set(i, tag);
                        break;
                    }
                }
                tagsAdapter.notifyDataSetChanged();
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);

        rv_tags.setLayoutManager(gridLayoutManager);
        // rv_tags.addItemDecoration(new GridSpacingItemDecoration(3,24,true));
        rv_tags.addItemDecoration(new SpaceItemDecoration(Utils.dip2px(EvaluateActivity.this,12),0,SpaceItemDecoration.GRIDLAYOUT));
        rv_tags.setAdapter(tagsAdapter);

        getLabel();
    }

    /**
     * 获取评价
     */
    private void getLabel() {
        HttpRequestUtils.httpRequest(this, "getLabel()", RequestValue.GET_LABEL, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        ArrayList<Tag> tags = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<Tag>>() {
                        });
                        if (tags != null && tags.size() > 0) {
                            for (int i=0;i<tags.size();i++) {
                                Tag tag = tags.get(i);
                                if (tag.getLabelType() == 1) {
                                    tagHp.add(tag);
                                } else {
                                    tagCp.add(tag);
                                }
                            }
                            mTags.addAll(tagHp);
                            //mTagListView.setTags(mTags);

                            tagsAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
            }
        });
    }

    @Override
    protected void initGetData() {
        Bundle bundle = getIntent().getExtras();
        Order b = (Order) bundle.getSerializable("data");
        if (b != null) {
            oid = b.getOrderId() + "";
        }
    }

    @OnClick({R.id.btn_comment_submit, R.id.tv_cha, R.id.tv_yiban, R.id.tv_haibucuo, R.id.tv_manyi, R.id.tv_tuijin})
    public void onClick(View v) {
        int c = getResources().getColor(R.color.btn_blue_nomal);
        switch (v.getId()) {
            case R.id.btn_comment_submit:
                if (checkData()) {// 上传评价
                    if (Utils.isFastClick(3000, RequestValue.METHOD_ORDER_STATUS_SET)) {
                        return;
                    }
                    setPraise();
                }
                break;
            case R.id.tv_cha:
                setNoSelect();
                tv_cha.setCompoundDrawables(null, null, null, Utils.getDrawable(this, R.drawable.pj011));
                star = 1;
                tv_cha.setTextColor(c);
                mTags.clear();
                mTags.addAll(tagCp);
                //mTagListView.setTags(mTags);
                tagsAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_yiban:
                setNoSelect();
                tv_yiban.setCompoundDrawables(null, null, null, Utils.getDrawable(this, R.drawable.pj022));
                star = 2;
                tv_yiban.setTextColor(c);
                mTags.clear();
                mTags.addAll(tagCp);
                //mTagListView.setTags(mTags);
                tagsAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_haibucuo:
                setNoSelect();
                tv_haibucuo.setCompoundDrawables(null, null, null, Utils.getDrawable(this, R.drawable.pj033));
                star = 3;
                tv_haibucuo.setTextColor(c);
                mTags.clear();
                mTags.addAll(tagHp);
                //mTagListView.setTags(mTags);
                tagsAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_manyi:
                setNoSelect();
                tv_manyi.setCompoundDrawables(null, null, null, Utils.getDrawable(this, R.drawable.pj044));
                star = 4;
                tv_manyi.setTextColor(c);
                mTags.clear();
                mTags.addAll(tagHp);
               // mTagListView.setTags(mTags);
                tagsAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_tuijin:
                setNoSelect();
                tv_tuijin.setCompoundDrawables(null, null, null, Utils.getDrawable(this, R.drawable.pj055));
                star = 5;
                tv_tuijin.setTextColor(c);
                mTags.clear();
                mTags.addAll(tagHp);
                //mTagListView.setTags(mTags);
                tagsAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void setNoSelect() {
        int c = getResources().getColor(R.color.black_66);
        tv_cha.setTextColor(c);
        tv_yiban.setTextColor(c);
        tv_haibucuo.setTextColor(c);
        tv_manyi.setTextColor(c);
        tv_tuijin.setTextColor(c);
        tv_cha.setCompoundDrawables(null, null, null, Utils.getDrawable(this, R.drawable.pj01));
        tv_yiban.setCompoundDrawables(null, null, null, Utils.getDrawable(this, R.drawable.pj02));
        tv_haibucuo.setCompoundDrawables(null, null, null, Utils.getDrawable(this, R.drawable.pj03));
        tv_manyi.setCompoundDrawables(null, null, null, Utils.getDrawable(this, R.drawable.pj04));
        tv_tuijin.setCompoundDrawables(null, null, null, Utils.getDrawable(this, R.drawable.pj05));
    }

    @Override
    protected void widgetListener() {
        // 设置输入框文本变化监听
        et_comment_content.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editable = et_comment_content.getText().toString();
                String str = EmojiUtil.INSTANCE.filterEmoji(editable); // 过滤特殊字符
                if (!editable.equals(str)) {
                    et_comment_content.setText(str);
                }
                if (!TextUtils.isEmpty(str)) {
                    tv_tip.setText(str.length()+"/100");
                } else {
                    tv_tip.setText("0/100");
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void init() {
    }

    /**
     * 进行评价
     *
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    private void setPraise() {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", oid);
        if (!TextUtils.isEmpty(et_comment_content.getText().toString().trim())) {
            params.put("content", et_comment_content.getText().toString().trim());
        } else {
            params.put("content", "");
        }
        params.put("synthesizeGrade", star + "");
        if (!TextUtils.isEmpty(commentLabels)){
            params.put("commentLabels", commentLabels.substring(0,commentLabels.length()-1));
        } else {
            params.put("commentLabels", "");
        }

        HttpRequestUtils.httpRequest(this, "orderStatusSet", RequestValue.METHOD_ORDER_STATUS_SET, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                if (common.getStatus().equals("1")) {
                    LocalBroadcastManager.getInstance(EvaluateActivity.this).sendBroadcast(new Intent(BroadcastFilters.NOTIFI_ORDER_PRIASE_MENTION));
                    ToastUtil.showToast(EvaluateActivity.this, "评价成功！");
                    finish();
                } else {
                    ToastUtil.showToast(EvaluateActivity.this, common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showToast(EvaluateActivity.this, msg);
            }
        });
    }

    /**
     * 检查数据
     *
     * @return
     * @version 1.0
     * @createTime 2015年8月20日, 下午5:31:28
     * @updateTime 2015年8月20日, 下午5:31:28
     * @createAuthor chencong
     * @updateAuthor
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    protected boolean checkData() {
        if (star == 0) {
            ToastUtil.showToast(EvaluateActivity.this, "请为本次服务进行评分!");
            return false;
        }
        commentLabels = "";
        for (int i=0;i<mTags.size();i++) {
            Tag t = mTags.get(i);
            if(t.isChecked()) {
                commentLabels = commentLabels+t.getLabelName()+",";
            }
        }
//        if (TextUtils.isEmpty(et_comment_content.getText().toString().trim())) {
//            ToastUtil.showToast(EvaluateActivity.this, "请输入评价内容!");
//            return false;
//        }
        return true;
    }

}
