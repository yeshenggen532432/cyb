package com.qwb.view.order.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qwb.view.step.ui.Step5Activity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.view.order.adapter.OrderAdapter;
import com.qwb.event.OrderEvent;
import com.qwb.event.RetreatEvent;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.order.parsent.POrder;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.qwb.view.order.model.QueryDhorderBean;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.Calendar;
import java.util.List;

import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 创建描述：订货下单模块：订货下单，退货下单
 */
public class OrderActivity extends XActivity<POrder> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_order;
    }

    @Override
    public POrder newP() {
        return new POrder();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 初始化EventBus
     */
    private void initEvent() {
        //订货-刷新
        BusProvider.getBus().toFlowable(OrderEvent.class)
                .subscribe(new Consumer<OrderEvent>() {
                    @Override
                    public void accept(OrderEvent orderEvent) throws Exception {
                        if(orderEvent.getTag()==ConstantUtils.Event.TAG_ORDER){
                            pageNo=1;
                            getP().loadDataOrder(context,mEtSearch,pageNo,mStartDate,mEndDate);
                            //触发自动刷新
                            mRefreshLayout.autoRefresh();
                        }
                    }
                });
        //退货-刷新
        BusProvider.getBus().toFlowable(RetreatEvent.class)
                .subscribe(new Consumer<RetreatEvent>() {
                    @Override
                    public void accept(RetreatEvent retreatEvent) throws Exception {
                        if(retreatEvent.getTag()==ConstantUtils.Event.TAG_RETREAT){
                            pageNoRetreat=1;
                            getP().loadDataRetreat(context,mEtSearch,pageNoRetreat,mStartDate2,mEndDate2);
                            //触发自动刷新
                            mRefreshLayoutRetreat.autoRefresh();
                        }
                    }
                });
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        initEvent();
        initUI();
        getP().loadDataOrder(context,mEtSearch,pageNo,mStartDate,mEndDate);//获取订货列表
        getP().loadDataRetreat(context,mEtSearch,pageNoRetreat,mStartDate2,mEndDate2);//获取退货列表
    }

    /**
     * 初始化UI
     */
    LinearLayout mLlOrder;
    LinearLayout mLlRetreat;
    private void initUI() {
        initHead();
        initAdapter();//订货
        initRefresh();
        initAdapterRetreat();//退货
        initRefreshRetreat();
        initBottom();//底部：下单，退货
        initScreening();//筛选时间，搜索
        mLlOrder=(LinearLayout) findViewById(R.id.ll_receive);//退货下单
        mLlRetreat=(LinearLayout) findViewById(R.id.ll_send);//退货下单
    }

    /**
     * 初始化筛选:时间和搜索
     */
    LinearLayout mRlSearch;
    EditText mEtSearch;
    TextView mIvSearch;
    TextView mTvStartEndTime;
    int mStartYear,mStartMonth,mStartDay,mEndYear,mEndMonth,mEndDay;//年，月，日
    String mStartDate,mEndDate,mStartDate2,mEndDate2;//开始时间，结束时间
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
        mTvHeadTitle.setText("订货下单");
        ImageView mIvHeadRight = (ImageView) findViewById(R.id.iv_head_right);
        mIvHeadRight.setImageResource(R.mipmap.ic_white_change);
        mIvHeadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChange();//“订货”和“退货”切换
            }
        });
    }

    /**
     * 底部：下单，退货
     */
    private void initBottom() {
        //订货下单
        findViewById(R.id.tv_bottom_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean query = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.DHXD_QUERY_NEW);
                boolean order = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.DHXD_ORDER_NEW);
                if(query && !order){
                    ToastUtils.showCustomToast("您没有下单权限");
                    return;
                }
                jumpSetp5(2,"下单"); // 1：拜访客户下单 2:单独下单(电话下单) 3：订货下单模块（列表）4：退货;5：退货下单（列表）
            }
        });
        //退货下单
        findViewById(R.id.tv_bottom_retreat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpSetp5(4,"退货"); // 1：拜访客户下单 2:单独下单(电话下单) 3：订货下单模块（列表）4：退货;5：退货下单（列表）
            }
        });
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
                getP().loadDataOrder(context,mEtSearch,pageNo,mStartDate,mEndDate);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo++;
                getP().loadDataOrder(context,mEtSearch,pageNo,mStartDate,mEndDate);
            }
        });
    }

    /**
     * 初始化适配器（退货）
     */
    RecyclerView mRvRetreat;
    OrderAdapter mRetreatAdapter;
    private int pageNoRetreat = 1;
    private void initAdapterRetreat() {
        mRvRetreat = (RecyclerView) findViewById(R.id.rv_send);
        mRvRetreat.setHasFixedSize(true);
        mRvRetreat.setLayoutManager(new LinearLayoutManager(this));
        //添加分割线
        mRvRetreat.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_5)
                .build());
        mRetreatAdapter = new OrderAdapter();
        mRetreatAdapter.openLoadAnimation();
        mRvRetreat.setAdapter(mRetreatAdapter);
        mRetreatAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                onItemClickOrderRetreat(baseQuickAdapter, i);//适配器（退货）-item点击
            }
        });
    }

    /**
     * 初始化刷新控件（退货）
     */
    RefreshLayout mRefreshLayoutRetreat;
    private void initRefreshRetreat(){
        mRefreshLayoutRetreat = (RefreshLayout) findViewById(R.id.refreshLayout_send);
        mRefreshLayoutRetreat.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNoRetreat=1;
                getP().loadDataRetreat(context,mEtSearch,pageNoRetreat,mStartDate2,mEndDate2);
            }
        });
        mRefreshLayoutRetreat.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNoRetreat++;
                getP().loadDataRetreat(context,mEtSearch,pageNoRetreat,mStartDate2,mEndDate2);
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
     * 刷新适配器-订货下单
     */
    public void refreshAdapterRetreat(List<QueryDhorderBean.Rows> dataList){
        if(!(dataList!=null && dataList.size()>0)){
            mRefreshLayoutRetreat.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }
        if(pageNoRetreat==1){
            //上拉刷新
            mRetreatAdapter.setNewData(dataList);
            mRefreshLayoutRetreat.finishRefresh();
            mRefreshLayoutRetreat.setNoMoreData(false);
        }else{
            //加载更多
            mRetreatAdapter.addData(dataList);
            mRefreshLayoutRetreat.finishLoadMore();
        }
    }

    /**
     * 关闭刷新
     */
    public void closeRefresh(){
        //关闭刷新，加载更多
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        mRefreshLayoutRetreat.finishRefresh();
        mRefreshLayoutRetreat.finishLoadMore();
    }


    /**
     * 搜索：显示搜索和隐藏搜索 - 关闭的时候：清空搜索
     */
    private void onClickTvSearch() {
        if(mRlSearch.getVisibility()== View.VISIBLE){
            mRlSearch.setVisibility(View.GONE);
            mEtSearch.setText("");
            switch (mChangeType){
                case 1://订货下单
                    mSearchOrder=null;
                    break;
                case 2://退货下单
                    mSearchRetreat=null;
                    break;
            }
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
        switch (mChangeType){
            case 1://订货
                pageNo=1;
                getP().loadDataOrder(context,mEtSearch,pageNo,mStartDate,mEndDate);
                break;
            case 2://退货
                pageNoRetreat=1;
                getP().loadDataRetreat(context,mEtSearch,pageNoRetreat,mStartDate2,mEndDate2);
                break;
        }
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
                        switch (mChangeType){
                            case 1://订货
                                mStartDate=startDate;
                                mEndDate=endDate;
                                pageNo=1;
                                getP().loadDataOrder(context,mEtSearch,pageNo,mStartDate,mEndDate);
                                break;
                            case 2://退货
                                mStartDate2=startDate;
                                mEndDate2=endDate;
                                pageNoRetreat=1;
                                getP().loadDataRetreat(context,mEtSearch,pageNoRetreat,mStartDate2,mEndDate2);
                                break;
                        }
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }

    /**
     * “订货”和“退货”切换
     */
    private void onClickChange() {
        String searchStr=mEtSearch.getText().toString().trim();
        if(mLlOrder.getVisibility()== View.VISIBLE){
            mLlOrder.setVisibility(View.GONE);
            mLlRetreat.setVisibility(View.VISIBLE);
            mTvHeadTitle.setText("退货下单");
            mChangeType=2;
            if(TextUtils.isEmpty(mStartDate2)){
                mTvStartEndTime.setText("筛选时间");
            }else{
                mTvStartEndTime.setText(mStartDate2+"至"+mEndDate2);
            }
            //搜索框的值先赋值给“订货下单”的搜索；然后“退货下单”的搜索赋值给搜索框
            mSearchOrder=searchStr;
            mEtSearch.setText(mSearchRetreat);
            if(!TextUtils.isEmpty(mSearchRetreat)){
                mRlSearch.setVisibility(View.VISIBLE);
            }else{
                mRlSearch.setVisibility(View.GONE);
            }
        }else{
            mLlOrder.setVisibility(View.VISIBLE);
            mLlRetreat.setVisibility(View.GONE);
            mTvHeadTitle.setText("订货下单");
            mChangeType=1;
            if(TextUtils.isEmpty(mStartDate)){
                mTvStartEndTime.setText("筛选时间");
            }else{
                mTvStartEndTime.setText(mStartDate+"至"+mEndDate);
            }
            //搜索框的值先赋值给“退货下单”的搜索；然后“订货下单”的搜索赋值给搜索框
            mSearchRetreat=searchStr;
            mEtSearch.setText(mSearchOrder);
            if(!TextUtils.isEmpty(mSearchOrder)){
                mRlSearch.setVisibility(View.VISIBLE);
            }else{
                mRlSearch.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 适配器（订货,退货）-item点击
     */
    private void onItemClickOrderRetreat(BaseQuickAdapter baseQuickAdapter, int i) {
        List<QueryDhorderBean.Rows> dataList=baseQuickAdapter.getData();
        QueryDhorderBean.Rows data=dataList.get(i);

        //根据公司类型：快消，卖场
        Class class1 = Step5Activity.class;
        String tpNmStr = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_TYPE);
        int orderType=0;
        switch (mChangeType){
            case 1://订货列表
                orderType=3;
                break;
            case 2://退货列表
                orderType=5;
                break;
        }
        Router.newIntent(context)
                .putInt(ConstantUtils.Intent.ORDER_ID, data.getId())// 订单id
                .putString(ConstantUtils.Intent.CLIENT_NAME, data.getKhNm())//客户名称
                .putString(ConstantUtils.Intent.ORDER_STATE, data.getOrderZt())// 订单状态：审核，未审核（可修改）
                .putInt(ConstantUtils.Intent.IS_ME, data.getIsMe())// 是否自己：1:我的，2：别人
                .putInt(ConstantUtils.Intent.ORDER_TYPE, orderType)// 1：拜访客户下单 2:单独下单(电话下单) 3：订货下单模块（列表）4：退货;5：退货下单（列表）
                .putString(ConstantUtils.Intent.ORDER_NO,data.getOrderNo())
                .to(class1)
                .launch();
    }

    /**
     * "订货下单"和"退货下单"按钮根据不同的type跳转(都跳转拜访步骤5复用)
     */
    private void jumpSetp5(int type,String titleName) {
        // type：1：拜访步骤下单 2:单独下单(电话下单) 3：订货列表 4：退货 5：退货列表
        //根据公司类型：快消，卖场
        Class class1= Step5Activity.class;
        String tpNmStr = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_TYPE);
        Router.newIntent(context)
                .putInt(ConstantUtils.Intent.ORDER_TYPE, type)// 1：拜访步骤下单 2:单独下单(电话下单) 3：订货列表 4：退货 5：退货列表
                .putString(ConstantUtils.Intent.CLIENT_NAME, "")//客户名称
                .putString(ConstantUtils.Intent.CLIENT_ID, "")// 客户id
                .putString(ConstantUtils.Intent.TITLE, titleName)// 标题
                .to(class1)
                .launch();
    }








}
