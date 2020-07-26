package com.qwb.view.storehouse.model;

import com.qwb.view.base.model.BaseBean;

import java.math.BigDecimal;

/**
 * 库位商品
 */
public class StorehouseWareBean extends BaseBean {

	private Integer id;
	private Integer houseId;//库位ID
	private Integer wareId;//商品id
	private String unitName;//单位
	private BigDecimal qty;//数量
	private String beUnit;
	private BigDecimal minQty;//小单位数量
	private String minUnitName;//小单位名称

	private String houseName;//库位名称
	private String wareNm;//商品名称
	private String wareGg;//规格
	private String wareCode;//编号
	private Integer isType;
	private Integer waretype;//所属分类
	private Integer stkId;
	private String stkName;
	private Double hsNum;// 换算数量(包装单位到单品单位的转换系数)
	private BigDecimal kwArea;//库位面积
	private BigDecimal kwVolume;//库位容量
	private String  kwBar;//库位条码
	private String houseIds;//库位IDs
	private String wareDw;//单位
	private String maxUnitCode;//B
	private String minUnitCode;//S
	private String minUnit;//计量单位
	private BigDecimal price;
	private String wareIds;
	private BigDecimal stkQty;
	private BigDecimal disQty;

	private BigDecimal minStkQty;
	private BigDecimal minDisQty;

	private BigDecimal disStkQty;//盘盈亏(大)
	private BigDecimal minDisStkQty;//盘盈亏(小)

	private Integer outStkId;
	private String outStkName;
	private Integer inStkId;
	private String inStkName;
	private BigDecimal inQty;
	private BigDecimal minInQty;

	public Integer getOutStkId() {
		return outStkId;
	}

	public void setOutStkId(Integer outStkId) {
		this.outStkId = outStkId;
	}

	public String getOutStkName() {
		return outStkName;
	}

	public void setOutStkName(String outStkName) {
		this.outStkName = outStkName;
	}

	public BigDecimal getInQty() {
		return inQty;
	}

	public void setInQty(BigDecimal inQty) {
		this.inQty = inQty;
	}

	public BigDecimal getMinInQty() {
		return minInQty;
	}

	public void setMinInQty(BigDecimal minInQty) {
		this.minInQty = minInQty;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getHouseId() {
		return houseId;
	}

	public void setHouseId(Integer houseId) {
		this.houseId = houseId;
	}

	public Integer getWareId() {
		return wareId;
	}

	public void setWareId(Integer wareId) {
		this.wareId = wareId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public String getBeUnit() {
		return beUnit;
	}

	public void setBeUnit(String beUnit) {
		this.beUnit = beUnit;
	}

	public BigDecimal getMinQty() {
		return minQty;
	}

	public void setMinQty(BigDecimal minQty) {
		this.minQty = minQty;
	}

	public String getMinUnitName() {
		return minUnitName;
	}

	public void setMinUnitName(String minUnitName) {
		this.minUnitName = minUnitName;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
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

	public Integer getIsType() {
		return isType;
	}

	public void setIsType(Integer isType) {
		this.isType = isType;
	}

	public Integer getWaretype() {
		return waretype;
	}

	public void setWaretype(Integer waretype) {
		this.waretype = waretype;
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

	public Double getHsNum() {
		return hsNum;
	}

	public void setHsNum(Double hsNum) {
		this.hsNum = hsNum;
	}

	public BigDecimal getKwArea() {
		return kwArea;
	}

	public void setKwArea(BigDecimal kwArea) {
		this.kwArea = kwArea;
	}

	public BigDecimal getKwVolume() {
		return kwVolume;
	}

	public void setKwVolume(BigDecimal kwVolume) {
		this.kwVolume = kwVolume;
	}

	public String getKwBar() {
		return kwBar;
	}

	public void setKwBar(String kwBar) {
		this.kwBar = kwBar;
	}

	public String getHouseIds() {
		return houseIds;
	}

	public void setHouseIds(String houseIds) {
		this.houseIds = houseIds;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getWareIds() {
		return wareIds;
	}

	public void setWareIds(String wareIds) {
		this.wareIds = wareIds;
	}

	public BigDecimal getStkQty() {
		return stkQty;
	}

	public void setStkQty(BigDecimal stkQty) {
		this.stkQty = stkQty;
	}

	public BigDecimal getDisQty() {
		return disQty;
	}

	public void setDisQty(BigDecimal disQty) {
		this.disQty = disQty;
	}

	public BigDecimal getMinStkQty() {
		return minStkQty;
	}

	public void setMinStkQty(BigDecimal minStkQty) {
		this.minStkQty = minStkQty;
	}

	public BigDecimal getMinDisQty() {
		return minDisQty;
	}

	public void setMinDisQty(BigDecimal minDisQty) {
		this.minDisQty = minDisQty;
	}

	public BigDecimal getDisStkQty() {
		return disStkQty;
	}

	public void setDisStkQty(BigDecimal disStkQty) {
		this.disStkQty = disStkQty;
	}

	public BigDecimal getMinDisStkQty() {
		return minDisStkQty;
	}

	public void setMinDisStkQty(BigDecimal minDisStkQty) {
		this.minDisStkQty = minDisStkQty;
	}
}
