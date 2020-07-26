package com.qwb.view.work.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 班次列表
 */
public class WorkClassListResult extends BaseBean {
	private Integer total;
	private List<WorkBean> rows;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<WorkBean> getRows() {
		return rows;
	}

	public void setRows(List<WorkBean> rows) {
		this.rows = rows;
	}
}
