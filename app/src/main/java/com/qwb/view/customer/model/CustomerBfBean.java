package com.qwb.view.customer.model;

import com.qwb.view.base.model.BaseBean;

/**
 * 获取客户拜访信息（判断有没有上传资料）
 */
public class CustomerBfBean extends BaseBean{
	private String qddate;   //上次拜访日期
	private String bcbfzj;   //本次拜访总结
	private String dbsx;     //代办事项
	private String xxzt;     //新鲜度
	private int count1;   //拜访签到拍照（1已上传，0未上传）
	private int count2;   //生动化检查（1已上传，0未上传）
	private int count3;   //陈列检查采集（1已上传，0未上传）
	private int count4;   //销售小结（1已上传，0未上传）
	private int count5;   //供货下单（1已上传，0未上传）
	private int count6;   //道谢并告知下次拜访（1已上传，0未上传）

	public String getQddate() {
		return qddate;
	}
	public void setQddate(String qddate) {
		this.qddate = qddate;
	}
	public String getBcbfzj() {
		return bcbfzj;
	}
	public void setBcbfzj(String bcbfzj) {
		this.bcbfzj = bcbfzj;
	}
	public String getDbsx() {
		return dbsx;
	}
	public void setDbsx(String dbsx) {
		this.dbsx = dbsx;
	}
	public String getXxzt() {
		return xxzt;
	}
	public void setXxzt(String xxzt) {
		this.xxzt = xxzt;
	}

	public int getCount1() {
		return count1;
	}

	public void setCount1(int count1) {
		this.count1 = count1;
	}

	public int getCount2() {
		return count2;
	}

	public void setCount2(int count2) {
		this.count2 = count2;
	}

	public int getCount3() {
		return count3;
	}

	public void setCount3(int count3) {
		this.count3 = count3;
	}

	public int getCount4() {
		return count4;
	}

	public void setCount4(int count4) {
		this.count4 = count4;
	}

	public int getCount5() {
		return count5;
	}

	public void setCount5(int count5) {
		this.count5 = count5;
	}

	public int getCount6() {
		return count6;
	}

	public void setCount6(int count6) {
		this.count6 = count6;
	}
}
