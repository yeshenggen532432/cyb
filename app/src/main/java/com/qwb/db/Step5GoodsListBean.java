package com.qwb.db;

import org.litepal.crud.LitePalSupport;


/**
 * 订货下单-选择商品
 */
public class Step5GoodsListBean extends LitePalSupport {
//	前提：查询条件
    private String cid;//客户
	private String userID;//用户id
	private String companyID;//公司id
	private String dhTp;//订货类型：1：拜访下单，2：拜访修改。。。。


	public String  goodsID = "c0";//步骤5-商品id
	public String  goodsName = "c1";//步骤5-商品名称
	public String  goodsXstp = "c2";//步骤5-销售类型
	public String  goodsCount = "c3";//步骤5-商品数量
	public String  goodsDw= "c4";//步骤5-商品单位
	public String  goodsDj = "c5";//步骤5-商品单价
	public String  goodsDjTemp = "c5_temp";//步骤5-商品单价(临时)
	public String  goodsZj = "c6";//步骤5-商品总价
	public String  goodsDel = "c7";//步骤5-删除
	public String  goodsBeunit = "c8";//步骤5-包装单位代码或计量单位代码
	public String  goodsMaxunitCode = "c9";//步骤5-包装单位代码
	public String  goodsMinunitCode= "c10";//步骤5-计量单位代码
	public String  goodsMaxunit = "c11";//步骤5-包装单位
	public String  goodsMinunit = "c12";//步骤5-计量单位
	public String  goodsHsNum = "c13";//步骤5-换算数量
	public String  goodsZxj = "c14";//步骤5- 执行价
	public String  goodsYj = "c15";//步骤5- 原价
	public String  goodsWg = "c16";//步骤5-商品规格
	public String  goodsRemark = "c17";//步骤5-备注

	public String getGoodsID() {
		return goodsID;
	}

	public void setGoodsID(String goodsID) {
		this.goodsID = goodsID;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsXstp() {
		return goodsXstp;
	}

	public void setGoodsXstp(String goodsXstp) {
		this.goodsXstp = goodsXstp;
	}

	public String getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}

	public String getGoodsDw() {
		return goodsDw;
	}

	public void setGoodsDw(String goodsDw) {
		this.goodsDw = goodsDw;
	}

	public String getGoodsDj() {
		return goodsDj;
	}

	public void setGoodsDj(String goodsDj) {
		this.goodsDj = goodsDj;
	}

	public String getGoodsDjTemp() {
		return goodsDjTemp;
	}

	public void setGoodsDjTemp(String goodsDjTemp) {
		this.goodsDjTemp = goodsDjTemp;
	}

	public String getGoodsZj() {
		return goodsZj;
	}

	public void setGoodsZj(String goodsZj) {
		this.goodsZj = goodsZj;
	}

	public String getGoodsDel() {
		return goodsDel;
	}

	public void setGoodsDel(String goodsDel) {
		this.goodsDel = goodsDel;
	}

	public String getGoodsBeunit() {
		return goodsBeunit;
	}

	public void setGoodsBeunit(String goodsBeunit) {
		this.goodsBeunit = goodsBeunit;
	}

	public String getGoodsMaxunitCode() {
		return goodsMaxunitCode;
	}

	public void setGoodsMaxunitCode(String goodsMaxunitCode) {
		this.goodsMaxunitCode = goodsMaxunitCode;
	}

	public String getGoodsMinunitCode() {
		return goodsMinunitCode;
	}

	public void setGoodsMinunitCode(String goodsMinunitCode) {
		this.goodsMinunitCode = goodsMinunitCode;
	}

	public String getGoodsMaxunit() {
		return goodsMaxunit;
	}

	public void setGoodsMaxunit(String goodsMaxunit) {
		this.goodsMaxunit = goodsMaxunit;
	}

	public String getGoodsMinunit() {
		return goodsMinunit;
	}

	public void setGoodsMinunit(String goodsMinunit) {
		this.goodsMinunit = goodsMinunit;
	}

	public String getGoodsHsNum() {
		return goodsHsNum;
	}

	public void setGoodsHsNum(String goodsHsNum) {
		this.goodsHsNum = goodsHsNum;
	}

	public String getGoodsZxj() {
		return goodsZxj;
	}

	public void setGoodsZxj(String goodsZxj) {
		this.goodsZxj = goodsZxj;
	}

	public String getGoodsYj() {
		return goodsYj;
	}

	public void setGoodsYj(String goodsYj) {
		this.goodsYj = goodsYj;
	}

	public String getGoodsWg() {
		return goodsWg;
	}

	public void setGoodsWg(String goodsWg) {
		this.goodsWg = goodsWg;
	}

	public String getGoodsRemark() {
		return goodsRemark;
	}

	public void setGoodsRemark(String goodsRemark) {
		this.goodsRemark = goodsRemark;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getCompanyID() {
		return companyID;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

	public String getDhTp() {
		return dhTp;
	}

	public void setDhTp(String dhTp) {
		this.dhTp = dhTp;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
}
