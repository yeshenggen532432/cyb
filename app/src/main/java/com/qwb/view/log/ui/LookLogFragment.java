package com.qwb.view.log.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qwb.view.audit.ui.AddShenPiRenActivity;
import com.qwb.widget.MyDatePickerDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.MyTimeUtils;
import com.qwb.view.log.adapter.LogAdapter;
import com.xmsx.cnlife.widget.ComputeHeightGridView;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.log.parsent.PLogLook;
import com.qwb.view.audit.ui.AddCurrentIds;
import com.qwb.view.audit.adapter.ShenPi_PersonAdatper;
import com.qwb.utils.ToastUtils;
import com.qwb.view.log.model.RizhiListBean;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 创建描述：工作汇报-看日报；看周报，看月报
 */
public class LookLogFragment extends XFragment<PLogLook> implements OnItemClickListener {

	@Override
	public int getLayoutId() {
		return R.layout.x_fragment_log_look;
	}

	@Override
	public PLogLook newP() {
		return new PLogLook();
	}


	private String startTime,endTime;//开始时间；接收时间
	@Override
	public void initData(Bundle savedInstanceState) {
		initUI();
		getP().loadDataReceive(context, pageNoReceive,startTime,endTime);
		getP().loadDataSend(context,pageNoSend);
	}

	/**
	 * 初始化UI
	 */
	@BindView(R.id.ll_receive)
	LinearLayout mLlReceive;//接收
	@BindView(R.id.ll_send)
	LinearLayout mLlSend;//发送
	private void initUI() {
		initHead();//头部
		initAdapter();//适配器
		initRefresh();
		initAdapterRetreat();
		initRefreshSend();
		initScreening();//筛选
	}


