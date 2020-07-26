package com.qwb.view.checkstorage.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 盘点仓库
 */
public class StkCheckDetailBean extends BaseBean implements Serializable {

	private StkCheckBean check;
	private List<StkCheckWareBean> list;

	public StkCheckBean getCheck() {
		return check;
	}

	public void setCheck(StkCheckBean check) {
		this.check = check;
	}

	public List<StkCheckWareBean> getList() {
		return list;
	}

	public void setList(List<StkCheckWareBean> list) {
		this.list = list;
	}
}
