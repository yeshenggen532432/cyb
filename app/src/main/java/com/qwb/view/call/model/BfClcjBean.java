package com.qwb.view.call.model;

import java.io.Serializable;
import java.util.List;

/**
 * 拜访查询--拜访纪录--陈列采集
 *
 */


public class BfClcjBean  implements Serializable {
	
	private static final long serialVersionUID = -1753813429233865382L;
	private String id;   //id
	private String mdid;//模板ID
	private String remo;//
	private String bds;//冰点数
	private String djpms;//端架
	private String hjpms; //货架
	private String sytwl; //收银台
	private String mdNm;
	private List<BfqdpzBean> bfxgPicLs;
	
	
	public String getMdNm() {
		return mdNm;
	}
	public void setMdNm(String mdNm) {
		this.mdNm = mdNm;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMdid() {
		return mdid;
	}
	public void setMdid(String mdid) {
		this.mdid = mdid;
	}
	public String getRemo() {
		return remo;
	}
	public void setRemo(String remo) {
		this.remo = remo;
	}
	public String getBds() {
		return bds;
	}
	public void setBds(String bds) {
		this.bds = bds;
	}
	public String getDjpms() {
		return djpms;
	}
	public void setDjpms(String djpms) {
		this.djpms = djpms;
	}
	public String getHjpms() {
		return hjpms;
	}
	public void setHjpms(String hjpms) {
		this.hjpms = hjpms;
	}
	public String getSytwl() {
		return sytwl;
	}
	public void setSytwl(String sytwl) {
		this.sytwl = sytwl;
	}
	public List<BfqdpzBean> getBfxgPicLs() {
		return bfxgPicLs;
	}
	public void setBfxgPicLs(List<BfqdpzBean> bfxgPicLs) {
		this.bfxgPicLs = bfxgPicLs;
	}
	
}
