package com.qwb.view.order.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.view.order.adapter.OrderAdapter;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.view.order.parsent.PHistoryOrder;
import com.qwb.utils.ToastUtils;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.qwb.view.order.model.QueryDhorderBean;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.Calendar;
import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 创建描述：历史订单
 */
public class HistoryOrderActivity extends XActivity<PHistoryOrder> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_history_order;
    }

    @Override
    public PHistoryOrder newP() {
        return new PHistoryOrder();
    }



    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        getP().loadDataOrder(context,mEtSearch,pageNo,mStartDate,mEndDate,cid);//获取订货列表
    }

    private String cid;//客户id
    private void initIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            cid=intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initAdapter();//订货
        initRefresh();
        initScreening();//筛选时间，搜索
    }

    /**
     * 初始化筛选:时间和搜索
     */
    RelativeLayout mRlSearch;
    EditText mEtSearch;
    ImageView mIvSearch;
    TextView mTvStartEndTime;
    int mStartYear,mStartMonth,mStartDay,mEndYear,mEndMonth,mEndDay;//年，月，日
    String mStartDate,mEndDate;//开始时间，结束时间
    Calendar calendar=Calendar.getInstance();
    String mSearchOrder,mSearchRetreat;//记录“订货”的搜索，“退货”的搜索
    private void initScreening() {
        mRlSearch =(RelativeLayout) findViewById(R.id.rl_search);
        mEtSearch=(EditText) findViewById(R.id.et_search);
        mIvSearch=(ImageView) findViewById(R.id.iv_search);
        mTvStartEndTime=(TextView) findViewById(R.id.tv_start_end_time);
        TextView mTvSearch=(TextView) findViewById(R.id.tv_search);
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTvSearch();//搜索：显示搜索和隐藏搜索
            }
        });
        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickIvSearch();//搜索按钮：请求数据
            }
        });
        mStartYear=mEndYear=calendar.get(Calendar.YEAR);//年
        mStartMonth=mEndMonth=calendar.get(Calendar.MONTH);//月
        mStartDay=mEndDay=calendar.get(Calendar.DAY_OF_MONTH);//日
        mTvStartEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogStartEndTime();
            }
        });
    }

    /**
     * 初始化UI-头部
     */
    TextView mTvHeadTitle;
    private void initHead() {
        OtherUtils.setStatusBarColor(context);//状态栏颜色，透明度
        findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvHeadTitle = (TextView) findViewById(R.id.tv_head_title);
        mTvHeadTitle.setText("历史订单");
        ImageView mIvHeadRight = (ImageView) findViewById(R.id.iv_head_right);
    }

    /**
     * 初始化适配器（订货）
     */
    RecyclerView mRvOrder;
    OrderAdapter mOrderAdapter;
    private int pageNo = 1;
    private void initAdapter() {
        mRvOrder = (RecyclerView) findViewById(R.id.rv_receiver);
        mRvOrder.setHasFixedSize(true);
        mRvOrder.setLayoutManager(new LinearLayoutManager(this));
        //添加分割线
        mRvOrder.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_5)
                .build());
        mOrderAdapter = new OrderAdapter();
        mOrderAdapter.openLoadAnimation();
        mRvOrder.setAdapter(mOrderAdapter);
        mOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                onItemClickOrderRetreat(baseQuickAdapter, i);
            }
        });
    }

    /**
     * 初始化刷新控件（订货）
     */
    RefreshLayout mRefreshLayout;
    private void initRefresh(){
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNo=1;
                getP().loadDataOrder(context,mEtSearch,pageNo,mStartDate,mEndDate,cid);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo++;
                getP().loadDataOrder(context,mEtSearch,pageNo,mStartDate,mEndDate,cid);
            }
        });
    }


    /**
     * 刷新适配器-订货下单
     */
    public void refreshAdapterOrder(List<QueryDhorderBean.Rows> dataList){
        if(!(dataList!=null && dataList.size()>0)){
            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }
        if(pageNo==1){
            //上拉刷新
            mOrderAdapter.setNewData(dataList);
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setNoMoreData(false);
        }else{
            //加载更多
            mOrderAdapter.addData(dataList);
            mRefreshLayout.finishLoadMore();
        }
    }

    /**
     * 关闭刷新
     */
    public void closeRefresh(){
        //关闭刷新，加载更多
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }


    /**
     * 搜索：显示搜索和隐藏搜索 - 关闭的时候：清空搜索
     */
    private void onClickTvSearch() {
        if(mRlSearch.getVisibility()== View.VISIBLE){
            mRlSearch.setVisibility(View.GONE);
            mEtSearch.setText("");
            mSearchOrder=null;
        }else{
            mRlSearch.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 搜索按钮：请求数据
     */
    private void onClickIvSearch() {
        String searchStr=mEtSearch.getText().toString();
        if(TextUtils.isEmpty(searchStr)){
            ToastUtils.showCustomToast("请输入要搜索的关键字");
            return;
        }
        pageNo=1;
        getP().loadDataOrder(context,mEtSearch,pageNo,mStartDate,mEndDate,cid);
    }

    /**
     * 筛选时间的对话框
     */
    private void showDialogStartEndTime() {
        new MyDoubleDatePickerDialog(context, "筛选时间", mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay,
                new MyDoubleDatePickerDialog.DateTimeListener() {
                    @Override
                    public void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate) {
                        mTvStartEndTime.setText(startDate+" 至 "+endDate);
                        mStartDate=startDate;
                        mEndDate=endDate;
                        pageNo=1;
                        getP().loadDataOrder(context,mEtSearch,pageNo,mStartDate,mEndDate,cid);

                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }


    /**
     * 适配器
     */
    private void onItemClickOrderRetreat(BaseQuickAdapter baseQuickAdapter, int i) {
        List<QueryDhorderBean.Rows> dataList=baseQuickAdapter.getData();
        QueryDhorderBean.Rows data=dataList.get(i);
        Router.newIntent(context)
                .putInt(ConstantUtils.Intent.ORDER_ID, data.getId())// 订单id
                .putInt(ConstantUtils.Intent.ORDER_TYPE, 1)
//                .to(OrderDetailActivity.class)
                .launch();
    }










}
