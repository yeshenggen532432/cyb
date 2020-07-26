package com.qwb.utils;

import com.qwb.view.base.model.BasePageBean;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.base.model.XbaseBean;


/**
 * App
 */
public class MyRequestUtil {
	/**
	 * 验证返回的数据是否成功
	 */
	public static boolean isSuccess(BaseBean bean) {
		try {
			if (bean != null && (bean.isState() || 2 ==bean.getCode())){
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 验证返回的数据是否成功
	 */
	public static boolean isSuccess(XbaseBean bean) {
		try {
			if (bean != null && (bean.isState() || 2 ==bean.getCode())){
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 验证返回的数据是否成功
	 */
	public static boolean isSuccess(BasePageBean bean) {
		try {
			if (bean != null && (bean.isState() || 2 ==bean.getCode())){
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}



}
