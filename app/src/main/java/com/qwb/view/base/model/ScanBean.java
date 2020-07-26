package com.qwb.view.base.model;


/**
 * 扫描
 */
public class ScanBean {

	private String type;//1:扫描关注公司
	private int companyId ;//公司id
	private String companyName ;//公司名称
	private String ticket ;//扫描登录

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
}
