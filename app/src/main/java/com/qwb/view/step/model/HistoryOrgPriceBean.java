package com.qwb.view.step.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/*
 * 历史价格，原价
 */
public class HistoryOrgPriceBean extends BaseBean {

	private String autoPrice;//0执行价；1历史价
	private List<Data> list;

	public List<Data> getList() {
		return list;
	}

	public void setList(List<Data> list) {
		this.list = list;
	}

	public String getAutoPrice() {
		return autoPrice;
	}

	public void setAutoPrice(String autoPrice) {
		this.autoPrice = autoPrice;
	}

	public static class Data {
//		{
//			"msg": "获取信息成功",
//				"state": true,
//				"list": [
//						{
//							"wareId": 9,
//								"hsNum": 24,
//								"beUnit": "B",
//								"orgPrice": 60,
//								"wareDj": 30,
//		"minHisPrice"
//						}
//			  ]
//		}

		private String orgPrice;//原价
		private String wareDj;//历史价格
		private String minHisPrice;//历史小单位

		private int id;
	    private int hsNum;
		private String xsTp;
		private String wareId;
		private String orderId;
		private String wareNum;
		private String wareZj;
		private String beUnit;

		public String getOrgPrice() {
			return orgPrice;
		}

		public void setOrgPrice(String orgPrice) {
			this.orgPrice = orgPrice;
		}

		public String getWareDj() {
			return wareDj;
		}

		public void setWareDj(String wareDj) {
			this.wareDj = wareDj;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getHsNum() {
			return hsNum;
		}

		public void setHsNum(int hsNum) {
			this.hsNum = hsNum;
		}

		public String getXsTp() {
			return xsTp;
		}

		public void setXsTp(String xsTp) {
			this.xsTp = xsTp;
		}

		public String getWareId() {
			return wareId;
		}

		public void setWareId(String wareId) {
			this.wareId = wareId;
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

		public String getBeUnit() {
			return beUnit;
		}

		public void setBeUnit(String beUnit) {
			this.beUnit = beUnit;
		}

		public String getMinHisPrice() {
			return minHisPrice;
		}

		public void setMinHisPrice(String minHisPrice) {
			this.minHisPrice = minHisPrice;
		}
	}
}
