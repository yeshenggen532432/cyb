package com.qwb.view.base.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.deadline.statebutton.StateButton;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.parsent.PXLogin;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.xmsx.cnlife.view.widget.MyVerifyCodeView;
import com.chiyong.t3.R;


import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 *  登录页面
 */
public class XLoginActivity extends XActivity<PXLogin> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_login2;
    }
//    public int getLayoutId() {
//        return R.layout.x_activity_login;
//    }

    @Override
    public PXLogin newP() {
        return new PXLogin();
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        //状态栏：mViewNeedOffset:需要向下移动的view
        MyStatusBarUtil.getInstance().setTranslucentForImageView(context, mViewNeedOffset);
        initIntent();
        initUI();
        initVerifyCode();
    }

    private int mType;
    private void initIntent() {
        Intent intent = getIntent();
        mType = intent.getIntExtra(ConstantUtils.Intent.TYPE, 0);
    }

    /**
     * 验证码
     */
    @BindView(R.id.layout_code)
    View mViewCode;
    @BindView(R.id.et_code)
    EditText mEtCode;
    MyVerifyCodeView myVerifyCodeView;
    private void initVerifyCode() {
        myVerifyCodeView = findViewById(R.id.MyVerifyCodeView);
        myVerifyCodeView.refreshCode();
        myVerifyCodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEtCode.setText("");
                myVerifyCodeView.refreshCode();
                mSbLogin.setEnabled(false);
            }
        });
        mEtCode.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
               String input = editable.toString().trim();
               String code = myVerifyCodeView.getvCode();
               if(code.equals(input)){
                   mSbLogin.setEnabled(true);
               }else{
                   mSbLogin.setEnabled(false);
               }
            }
        });
    }

    @BindView(R.id.view_need_offset)
    View mViewNeedOffset;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.btn_login)
    StateButton mSbLogin;
    @BindView(R.id.view_feedback)
    View mViewFeedback;
    public void initUI() {
        String phone = SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE);
        mEtPhone.setText(phone);

        mEtPwd.setText(MyLoginUtil.getPwd());

        // 记住密码：查询数据库
//        if (!TextUtils.isEmpty(phone)) {
//            List<PassWordBean> passWordBeanList = LitePal.where("phone = ?", phone).find(PassWordBean.class);
//            for (int i = 0; i < passWordBeanList.size(); i++) {
//                PassWordBean bean = passWordBeanList.get(i);
//            }
//            //先默认：不选中，密码为空，再查询数据库
////            cb_rememberpsw.setChecked(false);
//            mEtPwd.setText("");
//            if (passWordBeanList != null && passWordBeanList.size() > 0) {
//                PassWordBean bean = passWordBeanList.get(0);
//                if (bean.isCheck()) {
////                    cb_rememberpsw.setChecked(true);
//                    mEtPwd.setText(bean.getPwd());
//                }
//            }
//        }

    }

    @OnClick({R.id.tv_forget_pwd, R.id.tv_regist, R.id.btn_login})
    public void onClick(View iv) {
        switch (iv.getId()) {
            //忘记密码
            case R.id.tv_forget_pwd:
                ActivityManager.getInstance().jumpActivity(context, XForgetPwdActivity.class);
                break;
            //注册
            case R.id.tv_regist:
                ActivityManager.getInstance().jumpActivity(context, XRegisterActivity.class);
                break;
            //登录-tab
            case R.id.btn_login:
                login();
                break;
        }
    }

    public void login() {
        String phone = mEtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showCustomToast("用户名不能为空！");
            return;
        }
        String pwd = mEtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showCustomToast("密码不能为空！");
            return;
        }

        //TODO 第一次初始密码123456可以登录，后面初始密码123456不可以登录
        if(Constans.ISPASSWORD){
        }else{
            if (!SPUtils.getBoolean(ConstantUtils.Sp.INIT_PASSWORD)) {
                if ("123456".equals(pwd)) {
                    dialogNormalStyle();//初始密码123456修改提示
                    return;
                }
            }
        }
        //记住密码 默认true
        getP().submit(context, phone, pwd, true, mType);
    }

    /**
     * dialog-初始密码123456修改提示
     */
    private void dialogNormalStyle() {
        final NormalDialog dialog = new NormalDialog(context);
        dialog.setCanceledOnTouchOutside(false);//外部点击不消失
        dialog.content("您当前使用的密码是初始密码123456，请修改密码").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
//                        dialog.dismiss();
                ActivityManager.getInstance().jumpActivity(context, XForgetPwdActivity.class);
            }
        });
    }

    private int mErrorCount;//提交失败次数；3次要输入验证码；5次跳转"忘记密码"
    public void submitError(){
        mErrorCount++;
        if(3 == mErrorCount){
            mViewCode.setVisibility(View.VISIBLE);
            mSbLogin.setEnabled(false);
            ToastUtils.showCustomToast("错误3次，请输入验证码验证");
        }else if(5 <= mErrorCount){
            ToastUtils.showCustomToast("错误5次，请修改密码");
            ActivityManager.getInstance().jumpActivity(context, XForgetPwdActivity.class);
        }
    }

}
