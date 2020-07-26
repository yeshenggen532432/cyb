package com.qwb.event;

import com.qwb.view.customer.model.CustomerBean;
import com.qwb.utils.ConstantUtils;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 *  我的客户-（添加和修改成功后，刷新界面）
 */
public class MineClientEvent implements IBus.IEvent{

    private CustomerBean customerBean;

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_MINE_CLIENT;
    }


    public CustomerBean getCustomerBean() {
        return customerBean;
    }

    public void setCustomerBean(CustomerBean customerBean) {
        this.customerBean = customerBean;
    }
}
