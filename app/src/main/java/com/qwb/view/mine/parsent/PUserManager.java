package com.qwb.view.mine.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.event.ChangeCompanyEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.base.model.LoginBean;
import com.qwb.view.base.model.RoleBean;
import com.qwb.view.mine.ui.UserManagerActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.MD5;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.tab.ui.XTabActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 设置-账户管理
 */
public class PUserManager extends XPresent<UserManagerActivity>{

    private Activity activity;
    private String pwd;
    private boolean check;//是否记住
    private boolean isRz;//是否需要认证手机

    /**
     * 登录
     */
    public void submit(Activity activity, String phone, String pwd, boolean check) {
        this.activity = activity;
        this.pwd = pwd;
        this.check = check;

        OkHttpUtils
                .post()
                .url(Constans.loginURL)
                .addParams("mobile", phone)
                .addParams("pwd", MD5.hex_md5(pwd))
                .addParams("unId", MyUtils.getImei(activity))
                .id(1)
                .build()
                .execute(new MyStringCallback(), activity);
    }

    /**
     * 获取应用列表（角色）
     */
    public void loadDataRole(Activity activity, String token) {
        OkHttpUtils
                .post()
                .addParams("token", token)
                .url(Constans.menus)
                .id(2)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson2(response);
                    }
                });
    }

    //TODO ------------------------接口回調----------------------
    public class MyStringCallback extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            switch (id) {
                case 1://登录
                    parseJson1(response);
                    break;
                case 2://应用列表
                    parseJson2(response);
                    break;
            }
        }
    }

    //解析数据-登录
    private void parseJson1(String response) {
        try {
            LoginBean loginBean = JSON.parseObject(response, LoginBean.class);
            if (loginBean != null && loginBean.isState()) {
                //TODO 第一次初始密码123456可以登录，后面初始密码123456不可以登录
                SPUtils.setBoolean(ConstantUtils.Sp.INIT_PASSWORD, true);
                //登录后保存数据
                MyLoginUtil.login(loginBean, pwd,check);

                //手机认证
                Integer rzState = loginBean.getRzState();
                if (rzState != null && rzState == 0) {
                    isRz = true;
                }
                //获取应用列表
                loadDataRole(activity, loginBean.getToken());
            } else {
                ToastUtils.showLongCustomToast(loginBean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }


    //解析数据-应用列表
    private void parseJson2(String response) {
        try {
            RoleBean bean = JSON.parseObject(response, RoleBean.class);//角色
            if (bean != null || bean.isState()) {
                //处理应用列表
                MyLoginUtil.doApplyList(bean);
                //跳到首页或认证页面
                jumpActivity();
            } else {
                ToastUtils.showLongCustomToast(bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }


    /**
     * 跳到首页或认证页面
     */
    public void jumpActivity() {
        //跳到首页
        ActivityManager.getInstance().jumpMainActivity(activity, XTabActivity.class);
        BusProvider.getBus().post(new ChangeCompanyEvent());
    }




}
