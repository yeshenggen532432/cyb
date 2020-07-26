package com.qwb.view.wangpan.model;

import com.qwb.view.base.model.BaseBean;
import com.qwb.view.file.model.FileBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询云盘文件
 */
public class QueryYunFileBean extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4611291009829729249L;
	private Integer pageNo;
	private Integer pageSize;
	private Integer total;
	private Integer totalPage;
	private List<FileBean> rows = new ArrayList<>();

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

	public List<FileBean> getRows() {
		return rows;
	}

	public void setRows(List<FileBean> rows) {
		this.rows = rows;
	}
	
}
