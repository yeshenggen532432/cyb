package com.qwb.receive;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.widget.TextView;

import com.qwb.application.MyApp;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;

/**
 * 未读消息数字计算管理器
 * mark 该条消息来自哪个类型的唯一性标记
 * */
public class UnReadNumManager {
	private static UnReadNumManager unReadNumManager;
	private SQLiteDatabase mDB;

	private UnReadNumManager() {
	}

	public static UnReadNumManager getI() {
		if (unReadNumManager == null) {
			unReadNumManager = new UnReadNumManager();
		}
		return unReadNumManager;
	}

	private void getDB() {
		if (mDB == null) {
			mDB = MyApp.getDB();
		}
	}

	/**
	 * 添加未读消息到按时间排序模式 mark 
	 * "bangkuai"+板块对应的数字 1 ：系统通知 2：审批 3：易推事板 4： 圈动态 5：真心话 6：沟通
	 * */
	public void addNumberToTime(String mark, String lastmsg) {
		getDB();
		addAllNum();
		String userid = SPUtils.getSValues("memId");
		Cursor query = mDB.query("other", null, "mark=? and userid=?",
				new String[] { mark, userid }, null, null, null);
		if (query.moveToFirst()) {
			int allnmu = query.getInt(query.getColumnIndex("num"));
			mDB.execSQL("UPDATE other SET num=? WHERE mark=? and userid=?",
					new String[] { String.valueOf(allnmu + 1), mark, userid });
			mDB.execSQL("UPDATE other SET val=? WHERE mark=? and userid=?",
					new String[] { lastmsg, mark, userid });
		} else {
			ContentValues values2 = new ContentValues();
			values2.put("num", 1);
			values2.put("userid", userid);
			values2.put("mark", mark);
			values2.put("val", lastmsg);
			mDB.insert("other", null, values2);
		}
		query.close();
	}

	/**
	 * 时间排序模式的未读消息总条数减去条数
	 * */
	private void downAllNum(int num) {
		String userid = SPUtils.getID();
		Cursor query = mDB.query("other", null, "mark=? and userid=?",
				new String[] { "timeall", userid }, null, null, null);
		if (query.moveToFirst()) {
			int allnmu = query.getInt(query.getColumnIndex("num") - num);
			if (allnmu <= 0) {
				allnmu = 0;
			}
			mDB.execSQL("UPDATE other SET num=? WHERE mark=? and userid=?",
					new String[] { String.valueOf(allnmu), "timeall", userid });
		}
		query.close();
	}

	/**
	 * 时间排序模式的未读消息总条数加1
	 * */
	private void addAllNum() {
		String userid = SPUtils.getID();
		Cursor query = mDB.query("other", null, "mark=? and userid=?",
				new String[] { "timeall", userid }, null, null, null);
		if (query.moveToFirst()) {
			int allnmu = query.getInt(query.getColumnIndex("num"));
			mDB.execSQL("UPDATE other SET num=? WHERE mark=? and userid=?",
					new String[] { String.valueOf(allnmu + 1), "timeall",
							userid });
		} else {
			ContentValues values2 = new ContentValues();
			values2.put("num", 1);
			values2.put("userid", userid);
			values2.put("mark", "timeall");
			mDB.insert("other", null, values2);
		}
		query.close();
	}

	/**
	 * 添加未读消息到分板块的模式 mark "bangkuai"+板块对应的数字 1 ：系统通知 2：审批 3：易推事板 4： 圈动态 5：真心话
	 * 6：沟通
	 * */
	public void addNumberToBK(String mark, String lastmsg, String time) {
		getDB();
		String userid = SPUtils.getSValues("memId");
		Cursor query = mDB.query("other", null, "mark=? and userid=?",
				new String[] { mark, userid }, null, null, null);
		if (query.moveToFirst()) {
			int allnmu = query.getInt(query.getColumnIndex("num"));
			mDB.execSQL("UPDATE other SET num=? WHERE mark=? and userid=?",
					new String[] { String.valueOf(allnmu + 1), mark, userid });
		} else {
			ContentValues values2 = new ContentValues();
			values2.put("num", 1);
			values2.put("userid", userid);
			values2.put("mark", mark);
			mDB.insert("other", null, values2);
		}
		query.close();
	}

	/**
	 * 清除分板块的未读消息
	 * */
	public void cleanNumber(String mark) {

		getDB();
		String userid = SPUtils.getSValues("memId");
		Cursor query = mDB.query("other", null, "mark=? and userid=?",
				new String[] { mark, userid }, null, null, null);
		if (query.moveToFirst()) {
			mDB.execSQL("UPDATE other SET num=? WHERE mark=? and userid=?",
					new String[] { String.valueOf(0), mark, userid });
		}
		query.close();
	}

	/**
	 * 清除按时间排序的未读消息 这里需要计算
	 * */
	public void cleanTimeNumber(String mark) {

		getDB();
		String userid = SPUtils.getID();
		Cursor query = mDB.query("other", null, "mark=? and userid=?",
				new String[] { mark, userid }, null, null, null);
		if (query.moveToFirst()) {
			int allnmu = query.getInt(query.getColumnIndex("num"));
			// 先减去总的未读数
			downAllNum(allnmu);
			// 清掉本mark的条数
			mDB.execSQL("UPDATE other SET num=? WHERE mark=? and userid=?",
					new String[] { String.valueOf(0), mark, userid });
		}
		query.close();
	}

	public int getNumber(String mark) {
		getDB();
		String userid = SPUtils.getSValues("memId");
		Cursor query = mDB.query("other", null, "mark=? and userid=?",
				new String[] { mark, userid }, null, null, null);
		int int1 = 0;
		if (query.moveToFirst()) {
			int1 = query.getInt(query.getColumnIndex("num"));
		}
		query.close();
		return int1;
	}

	public void setLastMsg(String mark, TextView tv_last_gt, TextView tv_time_gt) {
		getDB();
		String userid = SPUtils.getSValues("memId");
		Cursor query = mDB.query("other", null, "mark=? and userid=?",
				new String[] { mark, userid }, null, null, null);
		if (query.moveToFirst()) {
			String msg = query.getString(query.getColumnIndex("val"));
			if (!TextUtils.isEmpty(msg)) {
				tv_last_gt.setText(msg);
			} else {
				tv_last_gt.setText("暂无消息");
			}

			String time = query.getString(query.getColumnIndex("rememberpsw"));
			String formatTime2 = MyUtils.formatTime2(time);
			tv_time_gt.setText(formatTime2);

		}
		query.close();
	}

	// 是否有未读消息
	public static boolean hasUnRead() {
		// 时间排序
		Cursor query = MyApp.getDB()
				.query("ur_sysnotify",null,"userid=? and isread=?",
						new String[] { SPUtils.getSValues("memId"),
								String.valueOf(0) }, null, null, null);
		if (query != null && query.moveToFirst() && query.getCount() > 0) {
			query.close();
//			MenuColorManager.getInstance().setColor();
			return true;
		} else {
			query.close();
//			MenuColorManager.getInstance().cleanColor();
			return false;
		}
	}
}
