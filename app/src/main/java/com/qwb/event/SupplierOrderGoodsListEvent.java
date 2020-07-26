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
public class SupplierOrderGoodsListEvent implements IBus.IEvent{
    private String shr;//收货人
    private String phone;//电话
    private String address;//地址
    private String remark;//备注

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_SUPPLIER_ORDER_GOODS_LIST;
    }

    public String getShr() {
        return shr;
    }

    public void setShr(String shr) {
        this.shr = shr;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
