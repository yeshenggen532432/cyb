package com.qwb.view.ware.model;

import com.qwb.view.step.model.WarePicBean;

import java.util.List;

/**
 * 商品
 */

public class WareBean {

//    private Integer wareId;//商品id
//    private Integer waretype;//所属分类
//    private String wareCode;//编码
//    private String wareNm;//名称
//
//    private String fbtime;//发布时间
//    private Integer isCy;//是否常用（1是；2否）
//    private Integer sunitFront;//开单默认显示辅单位
//    private String py;//拼音
//    private BigDecimal tranAmt;//运输费用
//    private BigDecimal tcAmt;//提成费用
//    private BigDecimal saleProTc;//销售利润提成
//    private BigDecimal saleGroTc;//销售毛利提成
//
//    private String qualityDays;// 保质期,单位是天
//    private String remark;//备注
//
//    private String produceDate;//生产日期
//    private Double lowerLimit; // 库存下限
//
//    private Long orderCd; // 排序码
//
//    //大小单位换算比例*		bUnit*maxUnitCode=sUnit*minUnit
//    private Double bUnit;//主单位乘数
//    private Double sUnit;//辅单位乘数
//    private Double hsNum;// 换算数量(包装单位到单品单位的转换系数)
//
//    private String providerName;//生产商
//    private String status;//启用状态 （1是；2否）
//    private String aliasName;//别名
//    private String asnNo;//标示码
//
//    private Boolean hasSync;//是否有更新
//
//    //---------进销存商品相关设置-----------------
//    private String wareDw;//单位--进销存：大单位名称
//    private String wareGg;//规格--进销存：大单位规格
//    private String packBarCode; // 包装条码--进销存：大单位条码
//    private Double inPrice; // 采购价--进销存：大单位采购价
//    private Double wareDj;//单价--进销存：大单位批发价
//    private Double lsPrice; //零售价--进销存：大单位零售价
//    private Double fxPrice;//分销价--进销存:大单位分销价 ????
//    private Double cxPrice;//促销价--进销存:大单位促销价 ????
//    private String maxUnitCode;//B 固定
//    private String maxUnit; // 包装单位--进销存：大单位名称 目前已wareDw为准
//    private Double warnQty; //预警数量--进销存：大单位预警数量
//
//    private String minUnit;//计量单位名称--进销存：小单位名称
//    private String minWareGg;//小单位规格--进销存：小单位规格 ????
//    private String beBarCode; // 单品条码--进销存：小单位条码
//    private Double minInPrice; //  小单位采购价--进销存：小单位采购价 ????
//    private BigDecimal sunitPrice;//小单位销售价--进销存：小单位批发价
//    private Double minLsPrice;//小单位零售价--进销存：小单位零售价 ????
//    private Double minFxPrice;//小单位分销价--进销存：小单位分销价 ????
//    private Double minCxPrice;//小单位促销价--进销存：小单位促销价 ????
//    private Double minWarnQty;//小单位预警最低库存数量--进销存：预警最低库存数量 ????
//    private String minUnitCode;//S 固定

    private int wareId;
    private String wareNm;
    private double hsNum;
    private int waretype;
    private String waretypeNm;
    private String wareDesc;

    //大的
    private String wareGg;//规格
    private String wareDw;//单位
    private double wareDj;//单价
    private String maxUnit;//大单位(被wareDw代替)
    private String packBarCode;//条码
    private double inPrice;//采购价



    //小的
    private String minWareGg;//规格
    private String minUnit;//单位
    private double sunitPrice;//单价
    private String beBarCode;//条码
    private double minInPrice;//采购价
    private String minUnitCode;



    private int pfPriceType;
    private int minLsPriceType;
    private int shopWareSmallPriceShow;
    private String platshopWareNm;
    private double tcAmt;
    private String py;
    private int minFxPriceType;
    private String remark;
    private String asnNo;
    private String posWareNm;
    private String produceDate;
    private String platshopWareType;
    private int shopWarePriceDefault;
    private String groupIds;


