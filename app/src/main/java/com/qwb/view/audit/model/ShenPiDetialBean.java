package com.qwb.view.audit.model;

import com.qwb.view.common.model.PicList;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.member.model.BuMenListBean;

import java.io.Serializable;
import java.util.List;


public class ShenPiDetialBean extends BaseBean {

    /**
     *
     */
    private static final long serialVersionUID = 2352313737231336555L;
    private String title;//标题
    private String memberNm;
    private String auditTp;
    private String checkNm;
    private String auditData;
    /**
     * 2 审批流程未走完
     * 1-1 完成 1-2 拒绝 1-3 撤销
     */
    private String isOver;
    private String memberHead;
    private String addTime;
    private String tp;
    private String memberId;
    private String dsc;
    private String amount;//报销金额
    private String auditNo;
    private String stime;
    private String etime;
    private List<LiuChengListBean> checkList;
    private List<PicList> picList;
    private String fileNms;
    private String zdyNm;//自定义模板名称

    private String execIds;//执行人id
    private List<BuMenListBean.MemberBean> execList;//执行人
    private String execOver;//是否执行完成
    private String objectName;//对象名称
    private String accName;//账户名称

    public String getExecOver() {
        return execOver;
    }

    public void setExecOver(String execOver) {
        this.execOver = execOver;
    }

    public String getZdyNm() {
        return zdyNm;
    }

    public void setZdyNm(String zdyNm) {
        this.zdyNm = zdyNm;
    }

    public String getMemberNm() {
        return memberNm;
    }

    public List<PicList> getPicList() {
        return picList;
    }

    public void setPicList(List<PicList> picList) {
        this.picList = picList;
    }

    public void setMemberNm(String memberNm) {
        this.memberNm = memberNm;
    }

    public String getAuditTp() {
        return auditTp;
    }


    public void setAuditTp(String auditTp) {
        this.auditTp = auditTp;
    }


    public String getCheckNm() {
        return checkNm;
    }


    public void setCheckNm(String checkNm) {
        this.checkNm = checkNm;
    }


    public String getAuditData() {
        return auditData;
    }


    public void setAuditData(String auditData) {
        this.auditData = auditData;
    }


    public String getIsOver() {
        return isOver;
    }


    public void setIsOver(String isOver) {
        this.isOver = isOver;
    }


    public String getMemberHead() {
        return memberHead;
    }


    public void setMemberHead(String memberHead) {
        this.memberHead = memberHead;
    }


    public String getAddTime() {
        return addTime;
    }


    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }


    public String getTp() {
        return tp;
    }


    public void setTp(String tp) {
        this.tp = tp;
    }


    public String getMemberId() {
        return memberId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }


    public String getDsc() {
        return dsc;
    }


    public void setDsc(String dsc) {
        this.dsc = dsc;
    }


    public String getAuditNo() {
        return auditNo;
    }


    public void setAuditNo(String auditNo) {
        this.auditNo = auditNo;
    }


    public String getStime() {
        return stime;
    }


    public void setStime(String stime) {
        this.stime = stime;
    }


    public String getEtime() {
        return etime;
    }


    public void setEtime(String etime) {
        this.etime = etime;
    }


    public List<LiuChengListBean> getCheckList() {
        return checkList;
    }


    public void setCheckList(List<LiuChengListBean> checkList) {
        this.checkList = checkList;
    }

    public String getExecIds() {
        return execIds;
    }

    public void setExecIds(String execIds) {
        this.execIds = execIds;
    }

    public List<BuMenListBean.MemberBean> getExecList() {
        return execList;
    }

    public void setExecList(List<BuMenListBean.MemberBean> execList) {
        this.execList = execList;
    }

    public String getFileNms() {
        return fileNms;
    }

    public void setFileNms(String fileNms) {
        this.fileNms = fileNms;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public class LiuChengListBean implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = -3893610572956509431L;
        private String checkNum;//审批序号
        private String memberNm;
        private String memberHead;
        private String memberId;
        private String checkTime;
        private String dsc;
        private String checkTp;
        private String isApprover;//1最后审批人
        private String isZr;//1转交
        /**
         * 2 审批过的审批人
         * 1 审批中审批人
         * 0 未审批审批人
         */
        private String isCheck;

        public String getCheckNum() {
            return checkNum;
        }

        public void setCheckNum(String checkNum) {
            this.checkNum = checkNum;
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

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getCheckTime() {
            return checkTime;
        }

        public void setCheckTime(String checkTime) {
            this.checkTime = checkTime;
        }

        public String getDsc() {
            return dsc;
        }

        public void setDsc(String dsc) {
            this.dsc = dsc;
        }

        public String getCheckTp() {
            return checkTp;
        }

        public void setCheckTp(String checkTp) {
            this.checkTp = checkTp;
        }

        public String getIsCheck() {
            return isCheck;
        }

        public void setIsCheck(String isCheck) {
            this.isCheck = isCheck;
        }

        public String getIsApprover() {
            return isApprover;
        }

        public void setIsApprover(String isApprover) {
            this.isApprover = isApprover;
        }

        public String getIsZr() {
            return isZr;
        }

        public void setIsZr(String isZr) {
            this.isZr = isZr;
        }


    }


}
