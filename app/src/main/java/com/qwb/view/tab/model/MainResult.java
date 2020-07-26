package com.qwb.view.tab.model;

import com.qwb.view.base.model.BaseBean;
import java.util.List;

/**
 */
public class MainResult extends BaseBean {

	private String inputAmt;
	private String stkInAmt;
	private String stkOutAmt;
	private String posAmt;
	private String recAmt;
	private String payAmt;
	private List<MainFuncBean> funcList;

	public String getInputAmt() {
		return inputAmt;
	}

	public void setInputAmt(String inputAmt) {
		this.inputAmt = inputAmt;
	}

	public String getStkInAmt() {
		return stkInAmt;
	}

	public void setStkInAmt(String stkInAmt) {
		this.stkInAmt = stkInAmt;
	}

	public String getStkOutAmt() {
		return stkOutAmt;
	}

	public void setStkOutAmt(String stkOutAmt) {
		this.stkOutAmt = stkOutAmt;
	}

	public String getPosAmt() {
		return posAmt;
	}

	public void setPosAmt(String posAmt) {
		this.posAmt = posAmt;
	}

	public String getRecAmt() {
		return recAmt;
	}

	public void setRecAmt(String recAmt) {
		this.recAmt = recAmt;
	}

	public String getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(String payAmt) {
		this.payAmt = payAmt;
	}

	public List<MainFuncBean> getFuncList() {
		return funcList;
	}

	public void setFuncList(List<MainFuncBean> funcList) {
		this.funcList = funcList;
	}
}
