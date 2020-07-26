package com.qwb.utils;

import android.content.SharedPreferences;

import com.qwb.application.MyApp;

/**
 * SharedPreferences 工具 token 标记 memId 用户id username 用户登陆名 memberHead 登陆者头像 psw 密码
 * memberMobile 登陆者手机 islogin 保存登陆状态 datasource 所属公司名字
 * iscreat 是不是公司创建者 （1是创建者0不是）
 * hasurmsg (hasUnreadMsg)是否有未读消息
 * */
public class SPUtils {
	private static SharedPreferences sp;

	private SPUtils() {
	}

	public static SharedPreferences getI() {
		if (sp == null) {
			sp = MyApp.getI().getSP();
		}
		return sp;
	}

	/**
	 * SharedPreferences里 获取token
	 * */
	public static String getTK() {
		getI();
		return sp.getString("token", "");
	}
	/**
	 * SharedPreferences里 获取用户id
	 * */
	public static String getID() {
		getI();
		return sp.getString("memId", "");
	}
	/**
	 * SharedPreferences里 获取用户名
	 * */
	public static String getUserName() {
		getI();
		return sp.getString(ConstantUtils.Sp.USER_NAME, "");
	}
	/**
	 * SharedPreferences里 获取账号（手机号）
	 * */
	public static String getMobile() {
		getI();
		return sp.getString(ConstantUtils.Sp.USER_MOBILE, "");
	}
	/**
	 * SharedPreferences里 获取公司名称
	 * */
	public static String getCompanyName() {
		getI();
		return sp.getString(ConstantUtils.Sp.COMPANY_NAME, "");
	}
	/**
	 * SharedPreferences里 获取公司id
	 * */
	public static String getCompanyId() {
		getI();
		String companyId = sp.getString("companyId", "");
		return companyId;
	}

	/**
	 * 往sp存放Stirng类型的数据
	 * */
	public static void setValues(String key, String value) {
		getI();
		key = MyStringUtil.replaceUserIdAndCompanyId(key);
		sp.edit().putString(key, value).commit();
	}
	public static void setintValues(String key, int value) {
		getI();
		key = MyStringUtil.replaceUserIdAndCompanyId(key);
		sp.edit().putInt(key, value).commit();
	}

	public static void setBoolean(String key, boolean value) {
		getI();
		key = MyStringUtil.replaceUserIdAndCompanyId(key);
		sp.edit().putBoolean(key, value).commit();
	}

	/**
	 * 获取Stirng类型的数据
	 * */
	public static String getSValues(String key) {
		getI();
		key = MyStringUtil.replaceUserIdAndCompanyId(key);
		return sp.getString(key, "");
	}
	/**
	 * 获取Stirng类型的数据
	 * */
	public static int getIValues(String key) {
		getI();
		key = MyStringUtil.replaceUserIdAndCompanyId(key);
		return sp.getInt(key, 0);
	}

	/**
	 * 获取boolean类型的数据
	 * */
	public static boolean getBoolean(String key) {
		getI();
		key = MyStringUtil.replaceUserIdAndCompanyId(key);
		return sp.getBoolean(key, false);
	}
	/**
	 * 获取boolean类型的数据
	 * */
	public static boolean getBoolean_true(String key) {
		getI();
		key = MyStringUtil.replaceUserIdAndCompanyId(key);
		return sp.getBoolean(key, true);
	}

}
