package com.qwb.view.ware.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.scanlibrary.ScanActivity;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.ware.adapter.WareManagerAdapter;
import com.qwb.view.ware.adapter.WareTreeAdapter;
import com.qwb.view.ware.parsent.PWareManager;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.step.model.ShopInfoBean.Data;
import com.qwb.view.base.model.TreeBean;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhy.tree.bean.Node;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 创建描述：商品管理 (备注这边是过滤非公司商品分类及商品)
 * 修改描述：
 */
public class WareManagerActivity extends XActivity<PWareManager> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_ware_manager;
    }

    @Override
    public PWareManager newP() {
        return new PWareManager();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        //备注：商品管理没有过滤非公司产品
        getP().queryDataCompanyWareTypes(context, noCompany, isType);//获得商品类型列表
    }

    private String noCompany = "";//过滤非公司产品:1-包括非公司；0或空-公司的
    private ArrayList<Data> mDatas = new ArrayList<>();
    private String isType = "0";//0:库存商品，1：原辅材料类 2.低值易耗品类  3.固定资产类

    /**
     * 初始化UI
     */
    @BindView(R.id.parent)
    LinearLayout parent;
    @BindView(R.id.tv_sum_choose_shop)
    TextView mTvSum;
    @BindView(R.id.tv_confirm_choose_shop)
    TextView mTvConfirm;
    private void initUI() {
        initHead();
        initAdapter();
        initRefresh();
        initStorage0();
        initScreening();
    }

    /**
     * 库存商品类等等；搜索
     */
    LinearLayout mLayoutSearch;
    EditText mEtSearch;
    TextView mIvSearch;
    TextView mTvIsTypeName;
    private void initScreening() {
        mLayoutSearch = findViewById(R.id.rl_search);
        mEtSearch = findViewById(R.id.et_search);
        mIvSearch = findViewById(R.id.iv_search);
        mTvIsTypeName = findViewById(R.id.tv_isType_name);
        TextView mTvSearch = findViewById(R.id.tv_search);
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLayoutSearch.getVisibility()== View.VISIBLE){
                    mLayoutSearch.setVisibility(View.GONE);
                    mEtSearch.setText("");
                }else{
                    mLayoutSearch.setVisibility(View.VISIBLE);
                }
            }
        });
        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchStr = mEtSearch.getText().toString().trim();
                if (MyStringUtil.isEmpty(searchStr)) {
                    ToastUtils.showCustomToast("请输入关键字");
                    return;
                }
                setRequestType(4, null);
                getP().getDataKeyWordGoodsList(context, searchStr, noCompany, pageNo, pageSize);
                mTreeAdapter.setmId(-1);//默认值
                mTreeAdapter.notifyDataSetChanged();
            }
        });
        mTvIsTypeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogIsType();
            }
        });
    }

    //初始化头部
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
        mHeadLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle.setText("商品管理");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_17), (int) getResources().getDimension(R.dimen.dp_17));
        mIvHeadRight.setLayoutParams(params);
        mIvHeadRight.setImageResource(R.mipmap.ic_jia_gray_333);
        mIvHeadRight2.setLayoutParams(params);
        mIvHeadRight2.setImageResource(R.mipmap.ic_scan_gray_33);
        mHeadRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().jumpToAddWareActivity(context, ConstantUtils.Menu.INT_ADD);
            }
        });
        mHeadRight2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().jumpScanActivity(context, false);//扫描
            }
        });
    }


    @BindView(R.id.layout_bottom)
    View mLayoutBottom;
    private void initStorage0() {
        mLayoutBottom.setVisibility(View.GONE);
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
    private WareManagerAdapter mWareAdapter;
    private void initAdapter() {
        mRvRight.setHasFixedSize(true);
        mRvRight.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRvRight.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_0_5)
                .build());
        mWareAdapter = new WareManagerAdapter();
        mWareAdapter.openLoadAnimation();
        mRvRight.setAdapter(mWareAdapter);
        mWareAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Data data = (Data) adapter.getData().get(position);
                Router.newIntent(context)
                        .to(WareEditActivity.class)
                        .putString(ConstantUtils.Intent.ID, "" + data.getWareId())
                        .putInt(ConstantUtils.Intent.TYPE, ConstantUtils.Menu.INT_UPDATE)
                        .launch();
            }
        });
    }

    /**
     * 初始化刷新控件
     */
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    private int pageNo = 1;
    private int pageSize = 10;
    private int mRequestType = 3;//1:常售 2：收藏  3：分类   4：搜索(默认)
    private String mWareType;
    private void initRefresh(){
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNo = 1;
                String search = mEtSearch.getText().toString().trim();
                //1:常售 2：收藏  3：分类  4：搜索
                switch (mRequestType){
                    case 3:
                        getP().getDataTypeGoodsList(context, mWareType, noCompany, pageNo, pageSize);
                        break;
                    case 4:
                        getP().getDataKeyWordGoodsList(context, search, noCompany, pageNo, pageSize);
                        break;
                }
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo ++;
                String search = mEtSearch.getText().toString().trim();
                //1:常售 2：收藏  3：分类   4：搜索
                switch (mRequestType){
                    case 3:
                        getP().getDataTypeGoodsList(context, mWareType, noCompany, pageNo, pageSize);
                        break;
                    case 4:
                        getP().getDataKeyWordGoodsList(context, search, noCompany, pageNo, pageSize);
                        break;
                }
            }
        });
    }


    /**
     * 点击事件,R.id.iv_search,R.id.tv_head_right
     */
    @OnClick({R.id.tv_confirm_choose_shop})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm_choose_shop:
                Intent intent = new Intent();
                intent.putExtra(ConstantUtils.Intent.EDIT_PRICE, mEditPrice);
                intent.putParcelableArrayListExtra(ConstantUtils.Intent.CHOOSE_WARE,mDatas);
                setResult(ConstantUtils.Intent.RESULT_CODE_CHOOSE_GOODS, intent);
                ActivityManager.getInstance().closeActivity(context);
                break;
        }
    }

    //TODO ********************************************接口回调处理**********************************************************
    /**
     * 刷新左边Tree
     */
    public void refreshAdapterTree(List<TreeBean> treeList) {
        //添加第一个商品分类
        if(treeList !=null && !treeList.isEmpty()){
            int id = treeList.get(0).get_id();
            setRequestType(3, String.valueOf(id));
            getP().getDataTypeGoodsList(context, String.valueOf(id), noCompany, pageNo, pageSize);
        }else{
            List<Data> list = new ArrayList<>();
            refreshAdapterRight(list, mEditPrice);
        }
        try {
            this.mTreeList = treeList;
            if (mTreeAdapter == null) {
                mTreeAdapter = new WareTreeAdapter<>(mTreeListView, context, this.mTreeList, 0);
                mTreeListView.setAdapter(mTreeAdapter);
                if(!MyStringUtil.isEmpty(mWareType)){
                    mTreeAdapter.setmId(Integer.valueOf(mWareType));
                }
                mTreeAdapter.setOnTreeNodeClickListener(new WareTreeAdapter.OnTreeNodeClickListener() {
                    @Override
                    public void onClick(Node node, int i) {
                        mTreeAdapter.setmId(node.getId());
                        mTreeAdapter.notifyDataSetChanged();
                        switch (node.getId()) {
                            default:// 根据类型查询商品
                                setRequestType(3, String.valueOf(node.getId()));
                                getP().getDataTypeGoodsList(context, String.valueOf(node.getId()), noCompany, pageNo, pageSize);
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
    //1:常售 2：收藏  3：分类   4：搜索(默认)
    private void setRequestType(int type, String wareType){
        mRequestType = type;
        pageNo = 1;
        mWareType = wareType;
    }

    /**
     * 刷新右边adapter
     */
    private boolean mEditPrice = true;//价格是否可以编辑(默认true)

    public void refreshAdapterRight(List<Data> list, boolean editPrice) {
        this.mEditPrice = editPrice;

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
        if(list.size() < pageSize){
            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }

    }


    /**
     * 库存商品类
     * 原辅材料类
     * 低值易耗品类
     * 固定资产类
     */
    public void showDialogIsType(){
        //0:库存商品类，1：原辅材料类 2.低值易耗品类  3.固定资产类
        final ArrayList<DialogMenuItem> baseItems = new ArrayList<>();
        baseItems.add(new DialogMenuItem("库存商品类", 0));
        baseItems.add(new DialogMenuItem("原辅材料类", 1));
        baseItems.add(new DialogMenuItem("低值易耗品类", 2));
        baseItems.add(new DialogMenuItem("固定资产类", 3));
        NormalListDialog dialog = new NormalListDialog(context, baseItems);
        dialog.show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogMenuItem item = baseItems.get(position);
                mTvIsTypeName.setText(item.mOperName);
                getP().queryDataCompanyWareTypes(context, noCompany, "" + item.mResId);
            }
        });
    }


    //TODO******************************扫描:start************************************
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //扫描二维码/条码回传
            if (data != null && RESULT_OK == resultCode && requestCode == ConstantUtils.Intent.REQUEST_CODE_SCAN) {
                String result = data.getStringExtra(ScanActivity.SCAN_RESULT);
                if (MyStringUtil.isNotEmpty(result)) {
                    mLayoutSearch.setVisibility(View.VISIBLE);
                    setRequestType(4, null);
                    mEtSearch.setText(result);
                    mEtSearch.setSelection(result.length());
                    getP().getDataKeyWordGoodsList(context, result, noCompany, pageNo, pageSize);
                } else {
                    ToastUtils.showCustomToast("扫描内容为空");
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }
    //TODO******************************扫描:end************************************











}
