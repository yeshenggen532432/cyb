package com.qwb.event;

import com.qwb.utils.ConstantUtils;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 创建描述：消息：收到信息，查看消息；删除信息；要更新红点
 * 创建作者：yeshenggen
 * 创建时间：
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class MessageEvent implements IBus.IEvent{

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_BASE;
    }


}
