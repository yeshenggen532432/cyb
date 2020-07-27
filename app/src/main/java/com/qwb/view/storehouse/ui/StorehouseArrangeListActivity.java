package com.qwb.view.storehouse.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.view.storehouse.adapter.StorehouseArrangeListAdapter;
import com.qwb.view.storehouse.model.StorehouseInBean;
import com.qwb.view.storehouse.parsent.PStorehouseArrangeList;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.event.OrderEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
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
 * 库位整理列表
 */
public class StorehouseArrangeListActivity extends XActivity<PStorehouseArrangeList> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_storehouse_arrange_list;
    }

    @Override
    public PStorehouseArrangeList newP() {
        return new PStorehouseArrangeList();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void bindEvent() {
        BusProvider.getBus().toFlowable(OrderEvent.class)
                .subscribe(new Consumer<OrderEvent>() {
                    @Override
                    public void accept(OrderEvent orderEvent) throws Exception {
                        if (orderEvent.getTag() == ConstantUtils.Event.TAG_ORDER) {
                            pageNo = 1;
                            //触发自动刷新
                            mRefreshLayout.autoRefresh();
                        }
                    }
                });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        queryData();
    }

    public void queryData(){
        getP().queryData(context,pageNo, pageSize, mStartDate, mEndDate, mStatus, mSearchStr);
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initScreening();
        initAdapter();
    }

    /**
     * 头部
     */
    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.iv_head_right)
    ImageView mIvHeadRight;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mTvHeadTitle.setText("库位整理");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_20), (int) getResources().getDimension(R.dimen.dp_20));
        mIvHeadRight.setLayoutParams(params);
        mIvHeadRight.setImageResource(R.mipmap.ic_add_gray_round);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mHeadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().jumpToStorehouseArrangeActivity(context, 0);
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
    @BindView(R.id.view_screening_tab3)
    View mViewScreeningTab3;
    @BindView(R.id.tv_screening_tab1)
    TextView mTvScreeningTab1;
    @BindView(R.id.tv_screening_tab2)
    TextView mTvScreeningTab2;
    @BindView(R.id.tv_screening_tab3)
    TextView mTvScreeningTab3;
    private String mSearchStr;
    private Integer mStatus;
    private String mStartDate = MyTimeUtils.getFirstOfMonth(), mEndDate = MyTimeUtils.getTodayStr();
    private void initScreening() {
        mTvScreeningTab1.setText(mStartDate + "至" + mEndDate);
        mTvScreeningTab2.setText("单据状态");
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
        showDialogStartEndTime();
    }
    //筛选2
    private void doScreeningTab2(){
        showDialogStatus();
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
        pageNo = 1;
        mSearchStr = mEtSearch.getText().toString().trim();
        queryData();
    }

    /**
     * 筛选时间的对话框
     */
    private void showDialogStartEndTime() {
        new MyDoubleDatePickerDialog(context, "筛选时间", mStartDate, mEndDate,
                new MyDoubleDatePickerDialog.DateTimeListener() {
                    @Override
                    public void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate) {
                        mTvScreeningTab1.setText(startDate + "至" + endDate);
                        mStartDate = startDate;
                        mEndDate = endDate;
                        pageNo = 1;
                        queryData();
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }

    /**
     * 选择单据状态
     */
    public void showDialogStatus(){
        String[] items = {"全部", "暂存", "已审批", "已作废"};
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.title("选择单据状态").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        mStatus = null;
                        mTvScreeningTab2.setText("单据状态");
                        break;
                    case 1:
                        mStatus = -2;
                        mTvScreeningTab2.setText("暂存");
                        break;
                    case 2:
                        mStatus = 1;
                        mTvScreeningTab2.setText("已审批");
                        break;
                    case 3:
                        mStatus = 2;
                        mTvScreeningTab2.setText("已作废");
                        break;
                }
                pageNo = 1;
                queryData();
            }
        });
    }

    /**
     * 初始化适配器
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    StorehouseArrangeListAdapter mAdapter;
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
                .sizeResId(R.dimen.dp_5)
                .build());
        mAdapter = new StorehouseArrangeListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                try {
                    mCurrentItem = (StorehouseInBean) baseQuickAdapter.getData().get(i);
                    mPosition = i;
                    ActivityManager.getInstance().jumpToStorehouseArrangeActivity(context, mCurrentItem.getId());
                } catch (Exception e) {
                }
            }
        });

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
     * 刷新适配器
     */
    public void refreshAdapter(List<StorehouseInBean> dataList) {
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
