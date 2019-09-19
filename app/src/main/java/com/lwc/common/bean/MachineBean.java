package com.lwc.common.bean;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 设备bean对象
 * 
 * @Description TODO
 * @author 何栋
 * @date 2015年12月1日
 * @Copyright: lwc
 */
public class MachineBean extends BaseBean {

	/** 变量描述 */
	private static final long serialVersionUID = 1L;
	/**  */
	private String countdate;
	/**  */
	private String ctime;
	/** 设备类型id */
	private String did;
	/** 公司id */
	private String cid;
	/** 设备id */
	private String udid;
	/** 设备名称 */
	private String dname;
	/** 设备品牌id */
	private String dtype;
	/** 设备品牌 */
	private String dtype_name;
	/** 来自谁的id */
	private String fuid;
	/**  */
	private String id;
	/**  */
	private String qrcode;
	/** status：0停用，1正常，2报修中，3返修中 */
	private String status;
	/**  */
	private String uid;

	// -----设备详情------------------
	/** 设备详情列表 */
	private ArrayList<MenuBean> machineInfoList = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@Override
	protected void init(JSONObject jSon) throws JSONException {
		// TODO Auto-generated method stub
		// "countPage":1,
		// "countSize":2,
		// "data":[{"countdate":"",
		// "ctime":"1447945158612",
		// "did":"1",
		// "dname":"惠普G4",
		// "dtype":"1",
		// "fuid":"12",
		// "id":"16",
		// "pageNow":1,
		// "pageSize":10,
		// "qrcode":"upload/device/qrcode/0974FC0B48714150BD44C99EB535BED4.jpg",
		// "status":"1",
		// "uid":"12"},

		setCountdate(jSon.optString("countdate"));
		setCtime(jSon.optString("ctime"));
		setDid(jSon.optString("did"));
		setDname(jSon.optString("dname"));
		setDtype(jSon.optString("dtype"));
		setFuid(jSon.optString("fuid"));
		setId(jSon.optString("id"));
		setQrcode(jSon.optString("qrcode"));
		setStatus(jSon.optString("status"));
		setUid(jSon.optString("uid"));
		setUdid(jSon.optString("udid"));
		setCid(jSon.optString("cid"));

		if (!TextUtils.isEmpty(jSon.optString("properties"))) {
			String json = "{\"data\":" + jSon.optString("properties") + "}";
			machineInfoList.addAll((ArrayList<MenuBean>) BaseBean.setObjectList(MenuBean.class, json).getModelList());
		}
	}

	public String getDtype_name() {
		return dtype_name;
	}

	public void setDtype_name(String dtype_name) {
		this.dtype_name = dtype_name;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public void setMachineInfoList(ArrayList<MenuBean> machineInfoList) {
		this.machineInfoList = machineInfoList;
	}

	public ArrayList<MenuBean> getMachineInfoList() {
		return machineInfoList;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public String getCountdate() {
		return countdate;
	}

	public String getCtime() {
		return ctime;
	}

	public String getDid() {
		return did;
	}

	public String getDname() {
		return dname;
	}

	public String getDtype() {
		return dtype;
	}

	public String getFuid() {
		return fuid;
	}

	public String getId() {
		return id;
	}

	public String getQrcode() {
		return qrcode;
	}

	public String getStatus() {
		return status;
	}

	public String getUid() {
		return uid;
	}

	public void setCountdate(String countdate) {
		this.countdate = countdate;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

	public void setFuid(String fuid) {
		this.fuid = fuid;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
