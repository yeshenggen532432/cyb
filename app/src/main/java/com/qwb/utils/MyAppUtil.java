package com.qwb.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.qwb.application.MyApp;


/**
 * App
 */
public class MyAppUtil {
	/**
	 * app版本信息
	 */
	public static String getAppVersion() {
		try {
			// 得到当前App版本号
			PackageManager packageManager = MyApp.getI().getPackageManager();
			PackageInfo info = packageManager.getPackageInfo( MyApp.getI().getPackageName(), 0);
			String versionCode = info.versionName;
			return versionCode;
		} catch (PackageManager.NameNotFoundException e) {
		}
		return "";
	}

	/**
	 * 获取app版本号
	 */
	public static int getAppVersionNo() {
		try {
			PackageManager packageManager = MyApp.getI().getPackageManager();
			PackageInfo info = packageManager.getPackageInfo(MyApp.getI().getPackageName(), 0);
			int versionCode = info.versionCode;
			return versionCode;
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 版本更新提示是否超过3次；超过3次不在提醒
	 */
	public static boolean isShowUpdateVersion(int versionNo){
		int count = SPUtils.getIValues("update_version_" + versionNo);
		if (count < 3){
			return true;
		}
		return false;
	}

	public static void saveVersionNo(int versionNo){
		int count = SPUtils.getIValues("update_version_" + versionNo);
		SPUtils.setintValues("update_version_" + versionNo, count + 1);
	}

}
