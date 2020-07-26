package com.qwb.view.ware.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 创建作者：yeshenggen 
 * 创建时间：2017-12-09 
 * 功能描述：获取商品类型
 * 修改作者： 
 * 修改时间： 
 * 修改描述：
 */
public class QueryStkWareType extends BaseBean {
	private static final long serialVersionUID = 6406795736583062704L;
	private List<List1> list;
	
	public List<List1> getList() {
		return list;
	}

	public void setList(List<List1> list) {
		this.list = list;
	}

	public static class List1 {
		private int waretypeId;
		private String waretypeLeaf;
		private String waretypeNm;
		private String waretypePath;
		private int waretypePid;
		private int isType;
		private List<List2> list2;

		public int getWaretypeId() {
			return waretypeId;
		}

		public void setWaretypeId(int waretypeId) {
			this.waretypeId = waretypeId;
		}

		public String getWaretypeLeaf() {
			return waretypeLeaf;
		}

		public void setWaretypeLeaf(String waretypeLeaf) {
			this.waretypeLeaf = waretypeLeaf;
		}

		public String getWaretypeNm() {
			return waretypeNm;
		}

		public void setWaretypeNm(String waretypeNm) {
			this.waretypeNm = waretypeNm;
		}

		public String getWaretypePath() {
			return waretypePath;
		}

		public void setWaretypePath(String waretypePath) {
			this.waretypePath = waretypePath;
		}

		public int getWaretypePid() {
			return waretypePid;
		}

		public void setWaretypePid(int waretypePid) {
			this.waretypePid = waretypePid;
		}

		public int getIsType() {
			return isType;
		}

		public void setIsType(int isType) {
			this.isType = isType;
		}

		public List<List2> getList2() {
			return list2;
		}

		public void setList2(List<List2> list2) {
			this.list2 = list2;
		}
	}
	
	
	public static class List2 {
		private int waretypeId;
		private String waretypeLeaf;
		private String waretypeNm;
		private String waretypePath;
		private int waretypePid;

		public int getWaretypeId() {
			return waretypeId;
		}

		public void setWaretypeId(int waretypeId) {
			this.waretypeId = waretypeId;
		}

		public String getWaretypeLeaf() {
			return waretypeLeaf;
		}

		public void setWaretypeLeaf(String waretypeLeaf) {
			this.waretypeLeaf = waretypeLeaf;
		}

		public String getWaretypeNm() {
			return waretypeNm;
		}

		public void setWaretypeNm(String waretypeNm) {
			this.waretypeNm = waretypeNm;
		}

		public String getWaretypePath() {
			return waretypePath;
		}

		public void setWaretypePath(String waretypePath) {
			this.waretypePath = waretypePath;
		}

		public int getWaretypePid() {
			return waretypePid;
		}

		public void setWaretypePid(int waretypePid) {
			this.waretypePid = waretypePid;
		}
	}



}
