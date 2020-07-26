package com.qwb.db;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 *  缓存我的客户（全部）
 */
public class DStep1Bean extends LitePalSupport {
	private String userId;//用户id
	private String companyId;//公司id

	private String count;//0:未上传，1：已上传
	private String cid;//客户id
	private String longitude;
	private String latitude;
	private String address;
	private String hbzt;
	private String ggyy;
	private String isXy;
	private String remo;
	private boolean isSuccess;
	private String pId;
	private String time;
	private List<String> picList = new ArrayList<>();//图片
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHbzt() {
		return hbzt;
	}

	public void setHbzt(String hbzt) {
		this.hbzt = hbzt;
	}

	public String getGgyy() {
		return ggyy;
	}

	public void setGgyy(String ggyy) {
		this.ggyy = ggyy;
	}

	public String getIsXy() {
		return isXy;
	}

	public void setIsXy(String isXy) {
		this.isXy = isXy;
	}

	public String getRemo() {
		return remo;
	}

	public void setRemo(String remo) {
		this.remo = remo;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean success) {
		isSuccess = success;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<String> getPicList() {
		return picList;
	}

	public void setPicList(List<String> picList) {
		this.picList = picList;
	}

	public int getSubmitCount() {
		return submitCount;
	}

	public void setSubmitCount(int submitCount) {
		this.submitCount = submitCount;
	}

	public String getKhNm() {
		return khNm;
	}

	public void setKhNm(String khNm) {
		this.khNm = khNm;
	}
}
