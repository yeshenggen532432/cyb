package com.qwb.view.mine.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.qwb.utils.MyPassWordUtil;
import com.qwb.view.mine.parsent.PUpdatePassword;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.qiweibao.R;
import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 设置-修改密码
 */
public class UpdatePasswordActivity extends XActivity<PUpdatePassword> implements OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_update_password;
    }

    @Override
    public PUpdatePassword newP() {
        return new PUpdatePassword();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        prepareTimer();
        initUI();
    }


    private String sessionId;
    private Handler handler;
    private boolean timeRunning;
    private Runnable runnable;
    private int totalTime = 60;

    /**
     * 准备计时器
     */
    private void prepareTimer() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                timeRunning = true;
                totalTime--;
                mBtnCode.setText("重新发送（" + totalTime + "）");
                mBtnCode.setEnabled(false);
                mBtnCode.setBackgroundResource(R.drawable.shap_roundcorner_gray);
                handler.postDelayed(this, 1000);
                if (totalTime <= 0) {
                    totalTime = 120;
                    timeRunning = false;
                    mBtnCode.setText("获取验证码");
                    mBtnCode.setEnabled(true);
                    mBtnCode.setBackgroundResource(R.drawable.select_roundcorner_green);
                    handler.removeCallbacks(this);
                }
            }
        };
    }

    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.et_old_password)
    EditText mEtOldPassword;
    @BindView(R.id.et_new_password)
    EditText mEtNewPassword;
    @BindView(R.id.et_new_password2)
    EditText mEtNewPassword2;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.btn_code)
    Button mBtnCode;

    private void initUI() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mTvHeadTitle.setText("修改密码");

        mEtNewPassword.setHint(MyPassWordUtil.TIP);
    }

    @OnClick({R.id.head_left, R.id.btn_code, R.id.btn_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                ActivityManager.getInstance().closeActivity(context);
                break;
            case R.id.btn_code:
                getCode();
                break;
            case R.id.btn_commit:
                addData();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        mBtnCode.setClickable(false);
        mBtnCode.setBackgroundResource(R.drawable.shap_roundcorner_gray);
        getP().getCode(context);
    }

    /**
     * 修改密码提交
     */
    String newPassword;
    private void addData() {
        try {
            String oldPassword = mEtOldPassword.getText().toString().trim();
            if (MyStringUtil.isEmpty(oldPassword)) {
                ToastUtils.showCustomToast("旧密码不能为空！");
                return;
            }
            newPassword = mEtNewPassword.getText().toString().trim();
            MyPassWordUtil.verify(newPassword);

            String newPassword2 = mEtNewPassword2.getText().toString().trim();
            if (!MyStringUtil.eq(newPassword, newPassword2)) {
                ToastUtils.showCustomToast("新密码两次输入的不一样！");
                return;
            }
            if (oldPassword.equals(newPassword2)) {
                ToastUtils.showCustomToast("新密码不能跟旧密码一样！");
                return;
            }
            if ("123456".equals(newPassword2)) {
                ToastUtils.showCustomToast("新密码不能为初始密码123456");
                return;
            }
            String code = mEtCode.getText().toString().trim();
            if (MyStringUtil.isEmpty(code)) {
                ToastUtils.showCustomToast("验证码不能为空");
                return;
            }

            getP().addData(context, newPassword, oldPassword, code, sessionId);

        } catch (Exception e) {
            ToastUtils.showError(e);
        }

    }

    /**
     * 处理获取验证码
     */
    public void doGetCodeResult(boolean success, String sessionId) {
        if (success) {
            this.sessionId = sessionId;
            mBtnCode.setClickable(false);
            mBtnCode.setBackgroundResource(R.drawable.shap_roundcorner_gray);
            if (!timeRunning) {
                handler.postDelayed(runnable, 10);
            }
        } else {
            this.sessionId = null;
            mBtnCode.setClickable(true);
            mBtnCode.setBackgroundResource(R.drawable.select_roundcorner_green);
        }

    }

    /**
     * 处理修改密码成功
     */
    public void doUpdatePasswordSuccess() {
        SPUtils.setValues(ConstantUtils.Sp.PASSWORD, newPassword);
        ActivityManager.getInstance().closeActivity(context);
    }


}
