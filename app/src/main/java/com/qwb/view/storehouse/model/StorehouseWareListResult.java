package com.qwb.view.storehouse.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 库位商品
 */
public class StorehouseWareListResult extends BaseBean {

	private List<StorehouseWareBean> list;

	public List<StorehouseWareBean> getList() {
		return list;
	}

	public void setList(List<StorehouseWareBean> list) {
		this.list = list;
	}

}
