package com.qwb.db;




import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 *  记录拜访状态
 */
public class CacheStep1Bean extends LitePalSupport {
	private String userId;//用户id
	private String companyId;//公司id

	private String statu;//0:未上传，1：已上传
	private String cid;
	private String longitude;
	private String latitude;
	private String address;
	private String hbzt;
	private String ggyy;
	private String isXy;
	private String remo;

	private List<CacheStepPicBean> picBeans=new ArrayList<>();//图片

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

	public String getStatu() {
		return statu;
	}

	public void setStatu(String statu) {
		this.statu = statu;
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

	public String getRemo() {
		return remo;
	}

	public void setRemo(String remo) {
		this.remo = remo;
	}

	public String getIsXy() {
		return isXy;
	}

	public void setIsXy(String isXy) {
		this.isXy = isXy;
	}

	public List<CacheStepPicBean> getPicBeans() {
		return picBeans;
	}

	public void setPicBeans(List<CacheStepPicBean> picBeans) {
		this.picBeans = picBeans;
	}
}
