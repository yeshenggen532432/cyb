package com.qwb.view.car.model;


/**
 *  车销收款
 */
public class CarRecMastBean {

	private Integer id;
	private String billNo;//单据号
	private double sumAmt;//收款金额
	private String remarks;

	private String recTime;
	private Integer orderId;//订单id
	private String orderNo;//订单单号
	private Integer status;//-2:暂存 1:已审批 2:作废 3:已入账
	private Integer empId;//业务员ID
	private String staff;//业务员名称
	private Integer billType;

	private String orderTime;//订单日期
	private String proName;//往来单位名称
	private Integer proType;//往来单位类别
	private Integer proId;//往来单位ID
	private Integer createId;//创建人ID
	private Integer stkId;//车销仓库ID
	private String stkNm;//车销仓库名称

	//----------------------------
	private String recTimeStr;
	private String sdate;
	private String edate;
	private String billTypeStr;
	private String billStatus;
	private String operator;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public double getSumAmt() {
		return sumAmt;
	}

	public void setSumAmt(double sumAmt) {
		this.sumAmt = sumAmt;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getStaff() {
		return staff;
	}

	public void setStaff(String staff) {
		this.staff = staff;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public Integer getProType() {
		return proType;
	}

	public void setProType(Integer proType) {
		this.proType = proType;
	}

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
		this.createId = createId;
	}

	public Integer getStkId() {
		return stkId;
	}

	public void setStkId(Integer stkId) {
		this.stkId = stkId;
	}

	public String getStkNm() {
		return stkNm;
	}

	public void setStkNm(String stkNm) {
		this.stkNm = stkNm;
	}

	public String getRecTimeStr() {
		return recTimeStr;
	}

	public void setRecTimeStr(String recTimeStr) {
		this.recTimeStr = recTimeStr;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public String getEdate() {
		return edate;
	}

	public void setEdate(String edate) {
		this.edate = edate;
	}

	public String getBillTypeStr() {
		return billTypeStr;
	}

	public void setBillTypeStr(String billTypeStr) {
		this.billTypeStr = billTypeStr;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getRecTime() {
		return recTime;
	}

	public void setRecTime(String recTime) {
		this.recTime = recTime;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
}
