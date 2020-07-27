package com.qwb.view.wangpan;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qwb.view.file.model.FileBean;
import com.qwb.view.base.ui.BaseNoTitleActivity;
import com.qwb.view.member.model.BuMenListBean;
import com.qwb.view.member.model.BuMenListBean.BuMenItem;
import com.qwb.view.member.model.BuMenListBean.MemberBean;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.cnlife.widget.CircleImageView;
import com.xmsx.cnlife.widget.ComputeHeightListView;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.chiyong.t3.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 云盘模块--转发给-个人
 *
 */
public class GeRenActivity extends BaseNoTitleActivity {

	private String bumemid,title;
	private List<BuMenItem> departls=new ArrayList<>();
	private List<MemberBean> members=new ArrayList<>();
	private ComputeHeightListView lv_pull;
	private ComputeHeightListView lv_mems;
	private BuMenAdapter myAdapter;
	private ChengYuanAdapter mySecondAdapter;
	
	// 云盘
	private boolean isYunpan;
	private FileBean fileBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_geren);
		Intent intent = getIntent();
		bumemid = intent.getStringExtra("id");
		title = intent.getStringExtra("title");
		// 云盘
		isYunpan = intent.getBooleanExtra("isYunpan", false);
		fileBean = intent.getParcelableExtra("fileBean");
		
		initUI();
		initPopup();
		getMemberList();
	}
	
	// 头部
		private void initHead() {
			findViewById(R.id.iv_head_back).setOnClickListener(this);
			// findViewById(R.id.tv_head_right).setOnClickListener(this);
			findViewById(R.id.img_head_right).setOnClickListener(this);
			TextView tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
			TextView tv_headRight = (TextView) findViewById(R.id.tv_head_right);
			ImageView img_head_right = (ImageView) findViewById(R.id.img_head_right);
			if(MyUtils.isEmptyString(title)){
				tv_headTitle.setText("通讯录");
			}else {
				tv_headTitle.setText(title);
			}
			img_head_right.setImageResource(R.drawable.icon_add_yunpan);
			tv_headRight.setVisibility(View.GONE);
			img_head_right.setVisibility(View.GONE);
		}
	
	private void initUI() {
		initHead() ;
		lv_pull = (ComputeHeightListView) findViewById(R.id.lv_pull);
		lv_mems = (ComputeHeightListView) findViewById(R.id.lv_mems);
	}

	private void getMemberList() {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("dataTp", "1");
		String url=null;
		if (!MyUtils.isEmptyString(bumemid)) {
			params.put("parentid",bumemid);
			url= Constans.queryDepartlszOrcyURL;
		}else{
			url= Constans.queryDepartlsURL;
		}
		OkHttpUtils
				.post()
				.params(params)
				.url(url)
				.id(1)
				.build()
				.execute(new MyStringCallback(),this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_head_back:
			finish();
			break;
		}
	}


	/**
	 * 刷新部门列表
	 * */
	private void refreshAdapter(){
		if (lv_pull != null) {
			if (myAdapter == null) {
				myAdapter = new BuMenAdapter();
				lv_pull.setAdapter(myAdapter);
			}else{
				myAdapter.notifyDataSetChanged();
			}
		}
	}
	
	/**
	 * 刷新成员列表
	 * */
	private void refreshSecondAdapter(){
		if (lv_mems != null) {
			if (mySecondAdapter == null) {
				mySecondAdapter = new ChengYuanAdapter();
				lv_mems.setAdapter(mySecondAdapter);
			}else{
				mySecondAdapter.notifyDataSetChanged();
			}
		}
	}
	
	//点击"查看成员"
	private OnClickListener myOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BuMenItem bean = (BuMenItem) v.getTag();
			Intent intent = new Intent(GeRenActivity.this,GeRenActivity.class);
			intent.putExtra("id", String.valueOf( bean.getBranchId()));
			intent.putExtra("title",bean.getBranchName());
			intent.putExtra("isYunpan", isYunpan);
			intent.putExtra("fileBean", fileBean);
			startActivity(intent);
		}
	};
	
	//点击名字进入聊天事件
	private int mPosition;//记录点击是哪一行
		private OnClickListener myChatOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPosition = (Integer) v.getTag();
				MemberBean memberBean = members.get(mPosition);
				if (SPUtils.getSValues("memId").equals(String.valueOf(memberBean.getMemberId()))) {
				}else{
					
					tv_zhuanfa_name.setText(memberBean.getMemberNm());
					popWin.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0,0);
					backgroundAlpha(0.5f);
				}
			}
		};
	
	//部门适配器
	class BuMenAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return departls.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.x_fragment_zuzhi_item, null);
			}
			
			final BuMenItem buMenItem = departls.get(position);
			TextView tv_bumen = MyUtils.getViewFromVH(convertView, R.id.tv_bumen);
			RelativeLayout rl_fu = MyUtils.getViewFromVH(convertView, R.id.rl_fu);
			View iv_more = MyUtils.getViewFromVH(convertView, R.id.iv_more);
			iv_more.setVisibility(View.GONE);
			rl_fu.setTag(buMenItem);
			rl_fu.setOnClickListener(myOnClickListener);
