package com.qwb.view.checkstorage.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 盘点仓库
 */
public class StkCheckListBean extends BaseBean implements Serializable {

	private Integer total;
	private List<StkCheckBean> rows;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<StkCheckBean> getRows() {
		return rows;
	}

	public void setRows(List<StkCheckBean> rows) {
		this.rows = rows;
	}


}
