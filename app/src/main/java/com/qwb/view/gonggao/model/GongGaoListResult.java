package com.qwb.view.gonggao.model;

import com.qwb.view.base.model.BaseBean;
import java.util.List;

/**
 * 公告列表
 */
public class GongGaoListResult extends BaseBean{
	private boolean hasNext;
	private List<GongGaoBean> rows;

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public List<GongGaoBean> getRows() {
		return rows;
	}

	public void setRows(List<GongGaoBean> rows) {
		this.rows = rows;
	}
}
