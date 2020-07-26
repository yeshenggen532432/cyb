package com.qwb.view.customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qwb.view.customer.adapter.CustomerVisitAdapter;
import com.qwb.view.customer.model.CustomerVisitBean;
import com.qwb.view.customer.model.CustomerVisitResult;
import com.qwb.view.customer.parsent.PCustomerVisit;
import com.qwb.view.storehouse.model.StorehouseInBean;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 客户拜访（我的拜访）
 */
public class CustomerVisitActivity extends XActivity<PCustomerVisit> {
    @Override
    public int getLayoutId() {
        return R.layout.x_activity_customer_visit;
    }

    @Override
    public PCustomerVisit newP() {
        return new PCustomerVisit();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        queryData();
    }

    private String mSearch, mStartDate, mEndDate, mMemberIds, mVisitId;
    public void queryData(){
        getP().queryData(context, mSearch, mStartDate, mEndDate, mCustomerId, mVisitId, mMemberIds);
    }

    private String mCustomerId, mType;
    private void initIntent(){
        Intent intent = getIntent();
        if (intent != null){
            mCustomerId = intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
            mType = intent.getStringExtra(ConstantUtils.Intent.TYPE);
        }
    }

    private void initUI() {
        initHead();
        initScreening();
        initAdapter();
    }

    @BindView(R.id.head_left)
    View mViewLeft;
    @BindView(R.id.head_right)
    View mViewRight;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);
        mTvHeadTitle.setText("我的拜访");
        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
    }

    /**
     * 初始化筛选
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
//    private String mStartDate = MyTimeUtils.getFirstOfMonth(), mEndDate = MyTimeUtils.getTodayStr();
    private void initScreening() {
        mTvScreeningTab1.setText("拜访时间");
        mTvScreeningTab2.setText("人员");
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doScreeningSearch();
            }
        });
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
    }

    //筛选1
    private void doScreeningTab1(){

    }
    //筛选2
    private void doScreeningTab2(){
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

    }

    /**
     * 初始化适配器
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    CustomerVisitAdapter mAdapter;
    private int pageNo = 1;
    private int pageSize = 10;
    private StorehouseInBean mCurrentItem;
    private int mPosition;
    private void initAdapter() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_1)
                .build());
        mAdapter = new CustomerVisitAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                try {
                    mCurrentItem = (StorehouseInBean) baseQuickAdapter.getData().get(i);
                    mPosition = i;
                } catch (Exception e) {
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNo = 1;
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo++;
            }
        });

    }


    /**
     * 刷新适配器
     */
    public void refreshAdapter(CustomerVisitResult bean) {
        List<CustomerVisitBean> dataList = bean.getRows();
        if (null == dataList) {
            return;
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







}
