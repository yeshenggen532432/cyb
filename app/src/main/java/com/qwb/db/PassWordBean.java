package com.qwb.db;

import org.litepal.crud.LitePalSupport;

/**
 * 记住密码
 */
public class PassWordBean extends LitePalSupport {
	private int companyId;//公司id
	private int userId;//用户id
	private String phone;//手机号
	private String pwd;//密码
	private boolean check;//是否记住

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}
}
