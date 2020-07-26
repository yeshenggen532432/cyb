package com.qwb.view.tab.model;

import com.qwb.view.base.model.BasePageBean;

import java.util.List;

/**
 */
public class ShopInfoResult extends BasePageBean {

	private List<ShopInfoBean> rows;

	public List<ShopInfoBean> getRows() {
		return rows;
	}

	public void setRows(List<ShopInfoBean> rows) {
		this.rows = rows;
	}
}
