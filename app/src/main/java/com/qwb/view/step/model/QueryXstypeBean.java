package com.qwb.view.step.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 销售小结-----销售类型
 */
public class QueryXstypeBean extends BaseBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7162230399657644133L;
	private List<QueryXstype> xstypels;
	

	public List<QueryXstype> getXstypels() {
		return xstypels;
	}


	public void setXstypels(List<QueryXstype> xstypels) {
		this.xstypels = xstypels;
	}


	public static class QueryXstype implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -9059670276048616052L;
		private Integer id;
		private String xstpNm;  //名称
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getXstpNm() {
			return xstpNm;
		}
		public void setXstpNm(String xstpNm) {
			this.xstpNm = xstpNm;
		}
		
		
	}
}
