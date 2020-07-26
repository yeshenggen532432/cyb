package com.qwb.view.car.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 *  车销收款
 */
public class CarRecMastPageBean extends BaseBean {
	private double sumAmt;
	private List<CarRecMastBean> rows;

	public List<CarRecMastBean> getRows() {
		return rows;
	}

	public void setRows(List<CarRecMastBean> rows) {
		this.rows = rows;
	}

	public double getSumAmt() {
		return sumAmt;
	}

	public void setSumAmt(double sumAmt) {
		this.sumAmt = sumAmt;
	}
}
