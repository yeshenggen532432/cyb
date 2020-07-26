package com.qwb.view.audit.model;

import com.qwb.view.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 审批模块：自定义模板
 */

public class ShenpiModelBean extends BaseBean {
	private static final long serialVersionUID = 2553343971687251971L;
	private List<ShenpiModel> list = new ArrayList<>();

	public List<ShenpiModel> getList() {
		return list;
	}

	public void setList(List<ShenpiModel> list) {
		this.list = list;
	}
}

