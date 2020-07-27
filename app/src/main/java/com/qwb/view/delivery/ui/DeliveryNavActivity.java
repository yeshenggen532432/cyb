package com.qwb.view.delivery.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.qwb.utils.ActivityManager;
import com.qwb.view.delivery.parsent.PDeliveryNav;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 配送中心：导航列表（选择配送单据，选择客户）
 */
public class DeliveryNavActivity extends XActivity<PDeliveryNav> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_delivery_nav;
    }

    @Override
    public PDeliveryNav newP() {
        return new PDeliveryNav();
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
        RadioGroup tabGroup = findViewById(R.id.radioGroup);
        tabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_map:
                        showFragment(1);
                        break;
                    case R.id.rb_list:
                        showFragment(2);
                        break;
                }
            }
        });
        showFragment(1);// 页面默认
    }

    /**
     * 初始化UI-头部
     */
    TextView mTvHeadRight;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);
        findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadRight = findViewById(R.id.tv_head_right);
        mTvHeadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				Router.newIntent(context)
						.to(DeliveryMapActivity.class)
						.launch();
            }
        });
    }

    /**
     */
    private DeliveryNavMapFragment deliveryNavMapFragment;
    private DeliveryNavListFragment deliveryNavListFragment;
    private void showFragment(int position) {
        FragmentManager manager=getSupportFragmentManager();;
        // 开启一个Fragment事务
        FragmentTransaction transaction = manager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (position) {
            case 1:
                if (deliveryNavMapFragment == null) {
                    deliveryNavMapFragment = new DeliveryNavMapFragment();
                    transaction.add(R.id.fl_manager, deliveryNavMapFragment);
                } else {
                    transaction.show(deliveryNavMapFragment);
                }
                transaction.commit();
                mTvHeadRight.setVisibility(View.GONE);
                break;
            case 2:
                if (deliveryNavListFragment == null) {
                    deliveryNavListFragment = new DeliveryNavListFragment();
                    transaction.add(R.id.fl_manager, deliveryNavListFragment);
                } else {
                    transaction.show(deliveryNavListFragment);
                }
                transaction.commit();
                mTvHeadRight.setVisibility(View.VISIBLE);
                break;
        }
    }
    /**
     * 将所有的Fragment都置为隐藏状态。
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (deliveryNavMapFragment != null) {
            transaction.hide(deliveryNavMapFragment);
        }
        if (deliveryNavListFragment != null) {
            transaction.hide(deliveryNavListFragment);
        }
    }

}
