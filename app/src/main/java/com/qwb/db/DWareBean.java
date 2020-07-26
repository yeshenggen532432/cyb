package com.qwb.db;

import org.litepal.crud.LitePalSupport;

/**
 *  缓存-商品
 */
public class DWareBean extends LitePalSupport {
	private long id;//自增id
	private String userId;//用户id
	private String companyId;//公司id
	private String pinyin;//汉字转拼音
	private String py;//简拼

	private int wareId; //商品id
	private String wareNm; //商品名称
	private String wareType; //商品类型
	private String hsNum ; //比例
	private String wareGg ; //规格
	private String wareDw ; //大单位
	private String minUnit ; //小单位
	private String wareDj; //大单位价格
	private String maxUnitCode ; //大单位代码
	private String minUnitCode;  //小单位代码
	private int sunitFront; //1:辅助单位

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public int getWareId() {
		return wareId;
	}

	public void setWareId(int wareId) {
		this.wareId = wareId;
	}

	public String getWareNm() {
		return wareNm;
	}

	public void setWareNm(String wareNm) {
		this.wareNm = wareNm;
	}

	public String getWareType() {
		return wareType;
	}

	public void setWareType(String wareType) {
		this.wareType = wareType;
	}

	public int getSunitFront() {
		return sunitFront;
	}

	public void setSunitFront(int sunitFront) {
		this.sunitFront = sunitFront;
	}

	public String getHsNum() {
		return hsNum;
	}

	public void setHsNum(String hsNum) {
		this.hsNum = hsNum;
	}

	public String getWareGg() {
		return wareGg;
	}

	public void setWareGg(String wareGg) {
		this.wareGg = wareGg;
	}


	public String getMinUnitCode() {
		return minUnitCode;
	}

	public void setMinUnitCode(String minUnitCode) {
		this.minUnitCode = minUnitCode;
	}

	public String getMaxUnitCode() {
		return maxUnitCode;
	}

	public void setMaxUnitCode(String maxUnitCode) {
		this.maxUnitCode = maxUnitCode;
	}

	public String getWareDw() {
		return wareDw;
	}

	public void setWareDw(String wareDw) {
		this.wareDw = wareDw;
	}

	public String getMinUnit() {
		return minUnit;
	}

	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}

	public String getWareDj() {
		return wareDj;
	}

	public void setWareDj(String wareDj) {
		this.wareDj = wareDj;
	}
}
