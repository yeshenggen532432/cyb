package com.qwb.view.customer.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

public class BfCountBean extends BaseBean {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3319639760072931146L;
	private List<BfCount> bfpcls;
	
	
	public List<BfCount> getBfpcls() {
		return bfpcls;
	}

	public void setBfpcls(List<BfCount> bfpcls) {
		this.bfpcls = bfpcls;
	}

	public static class BfCount implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -676807352206601152L;
		private Integer id;
		private String pcNm;  //名称
		private String coding;//编码
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getPcNm() {
			return pcNm;
		}
		public void setPcNm(String pcNm) {
			this.pcNm = pcNm;
		}
		public String getCoding() {
			return coding;
		}
		public void setCoding(String coding) {
			this.coding = coding;
		}
		
		
	}
}
