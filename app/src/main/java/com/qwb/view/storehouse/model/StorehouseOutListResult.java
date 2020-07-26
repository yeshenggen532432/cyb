package com.qwb.view.storehouse.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 库位:出仓单列表
 */
public class StorehouseOutListResult extends BaseBean {

	private List<StorehouseOutBean> list;

	public List<StorehouseOutBean> getList() {
		return list;
	}

	public void setList(List<StorehouseOutBean> list) {
		this.list = list;
	}
}
