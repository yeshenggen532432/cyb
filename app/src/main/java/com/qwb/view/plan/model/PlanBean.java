package com.qwb.view.plan.model;


import java.io.Serializable;

/**
 * 计划拜访--查询计划拜访
 */
public class PlanBean implements Serializable{

	private String address;   // 地址
	private Integer id;       //计划id
	private String memberNm;  //业务员名称
	private String khNm;      //客户名称
	private String branchName;//部门名称
	private String tel;//电话
	private String mobile;//电话
	private String linkman;//联系人
	private Integer mid;      //业务员id
	private Integer cid;      //客户id
	private Integer isWc;     //是否完成（1是；2否）
	private String scbfDate;     //上次拜访时间


	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getScbfDate() {
		return scbfDate;
	}

	public void setScbfDate(String scbfDate) {
		this.scbfDate = scbfDate;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMemberNm() {
		return memberNm;
	}

	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
	}

	public String getKhNm() {
		return khNm;
	}

	public void setKhNm(String khNm) {
		this.khNm = khNm;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Integer getMid() {
		return mid;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer getIsWc() {
		return isWc;
	}

	public void setIsWc(Integer isWc) {
		this.isWc = isWc;
	}

}

