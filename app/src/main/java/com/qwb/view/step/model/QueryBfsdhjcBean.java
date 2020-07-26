package com.qwb.view.step.model;

import java.io.Serializable;
import java.util.List;

/**
 * 查询--生动化检查（获取信息）
 */

public class QueryBfsdhjcBean  implements Serializable {
	
	private static final long serialVersionUID = -8770828434678802623L;
	private boolean state;
	private String msg;
	
	private String id; // 生动化检查id
	private String cid; // 客户id--// 作为关联的字段
	private String isXy;// 是否显眼（1有，2无
	private String remo1;// 生动化摘要
	private String remo2;// 堆头摘要
	private String pophb;// POP海报
	private String cq;// 串旗
	private String wq;// 围裙
	private List<SdPhoto1> list1;// 生动化图片集合
	private List<SdPhoto2> list2;// 堆头图片集合

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

	public String getIsXy() {
		return isXy;
	}

	public void setIsXy(String isXy) {
		this.isXy = isXy;
	}

	public String getRemo1() {
		return remo1;
	}

	public void setRemo1(String remo1) {
		this.remo1 = remo1;
	}

	public String getRemo2() {
		return remo2;
	}

	public void setRemo2(String remo2) {
		this.remo2 = remo2;
	}

	public String getPophb() {
		return pophb;
	}

	public void setPophb(String pophb) {
		this.pophb = pophb;
	}

	public String getCq() {
		return cq;
	}

	public void setCq(String cq) {
		this.cq = cq;
	}

	public String getWq() {
		return wq;
	}

	public void setWq(String wq) {
		this.wq = wq;
	}

	public List<SdPhoto1> getList1() {
		return list1;
	}

	public void setList1(List<SdPhoto1> list1) {
		this.list1 = list1;
	}

	public List<SdPhoto2> getList2() {
		return list2;
	}

	public void setList2(List<SdPhoto2> list2) {
		this.list2 = list2;
	}
	
	public QueryBfsdhjcBean() {
		super();
	}


	public static class SdPhoto1  implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2401698210751783583L;
		// 作为关联的字段
		private Integer cid; // 客户id--// 作为关联的字段

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

		public SdPhoto1() {
			super();
		}
		
	}

	public static class SdPhoto2  implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1412084749489349629L;
		// 作为关联的字段
		private Integer cid; // 客户id--// 作为关联的字段

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

		public SdPhoto2() {
			super();
		}

	}
}
