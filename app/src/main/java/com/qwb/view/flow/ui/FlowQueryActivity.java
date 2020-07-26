package com.qwb.view.flow.ui;

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
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.flow.adapter.QueryFlowAdapter;
import com.qwb.view.flow.model.QueryFlowBean;
import com.qwb.view.flow.parsent.PFlowQuery;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.widget.treedialog.MyTreeDialog;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.qwb.view.base.model.TreeBean;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 流动打卡查询
 */
public class FlowQueryActivity extends XActivity<PFlowQuery>{

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_flow_query;
    }


    @Override
    public PFlowQuery newP() {
        return new PFlowQuery();
    }

    public void initData(Bundle savedInstanceState) {
        initHead();
        initAdapter();
        initRefresh();
        initSearch();
        getP().queryFlow(mIds, mStartDate, mEndDate, pageNo, pageSize);
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
    @BindView(R.id.layout_member)
    View mLayoutMember;
    @BindView(R.id.tv_member)
    TextView mTvMember;
    private String mIds = SPUtils.getID();
    private void initSearch() {
        Calendar calendar=Calendar.getInstance();
        mStartYear = mEndYear = calendar.get(Calendar.YEAR);//年
        mStartMonth = mEndMonth = calendar.get(Calendar.MONTH);//月
        mStartDay = mEndDay = calendar.get(Calendar.DAY_OF_MONTH);//日
//        mStartDate = MyTimeUtils.getFirstOfMonth();
        mEndDate = MyTimeUtils.getTodayStr();
//        MyTimeUtils.getYMDArray()//设置年月日
//        mTvTime.setText(mStartDate + "至" +mEndDate);
        mLayoutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogStartEndTime();
            }
        });
        mLayoutMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null == mTreeDatas || mTreeDatas.isEmpty()){
                    getP().queryMemberTree(context);
                }else{
                    showDialogMember();
                }
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
                finish();
            }
        });
        mTvHeadTitle.setText("流动打卡查询");
    }

    /**
     * 初始化适配器
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    QueryFlowAdapter mAdapter;
    private void initAdapter() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_5)
                .build());
        mAdapter = new QueryFlowAdapter(context);
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
        //item点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                }catch (Exception e){
                    ToastUtils.showError(e);
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
                getP().queryFlow(mIds, mStartDate, mEndDate, pageNo, pageSize);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo ++;
                getP().queryFlow(mIds, mStartDate, mEndDate, pageNo, pageSize);
            }
        });
    }

    public void refreshAdapter(List<QueryFlowBean> queryFlowBeanList){
        if(null == mAdapter || null == queryFlowBeanList){
            return;
        }
        if(queryFlowBeanList.size() < 10){
            mRefreshLayout.finishLoadMoreWithNoMoreData();
        }
        if(pageNo == 1){
            mAdapter.setNewData(queryFlowBeanList);
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setNoMoreData(false);
        }else{
            mAdapter.addData(queryFlowBeanList);
            mRefreshLayout.finishLoadMore();
        }
    }


    /**
     * tree:选择员工对话框
     */
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
                mIds = checkIds;
                mRefreshLayout.autoRefresh();
            }
        });
    }

    public void refreshAdapterMemberTree(List<TreeBean> mDatas){
        this.mTreeDatas = mDatas;
        showDialogMember();
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
                        getP().queryFlow(mIds, mStartDate, mEndDate, pageNo, pageSize);
                        mTvTime.setText(startDate+"至"+endDate);
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }




}
