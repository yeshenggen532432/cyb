package com.qwb.view.base.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.parsent.PXForgetPwd;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyTimeCount;
import com.xmsx.qiweibao.R;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 忘记密码
 */
public class XForgetPwdActivity extends XActivity<PXForgetPwd> {
    private MyTimeCount time;
    public String sessionId;

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_forget_pwd;
    }

    @Override
    public PXForgetPwd newP() {
        return new PXForgetPwd();
    }

    public void initData(Bundle savedInstanceState) {
        initUI();
        initCode();
    }

    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.tv_code)
    TextView mTvCode;

    public void initUI() {
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
                Router.pop(context);
            }
        });//返回
        mIvHeadLeft.setImageResource(R.mipmap.icon_fanhui);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_8), (int) getResources().getDimension(R.dimen.dp_14));
        mIvHeadLeft.setLayoutParams(params);
//        mTvHeadTitle.setText("通讯录");
//        mTvHeadTitle.setTextColor(getResources().getColor(R.color.gray_3));
    }


    public void initCode() {
        time = new MyTimeCount(60000, 1000, mTvCode);
        mTvCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendCode();
            }
        });
    }


    @OnClick({R.id.btn_next})
    public void onClick(View iv) {
        switch (iv.getId()) {
            //下一步
            case R.id.btn_next:
                next();
                break;
        }
    }

    /**
     * 发送验证码
     */
    private void sendCode() {
        sessionId = null;
        String phone = mEtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || !MyUtils.isMobileNum(phone)) {
            ToastUtils.showCustomToast("手机号码格式不对");
            return;
        }
        time.start();
        getP().sendCode(context, phone);
    }


    /**
     * 下一步
     */
    private void next() {
        String phone = mEtPhone.getText().toString().trim();
        String code = mEtCode.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showCustomToast("手机号码不能为空");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showCustomToast("验证码不能为空");
            return;
        }
        if (Constans.SYSCODE.equals(code) || !MyStringUtil.isEmpty(sessionId)) {
            getP().submit(context, phone, code, sessionId);
        } else {
            sessionId = null;
            ToastUtils.showCustomToast("请重新获取验证码");
        }
    }

    /**
     * 下一步提交成功
     */
    public void submitSuccess(String phone) {
//        sessionId = null;
        String code = mEtCode.getText().toString().trim();
        Router.newIntent(context)
                .putString(ConstantUtils.Intent.PHONE, phone)
                .putString(ConstantUtils.Intent.CODE, code)
                .putString(ConstantUtils.Intent.SESSION_ID, sessionId)
                .to(XForgetPwdNextActivity.class)
                .launch();
    }

}
