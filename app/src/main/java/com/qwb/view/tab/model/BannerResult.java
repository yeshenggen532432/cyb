package com.qwb.view.tab.model;

import com.qwb.view.base.model.BasePageBean;
import java.util.List;

/**
 */
public class BannerResult extends BasePageBean {

	private List<BannerBean> rows;

	public List<BannerBean> getRows() {
		return rows;
	}

	public void setRows(List<BannerBean> rows) {
		this.rows = rows;
	}
}
