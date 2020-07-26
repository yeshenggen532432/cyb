package com.qwb.view.member.model;

import java.io.Serializable;

/**
 * 用户信息
 * @author yeshenggen
 *
 */
public class MemberBean  implements Serializable {
	private static final long serialVersionUID = 8811993693496555277L;
	
	private int memberId;//用户id
	private String memberHead;//用户头像
	private String memberNm;//用户名称
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
	public String getMemberHead() {
		return memberHead;
	}
	public void setMemberHead(String memberHead) {
		this.memberHead = memberHead;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	
	
}
