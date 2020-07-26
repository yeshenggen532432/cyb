package com.qwb.view.car.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.deadline.statebutton.StateButton;
import com.qwb.common.OrderTypeEnum;
import com.qwb.event.CrashEvent;
import com.qwb.utils.MyDividerUtil;
import com.qwb.utils.MyTableUtil;
import com.qwb.view.car.doui.DoCarOrder;
import com.qwb.view.car.parsent.PCarOrder;
import com.qwb.view.step.model.OrderConfigBean;
import com.qwb.widget.MyCrashHandler;
import com.qwb.event.CarOrderChooseClientEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.print.util.MyPrintUtil;
import com.qwb.utils.ToastUtils;
import com.qwb.view.step.adapter.Step5Left2Adapter;
import com.qwb.view.step.adapter.Step5Right2Adapter;
import com.qwb.view.step.adapter.Step5SearchWareAdapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.view.step.model.QueryXstypeBean;
import com.qwb.view.step.model.QueryBforderBean;
import com.qwb.view.stk.StorageBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.qwb.widget.table.TableHorizontalScrollView;
import com.xmsx.qiweibao.R;
import com.zyyoona7.lib.EasyPopup;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.functions.Consumer;

/**
 * 车销下单界面
 */
public class CarOrderActivity extends XActivity<PCarOrder> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_car_order;
    }
    
    @Override
    public PCarOrder newP() {
        return new PCarOrder();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 初始化EventBus
     */
    @Override
    public void bindEvent() {
        BusProvider.getBus().toFlowable(CarOrderChooseClientEvent.class)
                .subscribe(new Consumer<CarOrderChooseClientEvent>() {
                    @Override
                    public void accept(CarOrderChooseClientEvent event) throws Exception {
                        if (event.getTag() == ConstantUtils.Event.TAG_CAR_CHOOSE_CLIENT) {
                           mDo.doBindEvent(event);
                        }
                    }
                });
        BusProvider.getBus()
                .toFlowable(CrashEvent.class)
                .subscribe(new Consumer<CrashEvent>() {
                    @Override
                    public void accept(CrashEvent applyEvent) throws Exception {
                        if (applyEvent.getTag() == ConstantUtils.Event.TAG_CRASH) {
                            if(mOrderType == OrderTypeEnum.ORDER_CAR_ADD.getType()){
                                mDo.saveCacheData();
                            }
                        }
                    }
                });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new MyCrashHandler());//捕获异常
        initDo();
        initIntent();
        initUI();
        doIntent();
        mDo.createPopupSearchGoods();//语音搜索商品
        queryToken();
        queryOrderConfigWeb();
    }

    public void queryCacheData(){
        getP().queryCacheData(String.valueOf(mOrderType), mCustomerId);
    }

    public DoCarOrder mDo;
    public void initDo(){
        mDo = new DoCarOrder(CarOrderActivity.this);
    }

    public void  queryDhOrder(){
        getP().queryDhOrder(context, mOrderId);
    }
    public void  queryStorage(){
        getP().queryStorage(context);
    }
    public void  queryDataKeyWordGoodsList(){
        getP().queryWareListByKeyWord(context, mVoiceResult, true);
    }
    public void  queryXsTp(){
        getP().queryXsTp(context);
    }
    public void  queryToken(){
        getP().queryToken(null);
    }
    public void  queryOrderConfigWeb(){
        getP().queryOrderConfigWeb(context);
    }
    public void  addData(int autoSubmit){
        getP().addData(context, mCustomerId, mOrderType, mOrderId, mJsonStr, mShrStr, mPhoneStr, mAddressStr, mRemoStr,
                mZjeStr, mZdzkStr, mCjjeStr, mShTimeStr, mPszdStr, mStkId, mQueryToken, autoSubmit);
    }


    /**
     * 初始化Intent
     */
    public int mOrderType = 7;// 7.车销下单-添加 8.车销下单-修改和列表
    public String mKhNm;//客户名称
    public String mCustomerId;// 客户ID
    public String mAddress;
    public String mTel;//手机
    public String mPhone;//固定电话
    public String mLinkman;//联系人或收货人
    public int mOrderId;//订单id
    public int isMe;//1：我 2：别人
    public String mOrderZt;//订单状态:未审核；审核
    public String mOrderNo;//订单号
    public void initIntent() {
        mDo.initIntent();
    }

    /**
     * 处理Intent
     */
    public void doIntent() {
        mDo.doIntent();
    }

    /**
     * 初始化UI
     */
    @BindView(R.id.parent)
    public View mParent;
    public void initUI() {
        initHead();
        initTableView();
        initBottomUI();
        initOtherUI();
    }

    @BindView(R.id.head_left)
    public View mHeadLeft;
    @BindView(R.id.head_right)
    public View mHeadRight;
    @BindView(R.id.head_right2)
    public View mHeadRight2;
    @BindView(R.id.tv_head_right)
    public TextView mTvHeadRight;
    @BindView(R.id.tv_head_title)
    public TextView mTvHeadTitle;
    @BindView(R.id.tv_head_right2)
    public TextView mTvHeadRight2;
    public void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mTvHeadTitle.setText("车销下单");
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        //提交
        mHeadRight.setOnClickListener(new OnNoMoreClickListener() {
            @Override
            protected void OnMoreClick(View view) {
                mDo.addData(1);
            }
        });
        //提交并打印;打印
        mHeadRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDo.addDataAndPrint();
            }
        });
    }

    @BindView(R.id.tv_khNm)
    public TextView mTvKhNm;
    @BindView(R.id.et_shr)
    public EditText mEtShr;
    @BindView(R.id.et_address)
    public EditText mEtAddress;
    @BindView(R.id.et_phone)
    public EditText mEtPhone;
    @BindView(R.id.et_bz)
    public EditText mEtBz;
    @BindView(R.id.layout_show)
    public View mLayoutShow;
    @BindView(R.id.layout_hide)
    public View mLayoutHide;
    @BindView(R.id.iv_show)
    public ImageView mIvShow;
    @BindView(R.id.tv_storage)
    public TextView mTvStorage;
    @BindView(R.id.tv_time)
    public TextView mTvTime;
    @BindView(R.id.tv_pszd)
    public TextView mTvPszd;
    @BindView(R.id.tv_shr_lable)
    public TextView mTvShrLable;
    @BindView(R.id.layout_storage)
    public View mLayoutStorage;
    @BindView(R.id.tv_time_lable)
    public TextView mTvTimeLable;
    @BindView(R.id.layout_pszd)
    public View mLayoutPszd;
    @BindView(R.id.layout_shtime_pszd)
    public View mLayoutShTimePszd;
    public boolean isHide = true;
    public void initOtherUI() {
        //选择客户
        mTvKhNm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOrderType == OrderTypeEnum.ORDER_CAR_ADD.getType()){
                    ActivityManager.getInstance().jumpToClientManagerActivity(context, OrderTypeEnum.ORDER_CAR_ADD);
                }
            }
        });
        //显示和隐藏：电话，地址，仓库
        mLayoutShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDo.doShowHide();
            }
        });
        //选择仓库
        mTvStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDo.doChooseStorage();
            }
        });
        //选择时间
        mTvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDo.showDialogTime();
            }
        });
        //配送指定
        mTvPszd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDo.showDialogPszd();
            }
        });
    }


    @BindView(R.id.rv_left)
    public RecyclerView mRvLeft;
    @BindView(R.id.rv_right)
    public RecyclerView mRvRight;
    @BindView(R.id.sv_ware)
    public TableHorizontalScrollView mSvWare;
    public Step5Left2Adapter mLeftAdapter;
    public Step5Right2Adapter mRightAdapter;
    @BindView(R.id.tv_table_title_left)
    public TextView mTvTableTitleLeft;
    @BindView(R.id.tv_table_title_8_produce_date)
    public TextView mTvTableTitleProduceDate;
    @BindView(R.id.tv_table_title_7)
    public TextView mTvTableTitle7Del;
    public ShopInfoBean.Data mCurrentItem;
    public int mCurrentPosition;
    public void initTableView() {
        //left
        mRvLeft.setLayoutManager(new LinearLayoutManager(this));
        mRvLeft.addItemDecoration(MyDividerUtil.getH05CGray(context));
        mLeftAdapter = new Step5Left2Adapter();
        mRvLeft.setAdapter(mLeftAdapter);
        //right
        mRvRight.setLayoutManager(new LinearLayoutManager(this));
        mRvRight.addItemDecoration(MyDividerUtil.getH05CGray(context));
        mRightAdapter = new Step5Right2Adapter(0);
        mRvRight.setAdapter(mRightAdapter);

        //设置两个列表的同步滚动
        MyTableUtil.getInstance().setSyncScrollListener(mRvLeft, mRvRight, mSvWare);

        mRightAdapter.setOnChildListener(new Step5Right2Adapter.OnChildListener() {
            @Override
            public void onChildListener(int tag, int position, ShopInfoBean.Data item) {
                try {
                    mCurrentItem = item;
                    mCurrentPosition = position;
                    switch (tag) {
                        case Step5Right2Adapter.TAG_DW:
                            mDo.showDialogChangeDw(item, position);
                            break;
                        case Step5Right2Adapter.TAG_XSTP:
                            mDo.doChooseXstp();
                            break;
                        case Step5Right2Adapter.TAG_DEl:
                            mDo.showDialogDel();
                            break;
                        case Step5Right2Adapter.TAG_COUNT:
                        case Step5Right2Adapter.TAG_PRICE:
                            mDo.setSumMoney();
                            break;
                    }
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });

        //添加商品
        mTvTableTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDo.doChooseWare();
            }
        });

        mTvTableTitle7Del.setVisibility(View.VISIBLE);
        mTvTableTitleProduceDate.setVisibility(View.GONE);

    }


    @BindView(R.id.et_zdzk)
    public EditText mEtZdzk;
    @BindView(R.id.et_zdzk_percent)
    public  EditText mEtZdzkParent;
    @BindView(R.id.btn_zdzk_convert)
    public StateButton mBtnZdzkConvert;
    @BindView(R.id.tv_zje)
    public TextView mTvZje;
    @BindView(R.id.tv_cjje)
    public TextView mTvCjje;
    @BindView(R.id.layout_zdzk)
    public View mLayoutZdzk;
    @BindView(R.id.layout_voice)
    public View mLayoutVoice;

    public void initBottomUI() {
        //百分比转整单折扣金额
        mBtnZdzkConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDo.moneyConvert();
            }
        });
        // 整单折扣
        mEtZdzk.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable input) {
                mDo.doZdzkListener(input.toString().trim());
            }
        });
        // 语音
        mLayoutVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDo.showDialogVoice();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDo.doActivityResult(data, requestCode, resultCode);
    }

    /**
     * 刷新表格
     */
    public void refreshAdapterTable() {
        mDo.setRepeatMap();
        mLeftAdapter.notifyDataSetChanged();
        mRightAdapter.notifyDataSetChanged();
        mDo.setSumMoney();
    }

    /**
     * 选择销售类型
     */
    public List<QueryXstypeBean.QueryXstype> mXstpList = new ArrayList<>();
    public void showDialogXstp(List<QueryXstypeBean.QueryXstype> list) {
        mDo.showDialogXstp(list);
    }

    /**
     * 选择仓库
     */
    public List<StorageBean.Storage> mStorageList = new ArrayList<>();
    public String mStkId;//仓库id
    public void showDialogStorage(List<StorageBean.Storage> list){
        mDo.showDialogStorage(list);
    }

    /**
     * 搜索商品后刷新列表（语音和弹窗中的搜索框）
     */
    public void refreshAdapterSearch(List<ShopInfoBean.Data> list, boolean showPopup) {
        mDo.refreshAdapterSearch(list, showPopup);
    }

    /**
     * 下单提交数据成功
     */
    public void doSubmitSuccess() {
        mDo.doSubmitSuccess();
    }

    /**
     * 显示订单信息
     */
    public QueryBforderBean mCurrentData;
    public void doUI(QueryBforderBean data) {
        mDo.doUI(data);
    }

    /**
     * 提交失败
     */
    public int mErrorCount;
    public void doSubmitError() {
        mDo.doSubmitError();
    }

    /**
     * 避免重复的token
     */
    public String mQueryToken;
    public void doToken(String data){
        mQueryToken = data;
    }

    /**
     * 手机端下单配置（拜访下单，订货下单）
     * 1.是历史价还是执行价
     * 2.价格是否可以编辑
     */
    public boolean mEditPrice = true;
    public String mAutoPrice = "1";
    public void doOrderConfig(OrderConfigBean configBean){
        mDo.doOrderConfig(configBean);
    }

    /**
     * 提交并打印
     */
    public void doPrint(QueryBforderBean data) {
        MyPrintUtil.getInstance().print(context, data.getOrderNo(), data.getKhNm(), data, OrderTypeEnum.ORDER_CAR_ADD.getType());
        doSubmitSuccess();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDo.doCacheData();
    }

    //对话框-配送指定
    public List<String> mPszdList = new ArrayList<>();

    /**
     * 语音搜索商品
     */
    public EasyPopup mPopupSearchWare;
    public ListView mLvSearchWare;
    public EditText mEtSearchGoods;
    public List<ShopInfoBean.Data> mSearchWareList = new ArrayList<>();
    public Step5SearchWareAdapter mAdapterSearWare;
    public String mVoiceResult;
    public boolean mIsCache = true;//是否要缓存（默认是）什么时候设置false：1提交成功 2已缓存

    /**
     * 所有商品的总价求和
     */
    public double zjeDouble;//总金额
    public double cjjeDouble;//成交金额

    /**
     * 提交或修改数据
     */
    public String mJsonStr;
    public String mShrStr;
    public String mPhoneStr;
    public String mAddressStr;
    public String mRemoStr;
    public String mZdzkStr;
    public String mZjeStr;
    public String mCjjeStr;
    public String mShTimeStr;
    public String mPszdStr;

}
