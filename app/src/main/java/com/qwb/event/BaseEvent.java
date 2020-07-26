package com.qwb.event;

import com.qwb.utils.ConstantUtils;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 创建描述：基本Event,接受到，更新列表
 * 创建作者：yeshenggen
 * 创建时间：2018/09/04
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class BaseEvent implements IBus.IEvent{

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_BASE;
    }


}
