package com.qwb.view.storehouse.model;

import java.math.BigDecimal;

/**
 * 入仓单--子表
 */
public class StorehouseInSubBean {

	private String beUnit;//计量单位
	private Double hsNum;

	private Integer id;
	private Integer mastId;//主表id
	private Integer wareId;//商品id
	private BigDecimal qty;//入仓数量
	private BigDecimal price;//单价
	private BigDecimal amt;//金额
	private String unitName;//单位
	private String productDate;//生产日期
	private Integer proId;
	private String proName;//入库对象ID
	private Integer proType;//0供应商 1员工 2客户 3其它往来 4会员
	private Integer inStkId;//移入库位
	private String inStkName;//移入库位

	private BigDecimal minQty;//小单位入仓数量

	private BigDecimal inQty;//发票数量
	private BigDecimal inQty1;//剩余数量


	//----------------------------------
	private String wareNm;//商品名称
	private String wareGg;//规格
	private String wareCode;//编号
	private String wareDw;//单位
	private String maxUnitCode;//B
	private String minUnitCode;//S
	private String minUnit;//计量单位
	private String oldBeUnit;

	public BigDecimal getInQty1() {
		return inQty1;
	}

	public void setInQty1(BigDecimal inQty1) {
		this.inQty1 = inQty1;
	}

	public String getOldBeUnit() {
		return oldBeUnit;
	}

	public void setOldBeUnit(String oldBeUnit) {
		this.oldBeUnit = oldBeUnit;
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

	public String getProductDate() {
		return productDate;
	}

	public void setProductDate(String productDate) {
		this.productDate = productDate;
	}

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public Integer getProType() {
		return proType;
	}

	public void setProType(Integer proType) {
		this.proType = proType;
	}

	public Integer getInStkId() {
		return inStkId;
	}

	public void setInStkId(Integer inStkId) {
		this.inStkId = inStkId;
	}

	public String getInStkName() {
		return inStkName;
	}

	public void setInStkName(String inStkName) {
		this.inStkName = inStkName;
	}

	public BigDecimal getInQty() {
		return inQty;
	}

	public void setInQty(BigDecimal inQty) {
		this.inQty = inQty;
	}


	public BigDecimal getMinQty() {
		return minQty;
	}

	public void setMinQty(BigDecimal minQty) {
		this.minQty = minQty;
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

	public String getWareDw() {
		return wareDw;
	}

	public void setWareDw(String wareDw) {
		this.wareDw = wareDw;
	}

	public String getMaxUnitCode() {
		return maxUnitCode;
	}

	public void setMaxUnitCode(String maxUnitCode) {
		this.maxUnitCode = maxUnitCode;
	}

	public String getMinUnitCode() {
		return minUnitCode;
	}

	public void setMinUnitCode(String minUnitCode) {
		this.minUnitCode = minUnitCode;
	}

	public String getMinUnit() {
		return minUnit;
	}

	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}
}
