package com.qwb.db;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 *  拜访步骤3：库存检查
 */
public class DStep3Bean extends LitePalSupport {
	private String userId;//用户id
	private String companyId;//公司id

	private String count;//0:未上传，1：已上传
	private String cid;//客户id
	private String xxjh;
	private List<String> picList = new ArrayList<>();//图片
	private List<String> picList2 = new ArrayList<>();//图片
	private List<String> picList3 = new ArrayList<>();//图片
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

	public String getXxjh() {
		return xxjh;
	}

	public void setXxjh(String xxjh) {
		this.xxjh = xxjh;
	}

	public List<String> getPicList() {
		return picList;
	}

	public void setPicList(List<String> picList) {
		this.picList = picList;
	}

	public List<String> getPicList2() {
		return picList2;
	}

	public void setPicList2(List<String> picList2) {
		this.picList2 = picList2;
	}

	public List<String> getPicList3() {
		return picList3;
	}

	public void setPicList3(List<String> picList3) {
		this.picList3 = picList3;
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

	public String getKhNm() {
		return khNm;
	}

	public void setKhNm(String khNm) {
		this.khNm = khNm;
	}
}
