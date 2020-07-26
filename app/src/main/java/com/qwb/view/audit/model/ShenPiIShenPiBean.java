package com.qwb.view.audit.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

public class ShenPiIShenPiBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5030675440357729680L;
	/**
	 * 
	 */
	private List<ShenPiIShenPiItemBean> checkAuditList ;
	private int total ;
	private int totalPage ;
	
	


	public int getTotalPage() {
		return totalPage;
	}



	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}



	public int getTotal() {
		return total;
	}



	public void setTotal(int total) {
		this.total = total;
	}



	public List<ShenPiIShenPiItemBean> getCheckAuditList() {
		return checkAuditList;
	}



	public void setCheckAuditList(List<ShenPiIShenPiItemBean> checkAuditList) {
		this.checkAuditList = checkAuditList;
	}



	public class ShenPiIShenPiItemBean implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2734919385931163054L;
		private String title ;
		private String memberHead ;
		private String auditNo ;
		private String memberNm ;
		private String checkNm ;
		private String isOver ;
		private String checkTime ;
 		private String auditTp ;
		private int memberId ;
		
		


		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getMemberHead() {
			return memberHead;
		}

		public void setMemberHead(String memberHead) {
			this.memberHead = memberHead;
		}

		public String getAuditNo() {
			return auditNo;
		}

		public void setAuditNo(String auditNo) {
			this.auditNo = auditNo;
		}

		public String getMemberNm() {
			return memberNm;
		}

		public void setMemberNm(String memberNm) {
			this.memberNm = memberNm;
		}

		public String getCheckNm() {
			return checkNm;
		}

		public void setCheckNm(String checkNm) {
			this.checkNm = checkNm;
		}

		public String getIsOver() {
			return isOver;
		}

		public void setIsOver(String isOver) {
			this.isOver = isOver;
		}

		public String getCheckTime() {
			return checkTime;
		}

		public void setCheckTime(String checkTime) {
			this.checkTime = checkTime;
		}

		public String getAuditTp() {
			return auditTp;
		}

		public void setAuditTp(String auditTp) {
			this.auditTp = auditTp;
		}

		public int getMemberId() {
			return memberId;
		}

		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}
	}
}
