package com.qwb.view.member.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

public class BranchListBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9087152604597044999L;

	private List<BuMengItemBean> deptList;
	
	public List<BuMengItemBean> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<BuMengItemBean> deptList) {
		this.deptList = deptList;
	}

	public static class BuMengItemBean implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5550135355685939805L;
		private int parentId ;
		private int branchId ;
		private String branchName ;
		private String ischild ;//是否有子部门 1有 2 无
		private boolean isCheck ;
		
		public boolean isCheck() {
			return isCheck;
		}
		public void setCheck(boolean isCheck) {
			this.isCheck = isCheck;
		}
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
		public String getIschild() {
			return ischild;
		}
		public void setIschild(String ischild) {
			this.ischild = ischild;
		}
		
	}
}
