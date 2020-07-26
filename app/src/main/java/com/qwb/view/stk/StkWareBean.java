package com.qwb.view.stk;

import com.qwb.view.base.model.BaseBean;

/**
 *  仓库商品
 */
public class StkWareBean extends BaseBean {
	private String wareNm;
	private String wareGg;
	private double wareDj;
	private String stkName;
	private int wareId;
	private String unitName;
	private double hsNum;
	private double sumQty;
	private double minSumQty;
	private double sumAmt;
	private String minUnitName;
	private int stkId;

	public String getWareNm() {
		return wareNm;
	}

	public void setWareNm(String wareNm) {
		this.wareNm = wareNm;
	}

	public String getStkName() {
		return stkName;
	}

	public void setStkName(String stkName) {
		this.stkName = stkName;
	}

	public int getWareId() {
		return wareId;
	}

	public void setWareId(int wareId) {
		this.wareId = wareId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public double getHsNum() {
		return hsNum;
	}

	public void setHsNum(double hsNum) {
		this.hsNum = hsNum;
	}

	public double getSumQty() {
		return sumQty;
	}

	public void setSumQty(double sumQty) {
		this.sumQty = sumQty;
	}

	public double getMinSumQty() {
		return minSumQty;
	}

	public void setMinSumQty(double minSumQty) {
		this.minSumQty = minSumQty;
	}

	public double getSumAmt() {
		return sumAmt;
	}

	public void setSumAmt(double sumAmt) {
		this.sumAmt = sumAmt;
	}

	public String getMinUnitName() {
		return minUnitName;
	}

	public void setMinUnitName(String minUnitName) {
		this.minUnitName = minUnitName;
	}

	public int getStkId() {
		return stkId;
	}

	public void setStkId(int stkId) {
		this.stkId = stkId;
	}

	public String getWareGg() {
		return wareGg;
	}

	public void setWareGg(String wareGg) {
		this.wareGg = wareGg;
	}

	public double getWareDj() {
		return wareDj;
	}

	public void setWareDj(double wareDj) {
		this.wareDj = wareDj;
	}
}
