package com.lwc.common.module.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.User;
import com.lwc.common.utils.DisplayUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 修改个人信息
 * @author 何栋
 * @date 2018-08-08
 */
public class UpdateUserInfoActivity extends BaseActivity {

    /**
     * 修改类型：4姓名5填写邀请码
     */
    private int type;
    @BindView(R.id.txt_mention_1)
    TextView txt_mention_1;
    @BindView(R.id.et_mention_1)
    EditText et_mention_1;
    @BindView(R.id.btn_comment_submit)
    Button btn_comment_submit;
    SharedPreferencesUtils preferencesUtils;
    private User user = null;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_update_user_info;
    }

    /**
     * 初始化布局组件
     */
    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        btn_comment_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkData()){
                    switch (type) {
                        case 4: //修改昵称
                            if (Utils.isFastClick(3000, RequestValue.UP_USER_INFOR)) {
                                return;
                            }

                            String nickName = et_mention_1.getText().toString().trim();
                            String regEx = "^[\\u4E00-\\u9FA5A-Za-z0-9_]{2,8}$";
                            // 编译正则表达式
                            Pattern pattern = Pattern.compile(regEx);
                            Matcher matcher = pattern.matcher(nickName);
                            // 字符串是否与正则表达式相匹配
                            boolean rs = matcher.matches();

                            if(rs){
                                upUserInfor(nickName);
                            }else{
                                ToastUtil.showLongToast(UpdateUserInfoActivity.this, "请输入2到8个字的昵称！");
                            }

                            break;
                        case 5://填写邀请码
                            if (Utils.isFastClick(3000, RequestValue.UP_USER_INFOR)) {
                                return;
                            }
                            upUserInfor(et_mention_1.getText().toString().trim());
                            break;
                    }
                }
            }
        });
        et_mention_1.setVisibility(View.VISIBLE);
    }

    /**
     * 点击返回按钮回调方法
     *
     * @param view
     */
    public void onBack(View view) {
        onBackPressed();
    }

    /**
     * 初始化基础数据
     */
    @Override
    protected void init() {
        type = getIntent().getIntExtra("type", 0);
        preferencesUtils = SharedPreferencesUtils.getInstance(UpdateUserInfoActivity.this);
        user = preferencesUtils.loadObjectData(User.class);
        switch (type) {
            case 4:
                setTitle("修改昵称");
                txt_mention_1.setText("2-8个字符，可由中英文，数字组成");
                if (!TextUtils.isEmpty(user.getUserRealname())) {
                    et_mention_1.setText(user.getUserRealname());
                    et_mention_1.setSelection(user.getUserRealname().length());
                } else {
                    et_mention_1.setHint("请输入您的昵称");
                }
                et_mention_1.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                break;
            case 5:
                setTitle("填写邀请码");
                txt_mention_1.setText("邀请码由6位数字组成");
                et_mention_1.setHint("请填写您的邀请码");
                et_mention_1.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initGetData() {
    }

    @Override
    protected void widgetListener() {
    }

    /**
     * 更新用户信息
     *
     * @param remark
     */
    private void upUserInfor(String remark) {
        Map<String, String> map = new HashMap<>();
     if (type == 5 && !TextUtils.isEmpty(remark)) {
            map.put("inviteCode", remark);
        } else if (type == 4 && !TextUtils.isEmpty(remark)) {
            map.put("userRealname", remark);
        }

        HttpRequestUtils.httpRequest(this, "修改用户信息", RequestValue.UP_USER_INFOR, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        ToastUtil.showLongToast(UpdateUserInfoActivity.this, common.getInfo());
                        DisplayUtil.showInput(false, UpdateUserInfoActivity.this);
                        finish();
                        break;
                    default:
                        ToastUtil.showLongToast(UpdateUserInfoActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                if (msg != null && !msg.equals("")) {
                    ToastUtil.showLongToast(UpdateUserInfoActivity.this, msg);
                } else {
                    ToastUtil.showLongToast(UpdateUserInfoActivity.this, "请求失败，请稍候重试!");
                }
            }
        });
    }

    /**
     * 检查数据是否正确
     *
     * @return
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    private boolean checkData() {
        String trim = et_mention_1.getText().toString().trim();
        switch (type) {
            case 4:// 昵称
                if (TextUtils.isEmpty(trim)) {
                    ToastUtil.showToast(UpdateUserInfoActivity.this, "请输入您的昵称！");
                    return false;
                }
                return true;
            case 5:// 邀请码
                if (TextUtils.isEmpty(trim)) {
                    ToastUtil.showToast(UpdateUserInfoActivity.this, "请输入您的邀请码！");
                    return false;
                }
                return true;
            default:
        }
        return true;
    }
}
