package com.qwb.view.storehouse.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.common.TypeEnum;
import com.qwb.view.storehouse.adapter.StorehouseInListAdapter;
import com.qwb.view.storehouse.model.StorehouseInBean;
import com.qwb.view.storehouse.parsent.PStorehouseInList;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 库位：入仓单列表
 */
public class StorehouseInListActivity extends XActivity<PStorehouseInList> {


    public StorehouseInListActivity() {
    }

    @Override
    public int getLayoutId() {
        //复用：入仓单列表
        return R.layout.x_activity_storehouse_in_list;
    }

    @Override
    public PStorehouseInList newP() {
        return new PStorehouseInList();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        queryData();
    }

    private String mStatus;
    private void queryData(){
        getP().queryData(context, mSearchStr, pageNo, mStartDate, mEndDate, mStatus);
    }

    private void initUI() {
        initHead();
        initScreening();
        initAdapter();
    }

    @BindView(R.id.head_left)
    View mViewLeft;
    @BindView(R.id.head_right)
    View mViewRight;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mTvHeadTitle.setText("入仓单列表");
        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
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
    private String mStartDate, mEndDate = MyTimeUtils.getTodayStr();
    private void initScreening() {
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
        showDialogStartEndTime(context);
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
    private void showDialogStartEndTime(Context context) {
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
     * 出库类型
     */
    private void showDialogStatus() {
        final ArrayList<DialogMenuItem> items = new ArrayList<>();
        items.add(new DialogMenuItem("全部", 0));
        items.add(new DialogMenuItem("暂存", -2));
        items.add(new DialogMenuItem("已审批", 1));
        items.add(new DialogMenuItem("已作废", 2));
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.title("单据状态").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(0 == position){
                    mStatus = "";
                    mTvScreeningTab2.setText("单据状态");
                }else{
                    DialogMenuItem item = items.get(position);
                    mStatus = "" + item.mResId;
                    mTvScreeningTab2.setText(item.mOperName);
                }
                queryData();
            }
        });

    }

    /**
     * 初始化适配器
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    StorehouseInListAdapter mAdapter;
    private int pageNo = 1;
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    private StorehouseInBean mCurrentItem;
    private int mCurrentPosition;
    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_5)
                .build());
        mAdapter = new StorehouseInListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    StorehouseInBean item = (StorehouseInBean)adapter.getData().get(position);
                    mCurrentItem = item;
                    mCurrentPosition = position;
                    switch (view.getId()){
                        case R.id.content:
                            ActivityManager.getInstance().jumpToStorehouseInActivity(context, item.getId(), TypeEnum.DETAIL);
                            break;
                        case R.id.right_audit:
                            getP().updateStatus(context, item.getId());
                            break;
                        case R.id.right_cancel:
                            break;
                    }
                }catch (Exception e){
                    ToastUtils.showError(e);
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
     * 审批成功
     */
    public void auditSuccess(){
        mCurrentItem.setStatus(1);
        mAdapter.setData(mCurrentPosition, mCurrentItem);
        mAdapter.notifyItemChanged(mCurrentPosition);
    }





}
