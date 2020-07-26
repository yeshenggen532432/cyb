package com.qwb.view.customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.ui.BaseNoTitleActivity;
import com.qwb.view.tree.SimpleTreeAdapter_zuzhi;
import com.qwb.view.member.model.BranchBean;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.member.model.MemberBean;
import com.qwb.view.member.model.BranchListBean2;
import com.xmsx.qiweibao.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 获取成员列表--单选（转让客户）
 */
public class ZuzhiActivity extends BaseNoTitleActivity {

	private String cid;// 客户id
	private boolean isSignle;// 是否单选

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_zuzhi);

		Intent intent = getIntent();
		if (intent != null) {
			cid = intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
			isSignle = intent.getBooleanExtra(ConstantUtils.Intent.IS_SIGNLE, false);// 默认多选
		}
		initUI();
		initData_zuzhi();
	}

	// 获取部门以及成员信息
	private void initData_zuzhi() {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("dataTp", "1");// 角色
		OkHttpUtils.post().params(params).url(Constans.queryDepartMemLs).id(1).build().execute(new JsonCallback(),
				this);
	}

	// 转让客户
	private void initData_zr(String cid, String mid) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("id", cid);// 客户id
		params.put("mid", mid);// 转让给谁id
		Log.e("cid", "cid:" + cid);
		Log.e("mid", "mid:" + mid);
		OkHttpUtils.post().params(params).url(Constans.zrCustomerWeb).id(2).build().execute(new JsonCallback(), this);
	}

	private ListView mTree;

	private void initUI() {
		initHead();
		mTree = (ListView) findViewById(R.id.id_tree);

		// 重置
		findViewById(R.id.btn_reset).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Constans.ziTrueMap_zuzhi.clear();
				Constans.ParentTrueMap_zuzhi.clear();
				refreshAdapter_zuzhi();
			}
		});
	}

	// 头部
	private TextView tv_headRight;

	private void initHead() {
		findViewById(R.id.iv_head_back).setOnClickListener(this);
		findViewById(R.id.tv_head_right).setOnClickListener(this);
		TextView tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
		tv_headRight = (TextView) findViewById(R.id.tv_head_right);
		tv_headTitle.setText("选择转让人");
		tv_headRight.setText("确定");//
		tv_headRight.setVisibility(View.VISIBLE);
	}

	// 刷新--组织架构
	private List<TreeBean> mDatas = new ArrayList<TreeBean>();
	private SimpleTreeAdapter_zuzhi<TreeBean> mAdapter;

	private void refreshAdapter_zuzhi() {
		if ((mDatas != null && mDatas.size() > 0)) {
			if (mAdapter == null) {
				try {
					mAdapter = new SimpleTreeAdapter_zuzhi<TreeBean>(mTree, this, mDatas, 0, isSignle);
					mTree.setAdapter(mAdapter);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} else {
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_head_back:
			finish();
			break;
		case R.id.tv_head_right:// 提交数据
			queding_zuzhi();
			break;
		}
	}

	// 确定--组织架构
	private List<Integer> memIdList = new ArrayList<Integer>();
	private String entityStr2;

	private void queding_zuzhi() {
		// 成员
		memIdList.clear();
		Iterator<Integer> iter = Constans.ziTrueMap_zuzhi.keySet().iterator();
		while (iter.hasNext()) {
			Integer key = iter.next();
			Boolean val = Constans.ziTrueMap_zuzhi.get(key);
			if (val) {
				// 单选
				if (isSignle) {
					initData_zr(cid, String.valueOf(key - 100000));// -100000因为之前+100000
					return;
				} else {
					// 多选
					MemberBean memberBean = Constans.memberMap_zuzhi.get(key);
					if (memberBean != null) {
						Integer memberId = memberBean.getMemberId();
						memIdList.add(memberId);
					}
				}
			}
		}
		// 单选--没选择时提示
		if (isSignle) {
			ToastUtils.showShort(ZuzhiActivity.this, "请选择转让人");
			return;
		} else {
			// 成员
			String entityStr = JSON.toJSONString(memIdList);
			entityStr2 = entityStr.substring(1, entityStr.length() - 1);
			// 部门
			String branchStr = JSON.toJSONString(Constans.ParentTrueMap_zuzhi);// 格式如--{3:0,5:2,7:1,8:0}
			String branchStr2 = branchStr.substring(1, branchStr.length() - 1);// 去掉{}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Constans.ziTrueMap_zuzhi.clear();
		Constans.ParentTrueMap_zuzhi.clear();
	}

	// OKHttp回调数据
	public class JsonCallback extends StringCallback {
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
			jsonData(response, id);
		}
	}

	/**
	 * 回调数据
	 */
	private void jsonData(String json, int id) {
		if (!MyUtils.isEmptyString(json) && json.startsWith("{")) {
			switch (id) {
			case 1:// 组织架构
				BranchListBean2 parseObject3 = JSON.parseObject(json, BranchListBean2.class);
				if (parseObject3 != null) {
					if (parseObject3.isState()) {
						List<BranchBean> list = parseObject3.getList();
						mDatas.clear();
						if (list != null && list.size() > 0) {
							for (int i = 0; i < list.size(); i++) {
								BranchBean branchBean = list.get(i);
								// 第一层
								Integer branchId = branchBean.getBranchId();
								String branchName = branchBean.getBranchName();
								List<MemberBean> memls2 = branchBean.getMemls2();
								if (branchId != null && branchName != null) {
									TreeBean fileBean = new TreeBean(branchId, -1, branchName);
									if (fileBean != null) {
										mDatas.add(fileBean);
									}
									Constans.branchMap_zuzhi.put(branchId, branchBean);// 父

									// 第二层
									if (memls2 != null && memls2.size() > 0) {
										for (int j = 0; j < memls2.size(); j++) {
											MemberBean memberBean = memls2.get(j);
											Integer memberId = memberBean.getMemberId() + 100000;// 默认+10万--防止父ID与子ID重复
											String memberNm = memberBean.getMemberNm();
											if (memberId != null && memberNm != null) {
												mDatas.add(new TreeBean(memberId, branchId, memberNm));
												Constans.memberMap_zuzhi.put(memberId, memberBean);// 子
											}
										}
									}
								}
							}
						}
						refreshAdapter_zuzhi();
					} else {
						ToastUtils.showCustomToast(parseObject3.getMsg());
					}
				}
				break;
				
			case 2:
				try {
					JSONObject jsonObject = new JSONObject(json);
					if (jsonObject.getBoolean("state")) {
						ToastUtils.showCustomToast("申请转让成功");
						finish();
					} else {
						ToastUtils.showShort(ZuzhiActivity.this, jsonObject.getString("msg"));
					}
				} catch (JSONException e) {
				}
				break;
			}
		}
	}
}
