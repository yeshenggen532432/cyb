package com.qwb.view.cache.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.cache.adapter.CacheStep1Adapter;
import com.qwb.view.cache.adapter.CacheStep2Adapter;
import com.qwb.view.cache.adapter.CacheStep3Adapter;
import com.qwb.view.cache.adapter.CacheStep4Adapter;
import com.qwb.view.cache.adapter.CacheStep5Adapter;
import com.qwb.view.cache.adapter.CacheStep6Adapter;
import com.qwb.view.cache.adapter.CacheStepAllAdapter;
import com.qwb.db.DStep1Bean;
import com.qwb.db.DStep2Bean;
import com.qwb.db.DStep3Bean;
import com.qwb.db.DStep4Bean;
import com.qwb.db.DStep5Bean;
import com.qwb.db.DStep6Bean;
import com.qwb.db.DStepAllBean;
import com.qwb.view.cache.parsent.PCacheStep;
import com.qwb.utils.MyNetWorkUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 缓存-我的拜访
 */
public class CacheStepActivity extends XActivity<PCacheStep>{

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_cache_step;
    }

    @Override
    public PCacheStep newP() {
        return new PCacheStep();
    }

    public void initData(Bundle savedInstanceState) {
        initUI();
        getP().queryData(context, pageNo, pageSize);
    }

    private void initUI() {
        initHead();
        initAdapter();
    }

    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mHeadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangeStep();
            }
        });
        mTvHeadTitle.setText("我的拜访");
        mTvHeadRight.setText("切换");
    }

    /**
     * 初始化适配器
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    CacheStepAllAdapter mAdapter;
    CacheStep1Adapter mAdapter1;
    CacheStep2Adapter mAdapter2;
    CacheStep3Adapter mAdapter3;
    CacheStep4Adapter mAdapter4;
    CacheStep5Adapter mAdapter5;
    CacheStep6Adapter mAdapter6;
    private int pageNo = 1;
    private int pageSize = 10;
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    private void initAdapter() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_0_5)
                .build());
        mAdapter = new CacheStepAllAdapter(context);
        mAdapter1 = new CacheStep1Adapter();
        mAdapter2 = new CacheStep2Adapter();
        mAdapter3 = new CacheStep3Adapter();
//        mAdapter4 = new CacheStep4Adapter();
//        mAdapter5 = new CacheStep5Adapter();
        mAdapter6 = new CacheStep6Adapter(context);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNo = 1;
                if(0 == mStep){
                    pageNo = 1;
                    getP().queryData(context, pageNo, pageSize);
                }else if(4 == mStep){
                    getP().queryAllCache(context, 6 , pageNo, pageSize);
                }else {
                    getP().queryAllCache(context, mStep, pageNo, pageSize);
                }
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo ++;
                if(0 == mStep){
                    pageNo = 1;
                    getP().queryData(context, pageNo, pageSize);
                }else if(4 == mStep){
                    getP().queryAllCache(context, 6, pageNo, pageSize);
                }else {
                    getP().queryAllCache(context, mStep, pageNo, pageSize);
                }
            }
        });

//        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                try {
//                }catch (Exception e){
//                    ToastUtils.showError(e);
//                }
//            }
//        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    mPosition = position;
                    switch (view.getId()){
                        case R.id.right:
                            if(!MyNetWorkUtil.isNetworkConnected()){
                                ToastUtils.showCustomToast("当前网络不流通，请检查网络");
                                return;
                            }
                            DStepAllBean bean = (DStepAllBean) adapter.getData().get(position);
                            if(null != bean.getdStep6Bean()){
                                getP().doStep6(bean.getdStep6Bean(), 1);
                            }
                            if(null != bean.getdStep3Bean()){
                                getP().doStep3(bean.getdStep3Bean(), 1);
                            }
                            if(null != bean.getdStep2Bean()){
                                getP().doStep2(bean.getdStep2Bean(), 1);
                            }
                            if(null != bean.getdStep1Bean()){
                                getP().doStep1(bean.getdStep1Bean(), 1);
                            }
                            break;
                        default:
                            ConstantUtils.stepAllBean = (DStepAllBean) adapter.getData().get(position);
                            ActivityManager.getInstance().jumpActivity(context, CacheStepDetailActivity.class);
                            break;
                    }
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });
        mAdapter1.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    mPosition = position;
                    mDStep1Bean = (DStep1Bean) adapter.getData().get(position);
                    switch (view.getId()){
                        case R.id.item_layout_sd:
                            showDialogCaozuo(1);
                            break;
                    }
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });
        mAdapter2.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    mPosition = position;
                    mDStep2Bean = (DStep2Bean) adapter.getData().get(position);
                    switch (view.getId()){
                        case R.id.item_layout_sd:
                            showDialogCaozuo(2);
                            break;
                    }
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });
        mAdapter3.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    mPosition = position;
                    mDStep3Bean = (DStep3Bean) adapter.getData().get(position);
                    switch (view.getId()){
                        case R.id.item_layout_sd:
                            showDialogCaozuo(3);
                            break;
                    }
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });
//        mAdapter4.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                try {
//                    mPosition = position;
//                    mDStep4Bean = (DStep4Bean) adapter.getData().get(position);
//                    switch (view.getId()){
//                        case R.id.item_layout_sd:
//                            showDialogCaozuo(4);
//                            break;
//                    }
//                }catch (Exception e){
//                    ToastUtils.showError(e);
//                }
//            }
//        });
//        mAdapter5.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                try {
//                    mPosition = position;
//                    mDStep5Bean = (DStep5Bean) adapter.getData().get(position);
//                    switch (view.getId()){
//                        case R.id.item_layout_sd:
//                            showDialogCaozuo(5);
//                            break;
//                    }
//                }catch (Exception e){
//                    ToastUtils.showError(e);
//                }
//            }
//        });
        mAdapter6.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    mPosition = position;
                    mDStep6Bean = (DStep6Bean) adapter.getData().get(position);
                    switch (view.getId()){
                        case R.id.item_layout_sd:
                            showDialogCaozuo(6);
                            break;
                    }
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });
    }

    private int mStep;//0:全部步骤
    public void showDialogChangeStep(){
        String[] items = new String[]{"全部步骤","步骤1","步骤2","步骤3","步骤6",};
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.title("切换步骤").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                mStep = position;
                if(0 == position){
                    pageNo = 1;
                    getP().queryData(context, pageNo, pageSize);
                }else if(4 == position){
                    getP().queryAllCache(context, 6, pageNo, pageSize);
                }else {
                    getP().queryAllCache(context, position, pageNo, pageSize);
                }
            }
        });
    }

    private DStep1Bean mDStep1Bean;
    private DStep2Bean mDStep2Bean;
    private DStep3Bean mDStep3Bean;
    private DStep4Bean mDStep4Bean;
    private DStep5Bean mDStep5Bean;
    private DStep6Bean mDStep6Bean;
    private int mPosition;
    public void showDialogCaozuo(final int step){
        String[] items = new String[]{"删除","上传"};
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.title("操作").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                int count = 0;
                //删除
                if(0 == position){
                    switch (step){
                        case 1:
                            count = MyDataUtils.getInstance().delStep1(mDStep1Bean);
                            if(count > 0){
                                refreshAdapter1(null, true);
                                ToastUtils.showCustomToast("删除成功");
                            }
                            break;
                        case 2:
                            count = MyDataUtils.getInstance().delStep2(mDStep2Bean);
                            if(count > 0){
                                refreshAdapter2(null, true);
                                ToastUtils.showCustomToast("删除成功");
                            }
                            break;
                        case 3:
                            count = MyDataUtils.getInstance().delStep3(mDStep3Bean);
                            if(count > 0){
                                refreshAdapter3(null, true);
                                ToastUtils.showCustomToast("删除成功");
                            }
                            break;
                        case 4:
                            count = MyDataUtils.getInstance().delStep4(mDStep4Bean);
                            if(count > 0){
                                refreshAdapter4(null, true);
                                ToastUtils.showCustomToast("删除成功");
                            }
                            break;
                        case 5:
                            count = MyDataUtils.getInstance().delStep5(mDStep5Bean);
                            if(count > 0){
                                refreshAdapter5(null, true);
                                ToastUtils.showCustomToast("删除成功");
                            }
                            break;
                        case 6:
                            count = MyDataUtils.getInstance().delStep6(mDStep6Bean);
                            if(count > 0){
                                refreshAdapter6(null, true);
                                ToastUtils.showCustomToast("删除成功");
                            }
                            break;
                    }
                }

                //上传
                if(1 == position){
                    if(!MyNetWorkUtil.isNetworkConnected()){
                        ToastUtils.showCustomToast("当前网络不流通，请检查网络");
                        return;
                    }
                    switch (step){
                        case 1:
                            getP().doStep1(mDStep1Bean, 0);
                            break;
                        case 2:
                            getP().doStep2(mDStep2Bean, 0);
                            break;
                        case 3:
                            getP().doStep3(mDStep3Bean, 0);
                            break;
//                        case 4:
//                            getP().doStep4(mDStep4Bean);
//                            break;
//                        case 5:
//                            getP().doStep5(mDStep5Bean);
//                            break;
                        case 6:
                            getP().doStep6(mDStep6Bean, 0);
                            break;
                    }
                }
            }
        });
    }

    public void refreshAdapter(List<DStepAllBean> beans, boolean del){
        mTvHeadTitle.setText("我的拜访");
        mRecyclerView.setAdapter(mAdapter);
        if(del){
            mAdapter.remove(mPosition);
            mAdapter.notifyDataSetChanged();
        }else{
            if(pageNo == 1){
                mAdapter.setNewData(beans);
                mRefreshLayout.setNoMoreData(false);
                mRefreshLayout.finishRefresh();
            }else{
                mAdapter.addData(beans);
                mRefreshLayout.setNoMoreData(false);
                mRefreshLayout.finishLoadMore();
            }
            if(beans != null && beans.size() < pageSize){
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }
    public void refreshAdapter1(List<DStep1Bean> beans, boolean del){
        mTvHeadTitle.setText("我的拜访(步骤1)");
        mRecyclerView.setAdapter(mAdapter1);
        if(del){
            mAdapter1.remove(mPosition);
            mAdapter1.notifyDataSetChanged();
        }else{
            if(pageNo == 1){
                mAdapter1.setNewData(beans);
                mRefreshLayout.setNoMoreData(false);
                mRefreshLayout.finishRefresh();
            }else{
                mAdapter1.addData(beans);
                mRefreshLayout.setNoMoreData(false);
                mRefreshLayout.finishLoadMore();
            }
            if(beans != null && beans.size() < pageSize){
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }
    public void refreshAdapter2(List<DStep2Bean> beans, boolean del){
        mTvHeadTitle.setText("我的拜访(步骤2)");
        mRecyclerView.setAdapter(mAdapter2);
        if(del){
            mAdapter2.remove(mPosition);
            mAdapter2.notifyDataSetChanged();
        }else{
            if(pageNo == 1){
                mAdapter2.setNewData(beans);
                mRefreshLayout.setNoMoreData(false);
                mRefreshLayout.finishRefresh();
            }else{
                mAdapter2.addData(beans);
                mRefreshLayout.setNoMoreData(false);
                mRefreshLayout.finishLoadMore();
            }
            if(beans != null && beans.size() < pageSize){
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }
    public void refreshAdapter3(List<DStep3Bean> beans, boolean del){
        mTvHeadTitle.setText("我的拜访(步骤3)");
        mRecyclerView.setAdapter(mAdapter3);
        if(del){
            mAdapter3.remove(mPosition);
            mAdapter3.notifyDataSetChanged();
        }else{
            if(pageNo == 1){
                mAdapter3.setNewData(beans);
                mRefreshLayout.setNoMoreData(false);
                mRefreshLayout.finishRefresh();
            }else{
                mAdapter3.addData(beans);
                mRefreshLayout.setNoMoreData(false);
                mRefreshLayout.finishLoadMore();
            }
            if(beans != null && beans.size() < pageSize){
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }
    public void refreshAdapter4(List<DStep4Bean> beans, boolean del){
        mTvHeadTitle.setText("我的拜访(步骤4)");
        mRecyclerView.setAdapter(mAdapter4);
        if(del){
            mAdapter4.remove(mPosition);
            mAdapter4.notifyDataSetChanged();
        }else{
            mAdapter4.setNewData(beans);
        }
    }
    public void refreshAdapter5(List<DStep5Bean> beans, boolean del){
        mTvHeadTitle.setText("我的拜访(步骤5)");
        mRecyclerView.setAdapter(mAdapter5);
        if(del){
            mAdapter5.remove(mPosition);
            mAdapter5.notifyDataSetChanged();
        }else{
            mAdapter5.setNewData(beans);
        }
    }
    public void refreshAdapter6(List<DStep6Bean> beans, boolean del){
        mTvHeadTitle.setText("我的拜访(步骤6)");
        mRecyclerView.setAdapter(mAdapter6);
        if(del){
            mAdapter6.remove(mPosition);
            mAdapter6.notifyDataSetChanged();
        }else{
            if(pageNo == 1){
                mAdapter6.setNewData(beans);
                mRefreshLayout.setNoMoreData(false);
                mRefreshLayout.finishRefresh();
            }else{
                mAdapter6.addData(beans);
                mRefreshLayout.setNoMoreData(false);
                mRefreshLayout.finishLoadMore();
            }
            if(beans != null && beans.size() < pageSize){
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }




}
