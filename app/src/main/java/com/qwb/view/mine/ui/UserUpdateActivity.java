package com.qwb.view.mine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qwb.view.mine.parsent.PUserUpdate;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 用户信息：修改
 */
public class UserUpdateActivity extends XActivity<PUserUpdate>{

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_user_update;
	}

	@Override
	public PUserUpdate newP() {
		return new PUserUpdate();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initIntent();
		initUI();
	}

	private String key,value,title;
	private void initIntent(){
		Intent intent = getIntent();
		key = intent.getStringExtra(ConstantUtils.Intent.KEY);
		value = intent.getStringExtra(ConstantUtils.Intent.VALUE);
		title = intent.getStringExtra(ConstantUtils.Intent.TITLE);
	}

	@BindView(R.id.et_update)
	EditText mEtUpdate;
	@BindView(R.id.tv_head_title)
	TextView mTvHeadTitle;
	@BindView(R.id.tv_head_right)
	TextView mTvHeadRight;
	private void initUI() {
		MyStatusBarUtil.getInstance().setColorWhite(context);
		mTvHeadTitle.setText(title);
		mTvHeadRight.setText("保存");

    	if (MyStringUtil.isNotEmpty(value)) {
			mEtUpdate.setText(value);
			mEtUpdate.setSelection(value.length());
		}
	}

	@OnClick({R.id.head_left, R.id.head_right})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_left:
			ActivityManager.getInstance().closeActivity(context);
			break;
		case R.id.head_right:
			updateDate();
			break;
		}
	}

	private void updateDate() {
		String input = mEtUpdate.getText().toString().trim();
		if (MyStringUtil.isEmpty(input)) {
			ToastUtils.showCustomToast( "输入内容不能为空！");
			return ;
		}
		if ("邮箱".equals(title)) {
			if (!MyUtils.isEmail(input)) {
				ToastUtils.showCustomToast( "邮箱格式不对！");
				return ;
			}
		}
		getP().updateUser(context, key , input);
	}

	public void doUpdateSuccess(){
		setResult(ConstantUtils.Intent.REQUEST_CODE_UPDATE_SUCCESS);
		ActivityManager.getInstance().closeActivity(context);
	}



}
