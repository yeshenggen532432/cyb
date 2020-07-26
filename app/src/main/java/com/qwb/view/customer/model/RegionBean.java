package com.qwb.view.customer.model;

import com.qwb.view.base.model.BaseBean;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户所属区域
 */

public class RegionBean extends BaseBean {
	private List<Region> list = new ArrayList<>();

	public List<Region> getList() {
		return list;
	}

	public void setList(List<Region> list) {
		this.list = list;
	}

	public static class Region{
		private Integer regionId;//分类id
		private String regionNm;//分类名称
		private Integer regionPid;//所属分类
		private String regionPath;//分类路径
		private String regionLeaf;//分类末级
		private String upRegionNm;
		private List<Region> list2;

		public Integer getRegionId() {
			return regionId;
		}

		public void setRegionId(Integer regionId) {
			this.regionId = regionId;
		}

		public String getRegionNm() {
			return regionNm;
		}

		public void setRegionNm(String regionNm) {
			this.regionNm = regionNm;
		}

		public Integer getRegionPid() {
			return regionPid;
		}

		public void setRegionPid(Integer regionPid) {
			this.regionPid = regionPid;
		}

		public String getRegionPath() {
			return regionPath;
		}

		public void setRegionPath(String regionPath) {
			this.regionPath = regionPath;
		}

		public String getRegionLeaf() {
			return regionLeaf;
		}

		public void setRegionLeaf(String regionLeaf) {
			this.regionLeaf = regionLeaf;
		}

		public List<Region> getList2() {
			return list2;
		}

		public void setList2(List<Region> list2) {
			this.list2 = list2;
		}
	}
}
