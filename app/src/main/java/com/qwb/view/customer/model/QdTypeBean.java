package com.qwb.view.customer.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

public class QdTypeBean extends BaseBean {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4749469566942142833L;
	private List<Qdtypels> qdtypels;

	public List<Qdtypels> getQdtypels() {
		return qdtypels;
	}

	public void setQdtypels(List<Qdtypels> qdtypels) {
		this.qdtypels = qdtypels;
	}

	public static class Qdtypels implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2238459278439323130L;
		private Integer id;
		private String qdtpNm;  //名称
		private String remo;  //名称
		private String coding;//编码
		private String sxeDate;//失效日期
		private String isSx;//是否生效（1是；2否）
		private String sxaDate;//生效日期
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getQdtpNm() {
			return qdtpNm;
		}
		public void setQdtpNm(String qdtpNm) {
			this.qdtpNm = qdtpNm;
		}
		public String getCoding() {
			return coding;
		}
		public void setCoding(String coding) {
			this.coding = coding;
		}
		public String getSxeDate() {
			return sxeDate;
		}
		public void setSxeDate(String sxeDate) {
			this.sxeDate = sxeDate;
		}
		public String getIsSx() {
			return isSx;
		}
		public void setIsSx(String isSx) {
			this.isSx = isSx;
		}
		public String getSxaDate() {
			return sxaDate;
		}
		public void setSxaDate(String sxaDate) {
			this.sxaDate = sxaDate;
		}
		public String getRemo() {
			return remo;
		}
		public void setRemo(String remo) {
			this.remo = remo;
		}
	}
}
