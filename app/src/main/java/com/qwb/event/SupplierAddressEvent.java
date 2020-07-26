package com.qwb.event;

import com.qwb.utils.ConstantUtils;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 创建描述：供货商-会员地址
 * 创建作者：yeshenggen
 * 创建时间：2018-09-18
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class SupplierAddressEvent implements IBus.IEvent{

    private String address;
    private String longitude;
    private String latitude;
    private String mobile;
    private String linkman;

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_SUPPLIER_ADDRESS;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }
}
