package com.qwb.db;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 *  拜访步骤6：拍照签退
 */
public class DStep6Bean extends LitePalSupport {
	private String userId;//用户id
	private String companyId;//公司id

	private String bfId;
	private String count;//0:未上传，1：已上传
	private String cid;//客户id
	private String longitude;
	private String latitude;
	private String address;
	private String bcbfzj;
	private String dbsx;
	private String xcdate;
	private String xsjdNm;
	private String bfflNm;
	private String voice;
	private int voiceTime;
	private List<String> picList = new ArrayList<>();//图片
	private boolean isSuccess;
	private String time;
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

	public String getBfId() {
		return bfId;
	}

	public void setBfId(String bfId) {
		this.bfId = bfId;
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

	public String getBcbfzj() {
		return bcbfzj;
	}

	public void setBcbfzj(String bcbfzj) {
		this.bcbfzj = bcbfzj;
	}

	public String getDbsx() {
		return dbsx;
	}

	public void setDbsx(String dbsx) {
		this.dbsx = dbsx;
	}

	public String getXcdate() {
		return xcdate;
	}

	public void setXcdate(String xcdate) {
		this.xcdate = xcdate;
	}

	public String getXsjdNm() {
		return xsjdNm;
	}

	public void setXsjdNm(String xsjdNm) {
		this.xsjdNm = xsjdNm;
	}

	public String getBfflNm() {
		return bfflNm;
	}

	public void setBfflNm(String bfflNm) {
		this.bfflNm = bfflNm;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public int getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(int voiceTime) {
		this.voiceTime = voiceTime;
	}

	public List<String> getPicList() {
		return picList;
	}

	public void setPicList(List<String> picList) {
		this.picList = picList;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean success) {
		isSuccess = success;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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
