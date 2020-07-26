package com.qwb.event;

import com.qwb.utils.ConstantUtils;
import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 选择对象：1.供应商 2.员工 3.客户 4.其他往来单位
 */
public class ObjectEvent implements IBus.IEvent{

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_OBJECT;
    }

    private String name;
    private int id;
    private int type;//0.供应商 1.员工 2.客户 3.其他往来单位

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
