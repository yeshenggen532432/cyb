package com.qwb.view.call.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.base.ui.BaseNoTitleActivity;
import com.qwb.view.call.model.CommentItemBean;
import com.qwb.view.call.model.CallReplyBean;
import com.qwb.utils.MyGlideUtil;
import com.qwb.utils.MyTimeUtils;
import com.xmsx.cnlife.widget.emoji.AppPanel;
import com.xmsx.cnlife.widget.AudioRecorder;
import com.xmsx.cnlife.widget.emoji.CCPEditText;
import com.xmsx.cnlife.widget.emoji.CCPTextView;
import com.xmsx.cnlife.widget.emoji.EmojiGrid.OnEmojiItemClickListener;
import com.xmsx.cnlife.widget.CircleImageView;
import com.xmsx.cnlife.widget.ComputeHeightGridView;
import com.xmsx.cnlife.widget.photo.ImagePagerActivity;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.location.ui.MapLocationActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.JsonHttpUtil;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.tree.SimpleTreeAdapter_map;
import com.qwb.view.member.model.BranchBean;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.member.model.MemberBean;
import com.qwb.view.call.model.QueryCallon;
import com.qwb.view.call.model.QueryCallon.Pic;
import com.qwb.view.call.model.QueryCallonBean2;
import com.qwb.view.call.model.CallCommentBean;
import com.qwb.view.member.model.BranchListBean2;
import com.xmsx.qiweibao.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 拜访查询模块--拜访查询
 */
