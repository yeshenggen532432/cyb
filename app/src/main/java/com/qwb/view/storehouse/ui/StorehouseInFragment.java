package com.qwb.view.storehouse.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qwb.common.TypeEnum;
import com.qwb.view.storehouse.adapter.StorehouseStayInAdapter;
import com.qwb.view.storehouse.model.StorehouseStayInBean;
import com.qwb.view.storehouse.parsent.PStorehouseInFragment;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XFragment;

/**
 * 库位：待入仓发票
 */
public class StorehouseInFragment extends XFragment<PStorehouseInFragment> {


    public StorehouseInFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.x_fragment_storehouse_in;
    }

    @Override
    public PStorehouseInFragment newP() {
        return new PStorehouseInFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        getP().queryData(context, mSearchStr, pageNo, mStartDate, mEndDate);
    }

    private void initUI() {
        initScreening();
        initAdapter();
    }

    /**
     * 初始化筛选:时间和搜索
     */
    @BindView(R.id.rl_search)
    View mViewSearch;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.tv_screening_search)
    TextView mTvScreeningSearch;
    @BindView(R.id.tv_start_end_time)
    TextView mTvStartEndTime;
    private String mSearchStr;
    private String mStartDate, mEndDate = MyTimeUtils.getTodayStr();
    private void initScreening() {
//        mTvStartEndTime.setText(mStartDate + "至" + mEndDate);
        mTvScreeningSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewSearch.getVisibility() == View.VISIBLE) {
                    mViewSearch.setVisibility(View.GONE);
                    mEtSearch.setText("");
                    mSearchStr = "";
                } else {
                    mViewSearch.setVisibility(View.VISIBLE);
                }
            }
        });
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchStr = mEtSearch.getText().toString().trim();
                pageNo = 1;
                getP().queryData(context, mSearchStr, pageNo, mStartDate, mEndDate);
            }
        });
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
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    StorehouseStayInAdapter mAdapter;
    private int pageNo = 1;
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_5)
                .build());
        mAdapter = new StorehouseStayInAdapter();
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                try {
                    StorehouseStayInBean item = (StorehouseStayInBean)baseQuickAdapter.getData().get(i);
                    ActivityManager.getInstance().jumpToStorehouseInActivity(context, item.getId(), TypeEnum.UPDATE);
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNo = 1;
                getP().queryData(context, mSearchStr, pageNo, mStartDate, mEndDate);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo++;
                getP().queryData(context, mSearchStr, pageNo, mStartDate, mEndDate);
            }
        });

    }


    /**
     * 刷新适配器
     */
    public void refreshAdapter(List<StorehouseStayInBean> dataList) {
        if (!(dataList != null && dataList.size() > 0)) {
            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
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
        }
    }


    /**
     * 筛选时间的对话框
     */
    private void showDialogStartEndTime() {
        new MyDoubleDatePickerDialog(context, "筛选时间", mStartDate, mEndDate,
                new MyDoubleDatePickerDialog.DateTimeListener() {
                    @Override
                    public void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate) {
                        mTvStartEndTime.setText(startDate + "至" + endDate);
                        mStartDate = startDate;
                        mEndDate = endDate;
                        pageNo = 1;
                        getP().queryData(context, mSearchStr, pageNo, mStartDate, mEndDate);
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }


}
