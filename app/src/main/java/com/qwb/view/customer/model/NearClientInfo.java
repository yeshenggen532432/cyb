package com.qwb.view.customer.model;

import java.io.Serializable;


public class NearClientInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -461640187003636230L;
	private int id ;  //客户id
	private int khTp;//客户类型：（1经销商；2客户）
	private String khNm ; 		 //客户名称
	private String memberNm ; //业务员
	private String longitude ;
	private String latitude ;
	private String jlkm ;        //距离
	private String address ;     //地址
	private String linkman ;     //联系人
	private String mobile ;
	private String tel ; //电话
	private String scbfDate ; //上次拜访日期

	private String province ;    //省份
	private String city ;
	private String area ;
	
	private String xsjdNm ;      //销售阶段
	private String qdtpNm ;      //渠道类型
	private String xxzt ;        //新鲜值（临期，正常）

	private Integer locationTag;////位置标记(用于手机端纠正位置) 0.未标记

	public void setId(int id) {
		this.id = id;
	}

	public Integer getLocationTag() {
		return locationTag;
	}

	public void setLocationTag(Integer locationTag) {
		this.locationTag = locationTag;
	}

	public String getMemberNm() {
		return memberNm;
	}
	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
	}
	public String getScbfDate() {
		return scbfDate;
	}
	public void setScbfDate(String scbfDate) {
		this.scbfDate = scbfDate;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getXsjdNm() {
		return xsjdNm;
	}
	public void setXsjdNm(String xsjdNm) {
		this.xsjdNm = xsjdNm;
	}
	public String getJlkm() {
		return jlkm;
	}
	public void setJlkm(String jlkm) {
		this.jlkm = jlkm;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getKhNm() {
		return khNm;
	}
	public void setKhNm(String khNm) {
		this.khNm = khNm;
	}
	public String getQdtpNm() {
		return qdtpNm;
	}
	public void setQdtpNm(String qdtpNm) {
		this.qdtpNm = qdtpNm;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getXxzt() {
		return xxzt;
	}
	public void setXxzt(String xxzt) {
		this.xxzt = xxzt;
	}
	public int getKhTp() {
		return khTp;
	}
	public void setKhTp(int khTp) {
		this.khTp = khTp;
	}
}