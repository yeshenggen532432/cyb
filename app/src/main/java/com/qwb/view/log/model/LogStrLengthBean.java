package com.qwb.view.log.model;

import com.qwb.view.base.model.BaseBean;



/**
 * 日报；周报；月报： 限制文字输入的字数
 */
public class LogStrLengthBean extends BaseBean {

//    "state": true,
//			"msg": "获取报文字长度信息成功",
//			"gzNrcd": 0,
//			"gzZjcd": 0,
//			"gzJhcd": 0,
//			"gzBzcd": 0,
//			"remocd": 0

	private int gzNrcd ;//今日完成工作文字长度
	private int gzZjcd ;//未完成工作文字长度
	private int gzJhcd ;//需帮助与支持文字长度
	private int gzBzcd ;//
	private int remocd ;//备注文字长度


	public int getGzNrcd() {
		return gzNrcd;
	}

	public void setGzNrcd(int gzNrcd) {
		this.gzNrcd = gzNrcd;
	}

	public int getGzZjcd() {
		return gzZjcd;
	}

	public void setGzZjcd(int gzZjcd) {
		this.gzZjcd = gzZjcd;
	}

	public int getGzJhcd() {
		return gzJhcd;
	}

	public void setGzJhcd(int gzJhcd) {
		this.gzJhcd = gzJhcd;
	}

	public int getGzBzcd() {
		return gzBzcd;
	}

	public void setGzBzcd(int gzBzcd) {
		this.gzBzcd = gzBzcd;
	}

	public int getRemocd() {
		return remocd;
	}

	public void setRemocd(int remocd) {
		this.remocd = remocd;
	}

}
