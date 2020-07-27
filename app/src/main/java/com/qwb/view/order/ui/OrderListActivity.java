package com.qwb.view.order.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qwb.common.OrderListEnum;
import com.qwb.common.OrderTypeEnum;
import com.qwb.utils.Constans;
import com.qwb.utils.MyMenuUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.order.adapter.OrderListAdapter;
import com.qwb.view.order.model.OrderBean;
import com.qwb.view.order.parsent.POrderList;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.event.OrderEvent;
import com.qwb.event.RetreatEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.functions.Consumer;

/**
 * 订货下单，退货下单
 */
public class OrderListActivity extends XActivity<POrderList> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_order_list2;
    }
//    public int getLayoutId() {
//        return R.layout.x_activity_order_list;
//    }

    @Override
    public POrderList newP() {
        return new POrderList();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 初始化EventBus
     */
    @Override
    public void bindEvent() {
        //订货-刷新
        BusProvider.getBus().toFlowable(OrderEvent.class)
                .subscribe(new Consumer<OrderEvent>() {
                    @Override
                    public void accept(OrderEvent orderEvent) throws Exception {
                        if (orderEvent.getTag() == ConstantUtils.Event.TAG_ORDER) {
                            queryDataOrder(1);
                            mRefreshLayout.autoRefresh();
                        }
                    }
                });
        //退货-刷新
        BusProvider.getBus().toFlowable(RetreatEvent.class)
                .subscribe(new Consumer<RetreatEvent>() {
                    @Override
                    public void accept(RetreatEvent retreatEvent) throws Exception {
                        if (retreatEvent.getTag() == ConstantUtils.Event.TAG_RETREAT) {
                            queryDataRetreat(1);
                            mRefreshLayoutRetreat.autoRefresh();
                        }
                    }
                });
    }

    /**
     * 订货列表
     */
    public void queryDataOrder(int pageNo) {
        this.pageNo = pageNo;
        getP().queryDataOrder(context, mEtSearch, pageNo, mStartDate, mEndDate, mCustomerId);
    }

    /**
     * 退货列表
     */
    public void queryDataRetreat(int pageNoRetreat) {
        this.pageNoRetreat = pageNoRetreat;
        getP().queryDataRetreat(context, mEtSearch, pageNoRetreat, mStartDate2, mEndDate2);//获取退货列表
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        doIntent();
    }

    private int mType = OrderListEnum.ORDER.getType();
    private String mCustomerId;
    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null){
            mType = intent.getIntExtra(ConstantUtils.Intent.TYPE, OrderListEnum.ORDER.getType());
            mCustomerId = intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
        }
    }

    private void doIntent(){
        queryDataOrder(1);
        if (mType == OrderListEnum.HISTORY_ORDER.getType()){
            //历史订单
            mTvHeadTitle.setText("历史订单");
            mIvHeadRight.setVisibility(View.GONE);
            mViewRight.setClickable(false);
            mViewBottom.setVisibility(View.GONE);
        }else{
            //订货下单；退货下单
            queryDataRetreat(1);
        }
    }

    /**
     * 初始化UI
     */
    @BindView(R.id.view_order)
    LinearLayout mLlOrder;
    @BindView(R.id.view_retreat)
    LinearLayout mLlRetreat;
    private void initUI() {
        initHead();
        initAdapterOrder();//订货
        initAdapterRetreat();//退货
        initScreening();//筛选: 时间，搜索
        initBottom();//底部：下单，退货
    }

    @BindView(R.id.head_left)
    View mViewLeft;
    @BindView(R.id.head_right)
    View mViewRight;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.iv_head_right)
    ImageView mIvHeadRight;
    private int mChangeType = 1;//1.订货下单；2.退货下单
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);
        mTvHeadTitle.setText("订货下单");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_25), (int) getResources().getDimension(R.dimen.dp_25));
        mIvHeadRight.setLayoutParams(params);
        mIvHeadRight.setImageResource(R.mipmap.ic_white_change);

        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doChangeOrderType();//“订货”和“退货”切换
            }
        });
    }

    /**
     * 初始化适配器（订货）
     */
    @BindView(R.id.refreshLayout_order)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.rv_order)
    RecyclerView mRvOrder;
    OrderListAdapter mOrderAdapter;
    private int pageNo = 1;
    private OrderBean mCurrentBean;
    private int mCurrentPosition;
    private void initAdapterOrder() {
        mRvOrder.setLayoutManager(new LinearLayoutManager(this));
        mRvOrder.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_5)
                .build());
        mOrderAdapter = new OrderListAdapter();
        mRvOrder.setAdapter(mOrderAdapter);
        mOrderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                doItemClickOrderRetreat(adapter, position, view);
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                queryDataOrder(1);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                queryDataOrder(pageNo + 1);
            }
        });
    }

    /**
     * 初始化适配器（退货）
     */
    @BindView(R.id.rv_retreat)
    RecyclerView mRvRetreat;
    OrderListAdapter mRetreatAdapter;
    private int pageNoRetreat = 1;
    @BindView(R.id.refreshLayout_retreat)
    RefreshLayout mRefreshLayoutRetreat;
    private void initAdapterRetreat() {
        mRvRetreat.setLayoutManager(new LinearLayoutManager(this));
        mRvRetreat.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_5)
                .build());
        mRetreatAdapter = new OrderListAdapter();
        mRvRetreat.setAdapter(mRetreatAdapter);
        mRetreatAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                doItemClickOrderRetreat(adapter, position, view);
            }
        });
        mRefreshLayoutRetreat.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                queryDataRetreat(1);
            }
        });
        mRefreshLayoutRetreat.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                queryDataRetreat(pageNoRetreat + 1);
            }
        });
    }

    /**
     * 初始化筛选:时间和搜索
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
    @BindView(R.id.tv_screening_tab1)
    TextView mTvScreeningTab1;
    @BindView(R.id.tv_screening_tab2)
    TextView mTvScreeningTab2;
    String mStartDate, mEndDate, mStartDate2, mEndDate2;//开始时间，结束时间
    String mSearchOrder, mSearchRetreat;//记录“订货”的搜索，“退货”的搜索
    private void initScreening() {
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
    }


    /**
     * 底部：下单，退货
     */
    @BindView(R.id.view_bottom)
    View mViewBottom;
    @BindView(R.id.tv_bottom_red)
    View mTvBottomRed;
    private void initBottom() {
        if (Constans.ISDEBUG){
            mTvBottomRed.setVisibility(View.VISIBLE);
        }else{
            mTvBottomRed.setVisibility(View.GONE);
        }
        //订货下单
        findViewById(R.id.tv_bottom_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MyMenuUtil.hasMenuDhOrder();
                    ActivityManager.getInstance().jumpToStep5Activity(context, OrderTypeEnum.ORDER_DHXD_ADD,null, null, null, null,  null,0 );
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }

            }
        });
        //退货下单
        findViewById(R.id.tv_bottom_retreat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().jumpToStep5Activity(context, OrderTypeEnum.ORDER_THXD_ADD,null, null, null, null,  null, 0);
            }
        });
        //红字单
        findViewById(R.id.tv_bottom_red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().jumpToStep5Activity(context, OrderTypeEnum.ORDER_RED_ADD,null, null, null, null,  null, 1);
            }
        });
    }



    //TODO*************************************************************************************************************************************************
    /**
     * 刷新适配器-订货下单
     */
    public void refreshAdapterOrder(List<OrderBean> dataList) {
        if (!(dataList != null && dataList.size() > 0)) {
            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }
        if (pageNo == 1) {
            //上拉刷新
            mOrderAdapter.setNewData(dataList);
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setNoMoreData(false);
        } else {
            //加载更多
            mOrderAdapter.addData(dataList);
            mRefreshLayout.finishLoadMore();
        }
    }

    /**
     * 刷新适配器-订货下单
     */
    public void refreshAdapterRetreat(List<OrderBean> dataList) {
        if (!(dataList != null && dataList.size() > 0)) {
            mRefreshLayoutRetreat.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }
        if (pageNoRetreat == 1) {
            //上拉刷新
            mRetreatAdapter.setNewData(dataList);
            mRefreshLayoutRetreat.finishRefresh();
            mRefreshLayoutRetreat.setNoMoreData(false);
        } else {
            //加载更多
            mRetreatAdapter.addData(dataList);
            mRefreshLayoutRetreat.finishLoadMore();
        }
    }

    /**
     * 关闭刷新
     */
    public void closeRefresh() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        mRefreshLayoutRetreat.finishRefresh();
        mRefreshLayoutRetreat.finishLoadMore();
    }
    /**
     * “订货”和“退货”切换
     */
    private void doChangeOrderType() {
        String searchStr = mEtSearch.getText().toString().trim();
        if (1  == mChangeType) {
            mChangeType = 2;
            mSearchOrder = searchStr;
            showChangeOrderType(View.GONE, View.VISIBLE, "退货下单", mStartDate2, mEndDate2, mSearchRetreat);
        } else {
            mChangeType = 1;
            mSearchRetreat = searchStr;
            showChangeOrderType(View.VISIBLE, View.GONE, "订货下单", mStartDate, mEndDate, mSearchOrder);
        }
    }

    /**
     * 切换：“订货下单”，“退货下单”；显示不同的信息
     */
    public void showChangeOrderType(int visibleOrder, int visibleRetreat, String title, String startDate, String endDate, String search){
        mLlOrder.setVisibility(visibleOrder);
        mLlRetreat.setVisibility(visibleRetreat);
        mTvHeadTitle.setText(title);
        if (MyStringUtil.isEmpty(startDate)) {
            mTvScreeningTab1.setText("筛选时间");
        } else {
            mTvScreeningTab1.setText(startDate + "至" + endDate);
        }
        mEtSearch.setText(search);
        if (MyStringUtil.isNotEmpty(search)) {
            mViewSearch.setVisibility(View.VISIBLE);
        } else {
            mViewSearch.setVisibility(View.GONE);
        }
    }

    /**
     * 适配器（订货,退货）-item点击
     */
    private void doItemClickOrderRetreat(BaseQuickAdapter adapter, int position, View view) {
        try {
            List<OrderBean> dataList = adapter.getData();
            mCurrentBean = dataList.get(position);
            mCurrentPosition = position;
            switch (view.getId()){
                case R.id.tv_cancel:
                    getP().cancelOrder(context, mCurrentBean.getId() );
                    break;
                case R.id.content:
                    int redMark = mCurrentBean.getRedMark();
                    OrderTypeEnum orderType = OrderTypeEnum.ORDER_DHXD_LIST;//订货
                    if (2 == mChangeType){
                        orderType = OrderTypeEnum.ORDER_THXD_LIST;//退货
                    }else{
                        if(1 == redMark){
                            orderType = OrderTypeEnum.ORDER_RED_LIST;//红字单
                        }
                    }
                    if(mType == OrderListEnum.HISTORY_ORDER.getType()){
                        orderType = OrderTypeEnum.ORDER_HISTORY;//历史订单
                    }
                    ActivityManager.getInstance().jumpToStep5Activity(context, orderType, mCurrentBean.getId(), mCurrentBean.getKhNm(), mCurrentBean.getOrderZt(), mCurrentBean.getIsMe(),mCurrentBean.getOrderNo(), redMark);
                    break;
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //筛选1:选择时间
    private void doScreeningTab1(){
        showDialogStartEndTime();
    }
    //筛选2：搜索
    private void doScreeningTab2(){
        if (mViewSearch.getVisibility() == View.VISIBLE) {
            mViewSearch.setVisibility(View.GONE);
            mEtSearch.setText("");
            switch (mChangeType) {
                case 1://订货下单
                    mSearchOrder = null;
                    break;
                case 2://退货下单
                    mSearchRetreat = null;
                    break;
            }
        } else {
            mViewSearch.setVisibility(View.VISIBLE);
        }
    }
    //筛选：搜索-请求接口
    private void doScreeningSearch(){
        switch (mChangeType) {
            case 1://订货
                queryDataOrder(1);
                break;
            case 2://退货
                queryDataRetreat(1);
                break;
        }
    }

    /**
     * 筛选时间的对话框
     */
    private void showDialogStartEndTime() {
        String startDate,endDate;
        if (2 == mChangeType){
            startDate = mStartDate2;
            endDate = mEndDate2;
        }else{
            startDate = mStartDate;
            endDate = mEndDate;
        }
        new MyDoubleDatePickerDialog(context, "筛选时间", startDate, endDate, new MyDoubleDatePickerDialog.DateTimeListener() {
            @Override
            public void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate) {
                mTvScreeningTab1.setText(startDate + "至" + endDate);
                if (2 == mChangeType){
                    mStartDate2 = startDate;
                    mEndDate2 = endDate;
                    queryDataRetreat(1);//退货
                }else{
                    mStartDate = startDate;
                    mEndDate = endDate;
                    queryDataOrder(1);//订货
                }
            }

            @Override
            public void onCancel() {
            }
        }).show();
    }

    /**
     * 作废成功
     */
    public void doCanCelOrderSuccess(){
        mCurrentBean.setOrderZt("已作废");
        mOrderAdapter.notifyItemChanged(mCurrentPosition);
    }



}
