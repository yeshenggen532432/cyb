package com.qwb.view.base.model;

/**
 *	版本更新
 */
public class VersionBean {
//	versionContent	:	修改已知bug
//	isQz	:	2
//	appNo	:	210
//	id	:	208549631356862460
//	versionName	:	2.1.0
//	versionUrl	:	http://static.t.qweib.com/publicplat/app/2019/11/13/80f57cf2de7849a1a223bfdc6a7ca42d.apk

	private  Integer versionType;
	private  String versionContent;
	private  String isQz;
	private  Integer appNo;
	private  Long id;
	private  String versionName;
	private  String versionUrl;

	public Integer getVersionType() {
		return versionType;
	}

	public void setVersionType(Integer versionType) {
		this.versionType = versionType;
	}

	public String getVersionContent() {
		return versionContent;
	}

	public void setVersionContent(String versionContent) {
		this.versionContent = versionContent;
	}

	public String getIsQz() {
		return isQz;
	}

	public void setIsQz(String isQz) {
		this.isQz = isQz;
	}

	public Integer getAppNo() {
		return appNo;
	}

	public void setAppNo(Integer appNo) {
		this.appNo = appNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionUrl() {
		return versionUrl;
	}

	public void setVersionUrl(String versionUrl) {
		this.versionUrl = versionUrl;
	}
}
