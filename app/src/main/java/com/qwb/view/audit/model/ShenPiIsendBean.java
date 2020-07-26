package com.qwb.view.audit.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

public class ShenPiIsendBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6861005819642126980L;
	private List<ShenPiIsendItemBean> auditList ;
	private int total ;
	private int totalPage ;//总页数
	
	public int getTotal() {
		return total;
	}


	public  int getTotalPage() {
		return totalPage;
	}


	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}


	public void setTotal(int total) {
		this.total = total;
	}


	public List<ShenPiIsendItemBean> getAuditList() {
		return auditList;
	}


	public void setAuditList(List<ShenPiIsendItemBean> auditList) {
		this.auditList = auditList;
	}


	public class ShenPiIsendItemBean implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8888057374865086115L;
		/**
		 * (1 请假 2 报销 3 出差 4 物品领用5 通用审批',）
		 * */
		private String title ;//标题
		private String auditTp ;
		private String stime ;
		private String auditNo ;
		private String memberNm ;
		private int memberId ;
		
		
		/**
		 * 流程到那个审批人
		 * */
		private String checkNm ;
		
		/**
		 * 2 表示还在审批中
		 * 1 表示审批完成
		 * 1-1 完成 1-2 拒绝 1-3 撤销
		 * 
		 * */
		private String isOver ;
		
		
		
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getStime() {
			return stime;
		}
		public void setStime(String stime) {
			this.stime = stime;
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
		public int getMemberId() {
			return memberId;
		}
		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}
		public String getAuditTp() {
			return auditTp;
		}
		public void setAuditTp(String auditTp) {
			this.auditTp = auditTp;
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
		
	}
}
