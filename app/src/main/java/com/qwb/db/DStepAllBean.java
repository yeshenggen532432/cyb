package com.qwb.db;


import java.util.List;

/**
 *  拼接所有拜访步骤
 */
public class DStepAllBean{
	private String userId;//用户id
	private String companyId;//公司id
	private String cid;//客户id
	private String time;
	private String khNm;
	private List<PicBean> picList;

	private DStep1Bean dStep1Bean;
	private DStep2Bean dStep2Bean;
	private DStep3Bean dStep3Bean;
	private DStep6Bean dStep6Bean;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getKhNm() {
		return khNm;
	}

	public void setKhNm(String khNm) {
		this.khNm = khNm;
	}


	public List<PicBean> getPicList() {
		return picList;
	}

	public void setPicList(List<PicBean> picList) {
		this.picList = picList;
	}

	public DStep1Bean getdStep1Bean() {
		return dStep1Bean;
	}

	public void setdStep1Bean(DStep1Bean dStep1Bean) {
		this.dStep1Bean = dStep1Bean;
	}

	public DStep2Bean getdStep2Bean() {
		return dStep2Bean;
	}

	public void setdStep2Bean(DStep2Bean dStep2Bean) {
		this.dStep2Bean = dStep2Bean;
	}

	public DStep3Bean getdStep3Bean() {
		return dStep3Bean;
	}

	public void setdStep3Bean(DStep3Bean dStep3Bean) {
		this.dStep3Bean = dStep3Bean;
	}

	public DStep6Bean getdStep6Bean() {
		return dStep6Bean;
	}

	public void setdStep6Bean(DStep6Bean dStep6Bean) {
		this.dStep6Bean = dStep6Bean;
	}

	public static class PicBean{
		private String name;
		private String pic;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPic() {
			return pic;
		}

		public void setPic(String pic) {
			this.pic = pic;
		}
	}





}
