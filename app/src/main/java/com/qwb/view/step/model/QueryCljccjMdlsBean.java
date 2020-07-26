package com.qwb.view.step.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 拜访流程3--陈列检查采集（获取模板）
 */
public class QueryCljccjMdlsBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2615730081767426900L;
	private List<CljccjMdls> list;// 图片集合

	public List<CljccjMdls> getList() {
		return list;
	}

	public void setList(List<CljccjMdls> list) {
		this.list = list;
	}

	public static class CljccjMdls implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3224999401981066747L;
		private Integer id;// 陈列检查采集模板id
		private Integer isBds;// 图片id
		private Integer isDjpms;// 图片id
		private Integer isHjpms;// 图片id
		private Integer isSytwl;// 图片id
		private String mdNm; // 大图

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Integer getIsBds() {
			return isBds;
		}

		public void setIsBds(Integer isBds) {
			this.isBds = isBds;
		}

		public Integer getIsDjpms() {
			return isDjpms;
		}

		public void setIsDjpms(Integer isDjpms) {
			this.isDjpms = isDjpms;
		}

		public Integer getIsHjpms() {
			return isHjpms;
		}

		public void setIsHjpms(Integer isHjpms) {
			this.isHjpms = isHjpms;
		}

		public Integer getIsSytwl() {
			return isSytwl;
		}

		public void setIsSytwl(Integer isSytwl) {
			this.isSytwl = isSytwl;
		}

		public String getMdNm() {
			return mdNm;
		}

		public void setMdNm(String mdNm) {
			this.mdNm = mdNm;
		}

	}
}
