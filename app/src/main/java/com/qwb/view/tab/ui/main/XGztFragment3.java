package com.qwb.view.tab.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.qwb.event.ApplyYunEvent;
import com.qwb.event.CategroyMessageEvent;
import com.qwb.event.ChangeCompanyEvent;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.view.tab.adapter.ApplyAdapter2;
import com.qwb.view.base.model.ApplyBean;
import com.qwb.view.tab.model.ApplyBean2;
import com.qwb.view.base.model.NewApplyComparator;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 首页-工作台
 */
public class XGztFragment3 extends XFragment{

    public XGztFragment3() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.x_fragment_home_tab;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 初始化EventBus
     */
    private void initEvent() {
        //点击tab‘消息’
        BusProvider.getBus().toFlowable(CategroyMessageEvent.class)
                .subscribe(new Consumer<CategroyMessageEvent>() {
                    @Override
                    public void accept(CategroyMessageEvent event) throws Exception {
                        refreshAdapterMessage();
                    }
                });
        //切换公司
        BusProvider.getBus().toFlowable(ChangeCompanyEvent.class)
                .subscribe(new Consumer<ChangeCompanyEvent>() {
                    @Override
                    public void accept(ChangeCompanyEvent event) throws Exception {
                        //更新应用列表
                        initAdapterData();
                    }
                });
        //编辑应用列表后更新适配器
        BusProvider.getBus().toFlowable(ApplyYunEvent.class)
                .subscribe(new Consumer<ApplyYunEvent>() {
                    @Override
                    public void accept(ApplyYunEvent event) throws Exception {
                        //更新应用列表
                        initAdapterData();
                    }
                });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initEvent();
        initUI();
        initReceiver();
    }

    private void initUI() {
        initAdapter();
        initAdapterData();
    }

    private List<ApplyBean2> myItems = new ArrayList<>();

    private void initAdapterData() {
        try {
            myItems.clear();
            //自定义的
            String childrenStr = SPUtils.getSValues(ConstantUtils.Sp.APP_LIST_CHILDREN);
            String label0 = "快捷菜单";
            List<ApplyBean> applyList0 = new ArrayList<>();
            if (!MyUtils.isEmptyString(childrenStr)) {
                List<ApplyBean> children = JSON.parseArray(childrenStr, ApplyBean.class);
                Collections.sort(children, new NewApplyComparator());//排序
                if (children != null && !children.isEmpty()) {
                    Boolean isChangeNormal = SPUtils.getBoolean(ConstantUtils.Sp.APP_LIST_CHILDREN_NORMAL);//快捷菜单是否默认
                    for (ApplyBean child : children) {
                        //是否改变默认应用了
                        if(isChangeNormal == null || !isChangeNormal){
                            if (ConstantUtils.Apply.DHXD_NEW.equals(child.getApplyCode())) {// 订货下单
                                child.setMeApplySort(1);
                                applyList0.add(child);
                            } else if (ConstantUtils.Apply.KQ_NEW.equals(child.getApplyCode())) {// 考勤
                                child.setMeApplySort(2);
                                applyList0.add(child);
                            } else if (ConstantUtils.Apply.KHGL_NEW.equals(child.getApplyCode())) {// 我的客户--客户管理
                                child.setMeApplySort(3);
                                applyList0.add(child);
                            } else if (ConstantUtils.Apply.BFCX_NEW.equals(child.getApplyCode())) {// 我的拜访--拜访查询
                                child.setMeApplySort(4);
                                applyList0.add(child);
                            }else if (ConstantUtils.Apply.SP_NEW.equals(child.getApplyCode())) {// 审批
                                child.setMeApplySort(6);
                                applyList0.add(child);
                            } else if (ConstantUtils.Apply.SPZQ_NEW.equals(child.getApplyCode())) {//商品展区--商品信息
                                child.setMeApplySort(7);
                                applyList0.add(child);
                            }else if (ConstantUtils.Apply.GZHB_NEW.equals(child.getApplyCode())) {//工作汇报
                                child.setMeApplySort(8);
                                applyList0.add(child);
                            }else if (ConstantUtils.Apply.JSKC_NEW.equals(child.getApplyCode())) {//即使库存
                                child.setMeApplySort(9);
                                applyList0.add(child);
                            }else if (ConstantUtils.Apply.GG_NEW.equals(child.getApplyCode())) {// 企微社区--公告
                                int ggCount = MyDataUtils.getInstance().queryCategroyMessageCount(String.valueOf(ConstantUtils.Messeage.M_GG));
                                child.setMeApplySort(10);
                                child.setCount(ggCount);
                                applyList0.add(child);
                            }else if (ConstantUtils.Apply.TJBB_NEW.equals(child.getApplyCode())) {// 拜访报表--统计报表
                                child.setMeApplySort(11);
                                applyList0.add(child);
                            }else if (ConstantUtils.Apply.LDDK_NEW.equals(child.getApplyCode())) {// 流动打卡
                                child.setMeApplySort(12);
                                applyList0.add(child);
                            }else if (ConstantUtils.Apply.DKCX_NEW.equals(child.getApplyCode())) {// 打卡查询
                                child.setMeApplySort(13);
                                applyList0.add(child);
                            }else if (ConstantUtils.Apply.XSFP_NEW.equals(child.getApplyCode())) {// 销售发票
                                child.setMeApplySort(14);
                                applyList0.add(child);
                            } else if (ConstantUtils.Apply.WDDW_NEW.equals(child.getApplyCode())) {// 我的单位（创建公司）
                                child.setMeApplySort(15);
                                applyList0.add(child);
                            } else if (ConstantUtils.Apply.CONTACT.equals(child.getApplyCode())) {// 联系客服
                                child.setMeApplySort(16);
                                applyList0.add(child);
                            }
                        }else{
                            //快捷菜单（已修改）
                            if (MyStringUtil.eq("1", child.getIsMeApply())) {
                                applyList0.add(child);
                            }
                        }
                    }
                }
            }

            applyList0.add(new ApplyBean(R.mipmap.home_tab_tj, ConstantUtils.Apply.TJ_NEW, "添加", 10000));
            Collections.sort(applyList0, new NewApplyComparator());
            ApplyBean2 bean0 = new ApplyBean2();
            bean0.setApplys(applyList0);
            myItems.add(bean0);


            if (null != mAdapter) {
                mAdapter.setNewData(myItems);
            }

        } catch (Exception e) {
        }
    }

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    ApplyAdapter2 mAdapter;

