package com.qwb.view.object.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.qwb.utils.ActivityManager;
import com.qwb.view.common.model.TabEntity;
import com.xmsx.qiweibao.R;
import java.util.ArrayList;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 选择对象
 * 1.供应商 2.员工 3.客户 4.其他往来单位
 */
public class ChooseObjectActivity extends XActivity {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_choose_object;
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

    private TextView tv_head_title;
    private void initHead() {
        ImageView img_head_back = findViewById(R.id.iv_head_back);
        tv_head_title = findViewById(R.id.tv_head_title);
        tv_head_title.setText("选择对象");
        img_head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
    }


    private String[] mTitles = {"供应商", "员工", "客户", "其他往来单位"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    @BindView(R.id.commonTabLayout)
    CommonTabLayout mCommonTabLayout;
    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], 0, 0));
        }
        mCommonTabLayout.setTabData(mTabEntities);
        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                showFragment(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        showFragment(0);// 页面默认
    }

    /**
     */
    private ObjectProviderFragment providerFragment = null;
    private ObjectMemberFragment memberFragment = null;
    private ObjectCustomerFragment customerFragment = null;
    private ObjectFinUnitFragment finUnitFragment = null;
    private void showFragment(int position) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //先全部隐藏
        if (providerFragment != null) {
            transaction.hide(providerFragment);
        }
        if (memberFragment != null) {
            transaction.hide(memberFragment);
        }
        if (customerFragment != null) {
            transaction.hide(customerFragment);
        }
        if (finUnitFragment != null) {
            transaction.hide(finUnitFragment);
        }
        //再显示
        switch (position) {
            case 0:
                if (providerFragment == null) {
                    providerFragment = new ObjectProviderFragment();
                    transaction.add(R.id.tab_fl_manager, providerFragment);
                } else {
                    transaction.show(providerFragment);
                }
                break;
            case 1:
                if (memberFragment == null) {
                    memberFragment = new ObjectMemberFragment();
                    transaction.add(R.id.tab_fl_manager, memberFragment);
                } else {
                    transaction.show(memberFragment);
                }
                break;
            case 2:
                if (customerFragment == null) {
                    customerFragment = new ObjectCustomerFragment();
                    transaction.add(R.id.tab_fl_manager, customerFragment);
                } else {
                    transaction.show(customerFragment);
                }
                break;
            case 3:
                if (finUnitFragment == null) {
                    finUnitFragment = new ObjectFinUnitFragment();
                    transaction.add(R.id.tab_fl_manager, finUnitFragment);
                } else {
                    transaction.show(finUnitFragment);
                }
                break;
        }
        transaction.commit();
    }


}
