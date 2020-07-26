package com.qwb.view.audit.model;

import com.qwb.view.base.model.BaseBean;

/**
 * 审批模板
 */
public class AuditModelResult extends BaseBean{

	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data{
		private Integer id;//自定义id
		private String name;//名称
		private String tp;//1.类型 2.时间 3.详情 4.备注 5.金额 6.对象 7.账户 8.关联单
		private String detailName;//详情
		private String amountName;//金额
		private String typeName;//类型
		private String objectName;//对象
		private String remarkName;//备注
		private String accountName;//账号
		private String isNormal;//是否系统默认

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTp() {
			return tp;
		}

		public void setTp(String tp) {
			this.tp = tp;
		}

		public String getDetailName() {
			return detailName;
		}

		public void setDetailName(String detailName) {
			this.detailName = detailName;
		}

		public String getAmountName() {
			return amountName;
		}

		public void setAmountName(String amountName) {
			this.amountName = amountName;
		}

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public String getObjectName() {
			return objectName;
		}

		public void setObjectName(String objectName) {
			this.objectName = objectName;
		}

		public String getRemarkName() {
			return remarkName;
		}

		public void setRemarkName(String remarkName) {
			this.remarkName = remarkName;
		}

		public String getAccountName() {
			return accountName;
		}

		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}

		public String getIsNormal() {
			return isNormal;
		}

		public void setIsNormal(String isNormal) {
			this.isNormal = isNormal;
		}
	}

}
