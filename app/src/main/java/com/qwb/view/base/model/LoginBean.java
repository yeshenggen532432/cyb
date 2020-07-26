package com.qwb.view.base.model;

import java.util.List;

public class LoginBean extends BaseBean{

//	{
//		"msg": "登录成功",
//			"isUnitmng": "",
//			"msgmodel": "2",
//			"tpNm": "快消",
//			"token": "6cd2e35aac15b907093a1652cc26ed83",
//			"companyId": 285,
//			"memberNm": "叶生根",
//			"memberHead": "publicplat/member/1524747171026.jpg",
//			"rzState": 0,
//			"datasource": "xmqwbwlkjyxgs285",
//			"companys": "[{\"companyId\":590,\"companyName\":\"小帅集团\"},{\"companyId\":139,\"companyName\":\"鹭百川\"},{\"companyId\":285,\"companyName\":\"厦门企微宝网络科技有限公司\"},{\"companyId\":137,\"companyName\":\"思迅网络科技\"}]",
//			"state": true,
//			"memberMobile": "13950104779",
//			"memId": 195,
//			"cid": 0
//	}

	private int memId ;//用户id
	private int companyId ;//公司id
	private String token ;
	private String memberNm ;//用户名称
	private String memberHead ;//用户头像
	private String memberMobile ;//手机号（账号）
	private String isUnitmng ;//权限
	private String datasource ;//数据库
	private String msgmodel ;
	private String tpNm ;//公司类型：快消，卖场
	private String companys ;//公司组
	private Integer rzState ;
	private boolean state ;

	private String jwt;
	private String domain;
	private List<CompanyBean> companies;

	public void setRzState(Integer rzState) {
		this.rzState = rzState;
	}


	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public List<CompanyBean> getCompanies() {
		return companies;
	}

	public void setCompanies(List<CompanyBean> companies) {
		this.companies = companies;
	}

	public String getCompanys() {
		return companys;
	}

	public void setCompanys(String companys) {
		this.companys = companys;
	}

	public int getMemId() {
		return memId;
	}

	public void setMemId(int memId) {
		this.memId = memId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMemberNm() {
		return memberNm;
	}

	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
	}

	public String getMemberHead() {
		return memberHead;
	}

	public void setMemberHead(String memberHead) {
		this.memberHead = memberHead;
	}

	public String getMemberMobile() {
		return memberMobile;
	}

	public void setMemberMobile(String memberMobile) {
		this.memberMobile = memberMobile;
	}

	public String getIsUnitmng() {
		return isUnitmng;
	}

	public void setIsUnitmng(String isUnitmng) {
		this.isUnitmng = isUnitmng;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getMsgmodel() {
		return msgmodel;
	}

	public void setMsgmodel(String msgmodel) {
		this.msgmodel = msgmodel;
	}

	public String getTpNm() {
		return tpNm;
	}

	public void setTpNm(String tpNm) {
		this.tpNm = tpNm;
	}

	public Integer getRzState() {
		return rzState;
	}

	public void setRzState(int rzState) {
		this.rzState = rzState;
	}

	@Override
	public boolean isState() {
		return state;
	}

	@Override
	public void setState(boolean state) {
		this.state = state;
	}
}
