package com.qwb.view.purchase.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.event.OrderEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.company.adapter.PurchaseOrderListAdapter;
import com.qwb.view.company.model.PurchaseOrderBean;
import com.qwb.view.purchase.parsent.PPurchaseOrderList;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.Calendar;
import java.util.List;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 采购单列表
 */
public class PurchaseOrderListActivity extends XActivity<PPurchaseOrderList> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_purchase_order_list;
    }

    @Override
    public PPurchaseOrderList newP() {
        return new PPurchaseOrderList();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 初始化EventBus
     */
    private void initEvent() {
        BusProvider.getBus().toFlowable(OrderEvent.class)
                .subscribe(new Consumer<OrderEvent>() {
                    @Override
                    public void accept(OrderEvent orderEvent) throws Exception {
                        if(orderEvent.getTag()==ConstantUtils.Event.TAG_ORDER){
                            pageNo=1;
                            getP().queryOrderPage(context,mEtSearch,pageNo,mStartDate,mEndDate);
                            //触发自动刷新
                            mRefreshLayout.autoRefresh();
                        }
                    }
                });
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        initEvent();
        initUI();
        getP().queryOrderPage(context,mEtSearch,pageNo,mStartDate,mEndDate);
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initAdapter();
        initRefresh();
        initScreening();//筛选时间，搜索
    }

    /**
     * 初始化UI-头部
     */
    TextView mTvHeadTitle;
    int mChangeType=1;//"订货"和"退货"类型：1：订货，2：退货（默认订货）
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);
        findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvHeadTitle = (TextView) findViewById(R.id.tv_head_title);
        mTvHeadTitle.setText("采购单列表");
        TextView tvHeadRight = findViewById(R.id.tv_head_right);
        tvHeadRight.setText("下单");
        tvHeadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().jumpActivity(context, PurchaseOrderActivity.class);
            }
        });
    }

    /**
     * 初始化筛选:时间和搜索
     */
    LinearLayout mRlSearch;
    EditText mEtSearch;
    TextView mIvSearch;
    TextView mTvStartEndTime;
    int mStartYear,mStartMonth,mStartDay,mEndYear,mEndMonth,mEndDay;//年，月，日
    String mStartDate,mEndDate;//开始时间，结束时间
    Calendar calendar=Calendar.getInstance();
    String mSearchOrder,mSearchRetreat;//记录“订货”的搜索，“退货”的搜索
    private void initScreening() {
        mRlSearch =(LinearLayout) findViewById(R.id.rl_search);
        mEtSearch=(EditText) findViewById(R.id.et_search);
        mIvSearch=(TextView) findViewById(R.id.iv_search);
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
        mStartDate = MyTimeUtils.getFirstOfMonth();
        mEndDate = MyTimeUtils.getCurrentDate();
        mTvStartEndTime.setText(mStartDate+"至"+mEndDate);
    }




    /**
     * 初始化适配器（订货）
     */
    RecyclerView mRvOrder;
    PurchaseOrderListAdapter mOrderAdapter;
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
        mOrderAdapter = new PurchaseOrderListAdapter();
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
                getP().queryOrderPage(context,mEtSearch,pageNo,mStartDate,mEndDate);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo++;
                getP().queryOrderPage(context,mEtSearch,pageNo,mStartDate,mEndDate);
            }
        });
    }


    /**
     * 刷新适配器
     */
    public void refreshAdapterOrder(List<PurchaseOrderBean> dataList){
        if(dataList == null || dataList.isEmpty()){
            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }else{
            //过滤合计
            PurchaseOrderBean row = dataList.get(dataList.size()-1);
            if("合计：".equals(row.getBillNo())){
                dataList.remove(row);
            }
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
        pageNo=1;
        getP().queryOrderPage(context,mEtSearch,pageNo,mStartDate,mEndDate);
    }

    /**
     * 筛选时间的对话框
     */
    private void showDialogStartEndTime() {
        new MyDoubleDatePickerDialog(context, "筛选时间", mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay,
                new MyDoubleDatePickerDialog.DateTimeListener() {
                    @Override
                    public void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate) {
                        mTvStartEndTime.setText(startDate+"至"+endDate);
                        mStartDate=startDate;
                        mEndDate=endDate;
                        pageNo=1;
                        getP().queryOrderPage(context,mEtSearch,pageNo,mStartDate,mEndDate);
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }


    /**
     * 适配器-item点击
     */
    private void onItemClickOrderRetreat(BaseQuickAdapter baseQuickAdapter, int i) {
        try{
            List<PurchaseOrderBean> dataList=baseQuickAdapter.getData();
            PurchaseOrderBean data=dataList.get(i);
            Router.newIntent(context)
                    .putString(ConstantUtils.Intent.ORDER_ID, "" + data.getId())// 订单id
                    .to(PurchaseOrderActivity.class)
                    .launch();
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }









}
