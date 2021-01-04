package com.lwc.common.module.message.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;

import butterknife.BindView;

public class SystemMsgActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_content)
    TextView tv_content;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_system_msg;
    }

    @Override
    protected void findViews() {

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String time = intent.getStringExtra("time");

        setTitle("系统消息");

        tv_title.setText(title);
        tv_time.setText(time);
        tv_content.setText(content);
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
}
