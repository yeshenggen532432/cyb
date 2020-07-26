package com.qwb.view.customer.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 客户管理--我的客户
 *
 */
public class MineClientBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2919679221503290971L;
	private Integer pageNo;
	private Integer pageSize;
	private Integer total;
	private Integer totalPage;
	private List<MineClientInfo> rows;

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

	public List<MineClientInfo> getRows() {
		return rows;
	}

	public void setRows(List<MineClientInfo> rows) {
		this.rows = rows;
	}

}
