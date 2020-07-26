package com.qwb.view.call.ui;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.qwb.view.location.ui.MapLocationActivity;
import com.qwb.view.plan.model.PlanBean;
import com.qwb.view.base.ui.BaseNoTitleActivity;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.cnlife.widget.ComputeHeightGridView;
import com.xmsx.cnlife.widget.ComputeHeightListView;
import com.xmsx.cnlife.widget.MyGridView;
import com.xmsx.cnlife.widget.photo.ImagePagerActivity;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.JsonHttpUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.xmsx.cnlife.widget.MyTableListView;
import com.qwb.view.call.adapter.PhotoAdapter;
import com.qwb.view.call.adapter.XsxjAdapter_record;
import com.qwb.view.call.model.BfClcjBean;
import com.qwb.view.call.model.BfqdpzBean;
import com.qwb.view.call.model.BfxsxjBean;
import com.qwb.view.call.model.CallonRecordBean;
import com.qwb.view.table.model.MemberCallonBean.YwCanllon;
import com.xmsx.qiweibao.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 拜访记录(详情)--界面
 */
public class CallRecordActivity extends BaseNoTitleActivity  {

	private TextView tv_khNm;
	private TextView tv_memberNm;
	private TextView tv_Dqddate;
	private TextView tv_Dqdtime;
	private TextView tv_Dddtime;
	private TextView tv_setime;
	private TextView tv_Dbcbfzj;
	private TextView tv_Ddbsx;
	private TextView tv_khaddress;
	private TextView tv_Daddress;
	private TextView tv_qtAddress;
	private LinearLayout ll_count1;

	private List<String> bfqdpzList = new ArrayList<>();
	private List<String> bfsdhjcList = new ArrayList<>();
	private List<String> bfsdhjc2List = new ArrayList<>();
	private List<String> bfgzxcList = new ArrayList<>();
	private TextView tv_hbzt;
	private TextView tv_ggyy;
	private TextView tv_isXy;
	private LinearLayout ll_count2;
	private LinearLayout ll_count4;
	private TextView tv_pophb;
	private TextView tv_cq;
	private TextView tv_wq;
	private ComputeHeightGridView gridview_count1;
	private ComputeHeightGridView gridview_count2;
	private ComputeHeightGridView gridview_count2_duitou;
	private PhotoAdapter photoAdapter;
	private PhotoAdapter photoAdapter_count2;
	private PhotoAdapter photoAdapter_count2_duitou;
	private HorizontalScrollView hs;
	private int[] columns;
	private XsxjAdapter_record xsAdapter;
	private LinearLayout ll_count6;
	private ComputeHeightGridView gridview_count6;
	private PhotoAdapter photoAdapter_count6;
	private TextView tv_zj;
	private TextView tv_xcdate;
	private TextView tv_xxd;
	// 陈列采集
	private LinearLayout ll_count3;
	private ComputeHeightListView mListview_clcj;
	private List<BfClcjBean> clcjList = new ArrayList<>();
	private CallonRecordBean parseObject;
	private int type;
	private YwCanllon ywCanllon;// 2:业务拜访统计表
	private PlanBean mPlanBean;// 3:计划拜访

	// 查询下属计划详情
	private String date;// 日期
	private int id;;// 拜访id
	private String khNm;// 客户名称
	private String memberNm;// 用户名

