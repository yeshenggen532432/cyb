package com.qwb.view.checkstorage.model;

import com.qwb.view.base.model.BaseBean;
import java.util.List;

/**
 * 商品生产日期列表
 */
public class WareProduceDateListResult extends BaseBean {

	private List<WareProduceDateBean> rows;

	public List<WareProduceDateBean> getRows() {
		return rows;
	}

	public void setRows(List<WareProduceDateBean> rows) {
		this.rows = rows;
	}
}
