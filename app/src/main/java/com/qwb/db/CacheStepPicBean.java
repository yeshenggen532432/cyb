package com.qwb.db;


import org.litepal.crud.LitePalSupport;

/**
 *  缓存拜访的图片的路径
 */
public class CacheStepPicBean extends LitePalSupport {
	private String userId;//用户id
	private String companyId;//公司id

	private String cid ;//客户id
	private String step ;//1,2,3,6
	private String picPath ;

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

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
}
