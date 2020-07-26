package com.qwb.event;

import com.qwb.utils.ConstantUtils;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 创建描述：商品管理-（修改商品信息成功后，刷新界面）
 * 创建作者：yeshenggen
 * 创建时间：2018/08/10
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class GoodsManagerEvent implements IBus.IEvent{


    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_GOODS_MANAGER;
    }
}
