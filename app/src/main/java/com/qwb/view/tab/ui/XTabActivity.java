package com.qwb.view.tab.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.pgyersdk.update.PgyUpdateManager;
import com.xdandroid.hellodaemon.IntentWrapper;
import com.qwb.event.CategroyMessageEvent;
import com.qwb.event.ChangeCompanyEvent;
import com.qwb.event.NormalShopEvent;
import com.qwb.service.XMessageService;
import com.xmsx.cnlife.widget.DownloadDialog;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.view.longconnection.ScreenManager;
import com.qwb.view.longconnection.ScreenReceiverUtil;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.model.TabEntity;
import com.qwb.view.tab.parsent.PXTab;
import com.qwb.service.XStepService;
import com.qwb.utils.MyServiceUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyTraceServiceUtil;
import com.chiyong.t3.R;
import java.util.ArrayList;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.functions.Consumer;

/**
 * Tab页面
 */
public class XTabActivity extends XActivity<PXTab>{

//    "消息"
    private String[] mTitles = {"首页", "工作台", "商城", "我的"};
//    /*未选择时的icon*/
//    private int[] mIconUnselectIds = {
//            R.mipmap.tab_icon_main_g, R.mipmap.tab_icon_shop_g,
//            R.mipmap.tab_icon_message_g, R.mipmap.tab_icon_mine_g};
//    /*选择时的icon*/
//    private int[] mIconSelectIds = {
//            R.mipmap.tab_icon_main_b, R.mipmap.tab_icon_shop_b,
//            R.mipmap.tab_icon_message_b, R.mipmap.tab_icon_mine_b};
    /*未选择时的icon*/
    private int[] mIconUnselectIds = {  R.mipmap.tab_icon_main_gray, R.mipmap.tab_icon_more_gray,R.mipmap.tab_icon_shop_gray, R.mipmap.tab_icon_mine_gray};
    /*选择时的icon*/
    private int[] mIconSelectIds = {
            R.mipmap.tab_icon_main_green, R.mipmap.tab_icon_more_green, R.mipmap.tab_icon_shop_green,R.mipmap.tab_icon_mine_green};

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_tab;
    }

    @Override
    public PXTab newP() {
        return new PXTab();
    }

    public void initData(Bundle savedInstanceState) {
        initEvent();
        initUI();
        //未读接口
        getP().loadDataUnRead(context);
        // 初始化广播
        initReceiver();
        //1像素
        initOnePx();
        // 蒲公英版本更新（正式注释）
        if(Constans.ISDEBUG){
            checkUpdate();
        }else{
            getP().queryDataUpdateVersion();
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 初始化EventBus
     */
    private void initEvent() {
//      点击首页的‘消息’模块
        BusProvider.getBus().toFlowable(CategroyMessageEvent.class)
                .subscribe(new Consumer<CategroyMessageEvent>() {
                    @Override
                    public void accept(CategroyMessageEvent event) throws Exception {
                        setBadge();
                    }
                });
        //切换公司
        BusProvider.getBus().toFlowable(ChangeCompanyEvent.class)
                .subscribe(new Consumer<ChangeCompanyEvent>() {
                    @Override
                    public void accept(ChangeCompanyEvent event) throws Exception {
                        if(event.getTag() == ConstantUtils.Event.TAG_CHANGE_COMPANY){
                            setBadge();
                        }
                    }
                });
    }

    @BindView(R.id.commonTabLayout)
    CommonTabLayout mCommonTabLayout;
    public void initUI() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        initTab();
        setBadge();
    }


    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mCommonTabLayout.setTabData(mTabEntities);
        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                showFragment(position);// 页面默认
                //消息
                if(2 == position){
                    delBadge();
                }else if(1 == position){
                    BusProvider.getBus().post(new NormalShopEvent());
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        showFragment(0);// 页面默认
    }

    /**
     */
    private XMainFragment2 mainFragment = null;
    private XMainFragment moreFragment = null;
    private XShopFragment shopFragment = null;
    private XMessageFragment messageFragment = null;
    private XMineFragment mineFragment = null;
    private void showFragment(int position) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //先全部隐藏
        if (mainFragment != null) {
            transaction.hide(mainFragment);
        }
        if (moreFragment != null) {
            transaction.hide(moreFragment);
        }
        if (shopFragment != null) {
            transaction.hide(shopFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
        //再显示
        switch (position) {
            case 0:
                if (mainFragment == null) {
                    mainFragment = new XMainFragment2();
                    transaction.add(R.id.tab_fl_manager, mainFragment);
                } else {
                    transaction.show(mainFragment);
                }
                break;
            case 1:
                if (moreFragment == null) {
                    moreFragment = new XMainFragment();
                    transaction.add(R.id.tab_fl_manager, moreFragment);
                } else {
                    transaction.show(moreFragment);
                }
                break;
            case 2:
                if (shopFragment == null) {
                    shopFragment = new XShopFragment();
                    transaction.add(R.id.tab_fl_manager, shopFragment);
                } else {
                    transaction.show(shopFragment);
                }
                break;
            case 3:
                if (mineFragment == null) {
                    mineFragment = new XMineFragment();
                    transaction.add(R.id.tab_fl_manager, mineFragment);
                } else {
                    transaction.show(mineFragment);
                }
                break;
        }
        transaction.commit();
    }



    /**
     * 查看未读信息，删除未读信息
     * 收到信息
     * 切换公司
     */
    private void setBadge(){
        int count = MyDataUtils.getInstance().queryCategroyMessageCount(String.valueOf(ConstantUtils.Messeage.M_SUM));
        if(count > 0){
            mCommonTabLayout.showMsg(2, count);
            mCommonTabLayout.setMsgMargin(2, -5, 5);
        }else{
            mCommonTabLayout.hideMsg(2);
        }
    }

    private void delBadge(){
        //bankuai:(-1.总消息；1.系统通知；2.审批;3.易办事；4.拜访查询-评论；6.沟通；10.日志-工作汇报；11.商城; 12.公告）
        int count = MyDataUtils.getInstance().updateCategroyMessageCount(String.valueOf(ConstantUtils.Messeage.M_SUM),false);
        if(count > 0){
            mCommonTabLayout.showMsg(2, count);
            mCommonTabLayout.setMsgMargin(2, -5, 5);
        }else{
            mCommonTabLayout.hideMsg(2);
        }
        BusProvider.getBus().post(new CategroyMessageEvent());
    }


    /**
     * 双击退出应用
     */
//    private long mPressedTime = 0;
//    @Override
//    public void onBackPressed() {
//        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
//        if ((mNowTime - mPressedTime) > 2000) {//比较两次按键时间差
//            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//            mPressedTime = mNowTime;
//        }else{
//            this.finish();
//            System.exit(0);
//        }
//    }
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SPUtils.getBoolean(ConstantUtils.Sp.IS_LOGIN)) {
            Intent it = new Intent(context, XMessageService.class);
            it.setAction(Constans.UnRreadMsg);
            startService(it);
        }

//        //开启或关闭上传位置服务
//        MyTraceServiceUtil.getInstance().startOrStopService();
//
//        //上传拜访的缓存数据，下载"我的客户"，"商品分类"，"商品"
//        MyServiceUtil.startService(context, XStepService.class);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //按返回键返回桌面
        moveTaskToBack(true);
        //防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
        IntentWrapper.onBackPressed(context);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销广播
        if (myReceive != null) {
            unregisterReceiver(myReceive);
        }
        //(1像素) 注销锁屏监听
        mScreenListener.stopScreenReceiverListener();
    }

    //TODO *********************************广播:start*********************************
    /**
     * 初始化广播
     */
    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter(Constans.Action_login);
        intentFilter.addAction(Constans.Action_login);
        intentFilter.addAction(Constans.chatMsg);
        intentFilter.addAction(Constans.Action_versonup);
        intentFilter.addAction(Constans.UnRreadMsg);// res 未读消息 刷新广播
        registerReceiver(myReceive, intentFilter);
    }
    private BroadcastReceiver myReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constans.Action_login.equals(intent.getAction())) {
                ToastUtils.showCustomToast("账号异常请重新登陆！");
                MyLoginUtil.logout(XTabActivity.this);
            }
