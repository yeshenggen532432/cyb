package com.qwb.view.foot.model;

import com.qwb.view.common.model.PicBean;
import com.qwb.view.base.model.BasePageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的足迹
 */
public class FootBean extends BasePageBean {
    private Long id;
    private int memberId;
    private String address;
    private String memberName;
    private String memberHead;
    private String createTime;
    private String latitude;
    private String longitude;
    private String remarks;
    private String voicePath;
    private String voiceTime;
    private List<PicBean> picList = new ArrayList();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberHead() {
        return memberHead;
    }

    public void setMemberHead(String memberHead) {
        this.memberHead = memberHead;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public String getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(String voiceTime) {
        this.voiceTime = voiceTime;
    }

    public List<PicBean> getPicList() {
        return picList;
    }

    public void setPicList(List<PicBean> picList) {
        this.picList = picList;
    }
}
