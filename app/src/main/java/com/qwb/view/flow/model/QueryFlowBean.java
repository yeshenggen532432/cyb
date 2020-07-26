package com.qwb.view.flow.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 流动打卡查询
 */

public class QueryFlowBean implements Serializable {
	private int id;
	private int mid;
	private String address;
	private String memberNm;
	private String memberHead;
	private String signTime;
	private String latitude;
	private String longitude;
	private String remarks;
	private String voiceUrl;
	private int voiceTime;
	private List<PicBean> listpic = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMemberNm() {
		return memberNm;
	}

	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
	}

	public String getMemberHead() {
		return memberHead;
	}

	public void setMemberHead(String memberHead) {
		this.memberHead = memberHead;
	}

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getVoiceUrl() {
		return voiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}

	public int getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(int voiceTime) {
		this.voiceTime = voiceTime;
	}

	public List<PicBean> getListpic() {
		return listpic;
	}

	public void setListpic(List<PicBean> listpic) {
		this.listpic = listpic;
	}
}
