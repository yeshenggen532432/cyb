package com.qwb.view.company.model;

import com.qwb.view.base.model.BaseBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * 采购单
 */
public class PurchaseOrderBean extends BaseBean {

	private Integer id;//id
	private String billNo;//单号
	private String inDate;//发票日期
	private BigDecimal discount;//整单折扣
	private BigDecimal totalAmt;//总计
	private BigDecimal payAmt;//已付金额
	private BigDecimal disAmt;//发票金额
	private BigDecimal freeAmt;//核销金额
	private String remarks;
	private String billStatus;//发票状态
	private String proName;//供应商
	private String inType;//采购类型
	private Integer stkId;
	private String stkName;
	private Integer proId;
	private Integer proType;//0供应商  1员工  2客户 3其他往外  4：会员
	private Integer status;//-2暂存
	private Integer checkAutoPrice;//检查采购单是否自动更新商品信息价格
	private List<PurchaseOrderSubBean> list;

	public Integer getCheckAutoPrice() {
		return checkAutoPrice;
	}

	public void setCheckAutoPrice(Integer checkAutoPrice) {
		this.checkAutoPrice = checkAutoPrice;
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

	public Integer getProType() {
		return proType;
	}

	public void setProType(Integer proType) {
		this.proType = proType;
	}

	public Integer getStkId() {
		return stkId;
	}

	public void setStkId(Integer stkId) {
		this.stkId = stkId;
	}

	public String getStkName() {
		return stkName;
	}

	public void setStkName(String stkName) {
		this.stkName = stkName;
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

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
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

	public BigDecimal getDisAmt() {
		return disAmt;
	}

	public void setDisAmt(BigDecimal disAmt) {
		this.disAmt = disAmt;
	}

	public BigDecimal getFreeAmt() {
		return freeAmt;
	}

	public void setFreeAmt(BigDecimal freeAmt) {
		this.freeAmt = freeAmt;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getInType() {
		return inType;
	}

	public void setInType(String inType) {
		this.inType = inType;
	}

	public List<PurchaseOrderSubBean> getList() {
		return list;
	}

	public void setList(List<PurchaseOrderSubBean> list) {
		this.list = list;
	}


//	private String proName;//入库对角
//	private Integer openZfjz;//开启杂费结转 0:未开启 1:已开启
//	private Integer mid;
//	private BigDecimal totalAmt;
//	private BigDecimal payAmt;
//	private BigDecimal discount;
//	private BigDecimal disAmt;
//	private BigDecimal freeAmt;
//	private String inDate;
//	private List<StkInsub> list;//详情集合
//	private Integer isType;
////	private Date inTime;
//	private Integer stkId;
//	private String inType;
//	private String remarks;
//	private String submitUser;
////	private Date submitTime;
//	private String cancelUser;
//	private Date cancelTime;
//	private String operator;
//	private BigDecimal disAmt1;
////	private Date newTime;
//	private Integer orderId;
//	private String orderNo;
//	private Integer empId;
//	private String empNm;
//	private Integer driverId;
//	private Integer vehId;
//	private String driverName;
//	private String vehNo;
////	private Date sureTime;//确定时间 pc端修改销售退货后给该时间赋值
//	private String reauditDesc;//说明，主要记录反审核操作
//	private Integer waretype;//查询条件商品类别
//	private String keyWord;//查询条件 单号、供应商名称、品名、备注
//	private String inTimeStr;
//	private String submitTimeStr;
//	private String cancelTimeStr;
//	private String newTimeStr;
//	private String waretypeIds;
//	private Integer houseId;//总仓ID
//	private String houseName;//库位名称
//	private String inTypes;
//	private Integer showShop;
//	private String memberNm;//业务员名称
//	private String database;//数据库
//	private Integer count;//笔数
//
//	private String sdate;//开始时间
//	private String edate;//结束时间
//	private BigDecimal ddNum;//数量
//	private Integer isMe;//是否我的（1是；2否）
//
//	private String stkName;
//	private String billStatus;
//	private String payStatus;
//	private String wareNm;
//	private Boolean chk;
//	private BigDecimal needPay;
//	private Integer isPay;
//	private Integer wareId;
//	private String timeType;//时间类型 发票时间、发货时间
//	private String subIds;
//	private Integer checkAutoPrice;//检查采购单是否自动更新商品信息价格
//	private BigDecimal beginAmt;//金额查询起始金额
//	private BigDecimal endAmt;//金额查询终点金额
//	private BigDecimal sumQty;
//	private BigDecimal sumInQty;
//	private String proIds;//客户IDs
//	private String proNames;//客户名称
}
