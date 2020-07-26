package com.qwb.view.car.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.deadline.statebutton.StateButton;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ToastUtils;
import com.qwb.view.car.adapter.CarStkWareAdapter;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.car.parsent.PCarStkWare;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.view.step.model.ShopInfoBean;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 创建描述：仓库管理（车削查询库存）
 */
public class CarStkWareActivity extends XActivity<PCarStkWare> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_car_stk_ware;
    }

    @Override
    public PCarStkWare newP() {
        return new PCarStkWare();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        getP().queryDataStkWareCarList(context, stkId);
    }

    /**
     * 初始化Intent
     */
    private String stkId;//仓库id
    private String stkName;//仓库名
    private ArrayList<ShopInfoBean.Data> mCurrentList = new ArrayList<>();//本页面添加的数据
    private void initIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            stkId=intent.getStringExtra(ConstantUtils.Intent.STK_ID);
            stkName=intent.getStringExtra(ConstantUtils.Intent.NAME);
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initAdapter();
        initBottom();
    }

    @BindView(R.id.cb_all_select)
    CheckBox mCbAll;
    private boolean cbStatus = true;//全中状态
    private void initBottom() {
        //mCbAll
        mCbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbStatus = !cbStatus;
                mCurrentList.clear();
                if (cbStatus) {
                    mCurrentList.addAll(mAdapter.getData());
                }
                mAdapter.setSelectList(mCurrentList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.head_right2)
    View mHeadRight2;
    @BindView(R.id.sb_head_right)
    StateButton mSbHeadRight;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.iv_head_right2)
    ImageView mIvHeadRight2;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mTvHeadTitle.setText(stkName);
        mSbHeadRight.setVisibility(View.VISIBLE);
        mSbHeadRight.setText("回库");
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mSbHeadRight.setOnClickListener(new OnNoMoreClickListener() {
            @Override
            protected void OnMoreClick(View view) {
                Intent intent = new Intent(context, CarStkInOrderActivity.class);
                intent.putExtra(ConstantUtils.Intent.ORDER_TYPE,ConstantUtils.Order.O_CXHK);
                intent.putParcelableArrayListExtra(ConstantUtils.Intent.CHOOSE_WARE_NEW, mCurrentList);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化适配器（订货）
     */
    RecyclerView mRecyclerView;
    CarStkWareAdapter mAdapter;
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    private void initAdapter() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_0_5)
                .build());
        mAdapter = new CarStkWareAdapter();
        mRecyclerView.setAdapter(mAdapter);
        //item点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                        ShopInfoBean.Data bean = (ShopInfoBean.Data) adapter.getData().get(position);
                        boolean flag = false;
                        for (ShopInfoBean.Data item : mCurrentList) {
                            if((""+bean.getWareId()).equals(""+item.getWareId())){
                                flag = true;
                                break;
                            }
                        }
                        if(!flag){
                            mCurrentList.add(bean);
                            if (mCurrentList.size() == adapter.getData().size()) {
                                mCbAll.setChecked(true);
                                cbStatus = true;
                            } else {
                                mCbAll.setChecked(false);
                                cbStatus = false;
                            }
                        }else{
                            mCurrentList.remove(bean);
                            mCbAll.setChecked(false);
                            cbStatus = false;
                        }
                        mAdapter.setSelectList(mCurrentList);
                        mAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });

        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                getP().queryDataStkWareCarList(context, stkId);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                getP().queryDataStkWareCarList(context, stkId);
            }
        });
    }

    //----------------------------------------------------------------------------------------------
    private boolean isAdd = false;
    public void refreshAdapter(List<ShopInfoBean.Data> list){
        if(list!=null){
            if(!isAdd && list.size()> 0){
                setCurrentList(list);
                isAdd = true;
            }
            mAdapter.setSelectList(mCurrentList);
            mAdapter.setNewData(list);
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setNoMoreData(false);
        }
    }

    public void setCurrentList(List<ShopInfoBean.Data> list){
        if(list!=null){
            for (ShopInfoBean.Data bean:list) {
                bean.setCurrentCount(""+bean.getSumQty());
                bean.setCurrentCode("B");
                bean.setCurrentDw(bean.getWareDw());
                bean.setCurrentPrice(""+bean.getWareDj());
                bean.setCurrentXstp("正常销售");
                bean.setCurrentBz("");
                mCurrentList.add(bean);
            }
        }

    }



}
