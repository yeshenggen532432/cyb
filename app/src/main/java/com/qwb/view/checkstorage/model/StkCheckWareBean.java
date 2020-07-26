package com.qwb.view.checkstorage.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 盘点仓库--商品
 */
public class StkCheckWareBean implements Serializable {

	//公用
	private Integer wareId;//商品ID
	private String wareNm;//商品名称
	private String wareGg;//规格
	private Double qty;//大单位数量
	private Double minQty;//小单位数量
	private String hsNum;//换算数量
	private String disQty;//差量
	private String stkQty;//当前库存
	private String minStkQty;//当前小单位库存
	private String produceDate;//生产日期
	private String unitName;//大单位名称
	private String minUnit;//小单位名称

	//盘点
	private int sunitFront;//开单辅单位排前；1:是

	//批次盘点
	private BigDecimal zmPrice;//账面价格
	private BigDecimal price;//账面价格
	private Integer appendData;//账面价格
	private BigDecimal maxAmt;//账面价格
	private BigDecimal minAmt;//账面价格
	private Integer priceFlag;//账面价格
	private Integer maxQtyFlag;//账面价格
	private Integer minQtyFlag;//账面价格

	//------------------------------------

//	"wareNm": "青岛小优",
//			"waretype": 1,
//			"wareGg": "330*24",
//			"waretypePath": "-1-",
//			"unitName": "箱",
//			"wareId": 8,
//			"wareCode": "1002",
//			"produceDate": "",
//			"hsNum": 24,
//			"price": 36,
//			"qty": 25,
//			"id": 2395,
//			"minUnit": "瓶",
//			"mastId": 152,
//			"stkQty": 13405.26167,
//			"minQty": 36


	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public int getSunitFront() {
		return sunitFront;
	}

	public void setSunitFront(int sunitFront) {
		this.sunitFront = sunitFront;
	}

	public Integer getWareId() {
		return wareId;
	}

	public void setWareId(Integer wareId) {
		this.wareId = wareId;
	}

	public String getMinUnit() {
		return minUnit;
	}

	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}

	public String getHsNum() {
		return hsNum;
	}

	public void setHsNum(String hsNum) {
		this.hsNum = hsNum;
	}

	public String getDisQty() {
		return disQty;
	}

	public void setDisQty(String disQty) {
		this.disQty = disQty;
	}

	public String getStkQty() {
		return stkQty;
	}

	public void setStkQty(String stkQty) {
		this.stkQty = stkQty;
	}

	public String getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(String produceDate) {
		this.produceDate = produceDate;
	}

	public String getWareNm() {
		return wareNm;
	}

	public void setWareNm(String wareNm) {
		this.wareNm = wareNm;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public Double getMinQty() {
		return minQty;
	}

	public void setMinQty(Double minQty) {
		this.minQty = minQty;
	}

	public String getMinStkQty() {
		return minStkQty;
	}

	public void setMinStkQty(String minStkQty) {
		this.minStkQty = minStkQty;
	}


	public String getWareGg() {
		return wareGg;
	}

	public void setWareGg(String wareGg) {
		this.wareGg = wareGg;
	}

	public BigDecimal getZmPrice() {
		return zmPrice;
	}

	public void setZmPrice(BigDecimal zmPrice) {
		this.zmPrice = zmPrice;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getAppendData() {
		return appendData;
	}

	public void setAppendData(Integer appendData) {
		this.appendData = appendData;
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




}
