package com.qwb.view.storehouse.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 待入仓发票
 */
public class StorehouseStayInListResult extends BaseBean {

	private int total;
	private List<StorehouseStayInBean> list;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<StorehouseStayInBean> getList() {
		return list;
	}

	public void setList(List<StorehouseStayInBean> list) {
		this.list = list;
	}
}
