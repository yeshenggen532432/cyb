package com.qwb.view.delivery.ui;

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
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.event.ChooseDeliveryEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.delivery.adapter.DeliveryListAdapter;
import com.qwb.view.delivery.model.DeliveryBean;
import com.qwb.view.delivery.parsent.PDeliveryList;
import com.qwb.utils.MyStringUtil;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 配送中心:配送单列表（全部）
 */
public class DeliveryListActivity extends XActivity<PDeliveryList> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_delivery_list;
    }

    @Override
    public PDeliveryList newP() {
        return new PDeliveryList();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        queryData();
    }

    private void initUI() {
        initHead();
        initAdapter();
        initRefresh();
        initScreening();
    }

    /**
     * 初始化筛选:时间和搜索
     */
    @BindView(R.id.rl_search)
    LinearLayout mRlSearch;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.iv_search)
    TextView mIvSearch;
    @BindView(R.id.tv_start_end_time)
    TextView mTvStartEndTime;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    private void initScreening() {
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRlSearch.getVisibility() == View.VISIBLE){
                    mRlSearch.setVisibility(View.GONE);
                    String search = mEtSearch.getText().toString();
                    if(MyStringUtil.isEmpty(search)){
                        mTvSearch.setText("搜索");
                    }else{
                        mTvSearch.setText(search);
                    }
                }else{
                    mRlSearch.setVisibility(View.VISIBLE);
                }
            }
        });
        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNo = 1;
                queryData();
            }
        });
        mTvStartEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogStartEndTime();
            }
        });
        mTvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogStatus();
            }
        });
    }

    private String psState = "";//默认全部
    private String psStates = "1,2,3";
    private void queryData(){
        String search = mEtSearch.getText().toString();
        getP().queryData(context,psState, psStates, pageNo, pageSize, mStartDate, mEndDate, search);
    }


    /**
     * 初始化UI-头部
     */
    TextView mTvHeadTitle;
    private void initHead() {
        OtherUtils.setStatusBarColor(context);//状态栏颜色，透明度
        findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle = findViewById(R.id.tv_head_title);
        mTvHeadTitle.setText("配送单列表");
        TextView tvHeadRight = findViewById(R.id.tv_head_right);
        tvHeadRight.setText("确定");
        tvHeadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(ConstantUtils.Intent.REQUEST_CODE_CHOOSE_DELIVERY, intent);
                ActivityManager.getInstance().closeActivity(context);
                BusProvider.getBus().post(new ChooseDeliveryEvent());
            }
        });
    }

    /**
     * 初始化适配器
     */
    RecyclerView mRecyclerView;
    DeliveryListAdapter mAdapter;
    private void initAdapter() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_1)
                .build());
        mAdapter = new DeliveryListAdapter();
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
       //item点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DeliveryBean bean = (DeliveryBean) adapter.getData().get(position);
                boolean flag = false;
                for (DeliveryBean deliveryBean : ConstantUtils.selectDeliveryList) {
                    if(String.valueOf(deliveryBean.getId()).equals(String.valueOf(bean.getId()))){
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    ConstantUtils.selectDeliveryList.add(bean);
                }else{
                    ConstantUtils.selectDeliveryList.remove(bean);
                }
                mAdapter.setSelectList(ConstantUtils.selectDeliveryList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 初始化刷新控件
     */
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    private int pageNo = 1;
    private int pageSize = 10;
    private String mStartDate, mEndDate;
    private void initRefresh(){
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
    public void refreshAdapter(List<DeliveryBean> dataList){
        if(!(dataList!=null && dataList.size() < pageSize)){
            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }
        if(pageNo==1){
            //上拉刷新
            mAdapter.setNewData(dataList);
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setNoMoreData(false);
        }else{
            //加载更多
            mAdapter.addData(dataList);
            mRefreshLayout.finishLoadMore();
        }
    }


    /**
     * 筛选时间的对话框
     */
    int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;//年，月，日
    private void showDialogStartEndTime() {
        if(mStartYear == 0){
            mStartYear = mEndYear = MyTimeUtils.getYear();
            mStartMonth = mEndMonth = MyTimeUtils.getMonth();
            mStartDay = mEndDay = MyTimeUtils.getDay();
        }
        new MyDoubleDatePickerDialog(context, "筛选时间", mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay,
                new MyDoubleDatePickerDialog.DateTimeListener() {
                    @Override
                    public void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate) {
                        mTvStartEndTime.setText(startDate + " 至 " + endDate);
                        mStartYear = year;
                        mStartMonth = month;
                        mStartDay = day;
                        mEndYear = year2;
                        mEndMonth = month2;
                        mEndDay = day2;
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

    private String[] items = {"全部", "待接收", "已接收", "配送中"};
    private void showDialogStatus(){
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTvStatus.setText(items[position]);
                if(0 == position){
                    psState = "";
//                    mTvStatus.setText("订单状态");
                }else if(1 == position){
                    psState = "1";
                }else if(2 == position){
                    psState = "2";
                }else if(3 == position){
                    psState = "3";
                }
                pageNo = 1;
                queryData();
            }
        });
    }



}
