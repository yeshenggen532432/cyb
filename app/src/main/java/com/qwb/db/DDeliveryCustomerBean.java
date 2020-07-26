package com.qwb.db;

import org.litepal.crud.LitePalSupport;

/**
 *  配送单：选择要导航的客户
 */
public class DDeliveryCustomerBean extends LitePalSupport {
	private long id;//自增id
	private String userId;//用户id
	private String companyId;//公司id
	private String cid ;//客户id
	private String khNm ;
	private String address ;
	private String longitude ;
	private String latitude ;
	private String linkman ;
	private String mobile ;
	private String tel ;
	private Boolean isNav;//是否导航过
	private String billNo;//配送单单号
	private String psState;//状态

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getKhNm() {
		return khNm;
	}

	public void setKhNm(String khNm) {
		this.khNm = khNm;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Boolean getNav() {
		return isNav;
	}

	public void setNav(Boolean nav) {
		isNav = nav;
	}

	public String getPsState() {
		return psState;
	}

	public void setPsState(String psState) {
		this.psState = psState;
	}
}
