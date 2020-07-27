package com.qwb.view.car.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.common.TypeEnum;
import com.qwb.event.StkMoveEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDividerUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.car.adapter.CarStkMoveAdapter;
import com.qwb.view.car.model.StkMoveBean;
import com.qwb.view.car.parsent.PCarStkMoveList;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.chiyong.t3.R;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.functions.Consumer;

/**
 * 创建描述：移库列表：1.车销配货单列表，2.车销回库单列表
 */
public class CarStkMoveListActivity extends XActivity<PCarStkMoveList> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_car_stk_move_list;
    }

    @Override
    public PCarStkMoveList newP() {
        return new PCarStkMoveList();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void bindEvent() {
        BusProvider.getBus().toFlowable(StkMoveEvent.class)
                .subscribe(new Consumer<StkMoveEvent>() {
                    @Override
                    public void accept(StkMoveEvent event) throws Exception {
                        if (event.getTag() == ConstantUtils.Event.TAG_STK_MOVE) {
                            pageNo = 1;
                            mRefreshLayout.autoRefresh();
                        }
                    }
                });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        doIntent();
        queryData(true);
    }

    private String mStatus;
    public void queryData(boolean isRefresh){
        if (isRefresh){
            pageNo = 1;
        }else{
            pageNo++;
        }
        getP().page(context, pageNo, pageSize, mStartDate, mEndDate, mBillType, mStatus);//获取订货列表
    }

    public int mBillType;	//业务单据类型 0:正常移库 1:车销配货 2:车销回库
    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null){
            mBillType = intent.getIntExtra(ConstantUtils.Intent.TYPE, 1);
        }
    }

    public void doIntent(){
        if (1 == mBillType){
            mTvHeadTitle.setText("车销配货单列表");
        }else if (2 == mBillType){
            mTvHeadTitle.setText("车销回库单列表");
        }
    }
    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initScreening();
        initAdapter();
        initRefresh();
    }

    /**
     * 初始化UI-头部
     */
    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle.setText("车销配货单列表");
    }


    /**
     * 初始化筛选:时间和搜索
     */
    LinearLayout mRlSearch;
    EditText mEtSearch;
    TextView mIvSearch;
    TextView mTvStartEndTime;
    TextView mTvStatus;
    String mStartDate, mEndDate = MyTimeUtils.getToday_nyr();//开始时间，结束时间
    private void initScreening() {
        mRlSearch = findViewById(R.id.rl_search);
        mEtSearch = findViewById(R.id.et_search);
        mIvSearch = findViewById(R.id.iv_search);
        mTvStartEndTime = findViewById(R.id.tv_start_end_time);
        mTvStatus = findViewById(R.id.tv_status);
        mTvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogStatus();
            }
        });
        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickIvSearch();//搜索按钮：请求数据
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
     * 初始化适配器（订货）
     */
    RecyclerView mRvOrder;
    CarStkMoveAdapter mAdapter;
    private int pageNo = 1;
    private int pageSize = 10;
    private StkMoveBean mCurrentItem;
    private int mCurrentPosition;
    private void initAdapter() {
        mRvOrder = findViewById(R.id.rv_receiver);
        mRvOrder.setLayoutManager(new LinearLayoutManager(this));
        mRvOrder.addItemDecoration(MyDividerUtil.getH5CGray(context));
        mAdapter = new CarStkMoveAdapter();
        mRvOrder.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    mCurrentItem = (StkMoveBean) adapter.getData().get(position);
                    mCurrentPosition = position;
                    switch (view.getId()){
                        case R.id.content:
                            if (mBillType == 1){
                                ActivityManager.getInstance().jumpToCarStkOutOrderActivity(context, mCurrentItem.getId(), TypeEnum.UPDATE);
                            }else if (mBillType == 2){
                                ActivityManager.getInstance().jumpToCarStkInOrderActivity(context, mCurrentItem.getId(), TypeEnum.UPDATE);
                            }
                            break;
                        case R.id.tv_audit:
                            getP().audit(context, mCurrentItem.getId());
                            break;
                        case R.id.tv_cancel:
                            getP().cancel(context, mCurrentItem.getId());
                            break;
                    }

                } catch (Exception e) {
                    ToastUtils.showError(e);
                }

            }
        });
    }

    /**
     * 初始化刷新控件（订货）
     */
    RefreshLayout mRefreshLayout;
    private void initRefresh() {
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                queryData(true);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                queryData(false);
            }
        });
    }

    /**
     * 刷新适配器-订货下单
     */
    public void refreshAdapter(List<StkMoveBean> dataList) {
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

    /**
     * 关闭刷新
     */
    public void closeRefresh() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }


    /**
     * 搜索按钮：请求数据
     */
    private void onClickIvSearch() {
        queryData(true);
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
                        queryData(true);
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }

    /**
     * 状态
     */
    private void showDialogStatus() {
        String[] items = new String[]{"全部","暂存","已审批","已作废"};
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.title("选择状态").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (0 == position){
                    mStatus = "";
                    mTvStatus.setText("状态");
                }else if (1 == position){
                    mStatus = "-2";
                    mTvStatus.setText("暂存");
                } else if (2 == position){
                    mStatus = "1";
                    mTvStatus.setText("已审批");
                }else if (3 == position){
                    mStatus = "2";
                    mTvStatus.setText("已作废");
                }
                queryData(true);
            }
        });
    }

    //审批成功
    public void doAuditSuccess(){
        ToastUtils.showCustomToast("审批成功");
        mCurrentItem.setStatus(1);
        mAdapter.notifyItemChanged(mCurrentPosition);
    }

    //作废成功
    public void doCancelSuccess(){
        ToastUtils.showCustomToast("作废成功");
        mCurrentItem.setStatus(2);
        mAdapter.notifyItemChanged(mCurrentPosition);
    }


}
