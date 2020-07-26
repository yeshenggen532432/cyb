package com.qwb.view.member.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BranchBean  implements Serializable {

	private static final long serialVersionUID = 1168052805635874319L;
	private Integer branchId;
	private String branchName;
	private List<MemberBean> memls2 = new ArrayList<>();
	
	public Integer getBranchId() {
		return branchId;
	}
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public List<MemberBean> getMemls2() {
		return memls2;
	}
	public void setMemls2(List<MemberBean> memls2) {
		this.memls2 = memls2;
	}
		
}
