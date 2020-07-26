package com.qwb.view.company.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 采购类型
 */
public class PurchaseTypeBean extends BaseBean {
	private List<TypeBean> xstypels;
	

	public List<TypeBean> getXstypels() {
		return xstypels;
	}


	public void setXstypels(List<TypeBean> xstypels) {
		this.xstypels = xstypels;
	}


	public static class TypeBean implements Serializable {
		private Integer inTypeCode;
		private String inTypeName;  //名称

		public Integer getInTypeCode() {
			return inTypeCode;
		}

		public void setInTypeCode(Integer inTypeCode) {
			this.inTypeCode = inTypeCode;
		}

		public String getInTypeName() {
			return inTypeName;
		}

		public void setInTypeName(String inTypeName) {
			this.inTypeName = inTypeName;
		}
	}
}
