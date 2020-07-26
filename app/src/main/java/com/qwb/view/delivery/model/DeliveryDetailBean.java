package com.qwb.view.delivery.model;

import com.qwb.view.base.model.BaseBean;

/**
 * 配送单详情
 */
public class DeliveryDetailBean extends BaseBean {

	private DeliveryBean delivery;

	public DeliveryBean getDelivery() {
		return delivery;
	}

	public void setDelivery(DeliveryBean delivery) {
		this.delivery = delivery;
	}
}
