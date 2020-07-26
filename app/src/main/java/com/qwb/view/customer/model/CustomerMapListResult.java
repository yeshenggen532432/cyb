package com.qwb.view.customer.model;

import com.qwb.view.base.model.BasePageBean;
import java.util.List;

/**
 * 客户分布图
 */
public class CustomerMapListResult extends BasePageBean {

	private List<CustomerBean> list;

	public List<CustomerBean> getList() {
		return list;
	}

	public void setList(List<CustomerBean> list) {
		this.list = list;
	}
}
