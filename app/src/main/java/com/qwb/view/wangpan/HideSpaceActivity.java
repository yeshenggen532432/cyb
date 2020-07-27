package com.qwb.view.wangpan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qwb.view.base.ui.XForgetPwdActivity;
import com.qwb.view.file.model.FileBean;
import com.qwb.view.base.ui.BaseNoTitleActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.chiyong.t3.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 云盘模块--隐藏空间（）
 * 
 */
public class HideSpaceActivity extends BaseNoTitleActivity {

	private String yfPwd;
	private EditText edit_pwd;
	private int right_type;
	private TextView tv_wangjiPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_hide_space);

		initUI();
		initData_queryYfilepwd();// 获取文件密码
	}

	// 头部
	private void initHead() {
		findViewById(R.id.iv_head_back).setOnClickListener(this);
		findViewById(R.id.tv_head_right).setOnClickListener(this);
		// findViewById(R.id.img_head_right).setOnClickListener(this);
		TextView tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
		TextView tv_headRight = (TextView) findViewById(R.id.tv_head_right);
		ImageView img_head_right = (ImageView) findViewById(R.id.img_head_right);
		tv_headTitle.setText("隐藏空间");
		tv_headRight.setText("完成");
		img_head_right.setImageResource(R.drawable.icon_add_yunpan);
		tv_headRight.setVisibility(View.VISIBLE);
		img_head_right.setVisibility(View.GONE);
	}

	private void initUI() {
		initHead();

		edit_pwd = (EditText) findViewById(R.id.edit_pwd);
		tv_wangjiPwd = (TextView) findViewById(R.id.tv_wangjiPwd);
		tv_wangjiPwd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		
		switch (v.getId()) {
		case R.id.iv_head_back:
			finish();
			break;
		case R.id.tv_wangjiPwd://忘记密码
			intent=new Intent(HideSpaceActivity.this, XForgetPwdActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_head_right:
			// 1:设置隐藏空间密码 2：查验隐藏空间密码
			String trim = edit_pwd.getText().toString().trim();
			if (MyUtils.isEmptyString(trim)) {
				ToastUtils.showCustomToast("请填写隐藏空间密码");
				return;
			}
			if (1 == right_type) {
				initData_addYfilepwd(trim);
			} else if (2 == right_type) {
				if (!MyUtils.isEmail(yfPwd)) {
					if (yfPwd.equals(trim)) {
						FileBean fileBean = new FileBean();
						fileBean.setFileNm("隐藏空间");
						fileBean.setTp2(3);
						intent = new Intent(HideSpaceActivity.this,NextFileActivity.class);
						intent.putExtra("fileBean", fileBean);
						startActivity(intent);
					}else{
						ToastUtils.showCustomToast("密码错误");
					}
				}
			}
			break;
		}
	}

	// 获取文件密码
	private void initData_queryYfilepwd() {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.queryYfilepwd)
				.id(1)
				.build()
				.execute(new MyStringCallback(),null);
	}

	// 获取文件密码
	private void initData_addYfilepwd(String yfPwd) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("yfPwd", yfPwd);
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.addYfilepwd)
				.id(2)
				.build()
				.execute(new MyStringCallback(),this);
	}


	// ------------------------------TODO OKHttp回调数据--------------------------
	public class MyStringCallback extends StringCallback {
		@Override
		public void onError(Call call, Exception e, int id) {
			e.printStackTrace();
		}

		@Override
		public void onResponse(String response, int id) {
			resultData(response, id);
		}
	}
	/**
	 * 回调数据
	 */
	private void resultData(String json, int tag) {
		if (!MyUtils.isEmptyString(json) && json.startsWith("{")) {
			switch (tag) {
				case 1://获取文件密码
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject != null) {
							if (jsonObject.getBoolean("state")) {
								yfPwd = jsonObject.getString("yfPwd");
								if (MyUtils.isEmptyString(yfPwd)) {
									edit_pwd.setHint("请设置隐藏空间密码");
									tv_wangjiPwd.setVisibility(View.GONE);
									right_type = 1;// 1:设置隐藏空间密码 2：查验隐藏空间密码
								} else {
									edit_pwd.setHint("请输入隐藏空间密码");
									tv_wangjiPwd.setVisibility(View.VISIBLE);
									right_type = 2;// 1:设置隐藏空间密码 2：查验隐藏空间密码
								}
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				case 2://添加文件密码
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject != null) {
							if (jsonObject.getBoolean("state")) {
								FileBean fileBean = new FileBean();
								fileBean.setFileNm("隐藏空间");
								fileBean.setTp2(3);
								Intent intent = new Intent(HideSpaceActivity.this,NextFileActivity.class);
								intent.putExtra("fileBean", fileBean);
								startActivity(intent);
								finish();
							}
							ToastUtils.showCustomToast(jsonObject.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
			}
		}
	}
}
