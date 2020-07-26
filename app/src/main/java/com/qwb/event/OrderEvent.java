package com.qwb.event;

import com.qwb.utils.ConstantUtils;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 创建描述：订货下单-（添加和修改成功后，刷新界面）
 * 创建作者：yeshenggen
 * 创建时间：2018/05/14
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class OrderEvent implements IBus.IEvent{

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_ORDER;
    }
}
