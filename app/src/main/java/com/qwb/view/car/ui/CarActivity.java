package com.qwb.view.car.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.qwb.common.OrderTypeEnum;
import com.qwb.widget.MyCarCollectionDialog;
import com.qwb.widget.MyMenuPopup;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.view.car.adapter.CarStorageAdapter;
import com.qwb.event.OrderEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.stk.StorageBean;
import com.qwb.view.car.parsent.PCar;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.car.adapter.CarOrderAdapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.view.car.model.CarRecMastBean;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.qwb.view.order.model.QueryDhorderBean;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zyyoona7.lib.EasyPopup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 创建描述：车销单模块
 */
public class CarActivity extends XActivity<PCar> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_car;
    }

    @Override
    public PCar newP() {
        return new PCar();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void bindEvent() {
        BusProvider.getBus().toFlowable(OrderEvent.class)
                .subscribe(new Consumer<OrderEvent>() {
                    @Override
                    public void accept(OrderEvent orderEvent) throws Exception {
                        if (orderEvent.getTag() == ConstantUtils.Event.TAG_ORDER) {
                            pageNo = 1;
                            mRefreshLayout.autoRefresh();
                        }
                    }
                });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        stkId = SPUtils.getSValues(ConstantUtils.Sp.CAR_DEFAULT_STORAGE);
        initUI();
        createPopup();
        createPopupMenu();
        getP().queryStorageList();
        getP().queryCarOrder(context, mEtSearch, pageNo, pageSize, mStartDate, mEndDate, stkId);//获取订货列表
    }

    private String stkId;//仓库id
    private String stkName;//仓库名


    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initBottom();//底部：下单，退货
        initScreening();//筛选时间，搜索
        initAdapter();//订货
        initRefresh();
    }

    /**
     * 初始化UI-头部
     */
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
    @BindView(R.id.iv_head_right)
    ImageView mIvHeadRight;
    @BindView(R.id.iv_head_right2)
    ImageView mIvHeadRight2;

    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle.setText("车销管理");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_20), (int) getResources().getDimension(R.dimen.dp_20));
        mIvHeadRight.setLayoutParams(params);
        mIvHeadRight.setImageResource(R.mipmap.ic_ddd_h_gray_333);
        //具体车销仓库商品；车销回库
        mTvHeadTitle.setOnClickListener(new OnNoMoreClickListener() {
            @Override
            protected void OnMoreClick(View view) {
                jumpCarStorageGoodsActivity();
            }
        });
        //默认仓库，车销配货
        mIvHeadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMenuPopup.showAsDropDown(mIvHeadRight);
            }
        });
    }


    /**
     * 初始化筛选:时间和搜索
     */
    LinearLayout mRlSearch;
    EditText mEtSearch;
    TextView mIvSearch;
    TextView mTvStartEndTime;
    int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;//年，月，日
    String mStartDate, mEndDate;//开始时间，结束时间
    Calendar calendar = Calendar.getInstance();

    private void initScreening() {
        mRlSearch = findViewById(R.id.rl_search);
        mEtSearch = findViewById(R.id.et_search);
        mIvSearch = findViewById(R.id.iv_search);
        mTvStartEndTime = findViewById(R.id.tv_start_end_time);
        TextView mTvSearch = findViewById(R.id.tv_search);
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTvSearch();//搜索：显示搜索和隐藏搜索
            }
        });
        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickIvSearch();//搜索按钮：请求数据
            }
        });
        mStartYear = mEndYear = calendar.get(Calendar.YEAR);//年
        mStartMonth = mEndMonth = calendar.get(Calendar.MONTH);//月
        mStartDay = mEndDay = calendar.get(Calendar.DAY_OF_MONTH);//日
        mTvStartEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogStartEndTime();
            }
        });
    }

    /**
     * 搜索：显示搜索和隐藏搜索 - 关闭的时候：清空搜索
     */
    private void onClickTvSearch() {
        if (mRlSearch.getVisibility() == View.VISIBLE) {
            mRlSearch.setVisibility(View.GONE);
            mEtSearch.setText("");
        } else {
            mRlSearch.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 底部：下单，默认仓库
     */
    private void initBottom() {
        //下单
        findViewById(R.id.tv_bottom_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean query = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.CXGL_QUERY_NEW);
                boolean order = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.CXGL_ORDER_NEW);
                if (query && !order) {
                    ToastUtils.showCustomToast("您没有下单权限");
                    return;
                }
                ActivityManager.getInstance().jumpActivity(context, com.qwb.view.car.ui.CarOrderActivity.class);
//                ActivityManager.getInstance().jumpActivity(context, CarOrderActivity.class);
            }
        });
        //车销收款
        findViewById(R.id.tv_bottom_car_sk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().jumpActivity(context, CarRecMastActivity.class);
            }
        });
    }

    /**
     * 初始化适配器（订货）
     */
    RecyclerView mRvOrder;
    CarOrderAdapter mOrderAdapter;
    private int pageNo = 1;
    private int pageSize = 10;
    private int mCurrentPosition;
    private QueryDhorderBean.Rows mCurrentBean;

    private void initAdapter() {
        mRvOrder = findViewById(R.id.rv_receiver);
        mRvOrder.setHasFixedSize(true);
        mRvOrder.setLayoutManager(new LinearLayoutManager(this));
        //添加分割线
        mRvOrder.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_5)
                .build());
        mOrderAdapter = new CarOrderAdapter();
        mRvOrder.setAdapter(mOrderAdapter);
        mOrderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    mCurrentBean = (QueryDhorderBean.Rows) adapter.getData().get(position);
                    mCurrentPosition = position;
                    switch (view.getId()) {
                        case R.id.item_sb_zc://收款
                            showDialogSk(mCurrentBean);
                            break;
                        case R.id.content:
                            ActivityManager.getInstance().jumpToCarOrderActivity(context, mCurrentBean.getId(),mCurrentBean.getKhNm(), mCurrentBean.getOrderZt(), mCurrentBean.getIsMe(), mCurrentBean.getOrderNo(), OrderTypeEnum.ORDER_CAR_LIST );
                            break;
                        case R.id.tv_cancel:
                            getP().cancelOrder(context, mCurrentBean.getId());
                            break;
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * 初始化刷新控件（订货）
     */
    RefreshLayout mRefreshLayout;

    private void initRefresh() {
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNo = 1;
                getP().queryCarOrder(context, mEtSearch, pageNo, pageSize, mStartDate, mEndDate, stkId);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo++;
                getP().queryCarOrder(context, mEtSearch, pageNo, pageSize, mStartDate, mEndDate, stkId);
            }
        });
    }

    /**
     * 选择默认仓库
     */
    private EasyPopup mEasyPopCarStorage;

    public void createPopup() {
        mEasyPopCarStorage = new EasyPopup(context)
                .setContentView(R.layout.x_popup_car_storage)
                .createPopup();
        mRvStorage = mEasyPopCarStorage.getView(R.id.recyclerView_storage);
        initAdapterStorage();
    }

    private MyMenuPopup mMenuPopup;

    public void createPopupMenu() {
        String[] items = {"默认仓库", "车销配货", "车销回库","车销配货单列表","车销回库单列表"};
        mMenuPopup = new MyMenuPopup(context, items);
        mMenuPopup.createPopup();
        mMenuPopup.setOnItemClickListener(new MyMenuPopup.OnItemClickListener() {
            @Override
            public void setOnItemClickListener(String text, int position) {
                mMenuPopup.dismiss();
                switch (position) {
                    case 0:
                        mAdapterStorage.notifyDataSetChanged();
                        mEasyPopCarStorage.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
                        break;
                    case 1:
                        Router.newIntent(context)
                                .putInt(ConstantUtils.Intent.ORDER_TYPE, ConstantUtils.Order.O_CXPH)
                                .to(CarStkOutOrderActivity.class)
                                .launch();
                        break;
                    case 2:
                        jumpCarStorageGoodsActivity();
                        break;
                    case 3:
                        ActivityManager.getInstance().jumpToCarStkOutOrderListActivity(context, 1);
                        break;
                    case 4:
                        ActivityManager.getInstance().jumpToCarStkOutOrderListActivity(context, 2);
                        break;
                }
            }
        });
    }

    /**
     * 对话框：去收款
     */
    public void showDialogSk(final QueryDhorderBean.Rows bean) {
//        NormalDialog dialog = new NormalDialog(context);
//        StringBuffer content = new StringBuffer();
//        content.append("客　　户：" + bean.getKhNm()+"\n")
//                .append("收款金额：" + bean.getCjje() + "元");
//        dialog.title("收款提示")
//                .content(content.toString())
//                .style(NormalDialog.STYLE_THREE)
//                .show();
//        dialog.setOnBtnClickL(null, new OnBtnClickL() {
//            @Override
//            public void onBtnClick() {
//                //status: -2:暂存；1：确认收款
//                getP().updateStatusZc(context, bean.getId(), 1);
//            }
//        });
        MyCarCollectionDialog dialog = new MyCarCollectionDialog(context,bean.getKhNm(), bean.getCjje() );
        dialog.show();
        dialog.setOnClickListener(new MyCarCollectionDialog.OnClickListener() {
            @Override
            public void setOnClickListener(int accType, String money) {
                //status: -2:暂存；1：确认收款
                getP().updateStatusZc(context, bean.getId(), 1, accType, money);
            }
        });
    }

    /**
     * 初始化适配器（仓库）
     */
    private RecyclerView mRvStorage;
    private CarStorageAdapter mAdapterStorage;

    private void initAdapterStorage() {
        mRvStorage.setHasFixedSize(true);
        mRvStorage.setLayoutManager(new LinearLayoutManager(this));
        //添加分割线
        mRvStorage.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_0_5)
                .build());
        mAdapterStorage = new CarStorageAdapter(context);
        mAdapterStorage.openLoadAnimation();
        mRvStorage.setAdapter(mAdapterStorage);
        mAdapterStorage.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                try {
                    StorageBean.Storage data = (StorageBean.Storage) baseQuickAdapter.getData().get(i);
                    switch (view.getId()) {
                        case R.id.tv_default://设置默认仓库
                            mEasyPopCarStorage.dismiss();
                            SPUtils.setValues(ConstantUtils.Sp.CAR_DEFAULT_STORAGE, String.valueOf(data.getId()));
                            SPUtils.setValues(ConstantUtils.Sp.CAR_DEFAULT_STORAGE_NAME, String.valueOf(data.getStkName()));
                            stkId = data.getId().toString();
                            stkName = data.getStkName();
                            String str="车销单(<font color='#2597f0'>" +stkName+ "</font>)";
                            mTvHeadTitle.setText(Html.fromHtml(str));
                            //刷新数据
                            pageNo = 1;
                            getP().queryCarOrder(context, mEtSearch, pageNo, pageSize, mStartDate, mEndDate, stkId);
                            break;
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * 刷新：仓库列表
     */
    public void refreshAdapterStorage(List<StorageBean.Storage> datas) {
        mAdapterStorage.setNewData(datas);
        if (MyUtils.isEmptyString(stkId)) {
            mEasyPopCarStorage.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
        } else {
            for (StorageBean.Storage data : datas) {
                if (stkId.equals(data.getId().toString())) {
                    stkName = data.getStkName();
                    String str="车销单(<font color='#2597f0'>" +stkName+ "</font>)";
                    mTvHeadTitle.setText(Html.fromHtml(str));
                }
            }
        }
    }


    //==============================================================================================

    /**
     * 刷新适配器-订货下单
     */
    public void refreshAdapterOrder(List<QueryDhorderBean.Rows> dataList) {
        if (null == dataList) {
            return;
        }
        if (pageNo == 1) {
            //上拉刷新
            mOrderAdapter.setNewData(dataList);
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setNoMoreData(false);
        } else {
            //加载更多
            mOrderAdapter.addData(dataList);
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.setNoMoreData(false);
        }
        if (null != dataList && dataList.size() < pageSize) {
            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }
        getOrderIds(dataList);
    }

    public void getOrderIds(List<QueryDhorderBean.Rows> list) {
        String orderIds = "";
        for (QueryDhorderBean.Rows bean : list) {
            orderIds = MyStringUtil.getPjDh(orderIds, String.valueOf(bean.getId()));
        }
        if (!MyStringUtil.isEmpty(orderIds)) {
            getP().queryStkCarRectMastStatus(null, orderIds);
        }
    }

    /**
     * 刷新适配器-订货下单
     */
    private List<CarRecMastBean> mCarRecMastList = new ArrayList<>();

    public void refreshAdapterStatus(List<CarRecMastBean> list) {
        if (null == list) {
            return;
        }
        if (pageNo == 1) {
            mCarRecMastList.clear();
        }
        mCarRecMastList.addAll(list);
        mOrderAdapter.setCarRecMastList(mCarRecMastList);
        mOrderAdapter.notifyDataSetChanged();
    }

    /**
     * 收款成功
     */
    public void updateStatusSuccess() {
        CarRecMastBean bean = new CarRecMastBean();
        bean.setOrderId(mCurrentBean.getId());
        bean.setStatus(1);
        mCarRecMastList.add(bean);
        mOrderAdapter.setCarRecMastList(mCarRecMastList);
        mOrderAdapter.notifyDataSetChanged();
    }


    /**
     * 关闭刷新
     */
    public void closeRefresh() {
        //关闭刷新，加载更多
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }


    /**
     * 搜索按钮：请求数据
     */
    private void onClickIvSearch() {
        String searchStr = mEtSearch.getText().toString();
        if (TextUtils.isEmpty(searchStr)) {
            ToastUtils.showCustomToast("请输入要搜索的关键字");
            return;
        }
        pageNo = 1;
        getP().queryCarOrder(context, mEtSearch, pageNo, pageSize, mStartDate, mEndDate, stkId);
    }

    /**
     * 筛选时间的对话框
     */
    private void showDialogStartEndTime() {
        new MyDoubleDatePickerDialog(context, "筛选时间", mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay,
                new MyDoubleDatePickerDialog.DateTimeListener() {
                    @Override
                    public void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate) {
                        mTvStartEndTime.setText(startDate + "至" + endDate);
                        mStartDate = startDate;
                        mEndDate = endDate;
                        pageNo = 1;
                        getP().queryCarOrder(context, mEtSearch, pageNo, pageSize, mStartDate, mEndDate, stkId);
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }

    /**
     * 仓库管理
     */
    private void jumpCarStorageGoodsActivity() {
        if (!MyUtils.isEmptyString(stkId)) {
            Router.newIntent(context)
                    .to(CarStkWareActivity.class)
                    .putString(ConstantUtils.Intent.STK_ID, SPUtils.getSValues(ConstantUtils.Sp.CAR_DEFAULT_STORAGE))
                    .putString(ConstantUtils.Intent.NAME, SPUtils.getSValues(ConstantUtils.Sp.CAR_DEFAULT_STORAGE_NAME))
                    .launch();
        } else {
            ToastUtils.showCustomToast("请先设置默认仓库");
        }
    }

    /**
     * 作废成功
     */
    public void doCanCelOrderSuccess(){
        ToastUtils.showCustomToast("作废成功");
        mCurrentBean.setOrderZt("已作废");
        mOrderAdapter.notifyItemChanged(mCurrentPosition);
    }


}
