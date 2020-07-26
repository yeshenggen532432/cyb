package com.qwb.view.base.parsent;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyStringUtil;
import com.qwb.event.ChangeCompanyEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.view.base.model.LoginBean;
import com.qwb.view.base.model.RoleBean;
import com.qwb.utils.Constans;
import com.qwb.utils.MD5;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.ui.XForgetPwdNextActivity;
import com.qwb.view.tab.ui.XTabActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 忘记密码
 */
public class PXForgetPwdNext extends XPresent<XForgetPwdNextActivity> {

    private Activity context;
    private String phone;
    private String pwd;
    private boolean check;//是否记住

    /**
     *
     */
    public void submit(Activity context, String phone, String pwd, String code, String sessionId) {
        this.pwd = pwd;
        this.context = context;
        this.phone = phone;
        OkHttpUtils
                .post()
                .url(Constans.changepwdToURL)
                .addParams("token", SPUtils.getTK())
                .addParams("mobile", phone)
                .addParams("newpwd", MD5.hex_md5(pwd))
//                .addParams("sendCode", code)
//                .addParams("sessionId", sessionId)
                .id(1)
                .build()
                .execute(new MyHttpCallback(context) {
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
     * 登录
     */
    public void login(Activity context, String phone, String pwd, boolean check) {
        this.context = context;
        this.pwd = pwd;
        this.check = check;

        OkHttpUtils
                .post()
                .url(Constans.loginURL)
                .addParams("mobile", phone)
                .addParams("pwd", MD5.hex_md5(pwd))
                .addParams("unId", MyUtils.getImei(context))
                .id(2)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson2(response);
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

    /**
     * 获取应用列表（角色）
     */
    public void loadDataRoleNew(Activity activity, String token) {
        OkHttpUtils
                .post()
                .addParams("token", token)
                .url(Constans.menus)
                .id(33)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson33(response);
                    }
                });
    }

    //TODO ------------------------接口回調----------------------
    //解析数据-登录
    private void parseJson1(String response) {
        try {
            LoginBean loginBean = JSON.parseObject(response, LoginBean.class);
            if (loginBean != null && loginBean.isState()) {
                login(context, phone, pwd, true);
            } else {
                ToastUtils.showLongCustomToast(loginBean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据-登录
    private void parseJson2(String response) {
        try {
            LoginBean loginBean = JSON.parseObject(response, LoginBean.class);
            if (loginBean != null && loginBean.isState()) {
                String domain = loginBean.getDomain();
                if (MyStringUtil.isEmpty(domain)) {
                    loginBean.setMemberMobile(phone);
                    MyLoginUtil.login(loginBean, pwd, check);
                    MyLoginUtil.login(null, null);
                    //获取应用列表
                    loadDataRoleNew(null, loginBean.getToken());
                } else {
                    MyLoginUtil.login(loginBean.getJwt(), loginBean.getDomain());
                    queryLogin2(null, loginBean.getJwt());
                }

            } else {
                ToastUtils.showLongCustomToast(loginBean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据
    private void parseJson4(String response) {
        try {
            LoginBean loginBean = JSON.parseObject(response, LoginBean.class);//角色
            if (loginBean != null || loginBean.isState()) {
                MyLoginUtil.login(loginBean, pwd, check);
                loadDataRoleNew(null, loginBean.getToken());
            } else {
                ToastUtils.showLongCustomToast(loginBean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }


    //解析数据-应用列表
    private void parseJson33(String response) {
        try {
            RoleBean bean = JSON.parseObject(response, RoleBean.class);//角色
            if (bean != null || bean.isState()) {
                MyLoginUtil.doApplyListNew(bean);
                BusProvider.getBus().post(new ChangeCompanyEvent());
                //跳到首页或认证页面
                ActivityManager.getInstance().jumpMainActivity(context, XTabActivity.class);
            } else {
                ToastUtils.showLongCustomToast(bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


}



