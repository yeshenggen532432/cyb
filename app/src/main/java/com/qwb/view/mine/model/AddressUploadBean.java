package com.qwb.view.mine.model;

import com.qwb.view.base.model.BaseBean;

/**
 */
public class AddressUploadBean extends BaseBean {
	/**
	 * 备注：上传位置的逻辑
	 * 0）后台当设置“不上传”时，业务员自己设置不起作用；
	 * 1）后台当设置“手机控制”时，业务员自己可以设置上传还是不上传
	 * 2）后台当设置“上传”时，业务员自己设置不起作用
	 */
	private Integer id;
	private Integer memId;//员工id
	private Integer upload;//上传方式：0不上传，1.手机控制，2上传
	private Integer min;//上传间隔默认1分钟
	private Integer memUpload;//业务员自己修改上传方式：0不上传，1上传（默认）

	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer memId) {
		this.memId = memId;
	}

	public Integer getUpload() {
		return upload;
	}

	public void setUpload(Integer upload) {
		this.upload = upload;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMemUpload() {
		return memUpload;
	}

	public void setMemUpload(Integer memUpload) {
		this.memUpload = memUpload;
	}
}
