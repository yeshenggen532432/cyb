package com.qwb.event;

import com.qwb.utils.ConstantUtils;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 创建描述：配送单
 * 创建作者：yeshenggen
 * 创建时间：2018/05/17
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class DeliveryEvent implements IBus.IEvent{
    //0:待分配1:待接收2:已接收3:配送中4:已送达5:配送终止
    private String psState;

    public String getPsState() {
        return psState;
    }

    public void setPsState(String psState) {
        this.psState = psState;
    }

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_DELIVERY;
    }
}
