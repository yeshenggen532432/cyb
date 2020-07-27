package com.qwb.view.delivery.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qwb.utils.ActivityManager;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;

import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 创建描述：物流配送中心
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class DeliveryActivity extends XActivity {
    @Override
    public int getLayoutId() {
        return R.layout.x_activity_delivery;
    }

    @Override
    public Object newP() {
        return null;
    }
    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        showFragment(1);// 页面默认
        RadioGroup tabGroup = findViewById(R.id.radioGroup);
        tabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        showFragment(1);
                        break;
                    case R.id.rb2:
                        showFragment(2);
                        break;
                    case R.id.rb3:
                        showFragment(3);
                        break;
                    case R.id.rb4:
                        showFragment(4);
                        break;
                    case R.id.rb5:
                        showFragment(5);
                        break;
                    case R.id.rb6:
                        showFragment(6);
                        break;
                }
            }
        });
    }

    /**
     * 初始化UI-头部
     */
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);
        findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        TextView tv_headTitle = findViewById(R.id.tv_head_title);
        TextView tvHeadRight = findViewById(R.id.tv_head_right);
        tv_headTitle.setText("物流配送中心");
        tvHeadRight.setText("导航");
        findViewById(R.id.tv_head_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().jumpActivity(context, DeliveryNavActivity.class);
            }
        });
    }

    /**
     * 显示片段
     */
    private Delivery1Fragment delivery1Fragment;
    private Delivery2Fragment delivery2Fragment;
    private Delivery3Fragment delivery3Fragment;
    private Delivery4Fragment delivery4Fragment;
    private Delivery5Fragment delivery5Fragment;
    private Delivery6Fragment delivery6Fragment;
    private void showFragment(int position) {
        FragmentManager manager=getSupportFragmentManager();;
        // 开启一个Fragment事务
        FragmentTransaction transaction = manager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (position) {
            case 1://
                if (delivery1Fragment == null) {
                    delivery1Fragment = new Delivery1Fragment();
                    transaction.add(R.id.fl_manager, delivery1Fragment);
                } else {
                    transaction.show(delivery1Fragment);
                }
                transaction.commit();
                break;
            case 2://
                if (delivery2Fragment == null) {
                    delivery2Fragment = new Delivery2Fragment();
                    transaction.add(R.id.fl_manager, delivery2Fragment);
                } else {
                    transaction.show(delivery2Fragment);
                }
                transaction.commit();
                break;
            case 3://
                if (delivery3Fragment == null) {
                    delivery3Fragment = new Delivery3Fragment();
                    transaction.add(R.id.fl_manager, delivery3Fragment);
                } else {
                    transaction.show(delivery3Fragment);
                }
                transaction.commit();
                break;
            case 4://
                if (delivery4Fragment == null) {
                    delivery4Fragment = new Delivery4Fragment();
                    transaction.add(R.id.fl_manager, delivery4Fragment);
                } else {
                    transaction.show(delivery4Fragment);
                }
                transaction.commit();
                break;
            case 5://
                if (delivery5Fragment == null) {
                    delivery5Fragment = new Delivery5Fragment();
                    transaction.add(R.id.fl_manager, delivery5Fragment);
                } else {
                    transaction.show(delivery5Fragment);
                }
                transaction.commit();
                break;
            case 6://
                if (delivery6Fragment == null) {
                    delivery6Fragment = new Delivery6Fragment();
                    transaction.add(R.id.fl_manager, delivery6Fragment);
                } else {
                    transaction.show(delivery6Fragment);
                }
                transaction.commit();
                break;
        }
    }
    /**
     * 将所有的Fragment都置为隐藏状态。
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (delivery1Fragment != null) {
            transaction.hide(delivery1Fragment);
        }
        if (delivery2Fragment != null) {
            transaction.hide(delivery2Fragment);
        }
        if (delivery3Fragment != null) {
            transaction.hide(delivery3Fragment);
        }
        if (delivery4Fragment != null) {
            transaction.hide(delivery4Fragment);
        }
        if (delivery5Fragment != null) {
            transaction.hide(delivery5Fragment);
        }
        if (delivery6Fragment != null) {
            transaction.hide(delivery6Fragment);
        }
    }

}
