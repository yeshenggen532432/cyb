package com.qwb.view.flow.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 流动打卡-拜访回放
 */
public class FlowBfListBean extends BaseBean {


	private List<FlowBfBean> list = new ArrayList<>();
	
	public List<FlowBfBean> getList() {
		return list;
	}

	public void setList(List<FlowBfBean> list) {
		this.list = list;
	}

	public class FlowBfBean implements Serializable {
		private int id;
		private int mid;
		private String longitude;
		private String latitude;
		private String address;
		private String signTime;
		private String remarks;
		private Integer status;
		private int voiceTime;
		private String voiceUrl;
		private String signType;//1.上班，2：下班，3.流动

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

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getSignTime() {
			return signTime;
		}

		public void setSignTime(String signTime) {
			this.signTime = signTime;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public int getVoiceTime() {
			return voiceTime;
		}

		public void setVoiceTime(int voiceTime) {
			this.voiceTime = voiceTime;
		}

		public String getVoiceUrl() {
			return voiceUrl;
		}

		public void setVoiceUrl(String voiceUrl) {
			this.voiceUrl = voiceUrl;
		}

		public String getSignType() {
			return signType;
		}

		public void setSignType(String signType) {
			this.signType = signType;
		}
	}
}
