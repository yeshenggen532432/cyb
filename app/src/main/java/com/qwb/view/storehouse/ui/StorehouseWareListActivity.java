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
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.common.TypeEnum;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.storehouse.adapter.StorehouseWareListAdapter;
import com.qwb.view.storehouse.model.StorehouseBean;
import com.qwb.view.storehouse.model.StorehouseInBean;
import com.qwb.view.storehouse.model.StorehouseWareBean;
import com.qwb.view.storehouse.parsent.PStorehouseWareList;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ToastUtils;
import com.qwb.view.stk.StorageBean;
import com.qwb.utils.MyKeyboardUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 库位:库位商品列表
 */
public class StorehouseWareListActivity extends XActivity<PStorehouseWareList> {

    public StorehouseWareListActivity() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_storehouse_in_list;
    }

    @Override
    public PStorehouseWareList newP() {
        return new PStorehouseWareList();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        queryData();
    }

    private String mHouseId;
    private String mStkId;
    private void queryData(){
        getP().queryData(context, pageNo, mStkId, mHouseId, mSearchStr);
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
        mTvHeadTitle.setText("库位商品");
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
    private void initScreening() {
        mTvScreeningTab1.setText("仓库");
        mTvScreeningTab2.setText("库位");
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
        if(MyCollectionUtil.isNotEmpty(mStorageList)){
            showDialogStorage(mStorageList);
        }else {
            getP().queryStorage(context);
        }
    }
    //筛选2
    private void doScreeningTab2(){
//        if(MyCollectionUtil.isNotEmpty(mStorehouseList)){
//            showDialogStorehouse(mStorehouseList);
//        }else {
//            getP().queryStorehouseList(context, mStkId);
//        }
        if(MyStringUtil.isEmpty(mStkId))ToastUtils.showCustomToast("先选择仓库");
        getP().queryStorehouseList(context, mStkId);
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
     * 初始化适配器
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    StorehouseWareListAdapter mAdapter;
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
        mAdapter = new StorehouseWareListAdapter();
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
    public void refreshAdapter(List<StorehouseWareBean> dataList) {
        //过滤：合计数
        if (MyCollectionUtil.isNotEmpty(dataList)) {
            if (MyCollectionUtil.isNotEmpty(dataList)) {
                //最后一条数据是合计
                StorehouseWareBean  lastBean = dataList.get(dataList.size() - 1);
                if(lastBean.getWareId() == null){
                    dataList.remove(dataList.size() - 1);
                }
                for (StorehouseWareBean bean : dataList) {
                    bean.setOutStkId(bean.getHouseId());
                    bean.setOutStkName(bean.getHouseName());
                }
            } else {
                dataList = new ArrayList<>();
            }
        }else{
            dataList = new ArrayList<>();
        }

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
     * 对话框-选择仓库
     */
    private List<StorageBean.Storage> mStorageList = new ArrayList<>();
    public void showDialogStorage(List<StorageBean.Storage> storageList) {
        try {
            if (MyCollectionUtil.isEmpty(mStorageList)) {
                mStorageList = storageList;
            }
            if (MyCollectionUtil.isEmpty(mStorageList)) {
                ToastUtils.showCustomToast("没有仓库可以选择");
                return;
            }

            final ArrayList<DialogMenuItem> items = new ArrayList<>();
            for (StorageBean.Storage storage : mStorageList) {
                items.add(new DialogMenuItem(storage.getStkName(), storage.getId()));
            }
            NormalListDialog dialog = new NormalListDialog(context, items);
            dialog.title("选择仓库").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                    mStkId = "" + items.get(i).mResId;
                    mTvScreeningTab1.setText(items.get(i).mOperName);
                    pageNo = 1;
                    mHouseId = "";
                    mTvScreeningTab2.setText("库位");
                    queryData();
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    /**
     * 对话框-库位
     */
    private List<StorehouseBean> mStorehouseList = new ArrayList<>();
    public void showDialogStorehouse(List<StorehouseBean> storehouseList) {
        try {
            if (MyCollectionUtil.isEmpty(mStorehouseList)) {
                mStorehouseList = storehouseList;
            }
            if (MyCollectionUtil.isEmpty(mStorehouseList)) {
                ToastUtils.showCustomToast("没有库位");
                return;
            }
            final ArrayList<DialogMenuItem> items = new ArrayList<>();
            for (StorehouseBean bean : mStorehouseList) {
                items.add(new DialogMenuItem(bean.getHouseName(), bean.getId()));
            }
            NormalListDialog dialog = new NormalListDialog(context, items);
            dialog.title("选择库位").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                    try {
                        DialogMenuItem item = items.get(i);
                        pageNo = 1;
                        mHouseId = "" + item.mResId;
                        mTvScreeningTab2.setText(item.mOperName);
                        queryData();
                    } catch (Exception e) {
                        ToastUtils.showError(e);
                    }
                    MyKeyboardUtil.closeKeyboard(context);//强制关闭软键盘
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }







}
