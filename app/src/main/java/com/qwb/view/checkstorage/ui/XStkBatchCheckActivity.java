package com.qwb.view.checkstorage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.bluetooth.prt.HPRTHelper;
import com.example.scanlibrary.ScanActivity;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.common.StkOrderTypeEnum;
import com.qwb.db.DStkCheckWareBean;
import com.qwb.event.CrashEvent;
import com.qwb.utils.MyDataUtils;
import com.qwb.view.checkstorage.adapter.StkBatchCheckAdapter;
import com.qwb.view.checkstorage.model.WareProduceDateBean;
import com.qwb.view.checkstorage.parsent.PStkBatchCheck;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.widget.MyBluetoothScanDialog;
import com.qwb.event.StkCheckEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDoubleUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.checkstorage.model.StkCheckBean;
import com.qwb.view.checkstorage.model.StkCheckDetailBean;
import com.qwb.view.checkstorage.model.StkCheckWareBean;
import com.qwb.view.stk.StorageBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.view.common.ui.XScanActivity;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.widget.MyCrashHandler;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;
import km.lmy.searchview.SearchBean;
import km.lmy.searchview.SearchView;

/**
 * 批次盘点
 */
public class XStkBatchCheckActivity extends XActivity<PStkBatchCheck> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_check_storage;
    }

    @Override
    public PStkBatchCheck newP() {
        return new PStkBatchCheck();
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
                            doCacheDate();
                        }
                    }
                });
    }

    public void initData(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new MyCrashHandler());//捕获异常
        initIntent();
        initUI();
        doIntent();
        getP().queryToken(null);
    }

    private Integer mCurrentPosition;
    private StkCheckWareBean mCurrentBean;
    private int billId;//盘点单id
    private int type = StkOrderTypeEnum.ORDER_BATCH_ADD.getType();//5.批次盘点（添加）；6.批次盘点（修改）
    private Map<Integer, ShopInfoBean.Data> wareMap = new HashMap<>();

    private void initIntent() {
        Intent intent = getIntent();
        if (null != intent) {
            billId = intent.getIntExtra(ConstantUtils.Intent.ID, 0);
            type = intent.getIntExtra(ConstantUtils.Intent.TYPE, StkOrderTypeEnum.ORDER_BATCH_ADD.getType());
        }
    }

    private List<StkCheckWareBean> mDataList = new ArrayList<>();

    private void doIntent() {
        if (billId > 0) {
            getP().queryStkCheck(context, billId);
        } else {
//            //初始化适配器数据：默认一个没有商品的
//            mDataList.add(new StkCheckWareBean());
//            mTvStorage.setText(stkName);
//            mAdapter.setNewData(mDataList);

            //读缓存数据
            doGetCacheData();
        }
    }

    private void initUI() {
        initHead();
        initAdapter();
        initSearchView();
        initStorage();
        initOther();
    }

    //初始化仓库
    @BindView(R.id.tv_Storage)
    TextView mTvStorage;
    @BindView(R.id.layout_Storage)
    View mLayoutStorage;

    private void initStorage() {
        mLayoutStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyStkId()) {
                    showDialogStorageTip();
                } else {
                    if (null == storageList || storageList.isEmpty()) {
                        getP().queryDataStorageList(context);
                    } else {
                        showDialogStorage(storageList);
                    }
                }
            }
        });
    }

    private void initOther() {
        //删除全部
        findViewById(R.id.layout_delete_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDelAll();
            }
        });
        //添加商品
        findViewById(R.id.layout_add_ware).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!verifyStkId()) return;//验证仓库
                Map<Integer,Boolean>  tmpMap = new HashMap<>();
                ArrayList<ShopInfoBean.Data> dataList = new ArrayList<>();
                List<StkCheckWareBean> wareList = mAdapter.getData();
                if(MyCollectionUtil.isNotEmpty(wareList)){
                    for (StkCheckWareBean ware: wareList) {
                        //过滤tmpMap重复的id
                        if (MyStringUtil.isNotEmpty(ware.getWareId() + "") && !"0".equals(ware.getWareId()) && ( tmpMap.get(ware.getWareId()) == null || !tmpMap.get(ware.getWareId())) ){
                            tmpMap.put(ware.getWareId(), true);
                            ShopInfoBean.Data data = new ShopInfoBean.Data();
                            data.setWareId(ware.getWareId());
                            data.setHsNum(ware.getHsNum());
                            data.setStkQty(ware.getStkQty());
                            data.setMinStkQty(ware.getMinStkQty());
                            data.setInPrice2(ware.getZmPrice() + "");
                            data.setInPrice(ware.getPrice() + "");
                            dataList.add(data);
                        }
                    }
                }
                ActivityManager.getInstance().jumpToChooseWareActivity(context, stkId, dataList);
            }
        });
        //仓库商品
        findViewById(R.id.layout_stk_ware).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!verifyStkId()) return;//验证仓库
                    ArrayList<Integer> ids = new ArrayList<>();
                    List<StkCheckWareBean> data = mAdapter.getData();
                    for (StkCheckWareBean wareBean : data) {
                        Integer wareId = wareBean.getWareId();
                        if (wareId != null && wareId != 0) {
                            ids.add(wareId);
                        }
                    }
                    Router.newIntent(context)
                            .putInt(ConstantUtils.Intent.STK_ID, stkId)
                            .putString(ConstantUtils.Intent.NAME, stkName)
                            .putIntegerArrayList(ConstantUtils.Intent.IDS, ids)
                            .to(XStkWareActivity.class)
                            .launch();
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
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
                getP().getDataKeyWordGoodsList(context, searchText, stkId);
            }
        });
        //设置软键盘搜索按钮点击事件
        mSearchView.setOnSearchActionListener(new SearchView.OnSearchActionListener() {
            @Override
            public void onSearchAction(String searchText) {
                getP().getDataKeyWordGoodsList(context, searchText, stkId);
            }
        });
        //设置历史记录点击事件
        mSearchView.setHistoryItemClickListener(new SearchView.OnHistoryItemClickListener() {
            @Override
            public void onClick(SearchBean data, int position) {
                for (ShopInfoBean.Data ware : mWareList) {
                    if (data.getId() == ware.getWareId()) {
                        // doSelectWare(ware);
                        mSearchView.close();
                        mSearchBeanList.clear();
                        List<ShopInfoBean.Data> dataList = new ArrayList<>();
                        dataList.add(ware);
                        doChooseWareSuccess(dataList);
                        break;
                    }
                }
            }
        });
    }

    //头部
    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.head_right2)
    View mHeadRight2;
    @BindView(R.id.head_right3)
    View mHeadRight3;
    @BindView(R.id.iv_head_right2)
    ImageView mIvHeadRight2;
    @BindView(R.id.iv_head_right3)
    ImageView mIvHeadRight3;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;
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
        mTvHeadTitle.setText("批次盘点");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_17), (int) getResources().getDimension(R.dimen.dp_17));
        mIvHeadRight2.setLayoutParams(params);
        mIvHeadRight2.setImageResource(R.mipmap.ic_scan_gray_33);
        mIvHeadRight3.setLayoutParams(params);
        mIvHeadRight3.setImageResource(R.mipmap.ic_smq_gray_333);
        mTvHeadRight.setText("保存");
        mHeadRight3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //蓝牙扫描枪
                if (!verifyStkId()) return;//验证仓库
                showDialogBluetooth();
            }
        });
        mHeadRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //扫描
                if (!verifyStkId()) return;//验证仓库
                mCurrentBean = null;
                mCurrentPosition = null;
                if (SPUtils.getBoolean(ConstantUtils.Sp.SCAN_MULTIPLE)) {
                    ActivityManager.getInstance().jumpXScanActivity(context, true, stkId);
                } else {
                    showDialogMutilpleScan();
                }
            }
        });

        mHeadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存前验证数据的合法性
                if (doVerifySubmitData()) {
                    showDialogSave();
                }
            }
        });
    }


    //初始化适配器
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    StkBatchCheckAdapter mAdapter;
    View footer = null;

    private void initAdapter() {
        //尾部：添加商品
        footer = LayoutInflater.from(context).inflate(R.layout.x_layout_check_storage_footer, null);
        footer.findViewById(R.id.bottom_add_line).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!verifyStkId()) return;//验证仓库
                mCurrentBean = null;
                mCurrentPosition = null;
                doSelectWare(null);
            }
        });

        mAdapter = new StkBatchCheckAdapter(context);
        mAdapter.addFooterView(footer);
        mAdapter.setNewData(mDataList);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.x_item_line_f6)
                .sizeResId(R.dimen.dp_5)
                .build());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        //子控件点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    mCurrentPosition = position;
                    mCurrentBean = (StkCheckWareBean) adapter.getData().get(position);
                    switch (view.getId()) {
                        case R.id.item_tv_name: //打开搜索
                            if (!verifyStkId()) return;//验证仓库
                            mSearchView.open();
                            break;
                        case R.id.item_layout_ddd://单个扫描
                            if (!verifyStkId()) return;//验证仓库
                            ActivityManager.getInstance().jumpXScanActivity(context, false, stkId);
                            break;
                        case R.id.right_del://删除
                            showDialogDel();
                            break;
                        case R.id.item_layout_copy://复制
                        case R.id.right_copy://复制
                            doCopyRow();
                            break;
                    }
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
        //item长按
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    mCurrentPosition = position;
                    mCurrentBean = (StkCheckWareBean) adapter.getData().get(position);
                    showDialogDel();
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
                return false;
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
                    //单扫
                    if (MyStringUtil.isNotEmpty(result)) {
                        getP().getWareByScan(context, result, stkId, false);
                    }
                    //多扫
                    if (MyCollectionUtil.isNotEmpty(resultList)) {
                        doChooseWareSuccess(resultList);
                    }
                }
            }
            //选择商品
            if (requestCode == ConstantUtils.Intent.REQUEST_STEP_5_CHOOSE_GOODS && resultCode == ConstantUtils.Intent.RESULT_CODE_CHOOSE_GOODS) {
                if (data != null) {
                    ArrayList<ShopInfoBean.Data> datas = data.getParcelableArrayListExtra(ConstantUtils.Intent.CHOOSE_WARE);
                    doChooseWareSuccess(datas);
                }
            }

        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 选择商品成功后，再次请求“生产日期商品”
     */
    public void doChooseWareSuccess(List<ShopInfoBean.Data> datas) {
        String wareIds = MyStringUtil.getWareIds(datas, mAdapter.getData());
        if (MyStringUtil.isNotEmpty(wareIds)) {
            for (ShopInfoBean.Data ware : datas) {
                wareMap.put(ware.getWareId(), ware);
            }
            getP().getWareProduceDateList(context, stkId, wareIds);
        } else {
            ToastUtils.showCustomToast("没有改商品");
        }
    }


    //处理选择单个商品：1）扫描的结果 2）搜索的结果
    private void doSelectWare(ShopInfoBean.Data ware) {
        try {
            setTipPosition(-1);//恢复默认提示
            StkCheckWareBean bean = new StkCheckWareBean();
            if (null != ware) {
                wareToStkWare(ware, bean);
            }
            //判断商品是否已经添加过
            Map<String, Object> tempMap = isAddDataList(ware);
            if ((boolean) tempMap.get("tempAdd")) {
                //已添加
                showDialogRepeatWare((String) tempMap.get("tempName"), false);
                setTipPosition((int) tempMap.get("tempPosition"));
            } else {
                //未添加
                if (null != ware) {
                    doWareIdMap(ware.getWareId(), true);//记录商品个数（重复时列表会有颜色区分）
                }
                if (null == mCurrentBean) {
                    mAdapter.addData(bean);
                    refreshAdapter();
                    mRecyclerView.scrollToPosition(mAdapter.getData().size() - 1);
                } else {
                    doWareIdMap(mCurrentBean.getWareId(), false);//记录商品个数（重复时列表会有颜色区分）

                    bean.setProduceDate(mCurrentBean.getProduceDate());
                    bean.setQty(mCurrentBean.getQty());
                    bean.setMinQty(mCurrentBean.getMinQty());
                    doDisQty(bean);//处理差量
                    mAdapter.setData(mCurrentPosition, bean);
                    refreshAdapter();
                    mCurrentBean = bean;
                }
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }

    /**
     * 处理商品生产日期列表
     */
    public void doWareProduceDatList(List<WareProduceDateBean> rows) {
        try {
            if (MyCollectionUtil.isEmpty(rows)) {
                showDialogNoData();
                return;
            }
            setTipPosition(-1);//恢复默认提示

            List<StkCheckWareBean> stkCheckWareBeanList = new ArrayList<>();
            for (WareProduceDateBean ware : rows) {
                StkCheckWareBean bean = new StkCheckWareBean();
                bean.setWareId(ware.getWareId());
                bean.setWareNm(ware.getWareNm());
                if(MyStringUtil.isNotEmpty(ware.getHsNum() + "")){
                    bean.setHsNum(ware.getHsNum().toString());
                }else{
                    bean.setHsNum("1");
                }
                bean.setMinUnit(ware.getMinUnit());
                bean.setProduceDate(ware.getProduceDate());
                bean.setUnitName(ware.getUnitName());
                bean.setMinUnit(ware.getMinUnit());
                //账面数量（大，小，账面单位成本大，实际单位成本大：由商品中获取）
                for (Map.Entry<Integer, ShopInfoBean.Data> entry : wareMap.entrySet()) {
                    if (String.valueOf(ware.getWareId()).equals(String.valueOf(entry.getKey()))) {
                        if(MyStringUtil.isNotEmpty(entry.getValue().getStkQty() + "")){
                            bean.setStkQty(entry.getValue().getStkQty().toString());
                        }
                        if(MyStringUtil.isNotEmpty(entry.getValue().getMinStkQty() + "")){
                            bean.setMinStkQty(entry.getValue().getMinStkQty().toString());
                        }
                        bean.setZmPrice(new BigDecimal(entry.getValue().getInPrice2()));
                        bean.setPrice(new BigDecimal(entry.getValue().getInPrice2()));
                        break;
                    }
                }
//            bean.setAppendData(ware.getAppendData());
//            bean.setMaxAmt(bean.getMaxAmt());
//            bean.setMinAmt(bean.getMinAmt());
//            bean.setPriceFlag(bean.getPriceFlag());
//            bean.setMaxQtyFlag(bean.getMaxQtyFlag());
//            bean.setMinQtyFlag(bean.getMinQtyFlag());
                doWareIdMap(ware.getWareId(), true);
                stkCheckWareBeanList.add(bean);
            }
            mAdapter.addData(stkCheckWareBeanList);

            refreshAdapter();
            mRecyclerView.scrollToPosition(mAdapter.getData().size() - 1);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //处理返回的数据是多个：multipleScan-true多次扫描;false单次扫描
    private void doSelectWareList(List<ShopInfoBean.Data> list, boolean multipleScan) {
        if (null == list || list.isEmpty()) {
            showDialogNoData();
            return;
        }
        setTipPosition(-1);//恢复默认提示
        if (multipleScan) {
            //扫描多个条码
            String tempWareNmStr = "";
            boolean tempAdd = false;
            for (ShopInfoBean.Data ware : list) {
                StkCheckWareBean bean = new StkCheckWareBean();
                wareToStkWare(ware, bean);
                //判断列表是否已经存在过

                Map<String, Object> tempMap = isAddDataList(ware);
                if ((Boolean) tempMap.get("tempAdd")) {
                    if (MyStringUtil.isEmpty(tempWareNmStr)) {
                        tempWareNmStr += tempMap.get("tempName");
                    } else {
                        tempWareNmStr += "," + tempMap.get("tempName");
                    }
                    tempAdd = true;
                } else {
                    mAdapter.addData(bean);
                    doWareIdMap(ware.getWareId(), true);
                }
            }
            if (tempAdd) {
                showDialogRepeatWare(tempWareNmStr, true);
            }
            refreshAdapter();
            mRecyclerView.scrollToPosition(mAdapter.getData().size() - 1);
        } else {
            //扫描单个条码：可能有多个商品
            if (list.size() == 1) {
                //单个数据
                ShopInfoBean.Data ware = list.get(0);
                doSelectWare(ware);
            } else {
                //多个数据:弹出‘选择商品’对话框
                showDialogSelectWare(list);
            }
        }
    }

    //设置提示标志：重复商品，没有选择商品，没有设置大小单位数量
    public void setTipPosition(int tipPosition) {
        if (null != mAdapter) {
            mAdapter.setTipPosition(tipPosition);
            mRecyclerView.scrollToPosition(tipPosition);
            refreshAdapter();
        }
    }

    //列表中是否添加过
    public Map<String, Object> isAddDataList(ShopInfoBean.Data ware) {
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("tempAdd", false);
        tempMap.put("tempPosition", 0);
        tempMap.put("tempName", "");
        //查询列表是否已经存在：存在提示并定位该位置
        List<StkCheckWareBean> dataList = mAdapter.getData();
        if (null != dataList && !dataList.isEmpty() && null != ware) {
            for (int i = 0; i < dataList.size(); i++) {
                StkCheckWareBean tempBean = dataList.get(i);
                if (String.valueOf(ware.getWareId()).equals(String.valueOf(tempBean.getWareId()))) {
                    tempMap.put("tempAdd", true);
                    tempMap.put("tempPosition", i);
                    tempMap.put("tempName", tempBean.getWareNm());
                    break;
                }
            }
        }
        return tempMap;
    }

    //商品重复提示;multiple-true:多次扫描的；false-单次扫描
    private void showDialogRepeatWare(String wareNm, boolean multiple) {
        NormalDialog dialog = new NormalDialog(context);
        String content = wareNm + "，该商品已添加过了。";
        if (!multiple) {
            content += "默认该商品会移动到可见区域第一位";
        }
        dialog.content(content).btnNum(1).btnText("确定").show();
    }

    //处理计算差量
    private void doDisQty(StkCheckWareBean bean) {
        try {
            Integer wareId = bean.getWareId();
            if (null == wareId || 0 == wareId) {
                return;
            }
            double sum = 0;
            String stkQty = bean.getStkQty();
            Double qty = bean.getQty();
            Double minQty = bean.getMinQty();
            String hsNum = bean.getHsNum();
            String disQty = "";
            sum += MyDoubleUtils.getDoubleToDouble(qty);
            if (!MyStringUtil.isEmpty(hsNum) && !"0".equals(hsNum)) {
                sum += MyDoubleUtils.getDoubleToDouble(minQty) / MyDoubleUtils.getStringToDouble(hsNum);
            }
            if (!MyStringUtil.isEmpty(stkQty)) {
                disQty = "" + (sum - MyDoubleUtils.getStringToDouble(stkQty));
            }
            bean.setDisQty(disQty);
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }


    //单个条码多种商品-选择商品
    public void showDialogSelectWare(final List<ShopInfoBean.Data> list) {
        try {
            items.clear();
            for (ShopInfoBean.Data ware : list) {
                DialogMenuItem item = new DialogMenuItem(ware.getWareNm(), ware.getWareId());
                items.add(item);
            }
            NormalListDialog dialog = new NormalListDialog(context, items);
            dialog.title("单个条码多种商品").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    doSelectWare(list.get(position));
                    List<ShopInfoBean.Data> dataList = new ArrayList<>();
                    dataList.add(list.get(position));
                    doChooseWareSuccess(dataList);
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //TODO******************************扫描:end************************************
    //左上角扫描：可以多次提醒
    private void showDialogMutilpleScan() {
        NormalDialog dialog = new NormalDialog(context);
        dialog.content("可以多次扫描，会过滤掉重复的，按右上角‘确定’按钮来确认结果;按中间的‘预览扫描结果’可以预览")
                .show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                SPUtils.setBoolean(ConstantUtils.Sp.SCAN_MULTIPLE, true);
                ActivityManager.getInstance().jumpXScanActivity(context, true, stkId);
            }
        });
    }

    //对话框：长按删除
    private void showDialogDel() {
        NormalDialog dialog = new NormalDialog(context);
        dialog.content("确定要删除吗？").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                try {
                    doWareIdMap(mCurrentBean.getWareId(), false);
                    mAdapter.setTipPosition(-1);
                    mAdapter.remove(mCurrentPosition);
                    refreshAdapter();
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }

    public void doCopyRow(){
        StkCheckWareBean bean = new StkCheckWareBean();
        bean.setWareId(mCurrentBean.getWareId());
        bean.setWareNm(mCurrentBean.getWareNm());
        if(MyStringUtil.isNotEmpty(mCurrentBean.getHsNum() + "")){
            bean.setHsNum(mCurrentBean.getHsNum().toString());
        }else{
            bean.setHsNum("1");
        }
        bean.setMinUnit(mCurrentBean.getMinUnit());
        bean.setProduceDate(mCurrentBean.getProduceDate());
        bean.setUnitName(mCurrentBean.getUnitName());
        bean.setMinUnit(mCurrentBean.getMinUnit());
        //账面数量（大，小，账面单位成本大，实际单位成本大：由商品中获取）
        for (Map.Entry<Integer, ShopInfoBean.Data> entry : wareMap.entrySet()) {
            if (String.valueOf(mCurrentBean.getWareId()).equals(String.valueOf(entry.getKey()))) {
                if(MyStringUtil.isNotEmpty(entry.getValue().getStkQty() + "")){
                    bean.setStkQty(entry.getValue().getStkQty().toString());
                }
                if(MyStringUtil.isNotEmpty(entry.getValue().getMinStkQty() + "")){
                    bean.setMinStkQty(entry.getValue().getMinStkQty().toString());
                }
                bean.setZmPrice(new BigDecimal(entry.getValue().getInPrice2()));
                bean.setPrice(new BigDecimal(entry.getValue().getInPrice2()));
                break;
            }
        }
//            bean.setAppendData(ware.getAppendData());
//            bean.setMaxAmt(bean.getMaxAmt());
//            bean.setMinAmt(bean.getMinAmt());
//            bean.setPriceFlag(bean.getPriceFlag());
//            bean.setMaxQtyFlag(bean.getMaxQtyFlag());
//            bean.setMinQtyFlag(bean.getMinQtyFlag());
        doWareIdMap(mCurrentBean.getWareId(), true);
        mAdapter.addData(mCurrentPosition + 1, bean);
        refreshAdapter();
    }

    //对话框：删除全部
    private void showDialogDelAll() {
        NormalDialog dialog = new NormalDialog(context);
        dialog.content("确定要删除删除全部吗？").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                try {
                    //如果选择其他仓库，清空数据
                    mDataList.clear();
                    mDataList.add(new StkCheckWareBean());
                    mAdapter.setNewData(mDataList);
                    mAdapter.getWareIdMap().clear();
                    refreshAdapter();
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }

    //scan 没有数据
    private void showDialogNoData() {
        NormalDialog dialog = new NormalDialog(context);
        dialog.content("抱歉！没有匹配数据哦。可以去后台查看是否设置该条码的商品").btnNum(1).btnText("确定").show();
    }

    //记录重复商品的个数：添加或移除
    private void doWareIdMap(Integer wareId, boolean isAdd) {
        try {
            if (null == wareId || 0 == wareId || null == mAdapter) {
                return;
            }
            //添加商品个数
            Map<Integer, Integer> wareIdMap = mAdapter.getWareIdMap();
            Integer num = wareIdMap.get(wareId);
            if (isAdd) {
                if (null == num || 0 == num) {
                    mAdapter.getWareIdMap().put(wareId, 1);
                } else {
                    mAdapter.getWareIdMap().put(wareId, num + 1);
                }
            } else {
                if (null == num || 0 == num) {
                    mAdapter.getWareIdMap().put(wareId, 0);
                } else {
                    mAdapter.getWareIdMap().put(wareId, num - 1);
                }
            }
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
                    List<StkCheckWareBean> checkWareList =  mAdapter.getData();
                    String wareStr = JSON.toJSONString(checkWareList);
                    String staff = SPUtils.getSValues(ConstantUtils.Sp.USER_NAME);
                    String checkTimeStr = MyTimeUtils.getNowTime();
                    getP().addStkCheck(context, staff, stkId, checkTimeStr, wareStr, billId, mQueryToken);
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }


    //保存前验证数据的合法性
    private boolean doVerifySubmitData() {
        try {
            if (0 == stkId) {
                ToastUtils.showCustomToast("请选择仓库");
                return false;
            }
            //设置差量
            List<StkCheckWareBean> list = mAdapter.getData();
            if (null == list || list.isEmpty()) {
                ToastUtils.showLongCustomToast("请选择商品");
                return false;
            }
            for (int i = 0; i < list.size(); i++) {
                StkCheckWareBean bean = list.get(i);
                Integer wareId = bean.getWareId();
                if (null == wareId || 0 == wareId) {
                    setTipPosition(i);
                    ToastUtils.showLongCustomToast("第" + (i + 1) + "条：" + "没有设置商品");
                    return false;
                }
                String wareNm = bean.getWareNm();
                Double qty = bean.getQty();
                Double minQty = bean.getMinQty();
                boolean temp = false;//大小数量是否有填
                if (MyStringUtil.isEmpty("" + qty) && MyStringUtil.isEmpty("" + minQty)) {
                    temp = true;
                }
                if (temp) {
                    ToastUtils.showLongCustomToast("第" + (i + 1) + "条" + wareNm + "没有大小数量都没有设置");
                    setTipPosition(i);
                    return false;
                }
            }

        } catch (Exception e) {
            ToastUtils.showError(e);
        }
        return true;
    }

    //对话框：选择仓库
    private int stkId;//仓库id
    private String stkName;//仓库名
    private List<StorageBean.Storage> storageList = new ArrayList<>();//仓库列表
    private ArrayList<DialogMenuItem> items = new ArrayList<>();

    public void showDialogStorage(List<StorageBean.Storage> storageList) {
        try {
            this.storageList = storageList;
            if (null == storageList || storageList.isEmpty()) {
                ToastUtils.showCustomToast("没有仓库");
                return;
            }
            items.clear();
            for (StorageBean.Storage storage : storageList) {
                DialogMenuItem item = new DialogMenuItem(storage.getStkName(), storage.getId());
                items.add(item);
            }
            NormalListDialog dialog = new NormalListDialog(context, items);
            dialog.title("选择仓库").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DialogMenuItem item = items.get(position);
                    if (stkId != item.mResId) {
                        //如果选择其他仓库，清空数据
                        mDataList.clear();
                        mDataList.add(new StkCheckWareBean());
                        mAdapter.setNewData(mDataList);
                        refreshAdapter();
                    }
                    stkId = item.mResId;
                    stkName = item.mOperName;
                    mTvStorage.setText(item.mOperName);
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 重新选择仓库提示
     */
    public void showDialogStorageTip() {
        try {
            NormalDialog dialog = new NormalDialog(context);
            dialog.content("重新选择仓库后，已选择的商品会清空")
                    .show();
            dialog.setOnBtnClickL(null, new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    if (null == storageList || storageList.isEmpty()) {
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
        if (null == bean) {
            return;
        }
        try {
            StkCheckBean stkCheckBean = bean.getCheck();
            stkId = stkCheckBean.getStkId();
            mTvStorage.setText(stkCheckBean.getStkName());
            List<StkCheckWareBean> checkWareBeanList = bean.getList();
            mAdapter.setNewData(checkWareBeanList);

            //状态
            String status = stkCheckBean.getStatus();
            if ("-2".equals(status)) {
                mTvHeadRight.setText("修改");
            } else {
                mIvHeadRight3.setVisibility(View.GONE);
                mIvHeadRight2.setVisibility(View.GONE);
                mTvHeadRight.setVisibility(View.GONE);
                mHeadRight3.setEnabled(false);
                mHeadRight2.setEnabled(false);
                mHeadRight.setEnabled(false);
                mLayoutStorage.setEnabled(false);
                mTvStorage.setEnabled(false);
                mAdapter.setAddState(false);
                mAdapter.removeFooterView(footer);
            }
            refreshAdapter();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
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
//        doSelectWareList(list, multiple);
        doChooseWareSuccess(list);
    }

    //选择商品之前要先选择仓库
    public boolean verifyStkId() {
        boolean flag = true;
        if (0 == stkId) {
            flag = false;
            ToastUtils.showCustomToast("请先选择仓库");
        }
        return flag;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭蓝牙扫描枪连接
        closeBluetooth();
        doCacheDate();
    }

    /**
     * 蓝牙扫描枪
     */
    private HPRTHelper mHelper;

    private void showDialogBluetooth() {
        MyBluetoothScanDialog dialog = new MyBluetoothScanDialog(context);
        dialog.show();
        dialog.setOnClickListener(new MyBluetoothScanDialog.OnSuccessListener() {
            @Override
            public void onSuccessListener(String result) {
                if (MyStringUtil.isNotEmpty(result)) {
                    getP().getWareByScan(context, result, stkId, false);
                }
            }
        });
    }

    /**
     * 关闭蓝牙扫描枪连接
     */
    private void closeBluetooth() {
        if (mHelper != null) {
            mHelper.disconnect(new HPRTHelper.onDisconnect() {
                @Override
                public void succeed() {
                }
            });
        }
    }

    //避免重复的token
    private String mQueryToken;

    public void doToken(String data) {
        mQueryToken = data;
    }

    /**
     * 刷新适配器：判断第一条数据是否有商品（没有去掉）
     */
    public void refreshAdapter() {
        try {
            List<StkCheckWareBean> dataList = mAdapter.getData();
            if (dataList != null && dataList.size() >= 2) {
                StkCheckWareBean stkCheckWareBean = dataList.get(0);
                if (stkCheckWareBean.getWareId() == null || "0".equals(stkCheckWareBean.getWareId().toString())) {
                    mAdapter.remove(0);
                }
            }
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    /**
     * 缓存数据
     */
    public boolean isSaveCache = true;//是否保存缓存；提交成功不保存
    public void doCacheDate(){
        try{
            if (isSaveCache) {
                List<StkCheckWareBean> dataList = this.mAdapter.getData();
                if (MyCollectionUtil.isNotEmpty(dataList)) {
                    List<DStkCheckWareBean> saveList = new ArrayList<>();
                    for (StkCheckWareBean data : dataList) {
                        DStkCheckWareBean save = new DStkCheckWareBean();
                        save.setUserId(SPUtils.getID());
                        save.setCompanyId(SPUtils.getCompanyId());
                        save.setStkId(stkId);
                        save.setStkName(stkName);
                        save.setWareId(data.getWareId());
                        save.setWareNm(data.getWareNm());
                        save.setQty(data.getQty());
                        save.setMinQty(data.getMinQty());
                        save.setHsNum(data.getHsNum());
                        save.setDisQty(data.getDisQty());
                        save.setStkQty(data.getStkQty());
                        save.setProduceDate(data.getProduceDate());
                        save.setSunitFront(data.getSunitFront());
                        save.setUnitName(data.getUnitName());
                        save.setMinUnit(data.getMinUnit());
                        save.setType(type);
                        if (data.getZmPrice()!=null){
                            save.setZmPrice(data.getZmPrice().toString());
                        }
                        if (data.getPrice()!=null){
                            save.setPrice(data.getPrice().toString());
                        }
                        saveList.add(save);
                    }
                    MyDataUtils.getInstance().saveStkWare(saveList,type);
                }
            } else {
                MyDataUtils.getInstance().delStkWare(type);
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    /**
     * 获取缓存数据
     */
    public void doGetCacheData(){
        try{
            List<StkCheckWareBean> dataList = new ArrayList<>();
            //缓存
            List<DStkCheckWareBean> saveList = MyDataUtils.getInstance().queryStkWare(type);
            if (saveList != null && saveList.size() > 0) {
                for (DStkCheckWareBean save : saveList) {
                    stkId = save.getStkId();
                    stkName = save.getStkName();
                    StkCheckWareBean wareBean = new StkCheckWareBean();
                    wareBean.setWareId(save.getWareId());
                    wareBean.setWareNm(save.getWareNm());
                    wareBean.setQty(save.getQty());
                    wareBean.setMinQty(save.getMinQty());
                    wareBean.setHsNum(save.getHsNum());
                    wareBean.setDisQty(save.getDisQty());
                    wareBean.setStkQty(save.getStkQty());
                    wareBean.setProduceDate(save.getProduceDate());
                    wareBean.setSunitFront(save.getSunitFront());
                    wareBean.setUnitName(save.getUnitName());
                    wareBean.setMinUnit(save.getMinUnit());
                    if (MyStringUtil.isNotEmpty(save.getZmPrice())){
                        wareBean.setZmPrice(new BigDecimal(save.getZmPrice()));
                    }
                    if (MyStringUtil.isNotEmpty(save.getPrice())){
                        wareBean.setPrice(new BigDecimal(save.getPrice()));
                    }
                    dataList.add(wareBean);
                }
            } else {
                //初始化适配器数据：默认一个没有商品的
                dataList.add(new StkCheckWareBean());
            }
            this.mTvStorage.setText(stkName);
            mAdapter.setNewData(dataList);
            refreshAdapter();
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //商品信息转为库存商品信息
    public void wareToStkWare(ShopInfoBean.Data ware, StkCheckWareBean bean){
        bean.setSunitFront(ware.getSunitFront());
        bean.setWareId(ware.getWareId());
        bean.setWareNm(ware.getWareNm());
        bean.setHsNum(ware.getHsNum());
        bean.setMinUnit(ware.getMinUnit());
        bean.setStkQty(ware.getStkQty());
        bean.setProduceDate(ware.getProductDate());
        bean.setUnitName(ware.getWareDw());
        bean.setMinUnit(ware.getMinUnit());
        bean.setMinStkQty(ware.getMinStkQty());
    }

}
