package com.qwb.event;

import com.qwb.utils.ConstantUtils;
import com.qwb.view.customer.model.MineClientInfo;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 计划线路
 */
public class PlanLineEvent implements IBus.IEvent{

    private String type;
    private MineClientInfo bean;

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_PLAN_LINE;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MineClientInfo getBean() {
        return bean;
    }

    public void setBean(MineClientInfo bean) {
        this.bean = bean;
    }
}
