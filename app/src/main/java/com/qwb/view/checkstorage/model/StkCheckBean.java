package com.qwb.view.checkstorage.model;

import java.io.Serializable;

/**
 * 盘点仓库
 */
public class StkCheckBean implements Serializable {

	// "staff": "叶生根",
//		 "checkTimeStr": "2019-05-31 11:38:00",
//		 "operator": "叶生根",
//		 "stkId": 8,
//		 "stkName": "旧货仓",
//		 "totalAmt": 0,
//		 "checkTime": "Fri May 31 11:38:00 CST 2019",
//		 "submitUser": "",
//		 "newTime": "Fri May 31 11:40:28 CST 2019",
//		 "cancelUser": "",
//		 "id": 152,
//		 "billNo": "CHECK2019053100001",
//		 "remarks": "",
//		 "status": -2
	private String staff;
	private String checkTimeStr;
	private String operator;
	private int stkId;
	private String stkName;
	private Double totalAmt;
	private String checkTime;
	private String submitUser;
	private String newTime;
	private String cancelUser;
	private int id;
	private String billNo;
	private String remarks;
	private String status;//盘点单：-2暂存；0审批；2作废；临时盘点单：-2暂存；0已合并；1审批；2作废
	private Integer isPc;//1.批次盘点


	public Integer getIsPc() {
		return isPc;
	}

	public void setIsPc(Integer isPc) {
		this.isPc = isPc;
	}

	public String getStaff() {
		return staff;
	}

	public void setStaff(String staff) {
		this.staff = staff;
	}

	public String getCheckTimeStr() {
		return checkTimeStr;
	}

	public void setCheckTimeStr(String checkTimeStr) {
		this.checkTimeStr = checkTimeStr;
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

	public String getStkName() {
		return stkName;
	}

	public void setStkName(String stkName) {
		this.stkName = stkName;
	}

	public Double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
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

	public String getCancelUser() {
		return cancelUser;
	}

	public void setCancelUser(String cancelUser) {
		this.cancelUser = cancelUser;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
