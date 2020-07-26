package com.qwb.view.step.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 拜访流程4--查询销售小结
 * 
 */
public class queryXsxjBean  implements Serializable {
	private Integer cid; // 客户id
	private static final long serialVersionUID = 9214706235728464523L;
	private boolean state;
	private String msg;
	private List<QueryXsxj> list = new ArrayList<>();


	public List<QueryXsxj> getList() {
		return list;
	}

	public void setList(List<QueryXsxj> list) {
		this.list = list;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}


//	public  class QueryXsxj  implements Serializable {
//		private static final long serialVersionUID = -4262520155577321870L;
//		private String cid; // 客户id---关联
//		private String id; // 销售小结id
//		private String mid; // 业务员id
//		private String wid; // 商品id
//		private String dhNum;// 到货量
//		private String sxNum;// 实销量
//		private String kcNum;// 库存量
//		private String ddNum;// 订单数
//		private String xstp; // 售销类型
//		private String remo; // 备注
//		private String xjdate;// 日期
//		private String xxd;// 新鲜值
//		private String wareNm;// 商品名称
//		private String wareGg;// 规格
//		
//		public String getWid() {
//			return wid;
//		}
//
//		public void setWid(String wid) {
//			this.wid = wid;
//		}
//
//		public String getDhNum() {
//			return dhNum;
//		}
//
//		public void setDhNum(String dhNum) {
//			this.dhNum = dhNum;
//		}
//
//		public String getSxNum() {
//			return sxNum;
//		}
//
//		public void setSxNum(String sxNum) {
//			this.sxNum = sxNum;
//		}
//
//		public String getKcNum() {
//			return kcNum;
//		}
//
//		public void setKcNum(String kcNum) {
//			this.kcNum = kcNum;
//		}
//
//		public String getDdNum() {
//			return ddNum;
//		}
//
//		public void setDdNum(String ddNum) {
//			this.ddNum = ddNum;
//		}
//
//		public String getXstp() {
//			return xstp;
//		}
//
//		public void setXstp(String xstp) {
//			this.xstp = xstp;
//		}
//
//		public String getRemo() {
//			return remo;
//		}
//
//		public void setRemo(String remo) {
//			this.remo = remo;
//		}
//
//		public String get_Id() {
//			return id;
//		}
//
//		public void setId(String id) {
//			this.id = id;
//		}
//
//		public String getMid() {
//			return mid;
//		}
//
//		public void setMid(String mid) {
//			this.mid = mid;
//		}
//
//		public String getXjdate() {
//			return xjdate;
//		}
//
//		public void setXjdate(String xjdate) {
//			this.xjdate = xjdate;
//		}
//
//		public String getWareNm() {
//			return wareNm;
//		}
//
//		public void setWareNm(String wareNm) {
//			this.wareNm = wareNm;
//		}
//
//		public String getWareGg() {
//			return wareGg;
//		}
//
//		public void setWareGg(String wareGg) {
//			this.wareGg = wareGg;
//		}
//
//		public String getCid() {
//			return cid;
//		}
//
//		public void setCid(String cid) {
//			this.cid = cid;
//		}
//
//		public String getXxd() {
//			return xxd;
//		}
//
//		public void setXxd(String xxd) {
//			this.xxd = xxd;
//		}
//		
//	}

}
