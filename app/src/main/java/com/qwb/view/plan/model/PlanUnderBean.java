package com.qwb.view.plan.model;


import java.util.ArrayList;
import java.util.List;

/**
 */
public class PlanUnderBean {

//    branchId	:	4
//    memberNm	:	阙如新
//    pdate	:	2019-08-09
//    xlid	:	6
//    mid	:	1091
//    id	:	6
//    xlNm	:	test02

    private int branchId;//部门id
    private int mid;//业务员id
    private String memberNm;//业务员名称
    private String pdate;//日期
    private int id;
    private int xlid;//线路id
    private String xlNm;//线路名称

    private List<PlanSubBean> subList = new ArrayList<>();

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getMemberNm() {
        return memberNm;
    }

    public void setMemberNm(String memberNm) {
        this.memberNm = memberNm;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public int getXlid() {
        return xlid;
    }

    public void setXlid(int xlid) {
        this.xlid = xlid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getXlNm() {
        return xlNm;
    }

    public void setXlNm(String xlNm) {
        this.xlNm = xlNm;
    }

    public List<PlanSubBean> getSubList() {
        return subList;
    }

    public void setSubList(List<PlanSubBean> subList) {
        this.subList = subList;
    }
}
