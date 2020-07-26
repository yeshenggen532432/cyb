package com.qwb.utils;

import com.qwb.view.step.ui.StepActivity;
import java.util.Calendar;

/**
 * 六步骤：销售量日期
 */
public class MyStepDateUtil {

	/**
	 * 设置“上月”和“本月”的开始日期和结束日期
	 */
	public static void initDate(StepActivity context) {
		Calendar c = Calendar.getInstance();// 获取当前日期
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		// 1:1月1~1月28
		// 2:1月29-2月26日
		// 3:2月27日~3月28日
		// 其他：29~28
		if (month == 1) {
			context.shangFirst = (year - 1) + "-11" + "-29";
			context.shangLast = (year - 1) + "-12" + "-31";
			context.benFirst = year + "-01" + "-01";
			context.benLast = year + "-01" + "-28";
		} else if (month == 2) {
			context.shangFirst = year + "-01" + "-01";
			context.shangLast = year + "-01" + "-28";
			context.benFirst = year + "-01" + "-29";
			context.benLast = year + "-02" + "-26";
		} else if (month == 3) {
			context.shangFirst = year + "-01" + "-29";
			context.shangLast = year + "-02" + "-26";
			context.benFirst = year + "-02" + "-27";
			context.benLast = year + "-03" + "-28";
		} else if (month < 10 && month != 1 && month != 2 && month != 3) {
			context.shangFirst = year + "-0" + (month - 2) + "-29";
			context.shangLast = year + "-0" + (month - 1) + "-28";
			context.benFirst = year + "-0" + (month - 1) + "-29";
			context.benLast = year + "-0" + month + "-28";
		} else if (month == 10) {
			context.shangFirst = year + "-0" + (month - 2) + "-29";
			context.shangLast = year + "-0" + (month - 1) + "-28";
			context.benFirst = year + "-0" + (month - 1) + "-29";
			context.benLast = year + "-" + month + "-28";
		} else if (month == 11) {
			context.shangFirst = year + "-0" + (month - 2) + "-29";
			context.shangLast = year + "-" + (month - 1) + "-28";
			context.benFirst = year + "-" + (month - 1) + "-29";
			context.benLast = year + "-" + month + "-28";
		} else if (month == 12) {
			context.shangFirst = year + "-" + (month - 2) + "-29";
			context.shangLast = year + "-" + (month - 1) + "-28";
			context.benFirst = year + "-" + (month - 1) + "-29";
			context.benLast = year + "-" + month + "-28";
		}
	}



}
