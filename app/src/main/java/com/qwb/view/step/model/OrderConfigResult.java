package com.qwb.view.step.model;

import com.qwb.view.base.model.BaseBean;

/**
 * 下单配置
 */
public class OrderConfigResult extends BaseBean{
    private OrderConfigBean data;

    public OrderConfigBean getData() {
        return data;
    }

    public void setData(OrderConfigBean data) {
        this.data = data;
    }
}
