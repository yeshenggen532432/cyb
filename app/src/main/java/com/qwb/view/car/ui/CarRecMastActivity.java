package com.qwb.view.car.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.car.adapter.CarRecMastAdapter;
import com.qwb.view.car.model.CarRecMastBean;
import com.qwb.view.car.parsent.PCarRecMast;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 创建描述：车销收款
 */
public class CarRecMastActivity extends XActivity<PCarRecMast> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_car_rec_mast;
    }

    @Override
    public PCarRecMast newP() {
        return new PCarRecMast();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 初始化EventBus
     */
    private void initEvent() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initEvent();
        initUI();
        getP().queryData(context, pageNo, pageSize, mStartDate, mEndDate, status);
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initScreening();//筛选时间，搜索
        initAdapter();
        initRefresh();
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
    @BindView(R.id.iv_head_right3)
    ImageView mIvHeadRight3;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle.setText("收款流水");
    }

    /**
     * 初始化筛选:时间和搜索
     */
    TextView mTvStatus;//已收款，作废，未收款
    TextView mTvStartEndTime;
    String mStartDate, mEndDate;//开始时间，结束时间
    int status = 1;//默认已收款
    private void initScreening() {
        mTvStatus = findViewById(R.id.tv_status);
        mTvStartEndTime = findViewById(R.id.tv_start_end_time);
        mStartDate = mEndDate = MyTimeUtils.getTodayStr();
        mTvStartEndTime.setText(mStartDate + "至" + mEndDate);
        mTvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogStatus();
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
     * 初始化适配器
     */
    RecyclerView mRvOrder;
    CarRecMastAdapter mAdapter;
    private int pageNo = 1;
    private int pageSize = 10;
    private int mCurrentPosition;
    private CarRecMastBean mCurrentBean;
    private int mStatus;//0:全部 -2:暂存 1:已审批 2:作废 3:已入账
    private void initAdapter() {
        mRvOrder = findViewById(R.id.rv_receiver);
        mRvOrder.setHasFixedSize(true);
        mRvOrder.setLayoutManager(new LinearLayoutManager(this));
        //添加分割线
//        mRvOrder.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
//                .colorResId(R.color.gray_e)
//                .sizeResId(R.dimen.dp_5)
//                .build());
        mAdapter = new CarRecMastAdapter();
        mRvOrder.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                try {
                } catch (Exception e) {
                }
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    CarRecMastBean bean = (CarRecMastBean) adapter.getData().get(position);
                    mCurrentPosition = position;
                    mCurrentBean = bean;
                    switch (view.getId()){
                           //作废
                        case R.id.item_sb_zf:
                            mStatus = 2;
                            getP().updateStatusZf(context, bean.getId());
                            break;
                            //确认收款
                        case R.id.item_sb_qrsk:
                            mStatus = 1;
                            getP().updateStatusQrsk(context, bean.getId());
                            break;
                    }
                } catch (Exception e) {
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
                getP().queryData(context, pageNo, pageSize, mStartDate, mEndDate, status);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo++;
                getP().queryData(context, pageNo, pageSize, mStartDate, mEndDate, status);
            }
        });
    }

    /**
     * 刷新适配器
     */
    public void refreshAdapter(List<CarRecMastBean> dataList, double sumAmt) {
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
        }
        if (dataList.size() < pageSize) {
            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }

        mTvHeadTitle.setText("收款流水(" + sumAmt + ")");
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
     * 筛选时间的对话框
     */
    int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;//年，月，日
    Calendar calendar = Calendar.getInstance();
    private void showDialogStartEndTime() {
        if (0 == mStartYear) {
            mStartYear = mEndYear = calendar.get(Calendar.YEAR);//年
            mStartMonth = mEndMonth = calendar.get(Calendar.MONTH);//月
            mStartDay = mEndDay = calendar.get(Calendar.DAY_OF_MONTH);//日
        }
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
                        getP().queryData(context, pageNo, pageSize, mStartDate, mEndDate, status);
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }

    public void showDialogStatus(){
        final ArrayList<DialogMenuItem> baseItems = new ArrayList<>();
        baseItems.add(new DialogMenuItem("全部", 0));
        baseItems.add(new DialogMenuItem("未收款", -2));
        baseItems.add(new DialogMenuItem("已收款", 1));
        baseItems.add(new DialogMenuItem("作废单", 2));
        baseItems.add(new DialogMenuItem("已入账", 3));
        NormalListDialog dialog = new NormalListDialog(context, baseItems);
        dialog.show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogMenuItem item = baseItems.get(position);
                mTvStatus.setText(item.mOperName);
                pageNo = 1;
                status = item.mResId;
                getP().queryData(context, pageNo, pageSize, mStartDate, mEndDate, status);
            }
        });
    }

    /**
     * 作废或确认收款成功
     */
    public void updateStatusSuccess(){
        mCurrentBean.setStatus(mStatus);
        mAdapter.getData().set(mCurrentPosition, mCurrentBean);
        mAdapter.notifyDataSetChanged();
    }


}
