package com.qwb.view.cache.model;


/**
 *	我的缓存
 */

public class MineCacheBean{

	public Integer imgRes;//图片资源id
	private String name;

	public MineCacheBean() {
	}
	public MineCacheBean(Integer imgRes, String name) {
		this.imgRes = imgRes;
		this.name = name;
	}

	public Integer getImgRes() {
		return imgRes;
	}

	public void setImgRes(Integer imgRes) {
		this.imgRes = imgRes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
