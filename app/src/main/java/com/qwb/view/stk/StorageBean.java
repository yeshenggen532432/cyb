package com.qwb.view.stk;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 仓库
 */
public class StorageBean extends BaseBean {
	
	private List<Storage> list;

	public List<Storage> getList() {
		return list;
	}

	public void setList(List<Storage> list) {
		this.list = list;
	}

	public static class Storage implements Serializable {

//		 "remarks": "",
//				 "tel": "13774659914",
//				 "stkName": "1号仓库",
//				 "stkNo": "001",
//				 "fbtime": "2017-08-18 23:58:25",
//				 "stkManager": "zrg",
//				 "id": 7,
//				 "address": "测试测试",
//				 "status": 1

		private Integer id;
		private Integer status;
		private String remarks;
		private String tel;
		private String stkName;
		private String stkNo;
		private String fbtime;
		private String stkManager;
		private String address;
		private String isSelect;//1.默认仓库
		private String isFixed;//1.锁定仓库

		public String getIsSelect() {
			return isSelect;
		}

		public void setIsSelect(String isSelect) {
			this.isSelect = isSelect;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public String getTel() {
			return tel;
		}

		public void setTel(String tel) {
			this.tel = tel;
		}

		public String getStkName() {
			return stkName;
		}

		public void setStkName(String stkName) {
			this.stkName = stkName;
		}

		public String getStkNo() {
			return stkNo;
		}

		public void setStkNo(String stkNo) {
			this.stkNo = stkNo;
		}

		public String getFbtime() {
			return fbtime;
		}

		public void setFbtime(String fbtime) {
			this.fbtime = fbtime;
		}

		public String getStkManager() {
			return stkManager;
		}

		public void setStkManager(String stkManager) {
			this.stkManager = stkManager;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getIsFixed() {
			return isFixed;
		}

		public void setIsFixed(String isFixed) {
			this.isFixed = isFixed;
		}
	}
}