//            if (Constans.Action_versonup.equals(intent.getAction())) {
//                // 从服务器获取版本信息--版本更新
//                getP().loadDataVerSionUpdate();
//            }
            if (Constans.UnRreadMsg.equals(intent.getAction())) {
                setBadge();
            }
        }
    };
    //TODO *********************************广播:end*********************************


    //TODO*********************************版本更新：start*********************************
    /**
     * 设置版本更新
     * isQz是否强制更新
     */
    public void setVersionUpdate(String isQz, String versionUrl, String versionContent) {
        SPUtils.setValues(ConstantUtils.Sp.VERSION_UPDATE, "has");
        DownloadDialog dialog = new DownloadDialog(this, versionUrl, versionContent, isQz);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.setCancelable(false);
        dialog.show();
    }
    //TODO*********************************版本更新：end*********************************


    //TODO*******************************(1像素):start***********************************
    //1像素:注册锁屏广播监听器
    private void initOnePx() {
        mScreenListener = new ScreenReceiverUtil(this);
        mScreenManager = ScreenManager.getScreenManagerInstance(this);
        mScreenListener.setScreenReceiverListener(mScreenListenerer);
    }
    // 动态注册锁屏等广播
    private ScreenReceiverUtil mScreenListener;
    // 1像素Activity管理类
    private ScreenManager mScreenManager;
    private ScreenReceiverUtil.SreenStateListener mScreenListenerer = new ScreenReceiverUtil.SreenStateListener() {
        @Override
        public void onSreenOn() {
            mScreenManager.finishActivity();// 亮屏，移除"1像素"
        }
        @Override
        public void onSreenOff() {
            mScreenManager.startActivity();// 那么，我们就制造个"1像素"
        }
        @Override
        public void onUserPresent() {// 解锁，暂不用，保留
        }
    };
    //TODO*******************************(1像素):end***********************************


    //TODO*********************************蒲公英-检查更新start*********************************
    /**
     * 蒲公英-检查更新
     * 强制更新标志
     */
    private void checkUpdate() {
        new PgyUpdateManager.Builder()
                .setForced(true)                //设置是否强制更新
                .setUserCanRetry(false)         //失败后是否提示重新下载
                .setDeleteHistroyApk(true)     // 检查更新前是否删除本地历史 Apk
                .register();
    }
    //TODO*********************************蒲公英-检查更新end*********************************


}
