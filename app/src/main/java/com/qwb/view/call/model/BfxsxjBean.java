package com.qwb.view.call.model;

import java.io.Serializable;

/**
 * 拜访查询--拜访纪录--销售小结
 *
 */


public class BfxsxjBean  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4631212473505746295L;
	private String id;   //商品id
	private String wareNm;//到货量
	private String remo;//实销量
	private String cid;//库存量
	private String wareGg;//订单数
	private String mid; //售销类型
	private String ddNum; //备注
	private String dhNum; //备注
	private String kcNum; //备注
	private String sxNum; //备注
	private String wid; //备注
	private String xjdate; //备注
	private String xstp; //备注
	private String xxd; //新鲜度
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWareNm() {
		return wareNm;
	}
	public void setWareNm(String wareNm) {
		this.wareNm = wareNm;
	}
	public String getRemo() {
		return remo;
	}
	public void setRemo(String remo) {
		this.remo = remo;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getWareGg() {
		return wareGg;
	}
	public void setWareGg(String wareGg) {
		this.wareGg = wareGg;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getDdNum() {
		return ddNum;
	}
	public void setDdNum(String ddNum) {
		this.ddNum = ddNum;
	}
	public String getDhNum() {
		return dhNum;
	}
	public void setDhNum(String dhNum) {
		this.dhNum = dhNum;
	}
	public String getKcNum() {
		return kcNum;
	}
	public void setKcNum(String kcNum) {
		this.kcNum = kcNum;
	}
	public String getSxNum() {
		return sxNum;
	}
	public void setSxNum(String sxNum) {
		this.sxNum = sxNum;
	}
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public String getXjdate() {
		return xjdate;
	}
	public void setXjdate(String xjdate) {
		this.xjdate = xjdate;
	}
	public String getXstp() {
		return xstp;
	}
	public void setXstp(String xstp) {
		this.xstp = xstp;
	}
	public String getXxd() {
		return xxd;
	}
	public void setXxd(String xxd) {
		this.xxd = xxd;
	}
}
