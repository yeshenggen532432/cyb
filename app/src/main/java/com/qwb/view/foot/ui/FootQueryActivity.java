package com.qwb.view.foot.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.qwb.utils.MyDividerUtil;
import com.qwb.view.foot.adapter.FootAdapter;
import com.qwb.view.foot.model.FootBean;
import com.qwb.view.foot.parsent.PFootQuery;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;
import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 我的足迹（查询）
 */
public class FootQueryActivity extends XActivity<PFootQuery>{

    //复用：打卡查询
    @Override
    public int getLayoutId() {
        return R.layout.x_activity_foot_query;
    }


    @Override
    public PFootQuery newP() {
        return new PFootQuery();
    }

    public void initData(Bundle savedInstanceState) {
        initHead();
        initAdapter();
        initRefresh();
        initSearch();
        queryData();
    }

    public void queryData(){
        getP().queryFlow(mStartDate, mEndDate, pageNo, pageSize);
    }

    /**
     * 初始化筛选:时间，人员
     */
    @BindView(R.id.parent)
    View parent;
    @BindView(R.id.layout_time)
    View mLayoutTime;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    private void initSearch() {
        Calendar calendar=Calendar.getInstance();
        mStartYear = mEndYear = calendar.get(Calendar.YEAR);//年
        mStartMonth = mEndMonth = calendar.get(Calendar.MONTH);//月
        mStartDay = mEndDay = calendar.get(Calendar.DAY_OF_MONTH);//日
        mEndDate = MyTimeUtils.getTodayStr();
        mLayoutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogStartEndTime();
            }
        });
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
        mTvHeadTitle.setText("旅游画册");
    }

    /**
     * 初始化适配器
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    FootAdapter mAdapter;
    private void initAdapter() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerView.addItemDecoration(MyDividerUtil.getH5CGray(context));
        mAdapter = new FootAdapter(context);
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化刷新控件（我的客户）
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
                queryData();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo ++;
                queryData();
            }
        });
    }

    public void refreshAdapter(List<FootBean> list){
        if(null == mAdapter || null == list){
            return;
        }
        if(list.size() < 10){
            mRefreshLayout.finishLoadMoreWithNoMoreData();
        }
        if(pageNo == 1){
            mAdapter.setNewData(list);
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setNoMoreData(false);
        }else{
            mAdapter.addData(list);
            mRefreshLayout.finishLoadMore();
        }
    }

    /**
     * 筛选时间的对话框
     */
    private int mStartYear,mStartMonth,mStartDay,mEndYear,mEndMonth,mEndDay;//年，月，日
    private String mStartDate,mEndDate;//开始时间，结束时间
    private void showDialogStartEndTime() {
        new MyDoubleDatePickerDialog(context, "筛选时间", mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay,
                new MyDoubleDatePickerDialog.DateTimeListener() {
                    @Override
                    public void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate) {
                        mStartYear = year; mStartMonth = month; mStartDay = day;
                        mEndYear = year2; mEndMonth = month2; mEndDay = day2;
                        mStartDate = startDate;
                        mEndDate = endDate;
                        pageNo = 1;
                        mTvTime.setText(startDate+"至"+endDate);
                        queryData();
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }




}