//			iv_more.setVisibility(View.VISIBLE);
			tv_bumen.setText(buMenItem.getBranchName());
			
			
			rl_fu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(GeRenActivity.this,GeRenActivity.class);
					intent.putExtra("id", String.valueOf( buMenItem.getBranchId()));
					intent.putExtra("title",buMenItem.getBranchName());
					intent.putExtra("isYunpan", isYunpan);
					intent.putExtra("fileBean", fileBean);
					startActivity(intent);
				}
			});
			return convertView;
		}
	}
	
	//成员适配器
	class ChengYuanAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return members.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.x_fragment_friends_items2, null);
			}
			final MemberBean memberBean = members.get(position);
			TextView tv_name = MyUtils.getViewFromVH(convertView, R.id.tv_title);
			RelativeLayout rl = MyUtils.getViewFromVH(convertView, R.id.rl);
			tv_name.setTag(position);
			tv_name.setText(memberBean.getMemberNm());
			tv_name.setOnClickListener(myChatOnClickListener);
			
			View iv_tel = MyUtils.getViewFromVH(convertView, R.id.iv_tel);
			iv_tel.setVisibility(View.GONE);
			
			CircleImageView iv_userhead = MyUtils.getViewFromVH(convertView, R.id.iv_userhead);
			iv_userhead.setTag(memberBean.getMemberId());
			MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + memberBean.getMemberHead(), iv_userhead);
			
			if (SPUtils.getSValues("memId").equals(String.valueOf(memberBean.getMemberId()))) {
				tv_name.setClickable(false);
				rl.setBackgroundResource(R.color.yunpan_gray_bg);
			}else{
				tv_name.setClickable(true);
				rl.setBackgroundResource(R.drawable.tongxunlu_bg);
			}
			return convertView;
		}
	}
	
	
	/*
	 * 窗体
	 */
	private PopupWindow popWin = null;
	private View contentView = null;
	private TextView tv_zhuanfa_name;
	private void initPopup() {
		contentView = getLayoutInflater().inflate(R.layout.x_popup_yunpan_zhuanfa, null);
		tv_zhuanfa_name = (TextView) contentView.findViewById(R.id.tv_zhuanfa_name);
		popWin = new PopupWindow(contentView,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		popWin.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWin.setBackgroundDrawable(new BitmapDrawable());
		popWin.setOnDismissListener(new poponDismissListener());
		
		Button btn_cancel = (Button) contentView.findViewById(R.id.btn_cancel);
		Button btn_queding = (Button) contentView.findViewById(R.id.btn_confirm);
		btn_queding.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popWin.dismiss();
//				Intent intent = new Intent(WorkerGroupActivity.this,ChatActivity.class);
//				intent.putExtra("isYunpan", true);
//				intent.putExtra("id", String.valueOf(groupItem_yun.getGroupId()));
//				intent.putExtra("name", String.valueOf(groupItem_yun.getGroupNm()));
//				intent.putExtra("headurl", groupItem_yun.getGroupHead());
//				intent.putExtra("isopen", groupItem_yun.getIsOpen());
//				intent.putExtra("role", groupItem_yun.getRole());
//				intent.putExtra("type", String.valueOf(9));
//				intent.putExtra("fileBean", fileBean);
//				startActivity(intent);
			}
		});
		
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popWin.dismiss();
			}
		});
	}

	// 窗体的背景透明度0~1
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	// 窗体消失监听
	class poponDismissListener implements PopupWindow.OnDismissListener {
		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}
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
				case 1:
					BuMenListBean parseObject = JSON.parseObject(json, BuMenListBean.class);
					if (parseObject.isState()) {
						departls = parseObject.getDepartls();
						members = parseObject.getMemls();
						if (departls.size() > 0) {
							refreshAdapter();
						}
						if (members.size() > 0) {
							refreshSecondAdapter();
						}
					}
					break;
			}
		}
	}
}
