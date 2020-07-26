package com.qwb.view.checkstorage.model;

import java.io.Serializable;

/**
 * 盘点仓库--商品
 */
public class StkWareBean implements Serializable {

	private Integer wareId;//商品ID
	private String wareNm;//商品名称
	private String hsNum;//换算数量

	private String unitName;//大单位名称
	private String minSumQty;
	private String minUnitName;//小单位名称
	private String sumQty;
	private String sumAmt;
	private int stkId;//仓库id
	private String stkName;//仓库名称


	//------------------------------------
//	wareNm	:	青岛小优123
//	stkName	:	1号仓库
//	hsNum	:	123
//	unitName	:	件
//	wareId	:	8
//	minSumQty	:	1241107.7499999919
//	minUnitName	:	瓶
//	sumQty	:	10090.3069105686
//	sumAmt	:	238268.0638455325
//	stkId	:	7


	public Integer getWareId() {
		return wareId;
	}

	public void setWareId(Integer wareId) {
		this.wareId = wareId;
	}

	public String getWareNm() {
		return wareNm;
	}

	public void setWareNm(String wareNm) {
		this.wareNm = wareNm;
	}

	public String getHsNum() {
		return hsNum;
	}

	public void setHsNum(String hsNum) {
		this.hsNum = hsNum;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getMinSumQty() {
		return minSumQty;
	}

	public void setMinSumQty(String minSumQty) {
		this.minSumQty = minSumQty;
	}

	public String getMinUnitName() {
		return minUnitName;
	}

	public void setMinUnitName(String minUnitName) {
		this.minUnitName = minUnitName;
	}

	public String getSumQty() {
		return sumQty;
	}

	public void setSumQty(String sumQty) {
		this.sumQty = sumQty;
	}

	public String getSumAmt() {
		return sumAmt;
	}

	public void setSumAmt(String sumAmt) {
		this.sumAmt = sumAmt;
	}

	public int getStkId() {
		return stkId;
	}

	public void setStkId(int stkId) {
		this.stkId = stkId;
	}

	public String getStkName() {
		return stkName;
	}

	public void setStkName(String stkName) {
		this.stkName = stkName;
	}
}