    private void initAdapter() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new ApplyAdapter2(context);
        mRecyclerView.setAdapter(mAdapter);

    }

    //公共跳转界面方法
    public void jumpActivity(Class c) {
        Router.newIntent(context)
                .to(c)
                .launch();
    }

    /**
     * 更新总消息和公告红点
     */
    private void refreshAdapterMessage() {
        if (null == mAdapter) {
            return;
        }
        List<ApplyBean2> datas = mAdapter.getData();
        if (null != datas && !datas.isEmpty()) {
            for (int i = 0; i < datas.size(); i++) {
                ApplyBean2 data = datas.get(i);

                List<ApplyBean> beans = data.getApplys();
                for (int k = 0; k < beans.size(); k++) {
                    ApplyBean bean = beans.get(k);
                    //消息
                    if ("xx".equals(bean.getApplyCode())) {
                        int sumCount = MyDataUtils.getInstance().queryCategroyMessageCount(String.valueOf(ConstantUtils.Messeage.M_SUM));
                        bean.setCount(sumCount);
                    }
                    //公告 改为：企微社区
                    if (ConstantUtils.Apply.GG_NEW.equals(bean.getApplyCode())) {
                        int ggCount = MyDataUtils.getInstance().queryCategroyMessageCount(String.valueOf(ConstantUtils.Messeage.M_GG));
                        bean.setCount(ggCount);
                    }
                    beans.set(k, bean);
                }
                data.setApplys(beans);
                mAdapter.setData(i, data);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    //TODO--------------------------------------广播相关：start------------------------------------------------------------
    // 广播
    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter(Constans.Action_login);
        intentFilter.addAction(Constans.UnRreadMsg);// res 未读消息 刷新广播
        intentFilter.addAction(Constans.whitchbankuai);// 区别哪个板块对应显示红点
        context.registerReceiver(myReceive, intentFilter);
    }

    private BroadcastReceiver myReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constans.whitchbankuai.equals(intent.getAction())) {
                refreshAdapterMessage();
            } else if (Constans.UnRreadMsg.equals(intent.getAction())) {
                initAdapterData();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myReceive != null) {
            context.unregisterReceiver(myReceive);
        }

    }
    //TODO--------------------------------------广播相关：end------------------------------------------------------------


}
