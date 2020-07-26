package com.qwb.view.step.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 销售小结
 *
 */
public class QueryXsxj  implements Parcelable {
	private String cid; // 客户id---关联
	private String id; // 销售小结id
	private String mid; // 业务员id
	private String wid; // 商品id
	private String dhNum;// 到货量
	private String sxNum;// 实销量
	private String kcNum;// 库存量
	private String ddNum;// 订单数
	private String xstp; // 售销类型
	private String remo; // 备注
	private String xjdate;// 日期
	private String xxd;// 新鲜值
	private String wareNm;// 商品名称
	private String wareGg;// 规格
	
	
	
	public QueryXsxj() {
		super();
	}

	public String getWid() {
		return wid;
	}

	public void setWid(String wid) {
		this.wid = wid;
	}

	public String getDhNum() {
		return dhNum;
	}

	public void setDhNum(String dhNum) {
		this.dhNum = dhNum;
	}

	public String getSxNum() {
		return sxNum;
	}

	public void setSxNum(String sxNum) {
		this.sxNum = sxNum;
	}

	public String getKcNum() {
		return kcNum;
	}

	public void setKcNum(String kcNum) {
		this.kcNum = kcNum;
	}

	public String getDdNum() {
		return ddNum;
	}

	public void setDdNum(String ddNum) {
		this.ddNum = ddNum;
	}

	public String getXstp() {
		return xstp;
	}

	public void setXstp(String xstp) {
		this.xstp = xstp;
	}

	public String getRemo() {
		return remo;
	}

	public void setRemo(String remo) {
		this.remo = remo;
	}

	public String get_Id() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getXjdate() {
		return xjdate;
	}

	public void setXjdate(String xjdate) {
		this.xjdate = xjdate;
	}

	public String getWareNm() {
		return wareNm;
	}

	public void setWareNm(String wareNm) {
		this.wareNm = wareNm;
	}

	public String getWareGg() {
		return wareGg;
	}

	public void setWareGg(String wareGg) {
		this.wareGg = wareGg;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getXxd() {
		return xxd;
	}

	public void setXxd(String xxd) {
		this.xxd = xxd;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		
		out.writeString(id);
		out.writeString(cid);
		out.writeString(mid);
		out.writeString(wid);
		out.writeString(dhNum);
		out.writeString(sxNum);
		out.writeString(kcNum);
		out.writeString(ddNum);
		out.writeString(xstp);
		out.writeString(remo);
		out.writeString(xxd);
		out.writeString(xjdate);
		out.writeString(wareNm);
		out.writeString(wareGg);
	}
	
	public static final Parcelable.Creator<QueryXsxj> CREATOR = new Creator<QueryXsxj>() {
		@Override
		public QueryXsxj[] newArray(int size) {
			return new QueryXsxj[size];
		}

		@Override
		public QueryXsxj createFromParcel(Parcel in) {
			return new QueryXsxj(in);
		}
	};

	public QueryXsxj(Parcel in) {
		
		id = in.readString();
		cid = in.readString();
		mid = in.readString();
		wid = in.readString();
		dhNum = in.readString();
		sxNum = in.readString();
		kcNum = in.readString();
		ddNum = in.readString();
		xstp = in.readString();
		remo = in.readString();
		xxd = in.readString();
		xjdate = in.readString();
		wareNm = in.readString();
		wareGg = in.readString();
	}

	
}


