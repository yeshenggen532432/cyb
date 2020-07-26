package com.qwb.view.customer.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 新增客户---合作方式
 * @author Administrator
 *
 */
public class queryHzfsBean extends BaseBean {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1125222930380690706L;
	private List<queryHzfs> hzfsls;


	public List<queryHzfs> getHzfsls() {
		return hzfsls;
	}


	public void setHzfsls(List<queryHzfs> hzfsls) {
		this.hzfsls = hzfsls;
	}


	public static class queryHzfs implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 986180545328503990L;
		private Integer id;//合作方式id
		private String hzfsNm;  //合作方式名称
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getHzfsNm() {
			return hzfsNm;
		}
		public void setHzfsNm(String hzfsNm) {
			this.hzfsNm = hzfsNm;
		}
		
	}
}
