package com.qwb.view.plan.model;

import com.qwb.view.base.model.BasePageBean;

import java.util.List;

/**
 * 计划拜访--下属
 */

public class PlanUnderListResult extends BasePageBean{

//    msg	:	获取下属计划拜访列表成功
//    total	:	5
//    pageNo	:	1
//    totalPage	:	1
//    pageSize	:	10
//    state	:	true

    private List<PlanUnderBean> rows;

    public List<PlanUnderBean> getRows() {
        return rows;
    }

    public void setRows(List<PlanUnderBean> rows) {
        this.rows = rows;
    }
}
