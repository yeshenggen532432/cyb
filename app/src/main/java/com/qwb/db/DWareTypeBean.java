package com.qwb.db;

import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 *  缓存-商品分类
 */
public class DWareTypeBean extends LitePalSupport {
	private long id;//自增id
	private String userId;//用户id
	private String companyId;//公司id

	private int pid;//父id
	private int wareTypeId; //商品id
	private String wareTypeNm; //商品名称
	private String wareTypeLeaf; //商品类型
	private List<DWareTypeBean> list;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

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

	public int getWareTypeId() {
		return wareTypeId;
	}

	public void setWareTypeId(int wareTypeId) {
		this.wareTypeId = wareTypeId;
	}

	public String getWareTypeNm() {
		return wareTypeNm;
	}

	public void setWareTypeNm(String wareTypeNm) {
		this.wareTypeNm = wareTypeNm;
	}

	public String getWareTypeLeaf() {
		return wareTypeLeaf;
	}

	public void setWareTypeLeaf(String wareTypeLeaf) {
		this.wareTypeLeaf = wareTypeLeaf;
	}

	public List<DWareTypeBean> getList() {
		return list;
	}

	public void setList(List<DWareTypeBean> list) {
		this.list = list;
	}
}
