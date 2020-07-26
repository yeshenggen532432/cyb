package com.qwb.view.storehouse.model;

import com.qwb.view.base.model.BaseBean;

/**
 * 待出仓发票
 */
public class StorehouseStayOutResult extends BaseBean {

	private StorehouseStayOutBean data;

	public StorehouseStayOutBean getData() {
		return data;
	}

	public void setData(StorehouseStayOutBean data) {
		this.data = data;
	}
}
