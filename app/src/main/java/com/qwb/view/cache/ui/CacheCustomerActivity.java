package com.qwb.view.cache.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.view.step.ui.StepActivity;
import com.qwb.utils.ToastUtils;
import com.qwb.view.cache.adapter.CacheCustomerAdapter;
import com.qwb.db.DMineCustomerBean;
import com.qwb.view.cache.parsent.PCacheCustomer;
import com.qwb.utils.MyKeyboardUtil;
import com.qwb.utils.MyMapUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.cnlife.view.widget.MyLoadingDialog;
import com.xmsx.cnlife.view.widget.MyVoiceDialog;
import com.qwb.view.customer.model.MineClientInfo;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 我的缓存客户
 */
public class CacheCustomerActivity extends XActivity<PCacheCustomer>{

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_mine_customer;
    }

    @Override
    public PCacheCustomer newP() {
        return new PCacheCustomer();
    }

    public void initData(Bundle savedInstanceState) {
        initHead();
        initSearch();
        initAdapter();
        initRefresh();
        showLoadingDialog();
        initLocation();
    }

    private double mLatitude;
    private double mLongitude;
    private String mAddress;
    private void initLocation() {
        MyMapUtil.getInstance()
                .getLocationClient(context)
                .setOnLocationListener(new MyMapUtil.OnLocationListener() {
                    @Override
                    public void setOnSuccessListener(BDLocation bdLocation) {
                        mLongitude = bdLocation.getLongitude();
                        mLatitude = bdLocation.getLatitude();
                        mAddress = bdLocation.getAddrStr();
                        queryData();
                    }

                    @Override
                    public void setAddressListener(String addressStr) {
                        mAddress = addressStr;
                    }

                    @Override
                    public void setErrorListener() {
                    }
                });
    }

    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.head_right2)
    View mHeadRight2;
    @BindView(R.id.iv_head_right)
    ImageView mIvHeadRight;
    @BindView(R.id.iv_head_right2)
    ImageView mIvHeadRight2;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvHeadTitle.setText("我的缓存客户");
        LinearLayout.LayoutParams voiceParams = new LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.dp_17),(int)getResources().getDimension(R.dimen.dp_17));
        LinearLayout.LayoutParams downParams = new LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.dp_20),(int)getResources().getDimension(R.dimen.dp_20));
        mIvHeadRight.setLayoutParams(voiceParams);
        mIvHeadRight.setImageResource(R.mipmap.ic_voice_gray_33);
        mIvHeadRight2.setLayoutParams(downParams);
        mIvHeadRight2.setImageResource(R.mipmap.ic_download_gray_333);
        //语音
        mHeadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogVoice();
            }
        });
        //下载缓存客户
        mHeadRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCacheClient();
            }
        });
    }

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.layout_search)
    View mLayoutSearch;
    private void initSearch() {
        mLayoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSearchQueryData(mEtSearch.getText().toString().trim());
            }
        });
    }

    /**
     * 初始化适配器（我的客户）
     */
    @BindView(R.id.rv_mine_client)
    RecyclerView mRvMineClient;
    CacheCustomerAdapter mMineAdapter;
    private void initAdapter() {
        mRvMineClient.setHasFixedSize(true);
        mRvMineClient.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRvMineClient.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_5)
                .build());
        mMineAdapter = new CacheCustomerAdapter();
        mMineAdapter.openLoadAnimation();
        mRvMineClient.setAdapter(mMineAdapter);
        //item点击事件
        mMineAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    DMineCustomerBean item = (DMineCustomerBean) adapter.getData().get(position);
                    MineClientInfo client = new MineClientInfo();
                    client.setId(Integer.valueOf(item.getCid()));
                    client.setKhNm(item.getKhNm());
                    client.setMemberNm(item.getMemberNm());
                    client.setQdtpNm(item.getQdtpNm());
                    client.setAddress(item.getAddress());
                    client.setLinkman(item.getLinkman());
                    client.setMobile(item.getMobile());
                    client.setLatitude(item.getLatitude());
                    client.setLongitude(item.getLongitude());
                    client.setTel(item.getTel());
                    client.setKhTp(Integer.valueOf(item.getKhTp()));
                    client.setXxzt(item.getXxzt());
                    client.setScbfDate(item.getScbfDate());
                    Router.newIntent(context)
                            .putInt(ConstantUtils.Intent.TYPE,2)
                            .putParcelable(ConstantUtils.Intent.MINE_CLIENT,client)
                            .to(StepActivity.class)
                            .launch();
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });
        //子item点击事件
        mMineAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.item_layout_nav://导航
                        ActivityManager.getInstance().jumpActivityNavMap(context,String.valueOf(mLatitude),String.valueOf(mLongitude),mAddress);
                        break;
                }
            }
        });
    }

    /**
     * 初始化刷新控件（我的客户）
     */
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
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
                pageNo ++;
                queryData();
            }
        });
    }

    private int pageNo = 1;
    private int pageSize = 20;
    private void queryData() {
        String search = mEtSearch.getText().toString().trim();
        List<DMineCustomerBean> customerBeans = MyDataUtils.getInstance().queryMineClient2(pageNo, pageSize, search, mLatitude, mLongitude);
        dissmissLoadingDialog();
        if(pageNo == 1){
            mMineAdapter.setNewData(customerBeans);
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setNoMoreData(false);
        }else{
            mMineAdapter.addData(customerBeans);
            mRefreshLayout.finishLoadMore();
        }

//        mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
    }

    /**
     * 请求本地数据时：第一次进入页面；加载对话框
     */
    private MyLoadingDialog dialog;
    private void showLoadingDialog(){
        dialog = new MyLoadingDialog(context);
        dialog.show();
    }

    private void dissmissLoadingDialog(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 缓存我的客户
     */
    private void showDialogCacheClient() {
        NormalDialog dialog = new NormalDialog(context);
        dialog.content("是否刷新下载直属客户于本地，此操作可能会消耗较多时间，请在网络较好环境下使用") .show();
        dialog.setOnBtnClickL(null,new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        getP().loadDataAllClient(context,mLatitude,mLongitude);
                    }
                });
    }

    //语音搜索对话框
    private void showDialogVoice() {
        MyVoiceDialog dialog = new MyVoiceDialog(context);
        dialog.show();
        dialog.setOnSuccessOnclick(new MyVoiceDialog.OnSuccessListener() {
            @Override
            public void onSuccessOnclick(String result) {
                doSearchQueryData(result);
            }
        });
    }

    //处理搜索和语音搜索成功回调：搜索列表
    private void doSearchQueryData(String result) {
        mEtSearch.setText(result);
        mEtSearch.setSelection(mEtSearch.length());
        pageNo = 1 ;
        mRefreshLayout.autoRefresh();
        MyKeyboardUtil.closeKeyboard(context);
    }

    /**
     * 刷新adapter
     */
    public void refreshAdpater(List<DMineCustomerBean> beanList){
        mMineAdapter.setNewData(beanList);
    }

}
