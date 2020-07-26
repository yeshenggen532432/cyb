package com.qwb.view.base.parsent;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.ui.XForgetPwdActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;


import java.util.HashMap;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 忘记密码
 */
public class PXForgetPwd extends XPresent<XForgetPwdActivity> {

    private String phone;
    /**
     * 下一步
     */
    public void submit(Activity activity, String phone, String code, String sessionId) {
        this.phone = phone;

        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("mobile", phone);
        params.put("sendCode", code);
        params.put("sessionId", sessionId);
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.checkCodeURL)
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
     * 发送验证码
     */
    public void sendCode(Activity activity, String phone) {
        OkHttpUtils
                .post()
                .addParams("mobile", phone)
                .addParams("type", "4")// 1注册 2修改手机号3修改密码4找回密码
                .url(Constans.getCodeURL)
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
    //解析数据-登录
    private void parseJson1(String response) {
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            if (jsonObject.getBoolean("state")) {
                getV().submitSuccess(phone);
            } else {
                ToastUtils.showCustomToast(jsonObject.getString("msg"));
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast( e.getMessage());
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


}



