package com.qwb.view.tab.model;

import com.qwb.view.base.model.ApplyBean;

import java.io.Serializable;
import java.util.List;

/**
 *	应用列表
 */
public class ApplyBean2 implements Serializable {

    private String label;
    private List<ApplyBean> applys;

	public ApplyBean2() {
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<ApplyBean> getApplys() {
		return applys;
	}

	public void setApplys(List<ApplyBean> applys) {
		this.applys = applys;
	}
}
