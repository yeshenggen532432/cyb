package com.qwb.view.base.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.ToastUtils;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.view.base.parsent.PXRegister;
import com.qwb.view.tab.ui.XTabActivity;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyTimeCount;
import com.chiyong.t3.R;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 注册页面
 */
public class XRegisterActivity extends XActivity<PXRegister> {
    private MyTimeCount time;
    public String sessionId;
    @Override
    public int getLayoutId() {
        return R.layout.x_activity_register;
    }

    @Override
    public PXRegister newP() {
        return new PXRegister();
    }

    public void initData(Bundle savedInstanceState) {
        initUI();
        initCode();
        onNoMoreClick();//防止多次点击
    }

    @BindView(R.id.tv_code)
    TextView mTvCode;
    public void initCode(){
        time = new MyTimeCount(60000, 1000,mTvCode);
        mTvCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendCode();
            }
        });
    }

    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_code)
    EditText mEtCode;
    private void initUI() {
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.dp_8),(int)getResources().getDimension(R.dimen.dp_14));
        mIvHeadLeft.setLayoutParams(params);
    }

    @BindView(R.id.tv_next)
    public View  mTvNext ;
    @BindView(R.id.btn_submit)
    public View  mBtnSubmit ;
    public void onNoMoreClick(){
        //注册
        mBtnSubmit.setOnClickListener(new OnNoMoreClickListener() {
            @Override
            protected void OnMoreClick(View view) {
                regist(1);
            }
        });
        //next:完善信息
        mTvNext.setOnClickListener(new OnNoMoreClickListener() {
            @Override
            protected void OnMoreClick(View view) {
                regist(2);
            }
        });
    }




    /**
     * 发送验证码
     */
    private void sendCode() {
        sessionId = null;
        String phone = mEtPhone.getText().toString().trim();
        if (MyStringUtil.isNotMobile(phone)) {
            ToastUtils.showCustomToast( "请填写11位手机号码");
            return;
        }
        time.start();
        getP().sendCode(context,phone);
    }

    private void regist(int type) {
        String phone = mEtPhone.getText().toString().trim();
        String code = mEtCode.getText().toString().trim();
        if (MyStringUtil.isNotMobile(phone)) {
            ToastUtils.showCustomToast( "请填写11位手机号码");
            return;
        }
        if (MyStringUtil.isEmpty(code)) {
            ToastUtils.showCustomToast("验证码不能为空");
            return;
        }

        // 如果验证码不是默认的 就要验证sessionId
        if (Constans.SYSCODE.equals(code)) {
            getP().submit(context,phone,code,null,type);
            return;
        }else{
            if(TextUtils.isEmpty(sessionId)){
                sessionId = null;
                ToastUtils.showCustomToast("验证码失效,请重新获取");
            }
        }
        getP().submit(context,phone,code,sessionId,type);

    }

    /**
     * 注册提交成功
     */
    public void submitSuccess(String phone){
        sessionId = null;
        Router.newIntent(context)
                .putString(ConstantUtils.Intent.PHONE,phone)
                .to(XForgetPwdNextActivity.class)
                .launch();
    }

    public void showDialog(){
        NormalDialog normalDialog = new NormalDialog(context);
        normalDialog
                .content("您的初始化密码是123456")
                .btnText("进入驰用宝","完善信息")
                .show();
        normalDialog.setOnBtnClickL(new OnBtnClickL() {
                                        @Override
                                        public void onBtnClick() {
                                            ActivityManager.getInstance().jumpMainActivity(context,XTabActivity.class);
                                        }
                                    }, new OnBtnClickL() {
                                        @Override
                                        public void onBtnClick() {
                                            ActivityManager.getInstance().jumpActivity(context,XRegisterNextActivity.class);
                                        }
                                    }
        );
    }

}
