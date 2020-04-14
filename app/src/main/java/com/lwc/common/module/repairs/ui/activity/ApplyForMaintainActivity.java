package com.lwc.common.module.repairs.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.activity.ImageBrowseActivity;
import com.lwc.common.activity.InformationDetailsActivity;
import com.lwc.common.activity.MainActivity;
import com.lwc.common.adapter.MyGridViewPhotoAdpter;
import com.lwc.common.bean.Menu;
import com.lwc.common.bean.PickerView;
import com.lwc.common.configs.FileConfig;
import com.lwc.common.configs.ServerConfig;
import com.lwc.common.controler.global.GlobalValue;
import com.lwc.common.controler.http.RequestValue;
import com.lwc.common.module.bean.Address;
import com.lwc.common.module.bean.Common;
import com.lwc.common.module.bean.Malfunction;
import com.lwc.common.module.bean.PhotoToken;
import com.lwc.common.module.bean.Repairs;
import com.lwc.common.module.bean.RepairsCompany;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.order.ui.activity.FeeStandardActivity;
import com.lwc.common.module.repairs.presenter.ApplyForMaintainPresenter;
import com.lwc.common.utils.DialogUtil;
import com.lwc.common.utils.FileUtil;
import com.lwc.common.utils.HttpRequestUtils;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.PictureUtils;
import com.lwc.common.utils.QiniuUtil;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;
import com.lwc.common.view.MyGridView;
import com.lwc.common.view.TypeItem;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.zhihu.matisse.Matisse;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 申请维修
 */
public class ApplyForMaintainActivity extends BaseActivity {

