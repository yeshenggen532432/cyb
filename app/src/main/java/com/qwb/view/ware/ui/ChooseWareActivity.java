package com.qwb.view.ware.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.scanlibrary.ScanActivity;
import com.qwb.utils.MyCollectionUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.ware.adapter.ChooseWareRightAdapter;
import com.qwb.view.ware.parsent.PChooseWare;
import com.qwb.utils.ToastUtils;
import com.qwb.view.ware.adapter.WareTreeAdapter;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.xmsx.cnlife.view.widget.MyVoiceDialog;
import com.qwb.view.step.model.ShopInfoBean.Data;
import com.qwb.view.base.model.TreeBean;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhy.tree.bean.Node;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 用于普通的选择商品（库存盘点）
 * TODO 下单使用ChooseWareActivity3（销售小结和订货下单）
 */
public class ChooseWareActivity extends XActivity<PChooseWare> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_choose_ware;
    }

    @Override
    public PChooseWare newP() {
        return new PChooseWare();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        getP().queryDataWareTypes(context, stkId);//获得商品类型列表
    }

    private int mRequestType = 3;//1:常售 2：收藏  3：分类   4：搜索(默认)
    private String mWareType;//商品类型（只是当前的）
    private String mKeyWord;//关键字
    private void setRequestType(int type, String wareType) {
        mRequestType = type;
        pageNo = 1;
        mWareType = wareType;
        mKeyWord = mEtSearch.getText().toString().trim();
    }

    public void queryData(){
        switch (mRequestType) {
            case 3:
                getP().queryDataByWareType(context, mWareType, stkId, pageNo, pageSize);
                break;
            case 4:
                getP().queryDataByKeyWord(context, mKeyWord, stkId, pageNo, pageSize);
                break;
        }
    }

    private String stkId;//仓库id
    private ArrayList<Data> mSelectList = new ArrayList<>();//总的数据
    private void initIntent() {
        Intent intent = getIntent();
        if (null != intent) {
            stkId = intent.getStringExtra(ConstantUtils.Intent.STK_ID);
            ArrayList<Data> data = intent.getParcelableArrayListExtra(ConstantUtils.Intent.CHOOSE_WARE);
            if (MyCollectionUtil.isNotEmpty(data)) {
                mSelectList.addAll(data);
            }
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initSearch();
        initAdapter();
        initBottom();
    }

    //初始化头部
    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.head_right2)
    View mHeadRight2;
    @BindView(R.id.head_right3)
    View mHeadRight3;
    @BindView(R.id.iv_head_right)
    ImageView mIvHeadRight;
    @BindView(R.id.iv_head_right2)
    ImageView mIvHeadRight2;
    @BindView(R.id.iv_head_right3)
    ImageView mIvHeadRight3;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;

    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mTvHeadTitle.setText("选择商品");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_17), (int) getResources().getDimension(R.dimen.dp_17));
        mIvHeadRight.setImageResource(R.mipmap.ic_scan_gray_33);
        mIvHeadRight.setLayoutParams(params);
        mIvHeadRight2.setImageResource(R.mipmap.ic_voice_gray_33);
        mIvHeadRight2.setLayoutParams(params);

        mHeadLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mHeadRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().jumpScanActivity(context, false);//扫描
            }
        });
        mHeadRight2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogVoice();
            }
        });//语音
    }

    //初始化搜索
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.layout_search)
    View mViewSearch;
    private void initSearch() {
        mViewSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setRequestType(4, null);
                    queryData();
                    refreshAdapterLeft(null);
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }

    //初始化过滤0库存
    @BindView(R.id.tv_storage_0)
    TextView mTvStorage0;
    @BindView(R.id.tv_confirm_choose_shop)
    TextView mTvConfirm;
    @BindView(R.id.tv_sum_choose_shop)
    TextView mTvSum;
    private void initBottom() {
        //过滤0库存
        mTvStorage0.setVisibility(View.GONE);
        mTvSum.setVisibility(View.GONE);
        //确定按钮
        mTvConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(ConstantUtils.Intent.CHOOSE_WARE, mSelectList);
                setResult(ConstantUtils.Intent.RESULT_CODE_CHOOSE_GOODS, intent);
                ActivityManager.getInstance().closeActivity(context);
            }
        });

    }

    /**
     * 适配器
     */
    @BindView(R.id.rv_right)
    RecyclerView mRvRight;
    private ChooseWareRightAdapter mAdapterRight;
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    private int pageNo = 1;
    private int pageSize = 10;
    private void initAdapter() {
        mRvRight.setLayoutManager(new LinearLayoutManager(context));
        mRvRight.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_0_5)
                .build());
        mAdapterRight = new ChooseWareRightAdapter();
        mAdapterRight.setSelectList(mSelectList);//设置已选中的
        mRvRight.setAdapter(mAdapterRight);

        //item点击事件
        mAdapterRight.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    Data bean = (Data) adapter.getData().get(position);
                    boolean flag = true;
                    for (Data item : mSelectList) {
                        if ((String.valueOf(bean.getWareId())).equals(String.valueOf(item.getWareId()))) {
                            flag = false;
                            mSelectList.remove(item);
                            break;
                        }
                    }
                    if (flag) {
                        mSelectList.add(bean);
                    }
                    mAdapterRight.setSelectList(mSelectList);
                    mAdapterRight.notifyDataSetChanged();
                } catch (Exception e) {
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

    //TODO -----------------------------------对话框：start-------------------------------------------------------
    //语音搜索对话框
    private void showDialogVoice() {
        MyVoiceDialog dialog = new MyVoiceDialog(context);
        dialog.show();
        dialog.setOnSuccessOnclick(new MyVoiceDialog.OnSuccessListener() {
            @Override
            public void onSuccessOnclick(String result) {
                doScanOrVoiceToSearch(result);
            }
        });
    }

    //处理扫描和语音搜索成功回调：搜索列表
    private void doScanOrVoiceToSearch(String result) {
        try {
            mEtSearch.setText(result);
            mEtSearch.setSelection(mEtSearch.length());
            setRequestType(4, null);
            queryData();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }
    //TODO -----------------------------------对话框：end-------------------------------------------------------

    //TODO ********************************************接口回调处理**********************************************************
    /**
     * 刷新左边Tree
     */
    @BindView(R.id.id_tree)
    ListView mLvLeft;
    private WareTreeAdapter<TreeBean> mAdapterLeft;
    private List<TreeBean> mLeftList = new ArrayList<>();
    public void doAdapterLeft(List<TreeBean> treeList) {
        try {
            if(MyCollectionUtil.isNotEmpty(treeList)){
                this.mLeftList = treeList;
            }
            if (mAdapterLeft == null) {
                mAdapterLeft = new WareTreeAdapter<>(mLvLeft, context, mLeftList, 0);
                mLvLeft.setAdapter(mAdapterLeft);
                mAdapterLeft.setOnTreeNodeClickListener(new WareTreeAdapter.OnTreeNodeClickListener() {
                    @Override
                    public void onClick(Node node, int i) {
                        try {
                            setRequestType(3, String.valueOf(node.getId()));
                            queryData();
                            refreshAdapterLeft(node.getId());
                        } catch (Exception e) {
                            ToastUtils.showError(e);
                        }
                    }
                });
            }

            //默认加载第一个
            if (MyCollectionUtil.isNotEmpty(mLeftList)) {
                setRequestType(3, String.valueOf(treeList.get(0).get_id()));
                queryData();
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 刷新左边：主要选中颜色
     */
    public void refreshAdapterLeft(Integer mid){
        if(mid == null){
            mid = -10;
        }
        mAdapterLeft.setmId(mid);
        mAdapterLeft.notifyDataSetChanged();
    }

    /**
     * 刷新右边adapter
     */
    public void refreshAdapterRight(List<Data> list) {
        try {
            if (null == list) {
                return;
            }
            if (pageNo == 1) {
                //上拉刷新
                mAdapterRight.setNewData(list);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.setNoMoreData(false);
            } else {
                //加载更多
                mAdapterRight.addData(list);
                mRefreshLayout.setNoMoreData(false);
                mRefreshLayout.finishLoadMore();
            }
            if (list.size() < 10) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                ToastUtils.showCustomToast("没有更多数据");
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }

    }

    //TODO******************************扫描:start************************************
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //扫描二维码/条码回传
            if (RESULT_OK == resultCode && requestCode == ConstantUtils.Intent.REQUEST_CODE_SCAN) {
                if (data != null) {
                    String result = data.getStringExtra(ScanActivity.SCAN_RESULT);
                    if (!MyStringUtil.isEmpty(result)) {
                        doScanOrVoiceToSearch(result);
                    } else {
                        ToastUtils.showCustomToast("扫描内容为空");
                    }
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }
    //TODO******************************扫描:end************************************

}
