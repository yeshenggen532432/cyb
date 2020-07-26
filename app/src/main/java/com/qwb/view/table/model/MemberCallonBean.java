package com.qwb.view.table.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 拜访查询--拜访纪录
 * 
 */
public class MemberCallonBean extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3390911383674875237L;
	private Integer pageNo;
	private Integer pageSize;
	private Integer total;
	private Integer totalPage;

	private List<YwCanllon> rows = new ArrayList<>();

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

	public List<YwCanllon> getRows() {
		return rows;
	}

	public void setRows(List<YwCanllon> rows) {
		this.rows = rows;
	}

	public class YwCanllon implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4364553981322074418L;
		private String bfbz; // 完成拜访步骤
		private String timed; // 时间
		private Integer cid; // 客户id
		private Integer id; // 拜访id（拜访流程--拜访查询）
		private String memberNm; // 业务员名称
		private String khNm; // 客户名称
		private String khdjNm; // 客户等级
		private String qddate; // 日期
		private Integer mid; // 业务员id

		public String getBfbz() {
			return bfbz;
		}

		public void setBfbz(String bfbz) {
			this.bfbz = bfbz;
		}


		public String getTimed() {
			return timed;
		}

		public void setTimed(String timed) {
			this.timed = timed;
		}

		public Integer getCid() {
			return cid;
		}

		public void setCid(Integer cid) {
			this.cid = cid;
		}

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

		public String getQddate() {
			return qddate;
		}

		public void setQddate(String qddate) {
			this.qddate = qddate;
		}

		public Integer getMid() {
			return mid;
		}

		public void setMid(Integer mid) {
			this.mid = mid;
		}

	}
}