	private ImageView iv_voice;// 语音时间
	private LinearLayout ll_voice;// 语音时间
	private AnimationDrawable animationDrawable = new AnimationDrawable();
	private TextView tv_voicetime;// 语音时间
	private String path_voice;// 语音路径

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_call_record);
		// 动画
		animationDrawable.addFrame(getResources().getDrawable(R.drawable.voice_from_playing_s0), 200);
		animationDrawable.addFrame(getResources().getDrawable(R.drawable.voice_from_playing_s1), 200);
		animationDrawable.addFrame(getResources().getDrawable(R.drawable.voice_from_playing_s2), 200);
		animationDrawable.addFrame(getResources().getDrawable(R.drawable.voice_from_playing_s3), 200);
		animationDrawable.setOneShot(false);

		Intent intent = getIntent();
		if (intent != null) {
			type = intent.getIntExtra("type", 0);
			if (2 == type) {
				ywCanllon = (YwCanllon) intent.getSerializableExtra("ywCanllon");// 业务拜访统计表
			} else if (3 == type) {
				mPlanBean = (PlanBean) intent.getSerializableExtra("planCall");// 计划拜访
				date = intent.getStringExtra("date");
			}if (4 == type) {
				id = intent.getIntExtra(ConstantUtils.Intent.ID,0);
				khNm = intent.getStringExtra(ConstantUtils.Intent.CLIENT_NAME);
				memberNm = intent.getStringExtra(ConstantUtils.Intent.MEMBER_NAME);
			} else {
				id = intent.getIntExtra(Constans.id, 0);
				khNm = intent.getStringExtra(Constans.khNm);
				memberNm = intent.getStringExtra(Constans.memberNm);
			}
		}

		initUI();
		if (3 == type) {
			initData2();// 查询下属计划详情
		} else {
			initData();
		}
	}

	private void initData() {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		if (2 == type) {
			params.put("id", String.valueOf(ywCanllon.getId()));// 业务拜访统计表:拜访id
		} else {
			params.put("id", String.valueOf(id));// 拜访查询::拜访id
		}
		OkHttpUtils.post().params(params).url(Constans.queryBfkheWeb).id(1).build().execute(new MyHttpCallback(this) {
			@Override
			public void myOnError(Call call, Exception e, int id) {

			}

			@Override
			public void myOnResponse(String response, int id) {
				JsonData(response, 1);
			}
		});
	}

	// 查询下属计划详情
	private void initData2() {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("mid", "" + mPlanBean.getMid());// 业务员id
		params.put("cid", "" + mPlanBean.getCid());// 客户id
		params.put("date", "" + date);// 计划时间
		OkHttpUtils.post().params(params).url(Constans.queryBfkheWeb2).id(2).build().execute(new MyHttpCallback(this) {
			@Override
			public void myOnError(Call call, Exception e, int id) {

			}

			@Override
			public void myOnResponse(String response, int id) {
				JsonData(response, 2);
			}
		});
	}

	// 头部
	private void initHead() {
		findViewById(R.id.iv_head_back).setOnClickListener(this);
		findViewById(R.id.tv_head_right).setOnClickListener(this);
		TextView tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
		TextView tv_headRight = (TextView) findViewById(R.id.tv_head_right);
		tv_headTitle.setText("拜访记录");
		tv_headRight.setVisibility(View.GONE);
	}

	private void initUI() {
		initHead();

		tv_khNm = (TextView) findViewById(R.id.tv_khNm);
		tv_memberNm = (TextView) findViewById(R.id.tv_memberNm);
		tv_Dqddate = (TextView) findViewById(R.id.tv_Dqddate);// 日期
		tv_Dqdtime = (TextView) findViewById(R.id.tv_Dqdtime);// 签到时间
		tv_Dddtime = (TextView) findViewById(R.id.tv_Dddtime);// 离开时间
		tv_setime = (TextView) findViewById(R.id.tv_setime);// 时长
		tv_Dbcbfzj = (TextView) findViewById(R.id.tv_Dbcbfzj);// 拜访总结
		tv_Ddbsx = (TextView) findViewById(R.id.tv_Ddbsx);// 代办事项
		tv_khaddress = (TextView) findViewById(R.id.tv_khaddress);// 客户地址
		tv_Daddress = (TextView) findViewById(R.id.tv_Daddress);// 签到地址
		tv_qtAddress = (TextView) findViewById(R.id.tv_qtAddress);// 签退地址
		tv_xxd = (TextView) findViewById(R.id.tv_xxd);

		// 1拜访签到拍照
		ll_count1 = (LinearLayout) findViewById(R.id.ll_count1);// 1拜访签到拍照（1有信息；0没信息）
		tv_hbzt = (TextView) findViewById(R.id.tv_hbzt);// 及时更换外观破损，肮脏的海报招贴
		tv_ggyy = (TextView) findViewById(R.id.tv_ggyy);// 拆除过时的附有旧广告用语的宣传品
		tv_isXy = (TextView) findViewById(R.id.tv_isXy);// 是否显眼（1有，2无）
		gridview_count1 = (ComputeHeightGridView) findViewById(R.id.gridview_count1);
		photoAdapter = new PhotoAdapter(this, bfqdpzList);
		gridview_count1.setAdapter(photoAdapter);

		// 2生动化检查
		ll_count2 = (LinearLayout) findViewById(R.id.ll_count2);// 2生动化检查（1有信息；0没信息）
		tv_pophb = (TextView) findViewById(R.id.tv_pophb);// POP海报
		tv_cq = (TextView) findViewById(R.id.tv_cq);// 串旗
		tv_wq = (TextView) findViewById(R.id.tv_wq);// 围裙
		// remo1 = (TextView) findViewById(R.id.remo1);//生动化摘要
		// remo2 = (TextView) findViewById(R.id.remo2);//生动化摘要
		// isXy = (TextView) findViewById(R.id.isXy);//是否显眼（1有；2无）
		// 生动化
		gridview_count2 = (ComputeHeightGridView) findViewById(R.id.gridview_count2);
		photoAdapter_count2 = new PhotoAdapter(this, bfsdhjcList);
		gridview_count2.setAdapter(photoAdapter_count2);
		// 堆头
		gridview_count2_duitou = (ComputeHeightGridView) findViewById(R.id.gridview_count2_duitou);
		photoAdapter_count2_duitou = new PhotoAdapter(this, bfsdhjc2List);
		gridview_count2_duitou.setAdapter(photoAdapter_count2_duitou);

		// 3:陈列采集
		ll_count3 = (LinearLayout) findViewById(R.id.ll_count3);// list_clcj
		mListview_clcj = (ComputeHeightListView) findViewById(R.id.list_clcj);

		// 4:销售小结
		ll_count4 = (LinearLayout) findViewById(R.id.ll_count4);// 2生动化检查（1有信息；0没信息）
		hs = (HorizontalScrollView) findViewById(R.id.hs);
		String[] from = new String[] { "c1", "c2", "c3", "c4", "c5", "c6", "c10", "c7", "c8", "c9" };
		columns = new int[] { R.id.column1, R.id.column2, R.id.column3, R.id.column4, R.id.column5, R.id.column6,
				R.id.column10, R.id.column7, R.id.column8, R.id.column9 };
		xsAdapter = new XsxjAdapter_record(this, Constans.xsList, R.layout.x_head_item, from, columns, R.color.difColor,
				R.color.difColor1);
		MyTableListView myLisView = new MyTableListView(CallRecordActivity.this, hs, columns, R.id.hs, R.id.list, R.id.head,
				xsAdapter);
		myLisView.setHeadColor(getResources().getColor(R.color.white));// 标题栏颜色

		ll_count6 = (LinearLayout) findViewById(R.id.ll_count6);// 2生动化检查（1有信息；0没信息）
		tv_zj = (TextView) findViewById(R.id.tv_zj);// 拜访总结及代办事项
		tv_xcdate = (TextView) findViewById(R.id.tv_xcdate);// 下次拜访日期
		gridview_count6 = (ComputeHeightGridView) findViewById(R.id.gridview_count6);
		photoAdapter_count6 = new PhotoAdapter(this, bfgzxcList);
		gridview_count6.setAdapter(photoAdapter_count6);

		if (2 == type) {
			tv_khNm.setText(ywCanllon.getKhNm());
			tv_memberNm.setText(ywCanllon.getMemberNm());
		} else if (3 == type) {
			tv_khNm.setText(mPlanBean.getKhNm());
			tv_memberNm.setText(mPlanBean.getMemberNm());
		}if (4 == type) {
			tv_khNm.setText(khNm);
			tv_memberNm.setText(memberNm);
		} else {
			tv_khNm.setText(khNm);
			tv_memberNm.setText(memberNm);
		}

		gridview_count1.setOnItemClickListener(new PhotoListener(bfqdpzList));
		gridview_count2.setOnItemClickListener(new PhotoListener(bfsdhjcList));
		gridview_count2_duitou.setOnItemClickListener(new PhotoListener(bfsdhjc2List));
		gridview_count6.setOnItemClickListener(new PhotoListener(bfgzxcList));

		ll_voice = (LinearLayout) findViewById(R.id.ll_voice);
		tv_voicetime = (TextView) findViewById(R.id.tv_voicetime);
		iv_voice = (ImageView) findViewById(R.id.iv_voice);
		ll_voice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 下载语音
				setVoice(path_voice, 0);
			}
		});
	}

	// gridview监听，1：处于“删除状态”，点击删除图片 2：不处于“删除状态”，点击放大图片
	private final class PhotoListener implements OnItemClickListener {
		private List<String> mList;

		public PhotoListener(List<String> list) {
			this.mList = list;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String[] urls = addList(mList);
			// 点击放大图片
			Intent intent = new Intent(CallRecordActivity.this, ImagePagerActivity.class);
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
			startActivity(intent);
		}
	}

	private String[] addList(List<String> list) {
		String[] urls = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			urls[i] = list.get(i);
		}
		return urls;
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_head_back:
			finish();
			break;
		case R.id.tv_khaddress:// 客户地址
			intent = new Intent(CallRecordActivity.this, MapLocationActivity.class);
			intent.putExtra(Constans.callOnRecord, parseObject);
			intent.putExtra(Constans.type, 2);
			intent.putExtra("address_type", 2);
			startActivity(intent);
			break;
		case R.id.tv_Daddress:// 签到地址
			intent = new Intent(CallRecordActivity.this, MapLocationActivity.class);
			intent.putExtra(Constans.callOnRecord, parseObject);
			intent.putExtra(Constans.type, 2);
			intent.putExtra("address_type", 1);
			startActivity(intent);
			break;
		case R.id.tv_qtAddress:// 签退地址
			intent = new Intent(CallRecordActivity.this, MapLocationActivity.class);
			intent.putExtra(Constans.callOnRecord, parseObject);
			intent.putExtra(Constans.type, 2);
			intent.putExtra("address_type", 3);
			startActivity(intent);
			break;
		}
	}

	// 设置显示数据
	private void setData(String json) {
		parseObject = JSON.parseObject(json, CallonRecordBean.class);
		if (parseObject != null && parseObject.isState()) {
			clcjList.clear();

			tv_Dqddate.setText(parseObject.getDqddate());
			tv_Dqdtime.setText(parseObject.getDqdtime());
			tv_Dddtime.setText(parseObject.getDddtime());
			tv_setime.setText(parseObject.getSetime());
			tv_Dbcbfzj.setText(parseObject.getDbcbfzj());
			tv_Ddbsx.setText(parseObject.getDdbsx());
			tv_khaddress.setText(parseObject.getKhaddress());
			tv_Daddress.setText(parseObject.getDaddress());
			tv_qtAddress.setText(parseObject.getQtaddress());
			tv_xxd.setText(parseObject.getXxzt());

			tv_khaddress.setOnClickListener(this);
			tv_Daddress.setOnClickListener(this);
			tv_qtAddress.setOnClickListener(this);

			// 1拜访签到拍照
			if ("1".equals(parseObject.getCount1())) {// 1:可见 0：不可见
				ll_count1.setVisibility(View.VISIBLE);
				tv_hbzt.setText(parseObject.getHbzt());
				tv_ggyy.setText(parseObject.getGgyy());
				if ("1".equals(parseObject.getIsXy())) {
					tv_isXy.setText("显眼");
				} else {
					tv_isXy.setText("不显眼");
				}
				// 图片
				List<BfqdpzBean> bfqdpzPic = parseObject.getBfqdpzPic();
				if (bfqdpzPic != null && bfqdpzPic.size() > 0) {
					for (int i = 0; i < bfqdpzPic.size(); i++) {
						bfqdpzList.add(Constans.IMGROOTHOST + bfqdpzPic.get(i).getPic());
					}
					photoAdapter.notifyDataSetChanged();
				}
			} else {
				ll_count1.setVisibility(View.GONE);
			}

			// 2生动化检查
			if ("1".equals(parseObject.getCount2())) {// 1:可见 0：不可见
				ll_count2.setVisibility(View.VISIBLE);
				tv_pophb.setText(parseObject.getPophb());
				tv_cq.setText(parseObject.getCq());
				tv_wq.setText(parseObject.getWq());
				if ("1".equals(parseObject.getIsXy())) {
					tv_isXy.setText("显眼");
				} else {
					tv_isXy.setText("不显眼");
				}
				// 图片
				List<BfqdpzBean> bfsdhjcPic1 = parseObject.getBfsdhjcPic1();
				List<BfqdpzBean> bfsdhjcPic2 = parseObject.getBfsdhjcPic2();
				if (bfsdhjcPic1 != null && bfsdhjcPic1.size() > 0) {
					for (int i = 0; i < bfsdhjcPic1.size(); i++) {
						bfsdhjcList.add(Constans.IMGROOTHOST + bfsdhjcPic1.get(i).getPic());
					}
					photoAdapter_count2.notifyDataSetChanged();
				}
				if (bfsdhjcPic2 != null && bfsdhjcPic2.size() > 0) {
					for (int i = 0; i < bfsdhjcPic2.size(); i++) {
						bfsdhjc2List.add(Constans.IMGROOTHOST + bfsdhjcPic2.get(i).getPic());
					}
					photoAdapter_count2_duitou.notifyDataSetChanged();
				}
			} else {
				ll_count2.setVisibility(View.GONE);
			}

			// 3陈列采集
			if ("1".equals(parseObject.getCount3())) {// 1:可见 0：不可见
				ll_count3.setVisibility(View.VISIBLE);
				List<BfClcjBean> list1 = parseObject.getList1();
				if (list1 != null && list1.size() > 0) {
					clcjList.addAll(list1);
					mListview_clcj.setAdapter(new ClcjAdapter());
				}
			} else {
				ll_count3.setVisibility(View.GONE);
			}

			// 4销售小结
			if ("1".equals(parseObject.getCount4())) {// 1:可见 0：不可见
				ll_count4.setVisibility(View.VISIBLE);
				List<BfxsxjBean> list2 = parseObject.getList2();
				Constans.xsList.clear();
				for (int i = 0; i < list2.size(); i++) {
					BfxsxjBean bfxsxjBean = list2.get(i);
					HashMap<String, String> item = new HashMap<String, String>();
					item.put("c1", bfxsxjBean.getWareNm());
					item.put("c2", bfxsxjBean.getDhNum());
					item.put("c3", bfxsxjBean.getSxNum());
					item.put("c4", bfxsxjBean.getKcNum());
					item.put("c5", bfxsxjBean.getDdNum());
					item.put("c6", bfxsxjBean.getXstp());

					String xxdStr = bfxsxjBean.getXxd();
					if (!MyUtils.isEmptyString(xxdStr)) {
						if (Double.valueOf(xxdStr) > 0) {
							item.put("c10", "临期" + xxdStr);
						} else {
							item.put("c10", "正常");
						}
					} else {
						item.put("c10", "");
					}
					item.put("c7", bfxsxjBean.getWareGg());
					item.put("c8", bfxsxjBean.getRemo());
					item.put("c9", "");
					Constans.xsList.add(item);
				}
				xsAdapter.notifyDataSetChanged();

			} else {
				ll_count4.setVisibility(View.GONE);
			}

			// 6道谢并告知下次拜访
			if ("1".equals(parseObject.getCount6())) {// 1:可见 0：不可见
				ll_count6.setVisibility(View.VISIBLE);
				tv_zj.setText(parseObject.getBcbfzj() + "" + parseObject.getDbsx());// 拜访总结及代办事项
				tv_xcdate.setText(parseObject.getXcdate());// 下次拜访日期
				// 图片
				List<BfqdpzBean> bfgzxcPic = parseObject.getBfgzxcPic();
				if (bfgzxcPic != null && bfgzxcPic.size() > 0) {
					for (int i = 0; i < bfgzxcPic.size(); i++) {
						bfgzxcList.add(Constans.IMGROOTHOST + bfgzxcPic.get(i).getPic());
					}
					photoAdapter_count6.notifyDataSetChanged();
				}
			} else {
				ll_count6.setVisibility(View.GONE);
			}

			path_voice = parseObject.getVoiceUrl();
			if (!MyUtils.isEmptyString(path_voice)) {
				ll_voice.setVisibility(View.VISIBLE);
				tv_voicetime.setText(String.valueOf(parseObject.getVoiceTime()) + "\"");
			}
		}
	}

	// 3：陈列采集的适配器
	private final class ClcjAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return clcjList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View RootView = getLayoutInflater().inflate(R.layout.x_adapter_clcj, null);

			TextView tv_name = (TextView) RootView.findViewById(R.id.tv_title); // 客户名称
			TextView tv_hj = (TextView) RootView.findViewById(R.id.tv_hj); // 客户名称
			TextView tv_dj = (TextView) RootView.findViewById(R.id.tv_dj); // 客户名称
			TextView tv_syt = (TextView) RootView.findViewById(R.id.tv_syt); // 客户名称
			TextView tv_bds = (TextView) RootView.findViewById(R.id.tv_bds); // 客户名称
			LinearLayout ll_hj = (LinearLayout) RootView.findViewById(R.id.ll_hj); // 客户名称
			LinearLayout ll_dj = (LinearLayout) RootView.findViewById(R.id.ll_dj); // 客户名称
			LinearLayout ll_syt = (LinearLayout) RootView.findViewById(R.id.ll_syt); // 客户名称
			LinearLayout ll_bds = (LinearLayout) RootView.findViewById(R.id.ll_bds); // 客户名称
			MyGridView mGridview_clcj = (MyGridView) RootView.findViewById(R.id.gridview_pic_clcj); // 客户名称

			BfClcjBean bfClcjBean = clcjList.get(position);
			if (bfClcjBean != null) {
				tv_name.setText(bfClcjBean.getMdNm());
				if (MyUtils.isEmptyString(bfClcjBean.getHjpms())) {
					ll_hj.setVisibility(View.GONE);
				} else {
					ll_hj.setVisibility(View.VISIBLE);
					tv_hj.setText(bfClcjBean.getHjpms());
				}
				if (MyUtils.isEmptyString(bfClcjBean.getDjpms())) {
					ll_dj.setVisibility(View.GONE);
				} else {
					ll_dj.setVisibility(View.VISIBLE);
					tv_dj.setText(bfClcjBean.getDjpms());
				}
				if (MyUtils.isEmptyString(bfClcjBean.getSytwl())) {
					ll_syt.setVisibility(View.GONE);
				} else {
					ll_syt.setVisibility(View.VISIBLE);
					tv_syt.setText(bfClcjBean.getSytwl());
				}
				if (MyUtils.isEmptyString(bfClcjBean.getBds())) {
					ll_bds.setVisibility(View.GONE);
				} else {
					ll_bds.setVisibility(View.VISIBLE);
					tv_bds.setText(bfClcjBean.getBds());
				}

				List<BfqdpzBean> bfxgPicLs = bfClcjBean.getBfxgPicLs();
				if (bfxgPicLs != null && bfxgPicLs.size() > 0) {
					// TODO 容器不能设为全局变量
					mGridview_clcj.setAdapter(new PicAdapter(bfxgPicLs));
				}

				// 点击放大图片
				List<String> urlList = new ArrayList<>();
				urlList.clear();
				for (int i = 0; i < bfxgPicLs.size(); i++) {
					BfqdpzBean bfqdpzBean = bfxgPicLs.get(i);
					urlList.add(Constans.IMGROOTHOST + bfqdpzBean.getPic());
				}
				mGridview_clcj.setOnItemClickListener(new PhotoListener(urlList));
			}
			return RootView;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}
	}

	// 3：陈列采集图片的适配器
	private class PicAdapter extends BaseAdapter {
		private List<BfqdpzBean> mbfxgPicLs = null;

		public PicAdapter(List<BfqdpzBean> bfxgPicLs) {
			super();
			this.mbfxgPicLs = bfxgPicLs;
		}

		@Override
		public int getCount() {
			return mbfxgPicLs.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View RootView = getLayoutInflater().inflate(R.layout.x_adapter_photo_callon, null);
			ImageView iv_simple = RootView.findViewById(R.id.iv_simple);
			BfqdpzBean bfqdpzBean = mbfxgPicLs.get(position);
			if (bfqdpzBean != null) {
				MyGlideUtil.getInstance().displayImageSquere(Constans.IMGROOTHOST + bfqdpzBean.getPicMini(), iv_simple);
			}
			return RootView;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
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
	 */
	private void JsonData(String json, int id) {
		if (!MyUtils.isEmptyString(json) && json.startsWith("{")) {
			switch (id) {
			case 1:// 查询
			case 2:// 查询
				setData(json);
				break;
			}
		}
	}

	// 语音处理
	private boolean playState;
	private MediaPlayer mediaPlayer;
	private int bfid_play;// 以拜访id作为区分（当前点击播放是否与上次播放一致；一致暂停播放，不一致先暂停播放上次的，再播放当前的）

	private void setVoice(final String path, int id) {
		if (!playState) {
			downFileToStartPlayer(path);
		} else {
			stopPlayer();
			if (bfid_play != id) {
				downFileToStartPlayer(path);
			}
		}
	}

	/**
	 * 下载音频文件并播放
	 */
	private void downFileToStartPlayer(final String path) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				String fileName = JsonHttpUtil.getInstance().downFile(path);
				if (!TextUtils.isEmpty(fileName)) {
					openVioceFile(fileName);
				} else {
					ToastUtils.showCustomToast("文件下载中...");
				}
				Looper.loop();
			}
		}).start();
	}

	/**
	 * 自己发送的文件直接打开
	 */
	private void openVioceFile(String path) {
		ToastUtils.showCustomToast("播放中。。");
		mHandler.sendEmptyMessage(0);// handler--播放动画
		if (!playState) {
			mediaPlayer = new MediaPlayer();
			try {
				mediaPlayer.setDataSource(path);
				mediaPlayer.prepare();
				mediaPlayer.start();
				playState = true;
				// 设置播放结束时监听
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						playState = false;
						mediaPlayer.release();
						mediaPlayer = null;
						mHandler.sendEmptyMessage(1);// handler--停止播放动画
					}
				});
			} catch (IllegalArgumentException e) {
				ToastUtils.showCustomToast("播放失败");
			} catch (IllegalStateException e) {
				ToastUtils.showCustomToast("播放失败");
			} catch (IOException e) {
				ToastUtils.showCustomToast("播放失败");
			}
		}
	}

	/**
	 * 停止播放
	 */
	private void stopPlayer() {
		stopAnimation();// 停止播放动画
		if (mediaPlayer != null) {
			try {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
					mediaPlayer.release();// 释放资源
					mediaPlayer = null;
					playState = false;
				} else {
					playState = false;
				}
			} catch (IllegalStateException e) {
				playState = false;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopPlayer();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				playAnimation();
				break;
			case 1:
				stopAnimation();
				break;
			}
		}
	};

	// 播放动画
	private void playAnimation() {
		iv_voice.setImageDrawable(animationDrawable);
		if (!animationDrawable.isRunning()) {
			animationDrawable.start();
		}
	}

	// 停止播放动画
	private void stopAnimation() {
		if (animationDrawable.isRunning()) {
			animationDrawable.selectDrawable(0);
			animationDrawable.stop();
		}
	}
}
