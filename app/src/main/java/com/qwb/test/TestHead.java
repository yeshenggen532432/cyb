package com.qwb.test;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deadline.statebutton.StateButton;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;

import butterknife.BindView;

/**
 * 头部
 */

public class TestHead {

    private Activity context;

    @BindView(R.id.head_left)
    View mViewLeft;
    @BindView(R.id.head_right)
    View mViewRight;
    @BindView(R.id.head_right2)
    View mViewRight2;
    @BindView(R.id.head_right3)
    View mViewRight3;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;
    @BindView(R.id.tv_head_right2)
    TextView mTvHeadRight2;
    @BindView(R.id.tv_head_right3)
    TextView mTvHeadRight3;
    @BindView(R.id.iv_head_left)
    ImageView mIvHeadLeft;
    @BindView(R.id.iv_head_right)
    ImageView mIvHeadRight;
    @BindView(R.id.iv_head_right2)
    ImageView mIvHeadRight2;
    @BindView(R.id.iv_head_right3)
    ImageView mIvHeadRight3;
    @BindView(R.id.sb_head_right)
    StateButton mSbHeadRight;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);
        mTvHeadTitle.setText("测试");
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_25), (int) getResources().getDimension(R.dimen.dp_25));
//        mIvHeadRight2.setLayoutParams(params);
//        mIvHeadRight2.setImageResource(R.mipmap.x_ic_history_order);

        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mViewRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mViewRight3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }



}
