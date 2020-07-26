package com.qwb.view.mine.model;

import java.io.Serializable;

/**
 * 账号管理
 */
public class UserManagerBean implements Serializable {
	private static final long serialVersionUID = -9103926651985745839L;
	private String mid;// 用户id
	private String name;// 用户名
	private String phone;// 手机号
	private String memberHead;// 头像
	private String pwd;// 密码

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMemberHead() {
		return memberHead;
	}

	public void setMemberHead(String memberHead) {
		this.memberHead = memberHead;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	
}
