package com.lwc.common.module.setting.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.utils.DisplayUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.SystemUtil;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

import java.util.HashMap;

/**
 * 意见反馈页面
 *
 * @author 何栋
 * @Description TODO
 * @date 2015年11月9日
 * @Copyright: lwc
 */
public class SuggestActivity extends BaseActivity {

    /**
     * 反馈内容
     */
    private EditText et_comment_content;
    /**
     * 提交反馈
     */
    private Button btn_comment_submit;

    private TextView tv_word_number;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_suggest;
    }

    @Override
    protected void findViews() {
        setTitle("意见反馈");
        et_comment_content = (EditText) findViewById(R.id.et_comment_content);
        btn_comment_submit = (Button) findViewById(R.id.btn_comment_submit);
        tv_word_number = (TextView) findViewById(R.id.tv_word_number);

        et_comment_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_word_number.setText(String.valueOf(s.length())+"/200");
            }
        });
    }

    @Override
    protected void initGetData() {
    }

    @Override
    protected void widgetListener() {
        btn_comment_submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkData()) {// 上传反馈
                    if (Utils.isFastClick(3000, RequestValue.METHOD_SUGGEST)) {
                        return;
                    }
                    setSuggest();
                }

            }
        });
    }

    @Override
    protected void init() {
    }

    /**
     * 提交反馈
     *
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    private void setSuggest() {
        HashMap<String, String> params = new HashMap<>();
        params.put("device", "1");
        params.put("version", SystemUtil.getCurrentVersionName());
        params.put("content", et_comment_content.getText().toString().trim());
        HttpRequestUtils.httpRequest(this, "sendSuggest", RequestValue.METHOD_SUGGEST, params, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                DisplayUtil.showInput(false, SuggestActivity.this);
                finish();
                ToastUtil.showToast(SuggestActivity.this, "提交成功！");
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showToast(SuggestActivity.this, msg);
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
        if (TextUtils.isEmpty(et_comment_content.getText().toString().trim())) {
            ToastUtil.showToast(SuggestActivity.this, "请输入意见内容!");
            return false;
        }
        return true;
    }

}
