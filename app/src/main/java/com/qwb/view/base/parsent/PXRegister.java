package com.qwb.view.base.parsent;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.view.base.ui.XRegisterNextActivity;
import com.qwb.event.ChangeCompanyEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.view.base.model.LoginBean;
import com.qwb.utils.Constans;
import com.qwb.utils.MD5;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.RoleBean;
import com.qwb.view.base.ui.XRegisterActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 注册
 */
public class PXRegister extends XPresent<XRegisterActivity> {

    private String phone;
    private Activity context;
    private int type;//1:注册;2:完善信息
    /**
     * 注册
     */
    public void submit(Activity context, String phone, String code, String sessionId,int type) {
        this.context = context;
        this.phone = phone;
        this.type = type;

        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("memberNm", phone);
        params.put("sendCode", code);
        params.put("pwd", MD5.hex_md5("123456"));
        params.put("sessionId", sessionId);

        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.regnewURL)
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
     * 发送验证码
     */
    public void sendCode(Activity context, String phone) {
        OkHttpUtils
                .post()
                .addParams("mobile", phone)
                .addParams("type", "1")// 1注册 2修改手机号3修改密码4找回密码
                .url(Constans.getCodeURL)
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
     * 登录
     */
    public void login(Activity context, String phone, String pwd, boolean check) {
        OkHttpUtils
                .post()
                .url(Constans.loginURL)
                .addParams("mobile", phone)
                .addParams("pwd", MD5.hex_md5(pwd))
                .addParams("unId", MyUtils.getImei(context))
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


    //TODO ------------------------接口回調----------------------
    public class MyStringCallback extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            switch (id) {
                case 1://注册
                    parseJson1(response);
                    break;
                case 2://验证码
                    parseJson2(response);
                    break;

            }
        }
    }

    //解析数据-注册
    private void parseJson1(String response) {
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            if (jsonObject.getBoolean("state")) {
                ToastUtils.showCustomToast("恭喜您，注册成功!");
                LoginBean loginBean = new LoginBean();
                loginBean.setMemId(Integer.valueOf(jsonObject.getString("memberId")));
                loginBean.setToken(jsonObject.getString("token"));
                loginBean.setMemberMobile(phone);
                loginBean.setMemberNm(phone);
                //登录后保存数据
                MyLoginUtil.login(loginBean,"123456",true);
                loadDataRoleNew(null, loginBean.getToken());
                if(type == 1){
                    //跳到首页
                    getV().showDialog();
                }else{
                    //完善信息
                    ActivityManager.getInstance().jumpActivity(context,XRegisterNextActivity.class);
                }

            } else {
                ToastUtils.showLongCustomToast(jsonObject.getString("msg"));
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }


    //解析数据-验证码
    private void parseJson2(String response) {
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            if (jsonObject.getBoolean("state")) {
                String sessionId = jsonObject.getString("sessionId");
                ToastUtils.showCustomToast( "获取成功,注意查收短信");
                getV().sessionId = sessionId;
            } else {
                ToastUtils.showLongCustomToast(jsonObject.getString("msg"));
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast( e.getMessage());
        }
    }

    //解析数据-应用列表
    private void parseJson3(String response) {
        try {
            RoleBean bean = JSON.parseObject(response, RoleBean.class);//角色
            if (bean != null || bean.isState()) {
                //处理应用列表
                MyLoginUtil.doApplyListNew(bean);
                BusProvider.getBus().post(new ChangeCompanyEvent());
            } else {
                ToastUtils.showCustomToast(bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

}



