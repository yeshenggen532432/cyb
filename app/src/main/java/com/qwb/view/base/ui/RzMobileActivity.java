package com.qwb.view.base.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qwb.utils.ConstantUtils;
import com.qwb.view.base.parsent.PRzMobile;
import com.qwb.utils.ToastUtils;
import com.chiyong.t3.R;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 认证手机号
 */
public class RzMobileActivity extends XActivity<PRzMobile>{

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_rz_mobile;
	}

	@Override
	public PRzMobile newP() {
		return new PRzMobile();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		prepareTimer();
		initIntent();
	}

	@BindView(R.id.tv_name)
	TextView tv_name;
	@BindView(R.id.tv_mobile)
	TextView tv_mobile;
	@BindView(R.id.edit_yanzm)
	EditText edit_yanzm;
	@BindView(R.id.btn_code)
	Button btn_code;
	private void initIntent() {
		Intent intent = getIntent();
		if (intent != null) {
			String name = intent.getStringExtra(ConstantUtils.Sp.USER_NAME);
			String mobile = intent.getStringExtra(ConstantUtils.Sp.USER_MOBILE);
			tv_name.setText(name);
			tv_mobile.setText(mobile);
		}
	}

	/**
	 * 准备计时器
	 */
	private Handler handler;
	private boolean timeruning;
	private Runnable runnable;
	private int totaltime = 60;
	private void prepareTimer() {
		handler = new Handler();
		runnable = new Runnable() {

			@Override
			public void run() {
				timeruning = true;
				totaltime--;
				btn_code.setText("重新发送(" + totaltime + ")");
				btn_code.setClickable(false);
				btn_code.setBackgroundResource(R.drawable.shap_roundcorner_gray);
				handler.postDelayed(this, 1000);
				if (totaltime <= 0) {
					totaltime = 60;
					timeruning = false;
					btn_code.setText("获取验证码");
					btn_code.setClickable(true);
					btn_code.setBackgroundResource(R.drawable.select_roundcorner_blue);
					handler.removeCallbacks(this);
				}
			}
		};
	}

	//重置验证码
	public void resetCode(){
		if (!timeruning) {
			handler.postDelayed(runnable, 10);
		}
		btn_code.setClickable(true);
		btn_code.setBackgroundResource(R.drawable.select_roundcorner_blue);
	}

	@OnClick({R.id.btn_regist,R.id.btn_code})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_regist:
				String code = edit_yanzm.getText().toString().trim();
				if (TextUtils.isEmpty(code)){
					ToastUtils.showCustomToast("验证码不能为空");
					return;
				}
				getP().rzMobile(context,code);
				break;
			case R.id.btn_code:
				getP().getCode(context);
				btn_code.setClickable(false);
				btn_code.setBackgroundResource(R.drawable.shap_roundcorner_gray);
				break;
		}
	}

}
