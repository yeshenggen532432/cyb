package com.qwb.view.base.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *	应用列表
 */

public class ApplyBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5747050065823171203L;
	private boolean isMe = true;//是否是我的应用(默认true)
	private String isMeApply;//1: 默认菜单；；是否是"自定义管理"(默认false)
	private int meApplySort = 1000;//"自定义管理"排序(默认往后)
	public int sort;//排序
	public int count;//未读消息数量
	public int imgRes;//图片资源id
	
	private int id;
	private int applyNo;//应用排序
	private int PId;
	private String tp;
	private String menuTp;
	private String menuLeaf;
	public String applyName;//应用名称
	private String applyCode;
	private String applyIcon;
	private String applyDesc;
	private String applyIfwap;//应用类型 0 -原生 1-wap
	private String applyUrl;//h5跳转url
	private String createDate;
	private String createBy;
	private String isUse;
	private String dataTp;//数据类型 1 全部 2 部门及子部门 3 个人 4自定义
	private String menuNm;//
	private String sgtjz;//用来区分统计报表 1：业务拜访统计表，2：客户拜访统计表，3：产品订单统计表，4：销售订单统计表
	private String mids;//当dataTp=4时，返回自定义ids

	private List<ApplyBean> children = new ArrayList<>();//子菜单

	public ApplyBean() {
	}
	public ApplyBean(int meApplySort, String applyName) {
		this.meApplySort = meApplySort;
		this.applyName = applyName;
	}

	public ApplyBean(int sort, int imgRes, String applyCode, String applyName) {
		this.sort = sort;
		this.imgRes = imgRes;
		this.applyCode = applyCode;
		this.applyName = applyName;
	}
	public ApplyBean(int sort, int imgRes, String applyCode, String applyName, int count) {
		this.sort = sort;
		this.imgRes = imgRes;
		this.applyCode = applyCode;
		this.applyName = applyName;
		this.count = count;
	}
	public ApplyBean(int imgRes, String applyCode, String applyName, int count, int meApplySort) {
		this.meApplySort = meApplySort;
		this.imgRes = imgRes;
		this.applyCode = applyCode;
		this.applyName = applyName;
		this.count = count;
	}
	public ApplyBean(int imgRes, String applyCode, String applyName,int meApplySort) {
		this.meApplySort = meApplySort;
		this.imgRes = imgRes;
		this.applyCode = applyCode;
		this.applyName = applyName;
	}

	public String getIsMeApply() {
		return isMeApply;
	}

	public void setIsMeApply(String isMeApply) {
		this.isMeApply = isMeApply;
	}

	public Boolean getMe() {
		return isMe;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(int applyNo) {
		this.applyNo = applyNo;
	}
	public int getPId() {
		return PId;
	}
	public void setPId(int pId) {
		PId = pId;
	}
	public String getTp() {
		return tp;
	}
	public void setTp(String tp) {
		this.tp = tp;
	}
	public String getMenuTp() {
		return menuTp;
	}
	public void setMenuTp(String menuTp) {
		this.menuTp = menuTp;
	}
	public String getMenuLeaf() {
		return menuLeaf;
	}
	public void setMenuLeaf(String menuLeaf) {
		this.menuLeaf = menuLeaf;
	}
	public String getApplyName() {
		return applyName;
	}
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	public String getApplyCode() {
		return applyCode;
	}
	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}
	public String getApplyIcon() {
		return applyIcon;
	}
	public void setApplyIcon(String applyIcon) {
		this.applyIcon = applyIcon;
	}
	public String getApplyDesc() {
		return applyDesc;
	}
	public void setApplyDesc(String applyDesc) {
		this.applyDesc = applyDesc;
	}
	public String getApplyIfwap() {
		return applyIfwap;
	}
	public void setApplyIfwap(String applyIfwap) {
		this.applyIfwap = applyIfwap;
	}
	public String getApplyUrl() {
		return applyUrl;
	}
	public void setApplyUrl(String applyUrl) {
		this.applyUrl = applyUrl;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getDataTp() {
		return dataTp;
	}
	public void setDataTp(String dataTp) {
		this.dataTp = dataTp;
	}
	public String getMenuNm() {
		return menuNm;
	}
	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
	}
	public String getSgtjz() {
		return sgtjz;
	}
	public void setSgtjz(String sgtjz) {
		this.sgtjz = sgtjz;
	}
	public String getMids() {
		return mids;
	}
	public void setMids(String mids) {
		this.mids = mids;
	}

	public boolean isMe() {
		return isMe;
	}

	public void setMe(boolean me) {
		isMe = me;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getImgRes() {
		return imgRes;
	}

	public void setImgRes(int imgRes) {
		this.imgRes = imgRes;
	}

	public int getMeApplySort() {
		return meApplySort;
	}

	public void setMeApplySort(int meApplySort) {
		this.meApplySort = meApplySort;
	}

	public List<ApplyBean> getChildren() {
		return children;
	}

	public void setChildren(List<ApplyBean> children) {
		this.children = children;
	}
}
