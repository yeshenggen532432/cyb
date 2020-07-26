package com.qwb.view.work.model;

import com.qwb.view.base.model.BaseBean;

/**
 * 班次信息
 */
public class WorkClassBean extends BaseBean {
	private Integer id;
	private Integer bcId;
	private String startTime;
	private String endTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBcId() {
		return bcId;
	}

	public void setBcId(Integer bcId) {
		this.bcId = bcId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
