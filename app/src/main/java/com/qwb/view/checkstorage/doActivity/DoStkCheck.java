package com.qwb.view.checkstorage.doActivity;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.view.checkstorage.adapter.StkCheckAdapter;
import com.qwb.view.checkstorage.model.StkCheckBean;
import com.qwb.view.checkstorage.model.StkCheckDetailBean;
import com.qwb.view.checkstorage.model.StkCheckWareBean;
import com.qwb.view.checkstorage.ui.XStkCheckActivity;
import com.qwb.view.checkstorage.ui.XStkWareActivity;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DStkCheckWareBean;
import com.qwb.view.stk.StorageBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.router.Router;

/**
 * 处理：盘点开单
 */
public class DoStkCheck {
    private XStkCheckActivity activity;

    public void doInitActivity(XStkCheckActivity activity){
        this.activity = activity;
    }

    /**
     *  初始化头部
     */
    public void doInitHead(){
        this.activity.mTvHeadTitle.setText("盘点库存");
        if(this.activity.mType == 3 || this.activity.mType == 4){
            this.activity.mTvHeadTitle.setText("多人盘点");
        }
        this.activity.mTvHeadRight.setText("保存");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) activity.getResources().getDimension(R.dimen.dp_17), (int) activity.getResources().getDimension(R.dimen.dp_17));
        this.activity.mIvHeadRight2.setLayoutParams(params);
        this.activity.mIvHeadRight2.setImageResource(R.mipmap.ic_scan_gray_33);
        this.activity.mIvHeadRight3.setLayoutParams(params);
        this.activity.mIvHeadRight3.setImageResource(R.mipmap.ic_smq_gray_333);
    }

    /**
     * 初始化适配器
     */
    public void doInitAdapter(Activity context, RecyclerView recyclerView, StkCheckAdapter adapter, View footer){
        if(adapter == null){
            adapter = new StkCheckAdapter(context);
        }
        adapter.addFooterView(footer);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.x_item_line_f6)
                .sizeResId(R.dimen.dp_5)
                .build());
        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    /**
     * 验证是否已选择仓库
     */
    public boolean doVerifyStkId() {
        boolean flag = true;
        if (0 == this.activity.mStkId) {
            flag = false;
            ToastUtils.showCustomToast("请先选择仓库");
        }
        return flag;
    }

    /**
     *  点击“多次扫描（头部右边2）”按钮
     */
    public void doClickHeadRight2(){
        if (!doVerifyStkId()) return;//验证仓库
        this.activity.mCurrentBean = null;
        this.activity.mCurrentPosition = null;
        if (SPUtils.getBoolean(ConstantUtils.Sp.SCAN_MULTIPLE)) {
            ActivityManager.getInstance().jumpXScanActivity(this.activity, true, this.activity.mStkId);
        } else {
            showDialogMultipleScan();
        }
    }

    /**
     *  点击“添加一行”按钮（空商品）
     */
    public void doClickAddRow(){
        if (!doVerifyStkId()) return;//验证仓库
        doCurrentBean(null, null);
        doSelectWare(null);
    }

    /**
     *  点击“未盘点比对（仓库商品）”按钮
     */
    public void doClickStkWare(){
        if(!doVerifyStkId()){
            return;
        }
        ArrayList<Integer> ids = new ArrayList<>();
        List<StkCheckWareBean> data = activity.mAdapter.getData();
        for (StkCheckWareBean wareBean : data) {
            Integer wareId = wareBean.getWareId();
            if (wareId != null && wareId != 0) {
                ids.add(wareId);
            }
        }
        Router.newIntent(activity)
                .putInt(ConstantUtils.Intent.STK_ID, activity.mStkId)
                .putString(ConstantUtils.Intent.NAME, activity.mStkName)
                .putIntegerArrayList(ConstantUtils.Intent.IDS, ids)
                .to(XStkWareActivity.class)
                .launch();
    }

    /**
     * 点击“选择商品”按钮
     */
    public void doClickChooseWare(){
        if (!doVerifyStkId()) return;//验证仓库
        ActivityManager.getInstance().jumpToChooseWareActivity(this.activity, this.activity.mStkId, null);
    }

    /**
     * 点击“删除全部”按钮（清空已选商品）
     */
    public void doClickDelAll() {
        NormalDialog dialog = new NormalDialog(this.activity);
        dialog.content("确定要删除删除全部吗？").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                try {
                    List<StkCheckWareBean> dataList = new ArrayList<>();
                    dataList.add(new StkCheckWareBean());
                    activity.mAdapter.setNewData(dataList);
                    activity.mAdapter.getWareIdMap().clear();
                    refreshAdapter();
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }

    //处理选择单个商品：1）扫描的结果 2）搜索的结果
    public void doSelectWare(ShopInfoBean.Data ware) {
        try {
            setTipPosition(-1);//恢复默认提示
            StkCheckWareBean bean = new StkCheckWareBean();
            if (null != ware) {
                wareToStkWare(ware, bean);
            }
            //判断商品是否已经添加过(单人盘点：商品不能重复；多人盘点：商品可以重复)
            Map<String, Object> tempMap = isAddDataList(ware);
            if ((boolean) tempMap.get("tempAdd") && (this.activity.mType == 1 || this.activity.mType == 2)) {
                //已添加
                showDialogRepeatWare((String) tempMap.get("tempName"), false);
                setTipPosition((int) tempMap.get("tempPosition"));
            } else {
                //未添加
                if (null != ware) {
                    doWareIdMap(ware.getWareId(), true);//记录商品个数（重复时列表会有颜色区分）
                }
                if (null == this.activity.mCurrentBean) {
                    this.activity.mAdapter.addData(bean);
                    refreshAdapter();
                    this.activity.mRecyclerView.scrollToPosition(this.activity.mAdapter.getData().size() - 1);
                } else {
                    doWareIdMap(this.activity.mCurrentBean.getWareId(), false);//记录商品个数（重复时列表会有颜色区分）
                    bean.setProduceDate(this.activity.mCurrentBean.getProduceDate());
                    bean.setQty(this.activity.mCurrentBean.getQty());
                    bean.setMinQty(this.activity.mCurrentBean.getMinQty());
                    this.activity.mAdapter.setData(this.activity.mCurrentPosition, bean);
                    refreshAdapter();
                    this.activity.mCurrentBean = bean;
                }
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }


    /**
     * 处理返回的数据是多个：multipleScan-true多次扫描;false单次扫描
     */
    public void doSelectWareList(List<ShopInfoBean.Data> list, boolean multipleScan) {
        if (MyCollectionUtil.isEmpty(list)) {
            showDialogNoData();
            return;
        }
        setTipPosition(-1);//恢复默认提示
        if (multipleScan) {
            //扫描多个条码
            String tempWareNmStr = "";
            boolean tempAdd = false;
            for (ShopInfoBean.Data ware : list) {
                StkCheckWareBean bean = new StkCheckWareBean();
                wareToStkWare(ware, bean);

                //判断商品是否已经添加过(单人盘点：商品不能重复；多人盘点：商品可以重复)
                Map<String, Object> tempMap = isAddDataList(ware);
                if ((boolean) tempMap.get("tempAdd") && (this.activity.mType == 1 || this.activity.mType == 2)) {
                    if (MyStringUtil.isEmpty(tempWareNmStr)) {
                        tempWareNmStr += tempMap.get("tempName");
                    } else {
                        tempWareNmStr += "," + tempMap.get("tempName");
                    }
                    tempAdd = true;
                } else {
                    this.activity.mAdapter.addData(bean);
                    doWareIdMap(ware.getWareId(), true);
                }
            }
            if (tempAdd) {
                showDialogRepeatWare(tempWareNmStr, true);
            }
            refreshAdapter();
            this.activity.mRecyclerView.scrollToPosition( this.activity.mAdapter.getData().size() - 1);
        } else {
            //扫描单个条码：可能有多个商品
            if (list.size() == 1) {
                //单个数据
                ShopInfoBean.Data ware = list.get(0);
                doSelectWare(ware);
            } else {
                //多个数据:弹出‘选择商品’对话框
                showDialogSelectWare(list);
            }
        }
    }

    /**
     * 修改时：查询盘点单
     */
    public void doStkCheckDetail(StkCheckDetailBean bean) {
        if (null == bean) {
            return;
        }
        try {
            StkCheckBean stkCheckBean = bean.getCheck();
            this.activity.mStkId = stkCheckBean.getStkId();
            this.activity.mTvStorage.setText(stkCheckBean.getStkName());
            List<StkCheckWareBean> checkWareBeanList = bean.getList();
            this.activity.mAdapter.setNewData(checkWareBeanList);

            //状态:-2暂存
            String status = stkCheckBean.getStatus();
            if ("-2".equals(status)) {
                this.activity.mTvHeadRight.setText("修改");
            } else {
                //已审批
                this.activity.mTvHeadRight.setVisibility(View.GONE);
                this.activity.mIvHeadRight2.setVisibility(View.GONE);
                this.activity.mIvHeadRight3.setVisibility(View.GONE);
                this.activity.mHeadRight.setEnabled(false);
                this.activity.mHeadRight2.setEnabled(false);
                this.activity.mHeadRight3.setEnabled(false);
                this.activity.mViewStorage.setEnabled(false);
                this.activity.mTvStorage.setEnabled(false);
                this.activity.mAdapter.setAddState(false);
                this.activity.mAdapter.removeFooterView(this.activity.mFooterView);
            }
            refreshAdapter();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 缓存数据
     */
    public void doCacheDate(){
        if (this.activity.isSaveCache) {
            List<StkCheckWareBean> dataList = this.activity.mAdapter.getData();
            if (MyCollectionUtil.isNotEmpty(dataList)) {
                List<DStkCheckWareBean> saveList = new ArrayList<>();
                for (StkCheckWareBean data : dataList) {
                    DStkCheckWareBean save = new DStkCheckWareBean();
                    save.setUserId(SPUtils.getID());
                    save.setCompanyId(SPUtils.getCompanyId());
                    save.setStkId(this.activity.mStkId);
                    save.setStkName(this.activity.mStkName);
                    save.setWareId(data.getWareId());
                    save.setWareNm(data.getWareNm());
                    save.setQty(data.getQty());
                    save.setMinQty(data.getMinQty());
                    save.setHsNum(data.getHsNum());
                    save.setDisQty(data.getDisQty());
                    save.setStkQty(data.getStkQty());
                    save.setProduceDate(data.getProduceDate());
                    save.setSunitFront(data.getSunitFront());
                    save.setUnitName(data.getUnitName());
                    save.setMinUnit(data.getMinUnit());
                    save.setType(this.activity.mType);
                    saveList.add(save);
                }
                MyDataUtils.getInstance().saveStkWare(saveList,this.activity.mType);
            }
        } else {
            MyDataUtils.getInstance().delStkWare(this.activity.mType);
        }
    }

    /**
     * 获取缓存数据
     */
    public void doGetCacheData(){
        List<StkCheckWareBean> dataList = new ArrayList<>();
        //缓存
        List<DStkCheckWareBean> saveList = MyDataUtils.getInstance().queryStkWare(this.activity.mType);
        if (saveList != null && saveList.size() > 0) {
            for (DStkCheckWareBean save : saveList) {
                this.activity.mStkId = save.getStkId();
                this.activity.mStkName = save.getStkName();
                StkCheckWareBean wareBean = new StkCheckWareBean();
                wareBean.setWareId(save.getWareId());
                wareBean.setWareNm(save.getWareNm());
                wareBean.setQty(save.getQty());
                wareBean.setMinQty(save.getMinQty());
                wareBean.setHsNum(save.getHsNum());
                wareBean.setDisQty(save.getDisQty());
                wareBean.setStkQty(save.getStkQty());
                wareBean.setProduceDate(save.getProduceDate());
                wareBean.setSunitFront(save.getSunitFront());
                wareBean.setUnitName(save.getUnitName());
                wareBean.setMinUnit(save.getMinUnit());
                dataList.add(wareBean);
            }
        } else {
            //初始化适配器数据：默认一个没有商品的
            dataList.add(new StkCheckWareBean());
        }
        this.activity.mTvStorage.setText(this.activity.mStkName);
        this.activity.mAdapter.setNewData(dataList);
        refreshAdapter();
    }


    //TODO 公共部分************************************************************************************************************************************
    public void doCurrentBean(Integer position, StkCheckWareBean bean){
        this.activity.mCurrentPosition = position;
        this.activity.mCurrentBean = bean;
    }
    /**
     * 刷新适配器：判断第一条数据是否有商品（没有去掉）
     */
    public void refreshAdapter() {
        try{
            List<StkCheckWareBean> dataList = this.activity.mAdapter.getData();
            if (dataList != null && dataList.size() >= 2) {
                StkCheckWareBean stkCheckWareBean = dataList.get(0);
                if(stkCheckWareBean.getWareId() == null || "0".equals(stkCheckWareBean.getWareId().toString())){
                    this.activity.mAdapter.remove(0);
                }
            }
            this.activity.mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }
    /**
     * 多次扫描提醒
     */
    private void showDialogMultipleScan() {
        NormalDialog dialog = new NormalDialog(this.activity);
        dialog.content("可以多次扫描，会过滤掉重复的，按右上角‘确定’按钮来确认结果;按中间的‘预览扫描结果’可以预览")
                .show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                SPUtils.setBoolean(ConstantUtils.Sp.SCAN_MULTIPLE, true);
                ActivityManager.getInstance().jumpXScanActivity(activity, true, activity.mStkId);
            }
        });
    }

    /**
     * 没有数据
     */
    private void showDialogNoData() {
        NormalDialog dialog = new NormalDialog(this.activity);
        dialog.content("抱歉！没有匹配数据哦。可以去后台查看是否设置该条码的商品").btnNum(1).btnText("确定").show();
    }

    /**
     * 单个条码多种商品-选择商品
     */
    public void showDialogSelectWare(final List<ShopInfoBean.Data> list) {
        try {
            ArrayList<DialogMenuItem> items = new ArrayList<>();
            for (ShopInfoBean.Data ware : list) {
                DialogMenuItem item = new DialogMenuItem(ware.getWareNm(), ware.getWareId());
                items.add(item);
            }
            NormalListDialog dialog = new NormalListDialog(this.activity, items);
            dialog.title("单个条码多种商品").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    doSelectWare(list.get(position));
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 选择仓库
     */
    public void showDialogStorage(List<StorageBean.Storage> storageList) {
        try {
            if(MyCollectionUtil.isEmpty(this.activity.storageList)){
                this.activity.storageList = storageList;
            }
            if (MyCollectionUtil.isEmpty(storageList)) {
                ToastUtils.showCustomToast("没有仓库");
                return;
            }
            final ArrayList<DialogMenuItem> items = new ArrayList<>();
            for (StorageBean.Storage storage : storageList) {
                DialogMenuItem item = new DialogMenuItem(storage.getStkName(), storage.getId());
                items.add(item);
            }
            NormalListDialog dialog = new NormalListDialog(this.activity, items);
            dialog.title("选择仓库").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DialogMenuItem item = items.get(position);
                    if (activity.mStkId != item.mResId) {
                        //如果选择其他仓库，清空数据
                        List<StkCheckWareBean> dataList = new ArrayList<>();
                        dataList.add(new StkCheckWareBean());
                        activity.mAdapter.setNewData(dataList);
                        refreshAdapter();
                    }
                    activity.mStkId = item.mResId;
                    activity.mStkName = item.mOperName;
                    activity.mTvStorage.setText(item.mOperName);
                }
            });
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 提交数据前：验证数据的合法性
     */
    public boolean doVerifySubmitData() {
        try {
            if(!doVerifyStkId()){
                return false;
            }
            //设置差量
            List<StkCheckWareBean> list = this.activity.mAdapter.getData();
            if (null == list || list.isEmpty()) {
                ToastUtils.showLongCustomToast("请选择商品");
                return false;
            }
            for (int i = 0; i < list.size(); i++) {
                StkCheckWareBean bean = list.get(i);
                Integer wareId = bean.getWareId();
                if (null == wareId || 0 == wareId) {
                    setTipPosition(i);
                    ToastUtils.showLongCustomToast("第" + (i + 1) + "条：" + "没有设置商品");
                    return false;
                }
                String wareNm = bean.getWareNm();
                Double qty = bean.getQty();
                Double minQty = bean.getMinQty();
                boolean temp = false;//大小数量是否有填
                if (MyStringUtil.isEmpty("" + qty) && MyStringUtil.isEmpty("" + minQty)) {
                    temp = true;
                }
                if (temp) {
                    ToastUtils.showLongCustomToast("第" + (i + 1) + "条" + wareNm + "没有大小数量都没有设置");
                    setTipPosition(i);
                    return false;
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
        return true;
    }

    //设置提示标志：重复商品，没有选择商品，没有设置大小单位数量
    public void setTipPosition(int tipPosition) {
        if (null != this.activity.mAdapter) {
            this.activity.mAdapter.setTipPosition(tipPosition);
            this.activity.mAdapter.notifyDataSetChanged();
            this.activity.mRecyclerView.scrollToPosition(tipPosition);
        }
    }

    //对话框：长按删除(单行)
    public void showDialogDel() {
        NormalDialog dialog = new NormalDialog(this.activity);
        dialog.content("确定要删除吗？").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                try {
                    doWareIdMap(activity.mCurrentBean.getWareId(), false);
                    activity.mAdapter.setTipPosition(-1);
                    activity.mAdapter.remove(activity.mCurrentPosition);
                    refreshAdapter();
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
    }


    //记录重复商品的个数：添加或移除
    private void doWareIdMap(Integer wareId, boolean isAdd) {
        try {
            if (null == wareId || 0 == wareId || null == this.activity.mAdapter) {
                return;
            }
            //添加商品个数
            Map<Integer, Integer> wareIdMap = this.activity.mAdapter.getWareIdMap();
            Integer num = wareIdMap.get(wareId);
            if (isAdd) {
                if (null == num || 0 == num) {
                    this.activity.mAdapter.getWareIdMap().put(wareId, 1);
                } else {
                    this.activity.mAdapter.getWareIdMap().put(wareId, num + 1);
                }
            } else {
                if (null == num || 0 == num) {
                    this.activity.mAdapter.getWareIdMap().put(wareId, 0);
                } else {
                    this.activity.mAdapter.getWareIdMap().put(wareId, num - 1);
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    //判断商品是否已经添加过(单人盘点：商品不能重复；多人盘点：商品可以重复)
    public Map<String, Object> isAddDataList(ShopInfoBean.Data ware) {
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("tempAdd", false);
        tempMap.put("tempPosition", 0);
        tempMap.put("tempName", "");
        //查询列表是否已经存在：存在提示并定位该位置
        List<StkCheckWareBean> dataList = this.activity.mAdapter.getData();
        if (null != dataList && !dataList.isEmpty() && null != ware) {
            for (int i = 0; i < dataList.size(); i++) {
                StkCheckWareBean tempBean = dataList.get(i);
                if (String.valueOf(ware.getWareId()).equals(String.valueOf(tempBean.getWareId()))) {
                    tempMap.put("tempAdd", true);
                    tempMap.put("tempPosition", i);
                    tempMap.put("tempName", tempBean.getWareNm());
                    break;
                }
            }
        }
        return tempMap;
    }

    //商品重复提示;multiple-true:多次扫描的；false-单次扫描
    private void showDialogRepeatWare(String wareNm, boolean multiple) {
        NormalDialog dialog = new NormalDialog(this.activity);
        String content = wareNm + "，该商品已添加过了。";
        if (!multiple) {
            content += "默认该商品会移动到可见区域第一位";
        }
        dialog.content(content).btnNum(1).btnText("确定").show();
    }

    //商品信息转为库存商品信息
    public void wareToStkWare(ShopInfoBean.Data ware, StkCheckWareBean bean){
        bean.setSunitFront(ware.getSunitFront());
        bean.setWareId(ware.getWareId());
        bean.setWareNm(ware.getWareNm());
        bean.setHsNum(ware.getHsNum());
        bean.setMinUnit(ware.getMinUnit());
        bean.setStkQty(ware.getStkQty());
        bean.setProduceDate(ware.getProductDate());
        bean.setUnitName(ware.getWareDw());
        bean.setMinUnit(ware.getMinUnit());
        bean.setMinStkQty(ware.getMinStkQty());
    }

}

