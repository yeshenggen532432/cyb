package com.qwb.view.call.model;

import com.qwb.view.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 拜访查询2--查询客户
 */
public class QueryCallonBean2 extends BaseBean {
	private static final long serialVersionUID = 6210123873932038408L;
	private int pageNo;
	private int pageSize;
	private int num;
	private int total;
	private int totalPage;
	private List<QueryCallon> rows = new ArrayList<>();

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

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

	public List<QueryCallon> getRows() {
		return rows;
	}

	public void setRows(List<QueryCallon> rows) {
		this.rows = rows;
	}

	
}
