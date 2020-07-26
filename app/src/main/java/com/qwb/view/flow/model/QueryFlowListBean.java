package com.qwb.view.flow.model;

import com.qwb.view.base.model.BaseListBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class QueryFlowListBean extends BaseListBean {

	private List<QueryFlowBean> rows = new ArrayList<>();

	public List<QueryFlowBean> getRows() {
		return rows;
	}

	public void setRows(List<QueryFlowBean> rows) {
		this.rows = rows;
	}
}
