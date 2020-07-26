package com.qwb.db;

import org.litepal.crud.LitePalSupport;

/**
 *  缓存我的客户（全部）
 */
public class DMineCustomerBean extends LitePalSupport {
	private long id;//自增id
	private String userId;//用户id
	private String companyId;//公司id
	private String pinyin;//汉字转拼音
	private String py;//简拼
	private double distance;//距离

	private String cid ;//客户id
	private String khTp ;//客户类型：（1经销商；2客户）
	private String khNm ;
	private String memId ;         //客户--对应的成员id
	private String memberNm ;    //业务员（我的客户才有）
	private String branchName ;  //分组名称（我的客户才有）
	private String address ;
	private String longitude ;
	private String latitude ;
	private String linkman ;
	private String scbfDate ;//上次拜访时间
	private String xxzt ;        //新鲜度（临期，正常）
	private String mobile ;
	private String tel ;
	private String province ;
	private String city ;
	private String area ;
	private String qdtpNm ;//渠道类型
	private String xsjdNm;

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public String getXxzt() {
		return xxzt;
	}

	public void setXxzt(String xxzt) {
		this.xxzt = xxzt;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getKhTp() {
		return khTp;
	}

	public void setKhTp(String khTp) {
		this.khTp = khTp;
	}

	public String getKhNm() {
		return khNm;
	}

	public void setKhNm(String khNm) {
		this.khNm = khNm;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getScbfDate() {
		return scbfDate;
	}

	public void setScbfDate(String scbfDate) {
		this.scbfDate = scbfDate;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getQdtpNm() {
		return qdtpNm;
	}

	public void setQdtpNm(String qdtpNm) {
		this.qdtpNm = qdtpNm;
	}

	public String getXsjdNm() {
		return xsjdNm;
	}

	public void setXsjdNm(String xsjdNm) {
		this.xsjdNm = xsjdNm;
	}

	public String getMemId() {
		return memId;
	}

	public void setMemId(String memId) {
		this.memId = memId;
	}

	public String getMemberNm() {
		return memberNm;
	}

	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
}
