package com.qwb.view.plan.ui;


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
import com.deadline.statebutton.StateButton;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.common.ToStepEnum;
import com.qwb.view.plan.model.PlanBean;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.view.plan.parsent.PPlanMineFragment;
import com.xmsx.cnlife.widget.KCalendar;
import com.qwb.view.customer.ui.ClientManagerActivity;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.plan.adapter.PlanMineAdapter;
import com.qwb.view.plan.model.PlanLineBean;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.call.ui.CallRecordActivity;
import com.qwb.view.plan.model.PlanMineResult;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.router.Router;
import cn.droidlover.xrecyclerview.divider.HorizontalDividerItemDecoration;

/**
 * 计划拜访--我的拜访
 */
public class PlanMineFragment extends XFragment<PPlanMineFragment> {


    public PlanMineFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.x_fragment_plan_mine;
    }

    @Override
    public PPlanMineFragment newP() {
        return new PPlanMineFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        queryDataNewPlan();
    }

    //我的
    private int pageNo = 1;
    private int pageSize = 10;

    private void queryDataNewPlan() {
        getP().queryDataNewPlan(context, pageNo, pageSize, mDateStr, mid);
    }

    private String mDateStr = MyTimeUtils.getTodayStr();
    private String mid = SPUtils.getID();//我的拜访（默认自己）
    @BindView(R.id.sb_temp)
    StateButton mSbTemp;

    private void initUI() {
        initAdapter();
        initRefresh();
        initCalendar();
        initViewHeader();

        //临时拜访
        mSbTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.newIntent(context)
                        .putInt(ConstantUtils.Intent.TYPE, 1)
                        .putString(ConstantUtils.Intent.PDATE, mDateStr)
                        .to(ClientManagerActivity.class)
                        .launch();
            }
        });
    }

    /**
     * 初始化适配器
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    PlanMineAdapter mAdapter;
    private View headerView;

    private void initAdapter() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_0_5)
                .build());
        mAdapter = new PlanMineAdapter();
        mRecyclerView.setAdapter(mAdapter);

        headerView = getLayoutInflater().inflate(R.layout.x_header_plan, null);
        mAdapter.addHeaderView(headerView);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    PlanBean planBean = (PlanBean) adapter.getData().get(position);
                    if (planBean != null) {
                        Integer isWc = planBean.getIsWc();//isWc 是否完成（1是；2否）
                        //昨天：1"完成"跳到-"拜访记录" 2："未完成"跳到-"拜访步骤"
                        //今天：都跳到-"拜访步骤"
                        //明天：都跳到-客户详情
                        if (MyUtils.DateBefore(mDateStr)) {
                            //昨天
                            if (1 == isWc) {
                                jumpActivityToCallonRecord(planBean);
                            } else if (2 == isWc) {
                                jumpActivityToStep(planBean);
                            }
                        }else if(mDateStr.equals(MyTimeUtils.getTodayStr())){
                            //今天
                            jumpActivityToStep(planBean);
                        }else{
                            //明天
                            jumpActivityToClient(planBean);
                        }
                    }
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    boolean query = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.JHBF_QUERY_NEW);
                    boolean call = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.JHBF_CALL_NEW);
                    if (query && !call) {
                        ToastUtils.showCustomToast("您没有拜访权限");
                        return;
                    }
                    PlanBean bean = (PlanBean) adapter.getData().get(position);
                    switch (view.getId()) {
                        case R.id.tv_state:
                            jumpActivityToStep(bean);
                            break;
                        case R.id.tv_wancheng:
                            jumpActivityToStep(bean);
                            break;
                    }
                } catch (Exception e) {
                }
            }
        });


    }

    /**
     * 初始化刷新控件
     */
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    private void initRefresh() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNo = 1;
                queryDataNewPlan();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo++;
                queryDataNewPlan();
            }
        });
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

