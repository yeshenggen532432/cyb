package com.qwb.view.company.model;


/**
 * 采购单table要上传的商品json
 */
public class WareBean{

	//要上传的数据
	private String wareId;// 商品id
	private String qty;
	private String price;
	private String unitName;
	private String beUnit;
	private String hsNum;
	private Integer inTypeCode;
	private String inTypeName;
	private String rebatePrice;
	private String remarks;
	private String productDate;

	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getBeUnit() {
		return beUnit;
	}

	public void setBeUnit(String beUnit) {
		this.beUnit = beUnit;
	}

	public String getHsNum() {
		return hsNum;
	}

	public void setHsNum(String hsNum) {
		this.hsNum = hsNum;
	}

	public Integer getInTypeCode() {
		return inTypeCode;
	}

	public void setInTypeCode(Integer inTypeCode) {
		this.inTypeCode = inTypeCode;
	}

	public String getInTypeName() {
		return inTypeName;
	}

	public void setInTypeName(String inTypeName) {
		this.inTypeName = inTypeName;
	}

	public String getRebatePrice() {
		return rebatePrice;
	}

	public void setRebatePrice(String rebatePrice) {
		this.rebatePrice = rebatePrice;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getProductDate() {
		return productDate;
	}

	public void setProductDate(String productDate) {
		this.productDate = productDate;
	}
}
