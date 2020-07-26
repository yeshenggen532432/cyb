package com.qwb.view.table.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 统计表——销售订单
 * 
 */
public class Statement_xsddBean extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3089882487660488494L;
	private Integer pageNo;
	private Integer pageSize;
	private Integer total;
	private Integer totalPage;
	private List<Xsdd> rows = new ArrayList<>();
	private List<FooterBean> footer = new ArrayList<>();


	public List<FooterBean> getFooter() {
		return footer;
	}

	public void setFooter(List<FooterBean> footer) {
		this.footer = footer;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public List<Xsdd> getRows() {
		return rows;
	}

	public void setRows(List<Xsdd> rows) {
		this.rows = rows;
	}

	public class Xsdd implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -6545761704669726670L;
		private String orderIds; // 拜访id（拜访流程--拜访查询）
		private String memberNm; // 业务员名称
		private String khNm; // 客户名称
		private List<Statement_list1Bean> list1=new ArrayList<>();
		
		
		public String getOrderIds() {
			return orderIds;
		}
		public void setOrderIds(String orderIds) {
			this.orderIds = orderIds;
		}
		public String getMemberNm() {
			return memberNm;
		}
		public void setMemberNm(String memberNm) {
			this.memberNm = memberNm;
		}
		public String getKhNm() {
			return khNm;
		}
		public void setKhNm(String khNm) {
			this.khNm = khNm;
		}
		public List<Statement_list1Bean> getList1() {
			return list1;
		}
		public void setList1(List<Statement_list1Bean> list1) {
			this.list1 = list1;
		}
	}	
}
