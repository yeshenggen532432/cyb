package com.qwb.view.step.model;

import java.io.Serializable;
import java.util.List;

/**
 * 拜访流程7--道谢并告知下次拜访（获取信息）
 */
public class QueryBfgzxcBean  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8440292418550922671L;
	private boolean state;
	private String msg;
	private String id; // 道谢并告知下次拜访id
	private String cid; // 客户id
	private String xsjdNm;//客户状态
	private String bfflNm;//拜访分类
	private String bcbfzj;//本次拜访总结
	private String dbsx;//代办事项
	private String xcdate;//下次拜访日期
	private List<XcbfPhoto> list;// 图片集合
	
	private String longitude;//
	private String latitude;//
	private String address;// 
	private String voiceUrl;//语音路径
	private String voiceTime;//语音时间
	
	public String getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(String voiceTime) {
		this.voiceTime = voiceTime;
	}

	public String getVoiceUrl() {
		return voiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getXsjdNm() {
		return xsjdNm;
	}

	public void setXsjdNm(String xsjdNm) {
		this.xsjdNm = xsjdNm;
	}

	public String getBfflNm() {
		return bfflNm;
	}

	public void setBfflNm(String bfflNm) {
		this.bfflNm = bfflNm;
	}

	public String getBcbfzj() {
		return bcbfzj;
	}

	public void setBcbfzj(String bcbfzj) {
		this.bcbfzj = bcbfzj;
	}

	public String getDbsx() {
		return dbsx;
	}

	public void setDbsx(String dbsx) {
		this.dbsx = dbsx;
	}

	public String getXcdate() {
		return xcdate;
	}

	public void setXcdate(String xcdate) {
		this.xcdate = xcdate;
	}

	public List<XcbfPhoto> getList() {
		return list;
	}

	public void setList(List<XcbfPhoto> list) {
		this.list = list;
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
	
	

	@Override
	public String toString() {
		return "QueryBfgzxcBean [state=" + state + ", msg=" + msg + ", id="
				+ id + ", cid=" + cid + ", xsjdNm=" + xsjdNm + ", bfflNm="
				+ bfflNm + ", bcbfzj=" + bcbfzj + ", dbsx=" + dbsx
				+ ", xcdate=" + xcdate + ", list=" + list + ", longitude="
				+ longitude + ", latitude=" + latitude + ", address=" + address
				+ "]";
	}



	public static class XcbfPhoto  implements Serializable {

		// 作为关联的字段
		private Integer cid; // 客户id
		private static final long serialVersionUID = 6984295957633637734L;
		private Integer id;// 图片id
		private Integer type;// 1拜访签到拍照；2生动化；3堆头；4陈列检查采集；5道谢并告知下次拜访日期
		private Integer ssId;// 所属板块id
		private String picMini; // 小图
		private String pic; // 大图

		public Integer get_Id() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public Integer getSsId() {
			return ssId;
		}

		public void setSsId(Integer ssId) {
			this.ssId = ssId;
		}

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

		public Integer getCid() {
			return cid;
		}

		public void setCid(Integer cid) {
			this.cid = cid;
		}
	}
}
