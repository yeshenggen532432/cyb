package com.qwb.view.checkstorage.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品生产日期
 */
public class WareProduceDateBean extends BaseBean implements Serializable {

	private String beUnit;//计量单位
	private Double hsNum;
	private Integer id;
	private Integer mastId;
	private Integer wareId;
	private BigDecimal qty;
	private BigDecimal price;
	private BigDecimal amt;
	private String unitName;
	private BigDecimal disQty;
	private BigDecimal stkQty;
	private BigDecimal minStkQty;

	private BigDecimal minQty;//辅助数量
	private String minUnit;//辅助单位
	private String produceDate;


	private Integer priceFlag;//价格被改动标记--0或者空：价格未改动 1:价格已改动
	private Integer maxQtyFlag;//大单位数量被改动标记--0或者空：数量未改动 1:数量已改动
	private Integer minQtyFlag;//小单位数量被改动标记--0或者空：数量未改动 1:数量已改动

	private BigDecimal sunitPrice;//小单位价格

	private BigDecimal zmPrice;//账面单位成本

	private BigDecimal maxAmt;//大单位实际成本
	private BigDecimal minAmt;//小单位实际成本

	private Integer appendData;//是否追加数据 0:否 1:是

	///////////////////////////////////
	private String wareNm;
	private String wareGg;//规格
	private String wareCode;//编号
	private Integer waretype;
	private String waretypePath;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMastId() {
		return mastId;
	}

	public void setMastId(Integer mastId) {
		this.mastId = mastId;
	}

	public Integer getWareId() {
		return wareId;
	}

	public void setWareId(Integer wareId) {
		this.wareId = wareId;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public BigDecimal getDisQty() {
		return disQty;
	}

	public void setDisQty(BigDecimal disQty) {
		this.disQty = disQty;
	}

	public BigDecimal getStkQty() {
		return stkQty;
	}

	public void setStkQty(BigDecimal stkQty) {
		this.stkQty = stkQty;
	}

	public BigDecimal getMinStkQty() {
		return minStkQty;
	}

	public void setMinStkQty(BigDecimal minStkQty) {
		this.minStkQty = minStkQty;
	}

	public BigDecimal getMinQty() {
		return minQty;
	}

	public void setMinQty(BigDecimal minQty) {
		this.minQty = minQty;
	}

	public String getMinUnit() {
		return minUnit;
	}

	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}

	public String getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(String produceDate) {
		this.produceDate = produceDate;
	}

	public Integer getPriceFlag() {
		return priceFlag;
	}

	public void setPriceFlag(Integer priceFlag) {
		this.priceFlag = priceFlag;
	}

	public Integer getMaxQtyFlag() {
		return maxQtyFlag;
	}

	public void setMaxQtyFlag(Integer maxQtyFlag) {
		this.maxQtyFlag = maxQtyFlag;
	}

	public Integer getMinQtyFlag() {
		return minQtyFlag;
	}

	public void setMinQtyFlag(Integer minQtyFlag) {
		this.minQtyFlag = minQtyFlag;
	}

	public BigDecimal getSunitPrice() {
		return sunitPrice;
	}

	public void setSunitPrice(BigDecimal sunitPrice) {
		this.sunitPrice = sunitPrice;
	}

	public BigDecimal getZmPrice() {
		return zmPrice;
	}

	public void setZmPrice(BigDecimal zmPrice) {
		this.zmPrice = zmPrice;
	}

	public BigDecimal getMaxAmt() {
		return maxAmt;
	}

	public void setMaxAmt(BigDecimal maxAmt) {
		this.maxAmt = maxAmt;
	}

	public BigDecimal getMinAmt() {
		return minAmt;
	}

	public void setMinAmt(BigDecimal minAmt) {
		this.minAmt = minAmt;
	}

	public Integer getAppendData() {
		return appendData;
	}

	public void setAppendData(Integer appendData) {
		this.appendData = appendData;
	}

	public String getWareNm() {
		return wareNm;
	}

	public void setWareNm(String wareNm) {
		this.wareNm = wareNm;
	}

	public String getWareGg() {
		return wareGg;
	}

	public void setWareGg(String wareGg) {
		this.wareGg = wareGg;
	}

	public String getWareCode() {
		return wareCode;
	}

	public void setWareCode(String wareCode) {
		this.wareCode = wareCode;
	}

	public Integer getWaretype() {
		return waretype;
	}

	public void setWaretype(Integer waretype) {
		this.waretype = waretype;
	}

	public String getWaretypePath() {
		return waretypePath;
	}

	public void setWaretypePath(String waretypePath) {
		this.waretypePath = waretypePath;
	}


	public String getBeUnit() {
		return beUnit;
	}

	public void setBeUnit(String beUnit) {
		this.beUnit = beUnit;
	}

	public Double getHsNum() {
		return hsNum;
	}

	public void setHsNum(Double hsNum) {
		this.hsNum = hsNum;
	}
}
