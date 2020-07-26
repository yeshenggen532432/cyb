package com.qwb.view.company.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

public class CompanyInfoOldBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6067801390485633729L;
	private List<CompanyInforItemBean> companys ;
	
	public List<CompanyInforItemBean> getCompanys() {
		return companys;
	}

	public void setCompanys(List<CompanyInforItemBean> companys) {
		this.companys = companys;
	}

	public class CompanyInforItemBean implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3674275481528479267L;
		private int deptId ;
		private String deptNm ;
		
		public String getDeptNm() {
			return deptNm;
		}
		public void setDeptNm(String deptNm) {
			this.deptNm = deptNm;
		}
		public int getDeptId() {
			return deptId;
		}
		public void setDeptId(int deptId) {
			this.deptId = deptId;
		}
		
	}
}
