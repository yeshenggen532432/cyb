package com.qwb.view.tab.model;


import com.qwb.view.base.model.BaseBean;
import java.util.List;

/**
 *  热门商家
 */
public class HotShopListBean extends BaseBean {
	private int code;
	private int totalCount;
	private int page;
	private int size;
	private int totalPages;
	private boolean last;
	private boolean first;
	private Obj obj;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public Obj getObj() {
		return obj;
	}

	public void setObj(Obj obj) {
		this.obj = obj;
	}

	public class Obj{
		private List<HotShopBean> rows;

		public List<HotShopBean> getRows() {
			return rows;
		}

		public void setRows(List<HotShopBean> rows) {
			this.rows = rows;
		}
	}
}
