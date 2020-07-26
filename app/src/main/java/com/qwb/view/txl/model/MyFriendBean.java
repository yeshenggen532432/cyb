package com.qwb.view.txl.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

public class MyFriendBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1416926005615205382L;
	private int totalPage ;
	private List<FriendInfor> rows ;
	
	
	public int getTotalPage() {
		return totalPage;
	}


	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}


	public List<FriendInfor> getRows() {
		return rows;
	}


	public void setRows(List<FriendInfor> rows) {
		this.rows = rows;
	}


	public class FriendInfor implements Serializable,Comparable<FriendInfor> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5575323848781418536L;
		private String memberNm ;
		private String memberMobile ;
		private String memberHead ;
		private String isShield ;
		private String bindTp ;
		private String isUsed ;
		private String firstChar ;
		private int bindId ;
		private int bindMemberId ;
		private int memberId ;
		
		
		public String getMemberMobile() {
			return memberMobile;
		}
		public void setMemberMobile(String memberMobile) {
			this.memberMobile = memberMobile;
		}
		public String getFirstChar() {
			return firstChar;
		}
		public void setFirstChar(String firstChar) {
			this.firstChar = firstChar;
		}
		public String getMemberNm() {
			return memberNm;
		}
		public void setMemberNm(String memberNm) {
			this.memberNm = memberNm;
		}
		public String getMemberHead() {
			return memberHead;
		}
		public void setMemberHead(String memberHead) {
			this.memberHead = memberHead;
		}
		public String getIsShield() {
			return isShield;
		}
		public void setIsShield(String isShield) {
			this.isShield = isShield;
		}
		public String getBindTp() {
			return bindTp;
		}
		public void setBindTp(String bindTp) {
			this.bindTp = bindTp;
		}
		public String getIsUsed() {
			return isUsed;
		}
		public void setIsUsed(String isUsed) {
			this.isUsed = isUsed;
		}
		public int getBindId() {
			return bindId;
		}
		public void setBindId(int bindId) {
			this.bindId = bindId;
		}
		public int getBindMemberId() {
			return bindMemberId;
		}
		public void setBindMemberId(int bindMemberId) {
			this.bindMemberId = bindMemberId;
		}
		public int getMemberId() {
			return memberId;
		}
		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}
		@Override
		public int compareTo(FriendInfor another) {
			String addTime2 = another.getFirstChar();
            if (addTime2 == null)
            {
                return -1 ;
            }
            String addTime3 = this.getFirstChar();
            if (addTime3 == null)
            {
                return -1 ;
            }
            int compareTo = addTime3.compareTo(addTime2);
            return compareTo;
		}
		
	}
}
