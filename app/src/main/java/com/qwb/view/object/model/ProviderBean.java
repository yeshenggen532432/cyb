package com.qwb.view.object.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 选择供应商--供货商
 *
 */
public class ProviderBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4522368844143444663L;
	private List<Customerls> customerls;
	
	
	public List<Customerls> getCustomerls() {
		return customerls;
	}


	public void setCustomerls(List<Customerls> customerls) {
		this.customerls = customerls;
	}


	public static class Customerls implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3500538152815627987L;
		private Integer id;  //名称
		private String khNm;  //名称
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getKhNm() {
			return khNm;
		}
		public void setKhNm(String khNm) {
			this.khNm = khNm;
		}
		
		
		
	}
}
