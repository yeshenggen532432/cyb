package com.qwb.view.audit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.model.TabEntity;
import com.qwb.view.audit.adapter.AuditMySendAdapter;
import com.qwb.view.audit.adapter.SpMySpAdapter;
import com.qwb.view.audit.model.ShenPiIShenPiBean;
import com.qwb.view.audit.model.ShenPiIsendBean;
import com.qwb.view.audit.parsent.PAuditTabCommon;
import com.qwb.widget.MyDatePickerDialog;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XFragment;

/**
 * tab:我发起的，我审批的，我执行的（三个公用一个界面）
 */
public class AuditTabCommonFragment extends XFragment<PAuditTabCommon> {
   private int mType = 1;

	public AuditTabCommonFragment() {
	}

	@Override
	public int getLayoutId() {
		return R.layout.x_fragment_audit_tab_common;
	}

	@Override
	public PAuditTabCommon newP() {
		return new PAuditTabCommon();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		mType = getArguments().getInt(ConstantUtils.Intent.TYPE);
		initUI();
		queryData();
	}


	private void initUI() {
		initTab();
		initScreening();
		initAdapter();
	}

	/**
	 * tab
	 */
	private String[] mTitles = {"审批中", "审批完成"};
	private String[] mTitles2 = {"待我审批", "审批中", "已完成"};
	private String[] mTitles3 = {"执行中", "执行完成"};
	private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
	@BindView(R.id.commonTabLayout)
	CommonTabLayout mCommonTabLayout;
	private void initTab() {
		if(3 == mType){
			for (int i = 0; i < mTitles3.length; i++) {
				mTabEntities.add(new TabEntity(mTitles3[i], 0, 0));
			}
		}else if(2 == mType){
			for (int i = 0; i < mTitles2.length; i++) {
				mTabEntities.add(new TabEntity(mTitles2[i], 0, 0));
			}
		}else{
			for (int i = 0; i < mTitles.length; i++) {
				mTabEntities.add(new TabEntity(mTitles[i], 0, 0));
			}
		}
		mCommonTabLayout.setTabData(mTabEntities);
		mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
			@Override
			public void onTabSelect(int position) {
				if(2 == mType){
					if(2 == position){
						mIsCheck = "4";
					}else if(1 == position){
						mIsCheck = "3";
					}else{
						mIsCheck = "1";
					}
				}else{
					if(1 == position){
						mIsOver = "1";
						mIsCheck = "2";
					}else{
						mIsOver = "2";
						mIsCheck = "1";
					}
				}
				pageNo = 1;
				queryData();
			}

			@Override
			public void onTabReselect(int position) {
			}
		});
	}

	/**
	 * 初始化筛选:时间和搜索
	 */
	@BindView(R.id.rl_search)
	LinearLayout mRlSearch;
	@BindView(R.id.et_search)
	EditText mEtSearch;
	@BindView(R.id.iv_search)
	TextView mIvSearch;
	@BindView(R.id.tv_start_end_time)
	TextView mTvStartEndTime;
	@BindView(R.id.tv_search)
	TextView mTvSearch;
	private void initScreening() {
		mTvSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mRlSearch.getVisibility()== View.VISIBLE){
					mRlSearch.setVisibility(View.GONE);
					mEtSearch.setText("");
				}else{
					mRlSearch.setVisibility(View.VISIBLE);
				}
			}
		});
		mIvSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pageNo = 1;
				queryData();
			}
		});
		mTvStartEndTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogDate();
			}
		});
	}

	// 显示开始日期
	private int startYear, startMonth, startDay;
	private String mDateStr;
	private void showDialogDate() {
		if (0 == startDay) {
			startYear = MyTimeUtils.getYear();
			startMonth = MyTimeUtils.getMonth();
			startDay = MyTimeUtils.getDay();
		}
		new MyDatePickerDialog(context, "开始时间", startYear, startMonth, startDay, new MyDatePickerDialog.DateTimeListener() {
			@Override
			public void onSetTime(int year, int month, int day, String timeStr) {
				try {
					startYear = year;
					startMonth = month;
					startDay = day;
					mTvStartEndTime.setText(timeStr);
					mDateStr = timeStr;
					pageNo = 1;
					queryData();
				} catch (Exception e) {
					ToastUtils.showError(e);
				}
			}

			@Override
			public void onCancel() {
			}
		}).show();
	}

	/**
	 * 初始化适配器
	 */
	@BindView(R.id.recyclerView_common)
	RecyclerView mRecyclerView;
	@BindView(R.id.refreshLayout_common)
	RefreshLayout mRefreshLayout;
	AuditMySendAdapter mAdapter;
	SpMySpAdapter mAdapter2;
	private int pageNo = 1;
	private int pageSize = 10;
	private void initAdapter() {
		mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
		//添加分割线
		mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
				.colorResId(R.color.gray_e)
				.sizeResId(R.dimen.dp_1)
				.build());
		mAdapter2 = new SpMySpAdapter();
		mAdapter = new AuditMySendAdapter();
		if(3 == mType){
			mRecyclerView.setAdapter(mAdapter);
		}else if(2 == mType){
			mRecyclerView.setAdapter(mAdapter2);
		}else{
			mRecyclerView.setAdapter(mAdapter);
		}
		mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				try {
					ShenPiIsendBean.ShenPiIsendItemBean bean = (ShenPiIsendBean.ShenPiIsendItemBean) adapter.getData().get(position);
					String auditNo = bean.getAuditNo();//1 我的审核 （同意 、拒绝、 转交等操作）2 我发起的 （撤销操作）3.执行
					int needCheck = 2;//1 我的审核 （同意 、拒绝、 转交等操作）2 我发起的 （撤销操作）3.执行
					if(3 == mType){
						needCheck = 3;
					}
					Intent intent = new Intent(context, AuditDetailActivity.class);
					intent.putExtra(ConstantUtils.Intent.ID, auditNo);
					intent.putExtra(ConstantUtils.Intent.NEED_CHECK, needCheck);
					startActivity(intent);
				} catch (Exception e) {
				}
			}
		});
		mAdapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				try {
					ShenPiIShenPiBean.ShenPiIShenPiItemBean bean = (ShenPiIShenPiBean.ShenPiIShenPiItemBean) adapter.getData().get(position);
					String  auditNo = bean.getAuditNo();
					int needCheck = 1;//1 我的审核 （同意 、拒绝、 转交等操作）2 我发起的 （撤销操作）3.执行
					Intent intent = new Intent(context, AuditDetailActivity.class);
					intent.putExtra(ConstantUtils.Intent.ID, auditNo);
					intent.putExtra(ConstantUtils.Intent.NEED_CHECK, needCheck);
					startActivity(intent);
				} catch (Exception e) {
				}
			}
		});
		//刷新
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
				pageNo = 1;
				queryData();
			}
		});
		mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
				pageNo++;
				queryData();
			}
		});
	}


	/**
	 * 刷新，未完成数量
	 */
	public void refreshAdapter(List<ShenPiIsendBean.ShenPiIsendItemBean> list, int total) {
		try {
			if(list == null){
				return;
			}
			if ("2".equals(mIsOver) && 1 == mType) {
				TextView textView = mCommonTabLayout.getTitleView(0);
				if (total > 0) {
					textView.setText("审批中(" + total + ")");
				} else {
					textView.setText("审批中");
				}
			}
			if (pageNo == 1) {
				//上拉刷新
				mAdapter.setNewData(list);
				mRefreshLayout.finishRefresh();
				mRefreshLayout.setNoMoreData(false);
			} else {
				//加载更多
				mAdapter.addData(list);
				mRefreshLayout.finishLoadMore();
			}
			if (list.size() < pageSize) {
				mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
				ToastUtils.showCustomToast("没有更多数据");
			}
		}catch (Exception e){
			ToastUtils.showError(e);
		}
	}
	/**
	 * 刷新，未完成数量
	 */
	public void refreshAdapter2(List<ShenPiIShenPiBean.ShenPiIShenPiItemBean> list, int total) {
		try {
			if(list == null){
				return;
			}
			if ("1".equals(mIsCheck)) {
				TextView textView = mCommonTabLayout.getTitleView(0);
				if (total > 0) {
					textView.setText("待我审批(" + total + ")");
				} else {
					textView.setText("待我审批");
				}
			}
			if (pageNo == 1) {
				//上拉刷新
				mAdapter2.setNewData(list);
				mRefreshLayout.setNoMoreData(false);
				mRefreshLayout.finishRefresh();
			} else {
				//加载更多
				mAdapter2.addData(list);
				mRefreshLayout.finishLoadMore();
			}
			if (list.size() < pageSize) {
				mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
				ToastUtils.showCustomToast("没有更多数据");
			}
		}catch (Exception e){
			ToastUtils.showError(e);
		}
	}

	/**
	 * 接口
	 */
	private String mIsOver = "2";//1 已完成 2 未完成(我发起的，我执行的)
	private String mIsCheck = "1";//状态 1 待我审核 2 我已审核（我审批的）
	private void queryData() {
		String searchStr = mEtSearch.getText().toString();
		getP().queryData(context, pageNo, pageSize, mIsOver, mIsCheck, mDateStr,searchStr, mType);
	}




}
