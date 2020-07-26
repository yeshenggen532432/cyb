package com.qwb.view.cache.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.cache.model.CacheAdapter;
import com.qwb.view.cache.model.MineCacheBean;
import com.qwb.view.cache.parsent.PCache;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 手动缓存(我的客户;商品分类，商品)
 */
public class CacheActivity extends XActivity<PCache>{

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_cache;
    }

    @Override
    public PCache newP() {
        return new PCache();
    }

    public void initData(Bundle savedInstanceState) {
        //开启：停止上传拜访缓存数据
        SPUtils.setBoolean(ConstantUtils.Sp.STOP_UPLOAD_STEP_CACHE, true);
        initUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭：停止上传拜访缓存数据
        SPUtils.setBoolean(ConstantUtils.Sp.STOP_UPLOAD_STEP_CACHE, false);
    }

    private void initUI() {
        initHead();
        initAdapter();
        refreshAdapter();
    }

    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle.setText("我的缓存");
    }

    /**
     * 初始化适配器（我的客户）
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    CacheAdapter mAdapter;
    private void initAdapter() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_0_5)
                .build());
        mAdapter = new CacheAdapter();
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
        //item点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    jumpActivity(position);
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });
        //子item点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.layout_right:
                        showDialogCache(position);
                        break;
                    case R.id.layout_left:
                        jumpActivity(position);
                        break;
                }
            }
        });
    }

    private void jumpActivity(int position) {
        //0.我的客户 1.商品分类及商品 2.我的拜访 3.我的订单
        switch (position){
            case 0://我的客户
                ActivityManager.getInstance().jumpActivity(context, CacheCustomerActivity.class);
                break;
            case 1://商品分类及商品
                ActivityManager.getInstance().jumpActivity(context, CacheWareActivity.class);
                break;
            case 2://我的拜访
                ActivityManager.getInstance().jumpActivity(context, CacheStepActivity.class);
                break;
            case 3://我的订单
                ActivityManager.getInstance().jumpActivity(context, CacheOrderActivity.class);
                break;
        }
    }

    public void refreshAdapter(){
        List<MineCacheBean> beanList = new ArrayList<>();
        beanList.add(new MineCacheBean(R.mipmap.ic_download_gray_333, "我的客户"));
        beanList.add(new MineCacheBean(R.mipmap.ic_download_gray_333, "商品分类及商品"));
        beanList.add(new MineCacheBean(null, "我的拜访"));
        beanList.add(new MineCacheBean(null, "我的订单"));
        mAdapter.setNewData(beanList);
    }

    /**
     * 缓存商品
     */
    private void showDialogCache(final int position) {
        String content = "";
        switch (position){
            case 0:
                content = "是否下载'我的客户'于本地，此操作需要较多时间，请在网络较好环境下使用";
                break;
            case 1:
                content = "是否下载'商品分类及商品'于本地，此操作需要较多时间，请在网络较好环境下使用";
                break;
        }
        NormalDialog dialog = new NormalDialog(context);
        dialog.content(content) .show();
        dialog.setOnBtnClickL(null,new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                switch (position){
                    case 0:
                        getP().queryAllCustomer(context);
                        break;
                    case 1:
                        getP().queryAllCacheWare(context);
                        getP().queryAllCacheWareType(context);
                        break;
                }

            }
        });
    }




}
