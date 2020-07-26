package com.qwb.view.log.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.qwb.utils.ConstantUtils;
import com.xmsx.qiweibao.R;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 工作汇报-写日报；写周报，写月报
 */
public class LogWriteFragment extends XFragment implements OnClickListener {

	@Override
	public int getLayoutId() {
		return R.layout.x_fragment_log_write;
	}

	@Override
	public Object newP() {
		return null;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initUI();
	}

	private void initUI() {
		initHead();
	}

	// 头部
	@BindView(R.id.tv_head_title)
	TextView mTvHeadTitle;
	private void initHead() {
		mTvHeadTitle.setText("工作汇报");
	}

	@OnClick({R.id.iv_head_back,R.id.img_ribao,R.id.img_zhoubao,R.id.img_yuebao})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_head_back:
			getActivity().finish();
			break;
		case R.id.img_ribao:// 提交日报
			jumpActivity(1);
			break;
		case R.id.img_zhoubao:// 提交周报
			jumpActivity(2);
			break;
		case R.id.img_yuebao:// 提交月报
			jumpActivity(3);
			break;
		}
	}

	//跳到日报；周报；月报
	public void jumpActivity(int type){
		Router.newIntent(context)
				.putInt(ConstantUtils.Intent.TYPE,type)// 1:日报 2：周报3：月报
				.to(LogSubmitActivity.class)
				.launch();
	}


}
