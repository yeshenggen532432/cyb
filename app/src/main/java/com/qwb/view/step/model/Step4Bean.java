package com.qwb.view.step.model;

import java.io.Serializable;


public class Step4Bean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4533773538523294951L;
	private String wid;   //商品id
	private String dhNum;//到货量
	private String sxNum;//实销量
	private String kcNum;//库存量
	private String ddNum;//订单数
	private String xstp; //售销类型
	private String remo; //备注
	private String xxd; //
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public String getDhNum() {
		return dhNum;
	}
	public void setDhNum(String dhNum) {
		this.dhNum = dhNum;
	}
	public String getSxNum() {
		return sxNum;
	}
	public void setSxNum(String sxNum) {
		this.sxNum = sxNum;
	}
	public String getKcNum() {
		return kcNum;
	}
	public void setKcNum(String kcNum) {
		this.kcNum = kcNum;
	}
	public String getDdNum() {
		return ddNum;
	}
	public void setDdNum(String ddNum) {
		this.ddNum = ddNum;
	}
	public String getXstp() {
		return xstp;
	}
	public void setXstp(String xstp) {
		this.xstp = xstp;
	}
	public String getRemo() {
		return remo;
	}
	public void setRemo(String remo) {
		this.remo = remo;
	}
	public String getXxd() {
		return xxd;
	}
	public void setXxd(String xxd) {
		this.xxd = xxd;
	}
	
		
		
}
