package com.qwb.view.customer.model;

import com.qwb.view.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 周边客户--信息
 * @author yeshenggen
 *
 */
public class NearClientBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3975901681138012193L;

	private Integer pageNo;
	private Integer pageSize;
	private Integer total;
	private Integer totalPage;
	private List<NearClientInfo> rows = new ArrayList<>();

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public List<NearClientInfo> getRows() {
		return rows;
	}

	public void setRows(List<NearClientInfo> rows) {
		this.rows = rows;
	}
}
