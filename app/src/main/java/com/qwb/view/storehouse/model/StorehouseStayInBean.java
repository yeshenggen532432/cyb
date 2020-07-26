package com.qwb.view.storehouse.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 待入仓发票
 */
public class StorehouseStayInBean {

	private Integer id;
	private String billNo;
	private Integer status;//状态 -2暂存  0未审 1已审 2作废  3终止未发完
	private Integer proId;
	private Integer mid;
	private Date inTime;
	private Integer stkId;
	private String inType;
	private String remarks;
	private String submitUser;
	private Date submitTime;
	private String cancelUser;
	private Date cancelTime;
	private BigDecimal totalAmt;
	private BigDecimal payAmt;
	private BigDecimal discount;
	private BigDecimal disAmt;
	private String operator;

	private String  createName;

	private BigDecimal freeAmt;
	private String proName;//入库对角
	private Integer proType;//0供应商  1员工  2客户 3其他往外  4：会员
	private BigDecimal disAmt1;
	private Date newTime;
	private Integer orderId;
	private String orderNo;
	private Integer empId;
	private String empNm;
	private Integer driverId;
	private Integer vehId;
	private String driverName;
	private String vehNo;
	private Date sureTime;//确定时间 pc端修改销售退货后给该时间赋值

	private String reauditDesc;//说明，主要记录反审核操作

	private Integer openZfjz;//开启杂费结转 0:未开启 1:已开启

	//--------------------------------------
	private Integer waretype;//查询条件商品类别
	private Integer isType;
	private String keyWord;//查询条件 单号、供应商名称、品名、备注
	private String inTimeStr;
	private String submitTimeStr;
	private String cancelTimeStr;
	private String newTimeStr;

	private String waretypeIds;
	private Integer houseId;//总仓ID
	private String houseName;//库位名称

	private String inTypes;

	private Integer showShop;

	private String memberNm;//业务员名称
	private String database;//数据库
	private Integer count;//笔数
	private List<StorehouseStayInSubBean> list;//详情集合
	private String sdate;//开始时间
	private String edate;//结束时间
	private BigDecimal ddNum;//数量
	private Integer isMe;//是否我的（1是；2否）
	private String inDate;
	private String stkName;
	private String billStatus;
	private String payStatus;
	private String wareNm;
	private Boolean chk;
	private BigDecimal needPay;
	private Integer isPay;
	private Integer wareId;
	private String timeType;//时间类型 发票时间、发货时间
	private String subIds;
	private Integer checkAutoPrice;//检查采购单是否自动更新商品信息价格

	private BigDecimal beginAmt;//金额查询起始金额
	private BigDecimal endAmt;//金额查询终点金额

	private BigDecimal sumQty;
	private BigDecimal sumInQty;

