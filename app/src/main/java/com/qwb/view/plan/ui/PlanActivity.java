package com.qwb.view.plan.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;

import com.qwb.utils.ActivityManager;
import com.qwb.view.plan.parsent.PPlan;
import com.chiyong.t3.R;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 计划拜访
 */
public class PlanActivity extends XActivity<PPlan> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_plan;
    }

    @Override
    public PPlan newP() {
        return new PPlan();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
    }

    private void initUI() {
        RadioGroup tabGroup = findViewById(R.id.radioGroup);
        tabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_mine:// 我的拜访
                        showFragment(1);
                        break;
                    case R.id.radio_underling:// 下属拜访
                        showFragment(2);
                        break;
                }
            }
        });
        showFragment(1);// 页面默认

        //返回键
        findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        //线路
        findViewById(R.id.layout_line).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().jumpActivity(context, PlanLineActivity.class);
            }
        });
    }

    /**
     */
    private PlanMineFragment planMineFragment;
    private PlanUnderFragment planUnderFragment;
    private void showFragment(int position) {
        FragmentManager manager=getSupportFragmentManager();;
        // 开启一个Fragment事务
        FragmentTransaction transaction = manager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (position) {
            case 1:
                if (planMineFragment == null) {
                    planMineFragment = new PlanMineFragment();
                    transaction.add(R.id.fl_manager, planMineFragment);
                } else {
                    transaction.show(planMineFragment);
                }
                transaction.commit();
                break;
            case 2:
                if (planUnderFragment == null) {
                    planUnderFragment = new PlanUnderFragment();
                    transaction.add(R.id.fl_manager, planUnderFragment);
                } else {
                    transaction.show(planUnderFragment);
                }
                transaction.commit();
                break;
        }
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (planMineFragment != null) {
            transaction.hide(planMineFragment);
        }
        if (planUnderFragment != null) {
            transaction.hide(planUnderFragment);
        }
    }




}
