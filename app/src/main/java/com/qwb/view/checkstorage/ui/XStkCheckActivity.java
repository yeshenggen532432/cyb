package com.qwb.view.checkstorage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.scanlibrary.ScanActivity;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.qwb.event.CrashEvent;
import com.qwb.view.checkstorage.doActivity.DoStkCheck;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.widget.MyBluetoothScanDialog;
import com.qwb.event.StkCheckEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.view.stk.StorageBean;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.checkstorage.adapter.StkCheckAdapter;
import com.qwb.view.checkstorage.model.StkCheckDetailBean;
import com.qwb.view.checkstorage.model.StkCheckWareBean;
import com.qwb.view.checkstorage.parsent.PxStkCheck;
import com.qwb.view.common.ui.XScanActivity;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.widget.MyCrashHandler;
import com.chiyong.t3.R;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.functions.Consumer;
import km.lmy.searchview.SearchBean;
import km.lmy.searchview.SearchView;

/**
 * 盘点仓库(采用Do)
 */
public class XStkCheckActivity extends XActivity<PxStkCheck> {

    private DoStkCheck doStkCheckActivity = new DoStkCheck();
    //头部
    @BindView(R.id.head_left)
    public View mHeadLeft;
    @BindView(R.id.head_right)
    public View mHeadRight;
    @BindView(R.id.head_right2)
    public View mHeadRight2;
    @BindView(R.id.head_right3)
    public View mHeadRight3;
    @BindView(R.id.iv_head_right2)
    public ImageView mIvHeadRight2;
    @BindView(R.id.iv_head_right3)
    public ImageView mIvHeadRight3;
    @BindView(R.id.tv_head_right)
    public TextView mTvHeadRight;
    @BindView(R.id.tv_head_title)
    public TextView mTvHeadTitle;
    //适配器
    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;
    public StkCheckAdapter mAdapter;
    public View mFooterView;
    //另外（选择仓库，删除所有商品，选择商品，未盘点比对）
    @BindView(R.id.tv_Storage)
    public TextView mTvStorage;
    @BindView(R.id.layout_Storage)
    public View mViewStorage;
    @BindView(R.id.layout_delete_all)
    public View mViewDeleteAll;
    @Override
    public int getLayoutId() {
        return R.layout.x_activity_check_storage;
    }

    @Override
    public PxStkCheck newP() {
        return new PxStkCheck();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void bindEvent() {
        BusProvider.getBus()
                .toFlowable(CrashEvent.class)
                .subscribe(new Consumer<CrashEvent>() {
                    @Override
                    public void accept(CrashEvent applyEvent){
                        if (applyEvent.getTag() == ConstantUtils.Event.TAG_CRASH) {
                            isSaveCache = true;
                            doStkCheckActivity.doCacheDate();
                        }
                    }
                });
    }


    public void initData(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new MyCrashHandler());//捕获异常
        doStkCheckActivity.doInitActivity(this);
        initIntent();
        initUI();
        doIntent();
        getP().queryToken(null);
    }

    public Integer mCurrentPosition;
    public StkCheckWareBean mCurrentBean;
    public int mBillId;//盘点单id
    public int mType = 1;//1盘点开单（添加）；2盘点开单（修改）；3多人盘点开单（添加）；4多人盘点开单（修改）
    public int mStkId;//仓库id
    public String mStkName;//仓库名
    private void initIntent() {
        Intent intent = getIntent();
        if (null != intent) {
            mBillId = intent.getIntExtra(ConstantUtils.Intent.ID, 0);
            mType = intent.getIntExtra(ConstantUtils.Intent.TYPE, 1);
        }
    }

    private void doIntent() {
        if (mBillId > 0) {
            getP().queryStkCheck(context, mBillId, mType);
        } else {
            //读缓存数据
            doStkCheckActivity.doGetCacheData();
        }
    }

    private void initUI() {
        initHead();
        initAdapter();
        initOther();
        initSearchView();
    }

