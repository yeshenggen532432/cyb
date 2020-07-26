package com.qwb.view.table.model;

import java.io.Serializable;

/**
 * 统计表——销售订单
 * 
 */
public class Statement_list1Bean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5246838942022508602L;
	private String xsTp; // 销售类型
	private String wareNm; // 商品名称
	private Integer wareNum; // 订单数量
	private Double wareZj; // 订单金额
	private String wareDw; // 单位
	private Double wareDj; //单价
	public String getXsTp() {
		return xsTp;
	}
	public void setXsTp(String xsTp) {
		this.xsTp = xsTp;
	}
	public String getWareNm() {
		return wareNm;
	}
	public void setWareNm(String wareNm) {
		this.wareNm = wareNm;
	}
	public Double getWareZj() {
		return wareZj;
	}
	public void setWareZj(Double wareZj) {
		this.wareZj = wareZj;
	}
	public String getWareDw() {
		return wareDw;
	}
	public void setWareDw(String wareDw) {
		this.wareDw = wareDw;
	}
	public Double getWareDj() {
		return wareDj;
	}
	public void setWareDj(Double wareDj) {
		this.wareDj = wareDj;
	}
	public Integer getWareNum() {
		return wareNum;
	}
	public void setWareNum(Integer wareNum) {
		this.wareNum = wareNum;
	}
}
