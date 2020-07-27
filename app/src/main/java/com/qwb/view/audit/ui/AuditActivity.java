package com.qwb.view.audit.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.SPUtils;
import com.qwb.view.audit.parsent.PAudit;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.chiyong.t3.R;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 审批页面
 */
public class AuditActivity extends XActivity<PAudit> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_audit;
    }

    @Override
    public PAudit newP() {
        return new PAudit();
    }

    public void initData(Bundle savedInstanceState) {
        initUI();
    }

    private void initUI() {
        initHead();
        initTab();
    }

    /**
     * 初始化UI-头部
     */
    TextView mTvHeadTitle;
    TextView mTvHeadRight;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);
        findViewById(R.id.head_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle = findViewById(R.id.tv_head_title);
        mTvHeadRight = findViewById(R.id.tv_head_right);
        mTvHeadTitle.setText("审批");
        mTvHeadRight.setText("我发\n起的");
        findViewById(R.id.tv_head_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.newIntent(context)
                        .to(XAuditCommonActivity.class)
                        .putInt(ConstantUtils.Intent.TYPE, 1)
                        .launch();
            }
        });
    }

    @BindView(R.id.tab_tv_send_audit)
    TextView mTabTvSendAudit;
    @BindView(R.id.tab_tv_my_audit)
    TextView mTabTvMyAudit;
    @BindView(R.id.tab_tv_my_exec)
    TextView mTabTvMyExec;
    @BindView(R.id.tab_iv_send_audit)
    ImageView mTabIvSendAudit;
    @BindView(R.id.tab_iv_my_audit)
    ImageView mTabIvMyAudit;
    @BindView(R.id.tab_iv_my_exec)
    ImageView mTabIvMyExec;
    private void initTab() {
        mTabTvSendAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabTextColor("1");
                showFragment(1);
            }
        });
        mTabTvMyAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabTextColor("2");
                showFragment(2);
            }
        });
        mTabTvMyExec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabTextColor("3");
                showFragment(3);
            }
        });
        mTabIvSendAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialogChangeNormalFragment(1);
            }
        });
        mTabIvMyAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChangeNormalFragment(2);
            }
        });
        mTabIvMyExec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChangeNormalFragment(3);
            }
        });
        showNormalFragment();
    }

    /**
     * 显示默认fragment
     */
    public void showNormalFragment(){
        String type = SPUtils.getSValues(ConstantUtils.Sp.AUDIT_NORMAL_TAB);
        if(MyStringUtil.isEmpty(type)){
            type = "1";
        }
        setTabImage(type);
        setTabTextColor(type);
        showFragment(Integer.valueOf(type));// 页面默认
    }

    /**
     * 设置默认的字体颜色，图片
     */
    public void setTabTextColor(String type){
        mTabTvSendAudit.setTextColor(getResources().getColor(R.color.gray_6));
        mTabTvMyAudit.setTextColor(getResources().getColor(R.color.gray_6));
        mTabTvMyExec.setTextColor(getResources().getColor(R.color.gray_6));
        mTabTvSendAudit.setBackgroundResource(R.color.white);
        mTabTvMyAudit.setBackgroundResource(R.color.white);
        mTabTvMyExec.setBackgroundResource(R.color.white);
        if("1".equals(type)){
            mTabTvSendAudit.setTextColor(getResources().getColor(R.color.white));
            mTabTvSendAudit.setBackgroundResource(R.color.light_blue);
        }else if("2".equals(type)){
            mTabTvMyAudit.setTextColor(getResources().getColor(R.color.white));
            mTabTvMyAudit.setBackgroundResource(R.color.light_blue);
        }else if("3".equals(type)){
            mTabTvMyExec.setTextColor(getResources().getColor(R.color.white));
            mTabTvMyExec.setBackgroundResource(R.color.light_blue);
        }
    }



    /**
     * 显示片段
     */
    private AuditTabSendAuditFragment sendAuditFragment;
    private AuditTabCommonFragment myAuditFragment;
    private AuditTabCommonFragment myExecFragment;
    private void showFragment(int position) {
        FragmentManager manager=getSupportFragmentManager();;
        // 开启一个Fragment事务
        FragmentTransaction transaction = manager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (position) {
            case 1:
                if (sendAuditFragment == null) {
                    sendAuditFragment = new AuditTabSendAuditFragment();
                    transaction.add(R.id.fl_manager, sendAuditFragment);
                } else {
                    transaction.show(sendAuditFragment);
                }
                transaction.commit();
                break;
            case 2:
                if (myAuditFragment == null) {
                    myAuditFragment = new AuditTabCommonFragment();
                    Bundle args = new Bundle();
                    args.putInt(ConstantUtils.Intent.TYPE, 2);
                    myAuditFragment.setArguments(args);
                    transaction.add(R.id.fl_manager, myAuditFragment);
                } else {
                    transaction.show(myAuditFragment);
                }
                transaction.commit();
                break;
            case 3:
                if (myExecFragment == null) {
                    myExecFragment = new AuditTabCommonFragment();
                    Bundle args = new Bundle();
                    args.putInt(ConstantUtils.Intent.TYPE, 3);
                    myExecFragment.setArguments(args);
                    transaction.add(R.id.fl_manager, myExecFragment);
                } else {
                    transaction.show(myExecFragment);
                }
                transaction.commit();
                break;
        }
    }
    /**
     * 将所有的Fragment都置为隐藏状态。
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (sendAuditFragment != null) {
            transaction.hide(sendAuditFragment);
        }
        if (myAuditFragment != null) {
            transaction.hide(myAuditFragment);
        }
        if (myExecFragment != null) {
            transaction.hide(myExecFragment);
        }
    }

    /**
     * 修改“默认Fragment”
     */
    public void showDialogChangeNormalFragment(final int type){
        NormalDialog dialog = new NormalDialog(context);
        String tabName = "发起审批";
        if(1 == type){
            tabName = "发起审批";
        }else if(2 == type){
            tabName = "我审批的";
        }else if(3 == type){
            tabName = "我执行的";
        }
        dialog.content("是否修改下次进入审批页面默认tab为（"+tabName+")吗？").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                SPUtils.setValues(ConstantUtils.Sp.AUDIT_NORMAL_TAB,""+type);
                setTabImage("" + type);
            }
        });
    }

    public void setTabImage(String type){
        mTabIvSendAudit.setImageResource(R.mipmap.ic_audit_tab_unselect);
        mTabIvMyAudit.setImageResource(R.mipmap.ic_audit_tab_unselect);
        mTabIvMyExec.setImageResource(R.mipmap.ic_audit_tab_unselect);
        if("1".equals(type)){
            mTabIvSendAudit.setImageResource(R.mipmap.ic_audit_tab_select);
        }else if("2".equals(type)){
            mTabIvMyAudit.setImageResource(R.mipmap.ic_audit_tab_select);
        }else if("3".equals(type)){
            mTabIvMyExec.setImageResource(R.mipmap.ic_audit_tab_select);
        }
    }


}
