package com.qwb.view.tab.model;


import com.qwb.view.base.model.BaseBean;

/**
 *  热门商家
 */
public class HotShopBean extends BaseBean {
	private int id;
	private String name;
	private String fdId;
	private String logoUrl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
}
