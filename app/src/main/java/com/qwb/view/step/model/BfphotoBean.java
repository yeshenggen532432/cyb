package com.qwb.view.step.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BfphotoBean implements Parcelable {

	private String id;// 图片id
	private String type;// 1拜访签到拍照；2生动化；3堆头；4陈列检查采集；5道谢并告知下次拜访日期
	private String ssId;// 所属板块id
	private String picMini; // 小图
	private String pic; // 大图
	// 自己添加--与上面对应
	private String cid; // 客户id

	public BfphotoBean() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSsId() {
		return ssId;
	}

	public void setSsId(String ssId) {
		this.ssId = ssId;
	}

	public String getPicMini() {
		return picMini;
	}

	public void setPicMini(String picMini) {
		this.picMini = picMini;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeString(type);
		out.writeString(ssId);
		out.writeString(picMini);
		out.writeString(pic);
	}

	public static final Parcelable.Creator<BfphotoBean> CREATOR = new Creator<BfphotoBean>() {
		@Override
		public BfphotoBean[] newArray(int size) {
			return new BfphotoBean[size];
		}

		@Override
		public BfphotoBean createFromParcel(Parcel in) {
			return new BfphotoBean(in);
		}
	};

	public BfphotoBean(Parcel in) {
		id = in.readString();
		type = in.readString();
		ssId = in.readString();
		picMini = in.readString();
		pic = in.readString();
	}

}
