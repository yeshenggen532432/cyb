package com.qwb.view.step.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.deadline.statebutton.StateButton;
import com.qwb.common.OrderListEnum;
import com.qwb.common.OrderTypeEnum;
import com.qwb.event.CrashEvent;
import com.qwb.utils.MyTableUtil;
import com.qwb.view.step.doui.DoStep5;
import com.qwb.widget.MyCrashHandler;
import com.qwb.view.step.model.OrderConfigBean;
import com.qwb.event.CarOrderChooseClientEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.stk.StorageBean;
import com.qwb.utils.ToastUtils;
import com.qwb.view.step.adapter.Step5Left2Adapter;
import com.qwb.view.step.adapter.Step5Right2Adapter;
import com.qwb.view.step.adapter.Step5SearchWareAdapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.db.DStep5Bean;
import com.qwb.view.step.parsent.PStep5;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.qwb.widget.table.TableHorizontalScrollView;
import com.qwb.view.step.model.QueryBforderBean;
import com.qwb.view.step.model.QueryXstypeBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zyyoona7.lib.EasyPopup;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.functions.Consumer;

/**
 * 创建描述：拜访步骤5：下单
 * 默认仓库放在请求缓存数据后面，如果是没有缓存数据才请求默认仓库
 */
public class Step5Activity extends XActivity<PStep5> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_step5;
    }

    @Override
    public PStep5 newP() {
        return new PStep5();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

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
                            mDo.doCacheData();
                        }
                    }
                });
    }

    public void queryBfOrder(){
        getP().queryBfOrder(context, cId, mPdateStr);//获取订单信息
    }
    public void queryDhOrder(){
        getP().queryDhOrder(context, mOrderId);
    }
    public void queryRetreat(){
        getP().queryRetreat(context, mOrderId);
    }
    public void queryScOrder(){
        getP().queryScOrder(context, mOrderId, mCompanyId);
    }
    public void queryDataKeyWordGoodsList(){
        getP().getDataKeyWordGoodsList(context, mVoiceResult, true);
    }
    public void queryStorage(){
        getP().queryStorage(context, true);
    }
    public void queryXsTp(){
        getP().queryXsTp(context);
    }
    public void queryToken(){
        getP().queryToken(null);
    }
    public void queryOrderConfigWeb(){
        getP().queryOrderConfigWeb(context);
    }
    public void delCacheData(){
        getP().delCacheData(String.valueOf(mOrderType), cId);
    }
    public void queryCacheData(){
        getP().queryCacheData(String.valueOf(mOrderType), cId);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new MyCrashHandler());//捕获异常
        initDo();
        initIntent();
        initUI();
        doIntent();
        createPopupSearchGoods();//语音搜索商品
        queryToken();
