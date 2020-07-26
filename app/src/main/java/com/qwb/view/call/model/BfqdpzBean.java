package com.qwb.view.call.model;

import java.io.Serializable;

/**
 * 拜访查询--拜访纪录--1：拜访签到拍照的图片
 *
 */

public class BfqdpzBean  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7486729963408026849L;
	private String id;   //
	private String type;//
	private String picMini;//
	private String pic;//
	private String ssId;//
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
	public String getSsId() {
		return ssId;
	}
	public void setSsId(String ssId) {
		this.ssId = ssId;
	}
		
		
}
