package com.qwb.view.delivery.model;

import java.math.BigDecimal;

/**
 * 配送单明细
 */

public class DeliverySubBean {
    private Integer id;
    private Integer outSubId;//发票明细ID
    private Integer mastId;//主表id
    private Integer wareId;//商品id
    private BigDecimal qty;//出库数量
    private BigDecimal price;//单价
    private BigDecimal amt;//金额
    private String unitName;//单位
    private String xsTp;//销售类型
    private BigDecimal outQty;
    private BigDecimal outAmt;
    private String productDate;
    private String activeDate;
    private String remarks;

    private String sswId;//库存ID
    private BigDecimal helpQty;//辅助核算数量；
    private String helpUnit;//辅助核算单位；

    //-----------------------
    private String wareNm;//商品名称
    private String wareGg;//规格
    private String wareCode;//编号
    private BigDecimal outQty1;
    private String khNm;
    private String billNo;
    private BigDecimal rtnQty;
    private String beBarCode; // 单品条码
    private String packBarCode; // 包装条码
    private String providerName;//生产厂商

    private String wareDw;//单位
    private String maxUnitCode;//B
    private String minUnitCode;//S
    private String minUnit;//计量单位
    private BigDecimal sunitPrice;
    private BigDecimal bunitPrice;

    private BigDecimal rebatePrice;//销售返利

    private String billName;
    private String billId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOutSubId() {
        return outSubId;
    }

    public void setOutSubId(Integer outSubId) {
        this.outSubId = outSubId;
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

    public String getXsTp() {
        return xsTp;
    }

    public void setXsTp(String xsTp) {
        this.xsTp = xsTp;
    }

    public BigDecimal getOutQty() {
        return outQty;
    }

    public void setOutQty(BigDecimal outQty) {
        this.outQty = outQty;
    }

    public BigDecimal getOutAmt() {
        return outAmt;
    }

    public void setOutAmt(BigDecimal outAmt) {
        this.outAmt = outAmt;
    }

    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }

    public String getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSswId() {
        return sswId;
    }

    public void setSswId(String sswId) {
        this.sswId = sswId;
    }

    public BigDecimal getHelpQty() {
        return helpQty;
    }

    public void setHelpQty(BigDecimal helpQty) {
        this.helpQty = helpQty;
    }

    public String getHelpUnit() {
        return helpUnit;
    }

    public void setHelpUnit(String helpUnit) {
        this.helpUnit = helpUnit;
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

    public BigDecimal getOutQty1() {
        return outQty1;
    }

    public void setOutQty1(BigDecimal outQty1) {
        this.outQty1 = outQty1;
    }

    public String getKhNm() {
        return khNm;
    }

    public void setKhNm(String khNm) {
        this.khNm = khNm;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public BigDecimal getRtnQty() {
        return rtnQty;
    }

    public void setRtnQty(BigDecimal rtnQty) {
        this.rtnQty = rtnQty;
    }

    public String getBeBarCode() {
        return beBarCode;
    }

    public void setBeBarCode(String beBarCode) {
        this.beBarCode = beBarCode;
    }

    public String getPackBarCode() {
        return packBarCode;
    }

    public void setPackBarCode(String packBarCode) {
        this.packBarCode = packBarCode;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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

    public BigDecimal getSunitPrice() {
        return sunitPrice;
    }

    public void setSunitPrice(BigDecimal sunitPrice) {
        this.sunitPrice = sunitPrice;
    }

    public BigDecimal getBunitPrice() {
        return bunitPrice;
    }

    public void setBunitPrice(BigDecimal bunitPrice) {
        this.bunitPrice = bunitPrice;
    }

    public BigDecimal getRebatePrice() {
        return rebatePrice;
    }

    public void setRebatePrice(BigDecimal rebatePrice) {
        this.rebatePrice = rebatePrice;
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
}
