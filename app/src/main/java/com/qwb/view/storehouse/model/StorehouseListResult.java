package com.qwb.view.storehouse.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 库位
 */
public class StorehouseListResult extends BaseBean {

	private List<StorehouseBean> list;

	public List<StorehouseBean> getList() {
		return list;
	}

	public void setList(List<StorehouseBean> list) {
		this.list = list;
	}
}
