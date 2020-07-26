package com.qwb.event;

import com.qwb.utils.ConstantUtils;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 创建描述：供货商-选中的商品列表改变：通知上个界面更新UI
 * 创建作者：yeshenggen
 * 创建时间：2018/08/10
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class SupplierCacheGoodsListEvent implements IBus.IEvent{

    private int count;//缓存的商品数量

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_SUPPLIER_CACHE_GOODS_LIST;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
