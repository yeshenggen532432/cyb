package com.qwb.event;

import com.qwb.utils.ConstantUtils;

import cn.droidlover.xdroidmvp.event.IBus;

/**
 * 创建描述：车销下单：选择客户
 * 创建作者：yeshenggen
 * 创建时间：2018-09-20
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class CarOrderChooseClientEvent implements IBus.IEvent{

    private String clientNm;//客户名称
    private String cid;//客户id
    private String address;//地址
    private String linkman;//联系人
    private String phone;//电话
    private String tel;//电话

    @Override
    public int getTag() {
        return ConstantUtils.Event.TAG_CAR_CHOOSE_CLIENT;
    }

    public String getClientNm() {
        return clientNm;
    }

    public void setClientNm(String clientNm) {
        this.clientNm = clientNm;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
