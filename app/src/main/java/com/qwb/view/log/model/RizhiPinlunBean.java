package com.qwb.view.log.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志模块--获取报评论列表
 * 
 */
public class RizhiPinlunBean extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6579937807861785132L;
	private int pageNo;
	private int pageSize;
	private int total;
	private int totalPage;
	private List<RizhiPinlun> rows = new ArrayList<>();

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

	public List<RizhiPinlun> getRows() {
		return rows;
	}

	public void setRows(List<RizhiPinlun> rows) {
		this.rows = rows;
	}

	public class RizhiPinlun implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7531306487684605638L;
		private int id; // 评论id
		private int memberId; // 用户id
		private int bid; // 报id
		private String memberNm; // 用户名称
		private String content; // 评论内容
		private String pltime; // 评论时间

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getMemberId() {
			return memberId;
		}

		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}

		public int getBid() {
			return bid;
		}

		public void setBid(int bid) {
			this.bid = bid;
		}

		public String getMemberNm() {
			return memberNm;
		}

		public void setMemberNm(String memberNm) {
			this.memberNm = memberNm;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getPltime() {
			return pltime;
		}

		public void setPltime(String pltime) {
			this.pltime = pltime;
		}

	}
}
