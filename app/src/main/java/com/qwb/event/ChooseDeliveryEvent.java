package com.qwb.event;

import com.qwb.utils.ConstantUtils;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 创建描述：选择配送单
 */
public class ChooseDeliveryEvent implements IBus.IEvent{

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_CHOOSE_DELIVERY;
    }


}
