package com.qwb.view.tab.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.List;

public class MsgBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8476757199233346306L;
	private List<MsgItemBean> msgList;

	public List<MsgItemBean> getMsgList() {
		return msgList;
	}

	public void setMsgList(List<MsgItemBean> msgList) {
		this.msgList = msgList;
	}

	public static class MsgItemBean implements Serializable {


//		 "id": 6565,
//         "memberNm": "小林",
//         "msg": "评论你的拜访",
//         "tp": "43",
//         "memberId": 182,
//         "belongId": 263,
//         "belongNm": "",
//         "memberHead": "publicplat/member/1495187532782.jpg",
//         "addtime": "2017-09-04 09:22:58",
//         "belongMsg": "",
//         "msgTp": ""

		private static final long serialVersionUID = 4977900305556754886L;
		private int id;
		private int receiveId;// 接受方的id
		private int msgId;//对应存完整数据表的msgId
		private String msg;// 消息内容
		private String msgTp;// 消息的类型 1文字 2图片 3语音
		private String voiceTime;// 录音的时间
		private int memberId;
		private String memberNm;// 发送消息的人的名字
		private String memberHead;// 发送消息的人的头像
		private int belongId;// 对应id
		private String belongNm;//对应名称
		private String belongMsg;//对应更多信息（如圈聊天存放圈头像）
		private String longitude;
		private String latitude;
		private String addtime;// 消息的发送时间

		private String picUrl;// 聊天页面查看图片的时候用到
		private int isact;// 该条未读消息是否可操作
		private boolean isNeedSend;// 该条消息是需要发送
		private boolean isSendIng;// 该条消息是否发送完成（发送耗时没有完成之前有进度条提示）
		private boolean isSendFail;// 该条消息是否发送失败

		// 消息类型：
		// 1 添加圈主题     2 圈评论      3 圈点赞        4 添加照片墙    5 照片墙评论
		// 6 照片墙点赞     7 @人         8 加入退出圈请求 9 圈聊天      10催办
		// 11进度          12 企业公告    13 系统公告     14 部门聊天    15 私信聊
		// 16 邀请同事加入
		private String tp;

		public String getLongitude() {
			return longitude;
		}

		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		public String getLatitude() {
			return latitude;
		}

		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}

		public String getPicUrl() {
			return picUrl;
		}

		public void setPicUrl(String picUrl) {
			this.picUrl = picUrl;
		}

		public int getIsact() {
			return isact;
		}

		public void setIsact(int isact) {
			this.isact = isact;
		}

		public boolean isSendIng() {
			return isSendIng;
		}

		public void setSendIng(boolean isSendIng) {
			this.isSendIng = isSendIng;
		}

		public boolean isSendFail() {
			return isSendFail;
		}

		public void setSendFail(boolean isSendFail) {
			this.isSendFail = isSendFail;
		}

		public String getBelongNm() {
			return belongNm;
		}

		public boolean isNeedSend() {
			return isNeedSend;
		}

		public void setNeedSend(boolean isNeedSend) {
			this.isNeedSend = isNeedSend;
		}

		public void setBelongNm(String belongNm) {
			this.belongNm = belongNm;
		}

		public String getBelongMsg() {
			return belongMsg;
		}

		public void setBelongMsg(String belongMsg) {
			this.belongMsg = belongMsg;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
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

		public String getAddtime() {
			return addtime;
		}

		public void setAddtime(String addtime) {
			this.addtime = addtime;
		}

		public String getTp() {
			return tp;
		}

		public void setTp(String tp) {
			this.tp = tp;
		}

		public String getVoiceTime() {
			return voiceTime;
		}

		public void setVoiceTime(String voiceTime) {
			this.voiceTime = voiceTime;
		}

		public String getMsgTp() {
			return msgTp;
		}

		public void setMsgTp(String msgTp) {
			this.msgTp = msgTp;
		}

		public int getReceiveId() {
			return receiveId;
		}

		public void setReceiveId(int receiveId) {
			this.receiveId = receiveId;
		}

		public int getBelongId() {
			return belongId;
		}

		public void setBelongId(int belongId) {
			this.belongId = belongId;
		}

		public int getMsgId() {
			return msgId;
		}

		public void setMsgId(int msgId) {
			this.msgId = msgId;
		}

		public int getMemberId() {
			return memberId;
		}

		public void setMemberId(int memberId) {
			this.memberId = memberId;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return "MsgItemBean [msg=" + msg + ", msgId=" + msgId + ", msgTp=" + msgTp + ", voiceTime=" + voiceTime
					+ "]";
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

		//bankuai:(1.系统通知；2.审批;3.易办事；4.拜访查询-评论；6.沟通；10.日志-工作汇报；11.商城）
	}
}
