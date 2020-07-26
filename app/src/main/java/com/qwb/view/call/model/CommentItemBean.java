package com.qwb.view.call.model;

import java.io.Serializable;
import java.util.List;

public class CommentItemBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8241041056475492917L;
	private int voiceTime ;//语音时长
	private String content ;//当voiceTime不为0评论内容也相当于语音url
	private String memberNm ;
	private String rcNm ;
	private int commentId ;
	private int memberId ;
	private int belongId ;
	private int position ;//存储改数据在整个listview的位置
	private List<CallReplyBean> rcList ;//该条回复在回复列表的位置 模拟回复数据是插入改位置
	
	public List<CallReplyBean> getRcList() {
		return rcList;
	}
	public void setRcList(List<CallReplyBean> rcList) {
		this.rcList = rcList;
	}
	public String getRcNm() {
		return rcNm;
	}
	public void setRcNm(String rcNm) {
		this.rcNm = rcNm;
	}
	public int getBelongId() {
		return belongId;
	}
	public void setBelongId(int belongId) {
		this.belongId = belongId;
	}
	public String getMemberNm() {
		return memberNm;
	}
	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public int getVoiceTime() {
		return voiceTime;
	}
	public void setVoiceTime(int voiceTime) {
		this.voiceTime = voiceTime;
	}
}
