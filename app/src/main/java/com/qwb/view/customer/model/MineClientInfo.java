package com.qwb.view.customer.model;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * 客户管理--我的客户
 *
 */
public class MineClientInfo implements Parcelable {

	private int id ; //客户id
	private int khTp;//客户类型：（1经销商；2客户）
	private int memId ;         //客户--对应的成员id
	private String khNm ; 		 //客户名称
	private String memberNm ;    //业务员（我的客户才有）
	private String branchName ;  //分组名称（我的客户才有）
	private String longitude ;
	private String latitude ;
	private String jlkm ; //距离
	private String address ;     //地址
	private String linkman ;     //联系人
	private String mobile ;      //联系人电话
	private String tel ; //电话
	private String scbfDate ; //上次拜访时间
	private String qdtpNm ;      //渠道类型
	private String xsjdNm ;
	private String xxzt ;        //新鲜度（临期，正常）
	//销售阶段
	private String province ;    //省份
	private String city ;
	private String area ;
	private Integer locationTag;////位置标记(用于手机端纠正位置) 0.未标记

	public Integer getLocationTag() {
		return locationTag;
	}

	public void setLocationTag(Integer locationTag) {
		this.locationTag = locationTag;
	}

	public int getMemId() {
		return memId;
	}
	public void setMemId(int memId) {
		this.memId = memId;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public int getKhTp() {
		return khTp;
	}
	public void setKhTp(int khTp) {
		this.khTp = khTp;
	}
	public String getScbfDate() {
		return scbfDate;
	}
	public void setScbfDate(String scbfDate) {
		this.scbfDate = scbfDate;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getXsjdNm() {
		return xsjdNm;
	}
	public void setXsjdNm(String xsjdNm) {
		this.xsjdNm = xsjdNm;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getXxzt() {
		return xxzt;
	}
	public void setXxzt(String xxzt) {
		this.xxzt = xxzt;
	}
	public String getJlkm() {
		return jlkm;
	}
	public void setJlkm(String jlkm) {
		this.jlkm = jlkm;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		//要与createFromParcel顺序一致
		parcel.writeInt(id);
		parcel.writeInt(khTp);
		parcel.writeInt(memId);

		parcel.writeString(khNm);
		parcel.writeString(memberNm);
		parcel.writeString(branchName);
		parcel.writeString(longitude);
		parcel.writeString(latitude);
		parcel.writeString(jlkm);
		parcel.writeString(address);
		parcel.writeString(linkman);
		parcel.writeString(mobile);
		parcel.writeString(tel);
		parcel.writeString(scbfDate);
		parcel.writeString(qdtpNm);
		parcel.writeString(xsjdNm);
		parcel.writeString(xxzt);
		parcel.writeString(province);
		parcel.writeString(city);
		parcel.writeString(area);
	}

	public static final Parcelable.Creator<MineClientInfo> CREATOR = new Creator<MineClientInfo>() {

		@Override
		public MineClientInfo createFromParcel(Parcel source) {
			//要与writeToParcel顺序一致
			MineClientInfo data = new MineClientInfo();
			data.setId(source.readInt());
			data.setKhTp(source.readInt());
			data.setMemId(source.readInt());

			data.setKhNm(source.readString());
			data.setMemberNm(source.readString());
			data.setBranchName(source.readString());
			data.setLongitude(source.readString());
			data.setLatitude(source.readString());
			data.setJlkm(source.readString());
			data.setAddress(source.readString());
			data.setLinkman(source.readString());
			data.setMobile(source.readString());
			data.setTel(source.readString());
			data.setScbfDate(source.readString());
			data.setQdtpNm(source.readString());
			data.setXxzt(source.readString());

			data.setProvince(source.readString());
			data.setCity(source.readString());
			data.setArea(source.readString());
			return data;
		}

		@Override
		public MineClientInfo[] newArray(int size) {
			return new MineClientInfo[size];
		}
	};
}
