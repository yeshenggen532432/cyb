package com.qwb.view.member.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

public class BuMenListBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6678560347025308939L;
	private List<BuMenItem> departls ;
	private List<MemberBean> memls ;
	
	public List<BuMenItem> getDepartls() {
		return departls;
	}

	public List<MemberBean> getMemls() {
		return memls;
	}

	public void setMemls(List<MemberBean> memls) {
		this.memls = memls;
	}

	public void setDepartls(List<BuMenItem> departls) {
		this.departls = departls;
	}

	public class BuMenItem implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 290253597565079710L;
		private int parentId ;
		private int branchId ;//部门id
		private int num ;//数量
		private String branchName ;//部门名
		private String branchPath ;
		private String branchLeaf ;
		private String swSb ;
		private String xwSb ;
		private String swXb ;
		private String xwXb ;
		private String branchMemo ;
		public int getParentId() {
			return parentId;
		}
		public void setParentId(int parentId) {
			this.parentId = parentId;
		}
		public int getBranchId() {
			return branchId;
		}
		public void setBranchId(int branchId) {
			this.branchId = branchId;
		}
		public String getBranchName() {
			return branchName;
		}
		public void setBranchName(String branchName) {
			this.branchName = branchName;
		}
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public String getBranchPath() {
			return branchPath;
		}
		public void setBranchPath(String branchPath) {
			this.branchPath = branchPath;
		}
		public String getBranchLeaf() {
			return branchLeaf;
		}
		public void setBranchLeaf(String branchLeaf) {
			this.branchLeaf = branchLeaf;
		}
		public String getSwSb() {
			return swSb;
		}
		public void setSwSb(String swSb) {
			this.swSb = swSb;
		}
		public String getXwSb() {
			return xwSb;
		}
		public void setXwSb(String xwSb) {
			this.xwSb = xwSb;
		}
		public String getSwXb() {
			return swXb;
		}
		public void setSwXb(String swXb) {
			this.swXb = swXb;
		}
		public String getXwXb() {
			return xwXb;
		}
		public void setXwXb(String xwXb) {
			this.xwXb = xwXb;
		}
		public String getBranchMemo() {
			return branchMemo;
		}
		public void setBranchMemo(String branchMemo) {
			this.branchMemo = branchMemo;
		}
	}
	
	public static class MemberBean implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1747712754215370688L;
		private String memberNm ;
		private String memberMobile ;
		private String memberHead ;
		private String firstChar ;
		private boolean isCheck ;
		private int cy ;
		private String role ;
		private int memberId ;
		
		
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public boolean isCheck() {
			return isCheck;
		}
		public void setCheck(boolean isCheck) {
			this.isCheck = isCheck;
		}
		public int getCy() {
			return cy;
		}
		public void setCy(int cy) {
			this.cy = cy;
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
		public String getMemberHead() {
			return memberHead;
		}
		public void setMemberHead(String memberHead) {
			this.memberHead = memberHead;
		}
		public String getFirstChar() {
			return firstChar;
		}
		public void setFirstChar(String firstChar) {
			this.firstChar = firstChar;
		}
		public int getMemberId() {
			return memberId;
		}
		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}
		@Override
		public String toString() {
			return "MemberBean [memberNm=" + memberNm + ", memberMobile="
					+ memberMobile + ", memberHead=" + memberHead
					+ ", firstChar=" + firstChar + ", isCheck=" + isCheck
					+ ", cy=" + cy + ", role=" + role + ", memberId="
					+ memberId + "]";
		}
		
		
		
	}
	
}
