package com.qwb.view.plan.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 拜访计划-线路
 * 
 */
public class PlanLineResult extends BaseBean {
	private Integer pageNo;
	private Integer pageSize;
	private Integer total;
	private Integer totalPage;
	private List<PlanLineDetailBean> rows;

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

	public List<PlanLineDetailBean> getRows() {
		return rows;
	}

	public void setRows(List<PlanLineDetailBean> rows) {
		this.rows = rows;
	}
}
