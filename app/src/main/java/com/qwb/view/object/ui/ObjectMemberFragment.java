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
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.event.ObjectEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ToastUtils;
import com.qwb.view.order.adapter.MemberAdapter;
import com.qwb.view.member.model.MemberListBean;
import com.qwb.view.object.parsent.PObjectMember;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XFragment;

/**
 * 对象：选择员工
 */
public class ObjectMemberFragment extends XFragment<PObjectMember> {

    @Override
    public int getLayoutId() {
        return R.layout.x_fragment_object_common;
    }

    @Override
    public PObjectMember newP() {
        return new PObjectMember();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        queryData();
    }

    public void queryData(){
        String search = mEtSearch.getText().toString().trim();
        getP().queryCompanyMemberList(context, search);
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
    MemberAdapter mAdapter;
    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_1)
                .build());
        mAdapter = new MemberAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setEnableLoadMore(false);//没有加载更多
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                queryData();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    ActivityManager.getInstance().closeActivity(context);

                    MemberListBean.MemberBean bean = (MemberListBean.MemberBean)adapter.getData().get(position);
                    ObjectEvent event = new ObjectEvent();
                    event.setId(bean.getMemberId());
                    event.setName(bean.getMemberNm());
                    event.setType(1);
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
    public void refreshAdapter(List<MemberListBean.MemberBean> dataList){
        mAdapter.setNewData(dataList);
        mRefreshLayout.finishRefresh();
    }


}
