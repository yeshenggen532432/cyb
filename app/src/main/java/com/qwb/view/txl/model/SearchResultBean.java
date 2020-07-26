package com.qwb.view.txl.model;

import java.io.Serializable;
import java.util.List;

public class SearchResultBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4317055881861709364L;
	private boolean state;
	private String msg;
	private List<ResultItems> lists;

	public List<ResultItems> getLists() {
		return lists;
	}

	public void setLists(List<ResultItems> lists) {
		this.lists = lists;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public class ResultItems implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2142110492095883852L;
		private String headUrl;
		private String nm;
		 /**1 成员 2 公司 3 部门4员工圈*/
		private String tp;
		/**
		 * 1 是公开圈
		 * 2 非公开圈
		 * 
		 * */
		private String datasource;
		private int belongId;
		/**
		 * 如果是圈 ：0不在该圈1创建者 2管理员3普通成员
		 * 用户/部门：1是好友或成员 2不是
		 * */
		private int whetherIn ;
		
		public String getDatasource() {
			return datasource;
		}

		public void setDatasource(String datasource) {
			this.datasource = datasource;
		}

		public int getWhetherIn() {
			return whetherIn;
		}

		public void setWhetherIn(int whetherIn) {
			this.whetherIn = whetherIn;
		}

		public String getHeadUrl() {
			return headUrl;
		}

		public void setHeadUrl(String headUrl) {
			this.headUrl = headUrl;
		}

		public String getNm() {
			return nm;
		}

		public void setNm(String nm) {
			this.nm = nm;
		}

		public String getTp() {
			return tp;
		}

		public void setTp(String tp) {
			this.tp = tp;
		}

		public int getBelongId() {
			return belongId;
		}

		public void setBelongId(int belongId) {
			this.belongId = belongId;
		}

	}

}
