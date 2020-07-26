package com.qwb.view.step.doui;

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
import com.qwb.view.step.ui.Step5Activity;
import com.qwb.view.step.model.OrderConfigBean;
import com.qwb.widget.MyContentScrollViewDialog;
import com.qwb.widget.MyDateTimePickerDialog;
import com.qwb.event.CarOrderChooseClientEvent;
import com.qwb.event.OrderEvent;
import com.qwb.event.RetreatEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.MyDoubleUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.step.model.QueryXstypeBean;
import com.qwb.db.DStep5Bean;
import com.qwb.view.step.model.QueryBforderBean;
import com.qwb.view.stk.StorageBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.view.step.model.XiaJi;
import com.qwb.utils.MyKeyboardUtil;
import com.qwb.utils.MyNetWorkUtil;
import com.xmsx.cnlife.view.widget.MyVoiceDialog;
import com.xmsx.qiweibao.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.event.BusProvider;

/**
 * 处理：拜访步骤5：下单
 */
public class DoStep5 {

    private Step5Activity context;

    public DoStep5(Step5Activity activity) {
        this.context = activity;
    }

    /**
     * 通知更新UI
     */
    public void doBindEvent(CarOrderChooseClientEvent event){
        try {
            context.cId = event.getCid();
            context.mKhNm = event.getClientNm();
            context.mLinkman = event.getLinkman();
            context.mTel = event.getTel();
            context.mPhone = event.getPhone();
            context.mAddress = event.getAddress();
            context.mTvKhNm.setText(context.mKhNm);
            context.mEtShr.setText(context.mLinkman);
            context.mEtAddress.setText(context.mAddress);
            if (MyStringUtil.isNotEmpty(context.mTel)) {
                context.mEtPhone.setText(context.mTel);
            }else if (MyStringUtil.isNotEmpty(context.mPhone)){
                context.mEtPhone.setText(context.mPhone);
            }else{
                context.mEtPhone.setText("");
            }
            //查询缓存
            if(context.mOrderType == OrderTypeEnum.ORDER_CUSTOMER.getType() || context.mOrderType == OrderTypeEnum.ORDER_DHXD_ADD.getType() ||context.mOrderType == OrderTypeEnum.ORDER_THXD_ADD.getType() ||context.mOrderType == OrderTypeEnum.ORDER_RED_ADD.getType() ){
                context.queryCacheData();
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    /**
     *
     */
    public void initIntent() {
        Intent intent = context.getIntent();
        if (null != intent) {
            context.mRedMark = intent.getIntExtra(ConstantUtils.Intent.ORDER_RED_MARK, 0);
            context.mOrderType = intent.getIntExtra(ConstantUtils.Intent.ORDER_TYPE, 1);
            context.cId = intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
            context.mKhNm = intent.getStringExtra(ConstantUtils.Intent.CLIENT_NAME);
            context.mAddress = intent.getStringExtra(ConstantUtils.Intent.ADDRESS);
            context.mTel = intent.getStringExtra(ConstantUtils.Intent.TEL);//手机，没有手机号时会显示规定电话
            context.mPhone = intent.getStringExtra(ConstantUtils.Intent.MOBILE);//固定电话
            context.mLinkman = intent.getStringExtra(ConstantUtils.Intent.LINKMAN);
            context.mOrderId = intent.getIntExtra(ConstantUtils.Intent.ORDER_ID, -1);
            context.count5 = intent.getStringExtra(ConstantUtils.Intent.STEP);
            context.mPdateStr = intent.getStringExtra(ConstantUtils.Intent.P_DATE);//补拜访时间
            context.mOrderZt = intent.getStringExtra(ConstantUtils.Intent.ORDER_STATE);// 订单状态：未审核可修改
            context.isMe = intent.getIntExtra(ConstantUtils.Intent.IS_ME, 0);//1：我 2：别人
            context.mCompanyId = intent.getStringExtra(ConstantUtils.Intent.COMPANY_ID);
        }
    }

    //根据不同的类型处理不同的显示
    public void doIntent() {
        try {
            String tel = "";
            if (MyStringUtil.isNotEmpty(context.mPhone)) {
                tel = context.mPhone;
            } else {
                if (MyStringUtil.isNotEmpty(context.mTel)) {
                    tel = context.mTel;
                }
            }
            if (MyStringUtil.isNotEmpty(context.mKhNm)) {
                context.mTvKhNm.setText(context.mKhNm);
            }
            context.mEtPhone.setText(tel);
            context.mEtAddress.setText(context.mAddress);
            context.mEtShr.setText(context.mLinkman);

            String title = "订货下单";
            String rightText = "提交";
            // 1：拜访下单(添加或修改) 2:订货下单(添加) 3：订货下单列表（查看或修改）4：退货下单(添加) 5：退货下单列表（查看或修改）7：车销单（添加）8：车销单（修改，详情）9.商城详情 10.历史订单
            if (context.mOrderType == OrderTypeEnum.ORDER_CUSTOMER.getType()) {
                //1.拜访下单
                if ("1".equals(context.count5)) {
                    rightText = "确认\n修改";
                    context.queryBfOrder();
                } else {
                    context.queryCacheData();
                }

            } else if (context.mOrderType == OrderTypeEnum.ORDER_DHXD_ADD.getType()) {
                //2:订货下单(添加)
                if (MyStringUtil.isNotEmpty(context.mKhNm)) {
                    context.queryCacheData();
                }

            } else if (context.mOrderType == OrderTypeEnum.ORDER_DHXD_LIST.getType()) {
                //3：订货下单列表（查看或修改）
                if ("未审核".equals(context.mOrderZt) && 1 == context.isMe) {
                    rightText = "确认\n修改";
                } else {
                    rightText = "";
                }
                context.queryDhOrder();

            } else if (context.mOrderType == OrderTypeEnum.ORDER_THXD_ADD.getType()) {
                //4：退货下单(添加)
                title = "退货下单";
                context.mHeadRight2.setVisibility(View.INVISIBLE);
                context.mHeadRight2.setClickable(false);
                context.mLayoutPszd.setVisibility(View.GONE);//配送指定
                context.mLayoutZdzk.setVisibility(View.GONE);//整单折扣
                context.mLayoutStorage.setVisibility(View.INVISIBLE);//仓库
                context.mTvShrLable.setText("退货人:");
                context.mTvTimeLable.setText("退货时间");
                if (MyStringUtil.isNotEmpty(context.mKhNm)) {
                    context.queryCacheData();
                }
            } else if (context.mOrderType == OrderTypeEnum.ORDER_THXD_LIST.getType()) {
                //退货下单（查看或修改）
                title = "退货下单";
                context.mHeadRight2.setVisibility(View.INVISIBLE);
                context.mHeadRight2.setClickable(false);
                context.mLayoutPszd.setVisibility(View.GONE);//配送指定
                context.mLayoutZdzk.setVisibility(View.GONE);//整单折扣
                context.mLayoutStorage.setVisibility(View.INVISIBLE);//仓库
                context.mTvShrLable.setText("退货人:");
                context.mTvTimeLable.setText("退货时间");
                if ("未审核".equals(context.mOrderZt) && 1 == context.isMe) {
                    rightText = "确认\n修改";
                } else {
                    rightText = "";
                }
                context.queryRetreat();
            } else if (context.mOrderType == OrderTypeEnum.ORDER_RED_ADD.getType()) {
                //红字单(添加)
                title = "红字单";
                context.mHeadRight2.setVisibility(View.INVISIBLE);
                context.mHeadRight2.setClickable(false);
                context.mLayoutPszd.setVisibility(View.GONE);//配送指定
                context.mLayoutZdzk.setVisibility(View.GONE);//整单折扣
                context.mLayoutStorage.setVisibility(View.INVISIBLE);//仓库
                context.mTvShrLable.setText("退货人:");
                context.mTvTimeLable.setText("退货时间");
                if (MyStringUtil.isNotEmpty(context.mKhNm)) {
                    context.queryCacheData();
                }
            } else if (context.mOrderType == OrderTypeEnum.ORDER_RED_LIST.getType()) {
                //红字单（查看或修改）
                title = "红字单";
                context.mHeadRight2.setVisibility(View.INVISIBLE);
                context.mHeadRight2.setClickable(false);
                context.mLayoutPszd.setVisibility(View.GONE);//配送指定
                context.mLayoutZdzk.setVisibility(View.GONE);//整单折扣
                context.mLayoutStorage.setVisibility(View.INVISIBLE);//仓库
                context.mTvShrLable.setText("退货人:");
                context.mTvTimeLable.setText("退货时间");
                if ("未审核".equals(context.mOrderZt) && 1 == context.isMe) {
                    rightText = "确认\n修改";
                } else {
                    rightText = "";
                }
                context.queryDhOrder();
            } else if (context.mOrderType == OrderTypeEnum.ORDER_SC.getType()) {
                //9.商城详情
                title = "商城订单";
                rightText = "";
                context.mHeadRight2.setVisibility(View.INVISIBLE);
                context.mHeadRight2.setClickable(false);
                context.queryScOrder();

            } else if (context.mOrderType == OrderTypeEnum.ORDER_HISTORY.getType()) {
                //10.历史订单
                title = "订单详情";
                rightText = "";
                context.mHeadRight2.setVisibility(View.INVISIBLE);
                context.mHeadRight2.setClickable(false);
                context.queryDhOrder();
            }

            context.mTvHeadTitle.setText(title);
            context.mTvHeadRight.setText(rightText);
            if (MyStringUtil.isEmpty(rightText)) {
                context.mHeadRight.setClickable(false);
            }

            if (Constans.ISDEBUG){
                if (1 == context.mRedMark){
//                    context.mTvHeadTitle.setText("红字单");
                    context.mTvZje_.setVisibility(View.VISIBLE);
                    context.mTvCjje_.setVisibility(View.VISIBLE);
                }else{
                    context.mTvZje_.setVisibility(View.GONE);
                    context.mTvCjje_.setVisibility(View.GONE);
                }
            }

        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 处理选择商品返回数据
     */
    public void doActivityResult(Intent data) {
        try {
            ArrayList<ShopInfoBean.Data> datas = data.getParcelableArrayListExtra(ConstantUtils.Intent.CHOOSE_WARE_NEW);
            if (MyCollectionUtil.isNotEmpty(datas)) {
                context.mLeftAdapter.addData(datas);
                context.mRightAdapter.addData(datas);
                context.refreshAdapterTable();
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
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
        } catch (Exception e) {
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
                context.mSearchWareList.clear();
                context.mSearchWareList.addAll(list);
                context.mAdapterSearWare.notifyDataSetChanged();
                if (showPopup) {
                    context.mPopupSearchWare.showAtLocation(context.mParent, Gravity.CENTER, 0, 0);
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 添加商品到表格中：刷新table
     */
    public void doAddWareRefreshAdapter(ShopInfoBean.Data data) {
        try {
            //TODO 主要有：单位，单位代码，数量，价格，备注
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
            data.setCurrentXstp("正常销售");
            data.setCurrentBz("");
            context.mLeftAdapter.addData(data);
            context.mRightAdapter.addData(data);
            context.refreshAdapterTable();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 获取商品列表拼接的json
     * isThrows（是否抛出异常）：提交数据（是）；缓存（不抛）
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
                if (MyStringUtil.isEmpty(price)) {
                    price = "0";
                }
                xiaJi.setWareDj(price);
                if (MyStringUtil.isEmpty(count)) {
                    count = "0";
                }
                xiaJi.setWareNum(count);
                xiaJi.setWareZj(String.valueOf(Double.valueOf(count) * Double.valueOf(price)));
                if (1 == context.mRedMark){
                    xiaJi.setWareNum("-" + count);
                    xiaJi.setWareZj("-" + (Double.valueOf(count) * Double.valueOf(price)));
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
                xiaJi.setLowestSalePrice(data.getLowestSalePrice());
                list.add(xiaJi);

                //是否小于最低销售价(排除：退货单,红字单)
                String salePriceStr = data.getLowestSalePrice();
                if (!(this.context.mOrderType == OrderTypeEnum.ORDER_THXD_ADD.getType()
                        || this.context.mOrderType == OrderTypeEnum.ORDER_THXD_LIST.getType()
                        || this.context.mOrderType == OrderTypeEnum.ORDER_RED_ADD.getType()
                        || this.context.mOrderType == OrderTypeEnum.ORDER_RED_LIST.getType()
                )){
                    if (MyStringUtil.isNumber(salePriceStr) && MyStringUtil.isNumber(price) && MyStringUtil.eq("正常销售", data.getCurrentXstp())){
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
        }
        context.mJsonStr = JSON.toJSONString(list);//拼接的json字符串
    }

    /**
     * 选择客户：订货下单-添加；退货下单-添加
     */
    public void chooseCustomer() {
        try {
            if (context.mOrderType == OrderTypeEnum.ORDER_DHXD_ADD.getType() || context.mOrderType == OrderTypeEnum.ORDER_THXD_ADD.getType() || context.mOrderType == OrderTypeEnum.ORDER_RED_ADD.getType()) {
                ActivityManager.getInstance().jumpToClientManagerActivity(context, OrderTypeEnum.getByType(context.mOrderType));
//                Router.newIntent(context)
//                        .putInt(ConstantUtils.Intent.TYPE, context.mOrderType)
//                        .to(ClientManagerActivity.class)
//                        .launch();
//                ActivityManager.getInstance().closeActivity(context);//关闭界面
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 显示隐藏：布局（地址，电话， 收货人）
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
    public void chooseStorage() {
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
     * 选择时间
     */
    public void showDialogTime() {
        new MyDateTimePickerDialog(context, "选择时间", context.mTimeStr, new MyDateTimePickerDialog.DateTimeListener() {
            @Override
            public void onSetTime(int year, int month, int day, int hour, int minute, String timeStr) {
                context.mTimeStr = timeStr;
                context.mTvTime.setText(timeStr);
            }

            @Override
            public void onCancel() {
            }
        }).show();
    }

    /**
     * 配送指定
     */
    public void showDialogPszd() {
        try {
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
                    String pzsdNm = dialogMenuItems.get(i).mOperName;
                    context.mTvPszd.setText(pzsdNm);
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 金额换算
     */
    public void moneyConvert() {
        try {
            String percentStr = context.mEtZdzkParent.getText().toString().trim();
            if (MyStringUtil.isNumber(percentStr)) {
                String sumStr = context.mTvZje.getText().toString().trim();
                if (MyStringUtil.isNumber(sumStr)) {// 判断是否为数字
                    double zjeDouble = Double.parseDouble(sumStr);
                    double percent = Double.valueOf(percentStr);
                    double zdzkDouble = percent * zjeDouble / 100;//百分比*总金额
                    // "整单折扣"不能大于"总金额"
                    if (percent > 100) {
                        ToastUtils.showLongCustomToast("折扣金额已超过");
                        cjjeDouble = zjeDouble;
                        context.mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(cjjeDouble)));
                        context.mEtZdzk.setText(String.valueOf(MyDoubleUtils.getDecimal(zdzkDouble)));
                    } else {
                        cjjeDouble = zjeDouble - zdzkDouble;
                        context.mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(cjjeDouble)));
                        context.mEtZdzk.setText(String.valueOf(MyDoubleUtils.getDecimal(zdzkDouble)));
                    }
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
            if (MyStringUtil.isNumber(zjeStr)) {
                double zjeDouble = Double.parseDouble(zjeStr);
                if (MyStringUtil.isNumber(input) && zjeDouble > 0) {
                    double zdzkDouble = Double.valueOf(input);
                    percent = zdzkDouble * 100 / zjeDouble;//先乘100再除以总金额
                    // "整单折扣"不能大于"总金额"
                    if (zdzkDouble > zjeDouble) {
                        ToastUtils.showLongCustomToast("折扣金额已超过,请重新设置");
                        cjjeDouble = zjeDouble;
                    } else {
                        cjjeDouble = zjeDouble - zdzkDouble;
                    }
                } else {
                    cjjeDouble = zjeDouble;
                }
            } else {
                cjjeDouble = zjeDouble;
            }
            context.mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(cjjeDouble)));
            context.mEtZdzkParent.setText(String.valueOf(MyDoubleUtils.getDecimal(percent)));
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 语音搜索对话框
     */
    public void showDialogVoice() {
        MyVoiceDialog dialog = new MyVoiceDialog(context);
        dialog.show();
        dialog.setOnSuccessOnclick(new MyVoiceDialog.OnSuccessListener() {
            @Override
            public void onSuccessOnclick(String result) {
                try {
                    context.mVoiceResult = result;
                    if (MyStringUtil.isNotEmpty(context.mVoiceResult)) {
                        context.queryDataKeyWordGoodsList();
                    }
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }

    /**
     * 选择商品
     */
    public void chooseWare() {
        try {
            if (MyStringUtil.isEmpty(context.cId)) {
                ToastUtils.showShort(context, "先选择客户");
                return;
            }
            ArrayList<ShopInfoBean.Data> dataList = (ArrayList<ShopInfoBean.Data>) context.mRightAdapter.getData();
            ActivityManager.getInstance().jumpToChooseWare3Activity(context, ChooseWareTypeEnum.W_ORDER, context.cId, context.stkId, context.mAutoPrice, context.mEditPrice, dataList);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 选择销售类型
     */
    public void chooseXstp() {
        if (MyCollectionUtil.isEmpty(context.mXstpList)) {
            context.queryXsTp();
        } else {
            showDialogXstp(context.mXstpList);
        }
    }

    //对话框-销售类型
    public void showDialogXstp(List<QueryXstypeBean.QueryXstype> xstpDatas) {
        try {
            if (MyCollectionUtil.isEmpty(xstpDatas)) {
                ToastUtils.showCustomToast("没有销售类型");
                return;
            }
            if (MyCollectionUtil.isNotEmpty(xstpDatas) && MyCollectionUtil.isEmpty(context.mXstpList)) {
                context.mXstpList.addAll(xstpDatas);
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
                        if (!MyStringUtil.eq(xstpNm, currentXstp)) {
                            // 当选择"正常销售"时，价格为系统默认：其他时价格为0
                            if (MyStringUtil.eq("正常销售", xstpNm)) {
                                context.mCurrentItem.setCurrentPrice(context.mCurrentItem.getWareDj());
                            } else {
                                context.mCurrentItem.setCurrentPrice("0");
                            }
                            context.mCurrentItem.setCurrentXstp(xstpNm);
                            context.mRightAdapter.getData().set(context.mCurrentPosition, context.mCurrentItem);
                            context. refreshAdapterTable();
                        }
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
    public void showDialogChangeDw(final ShopInfoBean.Data data, final int position) {
        try {
            String wareNm = data.getWareNm();
            String maxDw = data.getWareDw();
            String minDw = data.getMinUnit();
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
                        String dw = data.getCurrentDw();
                        String maxCode = data.getMaxUnitCode();
                        String minCode = data.getMinUnitCode();
                        if (!MyStringUtil.eq(operName, dw)) {
                            data.setCurrentDw(operName);
                            //单位，单位代码，价格，总价，总金额
                            if (0 == id) {
                                data.setCurrentCode(maxCode);
                                data.setCurrentPrice(data.getCurrentMaxPrice());
                            } else {
                                data.setCurrentCode(minCode);
                                data.setCurrentPrice(data.getCurrentMinPrice());
                            }
                            context.mRightAdapter.getData().set(position, data);
                            context.refreshAdapterTable();
                        }
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
                        context.mRightAdapter.getData().remove(context.mCurrentPosition);
                        context.mLeftAdapter.getData().remove(context.mCurrentPosition);
                        context.refreshAdapterTable();
                    } catch (Exception e) {
                        ToastUtils.showError(e);
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 遍历--所有商品的总价求和
     */
    public double zjeDouble;//总金额
    public double cjjeDouble;//成交金额

    public void setSumMoney() {
        try {
            zjeDouble = getTableSumMoney();
            context.mTvZje.setText(String.valueOf(MyDoubleUtils.getDecimal(zjeDouble)));
            // 设置成交金额 和 百分比
            String zdzk = context.mEtZdzk.getText().toString().trim();
            if (MyStringUtil.isNumber(zdzk) && zjeDouble > 0) {// 条件得改
                double zdzkDouble = Double.valueOf(zdzk);
                double percent = zdzkDouble * 100 / zjeDouble;
                //计算百分比
                context.mEtZdzkParent.setText(String.valueOf(MyDoubleUtils.getDecimal(percent)));
                //计算成交金额
                if (zjeDouble - zdzkDouble > 0) {
                    // 如果:"整单折扣"不能大于"总金额"
                    context.mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(zjeDouble - zdzkDouble)));
                } else {
                    cjjeDouble = zjeDouble;
                    context.mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(cjjeDouble)));
                }
            } else {
                cjjeDouble = zjeDouble;
                context.mTvCjje.setText(String.valueOf(MyDoubleUtils.getDecimal(cjjeDouble)));
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //获取table中总金额
    public double getTableSumMoney() {
        double sum = 0.0;
        try {
            List<ShopInfoBean.Data> dataList = context.mRightAdapter.getData();
            if (MyCollectionUtil.isEmpty(dataList)) {
                return sum;
            }
            for (ShopInfoBean.Data item : dataList) {
                String count = item.getCurrentCount();
                String price = item.getCurrentPrice();
                if (MyStringUtil.isNumber(count) && MyStringUtil.isNumber(price)) {
                    double zj = Double.valueOf(count) * Double.valueOf(price);
                    sum += zj;
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
        return sum;
    }


    /**
     * 默认仓库
     */
    public void doNormalStorage(List<StorageBean.Storage> storageDatas) {
        try {
            if (MyCollectionUtil.isNotEmpty(storageDatas) && MyCollectionUtil.isEmpty(context.mStorageList)) {
                context.mStorageList.addAll(storageDatas);
            }
            if (storageDatas != null && storageDatas.size() > 0) {
                for (StorageBean.Storage storage : storageDatas) {
                    String isSelect = storage.getIsSelect();
                    String isFixed = storage.getIsFixed();
                    if ("1".equals(isSelect) || "1".equals(isFixed)) {
                        context.stkId = "" + storage.getId();
                        context.mTvStorage.setText(storage.getStkName());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //对话框-仓库
    public void showDialogStorage(List<StorageBean.Storage> storageDatas) {
        try {
            if (MyCollectionUtil.isEmpty(storageDatas)) {
                ToastUtils.showCustomToast("没有仓库可以选择");
                return;
            }
            if (MyCollectionUtil.isNotEmpty(storageDatas) && MyCollectionUtil.isEmpty(context.mStorageList)) {
                context.mStorageList.addAll(storageDatas);
            }

            final ArrayList<DialogMenuItem> dialogMenuItems = new ArrayList<>();
            for (StorageBean.Storage storage : context.mStorageList) {
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
                        context.stkId = dialogMenuItems.get(i).mResId + "";
                    } catch (Exception e) {
                        ToastUtils.showError(e);
                    }
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


//    /**
//     * 判断是否小于最低销售价
//     */
//    public void isLessThanSalePrice(int i, ShopInfoBean.Data data, String price) throws Exception {
//        String salePriceStr = data.getLowestSalePrice();
//        if (MyStringUtil.isNumber(salePriceStr) && MyStringUtil.isNumber(price) && MyStringUtil.eq("正常销售", data.getCurrentXstp())){
//            BigDecimal salePrice = new BigDecimal(salePriceStr);
//            BigDecimal priceB = new BigDecimal(price);
//            BigDecimal hsNum = new BigDecimal(1);
//            if (MyStringUtil.isNotEmpty(data.getHsNum())){
//                hsNum = new BigDecimal(data.getHsNum());
//            }
//            //小单位价格--salePrice
//            if (MyStringUtil.eq(data.getMinUnitCode(), data.getCurrentCode())){
//                salePrice = MyMathUtils.divideByScale(salePrice, hsNum, 3);
//                if (priceB.doubleValue() < salePrice.doubleValue()){
//                    String content = "第"+(i+1)+"行，"+data.getWareNm()+"的价格不能小于最低销售价格(小)"+salePrice.doubleValue();
//                    showDialogLessThanSalePrice(content);
//                    throw new Exception(content);
//                }
//            }else{
//                if (priceB.doubleValue() < salePrice.doubleValue()){
//                    String content = "第"+(i+1)+"行，"+data.getWareNm()+"的价格不能小于最低销售价格(大)"+salePrice.doubleValue();
//                    showDialogLessThanSalePrice(content);
//                    throw new Exception(content);
//                }
//            }
//        }
//    }

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
     * 显示界面
     */
    public void doUI(QueryBforderBean data, DStep5Bean dStep5Bean) {
        try {
            context.mDStep5Bean = dStep5Bean;//缓存对象
            context.stkId = "" + data.getStkId();
            context.mOrderId = data.getId();// 订单id
            context.cId = String.valueOf(data.getCid());// 客户id
            context.mOrderZt = data.getOrderZt();// 审核状态
            context.mTimeStr = data.getShTime();
            context.mEtShr.setText(data.getShr());
            context.mEtPhone.setText(data.getTel());
            context.mEtAddress.setText(data.getAddress());
            context.mEtBz.setText(data.getRemo());
            context.mEtZdzk.setText(data.getZdzk());
            context.mTvZje.setText(data.getZje());
            context.mTvCjje.setText(data.getCjje());
            if (MyStringUtil.isNotEmpty(data.getKhNm())) {
                context.mTvKhNm.setText(data.getKhNm());
            }
            if (!MyStringUtil.isEmpty(data.getShTime())) {
                context.mTvTime.setText(data.getShTime());
            }
            if (!MyStringUtil.isEmpty(data.getPszd())) {
                context.mTvPszd.setText(data.getPszd());
            }
            if (!MyStringUtil.isEmpty(data.getStkName())) {
                context.mTvStorage.setText(data.getStkName());
            }
            if (!MyStringUtil.isEmpty(data.getShTime())) {
                context.mTvTime.setText(data.getShTime());
            }
            if ("审核".equals(data.getOrderZt())) {
                context.mHeadRight.setVisibility(View.INVISIBLE);
            }

            //***********商品信息***************
            List<XiaJi> list = data.getList();
            if (null == list || list.isEmpty()) {
                return;
            }
            List<ShopInfoBean.Data> dataList = new ArrayList<>();
            for (XiaJi xiaji : list) {
                ShopInfoBean.Data bean = new ShopInfoBean.Data();
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
                bean.setLowestSalePrice(xiaji.getLowestSalePrice());

                if (1==context.mRedMark){
                    bean.setCurrentCount(MyMathUtils.getABS(xiaji.getWareNum()));
                }
                //商品原始数据
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
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    /**
     * 弹出缓存提示
     */
    public void showDialogCache() {
        NormalDialog dialog = new NormalDialog(context);
        dialog.content("是否数据缓存到本地,待网络正常后，自动缓存数据?").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                try {
                    context.mIsCache = false;
                    saveCacheData();
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });
    }


    /**
     *
     */
    public void doCacheData() {
        if (context.mIsCache) {
            //删除缓存数据(注意目前只针对添加的)
            switch (context.mOrderType) {
                case ConstantUtils.Order.O_BF:
                    if ("0".equals(context.count5)) {
                        saveCacheData();
                    }
                    break;
                case ConstantUtils.Order.O_DH:
                case ConstantUtils.Order.O_TH://退货
                    if (!MyStringUtil.isEmpty(context.mKhNm)) {
                        saveCacheData();
                    }
                    break;
            }
        }
    }

    //保存缓存数据
    public void saveCacheData() {
        try {
            if (1 == context.mAutoSubmit) {
                ToastUtils.showLongCustomToast("保存数据到缓存中，并自动上传缓存数据");
            }
            getJsonStr(false);//获取商品列表拼接的json
            if (!MyStringUtil.isEmpty(context.mJsonStr) && !"[]".equals(context.mJsonStr)) {
                if (context.mDStep5Bean == null) {
                    context.mDStep5Bean = new DStep5Bean();
                }
                context.mShrStr = context.mEtShr.getText().toString().trim();
                context.mPhoneStr = context.mEtPhone.getText().toString().trim();
                context.mAddressStr = context.mEtAddress.getText().toString().trim();
                context.mRemoStr = context.mEtBz.getText().toString().trim();
                context.mZdzkStr = context.mEtZdzk.getText().toString().trim();
                context.mZjeStr = context.mTvZje.getText().toString().trim();
                context.mCjjeStr = context.mTvCjje.getText().toString().trim();
                context.mShTimeStr = context.mTvTime.getText().toString().trim();
                context.mPszdStr = context.mTvPszd.getText().toString().trim();
                context.mDStep5Bean.setAutoType(context.mAutoSubmit);
                context.mDStep5Bean.setUserId(SPUtils.getID());
                context.mDStep5Bean.setCompanyId(SPUtils.getCompanyId());
                context.mDStep5Bean.setType("" + context.mOrderType);
                context.mDStep5Bean.setOrderId("" + context.mOrderId);
                context.mDStep5Bean.setCount(context.count5);
                context.mDStep5Bean.setCid(context.cId);
                context.mDStep5Bean.setKhNm(context.mKhNm);
                context.mDStep5Bean.setShr(context.mShrStr);
                context.mDStep5Bean.setTel(context.mPhoneStr);
                context.mDStep5Bean.setAddress(context.mAddressStr);
                context.mDStep5Bean.setRemo(context.mRemoStr);
                context.mDStep5Bean.setShTime(context.mShTimeStr);
                context.mDStep5Bean.setPszd(context.mPszdStr);
                context.mDStep5Bean.setStkId(context.stkId);
                context.mDStep5Bean.setStkName(context.mTvStorage.getText().toString().trim());
                context.mDStep5Bean.setZje(context.mZjeStr);
                context.mDStep5Bean.setZdzk(context.mZdzkStr);
                context.mDStep5Bean.setCjje(context.mCjjeStr);
                context.mDStep5Bean.setOrderxx(context.mJsonStr);
                context.mDStep5Bean.setTime(MyTimeUtils.getNowTime());
                MyDataUtils.getInstance().saveStep5(context.mDStep5Bean);
            }

            Intent data = new Intent();
            data.putExtra(ConstantUtils.Intent.SUCCESS, true);
            data.putExtra(ConstantUtils.Intent.COUNT, 2);
            context.setResult(0, data);
            ActivityManager.getInstance().closeActivity(context);
        } catch (Exception e) {
            if (!MyStringUtil.eq("小于最低销售价", e.getMessage())){
                ToastUtils.showError(e);
            }
        }
    }


    /**
     * 下单提交数据成功
     */
    public void submitSuccess(int tp) {
        try {
            context.mIsCache = false;

            Intent data = new Intent();
            data.putExtra(ConstantUtils.Intent.SUCCESS, true);
            context.setResult(0, data);

            //通知订货下单列表和退货列表刷新
            if (2 == tp) {
                BusProvider.getBus().post(new RetreatEvent());
            } else {
                BusProvider.getBus().post(new OrderEvent());
            }

            //删除缓存数据(注意目前只针对添加的)
            switch (context.mOrderType) {
                case ConstantUtils.Order.O_BF:
                    //count5:0未上传，1：已上传
                    if ("0".equals(context.count5)) {
                        context.delCacheData();
                    }
                    break;
                case ConstantUtils.Order.O_TH://退货
                case ConstantUtils.Order.O_DH:
                    if (!MyStringUtil.isEmpty(context.mKhNm)) {
                        context.delCacheData();
                    }
                    break;
            }

            //关闭界面
            ToastUtils.showCustomToast("操作成功");
            ActivityManager.getInstance().closeActivity(context);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 手机端下单配置（拜访下单，订货下单）
     * 1.是历史价还是执行价
     * 2.价格是否可以编辑
     */
    public void doOrderConfig(OrderConfigBean configBean) {
        try {
            if (configBean != null) {
                //退货可以修改
                if (!(context.mOrderType == OrderTypeEnum.ORDER_THXD_ADD.getType() || context.mOrderType == OrderTypeEnum.ORDER_THXD_LIST.getType()
                || context.mOrderType == OrderTypeEnum.ORDER_RED_ADD.getType() || context.mOrderType == OrderTypeEnum.ORDER_RED_LIST.getType())) {
                    context.mEditPrice = configBean.getEditPrice();
                }
                context.mAutoPrice = configBean.getAutoPrice();
                context.mRightAdapter.setEditPrice(context.mEditPrice);
                context.refreshAdapterTable();
            }
        }catch (Exception e){
        }
    }


    /**
     * 提交数据
     */
    public boolean addData() {
        try {
            List<ShopInfoBean.Data> dataList = context.mRightAdapter.getData();
            if (MyCollectionUtil.isEmpty(dataList)) {
                ToastUtils.showLongCustomToast("请添加商品+");
                return false;
            }
            getJsonStr(true);//获取商品列表拼接的json
            context.mShrStr = context.mEtShr.getText().toString().trim();
            context.mPhoneStr = context.mEtPhone.getText().toString().trim();
            context.mAddressStr = context.mEtAddress.getText().toString().trim();
            context.mRemoStr = context.mEtBz.getText().toString().trim();
            context.mZdzkStr = context.mEtZdzk.getText().toString().trim();
            context.mZjeStr = context.mTvZje.getText().toString().trim();
            context.mCjjeStr = context.mTvCjje.getText().toString().trim();
            context.mShTimeStr = context.mTvTime.getText().toString().trim();
            context.mPszdStr = context.mTvPszd.getText().toString().trim();
            if (!MyNetWorkUtil.isNetworkConnected()) {
                ToastUtils.showCustomToast("网络不流通，请检查网络是否正常");
                showDialogCache();
                return false;
            }
        } catch (Exception e) {
            if (!MyStringUtil.eq("小于最低销售价", e.getMessage())){
                ToastUtils.showError(e);
            }
        }
        return true;
    }


}
