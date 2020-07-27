package com.qwb.view.checkstorage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qwb.utils.MyColorUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.checkstorage.adapter.StkWareAdapter;
import com.qwb.view.checkstorage.model.StkWareBean;
import com.qwb.view.checkstorage.parsent.PxStkWare;
import com.qwb.view.ware.adapter.WareTreeAdapter;
import com.qwb.utils.ConstantUtils;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhy.tree.bean.Node;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 仓库商品
 */
public class XStkWareActivity extends XActivity<PxStkWare> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_stk_ware;
    }


    @Override
    public PxStkWare newP() {
        return new PxStkWare();
    }

    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        doIntent();
        getP().queryDataCompanyWareTypes(context, null, null, String.valueOf(stkId));//获得商品类型列表
    }

    private int stkId;//仓库id
    private String stkName;//仓库名称
    private ArrayList<Integer> ids;//已选商品ids
    private void initIntent() {
        Intent intent = getIntent();
        if (null != intent) {
            stkId = intent.getIntExtra(ConstantUtils.Intent.STK_ID, 0);
            stkName = intent.getStringExtra(ConstantUtils.Intent.NAME);
            ids = intent.getIntegerArrayListExtra(ConstantUtils.Intent.IDS);
        }
    }

    private void doIntent() {
        mTvHeadTitle.setText(stkName);
    }

    private void initUI() {
        initHead();
        initTab();
        initAdapterAllWare();//全部商品比对
//        initOther();
        initAdapterWareType();//商品分类比对
    }

    //头部
    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.head_right2)
    View mHeadRight2;
    @BindView(R.id.iv_head_right)
    ImageView mIvHeadRight;
    @BindView(R.id.tv_head_right2)
    TextView mTvHeadRight2;
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
        mTvHeadTitle.setText("未盘点比对");
    }

    @BindView(R.id.layout_tab1)
    View mLayoutTab1;
    @BindView(R.id.layout_tab2)
    View mLayoutTab2;
    @BindView(R.id.layout_tab_content1)
    View mLayoutTabContent1;
    @BindView(R.id.layout_tab_content2)
    View mLayoutTabContent2;
    @BindView(R.id.tv_tab1)
    TextView mTvTab1;
    @BindView(R.id.tv_tab2)
    TextView mTvTab2;
    private void initTab() {
        mLayoutTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvTab1.setTextColor(MyColorUtil.getColorResId(R.color.x_main_color));
                mTvTab2.setTextColor(MyColorUtil.getColorResId(R.color.gray_6));
                mLayoutTabContent1.setVisibility(View.VISIBLE);
                mLayoutTabContent2.setVisibility(View.GONE);
            }
        });
        mLayoutTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvTab1.setTextColor(MyColorUtil.getColorResId(R.color.gray_6));
                mTvTab2.setTextColor(MyColorUtil.getColorResId(R.color.x_main_color));
                mLayoutTabContent1.setVisibility(View.GONE);
                mLayoutTabContent2.setVisibility(View.VISIBLE);
                isAllWare = true;
                if(allWareList != null && allWareList.isEmpty()){
                    getP().queryStorageWarePage(context, stkId, 0,1, 10000);
                }

            }
        });
    }


    //初始化适配器
    @BindView(R.id.recyclerView_all_ware)
    RecyclerView mRvAllWare;
    private StkWareAdapter mAdapterAllWare;
    private void initAdapterAllWare() {
        mRvAllWare.setHasFixedSize(false);
        mRvAllWare.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRvAllWare.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_1)
                .build());
        mAdapterAllWare = new StkWareAdapter();
        mRvAllWare.setAdapter(mAdapterAllWare);
        mRvAllWare.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }


    private boolean isAllWare = false;//
    private List<StkWareBean> allWareList = new ArrayList<>();
    public void refreshAdapter(List<StkWareBean> list) {
        if(null == list){
            return;
        }
        List<StkWareBean> tempList = new ArrayList<>();
        if(ids != null && ids.size() > 0){
            for (StkWareBean wareBean:list) {
                boolean flag = true;
                for (int i : ids) {
                    if(String.valueOf(i).equals(""+wareBean.getWareId())){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    tempList.add(wareBean);
                }
            }
        }else{
            tempList.addAll(list);
        }

        if(isAllWare){
            allWareList.clear();
            allWareList.addAll(list);
            mAdapterAllWare.setIds(ids);
            mAdapterAllWare.setNewData(tempList);
        }else{
            mAdapterWare.setIds(ids);
            mAdapterWare.setNewData(tempList);
        }
    }


    /**
     * 适配器
     */
    // 左边的listView:树形列表
    @BindView(R.id.id_tree)
    ListView mTreeListView;
    private WareTreeAdapter<TreeBean> mTreeAdapter;
    private List<TreeBean> mTreeList = new ArrayList<>();
    @BindView(R.id.rv_right)
    RecyclerView mRvRight;
    private StkWareAdapter mAdapterWare;
    private void initAdapterWareType() {
        mRvRight.setHasFixedSize(true);
        mRvRight.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRvRight.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_0_5)
                .build());
        mAdapterWare = new StkWareAdapter();
        mRvRight.setAdapter(mAdapterWare);
    }

    public void refreshAdapterTree(List<TreeBean> treeList) {
        try {
            this.mTreeList = treeList;
            if (mTreeAdapter == null) {
                mTreeAdapter = new WareTreeAdapter<>(mTreeListView, context, this.mTreeList, 0);
                mTreeListView.setAdapter(mTreeAdapter);
                mTreeAdapter.setOnTreeNodeClickListener(new WareTreeAdapter.OnTreeNodeClickListener() {
                    @Override
                    public void onClick(Node node, int i) {
                        mTreeAdapter.setmId(node.getId());
                        mTreeAdapter.notifyDataSetChanged();
                        switch (node.getId()) {
                            default:// 根据类型查询商品
                                isAllWare = false;
                                getP().queryStorageWarePage(context, stkId, node.getId(),1, 10000);
                                break;
                        }
                    }
                });
            } else {
                mTreeAdapter.setNodes(this.mTreeList, 0);
                mTreeAdapter.notifyDataSetChanged();
            }

            //默认加载第一个（这边没有“常售商品”和“收藏商品”）
            if(treeList!=null && treeList.size()>0){
                getP().queryStorageWarePage(context, stkId, treeList.get(0).get_id(),1, 10000);
            }

        } catch (IllegalAccessException e) {
        }
    }


//    @BindView(R.id.iv_filter)
//    ImageView mIvFilter;
//    private boolean isFilter = true;//是否过滤已选商品（默认true）
//    private void initOther() {
//        findViewById(R.id.layout_filter).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isFilter = !isFilter;
//                if(isFilter){
//                    mIvFilter.setImageResource(R.drawable.icon_dxz);
//                }else{
//                    mIvFilter.setImageResource(R.drawable.icon_dx);
//                }
////                mAdapter.setFilter(isFilter);
////                mAdapter.notifyDataSetChanged();
//                pageNo = 1;
//                getP().queryStorageWarePage(context, stkId, pageNo, pageSize);
//            }
//        });
//    }


}