	/**
	 * 头部
	 */
	@BindView(R.id.iv_head_back)
	ImageView mIvHeadBack;
	@BindView(R.id.tv_head_title)
	TextView mTvHeadTitle;
	@BindView(R.id.tv_head_right)
	TextView mTvHeadRight;
	private void initHead() {
		mIvHeadBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Router.pop(context);
			}
		});
		mTvHeadRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Router.newIntent(getActivity())
						.to(LogTableActivity.class)
						.launch();
			}
		});
		mTvHeadTitle.setText("工作汇报");
		mTvHeadRight.setText("查看报表");
	}

	/**
	 * 初始化适配器（接收）
	 */
	@BindView(R.id.rv_receiver)
	RecyclerView mRvReceive;
	LogAdapter mReceiveAdapter;
	private int pageNoReceive = 1;
	private void initAdapter() {
		mRvReceive.setHasFixedSize(true);
		mRvReceive.setLayoutManager(new LinearLayoutManager(context));
		//添加分割线
		mRvReceive.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
				.colorResId(R.color.layout_bg)
				.sizeResId(R.dimen.dp_5)
				.build());
		mReceiveAdapter = new LogAdapter(context,1);
		mReceiveAdapter.openLoadAnimation();
		mRvReceive.setAdapter(mReceiveAdapter);
		mReceiveAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				RizhiListBean.RizhiList item=(RizhiListBean.RizhiList) adapter.getData().get(position);
				Router.newIntent(context)
						.putSerializable(ConstantUtils.Intent.LOOK_WORK_TABLE_DETAILS,item)
						.to( LogDetailActivity.class)
						.launch();
			}
		});
	}

	/**
	 * 初始化刷新控件（接收）
	 */
	@BindView(R.id.refreshLayout)
	RefreshLayout mRefreshLayout;
	private void initRefresh(){
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
				pageNoReceive =1;
				getP().loadDataReceive(context, pageNoReceive,startTime,endTime);
			}
		});
		mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
				pageNoReceive++;
				getP().loadDataReceive(context, pageNoReceive,startTime,endTime);
			}
		});
	}

	/**
	 * 初始化适配器（发送）
	 */
	@BindView(R.id.rv_send)
	RecyclerView mRvSend;
	LogAdapter mSendAdapter;
	private int pageNoSend = 1;
	private void initAdapterRetreat() {
		mRvSend.setHasFixedSize(true);
		mRvSend.setLayoutManager(new LinearLayoutManager(context));
		//添加分割线
		mRvSend.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
				.colorResId(R.color.layout_bg)
				.sizeResId(R.dimen.dp_5)
				.build());
		mSendAdapter = new LogAdapter(context,2);
		mSendAdapter.openLoadAnimation();
		mRvSend.setAdapter(mSendAdapter);
		mSendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				RizhiListBean.RizhiList item=(RizhiListBean.RizhiList) adapter.getData().get(position);
				Router.newIntent(context)
						.putSerializable(ConstantUtils.Intent.LOOK_WORK_TABLE_DETAILS,item)
						.to( LogDetailActivity.class)
						.launch();
			}
		});
	}

	/**
	 * 初始化刷新控件（发送）
	 */
	@BindView(R.id.refreshLayout_send)
	RefreshLayout mRefreshLayoutSend;
	private void initRefreshSend(){
		mRefreshLayoutSend.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
				pageNoSend=1;
				getP().loadDataSend(context,pageNoSend);
			}
		});
		mRefreshLayoutSend.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
				pageNoSend++;
				getP().loadDataSend(context,pageNoSend);
			}
		});
	}

	/**
	 * 初始化筛选
	 */
	@BindView(R.id.cb_shaixuan)
	TextView cb_shaixuan;
	@BindView(R.id.iv_screening)
	ImageView iv_screening;
	@BindView(R.id.ll_screening)
	LinearLayout ll_screening;
	@BindView(R.id.ll_shaixuan)
	View ll_shaixuan;
	@BindView(R.id.tv_startTime)
	TextView tv_startTime;
	@BindView(R.id.tv_endTime)
	TextView tv_endTime;
	@BindView(R.id.gv_person)
    ComputeHeightGridView gv_person;// 选择发送人
	private void initScreening() {
		gv_person.setOnItemClickListener(this);
		refreshPersonAdapter(false);
	}


	/**
	 * 选择时间 timeType：1）开始时间  2）结束时间
	 */
	private int timeType;
	private void showChooseDate() {
		String title=null;
		int year=0;
		int month=0;
		int day=0;
		switch (timeType) {
			case 1:
				title="开始时间";
				String startTime=tv_startTime.getText().toString().trim();
				if(TextUtils.isEmpty(startTime)){
					year= MyTimeUtils.getYear();
					month= MyTimeUtils.getMonth();
					day= MyTimeUtils.getDay();
				}else{
					String[] split=startTime.split("-");
					year=Integer.parseInt(split[0]);
					month=Integer.parseInt(split[1])-1;
					day=Integer.parseInt(split[2]);
				}
				break;
			case 2:
				title="结束时间";
				String endTime=tv_endTime.getText().toString().trim();
				if(TextUtils.isEmpty(endTime)){
					year= MyTimeUtils.getYear();
					month= MyTimeUtils.getMonth();
					day= MyTimeUtils.getDay();
				}else{
					String[] split=endTime.split("-");
					year=Integer.parseInt(split[0]);
					month=Integer.parseInt(split[1])-1;
					day=Integer.parseInt(split[2]);
				}
				break;
		}
		MyDatePickerDialog dialog=new MyDatePickerDialog(context, title, year, month, day, new MyDatePickerDialog.DateTimeListener() {
			@Override
			public void onSetTime(int year, int month, int day, String timeStr) {
				switch (timeType) {
					case 1:
						tv_startTime.setText(timeStr);
						break;
					case 2:
						tv_endTime.setText(timeStr);
						break;
				}
			}

			@Override
			public void onCancel() {
			}
		});
		dialog.show();
	}

	private boolean isDelModel_Person;
	private ShenPi_PersonAdatper personAdapter;
	private void refreshPersonAdapter(boolean isDelModel) {
		if (personAdapter == null) {
			personAdapter = new ShenPi_PersonAdatper(context,
					AddCurrentIds.getI().getChoiseIds(AddCurrentIds.TYPE_LOG, true));
			gv_person.setAdapter(personAdapter);
		} else {
			personAdapter.refreshAdapter(isDelModel);
		}
	}

	/**
	 * 跳转添加审批人act
	 */
	private void toAddRenACT() {
		isDelModel_Person = false;
		Intent intent = new Intent(context, AddShenPiRenActivity.class);
		intent.putExtra("type", 1);
		intent.putExtra("title", "选择审批人");
		startActivity(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshPersonAdapter(isDelModel_Person = false);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		if (parent.getId() == R.id.gv_person) {// 高级筛选的“选择发送人
			int count = parent.getAdapter().getCount();
			if (isDelModel_Person) {// 删除模式
				if (count >= 3) {
					if (position == count - 2) {// 倒数第二个item 添加审批人的item
						toAddRenACT();
					} else if (position == count - 1) {// 倒数第一个item 即删除图标的item
														// 只要切换删除模式即可
						isDelModel_Person = false;
						refreshPersonAdapter(isDelModel_Person);
					} else { // 移除
						personAdapter.removeItem(position);
					}
				} else {
					toAddRenACT();
				}
			} else {// 添加人模式
				if (count >= 3) {
					if (position == count - 1) {// 倒数第一个item 切换删除模式而已
						isDelModel_Person = true;
						refreshPersonAdapter(isDelModel_Person);
					} else {// 点击其他都是添加人
						toAddRenACT();
					}
				} else {
					toAddRenACT();
				}
			}
		}
	}


	/**
	 * 点击事件
	 */
	@OnClick({R.id.radio_shoudao,R.id.radio_send,R.id.ll_startTime,R.id.ll_endTime,R.id.btn_confirm,R.id.ll_screening})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.radio_shoudao://接收
				ll_screening.setVisibility(View.VISIBLE);
				mLlReceive.setVisibility(View.VISIBLE);
				mLlSend.setVisibility(View.GONE);
				break;
			case R.id.radio_send://发送
				ll_shaixuan.setVisibility(View.GONE);
				ll_screening.setVisibility(View.GONE);
				cb_shaixuan.setTextColor(getResources().getColor(R.color.gray_6));
				iv_screening.setImageResource(R.drawable.y_icon_gray_more_right);
				mLlReceive.setVisibility(View.GONE);
				mLlSend.setVisibility(View.VISIBLE);
				break;
			case R.id.ll_startTime://开始时间
				timeType = 1;
				showChooseDate();
				break;
			case R.id.ll_endTime://结束时间
				timeType = 2;
				showChooseDate();
				break;
			case R.id.btn_confirm://筛选-确定按钮
				cb_shaixuan.setTextColor(getResources().getColor(R.color.gray_6));
				iv_screening.setImageResource(R.drawable.y_icon_gray_more_right);
				ll_shaixuan.setVisibility(View.GONE);
				pageNoReceive = 1;
				startTime=tv_startTime.getText().toString().trim();
				endTime=tv_endTime.getText().toString().trim();
				getP().loadDataReceive(context, pageNoReceive,startTime,endTime);
				break;
			case R.id.ll_screening://筛选-按钮
				if (ll_shaixuan.getVisibility()==View.VISIBLE) {
					ll_shaixuan.setVisibility(View.GONE);
					cb_shaixuan.setTextColor(getResources().getColor(R.color.gray_6));
					iv_screening.setImageResource(R.drawable.y_icon_gray_more_right);
				} else {
					ll_shaixuan.setVisibility(View.VISIBLE);
					cb_shaixuan.setTextColor(getResources().getColor(R.color.light_blue));
					iv_screening.setImageResource(R.drawable.y_icon_xl);
				}
				break;
		}
	}

	//TODO ***********************************接口**************************************
	/**
	 * 刷新适配器-接收
	 */
	public void refreshAdapterReceive(List<RizhiListBean.RizhiList> dataList){
		if(!(dataList!=null && dataList.size()>0)){
			mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
			ToastUtils.showCustomToast("没有更多数据");
		}
		if(pageNoReceive ==1){
			//上拉刷新
			mReceiveAdapter.setNewData(dataList);
			mRefreshLayout.finishRefresh();
			mRefreshLayout.setNoMoreData(false);
		}else{
			//加载更多
			mReceiveAdapter.addData(dataList);
			mRefreshLayout.finishLoadMore();
		}
	}

	/**
	 * 刷新适配器-发送
	 */
	public void refreshAdapterSend(List<RizhiListBean.RizhiList> dataList){
		if(!(dataList!=null && dataList.size()>0)){
			mRefreshLayoutSend.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
			ToastUtils.showCustomToast("没有更多数据");
		}
		if(pageNoSend==1){
			//上拉刷新
			mSendAdapter.setNewData(dataList);
			mRefreshLayoutSend.finishRefresh();
			mRefreshLayoutSend.setNoMoreData(false);
		}else{
			//加载更多
			mSendAdapter.addData(dataList);
			mRefreshLayoutSend.finishLoadMore();
		}
	}

	/**
	 * 关闭刷新
	 */
	public void closeRefresh(){
		//关闭刷新，加载更多
		mRefreshLayout.finishRefresh();
		mRefreshLayout.finishLoadMore();
		mRefreshLayoutSend.finishRefresh();
		mRefreshLayoutSend.finishLoadMore();
	}









}
