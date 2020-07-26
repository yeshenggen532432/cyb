package com.qwb.view.table.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 拜访查询--拜访纪录
 * 
 */
public class Statement_khbfBean extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3390911383674875237L;
	private Integer pageNo;
	private Integer pageSize;
	private Integer total;
	private Integer totalPage;

	private List<KhBf> rows = new ArrayList<>();

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

	public List<KhBf> getRows() {
		return rows;
	}

	public void setRows(List<KhBf> rows) {
		this.rows = rows;
	}

	public class KhBf implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6950119282255628213L;
		private Integer id; // 拜访id（拜访流程--拜访查询）
		private String memberNm; // 业务员名称
		private String khNm; // 客户名称
		private String khdjNm; // 客户名称
		private String bfpl; // 客户名称
		private String khTp; // 客户名称
		private String createTime; // 客户名称
		private String scbfDate; // 客户名称
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
		public String getKhdjNm() {
			return khdjNm;
		}
		public void setKhdjNm(String khdjNm) {
			this.khdjNm = khdjNm;
		}
		public String getBfpl() {
			return bfpl;
		}
		public void setBfpl(String bfpl) {
			this.bfpl = bfpl;
		}
		public String getKhTp() {
			return khTp;
		}
		public void setKhTp(String khTp) {
			this.khTp = khTp;
		}
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
		public String getScbfDate() {
			return scbfDate;
		}
		public void setScbfDate(String scbfDate) {
			this.scbfDate = scbfDate;
		}

	}	
}
