package com.qwb.view.plan.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.MyTimeUtils;
import com.qwb.view.plan.parsent.PPlanUnderFragment;
import com.xmsx.cnlife.widget.KCalendar;
import com.qwb.utils.ToastUtils;
import com.qwb.view.plan.adapter.PlanUnderLeftAdapter;
import com.qwb.view.plan.adapter.PlanUnderRightAdapter;
import com.qwb.view.plan.adapter.PlanUnderlingAdapter;
import com.qwb.view.plan.model.PlanLineBean;
import com.qwb.view.plan.model.PlanUnderBean;
import com.qwb.utils.MyColorUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.member.model.MemberBean;
import com.chiyong.t3.R;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xrecyclerview.divider.HorizontalDividerItemDecoration;

/**
 */
public class PlanUnderFragment extends XFragment<PPlanUnderFragment> {


    public PlanUnderFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.x_fragment_plan_under;
    }

    @Override
    public PPlanUnderFragment newP() {
        return new PPlanUnderFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        queryDataNewUnderlingPlan();
        getP().loadDataFrame(context);
    }

    //下属
    private void queryDataNewUnderlingPlan(){
        getP().queryDataNewUnderlingPlan(context, pageNo, pageSize, mDateStr, "");
    }
    //查询下属线路
    private void queryDataPlanLine(int mid){
        getP().queryDataPlanLine(context, mid);
    }

    private String mDateStr = MyTimeUtils.getTodayStr();
    private int pageNo = 1;
    private int pageSize = 10;
    private void initUI() {
        //线路规划
        initAdapterUnderling();
        initRefreshUnderling();
        initCalendar();
        initViewHeader();

        //下属线路
        initAdapterLeft();
        initAdapterRight();

        //tab
        initTab();
    }

    @BindView(R.id.layout_tab1)
    View mLayoutTab1;
    @BindView(R.id.layout_tab2)
    View mLayoutTab2;
    @BindView(R.id.layout_tab_content1)
    View mLayoutTabContent1;
    @BindView(R.id.layout_tab_content2)
    View mLayoutTabContent2;
    @BindView(R.id.tv_tab1)
    TextView mTvTab1;
    @BindView(R.id.tv_tab2)
    TextView mTvTab2;
    private void initTab() {
        mLayoutTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvTab1.setTextColor(MyColorUtil.getColorResId(R.color.x_main_color));
                mTvTab2.setTextColor(MyColorUtil.getColorResId(R.color.gray_6));
                mLayoutTabContent1.setVisibility(View.VISIBLE);
                mLayoutTabContent2.setVisibility(View.GONE);
            }
        });
        mLayoutTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvTab1.setTextColor(MyColorUtil.getColorResId(R.color.gray_6));
                mTvTab2.setTextColor(MyColorUtil.getColorResId(R.color.x_main_color));
                mLayoutTabContent1.setVisibility(View.GONE);
                mLayoutTabContent2.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 初始化适配器
     */
    @BindView(R.id.recyclerView_underling)
    RecyclerView mRecyclerViewUnderling;
    PlanUnderlingAdapter mAdapterUnderling;
    private View headerView;
    private void initAdapterUnderling() {
        mRecyclerViewUnderling.setHasFixedSize(true);
        mRecyclerViewUnderling.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerViewUnderling.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_0_5)
                .build());
        mAdapterUnderling = new PlanUnderlingAdapter(context);
        mAdapterUnderling.setDate(mDateStr);
        mRecyclerViewUnderling.setAdapter(mAdapterUnderling);
        //头部
        headerView = getLayoutInflater().inflate(R.layout.x_header_plan, null);
        mAdapterUnderling.addHeaderView(headerView);
    }

    /**
     * 初始化刷新控件
     */
    @BindView(R.id.refreshLayout_underling)
    RefreshLayout mRefreshLayoutUnderling;
    private void initRefreshUnderling() {
        mRefreshLayoutUnderling.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNo = 1;
                queryDataNewUnderlingPlan();
            }
        });
        mRefreshLayoutUnderling.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo++;
                queryDataNewUnderlingPlan();
            }
        });
    }

    public void refreshAdapterUnderling(List<PlanUnderBean> data) {
        try {
            if (null == data) {
                return;
            }
            if (pageNo == 1) {
                //上拉刷新
                mAdapterUnderling.setPosition(-1);//默认
                mAdapterUnderling.setNewData(data);
                mRefreshLayoutUnderling.finishRefresh();
                mRefreshLayoutUnderling.setNoMoreData(false);
            } else {
                //加载更多
                mAdapterUnderling.addData(data);
                mRefreshLayoutUnderling.finishLoadMore();
            }

            if (data.size() < 10) {
                mRefreshLayoutUnderling.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            }
            mRecyclerViewUnderling.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 初始化日历
     */
    KCalendar mCalendar;
    TextView mTvYearMonth;
    private void initCalendar() {
        mTvYearMonth = headerView.findViewById(R.id.calendar_year_month);
        mCalendar = headerView.findViewById(R.id.calendar);
        if (!MyStringUtil.isEmpty(mDateStr)) {
            String[] split = mDateStr.split("-");
            int year = Integer.parseInt(split[0]);
            int month = Integer.parseInt(split[1]);
            mTvYearMonth.setText(year + "年" + month + "月");
            mCalendar.showCalendar(year, month);
            mCalendar.setCalendarDayBgColor(mDateStr, R.drawable.shape_oval_blue);
        }

        // 监听所选中的日期
        mCalendar.setOnCalendarClickListener(new KCalendar.OnCalendarClickListener() {
            public void onCalendarClick(int row, int col, String dateFormat) {
                try {
                    int month = Integer.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1, dateFormat.lastIndexOf("-")));
                    if (mCalendar.getCalendarMonth() - month == 1 || mCalendar.getCalendarMonth() - month == -11) {// 跨年跳转
                        //上月
                        mCalendar.lastMonth();
                    } else if (month - mCalendar.getCalendarMonth() == 1 || month - mCalendar.getCalendarMonth() == -11) {
                        //下月
                        mCalendar.nextMonth();
                    } else {
                        //本月
                        mCalendar.removeAllBgColor();
                        mCalendar.setCalendarDayBgColor(dateFormat, R.drawable.shape_oval_blue);
                    }

                    pageNo = 1;
                    mDateStr = dateFormat;
                    queryDataNewUnderlingPlan();
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });

        // 监听当前月份
        mCalendar.setOnCalendarDateChangedListener(new KCalendar.OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                mTvYearMonth.setText(year + "年" + month + "月");
            }
        });

        // 上月监听按钮
        headerView.findViewById(R.id.calendar_last_month)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        mCalendar.lastMonth();
                    }
                });

        // 下月监听按钮
        headerView.findViewById(R.id.calendar_next_month)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        mCalendar.nextMonth();
                    }
                });
    }

    /**
     */
    private View mLayoutHideLine;//线路名称
    private void initViewHeader() {
        mLayoutHideLine = headerView.findViewById(R.id.layout_hide_line);
        mLayoutHideLine.setVisibility(View.GONE);
    }

    //-----------------------------------------------下属线路：start-----------------------------------------------------------
    /**
     * 初始化适配器
     */
    @BindView(R.id.rv_left)
    RecyclerView mRvLeft;
    PlanUnderLeftAdapter mAdapterLeft;
    private void initAdapterLeft() {
        mRvLeft.setHasFixedSize(true);
        mRvLeft.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRvLeft.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_0_5)
                .build());
        mAdapterLeft = new PlanUnderLeftAdapter();
        mRvLeft.setAdapter(mAdapterLeft);

        mAdapterLeft.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    MemberBean item = (MemberBean) adapter.getData().get(position);
                    //改变左列表的字体颜色
                    mAdapterLeft.setPosition(position);
                    mAdapterLeft.notifyDataSetChanged();
                    queryDataPlanLine(item.getMemberId());
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });
    }

    /**
     * 刷新：下属线路（左-业务员）
     */
    public void refreshAdapterLeft(List<MemberBean> data) {
        try {
            if (null == data) {
                return;
            }
            mAdapterLeft.setNewData(data);

            //默认加载第一个
            if(data.size() > 0){
                queryDataPlanLine(data.get(0).getMemberId());
            }

        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 初始化适配器
     */
    @BindView(R.id.rv_right)
    RecyclerView mRvRight;
    PlanUnderRightAdapter mAdapterRight;
    private void initAdapterRight() {
        mRvRight.setHasFixedSize(true);
        mRvRight.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRvRight.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_0_5)
                .build());
        mAdapterRight = new PlanUnderRightAdapter(context);
        mRvRight.setAdapter(mAdapterRight);
    }

    /**
     * 刷新：下属线路（右-线路）
     */
    public void refreshAdapterRight(List<PlanLineBean> data) {
        try {
            if (null == data) {
                return;
            }
            mAdapterRight.setNewData(data);

        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


}
