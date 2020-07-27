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
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.model.TabEntity;
import com.qwb.view.audit.adapter.AuditMySendAdapter;
import com.qwb.view.audit.adapter.SpMySpAdapter;
import com.qwb.view.audit.model.ShenPiIShenPiBean;
import com.qwb.view.audit.model.ShenPiIsendBean.ShenPiIsendItemBean;
import com.qwb.view.audit.parsent.PAuditCommon;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.widget.MyDatePickerDialog;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 我发起的，我审批的，我执行的（三个公用一个界面）
 */
public class XAuditCommonActivity extends XActivity<PAuditCommon> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_audit_common;
    }

    @Override
    public PAuditCommon newP() {
        return new PAuditCommon();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        queryData();
        doIntent();
    }

    private int mType = 1;//1.我发起的 2.我审批的 3.我执行的
    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            mType = intent.getIntExtra(ConstantUtils.Intent.TYPE, 1);
        }
    }
    private void doIntent(){
        if(1 == mType){
            mTvHeadTitle.setText("我发起的");
        }else if(2 == mType){
            mTvHeadTitle.setText("我审批的");
        }else if(3 == mType){
            mTvHeadTitle.setText("我执行的");
        }
    }

    private void initUI() {
        initHead();
        initScreening();
        initTab();
        initAdapter();
    }

    /**
     * 头部
     */
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);
        findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle = findViewById(R.id.tv_head_title);
        mTvHeadTitle.setText("我执行的");
    }

    /**
     * 初始化筛选:时间和搜索
     */
    LinearLayout mRlSearch;
    EditText mEtSearch;
    TextView mIvSearch;
    TextView mTvStartEndTime;
    private void initScreening() {
        mRlSearch = findViewById(R.id.rl_search);
        mEtSearch= findViewById(R.id.et_search);
        mIvSearch= findViewById(R.id.iv_search);
        mTvStartEndTime= findViewById(R.id.tv_start_end_time);
        TextView mTvSearch= findViewById(R.id.tv_search);
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
    RecyclerView mRecyclerView;
    AuditMySendAdapter mAdapter;
    SpMySpAdapter mAdapter2;
    private int pageNo = 1;
    private int pageSize = 10;
    RefreshLayout mRefreshLayout;
    private void initAdapter() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
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
                    ShenPiIsendItemBean bean = (ShenPiIsendItemBean) adapter.getData().get(position);
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
        mRefreshLayout = findViewById(R.id.refreshLayout);
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
     * 刷新，未完成数量
     */
    public void refreshAdapter(List<ShenPiIsendItemBean> list, int total) {
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
                mRefreshLayout.finishRefresh();
                mRefreshLayout.setNoMoreData(false);
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
