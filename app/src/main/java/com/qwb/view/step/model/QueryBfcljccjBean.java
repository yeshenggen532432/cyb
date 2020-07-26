package com.qwb.view.step.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 拜访流程3--陈列检查采集（获取信息）
 */
public class QueryBfcljccjBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3175133435821390726L;
	private List<QueryBfcljccj> list;

	public List<QueryBfcljccj> getList() {
		return list;
	}

	public void setList(List<QueryBfcljccj> list) {
		this.list = list;
	}

	public static class QueryBfcljccj implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 900371207727534664L;
		private String id; // 陈列检查采集id
		private String mdid; // 模板id
		private String hjpms; // 货架排面数
		private String djpms; // 端架排面数
		private String sytwl; // 收银台围栏
		private String bds; // 冰点数
		private String remo; // 摘要

		private List<ClPhoto> bfxgPicLs;//图片

		public List<ClPhoto> getBfxgPicLs() {
			return bfxgPicLs;
		}

		public void setBfxgPicLs(List<ClPhoto> bfxgPicLs) {
			this.bfxgPicLs = bfxgPicLs;
		}

		public QueryBfcljccj() {
			super();
		}


		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getMdid() {
			return mdid;
		}

		public void setMdid(String mdid) {
			this.mdid = mdid;
		}

		public String getHjpms() {
			return hjpms;
		}

		public void setHjpms(String hjpms) {
			this.hjpms = hjpms;
		}

		public String getDjpms() {
			return djpms;
		}

		public void setDjpms(String djpms) {
			this.djpms = djpms;
		}

		public String getSytwl() {
			return sytwl;
		}

		public void setSytwl(String sytwl) {
			this.sytwl = sytwl;
		}

		public String getBds() {
			return bds;
		}

		public void setBds(String bds) {
			this.bds = bds;
		}

		public String getRemo() {
			return remo;
		}

		public void setRemo(String remo) {
			this.remo = remo;
		}

		public static class ClPhoto implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = -930859894423017764L;
			private Integer id;// 图片id
			private Integer type;// 1拜访签到拍照；2生动化；3堆头；4陈列检查采集；5道谢并告知下次拜访日期
			private Integer ssId;// 所属板块id
			private String picMini; // 小图
			private String pic; // 大图

			public Integer getId() {
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

		}

	}
}
