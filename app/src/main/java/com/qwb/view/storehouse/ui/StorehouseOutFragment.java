package com.qwb.view.storehouse.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.common.TypeEnum;
import com.qwb.view.storehouse.adapter.StorehouseStayOutAdapter;
import com.qwb.view.storehouse.model.StorehouseStayOutBean;
import com.qwb.view.storehouse.parsent.PStorehouseOutFragment;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.ToastUtils;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XFragment;

/**
 * 库位：待出仓发票
 */
public class StorehouseOutFragment extends XFragment<PStorehouseOutFragment> {


    public StorehouseOutFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.x_fragment_storehouse_out;
    }

    @Override
    public PStorehouseOutFragment newP() {
        return new PStorehouseOutFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        queryData();
    }

    public void queryData(){
        getP().queryData(context, mSearchStr, pageNo, mStartDate, mEndDate, mOutType);
    }

    private void initUI() {
        initScreening();
        initAdapter();
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
    private String mSearchStr, mOutType;
    private String mStartDate, mEndDate = MyTimeUtils.getTodayStr();
    private void initScreening() {
//        mTvScreeningTab1.setText(mStartDate + "至" + mEndDate);
        mTvScreeningTab2.setText("出库类型");
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

    private void doScreeningTab1(){
       showDialogStartEndTime();
    }
    private void doScreeningTab2(){
        showDialogOutType();
    }

    private void doScreeningTab3(){
        if (mViewSearch.getVisibility() == View.VISIBLE) {
            mViewSearch.setVisibility(View.GONE);
            mEtSearch.setText("");
            mSearchStr = "";
        } else {
            mViewSearch.setVisibility(View.VISIBLE);
        }
    }
    private void doScreeningSearch(){
        pageNo = 1;
        mSearchStr = mEtSearch.getText().toString().trim();
        queryData();
    }

    /**
     * 初始化适配器
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    StorehouseStayOutAdapter mAdapter;
    private int pageNo = 1;
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_5)
                .build());
        mAdapter = new StorehouseStayOutAdapter();
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                try {
                    StorehouseStayOutBean item = (StorehouseStayOutBean) baseQuickAdapter.getData().get(i);
                    ActivityManager.getInstance().jumpToStorehouseOutActivity(context, item.getId(), TypeEnum.UPDATE);
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
    public void refreshAdapter(List<StorehouseStayOutBean> dataList) {
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
     * 出库类型
     */
    private void showDialogOutType() {
        final String[] items = {"全部", "销售出库", "其它出库", "报损出库", "领用出库", "借出出库"};
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.title("出库类型").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(0 == position){
                    mOutType = "";
                   mTvScreeningTab2.setText("出库类型");
                }else{
                    mOutType = items[position];
                    mTvScreeningTab2.setText(items[position]);
                }
                pageNo = 1;
                queryData();
            }
        });

    }


}
