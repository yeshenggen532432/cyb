package com.qwb.view.common.model;

import com.qwb.view.base.model.BaseBean;

/**
 * 商品信息-图片(公共的)
 */
public class PublicPicBean extends BaseBean {
	private int id;
	private String picMini;
	private String pic;
	private String wareId;
	private int type;//1:为主图
//	"id": 1,
//			"picMini": "xmqwbwlkjyxgs285/ware/pic/small_85288a261014d54876c22fe99b0cf358.jpeg",
//			"pic": "xmqwbwlkjyxgs285/ware/pic/1531882135230.jpeg",
//			"wareId": 7

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
	}
}
