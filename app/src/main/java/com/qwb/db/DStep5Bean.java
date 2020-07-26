package com.qwb.db;

import org.litepal.crud.LitePalSupport;

/**
 *  拜访步骤5：下单
 */
public class DStep5Bean extends LitePalSupport {
	private String userId;//用户id
	private String companyId;//公司id
	private String type; // 1：拜访下单(添加或修改) 2:电话下单(添加) 3：订货下单列表（查看或修改）4：退货(添加或修改) 5：退货下单列表（查看或修改）
	private int autoType;//是否要自动提交 0：否 1：是（只有在下单界面要"提交"按钮时才自动提交）

	private String count;//0:未上传，1：已上传
	private String orderId;//订单id
	private String cid;  //客户id
	private String shr;  //收货人或联系人
	private String tel;
	private String address;
	private String remo; //备注
	private String zje;  //总金额
	private String zdzk; //整单折扣
	private String cjje; //成交金额
	private String shTime; //收货时间
	private String pszd;   //配送指定
	private String stkId;  //仓库id
	private String stkName;//仓库名称
	private String orderxx; //json
	private String time;
	private boolean isSuccess;
	private int submitCount;//自动提交次数

	private String khNm;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getShr() {
		return shr;
	}

	public void setShr(String shr) {
		this.shr = shr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemo() {
		return remo;
	}

	public void setRemo(String remo) {
		this.remo = remo;
	}

	public String getZje() {
		return zje;
	}

	public void setZje(String zje) {
		this.zje = zje;
	}

	public String getZdzk() {
		return zdzk;
	}

	public void setZdzk(String zdzk) {
		this.zdzk = zdzk;
	}

	public String getCjje() {
		return cjje;
	}

	public void setCjje(String cjje) {
		this.cjje = cjje;
	}

	public String getShTime() {
		return shTime;
	}

	public void setShTime(String shTime) {
		this.shTime = shTime;
	}

	public String getPszd() {
		return pszd;
	}

	public void setPszd(String pszd) {
		this.pszd = pszd;
	}

	public String getStkId() {
		return stkId;
	}

	public void setStkId(String stkId) {
		this.stkId = stkId;
	}

	public String getOrderxx() {
		return orderxx;
	}

	public void setOrderxx(String orderxx) {
		this.orderxx = orderxx;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean success) {
		isSuccess = success;
	}

	public int getSubmitCount() {
		return submitCount;
	}

	public void setSubmitCount(int submitCount) {
		this.submitCount = submitCount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getKhNm() {
		return khNm;
	}

	public void setKhNm(String khNm) {
		this.khNm = khNm;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getAutoType() {
		return autoType;
	}

	public void setAutoType(int autoType) {
		this.autoType = autoType;
	}

	public String getStkName() {
		return stkName;
	}

	public void setStkName(String stkName) {
		this.stkName = stkName;
	}
}
