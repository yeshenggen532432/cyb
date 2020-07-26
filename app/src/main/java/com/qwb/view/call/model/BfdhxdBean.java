package com.qwb.view.call.model;

import java.io.Serializable;

/**
 * 拜访查询--拜访纪录--订货下单
 *
 */


public class BfdhxdBean  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8726342350461333143L;
	private String id;   //商品id
	private String wareNm;//到货量
	private String wareId;//实销量
	private String wareDj;//库存量
	private String wareGg;//订单数
	private String orderId; //售销类型
	private String wareNum; //备注
	private String wareZj; //备注
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
	public String getWareId() {
		return wareId;
	}
	public void setWareId(String wareId) {
		this.wareId = wareId;
	}
	public String getWareDj() {
		return wareDj;
	}
	public void setWareDj(String wareDj) {
		this.wareDj = wareDj;
	}
	public String getWareGg() {
		return wareGg;
	}
	public void setWareGg(String wareGg) {
		this.wareGg = wareGg;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	
}
