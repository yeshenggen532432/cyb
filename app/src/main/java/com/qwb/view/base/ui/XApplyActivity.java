package com.qwb.view.base.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qwb.event.ApplyEvent;
import com.qwb.event.ApplyYunEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.view.base.adapter.EditApplyAdapter;
import com.qwb.view.base.model.NewApplyComparator;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.widget.channel.ItemDragHelperCallback;
import com.qwb.view.base.model.ApplyBean;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.functions.Consumer;

/**
 *  编辑应用菜单
 */
public class XApplyActivity extends XActivity {
    private boolean isSaveMenu = false;//是否保存菜单
    private List<ApplyBean> items = new ArrayList<>();
    private List<ApplyBean> otherItems = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_apply;
    }

    @Override
    public Object newP() {
        return null;
    }

    public void initData(Bundle savedInstanceState) {
        initEvent();
        initIntent();
        initUI();
        doIntent();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 初始化Event
     */
    private void initEvent(){
        BusProvider.getBus()
                .toFlowable(ApplyEvent.class)
                .subscribe(new Consumer<ApplyEvent>() {
                    @Override
                    public void accept(ApplyEvent applyEvent) throws Exception {
                        if(applyEvent.getTag() == ConstantUtils.Event.TAG_APPLY){
                            //更新保存应用列表
                            saveChangeData();
                            isSaveMenu = true;
                        }
                    }
                });
    }

    private int mType = 1;//1:编辑应用；2：快捷菜单
    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null){
            mType = intent.getIntExtra(ConstantUtils.Intent.TYPE, 1);
        }
    }

    private void doIntent(){
        if(2 == mType){
            mTvHeadTitle.setText("快捷菜单");
            mTvHeadRight.setText("默认菜单");
        }else{
            mHeadRight.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initData(false);
        initAdapter();
    }

    @BindView(R.id.head)
    View mHead;
    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.tv_head_left)
    TextView mTvHeadLeft;
    @BindView(R.id.iv_head_left)
    ImageView mIvHeadLeft;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;
    @BindView(R.id.iv_head_right)
    ImageView mIvHeadRight;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });//返回
        mTvHeadTitle.setText("编辑应用");
        mHeadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData(true);
                mAdapter.notifyDataSetChanged();
                isSaveMenu = true;
            }
        });
    }

    /**
     * 从缓存数据中读取，设置‘我的应用’，‘其他应用’数据列表
     */
    private void initData(boolean resetNormal) {
        try {
            items.clear();
            otherItems.clear();
            //快捷菜单
            if(2 == mType){
                String childrenStr = SPUtils.getSValues(ConstantUtils.Sp.APP_LIST_CHILDREN);
                if (!MyUtils.isEmptyString(childrenStr)) {
                    List<ApplyBean> children = JSON.parseArray(childrenStr, ApplyBean.class);
                    Collections.sort(children, new NewApplyComparator());//排序
                    for (ApplyBean child:children) {
                        Boolean isNormal = SPUtils.getBoolean(ConstantUtils.Sp.APP_LIST_CHILDREN_NORMAL);//快捷菜单是否默认
                        if((isNormal == null || !isNormal) || resetNormal){
                            //默认快捷菜单
                            if (ConstantUtils.Apply.DHXD_NEW.equals(child.getApplyCode())) {// 订货下单
                                child.setMeApplySort(1);
                                items.add(child);
                            } else if (ConstantUtils.Apply.KQ_NEW.equals(child.getApplyCode())) {// 考勤
                                child.setMeApplySort(2);
                                items.add(child);
                            } else if (ConstantUtils.Apply.KHGL_NEW.equals(child.getApplyCode())) {// 我的客户--客户管理
                                child.setMeApplySort(3);
                                items.add(child);
                            } else if (ConstantUtils.Apply.BFCX_NEW.equals(child.getApplyCode())) {// 我的拜访--拜访查询
                                child.setMeApplySort(4);
                                items.add(child);
                            }
                            else if (ConstantUtils.Apply.SP_NEW.equals(child.getApplyCode())) {// 审批
                                child.setMeApplySort(6);
                                items.add(child);
                            } else if (ConstantUtils.Apply.SPZQ_NEW.equals(child.getApplyCode())) {//商品展区--商品信息
                                child.setMeApplySort(7);
                                items.add(child);
                            }else if (ConstantUtils.Apply.GZHB_NEW.equals(child.getApplyCode())) {//工作汇报
                                child.setMeApplySort(8);
                                items.add(child);
                            }else if (ConstantUtils.Apply.JSKC_NEW.equals(child.getApplyCode())) {//即使库存
                                child.setMeApplySort(9);
                                items.add(child);
                            }else if (ConstantUtils.Apply.GG_NEW.equals(child.getApplyCode())) {//企微社区--公告
                                child.setMeApplySort(10);
                                items.add(child);
                            }else if (ConstantUtils.Apply.TJBB_NEW.equals(child.getApplyCode())) {//拜访报表--统计报表
                                child.setMeApplySort(11);
                                items.add(child);
                            }else if (ConstantUtils.Apply.LDDK_NEW.equals(child.getApplyCode())) {//流动打卡
                                child.setMeApplySort(12);
                                items.add(child);
                            }else if (ConstantUtils.Apply.DKCX_NEW.equals(child.getApplyCode())) {//打卡查询
                                child.setMeApplySort(13);
                                items.add(child);
                            }else if (ConstantUtils.Apply.XSFP_NEW.equals(child.getApplyCode())) {//销售发票
                                child.setMeApplySort(14);
                                items.add(child);
                            }else if (ConstantUtils.Apply.WDDW_NEW.equals(child.getApplyCode())) {//我的单位（创建公司）
                                child.setMeApplySort(15);
                                items.add(child);
                            }else if (ConstantUtils.Apply.CONTACT.equals(child.getApplyCode())) {// 联系客服
                                child.setMeApplySort(16);
                                items.add(child);
                            }else{
                                otherItems.add(child);
                            }
                        }else{
                            if(child.isMeApply()){
                                items.add(child);
                            }else{
                                otherItems.add(child);
                            }
                        }
                    }
                }
                Collections.sort(items, new NewApplyComparator());//排序
                Collections.sort(otherItems, new NewApplyComparator());//排序
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 适配器
     */
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    EditApplyAdapter mAdapter;
    private void initAdapter() {
        GridLayoutManager manager = new GridLayoutManager(context, 4);
        recyclerView.setLayoutManager(manager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        mAdapter = new EditApplyAdapter(context, helper, items, otherItems);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = mAdapter.getItemViewType(position);
                return viewType == EditApplyAdapter.TYPE_MY || viewType == EditApplyAdapter.TYPE_OTHER ? 1 : 4;
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isSaveMenu){
            SPUtils.setBoolean(ConstantUtils.Sp.APP_LIST_CHILDREN_NORMAL, true);
            saveChangeData();
            //判定应用是否修改了，修改通知首页应用更新列表
            BusProvider.getBus().post(new ApplyYunEvent());
        }
    }

    /**
     * 保存修改后的数据
     */
    private void saveChangeData() {
//        applyList.clear();
//        List<ApplyBean> myItems = mAdapter.getmMyChannelItems();
//        if(null != myItems && !myItems.isEmpty()){
//            for(int i = 0;i < myItems.size(); i++){
//                ApplyBean myBean = myItems.get(i);
//                if(2 == mType){
//                    myBean.setMeApply(true);
//                    myBean.setMeApplySort(i);
//                }else{
//                    myBean.setMe(true);
//                    myBean.setSort(i);
//                }
//            }
//            applyList.addAll(myItems);
//        }
//        List<ApplyBean> otherItems = mAdapter.getmOtherChannelItems();
//        if(null != otherItems && !otherItems.isEmpty()){
//            for(int i = 0; i < otherItems.size(); i++){
//                ApplyBean otherBean = otherItems.get(i);
//                if(2 == mType){
//                    otherBean.setMeApply(false);
//                    otherBean.setMeApplySort(i);
//                }else{
//                    otherBean.setMe(false);
//                    otherBean.setSort(i);
//                }
//            }
//            applyList.addAll(otherItems);
//        }
//        SPUtils.setValues(ConstantUtils.Sp.APP_LIST, JSON.toJSONString(applyList));

        //1.把“我的”和“其他”临时合并
        //遍历应用列表与临时合并的对比
        List<ApplyBean> tempList = new ArrayList<>();
        List<ApplyBean> myItems = mAdapter.getmMyChannelItems();
        if(null != myItems && !myItems.isEmpty()){
            for(int i = 0;i < myItems.size(); i++){
                ApplyBean myBean = myItems.get(i);
                if(2 == mType){
                    myBean.setMeApply(true);
                    myBean.setMeApplySort(i);
                }else{
                    myBean.setMe(true);
                    myBean.setSort(i);
                }
            }
            tempList.addAll(myItems);
        }
        List<ApplyBean> otherItems = mAdapter.getmOtherChannelItems();
        if(null != otherItems && !otherItems.isEmpty()){
            for(int i = 0; i < otherItems.size(); i++){
                ApplyBean otherBean = otherItems.get(i);
                if(2 == mType){
                    otherBean.setMeApply(false);
                    otherBean.setMeApplySort(i);
                }else{
                    otherBean.setMe(false);
                    otherBean.setSort(i);
                }
            }
            tempList.addAll(otherItems);
        }
        SPUtils.setValues(ConstantUtils.Sp.APP_LIST_CHILDREN, JSON.toJSONString(tempList));


//        String childrenStr = SPUtils.getSValues(ConstantUtils.Sp.APP_LIST_CHILDREN);
//        if (!MyUtils.isEmptyString(childrenStr)) {
//            List<ApplyBean> children = JSON.parseArray(childrenStr, ApplyBean.class);
//            Collections.sort(children, new ApplyComparator());
//            for (ApplyBean child :children) {
//                for(ApplyBean tempBean : tempList){
//                    if(child.getId() == tempBean.getId()){
//                        if(2 == mType){
//                            child.setMeApply(tempBean.isMeApply());
//                            child.setMeApplySort(tempBean.getMeApplySort());
//                        }else{
//                            child.setMe(tempBean.getMe());
//                            child.setSort(tempBean.getSort());
//                        }
//                       break;
//                    }
//                }
//            }
//            SPUtils.setValues(ConstantUtils.Sp.APP_LIST_CHILDREN, JSON.toJSONString(children));
//        }

    }
}
