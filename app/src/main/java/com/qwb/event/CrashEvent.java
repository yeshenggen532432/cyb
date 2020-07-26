package com.qwb.event;

import com.qwb.utils.ConstantUtils;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 闪退
 */
public class CrashEvent implements IBus.IEvent{

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_CRASH;
    }


}
