package com.qwb.view.storehouse.model;

import com.qwb.view.base.model.BaseBean;


/**
 * 库位整理单
 */
public class StorehouseArrangeResult extends BaseBean{

	private StorehouseInBean stkCrew;

	public StorehouseInBean getStkCrew() {
		return stkCrew;
	}

	public void setStkCrew(StorehouseInBean stkCrew) {
		this.stkCrew = stkCrew;
	}
}
