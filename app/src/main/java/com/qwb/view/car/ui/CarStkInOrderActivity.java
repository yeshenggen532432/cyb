package com.qwb.view.car.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.common.TypeEnum;
import com.qwb.event.StkMoveEvent;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyDividerUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyTableUtil;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyUnitUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.car.adapter.CarStkInRightAdapter;
import com.qwb.view.car.adapter.CarStkOutRightAdapter;
import com.qwb.view.car.model.StkMoveBean;
import com.qwb.view.car.model.StkMoveSubBean;
import com.qwb.view.step.adapter.Step5Left2Adapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.view.stk.StorageBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.view.step.model.XiaJi;
import com.qwb.view.car.parsent.PCarStkInOrder;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.widget.table.TableHorizontalScrollView;
import com.chiyong.t3.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 *  车销回库单
 */
public class CarStkInOrderActivity extends XActivity<PCarStkInOrder> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_car_stk_in_order;
    }

    @Override
    public PCarStkInOrder newP() {
        return new PCarStkInOrder();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        doIntent();
        getP().queryToken(null);
    }

    /**
     * 初始化Intent
     */
    private int billType = 2;//1.车销配货 2.车销回库
    private ArrayList<ShopInfoBean.Data> mDataList;
    private String mOrderType;// 1.添加 2.添加或修改
    private String mOrderId;//订单id
    private void initIntent() {
        Intent intent = getIntent();
        if (null != intent) {
            mDataList = intent.getParcelableArrayListExtra(ConstantUtils.Intent.CHOOSE_WARE_NEW);
            mOrderType = intent.getStringExtra(ConstantUtils.Intent.ORDER_TYPE);
            mOrderId = intent.getStringExtra(ConstantUtils.Intent.ORDER_ID);
        }
    }

    private void doIntent() {
        if(MyStringUtil.eq(mOrderType, TypeEnum.UPDATE.getType())){
            getP().queryData(context, mOrderId);
        }else{
            if(MyCollectionUtil.isNotEmpty(mDataList)){
                //TODO 把currentCount转为小单位数量,价格也变（价格：目前手机端和pc端两边不一致）
                for (ShopInfoBean.Data data:mDataList) {
                    data.setCurrentCount(MyUnitUtil.maxCountToMinCount(data.getHsNum(),data.getCurrentCount()));
                    data.setCurrentPrice(MyUnitUtil.minCountToMaxCount(data.getHsNum(),data.getCurrentPrice()));
                }
                mLeftAdapter.addData(mDataList);
                mRightAdapter.addData(mDataList);
                refreshAdapterRight();
            }
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initTableView();
        initOtherUI();
    }

    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle.setText("车销回库");
        mTvHeadRight.setText("提交");
        mHeadRight.setOnClickListener(new OnNoMoreClickListener() {
            @Override
            protected void OnMoreClick(View view) {
                addData();
            }
        });
    }

    @BindView(R.id.et_bz)
    EditText mEtBz;
    @BindView(R.id.tv_stk_out_name)
    TextView mTvStkOutName;
    @BindView(R.id.tv_stk_in_name)
    TextView mTvStkInName;
    String mStkInId, mStkOutId;
    private void initOtherUI() {
        String defaultStkId = SPUtils.getSValues(ConstantUtils.Sp.CAR_DEFAULT_STORAGE);
        String defaultStkName = SPUtils.getSValues(ConstantUtils.Sp.CAR_DEFAULT_STORAGE_NAME);
        mStkOutId = defaultStkId;
        mTvStkOutName.setText(defaultStkName);
        //入库仓库
        mTvStkInName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(MyCollectionUtil.isEmpty(mStkList)){
                        getP().queryStorage(context);
                    }else{
                        showDialogStorage(mStkList);
                    }
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });
    }

    private RecyclerView mRvLeft;
    private RecyclerView mRvRight;
    private TableHorizontalScrollView mSvWare;
    private Step5Left2Adapter mLeftAdapter;
    private CarStkInRightAdapter mRightAdapter;
    private ShopInfoBean.Data mCurrentItem;
    private int mCurrentPosition;
    public void initTableView() {
        mRvLeft = findViewById(R.id.rv_left);
        mRvRight = findViewById(R.id.rv_right);
        mSvWare = findViewById(R.id.sv_ware);

        mRvLeft.setLayoutManager(new LinearLayoutManager(this));
        mRvLeft.addItemDecoration(MyDividerUtil.getH05CGray(context));
        mLeftAdapter = new Step5Left2Adapter();
        mRvLeft.setAdapter(mLeftAdapter);

        mRvRight.setLayoutManager(new LinearLayoutManager(this));
        mRvRight.addItemDecoration(MyDividerUtil.getH05CGray(context));
        mRightAdapter = new CarStkInRightAdapter();
        mRvRight.setAdapter(mRightAdapter);

        //设置两个列表的同步滚动
        MyTableUtil.getInstance().setSyncScrollListener(mRvLeft, mRvRight, mSvWare);

        mRightAdapter.setOnChildListener(new CarStkInRightAdapter.OnChildListener() {
            @Override
            public void onChildListener(int tag, int position, ShopInfoBean.Data item) {
                try {
                    mCurrentItem = item;
                    mCurrentPosition = position;
                    switch (tag){
                        case CarStkOutRightAdapter.TAG_DEl:
                            showDialogDel();
                            break;
                    }
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });
    }

    /**
     * 刷新表格数据
     */
    private void refreshAdapterRight() {
        try {
            setRepeatMap();//标记商品的个数（重复商品颜色变）
            mLeftAdapter.notifyDataSetChanged();
            mRightAdapter.notifyDataSetChanged();
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

    /**
     * 对话框-选择出库仓库
     */
    private List<StorageBean.Storage> mStkList = new ArrayList<>();
    public void showDialogStorage(List<StorageBean.Storage> list){
        try {
            if (MyCollectionUtil.isEmpty(mStkList) && MyCollectionUtil.isNotEmpty(list)){
                this.mStkList.addAll(list);
            }
            if(MyCollectionUtil.isEmpty(mStkList)){
                ToastUtils.showCustomToast("没有仓库可以选择");
                return;
            }

            final ArrayList<DialogMenuItem> dialogMenuItems = new ArrayList<>();
            for(StorageBean.Storage storage : mStkList) {
                dialogMenuItems.add(new DialogMenuItem(storage.getStkName(), storage.getId()));
            }
            NormalListDialog dialog = new NormalListDialog(context,dialogMenuItems);
            dialog.title("选择仓库").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                    try{
                        String storageNm = dialogMenuItems.get(i).mOperName;
                        mTvStkInName.setText(storageNm);
                        mStkInId = dialogMenuItems.get(i).mResId + "";
                    }catch (Exception e){
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
    private String mJsonStr;
    private void addData() {
        try {
            List<ShopInfoBean.Data> dataList = mRightAdapter.getData();
            if(null != dataList && dataList.isEmpty()){
                ToastUtils.showLongCustomToast("请添加商品+");
                return;
            }
            if(MyStringUtil.isEmpty(mStkInId) || MyStringUtil.isEmpty(mStkOutId)){
                ToastUtils.showLongCustomToast("请选择仓库");
                return;
            }
            getJsonStr();//获取商品列表拼接的json
            String remark = mEtBz.getText().toString().trim();
            String stkOutName = mTvStkOutName.getText().toString().trim();
            String stkInName = mTvStkInName.getText().toString().trim();
            double sumMoney = getTableSumMoney();

            getP().addData(context, mOrderId, mStkOutId, stkOutName, mStkInId, stkInName, remark, String.valueOf(sumMoney),mJsonStr, billType,mQueryToken);
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
                    //TODO 已小单位为准
                    XiaJi xiaJi = new XiaJi();
                    String count = data.getCurrentCount();
                    String price = data.getCurrentPrice();

                    xiaJi.setWareId(data.getWareId() + "");
                    xiaJi.setWareNm(data.getWareNm());
                    xiaJi.setXsTp(data.getCurrentXstp());
                    xiaJi.setWareGg(data.getWareGg());//规格
                    xiaJi.setWareDw(data.getMinUnit());
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
                    xiaJi.setBeUnit(data.getMinUnitCode());// 包装单位代码或计量单位代码（已小单位为准）                    xiaJi.setRemark(data.getCurrentBz());//备注
                    xiaJi.setProductDate(data.getCurrentProductDate());//生产日期
                    list.add(xiaJi);
                }
            }
            mJsonStr = JSON.toJSONString(list);//拼接的json字符串
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    /**
     * 获取table中总金额
     */
    private double getTableSumMoney(){
        double sum = 0.0;
        try {
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
        }catch (Exception e){
            ToastUtils.showError(e);
        }
        return sum;
    }

    /**
     * 下单提交数据成功
     */
    public void submitSuccess(){
        try {
            Intent data = new Intent();
            data.putExtra(ConstantUtils.Intent.SUCCESS, true);
            setResult(0,data);
            //通知车销移库列表刷新
            BusProvider.getBus().post(new StkMoveEvent());
            ActivityManager.getInstance().closeActivity(context);
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    public void doUI(StkMoveBean data){
        try {
            mStkOutId = "" + data.getStkId();
            mStkInId = "" + data.getStkInId();
//            billType = data.getBillType();
            mTvStkInName.setText(data.getStkInName());
            mTvStkOutName.setText(data.getStkName());
            mEtBz.setText(data.getRemarks());

            //判断是否可以修改
            int status = data.getStatus();//-2:暂存 1.审批  2.作废
            int mid = data.getMid();
            if(SPUtils.getID().equals(""+mid) && -2 == status){
                mTvHeadRight.setText("修改");
            }else{
                mTvHeadRight.setText("");
                mHeadRight.setEnabled(false);
            }

            //***********商品信息***************
            List<StkMoveSubBean> list = data.getList();
            if(null == list || list.isEmpty()){
                return;
            }
            List<ShopInfoBean.Data> dataList = new ArrayList<>();
            for (StkMoveSubBean subBean: list) {
                ShopInfoBean.Data bean = new ShopInfoBean.Data();
                bean.setWareId(Integer.valueOf(subBean.getWareId()));
                bean.setWareNm(subBean.getWareNm());
                bean.setCurrentCount(""+subBean.getQty());
                bean.setCurrentPrice(""+subBean.getPrice());
//                bean.setCurrentDw(subBean.getWareDw());
//                bean.setCurrentCode(subBean.getBeUnit());
//                bean.setCurrentXstp("正常销售");
//                bean.setCurrentBz("");
//                bean.setCurrentProductDate("");
                //商品原有信息
                bean.setWareGg(subBean.getWareGg());
                bean.setHsNum(""+subBean.getHsNum());
                bean.setMinUnit(subBean.getMinUnit());
                bean.setMinUnitCode(subBean.getMinUnitCode());
                bean.setMaxUnitCode(subBean.getMaxUnitCode());
                bean.setWareDw(subBean.getWareDw());
                dataList.add(bean);
            }
            mLeftAdapter.addData(dataList);
            mRightAdapter.addData(dataList);
            refreshAdapterRight();
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    /**
     * 避免重复的token
     */
    private String mQueryToken;
    public void doToken(String data){
        mQueryToken = data;
    }








}
