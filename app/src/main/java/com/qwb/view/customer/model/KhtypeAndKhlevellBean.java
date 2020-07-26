package com.qwb.view.customer.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KhtypeAndKhlevellBean extends BaseBean {

	private static final long serialVersionUID = -2759358531423553359L;

	private List<Qdtypels> qdtypels = new ArrayList<>();
	private List<Khlevells> khlevells = new ArrayList<>();

	

	public List<Qdtypels> getQdtypels() {
		return qdtypels;
	}

	public void setQdtypels(List<Qdtypels> qdtypels) {
		this.qdtypels = qdtypels;
	}

	public List<Khlevells> getKhlevells() {
		return khlevells;
	}

	public void setKhlevells(List<Khlevells> khlevells) {
		this.khlevells = khlevells;
	}

	public class Qdtypels implements Serializable {
		private static final long serialVersionUID = 1011140786617516622L;
		private int id;
		private String qdtpNm;
		private String remo;
		private String coding;
		private String sxeDate;
		private String sxaDate;
		private int isSx;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getQdtpNm() {
			return qdtpNm;
		}

		public void setQdtpNm(String qdtpNm) {
			this.qdtpNm = qdtpNm;
		}

		public String getRemo() {
			return remo;
		}

		public void setRemo(String remo) {
			this.remo = remo;
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

		public String getSxaDate() {
			return sxaDate;
		}

		public void setSxaDate(String sxaDate) {
			this.sxaDate = sxaDate;
		}

		public int getIsSx() {
			return isSx;
		}

		public void setIsSx(int isSx) {
			this.isSx = isSx;
		}

	}

	public class Khlevells implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8409225930480389130L;
		private int id;
		private String khdjNm;
		private String coding;
		private int qdId;

		public int getId() {
			return id;
		}

		public void setId(int id) {
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

		public int getQdId() {
			return qdId;
		}

		public void setQdId(int qdId) {
			this.qdId = qdId;
		}

	}

}
