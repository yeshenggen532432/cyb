package com.qwb.view.storehouse.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 库位整理单
 */
public class StorehouseArrangeListResult extends BaseBean {

	private List<StorehouseInBean> rows;

	public List<StorehouseInBean> getRows() {
		return rows;
	}

	public void setRows(List<StorehouseInBean> rows) {
		this.rows = rows;
	}
}
