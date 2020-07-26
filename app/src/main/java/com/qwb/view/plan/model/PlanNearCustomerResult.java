package com.qwb.view.plan.model;

import com.qwb.view.base.model.BaseBean;
import com.qwb.view.customer.model.MineClientInfo;
import java.util.List;

/**
 * 线路地图-周边客户
 */
public class PlanNearCustomerResult extends BaseBean{
	private List<MineClientInfo> list;


	public List<MineClientInfo> getList() {
		return list;
	}

	public void setList(List<MineClientInfo> list) {
		this.list = list;
	}
}
