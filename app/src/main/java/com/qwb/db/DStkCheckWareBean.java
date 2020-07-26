package com.qwb.db;

import org.litepal.crud.LitePalSupport;

/**
 *  库存盘点
 */
public class DStkCheckWareBean extends LitePalSupport {
	private long id;//自增id
	private String userId;//用户id
	private String companyId;//公司id

	private Integer stkId;//仓库id
	private String stkName;//仓库名称

	private Integer wareId;//商品ID
	private String wareNm;//商品名称
	private Double qty;//大单位数量
	private Double minQty;//小单位数量
	private String hsNum;//换算数量
	private String disQty;//差量
	private String stkQty;//当前库存
	private String produceDate;//生产日期
	private int sunitFront;//开单辅单位排前；1:是
	private String unitName;//大单位名称
	private String minUnit;//小单位名称
	private String zmPrice;//账面单位成本（大）
	private String price;//实际单位成本（大）

	private int type;//1盘点开单（添加）；2盘点开单（修改）；3多人盘点开单（添加）；4多人盘点开单（修改）

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Integer getStkId() {
		return stkId;
	}

	public void setStkId(Integer stkId) {
		this.stkId = stkId;
	}

	public String getStkName() {
		return stkName;
	}

	public void setStkName(String stkName) {
		this.stkName = stkName;
	}

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

	public int getSunitFront() {
		return sunitFront;
	}

	public void setSunitFront(int sunitFront) {
		this.sunitFront = sunitFront;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getMinUnit() {
		return minUnit;
	}

	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getZmPrice() {
		return zmPrice;
	}

	public void setZmPrice(String zmPrice) {
		this.zmPrice = zmPrice;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
}
