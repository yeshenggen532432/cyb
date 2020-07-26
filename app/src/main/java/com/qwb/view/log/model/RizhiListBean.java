package com.qwb.view.log.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 所有用户轨迹列表
 * 
 */
public class RizhiListBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2712419169734819494L;
	private int pageNo;
	private int pageSize;
	private int total;
	private int totalPage;
	private List<RizhiList> rows;

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public List<RizhiList> getRows() {
		return rows;
	}

	public void setRows(List<RizhiList> rows) {
		this.rows = rows;
	}

	public static class RizhiList implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7962949315625050819L;
		private int id;
		private int tp;
		private String gzNr;
		private String gzZj;
		private String gzJh;
		private String gzBz;
		private String memberNm;
		private String fbTime;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getTp() {
			return tp;
		}

		public void setTp(int tp) {
			this.tp = tp;
		}

		public String getGzNr() {
			return gzNr;
		}

		public void setGzNr(String gzNr) {
			this.gzNr = gzNr;
		}

		public String getGzZj() {
			return gzZj;
		}

		public void setGzZj(String gzZj) {
			this.gzZj = gzZj;
		}

		public String getGzJh() {
			return gzJh;
		}

		public void setGzJh(String gzJh) {
			this.gzJh = gzJh;
		}

		public String getGzBz() {
			return gzBz;
		}

		public void setGzBz(String gzBz) {
			this.gzBz = gzBz;
		}

		public String getMemberNm() {
			return memberNm;
		}

		public void setMemberNm(String memberNm) {
			this.memberNm = memberNm;
		}

		public String getFbTime() {
			return fbTime;
		}

		public void setFbTime(String fbTime) {
			this.fbTime = fbTime;
		}

	}

}
