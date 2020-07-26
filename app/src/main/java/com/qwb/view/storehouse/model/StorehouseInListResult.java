package com.qwb.view.storehouse.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 库位:入仓单列表
 */
public class StorehouseInListResult extends BaseBean {

	private List<StorehouseInBean> list;

	public List<StorehouseInBean> getList() {
		return list;
	}

	public void setList(List<StorehouseInBean> list) {
		this.list = list;
	}
}
