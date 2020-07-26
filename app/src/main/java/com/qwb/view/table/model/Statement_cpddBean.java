package com.qwb.view.table.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 统计表——产品订单
 * 
 */
public class Statement_cpddBean extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3390911383674875237L;
	private Integer pageNo;
	private Integer pageSize;
	private Integer total;
	private Integer totalPage;

	private List<Cpdd> rows = new ArrayList<>();
	private List<FooterBean> footer = new ArrayList<>();

	public List<FooterBean> getFooter() {
		return footer;
	}

	public void setFooter(List<FooterBean> footer) {
		this.footer = footer;
	}

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

	public List<Cpdd> getRows() {
		return rows;
	}

	public void setRows(List<Cpdd> rows) {
		this.rows = rows;
	}

	public class Cpdd implements Serializable {
		
		private Integer id; //
		private String xsTp; //
		private String wareNm; //
		private Double wareDj; //
		private Double nums; //
		private Double zjs; //
		private String wareDw; // 单位
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getXsTp() {
			return xsTp;
		}
		public void setXsTp(String xsTp) {
			this.xsTp = xsTp;
		}
		public String getWareNm() {
			return wareNm;
		}
		public void setWareNm(String wareNm) {
			this.wareNm = wareNm;
		}
		public Double getWareDj() {
			return wareDj;
		}
		public void setWareDj(Double wareDj) {
			this.wareDj = wareDj;
		}
		public Double getNums() {
			return nums;
		}
		public void setNums(Double nums) {
			this.nums = nums;
		}
		public Double getZjs() {
			return zjs;
		}
		public void setZjs(Double zjs) {
			this.zjs = zjs;
		}

		public String getWareDw() {
			return wareDw;
		}

		public void setWareDw(String wareDw) {
			this.wareDw = wareDw;
		}
	}

}
