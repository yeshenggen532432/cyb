package com.qwb.view.work.model;


import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 考勤列表
 */

public class WorkListResult extends BaseBean{

	private static final long serialVersionUID = 6446258651139013796L;
	
	private List<KaoqianListBean> rows=new ArrayList<>();
	

	public List<KaoqianListBean> getRows() {
		return rows;
	}


	public void setRows(List<KaoqianListBean> rows) {
		this.rows = rows;
	}


	public class KaoqianListBean implements Serializable {
		private static final long serialVersionUID = -5698731733538728090L;
		private String psnId ;
		private String memberName ;//员工名称
		private String checkTime ;
		private List<WorkSubBean> list=new ArrayList<>();
		public String getPsnId() {
			return psnId;
		}
		public void setPsnId(String psnId) {
			this.psnId = psnId;
		}
		public String getCheckTime() {
			return checkTime;
		}
		public void setCheckTime(String checkTime) {
			this.checkTime = checkTime;
		}
		public List<WorkSubBean> getList() {
			return list;
		}
		public void setList(List<WorkSubBean> list) {
			this.list = list;
		}
		public String getMemberName() {
			return memberName;
		}
		public void setMemberName(String memberName) {
			this.memberName = memberName;
		}
	}	
		
}
