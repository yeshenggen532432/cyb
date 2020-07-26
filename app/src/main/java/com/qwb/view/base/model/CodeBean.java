package com.qwb.view.base.model;

/**
 * 短信验证码
 */
public class CodeBean extends XbaseBean {

//	{
//		"code": 200,
//			"message": "发送成功",
//			"data": {
//		"token": "8aef5faf44b24eb39c296b0aa971b174"
//	},
//		"success": true
//	}

	private String data;
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
