package com.qwb.view.base.parsent;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.event.ChangeCompanyEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MD5;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.LoginBean;
import com.qwb.view.base.model.RoleBean;
import com.qwb.view.base.ui.RzMobileActivity;
import com.qwb.view.base.ui.XLoginActivity;
import com.qwb.view.tab.ui.XTabActivity;
import com.qwb.utils.MyStringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import cn.droidlover.xdroidmvp.router.Router;
import okhttp3.Call;

/**
 * 登录
 */
public class PXLogin extends XPresent<XLoginActivity> {

    private Activity activity;
    private String pwd;
    private boolean check;//是否记住
    private boolean isRz;//是否需要认证手机
    private int mType;
    private String oldPhone;//之前登录的账号
    private String newPhone;//现在登录的账号
    private String phone;

    /**
     * 登录
     */
    public void submit(Activity activity, String phone, String pwd, boolean check, int type) {
        this.activity = activity;
        this.pwd = pwd;
        this.check = check;
        this.mType = type;
        oldPhone = SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE);
        newPhone = phone;
        this.phone = phone;
        OkHttpUtils
                .post()
                .url(Constans.loginURL)
                .addParams("mobile", phone)
                .addParams("pwd", MD5.hex_md5(pwd))
                .addParams("unId", MyUtils.getImei(activity))
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson1(response);
                    }
                });
    }



    /**
     * 获取应用列表（角色）
     */
    public void loadDataRoleNew(Activity activity, String token) {
        OkHttpUtils
                .post()
                .addParams("token", token)
                .url(Constans.menus)
                .id(3)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson3(response);
                    }
                });
    }


    /**
     * 登录接口：独立版的
     */
    public void queryLogin2(Activity activity, String jwt) {
        OkHttpUtils
                .post()
                .addParams("token", SPUtils.getTK())
                .addParams("jwt", jwt)
                .url(Constans.loginURL2)
                .id(4)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson4(response);
                    }
                });
    }

    //TODO ------------------------接口回調----------------------
    //解析数据-登录
    private void parseJson1(String response) {
        try {
            LoginBean loginBean = JSON.parseObject(response, LoginBean.class);
            if (loginBean != null && loginBean.isState()) {

                //手机认证
                Integer rzState = loginBean.getRzState();
                if (rzState != null && rzState == 0) {
                    if (Constans.ISRZ) {
                        isRz = true;
                    }
                }

                String domain = loginBean.getDomain();
                if (MyStringUtil.isEmpty(domain)) {
                    loginBean.setMemberMobile(phone);
                    MyLoginUtil.login(loginBean, pwd, check);
                    MyLoginUtil.login(null, null);
                    //获取应用列表
                    loadDataRoleNew(activity, loginBean.getToken());
                } else {
                    MyLoginUtil.login(loginBean.getJwt(), loginBean.getDomain());
                    queryLogin2(null, loginBean.getJwt());
                }
            } else {
                ToastUtils.showLongCustomToast(loginBean.getMsg());
                getV().submitError();
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }


    //解析数据-应用列表
    private void parseJson3(String response) {
        try {
            RoleBean bean = JSON.parseObject(response, RoleBean.class);//角色
            if (bean != null || bean.isState()) {
                //处理应用列表
                MyLoginUtil.doApplyListNew(bean);
                //跳到首页或认证页面
                jumpActivity();
                BusProvider.getBus().post(new ChangeCompanyEvent());
            } else {
                ToastUtils.showLongCustomToast(bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }

    //解析数据
    private void parseJson4(String response) {
        try {
            LoginBean loginBean = JSON.parseObject(response, LoginBean.class);//角色
            if (loginBean != null || loginBean.isState()) {
                loginBean.setRzState(1);
                MyLoginUtil.login(loginBean, pwd, check);
//                SPUtils.setValues(ConstantUtils.Sp.COMPANY_S, JSON.toJSONString(loginBean.getCompanies()));//所属公司
                loadDataRoleNew(activity, loginBean.getToken());
            } else {
                ToastUtils.showLongCustomToast(loginBean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }


    /**
     * 跳到首页或认证页面
     */
    public void jumpActivity() {
        if (isRz) {
            //跳到认证页
            Router.newIntent(activity)
                    .to(RzMobileActivity.class)
                    .putString(ConstantUtils.Sp.USER_NAME, SPUtils.getSValues(ConstantUtils.Sp.USER_NAME))
                    .putString(ConstantUtils.Sp.USER_MOBILE, SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE))
                    .launch();
        } else {
            if (1 == mType && (!MyStringUtil.isEmpty(newPhone) && newPhone.equals(oldPhone))) {
                //异地登录
                ActivityManager.getInstance().closeActivity(activity);
            } else {
                //跳到首页
                ActivityManager.getInstance().jumpMainActivity(activity, XTabActivity.class);
            }
        }
    }


}



