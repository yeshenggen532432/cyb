package com.qwb.view.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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
import com.qwb.common.OrderTypeEnum;
import com.qwb.view.shop.parsent.PShopStep;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDoubleUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.view.print.util.MyPrintUtil;
import com.qwb.utils.ToastUtils;
import com.qwb.view.step.adapter.Step5Left2Adapter;
import com.qwb.view.step.adapter.Step5Right2Adapter;
import com.qwb.view.step.adapter.Step5SearchWareAdapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.view.step.model.QueryXstypeBean;
import com.qwb.db.DStep5Bean;
import com.qwb.view.step.model.QueryBforderBean;
import com.qwb.view.stk.StorageBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.view.step.model.XiaJi;
import com.qwb.utils.MyKeyboardUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
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
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 创建描述：商城订单
 */
public class ShopStepActivity extends XActivity<PShopStep> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_step_shop;
    }

    @Override
    public PShopStep newP() {
        return new PShopStep();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        doIntent();
    }

    /**
     * 初始化Intent
     */
    private int mOrderType;// 1：拜访下单(添加或修改) 2:电话下单(添加) 3：订货下单列表（查看或修改）4：退货(添加或修改) 5：退货下单列表（查看或修改）
    private String cId;// 客户ID
    private String mAddress;
    private String mTel;//手机
    private String mPhone;//固定电话
    private String mLinkman;//联系人或收货人
    private int mOrderId;//订单id
    private String mCompanyId;//公司id
    private String mOrderNo;
    private String mKhNm;
    private QueryBforderBean mBean;
    private void initIntent() {
        Intent intent = getIntent();
        if (null != intent) {
            // 1：拜访下单(添加或修改) 2:电话下单(添加) 3：订货下单列表（查看或修改）4：退货(添加或修改) 5：退货下单列表（查看或修改）
            mOrderType = intent.getIntExtra(ConstantUtils.Intent.ORDER_TYPE, 1);
            cId = intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
            mAddress = intent.getStringExtra(ConstantUtils.Intent.ADDRESS);
            mTel = intent.getStringExtra(ConstantUtils.Intent.TEL);//手机，没有手机号时会显示规定电话
            mPhone = intent.getStringExtra(ConstantUtils.Intent.MOBILE);//固定电话
            mLinkman = intent.getStringExtra(ConstantUtils.Intent.LINKMAN);
            //商城详情
            if (ConstantUtils.Order.O_SC == mOrderType) {
                mOrderId = intent.getIntExtra(ConstantUtils.Intent.ORDER_ID, -1);
                mCompanyId = intent.getStringExtra(ConstantUtils.Intent.COMPANY_ID);
            }
        }
    }

    //根据不同的类型处理不同的显示
    private void doIntent() {
        mEtAddress.setText(mAddress);
        mEtShr.setText(mLinkman);
        if(!MyStringUtil.isEmpty(mTel)){
            mEtPhone.setText(mTel);
        }
        if(!MyStringUtil.isEmpty(mPhone)){
            mEtPhone.setText(mPhone);
        }
        switch (mOrderType){
            case ConstantUtils.Order.O_SC://商城订单详情
                mTvHeadTitle.setText("商城订单");
                mHeadRight .setVisibility(View.INVISIBLE);
                getP().queryScOrder(context, mOrderId, mCompanyId);
                break;
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
    @BindView(R.id.et_shr)
    EditText mEtShr;
    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_bz)
    EditText mEtBz;
    @BindView(R.id.layout_show)
    View mLayoutShow;
    @BindView(R.id.layout_hide)
    View mLayoutHide;
    @BindView(R.id.iv_show)
    ImageView mIvShow;
    @BindView(R.id.tv_storage)
    TextView mTvStorage;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_pszd)
    TextView mTvPszd;
    @BindView(R.id.tv_shr_lable)
    TextView mTvShrLable;
    @BindView(R.id.layout_storage)
    View mLayoutStorage;
    @BindView(R.id.tv_time_lable)
    TextView mTvTimeLable;
    @BindView(R.id.layout_pszd)
    View mLayoutPszd;
    @BindView(R.id.et_freight)
    EditText mEtFreight;
    private boolean isHide = true;
    private void initOtherUI() {
        //显示和隐藏：电话，地址，仓库
        mLayoutShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(isHide){
                        mLayoutHide.setVisibility(View.VISIBLE);
                        mIvShow.setImageResource(R.drawable.icon_jian);
                    }else{
                        mLayoutHide.setVisibility(View.GONE);
                        mIvShow.setImageResource(R.drawable.icon_jia);
                    }
                    isHide = !isHide;
                }catch (Exception e){
                    ToastUtils.showError(e);
                }

            }
        });
        //选择仓库
        mTvStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    if(null == mStorageDatas || mStorageDatas.isEmpty()){
//                        getP().queryStorage(context);
//                    }else{
//                        showDialogStorage(mStorageDatas);
//                    }
//                }catch (Exception e){
//                    ToastUtils.showError(e);
//                }
            }
        });
        //选择时间
        mTvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogTime();
            }
        });
        //配送指定
        mTvPszd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPszd();
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
    @BindView(R.id.tv_head_right2)
    TextView mTvHeadRight2;
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
        mTvHeadTitle.setText("商城订单");
        mTvHeadRight.setText("确认\n修改");
        mTvHeadRight2.setText("打印");
        //OnNoMoreClickListener:避免重复点击
        mHeadRight.setOnClickListener(new OnNoMoreClickListener() {
            @Override
            protected void OnMoreClick(View view) {
                if(mHeadRight.getVisibility() == View.VISIBLE){
                    addData();
                }
            }
        });
        mHeadRight2.setOnClickListener(new OnNoMoreClickListener() {
            @Override
            protected void OnMoreClick(View view) {
                MyPrintUtil.getInstance().print(context, mOrderNo, mKhNm, mBean, OrderTypeEnum.ORDER_SC.getType());
            }
        });
    }

    private RecyclerView mRvLeft;
    private RecyclerView mRvRight;
    private TableHorizontalScrollView mSvWare;
    private Step5Left2Adapter mLeftAdapter;
    private Step5Right2Adapter mRightAdapter;
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
        mRightAdapter = new Step5Right2Adapter(0);
        mRvRight.setAdapter(mRightAdapter);
        mRightAdapter.setOnChildListener(new Step5Right2Adapter.OnChildListener() {
            @Override
            public void onChildListener(int tag, int position, ShopInfoBean.Data item) {
                try {
                    mCurrentItem = item;
                    mCurrentPosition = position;
                    switch (tag){
                        case Step5Right2Adapter.TAG_DW:
                            showDialogChangeDw(item, position);
                            break;
                        case Step5Right2Adapter.TAG_XSTP:
                            if(null == mXstpDatas || mXstpDatas.isEmpty()){
                                getP().queryXsTp(context);
                            }else{
                                showDialogXstp(mXstpDatas);
                            }
                            break;
                        case Step5Right2Adapter.TAG_DEl:
                            showDialogDel();
                            break;
                        case Step5Right2Adapter.TAG_COUNT:
                        case Step5Right2Adapter.TAG_PRICE:
                            setSumMoney();
                            break;
                    }
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });

        //设置两个列表的同步滚动
        setSyncScrollListener();

        switch (mOrderType){
            //商城
            case ConstantUtils.Order.O_SC:
                mTvTableTitle7Del.setVisibility(View.GONE);
                mTvTableTitleProduceDate.setVisibility(View.GONE);
                break;
        }
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
    @BindView(R.id.tv_order_amount)
    TextView mTvOrderAmount;//促销总额
    @BindView(R.id.layout_zdzk)
    View mLayoutZdzk;
    @BindView(R.id.layout_voice)
    View mLayoutVoice;
    private void initBottomUI() {
        mLayoutZdzk.setVisibility(View.GONE);
        mLayoutVoice.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ConstantUtils.Intent.REQUEST_STEP_5_CHOOSE_GOODS && resultCode==ConstantUtils.Intent.RESULT_CODE_CHOOSE_GOODS){
            if(data!=null){
                try {
                    boolean editPrice = data.getBooleanExtra(ConstantUtils.Intent.EDIT_PRICE,true);
                    ArrayList<ShopInfoBean.Data> datas = data.getParcelableArrayListExtra(ConstantUtils.Intent.CHOOSE_WARE_NEW);
                    if(datas!=null && datas.size() > 0){
                        mRightAdapter.setEditPrice(editPrice);
                        mLeftAdapter.addData(datas);
                        mRightAdapter.addData(datas);
                        refreshAdapterRight();
                    }
                }catch (Exception e){
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
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }
    /**
     * 标记商品的个数（重复商品颜色变）--1:选择商品后 2：删除商品 3：获取订单数据
     */
    public void setRepeatMap(){
        try {
            Map<Integer,Integer> repeatMap = new HashMap<>();
            for (ShopInfoBean.Data data : mLeftAdapter.getData()) {
                int wareId = data.getWareId();
                if(repeatMap.containsKey(wareId)){
                    int qty = repeatMap.get(wareId);
                    repeatMap.put(wareId,qty + 1);
                }else{
                    repeatMap.put(wareId,1);
                }
            }
            mLeftAdapter.setRepeatMap(repeatMap);
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }


    //--------------------------------dialog:开始---------------------------------------------------
    private ShopInfoBean.Data mCurrentItem;
    private int mCurrentPosition;
    //对话框-切换单位
    private void showDialogChangeDw(final ShopInfoBean.Data item, final int position){
        try {
            String wareNm = item.getWareNm();
            String maxDw = item.getWareDw();
            String minDw = item.getMinUnit();
            final ArrayList<DialogMenuItem> items = new ArrayList<>();
            if(!MyStringUtil.isEmpty(maxDw)){
                items.add(new DialogMenuItem(maxDw, 0));
            }
            if(!MyStringUtil.isEmpty(minDw)){
                items.add(new DialogMenuItem(minDw, 1));
            }
            NormalListDialog dialog = new NormalListDialog(context,items);
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
                        if(!dw.equals(operName)){
                            item.setCurrentDw(operName);
                            //单位，单位代码，价格，总价，总金额
                            if(0 == id){
                                item.setCurrentCode(maxCode);
                                if(!MyStringUtil.isEmpty(price)){
                                    double newPrice = Double.valueOf(price) * Double.valueOf(hsNum);
                                    item.setCurrentPrice(""+newPrice);
                                }
                            }else{
                                item.setCurrentCode(minCode);
                                if(!MyStringUtil.isEmpty(price)){
                                    double newPrice = Double.valueOf(price) / Double.valueOf(hsNum);
                                    item.setCurrentPrice(""+newPrice);
                                }
                            }
                            mRightAdapter.getData().set(position,item);
                            refreshAdapterRight();
                        }
                        MyKeyboardUtil.closeKeyboard(context);//强制关闭软键盘
                    }catch (Exception e){
                        ToastUtils.showError(e);
                    }
                }
            });
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //对话框-销售类型
    private List<QueryXstypeBean.QueryXstype> mXstpDatas = new ArrayList<>();
    public void showDialogXstp(List<QueryXstypeBean.QueryXstype> xstpDatas){
        try {
            this.mXstpDatas = xstpDatas;
            String wareNm = mCurrentItem.getWareNm();
            if(null == mXstpDatas || mXstpDatas.isEmpty()){
                ToastUtils.showCustomToast("没有销售类型");
                return;
            }

            final ArrayList<DialogMenuItem> dialogMenuItems = new ArrayList<>();
            for(QueryXstypeBean.QueryXstype xstp : mXstpDatas) {
                dialogMenuItems.add(new DialogMenuItem(xstp.getXstpNm(), xstp.getId()));
            }
            NormalListDialog dialog = new NormalListDialog(context,dialogMenuItems);
            dialog.title(wareNm).show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {

                    try{
                        String currentXstp = mCurrentItem.getCurrentXstp();
                        String xstpNm = dialogMenuItems.get(i).mOperName;
                        if(!xstpNm.equals(currentXstp)){
                            // 当选择"正常销售"时，价格为系统默认：其他时价格为0
                            if ("正常销售".equals(xstpNm)) {
                                mCurrentItem.setCurrentPrice(mCurrentItem.getWareDj());
                            } else {
                                mCurrentItem.setCurrentPrice("0");
                            }
                            mCurrentItem.setCurrentXstp(xstpNm);
                            mRightAdapter.getData().set(mCurrentPosition, mCurrentItem);
                            refreshAdapterRight();
                        }
                    }catch (Exception e){
                        ToastUtils.showError(e);
                    }
                    MyKeyboardUtil.closeKeyboard(context);//强制关闭软键盘
                }
            });
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //对话框-删除商品
    public void showDialogDel(){
        NormalDialog dialog = new NormalDialog(context);
        dialog.content("你确定删除''"+mCurrentItem.getWareNm()+"''吗？").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                try {
                    mRightAdapter.getData().remove(mCurrentPosition);
                    mLeftAdapter.getData().remove(mCurrentPosition);
                    refreshAdapterRight();
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });
    }

    //对话框-仓库
    private List<StorageBean.Storage> mStorageDatas = new ArrayList<>();
    private String stkId;//仓库id
    public void showDialogStorage(List<StorageBean.Storage> storageDatas){
        try {
            this.mStorageDatas = storageDatas;
            if(null == mStorageDatas || mStorageDatas.isEmpty()){
                ToastUtils.showCustomToast("没有仓库可以选择");
                return;
            }

            final ArrayList<DialogMenuItem> dialogMenuItems = new ArrayList<>();
            for(StorageBean.Storage storage : mStorageDatas) {
                dialogMenuItems.add(new DialogMenuItem(storage.getStkName(), storage.getId()));
            }
            NormalListDialog dialog = new NormalListDialog(context,dialogMenuItems);
            dialog.title("选择仓库").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                    try{
                        String storageNm = dialogMenuItems.get(i).mOperName;
                        mTvStorage.setText(storageNm);
                        stkId = dialogMenuItems.get(i).mResId + "";
                    }catch (Exception e){
                        ToastUtils.showError(e);
                    }
                }
            });
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //对话框-配送指定
    private List<String> mPszdDatas = new ArrayList<>();
    public void showDialogPszd(){
        try {
            if(null == mPszdDatas || mPszdDatas.isEmpty()){
                mPszdDatas.add("公司直送");
                mPszdDatas.add("转二批配送");
            }

            final ArrayList<DialogMenuItem> dialogMenuItems = new ArrayList<>();
            for(String pszd : mPszdDatas) {
                dialogMenuItems.add(new DialogMenuItem(pszd, 0));
            }
            NormalListDialog dialog = new NormalListDialog(context,dialogMenuItems);
            dialog.title("选择配送指定").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                    try{
                        String pzsdNm = dialogMenuItems.get(i).mOperName;
                        mTvPszd.setText(pzsdNm);
                    }catch (Exception e){
                        ToastUtils.showError(e);
                    }
                }
            });
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //对话框-选择时间
    private int startYear,startMonth,startDay,startHour,startMin;
    private void showDialogTime() {
        if(startYear==0){
            startYear= MyTimeUtils.getYear();
            startMonth= MyTimeUtils.getMonth();
            startDay= MyTimeUtils.getDay();
            startHour= MyTimeUtils.getHour();
            startMin= MyTimeUtils.getMin();
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
                        }catch (Exception e){
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

    /**
     * 语音搜索商品
     */
    private EasyPopup mPopupSearchWare;
    private ListView mLvSearchWare;
    private EditText mEtSearchGoods;
    public List<ShopInfoBean.Data> mSearchWareList = new ArrayList<>();
    private Step5SearchWareAdapter mAdapterSearWare;

    //搜索商品后刷新列表（语音和弹窗中的搜索框）
    public void refreshAdapterSearch(List<ShopInfoBean.Data> list, boolean showPopup){
        try {
            //一个直接添加商品（语音的）；多个弹出选择
            if(null != list && list.size() ==1 && showPopup){
                doAddWareRefreshAdapter(list.get(0));
            }else{
                mEtSearchGoods.setText(mVoiceResult);
                mSearchWareList.clear();
                mSearchWareList.addAll(list);
                mAdapterSearWare.notifyDataSetChanged();
                if(showPopup){
                    mPopupSearchWare.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //添加商品到表格中：刷新table
    private void doAddWareRefreshAdapter(ShopInfoBean.Data data) {
        try {
            //TODO 主要有：单位，单位代码，数量，价格，备注
            int sunitFront = data.getSunitFront();//1辅助单位
            data.setCurrentCount("1");
            if(1 == sunitFront){
                data.setCurrentCode(data.getMinUnitCode());
                data.setCurrentDw(data.getMinUnit());
                data.setCurrentPrice((Double.valueOf(data.getWareDj()) / Double.valueOf(data.getHsNum())) +"");
            }else{
                data.setCurrentCode(data.getMaxUnitCode());
                data.setCurrentDw(data.getWareDw());
                data.setCurrentPrice(data.getWareDj());
            }
            data.setCurrentXstp("正常销售");
            data.setCurrentBz("");
            mLeftAdapter.addData(data);
            mRightAdapter.addData(data);
            refreshAdapterRight();
        }catch (Exception e){
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
        try{
            double  cjjeDouble = getTableSumMoney();
            double  zjeDouble = getSumMoney();
            mTvCjje.setText(String.valueOf(cjjeDouble));
            mTvZje.setText(String.valueOf(MyDoubleUtils.getDecimal(zjeDouble)));
            mTvOrderAmount.setText(String.valueOf(MyDoubleUtils.getDecimal(cjjeDouble + mBean.getFreight())));
//            // 设置成交金额 和 百分比
//            String zdzk = mEtZdzk.getText().toString().trim();
//            if (!MyStringUtil.isEmpty(zdzk) && zjeDouble > 0) {// 条件得改
//                double zdzkDouble =Double.valueOf(zdzk);
//                double percent = zdzkDouble * 100 / zjeDouble;
//                //计算百分比
//                mEtZdzkParent.setText(String.valueOf(MyDoubleUtils.getDecimal(percent)));
//                //计算成交金额
//                if (zjeDouble - zdzkDouble > 0) {
//                    // 如果:"整单折扣"不能大于"总金额"
//                    mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(zjeDouble - zdzkDouble)));
//                } else {
//                    cjjeDouble = zjeDouble;
//                    mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(cjjeDouble)));
//                }
//            } else {
//                cjjeDouble = zjeDouble;
//                mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(cjjeDouble)));
//            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //获取table中总金额
    private double getTableSumMoney(){
        double sum = 0.0;
        try {
            List<ShopInfoBean.Data> dataList = mRightAdapter.getData();
            if(null == dataList || dataList.isEmpty()){
                return sum;
            }
            for (ShopInfoBean.Data item : dataList) {
                String count = item.getCurrentCount();
                String price = item.getCurrentPrice();
                if(!MyStringUtil.isEmpty(count) && !MyStringUtil.isEmpty(price)){
                    double zj = Double.valueOf(count) * Double.valueOf(price);
                    sum += zj;
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
        return sum;
    }

    //获取table中总金额
    private double getSumMoney(){
        double sum = 0.0;
        try {
            List<ShopInfoBean.Data> dataList = mRightAdapter.getData();
            if(null == dataList || dataList.isEmpty()){
                return sum;
            }
            for (ShopInfoBean.Data item : dataList) {
                String count = item.getCurrentCount();
                Double price = item.getWareDjOriginal();
                if(!MyStringUtil.isEmpty(count) && !MyStringUtil.isEmpty(""+price)){
                    double zj = Double.valueOf(count) * Double.valueOf(price);
                    sum += zj;
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
        return sum;
    }


    //TODO ******************************接口******************************
    /**
     * 提交或修改数据
     */
    private String mJsonStr;
    private String mShrStr;
    private String mPhoneStr;
    private String mAddressStr;
    private String mRemoStr;
    private String mZdzkStr;
    private String mZjeStr;
    private String mCjjeStr;
    private String mShTimeStr;
    private String mPszdStr;
    private void addData() {
        try {
            List<ShopInfoBean.Data> dataList = mRightAdapter.getData();
            if(null != dataList && dataList.isEmpty()){
                ToastUtils.showLongCustomToast("请添加商品+");
                return;
            }
            getJsonStr();//获取商品列表拼接的json
            mShrStr = mEtShr.getText().toString().trim();
            mPhoneStr = mEtPhone.getText().toString().trim();
            mAddressStr = mEtAddress.getText().toString().trim();
            mRemoStr = mEtBz.getText().toString().trim();
            mZdzkStr = mEtZdzk.getText().toString().trim();
            mZjeStr = mTvZje.getText().toString().trim();
            mCjjeStr = mTvCjje.getText().toString().trim();
            mShTimeStr = mTvTime.getText().toString().trim();
            mPszdStr = mTvPszd.getText().toString().trim();
            String freight = mEtFreight.getText().toString().trim();

            getP().addData(context, cId, mOrderType, mOrderId,mJsonStr,mShrStr,mPhoneStr,mAddressStr,mRemoStr,mZjeStr,mZdzkStr,mCjjeStr,mShTimeStr,mPszdStr,stkId,freight);
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    /**
     * 获取商品列表拼接的json
     */
    private void getJsonStr() {
        try {
            // 品项，销售类型，数量，单位，单价，总价，操作
            List<XiaJi> list = new ArrayList<>();
            list.clear();
            List<ShopInfoBean.Data> dataList = mRightAdapter.getData();
            if(dataList != null && dataList.size() > 0){
                for (ShopInfoBean.Data data: dataList) {
                    XiaJi xiaJi = new XiaJi();
                    String count = data.getCurrentCount();
                    String price = data.getCurrentPrice();

                    xiaJi.setId(data.getId());
                    xiaJi.setWareId(data.getWareId() + "");
                    xiaJi.setWareNm(data.getWareNm());
                    xiaJi.setXsTp(data.getCurrentXstp());
                    xiaJi.setWareGg(data.getWareGg());//规格
                    xiaJi.setWareDw(data.getCurrentDw());
                    if(MyStringUtil.isEmpty(count)){
                        count = "0";
                    }
                    xiaJi.setWareNum(count);
                    if(MyStringUtil.isEmpty(price)){
                        price = "0";
                    }
                    xiaJi.setWareDj(price);
                    if(!MyStringUtil.isEmpty(count) && !MyStringUtil.isEmpty(price)){
                        xiaJi.setWareZj(String.valueOf(Double.valueOf(count) * Double.valueOf(price)));
                    }else{
                        xiaJi.setWareZj("0");
                    }
                    xiaJi.setBeUnit(data.getCurrentCode());// 包装单位代码或计量单位代码
                    xiaJi.setRemark(data.getCurrentBz());//备注
                    xiaJi.setProductDate(data.getCurrentProductDate());//生产日期

                    //商品原始数据
                    xiaJi.setMaxUnit(data.getWareDw());
                    xiaJi.setMinUnit(data.getMinUnit());
                    xiaJi.setMaxUnitCode(data.getMaxUnitCode());
                    xiaJi.setMinUnitCode(data.getMinUnitCode());
                    xiaJi.setHsNum(data.getHsNum());
                    list.add(xiaJi);
                }
            }
            mJsonStr = JSON.toJSONString(list);//拼接的json字符串
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    /**
     * 下单提交数据成功
     */
    public void submitSuccess(int tp){
        try {
            Intent data = new Intent();
            data.putExtra(ConstantUtils.Intent.SUCCESS, true);
            setResult(0,data);
            //关闭界面
            ToastUtils.showCustomToast("操作成功");
            ActivityManager.getInstance().closeActivity(context);
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }


    /**
     * /显示订单信息
     */
    public void doUI(QueryBforderBean data, DStep5Bean dStep5Bean){
        try {
            mBean = data;
            mOrderNo = data.getOrderNo();
            mKhNm = data.getKhNm();
            stkId = "" + data.getStkId();
            mOrderId = data.getId();// 订单id
            cId = String.valueOf(data.getCid());// 客户id
            mEtShr.setText(data.getShr());
            mEtPhone.setText(data.getTel());
            mEtAddress.setText(data.getAddress());
            mEtBz.setText(data.getRemo());
            mEtZdzk.setText(data.getZdzk());
            if (!MyStringUtil.isEmpty(data.getShTime())) {
                mTvTime.setText(data.getShTime());
            }
            if (!MyStringUtil.isEmpty(data.getPszd())) {
                mTvPszd.setText(data.getPszd());
            }
            if (!MyStringUtil.isEmpty(data.getStkName())) {
                mTvStorage.setText(data.getStkName());
            }
            if (!MyStringUtil.isEmpty(data.getShTime())) {
                mTvTime.setText(data.getShTime());
            }
            if (!MyStringUtil.isEmpty("" + data.getFreight())) {
                mEtFreight.setText("" + data.getFreight());
            }
            if ("未审核".equals(data.getOrderZt())) {
                mHeadRight.setVisibility(View.VISIBLE);
                mTvTableTitle7Del.setVisibility(View.VISIBLE);
                mTvTableTitleProduceDate.setVisibility(View.GONE);
                mRightAdapter.setDel(true);
            }else {
                mHeadRight.setVisibility(View.INVISIBLE);
                mTvTableTitle7Del.setVisibility(View.GONE);
                mTvTableTitleProduceDate.setVisibility(View.GONE);
                mRightAdapter.setDel(false);
            }

            //商城详情的
            if(ConstantUtils.Order.O_SC == mOrderType){
                mTvKhNm.setText(data.getKhNm());
            }

            //***********商品信息***************
            List<XiaJi> list = data.getList();
            if(null == list || list.isEmpty()){
                return;
            }
            List<ShopInfoBean.Data> dataList = new ArrayList<>();
            for (XiaJi xiaji: list) {
                ShopInfoBean.Data bean = new ShopInfoBean.Data();
                bean.setId(xiaji.getId());
                bean.setWareId(Integer.valueOf(xiaji.getWareId()));
                bean.setWareDj(xiaji.getWareDj());
                bean.setWareNm(xiaji.getWareNm());
                bean.setCurrentXstp(xiaji.getXsTp());
                bean.setCurrentDw(xiaji.getWareDw());
                bean.setCurrentCount(xiaji.getWareNum());
                bean.setCurrentPrice(xiaji.getWareDj());
                bean.setCurrentCode(xiaji.getBeUnit());
                bean.setWareGg(xiaji.getWareGg());
                bean.setCurrentBz(xiaji.getRemark());
                bean.setCurrentProductDate(xiaji.getProductDate());

                //商品原始数据
                bean.setHsNum(xiaji.getHsNum());
                bean.setWareDw(xiaji.getMaxUnit());
                bean.setMinUnit(xiaji.getMinUnit());
                bean.setMaxUnitCode(xiaji.getMaxUnitCode());
                bean.setMinUnitCode(xiaji.getMinUnitCode());
                //商品原价
                bean.setWareDjOriginal(xiaji.getWareDjOriginal());
                dataList.add(bean);
            }
            mLeftAdapter.addData(dataList);
            mRightAdapter.addData(dataList);
            refreshAdapterRight();

            mTvZje.setText(data.getZje());
            mTvCjje.setText(data.getCjje());
            if(!MyStringUtil.isEmpty(""+data.getOrderAmount())){
                mTvOrderAmount.setText(""+data.getOrderAmount());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }










}
