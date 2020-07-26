package com.qwb.view.cache.ui;

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

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ToastUtils;
import com.qwb.view.step.adapter.WareAdapter;
import com.qwb.view.ware.adapter.WareTreeAdapter;
import com.qwb.view.cache.parsent.PCacheWare;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
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
 * 创建描述：缓存商品
 * 修改描述：
 */
public class CacheWareActivity extends XActivity<PCacheWare> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_choose_ware;
    }

    @Override
    public PCacheWare newP() {
        return new PCacheWare();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        getP().queryCacheWareType(context);
        getP().queryCacheWare(context,"", null, pageNo, pageSize);
    }

    /**
     * 初始化UI
     */
    @BindView(R.id.parent)
    LinearLayout parent;
    @BindView(R.id.layout_bottom)
    View mLayoutBottom;
    @BindView(R.id.tv_sum_choose_shop)
    TextView mTvSum;
    @BindView(R.id.tv_confirm_choose_shop)
    TextView mTvConfirm;
    private void initUI() {
        initHead();
        initSearch();
        //左右列表
        initAdapter();
        //上拉下拉
        initRefresh();
        mLayoutBottom.setVisibility(View.GONE);
    }

    //初始化头部
    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.iv_head_right)
    ImageView mIvHeadRight;
    @BindView(R.id.iv_head_right2)
    ImageView mIvHeadRight2;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHeadLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle.setText("缓存商品");
    }

    //初始化搜索
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.layout_search)
    View mLayoutSearch;
    @BindView(R.id.view_search)
    View mViewSearch;
    private void initSearch() {
        mLayoutSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchStr = mEtSearch.getText().toString().trim();
                if (MyStringUtil.isEmpty(searchStr)) {
                    ToastUtils.showCustomToast("请输入关键字");
                    return;
                }
                setRequestType(4, null);
                getP().queryCacheWare(context,searchStr, null, pageNo, pageSize);
                mTreeAdapter.setmId(-1);//默认值
                mTreeAdapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * 适配器
     */
    // 左边的listView:树形列表
    @BindView(R.id.id_tree)
    ListView mTreeListView;
    private WareTreeAdapter<TreeBean> mTreeAdapter;
    private List<TreeBean> mTreeList = new ArrayList<TreeBean>();
    @BindView(R.id.rv_right)
    RecyclerView mRvRight;
    private WareAdapter mWareAdapter;
    private void initAdapter() {
        mRvRight.setHasFixedSize(true);
        mRvRight.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRvRight.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_0_5)
                .build());
        mWareAdapter = new WareAdapter();
        mWareAdapter.openLoadAnimation();
        mRvRight.setAdapter(mWareAdapter);
    }

    /**
     * 初始化刷新控件
     */
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    private int pageNo = 1;
    private int pageSize = 10;
    private String mWareType;
    private void initRefresh(){
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNo = 1;
                String search = mEtSearch.getText().toString().trim();
                getP().queryCacheWare(context, search, mWareType, pageNo, pageSize);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo ++;
                String search = mEtSearch.getText().toString().trim();
                getP().queryCacheWare(context, search, mWareType, pageNo, pageSize);
            }
        });
    }

    //1:常售 2：收藏  3：分类   4：搜索(默认)
    private void setRequestType(int type, String wareType){
        pageNo = 1;
        mWareType = wareType;
    }


    //TODO ********************************************接口回调处理**********************************************************
    /**
     * 刷新左边Tree
     */
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
                                //ids:包括下级的
                                String ids = String.valueOf(node.getId());
                                List<Node> children = node.getChildren();
                                if(children != null && children.size() > 0){
                                    for (Node node2:children ) {
                                        ids += "," + node2.getId();
                                    }
                                }
                                setRequestType(3, String.valueOf(ids));
                                getP().queryCacheWare(context, "", String.valueOf(ids), pageNo, pageSize);
                                break;
                        }
                    }
                });
            } else {
                mTreeAdapter.setNodes(this.mTreeList, 0);
                mTreeAdapter.notifyDataSetChanged();
            }
        } catch (IllegalAccessException e) {
        }
    }

    /**
     * 刷新右边adapter
     */
    public void refreshAdapterRight(List<Data> list, boolean editPrice) {
        if(null == list){
            return;
        }
        if(pageNo == 1){
            //上拉刷新
            mWareAdapter.setNewData(list);
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setNoMoreData(false);
        }else{
            //加载更多
            mWareAdapter.addData(list);
            mRefreshLayout.setNoMoreData(false);
            mRefreshLayout.finishLoadMore();
        }
        if(list.size() < 10){
            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }

    }



}
