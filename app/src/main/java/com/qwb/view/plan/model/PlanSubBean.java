package com.qwb.view.plan.model;


/**
 */

public class PlanSubBean {

// "address": "中国福建省厦门市思明区厦禾路1017号",
//         "isWc": 2,
//         "tel": "null",
//         "scbfDate": "2019-07-04",
//         "pid": 3,
//         "id": 6,
//         "khNm": "厦门元塘贸易有限公司",
//         "linkman": "郑士田",
//         "cid": 537

    private String address;//地址
    private int isWc;//1:完成；2：未完成
    private String tel;//电话
    private String scbfDate;//上次拜访日期
    private int pid;//计划拜访日期
    private int id;//
    private String khNm;//客户名称
    private String linkman;//联系人
    private int cid;//客户id

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsWc() {
        return isWc;
    }

    public void setIsWc(int isWc) {
        this.isWc = isWc;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getScbfDate() {
        return scbfDate;
    }

    public void setScbfDate(String scbfDate) {
        this.scbfDate = scbfDate;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKhNm() {
        return khNm;
    }

    public void setKhNm(String khNm) {
        this.khNm = khNm;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}
