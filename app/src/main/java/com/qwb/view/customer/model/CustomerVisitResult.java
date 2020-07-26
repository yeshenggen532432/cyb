package com.qwb.view.customer.model;

import com.qwb.view.base.model.BasePageBean;

import java.util.List;

/**
 * 客户拜访（我的客户）
 */
public class CustomerVisitResult extends BasePageBean {

	private List<CustomerVisitBean> rows;

	public List<CustomerVisitBean> getRows() {
		return rows;
	}

	public void setRows(List<CustomerVisitBean> rows) {
		this.rows = rows;
	}
}
