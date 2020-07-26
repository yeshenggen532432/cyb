package com.qwb.view.audit.model;

import com.qwb.view.base.model.BaseBean;
import java.util.ArrayList;
import java.util.List;

/**
 * 审批流
 */
public class AuditZdyListResult extends BaseBean {
	private List<AuditZdyBean> list = new ArrayList<>();

	public List<AuditZdyBean> getList() {
		return list;
	}

	public void setList(List<AuditZdyBean> list) {
		this.list = list;
	}
}

