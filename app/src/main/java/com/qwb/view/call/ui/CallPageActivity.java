package com.qwb.view.call.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ilike.voicerecorder.widget.VoiceRecorderView;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.Constans;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDividerUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.call.adapter.CallPageAdapter;
import com.qwb.view.call.model.CallReplyBean;
import com.qwb.view.call.model.CommentItemBean;
import com.qwb.view.call.model.QueryCallon;
import com.qwb.view.call.model.QueryCallonBean2;
import com.qwb.view.call.parsent.PCallPage;
import com.qwb.widget.MyCallChooseDateDialog;
import com.qwb.widget.treedialog.MyTreeDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xmsx.qiweibao.R;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 我的拜访
 */
public class CallPageActivity extends XActivity<PCallPage>  {

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_call_page;
	}

	@Override
	public PCallPage newP() {
		return new PCallPage();
	}

	private String type;
	private String cid;// 客户id
	private String bfId;// 消息评论-拜访id
	private boolean isShowRepeat = true;//adapter:是否显示“客户重复”
	private String bfid;
	private String mUserId = SPUtils.getID(), mUserName = SPUtils.getUserName();
	public void initIntent(){
		Intent intent = getIntent();
		if (intent != null) {
			// 1~6步骤，拜访回放 2：消息评论
			type = intent.getStringExtra(ConstantUtils.Intent.TYPE);
			cid = intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
			mSearch = intent.getStringExtra(ConstantUtils.Intent.CLIENT_NAME);
			mStartDate = intent.getStringExtra(ConstantUtils.Intent.START_TIME);
			mEndDate = intent.getStringExtra(ConstantUtils.Intent.END_TIME);
			mTimeTitle = intent.getStringExtra("bfTime");
			bfId = intent.getStringExtra("bfId");
			isShowRepeat = intent.getBooleanExtra(ConstantUtils.Intent.IS_SHOW, true);
		}
	}

	public String mSearch, mStartDate, mEndDate, mMemberIds, mTimeTitle;
	public void doIntent(){
		if ("2".equals(type)) {// 拜访评论
			getP().queryComment(context, bfid);
		} else {
			// 开始时间，结束时间默认今天
			queryData(true);
		}
		if (MyStringUtil.isNotEmpty(mTimeTitle)) {
			mTvScreeningTab1.setText(mTimeTitle);
		}else{
			mTvScreeningTab1.setText("拜访时间");
		}
		if (MyStringUtil.isNotEmpty(mSearch)) {
			mEtSearch.setText(mSearch);
			mViewSearch.setVisibility(View.VISIBLE);
			mEtSearch.setEnabled(false);
			mTvScreeningTab3.setEnabled(false);
			mTvScreeningTab3.setClickable(false);
		}
	}

	public void queryData(boolean isRefresh){
		if (isRefresh){
			pageNo = 1;
		}else{
			pageNo++;
		}
		getP().queryData(context, pageNo, pageSize,mSearch, mStartDate, mEndDate, cid, bfId, mMemberIds);
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initIntent();
		initUI();
		doIntent();
	}

	private void initUI() {
		initHead();
		initScreening();
		initAdapter();
		initComment();
	}

	// 头部
	@BindView(R.id.head_left)
	View mViewLeft;
	@BindView(R.id.tv_head_title)
	TextView mTvHeadTitle;
	private void initHead() {
		MyStatusBarUtil.getInstance().setColorBlue(context);
		mTvHeadTitle.setText("拜访查询");
		mTvHeadTitle.setTextSize(15);
		mViewLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityManager.getInstance().closeActivity(context);
			}
		});
		mTvHeadTitle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rl_editcontent.setVisibility(View.VISIBLE);
			}
		});
	}

	/**
	 * 初始化筛选:时间和搜索
	 */
	@BindView(R.id.view_search)
	View mViewSearch;
	@BindView(R.id.et_search)
	EditText mEtSearch;
	@BindView(R.id.tv_search)
	TextView mTvSearch;
	@BindView(R.id.view_screening_tab1)
	View mViewScreeningTab1;
	@BindView(R.id.view_screening_tab2)
	View mViewScreeningTab2;
	@BindView(R.id.view_screening_tab3)
	View mViewScreeningTab3;
	@BindView(R.id.tv_screening_tab1)
	TextView mTvScreeningTab1;
	@BindView(R.id.tv_screening_tab2)
	TextView mTvScreeningTab2;
	@BindView(R.id.tv_screening_tab3)
	TextView mTvScreeningTab3;
	private String mSearchStr, mOutType;
	private void initScreening() {
		mTvScreeningTab1.setText("拜访时间");
		mTvScreeningTab2.setText("人员");
		mViewScreeningTab1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doScreeningTab1();
			}
		});
		mViewScreeningTab2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doScreeningTab2();
			}
		});
		mViewScreeningTab3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doScreeningTab3();
			}
		});
		mTvSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doScreeningSearch();
			}
		});
	}

	/**
	 * 初始化适配器
	 */
	@BindView(R.id.recyclerView)
	RecyclerView mRecyclerView;
	@BindView(R.id.refreshLayout)
	RefreshLayout mRefreshLayout;
	CallPageAdapter mAdapter;
	private int pageNo = 1, pageSize = 10;
	private QueryCallon mCurrentItem;
	private int mCurrentPosition;
	private CommentItemBean mCurrentCommentItem;
	private CallReplyBean mCurrentReplyItem;
	private int mTag;
	private void initAdapter() {
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
		mRecyclerView.addItemDecoration(MyDividerUtil.getH5CGray(context));
		mAdapter = new CallPageAdapter(context, isShowRepeat);
		mRecyclerView.setAdapter(mAdapter);
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
				queryData(true);
			}
		});
		mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
				queryData(false);
			}
		});

		// mRecyclerView滑动时要隐藏“评论框”
		mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (1 == newState) {
					rl_editcontent.setVisibility(View.GONE);
				}
			}
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
			}
		});

		mAdapter.setOnClickListener(new CallPageAdapter.OnClickListener() {
			@Override
			public void onClickListener(QueryCallon item, int position, CommentItemBean commentItem, CallReplyBean replyBean, int tag) {
				try {
					mTag = tag;
					mCurrentItem = item;
					mCurrentPosition = position;
					mCurrentCommentItem = commentItem;
					mCurrentReplyItem = replyBean;
					if (CallPageAdapter.COMMENT == tag){
						doCommentOrReply(null);
					}else if(CallPageAdapter.REPLY == tag){
						doCommentOrReply(mCurrentCommentItem.getMemberNm());
					}else if(CallPageAdapter.REPLY_AGAIN == tag){
						doCommentOrReply(mCurrentReplyItem.getMemberNm());
					}
				} catch (Exception e) {
				}
			}
		});

		mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
				try {
					mCurrentItem = (QueryCallon) adapter.getData().get(position);
					mCurrentPosition = position;
					switch (view.getId()){
						case R.id.parent:
							ActivityManager.getInstance().jumpToCallRecordActivity(context, mCurrentItem.getCid(), mCurrentItem.getKhNm(), mCurrentItem.getMemberNm());
							break;
						case R.id.tv_zfcount:
							ActivityManager.getInstance().jumpToCallPageActivity(context, ""+mCurrentItem.getCid(),mCurrentItem.getKhNm(), mStartDate, mEndDate, mTvScreeningTab1.getText().toString(), false,"0");
							break;
					}
				} catch (Exception e) {
				}
			}
		});

	}

	//刷新
	public void refreshAdapter( QueryCallonBean2 bean){
		List<QueryCallon> dataList = bean.getRows();
		if (MyUtils.isEmptyString(bfid)) {
			mTvHeadTitle.setText("拜访次数：" + bean.getTotal() + "  拜访家数：" + bean.getNum());
		}
		if (pageNo == 1) {
			//上拉刷新
			mAdapter.setNewData(dataList);
			mRefreshLayout.finishRefresh();
			mRefreshLayout.setNoMoreData(false);
		} else {
			//加载更多
			mAdapter.addData(dataList);
			mRefreshLayout.finishLoadMore();
			mRefreshLayout.setNoMoreData(false);
		}
		if (null != dataList && dataList.size() < pageSize) {
			mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
			ToastUtils.showCustomToast("没有更多数据");
		}
	}

	@BindView(R.id.rl_editcontent)
	View rl_editcontent;
	@BindView(R.id.bt_voice)
	ImageView mIvVoice;
	@BindView(R.id.record)
	Button mBtnRecord;
	@BindView(R.id.et_repley)
	EditText et_repley;
	protected VoiceRecorderView voiceRecorderView;
	private void initComment() {
		findViewById(R.id.bt_send).setOnClickListener(new OnNoMoreClickListener() {
			@Override
			protected void OnMoreClick(View view) {
				String content = et_repley.getText().toString();
				doSendComment(content, 0, null);
			}
		});
		findViewById(R.id.bt_voice).setOnClickListener(new OnNoMoreClickListener() {
			@Override
			protected void OnMoreClick(View view) {
				doChangeModelVoiceText();
			}
		});
		voiceRecorderView = findViewById(R.id.voice_recorder);
		voiceRecorderView.setVoiceDirPath(Constans.DIR_IMAGE);
		findViewById(R.id.record).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				return voiceRecorderView.onPressToSpeakBtnTouch(view, motionEvent, new VoiceRecorderView.EaseVoiceRecorderCallback() {

					@Override
					public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
						doSendComment(voiceFilePath, (int) voiceTimeLength, new File(voiceFilePath));
					}
				});
			}
		});
	}

	//------------------------------------------------------------------------------------------------------------------
	//筛选1
	private int mTimeType;
	private void doScreeningTab1(){
		MyCallChooseDateDialog dialog = new MyCallChooseDateDialog(context, mStartDate, mEndDate, mTimeType);
		dialog.setOnClickListener(new MyCallChooseDateDialog.OnClickListener() {
			@Override
			public void setOnClickListener(String timeTitle, String startDate, String endDate, int type) {
				mStartDate = startDate;
				mEndDate = endDate;
				mTimeType = type;
				if (7 == type){
					mTvScreeningTab1.setText(startDate+"至"+endDate);
				}else{
					mTvScreeningTab1.setText(timeTitle);
				}
				queryData(true);
			}
		});
		dialog.show();
	}
	//筛选2
	private void doScreeningTab2(){
		if (null == mTreeDatas || mTreeDatas.isEmpty()) {
			getP().queryMember(context);
		} else {
			showDialogMember(mTreeDatas);
		}
	}

	//筛选3
	private void doScreeningTab3(){
		if (mViewSearch.getVisibility() == View.VISIBLE) {
			mViewSearch.setVisibility(View.GONE);
			mEtSearch.setText("");
			mSearchStr = "";
		} else {
			mViewSearch.setVisibility(View.VISIBLE);
		}
	}

	//筛选：搜索
	private void doScreeningSearch(){
        mSearch = mEtSearch.getText().toString().trim();
        queryData(true);
	}
	/**
	 * 选择员工
	 */
	private List<TreeBean> mTreeDatas = new ArrayList<>();
	private MyTreeDialog mTreeDialog;
	public void showDialogMember(List<TreeBean> mDatas) {
		mTreeDatas.clear();
		mTreeDatas.addAll(mDatas);
		if (null == mTreeDialog) {
			mTreeDialog = new MyTreeDialog(context, mTreeDatas, true);
		}
		mTreeDialog.title("选择员工").show();
		mTreeDialog.setOnClickListener(new MyTreeDialog.OnClickListener() {
			@Override
			public void onOkListener(String checkIds, String clientTypeIds, Map<Integer, Integer> checkMap) {
				mMemberIds = checkIds;
				queryData(true);
//				mRefreshLayout.autoRefresh();
//				if (!MyStringUtil.isEmpty(checkIds)) {
//					mTvFrame.setTextColor(getResources().getColor(R.color.yellow));
//				} else {
//					mTvFrame.setTextColor(getResources().getColor(R.color.gray_6));
//				}
			}
		});
	}

	//回复或评论
	private void doCommentOrReply(String replyMemberName) {
		rl_editcontent.setVisibility(View.VISIBLE);
		if (MyStringUtil.isNotEmpty(replyMemberName)){
			et_repley.setHint("回复" + replyMemberName);
		}else{
			et_repley.setHint("请输入评论");
		}
	}

	//切换录音还是文本
	private void doChangeModelVoiceText() {
		if (View.VISIBLE == mBtnRecord.getVisibility()) {
			mIvVoice.setImageResource(R.drawable.ht_ly);
			mBtnRecord.setVisibility(View.GONE);
		} else {
			mIvVoice.setImageResource(R.drawable.ht_jp);
			mBtnRecord.setVisibility(View.VISIBLE);
		}
	}

	//发送评论；回复评论
	private void doSendComment(String content, int voiceTime, File voiceFile) {
		if (CallPageAdapter.REPLY == mTag){
			CallReplyBean replyBean = new CallReplyBean();
			replyBean.setContent(content);
			replyBean.setMemberNm(mUserName);
			replyBean.setMemberId(Integer.valueOf(mUserId));
			replyBean.setRcNm(mCurrentCommentItem.getMemberNm());
			replyBean.setVoiceTime(voiceTime);
			mCurrentCommentItem.getRcList().add(replyBean);
			mAdapter.notifyItemChanged(mCurrentPosition);
			getP().addReply(context, ""+ mCurrentItem.getId(), content, mCurrentCommentItem.getCommentId(), mCurrentCommentItem.getMemberNm(), "" + mCurrentCommentItem.getMemberId(), voiceTime, voiceFile);
		} else if (CallPageAdapter.REPLY_AGAIN == mTag){
			CallReplyBean replyBean = new CallReplyBean();
			replyBean.setContent(content);
			replyBean.setMemberNm(mUserName);
			replyBean.setMemberId(Integer.valueOf(mUserId));
			replyBean.setRcNm(mCurrentReplyItem.getMemberNm());
			replyBean.setVoiceTime(voiceTime);
			mCurrentCommentItem.getRcList().add(replyBean);
			mAdapter.notifyItemChanged(mCurrentPosition);
			getP().addReply(context, ""+ mCurrentItem.getId(), content, mCurrentCommentItem.getCommentId(), mCurrentReplyItem.getMemberNm(), "" + mCurrentReplyItem.getMemberId(), voiceTime, voiceFile);
		}else{
			if (MyStringUtil.isNotEmpty(content)) {
				CommentItemBean currentCommentItemBean = new CommentItemBean();
				currentCommentItemBean.setContent(content);
				currentCommentItemBean.setMemberNm(SPUtils.getUserName());
				currentCommentItemBean.setMemberId(Integer.valueOf(SPUtils.getID()));
				currentCommentItemBean.setVoiceTime(voiceTime);
				List<CommentItemBean> commentList = mCurrentItem.getCommentList();
				commentList.add(currentCommentItemBean);
				mAdapter.notifyItemChanged(mCurrentPosition);
				getP().addComment(context, ""+mCurrentItem.getId(), content, voiceTime, voiceFile);
			}
		}

		et_repley.setText("");
		et_repley.setHint("");
		rl_editcontent.setVisibility(View.GONE);
		MyUtils.hideIMM(rl_editcontent);
	}


}
