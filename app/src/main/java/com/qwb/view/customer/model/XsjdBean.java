package com.qwb.view.customer.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

public class XsjdBean extends BaseBean {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1529990313435360282L;
	private List<Xsphasels> xsphasels;
	


	public List<Xsphasels> getXsphasels() {
		return xsphasels;
	}



	public void setXsphasels(List<Xsphasels> xsphasels) {
		this.xsphasels = xsphasels;
	}



	public static class Xsphasels implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7280575272156671183L;
		private Integer id;
		private String phaseNm;  //名称
		private String coding;//编码
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		
		public String getPhaseNm() {
			return phaseNm;
		}
		public void setPhaseNm(String phaseNm) {
			this.phaseNm = phaseNm;
		}
		public String getCoding() {
			return coding;
		}
		public void setCoding(String coding) {
			this.coding = coding;
		}
		
		
	}
}
