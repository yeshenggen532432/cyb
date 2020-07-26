package com.qwb.view.member.model;

import com.qwb.view.base.model.BaseBean;
import java.util.List;

/**
 * 用户信息
 */
public class MemberListBean extends BaseBean {
	private List<MemberBean> list;

	public List<MemberBean> getList() {
		return list;
	}

	public void setList(List<MemberBean> list) {
		this.list = list;
	}

	public static class MemberBean{
		/**
		 * 主键自增id
		 */
		private Integer memberId;
		/**
		 *  账号
		 */
		private String memberNm;
		/**
		 * 姓名
		 */
		private String memberName;
		/**
		 * 性别 1：男 2：女
		 */
		private String sex;
		/**
		 *  成员号码
		 */
		private String memberMobile;
		/**
		 * 成员密码
		 */
		private String memberPwd;
		/**
		 * 邮箱
		 */
		private String email;
		/**
		 * 职业
		 */
		private String memberJob;
		/**
		 * 行业
		 */
		private String memberTrade;
		/**
		 *  头像
		 */
		private String memberHead;
		/**
		 * 粉丝数
		 */
		private Integer memberFans;
		/**
		 *  好友
		 */
		private Integer memberAttentions;
		/**
		 * 黑名单数
		 */
		private Integer memberBlacklist;
		/**
		 * 家乡member_hometown
		 */
		private String memberHometown;
		/**
		 * 毕业院校
		 */
		private String memberGraduated;
		/**
		 * 公司
		 */
		private String memberCompany;
		/**
		 * 简介
		 */
		private String memberDesc;
		/**
		 * 激活状态 1:激活2：未激活
		 */
		private String memberActivate;
		/**
		 * 激活时间
		 */
		private String memberActivatime;
		/**
		 * 使用状态 1:使用2：禁用
		 */
		private String memberUse;
		/**
		 * 创建人
		 */
		private Integer memberCreator;
		/**
		 * 创建时间
		 */
		private String memberCreatime;
		/**
		 * 最后一次登录时间
		 */
		private String memberLogintime;
		/**
		 * 登录次数
		 */
		private Integer memberLoginnum;
		/**
		 * 短信验证码
		 */
		private String smsNo;
		/**
		 * 是否超级管理员:1.是0.否
		 */
		private String isAdmin;
		/**
		 * 所属单位
		 */
		private Integer unitId;
		/**
		 *
		 * 成员类型:0.普通成员1.单位超级管理员 2.单位管理员 3.部门管理员
		 */
		@Deprecated
		private String isUnitmng;
		/**
		 * 首字母
		 */
		private String firstChar;
		/**
		 * 所属分组ID
		 */
		private Integer branchId;
		/**
		 * 积分
		 */
		private Integer score;
		/**
		 * 是否领导1，是；2，否。
		 */
		private String isLead;
		/**
		 * 免打扰状态 1：是 2：否
		 */

		private String state;

		/**
		 * 标记是否删除  0：否 1：是
		 */
		private String isDel;

		private Integer cid;//客户id

		private Integer useDog;//使用软件狗否0不使用 1使用
		private String idKey;//软件狗的系列号
		//消息模板
		private String msgmodel;//1 默认乱序 2 模块化
		private String unId;//唯一码
		///不在数据库
		private String datasource;
		private String branchName; //分组名称
		private String oldtel;
		private String memberIds;
		private String roleIds;//角色id，多个以“，”隔开
		private String roleNames;
		private String platformCompanyId;//新平台唯一标识id
		private Integer upload;//位置上传方式 0：不上传；1：上传
		private Integer min;//位置上传间隔（单位分）
		private Integer memUpload;//业务员自己修改上传方式：0不上传，1上传(默认)
		private Integer isCustomerService;//0不是客服，1是客服
		private String rzMobile;//认证手机
		private Integer rzState;//认证手机状态 0:未认证；1：已认证

		public Integer getMemberId() {
			return memberId;
		}

		public void setMemberId(Integer memberId) {
			this.memberId = memberId;
		}

		public String getMemberNm() {
			return memberNm;
		}

		public void setMemberNm(String memberNm) {
			this.memberNm = memberNm;
		}

		public String getMemberName() {
			return memberName;
		}

