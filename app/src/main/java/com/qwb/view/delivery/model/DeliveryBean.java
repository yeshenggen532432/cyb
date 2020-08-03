package com.qwb.view.delivery.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 配送单
 */

public class DeliveryBean {
//		"proType": 2,
//		"empId": 765,
//		"orderId": 1728,
//		"saleType": "001",
//		"ddNum": 2,
//		"mid": 1990,
//		"discount": 0,
//		"operator": "",
//		"khNm": "test",
//		"epCustomerName": "",
//		"epCustomerId": "",
//		"vehNo": "闽DNT705",
//		"staffTel": "",
//		"cstId": 164,
//		"disAmt1": 13.17,
//		"pszd": "公司直送",
//		"tel": "8888888",
//		"outId": 8128679,
//		"id": 2,
//		"billNo": "XSPS2019031900001",
//		"outDate": "2019-03-19 16:50",
//		"address": "福建省厦门市同安区汀溪街辅路",
//		"orderNo": "T190319_164918582",
//		"staff": "郭建荣",

    private Integer id;
    private Integer orderId;
    private Integer cstId;
    private String tel;
    private String address;
    private Integer mid;
    private String outTime;
    private String pszd;
    private String remarks;
    private String outType;
    private Integer stkId;
    private String submitUser;
    private String submitTime;
    private String cancelUser;
    private String cancelTime;
    private BigDecimal totalAmt;
    private BigDecimal recAmt;
    private BigDecimal discount;
    private BigDecimal disAmt;
    private String operator;
    private String shr;
    private BigDecimal freeAmt;
    private String khNm;
    private Integer proType;
    private BigDecimal disAmt1;
    private String staff;
    private String staffTel;
    private String recStatus;
    private String newTime;
    private String createTime;
    private String sendTime;
    private Integer driverId;
    private Integer vehId;
    private String driverName;
    private String vehNo;
    private String saleType;
    private Integer waretype;
    private Integer carId;
    private String reauditDesc;
    private String epCustomerId;
    private String epCustomerName;
    private Integer empId;
    private String memberNm;
    private String database;
    private Integer count;
    private List<DeliverySubBean> list;
    private List<Map<String, Object>> maps;
    private String sdate;
    private String edate;
    private BigDecimal ddNum;
    private Integer isMe;
    private String stkName;
    private String outDate;
    private String orderNo;
    private String billStatus;
    private String xsTp;
    private String wareNm;
    private BigDecimal needRec;
    private Integer isPay;
    private Integer wareId;
    private Integer needRtn;
    private String customerType;
    private String timeType;
    private Integer wtype;
    private String branchName;
    private String subIds;
    private String sendStatus;
    private String showNoModify;
    private Integer isExpand;
    private Integer saleCar;
    private BigDecimal beginAmt;
    private BigDecimal endAmt;
    private Integer regionId;
    private String regionNm;
    private Integer outId;
    private String outNo;
    private Integer psState;
    private String billNo;
    private String driverMemberId;//司机对应的业务员id
    private String longitude; //客户经纬度
    private String latitude; //latitude

    public String getDriverMemberId() {
        return driverMemberId;
    }

