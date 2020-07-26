package com.qwb.utils;

import android.content.Context;
import android.widget.TextView;

/**
 * 选择时间的工具类
 */
public class MyChooseTimeUtil {

	public static void chooseDate(Context context, String title, final TextView tv){
		String date = tv.getText().toString().trim();
		if (!MyStringUtil.isValidDate(date)){
			date = MyTimeUtils.getToday_nyr();
		}
		new com.qwb.widget.MyDatePickerDialog(context, title, date, new com.qwb.widget.MyDatePickerDialog.DateTimeListener() {
			@Override
			public void onSetTime(int year, int month, int day, String timeStr) {
				tv.setText(timeStr);
			}

			@Override
			public void onCancel() {
			}
		}).show();
	}

}
