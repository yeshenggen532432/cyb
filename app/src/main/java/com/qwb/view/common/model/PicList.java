package com.qwb.view.common.model;

import java.io.Serializable;

public class PicList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6625696821865011690L;
	private String picMini ;
	private String pic ;
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
}
