package com.qwb.view.plan.model;

import com.qwb.view.base.model.BaseBean;
import java.util.List;

/**
 * 计划拜访--我的拜访
 */
public class PlanMineResult extends BaseBean {

	private Integer pageNo;
	private Integer pageSize;
	private Integer total;
	private Integer totalPage;
	private List<PlanBean> rows;
	private Integer coun1;//完成数
	private Integer coun2;//未完成数
	private String xlNm;//线路名称

	public Integer getCoun1() {
		return coun1;
	}

	public void setCoun1(Integer coun1) {
		this.coun1 = coun1;
	}

	public Integer getCoun2() {
		return coun2;
	}

	public void setCoun2(Integer coun2) {
		this.coun2 = coun2;
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

	public String getXlNm() {
		return xlNm;
	}

	public void setXlNm(String xlNm) {
		this.xlNm = xlNm;
	}

	public List<PlanBean> getRows() {
		return rows;
	}

	public void setRows(List<PlanBean> rows) {
		this.rows = rows;
	}
}
