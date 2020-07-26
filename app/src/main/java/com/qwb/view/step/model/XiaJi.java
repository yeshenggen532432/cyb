package com.qwb.view.step.model;

import java.io.Serializable;

/**
 * 供货下单获取信息--获取商品信息列表--公用“商品信息”
 *
 */
public class XiaJi implements Serializable {
	
	private static final long serialVersionUID = 4906151387568846118L;
	private String id; // 陈列检查采集id--区分-供货下单获取信息
	private String orderId; // 冰点数orderId---区分-供货下单获取信息

	//要上传的数据
	private String wareId;// 商品id
	private String wareNm; // 商品名称
	private String xsTp; // 销售类型
	private String wareGg; // 规格
	private String wareDw; // 单位---区分-获取商品信息列表
	private String wareNum; // 数量
	private String wareDj;// 单价
	private String wareZj; // 总价
	private String beUnit; // 包装单位代码或计量单位代码
	private String remark; // 备注
	private String productDate;//生产日期

	//商品原始数据
	private String minUnit;
	private String maxUnit;
	private String hsNum;
	private String maxUnitCode;
	private String minUnitCode;
	private Double wareDjOriginal;//商品原价
	private String lowestSalePrice;//最低销售价
	private String packBarCode;//大条形码
	private String beBarCode;//小条形码

	public String getPackBarCode() {
		return packBarCode;
	}

	public void setPackBarCode(String packBarCode) {
		this.packBarCode = packBarCode;
	}

	public String getBeBarCode() {
		return beBarCode;
	}

	public void setBeBarCode(String beBarCode) {
		this.beBarCode = beBarCode;
	}

	public String getLowestSalePrice() {
		return lowestSalePrice;
	}

	public void setLowestSalePrice(String lowestSalePrice) {
		this.lowestSalePrice = lowestSalePrice;
	}

	public String getXsTp() {
		return xsTp;
	}

	public void setXsTp(String xsTp) {
		this.xsTp = xsTp;
	}

	public String getWareNum() {
		return wareNum;
	}

	public void setWareNum(String wareNum) {
		this.wareNum = wareNum;
	}

	public String getWareZj() {
		return wareZj;
	}

	public void setWareZj(String wareZj) {
		this.wareZj = wareZj;
	}

	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
	}

	public String getWareNm() {
		return wareNm;
	}

	public void setWareNm(String wareNm) {
		this.wareNm = wareNm;
	}

	public String getWareDj() {
		return wareDj;
	}

	public void setWareDj(String wareDj) {
		this.wareDj = wareDj;
	}

	public String getWareDw() {
		return wareDw;
	}

	public void setWareDw(String wareDw) {
		this.wareDw = wareDw;
	}

	public String getWareGg() {
		return wareGg;
	}

	public void setWareGg(String wareGg) {
		this.wareGg = wareGg;
	}

	public String getBeUnit() {
		return beUnit;
	}

	public void setBeUnit(String beUnit) {
		this.beUnit = beUnit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProductDate() {
		return productDate;
	}

	public void setProductDate(String productDate) {
		this.productDate = productDate;
	}

	@Override
	public String toString() {
		return "XiaJi [wareGg=" + wareGg + ", wareNm=" + wareNm + ", wareId="
				+ wareId + ", wareDj=" + wareDj + ", wareZj=" + wareZj
				+ ", wareNum=" + wareNum + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMinUnit() {
		return minUnit;
	}

	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}

	public String getMaxUnit() {
		return maxUnit;
	}

	public void setMaxUnit(String maxUnit) {
		this.maxUnit = maxUnit;
	}

	public String getHsNum() {
		return hsNum;
	}

	public void setHsNum(String hsNum) {
		this.hsNum = hsNum;
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

	public Double getWareDjOriginal() {
		return wareDjOriginal;
	}

	public void setWareDjOriginal(Double wareDjOriginal) {
		this.wareDjOriginal = wareDjOriginal;
	}
}