    public void setDriverMemberId(String driverMemberId) {
        this.driverMemberId = driverMemberId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getCstId() {
        return cstId;
    }

    public void setCstId(Integer cstId) {
        this.cstId = cstId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getPszd() {
        return pszd;
    }

    public void setPszd(String pszd) {
        this.pszd = pszd;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public Integer getStkId() {
        return stkId;
    }

    public void setStkId(Integer stkId) {
        this.stkId = stkId;
    }

    public String getSubmitUser() {
        return submitUser;
    }

    public void setSubmitUser(String submitUser) {
        this.submitUser = submitUser;
    }


    public String getCancelUser() {
        return cancelUser;
    }

    public void setCancelUser(String cancelUser) {
        this.cancelUser = cancelUser;
    }


    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public BigDecimal getRecAmt() {
        return recAmt;
    }

    public void setRecAmt(BigDecimal recAmt) {
        this.recAmt = recAmt;
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

    public String getShr() {
        return shr;
    }

    public void setShr(String shr) {
        this.shr = shr;
    }

    public BigDecimal getFreeAmt() {
        return freeAmt;
    }

    public void setFreeAmt(BigDecimal freeAmt) {
        this.freeAmt = freeAmt;
    }

    public String getKhNm() {
        return khNm;
    }

    public void setKhNm(String khNm) {
        this.khNm = khNm;
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

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getStaffTel() {
        return staffTel;
    }

    public void setStaffTel(String staffTel) {
        this.staffTel = staffTel;
    }

    public String getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(String recStatus) {
        this.recStatus = recStatus;
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

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public Integer getWaretype() {
        return waretype;
    }

    public void setWaretype(Integer waretype) {
        this.waretype = waretype;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getReauditDesc() {
        return reauditDesc;
    }

    public void setReauditDesc(String reauditDesc) {
        this.reauditDesc = reauditDesc;
    }

    public String getEpCustomerId() {
        return epCustomerId;
    }

    public void setEpCustomerId(String epCustomerId) {
        this.epCustomerId = epCustomerId;
    }

    public String getEpCustomerName() {
        return epCustomerName;
    }

    public void setEpCustomerName(String epCustomerName) {
        this.epCustomerName = epCustomerName;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
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

//		public List<StkDeliverysub> getList() {
//			return list;
//		}
//
//		public void setList(List<StkDeliverysub> list) {
//			this.list = list;
//		}

    public List<Map<String, Object>> getMaps() {
        return maps;
    }

    public void setMaps(List<Map<String, Object>> maps) {
        this.maps = maps;
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

    public String getStkName() {
        return stkName;
    }

    public void setStkName(String stkName) {
        this.stkName = stkName;
    }

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getXsTp() {
        return xsTp;
    }

    public void setXsTp(String xsTp) {
        this.xsTp = xsTp;
    }

    public String getWareNm() {
        return wareNm;
    }

    public void setWareNm(String wareNm) {
        this.wareNm = wareNm;
    }

    public BigDecimal getNeedRec() {
        return needRec;
    }

    public void setNeedRec(BigDecimal needRec) {
        this.needRec = needRec;
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

    public Integer getNeedRtn() {
        return needRtn;
    }

    public void setNeedRtn(Integer needRtn) {
        this.needRtn = needRtn;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public Integer getWtype() {
        return wtype;
    }

    public void setWtype(Integer wtype) {
        this.wtype = wtype;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getSubIds() {
        return subIds;
    }

    public void setSubIds(String subIds) {
        this.subIds = subIds;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getShowNoModify() {
        return showNoModify;
    }

    public void setShowNoModify(String showNoModify) {
        this.showNoModify = showNoModify;
    }

    public Integer getIsExpand() {
        return isExpand;
    }

    public void setIsExpand(Integer isExpand) {
        this.isExpand = isExpand;
    }

    public Integer getSaleCar() {
        return saleCar;
    }

    public void setSaleCar(Integer saleCar) {
        this.saleCar = saleCar;
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

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getRegionNm() {
        return regionNm;
    }

    public void setRegionNm(String regionNm) {
        this.regionNm = regionNm;
    }

    public Integer getOutId() {
        return outId;
    }

    public void setOutId(Integer outId) {
        this.outId = outId;
    }

    public String getOutNo() {
        return outNo;
    }

    public void setOutNo(String outNo) {
        this.outNo = outNo;
    }

    public Integer getPsState() {
        return psState;
    }

    public void setPsState(Integer psState) {
        this.psState = psState;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getNewTime() {
        return newTime;
    }

    public void setNewTime(String newTime) {
        this.newTime = newTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
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

    public List<DeliverySubBean> getList() {
        return list;
    }

    public void setList(List<DeliverySubBean> list) {
        this.list = list;
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
}
