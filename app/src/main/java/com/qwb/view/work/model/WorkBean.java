package com.qwb.view.work.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 班次信息
 */
public class WorkBean extends BaseBean {
	private Integer id;
	private String bcCode;
	private Integer bcType;
	private String remarks;
	private String bcName;//班次名称
	private Integer lateMinute;
	private Integer crossDay;
	private Integer afterMinute;
	private Integer beforeMinute;
	private Integer earlyMinute;
	private String longitude;//经度
	private String latitude;//纬度
	private Integer outOf;//是否可超出范围0不可 1可以
	private Integer areaLong;//范围(米)
	private Integer status;
	private String address;
	private List<WorkClassBean> subList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBcCode() {
		return bcCode;
	}

	public void setBcCode(String bcCode) {
		this.bcCode = bcCode;
	}

	public Integer getBcType() {
		return bcType;
	}

	public void setBcType(Integer bcType) {
		this.bcType = bcType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBcName() {
		return bcName;
	}

	public void setBcName(String bcName) {
		this.bcName = bcName;
	}

	public Integer getLateMinute() {
		return lateMinute;
	}

	public void setLateMinute(Integer lateMinute) {
		this.lateMinute = lateMinute;
	}

	public Integer getCrossDay() {
		return crossDay;
	}

	public void setCrossDay(Integer crossDay) {
		this.crossDay = crossDay;
	}

	public Integer getAfterMinute() {
		return afterMinute;
	}

	public void setAfterMinute(Integer afterMinute) {
		this.afterMinute = afterMinute;
	}

	public Integer getBeforeMinute() {
		return beforeMinute;
	}

	public void setBeforeMinute(Integer beforeMinute) {
		this.beforeMinute = beforeMinute;
	}

	public Integer getEarlyMinute() {
		return earlyMinute;
	}

	public void setEarlyMinute(Integer earlyMinute) {
		this.earlyMinute = earlyMinute;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public Integer getOutOf() {
		return outOf;
	}

	public void setOutOf(Integer outOf) {
		this.outOf = outOf;
	}

	public Integer getAreaLong() {
		return areaLong;
	}

	public void setAreaLong(Integer areaLong) {
		this.areaLong = areaLong;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<WorkClassBean> getSubList() {
		return subList;
	}

	public void setSubList(List<WorkClassBean> subList) {
		this.subList = subList;
	}
}
