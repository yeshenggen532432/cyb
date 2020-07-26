package com.qwb.view.stk;

import com.qwb.view.base.model.BaseBean;
import java.util.List;

/**
 *  仓库商品列表
 */
public class StkWarePageBean extends BaseBean {
	private List<StkWareBean> list;

	public List<StkWareBean> getList() {
		return list;
	}

	public void setList(List<StkWareBean> list) {
		this.list = list;
	}
}
