package com.qwb.view.car.doui;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import com.alibaba.fastjson.JSON;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.common.ChooseWareTypeEnum;
import com.qwb.common.OrderTypeEnum;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyMathUtils;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.car.ui.CarOrderActivity;
import com.qwb.view.print.ui.BluetoothDeviceList;
import com.qwb.view.print.util.Constant;
import com.qwb.view.step.model.OrderConfigBean;
import com.qwb.widget.MyContentScrollViewDialog;
import com.qwb.widget.MyDateTimePickerDialog;
import com.qwb.event.CarOrderChooseClientEvent;
import com.qwb.event.OrderEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDoubleUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.view.print.util.MyPrintUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.step.adapter.Step5SearchWareAdapter;
import com.qwb.view.step.model.QueryXstypeBean;
import com.qwb.view.step.model.QueryBforderBean;
import com.qwb.view.stk.StorageBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.view.step.model.XiaJi;
import com.qwb.utils.MyKeyboardUtil;
import com.qwb.utils.MyNetWorkUtil;
import com.xmsx.cnlife.view.widget.MyVoiceDialog;
import com.xmsx.qiweibao.R;
import com.zyyoona7.lib.EasyPopup;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.droidlover.xdroidmvp.event.BusProvider;

/**
 * 车销下单
 */
public class DoCarOrder {

    private CarOrderActivity context;

    public DoCarOrder(CarOrderActivity activity) {
        this.context = activity;
    }


    public void initIntent() {
        Intent intent = context.getIntent();
        if (null != intent) {
            // 7.车销下单-添加；8.车销下单-修改和列表
            context.mOrderType = intent.getIntExtra(ConstantUtils.Intent.ORDER_TYPE, OrderTypeEnum.ORDER_CAR_ADD.getType());
            context.mOrderId = intent.getIntExtra(ConstantUtils.Intent.ORDER_ID, -1);
            context.isMe = intent.getIntExtra(ConstantUtils.Intent.IS_ME, 2);//1：我 2：别人
        }
    }

    //根据不同的类型处理不同的显示
    public void doIntent() {
        //隐藏：送货时间，配送指定;默认显示：电话，地址，出货仓库
        context.mLayoutShTimePszd.setVisibility(View.GONE);
        context.mLayoutHide.setVisibility(View.VISIBLE);
        context.mIvShow.setImageResource(R.drawable.icon_jian);
        context.isHide = false;

        context.mStkId = SPUtils.getSValues(ConstantUtils.Sp.CAR_DEFAULT_STORAGE);

        if (context.mOrderType == OrderTypeEnum.ORDER_CAR_LIST.getType()) {
            context.queryDhOrder();
        } else {
            String stkName = SPUtils.getSValues(ConstantUtils.Sp.CAR_DEFAULT_STORAGE_NAME);
            if (MyStringUtil.isNotEmpty(stkName)) {
                context.mTvStorage.setText(stkName);
            }
            context.mTvHeadRight.setText("提交");
            context.mTvHeadRight2.setText("提交\n打印");
        }
    }

