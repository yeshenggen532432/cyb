package com.qwb.view.customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.event.ChooseCustomerEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.ToastUtils;
import com.qwb.view.customer.adapter.ChooseMineClientAdapter;
import com.qwb.view.tree.SimpleTreeAdapter_kh;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.customer.parsent.PChooseCustomer;
import com.qwb.utils.MyMapUtil;
import com.qwb.view.customer.model.MineClientInfo;
import com.qwb.utils.MyStringUtil;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.qwb.widget.treedialog.MyTreeDialog;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zyyoona7.lib.EasyPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 创建描述：选择客户
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class ChooseCustomerActivity extends XActivity<PChooseCustomer> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_choose_customer;
    }

    @Override
    public PChooseCustomer newP() {
        return new PChooseCustomer();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        initLocation();
        createPopupClient();
    }

    public void queryData(){
        String search = mEtSearch.getText().toString().trim();
        getP().queryDataMine(context, pageNoMine, mLatitude, mLongitude, search,clientTypeStr,clientLevelStr, mRegionIds);
        getP().queryDataNear(context, pageNoNear, mLatitude, mLongitude, search,clientTypeStr,clientLevelStr, mRegionIds);
    }

    /**
     * 初始化Intent
     */
    public static int type = 1;//1：拜访客户下单 2:单独下单(电话下单) 3：订货下单模块（列表）4：退货;5：退货下单（列表），6:车销下单
    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null){
            type = intent.getIntExtra(ConstantUtils.Intent.TYPE, 1);
        }
    }

    /**
     * 初始化UI
     */
    @BindView(R.id.parent)
    View parent;
    private void initUI() {
        initHead();
        initScreening();
        initAdapterMine();
        initRefreshMine();
        initAdapterNear();
        initRefreshNear();
    }

    /**
     * 初始化筛选:排序，人员，搜索
     */
    LinearLayout mRlSearch;
    EditText mEtSearch;
    TextView mTvSearch;
    TextView mTvFrameClient;
    @BindView(R.id.tv_region)
    TextView mTvRegion;
    private void initScreening() {
        mTvSearch = findViewById(R.id.tv_search);//搜索
        mTvFrameClient = findViewById(R.id.tv_frame_client);//客户等级，客户类型
        mRlSearch = findViewById(R.id.rl_search);
        mEtSearch = findViewById(R.id.et_search);
        mEtSearch.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable input) {
                if(MyStringUtil.isEmpty(input.toString().trim())){
                    mTvSearch.setTextColor(getResources().getColor(R.color.gray_6));
                }else{
                    mTvSearch.setTextColor(getResources().getColor(R.color.yellow));
                }
            }
        });
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRlSearch.getVisibility()== View.VISIBLE){
                    mRlSearch.setVisibility(View.GONE);
                    mEtSearch.setText("");
                }else{
                    mRlSearch.setVisibility(View.VISIBLE);
                }
            }
        });
       findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               pageNoMine = 1;
               pageNoNear = 1;
               queryData();
           }
       });
        mTvFrameClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClientDatas!=null && mClientDatas.size()>0){
                    //公用的ids先清空；再重新赋值
                    Constans.ziTrueMap.clear();
                    Constans.ParentTrueMap2.clear();
                    Constans.ziTrueMap.putAll(ziTrueMapClient);
                    Constans.ParentTrueMap2.putAll(ParentTrueMap2Client);
                    refreshAdapterFrameClient(mClientDatas,mClientMap,false);
                    mEasyPopClient.showAtLocation(parent, Gravity.CENTER,0,0);
                }else{
                    //公用的ids先清空；再重新赋值
                    Constans.ziTrueMap.clear();
                    Constans.ParentTrueMap2.clear();
                    getP().loadDataFrameClient(context);
                }
            }
        });
        //客户所属区域
        mTvRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null == mTreeRegion || mTreeRegion.isEmpty()){
                    getP().queryRegionTree(context);
                }else{
                    showDialogRegion(mTreeRegion);
                }
            }
        });
    }

    /**
     * 初始化定位
     */
    private double mLatitude;
    private double mLongitude;
    private void initLocation() {
        MyMapUtil.getInstance().getLocationClient(context)
                .setOnLocationListener(new MyMapUtil.OnLocationListener() {
                    @Override
                    public void setOnSuccessListener(BDLocation bdLocation) {
                        mLatitude = bdLocation.getLatitude();
                        mLongitude = bdLocation.getLongitude();
                        queryData();
                    }

                    @Override
                    public void setErrorListener() {
                        ToastUtils.showCustomToast("定位失败请重新定位");
                    }

                    @Override
                    public void setAddressListener(String addressStr) {

                    }
                });
    }

    /**
     * 初始化UI-头部
     */
    TextView mTvHeadRightAdd;//新增
    @BindView(R.id.layout_near)
    View mLayoutNear;
    @BindView(R.id.layout_mine)
    View mLayoutMine;
    private void initHead() {
        StatusBarUtil.setColor(context, getResources().getColor(R.color.blue), ConstantUtils.STATUSBAR_ALPHA);//设置状态栏颜色
        findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadRightAdd = findViewById(R.id.tv_head_right_add);
        mTvHeadRightAdd.setText("确定");
        mTvHeadRightAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(ConstantUtils.Intent.REQUEST_CODE_CHOOSE_CUSTOMER, intent);
                ActivityManager.getInstance().closeActivity(context);
                BusProvider.getBus().post(new ChooseCustomerEvent());
            }
        });

        RadioGroup tabGroup = findViewById(R.id.rg_client_manager);
        tabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_near:// 周边客户
                        mLayoutNear.setVisibility(View.VISIBLE);
                        mLayoutMine.setVisibility(View.GONE);
                        break;
                    case R.id.rb_mine:// 我的客户
                        mLayoutMine.setVisibility(View.VISIBLE);
                        mLayoutNear.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    /**
     * 初始化适配
     */
    RecyclerView mRvMine;
    ChooseMineClientAdapter mMineAdapter;
    private int pageNoMine = 1;
    private void initAdapterMine() {
        mRvMine = findViewById(R.id.rv_mine);
        mRvMine.setHasFixedSize(true);
        mRvMine.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRvMine.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_5)
                .build());
        mMineAdapter = new ChooseMineClientAdapter();
        mMineAdapter.openLoadAnimation();
        mRvMine.setAdapter(mMineAdapter);
        //item点击事件
        mMineAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MineClientInfo bean = (MineClientInfo) adapter.getData().get(position);

                boolean flag = false;
                for (MineClientInfo mineClientInfo : ConstantUtils.selectCustomerList) {
                    if(String.valueOf(mineClientInfo.getId()).equals(String.valueOf(bean.getId()))){
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    ConstantUtils.selectCustomerList.add(bean);
                }else{
                    ConstantUtils.selectCustomerList.remove(bean);
                }
                mMineAdapter.setSelectList(ConstantUtils.selectCustomerList);
                mMineAdapter.notifyDataSetChanged();
                mNearAdapter.setSelectList(ConstantUtils.selectCustomerList);
                mNearAdapter.notifyDataSetChanged();
            }
        });
        //子item点击事件
        mMineAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
    }

    /**
     * 初始化刷新控件（订货）
     */
    RefreshLayout mRefreshLayoutMine;
    private void initRefreshMine(){
        mRefreshLayoutMine =  findViewById(R.id.refreshLayout_mine);
        mRefreshLayoutMine.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNoMine = 1;
                String search = mEtSearch.getText().toString().trim();
                getP().queryDataMine(context, pageNoMine, mLatitude, mLongitude, search,clientTypeStr,clientLevelStr, mRegionIds);
            }
        });
        mRefreshLayoutMine.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNoMine ++;
                String search = mEtSearch.getText().toString().trim();
                getP().queryDataMine(context, pageNoMine, mLatitude, mLongitude, search,clientTypeStr,clientLevelStr, mRegionIds);
            }
        });
    }

    /**
     * 初始化适配器（退货）
     */
    @BindView(R.id.rv_near)
    RecyclerView mRvNear;
    ChooseMineClientAdapter mNearAdapter;
    private int pageNoNear = 1;
    private void initAdapterNear() {
        mRvNear.setHasFixedSize(true);
        mRvNear.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRvNear.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_5)
                .build());
        mNearAdapter = new ChooseMineClientAdapter();
        mNearAdapter.openLoadAnimation();
        mRvNear.setAdapter(mNearAdapter);
        //item点击事件
        mNearAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MineClientInfo bean = (MineClientInfo) adapter.getData().get(position);

                boolean flag = false;
                for (MineClientInfo mineClientInfo : ConstantUtils.selectCustomerList) {
                    if(String.valueOf(mineClientInfo.getId()).equals(String.valueOf(bean.getId()))){
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    ConstantUtils.selectCustomerList.add(bean);
                }else{
                    ConstantUtils.selectCustomerList.remove(bean);
                }
                mMineAdapter.setSelectList(ConstantUtils.selectCustomerList);
                mMineAdapter.notifyDataSetChanged();
                mNearAdapter.setSelectList(ConstantUtils.selectCustomerList);
                mNearAdapter.notifyDataSetChanged();
            }
        });
        //子item点击事件
        mNearAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
    }

    /**
     * 初始化刷新控件（周边客户）
     */
    RefreshLayout mRefreshLayoutNear;
    private void initRefreshNear(){
        mRefreshLayoutNear = findViewById(R.id.refreshLayout_near);
        mRefreshLayoutNear.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNoNear = 1;
                String search = mEtSearch.getText().toString().trim();
                getP().queryDataNear(context, pageNoNear, mLatitude, mLongitude, search,clientTypeStr,clientLevelStr,mRegionIds);
            }
        });
        mRefreshLayoutNear.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNoNear ++;
                String search = mEtSearch.getText().toString().trim();
                getP().queryDataNear(context, pageNoNear, mLatitude, mLongitude, search,clientTypeStr,clientLevelStr,mRegionIds);
            }
        });
    }

    /**
     * 刷新适配器-我的客户
     */
    public void refreshAdapterMine(List<MineClientInfo> dataList){
        if(!(dataList!=null && dataList.size()>0)){
            mRefreshLayoutMine.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }
        mMineAdapter.setSelectList(ConstantUtils.selectCustomerList);
        if(pageNoMine == 1){
            //上拉刷新
            mMineAdapter.setNewData(dataList);
            mRefreshLayoutMine.finishRefresh();
            mRefreshLayoutMine.setNoMoreData(false);
        }else{
            //加载更多
            mMineAdapter.addData(dataList);
            mRefreshLayoutMine.finishLoadMore();
        }
    }

    /**
     * 刷新适配器-周边客户
     */
    public void refreshAdapterNear(List<MineClientInfo> dataList){
        if(!(dataList!=null && dataList.size()>0)){
            mRefreshLayoutNear.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }
        mMineAdapter.setSelectList(ConstantUtils.selectCustomerList);
        if(pageNoNear == 1){
            //上拉刷新
            mNearAdapter.setNewData(dataList);
            mRefreshLayoutNear.finishRefresh();
            mRefreshLayoutNear.setNoMoreData(false);
        }else{
            //加载更多
            mNearAdapter.addData(dataList);
            mRefreshLayoutNear.finishLoadMore();
        }
    }

    /**
     * tree：客户等级，客户类型
     */
    private ListView mClientTree;
    private EasyPopup mEasyPopClient;
    public void createPopupClient() {
        mEasyPopClient = new EasyPopup(context)
                .setContentView(R.layout.x_popup_frame)
                .createPopup();
        mClientTree=mEasyPopClient.getView(R.id.id_tree);
        TextView tvTitle=mEasyPopClient.getView(R.id.tv_title);
        tvTitle.setText("客户类型和客户等级");
        // 重置
        mEasyPopClient.getView(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constans.ziTrueMap.clear();
                Constans.ParentTrueMap2.clear();
                refreshAdapterFrameClient(mClientDatas,mClientMap,false);
            }
        });
        //ok
        mEasyPopClient.getView(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEasyPopClient.dismiss();
                onConfirmFrameClient();
            }
        });
    }

    /**
     * 刷新适配器-结构图：客户类型，等级
     */
    List<TreeBean> mClientDatas =new ArrayList<>();
    SimpleTreeAdapter_kh<TreeBean> mClientAdapter;
    HashMap<Integer, String> mClientMap = new HashMap<Integer, String>();
    public HashMap<Integer, Boolean> ziTrueMapClient=new HashMap<>();//保存商品是否选中
    public HashMap<Integer, Integer> ParentTrueMap2Client=new HashMap<>();//部门：0没选中，1全选中，2：部分选中
    public void refreshAdapterFrameClient(List<TreeBean> mDatas, HashMap<Integer, String> map, boolean isShowPop){
        this.mClientDatas =mDatas;
        this.mClientMap =map;
        if ((mDatas != null && mDatas.size() > 0)) {
            if (mClientAdapter == null) {
                try {
                    mClientAdapter = new SimpleTreeAdapter_kh<TreeBean>(mClientTree,context, mClientDatas, 0);
                    mClientTree.setAdapter(mClientAdapter);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                mClientAdapter.notifyDataSetChanged();
            }
            //是否弹窗
            if(isShowPop){
                mEasyPopClient.showAtLocation(parent,Gravity.CENTER,0,0);
            }
        }
    }

    private String clientLevelStr;
    private String clientTypeStr;
    private List<String> mClientTypeList = new ArrayList<>();
    private List<String> mClientLevelList = new ArrayList<>();
    private void onConfirmFrameClient() {
        mClientTypeList.clear();// 客户类型
        mClientLevelList.clear();// 客户等级
        Iterator<Integer> iter = Constans.ziTrueMap.keySet().iterator();
        while (iter.hasNext()) {
            Integer key = iter.next();
            Boolean val = Constans.ziTrueMap.get(key);
            if (val) {
                String vuals = mClientMap.get(key);
                if (key > ConstantUtils.TREE_ID) {
                    mClientLevelList.add(vuals);
                } else {
                    mClientTypeList.add(vuals);
                }
            }
        }
        String levelStr = JSON.toJSONString(mClientLevelList);
        String typeStr = JSON.toJSONString(mClientTypeList);
        clientLevelStr = levelStr.substring(1, levelStr.length() - 1);//去掉[]
        clientTypeStr = typeStr.substring(1, typeStr.length() - 1);//去掉[]
        mRefreshLayoutMine.autoRefresh();
        mRefreshLayoutNear.autoRefresh();
        //公用的ids赋值给临时的ids
        ziTrueMapClient.clear();
        ParentTrueMap2Client.clear();
        ziTrueMapClient.putAll(Constans.ziTrueMap);
        ParentTrueMap2Client.putAll(Constans.ParentTrueMap2);
        if(ziTrueMapClient!=null && ziTrueMapClient.size()>0){
            mTvFrameClient.setTextColor(getResources().getColor(R.color.yellow));
        }else{
            mTvFrameClient.setTextColor(getResources().getColor(R.color.gray_6));
        }
    }

    /**
     * 客户所属区域
     */
    private List<TreeBean> mTreeRegion = new ArrayList<>();
    private MyTreeDialog mTreeRegionDialog;
    private String mRegionIds;
    public void showDialogRegion(List<TreeBean> mDatas){
        mTreeRegion.clear();
        mTreeRegion.addAll(mDatas);
        if(null == mTreeRegionDialog){
            mTreeRegionDialog = new MyTreeDialog(context, mTreeRegion, true);
        }
        mTreeRegionDialog.title("客户所属区域").show();
        mTreeRegionDialog.setOnClickListener(new MyTreeDialog.OnClickListener() {
            @Override
            public void onOkListener(String checkIds, String clientTypeIds,Map<Integer, Integer> checkMap) {
                mRegionIds = clientTypeIds;
                if(!MyStringUtil.isEmpty(mRegionIds)){
                    mTvRegion.setTextColor(getResources().getColor(R.color.yellow));
                }else{
                    mTvRegion.setTextColor(getResources().getColor(R.color.gray_6));
                }
                queryData();
            }
        });
    }

}
