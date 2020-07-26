package com.qwb.view.audit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.qwb.application.MyApp;
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
import com.qwb.utils.ToastUtils;
import com.xmsx.qiweibao.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class AddShenPiRenActivity extends BaseNoTitleActivity implements OnClickListener {

	private String bumemid, title;
	private List<BuMenItem> departls;
	private List<MemberBean> members;
	private ComputeHeightListView lv_pull;
	private ComputeHeightListView lv_mems;
	private BuMenAdapter myAdapter;
	private ChengYuanAdapter mySecondAdapter;
	private List<Integer> ids;
	private int type;// 1 请假 2报销 3出差 4物品领用 5通用审批
	private MemberBean cuttentBean;

	/**
	 * 是否是单选 1为单选
	 */
	private int single;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_add_shen_pi_ren);
		MyApp.getI().addShenPiActivity(this);
		Intent intent = getIntent();
		if (intent != null) {
			bumemid = intent.getStringExtra("id");
			title = intent.getStringExtra("title");
			type = intent.getIntExtra("type", 0);
			single = intent.getIntExtra("single", 0);
		}
		if (1 != single) {
			getIds(false);
		}
		initUI();
	}

	/**
	 * 重新获取保存的已经选择的成员id
	 */
	private void getIds(boolean firstGet) {
		if (ids != null) {
			ids.clear();
		}
		List<MemberBean> choiseIds = AddCurrentIds.getI().getChoiseIds(type, firstGet);
		if (choiseIds != null && choiseIds.size() > 0) {
			ids = new ArrayList<Integer>();
			for (int i = 0; i < choiseIds.size(); i++) {
				ids.add(choiseIds.get(i).getMemberId());
			}
		}
	}

	private void getMemberList() {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("dataTp", "1");
		if (!MyUtils.isEmptyString(bumemid)) {
			params.put("parentid", bumemid);
			OkHttpUtils.post().params(params).url(Constans.queryDepartlszOrcyURL).id(1).build()
					.execute(new MyHttpCallback(this) {
						@Override
						public void myOnError(Call call, Exception e, int id) {

						}

						@Override
						public void myOnResponse(String response, int id) {
							JsonData(response, 1);
						}
					});
		} else {
			OkHttpUtils.post().params(params).url(Constans.queryDepartlsURL).id(1).build().execute(new MyHttpCallback(this) {
				@Override
				public void myOnError(Call call, Exception e, int id) {

				}

				@Override
				public void myOnResponse(String response, int id) {
					JsonData(response, 1);
				}
			});
		}

	}

	private void initUI() {
		findViewById(R.id.comm_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView comm_title = (TextView) findViewById(R.id.tv_head_title);
		comm_title.setText(title);

		TextView tv_top_right = (TextView) findViewById(R.id.tv_top_right);
		tv_top_right.setText("添加");
		tv_top_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (1 == single) {
					if (AuditDetailActivity.getI().getBean() != null) {
						AuditDetailActivity.getI().showZhuanJiaoDL();
						MyApp.getI().closeShenPiActivity();
					} else {
						ToastUtils.showCustomToast("请添加转交人");
					}
				} else {
					MyApp.getI().closeShenPiActivity();
				}
			}
		});

		lv_pull = (ComputeHeightListView) findViewById(R.id.lv_pull);
		lv_mems = (ComputeHeightListView) findViewById(R.id.lv_mems);
		getMemberList();
	}

	@Override
	public void onClick(View v) {
	}

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
				convertView = getLayoutInflater().inflate(R.layout.x_fragment_friends_items, null);
			}
			TextView tv_name = MyUtils.getViewFromVH(convertView, R.id.tv_title);
			CheckBox cb_setnomal = MyUtils.getViewFromVH(convertView, R.id.cb_setnomal);
			cb_setnomal.setVisibility(View.VISIBLE);
			CircleImageView iv_userhead = MyUtils.getViewFromVH(convertView, R.id.iv_userhead);

			MemberBean memberBean = members.get(position);
			if (memberBean != null) {
				tv_name.setText(memberBean.getMemberNm());
				int memberId = memberBean.getMemberId();
				if (1 == single) {
					if (cuttentBean != null && cuttentBean.getMemberId() == memberId) {
						cb_setnomal.setChecked(true);
					} else {
						cb_setnomal.setChecked(false);
					}
				} else {
					if (ids != null && ids.contains(memberId)) {
						cb_setnomal.setChecked(true);
					} else {
						cb_setnomal.setChecked(false);
					}
				}

				MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + memberBean.getMemberHead(), iv_userhead);
				cb_setnomal.setTag(memberBean);
				cb_setnomal.setOnClickListener(myAddClickListener);
			}
			return convertView;
		}
	}

	// 添加到审批人列表
	private OnClickListener myAddClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			CheckBox checkbox = (CheckBox) v;
			MemberBean memberBean = (MemberBean) checkbox.getTag();
			if (checkbox.isChecked()) {
				if (1 == single) {
					// AuditDetailActivity.getI().removeBean();
					AuditDetailActivity.getI().setBean(memberBean);
				} else {
					AddCurrentIds.getI().getChoiseIds(type, false).add(memberBean);
				}
			} else {
				if (1 == single) {
					AuditDetailActivity.getI().removeBean();
				} else {
					AddCurrentIds.getI().removeId(memberBean.getMemberId());
				}
			}

			if (1 == single) {
				getCuttentBean();
			} else {
				getIds(false);
			}
			refreshChengYuanAdapter();
		}

	};

	private void getCuttentBean() {
		cuttentBean = AuditDetailActivity.getI().getBean();
	}

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
			View iv_more = MyUtils.getViewFromVH(convertView, R.id.iv_more);
			iv_more.setTag(buMenItem);
			iv_more.setOnClickListener(myOnClickListener);
			iv_more.setVisibility(View.VISIBLE);
			tv_bumen.setText(buMenItem.getBranchName());
			return convertView;
		}
	}

	private OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BuMenItem bean = (BuMenItem) v.getTag();
			Intent intent = new Intent(AddShenPiRenActivity.this, AddShenPiRenActivity.class);
			intent.putExtra("id", String.valueOf(bean.getBranchId()));
			intent.putExtra("single", single);
			intent.putExtra("title", bean.getBranchName());
			startActivity(intent);
		}
	};

	/**
	 * 刷新部门列表
	 */
	private void refreshBuMenAdapter() {
		if (lv_pull != null) {
			if (myAdapter == null) {
				myAdapter = new BuMenAdapter();
				lv_pull.setAdapter(myAdapter);
			} else {
				myAdapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * 刷新成员列表
	 */
	private void refreshChengYuanAdapter() {
		if (lv_mems != null) {
			if (mySecondAdapter == null) {
				mySecondAdapter = new ChengYuanAdapter();
				lv_mems.setAdapter(mySecondAdapter);
			} else {
				mySecondAdapter.notifyDataSetChanged();
			}
		}
	}

	// OKHttp回调数据
	public class MyJsonCallback extends StringCallback {
		@Override
		public void onBefore(Request request, int id) {
		}

		@Override
		public void onAfter(int id) {
		}

		@Override
		public void inProgress(float progress, long total, int id) {
		}

		@Override
		public void onError(Call call, Exception e, int id) {
			e.printStackTrace();
			ToastUtils.showCustomToast(e.getMessage());
		}

		@Override
		public void onResponse(String response, int id) {
			JsonData(response, id);
		}
	}

	/**
	 * 回调数据
	 * @param id
	 */
	private void JsonData(String json, int id) {
		if (!MyUtils.isEmptyString(json) && json.startsWith("{")) {
			switch (id) {
			case 1:
				BuMenListBean parseObject = JSON.parseObject(json, BuMenListBean.class);
				if (parseObject != null && parseObject.isState()) {
					departls = parseObject.getDepartls();
					members = parseObject.getMemls();
					if (departls.size() > 0) {
						refreshBuMenAdapter();
					}
					if (members.size() > 0) {
						refreshChengYuanAdapter();
					}
				}
				break;
			}
		}
	}
}
