package com.qwb.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.qwb.utils.Constans;
import com.qwb.service.XMessageService;
import com.qwb.view.tab.ui.message.XMessageActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光推送消息过来的接收器
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();
		String action = intent.getAction();

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(action)) {
			//注册
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
			//自定义消息--不会提示在通知栏
			startMessageService(context);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action)) {
//			Log.e(TAG, " 接收到推送下来的通知");
			startMessageService(context);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
//			Log.e(TAG, "用户点击打开了通知");
			startMessageActivity(context,bundle);
			JPushInterface.reportNotificationOpened(context,bundle.getString(JPushInterface.EXTRA_MSG_ID));

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(action)) {

		} else {

		}
	}

	//打开信息服务
	public void startMessageService(Context context){
		Intent i = new Intent(context, XMessageService.class);
		i.setAction(Constans.UnRreadMsg);
		context.startService(i);
	}

	//打开信息界面
	public void startMessageActivity(Context context,Bundle bundle){
		Intent i = new Intent(context, XMessageActivity.class);
		i.putExtras(bundle);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}


	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

}
