package com.qwb.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;


/**
 * 拨打电话
 */
public class MyCallUtil {

	/**
	 * 跳到拨打电话界面
	 */
	public static void call(final Activity activity,final String phone) {
		try {
			if (MyStringUtil.isNumber(phone)){
				final NormalDialog dialog = new NormalDialog(activity);
				dialog.content("是否要拨打电话：" + phone).show();
				dialog.setOnBtnClickL(null, new OnBtnClickL() {
					@Override
					public void onBtnClick() {
						Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
						activity.startActivity(dialIntent);
					}
				});
			}
		} catch (Exception e) {
		}
	}



}
