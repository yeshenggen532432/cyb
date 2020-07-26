package com.qwb.view.work.model;

import java.io.Serializable;
import java.util.List;

public class WorkDetailBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4932605156539057306L;
	private int id;
	private String jobContent;
	private String latitude;
	private String longitude;
	private String location;
	private String memberNm;
	private String remark;
	private String checkTime;
	private String tp;
	private List<PicList> picList;
	private String msg ;
	private boolean state ;
	
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getTp() {
		return tp;
	}
	public void setTp(String tp) {
		this.tp = tp;
	}
	public String getMemberNm() {
		return memberNm;
	}
	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
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

	public List<PicList> getPicList() {
		return picList;
	}

	public void setPicList(List<PicList> picList) {
		this.picList = picList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getJobContent() {
		return jobContent;
	}

	public void setJobContent(String jobContent) {
		this.jobContent = jobContent;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public class PicList implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3679396419217689936L;
		private String picMini;
		private String pic;

		public String getPicMini() {
			return picMini;
		}

		public void setPicMini(String picMini) {
			this.picMini = picMini;
		}

		public String getPic() {
			return pic;
		}

		public void setPic(String pic) {
			this.pic = pic;
		}

	}
}
