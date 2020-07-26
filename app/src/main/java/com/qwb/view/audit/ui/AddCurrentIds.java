package com.qwb.view.audit.ui;

import android.content.ContentValues;
import android.database.Cursor;

import com.qwb.application.MyApp;
import com.qwb.view.member.model.BuMenListBean.MemberBean;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *  审批模块：添加审批人时中转用
 * */
public class AddCurrentIds {

	public static final int TYPE_QJ = 1;//请假
	public static final int TYPE_BX = 2;//报销
	public static final int TYPE_CC = 3;//出差
	public static final int TYPE_LY = 4;//物品领用
	public static final int TYPE_TY = 5;//通用审批
	public static final int TYPE_ZDY = 6;//自定义的
	public static final int TYPE_LOG = 7;//日志模块
	public static final int TYPE_LOG_RB = 10;//日志模块--日报
	public static final int TYPE_LOG_ZB = 11;//日志模块--周报
	public static final int TYPE_LOG_YB = 12;//日志模块--月报
	public static final int TYPE_LOG_BIAOBIAO = 8;//日志模块--报表 --选择发送人
	public static final int TYPE_EXEC = 13;//执行人
	public static final int TYPE_APPROVER = 14;//最终审批人

	private AddCurrentIds() {
	}

	/**
	 * 已经选的审批人
	 * */
	private List<MemberBean> idsMap = new ArrayList<MemberBean>();
	private static AddCurrentIds I;

	public static AddCurrentIds getI() {
		if (I == null) {
			I = new AddCurrentIds();
		}
		return I;
	}

	/**
	 * 从数据库获取对应模块的审批人 type 1 请假 2报销 3出差 4物品领用 5通用审批 6：自定义审批模板   7：日志模块          isFirst 是否是初次查询（初次查询要从数据库获取）
	 * */
	public List<MemberBean> getChoiseIds(int type, boolean isFirst) {
		if (isFirst) {
			idsMap.clear();
			Cursor cursor = MyApp.getDB().query(
					"other",null,"mark=? and userid=? and companyId=?",
					new String[] { String.valueOf(type),SPUtils.getID(), SPUtils.getCompanyId() }, null, null, null);
			boolean moveToFirst = cursor.moveToFirst();
			while (moveToFirst) {
				String head = MyUtils.getSFromCursor(cursor, "val");
				String name = MyUtils.getSFromCursor(cursor, "rememberpsw");
				int id = MyUtils.getIFromCursor(cursor, "num");
				MemberBean memberBean = new MemberBean();
				memberBean.setMemberHead(head);
				memberBean.setMemberId(id);
				memberBean.setMemberNm(name);
				idsMap.add(memberBean);
				moveToFirst = cursor.moveToNext();
			}
			cursor.close();
		}
		return idsMap;
	}

	/**
	 * 移除对应的缓存id容器 type 1 请假 2报销 3出差 4物品领用 5通用审批
	 * */
	public void removeId(int id) {
		for (int i = 0; i < idsMap.size(); i++) {
			MemberBean memberBean = idsMap.get(i);
			int memberId = memberBean.getMemberId();
			if (memberId == id) {
				idsMap.remove(memberBean);
				break;
			}
		}
	}

	/**
	 * 存到数据库：自定义审批模板（修改）
	 * */
	public void saveToDB(int type,List<MemberBean> idsMap) {
		if(idsMap == null){
			return;
		}
		// 先删掉原有的
		MyApp.getDB().delete("other","userid=? and companyId=? and mark=?",
				new String[] { SPUtils.getID(), SPUtils.getCompanyId(), String.valueOf(type) });
		for (int i = 0; i < idsMap.size(); i++) {
			MemberBean memberBean = idsMap.get(i);
			ContentValues values3 = new ContentValues();
			// 头像
			values3.put("mark", String.valueOf(type));
			values3.put("userid", SPUtils.getID());
			values3.put("companyId", SPUtils.getCompanyId());
			values3.put("val", memberBean.getMemberHead());
			// 名字
			values3.put("rememberpsw", memberBean.getMemberNm());
			// id
			values3.put("num", memberBean.getMemberId());
			MyApp.getDB().insert("other", null, values3);
		}
	}
	/**
	 * 存到数据库
	 * */
	public void saveToDB(int type) {
		// 先删掉原有的
		MyApp.getDB().delete("other","userid=? and companyId=? and mark=?",
				new String[] { SPUtils.getID(),SPUtils.getCompanyId(), String.valueOf(type) });
		for (int i = 0; i < idsMap.size(); i++) {
			MemberBean memberBean = idsMap.get(i);
			ContentValues values3 = new ContentValues();
			// 头像
			values3.put("mark", String.valueOf(type));
			values3.put("userid", SPUtils.getID());
			values3.put("companyId", SPUtils.getCompanyId());
			values3.put("val", memberBean.getMemberHead());
			// 名字
			values3.put("rememberpsw", memberBean.getMemberNm());
			// id
			values3.put("num", memberBean.getMemberId());
			MyApp.getDB().insert("other", null, values3);
		}
	}
	/**
	 * 删除数据库
	 * */
	public void deleteToDB(int type) {
		// 先删掉原有的
		MyApp.getDB().delete("other","userid=? and companyId=? and mark=?",
				new String[] { SPUtils.getID(), SPUtils.getCompanyId(),String.valueOf(type) });
	}

}
