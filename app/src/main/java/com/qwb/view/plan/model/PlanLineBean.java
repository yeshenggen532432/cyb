package com.qwb.view.plan.model;

import java.util.List;

/**
 * 拜访计划-线路
 */
public class PlanLineBean  {
	private Integer id; // 线路id
	private Integer mid; // 业务员id
	private Integer num; // 客户数量
	private String xlNm; // 线路名称
	private List<PlanLineDetailBean> children;

	//----------------------------
	private boolean check;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMid() {
		return mid;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getXlNm() {
		return xlNm;
	}

	public void setXlNm(String xlNm) {
		this.xlNm = xlNm;
	}

	public List<PlanLineDetailBean> getChildren() {
		return children;
	}

	public void setChildren(List<PlanLineDetailBean> children) {
		this.children = children;
	}

	public boolean getCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}
}
