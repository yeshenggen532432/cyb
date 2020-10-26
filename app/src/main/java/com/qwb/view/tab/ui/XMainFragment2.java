package com.qwb.view.tab.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.deadline.statebutton.StateButton;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.db.DMessageBean;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.Constans;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyDividerUtil;
import com.qwb.utils.MyMenuUtil;
import com.qwb.utils.MyRecyclerViewUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.ApplyBean;
import com.qwb.view.base.model.NewApplyComparator;
import com.qwb.view.storehouse.model.StorehouseInBean;
import com.qwb.view.tab.adapter.ApplyAdapter2;
import com.qwb.view.tab.adapter.CategroyAdapter;
import com.qwb.view.tab.adapter.MainAdapter2;
import com.qwb.view.tab.model.ApplyBean2;
import com.qwb.view.tab.model.MainFuncBean;
import com.qwb.view.tab.model.MainResult;
import com.qwb.view.tab.model.ShopInfoBean;
import com.qwb.view.tab.parsent.PXMain2;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XFragment;

/**
 * tab:首页
 */
public class XMainFragment2 extends XFragment<PXMain2> {

    public XMainFragment2() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.x_fragment_main2;
    }

    @Override
    public PXMain2 newP() {
        return new PXMain2();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    private void initEvent() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initEvent();
        initUI();
        doIntent();
        queryData();
    }

    private void doIntent() {
        //默认今天
        mStartDate = MyTimeUtils.getTodayStr();
        mEndDate = MyTimeUtils.getTodayStr();
        mTvStartDate.setText(mStartDate);
        mTvEndDate.setText(mEndDate);
        mRadioGroup.check(R.id.rb_today);
    }

    private String mShopNo, mStartDate, mEndDate;
    public void queryData(){
        getP().query(context, mShopNo, mStartDate, mEndDate);
    }

    public void initUI() {
        initHead();
        initOther();
        initAdapter();
        initAdapterMenu();
    }

    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorGreen(context);
        mTvHeadTitle.setText("全部门店");

        mTvHeadTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyCollectionUtil.isEmpty(mShopInfoList)){
                    getP().queryShop(context);
                }else{
                    showDialogShop(mShopInfoList);
                }
            }
        });
    }

    @BindView(R.id.tv_start_date)
    TextView mTvStartDate;
    @BindView(R.id.tv_end_date)
    TextView mTvEndDate;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.tv_pos_amt)
    TextView mTvPostAmt;
    @BindView(R.id.tv_input_amt)
    TextView mTvInputAmt;
    @BindView(R.id.tv_stk_out_amt)
    TextView mTvStkOutAmt;
    @BindView(R.id.tv_stk_in_amt)
    TextView mTvStkInAmt;
    @BindView(R.id.tv_rec_amt)
    TextView mTvRecAmt;
    @BindView(R.id.tv_pay_amt)
    TextView mTvPayAmt;
    private void initOther() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_yesterday:
                        doDate(MyTimeUtils.getYesterday(), MyTimeUtils.getYesterday());
                        break;
                    case R.id.rb_today:
                        doDate(MyTimeUtils.getTodayStr(), MyTimeUtils.getTodayStr());
                        break;
                    case R.id.rb_this_week:
                        doDate(MyTimeUtils.getFirstOfThisWeek(), MyTimeUtils.getLastOfThisWeek());
                        break;
                    case R.id.rb_this_month:
                        doDate(MyTimeUtils.getFirstOfMonth(), MyTimeUtils.getLastOfMonth());
                        break;
                }
            }
        });

        mTvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDate();
            }
        });
        mTvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDate();
            }
        });
    }

    private void showDialogDate() {
        new MyDoubleDatePickerDialog(context, "选择时间", mStartDate, mEndDate, new MyDoubleDatePickerDialog.DateTimeListener() {
            @Override
            public void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate) {
                mRadioGroup.clearCheck();
                doDate(startDate, endDate);
            }

            @Override
            public void onCancel() {
            }
        }).show();
    }

    public void doDate(String startDate, String endDate){
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        mTvStartDate.setText(mStartDate);
        mTvEndDate.setText(mEndDate);
        queryData();
    }

    /**
     * 初始化适配器
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    MainAdapter2 mAdapter;
    private void initAdapter() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        mAdapter = new MainAdapter2();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    MainFuncBean item = (MainFuncBean) adapter.getData().get(position);
                    String url = Constans.H5_BASE_URL+ "token=" + SPUtils.getTK() + "#" + item.getUrl();
                    ActivityManager.getInstance().jumpToWebX5Activity(context, url, null);
                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * 适配器
     */
    RecyclerView mRecyclerViewMenu;
    ApplyAdapter2 mAdapterMenu;
    private void initAdapterMenu() {
        View footView = LayoutInflater.from(context).inflate(R.layout.x_fragment_main2_bottom, null);
        mAdapter.setFooterView(footView);
        mRecyclerViewMenu = footView.findViewById(R.id.recyclerView_menu);
        mAdapterMenu = new ApplyAdapter2(context);
//        MyRecyclerViewUtil.init(context, mRecyclerViewMenu, mAdapterMenu);
        mRecyclerViewMenu.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerViewMenu.setAdapter(mAdapterMenu);
        initAdapterData();
    }

    private void initAdapterData() {
        try {
//            List<ApplyBean2> myItems = new ArrayList<>();
            String childrenStr = SPUtils.getSValues(ConstantUtils.Sp.APP_LIST_CHILDREN);
            List<ApplyBean2> applyList0 = new ArrayList<>();
            if (MyStringUtil.isNotEmpty(childrenStr)) {
                List<ApplyBean> children = JSON.parseArray(childrenStr, ApplyBean.class);
                Collections.sort(children, new NewApplyComparator());//排序
                if (MyCollectionUtil.isNotEmpty(children)) {
                    for (ApplyBean child : children) {
                        if (MyMenuUtil.hasMenuTabTable(child.getPId())){
                            ApplyBean2 bean1 = new ApplyBean2();
                            bean1.setLabel(child.getApplyName());
                            bean1.setApplys(child.getChildren());
                            applyList0.add(bean1);
                        }
                    }
                }
            }
//            Collections.sort(applyList0, new NewApplyComparator());
//            ApplyBean2 bean0 = new ApplyBean2();
//            bean0.setApplys(applyList0);
//            myItems.add(bean0);

            if (null != mAdapterMenu) {
                mAdapterMenu.setNewData(applyList0);
            }
        } catch (Exception e) {
        }
    }

    public void doUI(MainResult bean){
        mTvPostAmt.setText(bean.getPosAmt());
        mTvInputAmt.setText(bean.getInputAmt());
        mTvStkOutAmt.setText(bean.getStkOutAmt());
        mTvStkInAmt.setText(bean.getStkInAmt());
        mTvRecAmt.setText(bean.getRecAmt());
        mTvPayAmt.setText(bean.getPayAmt());
        refreshAdapter(bean.getFuncList());
    }

    private List<ShopInfoBean> mShopInfoList = new ArrayList<>();
    public void showDialogShop(final List<ShopInfoBean> dataList){
        if (MyCollectionUtil.isNotEmpty(dataList)){
            if (MyCollectionUtil.isEmpty(mShopInfoList)){
                mShopInfoList.addAll(dataList);
            }
           final ArrayList<DialogMenuItem> items = new ArrayList<>();
            //默认第一条数据：全部门店
            items.add(new DialogMenuItem("全部门店", 0));
            for (ShopInfoBean bean : dataList) {
                items.add(new DialogMenuItem(bean.getShopName(), bean.getId()));
            }
            NormalListDialog dialog = new NormalListDialog(context, items);
            dialog.title("选择门店").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DialogMenuItem item = items.get(position);
                    if (position == 0){
                        mShopNo = "";
                        mTvHeadTitle.setText(item.mOperName);
                        queryData();
                    }else{
                        for (ShopInfoBean bean : dataList) {
                            if (MyStringUtil.eq(""+ bean.getId(), ""+item.mResId)){
                                mShopNo = bean.getShopNo();
                                mTvHeadTitle.setText(bean.getShopName());
                                queryData();
                                return;
                            }
                        }
                    }
                }
            });
        }
    }


    /**
     * 刷新适配器
     */
    public void refreshAdapter(List<MainFuncBean> dataList) {
        if (null == dataList) {
            return;
        }
        mAdapter.setNewData(dataList);
    }



}
