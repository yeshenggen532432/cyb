package com.qwb.event;

import com.qwb.utils.ConstantUtils;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 创建描述：分类消息
 * 创建作者：yeshenggen
 * 创建时间：
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class CategroyMessageEvent implements IBus.IEvent{

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_BASE;
    }


}
