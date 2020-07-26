package com.qwb.view.plan.model;


/**
 * 计划拜访--线路--详情
 */
public class PlanLineDetailBean {
	private Integer xlId; // 线路id
	private String scbfDate; //上次拜访
	private Integer id;
	private String khNm; // 客户名称
	private String cid; // 客户id
	private String longitude ;
	private String latitude ;

	public Integer getXlId() {
		return xlId;
	}

	public void setXlId(Integer xlId) {
		this.xlId = xlId;
	}

	public String getScbfDate() {
		return scbfDate;
	}

	public void setScbfDate(String scbfDate) {
		this.scbfDate = scbfDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKhNm() {
		return khNm;
	}

	public void setKhNm(String khNm) {
		this.khNm = khNm;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
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
}
