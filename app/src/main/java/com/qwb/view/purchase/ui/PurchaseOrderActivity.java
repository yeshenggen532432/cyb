package com.qwb.view.purchase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.deadline.statebutton.StateButton;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.event.ObjectEvent;
import com.qwb.event.OrderEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDoubleUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.company.adapter.PurchaseOrderRightAdapter;
import com.qwb.view.step.adapter.Step5Left2Adapter;
import com.qwb.view.step.adapter.Step5SearchWareAdapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.db.DStep5Bean;
import com.qwb.view.company.model.PurchaseOrderBean;
import com.qwb.view.company.model.PurchaseOrderSubBean;
import com.qwb.view.company.model.PurchaseTypeBean;
import com.qwb.view.company.model.WareBean;
import com.qwb.view.stk.StorageBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.view.purchase.parsent.PPurchaseOrder;
import com.qwb.view.step.ui.ChooseWareActivity3;
import com.qwb.view.object.ui.ChooseObjectActivity;
import com.qwb.utils.MyKeyboardUtil;
import com.qwb.utils.MyNetWorkUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.xmsx.cnlife.view.widget.MyVoiceDialog;
import com.qwb.widget.table.TableHorizontalScrollView;
import com.qwb.widget.table.TableOnScrollListener;
import com.qwb.widget.MyDateTimePickerDialog;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zyyoona7.lib.EasyPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 采购单
 */
