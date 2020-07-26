package com.qwb.view.storehouse.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 待出仓发票
 */
public class StorehouseStayOutListResult extends BaseBean {

	private int total;
	private List<StorehouseStayOutBean> list;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<StorehouseStayOutBean> getList() {
		return list;
	}

	public void setList(List<StorehouseStayOutBean> list) {
		this.list = list;
	}
}
