package com.qwb.db;

import org.litepal.crud.LitePalSupport;


/**
 * 日报；周报；月报
 */
public class SaveLogBean extends LitePalSupport {

	private String gzNrcd ;//今日完成工作文字长度
	private String gzZjcd ;//未完成工作文字长度
	private String gzJhcd ;//需帮助与支持文字长度
	private String gzBzcd ;//
	private String remocd ;//备注文字长度

	private String userId;//用户id
	private String companyId;//公司id
	private String type;//1:日报，周报；月报

	public String getGzNrcd() {
		return gzNrcd;
	}

	public void setGzNrcd(String gzNrcd) {
		this.gzNrcd = gzNrcd;
	}

	public String getGzZjcd() {
		return gzZjcd;
	}

	public void setGzZjcd(String gzZjcd) {
		this.gzZjcd = gzZjcd;
	}

	public String getGzJhcd() {
		return gzJhcd;
	}

	public void setGzJhcd(String gzJhcd) {
		this.gzJhcd = gzJhcd;
	}

	public String getGzBzcd() {
		return gzBzcd;
	}

	public void setGzBzcd(String gzBzcd) {
		this.gzBzcd = gzBzcd;
	}

	public String getRemocd() {
		return remocd;
	}

	public void setRemocd(String remocd) {
		this.remocd = remocd;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
