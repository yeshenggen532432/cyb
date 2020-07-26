package com.qwb.event;

import com.qwb.utils.ConstantUtils;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 创建描述：默认商城
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class NormalShopEvent implements IBus.IEvent{

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_NORMAL_SHOP;
    }
}
