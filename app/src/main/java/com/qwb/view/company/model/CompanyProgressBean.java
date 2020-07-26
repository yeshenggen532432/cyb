package com.qwb.view.company.model;

import com.qwb.view.base.model.XbaseBean;

/**
 * 注册公司--进度查询
 */
public class CompanyProgressBean extends XbaseBean{
//	"code": 200,
//			"data": {
//		"status": 1,  //1 处理中 2成功 3失败
//				"message": "正在处理中"
//	},
//			"success": true

	private Data data;
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data{
		private int status;////1 处理中 2成功 3失败
		private String message;

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

}