    private String providerName;
    private String fbtime;
    private int minPfPriceType;

    private String aliasName;


    private String wareCode;
    private int lsPriceType;

    private String qualityDays;
    private String shopWareAlias;
    private int isCy;
    private int shopWarePriceShow;
    private int cxPriceType;
    private int tranAmt;

    private int minCxPriceType;
    private int fxPriceType;
    private String groupNms;
    private String maxUnitCode;
    private String status;

    private List<WarePicBean> warePicList;

    public String getWareNm() {
        return wareNm;
    }

    public void setWareNm(String wareNm) {
        this.wareNm = wareNm;
    }

    public int getWaretype() {
        return waretype;
    }

    public void setWaretype(int waretype) {
        this.waretype = waretype;
    }

    public String getMinWareGg() {
        return minWareGg;
    }

    public void setMinWareGg(String minWareGg) {
        this.minWareGg = minWareGg;
    }

    public int getPfPriceType() {
        return pfPriceType;
    }

    public void setPfPriceType(int pfPriceType) {
        this.pfPriceType = pfPriceType;
    }

    public int getMinLsPriceType() {
        return minLsPriceType;
    }

    public void setMinLsPriceType(int minLsPriceType) {
        this.minLsPriceType = minLsPriceType;
    }

    public int getShopWareSmallPriceShow() {
        return shopWareSmallPriceShow;
    }

    public void setShopWareSmallPriceShow(int shopWareSmallPriceShow) {
        this.shopWareSmallPriceShow = shopWareSmallPriceShow;
    }

    public String getPlatshopWareNm() {
        return platshopWareNm;
    }

    public void setPlatshopWareNm(String platshopWareNm) {
        this.platshopWareNm = platshopWareNm;
    }


