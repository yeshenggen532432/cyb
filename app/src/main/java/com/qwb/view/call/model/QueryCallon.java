package com.qwb.view.call.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 拜访查询
 */

public class QueryCallon implements Serializable {
	private static final long serialVersionUID = -8448879022361130391L;
	private int id;//拜访id
	private int cid;
	private int mid;
	private int jlm;

	private String voiceUrl;// 语音-路径
	private String voiceTime;// 语音-时长
	private String memberNm;
	private String khdjNm;
	private String qddate;// 时间段
	private String qdtime;// 时长
	private String khNm;
	private String memberHead;
	private String branchName;
	private String address;
	private int zfcount;//客户重复条数
	private String longitude;//签到
	private String latitude;
	private String longitude2;//签退
	private String latitude2;
	private String longitude3;//客户地址
	private String latitude3;
	private List<CommentItemBean> commentList = new ArrayList<>();
	private List<Pic> listpic = new ArrayList<>();

	private String bcbfzj;//拜访查询（总结和代办）；打卡查询（备注）
	private int type;//1:拜访查询； 2：打卡查询
	private String time;//时间（打卡查询）;
	
	public int getZfcount() {
		return zfcount;
	}

	public void setZfcount(int zfcount) {
		this.zfcount = zfcount;
	}

	public String getLongitude2() {
		return longitude2;
	}

	public void setLongitude2(String longitude2) {
		this.longitude2 = longitude2;
	}

	public String getLatitude2() {
		return latitude2;
	}

	public void setLatitude2(String latitude2) {
		this.latitude2 = latitude2;
	}

	public String getLongitude3() {
		return longitude3;
	}

	public void setLongitude3(String longitude3) {
		this.longitude3 = longitude3;
	}

	public String getLatitude3() {
		return latitude3;
	}

	public void setLatitude3(String latitude3) {
		this.latitude3 = latitude3;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(String voiceTime) {
		this.voiceTime = voiceTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getJlm() {
		return jlm;
	}

	public void setJlm(int jlm) {
		this.jlm = jlm;
	}

	public String getQdtime() {
		return qdtime;
	}

	public void setQdtime(String qdtime) {
		this.qdtime = qdtime;
	}

	public String getBcbfzj() {
		return bcbfzj;
	}

	public void setBcbfzj(String bcbfzj) {
		this.bcbfzj = bcbfzj;
	}

	public String getVoiceUrl() {
		return voiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}

	public String getMemberNm() {
		return memberNm;
	}

	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
	}

	public String getKhdjNm() {
		return khdjNm;
	}

	public void setKhdjNm(String khdjNm) {
		this.khdjNm = khdjNm;
	}

	public String getQddate() {
		return qddate;
	}

	public void setQddate(String qddate) {
		this.qddate = qddate;
	}

	public String getKhNm() {
		return khNm;
	}

	public void setKhNm(String khNm) {
		this.khNm = khNm;
	}

	public String getMemberHead() {
		return memberHead;
	}

	public void setMemberHead(String memberHead) {
		this.memberHead = memberHead;
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

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public List<CommentItemBean> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<CommentItemBean> commentList) {
		this.commentList = commentList;
	}

	public List<Pic> getListpic() {
		return listpic;
	}

	public void setListpic(List<Pic> listpic) {
		this.listpic = listpic;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	// 图片
	public static class Pic implements Serializable {
		private String picMin;
		private String pic;
		private String nm;

		public String getPicMin() {
			return picMin;
		}

		public void setPicMin(String picMin) {
			this.picMin = picMin;
		}

		public String getPic() {
			return pic;
		}

		public void setPic(String pic) {
			this.pic = pic;
		}

		public String getNm() {
			return nm;
		}

		public void setNm(String nm) {
			this.nm = nm;
		}
	}
}
