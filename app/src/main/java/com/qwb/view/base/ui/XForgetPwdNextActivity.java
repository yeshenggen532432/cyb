package com.qwb.view.base.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qwb.utils.MyPassWordUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.parsent.PXForgetPwdNext;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;
import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

public class XForgetPwdNextActivity extends XActivity<PXForgetPwdNext> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_forget_pwd_next;
    }

    @Override
    public PXForgetPwdNext newP() {
        return new PXForgetPwdNext();
    }

    public void initData(Bundle savedInstanceState) {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        initIntent();
        initUI();
    }

    private String phone;
    private String code;
    private String sessionId;

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            phone = intent.getStringExtra(ConstantUtils.Intent.PHONE);
            code = intent.getStringExtra(ConstantUtils.Intent.CODE);
            sessionId = intent.getStringExtra(ConstantUtils.Intent.SESSION_ID);
        }
    }

    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_pwd2)
    EditText mEtPwd2;

    private void initUI() {
        initHead();
        mEtPwd.setHint(MyPassWordUtil.TIP);
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
                Router.pop(context);
            }
        });//返回
        mIvHeadLeft.setImageResource(R.mipmap.icon_fanhui);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_8), (int) getResources().getDimension(R.dimen.dp_14));
        mIvHeadLeft.setLayoutParams(params);
    }

    @OnClick({R.id.btn_submit})
    public void onClick(View iv) {
        switch (iv.getId()) {
            //下一步
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private void submit() {
        try{
            String pwd = mEtPwd.getText().toString().trim();
            String pwd2 = mEtPwd2.getText().toString().trim();
            if ("123456".equals(pwd)) {
                ToastUtils.showCustomToast("密码不能设置为初始密码123456");
                return;
            }
            MyPassWordUtil.verify(pwd);

            if (!MyStringUtil.eq(pwd, pwd2)) {
                ToastUtils.showCustomToast("两次输入密码不一致");
                return;
            }
            getP().submit(context, phone, pwd, code, sessionId);
        }catch (Exception e){
            ToastUtils.showError(e);
        };
    }

}
