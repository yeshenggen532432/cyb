package com.qwb.view.storehouse.model;

import com.qwb.view.base.model.BaseBean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库位
 */
public class StorehouseBean extends BaseBean {

	private Integer id;
	private String houseName;//库位名称
	private Integer status;//状态 2禁用 1启用
	private String remarks;//备注
	private Integer isSelect;//  默认选中仓库 1:默认
	private Integer sort;//库位排序码

	private Integer stkId;//所属仓库ID
	private BigDecimal kwArea;//库位面积
	private BigDecimal kwVolume;//库位容量
	private String  kwBar;//库位条码
	private Integer kwSort;//库位排序码

	private Integer isTemp;//是否临时仓库 1:是
	private Date createTime;//创建时间
	private String stkName;//所属仓库名称
//	List<StkHouseWareVo> wareList;//商品集合


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(Integer isSelect) {
		this.isSelect = isSelect;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getStkId() {
		return stkId;
	}

	public void setStkId(Integer stkId) {
		this.stkId = stkId;
	}

	public BigDecimal getKwArea() {
		return kwArea;
	}

	public void setKwArea(BigDecimal kwArea) {
		this.kwArea = kwArea;
	}

	public BigDecimal getKwVolume() {
		return kwVolume;
	}

	public void setKwVolume(BigDecimal kwVolume) {
		this.kwVolume = kwVolume;
	}

	public String getKwBar() {
		return kwBar;
	}

	public void setKwBar(String kwBar) {
		this.kwBar = kwBar;
	}

	public Integer getKwSort() {
		return kwSort;
	}

	public void setKwSort(Integer kwSort) {
		this.kwSort = kwSort;
	}

	public Integer getIsTemp() {
		return isTemp;
	}

	public void setIsTemp(Integer isTemp) {
		this.isTemp = isTemp;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStkName() {
		return stkName;
	}

	public void setStkName(String stkName) {
		this.stkName = stkName;
	}
}
