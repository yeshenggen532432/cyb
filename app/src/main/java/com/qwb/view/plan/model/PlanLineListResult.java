package com.qwb.view.plan.model;

import com.qwb.view.base.model.BasePageBean;

import java.util.List;

/**
 * 拜访计划模块--查询计划线路
 */
public class PlanLineListResult extends BasePageBean {

	private List<PlanLineBean> rows;

	public List<PlanLineBean> getRows() {
		return rows;
	}

	public void setRows(List<PlanLineBean> rows) {
		this.rows = rows;
	}
}
