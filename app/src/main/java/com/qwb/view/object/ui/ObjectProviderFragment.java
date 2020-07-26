package com.qwb.view.object.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.event.ObjectEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ToastUtils;
import com.qwb.view.object.adapter.ProviderAdapter;
import com.qwb.view.object.model.ProviderListBean;
import com.qwb.view.object.parsent.PObjectProvider;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XFragment;

/**
 * 对象：供应商
 */
public class ObjectProviderFragment extends XFragment<PObjectProvider> {

    @Override
    public int getLayoutId() {
        return R.layout.x_fragment_object_common;
    }

    @Override
    public PObjectProvider newP() {
        return new PObjectProvider();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        queryData();
    }

    public void queryData(){
        String search = mEtSearch.getText().toString().trim();
        getP().queryProviderPage(context, search, pageNo, pageSize);
    }

    private void initUI() {
        initSearch();
        initAdapter();
    }

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    private void initSearch() {
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryData();
            }
        });
    }

    /**
     * 初始化适配器
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    ProviderAdapter mAdapter;
    int pageNo = 1;
    int pageSize = 10;
    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_1)
                .build());
        mAdapter = new ProviderAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                queryData();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                queryData();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    ActivityManager.getInstance().closeActivity(context);

                    ProviderListBean.ProviderBean bean = (ProviderListBean.ProviderBean)adapter.getData().get(position);
                    ObjectEvent event = new ObjectEvent();
                    event.setId(bean.getId());
                    event.setName(bean.getProName());
                    event.setType(0);
                    BusProvider.getBus().post(event);
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });
    }

    /**
     * 刷新适配器
     */
    public void refreshAdapter(List<ProviderListBean.ProviderBean> dataList){
        try {
            if(dataList == null){
                return;
            }
            if(pageNo == 1){
                mAdapter.setNewData(dataList);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.setNoMoreData(false);
            }else{
                mAdapter.addData(dataList);
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.setNoMoreData(false);
            }
            if(dataList.size() < pageSize){
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }catch (Exception e){}
    }




}
