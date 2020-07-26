package com.qwb.view.base.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View.OnClickListener;
import android.view.Window;

import com.pgyersdk.crash.PgyCrashManager;
import com.qwb.application.MyApp;
//import com.xmsx.cnlife.chat.MenuPopWindow;

import cn.jpush.android.api.JPushInterface;

public abstract class BaseNoTitleActivity extends FragmentActivity implements OnClickListener {

	public Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PgyCrashManager.register(this);// 蒲公英注册
		MyApp.getI().addActivity(this);// 添加界面
		//stest
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		mContext=this;
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// 其实这里什么都不要做
		super.onConfigurationChanged(newConfig);
	}

	public void returnJsonResult(String json, int tag) {

	}
}
