package com.qwb.view.car.model;

import com.qwb.view.base.model.BasePageBean;
import java.util.List;

/**
 *  移库：1.车销配货单列表 2.车销回库单
 */
public class CarStkMovePageResult extends BasePageBean {
	private List<StkMoveBean> rows;

	public List<StkMoveBean> getRows() {
		return rows;
	}

	public void setRows(List<StkMoveBean> rows) {
		this.rows = rows;
	}
}
