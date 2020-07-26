package com.qwb.view.tab.model;


import com.qwb.view.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的供货商列表
 */
public class SupplierListBean extends BaseBean{
//	{"state":true,"list":[{"fdId":3,"companyId":285,"outTime":"","memberId":1456,"memberNm":"测试会员","memberMobile":"13950104773","memberCompany":"厦门企微宝网络科技有限公司","inTime":"2018-44-16"}]}
    private List<SupplierBean> list=new ArrayList<>();

	public List<SupplierBean> getList() {
		return list;
	}

	public void setList(List<SupplierBean> list) {
		this.list = list;
	}
}