//                    int daysBetween;
//                    if (MyUtils.DateBefore(dateFormat)) {
//                        //昨天的
//                        daysBetween = MyUtils.daysBetween(dateFormat, MyTimeUtils.getTodayStr());
//                        if (1 == daysBetween) {
//                            mTvDate.setText("昨天:");
//                        } else {
//                            mTvDate.setText(daysBetween + "天前:");
//                        }
//                    } else if(dateFormat.equals(MyTimeUtils.getTodayStr())) {
//                        //今天的
//                        mTvDate.setText("今天:");
//                    }else{
//                        //明天的
//                        daysBetween = MyUtils.daysBetween(MyTimeUtils.getTodayStr(), dateFormat);
//                        if (1 == daysBetween) {
//                            mTvDate.setText("明天:");
//                        } else {
//                            mTvDate.setText(daysBetween + "天后:");
//                        }
//                    }
                    pageNo = 1;
                    mDateStr = dateFormat;
                    queryDataNewPlan();
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
     * 计划拜访的数量
     */
    private TextView mTvDate;// 昨天，今天，明天
    private TextView mTvNum;//计划拜访数
    private TextView mTvWcNum;//完成数量
    private TextView mTvWwcNum;//未完成数量
    private LinearLayout mLayoutWc;//如果没有拜访的要隐藏
    private LinearLayout mLayoutEditPlan;//添加计划
    private ImageView mIvEditPlan;//编辑计划
    private TextView mTvLineName;//线路名称

    private void initViewHeader() {
        mTvDate = headerView.findViewById(R.id.tv_date);
        mTvNum = headerView.findViewById(R.id.tv_jhSum);
        mTvWcNum = headerView.findViewById(R.id.tv_wc);
        mTvWwcNum = headerView.findViewById(R.id.tv_wwc);
        mLayoutWc = headerView.findViewById(R.id.ll_wancheng);
        mLayoutEditPlan = headerView.findViewById(R.id.layout_edit_plan);
        mIvEditPlan = headerView.findViewById(R.id.iv_edit_plan);
        mTvLineName = headerView.findViewById(R.id.tv_line_name);
        mLayoutEditPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getP().queryDataPlanLine(context);
            }
        });
    }

    public void refreshAdapter(PlanMineResult data) {
        try {
            if (null == data) {
                return;
            }
            mAdapter.setDate(mDateStr);//设置日期
            List<PlanBean> dataList = data.getRows();
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

            if (dataList.size() < 10) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                ToastUtils.showCustomToast("没有更多数据");
            }

            Integer coun1 = data.getCoun1();
            Integer coun2 = data.getCoun2();
            if (0 == coun1 && 0 == coun2) {
                mTvNum.setText("无计划");
                mLayoutWc.setVisibility(View.INVISIBLE);
                mLayoutEditPlan.setVisibility(View.VISIBLE);
                mTvLineName.setText("");
            } else {
                mTvNum.setText("计划" + (coun1 + coun2) + "家");
                mTvWcNum.setText(String.valueOf(coun1));
                mTvWwcNum.setText(String.valueOf(coun2));
                mLayoutWc.setVisibility(View.VISIBLE);
                mTvLineName.setText(data.getXlNm());

                //明天可以修改；其他不行
//                mLayoutEditPlan.setVisibility(View.GONE);
                mLayoutEditPlan.setVisibility(View.VISIBLE);
            }

            int daysBetween;
            if (MyUtils.DateBefore(mDateStr)) {
                //昨天的
                daysBetween = MyUtils.daysBetween(mDateStr, MyTimeUtils.getTodayStr());
                if (1 == daysBetween) {
                    mTvDate.setText("昨天:");
                } else {
                    mTvDate.setText(daysBetween + "天前:");
                }

                if (0 == coun1 && 0 == coun2) {
                    mTvLineName.setText("");
                    mTvNum.setText("无计划");
                    mLayoutWc.setVisibility(View.INVISIBLE);
                    mLayoutEditPlan.setVisibility(View.GONE);
                } else {
                    mTvLineName.setText(data.getXlNm());
                    mTvNum.setText("计划" + (coun1 + coun2) + "家");
                    mTvWcNum.setText(String.valueOf(coun1));
                    mTvWwcNum.setText(String.valueOf(coun2));
                    mLayoutWc.setVisibility(View.VISIBLE);
                    mLayoutEditPlan.setVisibility(View.GONE);
                }

            } else if (mDateStr.equals(MyTimeUtils.getTodayStr())) {
                //今天的
                mTvDate.setText("今天:");
                if (0 == coun1 && 0 == coun2) {
                    mTvLineName.setText("");
                    mTvNum.setText("无计划");
                    mLayoutWc.setVisibility(View.INVISIBLE);
                    mLayoutEditPlan.setVisibility(View.VISIBLE);
                    mIvEditPlan.setImageResource(R.mipmap.ic_jia_gray_666);
                } else {
                    mTvLineName.setText(data.getXlNm());
                    mTvNum.setText("计划" + (coun1 + coun2) + "家");
                    mTvWcNum.setText(String.valueOf(coun1));
                    mTvWwcNum.setText(String.valueOf(coun2));
                    mLayoutWc.setVisibility(View.VISIBLE);
                    mLayoutEditPlan.setVisibility(View.GONE);
                }
            } else {
                //明天的
                daysBetween = MyUtils.daysBetween(MyTimeUtils.getTodayStr(), mDateStr);
                if (1 == daysBetween) {
                    mTvDate.setText("明天:");
                } else {
                    mTvDate.setText(daysBetween + "天后:");
                }
                if (0 == coun1 && 0 == coun2) {
                    mTvLineName.setText("");
                    mTvNum.setText("无计划");
                    mLayoutWc.setVisibility(View.INVISIBLE);
                    mLayoutEditPlan.setVisibility(View.VISIBLE);
                    mIvEditPlan.setImageResource(R.mipmap.ic_jia_gray_666);
                } else {
                    mTvLineName.setText(data.getXlNm());
                    mTvNum.setText("计划" + (coun1 + coun2) + "家");
                    mTvWcNum.setText(String.valueOf(coun1));
                    mTvWwcNum.setText(String.valueOf(coun2));
                    mLayoutWc.setVisibility(View.VISIBLE);
                    mLayoutEditPlan.setVisibility(View.VISIBLE);
                    mIvEditPlan.setImageResource(R.mipmap.ic_update_gray_666);
                }
            }

        } catch (Exception e) {
        }
    }


    /**
     * 选择线路
     */
    public void showDialogChooseLine(List<PlanLineBean> datas) {
        if (datas == null || datas.isEmpty()) {
            ToastUtils.showCustomToast("没有线路，请添加线路");
            return;
        }
        final ArrayList<DialogMenuItem> items = new ArrayList<>();
        for (PlanLineBean bean : datas) {
            DialogMenuItem item = new DialogMenuItem(bean.getXlNm(), bean.getId());
            items.add(item);
        }
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.title("选择线路(单选)").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    DialogMenuItem item = items.get(position);
                    int xlid = item.mResId;
                    getP().addDataPlan(context, String.valueOf(xlid), mDateStr);
                } catch (Exception e) {
                }

            }
        });
    }

    /**
     * 添加计划成功
     */
    public void addSuccess() {
        ToastUtils.showCustomToast("添加计划成功");
        mRefreshLayout.autoRefresh();
    }


    //客户信息
    private void jumpActivityToClient(PlanBean planBean) {
        ActivityManager.getInstance().jumpToAddClientActivity(context, 3, "" + planBean.getCid());
    }

    //拜访步骤
    private void jumpActivityToStep(PlanBean data) {
//        Router.newIntent(context)
//                .putSerializable(ConstantUtils.Intent.PLAN_CALL, planBean)
//                .putString(ConstantUtils.Intent.PDATE, mDateStr)
//                .putInt(ConstantUtils.Intent.TYPE, 3)
//                .to(StepActivity.class)
//                .launch();
        ActivityManager.getInstance().jumpToStepActivity(context, ToStepEnum.STEP_JFBF, data.getCid(), data.getKhNm(), data.getAddress(), data.getTel(), data.getMobile(),
                data.getLinkman(), null, null, 0, mDateStr);
    }

    //拜访记录
    private void jumpActivityToCallonRecord(PlanBean planBean) {
        Router.newIntent(context)
                .putSerializable(ConstantUtils.Intent.PLAN_CALL, planBean)
                .putString(ConstantUtils.Intent.DATE, mDateStr)
                .putInt(ConstantUtils.Intent.TYPE, 3)
                .to(CallRecordActivity.class)
                .launch();
    }


}