    /**
     * 跳转地址管理
     */
    private final int INTENT_ADDRESS_MANAGER = 1000;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtPhone)
    TextView txtPhone;
    @BindView(R.id.txtCompany)
    TextView txtCompany;
    @BindView(R.id.tv_jgyj)
    TextView tv_jgyj;
    @BindView(R.id.edtMalfunction)
    EditText edtMalfunction;
    @BindView(R.id.gridview_my)
    MyGridView myGridview;
    @BindView(R.id.rLayoutAddress)
    RelativeLayout rLayoutAddress;
    @BindView(R.id.rLayoutCompany)
    RelativeLayout rLayoutCompany;
    @BindView(R.id.layout_type_list)
    LinearLayout layout_type_list;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.lLayout0)
    LinearLayout lLayout0;
    @BindView(R.id.rLayoutJg)
    RelativeLayout rLayoutJg;
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    @BindView(R.id.txtDeviceType)
    TextView txtDeviceType;
    @BindView(R.id.rLayoutDeviceType)
    RelativeLayout rLayoutDeviceType;

    @BindView(R.id.ll_sendType)
    LinearLayout ll_sendType;
    @BindView(R.id.et_express)
    EditText et_express;
    @BindView(R.id.ll_express)
    LinearLayout ll_express;
    @BindView(R.id.rg_sendType)
    RadioGroup rg_sendType;
    @BindView(R.id.ll_data_reserve)
    LinearLayout ll_data_reserve;

    private OptionsPickerView pvOptions;
    private OptionsPickerView pvDeviceOptions;
    //故障类型
    private List<Malfunction> malfunctions = new ArrayList<>();
    //维修类型
    private List<Repairs> repairses = new ArrayList<>();

    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<PickerView> options1Items = new ArrayList<>();
    private ArrayList<PickerView> options1CompanyItems = new ArrayList<>();
    private ArrayList<PickerView> secrecyCompanyItems = new ArrayList<>();
    //排序后的故障类型
    private List<List<Malfunction>> sortMalfunctions = new ArrayList<>();
    //维修服务公司
    private List<RepairsCompany> repairsCompanies = new ArrayList<>();
    private List<RepairsCompany> secrecyCompanies = new ArrayList<>();
    /**
     * 两个图片框图片路径
     */
    private File dataFile;
    private List<Address> addressesList = new ArrayList<>();
    /**
     * 选中的地址
     */
    private Address checkedAdd;
    /**
     * 选中的故障类型
     */
    private Malfunction checkedMan;
    /**
     * 选中的维修类型
     */
    private Repairs checkedRep;
    private ArrayList<Malfunction> malfunctionArrayList = new ArrayList<>();
    /**
     * 选中的维修公司
     */
    private RepairsCompany checkedCom;

    private String imgPath1;
    private ApplyForMaintainPresenter presenter;
    private SharedPreferencesUtils preferencesUtils;
    private User user;
    private ProgressDialog pd;
    private PhotoToken token;
    private int selectType = -1, countPhoto = 8;
    private String guangboStr, qrcodeIndex,leaseQrcodeIndex;
    private List<String> urlStrs = new ArrayList();
    private MyGridViewPhotoAdpter adpter;

    /**
     * 默认设备类型（默认不选择）
     */
    private long deviceMold = 0;


    //快递单号
    String experssStr = "";

    //寄送方式
    private String sendTypeStr;
    //是否为数据恢复
    private boolean isDataReset = true;
    //故障描述备注
    private String remark;
    private Dialog dialog;
    private String orderMsg;


    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_apply_for_maintain;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        //getRepairseAndMalfunctionsList();
        urlStrs.add("");
        adpter = new MyGridViewPhotoAdpter(this, urlStrs);
        myGridview.setAdapter(adpter);
        myGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adpter.getItem(position).equals("")) {
                    int count = countPhoto;
                    if (adpter.getCount() > 1) {
                        count = count - adpter.getCount()+1;
                    }
                    showTakePopupWindow1(count);
                } else {
                    Intent intent = new Intent(ApplyForMaintainActivity.this, ImageBrowseActivity.class);
                    intent.putExtra("index", position);
                    intent.putStringArrayListExtra("list", (ArrayList)adpter.getLists());
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 判断地址是否是空，做相关地址部分界面逻辑
     */
    private void ifAddressIsNull() {
        if (addressesList.size() == 0) {
            checkedAdd = null;
            lLayout0.setVisibility(View.GONE);
            txtAddress.setText("请添加维修地址");
        } else {
            setDefaultAddress();
            lLayout0.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置默认地址
     */
    private void setDefaultAddress() {
        for (int i = 0; i < addressesList.size(); i++) {
            Address address = addressesList.get(i);
            if (address.getIsDefault() == 1) {
                checkedAdd = address;
                txtName.setText("" + checkedAdd.getContactName());
                txtPhone.setText(checkedAdd.getContactPhone());
                txtAddress.setText("" + checkedAdd.getContactAddress().replace("_", ""));
                break;
            }
        }
    }

    /**
     * 获取维修类型和故障类型集合
     */
    private void getRepairseAndMalfunctionsList() {
//        malfunctions = DataSupport.findAll(Malfunction.class);
//        repairses = DataSupport.findAll(Repairs.class);
//        if (malfunctions == null || malfunctions.size() == 0 || repairses == null || repairses.size() == 0) {
            getRepairses();
//        } else {
//            setOptionsPickerData();
//        }
    }

    /**
     * 获取维修类型
     * 保存数据库
     */
    private void getRepairses() {
        if(deviceMold == 0){
            ToastUtil.showToast(this,"请选择设备类型");
            return;
        }
        HttpRequestUtils.httpRequest(this, "getRepairses", RequestValue.MALFUNCTION+"?deviceMold="+ deviceMold + "&clientType=person", null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        if (repairses == null) {
                            repairses = new ArrayList<>();
                        } else {
                            repairses.clear();
                        }
                        if (malfunctions == null) {
                            malfunctions = new ArrayList<>();
                        } else {
                            malfunctions.clear();
                        }
                        List<Menu> menus = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<Menu>>() {
                        });

                        if (menus != null && menus.size() > 0) {
                            //遍历数据
                            for (int i = 0; i < menus.size(); i++) {
                                Menu menu = menus.get(i);
                                LLog.i("Repairs " + menu.getDeviceTypeId() + "      " + menu.getDeviceTypeName());
                                repairses.add(new Repairs(menu.getDeviceTypeId(), menu.getDeviceTypeName(), null));
                                List<Menu.RepairsBean> repairs = menu.getRepairs();
                                if (repairs != null && repairs.size() > 0) {
                                    for (int j = 0; j < repairs.size(); j++) {
                                        Menu.RepairsBean repairsBean = repairs.get(j);
                                        malfunctions.add(new Malfunction(repairsBean.getReqair_id(), menu.getDeviceTypeId(), repairsBean.getReqair_name(), menu.getDeviceTypeName()));
                                    }
                                }
                            }
//                            DataSupport.saveAll(repairses);
//                            DataSupport.saveAll(malfunctions);
                            setOptionsPickerData();
                        }
                        break;
                    default:
                        ToastUtil.showLongToast(ApplyForMaintainActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
            }
        });
    }

    @Override
    public void init() {
        setTitle("申请报修");
        addressesList = DataSupport.findAll(Address.class);
        if (user != null && user.getParentCompanyId() != null && !user.getParentCompanyId().equals("")) {
            rLayoutCompany.setVisibility(View.GONE);
        }

            setRight("收费标准", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(deviceMold == 0){
                        ToastUtil.showToast(ApplyForMaintainActivity.this,"请先选择设备类型");
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("deviceTypeMold",String.valueOf(deviceMold));
                    bundle.putString("title", "收费标准");
                    IntentUtil.gotoActivity(ApplyForMaintainActivity.this, FeeStandardActivity.class, bundle);
                }
            });

        addressesList = DataSupport.findAll(Address.class);
        ifAddressIsNull();
    }

    @Override
    protected void initGetData() {
        checkedCom = (RepairsCompany)getIntent().getSerializableExtra("repairsCompany");
        checkedRep = (Repairs)getIntent().getSerializableExtra("repairs");
        checkedMan = (Malfunction) getIntent().getSerializableExtra("malfunction");
        preferencesUtils = SharedPreferencesUtils.getInstance(this);
        user = preferencesUtils.loadObjectData(User.class);
        qrcodeIndex = getIntent().getStringExtra("qrcodeIndex");
        leaseQrcodeIndex = getIntent().getStringExtra("leaseQrcodeIndex");
        if (checkedRep != null && checkedMan != null){
            String tx = checkedRep.getDeviceTypeName() + " -> "
                    + checkedMan.getReqairName();
            checkedMan.setDid(checkedRep.getDeviceTypeId());
            checkedMan.setDeviceTypeName(checkedRep.getDeviceTypeName());
            malfunctionArrayList.add(checkedMan);

            if(checkedRep.getDeviceTypeMold() != null){
                deviceMold = checkedRep.getDeviceTypeMold();
                if(deviceMold == 1){
                    txtDeviceType.setText("办公设备");
                }else if(deviceMold == 3){
                    txtDeviceType.setText("家用电器");
                }
            }
            updateTypeList();
        }

        pd = new ProgressDialog(this);
        pd.setMessage("正在上传图片...");
        pd.setCancelable(false);

        presenter = new ApplyForMaintainPresenter(this, this);
        String name = user.getBindCompanyName();
        if (checkedCom == null) {
            checkedCom = new RepairsCompany();
            if (name != null && !name.equals("")) {
                txtCompany.setText(name);
                String cid = user.getBindCompanyId();
                checkedCom.setCompanyId(cid);
                checkedCom.setCompanyName(name);
            } else {
//                txtCompany.setText("所有公司可接单");
                checkedCom.setCompanyId("0");
                checkedCom.setCompanyName("所有公司可接单");
            }
        } else {
            txtCompany.setText(checkedCom.getCompanyName());
        }

        guangboStr = preferencesUtils.loadString("guangboStr");
        if (tv_msg != null && !TextUtils.isEmpty(guangboStr)) {
            tv_msg.setText("注：" + guangboStr);
            tv_msg.setVisibility(View.VISIBLE);
        } else {
            tv_msg.setVisibility(View.GONE);
        }
    }

    @Override
    protected void widgetListener() {}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalValue.REPAIRS_PHOTO_REQUESTCODE0:
                    List<Uri> mSelected = Matisse.obtainResult(data);
                    if (mSelected != null && mSelected.size() > 0) {
                        urlStrs = adpter.getLists();
                        urlStrs.remove(urlStrs.size()-1);
                        for(int i=0; i<mSelected.size(); i++) {
                            Uri uri = mSelected.get(i);
                            urlStrs.add(PictureUtils.getPath(ApplyForMaintainActivity.this, uri));
                        }
                    }
                    if (urlStrs.size() < countPhoto) {
                        urlStrs.add("");
                    }
                    adpter.setLists(urlStrs);
                    adpter.notifyDataSetChanged();
                    break;
                case GlobalValue.REPAIRS_CAMERA_REQUESTCODE0:
                    try {
                        Bitmap bm = (Bitmap) data.getExtras().get("data");
                        if (bm == null) {
                            return;
                        }
                        File f = FileUtil.saveMyBitmap(bm);
                        File file = new Compressor.Builder(ApplyForMaintainActivity.this).setMaxHeight(1080).setMaxWidth(1920)
                                .setQuality(85).setCompressFormat(Bitmap.CompressFormat.PNG).setDestinationDirectoryPath(FileConfig.PATH_IMAGES)
                                .build().compressToFile(f);
                        if (file != null) {
                            dataFile = file;
                        } else {
                            dataFile = f;
                        }
                    } catch (Exception e) {
                    }
                    if (dataFile != null) {
                        if (urlStrs != null && urlStrs.get(urlStrs.size()-1).equals("")) {
                            urlStrs.remove(urlStrs.size()-1);
                        }
                        urlStrs.add(dataFile.getAbsolutePath());
                        if (urlStrs.size() < countPhoto) {
                            urlStrs.add("");
                        }
                        adpter.setLists(urlStrs);
                        adpter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showToast(ApplyForMaintainActivity.this, "选择图片失败");
                    }
                    break;
                case INTENT_ADDRESS_MANAGER:
                    checkedAdd = (Address) data.getSerializableExtra("address");
                    txtName.setText("" + checkedAdd.getContactName());
                    txtPhone.setText(checkedAdd.getContactPhone());
                    txtAddress.setText("" + checkedAdd.getContactAddress().replace("_", ""));
                    lLayout0.setVisibility(View.VISIBLE);
                    break;
            }
        } else if (requestCode == INTENT_ADDRESS_MANAGER) {
            addressesList = DataSupport.findAll(Address.class);
            ifAddressIsNull();
            setDefaultAddress();
        }
    }

    /**
     * 获取上传图片token
     * @param photoPath 图片文件
     */
    private void getToken(final File photoPath) {
        if (token != null) {
            return;
        }
        HttpRequestUtils.httpRequest(this, "获取上传图片token", RequestValue.GET_PICTURE, null, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
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

    private void uploadPhoto(final File path) {
        if (token == null) {
            getToken(path);
            return;
        }
        if (!pd.isShowing())
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
                                url = token.getDomain() + key;
                            }else{
                                url = ServerConfig.RUL_IMG + key;
                            }
                            if (TextUtils.isEmpty(imgPath1)) {
                                imgPath1 = url;
                            } else {
                                imgPath1 = imgPath1+","+url;
                            }
                            urlStrs.remove(path.getPath());
//                            Utils.deleteFile(path.getPath(), ApplyForMaintainActivity.this);
                            if (urlStrs.size() > 0 && !TextUtils.isEmpty(urlStrs.get(0))){
                                uploadPhoto(new File(urlStrs.get(0)));
                            } else {
                                submit();
                                pd.dismiss();
                            }
                            LLog.i("联网  图片地址" + url);
                        } else {
                            pd.dismiss();
                            ToastUtil.showLongToast(ApplyForMaintainActivity.this, "图片上传失败");
                        }
                    }
                }, null);
    }

    @OnClick({R.id.rLayoutJg, R.id.rLayoutAddress, R.id.rLayoutType,R.id.rLayoutDeviceType, R.id.rLayoutCompany, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rLayoutJg:
                String repIds = "";
                for (int i = 0; i < malfunctionArrayList.size(); i++) {
                    if (i == malfunctionArrayList.size()-1) {
                        repIds = repIds+malfunctionArrayList.get(i).getReqairId();
                    } else {
                        repIds = repIds+malfunctionArrayList.get(i).getReqairId()+",";
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putString("url", ServerConfig.DOMAIN.replace("https", "http")+"/main/h5/priceMsg.html");
                bundle.putString("repairsId", repIds);
                bundle.putString("title", "费用明细");
                IntentUtil.gotoActivity(ApplyForMaintainActivity.this, InformationDetailsActivity.class, bundle);
                break;
            case R.id.rLayoutAddress:
                IntentUtil.gotoActivityForResult(ApplyForMaintainActivity.this, AddressManagerActivity.class, INTENT_ADDRESS_MANAGER);
                break;
            case R.id.rLayoutType:
                if (malfunctionArrayList.size() < 5) {
                    initOptionPicker();
                } else {
                    ToastUtil.showToast(ApplyForMaintainActivity.this, "一个订单，最多只能选择5个维修类型！");
                }
                break;
            case R.id.rLayoutDeviceType:
                    initDevicePicker();
                break;
            case R.id.rLayoutCompany:

                break;
            case R.id.btnSubmit:
                if(Utils.isFastClick(2000, RequestValue.SUBMIT_ORDER)){
                    return;
                }
                if (checkedAdd == null) {
                    ToastUtil.showToast(this, "请选择维修地址");
                    return;
                }
                if(deviceMold == 0){
                    ToastUtil.showToast(this,"请选择设备类型");
                    return;
                }
                if (malfunctionArrayList== null || malfunctionArrayList.size() == 0) {
                    initOptionPicker();
                    ToastUtil.showToast(this, "请选择维修类型");
                    return;
                }
                if (adpter.getLists().size() > 1) {
                    urlStrs = adpter.getLists();
                    uploadPhoto(new File(urlStrs.get(0)));
                } else {
                    submit();
                }
                break;
        }
    }

    public void submit() {
        String imgs = "";
        if (!TextUtils.isEmpty(imgPath1)) {
            imgs = imgPath1;
        }
        final String imgPath = imgs;
        remark = edtMalfunction.getText().toString();

            //如果是数据恢复
            if(isDataReset && ll_sendType.getVisibility() == View.VISIBLE){
                if(TextUtils.isEmpty(sendTypeStr)){
                    ToastUtil.showToast(ApplyForMaintainActivity.this,"请选择寄送方式!");
                    return;
                }

                if(et_express != null && ll_express.getVisibility() == View.VISIBLE){
                    experssStr = et_express.getText().toString().trim();
                    if(TextUtils.isEmpty(experssStr)){
                        ToastUtil.showToast(ApplyForMaintainActivity.this,"请填写快递单号!");
                        return;
                    }
                    remark = remark +"配送方式：寄送   快递单号："+experssStr;
                }else{
                    remark = remark +"配送方式：自行送达";
                }
            }

            if(MainActivity.mainFragment.newestOrders != null && MainActivity.mainFragment.newestOrders.size() > 0){
                ToastUtil.showToast(ApplyForMaintainActivity.this,"你已有订单在进行中了");
            }

            if (!TextUtils.isEmpty(guangboStr) && !TextUtils.isEmpty(orderMsg)) {
                dialog = DialogUtil.dialog(ApplyForMaintainActivity.this, "温馨提示", "确定","取消",orderMsg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if(!TextUtils.isEmpty(qrcodeIndex)){
                            presenter.submitOrder(malfunctionArrayList, "",
                                    checkedAdd.getUserAddressId(), "", imgPath, remark, qrcodeIndex,experssStr,String.valueOf(deviceMold),false);
                        }else{
                            presenter.submitOrder(malfunctionArrayList, "",
                                    checkedAdd.getUserAddressId(), "", imgPath, remark, leaseQrcodeIndex,experssStr,String.valueOf(deviceMold),true);
                        }

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                },true);
            } else {
                if(!TextUtils.isEmpty(qrcodeIndex)){
                    presenter.submitOrder(malfunctionArrayList, "",
                            checkedAdd.getUserAddressId(), "", imgPath, remark, qrcodeIndex,experssStr,String.valueOf(deviceMold),false);
                }else{
                    presenter.submitOrder(malfunctionArrayList, "",
                            checkedAdd.getUserAddressId(), "", imgPath, remark, leaseQrcodeIndex,experssStr,String.valueOf(deviceMold),true);
                }

            }

    }

    /**
     * 设置维修类型选择数据
     */
    private void setOptionsPickerData() {
        options1Items = new ArrayList<>();
        sortMalfunctions = new ArrayList<>();
        options2Items = new ArrayList<>();
        for (int i = 0; i < repairses.size(); i++) {
            if (!TextUtils.isEmpty(repairses.get(i).getDeviceTypeName())) {
                options1Items.add(new PickerView(i, repairses.get(i).getDeviceTypeName()));
                if (selectType != -1 && checkedRep != null && checkedRep.getDeviceTypeName().equals(repairses.get(i).getDeviceTypeName())) {
                    selectType = i;
                }
            }
        }
        for (int j = 0; j < repairses.size(); j++) {
            ArrayList<String> options2Items_01 = new ArrayList<>();
            List<Malfunction> sortMalfunction1 = new ArrayList<>();
            Repairs repairs = repairses.get(j);
            for (int z = 0; z < malfunctions.size(); z++) {
                Malfunction malfunction = malfunctions.get(z);
                String repairsId = repairs.getDeviceTypeId();
                String malfunctionDid = malfunction.getDid();
                if (repairsId.equals(malfunctionDid)) {
                    options2Items_01.add(malfunction.getReqairName());
                    sortMalfunction1.add(new Malfunction(malfunction.getReqairId(), malfunction.getDid(), malfunction.getReqairName(), malfunction.getDeviceTypeName()));
                }
            }
            sortMalfunctions.add(sortMalfunction1);
            options2Items.add(options2Items_01);
        }
    }

    /**
     * 维修类型选择器初始化
     */
    private void initOptionPicker() {
        if (options1Items != null && options2Items != null && options1Items.size() > 0 && options2Items.size() > 0) {
            //if (pvOptions == null) {
                pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                //返回的分别是三个级别的选中位置
                        if (sortMalfunctions.get(options1) == null || sortMalfunctions.get(options1).size() == 0) {
                            ToastUtil.showLongToast(ApplyForMaintainActivity.this, "请选择正确的维修类型");
                            return;
                        }
                        if (checkedRep != null && !checkedRep.getDeviceTypeId().equals(repairses.get(options1).getDeviceTypeId())) {
                            checkedRep = repairses.get(options1);
                            if (user.getIsSecrecy() != null && user.getIsSecrecy().equals("2") && TextUtils.isEmpty(user.getParentCompanyId())) {
                                setRepairsCompanys();
                            }
                        } else if (checkedRep == null) {
                            checkedRep = repairses.get(options1);
                            if (user.getIsSecrecy() != null && user.getIsSecrecy().equals("2") && TextUtils.isEmpty(user.getParentCompanyId())) {
                                setRepairsCompanys();
                            }
                        } else {
                            checkedRep = repairses.get(options1);
                        }
                        checkedMan = sortMalfunctions.get(options1).get(options2);
                        malfunctionArrayList.add(checkedMan);
                        updateTypeList();
                    }
                })
                        .setTitleText("维修类型")
                        .setCancelText("取消")
                        .setSubmitText("确定")
                        .setContentTextSize(15)//设置滚轮文字大小
                        .setDividerColor(getResources().getColor(R.color.city_dialog_content_bg))//设置分割线的颜色
                        .setSelectOptions(0, 0)//默认选中项
                        .setBgColor(getResources().getColor(R.color.city_dialog_content_bg))
                        .setTitleBgColor(getResources().getColor(R.color.white))
                        .setTitleColor(getResources().getColor(R.color.city_dialog_black))
                        .setCancelColor(getResources().getColor(R.color.city_dialog_blue))
                        .setSubmitColor(getResources().getColor(R.color.city_dialog_blue))
                        .setTextColorCenter(getResources().getColor(R.color.city_dialog_content_black))
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .setLabels("", "", "")
                        .build();
                pvOptions.setPicker(options1Items, options2Items);//二级选择器
                if (selectType != -1)
                    pvOptions.setSelectOptions(selectType);
                pvOptions.show();
           /* } else {
                pvOptions.show();
            }*/
        } else if (pvOptions == null) {
            getRepairses();
        }
    }

    /**
     * 设备类型选择器初始化
     */
    private void initDevicePicker() {
        final List<PickerView> deviceItems = new ArrayList<>();
        deviceItems.add(new PickerView(1,"办公设备"));
        deviceItems.add(new PickerView(3,"家用电器"));
            if (pvDeviceOptions == null) {
                pvDeviceOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                      //  ToastUtil.showToast(ApplyForMaintainActivity.this,"options1:"+options1+"options2:"+options2+"options3:"+options3);
                        PickerView pickerView = deviceItems.get(options1);
                        deviceMold = pickerView.getId();
                        txtDeviceType.setText(pickerView.getName());
                        malfunctionArrayList.clear();
                        updateTypeList();
                        getRepairses();
                    }
                }).setTitleText("设备类型")
                        .setCancelText("取消")
                        .setSubmitText("确定")
                        .setContentTextSize(15)//设置滚轮文字大小
                        .setDividerColor(getResources().getColor(R.color.city_dialog_content_bg))//设置分割线的颜色
                        .setSelectOptions(0, 0)//默认选中项
                        .setBgColor(getResources().getColor(R.color.city_dialog_content_bg))
                        .setTitleBgColor(getResources().getColor(R.color.white))
                        .setTitleColor(getResources().getColor(R.color.city_dialog_black))
                        .setCancelColor(getResources().getColor(R.color.city_dialog_blue))
                        .setSubmitColor(getResources().getColor(R.color.city_dialog_blue))
                        .setTextColorCenter(getResources().getColor(R.color.city_dialog_content_black))
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .setLabels("", "", "")
                        .build();
                pvDeviceOptions.setPicker(deviceItems);//二级选择器
                if (selectType != -1)
                    pvDeviceOptions.setSelectOptions(selectType);
                pvDeviceOptions.show();
            } else {
                pvDeviceOptions.show();
            }
    }

    public void updateTypeList() {
        if (malfunctionArrayList.size() > 0) {
            layout_type_list.setVisibility(View.VISIBLE);
            layout_type_list.removeAllViews();
            isDataReset = true;
            for (int i = 0; i < malfunctionArrayList.size(); i++) {
                TypeItem reItem = new TypeItem(this);
               // reItem.setLineGone();
                Malfunction mBean = malfunctionArrayList.get(i);
                reItem.initData(mBean);
                if (i == malfunctionArrayList.size()-1) {
                    reItem.setLineGone();
                }
                reItem.setListener(malfunctionArrayList.get(i), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Malfunction m = (Malfunction) v.getTag();
                        malfunctionArrayList.remove(m);
                        updateTypeList();
                    }
                });
                layout_type_list.addView(reItem);

                if(!mBean.getDeviceTypeName().contains("数据恢复")){
                    isDataReset = false;
                }
                /**
                 * 普通用户才做数据恢复处理  机关单位不处理
                 */
                /*if("5".equals(user.getRoleId())) {
                    //如果维修类型是数据恢复 则单独处理
                    if (mBean.getDeviceTypeName().contains("数据恢复")) {
                        isDataReset = true;
                        ll_sendType.setVisibility(View.VISIBLE);
                        //et_express.setVisibility(View.VISIBLE);
                        final InputMethodManager imm = (InputMethodManager) ApplyForMaintainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        rg_sendType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if (checkedId == R.id.radio_self) {
                                    et_express.setVisibility(View.GONE);
                                    //关闭软键盘
                                    imm.hideSoftInputFromWindow(et_express.getWindowToken(), 0);
                                    sendTypeStr = "自行送达";
                                } else {
                                    et_express.setVisibility(View.VISIBLE);
                                    //获取焦点
                                    et_express.setFocusable(true);
                                    et_express.setFocusableInTouchMode(true);
                                    et_express.requestFocus();
                                    //打开软键盘
                                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                    sendTypeStr = "寄送";
                                }
                            }
                        });
                   *//* //如果选择了数据恢复还选择了 其它 则隐藏快递
                    if(malfunctionArrayList.size() > 1 && ll_sendType != null){
                        isDataReset = true;
                        ll_sendType.setVisibility(View.VISIBLE);
                        etExpress.setVisibility(View.VISIBLE);
                    }*//*
                    } else {
                        //如果选择了数据恢复还选择了 其它 则隐藏快递
                        if (ll_sendType != null && ll_sendType.getVisibility() == View.VISIBLE) {
                            isDataReset = false;
                            ll_sendType.setVisibility(View.GONE);
                            et_express.setVisibility(View.GONE);
                        }
                    }
                    if (malfunctionArrayList.size() == 1 && malfunctionArrayList.get(0).getDeviceTypeName().contains("数据恢复")) {
                        isDataReset = true;
                        ll_sendType.setVisibility(View.VISIBLE);
                        //etExpress.setVisibility(View.VISIBLE);
                    }
                }*/


            }

            //判断数据恢复是否显示
            if("5".equals(user.getRoleId())){
                if(isDataReset){
                    ll_sendType.setVisibility(View.VISIBLE);
                    ll_data_reserve.setVisibility(View.VISIBLE);
                    final InputMethodManager imm = (InputMethodManager) ApplyForMaintainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    rg_sendType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == R.id.radio_self) {
                                ll_express.setVisibility(View.GONE);
                                //关闭软键盘
                                imm.hideSoftInputFromWindow(et_express.getWindowToken(), 0);
                                sendTypeStr = "自行送达";
                            } else if(checkedId == R.id.radio_send){
                                ll_express.setVisibility(View.VISIBLE);
                                //获取焦点
                                et_express.setFocusable(true);
                                et_express.setFocusableInTouchMode(true);
                                et_express.requestFocus();
                                //打开软键盘
                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                sendTypeStr = "寄送";
                            }else{
                                ll_express.setVisibility(View.GONE);
                                sendTypeStr = "";
                            }
                        }
                    });
                }else{
                    ll_sendType.setVisibility(View.GONE);
                    ll_data_reserve.setVisibility(View.GONE);
                    ll_express.setVisibility(View.GONE);
                    rg_sendType.clearCheck();
                    sendTypeStr = "";
                }
            }

            if (user.getIsSecrecy() == null ||(user.getIsSecrecy() != null && !user.getIsSecrecy().equals("2"))) {
                getDetectionInfo();
            }
        } else {
            rLayoutJg.setVisibility(View.GONE);
            layout_type_list.setVisibility(View.GONE);
            ll_sendType.setVisibility(View.GONE);
            ll_data_reserve.setVisibility(View.GONE);
            ll_express.setVisibility(View.GONE);
            rg_sendType.clearCheck();
            sendTypeStr = "";
        }
    }

    /**
     * 设置维修公司类型集合
     */
    private void setRepairsCompanys() {
        options1CompanyItems.clear();
        secrecyCompanyItems.clear();
        options1CompanyItems.add(new PickerView(-1, "所有公司可接单"));
        secrecyCompanyItems.add(new PickerView(-1, "所有公司可接单"));
        getRepairsCompanyList();
    }

    /**
     * 获取维修公司列表
     */
    private void getRepairsCompanyList() {
        Map<String, String> map = new HashMap<>();
        if (checkedRep != null) {
            map.put("deviceTypeId", checkedRep.getDeviceTypeId());
        }
        HttpRequestUtils.httpRequest(this, "getRepairsCompanyList", RequestValue.REPAIRS_COMPANY, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        List<RepairsCompany> companies = JsonUtil.parserGsonToArray(JsonUtil.getGsonValueByKey(response, "data"), new TypeToken<ArrayList<RepairsCompany>>() {
                        });
                        boolean isExist = false;
                        if (companies != null && companies.size() > 0) {
                            repairsCompanies = companies;
                            secrecyCompanies = new ArrayList<>();
                            for (int i = 0; i < companies.size(); i++) {
                                RepairsCompany repairsCompany = companies.get(i);
                                options1CompanyItems.add(new PickerView(i, repairsCompany.getCompanyName()));
                                if (repairsCompany.getIsSecrecy() != null && repairsCompany.getIsSecrecy().equals("3")) {
                                    secrecyCompanies.add(repairsCompany);
                                    secrecyCompanyItems.add(new PickerView(i, repairsCompany.getCompanyName()));
                                }
                                if (checkedCom != null && checkedCom.getCompanyId().equals(repairsCompany.getCompanyId())) {
                                    isExist = true;
                                }
                            }
                        } else {
                            repairsCompanies = new ArrayList<>();
                            options1CompanyItems.clear();
                            secrecyCompanies = new ArrayList<>();
                            secrecyCompanyItems.clear();
                        }
                        if (!isExist) {
                            txtCompany.setText("");
                            checkedCom = null;
                        }
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
            }
        });
    }


    /**
     * 初始化拍照选择框
     */
    private void showTakePopupWindow1(int count) {
        ToastUtil.showPhotoSelect(this, count);
        getToken(null);
    }

    /**
     * 获取报价单
     */
    private void getDetectionInfo() {
        Map<String, String> map = new HashMap<>();
        String repIds = "";
        for (int i = 0; i < malfunctionArrayList.size(); i++) {
            if (i == malfunctionArrayList.size()-1) {
                repIds = repIds+malfunctionArrayList.get(i).getReqairId();
            } else {
                repIds = repIds+malfunctionArrayList.get(i).getReqairId()+",";
            }
        }
        map.put("repairId", repIds);
        HttpRequestUtils.httpRequest(this, "查询预计费用", RequestValue.GET_PRICE_MSG_2_9, map, "GET", new HttpRequestUtils.ResponseListener() {
            @Override
            public void getResponseData(String response) {
                Common common = JsonUtil.parserGsonToObject(response, Common.class);
                switch (common.getStatus()) {
                    case "1":
                        String data = JsonUtil.getGsonValueByKey(response, "data");
                        if (TextUtils.isEmpty(data)) {
                            ToastUtil.showLongToast(ApplyForMaintainActivity.this, "未查询到评估价格");
                            rLayoutJg.setVisibility(View.GONE);
                            return;
                        }

                        orderMsg = JsonUtil.getGsonValueByKey(data,"orderMsg");
                        String priceMsg = JsonUtil.getGsonValueByKey(data,"priceMsg");
                        
                        if(!TextUtils.isEmpty(priceMsg)){
                            rLayoutJg.setVisibility(View.VISIBLE);
                            tv_jgyj.setText(priceMsg);
                        }

                       
                       /* Spanned htmlData;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            htmlData = Html.fromHtml(data,Html.FROM_HTML_MODE_COMPACT);
                        }else{
                            htmlData = Html.fromHtml(data);
                        }*/
                      
                        break;
                    default:
                        rLayoutJg.setVisibility(View.GONE);
                        ToastUtil.showLongToast(ApplyForMaintainActivity.this, common.getInfo());
                        break;
                }
            }

            @Override
            public void returnException(Exception e, String msg) {
                LLog.eNetError(e.toString());
                rLayoutJg.setVisibility(View.GONE);
                ToastUtil.showLongToast(ApplyForMaintainActivity.this, msg);
            }
        });
    }
}
