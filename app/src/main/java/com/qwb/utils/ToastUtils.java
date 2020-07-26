package com.qwb.utils;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	private ToastUtils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static Context mContent;

	/**
	 * 在application中初始化
	 */
	public static void initUtils(Application app) {
		mContent = app.getApplicationContext();
	}

	public static boolean isShow = true;

	/**
	 * 短时间显示Toast
	 */
	public static void showCustomToast(CharSequence message) {
		if (isShow)
			Toast.makeText(mContent, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 短时间显示Toast
	 */
	public static void showShort(Context context, String message) {
		if (isShow && context!=null)
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 长时间显示Toast
	 */
	public static void showLongCustomToast(CharSequence message) {
		if (isShow)
			Toast.makeText(mContent, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 长时间显示Toast
	 */
	public static void showLong(Context context, int message) {
		if (isShow)
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 自定义显示Toast时间
	 */
	public static void show(Context context, CharSequence message, int duration) {
		if (isShow)
			Toast.makeText(context, message, duration).show();
	}

	/**
	 * 自定义显示Toast时间
	 */
	public static void show(Context context, int message, int duration) {
		if (isShow)
			Toast.makeText(context, message, duration).show();
	}

	/**
	 * 长时间显示Toast
	 */
	public static void showError(Exception e) {
		if (isShow && null != e){
			e.printStackTrace();
			Toast.makeText(mContent, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

}
