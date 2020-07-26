package com.qwb.event;

import com.qwb.utils.ConstantUtils;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 创建描述：缓存拜访步骤1：拍照签到
 * 创建时间：
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class CacheStep1Event implements IBus.IEvent{

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_CACHE_STEP_1;
    }


}
