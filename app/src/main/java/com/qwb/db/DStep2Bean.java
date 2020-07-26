package com.qwb.db;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 *  拜访步骤2：生动化检查
 */
public class DStep2Bean extends LitePalSupport {
	private String userId;//用户id
	private String companyId;//公司id

	private String bfId;
	private String count;//0:未上传，1：已上传
	private String cid;//客户id
	private String pophb;
	private String cq;
	private String wq;
	private String isXy;
	private String remo1;
	private String remo2;
	private List<String> picList = new ArrayList<>();//图片
	private List<String> picList2 = new ArrayList<>();//图片
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

	public String getBfId() {
		return bfId;
	}

	public void setBfId(String bfId) {
		this.bfId = bfId;
	}

	public String getPophb() {
		return pophb;
	}

	public void setPophb(String pophb) {
		this.pophb = pophb;
	}

	public String getCq() {
		return cq;
	}

	public void setCq(String cq) {
		this.cq = cq;
	}

	public String getWq() {
		return wq;
	}

	public void setWq(String wq) {
		this.wq = wq;
	}

	public String getIsXy() {
		return isXy;
	}

	public void setIsXy(String isXy) {
		this.isXy = isXy;
	}

	public String getRemo1() {
		return remo1;
	}

	public void setRemo1(String remo1) {
		this.remo1 = remo1;
	}

	public String getRemo2() {
		return remo2;
	}

	public void setRemo2(String remo2) {
		this.remo2 = remo2;
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
