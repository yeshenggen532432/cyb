package com.qwb.view.wangpan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qwb.view.file.model.FileBean;
import com.qwb.view.base.ui.BaseNoTitleActivity;
import com.chiyong.t3.R;

/**
 * 云盘模块：转发文件
 * 
 */
public class ZhuanFaActivity extends BaseNoTitleActivity {

	private boolean isYunpan;
	private FileBean fileBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_zhuanfa);

		Intent intent = getIntent();
		if (intent != null) {
			isYunpan = intent.getBooleanExtra("isYunpan", true);
			fileBean = intent.getParcelableExtra("fileBean");
		}
		
		initHead();
		findViewById(R.id.rl_yuangongyuan).setOnClickListener(this);// 员工圈
		findViewById(R.id.rl_tongxunlu).setOnClickListener(this);// 通讯录--个人
	}

	// 头部
	private void initHead() {
		findViewById(R.id.iv_head_back).setOnClickListener(this);
		// findViewById(R.id.tv_head_right).setOnClickListener(this);
		findViewById(R.id.img_head_right).setOnClickListener(this);
		TextView tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
		TextView tv_headRight = (TextView) findViewById(R.id.tv_head_right);
		ImageView img_head_right = (ImageView) findViewById(R.id.img_head_right);
		tv_headTitle.setText("发送给");
		// tv_headRight.setText("");
		img_head_right.setImageResource(R.drawable.icon_add_yunpan);
		tv_headRight.setVisibility(View.GONE);
		img_head_right.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_head_back:
			finish();
			break;
		case R.id.rl_yuangongyuan:// 员工圈
			break;
		case R.id.rl_tongxunlu:// 通讯录--个人
			intent= new Intent(ZhuanFaActivity.this,GeRenActivity.class);
			intent.putExtra("isYunpan", isYunpan);
			intent.putExtra("fileBean", fileBean);
			startActivity(intent);
			break;
		}
	}
}
