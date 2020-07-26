package com.qwb.view.delivery.model;

import com.qwb.view.base.model.BaseBean;
import java.util.List;

/**
 * 配送单列表
 */
public class DeliveryListBean extends BaseBean {
	private int pageNo;
	private int pageSize;
	private int total;
	private int totalPage;
	private List<DeliveryBean> rows;

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<DeliveryBean> getRows() {
		return rows;
	}

	public void setRows(List<DeliveryBean> rows) {
		this.rows = rows;
	}


}