		public void setMemberName(String memberName) {
			this.memberName = memberName;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public String getMemberMobile() {
			return memberMobile;
		}

		public void setMemberMobile(String memberMobile) {
			this.memberMobile = memberMobile;
		}

		public String getMemberPwd() {
			return memberPwd;
		}

		public void setMemberPwd(String memberPwd) {
			this.memberPwd = memberPwd;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getMemberJob() {
			return memberJob;
		}

		public void setMemberJob(String memberJob) {
			this.memberJob = memberJob;
		}

		public String getMemberTrade() {
			return memberTrade;
		}

		public void setMemberTrade(String memberTrade) {
			this.memberTrade = memberTrade;
		}

		public String getMemberHead() {
			return memberHead;
		}

		public void setMemberHead(String memberHead) {
			this.memberHead = memberHead;
		}

		public Integer getMemberFans() {
			return memberFans;
		}

		public void setMemberFans(Integer memberFans) {
			this.memberFans = memberFans;
		}

		public Integer getMemberAttentions() {
			return memberAttentions;
		}

		public void setMemberAttentions(Integer memberAttentions) {
			this.memberAttentions = memberAttentions;
		}

		public Integer getMemberBlacklist() {
			return memberBlacklist;
		}

		public void setMemberBlacklist(Integer memberBlacklist) {
			this.memberBlacklist = memberBlacklist;
		}

		public String getMemberHometown() {
			return memberHometown;
		}

		public void setMemberHometown(String memberHometown) {
			this.memberHometown = memberHometown;
		}

		public String getMemberGraduated() {
			return memberGraduated;
		}

		public void setMemberGraduated(String memberGraduated) {
			this.memberGraduated = memberGraduated;
		}

		public String getMemberCompany() {
			return memberCompany;
		}

		public void setMemberCompany(String memberCompany) {
			this.memberCompany = memberCompany;
		}

		public String getMemberDesc() {
			return memberDesc;
		}

		public void setMemberDesc(String memberDesc) {
			this.memberDesc = memberDesc;
		}

		public String getMemberActivate() {
			return memberActivate;
		}

		public void setMemberActivate(String memberActivate) {
			this.memberActivate = memberActivate;
		}

		public String getMemberActivatime() {
			return memberActivatime;
		}

		public void setMemberActivatime(String memberActivatime) {
			this.memberActivatime = memberActivatime;
		}

		public String getMemberUse() {
			return memberUse;
		}

		public void setMemberUse(String memberUse) {
			this.memberUse = memberUse;
		}

		public Integer getMemberCreator() {
			return memberCreator;
		}

		public void setMemberCreator(Integer memberCreator) {
			this.memberCreator = memberCreator;
		}

		public String getMemberCreatime() {
			return memberCreatime;
		}

		public void setMemberCreatime(String memberCreatime) {
			this.memberCreatime = memberCreatime;
		}

		public String getMemberLogintime() {
			return memberLogintime;
		}

		public void setMemberLogintime(String memberLogintime) {
			this.memberLogintime = memberLogintime;
		}

		public Integer getMemberLoginnum() {
			return memberLoginnum;
		}

		public void setMemberLoginnum(Integer memberLoginnum) {
			this.memberLoginnum = memberLoginnum;
		}

		public String getSmsNo() {
			return smsNo;
		}

		public void setSmsNo(String smsNo) {
			this.smsNo = smsNo;
		}

		public String getIsAdmin() {
			return isAdmin;
		}

		public void setIsAdmin(String isAdmin) {
			this.isAdmin = isAdmin;
		}

		public Integer getUnitId() {
			return unitId;
		}

		public void setUnitId(Integer unitId) {
			this.unitId = unitId;
		}

		public String getIsUnitmng() {
			return isUnitmng;
		}

		public void setIsUnitmng(String isUnitmng) {
			this.isUnitmng = isUnitmng;
		}

		public String getFirstChar() {
			return firstChar;
		}

		public void setFirstChar(String firstChar) {
			this.firstChar = firstChar;
		}

		public Integer getBranchId() {
			return branchId;
		}

		public void setBranchId(Integer branchId) {
			this.branchId = branchId;
		}

		public Integer getScore() {
			return score;
		}

		public void setScore(Integer score) {
			this.score = score;
		}

		public String getIsLead() {
			return isLead;
		}

		public void setIsLead(String isLead) {
			this.isLead = isLead;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getIsDel() {
			return isDel;
		}

		public void setIsDel(String isDel) {
			this.isDel = isDel;
		}

		public Integer getCid() {
			return cid;
		}

		public void setCid(Integer cid) {
			this.cid = cid;
		}

		public Integer getUseDog() {
			return useDog;
		}

		public void setUseDog(Integer useDog) {
			this.useDog = useDog;
		}

		public String getIdKey() {
			return idKey;
		}

		public void setIdKey(String idKey) {
			this.idKey = idKey;
		}

		public String getMsgmodel() {
			return msgmodel;
		}

		public void setMsgmodel(String msgmodel) {
			this.msgmodel = msgmodel;
		}

		public String getUnId() {
			return unId;
		}

		public void setUnId(String unId) {
			this.unId = unId;
		}

		public String getDatasource() {
			return datasource;
		}

		public void setDatasource(String datasource) {
			this.datasource = datasource;
		}

		public String getBranchName() {
			return branchName;
		}

		public void setBranchName(String branchName) {
			this.branchName = branchName;
		}

		public String getOldtel() {
			return oldtel;
		}

		public void setOldtel(String oldtel) {
			this.oldtel = oldtel;
		}

		public String getMemberIds() {
			return memberIds;
		}

		public void setMemberIds(String memberIds) {
			this.memberIds = memberIds;
		}

		public String getRoleIds() {
			return roleIds;
		}

		public void setRoleIds(String roleIds) {
			this.roleIds = roleIds;
		}

		public String getRoleNames() {
			return roleNames;
		}

		public void setRoleNames(String roleNames) {
			this.roleNames = roleNames;
		}

		public String getPlatformCompanyId() {
			return platformCompanyId;
		}

		public void setPlatformCompanyId(String platformCompanyId) {
			this.platformCompanyId = platformCompanyId;
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

		public Integer getIsCustomerService() {
			return isCustomerService;
		}

		public void setIsCustomerService(Integer isCustomerService) {
			this.isCustomerService = isCustomerService;
		}

		public String getRzMobile() {
			return rzMobile;
		}

		public void setRzMobile(String rzMobile) {
			this.rzMobile = rzMobile;
		}

		public Integer getRzState() {
			return rzState;
		}

		public void setRzState(Integer rzState) {
			this.rzState = rzState;
		}
	}
	
	
}
