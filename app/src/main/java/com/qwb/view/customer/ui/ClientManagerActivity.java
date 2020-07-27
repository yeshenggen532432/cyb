package com.qwb.view.customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.jaeger.library.StatusBarUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.widget.MyMenuPopup;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.chiyong.t3.R;
import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 创建描述：客户管理
 */
public class ClientManagerActivity extends XActivity {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_client_manager;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        createPopup();//客户分布图，新增客户，新增经销商
    }

    /**
     * 初始化Intent
     */
    public static int type = 1;//1：拜访客户下单 2:单独下单(电话下单) 3：订货下单模块（列表）4：退货;5：退货下单（列表），6:车销下单
    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null){
            type = intent.getIntExtra(ConstantUtils.Intent.TYPE, 1);
        }
    }

    /**
     * 初始化UI
     */
    @BindView(R.id.rb_near)
    RadioButton mRbNear;
    @BindView(R.id.rb_mine)
    public RadioButton mRbMine;
    private void initUI() {
        initHead();
        boolean query = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.KHGL_QUERY_NEW);
        boolean near = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.KHGL_NEAR_NEW);
        if(query && !near){
            mRbNear.setVisibility(View.GONE);
        }else{
            mRbNear.setVisibility(View.VISIBLE);
        }

        RadioGroup tabGroup = findViewById(R.id.rg_client_manager);
        tabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_near:// 周边客户
                        showFragment(1);
                        break;
                    case R.id.rb_mine:// 我的客户
                        showFragment(2);
                        break;
                    case R.id.rb_tomporary:// 临时客户
                        showFragment(3);
                        break;
                }
            }
        });
        showFragment(2);// 页面默认
    }

    /**
     * 初始化UI-头部
     */
    TextView mTvHeadRightAdd;//新增
    ImageView mIvHeadRightFrame;//组织架构
    ImageView mIvHeadRightMap;//地图
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorGreen(context);
        findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvHeadRightAdd = findViewById(R.id.tv_head_right_add);
        mIvHeadRightFrame = findViewById(R.id.iv_head_right_frame);
        mIvHeadRightMap = findViewById(R.id.iv_head_right_map);
        //选择客户
        if (2 == type) {
            mTvHeadRightAdd.setVisibility(View.GONE);
            mIvHeadRightFrame.setVisibility(View.GONE);
            mIvHeadRightMap.setVisibility(View.GONE);
        }
    }

    /**
     * 显示片段（1：周边客户:2：我的客户）
     * @param position
     */
    private MineClientFragment mineClientFragment;
    private NearClientFragment nearClientFragment;
    private void showFragment(int position) {
        FragmentManager manager=getSupportFragmentManager();;
        // 开启一个Fragment事务
        FragmentTransaction transaction = manager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (position) {
            case 1://周边客户
                if (nearClientFragment == null) {
                    nearClientFragment = new NearClientFragment();
                    transaction.add(R.id.fl_manager, nearClientFragment);
                } else {
                    transaction.show(nearClientFragment);
                }
                transaction.commit();
                mTvHeadRightAdd.setVisibility(View.GONE);
                mIvHeadRightFrame.setVisibility(View.GONE);
                mIvHeadRightMap.setVisibility(View.VISIBLE);
                break;
            case 2://我的客户
                if (mineClientFragment == null) {
                    mineClientFragment = new MineClientFragment();
                    transaction.add(R.id.fl_manager, mineClientFragment);
                } else {
                    transaction.show(mineClientFragment);
                }
                transaction.commit();
                mTvHeadRightAdd.setVisibility(View.VISIBLE);
                mIvHeadRightMap.setVisibility(View.GONE);
                break;
        }
        //选择客户
        if (2 == type) {
            mTvHeadRightAdd.setVisibility(View.GONE);
            mIvHeadRightMap.setVisibility(View.GONE);
            mIvHeadRightFrame.setVisibility(View.GONE);
        }
    }
    /**
     * 将所有的Fragment都置为隐藏状态。
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (nearClientFragment != null) {
            transaction.hide(nearClientFragment);
        }
        if (mineClientFragment != null) {
            transaction.hide(mineClientFragment);
        }
    }

    /**
     * 客户分布图，新增客户，新增经销商
     */
    private MyMenuPopup menuPopup;
    public void createPopup() {
        String[] items = {"客户分布图", "新增客户"};
        menuPopup = new MyMenuPopup(context, items);
        menuPopup.createPopup();
        menuPopup.setOnItemClickListener(new MyMenuPopup.OnItemClickListener() {
            @Override
            public void setOnItemClickListener(String text, int position) {
                menuPopup.dismiss();
                if (position == 0){
                    ActivityManager.getInstance().jumpActivity(context, CustomerMapActivity.class);
                }else{
                    ActivityManager.getInstance().jumpToAddClientActivity(context, 2, null);
                }
            }
        });
    }


    /**
     * 按钮点击事件
     */
    @OnClick({R.id.tv_head_right_add, R.id.iv_head_right_map, R.id.iv_head_right_frame})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.tv_head_right_add://客户分布图，新增客户，新增经销商
                menuPopup.showAsDropDown(mTvHeadRightAdd,0,0);
//                ActivityManager.getInstance().jumpToAddClientActivity(context, 2, null);
                break;
            case R.id.iv_head_right_map://客户分布图
            case R.id.iv_head_right_frame://组织架构
//                Router.newIntent(context)
//                        .putString(ConstantUtils.Intent.TYPE,"1")
//                        .to(WebTitleActivity.class)
//                        .launch();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空：结构图数据
        Constans.branchMap.clear();
        Constans.memberMap.clear();
        Constans.ziTrueMap.clear();
        Constans.ParentTrueMap.clear();
        Constans.ParentTrueMap2.clear();
    }


}
