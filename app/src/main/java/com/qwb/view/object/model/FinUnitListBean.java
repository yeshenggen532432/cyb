package com.qwb.view.object.model;

import com.qwb.view.base.model.BaseBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * 往来单位
 */
public class FinUnitListBean extends BaseBean {

	private List<FinUnitBean> list ;

	public List<FinUnitBean> getList() {
		return list;
	}

	public void setList(List<FinUnitBean> list) {
		this.list = list;
	}

	public static class FinUnitBean{
		private Integer id;
		private String proNo;//编号
		private String proName;//供应商名称
		private String address;//地址
		private String tel;//联系电话
		private String mobile;//手机
		private String contact;//联系人
		private String remarks;//备注
		private BigDecimal leftAmt;//欠款金额
		private String fbtime;//发布时间
		private Integer status;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getProNo() {
			return proNo;
		}

		public void setProNo(String proNo) {
			this.proNo = proNo;
		}

		public String getProName() {
			return proName;
		}

		public void setProName(String proName) {
			this.proName = proName;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getTel() {
			return tel;
		}

		public void setTel(String tel) {
			this.tel = tel;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getContact() {
			return contact;
		}

		public void setContact(String contact) {
			this.contact = contact;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public BigDecimal getLeftAmt() {
			return leftAmt;
		}

		public void setLeftAmt(BigDecimal leftAmt) {
			this.leftAmt = leftAmt;
		}

		public String getFbtime() {
			return fbtime;
		}

		public void setFbtime(String fbtime) {
			this.fbtime = fbtime;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}
	}
}