    /**
     * 头部
     */
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        doStkCheckActivity.doInitHead();
        //关闭
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        //蓝牙扫描枪
        mHeadRight3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!doStkCheckActivity.doVerifyStkId()) return;//验证仓库
                showDialogBluetoothScan();
            }
        });
        //手机多次扫描
        mHeadRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doStkCheckActivity.doClickHeadRight2();
            }
        });
        //提交前：验证数据的合法性
        mHeadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doStkCheckActivity.doVerifySubmitData()) {
                    showDialogSave();
                }
            }
        });
    }


    /**
     * 适配器
     */
    private void initAdapter() {
        mFooterView =  LayoutInflater.from(context).inflate(R.layout.x_layout_check_storage_footer, null);
        mAdapter = new StkCheckAdapter(context);
        doStkCheckActivity.doInitAdapter(context, mRecyclerView, mAdapter, mFooterView);
        //添加一行(空商品)
        mFooterView.findViewById(R.id.bottom_add_line).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doStkCheckActivity.doClickAddRow();
            }
        });

        //子控件点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    doStkCheckActivity.doCurrentBean(position, (StkCheckWareBean)adapter.getData().get(position));
                    switch (view.getId()) {
                        case R.id.item_tv_name: //打开搜索
                            if (!doStkCheckActivity.doVerifyStkId()) return;//验证仓库
                            mSearchView.open();
                            break;
                        case R.id.item_layout_ddd://单个扫描
                            if (!doStkCheckActivity.doVerifyStkId()) return;//验证仓库
                            ActivityManager.getInstance().jumpXScanActivity(context, false, mStkId);
                            break;
                        case R.id.right://删除
                            doStkCheckActivity.showDialogDel();
                            break;
                    }
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });

    }

    /**
     * 另外（选择仓库，删除所有商品，选择商品，未盘点比对）
     */
    private void initOther() {
        //选择仓库
        mViewStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doChooseStk();
            }
        });
        //删除全部商品
        mViewDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doStkCheckActivity.doClickDelAll();
            }
        });
        //添加商品
        findViewById(R.id.layout_add_ware).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doStkCheckActivity.doClickChooseWare();
            }
        });
        //未盘点比对（仓库商品）
        findViewById(R.id.layout_stk_ware).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doStkCheckActivity.doClickStkWare();
            }
        });
    }


    //搜索控件
    @BindView(R.id.searchView)
    SearchView mSearchView;
    private void initSearchView() {
        mSearchView.setOnSearchListener(new SearchView.OnSearchListener() {
            @Override
            public void onSearchListener(String searchText) {
                getP().getDataKeyWordGoodsList(context, searchText, mStkId);
            }
        });
        //设置软键盘搜索按钮点击事件
        mSearchView.setOnSearchActionListener(new SearchView.OnSearchActionListener() {
            @Override
            public void onSearchAction(String searchText) {
                getP().getDataKeyWordGoodsList(context, searchText, mStkId);
            }
        });
        //设置历史记录点击事件
        mSearchView.setHistoryItemClickListener(new SearchView.OnHistoryItemClickListener() {
            @Override
            public void onClick(SearchBean data, int position) {
                for (ShopInfoBean.Data ware : mWareList) {
                    if (data.getId() == ware.getWareId()) {
                        doStkCheckActivity.doSelectWare(ware);
                        mSearchView.close();
                        mSearchBeanList.clear();
                        break;
                    }
                }
            }
        });
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
                    ArrayList<ShopInfoBean.Data> resultList = data.getParcelableArrayListExtra(XScanActivity.SCAN_RESULT_LIST);
                    if (!TextUtils.isEmpty(result)) {
                        getP().getWareByScan(context, result, mStkId, false);
                    }
                    if (null != resultList && !resultList.isEmpty()) {
                        doStkCheckActivity.doSelectWareList(resultList, true);
                    }
                }
            }
            //选择商品
            if (requestCode == ConstantUtils.Intent.REQUEST_STEP_5_CHOOSE_GOODS && resultCode == ConstantUtils.Intent.RESULT_CODE_CHOOSE_GOODS) {
                if (data != null) {
                    ArrayList<ShopInfoBean.Data> datas = data.getParcelableArrayListExtra(ConstantUtils.Intent.CHOOSE_WARE);
                    if (null != datas && !datas.isEmpty()) {
                        doStkCheckActivity.doSelectWareList(datas, true);
                    }
                }
            }

        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //TODO******************************扫描:end************************************
    //选择仓库
    public void doChooseStk(){
        if (doStkCheckActivity.doVerifyStkId()) {
            showDialogStorageTip();
        } else {
            if (MyCollectionUtil.isEmpty(storageList)) {
                getP().queryDataStorageList(context);
            } else {
                showDialogStorage(storageList);
            }
        }
    }

    /**
     * 重新选择仓库提示
     */
    public void showDialogStorageTip() {
        try {
            NormalDialog dialog = new NormalDialog(context);
            dialog.content("重新选择仓库后，已选择的商品会清空").show();
            dialog.setOnBtnClickL(null, new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    if (MyCollectionUtil.isEmpty(storageList)) {
                        getP().queryDataStorageList(context);
                    } else {
                        showDialogStorage(storageList);
                    }
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    //对话框：保存数据-提交数据
    private void showDialogSave() {
        NormalDialog dialog = new NormalDialog(context);
        dialog.content("确定要保存吗？").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                try {
                    String wareStr = JSON.toJSONString(mAdapter.getData());
                    String staff = SPUtils.getSValues(ConstantUtils.Sp.USER_NAME);
                    String checkTimeStr = MyTimeUtils.getNowTime();
                    getP().addStkCheck(context, staff, mStkId, checkTimeStr, wareStr, mBillId, mType, mQueryToken);
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }

    public List<StorageBean.Storage> storageList = new ArrayList<>();//仓库列表
    public void showDialogStorage(List<StorageBean.Storage> storageList) {
        doStkCheckActivity.showDialogStorage(storageList);
    }


    //1）搜索商品-刷新搜索列表
    List<SearchBean> mSearchBeanList = new ArrayList<>();
    List<ShopInfoBean.Data> mWareList = new ArrayList<>();
    public void refreshSearchAdapter(List<ShopInfoBean.Data> list) {
        mWareList.clear();
        mWareList.addAll(list);
        mSearchBeanList.clear();
        if (null != list && !list.isEmpty()) {
            for (ShopInfoBean.Data bean : list) {
                SearchBean searchBean = new SearchBean();
                searchBean.setId(bean.getWareId());
                searchBean.setName(bean.getWareNm());
                mSearchBeanList.add(searchBean);
            }
            mSearchView.setNewHistoryList(mSearchBeanList);
        } else {
            mSearchView.setNewHistoryList(mSearchBeanList);
        }
    }

    //查询成功
    public void doStkCheckDetail(StkCheckDetailBean bean) {
        doStkCheckActivity.doStkCheckDetail(bean);
    }

    //添加盘点单成功
    public void doAddSuccess() {
        isSaveCache = false;
        ToastUtils.showCustomToast("保存成功");
        BusProvider.getBus().post(new StkCheckEvent());
        ActivityManager.getInstance().closeActivity(context);
    }

    //扫描-单个或多个
    public void doScanSuccess(List<ShopInfoBean.Data> list, boolean multiple) {
        doStkCheckActivity.doSelectWareList(list, multiple);
    }

    public boolean isSaveCache = true;//是否保存缓存；提交成功不保存
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doStkCheckActivity.doCacheDate();
    }

    /**
     * 蓝牙扫描枪
     */
    private  MyBluetoothScanDialog myBluetoothScanDialog;
    private void showDialogBluetoothScan() {
        if(myBluetoothScanDialog == null){
            myBluetoothScanDialog = new MyBluetoothScanDialog(context);
            myBluetoothScanDialog.show();
            myBluetoothScanDialog.setOnClickListener(new MyBluetoothScanDialog.OnSuccessListener() {
                @Override
                public void onSuccessListener(String result) {
                    if (MyStringUtil.isNotEmpty(result)) {
                        doStkCheckActivity.doCurrentBean(null, null);//扫描全部都是添加，没有做覆盖的
                        getP().getWareByScan(context, result, mStkId, false);
                    }
                }
            });
        }
    }


    /**
     * 避免重复的token
     */
    private String mQueryToken;
    public void doToken(String data) {
        mQueryToken = data;
    }

}
