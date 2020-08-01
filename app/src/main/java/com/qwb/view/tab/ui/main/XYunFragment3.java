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
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.view.tab.adapter.ApplyAdapter2;
import com.qwb.view.base.model.ApplyBean;
import com.qwb.view.tab.model.ApplyBean2;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 首页：云生活
 */
public class XYunFragment3 extends XFragment  {

    public XYunFragment3() {
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

            //后台返回对的
            String qwbList = SPUtils.getSValues(ConstantUtils.Sp.APP_LIST_NEW);
            if (!MyUtils.isEmptyString(qwbList)) {
                List<ApplyBean> list = JSON.parseArray(qwbList, ApplyBean.class);
                for (ApplyBean bean : list) {
                    List<ApplyBean> applyList1 = new ArrayList<>();
                    List<ApplyBean> children = bean.getChildren();
                    if (children != null && !children.isEmpty()) {
                        for (ApplyBean child : children) {
                            //企业办公
                            if (ConstantUtils.Apply.YBS_NEW.equals(child.getApplyCode())) {// 易办事
                                child.setImgRes(R.mipmap.home_tab_ybs);

                            } else if (ConstantUtils.Apply.SP_NEW.equals(child.getApplyCode())) {// 审批
                                child.setImgRes(R.mipmap.home_tab_sp);

                            } else if (ConstantUtils.Apply.KQ_NEW.equals(child.getApplyCode())) {// 考勤
                                child.setImgRes(R.mipmap.home_tab_kq);

                            } else if (ConstantUtils.Apply.GG_NEW.equals(child.getApplyCode())) {// 企微社区--公告
                                int ggCount = MyDataUtils.getInstance().queryCategroyMessageCount(String.valueOf(ConstantUtils.Messeage.M_GG));
                                child.setImgRes(R.mipmap.home_tab_gg);
                                child.setCount(ggCount);
//
                            } else if (ConstantUtils.Apply.SPZQ_NEW.equals(child.getApplyCode())) {//商品展区--商品信息
                                child.setImgRes(R.mipmap.home_tab_mdgl);

                            } else if (ConstantUtils.Apply.BFDT_NEW.equals(child.getApplyCode())) {// 员工在线--拜访地图
                                child.setImgRes(R.mipmap.home_tab_bfdt);

                            } else if (ConstantUtils.Apply.WDHC_NEW.equals(child.getApplyCode())) {//我的缓存
                                child.setImgRes(R.mipmap.home_tab_qycpk);

                            } else if (ConstantUtils.Apply.TJBB_NEW.equals(child.getApplyCode())) {//统计报表
                                child.setImgRes(R.mipmap.home_tab_tjbb);

                            }
                            //业务外勤管理
                            else if (ConstantUtils.Apply.KHGL_NEW.equals(child.getApplyCode())) {// 我的客户--客户管理
                                child.setImgRes(R.mipmap.home_tab_khgl);

                            } else if (ConstantUtils.Apply.DHXD_NEW.equals(child.getApplyCode())) {// 订货下单
                                child.setImgRes(R.mipmap.home_tab_dhxd);

                            } else if (ConstantUtils.Apply.JHBF_NEW.equals(child.getApplyCode())) {//计划拜访
                                child.setImgRes(R.mipmap.home_tab_bfjh);

                            } else if (ConstantUtils.Apply.GZHB_NEW.equals(child.getApplyCode())) {//工作汇报
                                child.setImgRes(R.mipmap.home_tab_gzhb);

                            } else if (ConstantUtils.Apply.WLPSZX_NEW.equals(child.getApplyCode())) {//物流配送中心
                                child.setImgRes(R.mipmap.home_tab_pszx);

                            } else if (ConstantUtils.Apply.CXGL_NEW.equals(child.getApplyCode())) {//车销管理--车销下单
                                child.setImgRes(R.mipmap.home_tab_pszx);

                            } else if (ConstantUtils.Apply.LDDK_NEW.equals(child.getApplyCode())) {//流动打卡
                                child.setImgRes(R.mipmap.home_tab_qycpk);

                            } else if (ConstantUtils.Apply.DKCX_NEW.equals(child.getApplyCode())) {//打卡查询
                                child.setImgRes(R.mipmap.home_tab_qycpk);

                            } else if (ConstantUtils.Apply.DKDT_NEW.equals(child.getApplyCode())) {// 打卡地图
                                child.setImgRes(R.mipmap.home_tab_bfdt);

                            } else if (ConstantUtils.Apply.BFCX_NEW.equals(child.getApplyCode())) {// 我的拜访--拜访查询
                                child.setImgRes(R.mipmap.home_tab_bfcx);

                            }
                            //库存管理
                            else if (ConstantUtils.Apply.JSKC_NEW.equals(child.getApplyCode())) {//即时库存
                                child.setImgRes(R.mipmap.home_tab_qycpk);

                            } else if (ConstantUtils.Apply.RKTJ_NEW.equals(child.getApplyCode())) {//入库统计
                                child.setImgRes(R.mipmap.home_tab_qycpk);

                            } else if (ConstantUtils.Apply.CKTJ_NEW.equals(child.getApplyCode())) {//出库统计
                                child.setImgRes(R.mipmap.home_tab_qycpk);

                            } else if (ConstantUtils.Apply.KCPD_NEW.equals(child.getApplyCode())) {//库存盘点
                                child.setImgRes(R.mipmap.home_tab_qycpk);

                            } else if (ConstantUtils.Apply.ZDKCYJ_NEW.equals(child.getApplyCode())) {//最低库存预警
                                child.setImgRes(R.mipmap.home_tab_qycpk);

                            }
                            //销售台账
                            else if (ConstantUtils.Apply.KHXSTJ_NEW.equals(child.getApplyCode())) {//客户销售统计
                                child.setImgRes(R.mipmap.home_tab_qycpk);

                            } else if (ConstantUtils.Apply.YWXSTJ_NEW.equals(child.getApplyCode())) {//业务销售统计
                                child.setImgRes(R.mipmap.home_tab_qycpk);

                            } else if (ConstantUtils.Apply.SPXSTJ_NEW.equals(child.getApplyCode())) {//商品销售统计
                                child.setImgRes(R.mipmap.home_tab_qycpk);

                            } else if (ConstantUtils.Apply.KHTRFYB_NEW.equals(child.getApplyCode())) {//客户投入费用表
                                child.setImgRes(R.mipmap.home_tab_qycpk);

                            }
                            //商城管理
                            else if (ConstantUtils.Apply.SCDD_NEW.equals(child.getApplyCode())) {//商城订单
                                child.setImgRes(R.mipmap.home_tab_scgl);

                            } else if (ConstantUtils.Apply.WDSJ_NEW.equals(child.getApplyCode())) {//供货商(我的商家)
                                child.setImgRes(R.mipmap.home_tab_scgl);

                            }
                            //我的空间
                            else if (ConstantUtils.Apply.TXL_NEW.equals(child.getApplyCode())) {// 通讯录
                                child.setImgRes(R.mipmap.home_tab_txl);

                            } else {//默认
                                child.setImgRes(R.mipmap.home_tab_qycpk);
                            }
                            applyList1.add(child);
                        }
                        ApplyBean2 bean1 = new ApplyBean2();
                        bean1.setLabel(bean.getApplyName());
                        bean1.setApplys(applyList1);
                        myItems.add(bean1);
                    }
                }
            }

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
