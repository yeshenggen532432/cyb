package com.qwb.view.audit.model;

import com.qwb.view.member.model.BuMenListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 审批流
 */
public class AuditZdyBean{
	public int id;//自定义id
	public int memberId;//用户id
	public String tp;//1.类型 2.时间 3.详情 4.备注 5.金额 6.对象 7.账户 8.关联单
	public String zdyNm;//自定义名称
	public String isSy;//1:私用 2：公用
	public String memIds;//固定审批人的id
	public String sendIds;//发起人
	public String execId;//执行人
	public List<BuMenListBean.MemberBean> mlist = new ArrayList<>();
	public List<BuMenListBean.MemberBean> sendList = new ArrayList<>();
	public List<BuMenListBean.MemberBean> execList = new ArrayList<>();
	public int approverId;//最终审批人id
	public Integer isNormal;//是否预设默认的：0.否，1.是
	public BuMenListBean.MemberBean approver;//最终审批人
	public Integer modelId;//审批模板id
	public Integer accountId;//付款账户id
	public String accountName;//付款账户名称
	public Integer isUpdateAudit;//是否可以修改审批人： 0不可以，1或空可以
	public Integer isUpdateApprover;//是否可以修改最终审批人： 0不可以，1或空可以

	public AuditZdyBean() {
		super();
	}

	public Integer getIsNormal() {
		return isNormal;
	}

	public void setIsNormal(Integer isNormal) {
		this.isNormal = isNormal;
	}

	public Integer getIsUpdateAudit() {
		return isUpdateAudit;
	}

	public void setIsUpdateAudit(Integer isUpdateAudit) {
		this.isUpdateAudit = isUpdateAudit;
	}

	public Integer getIsUpdateApprover() {
		return isUpdateApprover;
	}

	public void setIsUpdateApprover(Integer isUpdateApprover) {
		this.isUpdateApprover = isUpdateApprover;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getIsSy() {
		return isSy;
	}

	public void setIsSy(String isSy) {
		this.isSy = isSy;
	}

	public String getMemIds() {
		return memIds;
	}

	public void setMemIds(String memIds) {
		this.memIds = memIds;
	}


	public List<BuMenListBean.MemberBean> getMlist() {
		return mlist;
	}

	public void setMlist(List<BuMenListBean.MemberBean> mlist) {
		this.mlist = mlist;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getTp() {
		return tp;
	}

	public void setTp(String tp) {
		this.tp = tp;
	}

	public String getZdyNm() {
		return zdyNm;
	}

	public void setZdyNm(String zdyNm) {
		this.zdyNm = zdyNm;
	}

	public String getSendIds() {
		return sendIds;
	}

	public void setSendIds(String sendIds) {
		this.sendIds = sendIds;
	}

	public String getExecId() {
		return execId;
	}

	public void setExecId(String execId) {
		this.execId = execId;
	}

	public List<BuMenListBean.MemberBean> getSendList() {
		return sendList;
	}

	public void setSendList(List<BuMenListBean.MemberBean> sendList) {
		this.sendList = sendList;
	}

	public List<BuMenListBean.MemberBean> getExecList() {
		return execList;
	}

	public void setExecList(List<BuMenListBean.MemberBean> execList) {
		this.execList = execList;
	}

	public int getApproverId() {
		return approverId;
	}

	public void setApproverId(int approverId) {
		this.approverId = approverId;
	}

	public BuMenListBean.MemberBean getApprover() {
		return approver;
	}

	public void setApprover(BuMenListBean.MemberBean approver) {
		this.approver = approver;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}
}
