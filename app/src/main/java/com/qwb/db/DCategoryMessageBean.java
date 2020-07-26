package com.qwb.db;

import org.litepal.crud.LitePalSupport;

/**
 * 备注以D开头的是数据库相关类
 * 用于首页的tab红点，消息分类红点，点击tab,分类马上红点消失
 */
public class DCategoryMessageBean extends LitePalSupport {
	private String companyId ;//公司id
	private String userId ;//用户id
	private Integer bankuai ;//模块id
	private Integer count;

	//bankuai:(-1.总消息；1.系统通知；2.审批;3.易办事；4.拜访查询-评论；6.沟通；10.日志-工作汇报；11.商城）


	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getBankuai() {
		return bankuai;
	}

	public void setBankuai(Integer bankuai) {
		this.bankuai = bankuai;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
