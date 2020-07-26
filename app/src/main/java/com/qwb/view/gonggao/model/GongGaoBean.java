package com.qwb.view.gonggao.model;

/**
 * 公告
 */
public class GongGaoBean  {
	private String memberNm;
	private String noticeTitle;
	private String noticePic;
	private String noticeTime;
	private int noticeId;

	public String getMemberNm() {
		return memberNm;
	}

	public void setMemberNm(String memberNm) {
		this.memberNm = memberNm;
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticePic() {
		return noticePic;
	}

	public void setNoticePic(String noticePic) {
		this.noticePic = noticePic;
	}

	public String getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(String noticeTime) {
		this.noticeTime = noticeTime;
	}

	public int getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}
}
