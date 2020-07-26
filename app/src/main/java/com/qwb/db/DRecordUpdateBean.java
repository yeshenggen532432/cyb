package com.qwb.db;

import org.litepal.crud.LitePalSupport;

/**
 *  记录今天是否已缓存(1:我的客户；2：商品分类; 3:商品)
 */
public class DRecordUpdateBean extends LitePalSupport {
	private long id;//自增id
	private String userId;//用户id
	private String companyId;//公司id

	private String time;//时间
	private int update;//今天是否已更新(一天只更新一次，可以在设置模块，手动更新)  1:已更新
	private String model;//1:我的客户；2：商品分类; 3:商品

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getUpdate() {
		return update;
	}

	public void setUpdate(int update) {
		this.update = update;
	}
}
