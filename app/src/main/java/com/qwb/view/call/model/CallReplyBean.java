package com.qwb.view.call.model;

import java.io.Serializable;

/**
 * 拜访-回复
 */
public class CallReplyBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7820095424931597110L;
	private String content;// 当voiceTime不为0评论内容也相当于语音url
	private int voiceTime;// 语音时长
	private int memberId;
	private int commentId;

	private int belongId;
	private int rcPosition;
	private String rcNm;
	private String memberNm;

	public int getRcPosition() {
		return rcPosition;
	}

	public void setRcPosition(int rcPosition) {
		this.rcPosition = rcPosition;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public int getBelongId() {
		return belongId;
	}

	public void setBelongId(int belongId) {
		this.belongId = belongId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRcNm() {
		return rcNm;
	}

	public void setRcNm(String rcNm) {
		this.rcNm = rcNm;
	}

	public String getMemberNm() {
		return memberNm;
	}

	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
	}

	public CallReplyBean() {
		super();
	}

	public int getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(int voiceTime) {
		this.voiceTime = voiceTime;
	}

}
