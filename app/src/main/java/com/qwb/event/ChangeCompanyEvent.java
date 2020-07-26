package com.qwb.event;

import com.qwb.utils.ConstantUtils;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 创建描述：创建公司
 * 创建作者：yeshenggen
 * 创建时间：2018/08/14
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class ChangeCompanyEvent implements IBus.IEvent{

    private String companyName;

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_CHANGE_COMPANY;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
