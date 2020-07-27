package com.qwb.view.base.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qwb.utils.ActivityManager;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.parsent.PXRegisterNext;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 注册完善信息页面
 */
public class XRegisterNextActivity extends XActivity<PXRegisterNext> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_register_next;
    }

    @Override
    public PXRegisterNext newP() {
        return new PXRegisterNext();
    }

    public void initData(Bundle savedInstanceState) {
        initUI();
    }

    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_pwd2)
    TextView mEtPwd2;
    public void initUI(){
        initHead();
    }

    @BindView(R.id.head)
    View mHead;
    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.tv_head_left)
    TextView mTvHeadLeft;
    @BindView(R.id.iv_head_left)
    ImageView mIvHeadLeft;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;
    @BindView(R.id.iv_head_right)
    ImageView mIvHeadRight;
    @BindView(R.id.tv_head_right2)
    TextView mTvHeadRight2;
    @BindView(R.id.iv_head_right2)
    ImageView mIvHeadRight2;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mIvHeadLeft.setImageResource(R.mipmap.icon_fanhui);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.dp_8),(int)getResources().getDimension(R.dimen.dp_14));
        mIvHeadLeft.setLayoutParams(params);
    }

    @OnClick({R.id.btn_submit})
    public void onClick(View iv){
        switch (iv.getId()){
            case R.id.btn_submit:
                UpdateInfo();
                break;
        }
    }

    private void UpdateInfo() {
        String name = mEtName.getText().toString().trim();
        String pwd = mEtPwd.getText().toString().trim();
        String pwd2 = mEtPwd2.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showCustomToast( "名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showCustomToast( "验证码不能为空");
            return;
        }
        if (!pwd.equals(pwd2)) {
            ToastUtils.showCustomToast( "两次密码不一样");
            return;
        }

        getP().submit(context,name,pwd);

    }

}
