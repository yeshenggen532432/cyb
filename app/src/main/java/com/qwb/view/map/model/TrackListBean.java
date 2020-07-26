package com.qwb.view.map.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 员工分布图--所有用户轨迹列表
 * @date 2017-05-06  添加长连接需要的一些字段
 */
public class TrackListBean extends BaseBean {
	private static final long serialVersionUID = -7411664779963015176L;
	private int pageNo;
	private int pageSize;
	private int total;
	private String zt;    //1:只有自己
	private int totalPage;
	private List<TrackList> rows;
	

	public String getZt() {
		return zt;
	}


	public void setZt(String zt) {
		this.zt = zt;
	}


	public Integer getPageNo() {
		return pageNo;
	}


	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}


	public Integer getPageSize() {
		return pageSize;
	}


	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	public Integer getTotal() {
		return total;
	}


	public void setTotal(Integer total) {
		this.total = total;
	}


	public Integer getTotalPage() {
		return totalPage;
	}


	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}


	public List<TrackList> getRows() {
		return rows;
	}


	public void setRows(List<TrackList> rows) {
		this.rows = rows;
	}


	public static class TrackList implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7183933628310895488L;
		private int userId;//用户ID
		private String times;//时间
		private String userTel;//用戶电话
		private String userNm;//用户名称
		private String location;//经纬度
		private String zt;//运动；静止；异常
		
		private String address;//地址
		private String os;//  手机型号，版本号："CUN-TL00   5.1",
		private String azimuth;//方向（按照标准航向定义，正北为 0°，顺时针绕一圈为360°）
		private String workStatus;//状态：1：上班，2：下班，3：拜访签到，4：拜访签退
		private String speed;//速度
		private String stayTime;//停留时间
		private String checkInTime;
		private String heartbeatSpan;//表示该用户最后一次上传信息到目前的时间（秒）
		private String locationFrom;//定位类型--gps,wifi,lx
		private String workingDistance;//表示上班总里程，存在多次上班打卡的情况，每段上班时间里程分段统计后求和
		private String visitCheckInTime;//
		private String memberJob;//职位
		
		//暂时没有
		private String userHead;//头像
		
		
		
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
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getUserHead() {
			return userHead;
		}
		public void setUserHead(String userHead) {
			this.userHead = userHead;
		}
		public String getTimes() {
			return times;
		}
		public void setTimes(String times) {
			this.times = times;
		}
		public String getUserTel() {
			return userTel;
		}
		public void setUserTel(String userTel) {
			this.userTel = userTel;
		}
		public String getUserNm() {
			return userNm;
		}
		public void setUserNm(String userNm) {
			this.userNm = userNm;
		}
		public String getOs() {
			return os;
		}
		public void setOs(String os) {
			this.os = os;
		}
		public String getAzimuth() {
			return azimuth;
		}
		public void setAzimuth(String azimuth) {
			this.azimuth = azimuth;
		}
		public String getWorkStatus() {
			return workStatus;
		}
		public void setWorkStatus(String workStatus) {
			this.workStatus = workStatus;
		}
		public String getSpeed() {
			return speed;
		}
		public void setSpeed(String speed) {
			this.speed = speed;
		}
		public String getStayTime() {
			return stayTime;
		}
		public void setStayTime(String stayTime) {
			this.stayTime = stayTime;
		}
		public String getCheckInTime() {
			return checkInTime;
		}
		public void setCheckInTime(String checkInTime) {
			this.checkInTime = checkInTime;
		}
		public String getHeartbeatSpan() {
			return heartbeatSpan;
		}
		public void setHeartbeatSpan(String heartbeatSpan) {
			this.heartbeatSpan = heartbeatSpan;
		}
		public String getLocationFrom() {
			return locationFrom;
		}
		public void setLocationFrom(String locationFrom) {
			this.locationFrom = locationFrom;
		}
		public String getVisitCheckInTime() {
			return visitCheckInTime;
		}
		public void setVisitCheckInTime(String visitCheckInTime) {
			this.visitCheckInTime = visitCheckInTime;
		}
		public String getWorkingDistance() {
			return workingDistance;
		}
		public void setWorkingDistance(String workingDistance) {
			this.workingDistance = workingDistance;
		}


		@Override
		public String toString() {
			return times+"----"+userNm;
		}
	}
}
