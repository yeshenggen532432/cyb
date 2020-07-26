package com.qwb.view.car.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;


/**
 *  车销配货单
 */
public class StkMoveBean extends BaseBean {

	private int id;
	private int proType;
	private String bizType;
	private int billType;
	private int count;
	private String inDate;
	private int mid;
	private String operator;
	private int stkId;
	private String inTime;
	private String stkName;
	private String memberNm;
	private double totalAmt;
	private double disAmt;
	private String submitUser;
	private String newTime;
	private int proId;
	private String cancelUser;
	private String proName;
	private String billNo;
	private String remarks;
	private int stkInId;
	private String stkInName;
	private int status;//-2:暂存 1.审批  2.作废
	private List<StkMoveSubBean> list;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProType() {
		return proType;
	}

	public void setProType(int proType) {
		this.proType = proType;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public int getBillType() {
		return billType;
	}

	public void setBillType(int billType) {
		this.billType = billType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getStkId() {
		return stkId;
	}

	public void setStkId(int stkId) {
		this.stkId = stkId;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getStkName() {
		return stkName;
	}

	public void setStkName(String stkName) {
		this.stkName = stkName;
	}

	public String getMemberNm() {
		return memberNm;
	}

	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
	}

	public double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(double totalAmt) {
		this.totalAmt = totalAmt;
	}

	public double getDisAmt() {
		return disAmt;
	}

	public void setDisAmt(double disAmt) {
		this.disAmt = disAmt;
	}

	public String getSubmitUser() {
		return submitUser;
	}

	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}

	public String getNewTime() {
		return newTime;
	}

	public void setNewTime(String newTime) {
		this.newTime = newTime;
	}

	public int getProId() {
		return proId;
	}

	public void setProId(int proId) {
		this.proId = proId;
	}

	public String getCancelUser() {
		return cancelUser;
	}

	public void setCancelUser(String cancelUser) {
		this.cancelUser = cancelUser;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStkInName() {
		return stkInName;
	}

	public void setStkInName(String stkInName) {
		this.stkInName = stkInName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStkInId() {
		return stkInId;
	}

	public void setStkInId(int stkInId) {
		this.stkInId = stkInId;
	}

	public List<StkMoveSubBean> getList() {
		return list;
	}

	public void setList(List<StkMoveSubBean> list) {
		this.list = list;
	}
}
