package com.qwb.view.foot.model;

import com.qwb.view.base.model.BasePageBean;

import java.util.ArrayList;
import java.util.List;

public class FootPageResult extends BasePageBean {
    private List<FootBean> rows = new ArrayList<>();

    public List<FootBean> getRows() {
        return rows;
    }

    public void setRows(List<FootBean> rows) {
        this.rows = rows;
    }
}
