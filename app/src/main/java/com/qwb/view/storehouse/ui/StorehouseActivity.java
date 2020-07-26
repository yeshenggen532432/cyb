package com.qwb.view.storehouse.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.qiweibao.R;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 库位管理
 */
public class StorehouseActivity extends XActivity {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_storehouse;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
    }

    private void initUI() {
        initHead();
        initTab();
    }

    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);
        mTvHeadTitle.setText("库位管理");
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
    }

    @BindView(R.id.view_in)
    View mViewIn;
    @BindView(R.id.view_out)
    View mViewOut;
    @BindView(R.id.tab_tv_storehouse_in)
    TextView mTabTvStorehouseIn;
    @BindView(R.id.tab_tv_storehouse_out)
    TextView mTabTvStorehouseOut;
    @BindView(R.id.tab_iv_storehouse_in)
    ImageView mTabIvStorehouseIn;
    @BindView(R.id.tab_iv_storehouse_out)
    ImageView mTabIvStorehouseOut;
    private void initTab() {
        mViewIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabTextColor("1");
                showFragment(1);
            }
        });
        mViewOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabTextColor("2");
                showFragment(2);
            }
        });
        mTabIvStorehouseIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChangeNormalFragment(1);
            }
        });
        mTabIvStorehouseOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChangeNormalFragment(2);
            }
        });
        showNormalFragment();
    }

    /**
     * 显示默认fragment
     */
    public void showNormalFragment(){
        String type = SPUtils.getSValues(ConstantUtils.Sp.STOREHOUSE_NORMAL_TAB);
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
        mTabTvStorehouseIn.setBackgroundResource(R.color.white);
        mTabTvStorehouseOut.setBackgroundResource(R.color.white);
        mTabTvStorehouseIn.setTextColor(getResources().getColor(R.color.gray_6));
        mTabTvStorehouseOut.setTextColor(getResources().getColor(R.color.gray_6));
        if("1".equals(type)){
            mTabTvStorehouseIn.setTextColor(getResources().getColor(R.color.white));
            mTabTvStorehouseIn.setBackgroundResource(R.color.light_blue);
        }else if("2".equals(type)){
            mTabTvStorehouseOut.setTextColor(getResources().getColor(R.color.white));
            mTabTvStorehouseOut.setBackgroundResource(R.color.light_blue);
        }
    }


    /**
     * 显示片段
     */
    private StorehouseInFragment storehouseInFragment;
    private StorehouseOutFragment storehouseOutFragment;
    private void showFragment(int position) {
        FragmentManager manager=getSupportFragmentManager();;
        // 开启一个Fragment事务
        FragmentTransaction transaction = manager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (position) {
            case 1:
                if (storehouseInFragment == null) {
                    storehouseInFragment = new StorehouseInFragment();
                    transaction.add(R.id.fl_manager, storehouseInFragment);
                } else {
                    transaction.show(storehouseInFragment);
                }
                transaction.commit();
                break;
            case 2:
                if (storehouseOutFragment == null) {
                    storehouseOutFragment = new StorehouseOutFragment();
                    Bundle args = new Bundle();
                    args.putInt(ConstantUtils.Intent.TYPE, 2);
                    storehouseOutFragment.setArguments(args);
                    transaction.add(R.id.fl_manager, storehouseOutFragment);
                } else {
                    transaction.show(storehouseOutFragment);
                }
                transaction.commit();
                break;

        }
    }
    /**
     * 将所有的Fragment都置为隐藏状态。
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (storehouseInFragment != null) {
            transaction.hide(storehouseInFragment);
        }
        if (storehouseOutFragment != null) {
            transaction.hide(storehouseOutFragment);
        }
    }

    /**
     * 修改“默认Fragment”
     */
    public void showDialogChangeNormalFragment(final int type){
        NormalDialog dialog = new NormalDialog(context);
        String tabName = "入仓";
        if(1 == type){
            tabName = "入仓";
        }else if(2 == type){
            tabName = "出仓";
        }
        dialog.content("是否修改下次进入库位管理页面默认tab为（"+tabName+")吗？").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                SPUtils.setValues(ConstantUtils.Sp.STOREHOUSE_NORMAL_TAB,""+type);
                setTabImage("" + type);
            }
        });
    }

    public void setTabImage(String type){
        mTabIvStorehouseIn.setImageResource(R.mipmap.ic_audit_tab_unselect);
        mTabIvStorehouseOut.setImageResource(R.mipmap.ic_audit_tab_unselect);
        if("1".equals(type)){
            mTabIvStorehouseIn.setImageResource(R.mipmap.ic_audit_tab_select);
        }else if("2".equals(type)){
            mTabIvStorehouseOut.setImageResource(R.mipmap.ic_audit_tab_select);
        }
    }


}