public class CallQuery2Activity extends BaseNoTitleActivity implements OnEmojiItemClickListener {
	private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.BFCX_NEW, ConstantUtils.Apply.BFCX_OLD);
	private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.BFCX_NEW, ConstantUtils.Apply.BFCX_OLD);

	private PullToRefreshListView lv_pull;
	private boolean isRefresh;
	private boolean isRefresh_zuzhi;
	private int pageNo = 1;
	private ArrayList<QueryCallon> callonList = new ArrayList<>();
	private EditText edit_search;
	private String type;

	private Calendar calendar = Calendar.getInstance();
	private LinearLayout ll_search;

	private String sdate_dong;// 开始时间
	private String edate_dong;// 结束时间
	private String sdate;// 开始时间
	private String edate;// 结束时间

	private int imgWidth;// 代码中设置图片的宽高

	// 录音相关
	private ImageView bt_voice;
	private View bt_record;// 语音按钮
	private View layout_recode;// 语音按钮
	private ImageView dialog_img;
	private boolean isRecord;
	private static int RECORD_ING = 1; // 正在录音
	private static int RECODE_STATE = 0; // 录音的状态
	private static int RECODE_ED = 2; // 完成录音
	private static int RECORD_NO = 0; // 不在录音
	private static float recodeTime = 0.0f; // 录音的时间
	private static int MAX_TIME = 0; // 最长录制时间，单位秒，0为无时间限制
	private static int MIX_TIME = 1; // 最短录制时间，单位秒，0为无时间限制，建议设为1
	private static double voiceValue = 0.0; // 麦克风获取的音量值
	private AudioRecorder mr;
	private long l;
	private File file_voice;// 录音文件
	private String path_voice;// 语音路径
	private int voicePlayPosition = -1;// 记录播放动画的位置

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_call_query);

		myId = Integer.valueOf(SPUtils.getSValues("memId"));// 用户id
		scale = getResources().getDisplayMetrics().density;
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		imgWidth = (dm.widthPixels - 100) / 3;// (100:减去)

		sdate = MyTimeUtils.getToday_nyr();
		edate = MyTimeUtils.getToday_nyr();
		sdate_dong = MyTimeUtils.getToday_nyr();
		edate_dong = MyTimeUtils.getToday_nyr();

		Intent intent = getIntent();
		if (intent != null) {
			// 1:6步骤，拜访回放 2：消息评论
			entityStr2 = intent.getStringExtra("mid");
			cid = intent.getStringExtra("cid");
			sdate = intent.getStringExtra("sdate");
			edate = intent.getStringExtra("edate");
			sdate_dong = intent.getStringExtra("sdate");
			edate_dong = intent.getStringExtra("edate");
			bfTime = intent.getStringExtra("bfTime");
		}
		initUI();
		if (!MyUtils.isEmptyString(bfTime)) {
			tv_bfTime.setText(bfTime);
		}
		initData();
		creatPop();
		creatCopyPop();
	}

	// 头部
	private TextView tv_headRight;
	private TextView tv_headTitle;

	private void initHead() {
		findViewById(R.id.iv_head_back).setOnClickListener(this);
		findViewById(R.id.tv_head_right).setOnClickListener(this);
		tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
		tv_headRight = (TextView) findViewById(R.id.tv_head_right);
		tv_headTitle.setTextSize(15);
		tv_headTitle.setText("拜访量：");
		tv_headRight.setText("选择日期");
		if ("1".equals(type)) {
			tv_headRight.setVisibility(View.GONE);
		} else {
			tv_headRight.setVisibility(View.GONE);
		}
	}

	private TextView tv_bfTime;
	private String bfTime;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initUI() {
		initHead();
		tv_bfTime = (TextView) findViewById(R.id.tv_bfTime);
		TextView tv_zuzhi = (TextView) findViewById(R.id.tv_zuzhi);
		TextView tv_search = (TextView) findViewById(R.id.tv_search);
		tv_bfTime.setOnClickListener(this);
		tv_zuzhi.setOnClickListener(this);
		tv_search.setOnClickListener(this);
		ll_search = (LinearLayout) findViewById(R.id.ll_search);

		lv_pull = (PullToRefreshListView) findViewById(R.id.listView_callOnQuery);
		lv_pull.setOnRefreshListener(new OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				lv_pull.setMode(Mode.BOTH);
				isRefresh = true;
				pageNo = 1;
				bfid = "";
				initData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				isRefresh = false;
				pageNo++;
				initData();
			}
		});
		lv_pull.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 下属客户--先查看后拜访修改
				QueryCallon queryCallon = callonList.get(position - 1);
				Intent intent = new Intent(CallQuery2Activity.this, CallRecordActivity.class);
				intent.putExtra(Constans.id, queryCallon.getId());// 拜访id
				intent.putExtra(Constans.khNm, queryCallon.getKhNm());// 客户名称
				intent.putExtra(Constans.memberNm, queryCallon.getMemberNm());// 用户名
				startActivity(intent);
			}
		});
		// ListView滑动时要隐藏“评论框”
		lv_pull.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (1 == scrollState) {
					setMode(2, false);
					MyUtils.hideIMM(view);
					rl_editcontent.setVisibility(View.GONE);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
		lv_pull.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				setMode(2, false);
				MyUtils.hideIMM(view);
				rl_editcontent.setVisibility(View.GONE);
				return false;
			}
		});

		edit_search = (EditText) findViewById(R.id.edit_search);
		findViewById(R.id.iv_search).setOnClickListener(this);
		// 评论相关
		rl_editcontent = findViewById(R.id.rl_editcontent);
		et_repley = (CCPEditText) findViewById(R.id.et_repley);
		et_repley.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				setMode(2, false);
				return false;
			}
		});
		findViewById(R.id.iv_face).setOnClickListener(this);// 表情
		findViewById(R.id.bt_send).setOnClickListener(this);// 发送
		mAppPanel = (AppPanel) findViewById(R.id.ap_replay);
		mAppPanel.setOnEmojiItemClickListener(this);
		setMode(2, false);

		// 以下：评论+语音
		bt_voice = (ImageView) findViewById(R.id.bt_voice);
		bt_voice.setOnClickListener(this);
		layout_recode = findViewById(R.id.layout_recode);
		dialog_img = (ImageView) findViewById(R.id.iv_voiceProgress);
		bt_record = findViewById(R.id.record);
		bt_record.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (isRecord) {
					try {
						recoord(event);
					} catch (IOException e) {
						ToastUtils.showCustomToast("录音失败！检查sd卡是否损坏！");
					}
				}
				return false;
			}
		});
		bt_record.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				boolean sdCardExist = Environment.getExternalStorageState()
						.equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
				if (!sdCardExist) {
					ToastUtils.showCustomToast("SD卡不存在！");
				} else {
					if (RECODE_STATE != RECORD_ING) {
						l = System.currentTimeMillis();
						mr = new AudioRecorder(String.valueOf(l));
						RECODE_STATE = RECORD_ING;
						layout_recode.setVisibility(View.VISIBLE);
						try {
							mr.start();
							isRecord = true;
						} catch (IOException e) {
							ToastUtils.showCustomToast("操作失败！");
						}
						mythread();
					}
				}
				return false;
			}
		});
	}

	// 初始化数据
	private String bfid;
	private String cid;// 客户id

	private void initData() {
		String searchStr = edit_search.getText().toString().trim();

		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("pageNo", String.valueOf(1));
		params.put("pageSize", "10");
		params.put("khNm", searchStr);
		params.put("sdate", sdate);
		params.put("edate", edate);
		params.put("dataTp", dataTp);
		params.put("cid", cid);
		params.put("id", bfid);// 拜访id
		if (!MyUtils.isEmptyString(entityStr2)) {
			params.put("mids", entityStr2);// 角色
		}
		OkHttpUtils.post().params(params).url(Constans.queryBfkhLsWeb2).id(1).build().execute(new MyHttpCallback(this) {
			@Override
			public void myOnError(Call call, Exception e, int id) {

			}

			@Override
			public void myOnResponse(String response, int id) {
				jsonData(response, 1);
			}
		});
	}

	/**
	 * 评论帖子 content 内容 topicId 评论的帖子的id belongId 回复的评论id rcId 被回复的人的id rcNm
	 * 被回复人用户名
	 */
	private void initData_pingLun(String content, int voiceTime, File file_voice) {
		QueryCallon queryCallon2 = callonList.get(currentPosition);
		CommentItemBean currentCommentItemBean = new CommentItemBean();
		currentCommentItemBean.setContent(content);
		currentCommentItemBean.setMemberNm(SPUtils.getSValues("username"));
		currentCommentItemBean.setMemberId(Integer.valueOf(SPUtils.getSValues("memId")));
		currentCommentItemBean.setVoiceTime(voiceTime);

		List<CommentItemBean> commentList = queryCallon2.getCommentList();
		commentList.add(currentCommentItemBean);
		if (myAdapter != null) {
			myAdapter.notifyDataSetChanged();
		}

		et_repley.setText("");
		rl_editcontent.setVisibility(View.GONE);
		MyUtils.hideIMM(rl_editcontent);

		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("bfId", String.valueOf(queryCallon2.getId()));// 拜访id
		params.put("content", content);
		// 添加语音
		Map<String, File> files = new HashMap<>();
		if (voiceTime > 0 && file_voice != null) {
			files.put("voice", file_voice);// 语音文件
			params.put("voiceTime", String.valueOf((int) voiceTime));// 语音时长
		}
		OkHttpUtils.post().files(files).params(params).url(Constans.addBfcomment).id(4).build()
				.execute(new MyHttpCallback(this) {
					@Override
					public void myOnError(Call call, Exception e, int id) {

					}

					@Override
					public void myOnResponse(String response, int id) {
						jsonData(response, 4);
					}
				});
	}

	// 删除评论
	private void delComment(String topicId, String commentId) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("commentId", commentId);
		OkHttpUtils.post().params(params).url(Constans.deleteBfComment).id(5).build().execute(new MyHttpCallback(this) {
			@Override
			public void myOnError(Call call, Exception e, int id) {

			}

			@Override
			public void myOnResponse(String response, int id) {
				jsonData(response, 5);
			}
		});
	}

	/**
	 * 参数:String token,Integer topicId(主题/话题ID 必填),String content(发表内容
	 * 必填),回复时必填: belongId(回复的评论id或回复回复的id),rcId（被回复人id）,rcNm（被回复人用户名）
	 */
	private CallReplyBean currentRcBean;// 回复成功后吧该条评论的id附上去

	private void initData_reply(String content, String rcNm, List<CallReplyBean> rcList, int commentId, String memberNm2,
								int memberId, int voiceTime, File file_voice) {
		QueryCallon queryCallon2 = callonList.get(currentPosition);
		int id = queryCallon2.getId();

		currentRcBean = new CallReplyBean();
		currentRcBean.setContent(content);
		currentRcBean.setMemberNm(SPUtils.getSValues("username"));
		currentRcBean.setMemberId(myId);
		currentRcBean.setRcNm(rcNm);
		currentRcBean.setVoiceTime(voiceTime);
		rcList.add(currentRcBean);

		refreshAdapter2();
		et_repley.setText("");
		et_repley.setHint("");
		rl_editcontent.setVisibility(View.GONE);
		MyUtils.hideIMM(rl_editcontent);

		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("content", content);
		params.put("bfId", String.valueOf(id));// 拜访id
		params.put("belongId", String.valueOf(commentId));
		params.put("rcNm", memberNm2);
		params.put("rcId", String.valueOf(memberId));
		// 添加语音
		Map<String, File> files = new HashMap<>();
		if (voiceTime > 0 && file_voice != null) {
			files.put("voice", file_voice);// 语音文件
			params.put("voiceTime", String.valueOf((int) voiceTime));// 语音时长
		}
		OkHttpUtils.post().files(files).params(params).url(Constans.addBfcomment).id(4).build()
				.execute(new MyHttpCallback(this) {
					@Override
					public void myOnError(Call call, Exception e, int id) {

					}

					@Override
					public void myOnResponse(String response, int id) {
						jsonData(response, 4);
					}
				});
	}

	// 获取部门以及成员信息
	private void initData_zuzhi() {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("dataTp", dataTp);// 角色
		if ("4".equals(dataTp)) {
			params.put("mids", dataTpMids);// 角色
		}
		OkHttpUtils.post().params(params).url(Constans.queryDepartMemLs).id(3).build().execute(new MyHttpCallback(this) {
			@Override
			public void myOnError(Call call, Exception e, int id) {

			}

			@Override
			public void myOnResponse(String response, int id) {
				jsonData(response, 3);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_head_back:
			finish();
			break;
		case R.id.iv_search:// 搜索
			String searchStr = edit_search.getText().toString().trim();
			if (MyUtils.isEmptyString(searchStr)) {
				ToastUtils.showCustomToast("请输入关键字");
				return;
			}
			pageNo = 1;
			isRefresh = true;
			bfid = "";
			initData();
			break;
		case R.id.tv_head_right:// 选择时间
			showStarData();
			break;
		case R.id.tv_startTime:// 开始时间
			timeType = 0;
			showStarData();
			break;
		case R.id.tv_endTime:// 结束时间
			timeType = 1;
			showStarData();
			break;
		case R.id.tv_bfTime:// 拜访时间
			if (popWin == null) {
				initPopup_time();
			}
			backgroundAlpha(0.5f);
			popWin.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
			break;
		case R.id.tv_zuzhi:// 组织架构
			if (popWin_zuzhi == null) {
				initPopup2();
			}
			if (mDatas != null && mDatas.size() == 0) {
				isRefresh_zuzhi = true;
				initData_zuzhi();
			} else {
				backgroundAlpha(0.5f);
				popWin_zuzhi.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
				refreshAdapter_zuzhi();
			}
			break;
		case R.id.btn_confirm:// 确定--组织架构
			queding_zuzhi();
			break;
		case R.id.tv_search:// 搜索
			ll_search.getVisibility();
			if (ll_search.getVisibility() == ll_search.VISIBLE) {
				ll_search.setVisibility(View.GONE);
			} else {
				ll_search.setVisibility(View.VISIBLE);
			}
			break;
		// 发送评论
		case R.id.bt_send:
			String content = et_repley.getText().toString();
			if (!TextUtils.isEmpty(content)) {
				if (isReply) {
					if (isSecondReply) {// 第二次回复
						initData_reply(content, rcBean.getMemberNm(), commentBean.getRcList(),
								commentBean.getCommentId(), rcBean.getMemberNm(), rcBean.getMemberId(), 0, null);
					} else {// 首次回复
						initData_reply(content, commentBean.getMemberNm(), commentBean.getRcList(),
								commentBean.getCommentId(), commentBean.getMemberNm(), commentBean.getMemberId(), 0,
								null);
					}
				} else {
					initData_pingLun(content, 0, null);
				}
			} else {
				ToastUtils.showCustomToast("内容不能为空！");
			}
			break;
		case R.id.iv_face:// 表情
			MyUtils.hideIMM(v);
			int mode = mAppPanel.isPanelVisible() ? 2 : 3;
			setMode(mode, false);
			break;
		case R.id.bt_voice:// 切换 录音模式
			// 切换 录音模式
			setMode(2, false);
			int isV = bt_record.getVisibility();
			if (View.VISIBLE == isV) {
				// 录音按钮显示此时要切换成编辑文字模式
				bt_voice.setImageResource(R.drawable.ht_ly);
				bt_record.setVisibility(View.GONE);
				if (et_repley != null) {
					rl_editcontent.setVisibility(View.VISIBLE);
					et_repley.setHint("输入评论内容");
					MyUtils.opendIMM(et_repley);
				}
			} else {
				// 录音按钮非显示此时要切换成录音模式
				bt_voice.setImageResource(R.drawable.ht_jp);
				bt_record.setVisibility(View.VISIBLE);
				MyUtils.hideIMM(v);
			}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 清空组织架构
		Constans.branchMap.clear();
		Constans.memberMap.clear();
		Constans.ziTrueMap.clear();
		Constans.ParentTrueMap2.clear();// 0:没选中，1：全选中，2：部分选中
		// 界面停止时，停止播放
		stopPlayer();
	}

	// 确定--组织架构
	private List<Integer> memIdList = new ArrayList<Integer>();
	private String entityStr2 = "";

	private void queding_zuzhi() {
		pageNo = 1;
		isRefresh = true;
		bfid = "";
		// 成员
		memIdList.clear();
		Iterator<Integer> iter = Constans.ziTrueMap.keySet().iterator();
		while (iter.hasNext()) {
			Integer key = iter.next();
			Boolean val = Constans.ziTrueMap.get(key);
			if (val) {
				MemberBean memberBean = Constans.memberMap.get(key);
				if (memberBean != null) {
					Integer memberId = memberBean.getMemberId();
					memIdList.add(memberId);
				}
			}
		}
		// 成员
		String entityStr = JSON.toJSONString(memIdList);
		entityStr2 = entityStr.substring(1, entityStr.length() - 1);
		// SPUtils.setValues("memberIds_chagang" + SPUtils.getID(),
		// entityStr2);// 成员ID数组
		initData();
		popWin_zuzhi.dismiss();
		// 部门
		String branchStr = JSON.toJSONString(Constans.ParentTrueMap2);// 格式如--{3:0,5:2,7:1,8:0}
		String branchStr2 = branchStr.substring(1, branchStr.length() - 1);// 去掉{}
		// SPUtils.setValues("branchIds_chagang" + SPUtils.getID(),
		// branchStr2);// 部门ID数组
	}

	MyAdapter myAdapter;

	private void refreshAdapter2() {
		if (myAdapter == null) {
			myAdapter = new MyAdapter();
			lv_pull.setAdapter(myAdapter);
		} else {
			myAdapter.notifyDataSetChanged();
		}
	}

	// 刷新--组织架构
	private List<TreeBean> mDatas = new ArrayList<TreeBean>();
	private SimpleTreeAdapter_map<TreeBean> mAdapter;

	private void refreshAdapter_zuzhi() {
		if ((mDatas != null && mDatas.size() > 0)) {
			if (mAdapter == null) {
				try {
					mAdapter = new SimpleTreeAdapter_map<TreeBean>(mTree, this, mDatas, 0, false);
					mTree.setAdapter(mAdapter);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} else {
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	// 显示开始日期
	private int timeType;// 开始时间:0，结束时间:1
	private void showStarData() {
		String date = null;
		if (0 == timeType){
			date = tv_startTime.getText().toString().trim();
		}else if(1 == timeType){
			date = tv_endTime.getText().toString().trim();
		}
		if (!MyStringUtil.isValidDate(date)){
			date = MyTimeUtils.getToday_nyr();
		}
		new com.qwb.widget.MyDatePickerDialog(this, null, date, new com.qwb.widget.MyDatePickerDialog.DateTimeListener() {
			@Override
			public void onSetTime(int year, int month, int day, String timeStr) {
				if (0 == timeType){
					sdate_dong = timeStr;
					tv_startTime.setText(timeStr);
				}else if(1 == timeType){
					edate_dong = timeStr;
					tv_endTime.setText(timeStr);
				}
			}

			@Override
			public void onCancel() {
			}
		}).show();
	}

	/*
	 * 筛选--窗体
	 */
	private View contentView;
	private PopupWindow popWin;
	private TextView tv_startTime;
	private TextView tv_endTime;

	private void initPopup_time() {
		contentView = getLayoutInflater().inflate(R.layout.x_popwin_callon_query, null);
		// 默认设置开始时间，结束时间
		tv_startTime = (TextView) contentView.findViewById(R.id.tv_startTime);
		tv_endTime = (TextView) contentView.findViewById(R.id.tv_endTime);
		tv_startTime.setText(MyTimeUtils.getToday_nyr());
		tv_endTime.setText(MyTimeUtils.getToday_nyr());
		tv_startTime.setOnClickListener(this);
		tv_endTime.setOnClickListener(this);
		tv_startTime.setEnabled(false);
		tv_endTime.setEnabled(false);
		tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
		tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
		// 自定义，今天，昨天等等
		final RadioGroup radioGroup = (RadioGroup) contentView.findViewById(R.id.radioGroup);

		popWin = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
				true);
		popWin.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWin.setBackgroundDrawable(new BitmapDrawable());
		popWin.setOnDismissListener(new poponDismissListener());

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio0:
					setData_time("今天", false, R.drawable.shape_kuang_gray3, MyTimeUtils.getToday_nyr(),
							MyTimeUtils.getToday_nyr());
					break;
				case R.id.radio1:
					setData_time("昨天", false, R.drawable.shape_kuang_gray3, MyTimeUtils.getYesterday(),
							MyTimeUtils.getYesterday());
					break;
				case R.id.radio2:
					setData_time("本周", false, R.drawable.shape_kuang_gray3, MyTimeUtils.getFirstOfThisWeek(),
							MyTimeUtils.getLastOfThisWeek());
					break;
				case R.id.radio3:
					setData_time("上周", false, R.drawable.shape_kuang_gray3, MyTimeUtils.getFirstOfShangWeek(),
							MyTimeUtils.getLastOfShangWeek());
					break;
				case R.id.radio4:
					setData_time("本月", false, R.drawable.shape_kuang_gray3, MyTimeUtils.getFirstOfMonth(),
							MyTimeUtils.getLastOfMonth());
					break;
				case R.id.radio5:
					setData_time("上月", false, R.drawable.shape_kuang_gray3, MyTimeUtils.getFirstOfShangMonth(),
							MyTimeUtils.getLastOfShangMonth());
					break;
				case R.id.radio6:
					setData_time("自定义", true, R.drawable.shape_kuang_gray, tv_startTime.getText().toString(),
							tv_endTime.getText().toString());
					break;
				}
			}
		});
		// 取消
		contentView.findViewById(R.id.btn_quxiao).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popWin.dismiss();
			}
		});
		// 确定
		contentView.findViewById(R.id.btn_confirm).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popWin.dismiss();
				pageNo = 1;
				isRefresh = true;
				bfid = "";
				sdate = sdate_dong;
				edate = edate_dong;
				initData();
				tv_bfTime.setText(bfTime);
			}
		});
	}

	// 设置时间等状态，今天，昨天，本周，上周，本月，上月
	private void setData_time(String timeStr, boolean isEnable, int res, String sdate, String edate) {
		bfTime = timeStr;
		tv_startTime.setEnabled(isEnable);
		tv_endTime.setEnabled(isEnable);
		tv_startTime.setBackgroundResource(res);
		tv_endTime.setBackgroundResource(res);
		sdate_dong = sdate;
		edate_dong = edate;
	}

	/*
	 * 窗体--组织架构
	 */
	private ListView mTree;
	private PopupWindow popWin_zuzhi = null;

	private void initPopup2() {
		View contentView = getLayoutInflater().inflate(R.layout.x_popup_frame, null);
		mTree = (ListView) contentView.findViewById(R.id.id_tree);
		TextView tv_name = (TextView) contentView.findViewById(R.id.tv_title);
		tv_name.setText("选择部门或成员");
		contentView.findViewById(R.id.btn_confirm).setOnClickListener(this);
		popWin_zuzhi = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		popWin_zuzhi.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWin_zuzhi.setBackgroundDrawable(new BitmapDrawable());
		popWin_zuzhi.setOnDismissListener(new poponDismissListener());
		// 重置
		contentView.findViewById(R.id.btn_reset).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Constans.ziTrueMap.clear();
				Constans.ParentTrueMap2.clear();
				refreshAdapter_zuzhi();
			}
		});
	}

	/*
	 * 窗体的背景透明度0~1
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	/*
	 * 窗体消失监听
	 */
	class poponDismissListener implements PopupWindow.OnDismissListener {
		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}
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
		if (lv_pull != null) {
			lv_pull.onRefreshComplete();
		}
		if (!MyUtils.isEmptyString(json) && json.startsWith("{")) {
			switch (id) {
			case 1:// 查询
				QueryCallonBean2 parseObject = JSON.parseObject(json, QueryCallonBean2.class);
				if (parseObject != null && parseObject.isState()) {
					lv_pull.setMode(Mode.BOTH);
					if (isRefresh) {// 刷新清空
						callonList.clear();
					}
					List<QueryCallon> rows = parseObject.getRows();
					if (rows != null) {
						if (rows.size() > 0) {
							callonList.addAll(rows);
							if (MyUtils.isEmptyString(bfid)) {
								tv_headTitle
										.setText("拜访次数：" + parseObject.getTotal() + "  拜访家数：" + parseObject.getNum());
							}
							// 获取数据的最后一条拜访id
							QueryCallon queryCallon = rows.get(rows.size() - 1);
							if (queryCallon.getId() != 0) {
								bfid = "" + queryCallon.getId();
							}
						} else {
							tv_headTitle.setText("拜访次数：" + parseObject.getTotal() + "  拜访家数：" + parseObject.getNum());
							ToastUtils.showCustomToast("没有更多数据");
						}
						refreshAdapter2();
					}
				} else {
					ToastUtils.showCustomToast(parseObject.getMsg());
				}
				break;
			case 7:// 查询--消息评论
				CallCommentBean data = JSON.parseObject(json, CallCommentBean.class);
				if (data != null && data.isState()) {
					QueryCallon row = data.getXx();
					if (row != null) {
						callonList.add(row);
						refreshAdapter2();
						tv_headTitle.setText("拜访次数：" + callonList.size() + "  拜访家数：" + callonList.size());
					}
				} else {
					ToastUtils.showCustomToast(data.getMsg());
				}
				break;

			case 3:// 组织架构
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
									Constans.branchMap.put(branchId, branchBean);// 父

									// 第二层
									if (memls2 != null && memls2.size() > 0) {
										for (int j = 0; j < memls2.size(); j++) {
											MemberBean memberBean = memls2.get(j);
											Integer memberId = memberBean.getMemberId() + 100000;// 默认+10万--防止父ID与子ID重复
											String memberNm = memberBean.getMemberNm();
											if (memberId != null && memberNm != null) {
												mDatas.add(new TreeBean(memberId, branchId, memberNm));
												Constans.memberMap.put(memberId, memberBean);// 子
											}
										}
									}
								}
							}
						}
						if (isRefresh_zuzhi) {
							backgroundAlpha(0.5f);
							popWin_zuzhi.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
							refreshAdapter_zuzhi();
						}
					} else {
						ToastUtils.showCustomToast(parseObject3.getMsg());
					}
				}
			}
		}
	}

	private final class MyAdapter extends BaseAdapter {
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.x_adapter_callonquery2, null);
			}
			CircleImageView iv_memberHead = MyUtils.getViewFromVH(convertView, R.id.iv_memberHead);
			TextView tv_memberNm = MyUtils.getViewFromVH(convertView, R.id.tv_memberNm);
			TextView tv_branchName = MyUtils.getViewFromVH(convertView, R.id.tv_branchName);
			TextView tv_khNm = MyUtils.getViewFromVH(convertView, R.id.tv_khNm);
			TextView tv_bcbfzj = MyUtils.getViewFromVH(convertView, R.id.tv_bcbfzj);// 拜访总结
			TextView tv_time_duan = MyUtils.getViewFromVH(convertView, R.id.tv_time_duan);// 时段
			TextView tv_time_chang = MyUtils.getViewFromVH(convertView, R.id.tv_time_chang);// 时长
			TextView tv_address = MyUtils.getViewFromVH(convertView, R.id.tv_address);// 地址
			LinearLayout ll_address = MyUtils.getViewFromVH(convertView, R.id.ll_address);// 地址
			TextView tv_jl = MyUtils.getViewFromVH(convertView, R.id.tv_jl);// 距离
			TextView tv_zfcount = MyUtils.getViewFromVH(convertView, R.id.tv_zfcount);// 客户重复
			ComputeHeightGridView grideView = MyUtils.getViewFromVH(convertView, R.id.grideView);
			// 语音
			LinearLayout ll_voice = MyUtils.getViewFromVH(convertView, R.id.ll_voice);// 语音
			TextView tv_voicetime = MyUtils.getViewFromVH(convertView, R.id.tv_voicetime);// 语音
			final ImageView iv_voice = MyUtils.getViewFromVH(convertView, R.id.iv_voice);// 语音

			// 评论
			final View iv_zang_pinglun = MyUtils.getViewFromVH(convertView, R.id.iv_zang_pinglun);// 评论
			final View rl_zang_pinglun = MyUtils.getViewFromVH(convertView, R.id.rl_zang_pinglun);
			final TextView tv_zang_num = MyUtils.getViewFromVH(convertView, R.id.tv_zang_num);// 点赞
			final View tv_zang_line = MyUtils.getViewFromVH(convertView, R.id.tv_zang_line);

			final QueryCallon data = callonList.get(position);
			if (data != null) {
				tv_zang_num.setVisibility(View.GONE);// 点赞
				tv_zang_line.setVisibility(View.GONE);// 点赞

				MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + data.getMemberHead(), iv_memberHead);
				tv_memberNm.setText(data.getMemberNm());
				tv_branchName.setText("(" + data.getBranchName() + ")");
				tv_khNm.setText(data.getKhNm());
				tv_bcbfzj.setText(data.getBcbfzj());
				tv_time_duan.setText(data.getQddate());
				tv_time_chang.setText(data.getQdtime());
				tv_address.setText(data.getAddress());
				tv_jl.setText(data.getJlm() + "m");
				// 语音处理
				final String path_voice = data.getVoiceUrl();
				if (MyUtils.isEmptyString(path_voice)) {
					ll_voice.setVisibility(View.GONE);
				} else {
					// TODO ListView滑动可以继续播放动画(记录播放动画位置的区分)
					if (playState) {
						if (data.getId() == voicePlayPosition) {
							iv_voice.setId(data.getId());
							iv_playVoice = null;
							iv_playVoice = iv_voice;
							iv_playVoice.setImageResource(R.drawable.voice_from_playing_s0);
							iv_playVoice.setImageResource(R.drawable.animation_left);
							AnimationDrawable drawable = (AnimationDrawable) iv_playVoice.getDrawable();
							drawable.start();
						} else {
							iv_voice.setImageResource(R.drawable.voice_from_playing_s0);
						}
					} else {
						iv_voice.setImageResource(R.drawable.voice_from_playing_s0);
					}

					ll_voice.setVisibility(View.VISIBLE);
					tv_voicetime.setText(String.valueOf(data.getVoiceTime()) + "\"");
					iv_voice.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							iv_voice.setId(data.getId());// TODO 记录设置要播放动画的位置
							int id = data.getId();// 拜访id
							setVoice(path_voice, id, iv_voice);
							bfid_play = id;

						}
					});
				}
				// 地址--签到地址，签退地址，客户地址
				ll_address.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(CallQuery2Activity.this, MapLocationActivity.class);
						intent.putExtra("CallQueryActivity", data);
						intent.putExtra(Constans.type, 4);
						startActivity(intent);
					}
				});

				final List<Pic> listpic = data.getListpic();
				if (listpic != null && listpic.size() > 0) {
					grideView.setVisibility(View.VISIBLE);
					grideView.setAdapter(new BaseAdapter() {
						@Override
						public View getView(int position, View convertView, ViewGroup parent) {
							if (convertView == null) {
								convertView = LayoutInflater.from(parent.getContext())
										.inflate(R.layout.x_adapter_photo_callon, null);
							}
							Pic pic = listpic.get(position);
							ImageView iv_simple = MyUtils.getViewFromVH(convertView, R.id.iv_simple);
							MyGlideUtil.getInstance().displayImageSquere(Constans.IMGROOTHOST + pic.getPicMin(), iv_simple);
							TextView tv_title = MyUtils.getViewFromVH(convertView, R.id.tv_title);
							if (!MyUtils.isEmptyString(pic.getNm())) {
								tv_title.setText(pic.getNm());
							}
							// 代码中设置图片ImageView的宽高-宽高比例：1:1
							RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_simple
									.getLayoutParams();
							params.width = imgWidth;
							params.height = imgWidth / 5 * 4;
							iv_simple.setLayoutParams(params);
							return convertView;
						}

						@Override
						public long getItemId(int position) {
							return 0;
						}

						@Override
						public Object getItem(int position) {
							return null;
						}

						@Override
						public int getCount() {
							return listpic.size();
						}
					});

					grideView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							String[] urls = new String[listpic.size()];
							for (int i = 0; i < listpic.size(); i++) {
								urls[i] = Constans.IMGROOTHOST + listpic.get(i).getPic();
							}
							// 点击放大图片
							Intent intent = new Intent(CallQuery2Activity.this, ImagePagerActivity.class);
							intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
							intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
							startActivity(intent);
						}
					});
				} else {
					grideView.setVisibility(View.GONE);
				}
				// 头像-跳转新的界面
				iv_memberHead.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(CallQuery2Activity.this, CallQuery2Activity.class);
						intent.putExtra("mid", "" + data.getMid());// 查看那个业务员id
						intent.putExtra("sdate", sdate);// 开始时间
						intent.putExtra("edate", edate);// 结束时间
						intent.putExtra("bfTime", tv_bfTime.getText().toString().trim());// 时间
						startActivity(intent);
					}
				});
				// 客户重复
				int zfcount = data.getZfcount();
				if (zfcount < 2) {
					tv_zfcount.setVisibility(View.GONE);
				} else {
					tv_zfcount.setVisibility(View.GONE);// 备注单个是没有“客户重复”
					tv_zfcount.setText("客户重复" + zfcount);
					tv_zfcount.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(CallQuery2Activity.this, CallQuery2Activity.class);
							intent.putExtra("cid", "" + data.getCid());// 客户id
							intent.putExtra("sdate", sdate);// 开始时间
							intent.putExtra("edate", edate);// 结束时间
							intent.putExtra("bfTime", tv_bfTime.getText().toString().trim());// 时间
							startActivity(intent);
						}
					});
				}
				// 评论
				iv_zang_pinglun.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int[] location = new int[2];
						iv_zang_pinglun.getLocationOnScreen(location);
						int x = location[0];
						int y = location[1];
						Log.e("x_activity_rz_mobile", "x_activity_rz_mobile-" + x);
						Log.e("popXPosition", "popXPosition-" + popXPosition);
						mPopupWindow.showAtLocation(iv_zang_pinglun, Gravity.NO_GRAVITY, x - popXPosition,
								(int) (y - 5 * scale));
						currentPosition = position;
					}
				});

				// 评论列表
				List<CommentItemBean> commentList = data.getCommentList();
				if (commentList == null) {
					rl_zang_pinglun.setVisibility(View.GONE);
				} else {
					rl_zang_pinglun.setVisibility(View.VISIBLE);
				}
				if (commentList != null && commentList.size() <= 0) {
					rl_zang_pinglun.setVisibility(View.GONE);
				} else {
					rl_zang_pinglun.setVisibility(View.VISIBLE);
				}

				LinearLayout ll_commlist = MyUtils.getViewFromVH(convertView, R.id.ll_commlist);
				ll_commlist.removeAllViews();
				if (commentList != null && commentList.size() > 0) {
					ll_commlist.setVisibility(View.VISIBLE);
					for (int i = 0; i < commentList.size(); i++) {
						CommentItemBean commentItemBean = commentList.get(i);
						commentItemBean.setPosition(position);

						// 动态创建一个相对布局
						LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						lp2.setMargins(0, 20, 5, 20);
						lp3.height = MyUtils.getPXfromDP(30, CallQuery2Activity.this);// dp转px
						lp3.width = MyUtils.getPXfromDP(60, CallQuery2Activity.this);// dp转px

						LinearLayout lLayout = new LinearLayout(CallQuery2Activity.this);
						lLayout.setGravity(Gravity.CENTER_VERTICAL);
						lLayout.setLayoutParams(lp1);
						// 动态创建一个文本
						CCPTextView textView = new CCPTextView(CallQuery2Activity.this);
						textView.setGravity(Gravity.CENTER_VERTICAL);
						textView.setTag(R.id.tag_commbean, commentItemBean);
						textView.setOnClickListener(myClickListener);// 评论点击监听
						textView.setLayoutParams(lp2);
						// 动态创建一个语音按钮
						CCPTextView tv_voice = new CCPTextView(CallQuery2Activity.this);
						tv_voice.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
						tv_voice.setLayoutParams(lp2);
						//
						ImageView img_voice = new ImageView(CallQuery2Activity.this);
						img_voice.setBackgroundResource(R.drawable.chat_bg_arrow_left);
						img_voice.setImageResource(R.drawable.voice_from_playing_s0);
						img_voice.setPadding(0, 5, 5, 5);
						img_voice.setLayoutParams(lp3);
						img_voice.setId(commentItemBean.getCommentId());// TODO
																		// 记录播放动画的位置
						img_voice.setTag(R.id.tag_commbean, commentItemBean);
						img_voice.setOnClickListener(voiceClickListener);// 评论点击监听
						// TODO ListView滑动可以继续播放动画(记录播放动画位置的区分)
						if (playState) {
							if (commentItemBean.getCommentId() == voicePlayPosition) {
								img_voice.setId(commentItemBean.getCommentId());
								iv_playVoice = null;
								iv_playVoice = img_voice;
								iv_playVoice.setImageResource(R.drawable.voice_from_playing_s0);
								iv_playVoice.setImageResource(R.drawable.animation_left);
								AnimationDrawable drawable = (AnimationDrawable) iv_playVoice.getDrawable();
								drawable.start();
							} else {
								img_voice.setImageResource(R.drawable.voice_from_playing_s0);
							}
						} else {
							img_voice.setImageResource(R.drawable.voice_from_playing_s0);
						}

						StringBuilder builder = new StringBuilder();
						String memberNm = commentItemBean.getMemberNm();
						int voiceTime = commentItemBean.getVoiceTime();
						builder.append(memberNm);
						if (voiceTime > 0) {
							builder.append(":");
							tv_voice.setText(commentItemBean.getVoiceTime() + "\"");
						} else {
							builder.append(":" + commentItemBean.getContent());
						}
						SpannableString comm_analyseString = textView.analyseString(builder.toString());
						SpannableStringBuilder comm_sb = new SpannableStringBuilder(comm_analyseString);
						if (!MyUtils.isEmptyString(memberNm)) {
							int length = memberNm.length();
							comm_sb.setSpan(new ForegroundColorSpan(Color.parseColor("#0082CE")), 0, length,
									Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						textView.setText(comm_sb);
						textView.setTag(R.id.tag_copy, commentItemBean.getContent());
						textView.setOnLongClickListener(myCopyLongClickListener);// 评论长按监听--复制内容
						// 动态添加布局-评论
						if (voiceTime > 0) {
							lLayout.addView(textView);
							lLayout.addView(img_voice);
							lLayout.addView(tv_voice);
							ll_commlist.addView(lLayout);
						} else {
							lp1.setMargins(0, 20, 0, 20);
							ll_commlist.addView(textView, lp1);
						}

						// 回复该条评论的列表
						List<CallReplyBean> rcList = commentItemBean.getRcList();
						if (rcList != null && rcList.size() > 0) {
							for (int j = 0; j < rcList.size(); j++) {
								CallReplyBean rcBean = rcList.get(j);
								rcBean.setRcPosition(j);
								// 动态创建一个相对布局
								LinearLayout.LayoutParams lp1_hf = new LinearLayout.LayoutParams(
										ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
								LinearLayout.LayoutParams lp2_hf = new LinearLayout.LayoutParams(
										ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
								LinearLayout.LayoutParams lp3_hf = new LinearLayout.LayoutParams(
										ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
								lp2_hf.setMargins(0, 20, 5, 20);
								lp3_hf.height = MyUtils.getPXfromDP(30, CallQuery2Activity.this);// dp转px
								lp3_hf.width = MyUtils.getPXfromDP(60, CallQuery2Activity.this);// dp转px

								// 动态创建一个相对布局
								LinearLayout ll_hf = new LinearLayout(CallQuery2Activity.this);
								ll_hf.setGravity(Gravity.CENTER_VERTICAL);
								ll_hf.setLayoutParams(lp1_hf);

								CCPTextView rctextView = new CCPTextView(CallQuery2Activity.this);
								rctextView.setGravity(Gravity.CENTER_VERTICAL);
								rctextView.setTag(R.id.tag_commbean, commentItemBean);
								rctextView.setTag(R.id.tag_rcbean, rcBean);
								rctextView.setOnClickListener(myRcClickListener);// 回复评论点击监听
								rctextView.setLayoutParams(lp2_hf);
								// 动态创建一个语音按钮
								CCPTextView tv_voice_hf = new CCPTextView(CallQuery2Activity.this);
								tv_voice_hf.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
								//
								ImageView img_voice_hf = new ImageView(CallQuery2Activity.this);
								img_voice_hf.setBackgroundResource(R.drawable.chat_bg_arrow_left);
								img_voice_hf.setImageResource(R.drawable.voice_from_playing_s0);
								img_voice_hf.setPadding(0, 5, 5, 5);
								img_voice_hf.setLayoutParams(lp3_hf);
								img_voice_hf.setId(rcBean.getCommentId());// TODO
																			// 记录播放动画的位置
								img_voice_hf.setTag(R.id.tag_commbean, commentItemBean);
								img_voice_hf.setTag(R.id.tag_rcbean, rcBean);
								img_voice_hf.setOnClickListener(voiceClickListener_hf);// 评论点击监听
								// TODO ListView滑动可以继续播放动画(记录播放动画位置的区分)
								if (playState) {
									if (rcBean.getCommentId() == voicePlayPosition) {
										img_voice_hf.setId(rcBean.getCommentId());
										iv_playVoice = null;
										iv_playVoice = img_voice_hf;
										iv_playVoice.setImageResource(R.drawable.voice_from_playing_s0);
										iv_playVoice.setImageResource(R.drawable.animation_left);
										AnimationDrawable drawable = (AnimationDrawable) iv_playVoice.getDrawable();
										drawable.start();
									} else {
										img_voice_hf.setImageResource(R.drawable.voice_from_playing_s0);
									}
								} else {
									img_voice_hf.setImageResource(R.drawable.voice_from_playing_s0);
								}

								StringBuilder builder_rc = new StringBuilder();
								String memberNm2 = rcBean.getMemberNm();
								int voiceTime_hf = rcBean.getVoiceTime();
								rcBean.getVoiceTime();
								builder_rc.append(memberNm2);
								builder_rc.append("回复");
								String rcNm = rcBean.getRcNm();
								builder_rc.append(rcNm);
								if (voiceTime_hf > 0) {
									builder_rc.append(":");
									tv_voice_hf.setText(rcBean.getVoiceTime() + "\"");
								} else {
									builder_rc.append(":" + rcBean.getContent());
								}

								SpannableString analyseString = rctextView.analyseString(builder_rc.toString());
								int length = 0;
								SpannableStringBuilder sb = new SpannableStringBuilder(analyseString);
								if (!MyUtils.isEmptyString(memberNm2)) {
									length = memberNm2.length();
									sb.setSpan(new ForegroundColorSpan(Color.parseColor("#0082CE")), 0, length,
											Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
								if (!MyUtils.isEmptyString(rcNm)) {
									int length2 = rcNm.length();
									sb.setSpan(new ForegroundColorSpan(Color.parseColor("#0082CE")), length + 2,
											length + 2 + length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								}
								rctextView.setText(sb);
								rctextView.setTag(R.id.tag_copy, rcBean.getContent());
								rctextView.setOnLongClickListener(myCopyLongClickListener);// 回复评论长按监听--复制内容

								// 动态添加布局-回复评论
								if (voiceTime_hf > 0) {
									ll_hf.addView(rctextView);
									ll_hf.addView(img_voice_hf);
									ll_hf.addView(tv_voice_hf);
									ll_commlist.addView(ll_hf);
								} else {
									lp1_hf.setMargins(0, 20, 0, 20);
									ll_commlist.addView(rctextView, lp1_hf);
								}
							}
						}
					}
				} else {
					ll_commlist.setVisibility(View.GONE);
					tv_zang_line.setVisibility(View.INVISIBLE);
				}
			}
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public int getCount() {
			return callonList.size();
		}
	}

	private PopupWindow mPopupWindow;
	private int popXPosition;
	private float scale;
	private boolean isReply;// true:回复
	private View rl_editcontent;
	private CCPEditText et_repley;
	private AppPanel mAppPanel;
	private int currentPosition;// 该条数据在帖子列表的位置

	private void creatPop() {
		View view = getLayoutInflater().inflate(R.layout.x_popup_photowall_replace, null);
		View tv_ding_save = view.findViewById(R.id.tv_ding_save);
		View tv_replay_save = view.findViewById(R.id.tv_replay_save);
		tv_ding_save.setVisibility(View.GONE);// 点赞
		popXPosition = (int) (70 * scale);
		tv_ding_save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// zang();
				mPopupWindow.dismiss();
			}
		});
		tv_replay_save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isReply = false;
				mPopupWindow.dismiss();
				// if (rl_editcontent.getVisibility() == View.GONE) {
				// 切换 录音模式
				setMode(2, false);
				rl_editcontent.setVisibility(View.VISIBLE);
				int isV = bt_record.getVisibility();
				if (View.VISIBLE == isV) {
					// 录音按钮非显示此时要切换成录音模式
					bt_voice.setImageResource(R.drawable.ht_jp);
					bt_record.setVisibility(View.VISIBLE);
					MyUtils.hideIMM(v);
				} else {
					// 录音按钮显示此时要切换成编辑文字模式
					bt_voice.setImageResource(R.drawable.ht_ly);
					bt_record.setVisibility(View.GONE);
					if (et_repley != null) {
						et_repley.setHint("输入评论内容");
						MyUtils.opendIMM(et_repley);
					}
				}
				// }
			}
		});
		mPopupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setAnimationStyle(R.style.MyPopAnimation);
	}

	// 切换表情
	public void setMode(int mode, boolean input) {
		switch (mode) {
		case 1:
			resetChatFooter(false);
			break;
		case 2:
			resetChatFooter(true, false);
			break;
		case 3:
			resetChatFooter(true, true);
			break;
		}
	}

	private void resetChatFooter(boolean isTools) {
		resetChatFooter(isTools, false);
	}

	private void resetChatFooter(boolean isTools, boolean isEmoji) {
		if (!isTools) {
			mAppPanel.setPanelGone();
			return;
		}
		if (isEmoji) {
			mAppPanel.swicthToPanel(AppPanel.APP_PANEL_NAME_DEFAULT);
			mAppPanel.setVisibility(View.VISIBLE);
		} else {
			mAppPanel.setPanelGone();
		}
	}

	@Override
	public void onEmojiItemClick(int emojiid, String emojiName) {
		et_repley.requestFocus();
		et_repley.setEmojiText(emojiName);
	}

	@Override
	public void onEmojiDelClick() {
		et_repley.getInputConnection().sendKeyEvent(new KeyEvent(MotionEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
	}

	private boolean isSecondReply;
	private CommentItemBean commentBean;
	private int myId;
	// 评论点击事件
	private OnClickListener myClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			isSecondReply = false;
			TextView tv_comment = (TextView) v;
			commentBean = (CommentItemBean) tv_comment.getTag(R.id.tag_commbean);
			currentPosition = commentBean.getPosition();
			if (myId == commentBean.getMemberId()) {
				// 删除回复
				AlertDialog alertDialog = new AlertDialog.Builder(CallQuery2Activity.this).setMessage("删除评论")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						callonList.get(currentPosition).getCommentList().remove(commentBean);
						if (commentBean.getCommentId() != 0) {
							delComment(String.valueOf(callonList.get(currentPosition).getId()),
									String.valueOf(commentBean.getCommentId()));
						}
						refreshAdapter2();
					}
				}).setNegativeButton("取消", null).create();
				alertDialog.show();
			} else {
				isReply = true;
				// 切换 录音模式
				setMode(2, false);
				rl_editcontent.setVisibility(View.VISIBLE);
				int isV = bt_record.getVisibility();
				if (View.VISIBLE == isV) {
					// 录音按钮非显示此时要切换成录音模式
					bt_voice.setImageResource(R.drawable.ht_jp);
					bt_record.setVisibility(View.VISIBLE);
					MyUtils.hideIMM(v);
				} else {
					// 录音按钮显示此时要切换成编辑文字模式
					bt_voice.setImageResource(R.drawable.ht_ly);
					bt_record.setVisibility(View.GONE);
					if (et_repley != null) {
						et_repley.setHint("回复" + commentBean.getMemberNm());
						MyUtils.opendIMM(et_repley);
					}
				}
			}
		}
	};

	private String copyContent;
	private TextView tv_copy;
	// 评论长按监听--复制内容
	private OnLongClickListener myCopyLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			tv_copy = (TextView) v;
			String con = (String) tv_copy.getTag(R.id.tag_copy).toString().trim();
			if (!MyUtils.isEmptyString(con)) {
				int[] location = new int[2];
				v.getLocationOnScreen(location);
				int height = tv_copy.getHeight();
				int width = tv_copy.getWidth();
				tv_copy.setBackgroundResource(R.color.hale_light_gray);
				copyPopupWindow.showAsDropDown(v, (int) (width / 2 - 40 * scale), (int) (-height - 25 * scale));
				copyContent = con;
			} else {
				ToastUtils.showCustomToast("复制内容不能为空");
			}
			return false;
		}
	};

	private CallReplyBean rcBean;
	// 回复评论点击监听
	private OnClickListener myRcClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			isSecondReply = true;
			rcBean = (CallReplyBean) v.getTag(R.id.tag_rcbean);
			commentBean = (CommentItemBean) v.getTag(R.id.tag_commbean);
			final List<CallReplyBean> rcList = commentBean.getRcList();
			int memberId = rcBean.getMemberId();
			if (myId == memberId) {
				// 删除回复
				AlertDialog alertDialog = new AlertDialog.Builder(CallQuery2Activity.this).setMessage("删除评论")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						rcList.remove(rcBean);
						if (rcBean.getCommentId() != 0) {
							delComment(String.valueOf(callonList.get(currentPosition).getId()),
									String.valueOf(commentBean.getCommentId()));
						}
						refreshAdapter2();
					}
				}).setNegativeButton("取消", null).create();
				alertDialog.show();
			} else {
				isReply = true;
				// 切换 录音模式
				setMode(2, false);
				rl_editcontent.setVisibility(View.VISIBLE);
				int isV = bt_record.getVisibility();
				if (View.VISIBLE == isV) {
					// 录音按钮非显示此时要切换成录音模式
					bt_voice.setImageResource(R.drawable.ht_jp);
					bt_record.setVisibility(View.VISIBLE);
					MyUtils.hideIMM(v);
				} else {
					// 录音按钮显示此时要切换成编辑文字模式
					bt_voice.setImageResource(R.drawable.ht_ly);
					bt_record.setVisibility(View.GONE);
					if (et_repley != null) {
						et_repley.setHint("回复" + rcBean.getMemberNm());
						MyUtils.opendIMM(et_repley);
					}
				}
			}
		}
	};

	// 评论点击事件
	private OnClickListener voiceClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			isSecondReply = false;
			ImageView tv_comment = (ImageView) v;
			commentBean = (CommentItemBean) tv_comment.getTag(R.id.tag_commbean);
			v.setId(commentBean.getCommentId());//TODO 记录播放动画的位置
			int id = commentBean.getCommentId();// 评论id
			setVoice(commentBean.getContent(), id, (ImageView) v);
			bfid_play = id;
		}
	};
	private OnClickListener voiceClickListener_hf = new OnClickListener() {
		@Override
		public void onClick(View v) {
			isSecondReply = true;
			rcBean = (CallReplyBean) v.getTag(R.id.tag_rcbean);
			commentBean = (CommentItemBean) v.getTag(R.id.tag_commbean);
			v.setId(rcBean.getCommentId());//TODO 记录播放动画的位置
			int id = rcBean.getCommentId();// 评论id
			setVoice(rcBean.getContent(), id, (ImageView) v);
			bfid_play = id;
		}
	};

	// 复制--窗体
	private PopupWindow copyPopupWindow;
	ClipboardManager clipboardManager;

	private void creatCopyPop() {
		TextView mPopupText = new TextView(this);
		mPopupText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (clipboardManager == null) {
					clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				}
				if (MyUtils.isEmptyString(copyContent)) {
					ToastUtils.showCustomToast("复制的内容不能为空");
				} else {
					clipboardManager.setText(copyContent);
					ToastUtils.showCustomToast("复制成功");
				}
				copyPopupWindow.dismiss();
			}
		});
		mPopupText.setBackgroundResource(R.drawable.copy_bg);
		mPopupText.setTextColor(Color.WHITE);
		mPopupText.setPadding(0, 0, 0, 15);
		mPopupText.setText("复制");
		mPopupText.setGravity(Gravity.CENTER);
		copyPopupWindow = new PopupWindow(mPopupText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		copyPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		copyPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				if (tv_copy != null) {
					tv_copy.setBackgroundResource(R.color.transparency);
				}
			}
		});
	}

	// 语音处理
	private boolean playState;
	private MediaPlayer mediaPlayer;
	private int bfid_play;// 以拜访id作为区分（当前点击播放是否与上次播放一致；一致暂停播放，不一致先暂停播放上次的，再播放当前的）

	private void setVoice(final String path, int id, ImageView iv_voice) {
		if (!playState) {
			downFileToStartPlayer(path, iv_voice);
		} else {
			stopPlayer();
			if (bfid_play != id) {
				downFileToStartPlayer(path, iv_voice);
			}
		}
	}

	/**
	 * 下载音频文件并播放
	 */
	private void downFileToStartPlayer(final String path, final ImageView iv_voice) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				String fileName = JsonHttpUtil.getInstance().downFile(path);
				if (!TextUtils.isEmpty(fileName)) {
					openVioceFile(fileName, iv_voice);
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
	private void openVioceFile(String path, final ImageView iv_voice) {
		ToastUtils.showCustomToast("播放中。。");
		// TODO 子线程中不能更新ui（播放动画）
		Message message = new Message();
		message.what = 0;
		message.obj = iv_voice;
		mHandler.sendMessage(message);

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
						// TODO 子线程中不能更新ui（停止动画）
						mHandler.sendEmptyMessage(1);

						playState = false;
						mediaPlayer.release();// 释放资源
						mediaPlayer = null;
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
		// TODO 子线程中不能更新ui（停止动画）
		mHandler.sendEmptyMessage(1);

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

	// 录音计时线程
	private Thread recordThread;

	private void mythread() {
		recordThread = new Thread(ImgThread);
		recordThread.start();
	}

	// 录音线程
	private Runnable ImgThread = new Runnable() {

		@Override
		public void run() {
			recodeTime = 0.0f;
			while (RECODE_STATE == RECORD_ING) {
				if (recodeTime >= MAX_TIME && MAX_TIME != 0) {
					imgHandle.sendEmptyMessage(0);
				} else {
					try {
						Thread.sleep(200);
						recodeTime += 0.2;
						if (RECODE_STATE == RECORD_ING) {
							voiceValue = mr.getAmplitude();
							imgHandle.sendEmptyMessage(1);
						}
					} catch (InterruptedException e) {
					}
				}
			}
		}

		Handler imgHandle = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case 0:
					// 录音超过15秒自动停止
					if (RECODE_STATE == RECORD_ING) {
						RECODE_STATE = RECODE_ED;
						layout_recode.setVisibility(View.INVISIBLE);
						try {
							mr.stop();
							isRecord = false;
						} catch (IOException e) {
							ToastUtils.showCustomToast("录音失败请重新录音！");
						}
						voiceValue = 0.0;
						if (recodeTime < 1.0) {
							ToastUtils.showCustomToast("录音时间太短！");
							RECODE_STATE = RECORD_NO;
						}
					}
					break;
				case 1:
					setDialogImage();
					break;
				}
			}
		};
	};

	// 录音Dialog图片随声音大小切换
	private void setDialogImage() {
		if (voiceValue < 200.0) {
			dialog_img.setImageResource(R.drawable.record_animate_01);
		} else if (voiceValue > 200.0 && voiceValue < 400) {
			dialog_img.setImageResource(R.drawable.record_animate_02);
		} else if (voiceValue > 400.0 && voiceValue < 800) {
			dialog_img.setImageResource(R.drawable.record_animate_03);
		} else if (voiceValue > 800.0 && voiceValue < 1600) {
			dialog_img.setImageResource(R.drawable.record_animate_04);
		} else if (voiceValue > 1600.0 && voiceValue < 3200) {
			dialog_img.setImageResource(R.drawable.record_animate_05);
		} else if (voiceValue > 3200.0 && voiceValue < 5000) {
			dialog_img.setImageResource(R.drawable.record_animate_06);
		} else if (voiceValue > 5000.0 && voiceValue < 7000) {
			dialog_img.setImageResource(R.drawable.record_animate_07);
		} else if (voiceValue > 7000.0 && voiceValue < 10000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_08);
		} else if (voiceValue > 10000.0 && voiceValue < 14000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_09);
		} else if (voiceValue > 14000.0 && voiceValue < 17000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_10);
		} else if (voiceValue > 17000.0 && voiceValue < 20000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_11);
		} else if (voiceValue > 20000.0 && voiceValue < 24000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_12);
		} else if (voiceValue > 24000.0 && voiceValue < 28000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_13);
		} else if (voiceValue > 28000.0) {
			dialog_img.setImageResource(R.drawable.record_animate_14);
		}
	}

	/**
	 * 录音
	 */
	private void recoord(MotionEvent event) throws IOException {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			if (RECODE_STATE == RECORD_ING) {
				RECODE_STATE = RECODE_ED;
				layout_recode.setVisibility(View.INVISIBLE);
				mr.stop();
				isRecord = false;
				voiceValue = 0.0;

				if (recodeTime < MIX_TIME) {
					ToastUtils.showCustomToast("录音时间太短！");
					RECODE_STATE = RECORD_NO;
					File f = new File(Constans.DIR_VOICE);
					if (!f.isDirectory()) {
						f.mkdirs();
					}
					file_voice = new File(Constans.DIR_VOICE, l + ".amr");
					if (file_voice != null) {
						file_voice.delete();
					}
				} else {
					File f = new File(Constans.DIR_VOICE);
					if (!f.isDirectory()) {
						f.mkdirs();
					}
					file_voice = new File(Constans.DIR_VOICE, l + ".amr");
					path_voice = file_voice.getPath();
					recodeTime = (int) recodeTime;

					if (file_voice != null) {
						Log.e("isReply--", "" + isReply);
						if (isReply) {
							if (isSecondReply) {// 第二次回复
								initData_reply(path_voice, rcBean.getMemberNm(), commentBean.getRcList(),
										commentBean.getCommentId(), rcBean.getMemberNm(), rcBean.getMemberId(),
										(int) recodeTime, file_voice);
							} else {// 首次回复
								initData_reply(path_voice, commentBean.getMemberNm(), commentBean.getRcList(),
										commentBean.getCommentId(), commentBean.getMemberNm(),
										commentBean.getMemberId(), (int) recodeTime, file_voice);
							}
						} else {
							initData_pingLun(path_voice, (int) recodeTime, file_voice);
						}
					} else {
						ToastUtils.showCustomToast("录音文件错误");
					}
				}
			}
			break;
		}
	}

	// TODO 子线程中不能更新ui（播放动画，停止动画）
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				playAnimation((ImageView) msg.obj);
				break;
			case 1:
				stopAnimation();
				break;
			}
		}
	};

	// 播放动画
	private ImageView iv_playVoice;// TODO 备注播放动画的对象只有一个，（否则播放完动画停不了）

	private void playAnimation(ImageView iv_voice) {
		voicePlayPosition = iv_voice.getId();
		iv_playVoice = iv_voice;
		iv_playVoice.setImageResource(R.drawable.voice_from_playing_s0);
		iv_playVoice.setImageResource(R.drawable.animation_left);
		AnimationDrawable animation = (AnimationDrawable) iv_playVoice.getDrawable();
		animation.start();
	}

	// 停止播放动画
	private void stopAnimation() {
		voicePlayPosition = -1;
		if (iv_playVoice != null) {
			iv_playVoice.setImageResource(R.drawable.voice_from_playing_s0);
			iv_playVoice = null;
		}
	}

}
