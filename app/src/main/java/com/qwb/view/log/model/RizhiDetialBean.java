package com.qwb.view.log.model;

import com.qwb.view.base.model.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RizhiDetialBean extends BaseBean {
	
	private static final long serialVersionUID = 3938579579139927134L;
	private String gzNr;// 标题
	private String gzZj;
	private String gzJh;
	private String gzBz;
	private String remo;
	private String address;
	private String fileNms;//附件
	private List<List1> list1 = new ArrayList<>();
	private List<List2> list2 = new ArrayList<>();
	private List<List3> list3 = new ArrayList<>();
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFileNms() {
		return fileNms;
	}

	public void setFileNms(String fileNms) {
		this.fileNms = fileNms;
	}

	public String getGzNr() {
		return gzNr;
	}

	public void setGzNr(String gzNr) {
		this.gzNr = gzNr;
	}

	public String getGzZj() {
		return gzZj;
	}

	public void setGzZj(String gzZj) {
		this.gzZj = gzZj;
	}

	public String getGzJh() {
		return gzJh;
	}

	public void setGzJh(String gzJh) {
		this.gzJh = gzJh;
	}

	public String getGzBz() {
		return gzBz;
	}

	public void setGzBz(String gzBz) {
		this.gzBz = gzBz;
	}

	public String getRemo() {
		return remo;
	}

	public void setRemo(String remo) {
		this.remo = remo;
	}

	public List<List1> getList1() {
		return list1;
	}

	public void setList1(List<List1> list1) {
		this.list1 = list1;
	}

	public List<List2> getList2() {
		return list2;
	}

	public void setList2(List<List2> list2) {
		this.list2 = list2;
	}

	public List<List3> getList3() {
		return list3;
	}

	public void setList3(List<List3> list3) {
		this.list3 = list3;
	}

	public class List1 implements Serializable {
		private String memberHead;
		private String memberNm;
		private int jsMid;

		public String getMemberHead() {
			return memberHead;
		}

		public void setMemberHead(String memberHead) {
			this.memberHead = memberHead;
		}

		public String getMemberNm() {
			return memberNm;
		}

		public void setMemberNm(String memberNm) {
			this.memberNm = memberNm;
		}

		public int getJsMid() {
			return jsMid;
		}

		public void setJsMid(int jsMid) {
			this.jsMid = jsMid;
		}

	}

	public class List2 implements Serializable {
		private String picMini;
		private String pic;
		private int bid;

		public String getPicMini() {
			return picMini;
		}

		public void setPicMini(String picMini) {
			this.picMini = picMini;
		}

		public String getPic() {
			return pic;
		}

		public void setPic(String pic) {
			this.pic = pic;
		}

		public int getBid() {
			return bid;
		}

		public void setBid(int bid) {
			this.bid = bid;
		}

	}

	public class List3 implements Serializable {
		// private String picMini ;
		// private String pic ;
		// private int bid;
	}

}
