package com.qwb.view.audit.model;

import com.qwb.view.base.model.BaseBean;
import java.util.ArrayList;
import java.util.List;

/**
 * 审批模板
 */
public class AuditModelListResult extends BaseBean {
	private List<AuditModelBean> list = new ArrayList<>();

	public List<AuditModelBean> getList() {
		return list;
	}

	public void setList(List<AuditModelBean> list) {
		this.list = list;
	}
}

