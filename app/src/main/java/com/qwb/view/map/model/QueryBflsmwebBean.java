package com.qwb.view.map.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 员工在线--获取拜访客户轨迹
 * 
 */
public class QueryBflsmwebBean extends BaseBean {
	private static final long serialVersionUID = -6737614668186543000L;
	private List<QueryBfHf> list = new ArrayList<>();
	
	public List<QueryBfHf> getList() {
		return list;
	}
	public void setList(List<QueryBfHf> list) {
		this.list = list;
	}
	public class QueryBfHf implements Serializable {
		
		private static final long serialVersionUID = -7166116994292820036L;
		
		private int id;//拜访id
		private int cid;//客户id
		private int mid;//用户id
		private String address;//地址
		private String date;//日期
		private String time1;//签到时间
		private String time2;//签退时间
		private String khNm;//客户名称
		private String longitude;//经度
		private String latitude;//纬度
		private String xhNm;//序号--拜访的商家排号
		private String ys;//颜色--暂时“红色，绿色，蓝色，黄色，黑色”
		private String voiceUrl;//语音
		private int voiceTime;//语音
		private int fz;//停留时间--分
		
		
		
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
		public String getYs() {
			return ys;
		}
		public void setYs(String ys) {
			this.ys = ys;
		}
		public int getFz() {
			return fz;
		}
		public void setFz(int fz) {
			this.fz = fz;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getXhNm() {
			return xhNm;
		}
		public void setXhNm(String xhNm) {
			this.xhNm = xhNm;
		}
		public int getCid() {
			return cid;
		}
		public void setCid(int cid) {
			this.cid = cid;
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
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getTime1() {
			return time1;
		}
		public void setTime1(String time1) {
			this.time1 = time1;
		}
		public String getKhNm() {
			return khNm;
		}
		public void setKhNm(String khNm) {
			this.khNm = khNm;
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
		public String getTime2() {
			return time2;
		}
		public void setTime2(String time2) {
			this.time2 = time2;
		}
	}
}
