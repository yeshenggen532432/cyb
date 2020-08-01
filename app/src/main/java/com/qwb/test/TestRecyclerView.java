package com.qwb.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qwb.utils.MyDividerUtil;
import com.qwb.view.storehouse.adapter.StorehouseArrangeListAdapter;
import com.qwb.view.storehouse.model.StorehouseInBean;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ToastUtils;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * 适配器
 */

public class TestRecyclerView {
    private Context context;
    /**
     * 初始化适配器
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    StorehouseArrangeListAdapter mAdapter;
    private int pageNo = 1;
    private int pageSize = 10;
    private StorehouseInBean mCurrentItem;
    private int mPosition;
    private void initAdapter() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(MyDividerUtil.getH5CGray(context));
        mAdapter = new StorehouseArrangeListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                try {
                    mCurrentItem = (StorehouseInBean) baseQuickAdapter.getData().get(i);
                    mPosition = i;
                } catch (Exception e) {
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNo = 1;
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo++;
            }
        });

    }

    /**
     * 刷新适配器
     */
    public void refreshAdapter(List<StorehouseInBean> dataList) {
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
            mRefreshLayout.setNoMoreData(false);
        }
        if (null != dataList && dataList.size() < pageSize) {
            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }
    }


//    <com.scwang.smartrefresh.layout.SmartRefreshLayout
//    android:id="@+id/refreshLayout"
//    android:layout_marginTop="@dimen/dp_10"
//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"
//    app:srlEnableScrollContentWhenLoaded="true"
//    app:srlEnableFooterFollowWhenLoadFinished="true">
//        <!--srlAccentColor srlPrimaryColor 将会改变 Header 和 Footer 的主题颜色-->
//        <com.scwang.smartrefresh.layout.header.ClassicsHeader
//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"
//    app:srlAccentColor="@color/gray_6"
//    app:srlPrimaryColor="@color/layout_bg"
//    app:srlClassicsSpinnerStyle="FixedBehind"/>
//        <!--FixedBehind可以让Header固定在内容的背后，下拉的时候效果同微信浏览器的效果-->
//        <android.support.v7.widget.RecyclerView
//    android:id="@+id/recyclerView"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:fadingEdge="none"/>
//        <com.scwang.smartrefresh.layout.footer.ClassicsFooter
//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"
//    app:srlClassicsSpinnerStyle="Translate"/>
//    </com.scwang.smartrefresh.layout.SmartRefreshLayout>






}
