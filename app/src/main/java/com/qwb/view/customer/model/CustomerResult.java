package com.qwb.view.customer.model;

import com.qwb.view.base.model.BaseBean;

/**
 * 客户信息
 */
public class CustomerResult extends BaseBean{

    private CustomerBean customer;

    public CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBean customer) {
        this.customer = customer;
    }
}
