package com.qwb.db;


import org.litepal.crud.LitePalSupport;

/**
 *  记录拜访状态
 */
public class CacheBFStatuBean extends LitePalSupport {
	private String userId;//用户id
	private String companyId;//公司id

	private String cid ;//客户id
	private String step ;//1,2,3,4,5,6
	private String statu ;//已上传，已缓存，未上传
	private String dId ;//订单id
	private String date ;

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

	public String getStatu() {
		return statu;
	}

	public void setStatu(String statu) {
		this.statu = statu;
	}

	public String getdId() {
		return dId;
	}

	public void setdId(String dId) {
		this.dId = dId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
