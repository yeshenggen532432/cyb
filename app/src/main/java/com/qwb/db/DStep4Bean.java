package com.qwb.db;

import org.litepal.crud.LitePalSupport;

/**
 *  拜访步骤4：销售小结
 */
public class DStep4Bean extends LitePalSupport {
	private String userId;//用户id
	private String companyId;//公司id

	private String count;//0:未上传，1：已上传
	private String cid;//客户id
	private String xsxj;//json字符串
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

	public String getXsxj() {
		return xsxj;
	}

	public void setXsxj(String xsxj) {
		this.xsxj = xsxj;
	}

	public String getKhNm() {
		return khNm;
	}

	public void setKhNm(String khNm) {
		this.khNm = khNm;
	}
}
