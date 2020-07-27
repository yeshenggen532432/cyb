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
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.common.StkOrderTypeEnum;
import com.qwb.event.StkCheckEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.ToastUtils;
import com.qwb.view.checkstorage.adapter.StkCheckListAdapter;
import com.qwb.view.checkstorage.model.StkCheckBean;
import com.qwb.view.checkstorage.parsent.PxStkCheckListTemp;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 创建描述：暂时库存盘点单列表（多人盘点）
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class XStkCheckListTempActivity extends XActivity<PxStkCheckListTemp> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_stk_check_list;
    }

    @Override
    public PxStkCheckListTemp newP() {
        return new PxStkCheckListTemp();
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
    @BindView(R.id.iv_head_right)
    ImageView mIvHeadRight;
    @BindView(R.id.tv_head_right2)
    TextView mTvHeadRight2;
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
        mTvHeadTitle.setText("多人盘点单");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_17), (int) getResources().getDimension(R.dimen.dp_17));
        mIvHeadRight.setLayoutParams(params);
        mIvHeadRight.setImageResource(R.mipmap.x_add_blue);
        mTvHeadRight2.setText("合并");
        mHeadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpXStkCheckActivity(StkOrderTypeEnum.ORDER_MULTIPLE_ADD.getType(), 0);
            }
        });
        mHeadRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddData();
            }
        });
    }

    /**
     * 跳转界面：盘点开单
     * type:1盘点开单（添加）；2盘点开单（修改）；3多人盘点开单（添加）；4多人盘点开单（修改）
     */
    private void jumpXStkCheckActivity(int type, int id) {
        Router.newIntent(context)
                .to(XStkCheckActivity.class)
                .putInt(ConstantUtils.Intent.TYPE, type)
                .putInt(ConstantUtils.Intent.ID, id)
                .launch();
    }

    /**
     * 初始化适配器
     */
    RecyclerView mRvOrder;
    StkCheckListAdapter mAdapter;
    private int pageNo = 1;
    private int pageSize = 10;
    private String status;
    private List<StkCheckBean> selectList = new ArrayList<>();

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
        mAdapter.setType(2);
        mRvOrder.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    StkCheckBean stkCheck = (StkCheckBean) adapter.getData().get(position);
                    jumpXStkCheckActivity(StkOrderTypeEnum.ORDER_MULTIPLE_UPDATE.getType(), stkCheck.getId());
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    StkCheckBean bean = (StkCheckBean) adapter.getData().get(position);
                    switch (view.getId()) {
                        case R.id.item_iv_cb:
                            doCb(bean);
                            break;
                    }
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }

    /**
     * 处理cb
     */
    private boolean doCb(StkCheckBean bean) {
        boolean flag = false;
        for (StkCheckBean item : selectList) {
            if (("" + bean.getId()).equals("" + item.getId())) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            if (!selectList.isEmpty()) {
                StkCheckBean item = selectList.get(0);
                if (!String.valueOf(bean.getStkId()).equals(String.valueOf(item.getStkId()))) {
                    ToastUtils.showCustomToast("当前所选与已选的仓库不一样，请重新选择");
                    return false;
                }
            }
            selectList.add(bean);
        } else {
            selectList.remove(bean);
        }
        mAdapter.setSelectList(selectList);
        mAdapter.notifyDataSetChanged();
        return true;
    }

    /**
     * 初始化刷新控件
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
        if (!(dataList != null && dataList.size() > 0)) {
            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
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
    private String[] mStringItems = {"全部", "暂存", "已合并","已审批","已作废"};
    private void showDialogStatus() {
        final NormalListDialog dialog = new NormalListDialog(context, mStringItems);
        dialog.title("请选择单据状态") .show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(0 == position){
                    status = "";
                   mTvStatus.setText("单据状态");
                }else if(1 == position){
                    status = "-2";
                    mTvStatus.setText("暂存");
                }else if(2 == position){
                    status = "0";
                    mTvStatus.setText("已合并");
                }else if(3 == position){
                    status = "1";
                    mTvStatus.setText("已审批");
                }else if(4 == position){
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
    private void queryData(){
        getP().queryData(context, pageNo, pageSize, mStartDate, mEndDate, status);
    }

    public void showDialogAddData(){
        try {
            if (selectList.isEmpty()) {
                ToastUtils.showCustomToast("请选择要合并的单据");
                return;
            }
            NormalDialog dialog = new NormalDialog(context);
            dialog.content("你确定要合并吗").show();
            dialog.setOnBtnClickL(null, new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    StkCheckBean bean0 = selectList.get(0);
                    int stkId = bean0.getStkId();
                    String billIds = "" + bean0.getId();
                    for (int i = 1; i < selectList.size(); i++) {
                        StkCheckBean bean = selectList.get(i);
                        billIds += "," + bean.getId();
                    }
                    getP().addData(context, billIds, stkId);
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 合并单据成功
     */
    public void addDataSuccess() {
        ActivityManager.getInstance().closeActivity(context);
    }

    /**
     * 合并单据失败
     */
    public void addDataError(String msg) {
        NormalDialog dialog = new NormalDialog(context);
        dialog.content(msg).show();
    }


}