	private String proIds;//客户IDs
	private String proNames;//客户名称

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public Integer getMid() {
		return mid;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	public Date getInTime() {
		return inTime;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public Integer getStkId() {
		return stkId;
	}

	public void setStkId(Integer stkId) {
		this.stkId = stkId;
	}

	public String getInType() {
		return inType;
	}

	public void setInType(String inType) {
		this.inType = inType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSubmitUser() {
		return submitUser;
	}

	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public String getCancelUser() {
		return cancelUser;
	}

	public void setCancelUser(String cancelUser) {
		this.cancelUser = cancelUser;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public BigDecimal getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}

	public BigDecimal getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(BigDecimal payAmt) {
		this.payAmt = payAmt;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getDisAmt() {
		return disAmt;
	}

	public void setDisAmt(BigDecimal disAmt) {
		this.disAmt = disAmt;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public BigDecimal getFreeAmt() {
		return freeAmt;
	}

	public void setFreeAmt(BigDecimal freeAmt) {
		this.freeAmt = freeAmt;
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

	public BigDecimal getDisAmt1() {
		return disAmt1;
	}

	public void setDisAmt1(BigDecimal disAmt1) {
		this.disAmt1 = disAmt1;
	}

	public Date getNewTime() {
		return newTime;
	}

	public void setNewTime(Date newTime) {
		this.newTime = newTime;
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

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getEmpNm() {
		return empNm;
	}

	public void setEmpNm(String empNm) {
		this.empNm = empNm;
	}

	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

	public Integer getVehId() {
		return vehId;
	}

	public void setVehId(Integer vehId) {
		this.vehId = vehId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getVehNo() {
		return vehNo;
	}

	public void setVehNo(String vehNo) {
		this.vehNo = vehNo;
	}

	public Date getSureTime() {
		return sureTime;
	}

	public void setSureTime(Date sureTime) {
		this.sureTime = sureTime;
	}

	public String getReauditDesc() {
		return reauditDesc;
	}

	public void setReauditDesc(String reauditDesc) {
		this.reauditDesc = reauditDesc;
	}

	public Integer getOpenZfjz() {
		return openZfjz;
	}

	public void setOpenZfjz(Integer openZfjz) {
		this.openZfjz = openZfjz;
	}

	public Integer getWaretype() {
		return waretype;
	}

	public void setWaretype(Integer waretype) {
		this.waretype = waretype;
	}

	public Integer getIsType() {
		return isType;
	}

	public void setIsType(Integer isType) {
		this.isType = isType;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getInTimeStr() {
		return inTimeStr;
	}

	public void setInTimeStr(String inTimeStr) {
		this.inTimeStr = inTimeStr;
	}

	public String getSubmitTimeStr() {
		return submitTimeStr;
	}

	public void setSubmitTimeStr(String submitTimeStr) {
		this.submitTimeStr = submitTimeStr;
	}

	public String getCancelTimeStr() {
		return cancelTimeStr;
	}

	public void setCancelTimeStr(String cancelTimeStr) {
		this.cancelTimeStr = cancelTimeStr;
	}

	public String getNewTimeStr() {
		return newTimeStr;
	}

	public void setNewTimeStr(String newTimeStr) {
		this.newTimeStr = newTimeStr;
	}

	public String getWaretypeIds() {
		return waretypeIds;
	}

	public void setWaretypeIds(String waretypeIds) {
		this.waretypeIds = waretypeIds;
	}

	public Integer getHouseId() {
		return houseId;
	}

	public void setHouseId(Integer houseId) {
		this.houseId = houseId;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public String getInTypes() {
		return inTypes;
	}

	public void setInTypes(String inTypes) {
		this.inTypes = inTypes;
	}

	public Integer getShowShop() {
		return showShop;
	}

	public void setShowShop(Integer showShop) {
		this.showShop = showShop;
	}

	public String getMemberNm() {
		return memberNm;
	}

	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
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

	public BigDecimal getDdNum() {
		return ddNum;
	}

	public void setDdNum(BigDecimal ddNum) {
		this.ddNum = ddNum;
	}

	public Integer getIsMe() {
		return isMe;
	}

	public void setIsMe(Integer isMe) {
		this.isMe = isMe;
	}

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public String getStkName() {
		return stkName;
	}

	public void setStkName(String stkName) {
		this.stkName = stkName;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getWareNm() {
		return wareNm;
	}

	public void setWareNm(String wareNm) {
		this.wareNm = wareNm;
	}

	public Boolean getChk() {
		return chk;
	}

	public void setChk(Boolean chk) {
		this.chk = chk;
	}

	public BigDecimal getNeedPay() {
		return needPay;
	}

	public void setNeedPay(BigDecimal needPay) {
		this.needPay = needPay;
	}

	public Integer getIsPay() {
		return isPay;
	}

	public void setIsPay(Integer isPay) {
		this.isPay = isPay;
	}

	public Integer getWareId() {
		return wareId;
	}

	public void setWareId(Integer wareId) {
		this.wareId = wareId;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public String getSubIds() {
		return subIds;
	}

	public void setSubIds(String subIds) {
		this.subIds = subIds;
	}

	public Integer getCheckAutoPrice() {
		return checkAutoPrice;
	}

	public void setCheckAutoPrice(Integer checkAutoPrice) {
		this.checkAutoPrice = checkAutoPrice;
	}

	public BigDecimal getBeginAmt() {
		return beginAmt;
	}

	public void setBeginAmt(BigDecimal beginAmt) {
		this.beginAmt = beginAmt;
	}

	public BigDecimal getEndAmt() {
		return endAmt;
	}

	public void setEndAmt(BigDecimal endAmt) {
		this.endAmt = endAmt;
	}

	public BigDecimal getSumQty() {
		return sumQty;
	}

	public void setSumQty(BigDecimal sumQty) {
		this.sumQty = sumQty;
	}

	public BigDecimal getSumInQty() {
		return sumInQty;
	}

	public void setSumInQty(BigDecimal sumInQty) {
		this.sumInQty = sumInQty;
	}

	public String getProIds() {
		return proIds;
	}

	public void setProIds(String proIds) {
		this.proIds = proIds;
	}

	public String getProNames() {
		return proNames;
	}

	public void setProNames(String proNames) {
		this.proNames = proNames;
	}

	public List<StorehouseStayInSubBean> getList() {
		return list;
	}

	public void setList(List<StorehouseStayInSubBean> list) {
		this.list = list;
	}
}
