package com.qwb.db;

import org.litepal.crud.LitePalSupport;

/**
 * 拜访地图
 */
public class DMapTrackBean extends LitePalSupport {
	private String mid;//用户ID（账号）
	private String companyId;//公司id
	private int userId;//用户ID（TODO 业务员的）
	private String userNm;//用户名称
	private String userTel;//用戶电话
	private String userHead;//头像
	private String address;//地址
	private String location;//经纬度
	private String memberJob;//职位
	private String zt;//运动；静止；异常
	private String times;//时间

//	private String os;//  手机型号，版本号："CUN-TL00   5.1",
//	private String azimuth;//方向（按照标准航向定义，正北为 0°，顺时针绕一圈为360°）
//	private String workStatus;//状态：1：上班，2：下班，3：拜访签到，4：拜访签退
//	private String speed;//速度
//	private String stayTime;//停留时间
//	private String checkInTime;
//	private String heartbeatSpan;//表示该用户最后一次上传信息到目前的时间（秒）
//	private String locationFrom;//定位类型--gps,wifi,lx
//	private String workingDistance;//表示上班总里程，存在多次上班打卡的情况，每段上班时间里程分段统计后求和
//	private String visitCheckInTime;//


	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public String getUserTel() {
		return userTel;
	}

	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	public String getUserHead() {
		return userHead;
	}

	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMemberJob() {
		return memberJob;
	}

	public void setMemberJob(String memberJob) {
		this.memberJob = memberJob;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}
}
