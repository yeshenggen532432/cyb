package com.qwb.view.shop.model;

import com.qwb.view.base.model.BaseBean;

/**
 * 供货商-订单信息
 */
public class ShopAddressInfo extends BaseBean{

//"address": "福建省厦门市思明区湖滨南路815号-1-103号",
//		"id": 1,
//		"isDefault": "0",
//		"memId": 1458,
//		"longitude": "118.12552357497869",
//		"latitude": "24.48331650314069",
//		"mobile": "叶生根",
//		"hyId": 5,
//		"createTime": "Fri Sep 14 17:34:07 CST 2018",
//		"linkman": "13950104779"
    private int id;
    private int hyId;
    private int memId;
	private String isDefault;
	private String address;
	private String longitude;
	private String latitude;
	private String mobile;
	private String linkman;
	private String createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHyId() {
		return hyId;
	}

	public void setHyId(int hyId) {
		this.hyId = hyId;
	}

	public int getMemId() {
		return memId;
	}

	public void setMemId(int memId) {
		this.memId = memId;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
