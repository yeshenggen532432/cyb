package com.qwb.db;

import org.litepal.crud.LitePalSupport;

/**
 * 备注以D开头的是数据库相关类
 */
public class DMessageBean extends LitePalSupport {
	private String companyId ;//公司id
	private String userId ;//用户id
	private Integer bankuai ;//模块id
	private Integer isNeedClean ;//是否需要清除
	private String isRead ;//0：未读；1：已读

	private Integer memberId ;//// 发送消息的人的id
	private String memberName ;// 发送消息的人的名字
	private String memberHead ;// 发送消息的人的头像
	private Integer belongId ;//// 对应id
	private String belongName;//对应名称
	private String belongMsg ;//对应更多信息

	private String type ;
	private String msg ;// 消息内容
	private String addTime ;
	private String msgTp ;// 消息的类型 1文字 2图片 3语音
	private String mark ;

	private Integer receiveId;// 接受方的id
	private Integer isact ;//"同意"// 该条未读消息是否可操作

	private Integer mainId;//主id
	private Integer msgId;//对应存完整数据表的msgId

	//分类用的实际没有数据start
	private Integer count;
	private Integer imgResId;
	private String title;
	//分类用的实际没有数据end

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getImgResId() {
		return imgResId;
	}

	public void setImgResId(Integer imgResId) {
		this.imgResId = imgResId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

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

	public Integer getIsNeedClean() {
		return isNeedClean;
	}

	public void setIsNeedClean(Integer isNeedClean) {
		this.isNeedClean = isNeedClean;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberHead() {
		return memberHead;
	}

	public void setMemberHead(String memberHead) {
		this.memberHead = memberHead;
	}

	public Integer getBelongId() {
		return belongId;
	}

	public void setBelongId(Integer belongId) {
		this.belongId = belongId;
	}

	public String getBelongName() {
		return belongName;
	}

	public void setBelongName(String belongName) {
		this.belongName = belongName;
	}

	public String getBelongMsg() {
		return belongMsg;
	}

	public void setBelongMsg(String belongMsg) {
		this.belongMsg = belongMsg;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getMsgTp() {
		return msgTp;
	}

	public void setMsgTp(String msgTp) {
		this.msgTp = msgTp;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Integer getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(Integer receiveId) {
		this.receiveId = receiveId;
	}

	public Integer getIsact() {
		return isact;
	}

	public void setIsact(Integer isact) {
		this.isact = isact;
	}

	public Integer getMainId() {
		return mainId;
	}

	public void setMainId(Integer mainId) {
		this.mainId = mainId;
	}

	public Integer getMsgId() {
		return msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	//消息类型说明
	// 1  添加圈主题
	// 2  圈评论
	// 3  圈点赞
	// 4  添加照片墙
	// 5  照片墙评论
	// 6  照片墙点赞
	// 7  @人
	// 8  加入退出圈请求
	// 9  圈聊天
	// 10 催办
	// 11 进度
	// 12 企业公告
	// 13 系统公告
	// 14 部门聊天
	// 15 私信聊
	// 16 邀请同事加入公司
	// 17 真心话（全公司聊天）
	// 18 邀请到部门
	// 19 好友申请
	// 20 同意或不同意好友请求
	// 21 同意或不同意组织(公司)邀请
	// 22 群主同意或不同意成员加入圈
	// 23 成员退出圈通知群主
	// 24 群主解散圈通知圈成员
	// 25 群主或管理员剔除圈成员
	// 26 申请加入公司
	// 27 同意或不同意公司请求
	// 28 被移除公司的通知消息
	// 29 购开心
	// 30 版本更新推送
	// 31 审批
	// 32 计划拜访
	// 34 日志
	// 40 日志-日报，周报，月报评论
	// 41 转让客户
	// 42 转让客户
	// 43 拜访查询--评论
	//100 商城订单

	//bankuai:(1.系统通知；2.审批;3.易办事；4.拜访查询-评论；6.沟通；10.日志-工作汇报；11.商城；12.公告）



}
