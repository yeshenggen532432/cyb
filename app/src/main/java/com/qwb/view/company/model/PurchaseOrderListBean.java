package com.qwb.view.company.model;

import com.qwb.view.base.model.BaseBean;
import java.util.List;

/**
 * 采购单列表
 */
public class PurchaseOrderListBean extends BaseBean {

	private int total;
	private List<PurchaseOrderBean> rows;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<PurchaseOrderBean> getRows() {
		return rows;
	}

	public void setRows(List<PurchaseOrderBean> rows) {
		this.rows = rows;
	}
}