    public void setTcAmt(int tcAmt) {
        this.tcAmt = tcAmt;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public int getMinFxPriceType() {
        return minFxPriceType;
    }

    public void setMinFxPriceType(int minFxPriceType) {
        this.minFxPriceType = minFxPriceType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMinUnitCode() {
        return minUnitCode;
    }

    public void setMinUnitCode(String minUnitCode) {
        this.minUnitCode = minUnitCode;
    }

    public String getBeBarCode() {
        return beBarCode;
    }

    public void setBeBarCode(String beBarCode) {
        this.beBarCode = beBarCode;
    }

    public String getWareDesc() {
        return wareDesc;
    }

    public void setWareDesc(String wareDesc) {
        this.wareDesc = wareDesc;
    }

    public String getAsnNo() {
        return asnNo;
    }

    public void setAsnNo(String asnNo) {
        this.asnNo = asnNo;
    }

    public String getPosWareNm() {
        return posWareNm;
    }

    public void setPosWareNm(String posWareNm) {
        this.posWareNm = posWareNm;
    }

    public String getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(String produceDate) {
        this.produceDate = produceDate;
    }


    public void setHsNum(int hsNum) {
        this.hsNum = hsNum;
    }

    public String getPlatshopWareType() {
        return platshopWareType;
    }

    public void setPlatshopWareType(String platshopWareType) {
        this.platshopWareType = platshopWareType;
    }

    public int getShopWarePriceDefault() {
        return shopWarePriceDefault;
    }

    public void setShopWarePriceDefault(int shopWarePriceDefault) {
        this.shopWarePriceDefault = shopWarePriceDefault;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    public String getMaxUnit() {
        return maxUnit;
    }

    public void setMaxUnit(String maxUnit) {
        this.maxUnit = maxUnit;
    }

    public String getMinUnit() {
        return minUnit;
    }

    public void setMinUnit(String minUnit) {
        this.minUnit = minUnit;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getFbtime() {
        return fbtime;
    }

    public void setFbtime(String fbtime) {
        this.fbtime = fbtime;
    }

    public int getMinPfPriceType() {
        return minPfPriceType;
    }

    public void setMinPfPriceType(int minPfPriceType) {
        this.minPfPriceType = minPfPriceType;
    }

    public String getWareGg() {
        return wareGg;
    }

    public void setWareGg(String wareGg) {
        this.wareGg = wareGg;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public int getWareId() {
        return wareId;
    }

    public void setWareId(int wareId) {
        this.wareId = wareId;
    }

    public String getWaretypeNm() {
        return waretypeNm;
    }

    public void setWaretypeNm(String waretypeNm) {
        this.waretypeNm = waretypeNm;
    }

    public String getWareCode() {
        return wareCode;
    }

    public void setWareCode(String wareCode) {
        this.wareCode = wareCode;
    }

    public int getLsPriceType() {
        return lsPriceType;
    }

    public void setLsPriceType(int lsPriceType) {
        this.lsPriceType = lsPriceType;
    }

    public String getWareDw() {
        return wareDw;
    }

    public void setWareDw(String wareDw) {
        this.wareDw = wareDw;
    }

    public String getQualityDays() {
        return qualityDays;
    }

    public void setQualityDays(String qualityDays) {
        this.qualityDays = qualityDays;
    }

    public String getShopWareAlias() {
        return shopWareAlias;
    }

    public void setShopWareAlias(String shopWareAlias) {
        this.shopWareAlias = shopWareAlias;
    }

    public int getIsCy() {
        return isCy;
    }

    public void setIsCy(int isCy) {
        this.isCy = isCy;
    }

    public int getShopWarePriceShow() {
        return shopWarePriceShow;
    }

    public void setShopWarePriceShow(int shopWarePriceShow) {
        this.shopWarePriceShow = shopWarePriceShow;
    }

    public int getCxPriceType() {
        return cxPriceType;
    }

    public void setCxPriceType(int cxPriceType) {
        this.cxPriceType = cxPriceType;
    }

    public int getTranAmt() {
        return tranAmt;
    }

    public void setTranAmt(int tranAmt) {
        this.tranAmt = tranAmt;
    }

    public String getPackBarCode() {
        return packBarCode;
    }

    public void setPackBarCode(String packBarCode) {
        this.packBarCode = packBarCode;
    }

    public int getMinCxPriceType() {
        return minCxPriceType;
    }

    public void setMinCxPriceType(int minCxPriceType) {
        this.minCxPriceType = minCxPriceType;
    }

    public int getFxPriceType() {
        return fxPriceType;
    }

    public void setFxPriceType(int fxPriceType) {
        this.fxPriceType = fxPriceType;
    }

    public String getGroupNms() {
        return groupNms;
    }

    public void setGroupNms(String groupNms) {
        this.groupNms = groupNms;
    }

    public String getMaxUnitCode() {
        return maxUnitCode;
    }

    public void setMaxUnitCode(String maxUnitCode) {
        this.maxUnitCode = maxUnitCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public List<WarePicBean> getWarePicList() {
        return warePicList;
    }

    public void setWarePicList(List<WarePicBean> warePicList) {
        this.warePicList = warePicList;
    }

    public double getHsNum() {
        return hsNum;
    }

    public void setHsNum(double hsNum) {
        this.hsNum = hsNum;
    }

    public double getWareDj() {
        return wareDj;
    }

    public void setWareDj(double wareDj) {
        this.wareDj = wareDj;
    }

    public double getInPrice() {
        return inPrice;
    }

    public void setInPrice(double inPrice) {
        this.inPrice = inPrice;
    }

    public double getSunitPrice() {
        return sunitPrice;
    }

    public void setSunitPrice(double sunitPrice) {
        this.sunitPrice = sunitPrice;
    }

    public double getMinInPrice() {
        return minInPrice;
    }

    public void setMinInPrice(double minInPrice) {
        this.minInPrice = minInPrice;
    }

    public double getTcAmt() {
        return tcAmt;
    }

    public void setTcAmt(double tcAmt) {
        this.tcAmt = tcAmt;
    }
}
