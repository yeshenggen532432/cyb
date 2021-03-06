package com.qwb.view.delivery.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.event.DeliveryEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.delivery.adapter.Delivery2Adapter;
import com.qwb.view.delivery.model.DeliveryBean;
import com.qwb.view.delivery.parsent.PDelivery6;
import com.qwb.utils.MyStringUtil;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 创建描述：物流配送：配送完成（pc端是：已生成发货单）
 */
public class Delivery6Fragment extends XFragment<PDelivery6> {
	@Override
	public int getLayoutId() {
		return R.layout.x_fragment_delivery_one;
	}

	@Override
	public PDelivery6 newP() {
		return new PDelivery6();
	}

	public Delivery6Fragment() {
	}


	@Override
	public boolean useEventBus() {
		return true;
	}

	/**
	 * 初始化EventBus
	 */
	private void initEvent() {
		BusProvider.getBus().toFlowable(DeliveryEvent.class)
				.subscribe(new Consumer<DeliveryEvent>() {
					@Override
					public void accept(DeliveryEvent event) throws Exception {
						if(event.getTag()== ConstantUtils.Event.TAG_DELIVERY){
							if(event!=null ){
								if(event.getPsState().equals(psState)){
									mRefreshLayout.autoRefresh();//触发自动刷新
								}
							}
						}
					}
				});
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initEvent();
		initScreening();
		initAdapter();
		initRefresh();
		getP().loadData(context, psState, pageNo, pageSize, mStartDate, mEndDate, mSearch);
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
	@BindView(R.id.view_bg)
	View mViewBg;
	int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;//年，月，日
	String mStartDate, mEndDate;//开始时间，结束时间
	Calendar calendar = Calendar.getInstance();
	String mSearch;
	private void initScreening() {
		mTvSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mRlSearch.getVisibility() == View.VISIBLE){
					mRlSearch.setVisibility(View.GONE);
					mEtSearch.setText("");
					mSearch = "";
				}else{
					mRlSearch.setVisibility(View.VISIBLE);
				}
			}
		});
		mIvSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSearch = mEtSearch.getText().toString();
				if(MyStringUtil.isEmpty(mSearch)){
					ToastUtils.showCustomToast("请输入要搜索的关键字");
					return;
				}
				pageNo = 1;
				getP().loadData(context, psState, pageNo, pageSize, mStartDate, mEndDate, mSearch);
			}
		});
		mViewBg.setVisibility(View.VISIBLE);
		mViewBg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtils.showCustomToast("暂未开发");
			}
		});
		mStartYear = mEndYear = calendar.get(Calendar.YEAR);//年
		mStartMonth = mEndMonth = calendar.get(Calendar.MONTH);//月
		mStartDay = mEndDay = calendar.get(Calendar.DAY_OF_MONTH);//日
		mStartDate = mEndDate = MyTimeUtils.getTodayStr();
		mTvStartEndTime.setText(mStartDate + "至" + mEndDate);
		mTvStartEndTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogStartEndTime();
			}
		});
	}
	/**
	 * 初始化适配器
	 */
	@BindView(R.id.rv_receiver)
	RecyclerView mRvOrder;
	Delivery2Adapter mDeliveryAdapter;
	private int pageNo = 1;
	private int pageSize = 10;
	private String psState = "6";//0.待分配 1.待接收 2.已接收 3.配送中 4.已收货 5.配送终止 6.配送完成（已生成发货单）
	private void initAdapter() {
		mRvOrder.setHasFixedSize(true);
		mRvOrder.setLayoutManager(new LinearLayoutManager(context));
		//添加分割线
		mRvOrder.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
				.colorResId(R.color.gray_e)
				.sizeResId(R.dimen.dp_5)
				.build());
		mDeliveryAdapter = new Delivery2Adapter();
		mDeliveryAdapter.openLoadAnimation();
		mRvOrder.setAdapter(mDeliveryAdapter);
		mRvOrder.addOnItemTouchListener(new OnItemClickListener() {
			@Override
			public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
				DeliveryBean bean = (DeliveryBean) baseQuickAdapter.getData().get(position);
				Router.newIntent(context)
						.putString(ConstantUtils.Intent.ID, "" + bean.getId())
						.to(DeliveryDetailActivity.class)
						.launch();
			}
		});
		//子item点击事件
		mDeliveryAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
				DeliveryBean mDelivery =(DeliveryBean) adapter.getData().get(position);
				switch (view.getId()){
					case R.id.right:
						ActivityManager.getInstance().jumpActivityNavMap(context,mDelivery.getLatitude(), mDelivery.getLongitude(), mDelivery.getAddress() );
						break;
				}
			}
		});
	}

	/**
	 * 初始化刷新控件（订货）
	 */
	@BindView(R.id.refreshLayout)
	RefreshLayout mRefreshLayout;
	private void initRefresh(){
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
				pageNo=1;
				getP().loadData(context, psState, pageNo, pageSize, mStartDate, mEndDate, mSearch);
			}
		});
		mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
				pageNo++;
				getP().loadData(context, psState, pageNo, pageSize, mStartDate, mEndDate, mSearch);
			}
		});
	}

	/**
	 * 刷新适配器-订货下单
	 */
	public void refreshAdapterOrder(List<DeliveryBean> dataList){
		if(!(dataList!=null && dataList.size()<10)){
			mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
			ToastUtils.showCustomToast("没有更多数据");
		}
		if(pageNo==1){
			//上拉刷新
			mDeliveryAdapter.setNewData(dataList);
			mRefreshLayout.finishRefresh();
			mRefreshLayout.setNoMoreData(false);
		}else{
			//加载更多
			mDeliveryAdapter.addData(dataList);
			mRefreshLayout.finishLoadMore();
		}
	}

	/**
	 * 关闭刷新，加载更多
	 */
	public void closeRefresh(){
		mRefreshLayout.finishRefresh();
		mRefreshLayout.finishLoadMore();
	}

	/**
	 * 筛选时间的对话框
	 */
	private void showDialogStartEndTime() {
		new MyDoubleDatePickerDialog(context, "筛选时间", mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay,
				new MyDoubleDatePickerDialog.DateTimeListener() {
					@Override
					public void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate) {
						mTvStartEndTime.setText(startDate + " 至 " + endDate);
						mStartYear = year;
						mStartMonth = month;
						mStartDay = day;
						mEndYear = year2;
						mEndMonth = month2;
						mEndDay = day2;
						mStartDate = startDate;
						mEndDate = endDate;
						pageNo = 1;
						getP().loadData(context, psState, pageNo, pageSize, mStartDate, mEndDate, mSearch);
					}

					@Override
					public void onCancel() {
					}
				}).show();
	}


}