package com.qwb.view.step.model;

import com.qwb.view.base.model.BaseBean;
import com.qwb.view.step.model.XiaJi;

import java.util.List;

/**
 * 拜访流程6--供货下单获取信息
 */
public class QueryBforderBean extends BaseBean {
	private static final long serialVersionUID = -6165128123180903447L;
	private Integer id;
	private String orderNo;
	private Integer cid;
	private String shr;
	private String tel;
	private String address;
	private String remo;
	private String zje;//总金额
	private String zdzk;//整单折扣
	private String cjje;//成交金额
	private String shTime;//送货时间
	private String pszd;//配送指定（公司直送，转二批配送）
	private String orderZt;//订货下单--审核状态
	private Integer stkId;//仓库id
	private String stkName;//仓库名称
	private String khNm;//客户名称
	private Double freight;//运费
//	private Double promotionCost;//促销总额
	private Double orderAmount;//促销总额

	private List<XiaJi> list;

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public List<XiaJi> getList() {
		return list;
	}

	public void setList(List<XiaJi> list) {
		this.list = list;
	}

	public String getOrderZt() {
		return orderZt;
	}

	public void setOrderZt(String orderZt) {
		this.orderZt = orderZt;
	}

	public String getShTime() {
		return shTime;
	}

	public void setShTime(String shTime) {
		this.shTime = shTime;
	}

	public String getPszd() {
		return pszd;
	}

	public void setPszd(String pszd) {
		this.pszd = pszd;
	}

	public String getZje() {
		return zje;
	}

	public void setZje(String zje) {
		this.zje = zje;
	}

	public String getZdzk() {
		return zdzk;
	}

	public void setZdzk(String zdzk) {
		this.zdzk = zdzk;
	}

	public String getCjje() {
		return cjje;
	}

	public void setCjje(String cjje) {
		this.cjje = cjje;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getShr() {
		return shr;
	}

	public void setShr(String shr) {
		this.shr = shr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemo() {
		return remo;
	}

	public void setRemo(String remo) {
		this.remo = remo;
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

	public String getKhNm() {
		return khNm;
	}

	public void setKhNm(String khNm) {
		this.khNm = khNm;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}
