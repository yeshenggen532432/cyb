package com.qwb.view.tab.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.db.DMessageBean;
import com.qwb.event.ApplyYunEvent;
import com.qwb.event.CategroyMessageEvent;
import com.qwb.event.ChangeCompanyEvent;
import com.qwb.event.CreateCompanyEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.Constans;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.MyGlideUtil;
import com.qwb.utils.MyRecyclerViewUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.view.base.model.ApplyBean;
import com.qwb.view.base.model.NewApplyComparator;
import com.qwb.view.company.model.CompanysBean;
import com.qwb.view.tab.adapter.ApplyAdapter2;
import com.qwb.view.tab.adapter.CategroyAdapter;
import com.qwb.view.tab.model.ApplyBean2;
import com.qwb.view.tab.model.BannerBean;
import com.qwb.view.tab.parsent.PXMain;
import com.chiyong.t3.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import io.reactivex.functions.Consumer;

/**
 * tab:首页
 */
public class XMainFragment extends XFragment<PXMain> {

    @Override
    public int getLayoutId() {
        return R.layout.x_fragment_main;
    }

    @Override
    public PXMain newP() {
        return new PXMain();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initEvent();
        initUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        getP().queryBanner(null);
    }

    private void initEvent(){
        BusProvider.getBus().toFlowable(CategroyMessageEvent.class)
                .subscribe(new Consumer<CategroyMessageEvent>() {
                    @Override
                    public void accept(CategroyMessageEvent event) throws Exception {
                        initAdapterDataMessage();
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
        //创建公司
        BusProvider.getBus().toFlowable(CreateCompanyEvent.class)
                .subscribe(new Consumer<CreateCompanyEvent>() {
                    @Override
                    public void accept(CreateCompanyEvent event) throws Exception {
                        if (event.getTag() == ConstantUtils.Event.TAG_CREATE_COMPANY) {
                            //重新登录，重新获取应用列表
                            getP().queryDataLogin(context, SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE), SPUtils.getSValues(ConstantUtils.Sp.PASSWORD));
                        }
                    }
                });
        //切换公司
        BusProvider.getBus().toFlowable(ChangeCompanyEvent.class)
                .subscribe(new Consumer<ChangeCompanyEvent>() {
                    @Override
                    public void accept(ChangeCompanyEvent event) throws Exception {
                        //备注：改变公司名称；应用列表
                        doCompany();
                        initAdapterData();
                    }
                });
    }

    public void initUI() {
        initHead();
        initBanner();
        initAdapter();
        initAdapterData();
        initAdapterMessage();
        initAdapterDataMessage();
        initReceiver();
    }

    @BindView(R.id.tv_head_left)
    TextView mTvHeadLeft;
    private void initHead() {
        mTvHeadLeft.setText("首页");
        doCompany();
        mTvHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companys = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_S);
                if (!MyStringUtil.isEmpty(companys)) {
                    List<CompanysBean> companyList = JSON.parseArray(companys, CompanysBean.class);
                    if (companyList != null && companyList.size() > 1) {
                        //多公司才弹出
                        showDialogChangeCompany();
                    }
                }
            }
        });
    }

    @BindView(R.id.banner)
    BGABanner mBanner;
    private void initBanner() {
        mBanner.setAdapter(new BGABanner.Adapter<ImageView, BannerBean>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, BannerBean model, int position) {
                MyGlideUtil.getInstance().displayImageSquere(Constans.ROOT + model.getImageUrl(), itemView, false);
            }
        });
    }

    public void doBanner(List<BannerBean> dataList){
        mBanner.setData(dataList,null);
        mBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
            }
        });
        int count = mBanner.getItemCount();
        if (count == 1){
            mBanner.setAutoPlayAble(false);
        }else {
            mBanner.setAutoPlayAble(true);
        }
    }

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    ApplyAdapter2 mAdapter;
    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new ApplyAdapter2(context);
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<ApplyBean2> myItems = new ArrayList<>();
    private void initAdapterData() {
        try {
            myItems.clear();
            //自定义的
            String childrenStr = SPUtils.getSValues(ConstantUtils.Sp.APP_LIST_CHILDREN);
            List<ApplyBean> applyList0 = new ArrayList<>();
            if (!MyUtils.isEmptyString(childrenStr)) {
                List<ApplyBean> children = JSON.parseArray(childrenStr, ApplyBean.class);
                Collections.sort(children, new NewApplyComparator());//排序
                if (MyCollectionUtil.isNotEmpty(children)) {
                    for (ApplyBean child : children) {
                        //快捷菜单（已修改）
                        if (MyStringUtil.eq("1", child.getIsMeApply())) {
                            applyList0.add(child);
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

    /**
     * 适配器
     */
    @BindView(R.id.recyclerView_message)
    RecyclerView mRecyclerViewMessage;
    CategroyAdapter mMessageAdapter;
    private void initAdapterMessage() {
        mMessageAdapter = new CategroyAdapter(context);
        mMessageAdapter.setNewData(categoryList);
        MyRecyclerViewUtil.init(context,mRecyclerViewMessage, mMessageAdapter);
        mMessageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    DMessageBean item = (DMessageBean)adapter.getData().get(position);
                    int bankuai = Integer.valueOf(item.getBankuai());
                    switch (Integer.valueOf(item.getBankuai())){
                        case ConstantUtils.Messeage.M_XTTZ://系统通知
                            ActivityManager.getInstance().jumpToMessageActivity(context,ConstantUtils.Messeage.M_XTTZ,"系统通知");
                            break;
                        case ConstantUtils.Messeage.M_SP:// 审批
                            ActivityManager.getInstance().jumpToMessageActivity(context,ConstantUtils.Messeage.M_SP,"审批");
                            break;
                        case ConstantUtils.Messeage.M_PL://点评
                            ActivityManager.getInstance().jumpToMessageActivity(context,ConstantUtils.Messeage.M_PL,"点评");
                            break;
                        case ConstantUtils.Messeage.M_GT://沟通
                            ActivityManager.getInstance().jumpToMessageActivity(context,ConstantUtils.Messeage.M_GT,"沟通");
                            break;
                        case ConstantUtils.Messeage.M_GZHB://工作汇报
                            ActivityManager.getInstance().jumpToMessageActivity(context,ConstantUtils.Messeage.M_GZHB,"工作汇报");
                            break;
                        case ConstantUtils.Messeage.M_SC://商城订单
                            ActivityManager.getInstance().jumpToMessageActivity(context,ConstantUtils.Messeage.M_SC,"商城订单");
                            break;
                        case ConstantUtils.Messeage.M_GG://公告
                            ActivityManager.getInstance().jumpToMessageActivity(context,ConstantUtils.Messeage.M_GG,"公告");
                            break;
                    }

                    delBadge(bankuai);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * mark "bangkuai"+板块对应的数字 1 ：系统通知 2：审批 3：易推事板 4： 点评 5：真心话 6：沟通;10.工作汇报；11.商城订单
     */
    List<DMessageBean> categoryList =new ArrayList<>();
    private void initAdapterDataMessage() {
        categoryList.clear();
        setCategoryList(String.valueOf(ConstantUtils.Messeage.M_XTTZ),"系统通知",R.mipmap.msg_xttz);
        setCategoryList(String.valueOf(ConstantUtils.Messeage.M_GG),"公告",R.mipmap.msg_gg);
        setCategoryList(String.valueOf(ConstantUtils.Messeage.M_SP),"审批",R.mipmap.msg_sp);
        setCategoryList(String.valueOf(ConstantUtils.Messeage.M_PL),"点评",R.mipmap.msg_dp);
        setCategoryList(String.valueOf(ConstantUtils.Messeage.M_GZHB),"工作汇报",R.mipmap.msg_gzhb);
//        setCategoryList(String.valueOf(ConstantUtils.Messeage.M_SC),"商城订单",R.mipmap.msg_gt);
        mMessageAdapter.setNewData(categoryList);
    }

    private void setCategoryList(String bankuai,String title,int imgResId){
        List<DMessageBean> xttzList = MyDataUtils.getInstance().queryMessageByBankuai(bankuai);
        int count1 = MyDataUtils.getInstance().queryCategroyMessageCount(bankuai);
        DMessageBean item = new DMessageBean();
        if(null != xttzList && !xttzList.isEmpty()){
            item = xttzList.get(0);
            item.setCount(count1);
        }else {
            item.setMsg("暂无消息");
            item.setAddTime("");
            item.setCount(0);
        }
        item.setBankuai(Integer.valueOf(bankuai));
        item.setTitle(title);
        item.setImgResId(imgResId);
        categoryList.add(item);
    }

    //删除红点
    private void delBadge(int bankuai){
        int count = MyDataUtils.getInstance().updateCategroyMessageCount(String.valueOf(bankuai),false);
        if(count > 0){
            BusProvider.getBus().post(new CategroyMessageEvent());
            //公告要通知首页公告红点
            BusProvider.getBus().post(new CategroyMessageEvent());
        }
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
                receviceRefreshAdapter();
            }else if (Constans.UnRreadMsg.equals(intent.getAction())) {
                receviceRefreshAdapter();
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
    /**
     * 再当前页面，收到信息；刷新列表
     */
    public void receviceRefreshAdapter(){
        initAdapterDataMessage();
    }

    private void doCompany() {
        String companys = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_S);
        String companyId = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID);
        mTvHeadLeft.setText("直购猫");
        if (!TextUtils.isEmpty(companys) && !TextUtils.isEmpty(companyId)) {
            List<CompanysBean> companyList = JSON.parseArray(companys, CompanysBean.class);
            if (companyList != null && companyList.size() > 0) {
                baseItems.clear();
                for (CompanysBean bean : companyList) {
                    DialogMenuItem item = new DialogMenuItem(bean.getCompanyName(), bean.getCompanyId());
                    baseItems.add(item);
                    if (companyId.equals(String.valueOf(bean.getCompanyId()))) {
                        String companyName = bean.getCompanyName();
                        SPUtils.setValues(ConstantUtils.Sp.COMPANY_NAME, companyName);
                        mTvHeadLeft.setText(companyName);
                    }
                }
            }
        }
    }

    //dialog:切换公司
    private ArrayList<DialogMenuItem> baseItems = new ArrayList<>();
    private void showDialogChangeCompany() {
        NormalListDialog dialog = new NormalListDialog(context, baseItems);
        dialog.title("切换公司")
                .show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                String loginBaseUrl = SPUtils.getSValues(ConstantUtils.Sp.LOGIN_BASE_URL);
                if (MyStringUtil.isNotEmpty(loginBaseUrl)) {
                    getP().queryJwt(context, loginBaseUrl, String.valueOf(baseItems.get(position).mResId));
                } else {
                    getP().queryDataChangeCompany(context, String.valueOf(baseItems.get(position).mResId));
                }
            }
        });
    }



}
