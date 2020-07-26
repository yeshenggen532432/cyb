package com.qwb.view.step.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.scanlibrary.ScanActivity;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.qwb.utils.MyCollectionUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.step.parsent.PChooseWare3;
import com.qwb.utils.MyDoubleUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.step.adapter.WareAdapter;
import com.qwb.view.ware.adapter.WareTreeAdapter;
import com.qwb.utils.MyColorUtil;
import com.qwb.utils.MyKeyboardUtil;
import com.qwb.utils.MyNetWorkUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.xmsx.cnlife.view.widget.MyVoiceDialog;
import com.qwb.view.step.model.HistoryOrgPriceBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.view.step.model.ShopInfoBean.Data;
import com.qwb.view.base.model.TreeBean;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhy.tree.bean.Node;
import com.zyyoona7.lib.EasyPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 创建描述：选择商品 （4：销售小结 5：订货下单,车销下单 6.库存盘点-选择商品 7.车削下单）
 */
public class ChooseWareActivity3 extends XActivity<PChooseWare3> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_choose_ware;
    }

    @Override
    public PChooseWare3 newP() {
        return new PChooseWare3();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        createPopup4Xsxj();//窗体-销售小结
        createPopup5Dhxd();//窗体-订货下单

        //TODO tree控件数据变化时，刷新有问题
        if (!MyNetWorkUtil.isNetworkConnected()) {
            getP().queryCacheWareType(context);
            getP().queryCacheWare(context, "", null, pageNo, pageSize);
        }

        //备注：销售小结没有过滤非公司产品
        if ("4".equals(type)) {
            noCompany = "1";
            getP().queryDataWareTypes(context, cid, noCompany, stkId);//获得商品类型列表
        } else if ("8".equals(type)) {
            getP().queryDataCompanyWareTypes(context, cid, noCompany, stkId, isContainSale, type);//获得商品类型列表
        } else {
            getP().queryDataCompanyWareTypes(context, cid, noCompany, stkId, isContainSale, type);//获得商品类型列表
        }

    }

    private String cid;//客户id,常售列表用到
    private String stkId;//仓库id
    private String type;//4:销售小结，5：订货下单 7.采购单
    private String noCompany;//过滤非公司产品:1-过滤；0-没过滤
    private boolean isContainSale = true;//查询商品分类时是否包含“常售商品”，“收藏商品”
    private boolean mEditPrice = true;//价格是否可以编辑(默认true)
    private String mAutoPrice = "1";//1.历史价；其他执行价
    private ArrayList<Integer> mWareIdList = new ArrayList<>();//总的数据
    private ArrayList<ShopInfoBean.Data> mCurrentList = new ArrayList<>();//本页面添加的数据

    private void initIntent() {
        Intent intent = getIntent();
        if (null != intent) {
            type = intent.getStringExtra(ConstantUtils.Intent.TYPE);
            cid = intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
            stkId = intent.getStringExtra(ConstantUtils.Intent.STK_ID);
            isContainSale = intent.getBooleanExtra(ConstantUtils.Intent.IS_CONTAIN_SALE, true);
            mEditPrice = intent.getBooleanExtra(ConstantUtils.Intent.EDIT_PRICE, true);
            mAutoPrice = intent.getStringExtra(ConstantUtils.Intent.AUTO_PRICE);
            ArrayList<Integer> wareIdList = intent.getIntegerArrayListExtra(ConstantUtils.Intent.CHOOSE_WARE_ID);
            if (wareIdList != null) {
                mWareIdList.addAll(wareIdList);
            }
        }
    }

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
        initSearch();
        initAdapter();
        initRefresh();
        initStorage0();
    }

    //初始化头部
    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.head_right2)
    View mHeadRight2;
    @BindView(R.id.head_right3)
    View mHeadRight3;
    @BindView(R.id.iv_head_right)
    ImageView mIvHeadRight;
    @BindView(R.id.iv_head_right2)
    ImageView mIvHeadRight2;
    @BindView(R.id.iv_head_right3)
    ImageView mIvHeadRight3;
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
        mTvHeadTitle.setText("选择商品");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_17), (int) getResources().getDimension(R.dimen.dp_17));
        mIvHeadRight.setLayoutParams(params);
        mIvHeadRight.setImageResource(R.mipmap.ic_scan_gray_33);
        mIvHeadRight2.setLayoutParams(params);
        mIvHeadRight2.setImageResource(R.mipmap.ic_voice_gray_33);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_18), (int) getResources().getDimension(R.dimen.dp_18));
        mIvHeadRight3.setLayoutParams(params3);
        mIvHeadRight3.setImageResource(R.mipmap.ic_pic_gray_333);

        mHeadRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().jumpScanActivity(context, false);//扫描
            }
        });
        //语音
        mHeadRight2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogVoice();
            }
        });
        //显示隐藏图片
        mHeadRight3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doShowHidePic();
            }
        });
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
                doSearch();
            }
        });
    }

    //初始化过滤0库存
    @BindView(R.id.tv_storage_0)
    TextView mTvStorage0;
    private void initStorage0() {
        String storage0 = SPUtils.getSValues(ConstantUtils.Sp.STORAGE_ZERO);
        if (!MyStringUtil.isEmpty(storage0) && storage0.equals("1")) {
            mTvStorage0.setText("已过滤0库存");
        } else {
            mTvStorage0.setText("过滤0库存");
        }
        if ("7".equals(type)) {
            mTvStorage0.setText("");
        }
        mTvStorage0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog0();
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
    private List<TreeBean> mTreeList = new ArrayList<>();
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
        mWareAdapter = new WareAdapter(type);
        mRvRight.setAdapter(mWareAdapter);
        //子item点击事件
        mWareAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    mCurrentData = (Data) adapter.getData().get(position);
                    mCurrentPosition = position;
                    switch (view.getId()) {
                        case R.id.iv_save_choose_shop:// 收藏
                            int userId = mCurrentData.getUserId();
                            if (0 == userId) {
                                getP().getDataFavoritesGoodsList(context, String.valueOf(mCurrentData.getWareId()));// 收藏
                            } else {
                                getP().getDataUnFavoritesGoodsList(context, String.valueOf(mCurrentData.getWareId()));// 取消收藏
                            }
                            break;
                        case R.id.iv_add_choose_shop:// 添加商品 4:销售小结 5：订货下单 7.车削下单 8.采购单
                            if (isAddedGoods(mCurrentData.getWareId())) {
                                showDialogWareIsAdd(mCurrentData);
                            } else {
                                addGoods(mCurrentData);
                            }
                            break;
                    }
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }

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
    private int mRequestType = 1;//1:常售 2：收藏  3：分类   4：搜索(默认)
    private String mCacheWareType;//缓存的商品类型（包括下级id）
    private String mWareType;//商品类型（只是当前的）

    private void initRefresh() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                    pageNo = 1;
                    doRefresh();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo++;
                doRefresh();
            }
        });
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.tv_confirm_choose_shop})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm_choose_shop://确定
                try {
                    Intent intent = new Intent();
//                intent.putExtra(ConstantUtils.Intent.EDIT_PRICE, mEditPrice);
                    intent.putParcelableArrayListExtra(ConstantUtils.Intent.CHOOSE_WARE_NEW, mCurrentList);
                    setResult(ConstantUtils.Intent.RESULT_CODE_CHOOSE_GOODS, intent);
                    ActivityManager.getInstance().closeActivity(context);
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
                break;
        }
    }

    //TODO ************************************************2：弹窗-开始************************************************
    /**
     * 弹窗5:订货下单
     */
    private LinearLayout mLayoutTip5;
    private TextView mTvTip5;
    private TextView mTvTitle5;
    private EditText mEtCount5;
    private EditText mEtPrice5;
    private EditText mEtMinPrice5;
    private EditText mEtGg5;//规格
    private TextView mTvUnit5;//单位
    private LinearLayout mLayoutChangeUnit5;//大小单位切换
    private TextView mTvMaxUnit5;//单位（大）：如"箱"
    private TextView mTvMinUnit5;//单位（小）：如“瓶”
    private TextView mTvZYPrice5;//执行价，原价
    private String mMaxPriceStr5;
    private String mMinPriceStr5;//历史价（小单位）
    private String mOrgPriceStr5;//原价
    private String unitCode5;//包装单位代码或计量单位代码
    private EasyPopup mEasyPop5;
    private boolean isMaxUnit5 = true;//是否是主单位
    public ArrayList<HashMap<String, String>> mXsList = new ArrayList<>();

    public void createPopup5Dhxd() {
        mEasyPop5 = new EasyPopup(this)
                .setContentView(R.layout.x_popup_choose_ware_5)
                .createPopup();
        mLayoutTip5 = mEasyPop5.getView(R.id.popup_layout_tip);// 库存超出提示
        mTvTip5 = mEasyPop5.getView(R.id.popup_tv_tip);// 库存超出提示
        mTvTitle5 = mEasyPop5.getView(R.id.popup_tv_title);// 对应的商品名称
        mEtCount5 = mEasyPop5.getView(R.id.pupup_et_count);//数量
        mEtPrice5 = mEasyPop5.getView(R.id.popup_et_price);//大单位价格
        mTvUnit5 = mEasyPop5.getView(R.id.popup_tv_unit);//单位
        mEtGg5 = mEasyPop5.getView(R.id.popup_et_gg);//规格
        mLayoutChangeUnit5 = mEasyPop5.getView(R.id.popup_layout_unit_change);//单位
        mTvMaxUnit5 = mEasyPop5.getView(R.id.popup_tv_max_unit);//大单位
        mTvMinUnit5 = mEasyPop5.getView(R.id.popup_tv_min_unit);//小单位
        mTvZYPrice5 = mEasyPop5.getView(R.id.popup_tv_zy_price);//执行价，原价

        mEasyPop5.getView(R.id.popup_tv_ok).setOnClickListener(new PopUp5ClickListener());
        mEasyPop5.getView(R.id.popup_tv_cancel).setOnClickListener(new PopUp5ClickListener());
        mEasyPop5.getView(R.id.pupup_et_count).setOnClickListener(new PopUp5ClickListener());
        mEasyPop5.getView(R.id.popup_et_price).setOnClickListener(new PopUp5ClickListener());
        mEasyPop5.getView(R.id.popup_sb_convert).setOnClickListener(new PopUp5ClickListener());
        mEasyPop5.getView(R.id.popup_layout_unit).setOnClickListener(new PopUp5ClickListener());
        mEasyPop5.getView(R.id.popup_tv_min_unit).setOnClickListener(new PopUp5ClickListener());
        mEasyPop5.getView(R.id.popup_tv_max_unit).setOnClickListener(new PopUp5ClickListener());
        mEasyPop5.getView(R.id.popup_iv_jia).setOnClickListener(new PopUp5ClickListener());
        mEasyPop5.getView(R.id.popup_iv_jian).setOnClickListener(new PopUp5ClickListener());

        //--------------------开启开单小单价参考：start------------------------------------
        mEtMinPrice5 = mEasyPop5.getView(R.id.popup_et_min_price);//小单价
        LinearLayout layoutMinPrice = mEasyPop5.getView(R.id.popup_layout_min_price);
        final boolean openMinPrice = SPUtils.getBoolean(ConstantUtils.Sp.OPEN_SMALL_PRICE);
        if (openMinPrice) {
            layoutMinPrice.setVisibility(View.VISIBLE);
        } else {
            layoutMinPrice.setVisibility(View.GONE);
        }

        //改变小单位价格
        mEtPrice5.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (openMinPrice) {
                        String price = mEtPrice5.getText().toString().trim();
                        String hsNum = mCurrentData.getHsNum();
                        if (!MyStringUtil.isEmpty(price)) {// 判断是否为null
                            if (isMaxUnit5) {
                                mEtMinPrice5.setText("" + (Double.valueOf(price) / Double.valueOf(hsNum)));
                            } else {
                                mEtMinPrice5.setText(price);
                            }
                        } else {
                            mEtMinPrice5.setText("");
                        }
                    }
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
        //------------------------开启开单小单位参考:end----------------------------------

        //剩余库存与输入数量相比较:库存提醒
        mEtCount5.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    String input = editable.toString().trim();
                    if (!MyStringUtil.isEmpty(input)) {
                        int count = Integer.valueOf(input);
                        if (count > getStorageCount()) {
                            showStorageTip();
                        } else {
                            mLayoutTip5.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }

    //下单5--窗体
    private class PopUp5ClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            try {
                mLayoutChangeUnit5.setVisibility(View.GONE);//大小单位切换
                String price = mEtPrice5.getText().toString().trim();
                String minPriceStr = mEtMinPrice5.getText().toString().trim();
                String unit = mTvUnit5.getText().toString().trim();//原来的单位
                String hsNum = mCurrentData.getHsNum();
                String countStr = mEtCount5.getText().toString().trim();
                switch (view.getId()) {
                    case R.id.popup_tv_ok://确定
                        confirm5Dhxd(unitCode5);
                        mEasyPop5.dismiss();
                        MyKeyboardUtil.closeKeyboard(context);//强制关闭软键盘
                        break;
                    case R.id.popup_tv_cancel://取消
                        mEasyPop5.dismiss();
                        MyKeyboardUtil.closeKeyboard(context);//强制关闭软键盘
                        break;
                    case R.id.popup_sb_convert://小单位换算大单位
                        if (!MyStringUtil.isEmpty(hsNum) && !MyStringUtil.isEmpty(minPriceStr)) {
                            if (isMaxUnit5) {
                                mEtPrice5.setText("" + (Double.valueOf(minPriceStr) * Double.valueOf(hsNum)));
                            } else {
                                mEtPrice5.setText(minPriceStr);
                            }
                        }
                        break;
                    case R.id.popup_layout_unit://大小单位切换
                        mLayoutChangeUnit5.setVisibility(View.VISIBLE);
                        mTvMaxUnit5.setText(mCurrentData.getWareDw());
                        mTvMinUnit5.setText(mCurrentData.getMinUnit());
                        if (!MyStringUtil.isEmpty(mCurrentData.getWareDw()) && mCurrentData.getWareDw().equals(unit)) {
                            mTvMaxUnit5.setTextColor(getResources().getColor(R.color.x_main_color));
                            mTvMinUnit5.setTextColor(getResources().getColor(R.color.gray_6));
                        } else {
                            mTvMaxUnit5.setTextColor(getResources().getColor(R.color.gray_6));
                            mTvMinUnit5.setTextColor(getResources().getColor(R.color.x_main_color));
                        }
                        break;
                    case R.id.popup_tv_max_unit://大单位
                        isMaxUnit5 = true;//是否是主单位（默认是）
                        unitCode5 = mCurrentData.getMaxUnitCode();//单位代码
                        if (!unit.equals(mCurrentData.getWareDw())) {
                            mTvUnit5.setText(mCurrentData.getWareDw());
                            mEtPrice5.setText(mMaxPriceStr5);
                        }
                        break;
                    case R.id.popup_tv_min_unit://小单位
                        isMaxUnit5 = false;//是否是主单位（默认是）
                        unitCode5 = mCurrentData.getMinUnitCode();
                        if (!unit.equals(mCurrentData.getMinUnit())) {
                            mTvUnit5.setText(mCurrentData.getMinUnit());
                            mEtPrice5.setText(mMinPriceStr5);
                        }
                        break;
                    case R.id.popup_iv_jia://加号
                        if (MyStringUtil.isEmpty(countStr)) {
                            mEtCount5.setText("1");
                            mEtCount5.setSelection(1);//将光标移至文字末尾
                        } else {
                            int count = Integer.valueOf(countStr);
                            count++;
                            mEtCount5.setText(String.valueOf(count));
                            mEtCount5.setSelection(String.valueOf(count).length());//将光标移至文字末尾
                        }
                        break;
                    case R.id.popup_iv_jian://减号
                        if (MyStringUtil.isEmpty(countStr)) {
                            mEtCount5.setText("0");
                            mEtCount5.setSelection(1);//将光标移至文字末尾
                        } else {
                            int count = Integer.valueOf(countStr);
                            if (count != 0) {
                                count--;
                            }
                            mEtCount5.setText(String.valueOf(count));
                            mEtCount5.setSelection(String.valueOf(count).length());//将光标移至文字末尾
                        }
                        break;
                }
            } catch (Exception e) {
                ToastUtils.showError(e);
            }
        }
    }


    /**
     * 窗体4-销售小结
     */
    private TextView mTvTitle4;
    private EditText mEtDhl4;
    private EditText mEtSxl4;
    private EditText mEtKcl4;
    private EditText mEtDd4;
    private EditText mEtBz4;
    private EasyPopup mEasyPop4;

    public void createPopup4Xsxj() {
        try {
            mEasyPop4 = new EasyPopup(this)
                    .setContentView(R.layout.x_popup_choose_ware_4)
                    .createPopup();
            mTvTitle4 = mEasyPop4.getView(R.id.popup_tv_title_4);// 对应的商品名称
            mEtDhl4 = mEasyPop4.getView(R.id.popup_et_dhl);// 到货量
            mEtSxl4 = mEasyPop4.getView(R.id.popup_et_sxl);// 销售量
            mEtKcl4 = mEasyPop4.getView(R.id.popup_et_kcl);// 库存量
            mEtDd4 = mEasyPop4.getView(R.id.popup_et_dd);// 订单
            mEtBz4 = mEasyPop4.getView(R.id.popup_et_bz);// 备注
            mEasyPop4.getView(R.id.popup_tv_cancel_4).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEasyPop4.dismiss();
                    MyKeyboardUtil.closeKeyboard(context);//强制关闭软键盘
                }
            });
            mEasyPop4.getView(R.id.popup_tv_ok_4).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirm4Xsxj();
                    mEasyPop4.dismiss();
                    MyKeyboardUtil.closeKeyboard(context);//强制关闭软键盘
                }
            });
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    /**
     * 销售小结的确定
     */
    private int mSumCount;//总数量(用于红点)

    private void confirm4Xsxj() {
        try {
            String dhl = mEtDhl4.getText().toString().trim();
            String sxl = mEtSxl4.getText().toString().trim();
            String kcl = mEtKcl4.getText().toString().trim();
            String dd = mEtDd4.getText().toString().trim();
            String bz = mEtBz4.getText().toString().trim();
            mEtDhl4.setText("");// 清空
            mEtSxl4.setText("");// 清空
            mEtKcl4.setText("");// 清空
            mEtDd4.setText("");// 清空
            mEtBz4.setText("");// 清空
            mSumCount++;
            mTvSum.setText(String.valueOf(mSumCount));

            //TODO 主要有：到货量,实销量,库存量,订单,销售类型,新鲜值,规格,备注
            Data data = new Data();//要局部变量；不然添加同一个的时候，原来会被覆盖
            data.setWareId(mCurrentData.getWareId());
            data.setSunitFront(mCurrentData.getSunitFront());
            data.setWareNm(mCurrentData.getWareNm());
            data.setWareGg(mCurrentData.getWareGg());
            data.setWareDw(mCurrentData.getWareDw());
            data.setWareDj(mCurrentData.getWareDj());
            data.setHsNum(mCurrentData.getHsNum());
            data.setMinUnit(mCurrentData.getMinUnit());
            data.setMaxUnitCode(mCurrentData.getMaxUnitCode());
            data.setMinUnitCode(mCurrentData.getMinUnitCode());
            if (MyStringUtil.isEmpty(dhl)) {
                data.setCurrentDhl("0");
            } else {
                data.setCurrentDhl(dhl);
            }
            if (MyStringUtil.isEmpty(sxl)) {
                data.setCurrentSxl("0");
            } else {
                data.setCurrentSxl(sxl);
            }
            if (MyStringUtil.isEmpty(kcl)) {
                data.setCurrentKcl("0");
            } else {
                data.setCurrentKcl(kcl);
            }
            if (MyStringUtil.isEmpty(dd)) {
                data.setCurrentDd("0");
            } else {
                data.setCurrentDd(dd);
            }
            data.setCurrentXstp("正常销售");
            data.setCurrentXxz("正常");
            if (MyStringUtil.isEmpty(bz)) {
                data.setCurrentBz("");
            } else {
                data.setCurrentBz(bz);
            }
            mWareIdList.add(data.getWareId());
            mCurrentList.add(data);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

//    /**
//     * 是否小于最低销售价格(大)
//     */
//    private boolean isLessThanSalePrice(){
//        try{
//            String salePriceStr = mCurrentData.getLowestSalePrice();
//            String priceStr = mEtPrice5.getText().toString().trim();
//            if (MyStringUtil.isNumber(salePriceStr) && MyStringUtil.isNumber(priceStr)){
//                BigDecimal salePrice = new BigDecimal(salePriceStr);
//                BigDecimal price = new BigDecimal(priceStr);
//                BigDecimal hsNum = new BigDecimal(1);
//                if (MyStringUtil.isNotEmpty(mCurrentData.getHsNum())){
//                    hsNum = new BigDecimal(mCurrentData.getHsNum());
//                }
//                //小单位价格--salePrice
//                if (MyStringUtil.eq(mCurrentData.getMinUnitCode(), unitCode5)){
//                    salePrice = MyMathUtils.divideByScale(salePrice, hsNum, 3);
//                    if (price.doubleValue() < salePrice.doubleValue()){
//                        ToastUtils.showCustomToast("价格不能小于最低销售价格(小)"+salePrice.doubleValue());
//                        return true;
//                    }
//                }else{
//                    if (price.doubleValue() < salePrice.doubleValue()){
//                        ToastUtils.showCustomToast("价格不能小于最低销售价格(大)"+salePrice.doubleValue());
//                        return true;
//                    }
//                }
//            }
//        }catch (Exception e){
//        }
//        return false;
//    }

    /**
     * 订货下单的确定
     */
    private void confirm5Dhxd(String unitCode) {
        try {
            String countStr = mEtCount5.getText().toString().trim();
            String priceStr = mEtPrice5.getText().toString().trim();
            String dwStr = mTvUnit5.getText().toString().trim();
            if (MyStringUtil.isEmpty(countStr) || MyStringUtil.isEmpty(priceStr)) {
                ToastUtils.showCustomToast("数量或单价不能为空");
                return;
            }
            mEtCount5.setText("");// 清空
            mEtPrice5.setText("");// 清空
            mTvUnit5.setText("");// 清空
            mSumCount++;
            mTvSum.setText(String.valueOf(mSumCount));

            //TODO 主要有：单位，单位代码，数量，价格，备注
            Data data = new Data();//要局部变量；不然添加同一个的时候，原来会被覆盖
            data.setWareId(mCurrentData.getWareId());
            data.setSunitFront(mCurrentData.getSunitFront());

            data.setWareNm(mCurrentData.getWareNm());
            data.setWareGg(mCurrentData.getWareGg());
            data.setWareDw(mCurrentData.getWareDw());
            data.setWareDj(mCurrentData.getWareDj());
            data.setHsNum(mCurrentData.getHsNum());
            data.setMinUnit(mCurrentData.getMinUnit());
            data.setMaxUnitCode(mCurrentData.getMaxUnitCode());
            data.setMinUnitCode(mCurrentData.getMinUnitCode());
            data.setLowestSalePrice(mCurrentData.getLowestSalePrice());
            data.setPackBarCode(mCurrentData.getPackBarCode());
            data.setBeBarCode(mCurrentData.getBeBarCode());

            data.setCurrentCount(countStr);
            data.setCurrentCode(unitCode);
            data.setCurrentDw(dwStr);
            data.setCurrentPrice(priceStr);
            data.setCurrentXstp("正常销售");
            data.setCurrentMaxPrice(mMaxPriceStr5);
            data.setCurrentMinPrice(mMinPriceStr5);
            data.setCurrentBz("");

            mWareIdList.add(data.getWareId());
            mCurrentList.add(data);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //TODO -----------------------------------对话框：start-------------------------------------------------------
    //该商品是否添加过提示
    private void showDialogWareIsAdd(final Data data) {
        final NormalDialog dialog = new NormalDialog(context);
        dialog.setCanceledOnTouchOutside(false);//外部点击不消失
        dialog.content("该商品已添加过，是否继续添加?").btnText("取消", "添加").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                addGoods(data);
            }
        });
    }

    //TODO -----------------------------------对话框：end-------------------------------------------------------

    //TODO ********************************************接口回调处理**********************************************************
    /**
     * 刷新左边Tree
     */
    private boolean mIsFavoritesState;//是否处于收藏商品的目录下：点击“取消收藏”，要移除

    public void refreshAdapterTree(List<TreeBean> treeList) {
        try {
            this.mTreeList = treeList;
            if (mTreeAdapter == null) {
                mTreeAdapter = new WareTreeAdapter<>(mTreeListView, context, this.mTreeList, 0);
                mTreeListView.setAdapter(mTreeAdapter);
                mTreeAdapter.setOnTreeNodeClickListener(new WareTreeAdapter.OnTreeNodeClickListener() {
                    @Override
                    public void onClick(Node node, int i) {
                        try {
                            mTreeAdapter.setmId(node.getId());
                            mTreeAdapter.notifyDataSetChanged();
                            switch (node.getId()) {
                                case -3:// 车载商品
                                    setRequestType(5, null, String.valueOf(node.getId()));
                                    mIsFavoritesState = false;
                                    getP().queryDataStkWareCarList(context, stkId, cid);
                                    break;
                                case -1:// 常售商品
                                    setRequestType(1, null, String.valueOf(node.getId()));
                                    mIsFavoritesState = false;
                                    getP().getDataOftenGoodsList(context, cid, noCompany, stkId, pageNo, pageSize, type);//获得常卖商品列表
                                    break;
                                case -2:// 收藏商品
                                    setRequestType(2, null, String.valueOf(node.getId()));
                                    mIsFavoritesState = true;
                                    getP().getDataSaveGoodsList(context, cid, noCompany, stkId, pageNo, pageSize, type);//获得收藏商品列表
                                    break;
                                default:// 根据类型查询商品
                                    mIsFavoritesState = false;
                                    //ids:包括下级的
                                    String ids = String.valueOf(node.getId());
                                    List<Node> children = node.getChildren();
                                    if (children != null && children.size() > 0) {
                                        for (Node node2 : children) {
                                            ids += "," + node2.getId();
                                        }
                                    }
                                    setRequestType(3, String.valueOf(ids), String.valueOf(node.getId()));
                                    if (!MyNetWorkUtil.isNetworkConnected()) {
                                        getP().queryCacheWare(context, "", String.valueOf(ids), pageNo, pageSize);
                                    }
                                    getP().getDataTypeGoodsList(context, cid, String.valueOf(node.getId()), noCompany, stkId, pageNo, pageSize, type);
                                    break;
                            }
                        } catch (Exception e) {
                            ToastUtils.showError(e);
                        }
                    }
                });
            } else {
                mTreeAdapter.setNodes(this.mTreeList, 0);
                mTreeAdapter.notifyDataSetChanged();
            }

            //默认加载第一个
            if (treeList != null && treeList.size() > 0) {
                if ("7".equals(type)) {
                    //根目录：车载商品
                    mTreeAdapter.setmId(-3);
                    setRequestType(5, null, "-3");
                    getP().queryDataStkWareCarList(context, stkId, cid);
                } else {
                    if (isContainSale) {
                        //包含“常售商品”和“收藏商品”
                        getP().getDataOftenGoodsList(context, cid, noCompany, stkId, pageNo, pageSize, type);//获得常卖商品列表
                    } else {
                        //（这边没有“常售商品”和“收藏商品”）
                        getP().getDataTypeGoodsList(context, cid, String.valueOf(treeList.get(0).get_id()), noCompany, stkId, pageNo, pageSize, type);
                    }
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //1:常售 2：收藏  3：分类   4：搜索(默认) 5:车载商品
    private void setRequestType(int type, String cacheWareType, String wareType) {
        try {
            mRequestType = type;
            pageNo = 1;
            mCacheWareType = cacheWareType;
            mWareType = wareType;
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    /**
     * 刷新右边adapter
     */
    private Data mCurrentData;// 当前要操作的对象
    private int mCurrentPosition;//
    public void refreshAdapterRight(List<Data> list, boolean editPrice) {
        try {
//            this.mEditPrice = editPrice;

            if (null == list) {
                return;
            }
            if (pageNo == 1) {
                //上拉刷新
                mWareAdapter.setNewData(list);
                mRefreshLayout.setNoMoreData(false);
                mRefreshLayout.finishRefresh();
            } else {
                //加载更多
                mWareAdapter.addData(list);
                mRefreshLayout.setNoMoreData(false);
                mRefreshLayout.finishLoadMore();
            }
            if (list.size() < pageSize) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                ToastUtils.showCustomToast("没有更多数据");
            }
            //车载商品：不需要加载更多
            if (5 == mRequestType) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }

    }

    /**
     * 收藏商品,取消收藏
     */
    public void setSuccessFavorites() {
        try {
            int userId = mCurrentData.getUserId();
            if (0 == userId) {
                ToastUtils.showShort(context, "收藏成功");
                mCurrentData.setUserId(1);
            } else if (1 == userId) {
                ToastUtils.showShort(context, "取消收藏成功");
                mCurrentData.setUserId(0);
            }
            if (mIsFavoritesState) {
                mWareAdapter.remove(mCurrentPosition);
            } else {
                mWareAdapter.getData().set(mCurrentPosition, mCurrentData);
            }
            mWareAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 设置执行价，原价；历史价格，原价(及窗体的的一些赋值)
     */
    public void setZyPirce5(HistoryOrgPriceBean bean) {
        try {
            //商品默认;历史价(大单位)==执行价；历史价（小单位）== 历史价(大单位)/ 换算比例；
            if (mCurrentData.getWareDj() == null){
                mOrgPriceStr5 = mMaxPriceStr5 = "0";
            }else{
                mOrgPriceStr5 = mMaxPriceStr5 = mCurrentData.getWareDj();
            }
            if (!MyStringUtil.isEmpty(mMaxPriceStr5) && !MyStringUtil.isEmpty(mCurrentData.getHsNum())) {
                mMinPriceStr5 = "" + (Double.valueOf(mMaxPriceStr5) / Double.valueOf(mCurrentData.getHsNum()));
            }
            if (null != bean && bean.isState()) {
                List<HistoryOrgPriceBean.Data> list = bean.getList();
                if (null != list && !list.isEmpty()) {
                    HistoryOrgPriceBean.Data currentHistoryOrgData = list.get(0);
                    if (null != currentHistoryOrgData) {
                        String autoPrice = bean.getAutoPrice();
                        if (!MyStringUtil.isEmpty(autoPrice) && "0".equals(autoPrice)) {
                        } else {
                            //历史价（大单位）
                            String wareDj = currentHistoryOrgData.getWareDj();
                            if (!MyStringUtil.isEmpty(wareDj) && !"0".equals(wareDj) && !"0.0".equals(wareDj)) {
                                mMaxPriceStr5 = currentHistoryOrgData.getWareDj();
                            }
                            //历史价（小单位）
                            String minHisPrice = currentHistoryOrgData.getMinHisPrice();
                            if (!MyStringUtil.isEmpty(minHisPrice) && !"0".equals(minHisPrice) && !"0.0".equals(minHisPrice)) {
                                mMinPriceStr5 = minHisPrice;
                            }
                            String orgPrice = currentHistoryOrgData.getOrgPrice();
                            if (!MyStringUtil.isEmpty(orgPrice) && !"0".equals(orgPrice) && !"0.0".equals(orgPrice) && "1".equals(autoPrice)) {
                                mOrgPriceStr5 = currentHistoryOrgData.getOrgPrice();
                            }
                        }
                    }
                }
            }

            //共同的
            mTvTitle5.setText(mCurrentData.getWareNm());
            mEtGg5.setText(mCurrentData.getWareGg());//规格
            mEtCount5.setText("1");
            mEtCount5.setSelection(mEtCount5.getText().length());//光标移动到文本末尾
            int sunitFront = mCurrentData.getSunitFront();//1:开单辅助单位
            if (sunitFront == 1) {
                //小单位
                isMaxUnit5 = false;
                mEtPrice5.setText(mMinPriceStr5);
                mTvUnit5.setText(mCurrentData.getMinUnit());
                unitCode5 = mCurrentData.getMinUnitCode();
                mEtMinPrice5.setText(String.valueOf(Double.valueOf(mMaxPriceStr5)));
            } else {
                //大单位
                isMaxUnit5 = true;
                mEtPrice5.setText(mMaxPriceStr5);
                mTvUnit5.setText(mCurrentData.getWareDw());
                unitCode5 = mCurrentData.getMaxUnitCode();//默认是包装单位代码
                mEtMinPrice5.setText(String.valueOf(Double.valueOf(mMaxPriceStr5) / Double.valueOf(mCurrentData.getHsNum())));
            }
            mTvZYPrice5.setText("历史价(小):" + mMinPriceStr5 + "\n历史价(大):" + mMaxPriceStr5 + "\n执行价:" + mCurrentData.getWareDj() + "\n原价:" + mOrgPriceStr5);
            //价格是否可以编辑
            if (mEditPrice) {
                mEtPrice5.setEnabled(true);
                mEtMinPrice5.setEnabled(true);
                mEtPrice5.setTextColor(MyColorUtil.getColorResId(R.color.gray_6));
                mEtMinPrice5.setTextColor(MyColorUtil.getColorResId(R.color.gray_6));
            } else {
                mEtPrice5.setEnabled(false);
                mEtMinPrice5.setEnabled(false);
                mEtPrice5.setTextColor(MyColorUtil.getColorResId(R.color.gray_9));
                mEtMinPrice5.setTextColor(MyColorUtil.getColorResId(R.color.gray_9));
            }
            mLayoutChangeUnit5.setVisibility(View.GONE);//默认关闭
            mLayoutTip5.setVisibility(View.GONE);//库存超出提示
            mEasyPop5.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    public void showDialog5(){
        try {
            String hsNum = "1";
            if(MyStringUtil.isNotEmpty(mCurrentData.getHsNum())){
                hsNum = mCurrentData.getHsNum();
            }

            if (MyStringUtil.isNotEmpty(mCurrentData.getWareDj())){
                mOrgPriceStr5 = mMaxPriceStr5 = mCurrentData.getWareDj();
            }else{
                if (MyStringUtil.isNotEmpty(mCurrentData.getSunitPrice())){
                    mOrgPriceStr5 = mMaxPriceStr5 = "" + (Double.valueOf(mCurrentData.getSunitPrice()) * Double.valueOf(hsNum));
                }else{
                    mOrgPriceStr5 = mMaxPriceStr5 = "0";
                }
            }

            if(MyStringUtil.isNotEmpty(mCurrentData.getSunitPrice())){
                mMinPriceStr5 = mCurrentData.getSunitPrice();
            }else{
                if (!MyStringUtil.isEmpty(mMaxPriceStr5)) {
                    mMinPriceStr5 = "" + (Double.valueOf(mMaxPriceStr5) / Double.valueOf(hsNum));
                }
            }

            //把价格设置为历史价
            if (MyStringUtil.isNotEmpty(mAutoPrice) && "1".equals(mAutoPrice)){
                if(MyStringUtil.isNotEmpty(mCurrentData.getMaxHisPfPrice())){
                    mMaxPriceStr5 = mCurrentData.getMaxHisPfPrice();
                }else{
                    if (MyStringUtil.isNotEmpty(mCurrentData.getMinHisPfPrice())){
                        mMaxPriceStr5 = "" + (Double.valueOf(mCurrentData.getSunitPrice()) * Double.valueOf(hsNum));
                    }
                }
                if(MyStringUtil.isNotEmpty(mCurrentData.getMinHisPfPrice())){
                    mMinPriceStr5 = mCurrentData.getMinHisPfPrice();
                }else{
                    if (!MyStringUtil.isEmpty(mMaxPriceStr5)) {
                        mMinPriceStr5 = "" + (Double.valueOf(mMaxPriceStr5) / Double.valueOf(hsNum));
                    }
                }
            }

            //共同的
            mTvTitle5.setText(mCurrentData.getWareNm());
            mEtGg5.setText(mCurrentData.getWareGg());//规格
            mEtCount5.setText("1");
            mEtCount5.setSelection(mEtCount5.getText().length());//光标移动到文本末尾
            int sunitFront = mCurrentData.getSunitFront();//1:开单辅助单位
            if (sunitFront == 1) {
                //小单位
                isMaxUnit5 = false;
                mEtPrice5.setText(mMinPriceStr5);
                mTvUnit5.setText(mCurrentData.getMinUnit());
                unitCode5 = mCurrentData.getMinUnitCode();
                mEtMinPrice5.setText(String.valueOf(Double.valueOf(mMaxPriceStr5)));
            } else {
                //大单位
                isMaxUnit5 = true;
                mEtPrice5.setText(mMaxPriceStr5);
                mTvUnit5.setText(mCurrentData.getWareDw());
                unitCode5 = mCurrentData.getMaxUnitCode();//默认是包装单位代码
                mEtMinPrice5.setText(String.valueOf(Double.valueOf(mMaxPriceStr5) / Double.valueOf(mCurrentData.getHsNum())));
            }
            mTvZYPrice5.setText("历史价(小):" + mCurrentData.getMinHisPfPrice() + "\n历史价(大):" + mCurrentData.getMaxHisPfPrice() + "\n执行价:" + mCurrentData.getWareDj() + "\n原价:" + mOrgPriceStr5);
            //价格是否可以编辑
            if (mEditPrice) {
                mEtPrice5.setEnabled(true);
                mEtMinPrice5.setEnabled(true);
                mEtPrice5.setTextColor(MyColorUtil.getColorResId(R.color.gray_6));
                mEtMinPrice5.setTextColor(MyColorUtil.getColorResId(R.color.gray_6));
            } else {
                mEtPrice5.setEnabled(false);
                mEtMinPrice5.setEnabled(false);
                mEtPrice5.setTextColor(MyColorUtil.getColorResId(R.color.gray_9));
                mEtMinPrice5.setTextColor(MyColorUtil.getColorResId(R.color.gray_9));
            }
            mLayoutChangeUnit5.setVisibility(View.GONE);//默认关闭
            mLayoutTip5.setVisibility(View.GONE);//库存超出提示
            mEasyPop5.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 设置采购价
     */
    public void setPurchasePrice() {
        try {
            mMaxPriceStr5 = "0";
            //采购价（大）不空
            if (!MyStringUtil.isEmpty(mCurrentData.getInPrice())) {
                mMaxPriceStr5 = mCurrentData.getInPrice();
            }
            //采购价（大）不空，换算比例不空
            if (!MyStringUtil.isEmpty(mMaxPriceStr5) && !MyStringUtil.isEmpty(mCurrentData.getHsNum())) {
                mMinPriceStr5 = "" + (Double.valueOf(mMaxPriceStr5) / Double.valueOf(mCurrentData.getHsNum()));
            }
            //采购价（小）不空
            if (!MyStringUtil.isEmpty(mCurrentData.getMinInPrice())) {
                mMinPriceStr5 = mCurrentData.getMinInPrice();
            }
            //防止采购价（大）：采购价（大）空，采购价（小）不空，换算比例不空
            if (MyStringUtil.isEmpty(mCurrentData.getInPrice()) && !MyStringUtil.isEmpty(mCurrentData.getInPrice()) && !MyStringUtil.isEmpty(mCurrentData.getHsNum())) {
                mMaxPriceStr5 = "" + (Double.valueOf(mCurrentData.getInPrice()) * Double.valueOf(mCurrentData.getHsNum()));
            }
            //共同的
            mTvTitle5.setText(mCurrentData.getWareNm());
            mEtGg5.setText(mCurrentData.getWareGg());//规格
            mEtCount5.setText("1");
            mEtCount5.setSelection(mEtCount5.getText().length());//光标移动到文本末尾
            int sunitFront = mCurrentData.getSunitFront();//1:开单辅助单位
            if (sunitFront == 1) {
                //小单位
                isMaxUnit5 = false;
                mEtPrice5.setText(mMinPriceStr5);
                mTvUnit5.setText(mCurrentData.getMinUnit());
                unitCode5 = mCurrentData.getMinUnitCode();
            } else {
                //大单位
                isMaxUnit5 = true;
                mEtPrice5.setText(mMaxPriceStr5);
                mTvUnit5.setText(mCurrentData.getWareDw());
                unitCode5 = mCurrentData.getMaxUnitCode();//默认是包装单位代码
            }
            mTvZYPrice5.setText("采购价(小):" + mMinPriceStr5 + "\n采购价(大):" + mMaxPriceStr5 + "\n执行价:" + mCurrentData.getWareDj());
            //价格是否可以编辑
            if (mEditPrice) {
                mEtPrice5.setEnabled(true);
                mEtMinPrice5.setEnabled(true);
            } else {
                mEtPrice5.setEnabled(false);
                mEtMinPrice5.setEnabled(false);
            }
            mLayoutChangeUnit5.setVisibility(View.GONE);//默认关闭
            mLayoutTip5.setVisibility(View.GONE);//库存超出提示
            mEasyPop5.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
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
                    if (!MyStringUtil.isEmpty(result)) {
                        doScanOrVoiceToSearch(result);
                    } else {
                        ToastUtils.showCustomToast("扫描内容为空");
                    }
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }
    //TODO******************************扫描:end************************************


    /**
     * 该商品是否添加过
     */
    private boolean isAddedGoods(int wareId) {
        boolean flag = false;
        try {
            if (MyCollectionUtil.isEmpty(mWareIdList)) {
                return false;
            }
            for (int id: mWareIdList) {
                if (MyStringUtil.eq(id, wareId)) {
                    return true;
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
        return flag;
    }

    /**
     * 添加商品
     */
    public void addGoods(Data data) {
        try {
            isMaxUnit5 = true;//是否是主单位（默认是）
            if ("4".equals(type)) {//销售小结
                if (data != null) {
                    mTvTitle4.setText(data.getWareNm());
                    mEasyPop4.showAtLocation(parent, Gravity.CENTER, 0, 0);
                }
            } else if ("8".equals(type)) {//8.采购单
                if (data != null) {
                    setPurchasePrice();
                }
            } else {
                //5.订货下单 7.车削下单
                if (data != null) {
                    showDialog5();
//                    getP().getDataHistoryOrgPrice(context, cid, String.valueOf(data.getWareId()));
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //数量是否超出库存
    private void showStorageTip() {
        try {
            double maxStockCount;//主单位的数量
            double minStockCount;//辅助单位的数量
            if (isMaxUnit5) {
                maxStockCount = getStorageCount();
                minStockCount = MyDoubleUtils.getDecimal(maxStockCount * Double.valueOf(mCurrentData.getHsNum()));
            } else {
                minStockCount = getStorageCount();
                maxStockCount = MyDoubleUtils.getDecimal(minStockCount / Double.valueOf(mCurrentData.getHsNum()));
            }
            mLayoutTip5.setVisibility(View.VISIBLE);
            mTvTip5.setText("输入的数量超出剩余库存(" + maxStockCount + mCurrentData.getWareDw() + "=" + minStockCount + mCurrentData.getMinUnit() + ")");
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 获取与输入数量比较的“库存数量”(这边有单位切换时：切换换算)
     */
    private double getStorageCount() {
        try {
            //遍历该商品已添加的数量:全部以“大单位”计算
            ArrayList<HashMap<String, String>> tempXsList = new ArrayList<>();
            tempXsList.addAll(mXsList);
            tempXsList.addAll(Constans.xsList);
            double sum = 0;
            for (HashMap<String, String> map : tempXsList) {
                if (map.get(ConstantUtils.Map.STEP5_0_GOODS_ID).equals("" + mCurrentData.getWareId())) {
                    String count = map.get(ConstantUtils.Map.STEP5_18_STK_COUNT);
                    String hsNum = mCurrentData.getHsNum();
                    String beUnit = map.get(ConstantUtils.Map.STEP5_8_GOODS_BEUNIT);//包装单位代码或计量单位代码
                    String maxUnitCode = mCurrentData.getMaxUnitCode();//包装单位代码
                    if (!MyStringUtil.isEmpty(count)) {
                        if (beUnit.equals(maxUnitCode)) {
                            sum += Double.valueOf(count);
                        } else {
                            sum += Double.valueOf(count) / Double.valueOf(hsNum);
                        }
                    }
                }
            }

            String stkQty = mCurrentData.getStkQty();
            String occQty = mCurrentData.getOccQty();
            //如果为空，返回double的最大值
            if (MyStringUtil.isEmpty(stkQty)) {
                return Double.MAX_EXPONENT;
            }
            //“已占库存”如果为空，返回“实际库存”
            if (MyStringUtil.isEmpty(occQty)) {
                double doubleStkQty = Double.valueOf(stkQty);
                if (isMaxUnit5) {
                    return MyDoubleUtils.getDecimal(Double.valueOf(doubleStkQty) - sum);
                } else {
                    //如果是辅助单位；要乘以换算
                    return MyDoubleUtils.getDecimal((Double.valueOf(doubleStkQty) - sum)) * Double.valueOf(mCurrentData.getHsNum());
                }
            }
            //返回剩余库存
            if (!MyStringUtil.isEmpty(stkQty) && !MyStringUtil.isEmpty(occQty)) {
                double doubleSyQty = Double.valueOf(stkQty) - Double.valueOf(occQty);
                if (isMaxUnit5) {
                    return MyDoubleUtils.getDecimal(Double.valueOf(doubleSyQty) - sum);
                } else {
                    //如果是辅助单位；要乘以换算
                    double d = (doubleSyQty - sum) * Double.valueOf(mCurrentData.getHsNum());
                    return MyDoubleUtils.getDecimal(d);
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
            return Double.MAX_EXPONENT;
        }
        return Double.MAX_EXPONENT;
    }

    //TODO ********************************************************************************************************************************************************
    //TODO ********************************************************************************************************************************************************
    /**
     * 显示隐藏图片
     */
    private void doShowHidePic(){
        try {
            Boolean isShowWarePic = SPUtils.getBoolean(ConstantUtils.Sp.CHOOSE_WARE_PIC_SHOW);
            if (isShowWarePic != null && isShowWarePic) {
                SPUtils.setBoolean(ConstantUtils.Sp.CHOOSE_WARE_PIC_SHOW, false);
            } else {
                SPUtils.setBoolean(ConstantUtils.Sp.CHOOSE_WARE_PIC_SHOW, true);
            }
            mWareAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 语音搜索对话框
     */
    private void showDialogVoice() {
        MyVoiceDialog dialog = new MyVoiceDialog(context);
        dialog.show();
        dialog.setOnSuccessOnclick(new MyVoiceDialog.OnSuccessListener() {
            @Override
            public void onSuccessOnclick(String result) {
                doScanOrVoiceToSearch(result);
            }
        });
    }

    //处理扫描和语音搜索成功回调：搜索列表
    private void doScanOrVoiceToSearch(String result) {
        try {
            setRequestType(4, null, null);
            mEtSearch.setText(result);
            mEtSearch.setSelection(mEtSearch.length());
            getP().queryCacheWare(context, result, null, pageNo, pageSize);
            getP().getDataKeyWordGoodsList(context, cid, result, noCompany, stkId, pageNo, pageSize, type);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 搜索
     */
    private void doSearch(){
        try {
            String searchStr = mEtSearch.getText().toString().trim();
            setRequestType(4, null, null);
            mIsFavoritesState = false;
            getP().queryCacheWare(context, searchStr, null, pageNo, pageSize);
            getP().getDataKeyWordGoodsList(context, cid, searchStr, noCompany, stkId, pageNo, pageSize, type);
            mTreeAdapter.setmId(-10);//默认值
            mTreeAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //零库存过滤
    private void showDialog0() {
        try {
            String content;
            String storage0 = SPUtils.getSValues(ConstantUtils.Sp.STORAGE_ZERO);
            if (!MyStringUtil.isEmpty(storage0) && storage0.equals("1")) {
                content = "您已开启库存0过滤，是否要关闭？";
            } else {
                content = "您是否要开启库存0过滤？";
            }
            final NormalDialog dialog = new NormalDialog(context);
            dialog.content(content).show();
            dialog.setOnBtnClickL(null,
                    new OnBtnClickL() {
                        @Override
                        public void onBtnClick() {
                            try {
                                String storage0 = SPUtils.getSValues(ConstantUtils.Sp.STORAGE_ZERO);
                                if (!MyStringUtil.isEmpty(storage0) && storage0.equals("1")) {
                                    SPUtils.setValues(ConstantUtils.Sp.STORAGE_ZERO, "");
                                    mTvStorage0.setText("过滤0库存");
                                } else {
                                    SPUtils.setValues(ConstantUtils.Sp.STORAGE_ZERO, "1");
                                    mTvStorage0.setText("已过滤0库存");
                                }
                            } catch (Exception e) {
                                ToastUtils.showError(e);
                            }
                        }
                    });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    private void doRefresh(){
        try {
            String search = mEtSearch.getText().toString().trim();
            if (!MyNetWorkUtil.isNetworkConnected()) {
                getP().queryCacheWare(context, search, mCacheWareType, pageNo, pageSize);
            }
            //1:常售 2：收藏  3：分类   4：搜索 5:车载商品
            switch (mRequestType) {
                case 1:
                    getP().getDataOftenGoodsList(context, cid, noCompany, stkId, pageNo, pageSize, type);
                    break;
                case 2:
                    getP().getDataSaveGoodsList(context, cid, noCompany, stkId, pageNo, pageSize, type);
                    break;
                case 3:
                    getP().getDataTypeGoodsList(context, cid, mWareType, noCompany, stkId, pageNo, pageSize, type);
                    break;
                case 4:
                    getP().getDataKeyWordGoodsList(context, cid, search, noCompany, stkId, pageNo, pageSize, type);
                    break;
                case 5:
                    getP().queryDataStkWareCarList(context, stkId, cid);
                    break;
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

}