//        queryOrderConfigWeb();
    }

    public DoStep5 mDo;
    public void initDo(){
        mDo = new DoStep5(Step5Activity.this);
    }

    /**
     * 初始化Intent
     */
    public int mRedMark = 0;// 0.正常单；1.红字单
    public int mAutoSubmit;//是否要自动提交 0：否 1：是（只有在下单界面要"提交"按钮时才自动提交）
    public int mOrderType;// 1：拜访下单(添加或修改) 2:电话下单(添加) 3：订货下单列表（查看或修改）4：退货(添加或修改) 5：退货下单列表（查看或修改）
    public String mKhNm;//客户名称
    public String cId;// 客户ID
    public String mAddress;
    public String mTel;//手机
    public String mPhone;//固定电话
    public String mLinkman;//联系人或收货人
    public String count5 = "0";//0:未上传，1:已上传
    public String mPdateStr;//补拜访时间
    public int mOrderId;//订单id
    public int isMe;//1：我 2：别人
    public String mOrderZt;//订单状态:未审核；审核
    public String mCompanyId;//公司id

    public void initIntent() {
        mDo.initIntent();
    }

    //根据不同的类型处理不同的显示
    public void doIntent() {
        mDo.doIntent();
    }

    /**
     * 初始化UI
     */
    public String mVoiceResult;
    public String mTimeStr;
    public List<StorageBean.Storage> mStorageList = new ArrayList<>();
    public String stkId;//仓库id
    public List<QueryXstypeBean.QueryXstype> mXstpList = new ArrayList<>();
    public List<String> mPszdList = new ArrayList<>();
    public boolean mIsCache = true;//是否要缓存（默认是）什么时候设置false：1提交成功 2已缓存
    public boolean mEditPrice = true;
    public String mAutoPrice = "1";
    public DStep5Bean mDStep5Bean;

    @BindView(R.id.parent)
    public View mParent;
    public void initUI() {
        initHead();
        initTableView();
        initOtherUI();
        initBottomUI();
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
    @BindView(R.id.iv_head_right2)
    public ImageView mIvHeadRight2;
    public void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_25), (int) getResources().getDimension(R.dimen.dp_25));
        mIvHeadRight2.setLayoutParams(params);
        mIvHeadRight2.setImageResource(R.mipmap.x_ic_history_order);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAutoSubmit = 0;
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle.setText("订货下单");
        mTvHeadRight.setText("提交");
        //提交；修改
        mHeadRight.setOnClickListener(new OnNoMoreClickListener() {
            @Override
            protected void OnMoreClick(View view) {
                mAutoSubmit = 1;
                addData();
            }
        });
        //历史订单
        mHeadRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().jumpToOrderListActivity(context, OrderListEnum.HISTORY_ORDER, cId);
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
    public boolean isHide = true;
    public void initOtherUI() {
        //选择客户
        mTvKhNm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDo.chooseCustomer();
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
                mDo.chooseStorage();
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
        mRvLeft.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_b)
                .sizeResId(R.dimen.dp_0_5)
                .showLastDivider()
                .build());
        mLeftAdapter = new Step5Left2Adapter();
        mRvLeft.setAdapter(mLeftAdapter);
        //right
        mRvRight.setLayoutManager(new LinearLayoutManager(this));
        mRvRight.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_b)
                .sizeResId(R.dimen.dp_0_5)
                .showLastDivider()
                .build());
        mRightAdapter = new Step5Right2Adapter(mRedMark);
        mRightAdapter.setTypeEnum(OrderTypeEnum.getByType(mOrderType));
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
                            mDo.chooseXstp();
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
                mDo.chooseWare();
            }
        });

        if (mOrderType == OrderTypeEnum.ORDER_THXD_ADD.getType() || mOrderType == OrderTypeEnum.ORDER_THXD_LIST.getType()) {
            //退货相关:要生产日期
            mRightAdapter.setProductDate(true);
            mTvTableTitle7Del.setVisibility(View.VISIBLE);
            mTvTableTitleProduceDate.setVisibility(View.VISIBLE);
        } else if (mOrderType == OrderTypeEnum.ORDER_SC.getType()) {
            //商城
            mRightAdapter.setDel(false);
            mTvTableTitle7Del.setVisibility(View.GONE);
            mTvTableTitleProduceDate.setVisibility(View.GONE);
        } else {
            mTvTableTitle7Del.setVisibility(View.VISIBLE);
            mTvTableTitleProduceDate.setVisibility(View.GONE);
        }

    }


    @BindView(R.id.et_zdzk)
    public EditText mEtZdzk;
    @BindView(R.id.et_zdzk_percent)
    public EditText mEtZdzkParent;
    @BindView(R.id.btn_zdzk_convert)
    public StateButton mBtnZdzkConvert;
    @BindView(R.id.tv_zje_)
    public TextView mTvZje_;
    @BindView(R.id.tv_zje)
    public TextView mTvZje;
    @BindView(R.id.tv_cjje_)
    public TextView mTvCjje_;
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
        if (requestCode == ConstantUtils.Intent.REQUEST_STEP_5_CHOOSE_GOODS) {
            if (data != null) {
                mDo.doActivityResult(data);
            }
        }
    }


    /**
     * 刷新表格数据
     */
    public void refreshAdapterTable() {
        try {
            //标记商品的个数（重复商品颜色变）
            mDo.setRepeatMap();
            mLeftAdapter.notifyDataSetChanged();
            mRightAdapter.notifyDataSetChanged();
            mDo.setSumMoney();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //--------------------------------dialog:开始---------------------------------------------------


    /**
     * 语音搜索商品
     */
    public EasyPopup mPopupSearchWare;
    public ListView mLvSearchWare;
    public EditText mEtSearchGoods;
    public List<ShopInfoBean.Data> mSearchWareList = new ArrayList<>();
    public Step5SearchWareAdapter mAdapterSearWare;

    public void createPopupSearchGoods() {
        try {
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
                        mPopupSearchWare.dismiss();
                        ShopInfoBean.Data data = mSearchWareList.get(position);
                        mDo.doAddWareRefreshAdapter(data);
                    } catch (Exception e) {
                        ToastUtils.showError(e);
                    }
                }
            });
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

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
    public void addData() {
        if (mDo.addData()){
            if (1 == mRedMark){
                mCjjeStr= "-"+mCjjeStr;
                mZjeStr= "-"+mZjeStr;
            }
            getP().addData(context, cId, mOrderType, mPdateStr, count5, mOrderId, mJsonStr, mShrStr, mPhoneStr, mAddressStr, mRemoStr, mZjeStr, mZdzkStr, mCjjeStr, mShTimeStr, mPszdStr, stkId, mQueryToken, mRedMark);
        }
    }

    /**
     * /显示订单信息
     */
    public void doUI(QueryBforderBean data, DStep5Bean dStep5Bean) {
        mDo.doUI(data, dStep5Bean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDo.doCacheData();
    }


    //TODO***************************************************************************************************************************************************
    //TODO***************************************************************************************************************************************************
    //对话框-销售类型
    public void showDialogXstp(List<QueryXstypeBean.QueryXstype> list) {
        mDo.showDialogXstp(list);
    }

    //对话框-仓库
    public void showDialogStorage(List<StorageBean.Storage> list) {
        mDo.showDialogStorage(list);
    }

    /**
     * 默认仓库
     */
    public void doNormalStorage(List<StorageBean.Storage> list) {
        mDo.doNormalStorage(list);
    }

    public void submitSuccess(int tp) {
        mDo.submitSuccess(tp);
    }

    /**
     * 避免重复的token
     */
    public String mQueryToken;
    public void doToken(String data) {
        mQueryToken = data;
    }

    /**
     * 手机端下单配置（拜访下单，订货下单）
     * 1.是历史价还是执行价
     * 2.价格是否可以编辑
     */
    public void doOrderConfig(OrderConfigBean configBean) {
        mDo.doOrderConfig(configBean);
    }

    /**
     * 提交失败
     */
    public int mErrorCount;
    public void submitDataError() {
        mErrorCount++;
        if (mErrorCount > 1) {
            mDo.showDialogCache();
        }
    }

    //搜索商品后刷新列表（语音和弹窗中的搜索框）
    public void refreshAdapterSearch(List<ShopInfoBean.Data> list, boolean showPopup) {
        mDo.refreshAdapterSearch(list, showPopup);
    }






}