    /**
     * 通知更新UI
     */
    public void doBindEvent(CarOrderChooseClientEvent event){
        try {
            context.mCustomerId = event.getCid();
            context.mKhNm = event.getClientNm();
            context.mLinkman = event.getLinkman();
            context.mTel = context.mPhone = event.getPhone();
            context.mAddress = event.getAddress();
            context.mTvKhNm.setText(context.mKhNm);
            context.mEtShr.setText(context.mLinkman);
            context.mEtPhone.setText(context.mTel);
            context.mEtAddress.setText(context.mAddress);
//            if (MyStringUtil.isNotEmpty(context.mKhNm)){
//                context.queryCacheData();
//            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    /**
     * 提交并打印
     */
    public void addDataAndPrint() {
        try {
            if (context.mOrderType == OrderTypeEnum.ORDER_CAR_LIST.getType()) {
                MyPrintUtil.getInstance().print(context, context.mOrderNo, context.mKhNm, context.mCurrentData, OrderTypeEnum.ORDER_CAR_ADD.getType());
            } else {
                if (MyPrintUtil.getInstance().isCollect(context)) {
                    addData(2);
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 显示和隐藏：电话，地址，仓库
     */
    public void doShowHide() {
        try {
            if (context.isHide) {
                context.mLayoutHide.setVisibility(View.VISIBLE);
                context.mIvShow.setImageResource(R.drawable.icon_jian);
            } else {
                context.mLayoutHide.setVisibility(View.GONE);
                context.mIvShow.setImageResource(R.drawable.icon_jia);
            }
            context.isHide = !context.isHide;
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 选择仓库
     */
    public void doChooseStorage() {
        try {
            if (MyCollectionUtil.isEmpty(context.mStorageList)) {
                context.queryStorage();
            } else {
                showDialogStorage(context.mStorageList);
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 选择仓库
     */
    public void showDialogStorage(List<StorageBean.Storage> list) {
        if (MyCollectionUtil.isEmpty(list)) {
            ToastUtils.showCustomToast("没有仓库可以选择");
            return;
        }
        if (MyCollectionUtil.isNotEmpty(list) && MyCollectionUtil.isEmpty(context.mStorageList)){
            context.mStorageList.clear();
            context.mStorageList.addAll(list);
        }

        final ArrayList<DialogMenuItem> dialogMenuItems = new ArrayList<>();
        for (StorageBean.Storage storage : list) {
            dialogMenuItems.add(new DialogMenuItem(storage.getStkName(), storage.getId()));
        }
        NormalListDialog dialog = new NormalListDialog(context, dialogMenuItems);
        dialog.title("选择仓库").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                try {
                    String storageNm = dialogMenuItems.get(i).mOperName;
                    context.mTvStorage.setText(storageNm);
                    context.mStkId = dialogMenuItems.get(i).mResId + "";
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }

    /**
     * 选择时间
     */
    public void showDialogTime() {
        try {
        new MyDateTimePickerDialog(context, "选择时间", context.mShTimeStr, new MyDateTimePickerDialog.DateTimeListener() {
            @Override
            public void onSetTime(int year, int month, int day, int hour, int minute, String timeStr) {
                context.mShTimeStr = timeStr;
                context.mTvTime.setText(timeStr);
            }

            @Override
            public void onCancel() {
            }
        }).show();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 选择配送指定
     */
    public void showDialogPszd() {
        if (MyCollectionUtil.isEmpty(context.mPszdList)) {
            context.mPszdList.add("公司直送");
            context.mPszdList.add("转二批配送");
        }

        final ArrayList<DialogMenuItem> dialogMenuItems = new ArrayList<>();
        for (String pszd : context.mPszdList) {
            dialogMenuItems.add(new DialogMenuItem(pszd, 0));
        }
        NormalListDialog dialog = new NormalListDialog(context, dialogMenuItems);
        dialog.title("选择配送指定").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                try {
                    String pzsdNm = dialogMenuItems.get(i).mOperName;
                    context.mTvPszd.setText(pzsdNm);
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }

    /**
     * 金额换算
     */
    public void moneyConvert() {
        try {
            String perentStr = context.mEtZdzkParent.getText().toString().trim();
            if (!MyStringUtil.isEmpty(perentStr)) {
                String sumStr = context.mTvZje.getText().toString().trim();
                if (!MyUtils.isEmptyString(sumStr)) {// 判断是否为null
                    context.zjeDouble = Double.parseDouble(sumStr);
                    if (!MyUtils.isEmptyString(perentStr) && perentStr.length() > 0) {
                        double percent = Double.valueOf(perentStr);
                        double zdzkDouble = percent * context.zjeDouble / 100;//百分比*总金额
                        // "整单折扣"不能大于"总金额"
                        if (percent > 100) {
                            ToastUtils.showLongCustomToast("折扣金额已超过");
                            context.cjjeDouble = context.zjeDouble;
                            context.mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(context.cjjeDouble)));
                            context.mEtZdzk.setText(String.valueOf(MyDoubleUtils.getDecimal(zdzkDouble)));
                        } else {
                            context.cjjeDouble = context.zjeDouble - zdzkDouble;
                            context.mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(context.cjjeDouble)));
                            context.mEtZdzk.setText(String.valueOf(MyDoubleUtils.getDecimal(zdzkDouble)));
                        }
                    } else {
                        context.cjjeDouble = context.zjeDouble;
                        context.mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(context.zjeDouble)));
                        context.mEtZdzk.setText("");
                    }
                } else {
                    context.mTvCjje.setText("");
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 整单折扣变化监听
     */
    public void doZdzkListener(String input) {
        try {
            double percent = 0;
            String zjeStr = context.mTvZje.getText().toString().trim();
            if (!MyUtils.isEmptyString(zjeStr)) {// 判断是否为null
                double zjeDouble = Double.parseDouble(zjeStr);
                if (!MyUtils.isEmptyString(input) && input.length() > 0 && zjeDouble > 0) {
                    double zdzkDouble = Double.valueOf(input);
                    percent = zdzkDouble * 100 / zjeDouble;//先乘100再除以总金额
                    // "整单折扣"不能大于"总金额"
                    if (zdzkDouble > zjeDouble) {
                        ToastUtils.showLongCustomToast("折扣金额已超过");
                        context.cjjeDouble = zjeDouble;
                    } else {
                        context.cjjeDouble = zjeDouble - zdzkDouble;
                    }
                } else {
                    context.cjjeDouble = zjeDouble;
                }
            } else {
                context.cjjeDouble = context.zjeDouble;
            }
            context.mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(context.cjjeDouble)));
            context.mEtZdzkParent.setText(String.valueOf(MyDoubleUtils.getDecimal(percent)));
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 语音
     */
    public void showDialogVoice() {
        MyVoiceDialog dialog = new MyVoiceDialog(context);
        dialog.show();
        dialog.setOnSuccessOnclick(new MyVoiceDialog.OnSuccessListener() {
            @Override
            public void onSuccessOnclick(String result) {
                context.mVoiceResult = result;
                context.queryDataKeyWordGoodsList();
            }
        });
    }

    /**
     * 结果返回
     */
    public void doActivityResult(Intent data, int requestCode, int resultCode){
        try{
            if (requestCode == ConstantUtils.Intent.REQUEST_STEP_5_CHOOSE_GOODS) {
                if (data != null) {
                    ArrayList<ShopInfoBean.Data> datas = data.getParcelableArrayListExtra(ConstantUtils.Intent.CHOOSE_WARE_NEW);
                    if (MyCollectionUtil.isNotEmpty(datas)) {
                        context.mLeftAdapter.addData(datas);
                        context.mRightAdapter.addData(datas);
                        context.refreshAdapterTable();
                    }
                }
            }

            //蓝牙连接回调
            if (requestCode == Constant.BLUETOOTH_REQUEST_CODE && resultCode == context.RESULT_OK) {
                String macAddress = data.getStringExtra(BluetoothDeviceList.EXTRA_DEVICE_ADDRESS);
                MyPrintUtil.getInstance().connectBluetooth(macAddress);
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    /**
     * 选择销售类型
     */
    public void doChooseXstp(){
        if (MyCollectionUtil.isEmpty(context.mXstpList)) {
            context.queryXsTp();
        } else {
            showDialogXstp(context.mXstpList);
        }
    }

    /**
     * 选择销售类型
     */
    public void showDialogXstp(List<QueryXstypeBean.QueryXstype> list) {
        try {
            if (MyCollectionUtil.isEmpty(list)) {
                ToastUtils.showCustomToast("没有销售类型");
                return;
            }
            if (MyCollectionUtil.isEmpty(context.mXstpList)) {
                context.mXstpList.clear();
                context.mXstpList.addAll(list);
            }
            String wareNm = context.mCurrentItem.getWareNm();

            final ArrayList<DialogMenuItem> dialogMenuItems = new ArrayList<>();
            for (QueryXstypeBean.QueryXstype xstp : context.mXstpList) {
                dialogMenuItems.add(new DialogMenuItem(xstp.getXstpNm(), xstp.getId()));
            }
            NormalListDialog dialog = new NormalListDialog(context, dialogMenuItems);
            dialog.title(wareNm).show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                    try {
                        String currentXstp = context.mCurrentItem.getCurrentXstp();
                        String xstpNm = dialogMenuItems.get(i).mOperName;
                        if (!xstpNm.equals(currentXstp)) {
                            // 当选择"正常销售"时，价格为系统默认：其他时价格为0
                            if (ConstantUtils.NORMAL_XSTP.equals(xstpNm)) {
                                context.mCurrentItem.setCurrentPrice(context.mCurrentItem.getWareDj());
                            } else {
                                context.mCurrentItem.setCurrentPrice("0");
                            }
                            context.mCurrentItem.setCurrentXstp(xstpNm);
                            context.mRightAdapter.getData().set(context.mCurrentPosition, context.mCurrentItem);
                            context.refreshAdapterTable();
                        }
                        MyKeyboardUtil.closeKeyboard(context);//强制关闭软键盘
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
     * 切换单位
     */
    public void showDialogChangeDw(final ShopInfoBean.Data item, final int position) {
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
                                if (!MyStringUtil.isEmpty(price)) {
                                    double newPrice = Double.valueOf(price) * Double.valueOf(hsNum);
                                    item.setCurrentPrice("" + newPrice);
                                }
                            } else {
                                item.setCurrentCode(minCode);
                                if (!MyStringUtil.isEmpty(price)) {
                                    double newPrice = Double.valueOf(price) / Double.valueOf(hsNum);
                                    item.setCurrentPrice("" + newPrice);
                                }
                            }
                            context.mRightAdapter.getData().set(position, item);
                            context.refreshAdapterTable();
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

    /**
     * 删除商品
     */
    public void showDialogDel() {
        try {
            NormalDialog dialog = new NormalDialog(context);
            dialog.content("你确定删除''" + context.mCurrentItem.getWareNm() + "''吗？").show();
            dialog.setOnBtnClickL(null, new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    try {
                        context.mLeftAdapter.getData().remove(context.mCurrentPosition);
                        context.mRightAdapter.getData().remove(context.mCurrentPosition);
                        context.refreshAdapterTable();
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
     * 总金额
     */
    public void setSumMoney() {
        context.zjeDouble = 0.0;
        try {
            context.zjeDouble = getTableSumMoney();
            context.mTvZje.setText(String.valueOf(MyDoubleUtils.getDecimal(context.zjeDouble)));
            // 设置成交金额 和 百分比
            String zdzk = context.mEtZdzk.getText().toString().trim();
            if (!MyStringUtil.isEmpty(zdzk) && context.zjeDouble > 0) {// 条件得改
                double zdzkDouble = Double.valueOf(zdzk);
                double percent = zdzkDouble * 100 / context.zjeDouble;
                //计算百分比
                context.mEtZdzkParent.setText(String.valueOf(MyDoubleUtils.getDecimal(percent)));
                //计算成交金额
                if (context.zjeDouble - zdzkDouble > 0) {
                    // 如果:"整单折扣"不能大于"总金额"
                    context.mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(context.zjeDouble - zdzkDouble)));
                } else {
                    context.cjjeDouble = context.zjeDouble;
                    context.mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(context.cjjeDouble)));
                }
            } else {
                context.cjjeDouble = context.zjeDouble;
                context.mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(context.cjjeDouble)));
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 获取table中总金额
     */
    public double getTableSumMoney() {
        double sum = 0.0;
        try {
            if (MyCollectionUtil.isEmpty(context.mRightAdapter.getData())) {
                return sum;
            }
            for (ShopInfoBean.Data item : context.mRightAdapter.getData()) {
                String count = item.getCurrentCount();
                String price = item.getCurrentPrice();
                if (!MyStringUtil.isEmpty(count) && !MyStringUtil.isEmpty(price)) {
                    double zj = Double.valueOf(count) * Double.valueOf(price);
                    sum += zj;
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
        return sum;
    }

    /**
     * 标记商品的个数（重复商品颜色变）--1:选择商品后 2：删除商品 3：获取订单数据
     */
    public void setRepeatMap() {
        try {
            Map<Integer, Integer> repeatMap = new HashMap<>();
            for (ShopInfoBean.Data data : context.mLeftAdapter.getData()) {
                int wareId = data.getWareId();
                if (repeatMap.containsKey(wareId)) {
                    int qty = repeatMap.get(wareId);
                    repeatMap.put(wareId, qty + 1);
                } else {
                    repeatMap.put(wareId, 1);
                }
            }
            context.mLeftAdapter.setRepeatMap(repeatMap);
        }catch (Exception e){
            ToastUtils.showError(e);
        }

    }

    /**
     * 选择商品
     */
    public void doChooseWare(){
        try {
            if (MyStringUtil.isEmpty(context.mCustomerId)) {
                ToastUtils.showShort(context, "先选择客户");
                return;
            }
            if (MyStringUtil.isEmpty(context.mStkId)) {
                ToastUtils.showShort(context, "先选择仓库");
                return;
            }
            List<ShopInfoBean.Data> dataList = context.mRightAdapter.getData();
            ActivityManager.getInstance().jumpToChooseWare3Activity(context, ChooseWareTypeEnum.W_CAR, context.mCustomerId, context.mStkId, context.mAutoPrice, context.mEditPrice, dataList);
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    /**
     * 搜索商品后刷新列表（语音和弹窗中的搜索框）
     */
    public void refreshAdapterSearch(List<ShopInfoBean.Data> list, boolean showPopup) {
        try {
            //一个直接添加商品（语音的）；多个弹出选择
            if (null != list && list.size() == 1 && showPopup) {
                doAddWareRefreshAdapter(list.get(0));
            } else {
                context.mEtSearchGoods.setText(context.mVoiceResult);
                context. mSearchWareList.clear();
                context.mSearchWareList.addAll(list);
                context.mAdapterSearWare.notifyDataSetChanged();
                if (showPopup) {
                    context.mPopupSearchWare.showAtLocation(context.mParent, Gravity.CENTER, 0, 0);
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }

    }

    /**
     * 添加商品到表格中：刷新table
     */
    public void doAddWareRefreshAdapter(ShopInfoBean.Data data) {
        //TODO 主要有：单位，单位代码，数量，价格，备注
        try {
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
            data.setCurrentXstp(ConstantUtils.NORMAL_XSTP);
            data.setCurrentBz("");
            context.mLeftAdapter.getData().add(data);
            context.mRightAdapter.getData().add(data);
            context.refreshAdapterTable();
        }catch (Exception e){
            ToastUtils.showError(e);
        }

    }


    /**
     * 获取商品列表拼接的json
     */
    public void getJsonStr(boolean isThrows) throws Exception {
        // 品项，销售类型，数量，单位，单价，总价，操作
        List<XiaJi> list = new ArrayList<>();
        list.clear();
        List<ShopInfoBean.Data> dataList = context.mRightAdapter.getData();
        String content = "";
        boolean flag = false;
        if (MyCollectionUtil.isNotEmpty(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                XiaJi xiaJi = new XiaJi();
                ShopInfoBean.Data data = dataList.get(i);
                String count = data.getCurrentCount();
                String price = data.getCurrentPrice();

                xiaJi.setWareId(data.getWareId() + "");
                xiaJi.setWareNm(data.getWareNm());
                xiaJi.setXsTp(data.getCurrentXstp());
                xiaJi.setWareGg(data.getWareGg());//规格
                xiaJi.setWareDw(data.getCurrentDw());
                if (MyStringUtil.isEmpty(count)) {
                    count = "0";
                }
                xiaJi.setWareNum(count);
                if (MyStringUtil.isEmpty(price)) {
                    price = "0";
                }
                xiaJi.setWareDj(price);
                if (!MyStringUtil.isEmpty(count) && !MyStringUtil.isEmpty(price)) {
                    xiaJi.setWareZj(String.valueOf(Double.valueOf(count) * Double.valueOf(price)));
                } else {
                    xiaJi.setWareZj("0");
                }
                xiaJi.setBeUnit(data.getCurrentCode());// 包装单位代码或计量单位代码
                xiaJi.setRemark(data.getCurrentBz());//备注
                xiaJi.setLowestSalePrice(data.getLowestSalePrice());
                list.add(xiaJi);

                //是否小于最低销售价
                String salePriceStr = data.getLowestSalePrice();
                if (MyStringUtil.isNumber(salePriceStr) && MyStringUtil.isNumber(price) && MyStringUtil.eq(ConstantUtils.NORMAL_XSTP, data.getCurrentXstp())){
                    BigDecimal salePrice = new BigDecimal(salePriceStr);
                    BigDecimal priceB = new BigDecimal(price);
                    BigDecimal hsNum = new BigDecimal(1);
                    if (MyStringUtil.isNotEmpty(data.getHsNum())){
                        hsNum = new BigDecimal(data.getHsNum());
                    }
                    //小单位价格--salePrice
                    if (MyStringUtil.eq(data.getMinUnitCode(), data.getCurrentCode())){
                        salePrice = MyMathUtils.divideByScale(salePrice, hsNum, 3);
                        if (priceB.doubleValue() < salePrice.doubleValue()){
                            content += "第"+(i+1)+"行，"+data.getWareNm()+"的价格不能小于最低销售价格(小)"+salePrice.doubleValue()+"\n";
                            flag = true;
                        }
                    }else{
                        if (priceB.doubleValue() < salePrice.doubleValue()){
                            content += "第"+(i+1)+"行，"+data.getWareNm()+"的价格不能小于最低销售价格(大)"+salePrice.doubleValue()+"\n";
                            flag = true;
                        }
                    }
                }
            }
        }

        //是否小于最低销售价
        if (flag){
            showDialogLessThanSalePrice(content);
            context.refreshAdapterTable();
            if (isThrows){
                throw new Exception("小于最低销售价");
            }
        }
        context.mJsonStr = JSON.toJSONString(list);//拼接的json字符串
    }

    /**
     * 小于最低销售价提示
     */
    public void showDialogLessThanSalePrice(String content){
        try {
            MyContentScrollViewDialog dialog = new MyContentScrollViewDialog(context, content);
            dialog.show();
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    /**
     * 下单提交数据成功
     */
    public void doSubmitSuccess() {
        try {
            context.mIsCache = false;

            Intent data = new Intent();
            data.putExtra(ConstantUtils.Intent.SUCCESS, true);
            context.setResult(0, data);
            BusProvider.getBus().post(new OrderEvent());
            ActivityManager.getInstance().closeActivity(context);
            ToastUtils.showCustomToast("操作成功");
        }catch (Exception e){
            ToastUtils.showError(e);
        }

    }

    /**
     * 提交失败
     */
    public void doSubmitError() {
        try {
            context.mErrorCount++;
            if (context.mErrorCount > 1) {
//                showDialogCache();
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }

    }

    /**
     * 缓存对话框
     */
    public void showDialogCache() {
        NormalDialog dialog = new NormalDialog(context);
//        dialog.content("是否数据缓存到本地,待网络正常后，自动缓存数据?").show();
        dialog.content("是否数据缓存到本地?").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                context.mIsCache = false;
                saveCacheData();
            }
        });
    }

    /**
     * 保存缓存数据
     */
    public void saveCacheData() {
        try {
//            if (1 == context.mAutoSubmit) {
//                ToastUtils.showLongCustomToast("保存数据到缓存中，并自动上传缓存数据");
//            }
//            getJsonStr(false);//获取商品列表拼接的json
//            DStep5Bean bean = new DStep5Bean();
//            bean.setUserId(SPUtils.getID());
//            bean.setCompanyId(SPUtils.getCompanyId());
//            bean.setAutoType(0);
//            bean.setType("" + context.mOrderType);
//            bean.setOrderId("" + context.mOrderId);
//            bean.setCid(context.mCustomerId);
//            bean.setKhNm(context.mKhNm);
//            bean.setShr(context.mShrStr);
//            bean.setTel(context.mPhoneStr);
//            bean.setAddress(context.mAddressStr);
//            bean.setRemo(context.mRemoStr);
//            bean.setShTime(context.mShTimeStr);
//            bean.setPszd(context.mPszdStr);
//            bean.setStkId(context.mStkId);
//            bean.setZje(context.mZjeStr);
//            bean.setZdzk(context.mZdzkStr);
//            bean.setCjje(context.mCjjeStr);
//            bean.setOrderxx(context.mJsonStr);
//            bean.setTime(MyTimeUtils.getNowTime());
//            MyDataUtils.getInstance().saveStep5(bean);
//
//            Intent data = new Intent();
//            data.putExtra(ConstantUtils.Intent.SUCCESS, true);
//            data.putExtra(ConstantUtils.Intent.COUNT, 2);
//            context. setResult(0, data);
//            ActivityManager.getInstance().closeActivity(context);
        }catch (Exception e){
            if (!MyStringUtil.eq("小于最低销售价", e.getMessage())){
                ToastUtils.showError(e);
            }
        }
    }

    /**
     *
     */
    public void doCacheData() {
        if (context.mIsCache) {
            //删除缓存数据(注意目前只针对添加的)
            if (context.mOrderType == OrderTypeEnum.ORDER_CAR_ADD.getType()){
                saveCacheData();
            }
        }
    }

    /**
     * 手机端下单配置（拜访下单，订货下单）
     * 1.是历史价还是执行价
     * 2.价格是否可以编辑
     */
    public void doOrderConfig(OrderConfigBean configBean){
        if (configBean != null){
            context.mEditPrice = configBean.getEditPrice();
            context.mAutoPrice = configBean.getAutoPrice();
            context.mRightAdapter.setEditPrice(context.mEditPrice);
            context.refreshAdapterTable();
        }
    }

    /**
     * 显示数据
     */
    public void doUI(QueryBforderBean data) {
        context.mCurrentData = data;
        if (MyStringUtil.isNotEmpty(""+data.getStkId())){
            context.mStkId = "" + data.getStkId();
        }
        context.mCustomerId = String.valueOf(data.getCid());// 客户id
        context.mKhNm = data.getKhNm();// 客户id
        context.mTel = data.getTel();// 客户id
        context.mOrderZt = data.getOrderZt();// 审核状态
        context.mAddress = data.getAddress();
        context.mLinkman = data.getShr();
        context.mOrderZt = data.getOrderZt();
        context.mOrderNo = data.getOrderNo();

        context.mTvKhNm.setText(context.mKhNm);
        context.mEtShr.setText(context.mLinkman);
        context.mEtPhone.setText(context.mTel);
        context.mEtAddress.setText(context.mAddress);
        context.mEtBz.setText(data.getRemo());
        context.mEtZdzk.setText(data.getZdzk());
        context.mTvZje.setText(data.getZje());
        context.mTvCjje.setText(data.getCjje());
        if (!MyStringUtil.isEmpty(data.getShTime())) {
            context.mTvTime.setText(data.getShTime());
            context.mShTimeStr = data.getShTime();
        }
        if (!MyStringUtil.isEmpty(data.getPszd())) {
            context.mTvPszd.setText(data.getPszd());
        }
        if (!MyStringUtil.isEmpty(data.getStkName())) {
            context.mTvStorage.setText(data.getStkName());
        }
        if (context.mOrderType == OrderTypeEnum.ORDER_CAR_LIST.getType()){
            //1:我  2：别人 我的可修改，别人不可修改
            if (1 == context.isMe) {
                context. mTvHeadRight2.setText("打印");
                context.mHeadRight2.setClickable(true);
            } else {
                context. mTvHeadRight2.setText("");
                context.mHeadRight2.setClickable(false);
            }
            if (1 == context.isMe && "未审核".equals(context.mOrderZt)) {
                context.mTvHeadRight.setText("确认\n修改");
                context.mHeadRight.setClickable(true);
            } else {
                context.mTvHeadRight.setText("");
                context.mHeadRight.setClickable(false);
            }
        }

        //***********商品信息***************
        List<XiaJi> list = data.getList();
        if (MyCollectionUtil.isEmpty(list)) {
            return;
        }
        List<ShopInfoBean.Data> dataList = new ArrayList<>();
        for (XiaJi xiaji : list) {
            ShopInfoBean.Data bean = new ShopInfoBean.Data();
            bean.setWareId(Integer.valueOf(xiaji.getWareId()));
            bean.setWareNm(xiaji.getWareNm());
            bean.setCurrentXstp(xiaji.getXsTp());
            bean.setCurrentDw(xiaji.getWareDw());
            bean.setCurrentCount(xiaji.getWareNum());
            bean.setCurrentPrice(xiaji.getWareDj());
            bean.setCurrentCode(xiaji.getBeUnit());
            bean.setWareGg(xiaji.getWareGg());
            bean.setCurrentBz(xiaji.getRemark());
            bean.setLowestSalePrice(xiaji.getLowestSalePrice());

            //商品原始数据
            bean.setWareDj(xiaji.getWareDj());
            bean.setHsNum(xiaji.getHsNum());
            bean.setWareDw(xiaji.getMaxUnit());
            bean.setMinUnit(xiaji.getMinUnit());
            bean.setMaxUnitCode(xiaji.getMaxUnitCode());
            bean.setMinUnitCode(xiaji.getMinUnitCode());
            dataList.add(bean);
        }
        context.mLeftAdapter.addData(dataList);
        context.mRightAdapter.addData(dataList);
        context.refreshAdapterTable();
    }


    /**
     * 提交数据
     */
    public void addData(int autoSubmit) {
        try {
            if (!MyNetWorkUtil.isNetworkConnected()) {
                ToastUtils.showCustomToast("网络不流通，请检查网络是否正常");
//                showDialogCache();
                return;
            }
            if (MyCollectionUtil.isEmpty(context.mRightAdapter.getData())) {
                ToastUtils.showLongCustomToast("请添加商品+");
                return;
            }
            getJsonStr(true);
            context.mShrStr = context.mEtShr.getText().toString().trim();
            context.mPhoneStr = context.mEtPhone.getText().toString().trim();
            context.mAddressStr = context.mEtAddress.getText().toString().trim();
            context.mRemoStr = context.mEtBz.getText().toString().trim();
            context.mZdzkStr = context.mEtZdzk.getText().toString().trim();
            context.mZjeStr = context.mTvZje.getText().toString().trim();
            context.mCjjeStr = context.mTvCjje.getText().toString().trim();
            if (MyStringUtil.isEmpty(context.mShTimeStr)){
                context.mShTimeStr = MyTimeUtils.getNowTime();
            }
            context.mPszdStr = "公司直送";
            context.addData(autoSubmit);
        } catch (Exception e) {
            if (!MyStringUtil.eq("小于最低销售价", e.getMessage())){
                ToastUtils.showError(e);
            }
        }
    }


    /**
     * 创建搜索框
     */
    public void createPopupSearchGoods() {
        context.mPopupSearchWare = new EasyPopup(context)
                .setContentView(R.layout.x_popup_step5_search_ware)
                .createPopup();
        context.mEtSearchGoods = context.mPopupSearchWare.getView(R.id.et_search);
        context.mLvSearchWare = context.mPopupSearchWare.getView(R.id.listView_search_goods);
        context.mAdapterSearWare = new Step5SearchWareAdapter(context, context.mSearchWareList);
        context.mLvSearchWare.setAdapter(context.mAdapterSearWare);
        context.mPopupSearchWare.getView(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    context.mVoiceResult = context.mEtSearchGoods.getText().toString().trim();
                    context.queryDataKeyWordGoodsList();
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
        //item
        context. mLvSearchWare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ShopInfoBean.Data data = context.mSearchWareList.get(position);
                    doAddWareRefreshAdapter(data);
                    context.mPopupSearchWare.dismiss();
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }


}
