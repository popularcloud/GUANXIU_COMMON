package com.lwc.common.module.mine;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.configs.ServerConfig;
import com.lwc.common.controler.global.GlobalValue;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.PhotoToken;
import com.lwc.common.module.bean.User;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.FileUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.ImageLoaderUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.PictureUtils;
import com.lwc.common.utils.QiniuUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.SystemInvokeUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.MyTextView;
import com.lwc.common.widget.CircleImageView;
import com.lwc.common.widget.SelectPhotoDialog;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户个人信息页面
 *
 * @author 何栋
 * @date 2015年11月9日
 * @Copyright: lwc
 */
public class UserInfoActivity extends BaseActivity implements OnClickListener {

    @BindView(R.id.imgHeadIcon)
    CircleImageView imgHeadIcon;
    @BindView(R.id.txtId)
    TextView txtId;
    @BindView(R.id.txtAccounts)
    TextView txtAccounts;
    @BindView(R.id.txtSex)
    TextView txtSex;
    @BindView(R.id.txtIde)
    TextView txtIde;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtCompany)
    TextView txtCompany;
    @BindView(R.id.txtSign)
    TextView txtSign;
    @BindView(R.id.rLayoutCompany)
    RelativeLayout rLayoutCompany;
    @BindView(R.id.rLayoutName)
    RelativeLayout rLayoutName;
    @BindView(R.id.txtActionbarTitle)
    MyTextView txtActionbarTitle;
    @BindView(R.id.rlBindCompany)
    RelativeLayout rlBindCompany;
    @BindView(R.id.txt_bind_company)
    TextView txtBindCompany;
    @BindView(R.id.rLayoutInvite)
    RelativeLayout rLayoutInvite;
    @BindView(R.id.img_invite)
    ImageView img_invite;
    @BindView(R.id.txtInvite)
    TextView txtInvite;

    private final int REQUEST_CODE_UPDATE = 101;
    private SharedPreferencesUtils preferencesUtils;
    private User user;
    private File iconFile;//接收选择相册和拍照返回的图片文件
    private ImageLoaderUtil imageLoaderUtil;//图片加载对象
    private ProgressDialog pd;//图片上传中提示框
    private PhotoToken token;//上传图片到七牛的token
    private Dialog dialog;//选择相册弹框
    private Uri uritempFile;//接收选择相册和拍照返回的图片文件

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_user_info;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        pd = new ProgressDialog(this);
        pd.setMessage("正在上传图片...");
        pd.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfor();
    }

    @Override
    public void init() {
        txtActionbarTitle.setText("个人资料");
    }

    @Override
    protected void initGetData() {
        preferencesUtils = SharedPreferencesUtils.getInstance(UserInfoActivity.this);
        imageLoaderUtil = ImageLoaderUtil.getInstance();
        user = preferencesUtils.loadObjectData(User.class);
    }

    @Override
    protected void widgetListener() {
    }

    /**
     * 初始化性别提示框
     */
    private void showSexPopupWindos() {
        SelectPhotoDialog picturePopupWindow = new SelectPhotoDialog(this, new SelectPhotoDialog.CallBack() {
            @Override
            public void oneClick() {
                upUserInfor("0", null);
            }
            @Override
            public void twoClick() {
                upUserInfor("1", null);
            }
            @Override
            public void cancelCallback() {
            }
        }, "女", "男");
        picturePopupWindow.show();
    }

    /**
     * 初始化拍照提示框
     */
    private void showTakePopupWindow() {
        ToastUtil.showPhotoSelect(this);
        getToken(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalValue.REPAIRS_PHOTO_REQUESTCODE0:
                    iconFile = PictureUtils.getFile(UserInfoActivity.this, data);
                    SystemInvokeUtils.startImageZoom(this, Uri.fromFile(iconFile), uritempFile, GlobalValue.MODIFY_PHOTO_REQUESTCODE);
                    break;
                case GlobalValue.REPAIRS_CAMERA_REQUESTCODE0:
                    //上传图片
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    if (bitmap == null) {
                        return;
                    }
                    try {
                        iconFile = FileUtil.saveMyBitmap(bitmap);
                        SystemInvokeUtils.startImageZoom(this, Uri.fromFile(iconFile), uritempFile, GlobalValue.MODIFY_PHOTO_REQUESTCODE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case GlobalValue.MODIFY_PHOTO_REQUESTCODE:
                    //获取到裁剪后的图像
                    try {
                        Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                        iconFile = FileUtil.saveMyBitmap(bm);
                        uploadPhoto(iconFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case REQUEST_CODE_UPDATE:
                    getUserInfor();
                    break;
            }
        }
    }

    /**
     * 更新用户信息
     *
     * @param sex
     */
    private void upUserInfor(String sex, String head) {
        Map<String, String> map = new HashMap<>();
        if (sex != null && !sex.equals(""))
            map.put("sex", sex);
        if (!TextUtils.isEmpty(head)) {
            map.put("headImage", head);
        }
        HttpRequestUtils.httpRequest(this, "修改用户信息", RequestValue.UP_USER_INFOR, map, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        getUserInfor();
                        break;
                    default:
                        ToastUtil.showLongToast(UserInfoActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                if (msg != null && !msg.equals("")) {
                    ToastUtil.showLongToast(UserInfoActivity.this, msg);
                } else {
                    ToastUtil.showLongToast(UserInfoActivity.this, "请求失败，请稍候重试!");
                }
            }
        });
    }

    /**
     * 获取个人信息
     */
    private void getUserInfor() {
        HttpRequestUtils.httpRequest(this, "getUserInfor", RequestValue.USER_INFO, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
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
                        user.setRoleId(id);
                        preferencesUtils.saveObjectData(user);
                        setUserInfor(user);
                        break;
                    default:
                        ToastUtil.showLongToast(UserInfoActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                if (msg != null && !msg.equals("")) {
                    ToastUtil.showLongToast(UserInfoActivity.this, msg);
                } else {
                    LLog.eNetError(e.toString());
                    ToastUtil.showNetErr(UserInfoActivity.this);
                }
            }
        });
    }

    /**
     * 填充用户信息到页面
     * @param user 用户信息对象
     */
    @UiThread
    public void setUserInfor(User user) {
        txtId.setText(user.getUserId());

        if (!TextUtils.isEmpty(user.getRoleId()) && user.getRoleId().equals("5") && !TextUtils.isEmpty(user.getParentCompanyId()) && !TextUtils.isEmpty(user.getParentCompanyName())) {
            rlBindCompany.setVisibility(View.VISIBLE);
            txtBindCompany.setText(user.getParentCompanyName());
        } else {
            rlBindCompany.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(user.getRoleId()) && user.getRoleId().equals("5")) {
            txtIde.setText("用户");
            rLayoutCompany.setVisibility(View.GONE);
            rLayoutName.setVisibility(View.GONE);
            rLayoutInvite.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(user.getInviteCode())) {
                txtInvite.setText(user.getInviteCode());
                img_invite.setVisibility(View.INVISIBLE);
            }
        } else {
            rLayoutInvite.setVisibility(View.GONE);
            if (user.getIsSecrecy().equals("2")) {
                txtIde.setText("认证用户");
            } else {
                txtIde.setText("用户");
            }
        }
        //设置性别
        if (!TextUtils.isEmpty(user.getUserSex()) && "1".equals(user.getUserSex())) {
            txtSex.setText("男");
        } else if (!TextUtils.isEmpty(user.getUserSex()) && "0".equals(user.getUserSex())) {
            txtSex.setText("女");
        } else {
            txtSex.setText("其他");
        }
        if (!TextUtils.isEmpty(user.getUserRealname())) {
            txtName.setText(user.getUserRealname());
            rLayoutName.setVisibility(View.VISIBLE);
        } else {
            txtName.setText("暂无");
            rLayoutName.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(user.getUserPhone())) {
            txtAccounts.setText(user.getUserPhone());
        } else if (!TextUtils.isEmpty(user.getUserName())) {
            txtAccounts.setText(user.getUserName());
        } else {
            txtAccounts.setText("暂无");
        }

        if (!TextUtils.isEmpty(user.getUserCompanyName())) {
            txtCompany.setText(user.getUserCompanyName());
        } else {
            txtCompany.setText("暂无");
        }

        if (!TextUtils.isEmpty(user.getUserSignature())) {
            txtSign.setText(user.getUserSignature());
        } else {
            txtSign.setText("暂无");
        }
        if (!TextUtils.isEmpty(user.getUserHeadImage())) {
            imageLoaderUtil.displayFromNet(this, user.getUserHeadImage(), imgHeadIcon);
        }
    }

    @OnClick({R.id.rLayoutInvite, R.id.rLayoutSex, R.id.rLayoutSign, R.id.rLayoutChangePwd, R.id.rl_head, R.id.rlBindCompany,R.id.rLayoutName})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rLayoutInvite:
                if (!TextUtils.isEmpty(user.getInviteCode())) {
                    break;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("type", 5);
                IntentUtil.gotoActivityForResult(UserInfoActivity.this, UpdateUserInfoActivity.class, bundle, REQUEST_CODE_UPDATE);
                break;
            case R.id.rLayoutSex:
                showSexPopupWindos();
                break;
            case R.id.rLayoutSign:
                Bundle bundle3 = new Bundle();
                bundle3.putInt("type", 3);
                if(!TextUtils.isEmpty(user.getUserSignature())){
                    bundle3.putString("signature", user.getUserSignature());
                }
                IntentUtil.gotoActivityForResult(UserInfoActivity.this, UpdateSignActivity.class, bundle3, REQUEST_CODE_UPDATE);

                break;
            case R.id.rLayoutName:
                Bundle bundle4 = new Bundle();
                bundle4.putInt("type", 4);
                IntentUtil.gotoActivityForResult(UserInfoActivity.this, UpdateUserInfoActivity.class, bundle4, REQUEST_CODE_UPDATE);
                break;
            case R.id.rLayoutChangePwd:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("type", 0);
                IntentUtil.gotoActivityForResult(UserInfoActivity.this, UpdatePassWordActivity.class, bundle1, REQUEST_CODE_UPDATE);
                break;
            case R.id.rl_head:
                showTakePopupWindow();
                break;
            case R.id.rlBindCompany:
                String msg = "您将解除与"+user.getParentCompanyName()+"的专属绑定，点击确认后，您报修的订单可由平台所有公司接单！";
                dialog = DialogUtil.dialog(UserInfoActivity.this, "温馨提示", "确认", "取消",msg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRelieveBind();
                        dialog.dismiss();
                    }
                }, null, false);
                break;
        }
    }

    /**
     * 解除专属维修公司绑定
     */
    private void getRelieveBind() {
        HttpRequestUtils.httpRequest(this, "getRelieveBind", RequestValue.RELIEVE_BIND, null, "POST", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        user.setParentCompanyName("");
                        user.setParentCompanyId("");
                        preferencesUtils.saveObjectData(user);
                        rlBindCompany.setVisibility(View.GONE);
                        ToastUtil.showLongToast(UserInfoActivity.this, "您已成功解除绑定！");
                        break;
                    default:
                        ToastUtil.showLongToast(UserInfoActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                ToastUtil.showLongToast(UserInfoActivity.this, msg);
            }
        });
    }

    /**
     * 获取图片上传token
     * @param photoPath 图片文件
     */
    private void getToken(final File photoPath) {
        if (token != null) {
            return;
        }
        HttpRequestUtils.httpRequest(this, "获取上传图片token", RequestValue.GET_PICTURE, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                LLog.iNetSucceed(" getPhotoToken " + response);
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        token = JsonUtil.parserGsonToObject(JsonUtil.getGsonValueByKey(response, "data"), PhotoToken.class);
                        if (photoPath != null) {
                            uploadPhoto(photoPath);
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

    /**
     * 上传图片
     * @param path 图片文件
     */
    private void uploadPhoto(final File path) {
        if (token == null) {
            getToken(path);
            return;
        }
        pd.show();

        final String key = Utils.getImgName();
        UploadManager um = QiniuUtil.getUploadManager();
        um.put(path, key, token.getSecurityToken(),
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info,
                                         JSONObject response) {
                        if (info.isOK()) {
                            String url = "";
                            if(!TextUtils.isEmpty(token.getDomain())){
                                url = token.getDomain()+ key;
                            }else{
                                url = ServerConfig.RUL_IMG + key;
                            }

//                            Utils.deleteFile(path.getPath(), UserInfoActivity.this);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageLoaderUtil.displayFromFile(UserInfoActivity.this, imgHeadIcon, path);
                                }
                            });
                            user.setUserHeadImage(url);
                            preferencesUtils.saveObjectData(user);
                            upUserInfor(null, url);
                            Log.d("联网 url", url);
                            pd.dismiss();
                        } else {
                            pd.dismiss();
                            ToastUtil.showLongToast(UserInfoActivity.this, "图片上传失败");
                        }
                    }
                }, null);
    }
}
