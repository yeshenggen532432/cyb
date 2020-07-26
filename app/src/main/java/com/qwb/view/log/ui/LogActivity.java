package com.qwb.view.log.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.qwb.utils.OtherUtils;
import com.xmsx.qiweibao.R;

import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 工作汇报
 */
public class LogActivity extends XActivity implements OnCheckedChangeListener {

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_log;
	}

	@Override
	public Object newP() {
		return null;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		OtherUtils.setStatusBarColor(context);//设置状态栏颜色，透明度
		RadioGroup tabGroup = (RadioGroup) findViewById(R.id.rg);
		tabGroup.setOnCheckedChangeListener(this);
		showFragment(1);// 页面默认
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb0://
			showFragment(1);
			break;
		case R.id.rb1://
			showFragment(2);
			break;
		}
	}

	private LookLogFragment kanRizhiFragment;
	private LogWriteFragment xieRizhiFragment;
	private void showFragment(int position) {
		FragmentManager manager = getSupportFragmentManager();
		// 开启一个Fragment事务
		FragmentTransaction transaction = manager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);

		switch (position) {
		case 1:
			if (xieRizhiFragment == null) {
				xieRizhiFragment = new LogWriteFragment();
				transaction.add(R.id.fl, xieRizhiFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(xieRizhiFragment);
			}
			break;

		case 2:
			if (kanRizhiFragment == null) {
				kanRizhiFragment = new LookLogFragment();
				transaction.add(R.id.fl, kanRizhiFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(kanRizhiFragment);
			}
			break;
		}
		transaction.commit();
	}
	
	/**
	 * 将所有的Fragment都置为隐藏状态。
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (kanRizhiFragment != null) {
			transaction.hide(kanRizhiFragment);
		}
		if (xieRizhiFragment != null) {
			transaction.hide(xieRizhiFragment);
		}
	}


}
