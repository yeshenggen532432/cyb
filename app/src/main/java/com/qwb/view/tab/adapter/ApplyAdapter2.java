package com.qwb.view.tab.adapter;


import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.flow.ui.FlowCameraVoiceActivity;
import com.qwb.view.flow.ui.FlowQueryActivity;
import com.qwb.view.foot.ui.FootEditActivity;
import com.qwb.view.foot.ui.FootQueryActivity;
import com.qwb.view.order.ui.OrderListActivity;
import com.qwb.view.shop.ui.ShopActivity;
import com.qwb.view.storehouse.ui.StorehouseActivity;
import com.qwb.view.storehouse.ui.StorehouseArrangeListActivity;
import com.qwb.view.storehouse.ui.StorehouseInListActivity;
import com.qwb.view.storehouse.ui.StorehouseOutListActivity;
import com.qwb.view.storehouse.ui.StorehouseWareListActivity;
import com.qwb.view.web.ui.WebX5Activity;
import com.qwb.view.work.ui.WorkActivity;
import com.qwb.event.CategroyMessageEvent;
import com.qwb.view.base.adapter.SubApplyAdapter;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.Constans;
import com.qwb.view.company.ui.CompanyInfoActivity;
import com.qwb.view.company.ui.MyCompanyActivity;
import com.qwb.view.purchase.ui.PurchaseOrderListActivity;
import com.qwb.view.audit.ui.AuditActivity;
import com.qwb.view.mine.ui.FeedbackActivity;
import com.qwb.view.plan.ui.PlanActivity;
import com.qwb.view.txl.ui.TongXunLuActivity;
import com.qwb.view.car.ui.CarActivity;
import com.qwb.view.customer.ui.ClientManagerActivity;
import com.qwb.view.delivery.ui.DeliveryActivity;
import com.qwb.view.log.ui.LogActivity;
import com.qwb.view.map.ui.VisitMapActivity;
import com.qwb.view.shop.ui.ShopOrderListActivity;
import com.qwb.view.table.ui.TableListActivity;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.ApplyBean;
import com.qwb.view.tab.model.ApplyBean2;
import com.qwb.view.base.ui.XLoginActivity;
import com.qwb.view.checkstorage.ui.XStkCheckListActivity;
import com.qwb.view.gonggao.ui.GongGaoActivity;
import com.qwb.view.ware.ui.WareManagerActivity;
import com.qwb.view.cache.ui.CacheActivity;
import com.qwb.utils.MyStringUtil;
import com.xmsx.cnlife.view.widget.MyRecyclerView;
import com.xmsx.qiweibao.R;

import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 文 件 名: 首页tab
 * 修改时间：
 * 修改备注：
 */
public class ApplyAdapter2 extends BaseQuickAdapter<ApplyBean2, BaseViewHolder> {

    private Activity mActivity;