public class PurchaseOrderActivity extends XActivity<PPurchaseOrder> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_purchase_order;
    }

    @Override
    public PPurchaseOrder newP() {
        return new PPurchaseOrder();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        doIntent();
        createPopupSearchGoods();//语音搜索商品
        getP().queryToken(null);
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    ObjectEvent mCurrentObjectEvent;

    @Override
    public void bindEvent() {
        super.bindEvent();
        BusProvider.getBus().toFlowable(ObjectEvent.class)
                .subscribe(new Consumer<ObjectEvent>() {
                    @Override
                    public void accept(ObjectEvent event) throws Exception {
                        if (event.getTag() == ConstantUtils.Event.TAG_OBJECT && event != null) {
                            ToastUtils.showCustomToast(event.getName());
                            mTvKhNm.setText(event.getName());
                            mCurrentObjectEvent = event;
                        }
                    }
                });
    }

    /**
     * 初始化Intent
     */
    private String mOrderId;//订单id
    private void initIntent() {
        Intent intent = getIntent();
        if (null != intent) {
            mOrderId = intent.getStringExtra(ConstantUtils.Intent.ORDER_ID);
        }
    }

    //根据不同的类型处理不同的显示
    private void doIntent() {
        if (MyStringUtil.isEmpty(mOrderId)) {
            mTvTime.setText(MyTimeUtils.getNowTime());//时间（默认现在），可以修改
            getP().queryStorage(context, false);
        } else {
            getP().queryOrder(context, mOrderId);
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initTableView();
        initBottomUI();
        initOtherUI();
    }

    @BindView(R.id.tv_khNm)
    TextView mTvKhNm;
    @BindView(R.id.et_bz)
    EditText mEtBz;
    @BindView(R.id.tv_storage)
    TextView mTvStorage;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.layout_storage)
    View mLayoutStorage;
    @BindView(R.id.tv_time_lable)
    TextView mTvTimeLable;
    @BindView(R.id.cb_checkAutoPrice)
    CheckBox mCbCheckAutoPrice;
    private void initOtherUI() {
        mTvKhNm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().jumpActivity(context, ChooseObjectActivity.class);
            }
        });
        //选择仓库
        mTvStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (null == mStorageDatas || mStorageDatas.isEmpty()) {
                        getP().queryStorage(context, true);
                    } else {
                        showDialogStorage(mStorageDatas);
                    }
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
        //选择时间
        mTvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogTime();
            }
        });
    }

    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.head_right2)
    View mHeadRight2;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.iv_head_right2)
    ImageView mIvHeadRight2;

    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle.setText("采购下单");
        mTvHeadRight.setText("提交");
        mHeadRight.setOnClickListener(new OnNoMoreClickListener() {
            @Override
            protected void OnMoreClick(View view) {
                if (mHeadRight.getVisibility() == View.VISIBLE) {
                    addData();
                }
            }
        });
    }

    private RecyclerView mRvLeft;
    private RecyclerView mRvRight;
    private TableHorizontalScrollView mSvWare;
    private Step5Left2Adapter mLeftAdapter;
    private PurchaseOrderRightAdapter mRightAdapter;
    @BindView(R.id.tv_table_title_left)
    TextView mTvTableTitleLeft;
    @BindView(R.id.tv_table_title_8_produce_date)
    TextView mTvTableTitleProduceDate;
    @BindView(R.id.tv_table_title_7)
    TextView mTvTableTitle7Del;
    public void initTableView() {
        mRvLeft = findViewById(R.id.rv_left);
        mRvRight = findViewById(R.id.rv_right);
        mSvWare = findViewById(R.id.sv_ware);

        mRvLeft.setLayoutManager(new LinearLayoutManager(this));
        mRvLeft.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_b)
                .sizeResId(R.dimen.dp_0_5)
                .showLastDivider()
                .build());
        mLeftAdapter = new Step5Left2Adapter();
        mRvLeft.setAdapter(mLeftAdapter);
        mRvRight.setLayoutManager(new LinearLayoutManager(this));
        mRvRight.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_b)
                .sizeResId(R.dimen.dp_0_5)
                .showLastDivider()
                .build());
        mRightAdapter = new PurchaseOrderRightAdapter();
        mRvRight.setAdapter(mRightAdapter);
        mRightAdapter.setOnChildListener(new PurchaseOrderRightAdapter.OnChildListener() {
            @Override
            public void onChildListener(int tag, int position, ShopInfoBean.Data item) {
                try {
                    mCurrentItem = item;
                    mCurrentPosition = position;
                    switch (tag) {
                        case PurchaseOrderRightAdapter.TAG_DW:
                            showDialogChangeDw(item, position);
                            break;
                        case PurchaseOrderRightAdapter.TAG_XSTP:
                            if (null == mXstpDatas || mXstpDatas.isEmpty()) {
                                getP().queryXsTp(context);
                            } else {
                                showDialogXstp(mXstpDatas);
                            }
                            break;
                        case PurchaseOrderRightAdapter.TAG_DEl:
                            showDialogDel();
                            break;
                        case PurchaseOrderRightAdapter.TAG_COUNT:
                        case PurchaseOrderRightAdapter.TAG_PRICE:
                            setSumMoney();
                            break;
                    }
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });

        //设置两个列表的同步滚动
        setSyncScrollListener();

        //添加商品
        mTvTableTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mCurrentObjectEvent == null) {
                        ToastUtils.showShort(context, "先选择发货单位");
                        return;
                    }
                    ArrayList<ShopInfoBean.Data> dataList = (ArrayList<ShopInfoBean.Data>) mRightAdapter.getData();
                    Router.newIntent(context)
                            .putString(ConstantUtils.Intent.TYPE, "8")
                            .putString(ConstantUtils.Intent.STK_ID, stkId)
                            .putParcelableArrayList(ConstantUtils.Intent.CHOOSE_WARE, dataList)
                            .requestCode(ConstantUtils.Intent.REQUEST_STEP_5_CHOOSE_GOODS)
                            .to(ChooseWareActivity3.class)
                            .launch();
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }

    /**
     * 设置两个列表的同步滚动
     */
    private final RecyclerView.OnScrollListener mLeftOSL = new TableOnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // 当楼层列表滑动时，单元（房间）列表也滑动
            mRvRight.scrollBy(dx, dy);
        }
    };
    private final RecyclerView.OnScrollListener mRightOSL = new TableOnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // 当单元（房间）列表滑动时，楼层列表也滑动
            mRvLeft.scrollBy(dx, dy);
        }
    };

    private void setSyncScrollListener() {
        mRvLeft.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            private int mLastY;

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                // 当列表是空闲状态时
                if (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    onTouchEvent(rv, e);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                // 若是手指按下的动作，且另一个列表处于空闲状态
                if (e.getAction() == MotionEvent.ACTION_DOWN && mRvRight.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    // 记录当前另一个列表的y坐标并对当前列表设置滚动监听
                    mLastY = rv.getScrollY();
                    rv.addOnScrollListener(mLeftOSL);
                } else {
                    // 若当前列表原地抬起手指时，移除当前列表的滚动监听
                    if (e.getAction() == MotionEvent.ACTION_UP && rv.getScrollY() == mLastY) {
                        rv.removeOnScrollListener(mLeftOSL);
                    }
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
        mRvRight.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            private int mLastY;

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    onTouchEvent(rv, e);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN && mRvLeft.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    mLastY = rv.getScrollY();
                    rv.addOnScrollListener(mRightOSL);
                } else {
                    if (e.getAction() == MotionEvent.ACTION_UP && rv.getScrollY() == mLastY) {
                        rv.removeOnScrollListener(mRightOSL);
                    }
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
        mSvWare.setScrollViewListener(new TableHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(TableHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                mRvLeft.removeOnScrollListener(mLeftOSL);
                mRvRight.removeOnScrollListener(mRightOSL);
            }
        });
    }

    @BindView(R.id.et_zdzk)
    EditText mEtZdzk;
    @BindView(R.id.et_zdzk_percent)
    EditText mEtZdzkParent;
    @BindView(R.id.btn_zdzk_convert)
    StateButton mBtnZdzkConvert;
    @BindView(R.id.tv_zje)
    TextView mTvZje;
    @BindView(R.id.tv_cjje)
    TextView mTvCjje;
    @BindView(R.id.layout_zdzk)
    View mLayoutZdzk;
    @BindView(R.id.layout_voice)
    View mLayoutVoice;

    private void initBottomUI() {
        //百分比转整单折扣金额
        mBtnZdzkConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moneyConvert();
            }
        });
        // 整单折扣
        mEtZdzk.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable input) {
                doZdzkListener(input.toString().trim());
            }
        });
        // 语音
        mLayoutVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogVoice();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantUtils.Intent.REQUEST_STEP_5_CHOOSE_GOODS && resultCode == ConstantUtils.Intent.RESULT_CODE_CHOOSE_GOODS) {
            if (data != null) {
                try {
                    boolean editPrice = data.getBooleanExtra(ConstantUtils.Intent.EDIT_PRICE, true);
                    ArrayList<ShopInfoBean.Data> datas = data.getParcelableArrayListExtra(ConstantUtils.Intent.CHOOSE_WARE_NEW);
                    if (datas != null && datas.size() > 0) {
                        mRightAdapter.setEditPrice(editPrice);
                        mLeftAdapter.addData(datas);
                        mRightAdapter.addData(datas);
                        refreshAdapterRight();
                    }
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        }
    }


    /**
     * 刷新表格数据
     */
    private void refreshAdapterRight() {
        try {
            //标记商品的个数（重复商品颜色变）
            setRepeatMap();
            //刷新
            mLeftAdapter.notifyDataSetChanged();
            mRightAdapter.notifyDataSetChanged();
            setSumMoney();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 标记商品的个数（重复商品颜色变）--1:选择商品后 2：删除商品 3：获取订单数据
     */
    public void setRepeatMap() {
        try {
            Map<Integer, Integer> repeatMap = new HashMap<>();
            for (ShopInfoBean.Data data : mLeftAdapter.getData()) {
                int wareId = data.getWareId();
                if (repeatMap.containsKey(wareId)) {
                    int qty = repeatMap.get(wareId);
                    repeatMap.put(wareId, qty + 1);
                } else {
                    repeatMap.put(wareId, 1);
                }
            }
            mLeftAdapter.setRepeatMap(repeatMap);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    //--------------------------------dialog:开始---------------------------------------------------
    private ShopInfoBean.Data mCurrentItem;
    private int mCurrentPosition;

    //对话框-切换单位
    private void showDialogChangeDw(final ShopInfoBean.Data item, final int position) {
        try {
            String wareNm = item.getWareNm();
            String maxDw = item.getWareDw();
            String minDw = item.getMinUnit();
            final ArrayList<DialogMenuItem> items = new ArrayList<>();
            if (!MyStringUtil.isEmpty(maxDw)) {
                items.add(new DialogMenuItem(maxDw, 0));
            }
            if (!MyStringUtil.isEmpty(minDw)) {
                items.add(new DialogMenuItem(minDw, 1));
            }
            NormalListDialog dialog = new NormalListDialog(context, items);
            dialog.title(wareNm).show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                    try {
                        String operName = items.get(i).mOperName;
                        String hsNum = item.getHsNum();
                        String price = item.getCurrentPrice();
                        String dw = item.getCurrentDw();
                        String maxCode = item.getMaxUnitCode();
                        String minCode = item.getMinUnitCode();
                        if (!dw.equals(operName)) {
                            item.setCurrentDw(operName);
                            //单位，单位代码，价格，总价，总金额
                            if (0 == id) {
                                item.setCurrentCode(maxCode);
                                item.setCurrentPrice(item.getCurrentMaxPrice());
                            } else {
                                item.setCurrentCode(minCode);
                                item.setCurrentPrice(item.getCurrentMinPrice());
                            }
                            mRightAdapter.getData().set(position, item);
                            refreshAdapterRight();
                        }
                        MyKeyboardUtil.closeKeyboard(context);//强制关闭软键盘
                    } catch (Exception e) {
                        ToastUtils.showError(e);
                    }
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //对话框-销售类型
    private List<PurchaseTypeBean.TypeBean> mXstpDatas = new ArrayList<>();

    public void showDialogXstp(List<PurchaseTypeBean.TypeBean> datas) {
        try {
            this.mXstpDatas = datas;
            String wareNm = mCurrentItem.getWareNm();
            if (null == mXstpDatas || mXstpDatas.isEmpty()) {
                ToastUtils.showCustomToast("没有销售类型");
                return;
            }

            final ArrayList<DialogMenuItem> dialogMenuItems = new ArrayList<>();
            for (PurchaseTypeBean.TypeBean xstp : mXstpDatas) {
                dialogMenuItems.add(new DialogMenuItem(xstp.getInTypeName(), xstp.getInTypeCode()));
            }
            NormalListDialog dialog = new NormalListDialog(context, dialogMenuItems);
            dialog.title(wareNm).show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                    try {
                        DialogMenuItem item = dialogMenuItems.get(i);
                        mCurrentItem.setInTypeCode(item.mResId);
                        mCurrentItem.setInTypeName(item.mOperName);
                        mRightAdapter.getData().set(mCurrentPosition, mCurrentItem);
                        refreshAdapterRight();
                    } catch (Exception e) {
                        ToastUtils.showError(e);
                    }
                    MyKeyboardUtil.closeKeyboard(context);//强制关闭软键盘
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //对话框-删除商品
    public void showDialogDel() {
        NormalDialog dialog = new NormalDialog(context);
        dialog.content("你确定删除''" + mCurrentItem.getWareNm() + "''吗？").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                try {
                    mRightAdapter.getData().remove(mCurrentPosition);
                    mLeftAdapter.getData().remove(mCurrentPosition);
                    refreshAdapterRight();
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }

    //对话框-仓库
    private List<StorageBean.Storage> mStorageDatas = new ArrayList<>();
    private String stkId;//仓库id

    public void showDialogStorage(List<StorageBean.Storage> storageDatas) {
        try {
            this.mStorageDatas = storageDatas;
            if (null == mStorageDatas || mStorageDatas.isEmpty()) {
                ToastUtils.showCustomToast("没有仓库可以选择");
                return;
            }

            final ArrayList<DialogMenuItem> dialogMenuItems = new ArrayList<>();
            for (StorageBean.Storage storage : mStorageDatas) {
                dialogMenuItems.add(new DialogMenuItem(storage.getStkName(), storage.getId()));
            }
            NormalListDialog dialog = new NormalListDialog(context, dialogMenuItems);
            dialog.title("选择仓库").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                    try {
                        String storageNm = dialogMenuItems.get(i).mOperName;
                        mTvStorage.setText(storageNm);
                        stkId = dialogMenuItems.get(i).mResId + "";
                    } catch (Exception e) {
                        ToastUtils.showError(e);
                    }
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 默认仓库
     */
    public void doNormalStorage(List<StorageBean.Storage> storageDatas) {
        try {
            this.mStorageDatas = storageDatas;
            if (storageDatas != null && storageDatas.size() > 0) {
                //先查询是否有默认仓库，固定仓库；没有则选择第一个仓库
                for (StorageBean.Storage storage : storageDatas) {
                    String isSelect = storage.getIsSelect();
                    String isFixed = storage.getIsFixed();
                    if ("1".equals(isSelect) || "1".equals(isFixed)) {
                        stkId = "" + storage.getId();
                        mTvStorage.setText(storage.getStkName());
                        break;
                    }
                }
                if (MyStringUtil.isEmpty(stkId)) {
                    StorageBean.Storage storage = storageDatas.get(0);
                    stkId = "" + storage.getId();
                    mTvStorage.setText(storage.getStkName());
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    //对话框-选择时间
    private int startYear, startMonth, startDay, startHour, startMin;

    private void showDialogTime() {
        if (startYear == 0) {
            startYear = MyTimeUtils.getYear();
            startMonth = MyTimeUtils.getMonth();
            startDay = MyTimeUtils.getDay();
            startHour = MyTimeUtils.getHour();
            startMin = MyTimeUtils.getMin();
        }
        new MyDateTimePickerDialog(context, "选择时间", startYear, startMonth, startDay, startHour, startMin,
                new MyDateTimePickerDialog.DateTimeListener() {
                    @Override
                    public void onSetTime(int year, int month, int day, int hour, int minute, String timeStr) {
                        try {
                            startYear = year;
                            startMonth = month;
                            startDay = day;
                            startHour = hour;
                            startMin = minute;
                            mTvTime.setText(timeStr);
                        } catch (Exception e) {
                            ToastUtils.showError(e);
                        }
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();

    }

    //语音搜索对话框
    private String mVoiceResult;

    private void showDialogVoice() {
        MyVoiceDialog dialog = new MyVoiceDialog(context);
        dialog.show();
        dialog.setOnSuccessOnclick(new MyVoiceDialog.OnSuccessListener() {
            @Override
            public void onSuccessOnclick(String result) {
                try {
                    mVoiceResult = result;
                    if (!MyStringUtil.isEmpty(mVoiceResult)) {
                        getP().getDataKeyWordGoodsList(context, mVoiceResult, true);
                    }
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }

    /**
     * 语音搜索商品
     */
    private EasyPopup mPopupSearchWare;
    private ListView mLvSearchWare;
    private EditText mEtSearchGoods;
    public List<ShopInfoBean.Data> mSearchWareList = new ArrayList<>();
    private Step5SearchWareAdapter mAdapterSearWare;

    public void createPopupSearchGoods() {
        mPopupSearchWare = new EasyPopup(context)
                .setContentView(R.layout.x_popup_step5_search_ware)
                .createPopup();
        mEtSearchGoods = mPopupSearchWare.getView(R.id.et_search);
        mLvSearchWare = mPopupSearchWare.getView(R.id.listView_search_goods);
        mAdapterSearWare = new Step5SearchWareAdapter(context, mSearchWareList);
        mLvSearchWare.setAdapter(mAdapterSearWare);
        mPopupSearchWare.getView(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mVoiceResult = mEtSearchGoods.getText().toString().trim();
                    getP().getDataKeyWordGoodsList(context, mVoiceResult, false);
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
        //item
        mLvSearchWare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ShopInfoBean.Data data = mSearchWareList.get(position);
                    doAddWareRefreshAdapter(data);
                    mPopupSearchWare.dismiss();
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }

    //搜索商品后刷新列表（语音和弹窗中的搜索框）
    public void refreshAdapterSearch(List<ShopInfoBean.Data> list, boolean showPopup) {
        try {
            //一个直接添加商品（语音的）；多个弹出选择
            if (null != list && list.size() == 1 && showPopup) {
                doAddWareRefreshAdapter(list.get(0));
            } else {
                mEtSearchGoods.setText(mVoiceResult);
                mSearchWareList.clear();
                mSearchWareList.addAll(list);
                mAdapterSearWare.notifyDataSetChanged();
                if (showPopup) {
                    mPopupSearchWare.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //添加商品到表格中：刷新table
    private void doAddWareRefreshAdapter(ShopInfoBean.Data data) {
        try {
            //TODO 主要有：单位，单位代码，数量，价格，备注
            int sunitFront = data.getSunitFront();//1辅助单位
            data.setCurrentCount("1");
            if (1 == sunitFront) {
                data.setCurrentCode(data.getMinUnitCode());
                data.setCurrentDw(data.getMinUnit());
                data.setCurrentPrice((Double.valueOf(data.getWareDj()) / Double.valueOf(data.getHsNum())) + "");
            } else {
                data.setCurrentCode(data.getMaxUnitCode());
                data.setCurrentDw(data.getWareDw());
                data.setCurrentPrice(data.getWareDj());
            }
            data.setCurrentXstp("正常销售");
            data.setCurrentBz("");
            mLeftAdapter.addData(data);
            mRightAdapter.addData(data);
            refreshAdapterRight();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }
    //--------------------------------dialog:结束----------------------------------------------------


    /**
     * 遍历--所有商品的总价求和
     */
    private double zjeDouble;//总金额
    private double cjjeDouble;//成交金额

    private void setSumMoney() {
        zjeDouble = 0.0;
        try {
            zjeDouble = getTableSumMoney();
            mTvZje.setText(String.valueOf(MyDoubleUtils.getDecimal(zjeDouble)));
            // 设置成交金额 和 百分比
            String zdzk = mEtZdzk.getText().toString().trim();
            if (!MyStringUtil.isEmpty(zdzk) && zjeDouble > 0) {// 条件得改
                double zdzkDouble = Double.valueOf(zdzk);
                double percent = zdzkDouble * 100 / zjeDouble;
                //计算百分比
                mEtZdzkParent.setText(String.valueOf(MyDoubleUtils.getDecimal(percent)));
                //计算成交金额
                if (zjeDouble - zdzkDouble > 0) {
                    // 如果:"整单折扣"不能大于"总金额"
                    mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(zjeDouble - zdzkDouble)));
                } else {
                    cjjeDouble = zjeDouble;
                    mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(cjjeDouble)));
                }
            } else {
                cjjeDouble = zjeDouble;
                mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(cjjeDouble)));
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 金额换算
     */
    private void moneyConvert() {
        try {
            String perentStr = mEtZdzkParent.getText().toString().trim();
            if (!MyStringUtil.isEmpty(perentStr)) {
                String sumStr = mTvZje.getText().toString().trim();
                if (!MyUtils.isEmptyString(sumStr)) {// 判断是否为null
                    zjeDouble = Double.parseDouble(sumStr);
                    if (!MyUtils.isEmptyString(perentStr) && perentStr.length() > 0) {
                        double percent = Double.valueOf(perentStr);
                        double zdzkDouble = percent * zjeDouble / 100;//百分比*总金额
                        // "整单折扣"不能大于"总金额"
                        if (percent > 100) {
                            ToastUtils.showLongCustomToast("折扣金额已超过");
                            cjjeDouble = zjeDouble;
                            mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(cjjeDouble)));
                            mEtZdzk.setText(String.valueOf(MyDoubleUtils.getDecimal(zdzkDouble)));
                        } else {
                            cjjeDouble = zjeDouble - zdzkDouble;
                            mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(cjjeDouble)));
                            mEtZdzk.setText(String.valueOf(MyDoubleUtils.getDecimal(zdzkDouble)));
                        }
                    } else {
                        cjjeDouble = zjeDouble;
                        mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(zjeDouble)));
                        mEtZdzk.setText("");
                    }
                } else {
                    mTvCjje.setText("");
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //整单折扣变化监听
    private void doZdzkListener(String input) {
        try {
            double percent = 0;
            String zjeStr = mTvZje.getText().toString().trim();
            if (!MyUtils.isEmptyString(zjeStr)) {// 判断是否为null
                double zjeDouble = Double.parseDouble(zjeStr);
                if (!MyUtils.isEmptyString(input) && input.length() > 0 && zjeDouble > 0) {
                    double zdzkDouble = Double.valueOf(input);
                    percent = zdzkDouble * 100 / zjeDouble;//先乘100再除以总金额
                    // "整单折扣"不能大于"总金额"
                    if (zdzkDouble > zjeDouble) {
                        ToastUtils.showLongCustomToast("折扣金额已超过");
                        cjjeDouble = zjeDouble;
                    } else {
                        cjjeDouble = zjeDouble - zdzkDouble;
                    }
                } else {
                    cjjeDouble = zjeDouble;
                }
            } else {
                cjjeDouble = zjeDouble;
            }
            mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(cjjeDouble)));
            mEtZdzkParent.setText(String.valueOf(MyDoubleUtils.getDecimal(percent)));
        } catch (Exception e) {
            ToastUtils.showCustomToast("字符串转double错误！");
        }
    }

    //获取table中总金额
    private double getTableSumMoney() {
        double sum = 0.0;
        try {
            List<ShopInfoBean.Data> dataList = mRightAdapter.getData();
            if (null == dataList || dataList.isEmpty()) {
                return sum;
            }
            for (ShopInfoBean.Data item : dataList) {
                String count = item.getCurrentCount();
                String price = item.getCurrentPrice();
                if (!MyStringUtil.isEmpty(count) && !MyStringUtil.isEmpty(price)) {
                    double zj = Double.valueOf(count) * Double.valueOf(price);
                    sum += zj;
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
        return sum;
    }


    //TODO ******************************接口******************************
    /**
     * 提交或修改数据
     */
    private String mJsonStr;
    private String mRemoStr;
    private String mZdzkStr;
    private String mShTimeStr;

    private void addData() {
        try {
            List<ShopInfoBean.Data> dataList = mRightAdapter.getData();
            if (null != dataList && dataList.isEmpty()) {
                ToastUtils.showLongCustomToast("请添加商品+");
                return;
            }
            if (!MyNetWorkUtil.isNetworkConnected()) {
                ToastUtils.showCustomToast("网络不流通，请检查网络是否正常");
//                showDialogCache();
                return;
            }
            mRemoStr = mEtBz.getText().toString().trim();
            mZdzkStr = mEtZdzk.getText().toString().trim();
            mShTimeStr = mTvTime.getText().toString().trim();
            String proId = mCurrentObjectEvent.getId() + "";
            String proName = mCurrentObjectEvent.getName();
            String proType = mCurrentObjectEvent.getType() + "";
            int checkAutoPrice = 0;
            if(mCbCheckAutoPrice.isChecked()){
                checkAutoPrice = 1;
            }
            getJsonStr();//获取商品列表拼接的json
            getP().addData(context, mOrderId, proId, proName, proType, stkId, checkAutoPrice, mShTimeStr, mZdzkStr, mRemoStr, mJsonStr, mQueryToken);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 获取商品列表拼接的json
     */
    private void getJsonStr() {
        try {
            // 品项，销售类型，数量，单位，单价，总价，操作
            List<WareBean> list = new ArrayList<>();
            list.clear();
            List<ShopInfoBean.Data> dataList = mRightAdapter.getData();
            if (dataList != null && dataList.size() > 0) {
                for (ShopInfoBean.Data data : dataList) {
                    WareBean xiaJi = new WareBean();
                    String count = data.getCurrentCount();
                    String price = data.getCurrentPrice();

                    xiaJi.setWareId(data.getWareId() + "");
                    if (MyStringUtil.isEmpty(count)) {
                        count = "0";
                    }
                    xiaJi.setQty(count);
                    if (MyStringUtil.isEmpty(price)) {
                        price = "0";
                    }
                    xiaJi.setPrice(price);
                    xiaJi.setBeUnit(data.getCurrentCode());// 包装单位代码或计量单位代码
                    xiaJi.setRemarks(data.getCurrentBz());//备注
                    xiaJi.setProductDate(data.getCurrentProductDate());//生产日期

                    xiaJi.setHsNum(data.getHsNum());
                    xiaJi.setUnitName(data.getCurrentDw());
                    xiaJi.setProductDate(data.getCurrentProductDate());
                    if (MyStringUtil.isEmpty(data.getInTypeName())) {
                        xiaJi.setInTypeCode(10001);
                        xiaJi.setInTypeName("正常采购");
                    } else {
                        xiaJi.setInTypeCode(data.getInTypeCode());
                        xiaJi.setInTypeName(data.getInTypeName());
                    }
                    xiaJi.setInTypeCode(data.getInTypeCode());
                    xiaJi.setInTypeName(data.getInTypeName());
                    xiaJi.setRebatePrice(data.getRebatePrice());
                    list.add(xiaJi);
                }
            }
            mJsonStr = JSON.toJSONString(list);//拼接的json字符串
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 下单提交数据成功
     */
    public void submitSuccess(int tp) {
        try {
            Intent data = new Intent();
            data.putExtra(ConstantUtils.Intent.SUCCESS, true);
            setResult(0, data);
            BusProvider.getBus().post(new OrderEvent());
            //关闭界面
            ToastUtils.showCustomToast("操作成功");
            ActivityManager.getInstance().closeActivity(context);
            mIsCache = false;
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    /**
     * /显示订单信息
     */
    private DStep5Bean mDStep5Bean;

    public void doUI(PurchaseOrderBean data, DStep5Bean dStep5Bean) {
        try {
            //供应商
            mCurrentObjectEvent = new ObjectEvent();
            mCurrentObjectEvent.setId(data.getProId());
            mCurrentObjectEvent.setType(data.getProType());
            mCurrentObjectEvent.setName(data.getProName());

            mDStep5Bean = dStep5Bean;//缓存对象
            stkId = "" + data.getStkId();
            mTvKhNm.setText(data.getProName());
            mEtBz.setText(data.getRemarks());
            mEtZdzk.setText("" + data.getDiscount());
            mTvZje.setText("" + data.getTotalAmt());
            mTvCjje.setText("" + data.getDisAmt());
            mTvTime.setText(data.getInDate());
            mTvStorage.setText(data.getStkName());
            if(data.getCheckAutoPrice() != null && 1 == data.getCheckAutoPrice()){
                mCbCheckAutoPrice.setChecked(true);
            }
            //status=-2暂存可以修改
            if (!"-2".equals(""+data.getStatus())) {
                mHeadRight.setVisibility(View.INVISIBLE);
            }

            //***********商品信息***************
            List<PurchaseOrderSubBean> list = data.getList();
            if (null == list || list.isEmpty()) {
                return;
            }
            List<ShopInfoBean.Data> dataList = new ArrayList<>();
            for (PurchaseOrderSubBean subBean : list) {
                ShopInfoBean.Data bean = new ShopInfoBean.Data();
                bean.setWareId(Integer.valueOf(subBean.getWareId()));
                bean.setWareDj("" + subBean.getPrice());
                bean.setWareNm(subBean.getWareNm());
                bean.setCurrentDw(subBean.getUnitName());
                bean.setCurrentCount("" + subBean.getQty());
                bean.setCurrentPrice(subBean.getPrice()+"");
                bean.setCurrentCode(subBean.getBeUnit());
                bean.setWareGg(subBean.getWareGg());
                bean.setCurrentBz(subBean.getRemarks());
                bean.setCurrentProductDate(subBean.getProductDate());


                //商品原始数据
                bean.setHsNum(subBean.getHsNum() + "");
                bean.setWareDw(subBean.getWareDw());
                bean.setMinUnit(subBean.getMinUnit());
                bean.setMaxUnitCode(subBean.getMaxUnitCode());
                bean.setMinUnitCode(subBean.getMinUnitCode());

                if(!MyStringUtil.isEmpty(subBean.getInTypeCode() + "")){
                    bean.setInTypeCode(Integer.valueOf(subBean.getInTypeCode()));
                }
                bean.setInTypeName(subBean.getInTypeName());
                if(!MyStringUtil.isEmpty(subBean.getRebatePrice() + "")){
                    bean.setRebatePrice(subBean.getRebatePrice() + "");
                }
                dataList.add(bean);
            }
            mLeftAdapter.addData(dataList);
            mRightAdapter.addData(dataList);
            refreshAdapterRight();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    private int mErrorCount;

    public void submitDataError() {
        mErrorCount++;
        if (mErrorCount > 1) {
//            showDialogCache();
        }
    }

//    public void showDialogCache() {
//        NormalDialog dialog = new NormalDialog(context);
//        dialog.content("是否数据缓存到本地,待网络正常后，自动缓存数据?").show();
//        dialog.setOnBtnClickL(null, new OnBtnClickL() {
//            @Override
//            public void onBtnClick() {
//                saveCacheData();
//                mIsCache = false;
//            }
//        });
//    }

    private boolean mIsCache = true;//是否要缓存（默认是）什么时候设置false：1提交成功 2已缓存

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsCache) {
//            saveCacheData();
        }
    }

//    //保存缓存数据
//    public void saveCacheData() {
//        try {
//            if (1 == autoType) {
//                ToastUtils.showLongCustomToast("保存数据到缓存中，并自动上传缓存数据");
//            }
//
//            getJsonStr();//获取商品列表拼接的json
//            if (!MyStringUtil.isEmpty(mJsonStr) && !"[]".equals(mJsonStr)) {
//                if (mDStep5Bean == null) {
//                    mDStep5Bean = new DStep5Bean();
//                }
//                mRemoStr = mEtBz.getText().toString().trim();
//                mZdzkStr = mEtZdzk.getText().toString().trim();
//                mZjeStr = mTvZje.getText().toString().trim();
//                mCjjeStr = mTvCjje.getText().toString().trim();
//                mShTimeStr = mTvTime.getText().toString().trim();
//                mDStep5Bean.setAutoType(autoType);
//                mDStep5Bean.setUserId(SPUtils.getID());
//                mDStep5Bean.setCompanyId(SPUtils.getCompanyId());
////                mDStep5Bean.setType("" + mOrderType);
////                mDStep5Bean.setOrderId("" + mOrderId);
////                mDStep5Bean.setCid(cId);
////                mDStep5Bean.setKhNm(mKhNm);
//                mDStep5Bean.setRemo(mRemoStr);
//                mDStep5Bean.setShTime(mShTimeStr);
//                mDStep5Bean.setStkId(stkId);
//                mDStep5Bean.setStkName(mTvStorage.getText().toString().trim());
//                mDStep5Bean.setZje(mZjeStr);
//                mDStep5Bean.setZdzk(mZdzkStr);
//                mDStep5Bean.setCjje(mCjjeStr);
//                mDStep5Bean.setOrderxx(mJsonStr);
//                mDStep5Bean.setTime(MyTimeUtils.getNowTime());
//                MyDataUtils.getInstance().saveStep5(mDStep5Bean);
//            }
//
//            Intent data = new Intent();
//            data.putExtra(ConstantUtils.Intent.SUCCESS, true);
//            data.putExtra(ConstantUtils.Intent.COUNT, 2);
//            setResult(0, data);
//            ActivityManager.getInstance().closeActivity(context);
//        } catch (Exception e) {
//            ToastUtils.showError(e);
//        }
//    }

    //避免重复的token
    private String mQueryToken;

    public void doToken(String data) {
        mQueryToken = data;
    }


}
