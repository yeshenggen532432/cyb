package com.qwb.view.audit.model;

import com.qwb.view.base.model.BaseBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户列表
 */
public class AccountListBean extends BaseBean{

	private List<AccountBean> list;

	public List<AccountBean> getList() {
		return list;
	}

	public void setList(List<AccountBean> list) {
		this.list = list;
	}

	public static class AccountBean{
		private Integer id;
		private String bankName;//银行名称
		private String accNo;//账号
		private BigDecimal accAmt;//账户余额
		private Integer accType;//0 现金 1微信 2支付宝 3银行
		private Integer status;//状态
		private String remarks;
		private String isPost;//过账账户
		private String isPay;//付款账账户
		private String accTypeName;
		private String accName;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getBankName() {
			return bankName;
		}

		public void setBankName(String bankName) {
			this.bankName = bankName;
		}

		public String getAccNo() {
			return accNo;
		}

		public void setAccNo(String accNo) {
			this.accNo = accNo;
		}

		public BigDecimal getAccAmt() {
			return accAmt;
		}

		public void setAccAmt(BigDecimal accAmt) {
			this.accAmt = accAmt;
		}

		public Integer getAccType() {
			return accType;
		}

		public void setAccType(Integer accType) {
			this.accType = accType;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public String getIsPost() {
			return isPost;
		}

		public void setIsPost(String isPost) {
			this.isPost = isPost;
		}

		public String getIsPay() {
			return isPay;
		}

		public void setIsPay(String isPay) {
			this.isPay = isPay;
		}

		public String getAccTypeName() {
			return accTypeName;
		}

		public void setAccTypeName(String accTypeName) {
			this.accTypeName = accTypeName;
		}

		public String getAccName() {
			return accName;
		}

		public void setAccName(String accName) {
			this.accName = accName;
		}
	}

}
