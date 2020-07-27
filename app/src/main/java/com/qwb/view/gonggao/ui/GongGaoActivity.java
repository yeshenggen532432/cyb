package com.qwb.view.gonggao.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kennyc.view.MultiStateView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.view.gonggao.model.GongGaoBean;
import com.qwb.utils.ActivityManager;
import com.qwb.view.gonggao.adapter.GongGaoAdapter;
import com.qwb.view.gonggao.parsent.PGongGao;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 公告
 */
public class GongGaoActivity extends XActivity<PGongGao> {

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_gonggao;
	}

	@Override
	public PGongGao newP() {
		return new PGongGao();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initUI();
		getP().queryData(context, pageNo, pageSize);
	}


	/**
	 * 初始化UI
	 */
	private void initUI() {
		initHead();
		initAdapter();
		initRefresh();
		initMultiStateView();
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
		mTvHeadTitle.setText("公告");
	}


	/**
	 * 初始化适配器
	 */
	@BindView(R.id.recyclerView)
	RecyclerView mRecyclerView;
	GongGaoAdapter mAdapter;
	private void initAdapter() {
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
		//添加分割线
		mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
				.colorResId(R.color.gray_e)
				.sizeResId(R.dimen.dp_0_5)
				.build());
		mAdapter = new GongGaoAdapter(context);
		mAdapter.openLoadAnimation();
		mRecyclerView.setAdapter(mAdapter);
		//item点击事件
		mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				try {
					GongGaoBean item = (GongGaoBean)adapter.getData().get(position);
//					Intent intent = new Intent(GongGaoActivity.this, WebViewActivity.class);
//					intent.putExtra("key", "4");
//					intent.putExtra("id", String.valueOf( item.getNoticeId()));
//					startActivity(intent);
					String url = String.format(Constans.WEB_URL_gonggao, SPUtils.getTK(), item.getNoticeId(), "12");
					ActivityManager.getInstance().jumpToWebX5Activity(context, url, null);
				 }catch (Exception e){
				 }
			}
		});

		//子item点击事件
		mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
			}
		});
	}

	/**
	 * 初始化刷新控件
	 */
	@BindView(R.id.refreshLayout)
	RefreshLayout mRefreshLayout;
	private int pageNo = 1;
	private int pageSize = 10;
	private void initRefresh(){
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
				pageNo = 1;
				getP().queryData(context, pageNo, pageSize);
			}
		});
		mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
				pageNo ++;
				getP().queryData(context, pageNo, pageSize);
			}
		});
	}

	/**
	 * 四种状态布局:content,empty,error,loading
	 */
	@BindView(R.id.multiStateView)
	MultiStateView mMultiStateView;
	private void initMultiStateView() {
		TextView tvName = mMultiStateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.empty_tv_name);
		tvName.setText("暂无公告");
		mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.refresh)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
					}
				});
	}

	/**
	 * 刷新适配器
	 */
	public void refreshAdapter(List<GongGaoBean> dataList){
		if(pageNo == 1){
			//上拉刷新
			mAdapter.setNewData(dataList);
			mRefreshLayout.finishRefresh();
			mRefreshLayout.setNoMoreData(false);

			//没数据或每页不足
			if(null != dataList){
				if(dataList.isEmpty()){
					mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
				}else if(dataList.size() < pageSize){
					mRefreshLayout.finishLoadMoreWithNoMoreData();
				}
			}
		}else{
			//加载更多
			mAdapter.addData(dataList);
			mRefreshLayout.setNoMoreData(false);
			mRefreshLayout.finishLoadMore();

			//每页不足
			if(null != dataList && dataList.size() < pageSize){
				mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
			}
		}

	}



}
