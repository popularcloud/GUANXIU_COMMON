package com.lwc.common.module.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.AuthenticationInfo;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.User;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.widget.CustomDialog;
import com.lwc.common.widget.PhotoBigFrameDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 查询审核信息
 */
public class AttestationInfoActivity extends BaseActivity {

    @BindView(R.id.tv_wc)
    TextView tv_wc;
    @BindView(R.id.ll_content)
    LinearLayout ll_content;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.imgAdd1)
    ImageView imgAdd1;
    @BindView(R.id.imgAdd2)
    ImageView imgAdd2;
    @BindView(R.id.tv_sytime)
    TextView tv_sytime;
    @BindView(R.id.tv_shz)
    TextView tv_shz;
    @BindView(R.id.tv_defeated)
    TextView tv_defeated;
    @BindView(R.id.btnRevocation)
    Button btnRevocation;

    private SharedPreferencesUtils preferencesUtils;
    private ImageLoaderUtil imageLoaderUtil;
    private int second;
    private String imgPath1;
    private String imgPath2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_attestation_info;
    }

    @Override
    protected void findViews() {
        setTitle("机关单位认证");
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initGetData() {
        imageLoaderUtil = ImageLoaderUtil.getInstance();
        preferencesUtils = SharedPreferencesUtils.getInstance(AttestationInfoActivity.this);
        getUserInfor();
    }

    /**
     * 获取个人信息
     */
    private void getUserInfor() {
        HttpRequestUtils.httpRequest(this, "查询用户认证信息", RequestValue.GET_USER_AUTH_INFO, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        User user = preferencesUtils.loadObjectData(User.class);
                        AuthenticationInfo ai = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), AuthenticationInfo.class);
                        if (ai == null) {
                            DialogUtil.showUpdateAppDg(AttestationInfoActivity.this, "温馨提示", "确定", "老用户默认为已认证用户，因您没有提交审核信息，所以没有审核信息查看！", new CustomDialog.OnClickListener() {

                                @Override
                                public void onClick(CustomDialog dialog, int id, Object object) {
                                    onBackPressed();
                                    dialog.dismiss();
                                }
                            });
                            return;
                        }
                        if (!TextUtils.isEmpty(ai.getAuditStatus())) {
                            user.setIsSecrecy(ai.getAuditStatus());
                        }
                        ai.setUserId(user.getUserId());
                        preferencesUtils.saveObjectData(user);
                        preferencesUtils.saveObjectData(ai);
                        updateView(ai);
                        break;
                    default:
                        ToastUtil.showLongToast(AttestationInfoActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                if (msg != null && !msg.equals("")) {
                    ToastUtil.showLongToast(AttestationInfoActivity.this, msg);
                } else {
                    LLog.eNetError(e.toString());
                    ToastUtil.showNetErr(AttestationInfoActivity.this);
                }
            }
        });
    }

    public void updateView(AuthenticationInfo ai) {
        if (ai != null)
        {
            if (!TextUtils.isEmpty(ai.getCreateTime())) {
                tv_time.setText(ai.getCreateTime()+" 提交审核");
            }
            if (!TextUtils.isEmpty(ai.getApplicantName())) {
                txtName.setText(ai.getApplicantName());
            }

            if (!TextUtils.isEmpty(ai.getCityName())
                    && !TextUtils.isEmpty(ai.getProvinceName())
                    && !TextUtils.isEmpty(ai.getTownName())
                    && !TextUtils.isEmpty(ai.getDetailedAddress())) {

                String tx = ai.getCityName() + ai.getCityName() + ai.getTownName() + ai.getDetailedAddress();
                txtAddress.setText(tx);
            }

            if (!TextUtils.isEmpty(ai.getOrganizationName())) {
                tvCompanyName.setText(ai.getOrganizationName());
            }

            if (!TextUtils.isEmpty(ai.getApplicantImages())) {
                String[] arr = ai.getApplicantImages().split(",");
                if (arr.length > 0) {
                    imgPath1 = arr[0];
                    if (!TextUtils.isEmpty(imgPath1)) {
                        imageLoaderUtil.displayFromNet(AttestationInfoActivity.this, imgPath1, imgAdd1);
                    }
                    if (arr.length > 1) {
                        imgPath2 = arr[1];
                        if (!TextUtils.isEmpty(imgPath2)) {
                            imageLoaderUtil.displayFromNet(AttestationInfoActivity.this, imgPath2, imgAdd2);
                        }
                    }
                }
            }
            if (ai.getAuditStatus().equals("1")) {
                if (ai.getSecond() > 0) {
                    second = ai.getSecond();
                    tv_sytime.setText("剩余审核时间：" + Utils.getTime(second));
                    handle.sendEmptyMessageDelayed(0, 1000);
                } else {
                    tv_sytime.setText("审核超时，您可以在APP中拨打客服电话反馈！");
                }
                btnRevocation.setVisibility(View.VISIBLE);
            } else if (ai.getAuditStatus().equals("2")) {
                tv_sytime.setText(ai.getPassTime()+" 已通过审核");
                tv_sytime.setCompoundDrawables(Utils.getDrawable(AttestationInfoActivity.this, R.drawable.identitychange_succed), null, null, null);
                btnRevocation.setVisibility(View.GONE);
                tv_wc.setCompoundDrawables(null, Utils.getDrawable(this, R.drawable.identitychange_5), null, null);
            } else if (ai.getAuditStatus().equals("3")) {
                btnRevocation.setVisibility(View.VISIBLE);
                tv_sytime.setText("审核失败");
                tv_sytime.setTextColor(getResources().getColor(R.color.red_money));
                tv_sytime.setCompoundDrawables(Utils.getDrawable(AttestationInfoActivity.this, R.drawable.identitychange_fail), null, null, null);
                tv_wc.setText("审核失败");
                if (!TextUtils.isEmpty(ai.getRemark())){
                    tv_defeated.setText(ai.getRemark());
                } else {
                    tv_defeated.setText("");
                }
                btnRevocation.setText("重新填写资料");
                tv_shz.setCompoundDrawables(null, Utils.getDrawable(this, R.drawable.identitychange_8), null, null);
                tv_wc.setCompoundDrawables(null, Utils.getDrawable(this, R.drawable.identitychange_7), null, null);
            }

        }
    }

    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            second = second - 1;
            if (second > 0){
                tv_sytime.setText("剩余审核时间：" + Utils.getTime(second));
                handle.sendEmptyMessageDelayed(0, 1000);
            } else {
                tv_sytime.setText("超时未审核，请联系客服：400-881-0769！");
                tv_sytime.setTextColor(getResources().getColor(R.color.red_money));
                tv_sytime.setCompoundDrawables(Utils.getDrawable(AttestationInfoActivity.this, R.drawable.identitychange_timeout), null, null, null);
            }
        }
    };

    @Override
    protected void widgetListener() {
    }

    @OnClick({R.id.btnRevocation, R.id.imgAdd1, R.id.imgAdd2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnRevocation:
                //TODO
                if (btnRevocation.getText().toString().trim().equals("重新填写资料")) {
                    IntentUtil.gotoActivity(AttestationInfoActivity.this, PerfectionUserInfoActivity.class);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    revocation();
                }
                break;
            case R.id.imgAdd1:
                if (!TextUtils.isEmpty(imgPath1)) {
                    showPhotoBigFrameDialog(imgPath1);
                }
                break;
            case R.id.imgAdd2:
                if (!TextUtils.isEmpty(imgPath2)) {
                    showPhotoBigFrameDialog(imgPath2);
                }
                break;
        }
    }

    private void revocation() {
        HttpRequestUtils.httpRequest(this, "revocation撤销审核", RequestValue.GET_USER_REPEAL_AUTH, null, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        Intent intent = new Intent(MainActivity.KEY_UPDATE_USER);
                        LocalBroadcastManager.getInstance(AttestationInfoActivity.this).sendBroadcast(intent);
                        ToastUtil.showToast(AttestationInfoActivity.this, "审核已撤销");
                        onBackPressed();
                        break;
                    default:
                        ToastUtil.showToast(AttestationInfoActivity.this, common.getInfo());
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                ToastUtil.showToast(AttestationInfoActivity.this, msg);
            }
        });
    }

    private void showPhotoBigFrameDialog(String url) {
        PhotoBigFrameDialog frameDialog = new PhotoBigFrameDialog(this, url);
        frameDialog.showNoticeDialog();
    }
    public void finish() {
        super.finish();
//	    //关闭窗体动画显示
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
