package com.qwb.view.order.model;

import com.qwb.view.base.model.BasePageBean;
import java.util.List;

/**
 * 下单(订货下单，车销单，退货单)
 */
public class OrderListResult extends BasePageBean {

	private List<OrderBean> rows;

	public List<OrderBean> getRows() {
		return rows;
	}

	public void setRows(List<OrderBean> rows) {
		this.rows = rows;
	}
}
