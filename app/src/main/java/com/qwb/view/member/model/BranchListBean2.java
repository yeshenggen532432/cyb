package com.qwb.view.member.model;

import com.qwb.view.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取部门以及成员信息
 */

public class BranchListBean2 extends BaseBean {

	private static final long serialVersionUID = -4563390183634024671L;
	private List<BranchBean> list = new ArrayList<>();

	public List<BranchBean> getList() {
		return list;
	}

	public void setList(List<BranchBean> list) {
		this.list = list;
	}
}
