package com.qwb.db;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;


public class LocationBean extends LitePalSupport implements Serializable{
	private double longitude ;
	private double latitude ;
	private long location_time ;//定位时间
	private String address;
	private String location_from ;//gps,wifi
	private String os ;//android:ios
	private String location_date ;//日期
	private String upload_time ;//上传到服务器时间
	private double speed ;//速度--米/秒
	private long stay_time ;//停留时间--秒
	private int work_status ;//状态--1:上班，2：下班，3：拜访签到，4：拜访签退
	private long check_in_time ;//上班时间--保存本地，一直上传
	private long visit_check_in_time ;//拜访时间
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public long getLocation_time() {
		return location_time;
	}
	public void setLocation_time(long location_time) {
		this.location_time = location_time;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLocation_from() {
		return location_from;
	}
	public void setLocation_from(String location_from) {
		this.location_from = location_from;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getLocation_date() {
		return location_date;
	}
	public void setLocation_date(String location_date) {
		this.location_date = location_date;
	}
	public String getUpload_time() {
		return upload_time;
	}
	public void setUpload_time(String upload_time) {
		this.upload_time = upload_time;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public long getStay_time() {
		return stay_time;
	}
	public void setStay_time(long stay_time) {
		this.stay_time = stay_time;
	}
	public int getWork_status() {
		return work_status;
	}
	public void setWork_status(int work_status) {
		this.work_status = work_status;
	}
	public long getCheck_in_time() {
		return check_in_time;
	}
	public void setCheck_in_time(long check_in_time) {
		this.check_in_time = check_in_time;
	}
	public long getVisit_check_in_time() {
		return visit_check_in_time;
	}
	public void setVisit_check_in_time(long visit_check_in_time) {
		this.visit_check_in_time = visit_check_in_time;
	}
	
	
}
