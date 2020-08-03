package com.qwb.view.tab.model;


import com.qwb.view.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的供货商列表
 */
public class SupplierListBean extends BaseBean{
    private List<SupplierBean> list=new ArrayList<>();

	public List<SupplierBean> getList() {
		return list;
	}

	public void setList(List<SupplierBean> list) {
		this.list = list;
	}
}
