package com.qwb.view.tab.model;


import com.qwb.view.base.model.BaseBean;

/**
 * 我的供货商
 */
public class SupplierBean extends BaseBean{
//		"fdId": 3,
//		"companyId": 285,
//		"outTime": "",
//		"memberId": 1456,
//		"memberNm": "测试会员",
//		"memberMobile": "13950104773",
//		"memberCompany": "厦门企微宝网络科技有限公司",
//		"inTime": "2018-44-16"
    private int fdId;
    private int companyId;
    private int memberId;
	private String outTime;
	private String memberNm;
	private String memberMobile;
	private String memberCompany;
	private String inTime;

	//自己添加
	private int cacheCount;

	public int getFdId() {
		return fdId;
	}

	public void setFdId(int fdId) {
		this.fdId = fdId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getMemberNm() {
		return memberNm;
	}

	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
	}

	public String getMemberMobile() {
		return memberMobile;
	}

	public void setMemberMobile(String memberMobile) {
		this.memberMobile = memberMobile;
	}

	public String getMemberCompany() {
		return memberCompany;
	}

	public void setMemberCompany(String memberCompany) {
		this.memberCompany = memberCompany;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public int getCacheCount() {
		return cacheCount;
	}

	public void setCacheCount(int cacheCount) {
		this.cacheCount = cacheCount;
	}
}
