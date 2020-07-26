package com.qwb.view.customer.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;

public class ClientDetailBean extends BaseBean {

	private ClientDetail customer;

	public ClientDetail getCustomer() {
		return customer;
	}

	public void setCustomer(ClientDetail customer) {
		this.customer = customer;
	}

	public static class ClientDetail implements Serializable {
		private String address;
		private Integer id;
		private Integer memId;
		private Integer branchId;
		private Integer khTp;  //客户种类（1经销商；2客户）
		private String memberNm;
		private String isOpen;
		private String khNm;
		private String ghtpNm;
		private String khdjNm;
		private String qdtpNm;
		private Integer qdtypeId;//客户类型ID
		private Integer khlevelId;//客户等级
		private String sctpNm;
		private String wlNm;
		private String khCode;
		private String longitude;
		private String latitude;
		private String createTime;
		private String branchName;
		private String area;
		private String city;

		private String shZt;
		private String xsjdNm;
		private String wxCode;
		private String wltel;
		private String wllinkman;
		private String wladdress;
		private String qq;
		private String province;
		private String pkhNm;
		private String openDate;
		private String nxse;
		private String mobileCx;
		private String mobile;//手机
		private String linkman;//联系人--收货人
		private String jyfw;
		private String jxsztNm;
		private String jxsjbNm;
		private String jxsflNm;
		private String isYx;
		private String ftel;
		private String fman;
		private String fgqy;
		private String erpCode;
		private String dlqtpp;
		private String dlqtpl;
		private String closeDate;
		private String ckmj;
		private String bfpcNm;
		private String shTime;
		private String tel;//电话
		private String remo;
		private String hzfsNm;// 合作方式
		private Integer regionId;// 客户区域Id
		private String regionNm;// 客户区域名称



		public String getHzfsNm() {
			return hzfsNm;
		}

