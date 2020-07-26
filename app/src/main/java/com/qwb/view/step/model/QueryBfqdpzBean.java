package com.qwb.view.step.model;

import java.io.Serializable;
import java.util.List;

/**
 * 查询拜访签到拍照
 */
public class QueryBfqdpzBean  implements Serializable {
	
	private static final long serialVersionUID = 8674884442304642823L;
	private boolean state;
	private String msg;
	private String id; // 拜访签到拍照id
	private String cid; // 客户id
	private String longitude;//
	private String latitude;//
	private String address;// 
	private String hbzt;// 及时更换外观破损，肮脏的海报招贴
	private String ggyy;// 拆除过时的附有旧广告用语的宣传品
	private String isXy;// 是否显眼（1有，2无
	private String remo;// 摘要
	private List<BfphotoBean> list;// 图片集合

	public QueryBfqdpzBean() {
		super();
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

	public String getHbzt() {
		return hbzt;
	}

	public void setHbzt(String hbzt) {
		this.hbzt = hbzt;
	}

	public String getGgyy() {
		return ggyy;
	}

	public void setGgyy(String ggyy) {
		this.ggyy = ggyy;
	}

	public String getIsXy() {
		return isXy;
	}

	public void setIsXy(String isXy) {
		this.isXy = isXy;
	}

	public String getRemo() {
		return remo;
	}

	public void setRemo(String remo) {
		this.remo = remo;
	}


	public List<BfphotoBean> getList() {
		return list;
	}


	public void setList(List<BfphotoBean> list) {
		this.list = list;
	}

	

//	public class QdPhoto  implements Serializable {
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = -1412084749489349629L;
//		private Integer id;// 图片id
//		private Integer type;// 1拜访签到拍照；2生动化；3堆头；4陈列检查采集；5道谢并告知下次拜访日期
//		private Integer ssId;// 所属板块id
//		private String picMini; // 小图
//		private String pic; // 大图
//
//		
//		//自己添加--与上面对应
//		private Integer cid; // 客户id
//
//		public QdPhoto() {
//			super();
//		}
//
//		public Integer getI_d() {
//			return id;
//		}
//
//		public void setId(Integer id) {
//			this.id = id;
//		}
//
//		public Integer getCid() {
//			return cid;
//		}
//
//		public void setCid(Integer cid) {
//			this.cid = cid;
//		}
//
//		public Integer get_Id() {
//			return id;
//		}
//
//		public void set_Id(Integer id) {
//			this.id = id;
//		}
//
//		public Integer getType() {
//			return type;
//		}
//
//		public void setType(Integer type) {
//			this.type = type;
//		}
//
//		public Integer getSsId() {
//			return ssId;
//		}
//
//		public void setSsId(Integer ssId) {
//			this.ssId = ssId;
//		}
//
//		public String getPicMini() {
//			return picMini;
//		}
//
//		public void setPicMini(String picMini) {
//			this.picMini = picMini;
//		}
//
//		public String getPic() {
//			return pic;
//		}
//
//		public void setPic(String pic) {
//			this.pic = pic;
//		}
//
//	}
}
