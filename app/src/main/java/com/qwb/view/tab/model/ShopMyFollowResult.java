package com.qwb.view.tab.model;


import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 *  我的关注
 */
public class ShopMyFollowResult extends BaseBean {
	private List<HotShopBean> obj;

	public List<HotShopBean> getObj() {
		return obj;
	}

	public void setObj(List<HotShopBean> obj) {
		this.obj = obj;
	}
}
