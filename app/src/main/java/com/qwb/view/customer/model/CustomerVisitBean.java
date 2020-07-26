package com.qwb.view.customer.model;

import com.qwb.view.common.model.PicBean;

import java.util.List;

/**
 * 客户拜访
 */

public class CustomerVisitBean {
    private Integer id;//拜访签到拍照id
    private Integer mid;//业务员id
    private Integer cid;//客户id
    private String longitude;//经度(签到)
    private String latitude;//纬度（签到)
    private String longitude2;//经度(签退)
    private String latitude2;//纬度（签退)
    private String longitude3;//经度(客户)
    private String latitude3;//纬度（客户)
    private String address;//地址
    private String qddate;//签到日期
    private String qdtime;//签到时间
    private String khNm;//客户名称
    private String memberNm;//业务员名称
    private String memberHead;//业务员头像
    private String branchName;//部门名称
    private String khdjNm;//客户等级

    private Integer jlm;//距离（m）
    private String bcbfzj;//本次拜访总结
    private String voiceUrl;//语音地址
    private Integer voiceTime;//语音时长
    private List<PicBean> listpic;//图片集合
    private List<CommentBean> commentList;//评价/回复集合
    private Integer zfcount;//客户重复条数

    //-------------------------
    private Integer type;//1:拜访查询 2：打卡查询
    private String time;//时间：拜访查询（qddate + qdtime）; 打卡查询（signTime）

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
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

    public String getLongitude2() {
        return longitude2;
    }

    public void setLongitude2(String longitude2) {
        this.longitude2 = longitude2;
    }

    public String getLatitude2() {
        return latitude2;
    }

    public void setLatitude2(String latitude2) {
        this.latitude2 = latitude2;
    }

    public String getLongitude3() {
        return longitude3;
    }

    public void setLongitude3(String longitude3) {
        this.longitude3 = longitude3;
    }

    public String getLatitude3() {
        return latitude3;
    }

    public void setLatitude3(String latitude3) {
        this.latitude3 = latitude3;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQddate() {
        return qddate;
    }

    public void setQddate(String qddate) {
        this.qddate = qddate;
    }

    public String getQdtime() {
        return qdtime;
    }

    public void setQdtime(String qdtime) {
        this.qdtime = qdtime;
    }

    public String getKhNm() {
        return khNm;
    }

    public void setKhNm(String khNm) {
        this.khNm = khNm;
    }

    public String getMemberNm() {
        return memberNm;
    }

    public void setMemberNm(String memberNm) {
        this.memberNm = memberNm;
    }

    public String getMemberHead() {
        return memberHead;
    }

    public void setMemberHead(String memberHead) {
        this.memberHead = memberHead;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getKhdjNm() {
        return khdjNm;
    }

    public void setKhdjNm(String khdjNm) {
        this.khdjNm = khdjNm;
    }

    public Integer getJlm() {
        return jlm;
    }

    public void setJlm(Integer jlm) {
        this.jlm = jlm;
    }

    public String getBcbfzj() {
        return bcbfzj;
    }

    public void setBcbfzj(String bcbfzj) {
        this.bcbfzj = bcbfzj;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public Integer getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(Integer voiceTime) {
        this.voiceTime = voiceTime;
    }

    public List<PicBean> getListpic() {
        return listpic;
    }

    public void setListpic(List<PicBean> listpic) {
        this.listpic = listpic;
    }

    public List<CommentBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentBean> commentList) {
        this.commentList = commentList;
    }

    public Integer getZfcount() {
        return zfcount;
    }

    public void setZfcount(Integer zfcount) {
        this.zfcount = zfcount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
