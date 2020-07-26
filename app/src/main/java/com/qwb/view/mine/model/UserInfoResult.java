package com.qwb.view.mine.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;

/**
 * 用户信息
 */
public class UserInfoResult extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5436826919552422339L;

	private UserInfoBean sysMember ;
	
	
	public UserInfoBean getSysMember() {
		return sysMember;
	}


	public void setSysMember(UserInfoBean sysMember) {
		this.sysMember = sysMember;
	}


	public class UserInfoBean implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4122388581928415235L;
		private int memberId ;
		//账号
		private String memberNm ;
		private String email ;
		//姓名
		private String memberName ;
		private String memberMobile ;
		//所在地
		private String memberHometown ;
		//黑名单
		private String memberBlacklist ;
		//关注
		private String memberAttentions ;
		//性别
		private String sex ;
		private String memberCompany ;
		private String memberHead ;
		//职业
		private String memberJob ;
		//部门
		private String branchName ;
			//简介
		private String memberDesc ;
		
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getMemberName() {
			return memberName;
		}
		public void setMemberName(String memberName) {
			this.memberName = memberName;
		}
		public int getMemberId() {
			return memberId;
		}
		public void setMemberId(int memberId) {
			this.memberId = memberId;
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
		public String getMemberHometown() {
			return memberHometown;
		}
		public void setMemberHometown(String memberHometown) {
			this.memberHometown = memberHometown;
		}
		public String getMemberCompany() {
			return memberCompany;
		}
		public void setMemberCompany(String memberCompany) {
			this.memberCompany = memberCompany;
		}
		public String getMemberJob() {
			return memberJob;
		}
		public void setMemberJob(String memberJob) {
			this.memberJob = memberJob;
		}
		public String getMemberDesc() {
			return memberDesc;
		}
		public void setMemberDesc(String memberDesc) {
			this.memberDesc = memberDesc;
		}
		public String getMemberHead() {
			return memberHead;
		}
		public void setMemberHead(String memberHead) {
			this.memberHead = memberHead;
		}
		public String getMemberBlacklist() {
			return memberBlacklist;
		}
		public void setMemberBlacklist(String memberBlacklist) {
			this.memberBlacklist = memberBlacklist;
		}
		public String getMemberAttentions() {
			return memberAttentions;
		}
		public void setMemberAttentions(String memberAttentions) {
			this.memberAttentions = memberAttentions;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public String getBranchName() {
			return branchName;
		}
		public void setBranchName(String branchName) {
			this.branchName = branchName;
		}
		
	}
	
}
