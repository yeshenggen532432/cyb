package com.qwb.view.checkstorage.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.common.StkOrderTypeEnum;
import com.qwb.event.StkCheckEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ToastUtils;
import com.qwb.view.checkstorage.adapter.StkCheckListAdapter;
import com.qwb.view.checkstorage.model.StkCheckBean;
import com.qwb.view.checkstorage.parsent.PxStkCheckList;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 库存盘点单-列表
 */
public class XStkCheckListActivity extends XActivity<PxStkCheckList> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_stk_check_list;
    }

    @Override
    public PxStkCheckList newP() {
        return new PxStkCheckList();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 初始化EventBus
     */
    private void initEvent() {
        BusProvider.getBus().toFlowable(StkCheckEvent.class)
                .subscribe(new Consumer<StkCheckEvent>() {
                    @Override
                    public void accept(StkCheckEvent event) throws Exception {
                        if (event.getTag() == ConstantUtils.Event.TAG_STK_CHECK) {
                            pageNo = 1;
                            queryData();
                            mRefreshLayout.autoRefresh();//触发自动刷新
                        }
                    }
                });
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        initEvent();
        initUI();
        queryData();
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
     * 初始化筛选:时间和搜索
     */
    TextView mTvStartEndTime;
    TextView mTvStatus;
    int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;//年，月，日
    String mStartDate, mEndDate;//开始时间，结束时间
    Calendar calendar = Calendar.getInstance();

    private void initScreening() {
        mTvStartEndTime = findViewById(R.id.tv_start_end_time);
        mTvStatus = findViewById(R.id.tv_status);
        mTvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogStatus();
            }
        });
        mStartYear = mEndYear = calendar.get(Calendar.YEAR);//年
        mStartMonth = mEndMonth = calendar.get(Calendar.MONTH);//月
        mStartDay = mEndDay = calendar.get(Calendar.DAY_OF_MONTH);//日
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
    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.head_right2)
    View mHeadRight2;
    @BindView(R.id.head_right3)
    View mHeadRight3;
    @BindView(R.id.iv_head_right)
    ImageView mIvHeadRight;
    @BindView(R.id.iv_head_right2)
    ImageView mIvHeadRight2;
    @BindView(R.id.tv_head_right3)
    TextView mTvHeadRight3;
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
        mTvHeadTitle.setText("盘点单");
        mTvHeadRight3.setText("批次");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_17), (int) getResources().getDimension(R.dimen.dp_17));
        mIvHeadRight.setLayoutParams(params);
        mIvHeadRight.setImageResource(R.mipmap.x_add_blue);
        mIvHeadRight2.setLayoutParams(params);
        mIvHeadRight2.setImageResource(R.mipmap.ic_person_more);
        mHeadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpXStkCheckActivity(StkOrderTypeEnum.ORDER_SINGLE_ADD.getType(), 0, null);
            }
        });
        //多人盘点
        mHeadRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().jumpActivity(context, XStkCheckListTempActivity.class);
            }
        });
        //批次盘点
        mHeadRight3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpXStkCheckActivity(StkOrderTypeEnum.ORDER_BATCH_ADD.getType(), 0, 1);
            }
        });
    }

    /**
     * 跳转界面：盘点开单
     * type:1盘点开单（添加）；2盘点开单（修改）；3多人盘点开单（添加）；4多人盘点开单（修改）
     */
    private void jumpXStkCheckActivity(int type, int id, Integer isPc) {
        //批次盘点
        if (MyStringUtil.isNotEmpty(String.valueOf(isPc)) && "1".equals(String.valueOf(isPc))){
            Router.newIntent(context)
                    .to(XStkBatchCheckActivity.class)
                    .putString(ConstantUtils.Intent.TYPE, String.valueOf(type))
                    .putInt(ConstantUtils.Intent.ID, id)
                    .launch();
        }else{
            //单人盘点
            Router.newIntent(context)
                    .to(XStkCheckActivity.class)
                    .putInt(ConstantUtils.Intent.TYPE, type)
                    .putInt(ConstantUtils.Intent.ID, id)
                    .launch();
        }
    }

    /**
     * 初始化适配器
     */
    RecyclerView mRvOrder;
    StkCheckListAdapter mAdapter;
    private int pageNo = 1;
    private int pageSize = 10;
    private String status = "";

    private void initAdapter() {
        mRvOrder = findViewById(R.id.rv_receiver);
        mRvOrder.setHasFixedSize(true);
        mRvOrder.setLayoutManager(new LinearLayoutManager(this));
        //添加分割线
        mRvOrder.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_1)
                .build());
        mAdapter = new StkCheckListAdapter();
        mAdapter.openLoadAnimation();
        mRvOrder.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    StkCheckBean stkCheck = (StkCheckBean) adapter.getData().get(position);
                    if (MyStringUtil.isNotEmpty(String.valueOf( stkCheck.getIsPc())) && "1".equals(String.valueOf( stkCheck.getIsPc()))){
                        jumpXStkCheckActivity(StkOrderTypeEnum.ORDER_BATCH_UPDATE.getType(), stkCheck.getId(), stkCheck.getIsPc());
                    }else{
                        jumpXStkCheckActivity(StkOrderTypeEnum.ORDER_SINGLE_UPDATE.getType(), stkCheck.getId(), stkCheck.getIsPc());
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
     * 刷新适配器-订货下单
     */
    public void refreshAdapter(List<StkCheckBean> dataList) {
        if (pageNo == 1) {
            //上拉刷新
            mAdapter.setNewData(dataList);
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setNoMoreData(false);
        } else {
            //加载更多
            mAdapter.addData(dataList);
            mRefreshLayout.finishLoadMore();
        }
        if (!(dataList != null && dataList.size() > 0)) {
            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }
    }


    /**
     * 关闭刷新
     */
    public void closeRefresh() {
        //关闭刷新，加载更多
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }


    /**
     * 排序对话框
     */
    private String[] mStringItems = {"全部", "暂存","已审批", "已作废"};

    private void showDialogStatus() {
        final NormalListDialog dialog = new NormalListDialog(context, mStringItems);
        dialog.title("请选择单据状态").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (0 == position) {
                    status = "";
                    mTvStatus.setText("单据状态");
                } else if (1 == position) {
                    status = "-2";
                    mTvStatus.setText("暂存");
                } else if (2 == position) {
                    status = "0";
                    mTvStatus.setText("已审批");
                } else if (3 == position) {
                    status = "2";
                    mTvStatus.setText("已作废");
                }
                pageNo = 1;
                queryData();
            }
        });
    }


    /**
     * 筛选时间的对话框
     */
    private void showDialogStartEndTime() {
        new MyDoubleDatePickerDialog(context, "筛选时间", mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay,
                new MyDoubleDatePickerDialog.DateTimeListener() {
                    @Override
                    public void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate) {
                        mStartYear = year;
                        mStartMonth = month;
                        mStartDay = day;
                        mEndYear = year2;
                        mEndMonth = month2;
                        mEndDay = day2;
                        mTvStartEndTime.setText(startDate + "至" + endDate);
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
     * 查询
     */
    private void queryData() {
        getP().queryData(context, pageNo, pageSize, mStartDate, mEndDate, status);
    }


}