    public ApplyAdapter2(Activity activity) {
        super(R.layout.x_adapter_home_tab2);
        this.mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, ApplyBean2 item) {
        helper.setIsRecyclable(false);//不复用
        //点击事件
//        helper.addOnClickListener(R.id.tv_default);

        TextView tvLabel = helper.getView(R.id.item_tv_label);
        MyRecyclerView recyclerView = helper.getView(R.id.item_recyclerView);

        tvLabel.setText(item.getLabel());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 4));
        SubApplyAdapter adapter = new SubApplyAdapter(mActivity);
        adapter.setNewData(item.getApplys());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    if (!SPUtils.getBoolean(ConstantUtils.Sp.IS_LOGIN)) {
                        ActivityManager.getInstance().jumpActivity(mActivity, XLoginActivity.class);
                        return;
                    }

                    ConstantUtils.IS_APPLY_NEW = true;//点击应用模式是否是新的(目前是获取新旧应用dataTp)

                    ApplyBean item = (ApplyBean) adapter.getData().get(position);
                    switch (item.getApplyCode()) {
                        case ConstantUtils.Apply.TXL_NEW://通讯录
                            ActivityManager.getInstance().jumpActivity(mActivity, TongXunLuActivity.class);
                            break;
                        case ConstantUtils.Apply.GG_NEW://公告
                            ActivityManager.getInstance().jumpActivity(mActivity, GongGaoActivity.class);
                            delBadge("" + ConstantUtils.Messeage.M_GG);
                            break;
                        case ConstantUtils.Apply.SP_NEW://审批
                            ActivityManager.getInstance().jumpActivity(mActivity, AuditActivity.class);
                            break;
                        case ConstantUtils.Apply.KQ_NEW://考勤
                            ActivityManager.getInstance().jumpActivity(mActivity, WorkActivity.class);
                            break;
                        case ConstantUtils.Apply.SPZQ_NEW://商品展区--商品信息
                            ActivityManager.getInstance().jumpActivity(mActivity, WareManagerActivity.class);
                            break;
                        case ConstantUtils.Apply.BFDT_NEW://员工在线--拜访地图
                            ConstantUtils.BFHF_TYPE = "";
                            ActivityManager.getInstance().jumpActivity(mActivity, VisitMapActivity.class);
                            break;
                        case ConstantUtils.Apply.WDHC_NEW://我的缓存
                            ActivityManager.getInstance().jumpActivity(mActivity, CacheActivity.class);
                            break;
                        case ConstantUtils.Apply.KHGL_NEW:// 我的客户--客户管理
                            ActivityManager.getInstance().jumpActivity(mActivity, ClientManagerActivity.class);
                            break;
                        case ConstantUtils.Apply.BFCX_NEW://我的拜访--拜访查询
                            ActivityManager.getInstance().jumpToCallPageActivity(mActivity, null, null, null, null, null, true, "0");
                            break;
                        case ConstantUtils.Apply.DHXD_NEW://订货下单
                            ActivityManager.getInstance().jumpActivity(mActivity, OrderListActivity.class);
                            break;
                        case ConstantUtils.Apply.JHBF_NEW://计划拜访
                            ActivityManager.getInstance().jumpActivity(mActivity, PlanActivity.class);
                            break;
                        case ConstantUtils.Apply.GZHB_NEW://工作汇报
                            ActivityManager.getInstance().jumpActivity(mActivity, LogActivity.class);
                            break;
                        case ConstantUtils.Apply.WLPSZX_NEW://物流配送中心
                            ActivityManager.getInstance().jumpActivity(mActivity, DeliveryActivity.class);
                            break;
                        case ConstantUtils.Apply.CXGL_NEW://车销管理--车销下单
                            ActivityManager.getInstance().jumpActivity(mActivity, CarActivity.class);
                            break;
                        case ConstantUtils.Apply.LDDK_NEW://流动打卡
                            ActivityManager.getInstance().jumpActivity(mActivity, FlowCameraVoiceActivity.class);
                            break;
                        case ConstantUtils.Apply.DKCX_NEW://打卡查询
                            ActivityManager.getInstance().jumpActivity(mActivity, FlowQueryActivity.class);
                            break;
                        case ConstantUtils.Apply.DKDT_NEW://打卡地图
                            ConstantUtils.BFHF_TYPE = "2";
                            ActivityManager.getInstance().jumpActivity(mActivity, VisitMapActivity.class);
                            break;
                        case ConstantUtils.Apply.KCPD_NEW://库存盘点
                            ActivityManager.getInstance().jumpActivity(mActivity, XStkCheckListActivity.class);
                            break;
                        case ConstantUtils.Apply.SCDD_NEW://商城订单
                            ActivityManager.getInstance().jumpActivity(mActivity, ShopOrderListActivity.class);
                            break;
                        case ConstantUtils.Apply.WDSJ_NEW://供货商(我的商家)
                            ActivityManager.getInstance().jumpActivity(mActivity, ShopActivity.class);
                            break;
                        case ConstantUtils.Apply.TJBB_NEW://统计报表
                            ActivityManager.getInstance().jumpActivity(mActivity, TableListActivity.class);
                            break;
                        case ConstantUtils.Apply.WDDW_NEW://我的单位
                            ActivityManager.getInstance().jumpActivity(mActivity, MyCompanyActivity.class);
                            break;
                        case ConstantUtils.Apply.QYXXSZ_NEW://企业信息
                            ActivityManager.getInstance().jumpActivity(mActivity, CompanyInfoActivity.class);
                            break;
                        case ConstantUtils.Apply.YJFK_NEW://意见反馈
                            ActivityManager.getInstance().jumpActivity(mActivity, FeedbackActivity.class);
                            break;
                        case ConstantUtils.Apply.CGFP_NEW_PHONE://采购发票（原生的）
                            ActivityManager.getInstance().jumpActivity(mActivity, PurchaseOrderListActivity.class);
                            break;
                        case ConstantUtils.Apply.KWGL_NEW://库位管理
                            ActivityManager.getInstance().jumpActivity(mActivity, StorehouseActivity.class);
                            break;
                        case ConstantUtils.Apply.KWGL_KWZL_NEW://库位整理
                            ActivityManager.getInstance().jumpActivity(mActivity, StorehouseArrangeListActivity.class);
                            break;
                        case ConstantUtils.Apply.KWGL_RCD_NEW://入仓单
                            ActivityManager.getInstance().jumpActivity(mActivity, StorehouseInListActivity.class);
                            break;
                        case ConstantUtils.Apply.KWGL_CCD_NEW://出仓单
                            ActivityManager.getInstance().jumpActivity(mActivity, StorehouseOutListActivity.class);
                            break;
                        case ConstantUtils.Apply.KWGL_WARE_NEW://库位商品
                            ActivityManager.getInstance().jumpActivity(mActivity, StorehouseWareListActivity.class);
                            break;
                        case ConstantUtils.Apply.WDZJ_NEW://我的足迹
                            ActivityManager.getInstance().jumpActivity(mActivity, FootEditActivity.class);
                            break;
                        case ConstantUtils.Apply.LYHC_NEW://旅游画册
                            ActivityManager.getInstance().jumpActivity(mActivity, FootQueryActivity.class);
                            break;
                        case ConstantUtils.Apply.TJ_NEW://编辑应用
//                            ActivityManager.getInstance().jumpToApplyActivity(mActivity);
                            ActivityManager.getInstance().jumpActivity(mActivity, ClientManagerActivity.class);
                            break;
                        default:
                            String applyUrl = item.getApplyUrl();
                            if (MyStringUtil.isNotEmpty(applyUrl)) {
                                String url;
                                if (applyUrl.startsWith("http://erp.7weib.com") || applyUrl.startsWith("https://erp.7weib.com")) {
                                    //鹭百川协议
//                                  String url = "http://erp.7weib.com:8099/rfjxc/mobile/login.jsp?from=qwb&userId=195";
                                    url = applyUrl + SPUtils.getID();
                                } else if (applyUrl.startsWith("http") && applyUrl.contains("#")) {
                                    //客服
                                    String replaceStr = "?token=" + SPUtils.getTK() + "&type=qwb";
                                    StringBuffer sb = new StringBuffer(item.getApplyUrl());
                                    sb.insert(sb.indexOf("#"), replaceStr);
                                    url = sb.toString();
                                } else {
                                    String domain = SPUtils.getSValues(ConstantUtils.Sp.LOGIN_BASE_URL);
                                    if (MyStringUtil.isNotEmpty(domain)) {
                                        url = domain + "/h5?token=" + SPUtils.getTK() + "#" + item.getApplyUrl();
                                    } else {
                                        url = Constans.H5_BASE_URL + "token=" + SPUtils.getTK() + "#" + item.getApplyUrl();
                                    }
                                }
                                Router.newIntent(mActivity)
                                        .putString(ConstantUtils.Intent.URL, url)
                                        .to(WebX5Activity.class)
                                        .launch();

                            } else {
                                ToastUtils.showCustomToast("该模块还在开发中.....");
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //删除红点
    private void delBadge(String bankuai) {
        boolean isUpdateSumCount = false;
        //bankuai:(1.系统通知；2.审批;3.易办事；4.拜访查询-评论；6.沟通；10.日志-工作汇报；11.商城；12.公告）
        if (String.valueOf(ConstantUtils.Messeage.M_GG).equals(bankuai)) {
            //修改板块的数量同时也修改总数量（如点击首页的公告）
            isUpdateSumCount = true;
        }
        int count = MyDataUtils.getInstance().updateCategroyMessageCount(bankuai, isUpdateSumCount);
        if (count > 0) {
            BusProvider.getBus().post(new CategroyMessageEvent());
        }
    }
}
