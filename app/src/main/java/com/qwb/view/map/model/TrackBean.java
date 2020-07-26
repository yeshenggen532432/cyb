package com.qwb.view.map.model;

import com.qwb.db.LocationBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 长连接模块：--查询指定用户一天定位信息
 *
 */
public class TrackBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6933878918359852507L;
	private int code ;//0成功，1失败
	private int count ;//
	private int total ;//个数
	private List<LocationBean> location=new ArrayList<>();
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<LocationBean> getLocation() {
		return location;
	}
	public void setLocation(List<LocationBean> location) {
		this.location = location;
	}
	
}
