package com.qwb.view.storehouse.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 待入仓发票
 */
public class StorehouseStayInSubBean {

	private String beUnit;//计量单位
	private Double hsNum;

	private Integer id;
	private Integer mastId;//主表id
	private Integer wareId;//商品id
	private BigDecimal price;//单价
	private BigDecimal amt;//金额
	private String unitName;//单位
	private BigDecimal inQty;
	private BigDecimal inAmt;
	private String productDate;//生产日期
	private String inTypeCode;//采购类型
	private String inTypeName;//采购类型
	private BigDecimal rebatePrice;//采购返利

	private BigDecimal maxPrice;

	private String pickupSubIds;//领用明细ID和数量   格式id:20,id:30;

	//----------------------------------
	private String wareNm;//商品名称
	private String wareGg;//规格
	private String wareCode;//编号
	private BigDecimal inQty1;
	private String proName;
	private String billNo;
	private String billName;
	private String billId;

	private String minUnitName;
	private BigDecimal minQty;
	private BigDecimal minInQty;

	private String remarks;//备注

	private String wareDw;//单位
	private String maxUnitCode;//B
	private String minUnitCode;//S
	private String minUnit;//计量单位


	private Integer inStkId;
	private String inStkName;
	private BigDecimal qty;//入库数量
	private String oldBeUnit;

	public String getOldBeUnit() {
		return oldBeUnit;
	}

	public void setOldBeUnit(String oldBeUnit) {
		this.oldBeUnit = oldBeUnit;
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

	public BigDecimal getInQty() {
		return inQty;
	}

	public void setInQty(BigDecimal inQty) {
		this.inQty = inQty;
	}

	public BigDecimal getInAmt() {
		return inAmt;
	}

	public void setInAmt(BigDecimal inAmt) {
		this.inAmt = inAmt;
	}

	public String getProductDate() {
		return productDate;
	}

	public void setProductDate(String productDate) {
		this.productDate = productDate;
	}

	public String getInTypeCode() {
		return inTypeCode;
	}

	public void setInTypeCode(String inTypeCode) {
		this.inTypeCode = inTypeCode;
	}

	public String getInTypeName() {
		return inTypeName;
	}

	public void setInTypeName(String inTypeName) {
		this.inTypeName = inTypeName;
	}

	public BigDecimal getRebatePrice() {
		return rebatePrice;
	}

	public void setRebatePrice(BigDecimal rebatePrice) {
		this.rebatePrice = rebatePrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getPickupSubIds() {
		return pickupSubIds;
	}

	public void setPickupSubIds(String pickupSubIds) {
		this.pickupSubIds = pickupSubIds;
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

	public BigDecimal getInQty1() {
		return inQty1;
	}

	public void setInQty1(BigDecimal inQty1) {
		this.inQty1 = inQty1;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getMinUnitName() {
		return minUnitName;
	}

	public void setMinUnitName(String minUnitName) {
		this.minUnitName = minUnitName;
	}

	public BigDecimal getMinQty() {
		return minQty;
	}

	public void setMinQty(BigDecimal minQty) {
		this.minQty = minQty;
	}

	public BigDecimal getMinInQty() {
		return minInQty;
	}

	public void setMinInQty(BigDecimal minInQty) {
		this.minInQty = minInQty;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
