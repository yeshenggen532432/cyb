package com.qwb.view.base.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色--应用列表
 */
public class RoleBean extends BaseBean {
	private List<ApplyBean> applyList = new ArrayList<>();

	public List<ApplyBean> getApplyList() {
		return applyList;
	}
	public void setApplyList(List<ApplyBean> applyList) {
		this.applyList = applyList;
	}
	
	
}
