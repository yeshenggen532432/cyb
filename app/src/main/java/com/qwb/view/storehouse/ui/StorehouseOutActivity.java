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
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.common.TypeEnum;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyTableUtil;
import com.qwb.view.storehouse.adapter.StorehouseOutLeftAdapter;
import com.qwb.view.storehouse.adapter.StorehouseOutRightAdapter;
import com.qwb.view.storehouse.model.StorehouseBean;
import com.qwb.view.storehouse.model.StorehouseOutBean;
import com.qwb.view.storehouse.model.StorehouseOutSubBean;
import com.qwb.view.storehouse.parsent.PStorehouseOut;
import com.qwb.widget.MyDateTimePickerDialog;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.stk.StorageBean;
import com.qwb.utils.MyKeyboardUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.widget.table.TableHorizontalScrollView;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 库位管理:出仓单
 */
public class StorehouseOutActivity extends XActivity<PStorehouseOut> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_storehouse_out;
    }

    @Override
    public PStorehouseOut newP() {
        return new PStorehouseOut();
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

    private Integer mBillId;
    private String mType;
    private int mId;
    private void initIntent() {
        Intent intent = getIntent();
        if(intent!=null){
            mBillId = intent.getIntExtra(ConstantUtils.Intent.ID, 0);
            mType = intent.getStringExtra(ConstantUtils.Intent.TYPE);
        }
    }

    private void doIntent(){
        if(MyStringUtil.isNotEmpty(mType) && mBillId > 0){
            if (mType.equals(TypeEnum.DETAIL.getType())){
                getP().queryStorehouseOutByBillId(context, mBillId);
                mId = mBillId;
            }else{
                getP().queryStorehouseByBillId(context, mBillId);
            }
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
        mTvHeadTitle.setText("出仓单");
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

    @BindView(R.id.tv_bill_no)
    TextView mTvBillNo;
    @BindView(R.id.tv_storage)
    TextView mTvStorage;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.et_remark)
    EditText mEtRemark;

    private void initOther() {
        //选择仓库
        mTvStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogStorageTip();
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


    @BindView(R.id.rv_left)
    RecyclerView mRvLeft;
    @BindView(R.id.rv_right)
    RecyclerView mRvRight;
    @BindView(R.id.sv_ware)
    TableHorizontalScrollView mSvWare;
    @BindView(R.id.tv_table_title_left)
    TextView mTvTableTitleLeft;
    private StorehouseOutLeftAdapter mLeftAdapter;
    private StorehouseOutRightAdapter mRightAdapter;

    private StorehouseOutSubBean mCurrentItem;
    private int mCurrentPosition;

    public void initTableView() {
        //left
        mRvLeft.setLayoutManager(new LinearLayoutManager(this));
        mRvLeft.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_b)
                .sizeResId(R.dimen.dp_0_5)
                .showLastDivider()
                .build());
        mLeftAdapter = new StorehouseOutLeftAdapter();
        mRvLeft.setAdapter(mLeftAdapter);
        //right
        mRvRight.setLayoutManager(new LinearLayoutManager(this));
        mRvRight.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_b)
                .sizeResId(R.dimen.dp_0_5)
                .showLastDivider()
                .build());
        mRightAdapter = new StorehouseOutRightAdapter();
        mRvRight.setAdapter(mRightAdapter);
        //右边点击事件：1.切换单位；2.选择库位
        mRightAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    StorehouseOutSubBean item = (StorehouseOutSubBean) adapter.getData().get(position);
                    mCurrentItem = item;
                    mCurrentPosition = position;
                    switch (view.getId()) {
                        case R.id.tv_table_content_right_item1://切换单位
                            showDialogChangeUnit();
                            break;
                        case R.id.tv_table_content_right_item2://选择库位
                            mShowDialogStorehouse = true;
                            getP().queryStorehouseList(context, mStkId);
                            break;
                        case R.id.tv_table_content_right_item5://删除
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
    private String mTimeStr;

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

    public void doUI(StorehouseOutBean bean) {
        try {
            mTimeStr = bean.getInDate();
            if(MyStringUtil.isEmpty(mTimeStr)) {
                mTimeStr = MyTimeUtils.getNowTime();
            }
            mStkId = bean.getStkId();
            mSourceId = bean.getSourceId();
            mSourceNo = bean.getSourceNo();

            mTvTime.setText(mTimeStr);
            mTvBillNo.setText(bean.getSourceNo());
            mTvStorage.setText(bean.getStkName());
            mEtRemark.setText(bean.getRemarks());

            //出仓单：暂存可以修改，其他只能查看
            if(MyStringUtil.isNotEmpty(mType) && mType.equals(TypeEnum.DETAIL.getType())){
                Integer status = bean.getStatus();
                if(status != null && (status == 0 || status == -2)){
                }else{
                    mTvHeadRight.setText("");
                    mHeadRight.setClickable(false);
                }
            }

            //table
            List<StorehouseOutSubBean> subList = bean.getList();
            if (MyCollectionUtil.isNotEmpty(subList)) {
                for (StorehouseOutSubBean sub : subList) {
                    if(MyStringUtil.isNotEmpty(mType) && mType.equals(TypeEnum.UPDATE.getType())){
                        //初始化：移入数量为0；原来的单位代码
                        sub.setQty(new BigDecimal(0));
                    }
                    sub.setOldBeUnit(sub.getBeUnit());
                }
                mLeftAdapter.addData(bean.getList());
                mRightAdapter.addData(bean.getList());
                refreshAdapterRight();
            }
            //请求库位
            getP().queryStorehouseList(null, mStkId);
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

    /**
     * 切换仓库后，相应的库位也要跟着变
     */
    private void showDialogStorageTip(){
        NormalDialog dialog = new NormalDialog(context);
        dialog.content("切换仓库后，表格中库位都是临时库位，请重新选择库位").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                if (MyCollectionUtil.isEmpty(mStorageList)) {
                    getP().queryStorage(context);
                } else {
                    showDialogStorage(mStorageList);
                }
            }
        });
    }

    /**
     * 对话框-选择仓库
     */
    private List<StorageBean.Storage> mStorageList = new ArrayList<>();

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
            }
            NormalListDialog dialog = new NormalListDialog(context, items);
            dialog.title("选择仓库").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                    int mResId = items.get(i).mResId;
                    if(!String.valueOf(mResId).equals(String.valueOf(mStkId))){
                        List<StorehouseOutSubBean> list = mRightAdapter.getData();
                        if(MyCollectionUtil.isNotEmpty(list)){
                            for (StorehouseOutSubBean subBean:list) {
                                subBean.setOutStkId(null);
                                subBean.setOutStkName(null);
                            }
                        }
                        mRightAdapter.setNewData(list);
                        mRightAdapter.notifyDataSetChanged();
                    }
                    mStkId = items.get(i).mResId;
                    mTvStorage.setText(items.get(i).mOperName);
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    /**
     * 对话框-移出库位
     */
    private List<StorehouseBean> mStorehouseList = new ArrayList<>();
    private Integer mTempStkId;
    private String mTempStkName = "临时库位";
    private boolean mShowDialogStorehouse = false;

    public void showDialogStorehouse(List<StorehouseBean> storehouseList) {
        try {
//            if (MyCollectionUtil.isEmpty(mStorehouseList)) {
//                mStorehouseList = storehouseList;
//            }
            if (MyCollectionUtil.isEmpty(storehouseList)) {
                ToastUtils.showCustomToast("没有库位");
                return;
            }
            final ArrayList<DialogMenuItem> items = new ArrayList<>();
            for (StorehouseBean bean : storehouseList) {
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
                        mCurrentItem.setOutStkId(item.mResId);
                        mCurrentItem.setOutStkName(item.mOperName);
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
     * 切换单位
     */
    private void showDialogChangeUnit() {
        try {
            String wareNm = mCurrentItem.getWareNm();
            final String maxUnit = mCurrentItem.getWareDw();
            String minUnit = mCurrentItem.getMinUnit();
            final ArrayList<DialogMenuItem> items = new ArrayList<>();
            if (!MyStringUtil.isEmpty(maxUnit)) {
                items.add(new DialogMenuItem(maxUnit, 0));
            }
            if (!MyStringUtil.isEmpty(minUnit)) {
                items.add(new DialogMenuItem(minUnit, 1));
            }
            NormalListDialog dialog = new NormalListDialog(context, items);
            dialog.title(wareNm).show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                    try {
                        String operName = items.get(i).mOperName;
                        if (operName.equals(maxUnit)) {
                            mCurrentItem.setBeUnit(mCurrentItem.getMaxUnitCode());
                        } else {
                            mCurrentItem.setBeUnit(mCurrentItem.getMinUnitCode());
                        }
                        mRightAdapter.getData().set(mCurrentPosition, mCurrentItem);
                        refreshAdapterRight();
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

    /**
     * 对话框-删除商品
     */
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


    /**
     * 提交数据
     */
    private void addData() {
        String remark = mEtRemark.getText().toString().trim();
        //处理：如果移入库位是空的，赋值为”临时库位“
        List<StorehouseOutSubBean> subList = mRightAdapter.getData();
        if (MyCollectionUtil.isNotEmpty(subList)) {
            for (StorehouseOutSubBean bean : subList) {
                if (bean.getOutStkId() == null) {
                    bean.setOutStkId(mTempStkId);
                    bean.setOutStkName(mTempStkName);
                }
            }
        }
        String json = JSON.toJSONString(mRightAdapter.getData());
        getP().addData(context, mId, mStkId, mTimeStr, mSourceId, mSourceNo, remark, json, mBillId);
    }

}
