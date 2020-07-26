package com.qwb.view.work.model;

import java.io.Serializable;

/**
 * 考勤item
 */

public class WorkSubBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5315509180040863614L;
	private String tp ;
	private String checkTime ;
	private String locationup ;
	private String upid ;
	private String memberName;//员工名称
	public String getTp() {
		return tp;
	}
	public void setTp(String tp) {
		this.tp = tp;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getLocationup() {
		return locationup;
	}
	public void setLocationup(String locationup) {
		this.locationup = locationup;
	}
	public String getUpid() {
		return upid;
	}
	public void setUpid(String upid) {
		this.upid = upid;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
	
}
