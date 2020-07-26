package com.qwb.view.customer.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

public class ClientLevelBean extends BaseBean {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8817701385399549094L;
	private List<ClientLevel> khlevells;

	public List<ClientLevel> getKhlevells() {
		return khlevells;
	}

	public void setKhlevells(List<ClientLevel> khlevells) {
		this.khlevells = khlevells;
	}

	public static class ClientLevel implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2238459278439323130L;
		private Integer id;
		private String khdjNm;  //名称
		private String coding;//编码
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getKhdjNm() {
			return khdjNm;
		}
		public void setKhdjNm(String khdjNm) {
			this.khdjNm = khdjNm;
		}
		public String getCoding() {
			return coding;
		}
		public void setCoding(String coding) {
			this.coding = coding;
		}
		
		
	}
}