		public void setHzfsNm(String hzfsNm) {
			this.hzfsNm = hzfsNm;
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

		public String getShZt() {
			return shZt;
		}

		public void setShZt(String shZt) {
			this.shZt = shZt;
		}

		public String getXsjdNm() {
			return xsjdNm;
		}

		public void setXsjdNm(String xsjdNm) {
			this.xsjdNm = xsjdNm;
		}

		public String getWxCode() {
			return wxCode;
		}

		public void setWxCode(String wxCode) {
			this.wxCode = wxCode;
		}

		public String getWltel() {
			return wltel;
		}

		public void setWltel(String wltel) {
			this.wltel = wltel;
		}

		public String getWllinkman() {
			return wllinkman;
		}

		public void setWllinkman(String wllinkman) {
			this.wllinkman = wllinkman;
		}

		public String getWladdress() {
			return wladdress;
		}

		public void setWladdress(String wladdress) {
			this.wladdress = wladdress;
		}

		public String getQq() {
			return qq;
		}

		public void setQq(String qq) {
			this.qq = qq;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getPkhNm() {
			return pkhNm;
		}

		public void setPkhNm(String pkhNm) {
			this.pkhNm = pkhNm;
		}

		public String getOpenDate() {
			return openDate;
		}

		public void setOpenDate(String openDate) {
			this.openDate = openDate;
		}

		public String getNxse() {
			return nxse;
		}

		public void setNxse(String nxse) {
			this.nxse = nxse;
		}

		public String getMobileCx() {
			return mobileCx;
		}

		public void setMobileCx(String mobileCx) {
			this.mobileCx = mobileCx;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getLinkman() {
			return linkman;
		}

		public void setLinkman(String linkman) {
			this.linkman = linkman;
		}

		public String getJyfw() {
			return jyfw;
		}

		public void setJyfw(String jyfw) {
			this.jyfw = jyfw;
		}

		public String getJxsztNm() {
			return jxsztNm;
		}

		public void setJxsztNm(String jxsztNm) {
			this.jxsztNm = jxsztNm;
		}

		public String getJxsjbNm() {
			return jxsjbNm;
		}

		public void setJxsjbNm(String jxsjbNm) {
			this.jxsjbNm = jxsjbNm;
		}

		public String getJxsflNm() {
			return jxsflNm;
		}

		public void setJxsflNm(String jxsflNm) {
			this.jxsflNm = jxsflNm;
		}

		public String getIsYx() {
			return isYx;
		}

		public void setIsYx(String isYx) {
			this.isYx = isYx;
		}

		public String getFtel() {
			return ftel;
		}

		public void setFtel(String ftel) {
			this.ftel = ftel;
		}

		public String getFman() {
			return fman;
		}

		public void setFman(String fman) {
			this.fman = fman;
		}

		public String getFgqy() {
			return fgqy;
		}

		public void setFgqy(String fgqy) {
			this.fgqy = fgqy;
		}

		public String getErpCode() {
			return erpCode;
		}

		public void setErpCode(String erpCode) {
			this.erpCode = erpCode;
		}

		public String getDlqtpp() {
			return dlqtpp;
		}

		public void setDlqtpp(String dlqtpp) {
			this.dlqtpp = dlqtpp;
		}

		public String getDlqtpl() {
			return dlqtpl;
		}

		public void setDlqtpl(String dlqtpl) {
			this.dlqtpl = dlqtpl;
		}

		public String getCloseDate() {
			return closeDate;
		}

		public void setCloseDate(String closeDate) {
			this.closeDate = closeDate;
		}

		public String getCkmj() {
			return ckmj;
		}

		public void setCkmj(String ckmj) {
			this.ckmj = ckmj;
		}

		public String getBfpcNm() {
			return bfpcNm;
		}

		public void setBfpcNm(String bfpcNm) {
			this.bfpcNm = bfpcNm;
		}

		public String getShTime() {
			return shTime;
		}

		public void setShTime(String shTime) {
			this.shTime = shTime;
		}

		public String getTel() {
			return tel;
		}

		public void setTel(String tel) {
			this.tel = tel;
		}

		public String getRemo() {
			return remo;
		}

		public void setRemo(String remo) {
			this.remo = remo;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Integer getBranchId() {
			return branchId;
		}

		public void setBranchId(Integer branchId) {
			this.branchId = branchId;
		}

		public String getMemberNm() {
			return memberNm;
		}

		public void setMemberNm(String memberNm) {
			this.memberNm = memberNm;
		}

		public String getIsOpen() {
			return isOpen;
		}

		public void setIsOpen(String isOpen) {
			this.isOpen = isOpen;
		}

		public String getKhNm() {
			return khNm;
		}

		public void setKhNm(String khNm) {
			this.khNm = khNm;
		}

		public Integer getKhTp() {
			return khTp;
		}

		public void setKhTp(Integer khTp) {
			this.khTp = khTp;
		}

		public String getGhtpNm() {
			return ghtpNm;
		}

		public void setGhtpNm(String ghtpNm) {
			this.ghtpNm = ghtpNm;
		}

		public String getKhdjNm() {
			return khdjNm;
		}

		public void setKhdjNm(String khdjNm) {
			this.khdjNm = khdjNm;
		}

		public String getQdtpNm() {
			return qdtpNm;
		}

		public void setQdtpNm(String qdtpNm) {
			this.qdtpNm = qdtpNm;
		}

		public String getSctpNm() {
			return sctpNm;
		}

		public void setSctpNm(String sctpNm) {
			this.sctpNm = sctpNm;
		}

		public String getWlNm() {
			return wlNm;
		}

		public void setWlNm(String wlNm) {
			this.wlNm = wlNm;
		}

		public String getKhCode() {
			return khCode;
		}

		public void setKhCode(String khCode) {
			this.khCode = khCode;
		}

		public Integer getMemId() {
			return memId;
		}

		public void setMemId(Integer memId) {
			this.memId = memId;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getBranchName() {
			return branchName;
		}

		public void setBranchName(String branchName) {
			this.branchName = branchName;
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

		public Integer getQdtypeId() {
			return qdtypeId;
		}

		public void setQdtypeId(Integer qdtypeId) {
			this.qdtypeId = qdtypeId;
		}

		public Integer getKhlevelId() {
			return khlevelId;
		}

		public void setKhlevelId(Integer khlevelId) {
			this.khlevelId = khlevelId;
		}
	}
}
