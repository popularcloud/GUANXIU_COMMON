package com.lwc.common.module.repairs.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.reflect.TypeToken;
import com.lwc.common.R;
import com.lwc.common.activity.BaseActivity;
import com.lwc.common.bean.Location;
import com.lwc.common.bean.PickerView;
import com.lwc.common.module.bean.Address;
import com.lwc.common.module.bean.PoiBean;
import com.lwc.common.module.bean.Sheng;
import com.lwc.common.module.bean.Shi;
import com.lwc.common.module.bean.User;
import com.lwc.common.module.bean.Xian;
import com.lwc.common.module.repairs.presenter.AddAddressPresenter;
import com.lwc.common.module.repairs.ui.IAddAddressView;
import com.lwc.common.utils.FileUtil;
import com.lwc.common.utils.IntentUtil;
import com.lwc.common.utils.JsonUtil;
import com.lwc.common.utils.KeyboardUtil;
import com.lwc.common.utils.LLog;
import com.lwc.common.utils.SharedPreferencesUtils;
import com.lwc.common.utils.ToastUtil;
import com.lwc.common.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 * 添加地址
 */
public class AddAddressActivity extends BaseActivity implements IAddAddressView {

    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.btnAffirm)
    TextView btnAffirm;
    @BindView(R.id.edtDetailAddress)
    TextView edtDetailAddress;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.edtAddress)
    TextView edtAddress;
    private AddAddressPresenter presenter;

    private ArrayList<PickerView> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private List<Sheng> shengs = new ArrayList<>();
    private List<Shi> shis = new ArrayList<>();
    private List<Xian> xians = new ArrayList<>();
    //排序后的城市
    private List<List<Shi>> sortShi = new ArrayList<>();
    //排序后的县
    private List<List<List<Xian>>> sortXian = new ArrayList<>();
    /**
     * 选中的省
     */
    private Sheng selectedSheng;
    /**
     * 选中的市
     */
    private Shi selectedShi;
    /**
     * 选中的县
     */
    private Xian selectedXian;
    private Address addressData;
    private PoiBean poiBean;
    private String cityCode;
    private User user;
    private Location location;

    @Override
    protected int getContentViewId(Bundle savedInstanceState) {
        return R.layout.activity_add_address;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Utils.isChinese(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        edtName.setFilters(new InputFilter[]{filter});
    }

    /**
     * 获取地区数据
     */
    private void getAreaData() {
        String sheng = FileUtil.readAssetsFile(this, "sheng.json");
        String shi = FileUtil.readAssetsFile(this, "shi.json");
        String xian = FileUtil.readAssetsFile(this, "xian.json");
        shengs = JsonUtil.parserGsonToArray(sheng, new TypeToken<ArrayList<Sheng>>() {
        });
        shis = JsonUtil.parserGsonToArray(shi, new TypeToken<ArrayList<Shi>>() {
        });

        String address_province = (String) SharedPreferencesUtils.getParam(this,"address_province","湖南");
        String address_city = (String) SharedPreferencesUtils.getParam(this,"address_city","长沙");

        if (!TextUtils.isEmpty(address_city) && shis != null) {
            for (int i = 0; i < shis.size(); i++) {
                if (shis.get(i).getName().equals(address_city)) {
                    selectedShi = shis.get(i);
                    shis.remove(selectedShi);
                    break;
                }
            }
            if (selectedShi != null) {
                for (int i = 0; i < shengs.size(); i++) {
                    if (shengs.get(i).getDmId().equals(selectedShi.getParentid())) {
                        selectedSheng = shengs.get(i);
                        shengs.remove(selectedSheng);
                        break;
                    }
                }
                if (selectedSheng != null) {
                    //shengs.clear();
                    shengs.add(0,selectedSheng);
                   // shis.clear();
                    shis.add(0,selectedShi);
                }
            }
        }
        xians = JsonUtil.parserGsonToArray(xian, new TypeToken<ArrayList<Xian>>() {
        });
    }

    /**
     * 加载地区弹框数据
     */
    private void loadOptionsPickerViewData() {
        //添加省
        for (int i = 0; i < shengs.size(); i++) {
            Sheng sheng = shengs.get(i);
            options1Items.add(new PickerView(i, sheng.getName()));
        }
//        //添加市
        for (int j = 0; j < shengs.size(); j++) {
            Sheng sheng = shengs.get(j);
            ArrayList<String> options2Items_01 = new ArrayList<>();
            List<Shi> tempSortShi = new ArrayList<>();
            for (int z = 0; z < shis.size(); z++) {
                Shi shi = shis.get(z);
                if (sheng.getDmId().equals(shi.getParentid())) {
                    options2Items_01.add(shi.getName());
                    tempSortShi.add(shi);
                }
            }
            options2Items.add(options2Items_01);
            sortShi.add(tempSortShi);

        }
        //添加县
        for (int p = 0; p < shengs.size(); p++) { //遍历省级
            ArrayList<ArrayList<String>> provinceAreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            List<List<Xian>> tempProvince = new ArrayList<>();
            Sheng sheng = shengs.get(p);
            for (int s = 0; s < shis.size(); s++) {  //省级下的市
                ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
                List<Xian> tempXian = new ArrayList<>();
                Shi shi = shis.get(s);
                if (sheng.getDmId().equals(shi.getParentid())) {
                    for (int x = 0; x < xians.size(); x++) {
                        Xian xian = xians.get(x);
                        if (shi.getDmId().equals(xian.getParentid())) {
                            cityList.add(xian.getName());
                            tempXian.add(xian);
                        }
                    }
                    provinceAreaList.add(cityList);
                    tempProvince.add(tempXian);
                }
            }
            options3Items.add(provinceAreaList);
            sortXian.add(tempProvince);
        }
        LLog.i("options3Items    " + options3Items.toString());
    }

    private Thread myThread = new Thread() {
        @Override
        public void run() {
            super.run();
            loadOptionsPickerViewData();
        }
    };

    @Override
    protected void init() {
        Intent intent = getIntent();
        addressData = (Address) intent.getSerializableExtra("address");
        user = SharedPreferencesUtils.getInstance(AddAddressActivity.this).loadObjectData(User.class);
        location = SharedPreferencesUtils.getInstance(AddAddressActivity.this).loadObjectData(Location.class);
        if (addressData == null) {
            setTitle("添加新地址");
            if (location != null) {
                String[] list = location.getStrValue().split("#");
                if (list != null && list.length > 0) {
                    Map<String, String> map = new HashMap<>();
                    for (int i = 0; i < list.length; i++) {
                        String str = list[i];
                        if (str != null && !str.equals("")) {
                            String[] arr = str.split("=");
                            if (arr != null && arr.length > 1) {
                                map.put(arr[0], arr[1]);
                            }
                        }
                    }
                    String province = map.get("province");
                    String city = map.get("city");
                    String address = map.get("address");
                    cityCode = map.get("cityCode");
                  /*  if (province != null && city != null && address != null) {
                        edtDetailAddress.setText(address.replace(province + city, ""));
                    }*/
                }
            }
            if (user != null) {
                if (!TextUtils.isEmpty(user.getUserRealname()))
                    edtName.setText(user.getUserRealname());
                if (!TextUtils.isEmpty(user.getUserPhone())) {
                    edtPhone.setText(user.getUserPhone());
                } else if (!TextUtils.isEmpty(user.getUserName())) {
                    edtPhone.setText(user.getUserName());
                }
            }
        } else {
            setTitle("修改地址");
            edtName.setText(addressData.getContactName());
            edtPhone.setText(addressData.getContactPhone());
            String[] arr = addressData.getContactAddress().split("_");
            edtDetailAddress.setText(arr[0]);
            if (arr.length > 1) {
                edtAddress.setText(arr[1]);
            }
            String province = addressData.getProvinceName();
            if(!TextUtils.isEmpty(province)){
                selectedSheng = new Sheng();
                selectedSheng.setDmId(String.valueOf(addressData.getProvinceId()));
                selectedSheng.setName(addressData.getProvinceName());
            }
            String city = addressData.getCityName();
            if(!TextUtils.isEmpty(city)){
                selectedShi = new Shi();
                selectedShi.setDmId(String.valueOf(addressData.getCityId()));
                selectedShi.setName(addressData.getCityName());
            }
            String county = addressData.getTownName();
            if(!TextUtils.isEmpty(county)){
                selectedXian = new Xian();
                selectedXian.setDmId(String.valueOf(addressData.getTownId()));
                selectedXian.setName(addressData.getTownName());
            }
            txtAddress.setText(province + "-" + city + "-" + county);
        }
        getAreaData();
        myThread.start();
    }

    @Override
    protected void initGetData() {
        presenter = new AddAddressPresenter(this, this);
    }

    @Override
    protected void widgetListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1520 && resultCode == RESULT_OK) {
            poiBean = new PoiBean();
            poiBean.setTitleName(data.getStringExtra("address"));
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
            poiBean.setPoint(latLonPoint);
            edtDetailAddress.setText(poiBean.getTitleName());
        }
    }

    @OnClick({R.id.btnAffirm, R.id.txtAddress, R.id.ll_select_address})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.ll_select_address:
                if(selectedXian == null){
                    ToastUtil.showToast(this,"请先选择地区");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("city_str",selectedShi.getName());
                IntentUtil.gotoActivityForResult(AddAddressActivity.this, SelectAddressActivity.class, bundle,1520);
                break;
            case R.id.btnAffirm:
                String name = edtName.getText().toString();
                String phone = edtPhone.getText().toString();
                String address = edtDetailAddress.getText().toString();

                if (name == null || TextUtils.isEmpty(name)) {
                    ToastUtil.showLongToast(this, "名字不能为空");
                    return;
                }

                if (phone == null || TextUtils.isEmpty(phone)) {
                    ToastUtil.showLongToast(this, "电话号码不能为空");
                    return;
                }

                if (address == null || TextUtils.isEmpty(address)) {
                    ToastUtil.showLongToast(this, "地址不能为空");
                    return;
                }
                if (phone.length() < 11) {
                    ToastUtil.showLongToast(this, "请输入正确的电话号码,例如：18812345678或07691234567");
                    return;
                }

                if (addressData == null && (selectedSheng == null || selectedShi == null || selectedXian == null)) {
                    ToastUtil.showLongToast(this, "请选择地区");
                    return;
                }
                String add = edtAddress.getText().toString().trim();
                if (TextUtils.isEmpty(add)) {
                    ToastUtil.showLongToast(this, "请填写详细地址");
                    return;
                }
                if (Utils.isFastClick(3000, "y")) {
                    return;
                }
                double latitude = 0;
                double longitude = 0;
                String detailAddress = address + "_" + add;
                if (addressData == null) {
                    if (poiBean != null) {
                        latitude = poiBean.getPoint().getLatitude();
                        longitude = poiBean.getPoint().getLongitude();
                    } else if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                    presenter.addUserAddress(name, phone, detailAddress, selectedSheng.getDmId(), selectedShi.getDmId(), selectedXian.getDmId(), latitude, longitude);
                } else {
                    if (poiBean != null) {
                        latitude = poiBean.getPoint().getLatitude();
                        longitude = poiBean.getPoint().getLongitude();
                    } else if (addressData != null) {
                        if (!TextUtils.isEmpty(addressData.getLongitude())) {
                            latitude = Double.parseDouble(addressData.getLatitude());
                        }
                        if (!TextUtils.isEmpty(addressData.getLongitude())) {
                            longitude = Double.parseDouble(addressData.getLongitude());
                        }
                    }
                    if (selectedSheng != null && selectedShi != null && selectedXian != null) {
                        presenter.upUserAddress(addressData.getUserAddressId(), name, phone,
                                detailAddress, selectedSheng.getDmId(), selectedShi.getDmId(), selectedXian.getDmId(), latitude, longitude);
                    } else if (selectedSheng != null && selectedShi != null) {
                        presenter.upUserAddress(addressData.getUserAddressId(), name, phone,
                                detailAddress, selectedSheng.getDmId(), selectedShi.getDmId(), null, latitude, longitude);
                    } else if (selectedSheng != null) {
                        presenter.upUserAddress(addressData.getUserAddressId(), name, phone,
                                detailAddress, selectedSheng.getDmId(), null, null, latitude, longitude);
                    } else {
                        presenter.upUserAddress(addressData.getUserAddressId(), name, phone,
                                detailAddress, null, null, null, latitude, longitude);
                    }
                }
                break;
            case R.id.txtAddress:
                showPickerView();
                break;
        }
    }

    /**
     * 弹出区域选择器
     */
    private void showPickerView() {
        KeyboardUtil.showInput(false, this);
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                selectedSheng = shengs.get(options1);
                selectedShi = sortShi.get(options1).get(options2);
                selectedXian = sortXian.get(options1).get(options2).get(options3);
                String tx = selectedSheng.getName() + "-" +
                        selectedShi.getName() + "-" +
                        selectedXian.getName();
                txtAddress.setText(tx);

                edtDetailAddress.setText("");
            }
        })

                .setTitleText("选择区域")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }
}
