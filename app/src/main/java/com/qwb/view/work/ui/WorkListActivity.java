package com.qwb.view.work.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.view.work.adapter.WorkListAdapter;
import com.qwb.view.work.parsent.PWorkList;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.widget.treedialog.MyTreeDialog;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.work.model.WorkSubBean;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 考勤列表（上下班列表）
 */
public class WorkListActivity extends XActivity<PWorkList> {

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_work_list;
	}

	@Override
	public PWorkList newP() {
		return new PWorkList();
	}


	@Override
	public void initData(Bundle savedInstanceState) {
		initUI();
		getP().queryData(context, pageNo, pageSize, mMemberIds, mStartDate, mEndDate);
	}

	private String mMemberIds;
	private void initUI() {
		initHead();
		initScreening();
		initAdapter();
		initRefresh();
	}

	@BindView(R.id.head_left)
	View mHeadLeft;
	@BindView(R.id.tv_head_title)
	TextView mTvHeadTitle;
	private void initHead() {
		MyStatusBarUtil.getInstance().setColorWhite(context);
		mHeadLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityManager.getInstance().closeActivity(context);
			}
		});
		mTvHeadTitle.setText("考勤");
	}

	/**
	 * 初始化筛选:时间和人员
	 */
	TextView mTvStartEndTime;
	int mStartYear,mStartMonth,mStartDay,mEndYear,mEndMonth,mEndDay;//年，月，日
	String mStartDate,mEndDate;//开始时间，结束时间
	@BindView(R.id.tv_frame)
	TextView mTvFrame;
	private void initScreening() {
		mTvStartEndTime = findViewById(R.id.tv_start_end_time);
		mStartDate = MyTimeUtils.getFirstOfMonth();
		mEndDate = MyTimeUtils.getTodayStr();
		mTvStartEndTime.setText(mStartDate + "至"+ mEndDate);
		mStartYear = MyTimeUtils.getYear(mStartDate);
		mStartMonth = MyTimeUtils.getMonth(mStartDate);
		mStartDay = MyTimeUtils.getDay(mStartDate);
		mEndYear = MyTimeUtils.getYear(mEndDate);
		mEndMonth = MyTimeUtils.getMonth(mEndDate);
		mEndDay = MyTimeUtils.getDay(mEndDate);
		mTvStartEndTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogStartEndTime();
			}
		});
		mTvFrame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(null == mTreeDatas || mTreeDatas.isEmpty()){
					getP().loadDataFrame(context);
				}else{
					showDialogMember();
				}
			}
		});
	}


	/**
	 * 初始化适配器（我的客户）
	 */
	@BindView(R.id.recyclerView)
	RecyclerView mRecyclerView;
	WorkListAdapter mAdapter;
	private int pageNo = 1;
	private int pageSize = 10;
	private void initAdapter() {
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
		mAdapter = new WorkListAdapter(context);
		mAdapter.openLoadAnimation();
		mRecyclerView.setAdapter(mAdapter);
//		//子item点击事件
		mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
				WorkSubBean data = (WorkSubBean)adapter.getData().get(position);
				switch (view.getId()){
					case R.id.tv_sb:
						Router.newIntent(context)
								.putString(ConstantUtils.Intent.ID,data.getUpid())
								.to(WorkDetailActivity.class)
								.launch();
						break;
				}
			}
		});
	}

	/**
	 * 初始化刷新控件（我的客户）
	 */
	@BindView(R.id.refreshLayout)
	RefreshLayout mRefreshLayout;
	private void initRefresh(){
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
				pageNo = 1;
				getP().queryData(context, pageNo, pageSize, mMemberIds, mStartDate, mEndDate);
			}
		});
		mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
				pageNo ++;
				getP().queryData(context, pageNo, pageSize, mMemberIds, mStartDate, mEndDate);
			}
		});
	}

	/**
	 * 筛选时间的对话框
	 */
	private void showDialogStartEndTime() {
		new MyDoubleDatePickerDialog(context, "筛选时间", mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay,
				new MyDoubleDatePickerDialog.DateTimeListener() {
					@Override
					public void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate) {
						mStartYear = year;
						mStartMonth = month;
						mStartDay = day;
						mEndYear = year2;
						mEndMonth = month2;
						mEndDay = day2;
						mTvStartEndTime.setText(startDate+"至"+endDate);
						mStartDate = startDate;
						mEndDate = endDate;

						pageNo = 1;
						getP().queryData(context, pageNo, pageSize, mMemberIds, mStartDate, mEndDate);
					}

					@Override
					public void onCancel() {
					}
				}).show();
	}

	private List<TreeBean> mTreeDatas = new ArrayList<>();
	private MyTreeDialog mTreeDialog;
	private void showDialogMember(){
		if(null == mTreeDialog){
			mTreeDialog = new MyTreeDialog(context, mTreeDatas, true);
		}
		mTreeDialog.title("选择员工").show();
		mTreeDialog.setOnClickListener(new MyTreeDialog.OnClickListener() {
			@Override
			public void onOkListener(String checkIds, String clientTypeIds, Map<Integer, Integer> checkMap) {
				mMemberIds = checkIds;
				if(!MyStringUtil.isEmpty(checkIds)){
					mTvFrame.setTextColor(getResources().getColor(R.color.yellow));
				}else{
					mTvFrame.setTextColor(getResources().getColor(R.color.gray_6));
				}
				pageNo = 1;
				getP().queryData(context, pageNo, pageSize, mMemberIds, mStartDate, mEndDate);
			}
		});
	}

	public void refreshAdapterFrame(List<TreeBean> mDatas){
		this.mTreeDatas = mDatas;
		showDialogMember();
	}


	/**
	 * 刷新适配器-我的客户
	 */
	public void refreshAdapter(List<WorkSubBean> dataList){
		if(dataList == null){
			return;
		}
		if(pageNo == 1){
			//上拉刷新
			mAdapter.setNewData(dataList);
			mRefreshLayout.finishRefresh();
			mRefreshLayout.setNoMoreData(false);
		}else{
			//加载更多
			mAdapter.addData(dataList);
			mRefreshLayout.finishLoadMore();
		}
		if(dataList.size() < 10){
			mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
		}
	}


}
