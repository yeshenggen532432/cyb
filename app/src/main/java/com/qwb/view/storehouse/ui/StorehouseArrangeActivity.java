package com.qwb.view.storehouse.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.deadline.statebutton.StateButton;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyTableUtil;
import com.qwb.view.storehouse.adapter.StorehouseArrangeLeftAdapter;
import com.qwb.view.storehouse.adapter.StorehouseArrangeRightAdapter;
import com.qwb.view.storehouse.model.StorehouseInBean;
import com.qwb.view.storehouse.model.StorehouseBean;
import com.qwb.view.storehouse.model.StorehouseInSubBean;
import com.qwb.view.storehouse.model.StorehouseWareBean;
import com.qwb.view.storehouse.parsent.PStorehouseArrange;
import com.qwb.widget.MyDateTimePickerDialog;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.stk.StorageBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.utils.MyKeyboardUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.widget.table.TableHorizontalScrollView;
import com.qwb.widget.treedialog.MyTreeDialog;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 库位管理:库位整理
 */
public class StorehouseArrangeActivity extends XActivity<PStorehouseArrange> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_storehouse_arrange;
    }

    @Override
    public PStorehouseArrange newP() {
        return new PStorehouseArrange();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        doIntent();
    }

    private void initUI() {
        initHead();
        initOther();
        initTableView();
    }

    Integer mBillId;
    private void initIntent() {
        Intent intent = getIntent();
        mBillId = intent.getIntExtra(ConstantUtils.Intent.ID, 0);
    }

    private void doIntent() {
        if(mBillId != 0){
            getP().queryStorehouseById(context, mBillId);
        }
    }

    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;

    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mTvHeadTitle.setText("库位整理");
        mTvHeadRight.setText("提交");
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mHeadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }

    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_storage)
    TextView mTvStorage;
    @BindView(R.id.et_remark)
    EditText mEtRemark;
    @BindView(R.id.tv_storehouse)
    TextView mTvStorehouse;
    @BindView(R.id.tv_wares)
    TextView mTvWares;
    @BindView(R.id.sb_load_ware)
    StateButton mSbLoadWare;
    private void initOther() {
        //选择时间
        mTvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogTime();
            }
        });
        mTvTime.setText(MyTimeUtils.getNowTime());
        //选择仓库
        mTvStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowStorage = true;
                if (MyCollectionUtil.isEmpty(mStorageList)) {
                    getP().queryStorage(context);
                } else {
                    showDialogStorage(mStorageList);
                }
            }
        });
        //选择库位
        mTvStorehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    verifyStk();
                    showDialogTreeStorehouse();
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
        //选择商品
        mTvWares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    verifyStk();
                    ActivityManager.getInstance().jumpToChooseWareActivity(context, mStkId, null);
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
        //加载商品
        mSbLoadWare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getP().queryStkHouseWareList(context, mStkId, mStorehouseIds, mWareIds);
            }
        });

    }

    public void verifyStk() throws Exception {
        if (mStkId == 0){
            throw new Exception("请先选择仓库");
        }
    }


    @BindView(R.id.rv_left)
    RecyclerView mRvLeft;
    @BindView(R.id.rv_right)
    RecyclerView mRvRight;
    @BindView(R.id.sv_ware)
    TableHorizontalScrollView mSvWare;
    @BindView(R.id.tv_table_title_left)
    TextView mTvTableTitleLeft;
    private StorehouseArrangeLeftAdapter mLeftAdapter;
    private StorehouseArrangeRightAdapter mRightAdapter;

    private StorehouseWareBean mCurrentItem;
    private int mCurrentPosition;

    public void initTableView() {
        //left
        mRvLeft.setLayoutManager(new LinearLayoutManager(this));
        mRvLeft.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_b)
                .sizeResId(R.dimen.dp_0_5)
                .showLastDivider()
                .build());
        mLeftAdapter = new StorehouseArrangeLeftAdapter();
        mRvLeft.setAdapter(mLeftAdapter);
        //right
        mRvRight.setLayoutManager(new LinearLayoutManager(this));
        mRvRight.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_b)
                .sizeResId(R.dimen.dp_0_5)
                .showLastDivider()
                .build());
        mRightAdapter = new StorehouseArrangeRightAdapter();
        mRvRight.setAdapter(mRightAdapter);
        //右边点击事件：1.选择库位；2.删除
        mRightAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    StorehouseWareBean item = (StorehouseWareBean) adapter.getData().get(position);
                    mCurrentItem = item;
                    mCurrentPosition = position;
                    switch (view.getId()) {
                        case R.id.tv_table_content_right_item5://选择库位
                            mShowDialogStorehouse = true;
                            if (MyCollectionUtil.isEmpty(mStorehouseList)) {
                                getP().queryStorehouseList(context, mStkId);
                            } else {
                                showDialogStorehouse(mStorehouseList);
                            }
                            break;
                        case R.id.tv_table_content_right_item9://删除
                            showDialogDel();
                            break;
                    }
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });

        //设置两个列表的同步滚动
        MyTableUtil.getInstance().setSyncScrollListener(mRvLeft, mRvRight, mSvWare);

        //添加商品
        mTvTableTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });

    }


    /**
     * 对话框-选择时间
     */
    private String mTimeStr = MyTimeUtils.getNowTime();

    private void showDialogTime() {
        new MyDateTimePickerDialog(context, "选择时间", mTimeStr,
                new MyDateTimePickerDialog.DateTimeListener() {
                    @Override
                    public void onSetTime(int year, int month, int day, int hour, int minute, String timeStr) {
                        try {
                            mTimeStr = timeStr;
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

    /**
     * 处理UI(页面显示)
     */
    private int mStkId;
    private int mSourceId;
    private String mSourceNo;

    public void doUI(StorehouseInBean bean) {
        try {
//            判断是否可以修改
            if(!(bean.getStatus()!=null && (bean.getStatus() == -2 || bean.getStatus() == 0))){
                mTvHeadRight.setText("");
                mHeadRight.setClickable(false);
            }

            mTimeStr = MyTimeUtils.getNowTime();
            mStkId = bean.getStkId();
            mSourceId = bean.getId();
            mSourceNo = bean.getBillNo();

            mTvTime.setText(mTimeStr);
            mTvStorage.setText(bean.getStkName());
            getP().queryStorage(null);

            //table
            List<StorehouseInSubBean> subList = bean.getList();
            List<StorehouseWareBean> wareList = new ArrayList<>();
            if(MyCollectionUtil.isNotEmpty(subList)){
                for (StorehouseInSubBean subBean: subList) {
                    StorehouseWareBean wareBean = new StorehouseWareBean();
                    wareBean.setWareId(subBean.getWareId());
                    wareBean.setWareNm(subBean.getWareNm());
                    wareBean.setHsNum(subBean.getHsNum());
                    wareBean.setBeUnit(subBean.getBeUnit());
                    wareBean.setMaxUnitCode(subBean.getMaxUnitCode());
                    wareBean.setMinUnitCode(subBean.getMinUnitCode());
                    wareBean.setUnitName(subBean.getUnitName());
                    wareBean.setMinUnit(subBean.getMinUnit());
                    wareBean.setInStkId(subBean.getInStkId());
                    wareBean.setInStkName(subBean.getInStkName());
                    wareBean.setQty(subBean.getQty());
//                    wareBean.setHouseId(subBean.getOutStkId());
//                    wareBean.setHouseName(subBean.getOutStkName());
//                    wareBean.setInQty(subBean.getInQty());
//                    wareBean.setMinInQty(subBean.getMinInQty());
                    wareList.add(wareBean);
                }
                doTable(wareList);
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 刷新表格数据
     */
    private void refreshAdapterRight() {
        try {
            //标记商品的个数（重复商品颜色变）
//            setRepeatMap();
            //刷新
            mLeftAdapter.notifyDataSetChanged();
            mRightAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //选择商品
            if (resultCode == ConstantUtils.Intent.RESULT_CODE_CHOOSE_WARE){
                ArrayList<ShopInfoBean.Data> datas = data.getParcelableArrayListExtra(ConstantUtils.Intent.CHOOSE_WARE);
                doChooseWare(datas);
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 处理选择商品
     */
    private String mWareIds;
    public void doChooseWare(ArrayList<ShopInfoBean.Data> datas){
        String wareNms = "";
        if(MyCollectionUtil.isNotEmpty(datas)){
            for (ShopInfoBean.Data data:datas) {
                if(MyStringUtil.isEmpty(wareNms)){
                    mWareIds = "" + data.getWareId();
                    wareNms = "" + data.getWareNm();
                }else{
                    mWareIds += "," + data.getWareId();
                    wareNms += "," + data.getWareNm();
                }
            }
        }else{
            mWareIds = "";
        }
        mTvWares.setText(wareNms);
    }

    /**
     * 对话框-选择仓库
     */
    private List<StorageBean.Storage> mStorageList = new ArrayList<>();
    private boolean mShowStorage = false;
    public void showDialogStorage(List<StorageBean.Storage> storageList) {
        try {
            if (MyCollectionUtil.isEmpty(mStorageList)) {
                mStorageList = storageList;
            }
            if (MyCollectionUtil.isEmpty(mStorageList)) {
                ToastUtils.showCustomToast("没有仓库可以选择");
                return;
            }

            final ArrayList<DialogMenuItem> items = new ArrayList<>();
            for (StorageBean.Storage storage : mStorageList) {
                items.add(new DialogMenuItem(storage.getStkName(), storage.getId()));
                if(mStkId > 0 && String.valueOf(mStkId).equals("" + storage.getId())){
                    mTvStorage.setText(storage.getStkName());
                }
            }
            if (!mShowStorage){
                return;
            }
            NormalListDialog dialog = new NormalListDialog(context, items);
            dialog.title("选择仓库").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                    mStkId = items.get(i).mResId;
                    mTvStorage.setText(items.get(i).mOperName);
                    getP().queryStorehouseList(context, mStkId);
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    /**
     * 对话框-移入库位
     */
    private Integer mTempStkId;
    private String mTempStkName = "临时库位";
    private boolean mShowDialogStorehouse = false;

    public void showDialogStorehouse(List<StorehouseBean> storehouseList) {
        try {
            if (MyCollectionUtil.isEmpty(mStorehouseList)) {
                mStorehouseList = storehouseList;
            }
            if (MyCollectionUtil.isEmpty(mStorehouseList)) {
                ToastUtils.showCustomToast("没有库位");
                return;
            }
            final ArrayList<DialogMenuItem> items = new ArrayList<>();
            for (StorehouseBean bean : mStorehouseList) {
                items.add(new DialogMenuItem(bean.getHouseName(), bean.getId()));
                if (bean.getHouseName().equals(mTempStkName)) {
                    //临时库位赋值
                    mTempStkId = bean.getId();
                }
            }
            if (!mShowDialogStorehouse) {
                return;
            }
            NormalListDialog dialog = new NormalListDialog(context, items);
            dialog.title("选择库位").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                    try {
                        DialogMenuItem item = items.get(i);
                        mCurrentItem.setInStkId(item.mResId);
                        mCurrentItem.setInStkName(item.mOperName);
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


    /**
     * 对话框-删除商品
     */
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


    /**
     * 提交数据
     */
    private void addData() {
        try {
            if (mStkId == 0) {
                ToastUtils.showCustomToast("请选择仓库");
                return;
            }
            String remark = mEtRemark.getText().toString().trim();
            List<StorehouseWareBean> subList = mRightAdapter.getData();
            List<StorehouseWareBean> tempSubList = new ArrayList<>();
            if (MyCollectionUtil.isNotEmpty(subList)) {
                for (int i = 0; i < subList.size(); i++) {
                    StorehouseWareBean bean = subList.get(i);
                    if (MyStringUtil.isNotEmpty(bean.getInStkName())) {
                        if (bean.getInStkName().equals(bean.getOutStkName())) {
                            ToastUtils.showLongCustomToast("第" + (i + 1) + "行, <<" + bean.getWareNm() + ">>移入库位和移出库位一样");
                            return;
                        }
                        if (bean.getInQty() != null || bean.getMinInQty() != null) {
                            tempSubList.add(bean);
                        }
                    }
                }
            }
            if (MyCollectionUtil.isEmpty(tempSubList)) {
                ToastUtils.showCustomToast("请选择商品或请输入数量");
                return;
            }

            String json = JSON.toJSONString(tempSubList);
            getP().addData(context, mStkId, mTimeStr, mSourceId, mSourceNo, remark, json, mBillId);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    List<TreeBean> mTreeStorehouse = new ArrayList<>();
    List<StorehouseBean> mStorehouseList = new ArrayList<>();
    String wareIds = null;

    /**
     * 选择库位
     */
    public void doStorehouseTree(List<TreeBean> tree, List<StorehouseBean> datas) {
        this.mTreeStorehouse = tree;
        this.mStorehouseList = datas;
    }

    private String mStorehouseIds;
    private void showDialogTreeStorehouse() {
        final MyTreeDialog mTreeDialog = new MyTreeDialog(context, mTreeStorehouse, true);
        mTreeDialog.title("选择库位").show();
        mTreeDialog.setOnClickListener(new MyTreeDialog.OnClickListener() {
            @Override
            public void onOkListener(String containPIds, String noContainPIds, Map<Integer, Integer> checkMap) {
                String storehouseNames = "";
                for (StorehouseBean bean : mStorehouseList) {
                    if (noContainPIds.contains("" + bean.getId())) {
                        if (MyStringUtil.isEmpty(storehouseNames)) {
                            storehouseNames += "" + bean.getHouseName();
                        } else {
                            storehouseNames += "," + bean.getHouseName();
                        }
                    }
                }
                mTvStorehouse.setText(storehouseNames);
                mStorehouseIds = noContainPIds;
            }
        });
    }

    /**
     * 处理表格
     */
    public void doTable(List<StorehouseWareBean> datas) {
        try {
            //过滤：合计数
            if (MyCollectionUtil.isNotEmpty(datas)) {
                if (MyCollectionUtil.isNotEmpty(datas)) {
                    //最后一条数据是合计
                    StorehouseWareBean  lastBean = datas.get(datas.size() - 1);
                    if(lastBean.getWareId() == null){
                        datas.remove(datas.size() - 1);
                    }
                    for (StorehouseWareBean bean : datas) {
                        bean.setOutStkId(bean.getHouseId());
                        bean.setOutStkName(bean.getHouseName());
                    }
                } else {
                    datas = new ArrayList<>();
                }
            }else{
                datas = new ArrayList<>();
            }
            mLeftAdapter.setNewData(datas);
            mRightAdapter.setNewData(datas);
            refreshAdapterRight();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

}
