package com.qwb.view.checkstorage.model;

import java.io.Serializable;

/**
 * 盘点仓库
 */
public class CheckStorageBean implements Serializable {
	public int mId;
	public int imgRes;
	public String title;
	public String name;
	public String tp;
	public String nameVal;
	public String bigVal;
	public String smallVal;

	public String getNameVal() {
		return nameVal;
	}

	public void setNameVal(String nameVal) {
		this.nameVal = nameVal;
	}

	public String getBigVal() {
		return bigVal;
	}

	public void setBigVal(String bigVal) {
		this.bigVal = bigVal;
	}

	public String getSmallVal() {
		return smallVal;
	}

	public void setSmallVal(String smallVal) {
		this.smallVal = smallVal;
	}

	public int getImgRes() {
		return imgRes;
	}
	public void setImgRes(int imgRes) {
		this.imgRes = imgRes;
	}
	public int getmId() {
		return mId;
	}
	public void setmId(int mId) {
		this.mId = mId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getTp() {
		return tp;
	}
	public void setTp(String tp) {
		this.tp = tp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public CheckStorageBean() {
		super();
	}
	public CheckStorageBean(int mId, int imgRes, String name, String tp) {
		super();
		this.mId = mId;
		this.imgRes = imgRes;
		this.name = name;
		this.tp = tp;
	}
	public CheckStorageBean(int mId, int imgRes, String name, String tp, String title) {
		super();
		this.mId = mId;
		this.imgRes = imgRes;
		this.name = name;
		this.tp = tp;
		this.title = title;
	}

	
	
}
