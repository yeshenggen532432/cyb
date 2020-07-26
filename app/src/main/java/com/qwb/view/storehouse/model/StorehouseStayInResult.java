package com.qwb.view.storehouse.model;

import com.qwb.view.base.model.BaseBean;

/**
 * 待入仓发票信息
 */
public class StorehouseStayInResult extends BaseBean {

	private StorehouseStayInBean data;

	public StorehouseStayInBean getData() {
		return data;
	}

	public void setData(StorehouseStayInBean data) {
		this.data = data;
	}
}
