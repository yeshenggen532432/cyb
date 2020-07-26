package com.qwb.view.order.model;


/**
 * 下单(订货下单，车销单，退货单)
 */
public class OrderBean {

	private int id; // 订单id
	private String memberNm;// 业务员名称
	private String khNm; // 客户名称
	private String orderNo; // 订单号
	private String orderZt; // 订单状态（审核，未审核）
	private String oddate; // 日期
	private int isMe; // 1：我  2：别人
	private int redMark;//是否红字开单  0：正常单据  1：红字单据  默认0

	//快消--字段
	private String odtime; // 时间
	private String tel; // 电话
	private String shr; // 收货人
	private String cjje; // 金额
	private String ddNum; // 数量

	//卖场--字段
	private int mid; // 业代id
	private int cid; // 客户id

	public int getIsMe() {
		return isMe;
	}

	public void setIsMe(int isMe) {
		this.isMe = isMe;
	}

	public String getDdNum() {
		return ddNum;
	}

	public void setDdNum(String ddNum) {
		this.ddNum = ddNum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMemberNm() {
		return memberNm;
	}

	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
	}

	public String getKhNm() {
		return khNm;
	}

	public void setKhNm(String khNm) {
		this.khNm = khNm;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOdtime() {
		return odtime;
	}

	public void setOdtime(String odtime) {
		this.odtime = odtime;
	}

	public String getOrderZt() {
		return orderZt;
	}

	public void setOrderZt(String orderZt) {
		this.orderZt = orderZt;
	}

	public String getOddate() {
		return oddate;
	}

	public void setOddate(String oddate) {
		this.oddate = oddate;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getShr() {
		return shr;
	}

	public void setShr(String shr) {
		this.shr = shr;
	}

	public String getCjje() {
		return cjje;
	}

	public void setCjje(String cjje) {
		this.cjje = cjje;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getRedMark() {
		return redMark;
	}

	public void setRedMark(int redMark) {
		this.redMark = redMark;
	}
}
